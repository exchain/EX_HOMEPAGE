package com.finger.fwf.uxui.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.finger.agent.config.FcAgentConfig;
import com.finger.agent.exception.AgentException;
import com.finger.fwf.uxui.CommonCarts;
import com.finger.protocol.fwtp.FwtpHeader;
import com.finger.protocol.fwtp.FwtpMessage;

public class PrevLoadUX {
    private static Logger logger = Logger.getLogger(PrevLoadUX.class);

    private final static String    _SADM00000_  =  "SADM00000";  //서버정보
    
    public static void previousLoading(String specifyTask) { 

        FwtpMessage fwtp = new FwtpMessage();

        fwtp.setSysId("EXCHAIN");
        fwtp.setUserId("_PRE_LOAD_");
        
        if (UXUtil.isNull(specifyTask) || _SADM00000_.equals(specifyTask) ){  //서버정보
            task_SADM00000(fwtp);
        }        
    }

    /**
     * 메모리로딩 - SADM00000:SERVER ENVIRONMENT INFORMATION
     * @param fwtp
     */
    public static void task_SADM00000(FwtpMessage fwtp) {
        try {
            FwtpMessage result = null;

            fwtp.setTaskId(_SADM00000_);
            result = com.finger.agent.FWFTaskByUXUI.doFWFAgentListForUX ( fwtp );
            if (FwtpHeader._RESPONSE_OKAY_.equals(result.getResponseCode())) {
                CommonCarts.setServerEnvi( result.getResponseResultList().get(0) );
            }
        } catch (AgentException aex) {
            logger.error(aex.toString(), aex);
        }
    }    
}