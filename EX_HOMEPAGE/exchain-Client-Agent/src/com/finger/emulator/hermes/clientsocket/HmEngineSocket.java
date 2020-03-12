/*
 * ******************************************************
 * Program Name : @(#)HmEngineSocket.java
 * Function description :
 * Programmer Name :
 * Creation Date :
 *
 * ******************************************************
 */
package com.finger.emulator.hermes.clientsocket;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import org.apache.log4j.Logger;

import com.finger.agent.exception.AgentException;
import com.finger.agent.msg.FixedMsg;

public class HmEngineSocket {
    private static Logger logger = Logger.getLogger(HmEngineSocket.class);

    /** 소켓 정보 */
    private Socket socket;
    private String host;
    private int port;
    private int sendTimeout;
    private int recvTimeout;
    boolean isAlive = true;

    //--private final static int HEADER_SIZE = 200;

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
    public HmEngineSocket(String host,
                              int port,
                              int sendTimeout,
                              int recvTimeout
                              ) throws AgentException {
        this.host = host;
        this.port = port;
        this.sendTimeout = sendTimeout;
        this.recvTimeout = recvTimeout;

        connect();
    }

    /**
     * 서버에 데이타를 보내고 그 결과를 받는다.
     *
     * @param data 전송문자열
     * @return 전송결과 수신 문자열
     * @exception AgentException if send(),receive() throws LGLegacyException exception
     */
    public String communicate(String data) throws AgentException {
        send(data);
        return receive();
    }

    /**
     * DSTP프로토콜 형태로 서버에 데이타를 보낸다.
     * @param     data : sending data
     * @exception AgentException if IOException occurs
     */
    public void send(String data) throws AgentException {
        BufferedOutputStream out = null;
        try {
            socket.setSoTimeout(sendTimeout);
            out = new BufferedOutputStream(socket.getOutputStream());

            out.write(data.getBytes());
            //--out.write(0xFF);
            out.flush();
        }
        catch (IOException ioe) {
            throw new AgentException(FixedMsg._MSG_SOKT_K0003, ioe);
        }
    }

    /**
     * DSTP프로토콜 형태로 서버로 부터 데이타 수신
     * @return 수신문자열
     * @throws AgentException
     */
    public String receive() throws AgentException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        BufferedInputStream in = null;
        try {
            socket.setSoTimeout(recvTimeout);
            in = new BufferedInputStream(socket.getInputStream());

            // 헤더 분석
            int _sleepCount_ = 100;
            int nCount = 0;
            while (in.available() < 4) { if(nCount++>_sleepCount_) break; Thread.sleep(200); };

            int nSize = 4;
            if (nCount >= _sleepCount_) {
                nSize = in.available();
                byte[] overBuf = new byte[nSize];
                in.read( overBuf, 0, nSize );
logger.debug(">>>  overBuf = ["+ new String(overBuf) +"] ### ["+ host +":"+ port +"]");
                bout.write( overBuf );
                bout.close();
            }
            else {
                byte[] sizeBuf = new byte[nSize];
                in.read( sizeBuf, 0, nSize );
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
                bout.write( dataBuf );
                bout.close();
            }
        }
        catch (Exception ioe) {
            throw new AgentException(FixedMsg._MSG_SOKT_K0006, ioe);
        }

        return bout.toString();
    }

    /**
     * 서버와 연결을 맺는다.
     *
     * @exception ClientSocketException if IOException occurs
     */
    private void connect() throws AgentException {
        try {
            SocketAddress socketAddress = new InetSocketAddress(host, port);
            socket = new Socket();
            socket.connect( socketAddress, 5000 );
        } catch (IOException ioe) {
            throw new AgentException(FixedMsg._MSG_SOKT_K0001, ioe);
        }
    }


    /**
     * 서버와의 연결을 끊는다.
     */
    public void close() throws AgentException {
        try {
            if (socket != null) socket.close();
        } catch (IOException ioe) {
            throw new AgentException(FixedMsg._MSG_SOKT_K0002, ioe);
        }
    }
}