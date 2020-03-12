package com.finger.agent.scrap.socket;

public abstract class ClientSocket {

    /** 접속할 서버의 호스트IP */
    protected String    hostIp;
    /** 접속할 서버의 포트번호 */
    protected int       portNo;
    /** 보내기 제한시간 */
    protected int       sendTimeout;
    /** 받기 제한시간 */
    protected int       recvTimeout;

    private ClientConnection conn;

    protected ClientSocket() {
        initSocketData();
    }

    /**
     * 소켓관련 데이타를 로드한다.
     *
     * 하위 클래스에서 구현된다. 단 Exception을 throws해선 안된다.
     */
    protected abstract void initSocketData();

    /**
     * 소켓 통신하기 위한 커넥션을 얻는다.
     *
     * @return ConnectionSocket
     * @exception ClientSocketException
     */
    public ClientConnection getConnection() throws ClientSocketException {
        return new ClientConnection(hostIp, portNo, sendTimeout, recvTimeout);
    }

    /**
     * 데이터 전송
     * @param send
     * @return
     * @throws ClientSocketException
     */
    public String communicate(String send) throws ClientSocketException {
        try {
            if(conn == null)
                conn = getConnection();

            return conn.communicate(send);
        } catch (Exception e) {
            throw new ClientSocketException(this.toString(), e);
        } finally {
            if(conn != null) conn.close();
        }
    }

    public void send(String send) throws ClientSocketException {
        try {
            if(conn == null)
                conn = getConnection();
            conn.send(send);
        } catch (Exception e) {
            throw new ClientSocketException(e);
        } finally {
            if(conn != null) conn.close();
        }
    }

    /**
     * 서버 정보.
     */
    public String toString() {
        StringBuffer buff = new StringBuffer();
        buff.append("[Server Info : " + getClass().getName() + "]");
        buff.append(" HostIP: " + hostIp);
        buff.append(" PortNo: " + portNo);
        buff.append(" SendTimeout: " + sendTimeout);
        buff.append(" RecvTimeout: " + recvTimeout);
        return buff.toString();
    }
}