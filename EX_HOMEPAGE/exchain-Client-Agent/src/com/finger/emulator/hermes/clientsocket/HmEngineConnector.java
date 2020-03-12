/*
 * ******************************************************
 * Program Name : @(#)HmEngineConnector.java
 * Function description : 
 * Programmer Name :  
 * Creation Date :
 *
 * ******************************************************
 */
package com.finger.emulator.hermes.clientsocket;

import com.finger.agent.exception.AgentException;
import com.finger.agent.msg.FixedMsg;

public class HmEngineConnector {

    private HmEngineSocket 	_conn 			= null;

    public static String 	_host 			= "127.0.0.1";		// ip
    public static int 		_port 			= 25015;			// port  / FBK-25015, 중계-25051(개발)
    public static int 		_sendTimeout 	= 10000;			// 보내기 제한시간/ millisecond
    public static int 		_recvTimeout 	= 10000;			// 받기 제한시간/ millisecond
    
    public HmEngineConnector() {
    }
    
    public HmEngineConnector(String host, int port) {
    	_host 		= host;
    	_port		= port;
    }
    public HmEngineConnector(String host, int port, int sendTimeout, int recvTimeout) {
    	_host 		= host;
    	_port		= port;
    	_sendTimeout= sendTimeout;
    	_recvTimeout= recvTimeout;
    }

    /**
     * 소켓 통신하기 위한 커넥션을 얻는다.
     * @exception AgentException
     */
    public void makeConnection() throws AgentException {
    	if (_conn==null) {
    		_conn =  new HmEngineSocket(_host, _port, _sendTimeout, _recvTimeout);
    	}
    }

    /**
     * 데이터 전송
     * @param data 전송문자열
     * @return 전송결과 수신 문자열
     * @throws AgentException
     */
    public String communicate(String data) throws AgentException {
        try {
        	makeConnection();
            return _conn.communicate(data);
        } catch (Exception e) {
            throw new AgentException(FixedMsg._MSG_SOKT_K0000, e);
        } finally {
            if(_conn != null) _conn.close();
        }
    }

    public void send(String taskId, String dataType, String data) throws AgentException {
        try {
        	makeConnection();
        	_conn.send(data);
        } catch (Exception e) {
            throw new AgentException(FixedMsg._MSG_SOKT_K0000, e);
        }
    }
    
    public String receive() throws AgentException {
        try {
        	makeConnection();
        	return _conn.receive();
        } catch (Exception e) {
            throw new AgentException(FixedMsg._MSG_SOKT_K0000, e);
        }
    }    

    public void close() throws AgentException {
        if (_conn != null) _conn.close();
    }

    /**
     * 서버 정보.
     */
    public String toString() {
        StringBuffer buff = new StringBuffer();
        buff.append("[Server Info : " + getClass().getName() + "]");
        buff.append(" Host: " + _host);
        buff.append(" Port: " + _port);
        buff.append(" SendTimeout: " + _sendTimeout);
        buff.append(" RecvTimeout: " + _recvTimeout);
        return buff.toString();
    }
}