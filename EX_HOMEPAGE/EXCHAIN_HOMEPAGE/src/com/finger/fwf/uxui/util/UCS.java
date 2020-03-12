package com.finger.fwf.uxui.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.finger.fwf.uxui.CommonContents;

public class UCS {
    private static Logger logger = Logger.getLogger(UCS.class);

    /** Use Language Setting(Cookie)... */

    private static final String     _default_language_  =   "ko";
    private static final String     _user_lang_cookie_  =   "FWF_user_lang";

    protected static final String   _prefixMsgCd_       =   "MSGCD";

    /**
     * Getting User Using Language from Cookie.
     * @param request
     * @return
     */
    public static String getULang(HttpServletRequest request) {
        String lang = _default_language_;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int ii = 0; ii < cookies.length; ii++) {
                if (_user_lang_cookie_.equals(cookies[ii].getName())) {
                    lang = cookies[ii].getValue();
                    break;
                }
            }
        }
        return lang;
    }

    /**
     * Setting User Using Language to Cookie.
     * @param request
     * @param response
     * @param lang
     */
    public static void setUserLanguage(HttpServletRequest request, HttpServletResponse response, String lang) {
        if (UXUtil.isNull(lang))
            lang = _default_language_;
logger.debug("===!SET!  lang ["+ lang +"], referer.. ["+ request.getHeader("referer") +"]");

        Cookie cookie = new Cookie( _user_lang_cookie_, lang );
        cookie.setMaxAge(60*60*24*365);
        cookie.setPath("/");
        response.addCookie(cookie);
        try {
            response.sendRedirect( request.getHeader("referer") );
        } catch (IOException ioe) { ioe.printStackTrace(); }
    }

    /**
     * CONTENTS 키(Key)값에 대한 TEXT(내용)을 반환한다.
     */
    public static String Cont(String cttKey, String lang) {
        return CommonContents.getCommonContent(cttKey, lang);
    }
    public static String Cont(String cttKey, HttpServletRequest request) {
        return CommonContents.getCommonContent(cttKey, getULang(request));
    }
    /**
     * 메세지코드(5자리)에 해당하는 다국어TEXT를 반환한다.
     * _prefixMsgCd_를 사용하여 키를 완성한다.
     * @param cttKey
     * @param request
     * @return
     */
    public static String ContByMsgCd(String cttKey, HttpServletRequest request) {
        return CommonContents.getCommonContent(_prefixMsgCd_+cttKey, getULang(request));
    }
    /**
     * 그리드(Grid) 컬럼헤더 정보 생성 시...
     */
    public static String gridCols(String cttKeys, String pgmID, String lang) {
logger.debug("===>> cttKeys ["+ cttKeys +"]");
        String rtnGridCols = "";
        //리턴예> '계좌일련번호', '상태', '법인', '사업장', '은행', '계좌번호', '금일잔액', '잔액최종갱신일시'
        if (!UXUtil.isNull(cttKeys) && !UXUtil.isNull(pgmID)) {
            if (UXUtil.isNull(lang)) lang = _default_language_;
            String prefix = pgmID.substring(1, 6);
            StringTokenizer stz = new StringTokenizer( cttKeys, "|" );
            while (stz.hasMoreTokens()) {
                if (!UXUtil.isNull(rtnGridCols))
                    rtnGridCols += ", ";

                String _cttKey = UXUtil.nvl( stz.nextToken() );
                if (_cttKey.indexOf("*") > -1) {
                    if (_cttKey.indexOf("*") < 2) {
                    	rtnGridCols += "'"+"<span class=\"star3\"></span>"+ CommonContents.getCommonContent(prefix+_cttKey.substring(1), lang) +"'";
                    }
                    else {
                    	rtnGridCols += "'"+ CommonContents.getCommonContent(prefix+_cttKey.substring(0,_cttKey.length()-1), lang) +"<span class=\"star3\"></span>"+"'";
                    }
                }
                else if (_cttKey.indexOf("!") > -1) {
                    if (_cttKey.indexOf("!") < 2) {
                    	rtnGridCols += "'"+"<span class=\"attention\"></span>"+ CommonContents.getCommonContent(prefix+_cttKey.substring(1), lang) +"'";
                    }
                    else {
                    	rtnGridCols += "'"+ CommonContents.getCommonContent(prefix+_cttKey.substring(0,_cttKey.length()-1), lang) +"<span class=\"attention\"></span>"+"'";
                    }
                }
                else
                    rtnGridCols += "'"+ CommonContents.getCommonContent(prefix+_cttKey, lang) +"'";
            }
        }
logger.debug("===>> rtnGridCols ["+ rtnGridCols +"]");

        return rtnGridCols;
    }
    public static String gridCols(String cttKeys, HttpServletRequest request) {
        return gridCols(cttKeys, JSPUtil.getPgmID(request), getULang(request));
    }
    /**
     * 화면명(프로그램명)
     */
    public static String pgmNm(HttpServletRequest request) {
        return CommonContents.getCommonContent(JSPUtil.getPgmID(request), getULang(request));
    }
    /**
     * 
     * @param cttKey
     * @param request
     * @return
     */
    public static String ContMsg(String cttKey, String params, HttpServletRequest request) {
        String rtnMessage = "";
        if (!UXUtil.isNull(cttKey)) {
            String _lang = getULang(request);
            rtnMessage = CommonContents.getCommonContent(cttKey, _lang);
            if (!UXUtil.isNull(params)) {
                int iCnt = 0;
                StringTokenizer stz = new StringTokenizer( params, "|" );
                while (stz.hasMoreTokens()) {
                    String _param = stz.nextToken();
                    if (!UXUtil.isNull(_param)) {
                        iCnt ++;
                        rtnMessage = rtnMessage.replaceAll("%"+iCnt, CommonContents.getCommonContent(_param, _lang));
                    }
                }
            }
        }

        return rtnMessage;
    }

    /**
     * CONTENTS 전체 리스트
     */
    public static ArrayList<LinkedHashMap<String, String>> getContentsArr() {
        return CommonContents.getCommonContentsArr();
    }
}