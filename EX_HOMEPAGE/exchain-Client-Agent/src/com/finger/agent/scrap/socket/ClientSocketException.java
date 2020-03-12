package com.finger.agent.scrap.socket;

public class ClientSocketException extends Exception {
    private static final long serialVersionUID = -6516972564827290492L;

    public static String CONNECT_FAIL               = "소켓 연결이 되지 않았습니다.";
    public static String SEND_MESSAGE_FAIL          = "메세지 전송중 에러가 발생 하였습니다.";
    public static String RECEIVE_MESSAGE_FAIL       = "메세지 수신중 에러가 발생 하였습니다.";
    public static String CREATE_SEND_MESSAGE_FAIL   = "전송 메세지 생성에 실패 하였습니다.";
    public static String CREATE_RECEIVE_MESSAGE_FAIL= "수신 메세지 생성에 실패 하였습니다.";
    public static String DISCONNECT_FAIL            = "소켓 종료시 에러가 발생하였습니다.";

    /**
     * DefaultException
     */
    public ClientSocketException(){
        super();
    }

    /**
     * 특정한 메시지를 갖는 exception을 생성한다.
     * @param errorCode 메시지
     */
    public ClientSocketException(String errorCode){
        super(errorCode);
    }

    /**
     * 특정한 메시지와 exception을 생성한다.
     * @param errorCode
     * @param e
     */
    public ClientSocketException(String errorCode, Exception e) {
        super(errorCode, e);
    }

    /**
     * 특정한 메시지와 추가적 메세지를 혼합 생성한다.
     * @param errorCode
     * @param additionalExceptionMessage
     */
    public ClientSocketException(String errorCode, String additionalExceptionMessage){
        super(errorCode +" "+ additionalExceptionMessage);
    }

    /**
     * 특정한 메시지와 Throwable을 갖는 exception을 생성한다.
     * @param s 메시지
     * @param t exception chaining에 필요한 Throwable
     */
    public ClientSocketException(String s, Throwable t) {
        super(s,t);
    }

    /**
     * 특정한 Throwable을 갖는 exception을 생성한다.
     * @param t exception chaining에 필요한 Throwable
     */
    public ClientSocketException(Throwable t) {
        super(t);
    }

}