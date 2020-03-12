package com.finger.fwf.core.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.finger.agent.config.FcAgentConfig;
import com.finger.agent.util.AgentUtil;

public class FWFApsConfig {
    private static Logger logger = Logger.getLogger(FWFApsConfig.class);

    private static boolean		isPropertyLoadSuccess	=	false;
    public static boolean			_IsRealServer		=	false;
    
    private static final String		m_PropertyXMLFile	=	"conf/fwfApsvrProperty.xml";

    public static final String		_OS_NAME			=	System.getProperty("os.name");
    public static final String      _SEPARATOR_         =   System.getProperty("file.separator");
    
    public static int		PORT_APP_SERVER				=	25005;	//25105
    public static String	REAL_DEV_SVR_CHK			=	"";
    
    public static String	TASKS_XML_FILE				=	"";
    public static String	SQLMAPCONFIG_FILE			=	"";
    public static String	SQLMAPCONFIG_Other			=	"";
    public static String	SQLMAPCONFIG_DBConn			=	"";

    public static String	ScheduleList_TaskID			=	"";


    /*  업무별 수신파일 보관주기(days) 설정  */
    public static int		KeepingTerm_Corp_Card		=	30;	//days
    public static int		KeepingTerm_Multi_Pay		=	30;	//days

    public static String	_ReceiveFile_Home_          =   "";
    public static String	SYS_ID         				=   "";
    
    public FWFApsConfig() {
        loadFWFProperty();
    }

    public void printInfomation() {
        logger.info("-----------------------------------------------------------------");
        logger.info(" [EXCHAIN] "+ m_PropertyXMLFile );
        logger.info("-----------------------------------------------------------------");
        logger.info(" System<file.encoding>  : "+ System.getProperty("file.encoding") );
        logger.info(" _OS_NAME               : "+ _OS_NAME );
        logger.info(" PORT_APP_SERVER        : "+ PORT_APP_SERVER );
        logger.info(" TASKS_XML_FILE         : "+ TASKS_XML_FILE );
        logger.info(" SQLMAPCONFIG_FILE      : "+ SQLMAPCONFIG_FILE );
        logger.info(" SQLMAPCONFIG_Other     : "+ SQLMAPCONFIG_Other );
        logger.info(" SQLMAPCONFIG_DBConn    : "+ SQLMAPCONFIG_DBConn );
        logger.info(" ScheduleList_TaskID    : "+ ScheduleList_TaskID );
        logger.info(" KeepingTerm_Corp_Card  : "+ KeepingTerm_Corp_Card );
        logger.info(" KeepingTerm_Multi_Pay  : "+ KeepingTerm_Multi_Pay );
        logger.info(" _IsRealServer          : "+ _IsRealServer );
        logger.info(" REAL_DEV_SERVER_CHECK  : "+ REAL_DEV_SVR_CHK );
        logger.info(" SYS_ID                 : "+ SYS_ID );
        logger.info("-----------------------------------------------------------------");
    }

    public static boolean isLoadSuccess() {
        return isPropertyLoadSuccess;
    }

    private void loadFWFProperty() {
        
    	logger.info("-----------------------------------------------------------------");
    	logger.info(" Start loadFWFProperty ( FWFApsConfig ) ");
    	logger.info("-----------------------------------------------------------------");
    	
    	Properties prop = new Properties();

        String _app_home = "";
        try {
            if (!FcAgentConfig._CharSet_.equals(System.getProperty("file.encoding").toUpperCase()))
                System.setProperty("file.encoding", FcAgentConfig._CharSet_ );

            _app_home = System.getProperty("exchain.app.home");
            if (AgentUtil.isNull(_app_home)){
                _app_home = System.getProperty("user.dir");
            }
            
            logger.info("exchain.app.home=["+ _app_home +"]");
            logger.info("Property file  =["+ _app_home +_SEPARATOR_+ m_PropertyXMLFile +"]");

            prop.loadFromXML( new FileInputStream( _app_home +_SEPARATOR_+ m_PropertyXMLFile ) );

            try {
                PORT_APP_SERVER			=	Integer.parseInt(((String) prop.getProperty("PORT_APP_SERVER")));
            }
            catch (NumberFormatException e) {
                logger.error("##>>  APP-SERVER PORT is a Number type.");
            }
            
            logger.info("Property file  =["+ PORT_APP_SERVER +"]");            

            TASKS_XML_FILE				=	(String)prop.getProperty("TASKS_XML_FILE");
            SQLMAPCONFIG_FILE			=	(String)prop.getProperty("SQLMAPCONFIG_FILE");
            SQLMAPCONFIG_Other			=	(String)prop.getProperty("SQLMAPCONFIG_Other");
            SQLMAPCONFIG_DBConn			=	(String)prop.getProperty("SQLMAPCONFIG_DBConn");

            ScheduleList_TaskID			=	(String)prop.getProperty("ScheduleList_TaskID");

            REAL_DEV_SVR_CHK		    =	(String)prop.getProperty("REAL_DEV_SERVERS_CHK");
            

            SYS_ID				        =	(String)prop.getProperty("SYS_ID");
            
            logger.info("REAL_DEV_SVR_CHK =["+ (String)prop.getProperty("REAL_DEV_SERVERS_CHK") +"]");
            
            _ReceiveFile_Home_ = System.getProperty("vfile.recv.home");
            if (AgentUtil.isNull(_ReceiveFile_Home_))
                _ReceiveFile_Home_ = System.getProperty("user.dir");

            String hostIp = InetAddress.getLocalHost().getHostAddress();
            logger.info("Server Host IP       =["+ hostIp +"]");
            logger.info("REAL_SERVERS_ADDRESS =["+ (String)prop.getProperty("REAL_SERVERS_ADDRESS") +"]");
            
            StringTokenizer stz = new StringTokenizer((String)prop.getProperty("REAL_SERVERS_ADDRESS"));
            while (stz.hasMoreTokens()) {
                if (hostIp.equals(stz.nextToken())) {
                    _IsRealServer = true;
                    logger.info("TRUE [Hort Server IP = Real Server IP]");                    
                    break;
                }
            }
            
            isPropertyLoadSuccess = true;
        }
        catch (IOException ioe) {
            logger.error("###>>  FWF APP-SERVER Property File Not Found ==> "+ _app_home +_SEPARATOR_+ m_PropertyXMLFile, ioe );
        }
        finally {
            if (prop != null) { prop.clear(); }
        }
    }
}