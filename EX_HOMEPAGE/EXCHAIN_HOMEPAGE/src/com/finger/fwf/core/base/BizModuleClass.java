/* ---------------------------------------------------------------------
 * @(#)BizModuleClass.java
 * @Creator    Son,DongHak
 * @version    1.0
 * @date       2012-10-10
 * ---------------------------------------------------------------------
 */
package com.finger.fwf.core.base;

import org.apache.log4j.Logger;

import com.finger.fwf.core.dbm.SqlMapConfig;
import com.finger.protocol.fwtp.FwtpMessage;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * BizModuleClass
 * @version  1.0
 */
public abstract class BizModuleClass implements BizModule{
    private static Logger logger = Logger.getLogger(BizModuleClass.class);

    public BizModuleClass() {
        logger.debug("$$$$$$$$$$  BizModuleClass  $$$$$$$$$$");
    }

    public synchronized static SqlMapClient getSqlMap() {
        return SqlMapConfig.getInstance();
    }

    public synchronized static SqlMapClient getSqlMapOther(String sysMgtNo) {
        return SqlMapConfig.getInstanceOther(sysMgtNo);
    }

    /** 객체생성시 실행 */
    public void init() throws Exception {
        logger.debug("::: init()");
    }

    /** 트랜잭션  실행 */
    public abstract FwtpMessage execute(String action, FwtpMessage in) throws Exception;
    

    /** 객체종료시 실행 */
    public void close() {
        logger.debug("::: close()");
    }
}