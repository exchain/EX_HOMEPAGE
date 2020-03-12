package com.finger.fwf.uxui.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

/**
 * Servlet Filter implementation class UXAuthFilter
 */
public class UXAuthFilter extends BasisFilter {
    private static Logger logger = Logger.getLogger(UXAuthFilter.class);

    /**
     * Default constructor. 
     */
    public UXAuthFilter() {
        logger.debug("Constructor stub: UXAuthFilter() 호출!!");
        checkCharset(logger);
    }

    /**
     * @see Filter#init(FilterConfig)
     */
    //@Override
    public void init(FilterConfig fConfig) throws ServletException {
        // web.xml :: Filter 설정 시 <init-param> 입력 있을때... (필터안의 초기정보 처리)
        logger.debug("Method stub: init() 호출!!");
    }
    /**
     * @see Filter#destroy()
     */
    //@Override
    public void destroy() {
        logger.debug("Method stub: destroy() 호출!!");
    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    //@Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        logger.debug("Method stub: doFilter() 호출!!");
        
//        HttpServletRequest hrequest = (HttpServletRequest)request;
        	
//        // 세션 유지 여부를 확인한다.
//        HttpSession session = hrequest.getSession(false);
//        if (UXUtil.isNull(HttpServletUX.getSessionUserID(session))) {
//            logger.debug("session is null ...");
//            HashMap<String, Object> resultMap = new HashMap<String, Object>();
//            resultMap.put("_R-CODE"  , FwtpHeader._RESPONSE_ERROR_ );
//            resultMap.put("_M-CODE"  , FixedMsg._MSG_SYSU_S0001 );
//            resultMap.put("_M-DESC"  , UCS.ContByMsgCd(FixedMsg._MSG_SYSU_S0001, hrequest) );
//            resultMap.put("_M-CONT"  , "Session is null..." );
//
//            HttpServletUX.setResponseContentAndHeader( (HttpServletResponse)response );
//
//            Gson gson = new Gson();
//            response.getOutputStream().println( gson.toJson( resultMap ) );
//            return;
//        }
        

        // pass the request along the filter chain
        chain.doFilter(request, response);
    }
}