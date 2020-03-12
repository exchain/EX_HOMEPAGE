package com.finger.fwf.uxui.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import com.finger.agent.exception.AgentException;
import com.finger.fwf.uxui.CommonCarts;
import com.finger.tools.cipher.Crypto;

public class MarketUser {

    /**
     * 비밀번호 체크
     * @throws AgentException 
     */
    @SuppressWarnings("unused")
	public static boolean checkUserPassword(HttpServletRequest request) throws AgentException {
        boolean result = false;
        try {
        	System.out.println("§§§checkUserPassword call");
        	
        	//승인비밀번호 체크 로직 추가 2016.09.09
        	String apprPwduseYn = "N"; 
        	ArrayList<LinkedHashMap<String, String>> _m_system_info = CommonCarts.getSystemInfo();
        	
         	System.out.println("§§§getSysMgtNo() : " + HttpServletUX.getUserInfo(request).getSysMgtNo());
        	 
        	for (int i = 0; i < _m_system_info.size(); i++) {
                 LinkedHashMap<String, String> preMap = _m_system_info.get(i);

              	System.out.println("§§§preMap.get("+ i +")() : "  + preMap.get("sys_mgt_no"));
                  if( HttpServletUX.getUserInfo(request).getSysMgtNo().equals( preMap.get("sys_mgt_no") ) ){
                	  apprPwduseYn = preMap.get("appr_pwd_use_yn");
                    	System.out.println("§§§apprPwduseYn("+ i +")() : "  + apprPwduseYn);
                    	
                    	break;
                  }else{
                  	System.out.println("§§§checkUserPassword is not equal -> return false");
                	 return false;
                  }
            }
        	
        	System.out.println("apprPwduseYn="+apprPwduseYn);
        	 
        	//승인비밀번호 사용여부가 'Y'인 경우  승인비밀번호로 비교
        	if( "Y".equals(apprPwduseYn) ){
        		
        		
        		
                String ses_appr_pwd = HttpServletUX.getUserInfo(request).getApprPwd();  //승인비밀번호
            	String inp_user_pwd = UXUtil.nvl(request.getParameter("user_pwd"));     //입력비밀번호
            	
            	System.out.println("승인비밀번호 비교");
            	System.out.println("ses_appr_pwd="+ses_appr_pwd);
            	System.out.println("inp_user_pwd="+inp_user_pwd);
            	                
                if (!UXUtil.isNull(inp_user_pwd)) {
                	inp_user_pwd = Crypto.encryptMD5(inp_user_pwd);
                    result = inp_user_pwd.equals(ses_appr_pwd);
                }           		
        	
        	//사용자비밀번호로 비교	
        	}else{
        		
        		
                String ses_user_pwd = HttpServletUX.getUserInfo(request).getUserPwd();  //사용자비밀번호
            	String inp_user_pwd = UXUtil.nvl(request.getParameter("user_pwd"));     //입력비밀번호
            	                
            	System.out.println("사용자비밀번호 비교");
            	System.out.println("ses_appr_pwd="+ses_user_pwd);
            	System.out.println("inp_user_pwd="+inp_user_pwd);
            	
            	
                if (!UXUtil.isNull(inp_user_pwd)) {
                	inp_user_pwd = Crypto.encryptMD5(inp_user_pwd);
                    result = inp_user_pwd.equals(ses_user_pwd);
                }        		
        	}
        	 

        }
        catch (Exception e) { e.printStackTrace(); }

        return result;
    }

    /**
     * 법인정보
     * @throws AgentException 
     */
    public static LinkedHashMap<String, Object> getUserCrpnInfo(HttpServletRequest request) throws AgentException {
        ArrayList<LinkedHashMap<String, String>> _m_crpn_info = HttpServletUX.getUserCrpnInfo(request);
        String sys_mgt_no = UXUtil.nvl(request.getParameter("sys_mgt_no"));

        if (UXUtil.isNull(sys_mgt_no))
            throw new AgentException("시스템관리번호 입력이 필요합니다.<br/>시스템담장자에게 문의 바랍니다.");

        String _default = "";
        ArrayList<LinkedHashMap<String, String>> _arrList = new ArrayList<LinkedHashMap<String, String>>();
        for (int ii = 0; ii < _m_crpn_info.size(); ii++) {
            LinkedHashMap<String, String> preMap = _m_crpn_info.get(ii);
            LinkedHashMap<String, String> _hmap = new LinkedHashMap<String, String>();
            if (sys_mgt_no.equals(preMap.get("sys_mgt_no"))) {
                _hmap.put("value", preMap.get("crpn_seq_no") );
                _hmap.put("text" , UCS.Cont(UXUtil.nvl(preMap.get("smark_crpn_nm_key")), UCS.getULang(request)) );
                _arrList.add( _hmap );
            }
        }
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
        resultMap.put("options"             , _arrList );
        resultMap.put("defaultSelectedValue", _default );

        return resultMap;
    }

    /**
     * 사업장정보
     * @throws AgentException 
     */
    public static LinkedHashMap<String, Object> getUserBizrInfo(HttpServletRequest request) throws AgentException {
        ArrayList<LinkedHashMap<String, String>> _m_bizr_info = HttpServletUX.getUserBizrInfo(request);
        String crpn_seq_no = UXUtil.nvl(request.getParameter("crpn_seq_no"));
        String bizr_div = UXUtil.nvl(request.getParameter("bizr_div"));

        if (UXUtil.isNull(crpn_seq_no))
            throw new AgentException("법인일련번호 입력이 필요합니다.<br/>시스템담장자에게 문의 바랍니다.");

        String _default = "";
        ArrayList<LinkedHashMap<String, String>> _arrList = new ArrayList<LinkedHashMap<String, String>>();
        for (int ii = 0; ii < _m_bizr_info.size(); ii++) {
            LinkedHashMap<String, String> preMap = _m_bizr_info.get(ii);
            LinkedHashMap<String, String> _hmap = new LinkedHashMap<String, String>();
            if (UXUtil.isNull(crpn_seq_no)) {
                /*_hmap.put("value", preMap.get("biz_seq_no") );
                _hmap.put("text" , preMap.get("smark_biz_nm") );
                _arrList.add( _hmap );*/
                throw new AgentException("법인일련번호/ALL 입력이 필요합니다.");
            } else
            if (crpn_seq_no.equals(preMap.get("crpn_seq_no")) || "ALL".equals(crpn_seq_no)) {
                if (UXUtil.isNull(bizr_div) || (!UXUtil.isNull(bizr_div) && bizr_div.equals(preMap.get("bizr_div")))) {
                    _hmap.put("value", preMap.get("biz_seq_no") );
                    _hmap.put("text" , UCS.Cont(UXUtil.nvl(preMap.get("smark_biz_nm_key")), UCS.getULang(request)) );
                    _arrList.add( _hmap );
                }
            }
        }
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
        resultMap.put("options"             , _arrList );
        resultMap.put("defaultSelectedValue", _default );

        return resultMap;
    }

    /**
     * SelectBox 개발자정의용 - 로그인 후/세션
     * @throws AgentException
     */
    public static LinkedHashMap<String, Object> getUserSelectbox5050(HttpServletRequest request) throws AgentException {
        ArrayList<LinkedHashMap<String, String>> _m_common_code = HttpServletUX.getUserSelectbox5050(request);
        String _sboxGroup = UXUtil.nvl(request.getParameter("sboxGroup"));
        String _groupData = UXUtil.nvl(request.getParameter("groupData"));

        ArrayList<LinkedHashMap<String, String>> _arrList = new ArrayList<LinkedHashMap<String, String>>();
        String _default = "";
        for (int ii = 0; ii < _m_common_code.size(); ii++) {
            LinkedHashMap<String, String> preMap = _m_common_code.get(ii);
            if (_sboxGroup.equals(preMap.get("group_name"))) {
                if (_groupData.equals("ALL") || _groupData.equals(preMap.get("group_data"))) {
                	String value = UXUtil.nvl(preMap.get("sbox_code"));
                    String tKeys = UXUtil.nvl(preMap.get("sbox_nm_keys"));
                    String texts = UXUtil.nvl(preMap.get("sbox_nm_expr"));
                    String[] arrText = tKeys.split("[|]");
                    for (int kk = 0; kk < arrText.length; kk++) {
                        String text = UCS.Cont(arrText[kk], UCS.getULang(request));
                        texts = texts.replaceAll( "@"+(kk+1), text );
                    }
                    LinkedHashMap<String, String> _hmap = new LinkedHashMap<String, String>();
                    _hmap.put("value", value );
                    _hmap.put("text" , texts );

                    _arrList.add( _hmap );
                }
            }
        }
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
        resultMap.put("options"             , _arrList );
        resultMap.put("defaultSelectedValue", _default );

        return resultMap;
    }
}