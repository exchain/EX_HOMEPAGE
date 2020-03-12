package com.finger.agent.hook;

import org.apache.log4j.Logger;

import com.finger.agent.client.EngineSocket;
import com.finger.agent.exception.AgentException;

public class SocketShutdownHook extends Thread {
    private static Logger logger = Logger.getLogger(SocketShutdownHook.class);

    EngineSocket m_socket = null;

    public SocketShutdownHook() { }

    public SocketShutdownHook(EngineSocket socket) {
        this.m_socket = socket;
    }

    public void run() {
        shutdown();
    }

    private void shutdown() {
        if (m_socket != null) {
            try {
                this.m_socket.close();
                logger.debug("#HOOK# EngineSocket Connection closed!!");
            }
            catch (AgentException e) {
                e.printStackTrace();
            }
        }
    }
}