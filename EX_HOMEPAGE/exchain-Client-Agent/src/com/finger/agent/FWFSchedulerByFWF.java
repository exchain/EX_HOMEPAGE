package com.finger.agent;

import org.apache.log4j.Logger;

import com.finger.agent.client.EngineSocket;
import com.finger.agent.config.FcAgentConfig;
import com.finger.agent.exception.AgentException;
import com.finger.agent.msg.FixedMsg;

public class FWFSchedulerByFWF {
    private static Logger logger = Logger.getLogger(FWFSchedulerByFWF.class);

    public FWFSchedulerByFWF() { }

    /**
     * 이중화서버에서 상대방서버로의 연결 정보를 확인한다.
     * 입력한 서버IP값이 호출한 서버이므로 상대 IP로 연결상태를 확인하여 결과를 넘김
     * @param fwtp
     * @return
     * @throws AgentException
     */
    public static boolean doFWFAgentForFWF(String serverip) throws AgentException{
    	


        EngineSocket eSocket = null;

		return true;

    }
}