package com.finger.fwf.uxui.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.finger.agent.config.FcAgentConfig;
import com.finger.agent.exception.AgentException;
import com.finger.agent.msg.FixedMsg;
import com.finger.agent.msg.MsgManager;
import com.finger.agent.msg.MsgObj;
import com.finger.fwf.uxui.CommonCarts;
import com.finger.fwf.uxui.util.HttpServletUX;
import com.finger.fwf.uxui.util.ResponseUX;
import com.finger.fwf.uxui.util.UXUtil;
import com.finger.protocol.fwtp.FwtpHeader;
import com.finger.protocol.fwtp.FwtpMessage;

public class UXLoginService extends BasisService {
    private static Logger logger = Logger.getLogger(UXLoginService.class);

    private static UXLoginService _instance = new UXLoginService();

    private static final String	_Sys_Mgt_No_		=	"_sys_mgt_no_";
    private static final String	_LOGIN_TASK_ID		=	"SADS10010";    //사용자로그인정보

    private static final String	_LoginAfter_010		=	"SADM00010";	// 코인마켓정책
    
    
    //private static final String	_LoginAfter_020		=	"SADS10020";	// 메뉴
    //private static final String	_LoginAfter_021		=	"SADS10021";	// 법인정보
    //private static final String	_LoginAfter_022		=	"SADS10022";	// 사업장정보
    //private static final String	_LoginAfter_023		=	"SADS10023";	// 마이메뉴
    //private static final String	_LoginAfter_024		=	"SADS10024";	// 메뉴(보고서)
    //private static final String	_LoginAfter_5050	=	"SADM05050";	// SelectBox 개발자정의(모음)-로그인후/세션

    protected UXLoginService(){
        logger.debug(":: "+ this.getClass() +" ~~~~~~~~~~~~~~~~~~~~~");
    }

    public static UXLoginService getInstance() {
        logger.debug(":: getInstance() ~~~~~~~~~~~~~~~~~~~~~");
        return _instance;
    }

    public void doLogin(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug(":: doLogin() ~~~~~~~~~~~~~~~~~~~~~");
       
        
        HttpServletUX.setResponseContentAndHeader( response );

        MsgObj _msg_ = null;
        String _sysId = ""; 
        String _return_page = "";
        
        try {
            if (getParametersSize(request, logger) > 0)
            {
                FwtpMessage fwtp = new FwtpMessage();
                fwtp.setClientIp( request.getRemoteAddr() );
                fwtp.setUserId("_FWF_LOGIN_");
                fwtp.setSysMgtNo( ((ArrayList<LinkedHashMap<String, String>>)CommonCarts.getSystemInfo()).get(0).get("mrkt_nm") );
                fwtp.setTaskId( _LOGIN_TASK_ID );
                
                ArrayList<LinkedHashMap<String, String>> paramListHashMap =  parameterExtractList(request);
                fwtp.setRequestData( getRequestLoginInfo(request,paramListHashMap) );
                
                _sysId      = ((ArrayList<LinkedHashMap<String, String>>)CommonCarts.getSystemInfo()).get(0).get("mrkt_nm");
                _return_page = request.getParameter("return_page");
                
                logger.debug( "==========================================================" );
                logger.debug( "request.getParameter(_sysId)=["+ _sysId+"]");
                logger.debug( "request.getParameter(_return_page)=["+ _return_page+"]");
                logger.debug( "==========================================================" );
                
                //로그인확인
                FwtpMessage resultMessage = com.finger.agent.FWFTaskByUXUI.doFWFAgentListForUX ( fwtp );
                logger.debug( resultMessage.toString() );

                if (FwtpHeader._RESPONSE_OKAY_.equals(resultMessage.getResponseCode()))
                {
                    if (resultMessage.getResponseResultSize() > 0)
                    {
                    	//세션담기(로그인정보)
                        HttpServletUX.createLoginInfo( request, resultMessage , true);

                        FwtpMessage result = null;

                        FwtpMessage _fmsg = new FwtpMessage();
                        _fmsg.setSysMgtNo( HttpServletUX.getUserInfo(request).getSysMgtNo() );
                        _fmsg.setUserId( HttpServletUX.getUserInfo(request).getUserId() );
                        
                        _fmsg.setTaskId(_LoginAfter_010);	// 코인마켓정책
                        result = com.finger.agent.FWFTaskByUXUI.doFWFAgentListForUX ( _fmsg );
                        if (FwtpHeader._RESPONSE_OKAY_.equals(result.getResponseCode())) {
                            HttpServletUX.setMarketPolicyInfo(request, result);
                        }
                        

                        //로그인 ID 값을 쿠키에 저장
                        //setCookie(request, response);
                   		//response.sendRedirect(__main_page_name);
                        
                        //RequestDispatcher rd = request.getRequestDispatcher("/main.jsp");
                        //request.getRequestDispatcher( "/main.jsp" ).forward( request, response );
                        //rd.forward(request, response);

                        response.sendRedirect("main.jsp");
                        	
                    }
                    else {
                        throw new AgentException (new MsgObj(FixedMsg._MSG_SYSU_S2010, "ID/Password Check."));
                    }
                }
                else throw new AgentException (resultMessage.getMessageCode(), resultMessage.getErrorMessage());
            }
            else {
                //----------------------------------------
                throw new AgentException (new MsgObj(FixedMsg._MSG_SYSU_S2010, "No information is required for Login."));
            }
        } catch (IOException ioe) {
            logger.error(ioe.getMessage(), ioe);
            //--try { response.sendError(500, ioe.getMessage()); } catch (IOException e) { e.printStackTrace(); }
            _msg_ = MsgManager.getInstance().createMessage(ioe);
        } catch (AgentException ee) {
            logger.error(ee.getMessage(), ee);
            _msg_ = MsgManager.getInstance().createMessage(ee);
        } finally {
        	ResponseUX.toHtmlByLoginError( request, response, _msg_ );
        }
    }
    
    public void doLoginReload(HttpServletRequest request) throws IOException, ServletException {
        logger.debug(":: doLoginReload() ~~~~~~~~~~~~~~~~~~~~~");
       
        try {
            if (getParametersSize(request, logger) > 0)
            {
                FwtpMessage fwtp = new FwtpMessage();
                fwtp.setClientIp( request.getRemoteAddr() );
                fwtp.setUserId("_FWF_LOGIN_");
                fwtp.setSysMgtNo( ((ArrayList<LinkedHashMap<String, String>>)CommonCarts.getSystemInfo()).get(0).get("sys_mgt_no") );
                fwtp.setTaskId( _LOGIN_TASK_ID );
                
                if( HttpServletUX.getUserInfo(request)!=null )
                {
                	fwtp.setCustSeqno( HttpServletUX.getUserInfo(request).getCustSeqno() );
                }
                
                ArrayList<LinkedHashMap<String, String>> paramListHashMap =  parameterExtractList(request);
                fwtp.setRequestData( getRequestLoginInfo(request,paramListHashMap) );
                
                //로그인확인
                FwtpMessage resultMessage = com.finger.agent.FWFTaskByUXUI.doFWFAgentListForUX ( fwtp );
                logger.debug( resultMessage.toString() );

                if (FwtpHeader._RESPONSE_OKAY_.equals(resultMessage.getResponseCode()))
                {
                    if (resultMessage.getResponseResultSize() > 0)
                    {
                    	//세션담기(로그인정보)
                        HttpServletUX.createLoginInfo( request, resultMessage, false );
                    }
                    else {
                        throw new AgentException (new MsgObj(FixedMsg._MSG_SYSU_S2010, "ID/Password Check."));
                    }
                }
                else throw new AgentException (resultMessage.getMessageCode(), resultMessage.getErrorMessage());
            }
            else {
                //----------------------------------------
                throw new AgentException (new MsgObj(FixedMsg._MSG_SYSU_S2010, "No information is required for Login."));
            }
        } catch (AgentException ee) {
            logger.error(ee.getMessage(), ee);
        } finally {
        	
        }
        
    }
        
    
    public ArrayList<LinkedHashMap<String, String>> getRequestLoginInfo(HttpServletRequest request, ArrayList<LinkedHashMap<String, String>> paramListHashMap){
    	
    	ArrayList<LinkedHashMap<String, String>> returnArrayList = new ArrayList<LinkedHashMap<String, String>>();
        
    	try {
    		
            LinkedHashMap<String, String> _reqLoginInfo = paramListHashMap.get(0);
            
    		// 접속자 IP
    		String ip = request.getHeader("X-FORWARDED-FOR");
    		if (ip == null || ip.length() == 0) {
    		   ip = request.getHeader("Proxy-Client-IP");
    		}
    		if (ip == null || ip.length() == 0) {
    		   ip = request.getHeader("WL-Proxy-Client-IP");  // 웹로직
    		}
    		if (ip == null || ip.length() == 0) {
    		   ip = request.getRemoteAddr() ;
    		}

    		// 에이전트
    		String agent = request.getHeader("User-Agent");

    		// 접속 브라우져 구분
    		String brower = null; 
    		 

    		if (agent != null) {
    		   if (agent.indexOf("Trident") > -1) {
    		      brower = "MSIE";
    		   } else if (agent.indexOf("Chrome") > -1) {
    		      brower = "Chrome";
    		   } else if (agent.indexOf("Opera") > -1) {
    		      brower = "Opera";
    		   } else if (agent.indexOf("iPhone") > -1 && agent.indexOf("Mobile") > -1) {
    		      brower = "iPhone";
    		   } else if (agent.indexOf("Android") > -1 && agent.indexOf("Mobile") > -1) {
    		      brower = "Android";
    		   }
    		}


    		// 접속 OS 구분
    		String os = null; 
    		 
    		if(agent.indexOf("NT 10.0") != -1) os = "Windows 10";
    		else if(agent.indexOf("NT 6.1") != -1) os = "Windows 7";
    		else if(agent.indexOf("NT 6.0") != -1) os = "Windows Vista/Server 2008";
    		else if(agent.indexOf("NT 5.2") != -1) os = "Windows Server 2003";
    		else if(agent.indexOf("NT 5.1") != -1) os = "Windows XP";
    		else if(agent.indexOf("NT 5.0") != -1) os = "Windows 2000";
    		else if(agent.indexOf("NT") != -1) os = "Windows NT";
    		else if(agent.indexOf("9x 4.90") != -1) os = "Windows Me";
    		else if(agent.indexOf("98") != -1) os = "Windows 98";
    		else if(agent.indexOf("95") != -1) os = "Windows 95";
    		else if(agent.indexOf("Win16") != -1) os = "Windows 3.x";
    		else if(agent.indexOf("Windows") != -1) os = "Windows";
    		else if(agent.indexOf("Linux") != -1) os = "Linux";
    		else if(agent.indexOf("Macintosh") != -1) os = "Macintosh";
    		else os = ""; 

            _reqLoginInfo.put("LOGIN_IP_ADDR"     , ip);     //접속IP주소
            _reqLoginInfo.put("LOGIN_MAC_ADDR"    , "");     //접속Mac주소
            _reqLoginInfo.put("LOGIN_DEVICE"      , agent);  //접속단말기
            _reqLoginInfo.put("LOGIN_DEMEVI_MODEL", "");     //접속단말기모델
            _reqLoginInfo.put("LOGIN_OS"          , os);     //접속OS
            _reqLoginInfo.put("LOGIN_OS_VER"      , "");     //접속OS버전
            _reqLoginInfo.put("LOGIN_BROWSER"     , brower); //접속브라우저
            _reqLoginInfo.put("LOGIN_BROWSER_VER" , "");     //접속브라우저버전
	        
            
            returnArrayList.add(_reqLoginInfo);
            
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return returnArrayList;
    }        
    
    public void checkDualServerIP(HttpServletRequest request){
    	
		try {
	    	
			logger.debug("************************************************************************");
			logger.debug("DB 실행 서버 IP 조회");
			logger.debug("************************************************************************");
			
	    	// DB 실행 서버 IP 조회
	        FwtpMessage fwtp = new FwtpMessage();
            fwtp.setUserId("_FWF_LOGIN_");
            fwtp.setSysMgtNo( UXUtil.nvl(request.getParameter(_Sys_Mgt_No_)) );
	        fwtp.setTaskId("SADM00000");	//시스템정보
	        FwtpMessage response = com.finger.agent.FWFTaskByUX.doFWFAgentListForUX ( fwtp );
			
	        logger.debug(" DB 업데이트 응답=["+ response + "]");

	        if (FwtpHeader._RESPONSE_OKAY_.equals(response.getResponseCode())) {
          
	        	if( response.getResponseResultList().size()>0 ){
	        		LinkedHashMap<String, String> lHashMap = response.getResponseResultList().get(0);
	        		String dualServerIp =  lHashMap.get("dual_exec_svr_ip");
      		
	        	}
	        }
	        
		} catch (AgentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }        
}