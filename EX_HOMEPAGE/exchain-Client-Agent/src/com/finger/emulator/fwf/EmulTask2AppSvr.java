package com.finger.emulator.fwf;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.finger.agent.client.EngineConnector;
import com.finger.agent.config.FcAgentConfig;
import com.finger.agent.exception.AgentException;
import com.finger.protocol.fwtp.FwtpHeader;
import com.finger.protocol.fwtp.FwtpMessage;

public class EmulTask2AppSvr {
	private static Logger logger = Logger.getLogger(EmulTask2AppSvr.class);

	// Deliminator 정의
	public static String	COL_DELIM	=	"\t";
	public static String	ROW_DELIM	=	"\n";

    public static void main(String[] args) {
    	if (args.length < 3) {
    		System.out.println("useage :: emulTask2Aps [param1] [param2] [param3] [param4] [param5] [param6]");
    		System.out.println("          -param1 : SYS-MGT-NO");
    		System.out.println("          -param2 : Task ID");
    		System.out.println("          -param3 : Data Type [C:ommon|G:rid]");
    		System.out.println("          -param4 : User ID / Emp.No");
    		System.out.println("          -param5 : AP Server IP (option)");
    		System.out.println("          -param6 : AP Server Port (option)");
    		return;
    	}
    	System.setProperty("file.encoding", FcAgentConfig._CharSet_ );

        String reqData = "";
    	try {
            try {
    	        String  txtfile = "conf/emulator/"+"AP_"+ args[1] + ".txt";
    	        reqData = getOutputStreamByFile(txtfile);
            } catch (Exception e) { e.printStackTrace(); }

        	String retVal ;
    		if (args.length == 6)
    			retVal = doEmulatorByFWF (args[0], args[1], args[2], reqData, args[3], args[4], args[5]);
    		else
    			retVal = doEmulatorByFWF (args[0], args[1], args[2], reqData, args[3]);

    		System.out.println("\n>>>>>  retVal = ["+ retVal +"]");
		} catch (AgentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	System.exit(0);
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 에뮬레이터 doEmulatorByFWF()
     * @param sysMgtNo
     * @param taskId
     * @param dataType
     * @param reqData
     * @param userId
     * @return 응답(수신)문자열
     * @throws AgentException
     */
    public static String doEmulatorByFWF(String sysMgtNo, String taskId, String dataType, String reqData, String userId) throws AgentException {
    	return doEmulatorByFWF (sysMgtNo, taskId, dataType, reqData, userId, null, null);
    }
    public static String doEmulatorByFWF(String sysMgtNo, String taskId, String dataType, String reqData, String userId, String hostIp, String portNo) throws AgentException {
    	logger.debug(">>>  doEmulatorByFWF() ::: Input Parameters....."
    			+ "\n\t=> sysMgtNo["+ sysMgtNo +"]"
    			+ "\n\t=> taskId["  + taskId   +"]"
    			+ "\n\t=> dataType["+ dataType +"]"
    			+ "\n\t=> reqData[" + reqData  +"]"
    			+ "\n\t=> userId["  + userId   +"]"
    			+ "\n\t=> hostIp["  + hostIp   +"]"
    			+ "\n\t=> portNo["  + portNo   +"]");

    	if (taskId == null || "".equals(taskId))
    		throw new AgentException("TASK-ID가 누락되었습니다. 시스템담당자에게 문의 바랍니다.");

    	if (dataType == null || "".equals(dataType))
    		dataType = FwtpHeader._DATATYPE_COMMON;
    	if (hostIp == null || "".equals(hostIp))
    		hostIp = "127.0.0.1";
    	if (portNo == null || "".equals(portNo))
    		portNo = "27005";

    	/*EngineConnector._host = hostIp;
    	EngineConnector._port = Integer.parseInt(portNo);*/

        EngineConnector econn = new EngineConnector( hostIp, Integer.parseInt(portNo) );
        init(econn);

        FwtpMessage fwtp = new FwtpMessage();
        fwtp.setSysMgtNo( sysMgtNo );
        fwtp.setRequestBy( FwtpHeader._REQUEST_BY_EMULATOR  );
        fwtp.setClientIp( "127.0.0.1" );
        fwtp.setUserId( userId  );
        fwtp.setTaskId( taskId );
        fwtp.setDataType( dataType );
        fwtp.setRequestData( reqData );

    	return econn.communicate( fwtp );
    }


    /**
     * 에뮬레이터 doEmulatorListByFWF()
     * @param sysMgtNo
     * @param taskId
     * @param dataType
     * @param reqData
     * @param userId
     * @return
     * @throws AgentException
     */
    public static List<Map<?, ?>> doEmulatorListByFWF(String sysMgtNo, String taskId, String dataType, String reqData, String userId) throws AgentException {
    	return doEmulatorListByFWF (sysMgtNo, taskId, dataType, reqData, userId, null, null);
    }
    public static List<Map<?, ?>> doEmulatorListByFWF(String sysMgtNo, String taskId, String dataType, String reqData, String userId, String hostIp, String portNo) throws AgentException {
    	List<Map<?, ?>> retList = null ;

    	String strResult = doEmulatorByFWF (sysMgtNo, taskId, dataType, reqData, userId, hostIp, portNo);
logger.debug("\n>>>>>   strResult = ["+ strResult +"]");

    	if (strResult == null || "".equals(strResult))
    		;
    	else
    	{
    		String[] rows = strResult.split(ROW_DELIM);
    		String row = "";

    		/* 처음 3줄은 컬럼명, 컬럼타입, 컬럼사이즈를 담고 있음. */
    		if (rows.length > 3)
    		{
    			retList = new ArrayList<Map<?, ?>>();

    	        // 첫번째 라인은 FID (필드 리스트)
    	        row = rows[0].trim();	// 프로토콜상 레코드 구분자는 '\n'이나 '\r\n'를 보내는 경우를 위해 처리
    	        String[] arrFields = row.split(COL_DELIM);

    	        // 두번째 라인 이후는 데이타, 파싱해서 List(HashMap) 구조로 변환
    	        int nFieldCount = arrFields.length;

                for (int i=1; i<rows.length; i++) {
                    Map<String, String> map = new HashMap<String, String>();

                    // 필드의 값 배열
                    row = rows[i];
                    String[] arrValues = row.split(COL_DELIM);

                    for (int j=0; j<nFieldCount; j++) {
                        try {
                            map.put(arrFields[j], arrValues[j]);
                        }
                        catch(Exception e) {
                            map.put(arrFields[j], "");
                        }
                    }
                    retList.add( map );
                }
    		}
    	}

    	return retList;
    }

	private static void init(EngineConnector connector) {
		// ShutdownHook 등록
		EmulShutdownHook shutdownHook = new EmulShutdownHook(connector);
		Runtime.getRuntime().addShutdownHook(shutdownHook);
	}


	/**
	 * FcAgentConfig._CHAR_SET 형식으로 파일을 읽어 들인다. (None-BOM 파일이어야 함)
	 * @param fileName
	 */
	public static String getOutputStreamByFile (String fileName) {
		/*Charset charset = Charset.forName(_CHAR_SET);
		ByteArrayOutputStream rtnBAOS = new ByteArrayOutputStream();

		try {
			FileInputStream  fis = new FileInputStream( fileName );
			BufferedInputStream  bis = new BufferedInputStream(fis);
			if (bis.available() > 0) {
	        	byte[] buf = new byte[bis.available()];
	        	bis.read(buf);
	        	rtnBAOS.write(buf);
	        }
			rtnBAOS.close();
	        fis.close();
		} catch (FileNotFoundException fnfe) {
			logger.error(fnfe.getMessage(), fnfe);
		} catch (IOException ioe) {
			logger.error(ioe.getMessage(), ioe);
		}
		return rtnBAOS.toString(); */

		ByteArrayOutputStream rtnBAOS = new ByteArrayOutputStream();
		Charset charset = Charset.forName(FcAgentConfig._CharSet_);

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), charset));
			String str_line = null;
			while ( (str_line = reader.readLine()) != null ) {
				rtnBAOS.write(str_line.getBytes());
				rtnBAOS.write("\n".getBytes());
			}
			reader.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return rtnBAOS.toString();
	}
}