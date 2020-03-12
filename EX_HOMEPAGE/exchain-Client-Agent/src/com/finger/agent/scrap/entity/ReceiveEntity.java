package com.finger.agent.scrap.entity;

import java.util.HashMap;

import com.finger.agent.util.AgentUtil;

public class ReceiveEntity {

    /* 000 : return data
     * 001 : 보통 메세지-거래를 했스는데 거래내역이 없습니다.(정보성)
     * 002 : 에러메세지 - message에 셋팅
     * 003 : HTML형식
     * 004 : 결과무시
     * 900 : 취소
     * 901 : 타임아웃  - ICMSServiceException 처리
     * 902 : 스크립트 문법에러 - ICMSServiceException 처리
     * 999 : 기타오류 - ICMSServiceException 처리
     */

    private String serviceID;       /** 서비스 아이디 */
    private String retCode;         /** return code */
    private String message;         /** return message [에러메세지 및 정보 메세지] */
    private Entity entity;          /** 한개 레코드 */

    private Entity[] entityArray;   /** 여러개 레코드 */
    private HashMap<?, ?> hm;

    boolean isEtc = false;
    String etcCode = "";
    String etcMessage = "";
    public ReceiveEntity() {
    }

    public ReceiveEntity(String serviceID, String retCode, String message) {
        this.serviceID = serviceID;
        this.retCode = retCode;
        this.message = message;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public boolean getEtc() {
        return isEtc;
    }
    public void setEtc(boolean value ) {
        this.isEtc =value;
    }

    public String getEtcCode() {
        return etcCode;
    }
    public void setEtcCode(String value ) {
        this.etcCode =value;
    }
    public String getEtcMessage() {
        return etcMessage;
    }
    public void setEtcMessage(String value ) {
        this.etcMessage =value;
    }

    public String getServiceID() {
        return serviceID;
    }
    public void setHashMap(HashMap<?, ?> hm) {
        this.hm = hm;
    }

    public HashMap<?, ?> getHashMap() {
        return hm;
    }


    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getRetCode() {
        return retCode;
    }

    /**
     * return code가 000이외 message 얻을수 있다.
     * return code가 002였다면 message에 에러 내용이 존재 한다.
     * @param message
     */
    public void setMessage(String message) {
        if(message.length() >200 ){
            message = message.substring(0,200);
        }

        this.message = message;
    }

    public String getMessage() {
        return AgentUtil.scrapRecvDataFilter(this.message);
    }

    /**
     * 단일 레코드 - return code가 000이다면 entity값 존재
     * @param entity
     */
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    /**
     * 멀티 레코드 - return code가 000이다면 entity값 존재
     * @param entityArray
     */
    public void setEntityArray(Entity[] entityArray) {
        this.entityArray = entityArray;
    }

    /**
     * 멀티 레코드 Entity를 얻는다.
     * @return
     */
    public Entity[] getEntityArray() {
        return entityArray;
    }

    /**
     * ReceiveEntity에 객체변수 확일 가능
     */
    public String toString() {
        String toString = "Entity = ";
        if (entity == null)
            toString = toString + " \n";
        else
            toString = toString + entity.toString() + "\n";

        if (entityArray == null)
            toString = toString + "EntityArray = \n";
        else {
            for (int i=0; i < entityArray.length; i++)
                toString = toString + "EntityArray = " + entityArray[i].toString() + "\n";
        }

        return "\n" +
            "ServiceID = " + serviceID + "\n" +
            "RetCode   = " + retCode   + "\n" +
            "Message   = " + message   + "\n" +
            toString;
    }
}
