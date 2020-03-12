package com.finger.emulator.fwf;

import org.apache.log4j.Logger;

import com.finger.agent.client.EngineConnector;
import com.finger.agent.exception.AgentException;

class EmulShutdownHook extends Thread {
	private static Logger logger = Logger.getLogger(EmulShutdownHook.class);

	EngineConnector m_connector = null;

	public EmulShutdownHook() { }

	public EmulShutdownHook(EngineConnector connector) {
		this.m_connector = connector;
	}

	public void run() {
		shutdown();
	}

	private void shutdown() {
		if (m_connector != null) {
			try {
				m_connector.close();
				logger.debug("FWF Emulator, Connection closed !!");
			}
			catch (AgentException e) {
				e.printStackTrace();
			}
		}
	}
}