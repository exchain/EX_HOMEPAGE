package com.finger.fwf.uxui.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.finger.agent.config.FcAgentConfig;
import com.finger.agent.exception.AgentException;
import com.finger.agent.msg.FixedMsg;
import com.finger.agent.msg.MsgManager;
import com.finger.agent.msg.MsgObj;
import com.finger.fwf.uxui.CommonCarts;
import com.finger.fwf.uxui.UserInfo;
import com.finger.fwf.uxui.util.HttpServletUX;
import com.finger.fwf.uxui.util.ResponseUX;
import com.finger.fwf.uxui.util.UCS;
import com.finger.fwf.uxui.util.UXUtil;
import com.finger.protocol.fwtp.FwtpHeader;
import com.finger.protocol.fwtp.FwtpMessage;
import com.finger.tools.cipher.Crypto;

public class UXActionService extends BasisService {
    private static Logger logger = Logger.getLogger(UXActionService.class);

    private static UXActionService _instance = new UXActionService();

    protected UXActionService(){
        logger.debug(":: "+ this.getClass() +" ~~~~~~~~~~~~~~~~~~~~~");
    }

    public static UXActionService getInstance() {
        logger.debug(":: getInstance() ~~~~~~~~~~~~~~~~~~~~~");
        return _instance;
    }

    /**
     * 요청정보를 서버에 전송하고 결과를 리턴받아 오는 서비스
     * @param request
     * @param response
     * @throws IOException 
     * @throws ServletException 
     */
    public void doUXService(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.debug(":: doUXService() ~~~~~~~~~~~~~~~~~~~~~");

        HttpServletUX.setResponseContentAndHeader( response );

        FwtpMessage resultMessage = null;

        MsgObj _msg_ = null;
        try {

            if (getParametersSize(request, logger) > 0)
            {
                String task_id = getReqTaskId(request);	//request.getParameter("_TASK_ID_");

                if ( !UXUtil.isNull(task_id) )
                {
                    FwtpMessage fwtp = new FwtpMessage();

                    String   sys_id     = "EXCHAIN";

                    fwtp.setClientIp( request.getRemoteAddr() ); 
                    fwtp.setSysMgtNo(sys_id);
                    fwtp.setTaskId( task_id );
                    fwtp.setDataType( getReqDataType(request) );
                    fwtp.setRequestData( parameterExtractList(request) );
                    fwtp.setPageId(getProgramId(request));                    
                    fwtp.setSysId(sys_id );

                    if ( !fwtp.validateHeaderMap(fwtp.getHeader()) )
                        throw new AgentException(new MsgObj(FixedMsg._MSG_SYSU_S9999, "Request for the information is not valid."));	//요청을 위한 정보가 유효하지 않습니다.
                    
                    if( FwtpHeader._DATATYPE_MEMORY.equals(fwtp.getDataType()))
                    {
                    	//메모리 데이터 호출인 경우 처리
                    	resultMessage = doFwfMemoryDataListForUx( fwtp , sys_id);
                    	
                    }else {
                        resultMessage = com.finger.agent.FWFTaskByUXUI.doFWFAgentListForUX ( fwtp );                    	
                    }

                    if (FwtpHeader._RESPONSE_OKAY_.equals(resultMessage.getResponseCode()))
                    {
                        if (FwtpHeader._DATATYPE_GRID.equals(fwtp.getDataType()) || FwtpHeader._DATATYPE_MEMORY.equals(fwtp.getDataType()) )
                        {
                        	decryptMakeGridResponseData ( request, resultMessage );
                            ResponseUX.toGsonByActionResultGrid( request, response, resultMessage );
                        }
                        else if (FwtpHeader._DATATYPE_COMMON.equals(fwtp.getDataType()))
                        {
                        	decryptMakeResponseResultData ( request, resultMessage );
                            ResponseUX.toGsonByActionResult( request, response, resultMessage );
                        }
                        else if (FwtpHeader._DATATYPE_CHART.equals(fwtp.getDataType()))
                        {
                        	decryptMakeGridResponseData ( request, resultMessage );
                            ResponseUX.toGsonByActionResultChart( request, response, resultMessage );
                        }
                        //--request.setAttribute("resultMessage", new Gson().toJson(resultMessage) );
                        //--request.getRequestDispatcher( next_url ).forward( request, response );
                        

                        // 회원정보 변경 후 세션 재로딩 처리
                        // MB2010030:이메일인증완료처리, MB2020020:휴대폰본인인증완료처리, MB2040010:계좌인증, MB2050020:코인계좌인증처리
                        if( task_id.equals("MB2010030") || task_id.equals("MB2020020") || task_id.equals("MB2040010") || task_id.equals("MB2050020") )
                        {
                        	//세션담기(로그인정보)
                        	UXLoginService uxLoginServie = UXLoginService.getInstance();
                        	uxLoginServie.doLoginReload(request);
                        }
                        	
                    }
                    else 
                    {
                    	throw new AgentException(new MsgObj(resultMessage.getMessageCode(), resultMessage.getErrorMessage()), resultMessage.getResponseCode());
                    }
                }	//if ( !UXUtil.isNull(task_id) )
                else {
                    throw new AgentException(new MsgObj(FixedMsg._MSG_TASK_T0000, "Please contact the system administrator."));
                }
            }
            else {
                throw new AgentException(new MsgObj(FixedMsg._MSG_TASK_T0002, "Please contact the system administrator."));
            }
        } catch (AgentException ae) {
            ae.printStackTrace();
            _msg_ = MsgManager.getInstance().createMessage(ae);
            /*try { response.sendError(500, ee.toString()); } catch (IOException e1) { e1.printStackTrace(); }*/
        } catch (Exception ex) {
            ex.printStackTrace();
            _msg_ = new MsgObj(FixedMsg._MSG_SYSU_S9999, ex.getMessage());
        } finally {
        	
        	/*
            //이중화 서버IP DB 업데이트 여부
            if( FcAgentConfig._AP_SERVER_DUAL_USE && "Y".equals(FcAgentConfig._DUAL_SVR_IP_UP_YN) ){
            	
    			logger.debug("************************************************************************");
    			logger.debug("이중화서버구성여부가 'Y'이고 이중화 서버IP DB 업데이트 여부가 'Y'인 경우");
    			logger.debug("************************************************************************");
            	
            	updateDualServerIP(request);	
            }
            */
        	
            ResponseUX.toGsonByActionError( request, response, _msg_ );
        }
    }
    
    public FwtpMessage doFwfMemoryDataListForUx(FwtpMessage fwtp , String coinName){
    	
    	
    	FwtpMessage rstMessage = new FwtpMessage();
    	
    	//거래량조회
    	if( "SADM00121".equals(fwtp.getTaskId()) ) 
    	{
    		ArrayList<LinkedHashMap<String, String>> arrayList = CommonCarts.getTranListInfo();
    		rstMessage.setResponseResultList(arrayList);
    		
    	}
    	//판매목록조회
    	else if( "SADM00131".equals(fwtp.getTaskId()) )
    	{
    		// 코인 추가시 로직 추가 
    		ArrayList<LinkedHashMap<String, String>> arrayList = null;
    		if(coinName.equals("BOS"))
    			arrayList = CommonCarts.getBosSellListInfo();
    		else if(coinName.equals("ETH"))
    			arrayList = CommonCarts.getEthSellListInfo();
    		else if(coinName.equals("MCC"))
    			arrayList = CommonCarts.getERC20SellListInfo(coinName);
    		else if(coinName.equals("OMG"))
    			arrayList = CommonCarts.getERC20SellListInfo(coinName);
    		
    		rstMessage.setResponseResultList(arrayList);
    	}
    	//챠트조회 일별
    	else if( "SADM00141".equals(fwtp.getTaskId()) )
    	{
    		// 코인 추가시 로직 추가
    		ArrayList<LinkedHashMap<String, String>> arrayList = null;
    		if(coinName.equals("BOS"))
    			arrayList = CommonCarts.getBosChartListInfo();
    		else if(coinName.equals("ETH"))
    			arrayList = CommonCarts.getEthChartListInfo();
    		else if(coinName.equals("MCC"))
    			arrayList = CommonCarts.getERC20ChartListInfo(coinName);
    		else if(coinName.equals("OMG"))
    			arrayList = CommonCarts.getERC20ChartListInfo(coinName);
    		
    		rstMessage.setResponseResultList(arrayList);
    	}
    	
    	//챠트조회 시간별
    	else if( "SADM00151".equals(fwtp.getTaskId()) )
    	{
    		ArrayList<LinkedHashMap<String, String>> arrayList = CommonCarts.getTimeChartListInfo();
    		logger.debug("#################################################################");
    		logger.debug("#################################################################");
    		logger.debug("#################################################################");
    		logger.debug(arrayList);
    		logger.debug("#################################################################");
    		logger.debug("#################################################################");
    		logger.debug("#################################################################");
    		logger.debug("#################################################################");
    		rstMessage.setResponseResultList(arrayList);
    	}
    	
    	return rstMessage;  	
    	
    }
    
    public void updateDualServerIP(HttpServletRequest request){
    	
		try {
	    	
			logger.debug("************************************************************************");
			logger.debug("이중화 서버 IP 업데이트 실행");
			logger.debug("************************************************************************");
			
	    	// DB 실행 서버 IP 업데이트
	        FwtpMessage fwtp = new FwtpMessage();
	        fwtp.setSysMgtNo( HttpServletUX.getUserInfo(request).getSysMgtNo() );
	        fwtp.setUserId( HttpServletUX.getUserInfo(request).getUserId() );
	        fwtp.setTaskId("SADM00200");	// 실행 서버IP 업데이트 TASK
	        FwtpMessage response = com.finger.agent.FWFTaskByUX.doFWFAgentListForUX ( fwtp );
			
	        logger.debug(" DB 업데이트 응답=["+ response + "]");
	        
		} catch (AgentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }    


    
    /**
     * (조회)결과값 그리드관련 암호화 필드 전환부분
     * @param request
     * @param fwtpMessage
     */
    private void decryptMakeGridResponseData(HttpServletRequest request, FwtpMessage fwtpMessage) {
        ArrayList<LinkedHashMap<String, String>> resultList = fwtpMessage.getResponseResultList();
        if (resultList == null || resultList.isEmpty())
            return;

        ArrayList<LinkedHashMap<String, String>> responseList = new ArrayList<LinkedHashMap<String, String>>();

        for (int ii = 0; ii < resultList.size(); ii++) {
            LinkedHashMap<String, String> responseMap = new LinkedHashMap<String, String>();
            LinkedHashMap<String, String> resultMap = resultList.get(ii);
            Iterator <String> iter =  resultMap.keySet().iterator();
            String colKey = "";
            String colValue = "";
            while (iter.hasNext()) {
                colKey = iter.next();
                colValue = resultMap.get(colKey);
          // logger.debug("~~~~~~~~~~  colKey ["+ colKey +"], colValue["+ colValue +"]");
                if (colKey.toUpperCase().indexOf("ACCT_NO") > -1 || colKey.toUpperCase().indexOf("SECRET_KEY") > -1 ) {

                	String cnvValue;
                	if ("".equals(UXUtil.nvl(colValue))) {
                        responseMap.put(colKey, colValue);                		
                	}else {
						try {
							cnvValue = Crypto.decryptSDCBC(colValue);
	                        responseMap.put(colKey, cnvValue);
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                	}
                }
                else
                    responseMap.put(colKey, colValue);
            }
            responseList.add(responseMap);
        }
        fwtpMessage.setResponseResultList(responseList);
    }
    /**
     * (조회)결과값 단건 암호화 필드 전환부분
     * @param request
     * @param fwtpMessage
     */
    private void decryptMakeResponseResultData(HttpServletRequest request, FwtpMessage fwtpMessage) {
        LinkedHashMap<String, String> resultMap = fwtpMessage.getResponseResultData();
        if (resultMap == null || resultMap.isEmpty())
            return;

        LinkedHashMap<String, String> responseMap = new LinkedHashMap<String, String>();
        Iterator <String> iter =  resultMap.keySet().iterator();
        String colKey = "";
        String colValue = "";
        while (iter.hasNext()) {
            colKey = iter.next();
            colValue = resultMap.get(colKey);
        //  logger.debug("~~~~~~~~~~  colKey ["+ colKey +"], colValue["+ colValue +"]");
            if (colKey.toUpperCase().indexOf("ACCT_NO") > -1 || colKey.toUpperCase().indexOf("SECRET_KEY") > -1 ) {

            	String cnvValue;
            	if ("".equals(UXUtil.nvl(colValue))) {
                    responseMap.put(colKey, colValue);                		
            	}else {
					try {
						cnvValue = Crypto.decryptSDCBC(colValue);
                        responseMap.put(colKey, cnvValue);
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	}
            }
            else
                responseMap.put(colKey, colValue);
        }

        ArrayList<LinkedHashMap<String, String>> responseList = new ArrayList<LinkedHashMap<String, String>>();
        if (responseList.add(responseMap))
            fwtpMessage.setResponseResultList(responseList);
    }  
    
    /**
     * (조회)결과값 그리드관련 다국어-KEY 전환부분
     * @param request
     * @param fwtpMessage
     */
    private void makeGridResponseData(HttpServletRequest request, FwtpMessage fwtpMessage) {
        ArrayList<LinkedHashMap<String, String>> resultList = fwtpMessage.getResponseResultList();
        if (resultList == null || resultList.isEmpty())
            return;

        ArrayList<LinkedHashMap<String, String>> responseList = new ArrayList<LinkedHashMap<String, String>>();

        for (int ii = 0; ii < resultList.size(); ii++) {
            LinkedHashMap<String, String> responseMap = new LinkedHashMap<String, String>();
            LinkedHashMap<String, String> resultMap = resultList.get(ii);
            Iterator <String> iter =  resultMap.keySet().iterator();
            String colKey = "";
            String colValue = "";
            while (iter.hasNext()) {
                colKey = iter.next();
                colValue = resultMap.get(colKey);
//--logger.debug("~~~~~~~~~~  colKey ["+ colKey +"], colValue["+ colValue +"]");
                if (colKey.indexOf("_xx") > -1) {
                    String cnvValue = "";
                    if (UXUtil.nvl(colValue).indexOf(">") > -1) {
                        StringTokenizer stz = new StringTokenizer( colValue, ">" );
                        while (stz.hasMoreTokens()) {
                            cnvValue += " > "+ UCS.Cont( stz.nextToken(), request );
                        }
                    }
                    else
                        cnvValue = UCS.Cont( colValue, request );
                    responseMap.put(colKey, colValue);
                    responseMap.put(colKey.replaceAll("_xx", "_nm"), cnvValue);
                }
                else
                    responseMap.put(colKey, colValue);
            }
            responseList.add(responseMap);
        }
        fwtpMessage.setResponseResultList(responseList);
    }
    /**
     * (조회)결과값 단건 다국어-KEY 전환부분
     * @param request
     * @param fwtpMessage
     */
    private void makeResponseResultData(HttpServletRequest request, FwtpMessage fwtpMessage) {
        LinkedHashMap<String, String> resultMap = fwtpMessage.getResponseResultData();
        if (resultMap == null || resultMap.isEmpty())
            return;

        LinkedHashMap<String, String> responseMap = new LinkedHashMap<String, String>();
        Iterator <String> iter =  resultMap.keySet().iterator();
        String colKey = "";
        String colValue = "";
        while (iter.hasNext()) {
            colKey = iter.next();
            colValue = resultMap.get(colKey);
//--logger.debug("~~~~~~~~~~  colKey ["+ colKey +"], colValue["+ colValue +"]");
            if (colKey.indexOf("_xx") > -1) {
                String cnvValue = "";
                if (UXUtil.nvl(colValue).indexOf(">") > -1) {
                    StringTokenizer stz = new StringTokenizer( colValue, ">" );
                    while (stz.hasMoreTokens()) {
                        cnvValue += " > "+ UCS.Cont( stz.nextToken(), request );
                    }
                }
                else
                    cnvValue = UCS.Cont( colValue, request );
                responseMap.put(colKey, colValue);
                responseMap.put(colKey.replaceAll("_xx", "_nm"), cnvValue);
            }
            else
                responseMap.put(colKey, colValue);
        }

        ArrayList<LinkedHashMap<String, String>> responseList = new ArrayList<LinkedHashMap<String, String>>();
        if (responseList.add(responseMap))
            fwtpMessage.setResponseResultList(responseList);
    }    
}