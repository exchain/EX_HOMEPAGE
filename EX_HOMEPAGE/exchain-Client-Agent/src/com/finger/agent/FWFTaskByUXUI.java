package com.finger.agent;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;

import com.finger.agent.client.EngineConnector;
import com.finger.agent.config.FcAgentConfig;
import com.finger.agent.exception.AgentException;
import com.finger.agent.msg.FixedMsg;
import com.finger.agent.msg.MsgObj;
import com.finger.agent.util.AgentUtil;
import com.finger.protocol.fwtp.FwtpHeader;
import com.finger.protocol.fwtp.FwtpMessage;

public class FWFTaskByUXUI {
    private static Logger logger = Logger.getLogger(FWFTaskByUXUI.class);

    public FWFTaskByUXUI() { }

    /**
     * 서버에서 받은 결과 문자열(String) 값을 맵리스트 형태로 반환한다.
     * @param fwtp
     * @return
     * @throws AgentException
     */
    public static FwtpMessage doFWFAgentListForUX(FwtpMessage fwtp) throws AgentException {
        return makeReceiveDataToFWTP( doFWFAgentForUX ( fwtp ) );
    }

    /**
     * 연결정보에 의하여 서버에 접속 및 요청내역을 전송하고 처리결과 값을 받는다.
     * @param fwtp
     * @return
     * @throws AgentException
     */
    public static String doFWFAgentForUX(FwtpMessage fwtp) throws AgentException {

        if (fwtp.toString().getBytes().length <= 1000){
            logger.debug("(Input)FwtpMessage (1,000 byte in) = ["+ fwtp.toString() +"]");
        }else{
        	logger.debug("(Input)FwtpMessage (1,000 byte over)= ["+ new String(fwtp.toString().getBytes(), 0, 900) +".....]");
        }

        //if (AgentUtil.isNull(fwtp.getSysMgtNo()))
        //    throw new AgentException("시스템관리번호가 누락되었습니다.", "시스템담당자에게 문의 바랍니다.");
        if (AgentUtil.isNull(fwtp.getTaskId()))
            throw new AgentException("TASK-ID가 누락되었습니다.", "시스템담당자에게 문의 바랍니다.");
        if (AgentUtil.isNull(fwtp.getRequestBy()))
            fwtp.setRequestBy(FwtpHeader._REQUEST_BY_UX);
        if (AgentUtil.isNull(fwtp.getClientIp()))
            fwtp.setClientIp("127.0.0.1");

        //if (AgentUtil.isNull(fwtp.getUserId()))
        //    throw new AgentException("USER-ID가 누락되었습니다.", "시스템담당자에게 문의 바랍니다.");

        if (!FcAgentConfig._PROPERTY_READ_SUCCESS)
            throw new AgentException(FixedMsg._MSG_SOKT_K9999, "시스템담당자에게 문의 바랍니다.");

        EngineConnector econn = new EngineConnector( FcAgentConfig._FWF_APP_SVR_HOST_IP_, FcAgentConfig._FWF_APP_SVR_PORT_NO_ );

    	// **************************************************
    	// AP 서버 이중화 여부 확인 2015-04-23 By hong
    	// **************************************************
    	/*if( FcAgentConfig._AP_SERVER_DUAL_USE ){
    		
        	logger.debug("*********************************************************************************************");
        	logger.debug("*** AP Server Dual Setting Infomation ");
        	logger.debug("*** [IP:PORT]=["+ FcAgentConfig._FWF_APP_SVR_HOST_IP_ +":" + FcAgentConfig._VRLY_TO_HERMES_PORT_ +"] ");
        	logger.debug("*** [IP:PORT]=["+ FcAgentConfig._VRLY_TO_HERMES_HOST_2 +":" + FcAgentConfig._VRLY_TO_HERMES_PORT_ +"] ");
        	logger.debug("*********************************************************************************************");

            econn = new EngineConnector( FcAgentConfig._FWF_APP_SVR_HOST_IP_, FcAgentConfig._FWF_APP_SVR_PORT_NO_ ,"_FWF_APP_SVR_HOST_IP_");        	
    		
    	}else{
            econn = new EngineConnector( FcAgentConfig._FWF_APP_SVR_HOST_IP_, FcAgentConfig._FWF_APP_SVR_PORT_NO_ );    		
    	}*/
    	
        return econn.communicate( fwtp );
    }


    /**
     * (서버에서 받은 결과) 문자열 값을 FwtpMessage 형태로 반환한다.
     * @param inRecvData
     * @return
     * @throws AgentException 
     */
    private static FwtpMessage makeReceiveDataToFWTP(String inRecvData) throws AgentException {

        FwtpMessage fwtpMessage = new FwtpMessage();

        if (AgentUtil.isNull(inRecvData)) {
            logger.debug("\n>>> inRecvData = ["+ inRecvData +"]");
            fwtpMessage.setMessageCode(FixedMsg._MSG_SOKT_K0000);
        }
        else
        {

            if (inRecvData.getBytes().length <= 1000){
                logger.debug("inRecvData (1,000 byte in) = ["+ inRecvData +"]");
            }else{
            	logger.debug("inRecvData (1,000 byte over)= ["+ new String(inRecvData.getBytes(), 0, 900) +".....]");
            }
        	
            LinkedHashMap<String, String> headerMap = new LinkedHashMap<String, String>();

            byte[] recvDataBuf = inRecvData.getBytes();
            byte[] headerBuf = new byte[FwtpHeader._HEADER_SIZE];
            System.arraycopy(recvDataBuf, 0, headerBuf, 0, FwtpHeader._HEADER_SIZE);
            headerMap = FwtpHeader.makeHeaderMap( new String(inRecvData.getBytes(), 0, FwtpHeader._HEADER_SIZE) );

            fwtpMessage.setHeader( headerMap );
            
            logger.debug(">>> __DATA_LEN: "+ fwtpMessage.getItemData(FwtpHeader.__DATA_LEN) +", __ROW_COUNT: "+ fwtpMessage.getItemData(FwtpHeader.__ROW_COUNT));

            byte[] dataBuf = new byte[recvDataBuf.length - FwtpHeader._HEADER_SIZE];
            System.arraycopy( recvDataBuf, FwtpHeader._HEADER_SIZE, dataBuf, 0, (recvDataBuf.length - FwtpHeader._HEADER_SIZE) );

            if (FwtpHeader._RESPONSE_OKAY_.equals(fwtpMessage.getResponseCode()))
            {
                if (AgentUtil.isNull(new String(dataBuf))) {
                    return fwtpMessage;
                }
                String[] rows = (new String(dataBuf)).split(FwtpHeader._ROW_DELIM);
                String row = "";

                String [] arrFieldsName = null;      // 필드 이름
                String [] arrFieldsType = null;      // 필드 타입
                String [] arrFieldsSize = null;      // 필드 사이즈

                // 첫번째 라인: 필드명 리스트
                row = rows[0].trim();   // 프로토콜상 레코드 구분자는 '\n'이나 '\r\n'를 보내는 경우를 위해 처리
                arrFieldsName = row.split(FwtpHeader._COL_DELIM);
                // 두번째 라인: 필드타입 리스트
                row = rows[1].trim();
                arrFieldsType = row.split(FwtpHeader._COL_DELIM);
                // 세번째 라인: 필드길이 리스트
                row = rows[2].trim();
                arrFieldsSize = row.split(FwtpHeader._COL_DELIM);

                fwtpMessage.setResponseTableInfo( arrFieldsName, arrFieldsType, arrFieldsSize );

                ArrayList<LinkedHashMap<String, String>> responseList = new ArrayList<LinkedHashMap<String, String>>();

                int nFieldCount = arrFieldsName.length;
                // 네번째 라인 이후는 데이터, 파싱해서 ArrayList(HashMap) 구조로 변환
                for (int i = 3; i < rows.length; i++) {
                	LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

                    // 필드의 값 배열
                    row = rows[i];
                    String[] arrValues = row.split(FwtpHeader._COL_DELIM);

                    for (int j=0; j<nFieldCount; j++) {
                        try {
                            map.put(arrFieldsName[j], arrValues[j]);
                        } catch(Exception e) {
                            map.put(arrFieldsName[j], "");
                        }
                    }
                    responseList.add( map );
                }
                fwtpMessage.setResponseResultList( responseList );
            }	//if (FwtpHeader._RESPONSE_OKAY_...
            else {
                throw new AgentException(new MsgObj(fwtpMessage.getMessageCode(), new String(dataBuf)), fwtpMessage.getResponseCode() );
            }
        }

        return fwtpMessage;
    }

    /**
     * Socket Shutdown Hook
     * @param socket
     */
    /*private static void initHook(EngineSocket socket) {
        SocketShutdownHook shutdown = new SocketShutdownHook(socket);
        Runtime.getRuntime().addShutdownHook(shutdown);
    }*/
}