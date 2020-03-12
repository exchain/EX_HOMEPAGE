package com.finger.fwf.core.task;

import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import com.finger.agent.config.FcAgentConfig;
import com.finger.agent.msg.FixedMsg;
import com.finger.fwf.core.config.FWFApsConfig;
import com.finger.fwf.core.rpc.DefaultBizService;
import com.finger.protocol.fwtp.FwtpMessage;
import com.ibatis.common.resources.Resources;

/**
 * Tasks.xml 파일에서 Task 정보를 얻는다.
 */
public class FwfTasksLoader {
    private static Logger logger = Logger.getLogger(FwfTasksLoader.class);

    public static final String  _K_FILE         =   "file";
    public static final String  _K_DESC         =   "desc";
    public static final String  _K_TID          =   "tid";
    public static final String  _K_CLASS        =   "class";
    public static final String  _K_ACTION       =   "action";

    private static FwfTasksLoader _TILoader = new FwfTasksLoader();
    public Hashtable<String, Object> _transMap = null;

    public FwfTasksLoader() { }

    public static void reloadTransMap() {
        _TILoader.readTransMap();
    }

    /**
     * Tasks 그룹별 파일 목록 정보를 얻는다.
     * @param  None
     * @return None
     */
    private void readTransMap() {
        _transMap = new Hashtable<String, Object>();

        try {
            Charset charset = Charset.forName(FcAgentConfig._CharSet_);
            Resources.setCharset(charset);
            Reader reader = Resources.getResourceAsReader( FWFApsConfig.TASKS_XML_FILE );
            Document doc = new SAXBuilder().build( reader );

            Element root  = doc.getRootElement();

            // 트랜잭션 리스트를 구해 자료구조 만들기
            List<Element> transList = root.getChildren();

            for (Iterator<Element> i = transList.iterator();i.hasNext();) {
                Element item = i.next();

                if ( readTasksGroup( item.getAttribute(_K_FILE).getValue() ) ) {
                    logger.debug(">> TasksGroup ["+ item.getAttribute(_K_FILE).getValue() +"|"+ item.getAttribute(_K_DESC).getValue() +"] Read OK!");
                }
            }
        }
        catch (Exception ex) {
            logger.error(ex.toString(), ex);
        }
    }
    /**
     * 그룹별(Tasks.xml) 에서 Task 목록 정보를 얻는다.
     * @param  tasks_xml_file
     * @return None
     */
    private boolean readTasksGroup(String tasks_xml_file) {
        boolean boolRet = false;
        try {
            Charset charset = Charset.forName(FcAgentConfig._CharSet_);
            Resources.setCharset(charset);
            Reader reader = Resources.getResourceAsReader( tasks_xml_file );
            Document doc = new SAXBuilder().build( reader );

            Element root  = doc.getRootElement();

            // 트랜잭션 리스트를 구해 자료구조 만들기
            List<Element> transList = root.getChildren();

            for (Iterator<Element> i = transList.iterator();i.hasNext();) {
                Element item = i.next();

                Hashtable<String, String> tranInfo = new Hashtable<String, String>();
                List<Attribute> tran = item.getAttributes();
                for (Iterator<Attribute> j = tran.iterator(); j.hasNext();) {
                    Attribute attr = j.next();
                    tranInfo.put(attr.getName(), attr.getValue());
                }

                _transMap.put(item.getAttribute(_K_TID).getValue(), tranInfo);
            }
            boolRet = true;
        }
        catch (Exception ex) {
            logger.error(ex.toString(), ex);
        }
        return boolRet;
    }

    public static void loadTransMap() {
        _TILoader.readTransMap();
    }

    /**
     * TaskId로 tasks.xml에 정의된 다른 속성정보를 얻는다
     * @param tranId TaskId
     * @param key 알고 싶은 attribute key 값
     * @return key value 값
     */
    @SuppressWarnings("unchecked")
    public static String getTransInfo(String tranId, String key) {
        Hashtable<String, Object> tranInfo = (Hashtable<String, Object>)_TILoader._transMap.get(tranId);
        if (tranInfo == null) return "";

        return (String)tranInfo.get(key);
    }

    /**
     * 서버 Monitor 용
     */
    @SuppressWarnings("unchecked")
    public static String getTranDesc(String tranId) {
        String ret = "";

        // tranId 가 없는 경우는 리스트 리턴
        if (tranId.equals("")) {
            for (Enumeration<String> e = _TILoader._transMap.keys(); e.hasMoreElements() ;) {
                String key = e.nextElement();
                Hashtable<String, Object> tranInfo = (Hashtable<String, Object>)_TILoader._transMap.get(key);
                ret += key + "\t\t" + tranInfo.get(_K_DESC) + "\r\n";
            }
        }
        else {
            Hashtable<String, Object> tranInfo = (Hashtable<String, Object>)_TILoader._transMap.get(tranId);
            if (tranInfo == null)return "";

            for (Enumeration<String> e = tranInfo.keys(); e.hasMoreElements() ;) {
                String key = e.nextElement();
                ret += key + "\t\t" + tranInfo.get(key) + "\r\n";
            }
        }

        return ret;
    }

    /**
     * 트랜잭션 Task 를 실행한다.
     * @param request 입력 FwtpMessage
     * @return 결과 FwtpMessage
     */
    public static FwtpMessage loadTransTask(FwtpMessage request) {
        String strTaskId = request.getTaskId();
        String className = FwfTasksLoader.getTransInfo( strTaskId, _K_CLASS );

        if (className == "") {
            logger.error(FixedMsg.getMessage(FixedMsg._MSG_TASK_T0003) +" - Task ID ["+ strTaskId +"]");
            return null;
        }

        String action = FwfTasksLoader.getTransInfo( strTaskId, _K_ACTION );
        request.setBizTaskClass( className );
        request.setBizTaskAction( action );
        String actionDesc = FwfTasksLoader.getTransInfo( strTaskId, _K_DESC );        

        //RunningTime runtime = new RunningTime( strTaskId );
        //runtime.start();
        
        FwtpMessage response = null;

        DefaultBizService dfBizServie = new DefaultBizService();
        dfBizServie.setTaskDesc(actionDesc);
        
        response = dfBizServie.callService( request );

        // 처리소요시간 측정
        //runtime.stop();
        //if (logger.isInfoEnabled()) {
        //    logger.info(runtime.toPrinting());
        //}
        
        return response;
    }
}