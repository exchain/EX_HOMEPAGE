package com.finger.fwf.schedule.task;

import java.util.LinkedHashMap;

import org.apache.log4j.Logger;

import com.finger.fwf.core.task.FwfTasksLoader;
import com.finger.fwf.schedule.ScheduleAgent;
import com.finger.fwf.schedule.hist.ExecHistoryNHI;
import com.finger.protocol.fwtp.FwtpMessage;


public class TaskExecutorNHIm extends Thread {
    private Logger logger = Logger.getLogger(TaskExecutorNHIm.class);

    private String                        _ScheduleId_ ;
    private LinkedHashMap<String, String> _scheduleInfo ;

    private ExecHistoryNHI _history = null;

    public TaskExecutorNHIm(String scheduleId, LinkedHashMap<String, String> scheduleInfo) {
        this._ScheduleId_  = scheduleId;
        this._scheduleInfo = scheduleInfo;
        _history = new ExecHistoryNHI( _ScheduleId_ );
    }

    public void run() {
logger.debug("\n//x///x///x///x///x///x///x///x///x///x///x///x///x///x///x///x///x///"
            +"\n   _ScheduleId_  => "+ _ScheduleId_
            +"\n   _scheduleInfo => "+ _scheduleInfo
            );

        /* 전역변수의 상태정보 체크 - 실행중이면 PASS */
        ScheduleAgent scheduleAgent = new ScheduleAgent();
        try {
            _history.registHistory();

            String _sys_mgt_no = (String) _scheduleInfo.get("SYS_MGT_NO");	//시스템관리번호
            String _task_id    = (String) _scheduleInfo.get("TASK_ID");		//TASK-ID

            if ( scheduleAgent.isRunningSchedule(_ScheduleId_) ) {
                _history.updateSkipHistory( null );
                logger.info("::Manual::["+_ScheduleId_+"] Batch job is running. After completion, please let run.");
                return;
            }
            scheduleAgent.setStateRunning(_ScheduleId_);

            /*===============================================================*/
            FwtpMessage fwtp = new FwtpMessage();
            fwtp.setSysMgtNo( _sys_mgt_no );
            fwtp.setTaskId( _task_id );
            fwtp.setUserId("SCHEDULER");
            FwtpMessage response = FwfTasksLoader.loadTransTask(fwtp);
            /*===============================================================*/

            _history.updateHistory( response.isSuccessMessage(), null );
        } catch (Exception ex) {
            logger.error(ex);
        } finally {
            try {
                scheduleAgent.setStateWaiting(_ScheduleId_);
            } catch (Exception ex) {
                logger.error(ex);
            }
        }
    }
}