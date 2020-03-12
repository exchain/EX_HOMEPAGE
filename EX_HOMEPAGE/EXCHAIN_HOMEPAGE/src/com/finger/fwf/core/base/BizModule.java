/* ---------------------------------------------------------------------
 * @(#)BizModule.java 
 * @Creator    Son,DongHak
 * @version    1.0
 * @date       2012-10-10
 * ---------------------------------------------------------------------
 */
package com.finger.fwf.core.base;

import com.finger.protocol.fwtp.FwtpMessage;

/**
 * BizModule
 * @version 1.0
 */
public interface BizModule {
    /** 객체생성시 실행 */
    public void init() throws Exception;
    /** 트랜잭션  실행  */
    public FwtpMessage execute(String action, FwtpMessage in) throws Exception;
    /** 객체종료시 실행 */
    public void close();
}