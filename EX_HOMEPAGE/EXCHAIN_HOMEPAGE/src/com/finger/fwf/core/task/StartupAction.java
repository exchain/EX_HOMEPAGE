package com.finger.fwf.core.task;


import org.apache.log4j.Logger;

import com.finger.fwf.core.base.BizModuleClass;
import com.finger.fwf.core.config.FWFApsConfig;
import com.finger.fwf.schedule.ScheduleAgent;
import com.finger.protocol.fwtp.FwtpMessage;

/**
 * ApServer 시작 시 처리해야할 작업들을 실행한다.
 * 
 * @author Finger21
 */
public class StartupAction extends BizModuleClass  {
    private static Logger logger = Logger.getLogger(StartupAction.class);

    public StartupAction() { }

    public void init() {
        logger.debug("Startup.......");
    }

    @Override
    public FwtpMessage execute(String action, FwtpMessage in) throws Exception {
        /* 배치모듈 실행 - 파일읽어 APS에 전송하기 */
        try {
        	/* Finger-WebFrame 스케쥴 로딩 */
            //loadingScheduleTasks();
        }
        catch(Exception ex) { logger.error(ex.getMessage(), ex); }

        return null;
    }

    /**
     * DB에 등록된 스케쥴정보를 읽어 구동시킨다.
     * @throws Exception
     */
    private void loadingScheduleTasks() throws Exception {

        ScheduleAgent scheduleAgent = new ScheduleAgent();

        //Real Server Check - Scheduler(스케쥴) 체크          
        if( !FWFApsConfig._IsRealServer ){
            throw new Exception("\n Unable to start the server startup Scheduler.(No Real Server)"+
                                "\n 서버 시작 시 Scheduler를 가동할 수 없습니다.(운영서버 아님)");
        }
        
        scheduleAgent.loadingScheduleTasks();

    }
}