package com.finger.fwf.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.AccessControlException;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.finger.agent.util.AgentUtil;
import com.finger.fwf.core.config.FWFApsConfig;

public class FWFControlCenter extends Thread {
    private static Logger logger = Logger.getLogger(FWFControlCenter.class);

    static {
        //FWFApsConfig config = new FWFApsConfig();
        if (!FWFApsConfig.isLoadSuccess()) {
            logger.error("#1#  I have not read the property information. Abnormal program exit.");
            System.exit(1);
        }
    }

    private static final int    _COMMAND_PORT_NO_   =   FWFApsConfig.PORT_APP_SERVER + 90;

    private boolean isRunning ;
    private Random random ;
    private Timer timer ;

    private ServerSocket serverSocket ;

    /**
     * ContronCenter 종료 by ShutdownHook 발동 시
     */
    public void run() {
        logger.debug("FWF Control Center 시작~~~["+_COMMAND_PORT_NO_+"]");
        startAwait();
    }

    /**
     * 서버소켓을 열고 shutdown 요청 대기
     */
    private void startAwait() {
        isRunning = false;
        random = null ;

        timer = new Timer();
        timer.scheduleAtFixedRate(
        		new TimerTask() {
        			public void run() {
        				if(isRunning) logger.debug("FWFControlCenter Await...(Port:"+_COMMAND_PORT_NO_+")");
        			}
        		}
        		,	new Date()
        		,	600000 );	// 10분마다 메세지 출력..

        serverSocket = null;

        boolean isShutdown = false;
        try {
        	try {
        		serverSocket = new ServerSocket( _COMMAND_PORT_NO_, 1, InetAddress.getByName("127.0.0.1") );
        		isRunning = true;
        	} catch (IOException ioe) {
        		logger.error(ioe.getMessage(), ioe);
        	}
        	while ( isRunning ) {
        		if (Thread.currentThread().isInterrupted()) break;

        		Socket socket = null;
        		InputStream stream = null;
        		try {
        			socket = serverSocket.accept();
        			socket.setSoTimeout( 10 * 1000 );
        			stream = socket.getInputStream();
        		} catch (AccessControlException ace) {
        			logger.error(ace);
        			continue;
        		} catch (IOException ioe) {
        			System.exit(1);
        		}

        		// Cut off to avoid DoS attack
        		int expected = 1024;
        		while (expected < _CMD_SHUTDOWN.length()) {
        			if (random == null)
        				random = new Random(System.currentTimeMillis());
        			expected += (random.nextInt() % 1024);
        			logger.debug(">>  expected ::: "+ expected );
        		}
        		logger.debug(">> #expected ::: "+ expected );

        		StringBuffer command = new StringBuffer();

        		while (expected > 0) {
        			int ch = -1;
        			try {
        				ch = stream.read();
        			} catch (IOException ioe) {
        				ch = -1;
        			}
        			// EOF
        			if (ch < 32) break;
        			command.append( (char)ch );
        			expected--;
        		}
        		// 소켓을 닫는다.
        		try { socket.close(); } catch (IOException ign) {}

        		String cmd = command.toString();

        		/* *******  실행명령(Execute Command)  ******* */
        		if (_CMD_SHUTDOWN.equals(cmd)) {
        			isShutdown = true;
        			break;
        		}
        		/* ******************************************* */
        		else {
        			logger.debug("# `"+ cmd +"` Command is not defined.");
        		}
        	}
        } catch (Throwable t) {
        	logger.error(t.getMessage(), t);
        } finally {
        	logger.debug("currentThread().isInterrupted: "+ Thread.currentThread().isInterrupted() );
        	// 서버소켓을 닫는다.
        	try { serverSocket.close(); } catch (IOException ign) {}
        	try {
        		this.timer.cancel();
        		this.timer = null;
        		logger.debug("ControlCenter shutdown !! ["+_COMMAND_PORT_NO_+"]");
        	} catch (Throwable ign) {}
        	logger.debug("FWF Application shutdown !!");

        	if (isShutdown) System.exit(0);
        }
    }

    /*  실행명령(Execute Command)  */
    private static final String _CMD_SHUTDOWN       =   "shutdown";

    /**
     * 소켓으로 명령을 보낸다.
     */
    private void sendCommand(String command) {
    	Socket socket = null;
    	OutputStream os = null;
    	try {
    		if (AgentUtil.isNull(command)) {
    			logger.info("==> Input Command is null !!");
    			return;
    		}
    		logger.info("==> Sending Command:`"+ command +"` to "+_COMMAND_PORT_NO_+" port.");
    		socket = new Socket("127.0.0.1", _COMMAND_PORT_NO_);
    		socket.setSoTimeout( 5 * 1000 );
    		os = socket.getOutputStream();

    		for (int ii = 0; ii < command.length(); ii++)
    			os.write( command.charAt(ii) );

    		os.flush();
    	} catch (Throwable t) {
    		logger.error(t.getMessage(), t);
    		System.exit(1);
    	} finally {
    		try { os.close(); } catch (Throwable ign) {}
    		try { socket.close(); } catch (Throwable ign) {}
    	}
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) {
        if (args.length > 0) {
            FWFControlCenter controlCenter = new FWFControlCenter();
            controlCenter.sendCommand( args[0] );
        }
        else
            logger.info("#Useage :: FWFControlCenter [shutdown|...]");
    }
}