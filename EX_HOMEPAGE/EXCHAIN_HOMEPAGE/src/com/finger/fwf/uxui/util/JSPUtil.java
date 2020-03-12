package com.finger.fwf.uxui.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.finger.agent.exception.AgentException;
import com.finger.fwf.uxui.CommonCarts;
import com.finger.fwf.uxui.UserInfo;
import com.finger.protocol.fwtp.FwtpHeader;
import com.finger.protocol.fwtp.FwtpMessage;

public class JSPUtil {
    private static Logger logger = Logger.getLogger(JSPUtil.class);

    private static final String		_PGM_ID_	=	"_PGM_ID_";
    private static final String		_PAGE_PATH_	=	"_PAGE_PATH_";
    private static final String		_reLANG_	=	"##";

    public static ArrayList<LinkedHashMap<String, String>> getUserMenuInfo(HttpServletRequest request) {
        return HttpServletUX.getUserMenuInfo(request);
    }

    public static ArrayList<LinkedHashMap<String, String>> getUserMyMenu(HttpServletRequest request) {
        return HttpServletUX.getUserMyMenu(request);
    }

    public static ArrayList<LinkedHashMap<String, String>> getUserBriefMenu(HttpServletRequest request) {
        return HttpServletUX.getUserBriefMenu(request); 
    }
/* -------------------------------------------------------------------------------------------------------- */
    public static String getPgmID(HttpServletRequest request) {
        if (request == null)
            return "";

        return (String)request.getAttribute(_PGM_ID_);
    }
    public static void setPgmID(String pgmId, HttpServletRequest request) {
        request.setAttribute(_PGM_ID_, pgmId );
        setPageInfo(request);
    }
    
    public static void setPageInfo(HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        if(session != null)
        	session.setAttribute("return_page", "");
        
        HttpServletUX.setPagePath(request, request.getRequestURI().toString().substring(1, request.getRequestURI().toString().length()));
        request.setAttribute(_PAGE_PATH_, request.getRequestURI().toString().substring(1, request.getRequestURI().toString().length()));
    }
    
    public static void setCoinSymbol(String coinSymbol, HttpServletRequest request) {
    	HttpServletUX.setCoinSymbol(request, coinSymbol);
    }
    
    public static String getCoinSymbol(HttpServletRequest request) {
        if (request == null)
            return "";
        return HttpServletUX.getCoinSymbol(request);
    }
        
    public static String getPageName(HttpServletRequest request) {
        if (request == null)
            return "";

        return UCS.Cont(HttpServletUX.getUserMenuNmKey(request, (String)request.getAttribute(_PGM_ID_)), request);
    }

    public static String getPagePath(HttpServletRequest request) {
        if (request == null)
            return "";

        return makePagePath( request, HttpServletUX.getUserMenuPaths(request, (String)request.getAttribute(_PGM_ID_)) );
    }
    private static String makePagePath(HttpServletRequest request, String menuPath) {
        String rtnPath = "";
        if (UXUtil.isNull(menuPath))
            return rtnPath;

        StringTokenizer stz = new StringTokenizer( menuPath, ">" );
        while (stz.hasMoreTokens()) {
            rtnPath += " > "+ UCS.Cont( stz.nextToken(), request );
        }

        return rtnPath;
    }

    public static String getHelpUri(HttpServletRequest request) {
        if (request == null)
            return "";
        /* `/help/work/##/uf10/UF1010010.pdf` : `##`부분을 해당 언어 구분으로 치환한 값으로 반환한다. */
        return UXUtil.replaceAll( HttpServletUX.getUserHelpUri(request, (String)request.getAttribute(_PGM_ID_)), _reLANG_, UCS.getULang(request) );
    }

/* -------------------------------------------------------------------------------------------------------- */

    public static String getSysInfo(String colName, HttpServletRequest request) {
        String rtnValue = "";
        if (UXUtil.isNull(colName) || request == null)
            return rtnValue;
        ArrayList<LinkedHashMap<String, String>> arrSysInfo = CommonCarts.getSystemInfo();
        if (arrSysInfo == null || arrSysInfo.isEmpty())
            return rtnValue;

        UserInfo userInfo = HttpServletUX.getUserInfo(request);
        if (userInfo != null) {
            String sysMgtNo = userInfo.getSysMgtNo();
            if (!UXUtil.isNull(sysMgtNo)) {
                for (int ii = 0; ii < arrSysInfo.size(); ii++)
                    if (sysMgtNo.equals(arrSysInfo.get(ii).get("sys_mgt_no"))) {
                        rtnValue = arrSysInfo.get(ii).get(colName.toLowerCase());
                        break;
                    }
            }
        }
        return rtnValue;
    }

/* ========================================================================================================================== */
    /**
     * 그룹코드에 해당하는 공통코드 리스트 반환
     * @param request
     * @param groupCd
     * @return
     */
    public static ArrayList<HashMap<String, String>> getComCodeGroup(HttpServletRequest request, String groupCd) {
        ArrayList<HashMap<String, String>> arrGroup = new ArrayList<HashMap<String, String>>();

        if (!UXUtil.isNull(groupCd)) {
            ArrayList<LinkedHashMap<String, String>> comCodeList = CommonCarts.getCommonCode();

            for (int i=0; i<comCodeList.size(); i++) {
                HashMap<String, String> comCodeMap = (HashMap<String, String>) comCodeList.get(i);

                if (groupCd.equals(UXUtil.nvl(comCodeMap.get("grp_cd"))))
                    arrGroup.add( comCodeMap );
            }
        }

        return arrGroup;
    }
/* -------------------------------------------------------------------------------------------------------------------------- */
    /**
     * 공통코드 셀렉트박스 생성
     *
     * @param request
     * @param grpCd
     * @param selectName
     * @param option
     * @return
     */
    public static String makeComCodeSelect(HttpServletRequest request, String grpCd, String selectName, String option) {

        String selectedValue = HttpServletUX.getParameter(request, selectName);

        ArrayList<LinkedHashMap<String, String>> comCodeList = CommonCarts.getCommonCode();
        if (comCodeList == null)
            return "";

        ArrayList<LinkedHashMap<String, String>> optionList = new ArrayList<LinkedHashMap<String, String>>();

        for (int i=0; i<comCodeList.size(); i++) {

            LinkedHashMap<String, String> comCodeMap = comCodeList.get(i);
            String _grpCd = UXUtil.nvl(comCodeMap.get("grp_cd"));

            if (grpCd.equals(_grpCd)) {
                optionList.add(comCodeMap);
            }
        }

        return makeSelectBox(optionList, "com_cd", "com_cd_nm_key", option, selectedValue, UCS.getULang(request));
    }
    /**
     * 공통코드 셀렉트박스 생성(초기선택값)
     * @param request
     * @param grpCd
     * @param option
     * @param selectedValue
     * @return
     */
    public static String makeComCodeSelect2(HttpServletRequest request, String grpCd, String option, String selectedValue) {

        selectedValue = UXUtil.nvl(selectedValue);

        ArrayList<LinkedHashMap<String, String>> comCodeList = CommonCarts.getCommonCode();
        if (comCodeList == null)
            return "";

        ArrayList<LinkedHashMap<String, String>> optionList = new ArrayList<LinkedHashMap<String, String>>();

        for (int i=0; i<comCodeList.size(); i++) {

            LinkedHashMap<String, String> comCodeMap = comCodeList.get(i);
            String _grpCd = UXUtil.nvl(comCodeMap.get("grp_cd"));

            if (grpCd.equals(_grpCd)) {
                optionList.add(comCodeMap);
            }
        }

        return makeSelectBox(optionList, "com_cd", "com_cd_nm_key", option, selectedValue, UCS.getULang(request));
    }
    
    /**
     * 공통코드 셀렉트박스 생성(코드값과 코드명 같이 보여줌)
     *
     * @param request
     * @param grpCd
     * @param selectName
     * @param option
     * @return
     */
    public static String makeComCodeSelect3(HttpServletRequest request, String grpCd, String selectName, String option) {

        String selectedValue = HttpServletUX.getParameter(request, selectName);

        ArrayList<LinkedHashMap<String, String>> comCodeList = CommonCarts.getCommonCode();
        if (comCodeList == null)
            return "";

        ArrayList<LinkedHashMap<String, String>> optionList = new ArrayList<LinkedHashMap<String, String>>();

        for (int i=0; i<comCodeList.size(); i++) {

            LinkedHashMap<String, String> comCodeMap = comCodeList.get(i);
            String _grpCd = UXUtil.nvl(comCodeMap.get("grp_cd"));
            String _comCd = UXUtil.nvl(comCodeMap.get("com_cd"));
            String _comCdNm = UXUtil.nvl(comCodeMap.get("com_cd_nm"));

            if (grpCd.equals(_grpCd)) {
            	
            	comCodeMap.put("com_cd_nm_key", _comCd + " : " +_comCdNm);
                optionList.add(comCodeMap);
            }
        }

        return makeSelectBox(optionList, "com_cd", "com_cd_nm_key", option, selectedValue, UCS.getULang(request));
    }
    
    /**
     * 공통코드 셀렉트박스 생성(초기선택값)
     * @param request
     * @param grpCd
     * @param attr1
     * @param attr2
     * @param attr3
     * @param option
     * @param selectedValue
     * @return
     */
    public static String makeComCodeSelectAttr(HttpServletRequest request, String grpCd, String attr1, String attr2, String attr3, String option, String selectedValue) {

        selectedValue = UXUtil.nvl(selectedValue);

        ArrayList<LinkedHashMap<String, String>> comCodeList = CommonCarts.getCommonCode();
        if (comCodeList == null)
            return "";

        ArrayList<LinkedHashMap<String, String>> optionList = new ArrayList<LinkedHashMap<String, String>>();

        for (int i=0; i<comCodeList.size(); i++) {

            LinkedHashMap<String, String> comCodeMap = comCodeList.get(i);
            String _grpCd = UXUtil.nvl(comCodeMap.get("grp_cd"));
            String _attr1 = UXUtil.nvl(comCodeMap.get("attr_1_cd"));
            String _attr2 = UXUtil.nvl(comCodeMap.get("attr_2_cd"));
            String _attr3 = UXUtil.nvl(comCodeMap.get("attr_3_cd"));

            if (grpCd.equals(_grpCd)) {
                if (UXUtil.isNull(attr1) && UXUtil.isNull(attr2) && UXUtil.isNull(attr3))
                    optionList.add(comCodeMap);
                else if ( (!UXUtil.isNull(attr1) && _attr1.equals(attr1))
                       || (!UXUtil.isNull(attr2) && _attr2.equals(attr2))
                       || (!UXUtil.isNull(attr3) && _attr3.equals(attr3)) )
                    optionList.add(comCodeMap);
            }
        }

        return makeSelectBox(optionList, "com_cd", "com_cd_nm_key", option, selectedValue, UCS.getULang(request));
    }

    /**
     * 사용자권한별 사용자구분코드
     * @param request
     * @param selectName
     * @param option
     * @return
     */
    public static String makeUserTypeCdSelect(HttpServletRequest request, String selectName, String option) {

        int userTypeCd = Integer.parseInt(HttpServletUX.getUserInfo(request).getUserTypeCd());
        String selectedValue = HttpServletUX.getParameter(request, selectName);

        ArrayList<LinkedHashMap<String, String>> comCodeList = CommonCarts.getCommonCode();
        if (comCodeList == null)
            return "";

        ArrayList<LinkedHashMap<String, String>> optionList = new ArrayList<LinkedHashMap<String, String>>();

        for (int i=0; i<comCodeList.size(); i++) {

            LinkedHashMap<String, String> comCodeMap = comCodeList.get(i);
            String _grpCd = UXUtil.nvl(comCodeMap.get("grp_cd"));

            if ("COM104".equals(_grpCd)) {
                int _comCd = Integer.parseInt( UXUtil.nvl(comCodeMap.get("com_cd"), "0") );
                if (userTypeCd <= _comCd) {
                    optionList.add(comCodeMap);
                }
            }
        }

        return makeSelectBox(optionList, "com_cd", "com_cd_nm_key", option, selectedValue, UCS.getULang(request));
    }

    /**
     * 사용자권한별 법인 셀렉트박스 생성
     *
     * @param request
     * @param selectName
     * @param option
     * @return
     */
    public static String makeCrpnSelect(HttpServletRequest request, String crpnDiv, String selectName, String option) {

        String selectedValue = HttpServletUX.getParameter(request, selectName);

        ArrayList<LinkedHashMap<String, String>> userCrpnList = HttpServletUX.getUserCrpnInfo(request);
        if (userCrpnList == null)
            return "";

        ArrayList<LinkedHashMap<String, String>> optionList = new ArrayList<LinkedHashMap<String, String>>();

        for (int i=0; i<userCrpnList.size(); i++) {
            LinkedHashMap<String, String> crpnMap = userCrpnList.get(i);
            String _crpnDiv = UXUtil.nvl(crpnMap.get("crpn_div"));
            if (UXUtil.isNull(crpnDiv))
                optionList.add( crpnMap );
            else if (!UXUtil.isNull(crpnDiv) && _crpnDiv.equals(crpnDiv))
                optionList.add( crpnMap );
        }

        return makeSelectBox(optionList, "crpn_seq_no", "smark_crpn_nm_key", option, selectedValue, UCS.getULang(request));
    }

    /**
     * 사용자권한별 사업장 셀렉트박스 생성
     *
     * @param request
     * @param crpnSelectName
     * @param selectName
     * @param option
     * @return
     */
    public static String makeBizrSelect(HttpServletRequest request, String bizrDiv, String crpnSelectName, String selectName, String option) {

        String selectedValue = HttpServletUX.getParameter(request, selectName);
        String crpnSeqNo = HttpServletUX.getParameter(request, crpnSelectName, "ALL");

        ArrayList<LinkedHashMap<String, String>> userBizrList = HttpServletUX.getUserBizrInfo(request);
        if (userBizrList == null)
            return "";

        ArrayList<LinkedHashMap<String, String>> optionList = new ArrayList<LinkedHashMap<String, String>>();

        if (crpnSeqNo.equals("ALL")) {

            for (int i=0; i<userBizrList.size(); i++) {

                LinkedHashMap<String, String> bizrMap = userBizrList.get(i);
                String _bizrDiv = UXUtil.nvl(bizrMap.get("bizr_div"));
                if (UXUtil.isNull(bizrDiv))
                    optionList.add( bizrMap );
                else if (!UXUtil.isNull(bizrDiv) && _bizrDiv.equals(bizrDiv))
                    optionList.add( bizrMap );
            }
        } else {

            for (int i=0; i<userBizrList.size(); i++) {
                LinkedHashMap<String, String> bizrMap = userBizrList.get(i);
                String _crpnSeqNo = UXUtil.nvl(bizrMap.get("crpn_seq_no"));

                if (crpnSeqNo.equals(_crpnSeqNo))
                    optionList.add(bizrMap);
            }
        }

        return makeSelectBox(optionList, "biz_seq_no", "smark_biz_nm_key", option, selectedValue, UCS.getULang(request));
    }

    /**
     * 전체법인 셀렉트박스 생성
     *
     * @param request
     * @param selectName
     * @param option
     * @return
     */
    public static String makeCrpnAllSelect(HttpServletRequest request, String crpnDiv, String selectName, String option) {

        String selectedValue = HttpServletUX.getParameter(request, selectName);
        String sysMgtNo = (String)request.getSession(false).getAttribute("_sys_mgt_no_");

        ArrayList<LinkedHashMap<String, String>> crpnList = CommonCarts.getCrpnInfo();
        if (crpnList == null)
            return "";

        ArrayList<LinkedHashMap<String, String>> optionList = new ArrayList<LinkedHashMap<String, String>>();

        for (int i=0; i<crpnList.size(); i++) {

            LinkedHashMap<String, String> crpnMap = crpnList.get(i);
            String sys_mgt_no = UXUtil.nvl(crpnMap.get("sys_mgt_no"));
            String _crpnDiv = UXUtil.nvl(crpnMap.get("crpn_div"));
            if (sysMgtNo.equals(sys_mgt_no)) {
                if (UXUtil.isNull(crpnDiv))
                    optionList.add( crpnMap );
                else if (!UXUtil.isNull(crpnDiv) && _crpnDiv.equals(crpnDiv))
                    optionList.add( crpnMap );
            }
        }

        return makeSelectBox(optionList, "crpn_seq_no", "smark_crpn_nm_key", option, selectedValue, UCS.getULang(request));
    }

    /**
     * 전체사업장 셀렉트박스 생성
     *
     * @param request
     * @param crpnSelectName
     * @param selectName
     * @param option
     * @return
     */
    public static String makeBizrAllSelect(HttpServletRequest request, String bizrDiv, String crpnSelectName, String selectName, String option) {

        String selectedValue = HttpServletUX.getParameter(request, selectName);
        String sysMgtNo = (String)request.getSession(false).getAttribute("_sys_mgt_no_");
        String crpnSeqNo = HttpServletUX.getParameter(request, crpnSelectName, "ALL");

        ArrayList<LinkedHashMap<String, String>> bizrList = CommonCarts.getBizrInfo();
        if (bizrList == null)
            return "";

        ArrayList<LinkedHashMap<String, String>> optionList = new ArrayList<LinkedHashMap<String, String>>();

        for (int i=0; i<bizrList.size(); i++) {

            LinkedHashMap<String, String> bizrMap = bizrList.get(i);
            String sys_mgt_no = UXUtil.nvl(bizrMap.get("sys_mgt_no"));
            String crpn_seq_no = UXUtil.nvl(bizrMap.get("crpn_seq_no"));
            String _bizrDiv = UXUtil.nvl(bizrMap.get("bizr_div"));
            if (sysMgtNo.equals(sys_mgt_no)) {
                if (crpnSeqNo.equals("ALL") || crpnSeqNo.equals(crpn_seq_no)) {
                    if (UXUtil.isNull(bizrDiv))
                        optionList.add( bizrMap );
                    else if (!UXUtil.isNull(bizrDiv) && _bizrDiv.equals(bizrDiv))
                        optionList.add( bizrMap );
                }
            }
        }

        return makeSelectBox(optionList, "biz_seq_no", "smark_biz_nm_key", option, selectedValue, UCS.getULang(request));
    }

    /**
     * SADM05.SADM05000 셀렉트박스 생성
     * @param request
     * @param grpName
     * @param selectName
     * @param option
     * @return
     */
    public static String make5000Select(HttpServletRequest request, String grpName, String selectName, String option) {

        String selectedValue = HttpServletUX.getParameter(request, selectName);
        String sysMgtNo = (String)request.getSession(false).getAttribute("_sys_mgt_no_");

        ArrayList<LinkedHashMap<String, String>> resultList = CommonCarts.getSelectbox5000();
        if (resultList == null)
            return "";

        ArrayList<LinkedHashMap<String, String>> optionList = new ArrayList<LinkedHashMap<String, String>>();

        for (int i=0; i<resultList.size(); i++) {

            LinkedHashMap<String, String> resultMap = resultList.get(i);
            String group_name = UXUtil.nvl(resultMap.get("group_name"));
            String sys_mgt_no = UXUtil.nvl(resultMap.get("sys_mgt_no"));

            if (grpName.equals(group_name) && sysMgtNo.equals(sys_mgt_no)) {
                optionList.add(resultMap);
            }
        }

        return makeExprSelectBox(optionList, "sbox_code", "sbox_nm_keys", "sbox_nm_expr", option, selectedValue, UCS.getULang(request));
    }

    /**
     * SADM05.SADM05050 셀렉트박스 생성
     * @param request
     * @param grpName
     * @param grpData
     * @param selectName
     * @param option
     * @return
     */
    public static String make5050Select(HttpServletRequest request, String grpName, String grpData, String selectName, String option) {

        String selectedValue = HttpServletUX.getParameter(request, selectName);
        grpData = HttpServletUX.getParameter(request, grpData, "ALL");

        if (option.equals("ALL_DATA") && grpData.equals("ALL")) {
            grpData = "ALL_DATA";
        }

        ArrayList<LinkedHashMap<String, String>> resultList = HttpServletUX.getUserSelectbox5050(request);
        if (resultList == null)
            return "";

        ArrayList<LinkedHashMap<String, String>> optionList = new ArrayList<LinkedHashMap<String, String>>();

        for (int i=0; i<resultList.size(); i++) {

            LinkedHashMap<String, String> resultMap = resultList.get(i);
            String group_name = UXUtil.nvl(resultMap.get("group_name"));
            String group_data = UXUtil.nvl(resultMap.get("group_data"));

            if (grpName.equals(group_name)) {
                if (grpData.equals("ALL") || grpData.equals(group_data)) {
                    optionList.add(resultMap);
                }
            }
        }

        return makeExprSelectBox(optionList, "sbox_code", "sbox_nm_keys", "sbox_nm_expr", option, selectedValue, UCS.getULang(request));
    }
    
    /**
     * SADM05.SADM05050 선택 리스트 반환
     * @param request
     * @param grpName
     * @param grpData
     * @param selectName
     * @param option
     * @return
     */
    public static ArrayList<HashMap<String, String>> get5050(HttpServletRequest request, String grpName) {
        ArrayList<HashMap<String, String>> optionList = new ArrayList<HashMap<String, String>>();

        if (!UXUtil.isNull(grpName)) {
            ArrayList<LinkedHashMap<String, String>> resultList = HttpServletUX.getUserSelectbox5050(request);

            for (int i=0; i<resultList.size(); i++) {

                LinkedHashMap<String, String> resultMap = resultList.get(i);
                String group_name = UXUtil.nvl(resultMap.get("group_name"));

                if (grpName.equals(group_name)) {
                    optionList.add(resultMap);
                }
            }
        }

        return optionList;
    }

    /**
     * 
     * @param optionList
     * @param valueKey
     * @param textKey
     * @param option
     * @param selectedValue
     * @return
     */
    //--private static String makeSelectBox(ArrayList<LinkedHashMap<String, String>> optionList, String ... param) {
    private static String makeSelectBox(ArrayList<LinkedHashMap<String, String>> optionList, String valueKey, String textKey, String option, String selectedValue, String _lang) {
        logger.debug(optionList);

        /*String valueKey = param[0];
        String textKey = param[1];
        String option = param[2];
        String selectedValue = param[3];*/

        StringBuffer sb = new StringBuffer(100);

        if (option == null) {
        } else if (option.equals("ALL")) {
            sb.append("<option value='ALL'>--ALL--</option>\n");
        } else if (option.equals("CHOICE")) {
            sb.append("<option value=''>-SELECT-</option>\n");
        } else if (option.equals("ALL_DATA")) {
            sb.append("<option value='ALL_DATA'>--ALL--</option>\n");
        }

        for (int i=0; i<optionList.size(); i++) {

            LinkedHashMap<String, String> optionMap = optionList.get(i);

            String value = UXUtil.nvl(optionMap.get(valueKey));
            String text = UXUtil.isNull(_lang) ? UXUtil.nvl(optionMap.get(textKey)) : UCS.Cont(UXUtil.nvl(optionMap.get(textKey)), _lang);

            if (selectedValue != null && selectedValue.equals(value)) {
                sb.append("<option value='"+ value +"' selected>"+ text +"</option>\n");
            } else {
                sb.append("<option value='"+ value +"'>"+ text +"</option>\n");
            }
        }

        return sb.toString();
    }
    private static String makeExprSelectBox(ArrayList<LinkedHashMap<String, String>> optionList, String valueKey, String textKeys, String textExpr, String option, String selectedValue, String _lang) {
        logger.debug(optionList);
        logger.debug("==>>  valueKey["+ valueKey +"], textKeys["+ textKeys +"], textExpr["+ textExpr +"], option["+ option +"], selectedValue["+ selectedValue +"], _lang ["+ _lang +"]");
        /*String valueKey = param[0];
        String textKey = param[1];
        String option = param[2];
        String selectedValue = param[3];*/

        StringBuffer sb = new StringBuffer(100);

        if (option == null) {
        } else if (option.equals("ALL")) {
            sb.append("<option value='ALL'>--ALL--</option>\n");
        } else if (option.equals("CHOICE")) {
            sb.append("<option value=''>-SELECT-</option>\n");
        } else if (option.equals("ALL_DATA")) {
            sb.append("<option value='ALL_DATA'>--ALL--</option>\n");
        }

        for (int i=0; i<optionList.size(); i++) {

            LinkedHashMap<String, String> optionMap = optionList.get(i);

            String value = UXUtil.nvl(optionMap.get(valueKey));
            String tKeys = UXUtil.nvl(optionMap.get(textKeys));
            String texts = UXUtil.nvl(optionMap.get(textExpr));
logger.debug(":::  optionList :: "+ i +" :: value ["+ value +"], tKeys ["+ tKeys +"], texts ["+ texts +"]");
            String[] arrText = tKeys.split("[|]");
            for (int ii = 0; ii < arrText.length; ii++) {
                String text = UXUtil.isNull(_lang) ? arrText[ii] : UCS.Cont(arrText[ii], _lang);
logger.debug("                :: "+ i +" :: ii = "+ ii +" --> text ["+ text +"]");
                texts = texts.replaceAll( "@"+(ii+1), text );
            }

            if (selectedValue != null && selectedValue.equals(value)) {
                sb.append("<option value='"+ value +"' selected>"+ texts +"</option>\n");
            } else {
                sb.append("<option value='"+ value +"'>"+ texts +"</option>\n");
            }
        }

        return sb.toString();
    }

/* =================================================================================================================== */

    /**
     * TASK 호출 결과로 SelectBox를 생성한다. (로그인 필요)
     * @param request
     * @param taskId		TASK-ID
     * @param valueKey		Value값으로 사용할 컬럼명
     * @param textKey		Text값으로 사용할 컬럼명
     * @param selectedKey	선택여부(`Y`) 컬럼명
     * @param option		ALL or CHOICE...
     * @return
     */
    public static String makeSelectOfAction(HttpServletRequest request, String taskId, String valueKey, String textKey, String selectedKey, String option) {
        return makeSelectBoxOfAction(request, taskId, valueKey, textKey, selectedKey, option);
    }
    private static String makeSelectBoxOfAction(HttpServletRequest request, String taskId, String valueKey, String textKey, String selectedKey, String option) {

        FwtpMessage fwtp = new FwtpMessage();
        fwtp.setClientIp( request.getRemoteAddr() );
        fwtp.setSysMgtNo( HttpServletUX.getUserInfo(request).getSysMgtNo() );
        fwtp.setUserId( HttpServletUX.getUserInfo(request).getUserId() );
        fwtp.setTaskId( taskId );
        fwtp.setDataType( FwtpHeader._DATATYPE_COMMON );

        if ( !fwtp.validateHeaderMap(fwtp.getHeader()) )
            return "";
        else {
            FwtpMessage resultMessage = null;
            try {
                resultMessage = com.finger.agent.FWFTaskByUXUI.doFWFAgentListForUX ( fwtp );
            } catch (AgentException aex) { aex.printStackTrace(); }

            if (resultMessage == null || !FwtpHeader._RESPONSE_OKAY_.equals(resultMessage.getResponseCode()))
                return "";
            else
                return makeSelectBoxOfAction(resultMessage.getResponseResultList(), valueKey, textKey, selectedKey, option);
        }
    }

    /**
     * 
     * @param optionList
     * @param valueKey
     * @param textKey
     * @param option
     * @return
     */
    private static String makeSelectBoxOfAction(ArrayList<LinkedHashMap<String, String>> optionList, String valueKey, String textKey, String selectedKey, String option) {
        logger.debug(optionList);

        StringBuffer sb = new StringBuffer(100);

        if (option.equals("ALL")) {
            sb.append("<option value='ALL'>--ALL--</option>\n");
        } else if (option.equals("CHOICE")) {
            sb.append("<option value=''>-SELECT-</option>\n");
        }

        for (int i=0; i<optionList.size(); i++) {

            LinkedHashMap<String, String> optionMap = optionList.get(i);

            String text = UXUtil.nvl(optionMap.get(textKey));
            String value = UXUtil.nvl(optionMap.get(valueKey));
            String selected = UXUtil.nvl(optionMap.get(selectedKey));

            if (selected != null && selected.equalsIgnoreCase("Y")) {
                sb.append("<option value='" + value + "' selected>" + text + "</option>\n");
            } else {
                sb.append("<option value='" + value + "'>" + text + "</option>\n");
            }
        }

        return sb.toString();
    }

    
}