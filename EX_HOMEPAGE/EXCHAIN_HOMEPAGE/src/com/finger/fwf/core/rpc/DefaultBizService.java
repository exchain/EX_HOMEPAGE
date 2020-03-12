package com.finger.fwf.core.rpc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;

import com.finger.agent.msg.FixedMsg;
import com.finger.fwf.core.base.BizModuleClass;
import com.finger.protocol.fwtp.FwtpMessage;
import com.finger.fwf.core.config.FWFApsConfig;

public class DefaultBizService implements BizService {
	
    private static Logger logger = Logger.getLogger(DefaultBizService.class);  
    private String _actionDesc = "";
    private static final String  _sys_id = FWFApsConfig.SYS_ID;
    
    public FwtpMessage callService(FwtpMessage in) {
        logger.debug("DefaultBizService.callService" + " ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        FwtpMessage out = in;
        BizModuleClass bizmcls = null;
		long startTime =0;

		//실행시작시간
		startTime = System.currentTimeMillis();
		
        String execDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String seqNo = String.valueOf(new Date().getTime());
		
        try {
            Object object = Class.forName(in.getBizTaskClass()).newInstance();
            
            if (object instanceof BizModuleClass) {
                bizmcls = (BizModuleClass) object;
                bizmcls.init();
                
                out = bizmcls.execute(in.getBizTaskAction(), in);
                
                logger.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
                logger.debug("out.getMessageCode()=["+out.getMessageCode()+"]" );
                logger.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
                
                if( out != null && out.getMessageCode() != null ){
                	
	                if(	out.getMessageCode().length()>0	&& "0".equals(out.getMessageCode().substring(0,1))
	                		|| out.getResponseCode().length()>0	&&  "OK".equals(out.getResponseCode()) ){
	                    //실행내역로그 DB 저장(성공)
	                    insertSysTrnsLog(in, out, execDate, seqNo, "Y", startTime);
	                }else{
	                    //실행내역로그 DB 저장(실패)
	                	insertSysTrnsLog(in, out, execDate, seqNo, "N", startTime);
	                	//에러내역로그 DB 저장
	                	insertSysErrLog(in, out, execDate, seqNo, out.getErrorMessage());
	                }
                }else{
                	
                    //실행내역로그 DB 저장(실패)
                	insertSysTrnsLog(in, out, execDate, seqNo, "N", startTime);
                	//에러내역로그 DB 저장
                	insertSysErrLog(in, out, execDate, seqNo, out.getErrorMessage());
                	
                }
            }
            else {
                throw new Exception("트랜잭션에 해당하는 모듈생성 포맷을 인식할수 없습니다.");
            }
        }
        catch (Exception e) {
        	
        	logger.error("Task Module Generation Exception : "+ e.toString(), e);
            out.setMessageCode(FixedMsg._MSG_TASK_T9996);	// 동적 Task 생성 실패
            out.addErrorMessage("Task Module Generation Exception : " + e.toString());
            
            //실행내역로그 DB 저장(실패)
        	insertSysTrnsLog(in, out, execDate, seqNo, "N", startTime);
        	//에러내역로그 DB 저장
        	insertSysErrLog(in, out, execDate, seqNo, out.getErrorMessage());
            
        }
        finally {
            bizmcls.close();
        }

        return out;
    }
    
    /*
     *  CMS 의 모든 거래 내역을 DB 로그에 저장 
     *  
     */
    public void insertSysTrnsLog(FwtpMessage in, FwtpMessage out, String execDate, String seqNo, String successYn, long startTime){

    	// 거래현황(SADM00120), 판매목록조회(SADM00130), 챠트조회(SADM00140) 제외 
    	// main.jsp 에서 1분만 마다 CALL 처리 됨 
    	if(   "SADM00120".equals(in.getTaskId()) || "SADM00130".equals(in.getTaskId()) || "SADM00140".equals(in.getTaskId()) ){
    		return;
    	}
    	
    	FwtpMessage infwfp = new FwtpMessage();
    	BizModuleClass bizmcls = null;
		long endTime =0;
		String execTime = "0";
    	
    	String clsNm = "com.finger.exchain.tasks.sadm.SADM00";
    	String actNm = "fncSADM00300";

//        logger.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
//        logger.debug("in.getBizTaskClass()="+in.getBizTaskClass());
//        logger.debug("in.getBizTaskAction()="+in.getBizTaskAction());
//        logger.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++");

		//프로그램ID
		String pgmID = in.getTaskId();
		//화면ID
		String pageID = in.getPageId();
		//사용자ID(사용자ID가 없는 경우 
		String userId = in.getUserId()==null||in.getUserId()==""?"NOUSERID":in.getUserId();
    	//실행일자
        String execYMD  = new SimpleDateFormat("yyyyMMdd").format(new Date());
        
        //거래구분 - System : SYS, 업무 : TASK
        String trnsDiv = "";
        if( in.getBizTaskClass().indexOf(".sadm.")>=0 ){
        	trnsDiv = "SYS";  
        }else{
        	trnsDiv = "TASK"; 
        }
        
        //실행자가 시스템인 경우
        if( "_STARTUP_".equals(userId) ||  "SCHEDULER".equals(userId) ||  "NOUSERID".equals(userId)  
        		|| "_PRE_LOAD_".equals(userId) ||  "HERMESRECV".equals(userId) || "RELAYRECV".equals(userId) 
        		    || "SYS_BATCH".equals(userId) ){
        	trnsDiv = "SYS";  
        }
        
		//종료시간
		endTime = System.currentTimeMillis();
		//실행된 시간
		execTime = getExecTime(startTime, endTime);
		
		//로그인 할때 (로그인을 호출했을때 USER_ID를 접속성공한 USER_ID로 변경)
		if( "SADS10010".equals(pgmID) ){
	        ArrayList<LinkedHashMap<String, String>> responseList = in.getResponseData();
	        if( responseList.size() > 0 ){
	        	LinkedHashMap<String, String> responseMap = responseList.get(0);
	        	userId = responseMap.get("USER_ID");
	        	pageID = "LOGIN";
	        	trnsDiv = "TASK"; 
	        }
		}		
		
		//로그아웃 할때
		if( "SADS10040".equals(pgmID) ){
			successYn = "Y";
			pageID = "LOGOUT";
			trnsDiv = "TASK"; 
		}
        
		//Client IP가 NULL인 경우 (서버 기동시 NULL이 있는 경우가 있음)
		String clientIp = in.getClientIp();
		if(clientIp == null || ("").equals(clientIp) || clientIp.length()==0 ){
			clientIp = "127.0.0.1";
		}
    	
		// 시스템로그는 저장하지 않는다.
        if(trnsDiv.equals("SYS"))
        	return;
        
        try {
            Object object = Class.forName(clsNm).newInstance();
            
            LinkedHashMap<String, String> paramMap = new LinkedHashMap<String, String> ();
            
            paramMap.put("exec_date",     execDate);
            paramMap.put("seq_no",        seqNo);
            paramMap.put("acs_ip",        clientIp);
            paramMap.put("crt_id",        userId);
            paramMap.put("exec_dt",       execYMD);
            paramMap.put("pgm_id",        pgmID);
            paramMap.put("pgm_nm",        _actionDesc);
            paramMap.put("page_id",       pageID);
            paramMap.put("class_nm",      in.getBizTaskClass());
            paramMap.put("method_nm",     in.getBizTaskAction());
            paramMap.put("trns_div",      trnsDiv);
            paramMap.put("scs_yn",        successYn);
            paramMap.put("exec_str_dtm",  formatTime(startTime));
            paramMap.put("exec_end_dtm",  formatTime(endTime));
            paramMap.put("exec_dtm",      execTime);
            paramMap.put("sys_id",        _sys_id);
            //paramMap.put("sys_id",        in.getSysId());
           
            infwfp.setRequestData(paramMap);

            if (object instanceof BizModuleClass) {
                bizmcls = (BizModuleClass) object;
                bizmcls.init();
                bizmcls.execute(actNm, infwfp);
            }
            else {
                throw new Exception("시스템 거래 로그 모듈 포맷을 인식할수 없습니다.(Not format for Instance of Class)");
            }
        }
        catch (Exception e) {
            logger.error("insertSysTrnsLog Exception : "+ e.toString(), e);
        }
        finally {
            bizmcls.close();
        }
    }
    
    
    /*
     *  CMS 의 모든 에러 내역을 DB 로그에 저장 
     *  
     */
    public void insertSysErrLog(FwtpMessage in, FwtpMessage out, String execDate, String seqNo, String errmsg){

    	
    	FwtpMessage infwfp = new FwtpMessage();
    	BizModuleClass bizmcls = null;
    	
    	String clsNm = "com.finger.exchain.tasks.sadm.SADM00";
    	String actNm = "fncSADM00400";

        String execYMD  = new SimpleDateFormat("yyyyMMdd").format(new Date());
        
        try {
            Object object = Class.forName(clsNm).newInstance();
            
            LinkedHashMap<String, String> paramMap = new LinkedHashMap<String, String> ();
            
            //에러메시지 4000바이트로 줄임
            String byteMsg = getByteString(errmsg, 4000);
    		System.out.println("**************************************************");
    		System.out.println("Error Massage length(byteMsg)="+byteMsg.length());
    		System.out.println("**************************************************");
    		
    		//에러유형
    		String errType = "";
    		if( errmsg.indexOf("SQL")>0 ){
    			errType = "DB";
    		}else{
    			errType = "PGM";
    		}
    		
    		//Client IP가 NULL인 경우 (서버 기동시 NULL이 있는 경우가 있음)
    		String clientIp = in.getClientIp();
    		if(clientIp == null || ("").equals(clientIp) || clientIp.length()==0 ){
    			clientIp = "127.0.0.1";
    		}
        	    		
            
            paramMap.put("err_log_date",     execDate);
            paramMap.put("err_log_seq_no",   seqNo);
            paramMap.put("acs_ip",           clientIp);
            paramMap.put("crt_id",           in.getUserId());
            paramMap.put("err_dt",           execYMD);
            paramMap.put("err_type",         errType);
            paramMap.put("err_desc",         byteMsg);
            paramMap.put("err_o_pgm_id",     in.getTaskId());
            paramMap.put("err_o_pgm_nm",     _actionDesc);
            paramMap.put("err_o_scrn_id",    in.getPageId());
            //paramMap.put("sys_id",           in.getSysMgtNo());
            paramMap.put("sys_id",        _sys_id);
           
            infwfp.setRequestData(paramMap);

            if (object instanceof BizModuleClass) {
                bizmcls = (BizModuleClass) object;
                bizmcls.init();
                bizmcls.execute(actNm, infwfp);
            }
            else {
                throw new Exception("시스템 에러 로그 모듈 포맷을 인식할수 없습니다.(Not format for Instance of Class)");
            }
        }
        catch (Exception e) {
            logger.error("insertSysTrnsLog Exception : "+ e.toString(), e);
        }
        finally {
            bizmcls.close();
        }
    }
    
    
    /*
     *  실행시간 Stirng 변환 
     */    
	public String getExecTime(long startTime, long endTime) {
		
		String rstTime = "";
		long execTime = endTime - startTime;
		
		rstTime =  (execTime/1000.0f)+"초";	
		
		return rstTime;
	}
	
	
	/*
	 * 밀리초(ms)단위의 시간을 날자 포맷으로 변경
	 */
	public String formatTime(long ITime) {
		
		StringBuffer rstDate = new StringBuffer();
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(ITime);
		
		int iYear   = c.get(Calendar.YEAR);
		int iMonth  = c.get(Calendar.MONTH)+1;
		int iDay    = c.get(Calendar.DAY_OF_MONTH);
		int iHour   = c.get(Calendar.HOUR_OF_DAY);
		int iMin    = c.get(Calendar.MINUTE);
		int iSec    = c.get(Calendar.SECOND);
		
		//월 1자리 앞에 0 채우기 
		String sMonth = String.valueOf(iMonth);
		if( iMonth < 10 ){
			sMonth = "0"+ sMonth;
		}
		
		//일 1자리 앞에 0 채우기 
		String sDay = String.valueOf(iDay);
		if( iDay < 10 ){
			sDay = "0"+ sDay;
		}

		//시간 1자리 앞에 0 채우기 
		String sHour = String.valueOf(iHour);
		if( iHour < 10 ){
			sHour = "0"+ sHour;
		}

		//분 1자리 앞에 0 채우기 
		String sMin = String.valueOf(iMin);
		if( iMin < 10 ){
			sMin = "0"+ sMin;
		}

		//초 1자리 앞에 0 채우기 
		String sSec = String.valueOf(iSec);
		if( iSec < 10 ){
			sSec = "0"+ sSec;
		}
		
		//년-월-일 시:분:초
		rstDate.append(iYear).append("-").append(sMonth).append("-").append(sDay);
		rstDate.append(" ");
		rstDate.append(sHour).append(":").append(sMin).append(":").append(sSec); 
		
		return rstDate.toString();
	}
    
	/**
	 * 입력한 Byte 수만큼 String을 잘라서 리턴
	 * @param String 문자
	 * @param int 문자최대값 
	 * @return String
	 * @throws 
	 */
	public String getByteString(String inStr, int chkByte) {
		
		String retStr = "";  
		int chkLength = 0;
		
		for( int i=0 ; i<inStr.length() ; i++){
			
			chkLength += String.valueOf(inStr.charAt(i)).getBytes().length;
			
			if( chkLength < chkByte){
				
				//값에 "'" 있는 경우 "" 로 처리(DB insert 시 특수문자 "'"  에러 발생)
				if( inStr.charAt(i) == '\'' ){
					continue;
				//한글인 경우 3Byte 	
				}else if( isKoreanCharacter(inStr.charAt(i)) ){
					retStr += inStr.charAt(i);
					chkLength = chkLength + 3;
				}else{
					retStr += inStr.charAt(i);
				}
			}
			
		}
		return retStr;
		
	}    
	
	/**
	 * 한글 여부 확인
	 * @param ch 
	 * @return boolean
	 * @throws 
	 */
	public boolean isKoreanCharacter(char ch) {
		String block = Character.UnicodeBlock.of(ch).toString();
		
		if( block.equals("HANGUL_JAMO") || block.equals("HANGUL_SYLLABLES") || block.equals("HANGUL_COMPATIBILITY_JAMO") ){
			return true;
		}
		
		return false;
	}
		
	
    /*
     * Task Desc 
     */
    public void setTaskDesc(String value) {
        this._actionDesc = value;
    }
    public String getTaskDesc() {
        return this._actionDesc;
    }
	
}
