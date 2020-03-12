package com.finger.emulator.hermes;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.finger.agent.config.FcAgentConfig;
import com.finger.agent.util.AgentUtil;
import com.finger.emulator.hermes.clientsocket.HmEngineConnector;
import com.finger.protocol.hermes.HermesMessage;

public class HmEmulClient {

    public static void main(String[] args) throws Exception {

        String s_TaskId  =  "FM1000100";
        String s_HostIp  =  "127.0.0.1";
        String s_PortNo  =  "27015";
        String s_TrMesg  =  "";

        if (args.length < 3) {
        	System.out.println("useage :: emulTask2Fbk [param1] [param2] [param3]");
        	System.out.println("          [param1] : TASK ID");
        	System.out.println("          [param2] : 서버 IP");
        	System.out.println("          [param3] : 서버 Port");
        	System.out.println("[ex] emulTask2Fbk.cmd FM1000100 127.0.0.1 27015");
        	System.exit(-1);
        }
        else {
        	s_TaskId  = args[0];
        	s_HostIp  = args[1];
        	s_PortNo  = args[2];

        	System.out.println(">>>  Input, args[] = ["+ s_TaskId +"] ["+s_HostIp +"] ["+ s_PortNo +"]");
        }
        try {
            if ("REQPOLL".equals(s_TaskId)) {
            	s_TrMesg = "HDRREQPOLL" + (new SimpleDateFormat("yyMMddmmss")).format(new Date());
            }
            else {
            	ByteArrayOutputStream baout = new ByteArrayOutputStream();

            	String  txtfile = "conf/emulator/"+"HM_"+ s_TaskId + ".txt";

            	baout = getOutputStreamByFile(txtfile);
            	baout.close();

            	s_TrMesg = baout.toString();
            }
        }
        catch (Exception e) { e.printStackTrace(); }

        int _Length_ = HermesMessage._VAN_FILESND_.equals(s_TaskId) ? HermesMessage._SizeOfMessage : 4;

        StringBuilder sendMsg = new StringBuilder();
        if (!(s_TaskId.indexOf("VN") > -1))
        sendMsg.append( AgentUtil.fillNumericString(String.valueOf(s_TrMesg.getBytes().length), _Length_) );
        sendMsg.append( s_TrMesg );
        System.out.println("===>>  sendMsg = ["+ sendMsg.toString() +"]");

        HmEngineConnector hmconn = new HmEngineConnector( s_HostIp, Integer.parseInt(s_PortNo) );
        init(hmconn);

        String retVal = hmconn.communicate( sendMsg.toString() );
        System.out.println("===>>  retVal  = ["+ retVal +"]");

        hmconn.close();
        System.exit(0);
    }

    private static void init(HmEngineConnector connector) {
        // ShutdownHook 등록
        HmEmulShutdownHook shutdownHook = new HmEmulShutdownHook(connector);
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }

    /**
     * FcAgentConfig._CHAR_SET 형식으로 파일을 읽어 들인다. (None-BOM 파일이어야 함)
     * @param fileName
     */
    public static ByteArrayOutputStream getOutputStreamByFile (String fileName) {
        ByteArrayOutputStream rtnBAOS = new ByteArrayOutputStream();
        Charset charset = Charset.forName(FcAgentConfig._CharSet_);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), charset));
            String str_line = null;
            while ( (str_line = reader.readLine()) != null ) {
                rtnBAOS.write(str_line.getBytes());
            }
            reader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return rtnBAOS;
    }
}