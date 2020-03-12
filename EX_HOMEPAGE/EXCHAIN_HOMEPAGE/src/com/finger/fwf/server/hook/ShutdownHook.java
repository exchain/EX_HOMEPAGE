package com.finger.fwf.server.hook;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class ShutdownHook extends Thread {
    private static Logger logger = Logger.getLogger(ShutdownHook.class);

    private NioSocketAcceptor[] nioSocketAcceptor;

    public ShutdownHook() { }

    public ShutdownHook(NioSocketAcceptor... socketAcceptors) {

        nioSocketAcceptor = new NioSocketAcceptor[socketAcceptors.length];

        for (int i = 0; i < socketAcceptors.length; i++) {
            nioSocketAcceptor[i] = socketAcceptors[i];
        }
    }

    public void run() {
        shutdown();
    }

    private void shutdown() {
        logger.info(">>>  Server, Shutdown progress. please wait.....");

        try {
            for (int i = 0; i < nioSocketAcceptor.length; i++) {
                setSessionClose(nioSocketAcceptor[i].getManagedSessions());
            }
            logger.info(">>>  Server, Shutdown complete !!!");
        } catch (Exception e) {
            logger.error("shutdown() Exception ::: "+ e );
        }
    }

    private void setSessionClose(Map<Long, IoSession> map) {
        logger.debug("     Session Size = ["+ map.size() +"], Closing.......");

        Collection<IoSession> col = map.values();
        Iterator<IoSession> iter = col.iterator();

        while (iter.hasNext()) {
            IoSession session = iter.next();
            logger.info("          => RemoteAddress = ["+ session.getRemoteAddress() +"]");
            session.close(true);	//세션을 즉시 종료(보류중인 쓰기 요청은 삭제됨)
        }
    }
}