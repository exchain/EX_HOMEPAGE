package com.finger.fwf.server.handler;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.finger.agent.msg.FixedMsg;
import com.finger.fwf.core.task.FwfTasksLoader;
import com.finger.protocol.fwtp.FwtpMessage;

/**
 * {@link IoHandler} implementation of a simple chat server protocol.
 */
public class FWFProtocolHandler extends IoHandlerAdapter {
    private static Logger logger = Logger.getLogger(FWFProtocolHandler.class);

    public void sendErrorMessage(IoSession session, String errCode) {
        sendErrorMessage(session, errCode, null);
    }

    public void sendErrorMessage(IoSession session, Throwable e) {
        sendErrorMessage(session, FixedMsg._MSG_TASK_T9998, new Exception(e));
    }

    public void sendErrorMessage(IoSession session, String errCode, Exception ex) {
        FwtpMessage message = new FwtpMessage();
        message.setMessageCode(errCode);

        String strErrMsg = FixedMsg.getMessage(errCode);
        message.addErrorMessage(strErrMsg);
        logger.error("=>["+ errCode +"]["+ strErrMsg +"]");
        if (ex != null)
            logger.error(ex.getMessage(), ex);

        sendErrorMessage(session, message);
    }

    public void sendErrorMessage(IoSession session, FwtpMessage message) {
        if (session.isConnected()) session.write(message);
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) {
        logger.error(cause.getMessage(), cause);
        // Close connection when unexpected exception is caught.
        //if ((boolean)session.getAttribute(HEADER_ERROR))
        sendErrorMessage(session, cause);
    }


    /**
     * 수신된 정보(Message)에 해당하는 Task 를 실행한 후 결과를 리턴한다.
     */
    @Override
    public void messageReceived(IoSession session, Object message) {
        logger.debug("### [APSERVER] messageReceived()");

        FwtpMessage  request  = (FwtpMessage)message;

        FwtpMessage response = null;
        try {
            response = FwfTasksLoader.loadTransTask(request);

            if (session.isConnected())
                session.write(response);
        }
        catch(Exception ex) {
            sendErrorMessage(session, FixedMsg._MSG_TASK_T9998, ex);
        }
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        logger.debug("### [APSERVER] messageSent()");
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        logger.info("### [APSERVER] sessionOpened - Connected Client : " + session.getRemoteAddress());
        // set idle time to ?? seconds
        session.getConfig().setReaderIdleTime(3600);	//seconds
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        logger.info("### [APSERVER] sessionClosed - Closed IP : "+ session.getRemoteAddress());
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) {
        logger.debug("### [APSERVER] sessionIdle - Disconnecting the idle. " + status );
        // disconnect an idle client
        sendErrorMessage(session, FixedMsg._MSG_TASK_T0004);	// 주로 Timeout
        session.close(true);
    }
}