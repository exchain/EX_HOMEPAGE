package com.finger.fwf.uxui.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.finger.agent.msg.MsgManager;
import com.finger.agent.msg.MsgObj;
import com.finger.fwf.uxui.util.HttpServletUX;
import com.finger.fwf.uxui.util.ResponseUX;
import com.finger.protocol.fwtp.FwtpMessage;

public class UXLogoutService extends BasisService {
    private static Logger logger = Logger.getLogger(UXLogoutService.class);

    private static UXLogoutService _instance = new UXLogoutService();

    private static final String	_LOGOUT_TASK_ID		=	"SADS10040";

    //private static final String	_Login_Acs_Dt		=	"login_acs_dt";
    //private static final String	_Login_Acs_Dt_Seq	=	"login_acs_dt_seq";

    protected UXLogoutService(){
        logger.debug(":: "+ this.getClass() +" ~~~~~~~~~~~~~~~~~~~~~");
    }

    public static UXLogoutService getInstance() {
        logger.debug(":: getInstance() ~~~~~~~~~~~~~~~~~~~~~");
        return _instance;
    }

    public void doLogout(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug(":: doLogout() ~~~~~~~~~~~~~~~~~~~~~");

        HttpServletUX.setResponseContentAndHeader( response );

        MsgObj	_msg_ = null;
        try {

        	/*        	
        	FwtpMessage fwtp = new FwtpMessage();
            fwtp.setClientIp( request.getRemoteAddr() );
            fwtp.setSysMgtNo( HttpServletUX.getUserInfo(request).getSysMgtNo() );
            fwtp.setUserId( HttpServletUX.getUserInfo(request).getUserId() );
            fwtp.setTaskId( _LOGOUT_TASK_ID );*/
            /*
            ArrayList<LinkedHashMap<String, String>> resultList = new ArrayList<LinkedHashMap<String, String>>();
            LinkedHashMap<String, String> hmap = new LinkedHashMap<String, String>();
            hmap.put(_Login_Acs_Dt    , HttpServletUX.getUserInfo(request).getValue(_Login_Acs_Dt) );
            hmap.put(_Login_Acs_Dt_Seq, HttpServletUX.getUserInfo(request).getValue(_Login_Acs_Dt_Seq) );
            resultList.add(hmap);
            fwtp.setRequestData( resultList );

            FwtpMessage resultMessage = com.finger.agent.FWFTaskByUXUI.doFWFAgentListForUX ( fwtp );
            logger.debug( resultMessage.toString() );
            */

            //if (FwtpHeader._RESPONSE_OKAY_.equals(resultMessage.getResponseCode()))
            //{
                HttpServletUX.removeLoginInfo( request );
            //}
            //else
            //    throw new AgentException (resultMessage.getMessageCode(), resultMessage.getErrorMessage());
        } catch (NullPointerException npex) {
            logger.error(npex.getMessage(), npex);
        } catch (Exception ee) {
            logger.error(ee.getMessage(), ee);
            _msg_ = MsgManager.getInstance().createMessage(ee);
        }
        finally {
            if (_msg_ == null)
                //request.getRequestDispatcher( "/main.jsp" ).forward( request, response );
                response.sendRedirect("main.jsp");
            else
                ResponseUX.toHtmlByLogoutError( request, response, _msg_ );
        }
    }
}