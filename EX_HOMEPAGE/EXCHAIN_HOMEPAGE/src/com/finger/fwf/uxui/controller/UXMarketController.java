package com.finger.fwf.uxui.controller;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.finger.fwf.uxui.service.UXMarketService;

/**
 * Servlet implementation class UXMarketController
 */
public class UXMarketController extends BasisController {
    private static final long serialVersionUID = -146103032182660656L;
    private static Logger logger = Logger.getLogger(UXMarketController.class);

    public UXMarketController() {
        logger.debug(":: HttpServlet -> "+ this.getClass() +" 호출!!");
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        logger.debug("method stub: init() 호출~!");
    }

    @Override
    protected void doRequestProcessing(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
            throws ServletException, IOException {
        UXMarketService service = UXMarketService.getInstance();
        service.doUXService(httpservletrequest, httpservletresponse);
    }
}