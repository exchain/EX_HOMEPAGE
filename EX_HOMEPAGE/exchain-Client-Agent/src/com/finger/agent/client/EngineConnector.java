/*
 * ******************************************************
 * Program Name : @(#)EngineConnector.java
 * Function description : 
 * Programmer Name :  
 * Creation Date :
 *
 * ******************************************************
 */
package com.finger.agent.client;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.log4j.Logger;

import com.finger.agent.config.FcAgentConfig;
import com.finger.agent.exception.AgentException;
import com.finger.agent.msg.FixedMsg;
import com.finger.protocol.fwtp.FwtpMessage;
import com.finger.protocol.hermes.HermesMessage;
import com.finger.protocol.relay.RelayMessage;

public class EngineConnector {

	private static Logger logger = Logger.getLogger(EngineSocket.class);
	
    private EngineSocket 		_conn 		= null;

    private String 				_host 		= "127.0.0.1";	// host ip
    public static String 		_host2		= "127.0.0.1";	// host ip(이중화 IP)    
    public static String 		_svrName	= "";
    private int 				_port 		= 27005;		// port no
    private boolean             _svrDual    = false;        // 이중화 체크
    private int                 _connErrCnt = 0;

    /*public EngineConnector() { }*/

    public EngineConnector(String host, int port) {
        _host	=	host;
        _port	=	port;
    }
    
    public EngineConnector(String host, int port, String svrName) {
        _host	=	host;
        _port	=	port;
        _svrName = svrName;
        _svrDual = true;
    }
    

    /**
     * 소켓 통신하기 위한 커넥션을 생성한다.
     * 
     * @return ConnectionSocket
     * @exception AgentException
     */
    private void makeConnection() throws AgentException {
        if (_conn == null) {
            _conn =  new EngineSocket( _host, _port );
        }
    }
    
    /**
     * 소켓 통신하기 위한 커넥션을 생성한다.(이중화 소켓통신)
     * 
     * @return ConnectionSocket
     * @exception AgentException
     */
    private void makeDualConnection() throws AgentException {
    	
        if (_conn == null) {
        	
        	try {
        		_conn =  new EngineSocket( _host, _port );
        		
        		if( _conn == null ){
        			logger.debug("*** [ _conn == null ] Connection Error ");
        		}else{
        			logger.debug("*** [ _conn ] Connection Sucess ");
        			
                	logger.debug("*********************************************************************************************");
                	logger.debug("*** EngineSocket 연결 성공");
            		logger.debug("*********************************************************************************************");
        			
   
        		  	// UX => FWF
                	if( "_FWF_APP_SVR_HOST_IP_".equals(_svrName) ){
                		
                		FcAgentConfig._FWF_APP_SVR_HOST_IP_ = _host ;
                		
                    	logger.debug("*********************************************************************************************");
                    	logger.debug("*** FcAgentConfig._FWF_APP_SVR_HOST_IP_ 접속성공 IP 세팅 = ["+ _host +"] ");
                		logger.debug("*********************************************************************************************");
                		
                	//Hermes-Relay=>Hermes-FBK(파일)	
                	}
        		}
        		
            }
        	catch (Exception e) {

        		logger.debug("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
            	logger.debug("----------------------------------------------");
            	logger.debug("---[ERROR] Socket Connection Exception    ----");
            	logger.debug("----------------------------------------------");
            	logger.debug("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
            	
            	//IP변경하여 재접속 시도
            	reConnectServer();
            	
            	
	        }
	    }
    }
    
    
    public void firstConnectServer() throws AgentException{
    	
	
    }
    
    public void reConnectServer() throws AgentException{
    	
    	//3번 이상 커넥션 오류가 발생시 오류 처리
    	if( _connErrCnt >=4 ){

        	logger.debug("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        	logger.debug("$$$  4번 이상 connect Exception           $$$");
        	logger.debug("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
    		
    		throw new AgentException(FixedMsg._MSG_SOKT_K0000, "reconnect Server failed");
    	}

    		
    }
    
    

    
    /* =====[ UX => XXX ]============================================================================================= */
    /**
     * 데이터 전송 (FwtpMessage) [ UX => FWF ]
     * @param fwmsg
     * @return
     * @throws AgentException
     */
    public String communicate(FwtpMessage fwmsg) throws AgentException {
        try {
            // AP서버가 이중화인 경우
            if( _svrDual ){
            	makeDualConnection();
            }else{
            	makeConnection();
            }        	
            return _conn.communicateFwtp( fwmsg );
        } catch (Exception e) {
        		throw new AgentException(FixedMsg._MSG_SOKT_K0000, e);
        } finally {
            if(_conn != null) _conn.close();
        }
    }
    public void communiJustSend(FwtpMessage fwmsg) throws AgentException {
        try {
            makeConnection();
            _conn.communiFwtpJustSend( fwmsg );
        } catch (Exception e) {
            throw new AgentException(FixedMsg._MSG_SOKT_K0000, e);
        } finally {
            if(_conn != null) _conn.close();
        }
    }
    
    /* ================================================================================================================= */


    
    /* =====[ FWF-Core => XXX ]============================================================================================= */
    /**
     * 데이터 전송 for Hermes-FBK! from FWF-Core. [ FWF => HERMES-FBK ]
     * @param hermes
     * @return
     * @throws AgentException
     */
    public String communicateHermes(HermesMessage hermes) throws AgentException {
        try {
            makeConnection();
            return _conn.communicateHermes( hermes );
        } catch (Exception e) {
            throw new AgentException(FixedMsg._MSG_SOKT_K0000, e);
        } finally {
            if(_conn != null) _conn.close();
        }
    }
    /* ================================================================================================================= */
    
    
    
    /* =====[ HERMES-FBK => XXX ]============================================================================================= */

    /**
     * 데이터 전송 (FwtpMessage) [ HERMES-FBK => FWF ]
     * @param fwmsg
     * @return
     * @throws AgentException
     */
    public String communicateByHms(FwtpMessage fwmsg) throws AgentException {
        try {
            makeConnection();
            return _conn.communicateFwtpByHms( fwmsg );
        } catch (Exception e) {
            throw new AgentException(FixedMsg._MSG_SOKT_K0000, e);
        } finally {
            if(_conn != null) _conn.close();
        }
    }
    public void communiJustSendByHms(FwtpMessage fwmsg) throws AgentException {
        try {
            makeConnection();
            _conn.communiFwtpJustSendByHms( fwmsg );
        } catch (Exception e) {
            throw new AgentException(FixedMsg._MSG_SOKT_K0000, e);
        } finally {
            if(_conn != null) _conn.close();
        }
    }
    
    
    /**
     * 데이터 전송 for Outside(FIRM/VAN)! at Relay. [ HERMES-FBK => HERMES-Relay ]
     * @param hermes
     * @return
     * @throws AgentException
     */
    public String communicateHms2Rly(HermesMessage hermes) throws AgentException {
        try {
            makeConnection();
            return _conn.communicateHms2Rly( hermes );
        } catch (Exception e) {
            throw new AgentException(FixedMsg._MSG_SOKT_K0000, e);
        } finally {
            if(_conn != null) _conn.close();
        }
    }
    
    /**
     * 데이터 전송 for Relay File(VAN)! [  HERMES-FBK => HERMES-Relay ]
     * @param hermes
     * @return
     * @throws AgentException
     */
    public String communicateHms2RlyFile(HermesMessage hermes) throws AgentException {
        try {
            makeConnection();
            return _conn.communicateHms2RlyFile( hermes );
        } catch (Exception e) {
            throw new AgentException(FixedMsg._MSG_SOKT_K0000, e);
        } finally {
            if(_conn != null) _conn.close();
        }
    }
    
    /* ================================================================================================================= */
    
    

    /* =====[ HERMES-Relay => XXX ]============================================================================================= */
    /**
     * 데이터 전송 for Relay! [ HERMES-Relay => VAN ]
     * @param relayData
     * @return
     * @throws AgentException
     */
    public String communicateRelayTo(String relayData, int messageSize) throws AgentException {
        try {
            makeConnection();
            return _conn.communicateRelayTo( relayData, messageSize );
        } catch (Exception e) {
            throw new AgentException(FixedMsg._MSG_SOKT_K0000, e);
        } finally {
            if(_conn != null) _conn.close();
        }
    }
    
    /**
     * 데이터 전송 for Relay! [  HERMES-Relay => HERMES-FBK ]
     * @param relayData
     * @return
     * @throws AgentException
     */
    public String communicateRlyFile2Hms(RelayMessage relay) throws AgentException {
        try {
            // AP서버가 이중화인 경우
            if( _svrDual ){
            	makeDualConnection();
            }else{
            	makeConnection();
            }           	
            return _conn.communicateRlyFile2Hms( relay);
        } catch (Exception e) {
            throw new AgentException(FixedMsg._MSG_SOKT_K0000, e);
        } finally {
            if(_conn != null) _conn.close();
        }
    }
    
    /* ================================================================================================================= */
    
    
    /* =====[ VAN => XXX ]============================================================================================= */
    /**
     * 데이터 전송 for Relay! [ VAN => HERMES-Relay ]
     * @param relayData
     * @return
     * @throws AgentException
     */
    public String communicateOutRelayTo(String relayData, int messageSize) throws AgentException {
        try {
            // AP서버가 이중화인 경우
            if( _svrDual ){
            	makeDualConnection();
            }else{
            	makeConnection();
            }           	
            return _conn.communicateOutRelayTo( relayData, messageSize );
        } catch (Exception e) {
            throw new AgentException(FixedMsg._MSG_SOKT_K0000, e);
        } finally {
            if(_conn != null) _conn.close();
        }
    }

    /* ================================================================================================================= */

    public void close() throws AgentException {
        if (_conn != null) _conn.close();
    }

    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}