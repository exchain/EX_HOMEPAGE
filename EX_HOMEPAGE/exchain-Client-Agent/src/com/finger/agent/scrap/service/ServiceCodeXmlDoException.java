package com.finger.agent.scrap.service;

public class ServiceCodeXmlDoException extends Exception {
    private static final long serialVersionUID = 221264007470711977L;

    /**
     * DefaultException
     */
    public ServiceCodeXmlDoException(){
        super();
    }

    /**
     * 특정한 메시지를 갖는 exception을 생성한다.
     * @param errorCode 메시지
     */
    public ServiceCodeXmlDoException(String errorCode){
        super(errorCode);
    }

    /**
     * 특정한 메시지와 exception을 생성한다.
     * @param errorCode
     * @param e
     */
    public ServiceCodeXmlDoException(String errorCode, Exception e) {
        super(errorCode, e);
    }

    /**
     * 특정한 메시지와 추가적 메세지를 혼합 생성한다.
     * @param errorCode
     * @param additionalExceptionMessage
     */
    public ServiceCodeXmlDoException(String errorCode, String additionalExceptionMessage){
        super(errorCode +" "+ additionalExceptionMessage);
    }

    /**
     * 특정한 메시지와 Throwable을 갖는 exception을 생성한다.
     * @param s 메시지
     * @param t exception chaining에 필요한 Throwable
     */
    public ServiceCodeXmlDoException(String s, Throwable t) {
        super(s,t);
    }

    /**
     * 특정한 Throwable을 갖는 exception을 생성한다.
     * @param t exception chaining에 필요한 Throwable
     */
    public ServiceCodeXmlDoException(Throwable t) {
        super(t);
    }

}