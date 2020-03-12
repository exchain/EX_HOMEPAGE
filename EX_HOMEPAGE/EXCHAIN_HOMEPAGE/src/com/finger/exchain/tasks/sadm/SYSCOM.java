/* =============================================================================
 *  Project      : F-CMS
 *  FileName     : SYSCOM.java
 *  Version      : 1.0
 *  Description  : APP > APP공통 > 시스템정보
 * -----------------------------------------------------------------------------
 *  Modify Hist. :  Date        Author  Description
 *                 -----------  ------  ----------------------------------------
 *                  2012.12.05  손동학  초기생성/등록
 * -----------------------------------------------------------------------------
 *  Copyrights 2012 by FINGER,Inc. All Rights Reserved.
 * =============================================================================
 */
package com.finger.exchain.tasks.sadm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;

import com.finger.agent.util.AgentUtil;
import com.ibatis.sqlmap.client.SqlMapClient;

public class SYSCOM {
/*public class SYSCOM extends BizModuleClass {*/
    private static Logger logger = Logger.getLogger(SYSCOM.class);
    private static String C_sqlMap_Name = SYSCOM.class.getName().substring(SYSCOM.class.getName().lastIndexOf(".")+1);

/* ========================================================================================================== */

    /**
     * 현재날짜/시간 얻기 - YYYYMMDDHH24MISSFF / 20자리
     * @return
     */
    public static String nowDatetime(SqlMapClient sqlMap) {
        if (sqlMap==null) return null;

        String result = "";
        try{
            result = (String)sqlMap.queryForObject(C_sqlMap_Name+".Now_Datetime");
        }
        catch(Exception e){
            logger.error(e.getMessage(), e);
        }
        finally { }

        return result;
    }
 
    /**
     * 시스템정보 조회
     *
     * @param sysMgtNo
     * @return
     */
    @SuppressWarnings("unchecked")
    public static HashMap<String, Object> getSystemInfo(SqlMapClient sqlMap, String sysMgtNo) {
        if (sqlMap==null) return null;

        HashMap<String, Object> resultMap = null;
        try{
            resultMap = (HashMap<String, Object>)sqlMap.queryForObject(C_sqlMap_Name+".System_Info", sysMgtNo);
        }
        catch(Exception e){
            logger.error(e.getMessage(), e);
        }
        finally { }

        return resultMap;
    }

    @SuppressWarnings("unchecked")
    public static HashMap<String, Object> getFirmInfo(SqlMapClient sqlMap, String bizSeqNo, String fbkDivCd, String srvDivCd, String bank_cd) {
        if (sqlMap==null) return null;

        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("biz_seq_no", bizSeqNo);
        paramMap.put("fbk_div_cd", fbkDivCd);
        paramMap.put("srv_div_cd", srvDivCd);
        paramMap.put("bank_cd"   , bank_cd);

        HashMap<String, Object> resultMap = null;
        try{
            resultMap = (HashMap<String, Object>)sqlMap.queryForObject(C_sqlMap_Name+".Firm_Info", paramMap);
        }
        catch(Exception e){
            logger.error(e.getMessage(), e);
        }
        finally {
            if (resultMap == null) {
                resultMap = new HashMap<String, Object>();
            }
        }

        return resultMap;
    }

    /**
     * 외부데이터 전송용 전문번호(채번)
     * @return 전문번호
     */
    public static synchronized String createMsgNo(SqlMapClient sqlMap, String sys_mgt_no, String trns_dt) {
        if (sqlMap==null) return null;

        String _msg_no = "";
        try{
            HashMap<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("sys_mgt_no", sys_mgt_no);
            paramMap.put("msg_div", "01");
            paramMap.put("trns_dt", trns_dt);

            _msg_no = (String)sqlMap.queryForObject(C_sqlMap_Name+".select_MsgNo", paramMap);
            paramMap.put("msg_no", _msg_no);
            if (Integer.parseInt(_msg_no) > 1)
                sqlMap.update(C_sqlMap_Name+".update_MsgNo", paramMap);
            else
                sqlMap.insert(C_sqlMap_Name+".insert_MsgNo", paramMap);
        }
        catch(Exception e){
            logger.error(e.getMessage(), e);
        }
        finally { }

        return _msg_no;
    }

    /**
     * 입력값을 파싱하여 맵형식으로 리턴한다.
     * @param keyNames   key 배열  ex.{"key1","key2"}
     * @param valSizes   size 배열 ex.{ 5, 10 }
     * @param inputData  ex."123450123456789"
     * @return
     */
    public static LinkedHashMap<String, String> parseStringOnFormat(String[] keyNames, int[] valSizes, String inputData) {

        if (keyNames == null || valSizes == null || inputData == null)
            return null;
        if (keyNames.length != valSizes.length)
            return null;

        int nTotalSize = 0;
        for (int ii=0; ii<valSizes.length; ii++)
            nTotalSize += valSizes[ii];

        if (inputData.getBytes().length != nTotalSize)
            return null;

        LinkedHashMap<String, String> resultMap = new LinkedHashMap<String, String>();

        int offset = 0;
        for (int ii=0; ii<keyNames.length; ii++)
        {
            String _item = new String( inputData.getBytes(), offset, valSizes[ii] );
            resultMap.put( keyNames[ii], _item );
            offset += valSizes[ii];
        }

        return resultMap;
    }

    /**
     * 입력값을 파싱하여 배열형식으로 리턴한다.
     * 
     * @param valSizes		size 배열 ex.{ 5, 10 }
     * @param inputData		ex."123450123456789"
     * @return
     */
    public static String[] parseStringOnFormat(int[] valSizes, String inputData) {
        /*int nTotalSize = 0;
        for (int ii=0; ii<valSizes.length; ii++) {
            nTotalSize += valSizes[ii];
        }*/

        String[] parseData = new String[valSizes.length];

        int offset = 0;
        for (int i=0; i<parseData.length; i++) {
            String data = new String( inputData.getBytes(), offset, valSizes[i] );
            parseData[i] = data;
            offset += valSizes[i];
        }

        return parseData;
    }

    /**
     * 응답리스트를 만든다.
     * 
     * @param resultList
     */
    public static void makeReponseList(ArrayList<LinkedHashMap<String,Object>> resultList) {

    	if (resultList.size() == 0) {
    		return;
    	}

    	LinkedHashMap<String, Object> dataMap = (LinkedHashMap<String, Object>)resultList.get(0);
    	LinkedHashMap<String, Object> fieldNameMap = new LinkedHashMap<String, Object>();
    	LinkedHashMap<String, Object> fieldTypeMap = new LinkedHashMap<String, Object>();
    	LinkedHashMap<String, Object> fieldSizeMap = new LinkedHashMap<String, Object>();

    	Iterator<String> keyIter = dataMap.keySet().iterator();
		while (keyIter.hasNext()) {
			String key = (String)keyIter.next();
			Object value = dataMap.get(key);

			fieldNameMap.put(key, key);
			fieldTypeMap.put(key, "VARCHAR");

			if (value == null) {
				fieldSizeMap.put(key, "0");
			} else {
				fieldSizeMap.put(key, Integer.toString(value.toString().length()));	
			}
		}

		resultList.add(0, fieldNameMap);
		resultList.add(1, fieldTypeMap);
		resultList.add(2, fieldSizeMap);
    }

    /**
     * 펌뱅킹 전문 응답코드에 대한 메세지 조회
     * 기관구분 inst_div(20 : 실시간, 30: 공과금) 
     */
    public static String getErrCodeMsg(SqlMapClient sqlMap, String bank_cd, String inst_div, String err_cd) {
        if (sqlMap==null) return null;

        String errMsg = "";
        
        if (AgentUtil.isNull(inst_div) ||  AgentUtil.isNull(bank_cd)  || AgentUtil.isNull(err_cd) )
            return errMsg;

        try{
        	LinkedHashMap<String, Object> paramMap = new LinkedHashMap<String, Object>();
            
            paramMap.put("inst_div"	, inst_div);
            paramMap.put("bank_cd"	, bank_cd);
            paramMap.put("err_cd"	, err_cd);
            
            errMsg = (String)sqlMap.queryForObject(C_sqlMap_Name+".errCodeMsg", paramMap);
        }
        catch(Exception e){
            logger.error(e.getMessage(), e);
        }
        return errMsg;
    }
}