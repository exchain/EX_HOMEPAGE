package com.finger.agent.scrap.socket;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

public class ClientConnection {
    private Logger LOG = Logger.getLogger(ClientConnection.class);

    /** 서버와 통신하기 위한 소켓 */
    private Socket socket;

    /** 접속할 서버의 호스트 */
    private String host;
    /** 접속할 서버의 포트번호 */
    private int port;
    /** 보내기 제한시간 */
    private int sendTimeout;
    /** 받기 제한시간 */
    private int recvTimeout;

    boolean isAlive = true;

     /** 데이터 전송시 구분자 */
    public static final String[] splitWD  = { "F", "M", "L", "O" };

    /**
     * Socket 커넥션을 만든다.
     *
     * 소켓을 생성하여 서버와 연결한다.
     *
     * @param host 호스트
     * @param port 포트
     * @param sendTimeout 보내기 제한시간
     * @param recvTimeout 받기 제한시간
     * @exception ClientSocketException if connection exception occurs
     */
    public ClientConnection(String host,
                              int port,
                              int sendTimeout,
                              int recvTimeout
                              ) throws ClientSocketException {
        this.host = host;
        this.port = port;
        this.sendTimeout = sendTimeout;
        this.recvTimeout = recvTimeout;
        connect();
    }

    /**
     * 서버에 데이타를 보내고 그 결과를 받는다.
     *
     * @param send sending data
     * @return received data
     * @exception ClientSocketException if send(),receive() throws LGLegacyException exception
     */
    public String communicate(String send) throws ClientSocketException {
        send(send);
        return receive();
    }

    /**
     * 서버에 데이타를 보낸다.
     *
     * @param s Sending data
     * @exception ClientSocketException if IOException occurs
     */
    public void send(String s) throws ClientSocketException {
        BufferedOutputStream out = null;
        try {
LOG.debug("\n===================================================================\n"+ s +"\n===================================================================\n");
            socket.setSoTimeout(sendTimeout);
            out = new BufferedOutputStream(socket.getOutputStream());
            List<byte[]> list = writeSendData(s);
            for(Iterator<byte[]> i = list.iterator();i.hasNext();) {
                byte[] value = (byte[])i.next();
LOG.debug("\n■■"+ new String(value) +"■■");
                out.write(value);
                out.flush();
            }
        } catch (IOException ioe) {
            throw new ClientSocketException(ClientSocketException.SEND_MESSAGE_FAIL, ioe);
        }
    }

    /**
     * Message receive 처리
     * @return
     * @throws ClientSocketException
     */
    private String receive() throws ClientSocketException {

     // System.out.println("><><><><><><>>>>>>>>>>>>>>>>>>>>>>receive");
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        BufferedInputStream in = null;
        int headerSize = 5;
        try {
            socket.setSoTimeout(recvTimeout);
            in = new BufferedInputStream(socket.getInputStream());
            boolean isFirst = false;
            while( true ) {
                //1024F (header 포함 총 length(4자리), 구분자(1자리-O,F,M,L)로 구성
                byte[] buff = new byte[headerSize];
                buff = read_data(in, headerSize);

                String header = new String(buff, 0, headerSize);
                //구분자 자른다
                String rule = header.substring(4,5);
                //총 length 구한다.
                int totalSize = Integer.parseInt(header.substring(0, header.length()-1));
                //총length에 header포함 되어 있으므로 삭감
                int bodySize = totalSize - headerSize;
                byte[] body = new byte[bodySize];

                if ( rule.equals("F") )
                    isFirst = true;
                else if ( rule.equals("M") && !isFirst ) //First없이 M을 받았을때 Exception throw
                    throw new IOException("read message error");
                else if ( rule.equals("L") && !isFirst ) //First없이 L을 받았을때 Exception throw
                    throw new IOException("read message error2");

                //body size 만큼 read
                body = read_data(in, bodySize);

                //byteArray에 write
                bout.write(body, 0, bodySize);

                if ( rule.equals("O") || rule.equals("L") )
                    break;
            }
            bout.close();
        } catch (Exception ioe) {
            LOG.error(ioe.getMessage(), ioe);
            throw new ClientSocketException(ClientSocketException.CREATE_RECEIVE_MESSAGE_FAIL, ioe);
        }
        return bout.toString();
    }

    /**
     * length 만큼 byte 읽는다.
     * @param in
     * @param len
     * @return
     * @throws Exception
     */
    private static byte[] read_data(InputStream in, int len) throws Exception {
        java.io.ByteArrayOutputStream bout = new java.io.ByteArrayOutputStream();
        int bcount = 0;
        byte[] buf = new byte[1024];
        while( bcount < len ) {
          int n = in.read(buf,0, len-bcount < 1024 ? len-bcount : 1024 );
          if (n == -1) break;
          bcount += n;
          bout.write(buf,0,n);
        }
        bout.flush();
        return bout.toByteArray();
    }

    /**
     * 서버와 연결을 맺는다.
     *
     * @exception ClientSocketException if IOException occurs
     */
    private void connect() throws ClientSocketException {
        try {
            socket = new Socket(host, port);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new ClientSocketException(ClientSocketException.CONNECT_FAIL,ioe);
        }
    }


    /**
     * 서버와의 연결을 끊는다.
     */
    public void close() throws ClientSocketException {
        try {
            if (socket != null) socket.close();
        } catch (IOException ioe) {
            throw new ClientSocketException(ClientSocketException.DISCONNECT_FAIL, ioe);
        }
    }

    /**
     * 전송 데이터 header Length 자리수에 맞게 채우기
     * @param buffer
     * @param value
     * @param len
     */
    private void writeHeader(StringBuffer buffer, int value, int len) {
        String s = String.valueOf(value);
        if(s.length() < len) {
            for (int i = 0; i < len-s.length(); i++) {
                buffer.append("0");
            }
            buffer.append(s);
        }
        else
            buffer.append(s);
    }

    /**
     * 전송 데이터 1024 byte size 단위로 나눈다
     * "F : 1024 큰 경우 첫번째 전송시", "M : 1024 보다 큰경우 2번째 이후 전송",
     * "L : 1024 큰 경우 마지막 전송시", "O : 1024 보다 작은경우"
     * @param s
     * @return
     */
    public List<byte[]> writeSendData(String s) {
        List<byte[]> list = new Vector<byte[]>();
        StringBuffer headerData = new StringBuffer();
        int offset = 1024;
        int headerLen = 5;
        byte[] buff = null;

        byte[] data = s.getBytes();
        int totalLen = data.length;
        int pointer = offset - headerLen;

        for (int i=0; totalLen > 0; i++) {
            headerData.delete(0,headerData.length());
            if(totalLen > pointer) {
                buff =  new byte[offset];
                writeHeader(headerData, offset, 4); //데이터 크기 4자리
                if( i == 0)
                    headerData.append( splitWD[0] ); //구분자
                else
                    headerData.append( splitWD[1] ); //구분자
            } else {
                buff =  new byte[totalLen+headerLen];
                headerData.delete(0,headerData.length());
                writeHeader(headerData, totalLen+headerLen, 4); //데이터 크기 4자리
            //    if(totalLen < pointer && i == 0)
                if(totalLen <= pointer && i == 0)
                    headerData.append( splitWD[3] ); //구분자
                else
                    headerData.append( splitWD[2] ); //구분자
            }

            byte[] headers = headerData.toString().getBytes();
            System.arraycopy(headers, 0, buff, 0, headerLen);
            System.arraycopy(data, (pointer)*i, buff, headerLen, totalLen > pointer ? pointer : totalLen);

            list.add(buff);
            totalLen = totalLen - pointer;
        }
        return list;
    }
}
