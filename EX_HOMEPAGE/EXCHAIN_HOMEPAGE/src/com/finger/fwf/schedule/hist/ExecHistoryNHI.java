package com.finger.fwf.schedule.hist;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.finger.fwf.core.config.FWFApsConfig;
import com.finger.fwf.core.dbm.SqlMapConfig;
import com.ibatis.sqlmap.client.SqlMapClient;

public class ExecHistoryNHI {

    private static final String  _m_sqlTASK_ = FWFApsConfig.ScheduleList_TaskID.substring(0,6)
    											+"."+ FWFApsConfig.ScheduleList_TaskID;
    private static final String  _sys_id     = FWFApsConfig.SYS_ID;
    
    private String  _ScheduleId_ ;
    private String _CoinType_ ;
    
    private HashMap<String,String>  m_param ;

    public ExecHistoryNHI(String scheduleId) {
        _ScheduleId_ = scheduleId;
        m_param = new HashMap<String,String>();
        m_param.put("job_seq_no", _ScheduleId_);
        m_param.put("user_id"   , "SCHEDULER" );
        m_param.put("sys_id"    , _sys_id );
        
    }

    public ExecHistoryNHI(String scheduleId, String coinType) {
    	_CoinType_   = coinType;
        _ScheduleId_ = scheduleId;
        m_param = new HashMap<String,String>();
        m_param.put("job_seq_no", _ScheduleId_);
        m_param.put("coin_type" , _CoinType_);
        m_param.put("user_id"   , "SCHEDULER" );
        m_param.put("sys_id"    , _sys_id );
        
    }
    public void initializeSchedule(String nextDate) throws Exception {
        SqlMapClient sqlMap = getSqlMap();

        m_param.put("exec_st_cd"    , "00"      );	//COM003(실행상태)-00:대기
        m_param.put("next_exec_date", nextDate  );	//다음실행일시[YYYYMMDDHH24MISS]

        /* 스케쥴정보 - 수정(00:대기) */
        sqlMap.update(_m_sqlTASK_+"_1", m_param );
    }

    public void disabledSchedule() throws Exception {
    	SqlMapClient sqlMap = getSqlMap();

        m_param.put("exec_st_cd"    , "90"      );	//COM003(실행상태)-90:실행불가

        /* 스케쥴정보 - 수정(90:실행불가) */
        sqlMap.update(_m_sqlTASK_+"_1", m_param );
    }

    @SuppressWarnings("unchecked")
    public void registHistory() throws Exception {
    	SqlMapClient sqlMap = getSqlMap();

        /* 스케쥴실행이력 - 일자,일련번호 구하기 */
        LinkedHashMap<String,String> userMap = (LinkedHashMap<String, String>)sqlMap.queryForObject(_m_sqlTASK_+"_9", m_param );
        m_param.put("exec_dt"    , userMap.get("EXEC_DT") );
        m_param.put("exec_dt_seq", String.valueOf(userMap.get("EXEC_DT_SEQ")) );
        m_param.put("exec_svr_ip", InetAddress.getLocalHost().getHostAddress());

        m_param.put("exec_st_cd" , "10"         );	//COM003(실행상태):10:실행중 -> 이력 시 컬럼 = JOB_EXEC_ST (JOB실행상태)

        /* 스케쥴정보 - 수정(10:실행중) */
        sqlMap.update(_m_sqlTASK_+"_1", m_param );

        /* 스케쥴실행이력 - 등록 */
        sqlMap.insert(_m_sqlTASK_+"_2", m_param );
    }

    public void updateHistory(boolean isSuccess, String nextDate) throws Exception {
        SqlMapClient sqlMap = getSqlMap();

        m_param.put("next_exec_date", nextDate  );	//다음실행일시[YYYYMMDDHH24MISS]
        if (isSuccess)
            m_param.put("exec_st_cd", "20"      );	//COM003(실행상태):20:실행완료
        else
            m_param.put("exec_st_cd", "30"      );	//COM003(실행상태):30:실행오류

        /* 스케쥴정보 - 수정 */
        sqlMap.update(_m_sqlTASK_+"_1", m_param );

        /* 스케쥴실행이력 - 수정 */
        sqlMap.update(_m_sqlTASK_+"_3", m_param );
    }
    public void updateSkipHistory(String nextDate) throws Exception {
        SqlMapClient sqlMap = getSqlMap();

        m_param.put("next_exec_date", nextDate  );	//다음실행일시[YYYYMMDDHH24MISS]
        m_param.put("exec_st_cd"    , "25"      );	//COM003(실행상태):25:실행SKIP

        /* 스케쥴정보 - 수정 */
        sqlMap.update(_m_sqlTASK_+"_1", m_param );

        /* 스케쥴실행이력 - 수정 */
        sqlMap.update(_m_sqlTASK_+"_3", m_param );
    }

    private static synchronized SqlMapClient getSqlMap() {
        return SqlMapConfig.getInstance();
    }
}