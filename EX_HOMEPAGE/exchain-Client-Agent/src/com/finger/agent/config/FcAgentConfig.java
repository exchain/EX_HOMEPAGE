package com.finger.agent.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.finger.agent.util.AgentUtil;

public class FcAgentConfig {
    private static Logger logger = Logger.getLogger(FcAgentConfig.class);

    private static final String _SEPARATOR_             =   System.getProperty("file.separator");
    private static final String m_PropertyFolderNm      =   "conf";    
    private static final String m_PropertyXMLFile       =   "FingerClientAgent.xml";
    

    public static final String      _CharSet_           =   "UTF-8";
    private static String      _LocalIPRange            =   "";		 // _local.xml 프로퍼티파일 사용여부 판단을 위해
    public static boolean   _PROPERTY_READ_SUCCESS      =   false;
        
    /* 클라이언트에서 확인할 운영/개발서버 여부 */
    public static String    _REAL_DEV_SERVERS_CHK_   	=	"";
    
    /*  FWF의 HOST & PORT 정보  */
    public static String    _FWF_APP_SVR_HOST_IP_       =   "127.0.0.1"; // [ UX => FWF                 ]
    public static int       _FWF_APP_SVR_PORT_NO_       =   25005;       // [ 내부 송신                                                    ]
    
    /*  HERMES-FBK의 HOST & PORT 정보  */
    public static String    _HERMES_FBK_HOST_IP_        =   "127.0.0.1"; // [ FWF => Hermes-FBK         ]
    public static int       _HERMES_FBK_PORT_NO_        =   25007;       // [ 내부 송신                                                    ]
    
    /*  FWF의 HOST & PORT 정보  */
    public static String    _HMS_TO_FWF_HOST_IP		    =	"127.0.0.1"; // [ Hermes-FBK => FWF         ]
    public static int       _HMS_TO_FWF_PORT_NO		    =	25005;       // [ 내부 송신                                                    ]

    /*  통신시간제한(milliseconds) 설정 => 단위: 1000 = 1초  */
    public static int       _MESSAGESEND_TIMEOUT_		=	30000;	// 전송 Timeout(공통) 
    public static int       _COMMUNICATE_TIMEOUT_		=	65000;	// Core⇔Hermes⇔VAN(Bank) 
    public static int       _UI_RESPONSE_TIMEOUT_		=	300000;	// UXUI⇔Core 
    public static int       _FI_RESPONSE_TIMEOUT_		=	180000;	// H-FBK⇔H-Relay 


    public static String    _ApplicationHome_           =   "";
    public static String    _LoggingHome_               =   "";
    private static Properties _config ;
    
    /* 휴대폰 인증서비스 인증서 파일위치 */
    public static String    _PHON_CERT_FILE_PATH_		=	"";
    public static String    _PHON_PRI_FILE_PATH_		=	"";

    /* 계좌번호 검증시 사용할 인사이드뱅크 서버정보 */
    public static String    _INSIDEBANK_SCD_INFO_		=	"";
    
    /* 모바일 인증 URL */
    public static String    _MOBILE_OK_POPUP_URL_		=	"";
    
    /* SMS Service */
    public static String    _KT_TCS_USER_ID_			=	"";
    public static String    _KT_TCS_CALLBACK_NO_		=	"";
    public static String    _KT_TCS_SEND_COUNT_         =	"";
    
    
    static {
        _config = new Properties();
        
        try {
        	
        	/*  FILE Encoding 정보  */
            if (!_CharSet_.equals(System.getProperty("file.encoding").toUpperCase())){
                System.setProperty("file.encoding", _CharSet_ );
            }
            logger.debug("file.encoding ==> ["+ System.getProperty("file.encoding") +"]");            

            /*  Application Home 디렉토리 정보  */
            _ApplicationHome_ = System.getProperty("exchain.app.home");
            if (AgentUtil.isNull(_ApplicationHome_)){
                _ApplicationHome_ = System.getProperty("user.dir");
            }
            logger.debug("exchain.app.home ==> ["+ System.getProperty("exchain.app.home") +"]");
            logger.debug("ApplicationHome ==> ["+ _ApplicationHome_ +"]");

            /*  Application Log 디렉토리 정보  */
            _LoggingHome_ = System.getProperty("exchain.log.home");
            if (AgentUtil.isNull(_LoggingHome_)){
                _LoggingHome_ = System.getProperty("user.dir");
            }
            logger.debug("exchain.log.home ==> ["+ System.getProperty("exchain.log.home") +"]");
            logger.debug("LoggingHome      ==> ["+ _LoggingHome_ +"]");
            logger.debug("PropertyFolder   ==> ["+ m_PropertyFolderNm +"]");
            logger.debug("_SEPARATOR_      ==> ["+ _SEPARATOR_ +"]");
            logger.debug("FILE Path        ==> ["+ _ApplicationHome_ +_SEPARATOR_+ m_PropertyFolderNm +_SEPARATOR_+ m_PropertyXMLFile  +"]");
            
            String fullPath = _ApplicationHome_ +_SEPARATOR_+ m_PropertyFolderNm +_SEPARATOR_+ m_PropertyXMLFile;
        	
            /*  Application 환경설정 파일(XML) */
            _config.loadFromXML( new FileInputStream(fullPath) );
                        
            /*  FWF의 HOST & PORT 정보  */
            _FWF_APP_SVR_HOST_IP_       =   (String)_config.getProperty("_FWF_APP_SVR_HOST_IP");
            _FWF_APP_SVR_PORT_NO_       =   Integer.parseInt((String)_config.getProperty("_FWF_APP_SVR_PORT_NO"));
            
            /*  Timeout  */
            _MESSAGESEND_TIMEOUT_		=	Integer.parseInt(((String)_config.getProperty("_MESSAGESEND_TIMEOUT")));
            _COMMUNICATE_TIMEOUT_		=	Integer.parseInt(((String)_config.getProperty("_COMMUNICATE_TIMEOUT")));
            _UI_RESPONSE_TIMEOUT_		=	Integer.parseInt(((String)_config.getProperty("_UI_RESPONSE_TIMEOUT")));

            /* 휴대폰 인증서비스 인증서 파일위치 */
            _PHON_CERT_FILE_PATH_		=	_ApplicationHome_ +_SEPARATOR_+ m_PropertyFolderNm +_SEPARATOR_+ "phonCert" +_SEPARATOR_+ "exchainCert.der";
            _PHON_PRI_FILE_PATH_		=	_ApplicationHome_ +_SEPARATOR_+ m_PropertyFolderNm +_SEPARATOR_+ "phonCert" +_SEPARATOR_+ "exchainPri.key";

            /* 계좌번호 검증시 사용할 인사이드뱅크 서버정보 */
            _INSIDEBANK_SCD_INFO_		=	(String)_config.getProperty("_INSIDEBANK_SCD_INFO_");
            
            _PROPERTY_READ_SUCCESS      =   true;
            
            /* 모바일 인증 URL */
            _MOBILE_OK_POPUP_URL_       = (String)_config.getProperty("_MOBILE_OK_POPUP_URL_");
            
            /* SMS Service*/
            _KT_TCS_USER_ID_			= (String)_config.getProperty("_KT_TCS_USER_ID_");		// 회원ID
            _KT_TCS_CALLBACK_NO_		= (String)_config.getProperty("_KT_TCS_CALLBACK_NO_");	// 발신전화번호
            _KT_TCS_SEND_COUNT_         = (String)_config.getProperty("_KT_TCS_SEND_COUNT_");	// 서버전송에러시 재발송횟수
            
            /* 클라이언트에서 확인할 운영개발서버 여부 */
            _REAL_DEV_SERVERS_CHK_	    = (String)_config.getProperty("_REAL_DEV_SERVERS_CHK_");
        }
        catch (IOException ioe) {
            logger.error("###>> Client Agent Property File Not Found...: "+ _ApplicationHome_ +_SEPARATOR_+ m_PropertyXMLFile, ioe );
        }
        finally {
            if (_config != null) { _config.clear(); }
        }
    }
}