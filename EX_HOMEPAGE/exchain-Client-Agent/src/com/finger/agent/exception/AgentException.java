/*
 * ******************************************************
 * Program Name : @(#)AgentException.java
 * Function description : 
 * Programmer Name :  
 * Creation Date :
 * ******************************************************
 */
package com.finger.agent.exception;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.finger.agent.msg.MsgObj;

public class AgentException extends Exception {
    private static final long serialVersionUID = 4878303830768120713L;

    private String _err_code ;
    private String _err_extra ;
    private String _err_respo ;

    public AgentException(){
        _err_code = null;
        _err_extra = null;
        _err_respo = null;
    }

    /**
     * 특정한 메시지를 갖는 exception 을 생성한다.
     */
    public AgentException(String errCode){
        super(errCode);
        _err_code = errCode;
    }

    /**
     * 특정한 메시지를 갖는 exception 을 생성한다.
     */
    public AgentException(String errCode, String errExtra){
        super(errCode);
        _err_code = errCode;
        _err_extra = errExtra;
    }
    public AgentException(MsgObj msgObj){
        super(msgObj.getCode());
        _err_code = msgObj.getCode();
        _err_extra = msgObj.getConts();
    }
    public AgentException(MsgObj msgObj, String errRespo){
        super(msgObj.getCode());
        _err_code = msgObj.getCode();
        _err_extra = msgObj.getConts();
        _err_respo = errRespo;
    }

    /**
     * 특정한 메시지와 Exception 을 생성한다.
     */
    public AgentException(String errCode, Exception ex) {
        super(errCode, ex);
        _err_code = errCode;
        _err_extra = ex.getMessage();
    }

    public String getErrCode() {
        return _err_code;
    }
    public String getErrExtra() {
        return _err_extra;
    }
    public String getErrRespo() {
        return _err_respo;
    }

    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}