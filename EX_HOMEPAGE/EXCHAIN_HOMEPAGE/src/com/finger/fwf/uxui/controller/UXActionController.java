package com.finger.fwf.uxui.controller;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.finger.fwf.uxui.service.UXActionService;

/**
 * Servlet implementation class UXActionController
 */
public class UXActionController extends BasisController {
    private static final long serialVersionUID = 6651245252269308346L;
    private static Logger logger = Logger.getLogger(UXActionController.class);

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UXActionController() {
        logger.debug(":: HttpServlet -> "+ this.getClass() +" 호출!!");
    }

    /**
     * @see Servlet#init(ServletConfig)
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        logger.debug("method stub: init() 호출~!");
    }

    @Override
    protected void doRequestProcessing(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
            throws ServletException, IOException {
        UXActionService service = UXActionService.getInstance();
        service.doUXService(httpservletrequest, httpservletresponse);
    }
}