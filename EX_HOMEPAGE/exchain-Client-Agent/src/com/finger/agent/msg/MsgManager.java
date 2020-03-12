package com.finger.agent.msg;

import com.finger.agent.exception.AgentException;

public final class MsgManager {

    private static MsgManager m_this = new MsgManager();

    private MsgManager() { }

    public static MsgManager getInstance() {
        return m_this;
    }

    public MsgObj createMessage(Exception ex)
    {
        if (ex instanceof AgentException) {
            AgentException aex = (AgentException)ex;
            MsgObj msgObj = new MsgObj(aex.getErrCode(), aex.getErrExtra());
            msgObj.setRepos(aex.getErrRespo());
            return msgObj;
        }
        else {
            return ( new MsgObj(ex.getMessage(), ex.toString()) );
        }
    }
}