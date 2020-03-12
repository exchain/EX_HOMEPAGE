package com.finger.fwf.uxui.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.finger.fwf.uxui.UserInfo;
import com.finger.fwf.uxui.util.HttpServletUX;
import com.finger.fwf.uxui.util.ResponseUX;
import com.finger.fwf.uxui.util.UXUtil;

/**
 * Servlet Filter implementation class UXAccessFilter
 */
public class UXAccessFilter extends BasisFilter {
    private static Logger logger = Logger.getLogger(UXAccessFilter.class);

    /**
     * Default constructor. 
     */
    public UXAccessFilter() {
        logger.debug("Constructor stub: UXAccessFilter() 호출!!");
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
        //--logger.debug("Method stub: doFilter() 호출!!");
        HttpServletRequest hrequest = (HttpServletRequest)request;
        if (hrequest.getRequestURI() != null) {
            String reqUri = hrequest.getRequestURI();
            int nIdx = 0;
            nIdx = (reqUri.indexOf(".htm"     ) > -1 ? 1 : 0)
                 + (reqUri.indexOf(".html"    ) > -1 ? 1 : 0)
                 + (reqUri.indexOf(".jsp"     ) > -1 ? 1 : 0)
                 + (reqUri.indexOf("/uxLogin" ) > -1 ? 1 : 0)
                 + (reqUri.indexOf("/uxAction") > -1 ? 1 : 0);
            if (nIdx > 0)
                logger.debug( "\n\t.getRequestURI()         => "+ hrequest.getRequestURI()
                        	+ "\n\t.getQueryString()        => "+ hrequest.getQueryString()
                        	+ "\n\t.getRemoteAddr()         => "+ hrequest.getRemoteAddr()
                        	+ "\n\t.getHeader(`User-Agent`) => "+ hrequest.getHeader("User-Agent")
                        	+ "\n\t.getHeader(`referer`)    => "+ hrequest.getHeader("referer")
                        	+ "\n" );
    
           
        }
        // pass the request along the filter chain
        chain.doFilter(request, response);
    }
}