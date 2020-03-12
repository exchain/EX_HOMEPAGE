package com.finger.agent.msg;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class MsgObj {

    private String	_msg_code	=	"";
    private String	_msg_conts	=	"";
    private String	_msg_respo	=	"";

    public MsgObj(String msgCd) {
        if (msgCd != null)
            _msg_code = msgCd;
    }

    public MsgObj(String msgCd, String msgConts) {
        if (msgCd != null)
            _msg_code = msgCd;
        if (msgConts != null)
            _msg_conts = msgConts;
    }

    public String getCode() {
        if (MsgInfo.isValidateCode(_msg_code))
            return _msg_code;
        else
            return "E9XXX";
    }

    public String getDesc() {
        if (MsgInfo.isValidateCode(_msg_code))
            return MsgInfo.getMessage(_msg_code);
        else
            return _msg_code;
    }

    public String getConts() {
        return _msg_conts;
    }

    public String getRepos() {
        return _msg_respo;
    }
    public void setRepos(String msgRespo) {
        _msg_respo = msgRespo;
    }

    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}