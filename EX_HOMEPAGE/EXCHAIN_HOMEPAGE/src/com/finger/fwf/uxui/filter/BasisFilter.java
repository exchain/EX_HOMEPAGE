package com.finger.fwf.uxui.filter;

import javax.servlet.Filter;

import org.apache.log4j.Logger;

import com.finger.agent.config.FcAgentConfig;

public abstract class BasisFilter implements Filter {

    public BasisFilter() { }

    protected void checkCharset(Logger logger) {
        if (!FcAgentConfig._CharSet_.equals(System.getProperty("file.encoding"))) {
            logger.debug("==> file.encoding = ["+ System.getProperty("file.encoding") +"] to ["+ FcAgentConfig._CharSet_ +"]");
            System.setProperty("file.encoding", FcAgentConfig._CharSet_ );
        }
    }
}