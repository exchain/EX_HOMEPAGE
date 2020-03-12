package com.finger.agent.msg;

import java.util.Hashtable;

import com.finger.agent.util.AgentUtil;

public class FixedMsg {

    private static Hashtable<String, String>	_MessageInfo	=	new Hashtable<String, String>();

    public static final int		_MSG_CODE_SIZE_		=	5 ;

    /** 정상처리되었습니다. */
    public static final String	_MSG_SUCCESS		=	"00000";

    /** 정상적으로 조회가 완료되었습니다. */
    public static final String	_MSG_CODE_00001		=	"00001";
    /** 조회 시 오류가 발생하였습니다. */
    public static final String	_MSG_CODE_90001		=	"90001";
    /** 조회된 데이터가 없습니다. */
    public static final String	_MSG_CODE_90002		=	"90002";


    /* ***  TASK 처리 관련  *** */
    /** Task 처리 중 오류가 발생하였습니다. */
    public static final String	_MSG_TASK_T9999		=	"T9999";
    /** Task 처리 중 오류가 발생하였습니다.(messageReceived()) */
    public static final String	_MSG_TASK_T9998		=	"T9998";
    /** Task 처리 중 오류가 발생하였습니다.(sendErrorMessage()) */
    public static final String	_MSG_TASK_T9997		=	"T9997";
    /** 동적 Task 생성과정에서 오류가 발생하였습니다. */
    public static final String	_MSG_TASK_T9996		=	"T9996";

    /** 서비스 호출을 위한 Task-ID 값이 없습니다. */
    public static final String	_MSG_TASK_T0000		=	"T0000";
    /** ~Tasks.xml에 설정된 action 값이 Task(Java)에 구현되어 있지 않습니다. */
    public static final String	_MSG_TASK_T0001		=	"T0001";
    /** 입력받은 Parameter 값이 1건(row)도 존재하지 않습니다. */
    public static final String	_MSG_TASK_T0002		=	"T0002";
    /** 정의되지 않은 Task 정보를 호출하였습니다. */
    public static final String	_MSG_TASK_T0003		=	"T0003";
    /** Timeout 발생 !! */
    public static final String	_MSG_TASK_T0004		=	"T0004";
    /** FWTP로 받은 Input Parameter 에서 해당 정보의 데이터를 얻을 수 없습니다. */
    public static final String	_MSG_TASK_T0005		=	"T0005";

    /*  TASK 처리 중 업무관련 정의 메시지 추가부분  */
    /** 결재선 목록에 포함된 사용자입니다. */
    public static final String	_MSG_TASK_T1001		=	"T1001";

    /** 잘못된 Parameter 값이 입력되었습니다. */
    public static final String	_MSG_TASK_P0001		=	"P0001";


    /* ***  iBatis 처리 관련  *** */
    /** SqlMap 처리 중 오류가 발생하였습니다. */
    public static final String	_MSG_SQLM_Q9999		=	"Q9999";

    /** Key 생성에 실패하였습니다. */
    public static final String	_MSG_SQLM_Q0001		=	"Q0001";
    /** Query 실행 중 오류가 발생하였습니다. */
    public static final String	_MSG_SQLM_Q0002		=	"Q0002";
    /** (Broadcasting) 조건을 만족하는 데이터가 존재하지 않습니다. */
    public static final String	_MSG_SQLM_Q0003		=	"Q0003";
    /** (Broadcasting : Set) 기존 결과와 같습니다. */
    public static final String	_MSG_SQLM_Q0004		=	"Q0004";


    /* ***  Engine Socket 오류 정의  *** */
    /** ClientEngine 작업 중 예기치 않은 오류가 발생하였습니다. */
    public static final String	_MSG_SOKT_K0000		=	"K0000";
    /** 소켓 연결이 되지 않았습니다. */
    public static final String	_MSG_SOKT_K0001		=	"K0001";
    /** 소켓 종료시 에러가 발생하였습니다. */
    public static final String	_MSG_SOKT_K0002		=	"K0002";
    /** 메세지 전송중 에러가 발생 하였습니다. */
    public static final String	_MSG_SOKT_K0003		=	"K0003";
    /** 메세지 수신중 에러가 발생 하였습니다. */
    public static final String	_MSG_SOKT_K0004		=	"K0004";	//xx
    /** 전송 메세지 생성에 실패 하였습니다. */
    public static final String	_MSG_SOKT_K0005		=	"K0005";	//xx
    /** 수신 메세지 생성에 실패 하였습니다. */
    public static final String	_MSG_SOKT_K0006		=	"K0006";
    /** FWTP 프로토콜 오류 입니다. */
    public static final String	_MSG_SOKT_K0007		=	"K0007";
    /** 호스트 접속(Config)정보를 확인할 수 없습니다. */
    public static final String	_MSG_SOKT_K9999		=	"K9999";


    /* ***  SYSTEM 처리(UX포함) 관련 오류 메세지 정의  *** */
    /** 요청작업 처리 중 예기치 않은 오류가 발생하였습니다. */
    public static final String	_MSG_SYSU_S9999		=	"S9999";
    /** 사용자 세션시간이 만료 되었습니다. */
    public static final String	_MSG_SYSU_S0001		=	"S0001";

    /** 로그인 회사 또는 아이디를 확인 하세요. */
    public static final String	_MSG_SYSU_S2010		=	"S2010";
    /** 사용자 비밀번호가 틀렸습니다. */
    public static final String	_MSG_SYSU_S2011		=	"S2011";
    /** 비밀번호 오류 횟수 제한으로 로그인할 수 없습니다.<br/>잠시 후 재시도해 보시기 바랍니다. */
    public static final String	_MSG_SYSU_S2012		=	"S2012";
    /** 비밀번호 변경 기간이 만료 되었습니다.<br/>변경 후 사용하시기 바랍니다. */
    public static final String	_MSG_SYSU_S2013		=	"S2013";

    /* ***  TASKS(System관련) 처리 중 오류 메세지 정의  *** */
    /** 기존 비밀번호와 동일한 값을 입력하였습니다. */
    public static final String	_MSG_SYSU_S3001		=	"S3001";
    /** 잔액최종갱신일시가 실시간잔액유효시간을 경과한 경우에만 잔액조회 서비스를 사용할 수 있습니다. */
    public static final String	_MSG_SYSU_S3002		=	"S3002";
    /** 관리자에게 문의하세요.<br/>결재진행일련번호가 조회되지 않았습니다.*/
    public static final String	_MSG_SYSU_S3005		=	"S3003";
    /** 관리자에게 문의하세요.<br/>결재진행상태코드가 올바르지 않습니다.*/
    public static final String	_MSG_SYSU_S3006		=	"S3004";

    /* ***  Hermes-GFBK 처리 관련 오류 메세지 정의  *** */
    /** 정의되지 않은 외부 전송전문을 요청 하였습니다. */
    public static final String	_MSG_HFBK_H0001		=	"H0001";
    /** 외부 전송요청에 필요한 데이터의 입력이 누락 되었습니다. */
    public static final String	_MSG_HFBK_H0002		=	"H0002";
    /** 외부 전송전문 중 필수입력 항목이 누락 되었습니다. */
    public static final String	_MSG_HFBK_H0003		=	"H0003";
    /** 외부 전문전송 중 오류메세지가 수신되었습니다. */
    public static final String	_MSG_HFBK_H0004		=	"H0004";
    /** 전문 송수신 기록(로그테이블) 중 오류가 발생하였습니다. */
    public static final String	_MSG_HFBK_H0005		=	"H0005";
    /** 전문 송수신 처리 중 오류가 발생하였습니다. */
    public static final String	_MSG_HFBK_H9999		=	"H9999";

    /* ***  스크래핑 처리 관련 오류 메세지 정의  *** */
    /** 스크래핑 통신 중 장애가 발생하였습니다. */
    public static final String	_MSG_SCRP_P9999		=	"P9999";
    /** 계좌종류 검증 오류입니다. 계좌종류가 정확한지 확인하세요. **/
    public static final String	_MSG_SCRP_P1001		=	"P1001";
    
    /* ***  Scheduler 처리 관련 오류 메세지 정의  *** */
    /** 스케쥴 실행 중 오류가 발행 하였었습니다. */
    public static final String	_MSG_SCHD_J9999		=	"J9999";

    /**
     * 메시지코드에 대한 설명정보를 맵에 등록한다.
     */
    private static void setMessageInfo()
    {
        _MessageInfo	=	new Hashtable<String, String>();

        _MessageInfo.put(_MSG_SUCCESS   , "정상처리되었습니다.");

        _MessageInfo.put(_MSG_CODE_00001, "정상적으로 조회가 완료되었습니다.");
        _MessageInfo.put(_MSG_CODE_90001, "조회 시 오류가 발생하였습니다.");
        _MessageInfo.put(_MSG_CODE_90002, "조회된 데이터가 없습니다.");

        _MessageInfo.put(_MSG_TASK_T9999, "Task 처리 중 오류가 발생하였습니다.");
        _MessageInfo.put(_MSG_TASK_T9998, "Task 처리 중 오류가 발생하였습니다.(messageReceived())");
        _MessageInfo.put(_MSG_TASK_T9997, "Task 처리 중 오류가 발생하였습니다.(sendErrorMessage())");
        _MessageInfo.put(_MSG_TASK_T9996, "동적 Task 생성과정에서 오류가 발생하였습니다.");

        _MessageInfo.put(_MSG_TASK_T0000, "서비스 호출을 위한 Task-ID 값이 없습니다.");
        _MessageInfo.put(_MSG_TASK_T0001, "~Tasks.xml에 설정된 action 값이 Task(Java)에 구현되어 있지 않습니다.");
        _MessageInfo.put(_MSG_TASK_T0002, "입력받은 Parameter 값이 1건(row)도 존재하지 않습니다.");
        _MessageInfo.put(_MSG_TASK_T0003, "정의되지 않은 Task 정보를 호출하였습니다.");
        _MessageInfo.put(_MSG_TASK_T0004, "Timeout 발생 !!");
        _MessageInfo.put(_MSG_TASK_T0005, "FWTP로 받은 Input Prameter 에서 해당 정보의 데이터를 얻을 수 없습니다.");

        _MessageInfo.put(_MSG_TASK_T1001, "결재선 목록에 포함된 사용자입니다.");

        _MessageInfo.put(_MSG_TASK_P0001, "잘못된 Parameter 값이 입력되었습니다.");

        _MessageInfo.put(_MSG_SQLM_Q9999, "SqlMap 처리 중 오류가 발생하였습니다");

        _MessageInfo.put(_MSG_SQLM_Q0001, "Key 생성에 실패하였습니다.");
        _MessageInfo.put(_MSG_SQLM_Q0002, "Query 실행 중 오류가 발생하였습니다.");
        _MessageInfo.put(_MSG_SQLM_Q0003, "(Broadcasting) 조건을 만족하는 데이터가 존재하지 않습니다.");
        _MessageInfo.put(_MSG_SQLM_Q0004, "(Broadcasting : Set) 기존 결과와 같습니다.");

        _MessageInfo.put(_MSG_SOKT_K0000, "ClientEngine 작업 중 예기치 않은 오류가 발생하였습니다.");
        _MessageInfo.put(_MSG_SOKT_K0001, "소켓 연결이 되지 않았습니다.");
        _MessageInfo.put(_MSG_SOKT_K0002, "소켓 종료시 에러가 발생하였습니다.");
        _MessageInfo.put(_MSG_SOKT_K0003, "메세지 전송중 에러가 발생 하였습니다.");
        _MessageInfo.put(_MSG_SOKT_K0004, "메세지 수신중 에러가 발생 하였습니다.");
        _MessageInfo.put(_MSG_SOKT_K0005, "전송 메세지 생성에 실패 하였습니다.");
        _MessageInfo.put(_MSG_SOKT_K0006, "수신 메세지 생성에 실패 하였습니다.");
        _MessageInfo.put(_MSG_SOKT_K0007, "FWTP 프로토콜 오류 입니다.");
        _MessageInfo.put(_MSG_SOKT_K9999, "호스트 접속(Config)정보를 확인할 수 없습니다.");

        _MessageInfo.put(_MSG_SYSU_S9999, "요청작업 처리 중 예기치 않은 오류가 발생하였습니다.");
        _MessageInfo.put(_MSG_SYSU_S0001, "사용자 세션시간이 만료 되었습니다.");

        _MessageInfo.put(_MSG_SYSU_S2010, "로그인 회사 또는 아이디를 확인 하세요.");
        _MessageInfo.put(_MSG_SYSU_S2011, "사용자 비밀번호가 틀렸습니다.");
        _MessageInfo.put(_MSG_SYSU_S2012, "비밀번호 오류 횟수 제한으로 로그인할 수 없습니다.<br/>잠시 후 재시도해 보시기 바랍니다.");
        _MessageInfo.put(_MSG_SYSU_S2013, "비밀번호 변경 기간이 만료 되었습니다.<br/>변경 후 사용하시기 바랍니다.");

        _MessageInfo.put(_MSG_SYSU_S3001, "기존 비밀번호와 동일한 값을 입력하였습니다.");
        _MessageInfo.put(_MSG_SYSU_S3002, "잔액최종갱신일시가 실시간잔액유효시간을 경과한 경우에만 잔액조회 서비스를 사용할 수 있습니다.");

        _MessageInfo.put(_MSG_HFBK_H0001, "정의되지 않은 외부 전송전문을 요청 하였습니다.");
        _MessageInfo.put(_MSG_HFBK_H0002, "외부 전송요청에 필요한 데이터의 입력이 누락 되었습니다.");
        _MessageInfo.put(_MSG_HFBK_H0003, "외부 전송전문 중 필수입력 항목이 누락 되었습니다.");
        _MessageInfo.put(_MSG_HFBK_H0004, "외부 전문전송 중 오류메세지가 수신되었습니다.");
        _MessageInfo.put(_MSG_HFBK_H0005, "전문 송수신 기록(로그테이블) 중 오류가 발생하였습니다.");
        _MessageInfo.put(_MSG_HFBK_H9999, "전문 송수신 처리 중 오류가 발생하였습니다.");

        _MessageInfo.put(_MSG_SCRP_P9999, "스크래핑 통신 중 장애가 발생하였습니다.");
        _MessageInfo.put(_MSG_SCRP_P1001, "계좌종류 검증 오류입니다. 계좌종류가 정확한지 확인하세요.<br/>(Account type validation error. Chek the correct type of account.)" );
        
        _MessageInfo.put(_MSG_SCHD_J9999, "스케쥴 실행 중 오류가 발행 하였었습니다.");
    }

    public static boolean isValidCode(String code) {
        if (code == null || code.trim().length() != _MSG_CODE_SIZE_)
            return false;

        if (_MessageInfo == null || _MessageInfo.isEmpty()) setMessageInfo();

        String retErrMsg = _MessageInfo.get(code);
        if (retErrMsg == null || "".equals(retErrMsg.trim()))
            return false;

        return true;
    }

    /**
     * 메시지코드에 해당하는 설명을 얻는다
     * @param code 오류코드
     * @return 오류코드 설명값
     */
    public static String getMessage(String code) {
        String retErrMsg = null;

        if (code == null || code.trim().length() != _MSG_CODE_SIZE_) {
            retErrMsg = "메시지 코드는 반드시 `"+ _MSG_CODE_SIZE_ +"`자리로 입력되어야 합니다. - ["+ code +"]";
            return retErrMsg;
        }

        if (_MessageInfo == null || _MessageInfo.isEmpty()) setMessageInfo();

        retErrMsg = _MessageInfo.get(code);

        if (AgentUtil.isNull(retErrMsg)) {
            retErrMsg = "### 정의되지 않은 메세지 코드를 사용하였습니다.<br/>"
            		  + "["+ code +"]에 해당하는 메세지를 `"+ FixedMsg.class +"` Class에 정의하시기 바랍니다!";
        }

        return retErrMsg;
    }

    public static Hashtable<String, String> getMessageTable() {
        if (_MessageInfo == null || _MessageInfo.isEmpty()) setMessageInfo();
        return _MessageInfo;
    }
}