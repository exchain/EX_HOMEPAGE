package com.finger.fwf.uxui.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.finger.fwf.uxui.util.HttpServletUX;
import com.finger.fwf.uxui.util.UXUtil;
import com.finger.fwf.uxui.util.UXXss;
import com.finger.tools.util.lang.DateUtil;

public class BasisService {

    /* 요청 시 사용할 필드 값 정의 */
    protected static final String	_TASK_ID_		=	"_TASK_ID_";
    protected static final String	_DATATYPE_		=	"_DATATYPE_";
    protected static final String	_PGM_ID_		=	"_PGM_ID_";
    protected static final String	_GridRows_		=	"_GridRows_";

    protected static final String	_DateF_Parameter	=	"~D~";
    protected static final String	_DateM_Parameter	=	"~Dm~";
    protected static final String	_Time_Parameter		=	"~T~";
    protected static final String	_Amount_Parameter	=	"~N~";
    protected static final String	_DBPointSeparator	=	".";

    protected static String getReqTaskId(HttpServletRequest request) {
        return (String)request.getParameter(_TASK_ID_);
    }

    protected static String getReqDataType(HttpServletRequest request) {
        return (String)request.getParameter(_DATATYPE_);
    }

    protected static String getProgramId(HttpServletRequest request) {
        return (String)request.getParameter(_PGM_ID_);
    }

    protected static ArrayList<LinkedHashMap<String, String>> parameterExtractList(HttpServletRequest request) {
        ArrayList<LinkedHashMap<String, String>> resultList = new ArrayList<LinkedHashMap<String, String>>();

        String _pgm_id = UXUtil.nvl(getProgramId(request));
        String _date_separator = "["+UXUtil.nvl(HttpServletUX.getDateSeparator(request))  +"]";
        String _time_separator = "["+UXUtil.nvl(HttpServletUX.getTimeSeparator(request))  +"]";
        String _amnt_separator = "["+UXUtil.nvl(HttpServletUX.getAmountSeparator(request))+"]";
        String _pont_separator = "["+UXUtil.nvl(HttpServletUX.getPointSeparator(request)) +"]";

        String _date_ymd_order = UXUtil.nvl(HttpServletUX.getDateYMDOrder(request));
        String _date_ymx_order = UXUtil.nvl(HttpServletUX.getDateYMxOrder(request));

        Map<String, String[]> mapReqParam = request.getParameterMap();
        int nKeyCnt = mapReqParam.keySet().size();
        String[] keysName = mapReqParam.keySet().toArray(new String[nKeyCnt]);

        LinkedHashMap<String, String> hmap = new LinkedHashMap<String, String>();
        for (int ii=0; ii<nKeyCnt; ii++) {
            if (_TASK_ID_.equals(keysName[ii]) || _DATATYPE_.equals(keysName[ii]) || _PGM_ID_.equals(keysName[ii])) { }
            else {
                String _keyName = keysName[ii].replaceFirst(_pgm_id, "");
                if (_GridRows_.equals(keysName[ii])) {		// 그리드 데이터 일때...
                    String[] params = mapReqParam.get(keysName[ii]);
                    for (int kk=0; kk<params.length; kk++) {
                        String xss_param = UXXss.XSSFilter(params[kk]);		// UXXss.XSSFilter :: Cross-Site Scripting 방지
                        hmap.put( (_keyName + (kk==0?"":"_"+kk)), xss_param );
                    }
                }
                else {
                    String[] params = mapReqParam.get(keysName[ii]);
                    if (_keyName.startsWith(_DateF_Parameter))
                        for (int kk=0; kk<params.length; kk++)
                            hmap.put( (_keyName.replaceFirst(_DateF_Parameter, "") + (kk==0?"":"_"+kk))
                                    , UXXss.XSSFilter( DateUtil.dateOrderToRegular(UXUtil.replaceAll(params[kk], _date_separator), _date_ymd_order) ) );
                    else if (_keyName.startsWith(_DateM_Parameter))
                        for (int kk=0; kk<params.length; kk++)
                            hmap.put( (_keyName.replaceFirst(_DateM_Parameter, "") + (kk==0?"":"_"+kk))
                                    , UXXss.XSSFilter( DateUtil.dateOrderToRegular(UXUtil.replaceAll(params[kk], _date_separator), _date_ymx_order) ) );
                    else if (_keyName.startsWith(_Time_Parameter))
                        for (int kk=0; kk<params.length; kk++)
                            hmap.put( (_keyName.replaceFirst(_Time_Parameter, "") + (kk==0?"":"_"+kk))
                                    , UXXss.XSSFilter( UXUtil.replaceAll(params[kk], _time_separator) ) );
                    else if (_keyName.startsWith(_Amount_Parameter))
                        for (int kk=0; kk<params.length; kk++)
                            hmap.put( (_keyName.replaceFirst(_Amount_Parameter, "") + (kk==0?"":"_"+kk))
                                    , UXXss.XSSFilter( UXUtil.replaceAll( UXUtil.replaceAll(params[kk], _amnt_separator), _pont_separator, _DBPointSeparator) ) );
                    else
                        for (int kk=0; kk<params.length; kk++)
                            hmap.put( (_keyName + (kk==0?"":"_"+kk)), UXXss.XSSFilter(params[kk]) );
                }
            }
        }
        resultList.add(hmap);

        return resultList;
    }

    /**
     * Request 파라미터의 개수를 구한다.
     * @param request
     * @return
     */
    protected int getParametersSize(HttpServletRequest request) {
        return getParametersSize(request, null);
    }
    protected int getParametersSize(HttpServletRequest request, Logger logger) {
        String _date_separator = "["+UXUtil.nvl(HttpServletUX.getDateSeparator(request))  +"]";
        String _time_separator = "["+UXUtil.nvl(HttpServletUX.getTimeSeparator(request))  +"]";
        String _amnt_separator = "["+UXUtil.nvl(HttpServletUX.getAmountSeparator(request))+"]";
        String _pont_separator = "["+UXUtil.nvl(HttpServletUX.getPointSeparator(request)) +"]";

        String _date_ymd_order = UXUtil.nvl(HttpServletUX.getDateYMDOrder(request));
        String _date_ymx_order = UXUtil.nvl(HttpServletUX.getDateYMxOrder(request));

        Map<String, String[]> mapReqParam = request.getParameterMap();
        int nKeyCnt = mapReqParam.keySet().size();
        if (logger != null && logger.isDebugEnabled()) {
            String _pgm_id = UXUtil.nvl(getProgramId(request));
            String[] keysName = mapReqParam.keySet().toArray(new String[nKeyCnt]);
logger.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!  Parameter Cnt [ "+ nKeyCnt +" ]");
            for (int ii=0; ii<nKeyCnt; ii++) {
                String[] params = mapReqParam.get(keysName[ii]);
                String keyName = keysName[ii];
                if (_GridRows_.equals(keyName)) {
                    for (int kk=0; kk<params.length; kk++) {
                        String param = UXXss.XSSFilter(params[kk]);
logger.debug("##::["+ keyName + (kk==0 ? "":"_"+kk) +"]:["+ keyName.replaceFirst(_pgm_id, "") +"]@@@\n@@@["+ param +"]::##");
                        /* JsonObject jsonObjA = (JsonObject)new JsonParser().parse( param );
                        Type gridType = new TypeToken<ArrayList<HashMap<String, String>>>(){}.getType();
                        JsonArray jsonArray = jsonObjA.get("rows").getAsJsonArray();
                        ArrayList<HashMap<String, String>> gridList = new Gson().fromJson(jsonArray, gridType);
                        logger.debug("///--->> gridList : \n"+ gridList ); */
                    }
                }
                else {
                    if (keyName.startsWith(_DateF_Parameter))
                        for (int kk=0; kk<params.length; kk++) {
                            String param = UXXss.XSSFilter(params[kk]);
logger.debug("##::["+ keyName + (kk==0 ? "":"_"+kk) +"]:["+ keyName.replaceFirst(_pgm_id, "").replaceFirst(_DateF_Parameter, "") +"]@["+ param +"]::##");
param = UXXss.XSSFilter( DateUtil.dateOrderToRegular(UXUtil.replaceAll(params[kk], _date_separator), _date_ymd_order) );
logger.debug("    ["+ keyName + (kk==0 ? "":"_"+kk) +"]:["+ keyName.replaceFirst(_pgm_id, "").replaceFirst(_DateF_Parameter, "") +"]@["+ param +"]::##");
                        }
                    else if (keyName.startsWith(_DateM_Parameter))
                        for (int kk=0; kk<params.length; kk++) {
                            String param = UXXss.XSSFilter(params[kk]);
logger.debug("##::["+ keyName + (kk==0 ? "":"_"+kk) +"]:["+ keyName.replaceFirst(_pgm_id, "").replaceFirst(_DateM_Parameter, "") +"]@["+ param +"]::##");
param = UXXss.XSSFilter( DateUtil.dateOrderToRegular(UXUtil.replaceAll(params[kk], _date_separator), _date_ymx_order) );
logger.debug("    ["+ keyName + (kk==0 ? "":"_"+kk) +"]:["+ keyName.replaceFirst(_pgm_id, "").replaceFirst(_DateM_Parameter, "") +"]@["+ param +"]::##");
                        }
                    else if (keyName.startsWith(_Time_Parameter))
                        for (int kk=0; kk<params.length; kk++) {
                            String param = UXXss.XSSFilter(params[kk]);
logger.debug("##::["+ keyName + (kk==0 ? "":"_"+kk) +"]:["+ keyName.replaceFirst(_pgm_id, "").replaceFirst(_Time_Parameter, "") +"]@["+ param +"]::##");
param = UXXss.XSSFilter( UXUtil.replaceAll(params[kk], _time_separator) );
logger.debug("    ["+ keyName + (kk==0 ? "":"_"+kk) +"]:["+ keyName.replaceFirst(_pgm_id, "").replaceFirst(_Time_Parameter, "") +"]@["+ param +"]::##");
                        }
                    else if (keyName.startsWith(_Amount_Parameter))
                        for (int kk=0; kk<params.length; kk++) {
                            String param = UXXss.XSSFilter(params[kk]);
logger.debug("##::["+ keyName + (kk==0 ? "":"_"+kk) +"]:["+ keyName.replaceFirst(_pgm_id, "").replaceFirst(_Amount_Parameter, "") +"]@["+ param +"]::##");
param = UXXss.XSSFilter( UXUtil.replaceAll( UXUtil.replaceAll(params[kk], _amnt_separator), _pont_separator, _DBPointSeparator) );
logger.debug("    ["+ keyName + (kk==0 ? "":"_"+kk) +"]:["+ keyName.replaceFirst(_pgm_id, "").replaceFirst(_Amount_Parameter, "") +"]@["+ param +"]::##");
                        }
                    else
                        for (int kk=0; kk<params.length; kk++) {
                            String param = UXXss.XSSFilter(params[kk]);
logger.debug("##::["+ keyName + (kk==0 ? "":"_"+kk) +"]:["+ keyName.replaceFirst(_pgm_id, "") +"]@["+ param +"]::##");
                        }
                }
            }
logger.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
        return nKeyCnt;
    }

    protected static final String	_in_sys_mgt_no		=	"_sys_mgt_no_";
    protected static final String	_in_fwf_user_id		=	"_fwf_user_id";
    protected static final String	_in_saved_check		=	"_saved_check";
    protected static final String	__main_page_name	=	"main.jsp";
    protected static final String	__admin_page_name	=	"admin_main.jsp";
    
    /**
     * Cookie 설정/해제
     * @param request
     * @param response
     */
    protected void setCookie(HttpServletRequest request, HttpServletResponse response) {
        // 쿠키(Cookie)저장
        final int nCookieCount = 3;
        Cookie[] cookie = new Cookie[nCookieCount];
        if (request.getParameter(_in_saved_check) != null && request.getParameter(_in_saved_check).equalsIgnoreCase("on") ) {
        	cookie[0] = new Cookie(_in_sys_mgt_no , request.getParameter(_in_sys_mgt_no) );
        	cookie[1] = new Cookie(_in_fwf_user_id, request.getParameter(_in_fwf_user_id) );
        	cookie[2] = new Cookie(_in_saved_check, request.getParameter(_in_saved_check) );
        	for (int x = 0; x < nCookieCount; x++) {
        		cookie[x].setMaxAge(60*60*24*365);
        		cookie[x].setPath("/");
        		response.addCookie(cookie[x]);
        	}
        // 쿠키(Cookie)삭제
        } else {
        	cookie[0] = new Cookie(_in_sys_mgt_no , request.getParameter(_in_sys_mgt_no) );
        	cookie[1] = new Cookie(_in_fwf_user_id, request.getParameter(_in_fwf_user_id) );
        	cookie[2] = new Cookie(_in_saved_check, request.getParameter(_in_saved_check) );
        	for (int x = 0; x < nCookieCount; x++) {
        		cookie[x].setMaxAge(1);
        		cookie[x].setPath("/");
        		response.addCookie(cookie[x]);
        	}
        }
    }
}