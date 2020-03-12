package com.finger.fwf.uxui.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.finger.fwf.server.FWFAppSvrBoot;
import com.finger.fwf.uxui.util.PrevLoadUX;

public class UXPrevLoadFilter extends BasisFilter {
    private static Logger logger = Logger.getLogger(UXPrevLoadFilter.class);

    private boolean  _is_Not_Loading  =  true;

    public UXPrevLoadFilter() {
        logger.debug("Constructor stub: UXPreLoadFilter() 호출!!");
        checkCharset(logger);
    }

  //@Override    
    public void init(FilterConfig fConfig) throws ServletException {
        logger.debug("Method stub: init() 호출!!");
        
        _is_Not_Loading = FWFAppSvrBoot.call();
        
        int inTime = 1000;
        
        logger.debug("####################################");
        logger.debug("####################################");
        logger.debug("### sleeep start ["+inTime+"]     ##");
        logger.debug("####################################");
        
        
        try {
			Thread.sleep(inTime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        logger.debug("### sleeep end ["+inTime+"]       ##");
        logger.debug("####################################");
        logger.debug("####################################");        
        

        if (_is_Not_Loading)
        {
            logger.debug("############## PrevLoadUX start ######################");
            PrevLoadUX.previousLoading(null);
            logger.debug("############## PrevLoadUX end   ######################");
        }
        _is_Not_Loading = false;
    }

  //@Override
    public void destroy() { }

  //@Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        logger.debug("Method stub: UXPreLoadFilter doFilter() 호출!!");
    }
  

}