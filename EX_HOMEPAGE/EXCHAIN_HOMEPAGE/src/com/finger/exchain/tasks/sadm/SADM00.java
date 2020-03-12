/* =============================================================================
 *  Project      : F-CMS
 *  FileName     : SADM00.java
 *  Version      : 1.0
 *  Description  : APP > APP공통 > Reload (iBatis XML 쿼리, Task Map XML)
 * -----------------------------------------------------------------------------
 *  Modify Hist. :  Date        Author  Description
 *                 -----------  ------  ----------------------------------------
 *                  2012.09.01  ㅇㅇㅇ  초기생성/등록
 * -----------------------------------------------------------------------------
 *  Copyrights 2012 by FINGER,Inc. All Rights Reserved.
 * =============================================================================
 */
package com.finger.exchain.tasks.sadm;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.finger.agent.msg.FixedMsg;
import com.finger.fwf.core.base.BizModuleClass;
import com.finger.fwf.core.config.FWFApsConfig;
import com.finger.fwf.core.dbm.SqlMapConfig;
import com.finger.protocol.fwtp.FwtpMessage;
import com.finger.tools.dbm.QueryExecutor;
import com.ibatis.sqlmap.client.SqlMapClient;

public class SADM00 extends BizModuleClass {
    private static Logger logger = null;
    private static String C_sqlMap_Name = null;

    public SADM00() {
        logger = Logger.getLogger(this.getClass());
        C_sqlMap_Name = this.getClass().getName().substring(this.getClass().getName().lastIndexOf(".")+1);
    }
    public void init() throws Exception {}
    public void close() {}

    /**
     * <p>요청정보에 따른 비즈니스 업무를 호출함.</p>
     * @throws Exception
     */
    @Override
    public FwtpMessage execute(String action, FwtpMessage in) throws Exception {
        logger.debug(this.getClass().getName()+ " cmdExecute start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        FwtpMessage out = in;

        if (action.equals("fncSADM00000")) {			//SERVER ENVIRONMENT INFORMATION
            out = fncSADM00000(in);
        }
        else if (action.equals("fncSADM00010")) {		//시스템정보
            out = fncSADM00010(in);
        }
        else {
            out.setMessageCode(FixedMsg._MSG_TASK_T0001);
        }

        logger.debug(this.getClass().getName()+ " cmdExecute finish ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        return out;
    }

    /**
     * 공통 - SERVER ENVIRONMENT INFORMATION
     * @param in
     * @return
     */
    @SuppressWarnings("unchecked")
	private FwtpMessage fncSADM00000(FwtpMessage in) {
        SqlMapClient sqlMap = super.getSqlMap();
        if (sqlMap==null) return null;

        QueryExecutor qe = new QueryExecutor();
        try {
            LinkedHashMap<String, String> _map = in.getParameterMap();

            //운영,개발서버 구분
            if( "REAL".equals(FWFApsConfig.REAL_DEV_SVR_CHK) ){
                _map.put("is_real_server", "TRUE" );
            }else{
            	_map.put("is_real_server", "FALSE" );
            }

            _map.put("db_driver",   SqlMapConfig.getDB_Driver());
            _map.put("db_url",      SqlMapConfig.getDB_Url());
            _map.put("db_username", SqlMapConfig.getDB_UserName());
            _map.put("db_password", SqlMapConfig.getDB_PassWord());

            List<LinkedHashMap<String, Object>> listData = null;
            listData = (List<LinkedHashMap<String, Object>>)sqlMap.queryForList(C_sqlMap_Name+".SADM00000", _map );

            qe.setResult(in, listData);
        }
        catch(Exception ex){
            in.setMessageCode(FixedMsg._MSG_TASK_T9999);
            in.addErrorMessage(ex.getMessage());
            logger.error(ex.toString(), ex);
        }
        finally {
        }

        return in;
    }

    /**
     * 공통 - Sbox_시스템정보_조회
     * @param in
     * @return
     */
    @SuppressWarnings("unchecked")
	private FwtpMessage fncSADM00010(FwtpMessage in) {
        SqlMapClient sqlMap = super.getSqlMap();
        if (sqlMap==null) return null;

        QueryExecutor qe = new QueryExecutor();
        try {

            LinkedHashMap<String, String> _map = in.getParameterMap();

            List<LinkedHashMap<String, Object>> listData = null;
            listData = (List<LinkedHashMap<String, Object>>)sqlMap.queryForList(C_sqlMap_Name+".SADM00010", _map);

            qe.setResult(in, listData);
        }
        catch(Exception ex){
            in.setMessageCode(FixedMsg._MSG_TASK_T9999);
            in.addErrorMessage(ex.getMessage());
            logger.error(ex.toString(), ex);
        }
        finally {
        }

        return in;
    }

    /**
     * 공통 - Sbox_법인정보_조회
     * @param in
     * @return
     */
    @SuppressWarnings("unchecked")
	private FwtpMessage fncSADM00020(FwtpMessage in) {
        SqlMapClient sqlMap = super.getSqlMap();
        if (sqlMap==null) return null;

        QueryExecutor qe = new QueryExecutor();
        try {
            LinkedHashMap<String, String> _map = in.getParameterMap();
            _map.put("sys_id"    , in.getSysMgtNo());  //시스템ID

            List<LinkedHashMap<String, Object>> listData = null;
            listData = (List<LinkedHashMap<String, Object>>)sqlMap.queryForList(C_sqlMap_Name+".SADM00020", _map);

            qe.setResult(in, listData);
        }
        catch(Exception ex){
            in.setMessageCode(FixedMsg._MSG_TASK_T9999);
            in.addErrorMessage(ex.getMessage());
            logger.error(ex.toString(), ex);
        }
        finally {
        }

        return in;
    }

    /**
     * 공통 - Sbox_사업장정보_조회
     * @param in
     * @return
     */
    @SuppressWarnings("unchecked")
	private FwtpMessage fncSADM00030(FwtpMessage in) {
        SqlMapClient sqlMap = super.getSqlMap();
        if (sqlMap==null) return null;

        QueryExecutor qe = new QueryExecutor();
        try {
            LinkedHashMap<String, String> _map = in.getParameterMap();
            _map.put("sys_id"    , in.getSysMgtNo());  //시스템ID

            List<LinkedHashMap<String, Object>> listData = null;
            listData = (List<LinkedHashMap<String, Object>>)sqlMap.queryForList(C_sqlMap_Name+".SADM00030", _map);

            qe.setResult(in, listData);
        }
        catch(Exception ex){
            in.setMessageCode(FixedMsg._MSG_TASK_T9999);
            in.addErrorMessage(ex.getMessage());
            logger.error(ex.toString(), ex);
        }
        finally {
        }

        return in;
    }

    /**
     * 공통코드 - 조회(+조건)
     * @param in
     * @return
     */
    @SuppressWarnings("unchecked")
    private FwtpMessage fncSADM00110(FwtpMessage in) {
        SqlMapClient sqlMap = super.getSqlMap();
        if (sqlMap==null) return null;

        QueryExecutor qe = new QueryExecutor();
        try {
            LinkedHashMap<String, String> _map = in.getParameterMap();
            _map.put("sys_id"    , in.getSysMgtNo());  //시스템ID

            List<LinkedHashMap<String, Object>> listData = null;
            listData = (List<LinkedHashMap<String, Object>>)sqlMap.queryForList(C_sqlMap_Name+".SADM00110", _map);

            qe.setResult(in, listData);
        }
        catch(Exception ex){
            in.setMessageCode(FixedMsg._MSG_TASK_T9999);
            in.addErrorMessage(ex.getMessage());
            logger.error(ex.toString(), ex);
        }
        finally {
        }

        return in;
    }

    /**
     * 거래현황
     * @param in
     * @return
     */
    @SuppressWarnings("unchecked")
    private FwtpMessage fncSADM00120(FwtpMessage in) {
        SqlMapClient sqlMap = super.getSqlMap();
        if (sqlMap==null) return null;

        QueryExecutor qe = new QueryExecutor();
        try {

            LinkedHashMap<String, String> _map = in.getParameterMap();
            _map.put("sys_id"    , in.getSysMgtNo());  //시스템ID

            List<LinkedHashMap<String, Object>> listData = null;
            listData = (List<LinkedHashMap<String, Object>>)sqlMap.queryForList(C_sqlMap_Name+".SADM00120", _map);

            qe.setResult(in, listData);
        }
        catch(Exception ex){
            in.setMessageCode(FixedMsg._MSG_TASK_T9999);
            in.addErrorMessage(ex.getMessage());
            logger.error(ex.toString(), ex);
        }
        finally {
        }

        return in;
    }

    /**
     * BOS 판매목록조회
     * @param in
     * @return
     */
    @SuppressWarnings("unchecked")
    private FwtpMessage fncSADM00130(FwtpMessage in) {
        SqlMapClient sqlMap = super.getSqlMap();
        if (sqlMap==null) return null;

        QueryExecutor qe = new QueryExecutor();
        try {

            LinkedHashMap<String, String> _map = in.getParameterMap();
            _map.put("sys_id"    , in.getSysId());  //코인명ID

            List<LinkedHashMap<String, Object>> listData = null;
            listData = (List<LinkedHashMap<String, Object>>)sqlMap.queryForList(C_sqlMap_Name+".SADM00130", _map);

            qe.setResult(in, listData);
        }
        catch(Exception ex){
            in.setMessageCode(FixedMsg._MSG_TASK_T9999);
            in.addErrorMessage(ex.getMessage());
            logger.error(ex.toString(), ex);
        }
        finally {
        }

        return in;
    }

    /**
     * ETH 판매목록조회
     * @param in
     * @return
     */
    @SuppressWarnings("unchecked")
    private FwtpMessage fncSADM00130_ETH(FwtpMessage in) {
        SqlMapClient sqlMap = super.getSqlMap();
        if (sqlMap==null) return null;

        QueryExecutor qe = new QueryExecutor();
        try {

            LinkedHashMap<String, String> _map = in.getParameterMap();
            _map.put("sys_id"    , in.getSysId());  //코인명ID

            List<LinkedHashMap<String, Object>> listData = null;
            listData = (List<LinkedHashMap<String, Object>>)sqlMap.queryForList(C_sqlMap_Name+".SADM00130_ETH", _map);

            qe.setResult(in, listData);
        }
        catch(Exception ex){
            in.setMessageCode(FixedMsg._MSG_TASK_T9999);
            in.addErrorMessage(ex.getMessage());
            logger.error(ex.toString(), ex);
        }
        finally {
        }

        return in;
    }
    
    /**
     * ERC20 판매목록조회
     * @param in
     * @return
     */
    @SuppressWarnings("unchecked")
    private FwtpMessage fncSADM00130_ERC20(FwtpMessage in) {
        SqlMapClient sqlMap = super.getSqlMap();
        if (sqlMap==null) return null;

        QueryExecutor qe = new QueryExecutor();
        try {

            LinkedHashMap<String, String> _map = in.getParameterMap();
            _map.put("sys_id"    , in.getSysId());  //코인명ID

            List<LinkedHashMap<String, Object>> listData = null;
            listData = (List<LinkedHashMap<String, Object>>)sqlMap.queryForList(C_sqlMap_Name+".SADM00130_ERC20", _map);

            qe.setResult(in, listData);
        }
        catch(Exception ex){
            in.setMessageCode(FixedMsg._MSG_TASK_T9999);
            in.addErrorMessage(ex.getMessage());
            logger.error(ex.toString(), ex);
        }
        finally {
        }

        return in;
    }
    
    
    /**
     * BOS 챠트조회 일별
     * @param in
     * @return
     */
    @SuppressWarnings("unchecked")
    private FwtpMessage fncSADM00140(FwtpMessage in) {
        SqlMapClient sqlMap = super.getSqlMap();
        if (sqlMap==null) return null;

        QueryExecutor qe = new QueryExecutor();
        try {

            LinkedHashMap<String, String> _map = in.getParameterMap();
            _map.put("sys_id"    , in.getSysId());  //코인명ID

            List<LinkedHashMap<String, Object>> listData = null;
            listData = (List<LinkedHashMap<String, Object>>)sqlMap.queryForList(C_sqlMap_Name+".SADM00140", _map);

            qe.setResult(in, listData);
        }
        catch(Exception ex){
            in.setMessageCode(FixedMsg._MSG_TASK_T9999);
            in.addErrorMessage(ex.getMessage());
            logger.error(ex.toString(), ex);
        }
        finally {
        }

        return in;
    }

    /**
     * ETH 챠트조회 일별
     * @param in
     * @return
     */
    @SuppressWarnings("unchecked")
    private FwtpMessage fncSADM00140_ETH(FwtpMessage in) {
        SqlMapClient sqlMap = super.getSqlMap();
        if (sqlMap==null) return null;

        QueryExecutor qe = new QueryExecutor();
        try {

            LinkedHashMap<String, String> _map = in.getParameterMap();
            _map.put("sys_id"    , in.getSysId());  //코인명ID

            List<LinkedHashMap<String, Object>> listData = null;
            listData = (List<LinkedHashMap<String, Object>>)sqlMap.queryForList(C_sqlMap_Name+".SADM00140_ETH", _map);

            qe.setResult(in, listData);
        }
        catch(Exception ex){
            in.setMessageCode(FixedMsg._MSG_TASK_T9999);
            in.addErrorMessage(ex.getMessage());
            logger.error(ex.toString(), ex);
        }
        finally {
        }

        return in;
    }
    
    /**
     * ERC20 챠트조회 일별
     * @param in
     * @return
     */
    @SuppressWarnings("unchecked")
    private FwtpMessage fncSADM00140_ERC20(FwtpMessage in) {
        SqlMapClient sqlMap = super.getSqlMap();
        if (sqlMap==null) return null;

        QueryExecutor qe = new QueryExecutor();
        try {

            LinkedHashMap<String, String> _map = in.getParameterMap();
            _map.put("sys_id"    , in.getSysId());  //코인명ID

            List<LinkedHashMap<String, Object>> listData = null;
            listData = (List<LinkedHashMap<String, Object>>)sqlMap.queryForList(C_sqlMap_Name+".SADM00140_ERC20", _map);

            qe.setResult(in, listData);
        }
        catch(Exception ex){
            in.setMessageCode(FixedMsg._MSG_TASK_T9999);
            in.addErrorMessage(ex.getMessage());
            logger.error(ex.toString(), ex);
        }
        finally {
        }

        return in;
    }
    
    /**
     * 챠트조회 시간별
     * @param in
     * @return
     */
    @SuppressWarnings("unchecked")
    private FwtpMessage fncSADM00150(FwtpMessage in) {
    	SqlMapClient sqlMap = super.getSqlMap();
    	if (sqlMap==null) return null;

    	QueryExecutor qe = new QueryExecutor();
    	try {

            LinkedHashMap<String, String> _map = in.getParameterMap();
            _map.put("sys_id"    , "BOS");  //코인명ID

    		List<LinkedHashMap<String, Object>> listData = null;
    		listData = (List<LinkedHashMap<String, Object>>)sqlMap.queryForList(C_sqlMap_Name+".SADM00150", _map);

    		qe.setResult(in, listData);
    	}
    	catch(Exception ex){
    		in.setMessageCode(FixedMsg._MSG_TASK_T9999);
    		in.addErrorMessage(ex.getMessage());
    		logger.error(ex.toString(), ex);
    	}
    	finally {
    	}

    	return in;
    }


    /**
     * 스케쥴 서버 IP 수정
     *
     * @param in
     * @return
     */
	private FwtpMessage fncSADM00200(FwtpMessage in) {

    	SqlMapClient sqlMap = super.getSqlMap();

    	try {

	    	//자신의 서버 IP
	    	String serverip = InetAddress.getLocalHost().getHostAddress();

    		HashMap<String, String> paramMap = in.getParameterMap();
    		paramMap.put("dual_exec_svr_ip", serverip);

			sqlMap.update(C_sqlMap_Name+".SADM00200", paramMap);

    	} catch (Exception e) {

    		in.setMessageCode(FixedMsg._MSG_SQLM_Q9999);
    		in.addErrorMessage(e.getMessage());
    		logger.error(in, e);
    	} finally {
    	}

    	return in;
    }

    /**
     * 시스템 거래 로그
     *
     * @param in
     * @return
     */
	private FwtpMessage fncSADM00300(FwtpMessage in) {

    	SqlMapClient sqlMap = super.getSqlMap();

    	try {

    		HashMap<String, String> paramMap = in.getParameterMap();

			sqlMap.insert(C_sqlMap_Name+".SADM00300", paramMap);

    	} catch (Exception e) {

    		in.setMessageCode(FixedMsg._MSG_SQLM_Q9999);
    		in.addErrorMessage(e.getMessage());
    		logger.error(in, e);
    	} finally {
    	}

    	return in;
    }

    /**
     * 시스템 에러 로그
     *
     * @param in
     * @return
     */
	private FwtpMessage fncSADM00400(FwtpMessage in) {

    	SqlMapClient sqlMap = super.getSqlMap();

    	try {

    		HashMap<String, String> paramMap = in.getParameterMap();

			sqlMap.insert(C_sqlMap_Name+".SADM00400", paramMap);

    	} catch (Exception e) {

    		in.setMessageCode(FixedMsg._MSG_SQLM_Q9999);
    		in.addErrorMessage(e.getMessage());
    		logger.error(in, e);
    	} finally {
    	}

    	return in;
    }

	 /**
     * 공휴일 및 거래가능시간 체크
     * @param in
     * @return
     */
	private FwtpMessage fncSADM00500(FwtpMessage in) {

    	SqlMapClient sqlMap = super.getSqlMap();
        QueryExecutor qe = new QueryExecutor();

    	try {

    		HashMap<String, String> paramMap = in.getParameterMap();
    		paramMap.put("sys_id" , in.getSysMgtNo());

    		// 공휴일 및 거래가능시간을 체크해서 Y/N으로 결과를 받습니다.
    		// Y : 거래가능, N : 거래불가
    		String result = (String)sqlMap.queryForObject(C_sqlMap_Name+".SADM00500", paramMap);

    		logger.debug("=========================================================================");
    		logger.debug("거래가능여부 : " + result);
    		logger.debug("=========================================================================");

    		ArrayList<LinkedHashMap<String, Object>> resulData = new ArrayList<LinkedHashMap<String, Object>> ();
    		LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
        	resultMap.put("result"  , result);
        	resulData.add(resultMap);

			SYSCOM.makeReponseList(resulData);

    		logger.debug("=========================================================================");
    		logger.debug("resulData     : " + resulData);
    		logger.debug("=========================================================================");

			qe.setResult(in, resulData);
    	} catch (Exception e) {

    		in.setMessageCode(FixedMsg._MSG_SQLM_Q9999);
    		in.addErrorMessage(e.getMessage());
    		logger.error(in, e);
    	} finally {
    	}

    	return in;
    }

	/**
     * 지갑공개주소 등록 유무 조회
     * @param in
     * @return
     */
	private FwtpMessage fncSADM00710(FwtpMessage in) {
    	SqlMapClient sqlMap = super.getSqlMap();
        QueryExecutor qe = new QueryExecutor();

    	try {
    		HashMap<String, String> paramMap = in.getParameterMap();
    		paramMap.put("sys_id", 	   in.getSysMgtNo());
    		paramMap.put("cust_seqno", in.getCustSeqno());

    		// 고객의 지갑공개주소 건수를 확인해 존재 유무를 판단.  
    		int walletAddrCnt = (Integer)sqlMap.queryForObject(C_sqlMap_Name + ".SADM00710", paramMap);

    		logger.debug("=========================================================================");
    		logger.debug("지갑공개주소 건수 : " + walletAddrCnt);
    		logger.debug("=========================================================================");

    		ArrayList<LinkedHashMap<String, Object>> resultData = new ArrayList<LinkedHashMap<String, Object>> ();
    		LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
        	resultMap.put("wallet_addr_cnt", walletAddrCnt);
        	resultData.add(resultMap);

			SYSCOM.makeReponseList(resultData);

    		logger.debug("=========================================================================");
    		logger.debug("resultData     : " + resultData);
    		logger.debug("=========================================================================");

			qe.setResult(in, resultData);
    	} catch (Exception e) {
    		in.setMessageCode(FixedMsg._MSG_SQLM_Q9999);
    		in.addErrorMessage(e.getMessage());
    		logger.error(in, e);
    	} finally {
    	}

    	return in;
    }
	
	/**
     * ERC20 토큰조회
     * @param in
     * @return
     */
    @SuppressWarnings("unchecked")
    private FwtpMessage fncSADM00800(FwtpMessage in) {
        SqlMapClient sqlMap = super.getSqlMap();
        if (sqlMap==null) return null;

        QueryExecutor qe = new QueryExecutor();
        try {

    		HashMap<String, String> paramMap = in.getParameterMap();
    		
    		paramMap.put("sys_id", 	       in.getSysId());
    		paramMap.put("coin_type", 	   in.getParameter("COIN_TYPE"));
    		
            List<LinkedHashMap<String, Object>> listData = null;
            listData = (List<LinkedHashMap<String, Object>>)sqlMap.queryForList(C_sqlMap_Name+".SADM00800", paramMap);

    		logger.debug("=========================================================================");
    		logger.debug("코인정책 조회     : " + listData);
    		logger.debug("=========================================================================");
    		
            qe.setResult(in, listData);
        }
        catch(Exception ex){
            in.setMessageCode(FixedMsg._MSG_TASK_T9999);
            in.addErrorMessage(ex.getMessage());
            logger.error(ex.toString(), ex);
        }
        finally {
        }

        return in;
    }
}