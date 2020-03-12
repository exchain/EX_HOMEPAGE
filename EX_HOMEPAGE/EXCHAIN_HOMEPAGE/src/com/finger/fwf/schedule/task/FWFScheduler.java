package com.finger.fwf.schedule.task;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Timer;

import org.apache.log4j.Logger;

import com.finger.agent.config.FcAgentConfig;
import com.finger.agent.util.AgentUtil;
import com.finger.fwf.core.config.FWFApsConfig;
import com.finger.fwf.core.task.FwfTasksLoader;
import com.finger.protocol.fwtp.FwtpMessage;

public class FWFScheduler {
    private static Logger logger = Logger.getLogger(FWFScheduler.class);

    protected static boolean _isPossibility = true;

    protected static final LinkedHashMap<String, Timer>  _m_Scheduler    = new LinkedHashMap<String, Timer>();
    protected static final LinkedHashMap<String, Object> _m_ScheduleInfo = new LinkedHashMap<String, Object>();
    protected static final LinkedHashMap<String, String> _m_ScheduleStat = new LinkedHashMap<String, String>();
    
    public static final String _STAT_INITIAL_ = "INITIAL";
    public static final String _STAT_RUNNING_ = "RUNNING";
    public static final String _STAT_WAITING_ = "WAITING";
    
    public static String _dbDualSvrIp = "";
    public int delaySec = -30;
    

    static {
        if (FWFApsConfig._OS_NAME.toUpperCase().indexOf("WINDOW") > -1) {
            _isPossibility = false;
        }
    }

    /**
     * getScheduleCount()
     * @return
     */
    protected int getScheduleCount() {
        int iCount = 0;
        if (!_m_Scheduler.isEmpty()) {
            Set<String> keyset = _m_Scheduler.keySet();
            iCount = keyset.toArray().length;
        }
        return iCount;
    }

    /**
     * getScheduleList()
     * @return
     * @throws Exception 
     */
    protected String[] getScheduleList() throws Exception {
        String[] schList = null;

        Set<String> keyset = null;
        Object[] haskeys = null;

        if (!_m_ScheduleInfo.isEmpty()) {
            keyset = _m_ScheduleInfo.keySet();
            haskeys = keyset.toArray();
            for (int i = 0; i < haskeys.length; i++) {
                String tmpKey = (String) haskeys[i];
                Timer schedule = _m_Scheduler.get(tmpKey);
                if (schedule == null)
                    terminateSchedule(tmpKey);
            }   // for..

            keyset = _m_ScheduleInfo.keySet();
            haskeys = keyset.toArray();
            schList = new String[haskeys.length];
            for (int i = 0; i < haskeys.length; i++)
                schList[i] = (String) haskeys[i];
        }   // if..
        return schList;
    }

    /**
     * 대상 스케쥴의 존재를 확인한다.
     * @param scheduleId
     * @throws Exception
     */
    protected boolean isExistSchedule(String scheduleId) throws Exception {
        if (!_m_Scheduler.isEmpty()) {
            Timer schedule = _m_Scheduler.get( scheduleId );
            if (schedule != null)
                return true;
        }
        return false;
    }
    
    /**
     * DB에 등록된 스케쥴를 구동시킨다.
     * @throws Exception
     */    
    public void loadingScheduleTasks() throws Exception{
	    
	    FwtpMessage fwtp = new FwtpMessage();
	    
	    fwtp.setSysMgtNo(FWFApsConfig.SYS_ID);
	    fwtp.setUserId("_STARTUP_");
	    fwtp.setTaskId( FWFApsConfig.ScheduleList_TaskID );		// 스케줄TASK로딩
	    FwtpMessage response = FwfTasksLoader.loadTransTask(fwtp);
	    logger.debug(response);
	
	    ArrayList<LinkedHashMap<String, String>> scheduleList = response.getResponseData();
	    
	    String temp = "";
	    delaySec = -30;  //초기화
	
	    //스케쥴 정보조회는 JOB일련번호로 정렬이 되어 조회가 됨(순차적)
	    for (int i = 0; i < scheduleList.size(); i++) {
	        LinkedHashMap<String,String> hmap = scheduleList.get(i);
	        String job_seq_no = (String) hmap.get("JOB_SEQ_NO");
	        //String sys_id = (String) hmap.get("SYS_ID");
	        String coin_type = (String) hmap.get("COIN_TYPE");
	        
		    if( !temp.equals(coin_type) ){
		    	delaySec = delaySec + 30 ; //30초씩 추가
		    }
		    temp = coin_type;
	        
	        startingSchedule(job_seq_no, hmap);
	    }
	    debugScheduleInfo();
    }      
    
    
    /**
     * 대상 스케쥴을 시작한다.  이미 실행된 경우 종료 후 시작한다.
     * @param scheduleId
     * @throws Exception
     */
    protected void startingSchedule(String scheduleId, LinkedHashMap<String, String> scheduleInfo) throws Exception {

        //Real Server Check - Scheduler(스케쥴) 체크         
        if( !FWFApsConfig._IsRealServer ){
            throw new Exception("\n Schedule ["+ scheduleId +"] can not run. (No Real Server)"+
                                "\n 스케쥴["+ scheduleId +"]을 실행할 수 없습니다.(운영서버 아님)");
        }

        if (isExistSchedule(scheduleId))
            terminateSchedule( scheduleId );      // 이미 실행된 것은 종료하고 다시 시작한다.

        TaskExecutorNHI executer = new TaskExecutorNHI( scheduleId, (String) scheduleInfo.get("COIN_TYPE") );
        
        if ( executer.initSchedule( scheduleInfo ) ) {
        	
            scheduleInfo.put("", "INIT");
            Timer schedule = new Timer();
            schedule.scheduleAtFixedRate( executer, executer._BeginDate, executer._Period );

            _m_Scheduler.put( scheduleId, schedule );
            _m_ScheduleInfo.put( scheduleId, scheduleInfo );
            _m_ScheduleStat.put( scheduleId, "INITIAL" );

            logger.debug(">> Starting.... id:["+ scheduleId +"]/[Begin:"+ executer._BeginDate +" | Period:"+ executer._Period +" | Between:"+ executer._Between +"]");
        }
        else {
            executer.disabledSchedule();
            logger.debug("\n>>> Schedule id:["+ scheduleId +"] Failure to Start!!"
                        +"\n##################################################"
                        +"\n"+ scheduleInfo
                        +"\n##################################################");
            executer.cancel();
        }
    }
    
    
    /**
     * 대상 스케쥴을 종료한다.
     * @param scheduleId
     * @throws Exception
     */
    protected void terminateSchedule(String scheduleId) throws Exception {

        //Real Server Check - Scheduler(스케쥴) 체크         
        if( !FWFApsConfig._IsRealServer ){
            throw new Exception("\n Schedule ["+ scheduleId +"] can not run. (No Real Server)"+
                                "\n 스케쥴["+ scheduleId +"]을 실행할 수 없습니다.(운영서버 아님)");
        }

    	logger.debug("######################################################");
    	logger.debug("### terminateScheduleAll Start [스케쥴 종료 처리]");
    	logger.debug("######################################################");
        
        String _desc = "## Terminate.... id:["+ scheduleId +"]";
        logger.debug( _desc );
        
        if (!_m_Scheduler.isEmpty()) {
            Timer schedule = _m_Scheduler.get( scheduleId );
            if (schedule != null) {
                schedule.cancel();
                _m_Scheduler.remove( scheduleId );
                _m_ScheduleInfo.remove( scheduleId );

                _desc = "## Schedule `"+ scheduleId +"` is terminated.";
                logger.debug( _desc );
            }
            else
            {
                logger.debug( "No Data(Schedule ID:["+scheduleId  +"])" );            	
            }
        }

        
        logger.debug("######################################################");
        logger.debug("### terminateScheduleAll End [스케쥴 종료 처리]");
        logger.debug("######################################################");
        
    }

    
    /**
     * 전체 스케쥴을 종료한다.
     * @throws Exception
     */
    protected void terminateScheduleAll() throws Exception {
    	
        //Real Server Check - Scheduler(스케쥴) 체크         
        if( !FWFApsConfig._IsRealServer ){
            throw new Exception("\n Unable to delete Schedule [ALL]. (No Real Server)"+
                                "\n 스케쥴[ALL]을 삭제할 수 없습니다.(운영서버 아님)");
        }
    	
    	logger.debug("######################################################");
    	logger.debug("### terminateScheduleAll Start [스케쥴 전체 삭제 처리]");
    	logger.debug("######################################################");

        if (!_m_Scheduler.isEmpty()) {
        	
        	String [] scheduleKeyName = getScheduleList();
            
            for (int i = 0; i < scheduleKeyName.length; i++) {

                Timer schedule = _m_Scheduler.get(scheduleKeyName[i]);
                
                if (schedule != null) {
                    schedule.cancel();
                    _m_Scheduler.remove( scheduleKeyName[i] );
                    _m_ScheduleInfo.remove( scheduleKeyName[i] );

                    logger.debug("### Schedule ["+ scheduleKeyName[i] +"] is terminated.[삭제]");
                }

            }
            
            _m_Scheduler.clear();
            _m_ScheduleInfo.clear();
        }
        
        logger.debug("######################################################");
        logger.debug("### terminateScheduleAll End [스케쥴 전체 삭제완료]");
        logger.debug("######################################################");

    }    
    
    
    /**
     * 시스템관리번호에 해당하는 전체 스케쥴을 종료한다.
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    protected void terminateScheduleSMN(String sysMgtNo) throws Exception {
    	
        //Real Server Check - Scheduler(스케쥴) 체크         
        if( !FWFApsConfig._IsRealServer ){
            throw new Exception("\n Unable to delete Schedule [SysMgtNo="+ sysMgtNo +"]. (No Real Server)"+
                                "\n 스케쥴 [SysMgtNo="+ sysMgtNo +"]을 삭제할 수 없습니다.(운영서버 아님)");
        }
    	

        if (!_m_Scheduler.isEmpty()) {
            for (int ii = 0; ii < _m_ScheduleInfo.size(); ii++) {
                LinkedHashMap<String, String> scheduleInfo = (LinkedHashMap<String, String>) _m_ScheduleInfo.get(ii);
                String _sys_id = (String) scheduleInfo.get("SYS_ID");		//시스템ID
                String _job_seq_no = (String) scheduleInfo.get("JOB_SEQ_NO");		//JOB일련번호
                if (AgentUtil.nvl(_sys_id).equals(sysMgtNo)) {
                    Timer schedule = _m_Scheduler.get(ii);
                    if (schedule != null) {
                        schedule.cancel();
                        _m_Scheduler.remove( _job_seq_no );
                        _m_ScheduleInfo.remove( _job_seq_no );

                        logger.debug("## Schedule `"+ _job_seq_no +"` is terminated.");
                    }
                }
            }
        }
    }

    /**
     * 대상 스케쥴의 TASK를 직접 실행한다.
     * @param scheduleId
     * @param scheduleInfo
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    protected void immediatelyExecute(String scheduleId) throws Exception {
    	
        //Real Server Check - Scheduler(스케쥴) 체크         
        if( !FWFApsConfig._IsRealServer ){
            throw new Exception("\n Schedule ["+ scheduleId +"] can not run. (No Real Server)"+
                                "\n 스케쥴["+ scheduleId +"]을 실행할 수 없습니다.(운영서버 아님)");
        }
    	
        if (!isExistSchedule(scheduleId))
            throw new Exception("스케쥴["+ scheduleId +"]이 존재하지 않습니다.");
        
        if (isRunningSchedule(scheduleId))
            throw new Exception("Schedule ["+ scheduleId +"] is already running.");

        LinkedHashMap<String, String> scheduleInfo = (LinkedHashMap<String, String>) _m_ScheduleInfo.get(scheduleId);

        TaskExecutorNHIm executer = new TaskExecutorNHIm( scheduleId, scheduleInfo );
        executer.start();
    }

    protected void debugScheduleInfo() throws Exception {
        logger.debug(_m_Scheduler);
        logger.debug(_m_ScheduleInfo);
        String[] scheduleArr = getScheduleList();
        
        if( scheduleArr==null || scheduleArr.length==0 ){
        	logger.debug("Scheduler 대상이 없습니다.사용여부를 확인하세요.");
        }else{
	        for (int ii=0; ii<scheduleArr.length; ii++){
	            logger.debug("  --> "+ ii +" ["+ scheduleArr[ii] +"]");
	        }
        }        
    }
    
    /**
     * 대상 스케쥴의 상태를 (실행중) 변경한다.
     * @param scheduleId
     * @throws Exception
     */
    protected void setStateRunning(String scheduleId) throws Exception {
        if ( isExistSchedule(scheduleId) )
            _m_ScheduleStat.put( scheduleId, _STAT_RUNNING_ );
    }
    
    /**
     * 대상 스케쥴의 상태를 (대기중) 변경한다.
     * @param scheduleId
     * @throws Exception
     */
    protected void setStateWaiting(String scheduleId) throws Exception {
        if ( isExistSchedule(scheduleId) )
            _m_ScheduleStat.put( scheduleId, _STAT_WAITING_ );
    }
    
    /**
     * 대상 스케쥴의 실행중 여부를 확인한다.
     * @param scheduleId
     * @throws Exception
     */
    protected boolean isRunningSchedule(String scheduleId) throws Exception {
        if ( isExistSchedule(scheduleId) ) {
            String scheduleStat = _m_ScheduleStat.get( scheduleId );	
            logger.debug("//////////:: ===>> id ["+ scheduleId +"], state ["+ scheduleStat +"]");
            if (_STAT_RUNNING_.equals(scheduleStat))
                return true;
        }
        return false;
    }    
    
}