package com.finger.emulator.hermes;

import com.finger.agent.exception.AgentException;
import com.finger.emulator.hermes.clientsocket.HmEngineConnector;

class HmEmulShutdownHook extends Thread {

	HmEngineConnector m_connector = null;

	public HmEmulShutdownHook() {
	}

	public HmEmulShutdownHook(HmEngineConnector connector) {
		this.m_connector = connector;
	}

	public void run() {
		shutdown();
	}

	private void shutdown() {
		if (m_connector != null) {
			try {
				m_connector.close();
				System.out.println("Hermes Emulator, Connection closed !!");
			}
			catch (AgentException e) {
				e.printStackTrace();
			}
		}
	}
}