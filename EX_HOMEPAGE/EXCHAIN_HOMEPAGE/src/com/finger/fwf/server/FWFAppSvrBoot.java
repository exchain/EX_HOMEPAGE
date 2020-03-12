package com.finger.fwf.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.finger.fwf.core.config.FWFApsConfig;
import com.finger.fwf.core.task.FwfTasksLoader;
import com.finger.fwf.core.task.StartupAction;
import com.finger.fwf.server.codec.FwtpProtocolCodecFactory;
import com.finger.fwf.server.handler.FWFProtocolHandler;
import com.finger.fwf.server.hook.ShutdownHook;

public class FWFAppSvrBoot {
    private static Logger logger = Logger.getLogger(FWFAppSvrBoot.class);

    public static void main(String[] args) {

        FWFApsConfig config = new FWFApsConfig();
        if (!FWFApsConfig.isLoadSuccess()) {
            logger.error("#1#  I have not read the property information. Abnormal program exit.");
            System.exit(1);
        }
        config.printInfomation();


        FwfTasksLoader.loadTransMap();


        StartupAction startup = new StartupAction();
        startup.init();

        try {
            startup.execute(null, null);
        } catch (Exception e) {
            logger.error("#2#  Startup, Abnormal program exit !!", e);
            System.exit(2);
        }


        // Client Protocol Handler
        NioSocketAcceptor acceptorClient = new NioSocketAcceptor();

        acceptorClient.getFilterChain().addLast("protocolFilter", new ProtocolCodecFilter(new FwtpProtocolCodecFactory()));
        acceptorClient.getFilterChain().addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));
        acceptorClient.setHandler(new FWFProtocolHandler());
        try {
            acceptorClient.bind(new InetSocketAddress( FWFApsConfig.PORT_APP_SERVER ));
        } catch (IOException e) {
            logger.error("#9#  Socket Bind, Abnormal program exit !!", e);
            System.exit(9);
        }
        logger.info(" ** FWFApServer Client listening on port "+ FWFApsConfig.PORT_APP_SERVER +".");


        ShutdownHook shutdownHook = new ShutdownHook(acceptorClient);
        Runtime.getRuntime().addShutdownHook(shutdownHook);


        //////////////////////////////////////////////////////////////////////////////////
        FWFControlCenter controlCenter = new FWFControlCenter();
        controlCenter.start();
        //////////////////////////////////////////////////////////////////////////////////

    }
    
    public static boolean call() {

        FWFApsConfig config = new FWFApsConfig();
        if (!FWFApsConfig.isLoadSuccess()) {
            logger.error("#1#  I have not read the property information. Abnormal program exit.");
            System.exit(1);
        }
        config.printInfomation();


        FwfTasksLoader.loadTransMap();


        StartupAction startup = new StartupAction();
        startup.init();

        try {
            startup.execute(null, null);
        } catch (Exception e) {
            logger.error("#2#  Startup, Abnormal program exit !!", e);
            System.exit(2);
        }


        // Client Protocol Handler
        NioSocketAcceptor acceptorClient = new NioSocketAcceptor();

        acceptorClient.getFilterChain().addLast("protocolFilter", new ProtocolCodecFilter(new FwtpProtocolCodecFactory()));
        acceptorClient.getFilterChain().addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));
        acceptorClient.setHandler(new FWFProtocolHandler());
        try {
            acceptorClient.bind(new InetSocketAddress( FWFApsConfig.PORT_APP_SERVER ));
        } catch (IOException e) {
            logger.error("#9#  Socket Bind, Abnormal program exit !!", e);
            System.exit(9);
        }
        logger.info(" ** FWFApServer Client listening on port "+ FWFApsConfig.PORT_APP_SERVER +".");


        ShutdownHook shutdownHook = new ShutdownHook(acceptorClient);
        Runtime.getRuntime().addShutdownHook(shutdownHook);


        //////////////////////////////////////////////////////////////////////////////////
        FWFControlCenter controlCenter = new FWFControlCenter();
        controlCenter.start();
        //////////////////////////////////////////////////////////////////////////////////
        return true;
    }        
    
        
}