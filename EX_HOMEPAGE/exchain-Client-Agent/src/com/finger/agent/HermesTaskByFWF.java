package com.finger.agent;

import java.util.LinkedHashMap;

import org.apache.log4j.Logger;

import com.finger.agent.client.EngineConnector;
import com.finger.agent.config.FcAgentConfig;
import com.finger.agent.exception.AgentException;
import com.finger.agent.msg.FixedMsg;
import com.finger.agent.util.AgentUtil;
import com.finger.protocol.hermes.HermesMessage;

public class HermesTaskByFWF {
    private static Logger logger = Logger.getLogger(HermesTaskByFWF.class);

    public HermesTaskByFWF() { }

    /**
     * 서버에서 받은 결과 문자열(String) 값을 HermesMessage 전문형태로 반환한다.
     * @param hermes
     * @return
     * @throws AgentException
     */
    public static HermesMessage doHermesAgentForFWF(HermesMessage hermes) throws AgentException {
        return makeReceiveDataToHermes( doHermesAgentForMessage ( hermes ) );
    }

    /**
     * 연결정보에 의하여 서버에 접속 및 요청내역을 전송하고 처리결과 값(String)을 받는다.
     * @param hermes
     * @return
     * @throws AgentException
     */
    private static String doHermesAgentForMessage(HermesMessage hermes) throws AgentException {

        if (hermes.toString().getBytes().length <= 1000){
            logger.debug("(Input)HermesMessage (1,000 byte in) = ["+ hermes.toString() +"]");
        }else{
        	logger.debug("(Input)HermesMessage (1,000 byte over)= ["+ new String(hermes.toString().getBytes(), 0, 900) +".....]");
        }
    	
        if (AgentUtil.isNull(hermes.getHmTaskId()))
            throw new AgentException("TASK-ID가 누락되었습니다.", "시스템담당자에게 문의 바랍니다.");

        if (!hermes.validateMessageByHermes())
            throw new AgentException(hermes.getErrorCode(), hermes.getErrorExtra());

        if (!FcAgentConfig._PROPERTY_READ_SUCCESS)
            throw new AgentException(FixedMsg._MSG_SOKT_K9999, "시스템담당자에게 문의 바랍니다.");

        EngineConnector econn = new EngineConnector( FcAgentConfig._HERMES_FBK_HOST_IP_, FcAgentConfig._HERMES_FBK_PORT_NO_ );

        return econn.communicateHermes( hermes );
    }

    /**
     * 중계서버 파일저장과 관련된 작업을 호출한다.
     * @param fileName
     * @param fileData
     * @return
     * @throws AgentException
     */
    public static HermesMessage doHermesFileAgentForFWF(String fileName, String fileData) throws AgentException {

        HermesMessage hermes = new HermesMessage();

        if (AgentUtil.isNull(fileName) || AgentUtil.isNull(fileData)) {
            hermes.setResponseCode(HermesMessage._FAILURE_RCODE_);
            return hermes;
        }

        LinkedHashMap<String, String> hmRequest = new LinkedHashMap<String, String>();
        hmRequest.put(HermesMessage.__Key_RlyFileName_, fileName );
        hmRequest.put(HermesMessage.__Key_RlyFileData_, fileData );

        hermes.setHmTaskId(HermesMessage._VAN_FILESND_);
        hermes.setHermesSData( hmRequest );

        return doHermesAgentForFWF ( hermes );
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * (서버에서 받은 결과) 문자열 값을 HermesMessage 형태로 반환한다.
     * @param inRecvData
     * @return
     * @throws AgentException 
     */
    private static HermesMessage makeReceiveDataToHermes(String inRecvData) throws AgentException {

        HermesMessage message = new HermesMessage();

        if (AgentUtil.isNull(inRecvData)) {
            logger.debug("\n>>> inRecvData = ["+ inRecvData +"]");
            message.setErrorCode(FixedMsg._MSG_SOKT_K0000);
        }
        else
        {
            if (inRecvData.getBytes().length <= 1000){
                logger.debug("inRecvData (1,000 byte in) = ["+ inRecvData +"]");
            }else{
            	logger.debug("inRecvData (1,000 byte over)= ["+ new String(inRecvData.getBytes(), 0, 900) +".....]");
            }

            message.setOriginData(inRecvData);

            String  sHmTaskId  = inRecvData.substring(0, HermesMessage._Len_Task_ID_).trim();
                    inRecvData = inRecvData.substring(HermesMessage._Len_Task_ID_);
            String  sRespCode  = inRecvData.substring(0, HermesMessage._Len_ResCode_);

            message.setHmTaskId( sHmTaskId );
            message.setResponseCode( sRespCode );
            message.setHermesRData( inRecvData.substring(HermesMessage._Len_ResCode_) );
        }

        return message;
    }

    /**
     * Socket Shutdown Hook
     * @param socket
     */
    /*private static void initHook(EngineSocket socket) {
        SocketShutdownHook shutdown = new SocketShutdownHook(socket);
        Runtime.getRuntime().addShutdownHook(shutdown);
    }*/
}