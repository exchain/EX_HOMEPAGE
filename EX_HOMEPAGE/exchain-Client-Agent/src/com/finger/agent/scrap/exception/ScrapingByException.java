package com.finger.agent.scrap.exception;

public class ScrapingByException extends Exception {
    private static final long serialVersionUID = 8090702703852382839L;

    public static String MESSAGE_RECEIVE_FAIL 	= "메세지 를 받지 못 하였습니다. 전송 서버가 다운되었거나 네트웍이 끊어졌습니다.";
    public static String RECEIVE_RT_CODE 		= "수신 메세지 실패:메세지 수신코드가 아닙니다.";
    public static String RECEIVE_MESSAGE_FAIL 	= "수신 메세지 실패:전송키와 수신키가 다릅니다.";
    public static String RETURN_CODE_901		= "메세지 수신중 타임 아웃 되었습니다.";
    public static String RETURN_CODE_902		= "스크립트 문법 에러 입니다.";
    public static String RETURN_CODE_999		= "기타 오류가 발생하였습니다.";
    public static String RETURN_CODE_801		= " .";
    public static String SEND_DATA_FORMAT_ERROR = "전송데이터  포멧 오류 입니다. \n 서비스 코드 12자리가 아닙니다.";
    public static String SYSTEM_ERROR			= "수신중 오류가 발생하였습니다.";

    public String retCode;
    public String retMessage;
    /**
     * DefaultException
     */
    public ScrapingByException(){
        super();
    }

    /**
     * 특정한 메시지를 갖는 exception을 생성한다.
     * @param errorCode 메시지
     */
    public ScrapingByException(String errorCode){
        super(errorCode);
        retCode = errorCode;
    }

    /**
     * 특정한 메시지와 exception을 생성한다.
     * @param errorCode
     * @param e
     */
    public ScrapingByException(String errorCode, Exception e) {
        super(errorCode, e);
        retCode = errorCode;
    }

    /**
     * 특정한 메시지와 추가적 메세지를 혼합 생성한다.
     * @param errorCode
     * @param additionalExceptionMessage
     */
    public ScrapingByException(String errorCode, String additionalExceptionMessage){
        super(additionalExceptionMessage);
        retCode = errorCode;
        retMessage = additionalExceptionMessage;
    }

    /**
     * 특정한 메시지와 Throwable을 갖는 exception을 생성한다.
     * @param errorCode 에러코드
     * @param t Exception chaining에 필요한 Throwable
	 */
    public ScrapingByException(String errorCode, Throwable t) {
        super(errorCode,t);
        retCode = errorCode;
    }

    /**
     * 특정한 Throwable을 갖는 exception을 생성한다.
     * @param t exception chaining에 필요한 Throwable
	 */
    public ScrapingByException(Throwable t) {
        super(t);
    }

    public String getRetCode() {
        return retCode;
    }

}