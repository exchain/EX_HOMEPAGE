/*
 * ******************************************************
 * Program Name : @(#)EngineSocket.java
 * Function description : 
 * Programmer Name :  
 * Creation Date :
 *
 * ******************************************************
 */
package com.finger.agent.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import org.apache.log4j.Logger;

import com.finger.agent.config.FcAgentConfig;
import com.finger.agent.exception.AgentException;
import com.finger.agent.msg.FixedMsg;
import com.finger.agent.util.AgentUtil;
import com.finger.protocol.fwtp.FwtpHeader;
import com.finger.protocol.fwtp.FwtpMessage;
import com.finger.protocol.hermes.HermesMessage;
import com.finger.protocol.relay.RelayMessage;
import com.finger.tools.cipher.Crypto;

public class EngineSocket {
    private static Logger logger = Logger.getLogger(EngineSocket.class);

    private static final int	_connectTimeout_	=	5000;  //5초 (단위: 1000 = 1초)

    private static final int 	_sendingTimeout_ 	=	FcAgentConfig._MESSAGESEND_TIMEOUT_; // 공통 보내기 제한시간(milliseconds) 30000 (30초)
    private static final int 	_applRecvTimeout_ 	=	FcAgentConfig._COMMUNICATE_TIMEOUT_; // APPL 받기 제한시간(milliseconds)  100000 (100초)
    private static final int 	_uxuiRecvTimeout_ 	=	FcAgentConfig._UI_RESPONSE_TIMEOUT_; // UXUI 받기 제한시간(milliseconds)  300000 (5분)
    private static final int 	_fileRecvTimeout_ 	=	FcAgentConfig._UI_RESPONSE_TIMEOUT_; // UXUI 받기 제한시간(milliseconds)  180000 (3분)

    private static final long	_recvSleepTimes_	=	200;		// 300x200 = 60초
    private static final int	_applSleepCount_	=	_applRecvTimeout_ / (int)_recvSleepTimes_ + 5;
    private static final int	_uxuiSleepCount_	=	_uxuiRecvTimeout_ / (int)_recvSleepTimes_ + 5;
    private static final int	_fileSleepCount_	=	_fileRecvTimeout_ / (int)_recvSleepTimes_ + 5;

    /** 소켓 정보 */
    private Socket	socket;
    private String	host;
    private int		port;

    static {
        logger.debug("\n---------------------------------------------"
                    +"\n   _connectTimeout_  :  "+ _connectTimeout_  +" millis"
                    +"\n   _sendingTimeout_  :  "+ _sendingTimeout_  +" millis"
                    +"\n   _applRecvTimeout_ :  "+ _applRecvTimeout_ +" millis"
                    +"\n   _uxuiRecvTimeout_ :  "+ _uxuiRecvTimeout_ +" millis"
                    +"\n   _fileRecvTimeout_ :  "+ _fileRecvTimeout_ +" millis"
                    +"\n   _recvSleepTimes_  :  "+ _recvSleepTimes_  +" millis"
                    +"\n   _applSleepCount_  :  "+ _applSleepCount_  +" times"
                    +"\n   _uxuiSleepCount_  :  "+ _uxuiSleepCount_  +" times"
                    +"\n   _fileSleepCount_  :  "+ _fileSleepCount_  +" times"
                    +"\n---------------------------------------------");
    }



    /**
     * Socket 커넥션을 만든다.
     *
     * 소켓을 생성하여 서버와 연결한다.
     *
     * @param host 호스트
     * @param port 포트
     * @param sendTimeout 보내기 제한시간
     * @param recvTimeout 받기 제한시간
     * @exception AgentException if connection exception occurs
     */
    public EngineSocket ( String host, int port ) throws AgentException {
        this.host = host;
        this.port = port;

        makeConnect();
    }

    /**
     * 서버와 연결을 맺는다.
     *
     * @exception AgentException if IOException occurs
     */
    private void makeConnect() throws AgentException {
        try {
        	if (socket == null || !socket.isConnected()) {
                if (socket != null)
                    try {
                        socket.close();
                    } catch (Exception ex) { }

                SocketAddress socketAddress = new InetSocketAddress(host, port);
                socket = new Socket();
                socket.connect( socketAddress, _connectTimeout_ );
                
                logger.debug("Socket Connected [ Host:"+ host +", Port:"+ port +" ]");
            }
        } catch (IOException ioe) {
            logger.error(ioe.getMessage() +"[ Host:"+ host +", Port:"+ port +" ]");
            throw new AgentException(FixedMsg._MSG_SOKT_K0001, ioe);
        }
    }

    
/* =====[ UX => FWF-Core 호출 : 25005 ]============================================================================================= */    
    /**
     * 서버에 데이터를 보내고 그 결과를 받는다.
     * @param fwmsg
     * @return 요청결과 수신 문자열
     * @throws AgentException
     */
    public String communicateFwtp(FwtpMessage fwmsg) throws AgentException {
        sendFwtp ( fwmsg );
        return receiveFWF();
    }
    public void communiFwtpJustSend(FwtpMessage fwmsg) throws AgentException {
        sendFwtp ( fwmsg );
    }

    /**
     * FWTP프로토콜 형태로 서버에 데이터를 보낸다.
     * @param fwmsg
     * @throws AgentException
     */
    public void sendFwtp(FwtpMessage fwmsg) throws AgentException {
        BufferedOutputStream out = null;
        try {
            
            if (fwmsg.toString().getBytes().length <= 1000){
                logger.debug("(sendFwtp)FwtpMessage (1,000 byte in) = ["+ fwmsg.toString() +"]");
            }else{
            	logger.debug("(sendFwtp)FwtpMessage (1,000 byte over)= ["+ new String(fwmsg.toString().getBytes(), 0, 900) +".....]");
            }
            
            socket.setSoTimeout(_sendingTimeout_);
            out = new BufferedOutputStream(socket.getOutputStream());

            String strMessage = fwmsg.buildRequestMessage();
            
            logger.debug("*****************************************************");
            logger.debug("*** [ 내부 서비스 ] ["+ host +":"+ port +"]       ***");
            logger.debug("*** [ UX => FWF-CORE ] sendFwtp                   ***");
            logger.debug("*****************************************************");
            if (strMessage.toString().getBytes().length <= 1000){
                logger.debug("sendMsg (1,000 byte in) = ["+ strMessage.toString() +"]");
            }else{
            	logger.debug("sendMsg (1,000 byte over)= ["+ new String(strMessage.toString().getBytes(), 0, 900) +".....]");
            }
            logger.debug("*****************************************************");            
            
            out.write( strMessage.toString().getBytes() );
            out.flush();
        }
        catch (IOException ioe) {
            throw new AgentException(FixedMsg._MSG_SOKT_K0003, ioe);
        }
    }

    /**
     * FWTP프로토콜 형태로 서버로 부터 데이터를 수신한다.
     * @return
     * @throws AgentException
     */
    public String receiveFWF() throws AgentException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        BufferedInputStream in = null;      
        try {
            socket.setSoTimeout(_uxuiRecvTimeout_);
            in = new BufferedInputStream(socket.getInputStream());

            // 헤더 분석
            int nCount = 0;
            while (in.available() < FwtpHeader._HEADER_SIZE) { if(++nCount>_uxuiSleepCount_) break; Thread.sleep(_recvSleepTimes_); };

            int nSize = 4;
            if (nCount >= _uxuiSleepCount_) {
                nSize = in.available();
                byte[] overBuf = new byte[nSize];
                in.read( overBuf, 0, nSize );
                
                logger.debug(">>> receiveFWF ["+ host +":"+ port +"], length: "+ overBuf.length +", timeout: "+ FcAgentConfig._COMMUNICATE_TIMEOUT_ );
                if (overBuf.length <= 1000){
                    logger.debug("receiveFWF overBuf (1,000 byte in) = ["+ new String(overBuf) +"]");
                }else{
                	logger.debug("receiveFWF overBuf (1,000 byte over)= ["+ new String(overBuf, 0, 900) +".....]");
                }                
                
                bout.write( overBuf );
                bout.close();
            }
            else {

                byte[] hBuf = new byte[FwtpHeader._HEADER_SIZE];
                in.read(hBuf, 0, FwtpHeader._HEADER_SIZE);
                //logger.debug(">>>  hBuf = ["+ new String(hBuf) +"]");

                // METP Protocol 확인
                if (hBuf[0] != FwtpHeader._C_INITIAL.getBytes()[0] || hBuf[1] != FwtpHeader._C_INITIAL.getBytes()[1] || 
                    hBuf[2] != FwtpHeader._C_INITIAL.getBytes()[2] || hBuf[3] != FwtpHeader._C_INITIAL.getBytes()[3]) {
                    throw new AgentException(FixedMsg._MSG_SOKT_K0007);
                }

                // Content length 찾기
                int nStart = FwtpHeader.FWTP_HEADER_SIZE[FwtpHeader.__INITIAL];
                int nEnd = FwtpHeader.FWTP_HEADER_SIZE[FwtpHeader.__INITIAL] + FwtpHeader.FWTP_HEADER_SIZE[FwtpHeader.__DATA_LEN];
                StringBuilder contentLength = new StringBuilder();
                for (int i=nStart; i<nEnd; i++) {
                    contentLength.append(new String(new byte[]{ hBuf[i] }));
                }
                bout.write(hBuf);

                logger.debug(">>>>>  contentLength = ["+ contentLength.toString() +"]");
                int lenContent = Integer.parseInt(contentLength.toString().trim());

                // Data 영역 수신 대기
                while (in.available() < lenContent) {
                    int len = in.available();
                    byte[] dBuf = new byte[len];
                    in.read(dBuf, 0, len);
                    bout.write(dBuf);

                    lenContent -= len;
                }
                byte[] dBuf = new byte[lenContent];
                in.read(dBuf, 0, lenContent);

                if (dBuf.length <= 1000){
                    logger.debug("receiveFWF dataBuf (1,000 byte in) len =["+dBuf.length+"] data= ["+ new String(dBuf) +"]");
                }else{
                	logger.debug("receiveFWF dataBuf (1,000 byte over) len =["+dBuf.length+"] data= ["+ new String(dBuf, 0, 900) +".....]");
                }                
                
                bout.write(dBuf);
                bout.close();
            }
        }
        catch (Exception ioe) {
            throw new AgentException(FixedMsg._MSG_SOKT_K0006, ioe);
        }

        return bout.toString();
    }
    /* ================================================================================================================= */
    
    
    /* =====[ HERMES-FBK => FWF-Core 호출 : 25005 ]============================================================================================= */    
    /**
     * 서버에 데이터를 보내고 그 결과를 받는다.
     * @param fwmsg
     * @return 요청결과 수신 문자열
     * @throws AgentException
     */
    public String communicateFwtpByHms(FwtpMessage fwmsg) throws AgentException {
        sendFwtpByHms ( fwmsg );
        return receiveFWF();
    }
    public void communiFwtpJustSendByHms(FwtpMessage fwmsg) throws AgentException {
        sendFwtpByHms ( fwmsg );
    }

    /**
     * FWTP프로토콜 형태로 서버에 데이터를 보낸다.
     * @param fwmsg
     * @throws AgentException
     */
    public void sendFwtpByHms(FwtpMessage fwmsg) throws AgentException {
        BufferedOutputStream out = null;
        try {
            
            if (fwmsg.toString().getBytes().length <= 1000){
                logger.debug("(sendFwtp)FwtpMessage (1,000 byte in) = ["+ fwmsg.toString() +"]");
            }else{
            	logger.debug("(sendFwtp)FwtpMessage (1,000 byte over)= ["+ new String(fwmsg.toString().getBytes(), 0, 900) +".....]");
            }
            
            socket.setSoTimeout(_sendingTimeout_);
            out = new BufferedOutputStream(socket.getOutputStream());

            String strMessage = fwmsg.buildRequestMessage();
            
            logger.debug("*****************************************************");
            logger.debug("*** [ 내부 서비스 ] ["+ host +":"+ port +"]       ***");
            logger.debug("*** [ HERMES-FBK => FWF-CORE ] sendFwtp           ***");
            logger.debug("*****************************************************");
            if (strMessage.toString().getBytes().length <= 1000){
                logger.debug("sendMsg (1,000 byte in) = ["+ strMessage.toString() +"]");
            }else{
            	logger.debug("sendMsg (1,000 byte over)= ["+ new String(strMessage.toString().getBytes(), 0, 900) +".....]");
            }
            logger.debug("*****************************************************");            
            
            out.write( strMessage.toString().getBytes() );
            out.flush();
        }
        catch (IOException ioe) {
            throw new AgentException(FixedMsg._MSG_SOKT_K0003, ioe);
        }
    }

    /**
     * FWTP프로토콜 형태로 서버로 부터 데이터를 수신한다.
     * @return
     * @throws AgentException
     */
    public String receiveFWFByHms() throws AgentException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        BufferedInputStream in = null;      
        try {
            socket.setSoTimeout(_uxuiRecvTimeout_);
            in = new BufferedInputStream(socket.getInputStream());

            // 헤더 분석
            int nCount = 0;
            while (in.available() < FwtpHeader._HEADER_SIZE) { if(++nCount>_uxuiSleepCount_) break; Thread.sleep(_recvSleepTimes_); };

            int nSize = 4;
            if (nCount >= _uxuiSleepCount_) {
                nSize = in.available();
                byte[] overBuf = new byte[nSize];
                in.read( overBuf, 0, nSize );

                logger.debug(">>> receiveFWFByHms ["+ host +":"+ port +"], length: "+ overBuf.length +", timeout: "+ FcAgentConfig._COMMUNICATE_TIMEOUT_ );
                if (overBuf.length <= 1000){
                    logger.debug("receiveFWFByHms overBuf (1,000 byte in) = ["+ new String(overBuf) +"]");
                }else{
                	logger.debug("receiveFWFByHms overBuf (1,000 byte over)= ["+ new String(overBuf, 0, 900) +".....]");
                }                
                
                bout.write( overBuf );
                bout.close();
            }
            else {

                byte[] hBuf = new byte[FwtpHeader._HEADER_SIZE];
                in.read(hBuf, 0, FwtpHeader._HEADER_SIZE);
                //logger.debug(">>>  hBuf = ["+ new String(hBuf) +"]");

                // METP Protocol 확인
                if (hBuf[0] != FwtpHeader._C_INITIAL.getBytes()[0] || hBuf[1] != FwtpHeader._C_INITIAL.getBytes()[1] || 
                    hBuf[2] != FwtpHeader._C_INITIAL.getBytes()[2] || hBuf[3] != FwtpHeader._C_INITIAL.getBytes()[3]) {
                    throw new AgentException(FixedMsg._MSG_SOKT_K0007);
                }

                // Content length 찾기
                int nStart = FwtpHeader.FWTP_HEADER_SIZE[FwtpHeader.__INITIAL];
                int nEnd = FwtpHeader.FWTP_HEADER_SIZE[FwtpHeader.__INITIAL] + FwtpHeader.FWTP_HEADER_SIZE[FwtpHeader.__DATA_LEN];
                StringBuilder contentLength = new StringBuilder();
                for (int i=nStart; i<nEnd; i++) {
                    contentLength.append(new String(new byte[]{ hBuf[i] }));
                }
                bout.write(hBuf);

                logger.debug(">>>>>  contentLength = ["+ contentLength.toString() +"]");
                int lenContent = Integer.parseInt(contentLength.toString().trim());

                // Data 영역 수신 대기
                while (in.available() < lenContent) {
                    int len = in.available();
                    byte[] dBuf = new byte[len];
                    in.read(dBuf, 0, len);
                    bout.write(dBuf);

                    lenContent -= len;
                }
                byte[] dBuf = new byte[lenContent];
                in.read(dBuf, 0, lenContent);

                if (dBuf.length <= 1000){
                    logger.debug("receiveFWFByHms dataBuf (1,000 byte in) len =["+dBuf.length+"] data= ["+ new String(dBuf) +"]");
                }else{
                	logger.debug("receiveFWFByHms dataBuf (1,000 byte over) len =["+dBuf.length+"] data= ["+ new String(dBuf, 0, 900) +".....]");
                }                
                
                bout.write(dBuf);
                bout.close();
            }
        }
        catch (Exception ioe) {
            throw new AgentException(FixedMsg._MSG_SOKT_K0006, ioe);
        }

        return bout.toString();
    }
    /* ================================================================================================================= */
        
    
    
    /* =====[ FWF-Core => HERMES-FBK 호출 : 25007 ]============================================================================================= */
    /**
     * 호스트(HERMES-GFBK)에 데이터를 보내고 그 결과를 받는다. for Hermes-GFBK!
     * @param hermes
     * @return
     * @throws AgentException
     */
    public String communicateHermes(HermesMessage hermes) throws AgentException {
        sendHermes ( hermes );
        return receiveHermes();
    }

    /**
     * 스트링 문자열 형태로 호스트에 데이터를 보낸다. for Hermes-GFBK!
     * @param hermes
     * @throws AgentException
     */
    public void sendHermes(HermesMessage hermes) throws AgentException {
        BufferedOutputStream out = null;
        try {
        	
            if (hermes.toString().getBytes().length <= 1000){
                logger.debug("(sendHermes)HermesMessage (1,000 byte in) = ["+ hermes.toString() +"]");
            }else{
            	logger.debug("(sendHermes)HermesMessage (1,000 byte over)= ["+ new String(hermes.toString().getBytes(), 0, 900) +".....]");
            }
        	
            socket.setSoTimeout(_sendingTimeout_);
            out = new BufferedOutputStream(socket.getOutputStream());

            String _messageText = hermes.buildMessageByHermes();
            StringBuilder sendMsg = new StringBuilder();
            sendMsg.append( AgentUtil.fillNumericString(String.valueOf(_messageText.getBytes().length), HermesMessage._SizeOfMessage) );
            sendMsg.append( _messageText );

            logger.debug("*****************************************************");
            logger.debug("*** [ 중계전문 서비스 ] ["+ host +":"+ port +"]   ***");
            logger.debug("*** [ FWF-CORE => Hermes-FBK ] sendHermes         ***");
            logger.debug("*****************************************************");
            if (sendMsg.toString().getBytes().length <= 1000){
                logger.debug("sendMsg (1,000 byte in) = ["+ sendMsg.toString() +"]");
            }else{
            	logger.debug("sendMsg (1,000 byte over)= ["+ new String(sendMsg.toString().getBytes(), 0, 900) +".....]");
            }
            logger.debug("*****************************************************");            
            
            out.write( sendMsg.toString().getBytes() );
            out.flush();
        }
        catch (IOException ioe) {
            throw new AgentException(FixedMsg._MSG_SOKT_K0003, ioe);
        }
    }

    /**
     * 스트링 문자열 형태의 데이터를 호스트로 부터 수신한다. for Hermes-GFBK!
     * @return
     * @throws AgentException
     */
    public String receiveHermes() throws AgentException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        BufferedInputStream in = null;      
        try {
            socket.setSoTimeout(_applRecvTimeout_);
            in = new BufferedInputStream(socket.getInputStream());

            // 헤더 분석
            int nCount = 0;
            while (in.available() < 4) { if(++nCount>_applSleepCount_) break; Thread.sleep(_recvSleepTimes_); };

            int nSize = 4;
            if (nCount >= _applSleepCount_) {
                nSize = in.available();
                byte[] overBuf = new byte[nSize];
                in.read( overBuf, 0, nSize );

                logger.debug(">>> receiveFWF ["+ host +":"+ port +"], length: "+ overBuf.length +", timeout: "+ FcAgentConfig._COMMUNICATE_TIMEOUT_ );
                if (overBuf.length <= 1000){
                    logger.debug("receiveFWF overBuf (1,000 byte in) = ["+ new String(overBuf) +"]");
                }else{
                	logger.debug("receiveFWF overBuf (1,000 byte over)= ["+ new String(overBuf, 0, 900) +".....]");
                }                
                
                bout.write( overBuf );
                bout.close();
            }
            else {
                byte[] sizeBuf = new byte[nSize];
                in.read( sizeBuf, 0, nSize );
                logger.debug(">>>  sizeBuf = ["+ new String(sizeBuf) +"]");
                int lenContent = Integer.parseInt(new String(sizeBuf).trim());

                // Data 영역 수신 대기
                while (in.available() < lenContent) {
                    int len = in.available();
                    byte[] dataBuf = new byte[len];
                    in.read(dataBuf, 0, len);
                    bout.write(dataBuf);
                    lenContent -= len;
                }
                byte[] dataBuf = new byte[lenContent];
                in.read(dataBuf, 0, lenContent);
                
                if (dataBuf.length <= 1000){
                    logger.debug("receiveHermes dataBuf (1,000 byte in) len =["+dataBuf.length+"] data= ["+ new String(dataBuf) +"]");
                }else{
                	logger.debug("receiveHermes dataBuf (1,000 byte over) len =["+dataBuf.length+"] data= ["+ new String(dataBuf, 0, 900) +".....]");
                }                
                
                bout.write( dataBuf );
                bout.close();
            }
        }
        catch (Exception ioe) {
            throw new AgentException(FixedMsg._MSG_SOKT_K0006, ioe);
        }

        return bout.toString();
    }
    /* ================================================================================================================= */

    
    
    
    /* =====[ HERMES-FBK => HERMES-Relay : 25041 ]=================================================================== */
    /**
     * 외부 호스트에 데이터를 보내고 그 결과를 받는다. for Outside(FIRM/VAN)! and Relay.
     * @param outside
     * @return
     * @throws AgentException
     */
    public String communicateHms2Rly(HermesMessage outside) throws AgentException {
        sendHms2Rly ( outside );
        return receiveHms2Rly( outside );
    }

    /**
     * 스트링 문자열 형태로 외부 호스트에 데이터를 보낸다. for Outside(FIRM/VAN)! to Relay.
     * @param outside
     * @throws AgentException
     */
    public void sendHms2Rly(HermesMessage outside) throws AgentException {
        BufferedOutputStream out = null;
        try {
        	
            if (outside.toString().getBytes().length <= 1000){
                logger.debug("(sendHms2Rly)HermesMessage (1,000 byte in) = ["+ outside.toString() +"]");
            }else{
            	logger.debug("(sendHms2Rly)HermesMessage (1,000 byte over)= ["+ new String(outside.toString().getBytes(), 0, 900) +".....]");
            }
        	
            socket.setSoTimeout(_sendingTimeout_);
            out = new BufferedOutputStream(socket.getOutputStream());

            String _messageText = outside.buildMessageByOutside();
            StringBuilder sendMsg = new StringBuilder();
            sendMsg.append( AgentUtil.fillNumericString(String.valueOf(_messageText.getBytes().length), 4) );
            sendMsg.append( _messageText );

            logger.debug("*****************************************************");
            logger.debug("*** [ 실시간송신 서비스 ] ["+ host +":"+ port +"] ***");
            logger.debug("*** [ Hermes-FBK => Hermes-Relay ] sendHms2Rly    ***");
            logger.debug("*****************************************************");
            if (sendMsg.toString().getBytes().length <= 1000){
                logger.debug("sendMsg (1,000 byte in) = ["+ sendMsg.toString() +"]");
            }else{
            	logger.debug("sendMsg (1,000 byte over)= ["+ new String(sendMsg.toString().getBytes(), 0, 900) +".....]");
            }
            logger.debug("***************************************************");            
            
          
            out.write( sendMsg.toString().getBytes() );
            out.flush();
            
            logger.debug("****************************************************************");
            logger.debug("********     sendHms2Rly END (Data send)         ***************");
            logger.debug("****************************************************************");
            
        }
        catch (IOException ioe) {
            throw new AgentException(FixedMsg._MSG_SOKT_K0003, ioe);
        }
    }
    
    /**
     * 스트링 문자열 형태의 데이터를 외부 호스트로 부터 수신한다. for Outside(FIRM/VAN)! to Relay.
     * @return
     * @throws AgentException
     */
    public String receiveHms2Rly(HermesMessage outside) throws AgentException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        BufferedInputStream in = null;      
        try {
            socket.setSoTimeout(_applRecvTimeout_);
            in = new BufferedInputStream(socket.getInputStream());
            
            // 헤더 분석
            int nCount = 0;
            while (in.available() < 4) { if(++nCount>_applSleepCount_) break; Thread.sleep(_recvSleepTimes_); };

            logger.debug("*****************************************************");
            logger.debug("*** [ 실시간송신 서비스 응답처리 ]                ***");
            logger.debug("*** [ Hermes-FBK => Hermes-Relay ] receiveHms2Rly ***");
            logger.debug("*****************************************************");
                        
    	    //암복호화 처리 확인(2015-03-18 By Hong)
            //암호화처리 구분이 true 인 경우 암호화는 [길이(4)+전문)]를 전체 암호화 한 후 [길이+암호화[길이(4)+전문)]] 변환한다.
            //암호화처리 구분이 true 인 경우 복호화는 [길이(4)+암호화전문)]를 길이만큼 가져온 후  [복호화[길이(4)+전문)]] 변환한다.            
    	    
    	    	
	    	//전문길이 앞 4자리
            int nSize = 4;
            if (nCount >= _applSleepCount_) {
                nSize = in.available();
                byte[] overBuf = new byte[nSize];
                in.read( overBuf, 0, nSize );
                
                logger.debug(">>> receiveHms2Rly ["+ host +":"+ port +"], length: "+ overBuf.length +", timeout: "+ FcAgentConfig._COMMUNICATE_TIMEOUT_ );
                if (overBuf.length <= 1000){
                    logger.debug("receiveHms2Rly overBuf (1,000 byte in) = ["+ new String(overBuf) +"]");
                }else{
                	logger.debug("receiveHms2Rly overBuf (1,000 byte over) = ["+ new String(overBuf, 0, 900) +".....]");
                }                
                
                bout.write( overBuf );
                bout.close();
            }
            else {
                byte[] sizeBuf = new byte[nSize];
                in.read( sizeBuf, 0, nSize );
                
                logger.debug(">>>  sizeBuf = ["+ new String(sizeBuf) +"]");
                
                int lenContent = Integer.parseInt(new String(sizeBuf).trim());

                // Data 영역 수신 대기
                while (in.available() < lenContent) {
                    int len = in.available();
                    byte[] dataBuf = new byte[len];
                    in.read(dataBuf, 0, len);
                    bout.write(dataBuf);
                    lenContent -= len;
                }
                byte[] dataBuf = new byte[lenContent];
                in.read(dataBuf, 0, lenContent);
                
                if (dataBuf.length <= 1000){
                    logger.debug("receiveHms2Rly dataBuf (1,000 byte in) len =["+dataBuf.length+"] data= ["+ new String(dataBuf) +"]");
                }else{
                	logger.debug("receiveHms2Rly dataBuf (1,000 byte over) len =["+dataBuf.length+"] data= ["+ new String(dataBuf, 0, 900) +".....]");
                }                

                bout.write( dataBuf );					
                bout.close();
            }

    
    	    
        }
        catch (Exception ioe) {
            throw new AgentException(FixedMsg._MSG_SOKT_K0006, ioe);
        }

        return bout.toString();
    }
    /* ================================================================================================================= */

    /* =====[ HERMES-RELAY => VAN : 9238 ]================================================================== */
    /**
     * 대상 호스트에 데이터를 보내고 그 결과를 받는다. for Hermes-Relay!
     * @param relayData
     * @return
     * @throws AgentException
     */
    public String communicateRelayTo(String relayData, int messageSize) throws AgentException {
        sendRelayTo ( relayData, messageSize );
        return receiveRelayTo( messageSize );
    }

    /**
     * 수신 데이터를 문자열 그대로 호스트에 보낸다. for Hermes-Relay!
     * @param relayData
     * @throws AgentException
     */
    public void sendRelayTo(String relayData, int messageSize) throws AgentException {
        BufferedOutputStream out = null;
        try {
            
            if (relayData.getBytes().length <= 1000){
                logger.debug("(sendRelayTo)relayData (1,000 byte in) = ["+ relayData +"]");
            }else{
            	logger.debug("(sendRelayTo)relayData (1,000 byte over)= ["+ new String(relayData.getBytes(), 0, 900) +".....]");
            }
        	
        	socket.setSoTimeout(_sendingTimeout_);
            out = new BufferedOutputStream(socket.getOutputStream());
            
            StringBuilder sendMsg = new StringBuilder();
            if ( messageSize < 1 ){
            	sendMsg.append( AgentUtil.fillNumericString(String.valueOf(relayData.getBytes().length), 4) );
            }            
            sendMsg.append( relayData );
            
            logger.debug("*****************************************************");
            logger.debug("*** [ 실시간송신 서비스 ] ["+ host +":"+ port +"] ***");
            logger.debug("*** [ Hermes-Relay => VAN ] sendRelayTo           ***");
            logger.debug("*****************************************************");
            if (sendMsg.toString().getBytes().length <= 1000){
                logger.debug("sendMsg (1,000 byte in) = ["+ sendMsg.toString() +"]");
            }else{
            	logger.debug("sendMsg (1,000 byte over)= ["+ new String(sendMsg.toString().getBytes(), 0, 900) +".....]");
            }
            logger.debug("*****************************************************");            

            out.write( sendMsg.toString().getBytes() );
            out.flush();
            
            logger.debug("****************************************************************");
            logger.debug("***  sendRelayTo END (Data send) VAN 로 보냄                ****");
            logger.debug("****************************************************************");

        }
        catch (IOException ioe) {
            throw new AgentException(FixedMsg._MSG_SOKT_K0003, ioe);
        }
    }

    /**
     * RELAY프로토콜 형태로 호스트로 부터 데이터를 수신한다.. for Hermes-Relay!
     * @return
     * @throws AgentException
     */
    public String receiveRelayTo(int messageSize) throws AgentException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        BufferedInputStream in = null;      
        try {
            socket.setSoTimeout(_applRecvTimeout_);
            in = new BufferedInputStream(socket.getInputStream());

            // 헤더 분석
            int nSize = (messageSize > 0) ? messageSize : 4;
            int nCount = 0;
            while (in.available() < nSize) { if(++nCount>_applSleepCount_) break; Thread.sleep(_recvSleepTimes_); };

            logger.debug("*****************************************************");
            logger.debug("*** [ 실시간송신 서비스 응답처리 ]                ***");
            logger.debug("*** [ Hermes-Relay => VAN ] receiveRelayTo        ***");
            logger.debug("*****************************************************");
            
            if (nCount >= _applSleepCount_) {
                nSize = in.available();
                byte[] overBuf = new byte[nSize];
                in.read( overBuf, 0, nSize );
                
                logger.debug(">>> receiveRelayTo ["+ host +":"+ port +"], length: "+ overBuf.length +", timeout: "+ FcAgentConfig._COMMUNICATE_TIMEOUT_ );
                if (overBuf.length <= 1000){
                    logger.debug("receiveRelayTo overBuf (1,000 byte in) = ["+ new String(overBuf) +"]");
                }else{
                	logger.debug("receiveRelayTo overBuf (1,000 byte over) = ["+ new String(overBuf, 0, 900) +".....]");
                }                
                
                bout.write( overBuf );
                bout.close();
            }
            else {
                if (messageSize > 0) {
                    byte[] dataBuf = new byte[messageSize];
                    in.read(dataBuf, 0, messageSize);
                    logger.debug(">>>  dataBuf = ["+ new String(dataBuf) +"], len:"+ dataBuf.length);
                    bout.write( dataBuf );
                    bout.close();
                }
                else {
                    byte[] sizeBuf = new byte[nSize];
                    in.read( sizeBuf, 0, nSize );
                    int lenContent = Integer.parseInt(new String(sizeBuf).trim());
                    logger.debug(">>>  sizeBuf = ["+ new String(sizeBuf) +"]");

                    // Data 영역 수신 대기
                    while (in.available() < lenContent) {
                        int len = in.available();
                        byte[] dataBuf = new byte[len];
                        in.read(dataBuf, 0, len);
                        bout.write(dataBuf);
                        lenContent -= len;
                    }
                    byte[] dataBuf = new byte[lenContent];
                    in.read(dataBuf, 0, lenContent);
                    
                    if (dataBuf.length <= 1000){
                        logger.debug("receiveRelayTo dataBuf (1,000 byte in) len =["+dataBuf.length+"] data= ["+ new String(dataBuf) +"]");
                    }else{
                    	logger.debug("receiveRelayTo dataBuf (1,000 byte over) len =["+dataBuf.length+"] data= ["+ new String(dataBuf, 0, 900) +".....]");
                    }                

                    bout.write( dataBuf );
                    bout.close();
                }
            }
        }
        catch (Exception ioe) {
            throw new AgentException(FixedMsg._MSG_SOKT_K0006, ioe);
        }

        return bout.toString();
    }    

    /* =====[ HERMES-RELAY => HERMES-FBK : 25017 ]================================================================== */
    /**
     * 대상 호스트에 데이터를 보내고 그 결과를 받는다. for Hermes-Relay!
     * @param relayData
     * @return
     * @throws AgentException
     */
    public String communicateOutRelayTo(String relayData, int messageSize) throws AgentException {
        sendOutRelayTo ( relayData, messageSize );
        return receiveOutRelayTo();
    }

    /**
     * 수신 데이터를 문자열 그대로 호스트에 보낸다. for Hermes-Relay!
     * @param relayData
     * @throws AgentException
     */
    public void sendOutRelayTo(String relayData, int messageSize) throws AgentException {
        BufferedOutputStream out = null;
        try {
        	
            if (relayData.getBytes().length <= 1000){
                logger.debug("(sendOutRelayTo)relayData (1,000 byte in) = ["+ relayData +"]");
            }else{
            	logger.debug("(sendOutRelayTo)relayData (1,000 byte over)= ["+ new String(relayData.getBytes(), 0, 900) +".....]");
            }
        	
            socket.setSoTimeout(_sendingTimeout_);
            out = new BufferedOutputStream(socket.getOutputStream());
            
            StringBuilder sendMsg = new StringBuilder();
            if ( messageSize < 1 ){
            	sendMsg.append( AgentUtil.fillNumericString(String.valueOf(relayData.getBytes().length), 4) );
            }
            sendMsg.append( relayData );

            logger.debug("*****************************************************");
            logger.debug("*** [ 통지서비스 ] ["+ host +":"+ port +"]        ***");
            logger.debug("*** [ Hermes-Relay => HERMES-FBK ] sendOutRelayTo ***");
            logger.debug("*****************************************************");
            if (sendMsg.toString().getBytes().length <= 1000){
                logger.debug("sendMsg (1,000 byte in) = ["+ sendMsg.toString() +"]");
            }else{
            	logger.debug("sendMsg (1,000 byte over)= ["+ new String(sendMsg.toString().getBytes(), 0, 900) +".....]");
            }
            logger.debug("*****************************************************");            
            
	            out.write( sendMsg.toString().getBytes() );
	            out.flush();
            
            logger.debug("****************************************************************");
            logger.debug("***  sendOutRelayTo END (Data send) HERMES-FBK 로 보냄      ****");
            logger.debug("****************************************************************");

        }
        catch (IOException ioe) {
            throw new AgentException(FixedMsg._MSG_SOKT_K0003, ioe);
        }
    }

    /**
     * RELAY프로토콜 형태로 호스트로 부터 데이터를 수신한다.. for Hermes-Relay!
     * @return
     * @throws AgentException
     */
    public String receiveOutRelayTo() throws AgentException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        BufferedInputStream in = null;      
        try {
            socket.setSoTimeout(_applRecvTimeout_);
            in = new BufferedInputStream(socket.getInputStream());

            // 헤더 분석
            int nSize =  4;
            int nCount = 0;
            while (in.available() < nSize) { if(++nCount>_applSleepCount_) break; Thread.sleep(_recvSleepTimes_); };

            logger.debug("********************************************************");
            logger.debug("*** [ 통지서비스 응답처리]                           ***");
            logger.debug("*** [ Hermes-Relay => HERMES-FBK ] receiveOutRelayTo ***");
            logger.debug("********************************************************");
                       
            
	            if (nCount >= _applSleepCount_) {
	                nSize = in.available();
	                byte[] overBuf = new byte[nSize];
	                in.read( overBuf, 0, nSize );

	                logger.debug(">>> receiveOutRelayTo ["+ host +":"+ port +"], length: "+ overBuf.length +", timeout: "+ FcAgentConfig._COMMUNICATE_TIMEOUT_ );
	                if (overBuf.length <= 1000){
	                    logger.debug("receiveOutRelayTo overBuf (1,000 byte in) = ["+ new String(overBuf) +"]");
	                }else{
	                	logger.debug("receiveOutRelayTo overBuf (1,000 byte over) = ["+ new String(overBuf, 0, 900) +".....]");
	                }                
	                
	                bout.write( overBuf );
	                bout.close();
	            }
	            else {
                    byte[] sizeBuf = new byte[nSize];
                    in.read( sizeBuf, 0, nSize );
                    int lenContent = Integer.parseInt(new String(sizeBuf).trim());
                    logger.debug(">>>  sizeBuf = ["+ new String(sizeBuf) +"]");

                    // Data 영역 수신 대기
                    while (in.available() < lenContent) {
                        int len = in.available();
                        byte[] dataBuf = new byte[len];
                        in.read(dataBuf, 0, len);
                        bout.write(dataBuf);
                        lenContent -= len;
                    }
                    byte[] dataBuf = new byte[lenContent];
                    in.read(dataBuf, 0, lenContent);
                    
                    if (dataBuf.length <= 1000){
                        logger.debug("receiveOutRelayTo dataBuf (1,000 byte in) len =["+dataBuf.length+"] data= ["+ new String(dataBuf) +"]");
                    }else{
                    	logger.debug("receiveOutRelayTo dataBuf (1,000 byte over) len =["+dataBuf.length+"] data= ["+ new String(dataBuf, 0, 900) +".....]");
                    }                
                    
                    bout.write( dataBuf );
                    bout.close();
	            }
    	    
        }
        catch (Exception ioe) {
            throw new AgentException(FixedMsg._MSG_SOKT_K0006, ioe);
        }

        return bout.toString();
    }    

    
    /* =====[ HERMES-RELAY => HERMES-FBK : 25015 ]================================================================== */
    /**
     * 대상 호스트에 데이터를 보내고 그 결과를 받는다. for Hermes-Relay!
     * @param relayData
     * @return
     * @throws AgentException
     */

    public String communicateRlyFile2Hms(RelayMessage relay) throws AgentException {
        sendRlyFile2Hms ( relay );
        return receiveRlyFile2Hms();
    }

    
    public void sendRlyFile2Hms(RelayMessage relay) throws AgentException {
        BufferedOutputStream out = null;
        try {
        	
            if (relay.toString().getBytes().length <= 1000){
                logger.debug("(sendRlyFile2Hms)RelayMessage (1,000 byte in) = ["+ relay.toString() +"]");
            }else{
            	logger.debug("(sendRlyFile2Hms)RelayMessage (1,000 byte over)= ["+ new String(relay.toString().getBytes(), 0, 900) +".....]");
            }
        	
            socket.setSoTimeout(_sendingTimeout_);
            out = new BufferedOutputStream(socket.getOutputStream());
            
            StringBuilder sendMsg = new StringBuilder();
            sendMsg.append( AgentUtil.fillNumericString(String.valueOf(relay.getResponseString().getBytes().length), HermesMessage._SizeOfMessage) );
            sendMsg.append( relay.getResponseString() );

            logger.debug("**********************************************************");
            logger.debug("*** [ 파일(법인카드/대량이체 ] ["+ host +":"+ port +"] ***");
            logger.debug("*** [ Hermes-Relay => HERMES-FBK ] sendRlyFile2Hms     ***");
            logger.debug("**********************************************************");
            if (sendMsg.toString().getBytes().length <= 1000){
                logger.debug("sendMsg (1,000 byte in) = ["+ sendMsg.toString() +"]");
            }else{
            	logger.debug("sendMsg (1,000 byte over)= ["+ new String(sendMsg.toString().getBytes(), 0, 900) +".....]");
            }
            logger.debug("**********************************************************");            
            
            //암복호화 처리 확인(2015-03-18 By Hong)
            //암호화처리 구분이 true 인 경우 암호화는 [길이(9)+전문)]를 전체 암호화 한 후 [길이+암호화[길이(9)+전문)]] 변환한다.
            //암호화처리 구분이 true 인 경우 복호화는 [길이(9)+암호화전문)]를 길이만큼 가져온 후  [복호화[길이(9)+전문)]] 변환한다.            
            
                       
	            out.write( sendMsg.toString().getBytes() );
	            out.flush();
            
            logger.debug("****************************************************************");
            logger.debug("**  sendRlyFile2Hms END (Data send) HERMES-FBK 로 보냄       **");
            logger.debug("****************************************************************");
            
        }
        catch (IOException ioe) {
            throw new AgentException(FixedMsg._MSG_SOKT_K0003, ioe);
        }
    }

    /**
     * RELAY프로토콜 형태로 호스트로 부터 데이터를 수신한다.. for Hermes-Relay!
     * @return
     * @throws AgentException
     */
    public String receiveRlyFile2Hms() throws AgentException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        BufferedInputStream in = null;      
        
        try {
            socket.setSoTimeout(_fileRecvTimeout_);
            in = new BufferedInputStream(socket.getInputStream());

            // 헤더 분석
            int nSize = 9;
            int nCount = 0;
            while (in.available() < nSize) { if(++nCount>_fileSleepCount_) break; Thread.sleep(_recvSleepTimes_); };

            logger.debug("*********************************************************");
            logger.debug("*** [ 파일(법인카드/대량이체 서비스 응답처리 ]        ***");
            logger.debug("*** [ Hermes-Relay => HERMES-FBK ] receiveRlyFile2Hms ***");
            logger.debug("*********************************************************");
            
    	
	            if (nCount >= _fileSleepCount_) {
	                nSize = in.available();
	                byte[] overBuf = new byte[nSize];
	                in.read( overBuf, 0, nSize );
	                
	                logger.debug(">>> receiveFWF ["+ host +":"+ port +"], length: "+ overBuf.length +", timeout: "+ FcAgentConfig._COMMUNICATE_TIMEOUT_ );
	                if (overBuf.length <= 1000){
	                    logger.debug("receiveFWF overBuf (1,000 byte in) = ["+ new String(overBuf) +"]");
	                }else{
	                	logger.debug("receiveFWF overBuf (1,000 byte over) = ["+ new String(overBuf, 0, 900) +".....]");
	                }                

	                bout.write( overBuf );
	                bout.close();

	            }
	            else {
                    byte[] sizeBuf = new byte[nSize];
                    in.read( sizeBuf, 0, nSize );
                    int lenContent = Integer.parseInt(new String(sizeBuf).trim());
                    logger.debug(">>>  sizeBuf = ["+ new String(sizeBuf) +"]");

                    // Data 영역 수신 대기
                    while (in.available() < lenContent) {
                        int len = in.available();
                        byte[] dataBuf = new byte[len];
                        in.read(dataBuf, 0, len);
                        bout.write(dataBuf);
                        lenContent -= len;
                    }
                    byte[] dataBuf = new byte[lenContent];
                    in.read(dataBuf, 0, lenContent);
                    
                    if (dataBuf.length <= 1000){
                        logger.debug("receiveRlyFile2Hms dataBuf (1,000 byte in) len =["+dataBuf.length+"] data= ["+ new String(dataBuf) +"]");
                    }else{
                    	logger.debug("receiveRlyFile2Hms dataBuf (1,000 byte over) len =["+dataBuf.length+"] data= ["+ new String(dataBuf, 0, 900) +".....]");
                    }                
                    
                    bout.write( dataBuf );
                    bout.close();
	                
	            }
    	    
        }
        catch (Exception ioe) {
            throw new AgentException(FixedMsg._MSG_SOKT_K0006, ioe);
        }

        return bout.toString();
    }        

    /* ================================================================================================================= */

    /* =====[ HERMES-FBK => HERMES-Relay : 25045 ]=================================================================== */
    /**
     * 중계 호스트에 데이터를 보내고 저장 후 그 결과를 받는다. for Outside(VAN#File)!
     * @param outside
     * @return
     * @throws AgentException
     */
    public String communicateHms2RlyFile(HermesMessage outside) throws AgentException {
        sendHms2RlyFile ( outside );
        return receiveHms2RlyFile();
    }

    /**
     * 스트링 문자열 형태로 중계 호스트에 데이터를 보낸다. for Outside(VAN#File)!
     * @param outside
     * @throws AgentException
     */
    public void sendHms2RlyFile(HermesMessage outside) throws AgentException {
        BufferedOutputStream out = null;
        try {
        	
            if (outside.toString().getBytes().length <= 1000){
                logger.debug("(sendHms2RlyFile)HermesMessage (1,000 byte in) = ["+ outside.toString() +"]");
            }else{
            	logger.debug("(sendHms2RlyFile)HermesMessage (1,000 byte over)= ["+ new String(outside.toString().getBytes(), 0, 900) +".....]");
            }
        	
            socket.setSoTimeout(_sendingTimeout_);
            out = new BufferedOutputStream(socket.getOutputStream());

            String _messageText = outside.buildMessageByRly2File();
            StringBuilder sendMsg = new StringBuilder();
            sendMsg.append( AgentUtil.fillNumericString(String.valueOf(_messageText.getBytes().length), HermesMessage._SizeOfMessage) );
            sendMsg.append( _messageText );

            logger.debug("*********************************************************");
            logger.debug("*** [ 파일(대량이체) 서비스 ] ["+ host +":"+ port +"] ***");
            logger.debug("*** [ Hermes-FBK => Hermes-Relay ] sendHms2RlyFile    ***");
            logger.debug("*********************************************************");
            if (sendMsg.toString().getBytes().length <= 1000){
                logger.debug("sendMsg (1,000 byte in) = ["+ sendMsg.toString() +"]");
            }else{
            	logger.debug("sendMsg (1,000 byte over)= ["+ new String(sendMsg.toString().getBytes(), 0, 900) +".....]");
            }
            logger.debug("*********************************************************");            

 
                out.write( sendMsg.toString().getBytes() );
                out.flush();
            
            logger.debug("****************************************************************");
            logger.debug("********     sendHms2RlyFile END (Data send)     ***************");
            logger.debug("****************************************************************");
            
            
        }
        catch (IOException ioe) {
            throw new AgentException(FixedMsg._MSG_SOKT_K0003, ioe);
        }
    }

    /**
     * 스트링 문자열 형태의 데이터를 중계 호스트로 부터 수신한다. for Outside(VAN#File)!
     * @return
     * @throws AgentException
     */
    public String receiveHms2RlyFile() throws AgentException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        BufferedInputStream in = null;      
        try {
            socket.setSoTimeout(_fileRecvTimeout_);
            in = new BufferedInputStream(socket.getInputStream());

            // 헤더 분석
            int nCount = 0;
            while (in.available() < 9) { if(++nCount>_fileSleepCount_) break; Thread.sleep(_recvSleepTimes_); };

            logger.debug("*********************************************************");
            logger.debug("*** [ 파일(대량이체) 서비스 응답처리 ]                ***");
            logger.debug("*** [ Hermes-FBK => Hermes-Relay ] receiveHms2RlyFile ***");
            logger.debug("*********************************************************");
            
    	    
    	    	
    	    	//전문길이 앞 9자리
	            int nSize = 9;
	            if (nCount >= _fileSleepCount_) {
	                nSize = in.available();
	                byte[] overBuf = new byte[nSize];
	                in.read( overBuf, 0, nSize );
	                
	                logger.debug(">>> receiveHms2RlyFile ["+ host +":"+ port +"], length: "+ overBuf.length +", timeout: "+ FcAgentConfig._COMMUNICATE_TIMEOUT_ );
	                if (overBuf.length <= 1000){
	                    logger.debug("receiveHms2RlyFile overBuf (1,000 byte in) = ["+ new String(overBuf) +"]");
	                }else{
	                	logger.debug("receiveHms2RlyFile overBuf (1,000 byte over) = ["+ new String(overBuf, 0, 900) +".....]");
	                }                
	                
	                bout.write( overBuf );
	                bout.close();
	            }
	            else {
	                byte[] sizeBuf = new byte[nSize];
	                in.read( sizeBuf, 0, nSize );
	                int lenContent = Integer.parseInt(new String(sizeBuf).trim());
	                logger.debug(">>>  sizeBuf = ["+ new String(sizeBuf) +"]");
	
	                // Data 영역 수신 대기
	                while (in.available() < lenContent) {
	                    int len = in.available();
	                    byte[] dataBuf = new byte[len];
	                    in.read(dataBuf, 0, len);
	                    bout.write(dataBuf);
	                    lenContent -= len;
	                }
	                byte[] dataBuf = new byte[lenContent];
	                in.read(dataBuf, 0, lenContent);
	                
	                if (dataBuf.length <= 1000){
	                    logger.debug("receiveHms2RlyFile dataBuf (1,000 byte in) len =["+dataBuf.length+"] data= ["+ new String(dataBuf) +"]");
	                }else{
	                	logger.debug("receiveHms2RlyFile dataBuf (1,000 byte over) len =["+dataBuf.length+"] data= ["+ new String(dataBuf, 0, 900) +".....]");
	                }                
	                
	                logger.debug(">>>  dataBuf = ["+ new String(dataBuf) +"] len:"+ dataBuf.length);
	                bout.write( dataBuf );
	                bout.close();
	            }
            
        }
        catch (Exception ioe) {
            throw new AgentException(FixedMsg._MSG_SOKT_K0006, ioe);
        }

        return bout.toString();
    }
    /* ================================================================================================================= */

    
    /**
     * 전문 String 문자열을 암호화 한다. (2015-03-18)
     * @return
     */
    public String encryptMessage(String orgData) {

    	String cryptoData = new String("");
    	try {
    		
    		logger.debug("========================================================================");
    		logger.debug("= 암호화 Orignal Data Length =["+ orgData.getBytes().length +"] ");
    		logger.debug("========================================================================");
    		logger.debug("= 암호화 Orignal Data=["+orgData+"]");
    		logger.debug("========================================================================");
    		
			cryptoData = Crypto.encryptSD(orgData);
			
    		logger.debug("========================================================================");
    		logger.debug("= 암호화 Crypt Data Length =["+ cryptoData.getBytes().length +"] ");
    		logger.debug("========================================================================");
			logger.debug("= 암호화 Crypt Data=["+cryptoData+"]");
			logger.debug("========================================================================");

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    	
        return cryptoData;
    }
        
    /**
     * 암호화된 전문 String 문자열을 복호화 한다. (2015-03-18)
     * @return
     */
    public String decryptMessage(String cryptoData) {
    	
    	String orgData = new String("");
    	try {
    		logger.debug("========================================================================");
    		logger.debug("= 복호화 Crypt Data Length =["+ cryptoData.getBytes().length +"] ");
    		logger.debug("========================================================================");
			logger.debug("= 복호화 Crypt Data=["+cryptoData+"]");
			logger.debug("========================================================================");
			
    		orgData = Crypto.decryptSD(cryptoData);
    		
    		logger.debug("========================================================================");
    		logger.debug("= 복호화 Orignal Data Length =["+ orgData.getBytes().length +"] ");
    		logger.debug("========================================================================");
    		logger.debug("= 복호화 Orignal Data=["+orgData+"]");
    		logger.debug("========================================================================");
    		
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    	
        return orgData;
    }

    /**
     * 호스트와의 연결상태를 리턴
     */
    public boolean isConnected() {
        if (socket != null && socket.isConnected() ){
			return true;
		}else{
			return false;
		}
    }
    
    
    /**
     * 호스트와의 연결을 끊는다.
     */
    public void close() throws AgentException {
        try {
            if (socket != null) socket.close();
        } catch (IOException ioe) {
            throw new AgentException(FixedMsg._MSG_SOKT_K0002, ioe);
        }
    }
    
}