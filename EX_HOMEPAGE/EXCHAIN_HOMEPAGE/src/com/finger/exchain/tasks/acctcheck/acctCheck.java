/* =============================================================================
 *  Project      : DEPOSIT DEMO
 *  FileName     : krwDeposit.java
 *  Version      : 1.0
 *  Description  : 가상계좌 원화입금 솔루션
 * -----------------------------------------------------------------------------
 *  Modify Hist. :  Date        Author  Description
 *                 -----------  ------  ----------------------------------------
 *                  2018.11.01  이성주       초기생성/등록
 * -----------------------------------------------------------------------------
 *  Copyrights 2018 by EXCHAIN,Inc. All Rights Reserved.
 * =============================================================================
 */
package com.finger.exchain.tasks.acctcheck;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.finger.agent.msg.FixedMsg;
import com.finger.fwf.core.base.BizModuleClass;
import com.finger.fwf.core.config.FWFApsConfig;
import com.finger.fwf.core.dbm.SqlMapConfig;
import com.finger.protocol.fwtp.FwtpMessage;
import com.finger.tools.dbm.QueryExecutor;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.finger.exchain.tasks.sadm.SYSCOM;

public class acctCheck extends BizModuleClass {
    private static Logger logger = null;
    private static String C_sqlMap_Name = null;

    public acctCheck() {
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

        if (action.equals("fncACCT00010")) {			// 예금주명 조회
            out = fncACCT00010(in);
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
	private FwtpMessage fncACCT00010(FwtpMessage in) {
    	
        SqlMapClient sqlMap = super.getSqlMap();
        if (sqlMap==null) return null;

        QueryExecutor qe = new QueryExecutor();
        try {
        	
    		// 조회요청할 데이터 셋팅
	        Map<String,Object> params = new LinkedHashMap<>(); 
	        
	        params.put("ex_cust_id" , in.getParameter("ex_cust_id"));		// 고객아이디
	        params.put("ex_svc_cd"  , in.getParameter("ex_svc_cd"));		// 서비스코드
	        params.put("bank_cd"   	, in.getParameter("bank_cd"));			// 은행코드
	        params.put("acct_no"   	, in.getParameter("acct_no"));			// 계좌번호
	        params.put("acct_nm"   	, in.getParameter("acct_nm"));			// 고객명
	 
	        StringBuilder paramData = new StringBuilder();
	        
	        for(Map.Entry<String,Object> param : params.entrySet()) 
	        {
	            if(paramData.length() != 0) 
	            	paramData.append('&');

	            paramData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
	            paramData.append('=');
	            paramData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
	        }

	        // GET 방식 
	        /*
	        URL url = new URL("http://13.125.188.197:8750/api/bank/EX000100?"  +paramData.toString());
	        
	        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	        conn.setRequestMethod("GET");
	        conn.setRequestProperty("Accept", "application/json");
	        */
	        
	        // POST 방식
			URL url = new URL("https://dev.2xchange.co.kr:9443/ASP/api/bank/EX0110");		// 개발
			//URL url = new URL("https://www.2xchange.co.kr/ASP/api/bank/EX0110");		// 운영
	        
	        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	        
	        byte[] postDataBytes = paramData.toString().getBytes("UTF-8");
	       
	        conn.setRequestMethod("POST");
	        conn.setRequestProperty("Accept", "application/json");
	        conn.setDoOutput(true);
	        conn.getOutputStream().write(postDataBytes);

	        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
	 
	        StringBuffer sb = new StringBuffer();
	        String data;
	        
	        while((data = br.readLine()) != null) { 
	        	 sb.append(data);
	        }
	        
	        br.close();

	        // 조회 결과 파싱
			JsonParser jsonParser = new JsonParser();
			JsonObject jsonObject = (JsonObject) jsonParser.parse(sb.toString()).getAsJsonObject();

	        // 조회 결과 출력

    		ArrayList<LinkedHashMap<String, Object>> resultData = new ArrayList<LinkedHashMap<String, Object>> ();
    		LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
    		
     		//resultMap.put("rst_name", jsonObject.get("rst_name").getAsString());	// 조회된예금주명
    		resultMap.put("rst_code", jsonObject.get("rst_code").getAsString());	// 결과코드
    		resultMap.put("rst_msg" , jsonObject.get("rst_msg").getAsString());		// 결과메세지
    		
        	resultData.add(resultMap);

			SYSCOM.makeReponseList(resultData);

			qe.setResult(in, resultData);
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