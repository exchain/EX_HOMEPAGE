package com.finger.fwf.core.dbm;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.finger.agent.config.FcAgentConfig;
import com.finger.fwf.core.config.FWFApsConfig;
import com.finger.tools.cipher.Crypto;
import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

public class SqlMapConfig {
    private static Logger logger = Logger.getLogger(SqlMapConfig.class);

    private static final SqlMapClient   m_sqlMapper ;       //CMS일반-DB
    //private static final SqlMapClient   m_sqlMapperOther ;  //타시스템연계DB
    private static final LinkedHashMap<String, SqlMapClient> m_sqlMapperOtherMap = new LinkedHashMap<String, SqlMapClient>(); //타시스템연계DB

    private static final String  _DB_DbName_        =   "db.dbname";
    private static final String  _DB_Driver_        =   "db.driver";
    private static final String  _DB_Url_           =   "db.url";
    private static final String  _DB_UserName_      =   "db.username";
    private static final String  _DB_PassWord_      =   "db.password";
    private static final String  _DB_ValidQuery_    =   "db.validationquery";
    
    private static final String  _Other_DbName_     =   "other.dbname";
    private static final String  _Other_Driver_     =   "other.driver";
    private static final String  _Other_Url_   	 	=   "other.url";
    private static final String  _Other_UserName_   =   "other.username";
    private static final String  _Other_PassWord_   =   "other.password";
    private static final String  _Other_ValidQuery_ =   "other.validationquery";
    
    private static String         db_dbname         =   "";
    private static String         db_driver         =   "";
    private static String         db_url            =   "";
    private static String         db_username       =   "";
    private static String         db_password       =   "";
    
    private static String        validQuery_defalut =   "select 1 from dual";
    private static String        validQuery_oralce  =   "select 1 from dual";
    private static String        validQuery_mssql   =   "select 1";
    

    static {
        logger.debug(">>>  SqlMapClient ===> ["+ FWFApsConfig.SQLMAPCONFIG_FILE +"]["+ FWFApsConfig.SQLMAPCONFIG_DBConn +"]");
        try {
            Charset charset = Charset.forName(FcAgentConfig._CharSet_);
            Resources.setCharset(charset);

            Properties properties = new Properties();
            
            String _exchain_bin_home = System.getProperty("exchain.app.home");
            if (_exchain_bin_home == null || "".equals(_exchain_bin_home)){
            	_exchain_bin_home = System.getProperty("user.dir");
            }
            
            String _SEPARATOR_ = System.getProperty("file.separator");
            properties.load( new FileInputStream( _exchain_bin_home +_SEPARATOR_+ FWFApsConfig.SQLMAPCONFIG_DBConn ) );
            
            //properties.load(new FileInputStream(FWFApsConfig.SQLMAPCONFIG_DBConn));
            
            db_dbname = properties.getProperty(_DB_DbName_);
            
            db_driver = properties.getProperty(_DB_Driver_);
            db_url    = properties.getProperty(_DB_Url_);
            
            db_username = properties.getProperty(_DB_UserName_);
            db_password = properties.getProperty(_DB_PassWord_);
            
            // DB ID/PWD 암복호화
            //--logger.debug("@@@@@  "+_DB_UserName_+" ["+ db_username +"], "+_DB_PassWord_+" ["+ db_password +"]");
            db_username = Crypto.decryptSDCBC(db_username);
            db_password = Crypto.decryptSDCBC(db_password);
            //--logger.debug("@@@@@  "+_DB_UserName_+" ["+ db_username +"], "+_DB_PassWord_+" ["+ db_password +"]");

            properties.setProperty(_DB_UserName_, db_username );
            properties.setProperty(_DB_PassWord_, db_password );

            //DB Properties file Validation 체크
            String validationResult = checkValidationDB();
            if( !"PASS".equals(validationResult) )
            {
            	logger.error(">>>  ===============================================================================================");
                logger.error(">>>  Validation Check :" + validationResult);
                logger.error(">>>  ===============================================================================================");
                
                throw new RuntimeException(validationResult);
            }
            
            //DB Name으로 구분하여 ValidationQuery 값을 셋팅한다.(비교시 대소문자 구분없음)
            if( "ORACLE".equals(db_dbname.toUpperCase()) )
            {
            	properties.setProperty(_DB_ValidQuery_, validQuery_oralce );
            }
            else if( "MSSQL".equals(db_dbname.toUpperCase()) )
            {
            	properties.setProperty(_DB_ValidQuery_, validQuery_mssql );
            }
            else
            {
            	properties.setProperty(_DB_ValidQuery_, validQuery_defalut );
            }
            

            Reader reader = Resources.getResourceAsReader( FWFApsConfig.SQLMAPCONFIG_FILE );

            m_sqlMapper = SqlMapClientBuilder.buildSqlMapClient( reader, properties );
            reader.close();
            
            logger.debug(">>>  ==================================================================================");
            logger.debug(">>>  Success SqlMapClient db_driver ["+ db_driver +"]");
            logger.debug(">>>  Success SqlMapClient db_url    ["+ db_url +"]");
            logger.debug(">>>  ==================================================================================");
            
        } catch (IOException ioe) {
            throw new RuntimeException("Something bad happened while building the SqlMapClient instance." +
            		"\n  -> config file name : "+ FWFApsConfig.SQLMAPCONFIG_FILE +
            		"\n  -> dbconn file name : "+ FWFApsConfig.SQLMAPCONFIG_DBConn, ioe );
        }
    }

    /**
     * sqlMapper 반환
     * @return
     */
    public static SqlMapClient getInstance() {
        return m_sqlMapper;
    }
    public static SqlMapClient getInstanceOther(String sysMgtNo) {
        return m_sqlMapperOtherMap.get(sysMgtNo);
    }

    
    /**
     * DB Properties 정보 GET
     * @return
     */
    public static String getDB_DbName() {
        return db_dbname;
    }
    public static String getDB_Driver() {
        return db_driver;
    }
    public static String getDB_Url() {
        return db_url;
    }
    public static String getDB_UserName() {
        return db_username;
    }
    public static String getDB_PassWord() {
        return db_password;
    }

    /**
     * CMS DB Properties 정보 NULL 체크
     * @return
     */
    public static String checkValidationDB() 
    {
    	if( db_dbname == null || ("").equals(db_dbname) )
    	{
    		return "dbconnA.properties : db.dbname is null";
    	}
        
    	if( db_driver == null || ("").equals(db_driver) )
    	{
    		return "dbconnA.properties : db.driver is null";
    	}
    	
    	if( db_url == null || ("").equals(db_url) )
    	{
    		return "dbconnA.properties : db.url is null";
    	}
        
    	if( db_username == null || ("").equals(db_username) )
    	{
    		return "dbconnA.properties : db.username is null";
    	}

    	if( db_password == null || ("").equals(db_password) )
    	{
    		return "dbconnA.properties : db.password is null";
    	}
    	
    	return "PASS";
    }
    
    /**
     * ERP 연계 DB Properties 정보 NULL 체크
     * @return
     */
    public static String checkValidationOtherDB(String other_dbname, String other_driver, String other_url, String other_username, String other_password) 
    {
    	if( other_dbname == null || ("").equals(other_dbname) )
    	{
    		return "dbconnA.properties : other.dbname is null";
    	}
        
    	if( other_driver == null || ("").equals(other_driver) )
    	{
    		return "dbconnA.properties : other.driver is null";
    	}
    	
    	if( other_url == null || ("").equals(other_url) )
    	{
    		return "dbconnA.properties : other.url is null";
    	}
        
    	if( other_username == null || ("").equals(other_username) )
    	{
    		return "dbconnA.properties : other.username is null";
    	}

    	if( other_password == null || ("").equals(other_password) )
    	{
    		return "dbconnA.properties : other.password is null";
    	}
    	
    	return "PASS";
    }
    
}