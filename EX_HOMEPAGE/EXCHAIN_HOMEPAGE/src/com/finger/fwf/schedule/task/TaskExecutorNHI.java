package com.finger.fwf.schedule.task;

import static com.finger.tools.util.lang.StrUtil.nvl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.finger.agent.util.AgentUtil;
import com.finger.fwf.core.task.FwfTasksLoader;
import com.finger.fwf.schedule.ScheduleAgent;
import com.finger.fwf.schedule.hist.ExecHistoryNHI;
import com.finger.fwf.schedule.util.Calculator;
import com.finger.protocol.fwtp.FwtpMessage;
import com.finger.tools.util.lang.StrUtil;

public class TaskExecutorNHI extends TimerTask {
    private static Logger logger = Logger.getLogger(TaskExecutorNHI.class);

    private boolean _m_bRun     = false;

    private String _ScheduleId_ ;
    private String _CoinType_ ;
    
    private String _sys_mgt_no  = "";
    private String _sys_id      = "";
    private String _task_id     = "";
    private String _task_nm     = "";
    private String _param       = "";
    private String _exec_fo_cd  = "";
    private int    _act_str_dtm = 0;
    private int    _act_end_dtm = 2400;
    private String _exec_re_dtm = "";
    private String _exec_dl_dtm = "";
    private String _sche_time   = "";

    public Date _BeginDate ;
    public long _Period ;
    public String _Between ;

    private ExecHistoryNHI _history = null;

    public TaskExecutorNHI(String scheduleId) {
        _ScheduleId_ = scheduleId;
        _history = new ExecHistoryNHI( _ScheduleId_ );
    }

    public TaskExecutorNHI(String scheduleId, String coinType) {
        _ScheduleId_ = scheduleId;
        _CoinType_   = coinType;
        _history = new ExecHistoryNHI( _ScheduleId_ , _CoinType_);
    }
    
    public boolean initSchedule(LinkedHashMap<String, String> scheduleInfo) {
    	    	 
        _sys_mgt_no  = (String) scheduleInfo.get("SYS_MGT_NO");		//시스템관리번호
        //_sys_id      = (String) scheduleInfo.get("SYS_ID");		//시스템ID
        _sys_id      = (String) scheduleInfo.get("COIN_TYPE");		//시스템ID
        _task_id     = (String) scheduleInfo.get("TASK_ID");		//TASK-ID
        _task_nm     = (String) scheduleInfo.get("TASK_NM");		//TASK-NM
        _param       = (String) scheduleInfo.get("PARAM");			//파라미터
        //_exec_st_cd  = (String) scheduleInfo.get("EXEC_ST_CD");	//실행상태코드
        _exec_fo_cd  = (String) scheduleInfo.get("EXEC_FO_CD");		//실행방식코드

        _act_str_dtm = Integer.parseInt(nvl(scheduleInfo.get("ACT_STR_DTM"),"0000"));	//가동시작시간
        _act_end_dtm = Integer.parseInt(nvl(scheduleInfo.get("ACT_END_DTM"),"2400"));	//가동종료시간
        _Between = _act_str_dtm +" ~ "+ _act_end_dtm;

        _exec_re_dtm = (String) scheduleInfo.get("EXEC_RE_DTM");	//실행반복간격(분)
        _exec_dl_dtm = (String) scheduleInfo.get("EXEC_DL_DTM");	//실행지연시간(분)
        _sche_time   = (String) scheduleInfo.get("SCHE_TIME");	    //일정시각(DDHHMI)

//        if (AgentUtil.isNull(_sys_id))		    //시스템ID
//            return false;
        if (AgentUtil.isNull(_task_id))			//TASK-ID
            return false;
        if (AgentUtil.isNull(_exec_fo_cd))		//실행방식
            return false;
        if (AgentUtil.isNull(_param)) { }

        /* 반복실행 시 시작시간의 지연이 필요할때 사용 */
        _exec_dl_dtm = StrUtil.nvl(_exec_dl_dtm, "0");	//실행지연시간(분)

        if ("10".equals(_exec_fo_cd))			//10:반복실행
            if (AgentUtil.isNull(_exec_re_dtm))	//실행반복간격
                return false;
        if ("20".equals(_exec_fo_cd))			//20:일정시각
            if (AgentUtil.isNull(_sche_time))	//일정시각
                return false;

        /*  TASK 실행 클래스 확인  */
        String className = FwfTasksLoader.getTransInfo(_task_id, "class");
        if (AgentUtil.isNull(className))
            return false;
        try {
            Class.forName( className );
        } catch(ClassNotFoundException cnfe) {
            logger.error(cnfe.getMessage(), cnfe);
            return false;
        }

        try {
            if ("10".equals(_exec_fo_cd)) {		//10:반복실행 <- 20130205_실행지연시간(분)추가
                _BeginDate = Calculator.executeDate(_exec_re_dtm, Integer.parseInt(_exec_dl_dtm));
                _Period = Long.parseLong(_exec_re_dtm)*60*1000;
            } else
            //--if ("20".equals(_exec_fo_cd) && "00".equals(_sche_time.substring(0,2))) {	//20:일정시각, 00xxxx:매일
            if ("20".equals(_exec_fo_cd)) {		//20:일정시각 <-- 매일지정시간에 실행... ddHHmm
                _BeginDate = Calculator.nextRunTimeByDay(_sche_time);
                _Period = 24*60*60*1000;		//1일
            }
            else
                return false;

            ExecHistoryNHI history = new ExecHistoryNHI( _ScheduleId_ , _CoinType_);
            history.initializeSchedule( (new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(_BeginDate)) );

        } catch(Exception ex) {
            logger.error(ex.getMessage(), ex);
            return false;
        }

        return true;
    }

    public void disabledSchedule() {
        try {
            _history.disabledSchedule();
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    private String getNextDate() {
        Date nextDate = null;

        if (!AgentUtil.isNull(_CoinType_)) {		// 코인유형
            try {
                if ("10".equals(_exec_fo_cd)) {		//10:반복실행
                    nextDate = Calculator.executeDate(_exec_re_dtm);
                } else
                if ("20".equals(_exec_fo_cd)) {		//20:일정시각 <-- 매일지정시간에 실행... ddHHmm
                    nextDate = Calculator.nextRunTimeByDay(_sche_time);
                }
            } catch(Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }

        return (new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(nextDate));
    }

    public void run() {
        if (_m_bRun) {
            logger.info("["+_ScheduleId_+"] Batch job is running(1). After completion, please let run.");
            return;
        }

        _m_bRun = true;
        try {
            logger.info("### ["+_ScheduleId_+"] START :: "+ (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()) );

            // 배치실행
            executeBatch();

            logger.info("### ["+_ScheduleId_+"] E_N_D :: "+ (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()) );
        }
        catch (Exception ex) {
            try {
                _history.updateHistory( false, getNextDate() );
            } catch (Exception e) { e.printStackTrace(); }
            logger.error(ex.getMessage(), ex);
        }
        finally {
            try {
                ScheduleAgent scheduleAgent = new ScheduleAgent();
                scheduleAgent.setStateWaiting(_ScheduleId_);
            } catch (Exception ex) {
                logger.error(ex);
            }
        }
        _m_bRun = false;
    }

    private void executeBatch() throws Exception {
        SimpleDateFormat dateFormatHM = new SimpleDateFormat("HHmm");
        int iNowHHmm = Integer.parseInt(dateFormatHM.format(new Date()));

        logger.debug("\n ~| _sys_mgt_no  : "+ _sys_mgt_no
        		    +"\n ~| _sys_id      : "+ _sys_id
                    +"\n ~| _task_id     : "+ _task_id
                    +"\n ~| _param       : "+ _param
                    +"\n ~| _exec_fo_cd  : "+ _exec_fo_cd
                    +"\n ~| _act_str_dtm : "+ _act_str_dtm
                    +"\n ~| _act_end_dtm : "+ _act_end_dtm +"  / iNowHHmm: "+ iNowHHmm
                    +"\n ~| _exec_re_dtm : "+ _exec_re_dtm
                    +"\n ~| _sche_time   : "+ _sche_time );
        
        ScheduleAgent scheduleAgent = new ScheduleAgent();
        
        logger.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        logger.debug("+  Scheduler 이중화 체크하여 실행여부 확인            +");
        logger.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        logger.debug("+ 시스템관리번호 [sys_mgt_no ]=["+ _sys_mgt_no  + "]");
        logger.debug("+ 시스템ID    [sys_id    ]=["+ _sys_id      + "]");
        logger.debug("+ JOB일련번호  [job_seq_no ]=["+ _ScheduleId_  + "]");
        logger.debug("+ 타스크ID   [task_id    ]=["+ _task_id      + "]");
        logger.debug("+ 타스크명        [task_nm    ]=["+ _task_nm     + "]");
        logger.debug("+ 파라미터        [param      ]=["+ _param       + "]");
        logger.debug("+ 실행방식코드   [exec_fo_cd ]=["+ _exec_fo_cd  + "]");
        logger.debug("+ 실행반복시간   [exec_re_dtm]=["+ _exec_re_dtm + "]");
        logger.debug("+ 일정시각       [sche_time  ]=["+ _sche_time + "]");
        logger.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        
        //스케쥴이력데이터 생성        
        _history.registHistory();

        //파라미터 값이 WORKING_DAY=20150515 이고 입력한 날짜와 오늘날짜가 다르면 PASS
        // => 같은날인 경우만 실행
        String[] temps = _param.split("=");
        if (temps.length > 1) {
            if ("WORKING_DAY".equalsIgnoreCase(temps[0])) {
                String today = (new SimpleDateFormat("dd", Locale.KOREA).format(new Date()));
                if (!today.equals(temps[1])) {
                    _history.updateSkipHistory( getNextDate() );
                    logger.debug("*** Schedule ["+_ScheduleId_+"] SKIP!...{ param ["+ temps[1] +"], today ["+ today +"] }");
                    return;
                }
            }
        }
        
        /* 실행가능시간 체크 */
        boolean isExecute = false;
        if (_act_str_dtm < _act_end_dtm) {
            if (_act_str_dtm <= iNowHHmm && iNowHHmm <= _act_end_dtm)
                isExecute = true;
        }
        else {
            if (_act_str_dtm <= iNowHHmm || iNowHHmm <= _act_end_dtm)
                isExecute = true;
        }
        if (!isExecute) {
            _history.updateSkipHistory( getNextDate() );		// 실행가능시간
            logger.debug("*** Schedule ["+_ScheduleId_+"] SKIP!...{ Run-time:["+ _act_str_dtm +"]~["+ _act_end_dtm +"], NOW HHmm:["+ iNowHHmm +"] }");
            return;
        }

        /* 전역변수의 상태정보 체크 - 실행중이면 PASS */
        if ( scheduleAgent.isRunningSchedule(_ScheduleId_) ) {
            _history.updateSkipHistory( getNextDate() );
            logger.info("["+_ScheduleId_+"] Batch job is running(2). After completion, please let run.");
            return;
        }
        scheduleAgent.setStateRunning(_ScheduleId_);

        /*===============================================================*/
        FwtpMessage fwtp = new FwtpMessage();
        fwtp.setSysMgtNo( _sys_mgt_no );
        fwtp.setSysId( _sys_id );
        fwtp.setTaskId( _task_id );
        fwtp.setUserId("SCHEDULER");
        FwtpMessage response = FwfTasksLoader.loadTransTask(fwtp);
        /*logger.debug(response);*/
        /*===============================================================*/

      //스케쥴이력데이터 실행 후 업데이트   
        _history.updateHistory( response.isSuccessMessage(), getNextDate() );
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void main(String args[]) {
        /*SimpleDateFormat dateFormatYMD = new SimpleDateFormat("yyyyMMdd");*/
        SimpleDateFormat dateFormatHM = new SimpleDateFormat("HHmm");
        System.out.println("==>  .toPattern : "+ dateFormatHM.toPattern() );
        Date dateToday = new Date();;
        int nowHHmm = Integer.parseInt(dateFormatHM.format(dateToday));
        System.out.println("==>  .parseInt(`0000`) : "+ Integer.parseInt("0000") );
        System.out.println("==>  .parseInt(`2400`) : "+ Integer.parseInt("2400") );
        System.out.println("==>  nowHHmm : "+ nowHHmm );
    }
}