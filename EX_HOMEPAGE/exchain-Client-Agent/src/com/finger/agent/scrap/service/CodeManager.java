package com.finger.agent.scrap.service;

import java.io.File;
import java.util.HashMap;

import com.finger.agent.config.FcAgentConfig;

public class CodeManager {

    private static CodeManager manager = new CodeManager();
    private static HashMap<String, ServiceCodeDataSet> codes;
    static long ld_time = 0;
    static long cur_time;
    static File f = null;
    private static Object lock = new Object();

    private CodeManager() {
        initialize();
    }

    public static CodeManager getInstance() {
        return manager;
    }

    public void initialize() {
        try {
            f = new File( "" );	//= 경로 + 파일명
            ld_time = f.lastModified();
            new ServiceCodeXmlDO();
            codes = ServiceCodeXmlDO.loadCodeDefinitions(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ServiceCodeDataSet getCode(String strCode) {

        if ( f == null )
            cur_time = 1;
        else
            cur_time = f.lastModified();

        synchronized ( lock ) {
            if (ld_time < cur_time)
                initialize();
        }
        return (ServiceCodeDataSet)codes.get(strCode);
    }
}
