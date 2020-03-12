package com.finger.fwf.uxui.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.finger.agent.config.FcAgentConfig;
import com.finger.agent.msg.FixedMsg;
import com.finger.agent.msg.MsgInfo;
import com.finger.agent.msg.MsgObj;
import com.finger.protocol.fwtp.FwtpHeader;
import com.finger.protocol.fwtp.FwtpMessage;
import com.google.gson.Gson;

public class ResponseUX {
    private static Logger logger = Logger.getLogger(ResponseUX.class);

    protected static final String	__LoginFail_URI	       =  "/page/process/loginFail.jsp";
    protected static final String	__LogoutFail_URI       =  "/page/process/logoutFail.jsp";
    protected static final String	__AdminLoginFail_URI   =  "/page/process/adminloginFail.jsp";
    protected static final String	__AdminLogoutFail_URI  =  "/page/process/adminlogoutFail.jsp";
    protected static final String	__AccessError_URI      =  "/page/process/page403.jsp";
    
        
    protected static final String	__LoginPassError_URI   =  "/page/process/pageLogin.jsp";
    protected static final String	__MemberPassError_URI  =  "/page/process/pageMember.jsp";
    
    protected static final String	__CustLevelError_010_URI      =  "/page/process/page403.jsp";
    protected static final String	__CustLevelError_100_URI      =  "/page/process/page403.jsp";
    protected static final String	__CustLevelError_600_URI      =  "/page/process/page403.jsp";
    protected static final String	__CustLevelError_700_URI      =  "/page/process/page403.jsp";
    protected static final String	__CustLevelError_800_URI      =  "/page/process/page403.jsp";

    /* 응답 시 사용할 필드 값 정의 */
    protected static final String	__R_CODE               =  "_R-CODE";
    protected static final String	__M_CODE               =  "_M-CODE";
    protected static final String	__M_DESC               =  "_M-DESC";
    protected static final String	__M_CONT               =  "_M-CONT";
    /* 그리드 관련 등답 항목 값 정의 */
    protected static final String	__answerTime           =  "answerTime";
    protected static final String	__rowNum               =  "rowNum";
    protected static final String	__rows                 =  "rows";
    protected static final String	__chart                =  "chart";
    /* COMMON 타입의 응답 결과 항목 정의 */
    protected static final String	__resultData           =  "resultData";

    //로그인화면으로
    public static void toHtmlByLoginPassError(HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException 
    {
        request.getRequestDispatcher( __LoginPassError_URI ).forward( request, response );
    }
    
    //정회원인증화면으로
    public static void toHtmlByMemberPassError(HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException 
    {
        request.getRequestDispatcher( __MemberPassError_URI ).forward( request, response );
    }
    

    public static void toHtmlByLoginError(HttpServletRequest request, HttpServletResponse response, MsgObj _msg) throws IOException, ServletException {
        if (response == null || _msg == null) return;

        request.setAttribute("_msgCode" , _msg.getCode() );
        request.setAttribute("_msgDesc" , _msg.getDesc() );
//        request.setAttribute("_msgDesc" , UCS.ContByMsgCd(_msg.getCode(), request));
        request.setAttribute("_msgConts", _msg.getConts() );

        request.getRequestDispatcher( __LoginFail_URI ).forward( request, response );
    }

    
    public static void toHtmlByLogoutError(HttpServletRequest request, HttpServletResponse response, MsgObj _msg) throws IOException, ServletException {
        if (response == null || _msg == null) return;

        request.setAttribute("_msgCode" , _msg.getCode() );
        request.setAttribute("_msgDesc" , _msg.getDesc() );
//        request.setAttribute("_msgDesc" , UCS.ContByMsgCd(_msg.getCode(), request));
        request.setAttribute("_msgConts", _msg.getConts() );

        request.getRequestDispatcher( __LogoutFail_URI ).forward( request, response );
    }
    
    public static void toHtmlByAdminLogoutError(HttpServletRequest request, HttpServletResponse response, MsgObj _msg) throws IOException, ServletException {
        if (response == null || _msg == null) return;

        request.setAttribute("_msgCode" , _msg.getCode() );
        request.setAttribute("_msgDesc" , _msg.getDesc() );
//        request.setAttribute("_msgDesc" , UCS.ContByMsgCd(_msg.getCode(), request));
        request.setAttribute("_msgConts", _msg.getConts() );

        request.getRequestDispatcher( __AdminLogoutFail_URI ).forward( request, response );
    }
    

    public static void toHtmlByAccessError(HttpServletRequest request, HttpServletResponse response, boolean isSession) throws IOException, ServletException 
    {

        request.getRequestDispatcher( __AccessError_URI ).forward( request, response );
    }

    public static void toHtmlByCustLevelError(HttpServletRequest request, HttpServletResponse response, String custState) throws IOException, ServletException {

        //정회원인 경우 
        if( "100".equals(custState) )
        {
        	request.getRequestDispatcher( __CustLevelError_100_URI ).forward( request, response );
        }
        //휴면회원인 경우
        else if( "600".equals(custState) )
        {
        	request.getRequestDispatcher( __CustLevelError_600_URI ).forward( request, response );
        }
        //차단회원인 경우
        else if( "700".equals(custState) )
        {
        	request.getRequestDispatcher( __CustLevelError_700_URI ).forward( request, response );
        }
        //탈퇴회원인 경우
        else if( "800".equals(custState) )
        {
        	request.getRequestDispatcher( __CustLevelError_800_URI ).forward( request, response );
        }
        
    }

    
    /** GSON 형식으로 결과값을 보낸다.*/
    public static void toGsonByMarketResult(HttpServletResponse response, HashMap<String, Object> _hmap) throws IOException {
        if (response == null || _hmap == null) return;

        _hmap.put(__R_CODE, FwtpHeader._RESPONSE_OKAY_ );
        
        //logger.debug("###############\n"+ new Gson().toJson( _hmap ) );

        OutputStream out = null;
        out = response.getOutputStream();
        out.write( new Gson().toJson( _hmap ).getBytes(FcAgentConfig._CharSet_) );
        if (out != null) out.close();
    }
    public static void toGsonByActionResult(HttpServletRequest request, HttpServletResponse response, FwtpMessage _message) throws IOException {
        if (response == null || _message == null) return;

        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        if (_message.getResponseResultSize() > 0) {
            resultMap.put(__R_CODE    , _message.getResponseCode() );
            resultMap.put(__M_CODE    , _message.getMessageCode() );
            resultMap.put(__M_DESC    , MsgInfo.getMessage(_message.getMessageCode()) );
//            resultMap.put(__M_DESC    , UCS.ContByMsgCd(_message.getMessageCode(), request) );
            resultMap.put(__M_CONT    , _message.getErrorMessage() );
            resultMap.put(__answerTime, _message.getTimeStamp() );
            resultMap.put(__resultData, _message.getResponseResultData() );
        }
        else {
            resultMap.put(__R_CODE    , _message.getResponseCode() );
            resultMap.put(__M_CODE    , FixedMsg._MSG_CODE_90002 );
            resultMap.put(__M_DESC    , MsgInfo.getMessage(FixedMsg._MSG_CODE_90002) );
//            resultMap.put(__M_DESC    , UCS.ContByMsgCd(FixedMsg._MSG_CODE_90002, request) );
            resultMap.put(__M_CONT    , _message.getErrorMessage() );
            resultMap.put(__answerTime, _message.getTimeStamp() );
            resultMap.put(__resultData, _message.getResponseResultData() );
        }

        OutputStream out = null;
        out = response.getOutputStream();
        out.write( new Gson().toJson( resultMap ).getBytes(FcAgentConfig._CharSet_) );
        if (out != null) out.close();
    }
    public static void toGsonByActionResultGrid(HttpServletRequest request, HttpServletResponse response, FwtpMessage _message) throws IOException {
        if (response == null || _message == null) return;

        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put(__R_CODE    , _message.getResponseCode() );
        resultMap.put(__M_CODE    , _message.getMessageCode() );
        resultMap.put(__M_DESC    , MsgInfo.getMessage(_message.getMessageCode()) );
//        resultMap.put(__M_DESC    , UCS.ContByMsgCd(_message.getMessageCode(), request) );
        resultMap.put(__M_CONT    , _message.getErrorMessage() );
        resultMap.put(__answerTime, _message.getTimeStamp() );
        resultMap.put(__rowNum    , _message.getResponseResultSize() );
        resultMap.put(__rows      , _message.getResponseResultList() );
        //logger.debug("###############\n"+ UXUtil.shortenString(new Gson().toJson( resultMap ), 2000, "。。。") );

        OutputStream out = null;
        out = response.getOutputStream();
        out.write( new Gson().toJson( resultMap ).getBytes(FcAgentConfig._CharSet_) );
        if (out != null) out.close();
    }
    public static void toGsonByActionResultChart(HttpServletRequest request, HttpServletResponse response, FwtpMessage _message) throws IOException {
        if (response == null || _message == null) return;

        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put(__R_CODE    , _message.getResponseCode() );
        resultMap.put(__M_CODE    , _message.getMessageCode() );
        resultMap.put(__M_DESC    , MsgInfo.getMessage(_message.getMessageCode()) );
//        resultMap.put(__M_DESC    , UCS.ContByMsgCd(_message.getMessageCode(), request) );
        resultMap.put(__M_CONT    , _message.getErrorMessage() );
        resultMap.put(__answerTime, _message.getTimeStamp() );
        resultMap.put(__rowNum    , _message.getResponseResultSize() );
        resultMap.put(__rows      , _message.getResponseResultList() );
        resultMap.put(__chart     , UXUtil.makeChartData(_message.getResponseResultList()) );
        //logger.debug("###############\n"+ UXUtil.shortenString(new Gson().toJson( resultMap ), 2000, "。。。") );

        OutputStream out = null;
        out = response.getOutputStream();
        out.write( new Gson().toJson( resultMap ).getBytes(FcAgentConfig._CharSet_) );
        if (out != null) out.close();
    }

    public static void toGsonByMarketResult(HttpServletResponse response, boolean bool) throws IOException {
        if (response == null) return;

        OutputStream out = null;
        out = response.getOutputStream();
        out.write( new Gson().toJson( bool ).getBytes(FcAgentConfig._CharSet_) );
        if (out != null) out.close();
    }

    public static void toGsonByActionError(HttpServletRequest request, HttpServletResponse response, MsgObj _msg) throws IOException {
        if (response == null || _msg == null) return;

        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put(__R_CODE   , UXUtil.nvl(_msg.getRepos(), FwtpHeader._RESPONSE_ERROR_) );
        resultMap.put(__M_CODE   , _msg.getCode()  );
        resultMap.put(__M_DESC   , _msg.getDesc().replaceAll("\n", "<br/>")  );
        //resultMap.put(__M_DESC   , UCS.ContByMsgCd(_msg.getCode(), request).replaceAll("\n", "<br/>")  );
        resultMap.put(__M_CONT   , _msg.getConts().replaceAll("\n", "<br/>") );
        
        //logger.debug("###############\n"+ UXUtil.shortenString(new Gson().toJson( resultMap ), 2000, "。。。") );

        OutputStream out = null;
        out = response.getOutputStream();
        out.write( new Gson().toJson( resultMap ).getBytes(FcAgentConfig._CharSet_) );
        if (out != null) out.close();
    }
}