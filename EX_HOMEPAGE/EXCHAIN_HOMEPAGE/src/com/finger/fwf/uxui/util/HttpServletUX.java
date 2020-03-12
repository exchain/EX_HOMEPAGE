package com.finger.fwf.uxui.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.finger.agent.config.FcAgentConfig;
import com.finger.fwf.uxui.MarketPolicyInfo;
import com.finger.fwf.uxui.UserInfo;
import com.finger.protocol.fwtp.FwtpMessage;

public class HttpServletUX {
    private static Logger logger = Logger.getLogger(HttpServletUX.class);

    private static final String		__ATTR__SYS_MGT_NO		=	"_sys_mgt_no_";
    private static final String		__ATTR__USER_ID			=	"_fwf_user_id";
    private static final String		_SESSION__USER_INFO_	=	"FWF_User_Info";
    private static final String     __CUST_STATE			=   "_cust_state";
    private static final String		__ATTR_Date_Separator	=	"format_date_separator";
    private static final String		__ATTR_Time_Separator	=	"format_time_separator";
    private static final String		__ATTR_Amnt_Separator	=	"format_amount_separator";
    private static final String		__ATTR_Pont_Separator	=	"format_point_separator";
    private static final String		__ATTR_Date_YMD_Order	=	"format_date_ymd_order";
    private static final String		__ATTR_Date_YMx_Order	=	"format_date_ymx_order";
    
    /* ************************************************************************* *
    /* *******  사용자 로그인 후 관련 정보 세션에 담는 작업을 진행한다.  ******* *
     * ************************************************************************* */

    private static final String		__ATTR_User_Menu_Info	=	"User_Menu_Info";
    
    private static final String		__ATTR_User_MenuNm_Key	=	"User_MenuNm_Key";
    private static final String		__ATTR_User_Menu_Paths	=	"User_Menu_Paths";
    private static final String		__ATTR_User_Menu_HelpU	=	"User_Menu_HelpU";
    
    private static final String		__ATTR_User_Crpn_Info	=	"User_Crpn_Info";
    private static final String		__ATTR_User_Bizr_Info	=	"User_Bizr_Info";
    private static final String		__ATTR_User_My_Menu		=	"User_My_Menu";
    private static final String		__ATTR_User_Brief_Menu	=	"User_Brief_Menu";
    private static final String		__ATTR_User_Select5050	=	"User_Select5050";
    
    private static final String		_MARKET_POLICAY_INFO_	=	"Market_Policy_Info";
    
    private static final String		__PAGE_PATH_			=	"Page_Path";
    private static final String		__COIN_SYMBOL_			=	"Coin_Symbol";
    
    public HttpServletUX() { }

    public static void setResponseContentAndHeader(HttpServletResponse response) {
        response.setContentType("text/html; charset="+ FcAgentConfig._CharSet_.toLowerCase() );
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Expires", "0");
        response.setHeader("Pragma", "no-cache");
    }

    public static String[] getParameterNames(HttpServletRequest request) {
        if (request == null) return null;

        ArrayList<String> alist = new ArrayList<String>();
        Enumeration<String> emt = request.getParameterNames();

        if (!emt.hasMoreElements()) return new String[0];

        for (Enumeration<String> emt2 = request.getParameterNames(); emt2.hasMoreElements(); alist.add(emt2.nextElement()));
        String retArray[] = new String[alist.size()];
        for (int i = 0; i < retArray.length; i++)
            retArray[i] = alist.get(i);

        Arrays.sort(retArray);
        return retArray;
    }

    public static String getParameter(HttpServletRequest request, String key) {
        return getParameter(request, key, "");
    }
    public static String getParameter(HttpServletRequest request, String key, String defaultValue) {
        String tmp = request.getParameter(key);
        if (UXUtil.isNull(tmp))
            return defaultValue;
        else
            return tmp;
    }

    /**
     * (로그인) 세션을 생성한다.
     * @param request
     * @param message
     */
    public static void createLoginInfo(HttpServletRequest request, FwtpMessage message, boolean sesionCreate) {

    	LinkedHashMap<String, String> hmap = message.getResponseResultList().get(0);
        logger.debug("===<< getResponseResultList.("+ 0 +") >>===\n"+ hmap );

        UserInfo userInfo = new UserInfo(hmap);

        userInfo.setValue(UserInfo._IP_ADDRES , request.getRemoteAddr() );
        userInfo.setValue(UserInfo._LOGIN_DT  , message.getTimeStamp() );

        HttpSession session = null;
        if(sesionCreate) {
        	session = request.getSession(true);
            //session.setMaxInactiveInterval( 30 * 60 );	// 초 단위 - 30분(임시)
            session.setAttribute("return_page", request.getParameter("return_page"));
            session.setAttribute("session_key", UXUtil.makeSessionkey());
        }else {
        	session = request.getSession(false);
        }
    
        userInfo.setValue(UserInfo._SESSION_ID, session.getId() );
        setUserInfo ( request, userInfo );
    }

    /**
     * (로그아웃) 세션정보를 제거한다.
     * @param request
     */
    public static void removeLoginInfo(HttpServletRequest request) {
        if (request == null)
            return;

        HttpSession session = request.getSession(false);
        if (session == null)
            return;

        //session.removeAttribute(__ATTR__SYS_MGT_NO);
        //session.removeAttribute(__ATTR__USER_ID);
        session.removeAttribute(_SESSION__USER_INFO_);
        session.removeAttribute(_MARKET_POLICAY_INFO_);

        session.invalidate();
    }

    /**
     * 사용자정보 세션에서 얻기
     * @param request
     * @return
     */
    public static UserInfo getUserInfo(HttpServletRequest request) {
        if (request == null)
            return null;
        else
            return getUserInfo(request.getSession(false));
    }
    public static UserInfo getUserInfo(HttpSession session) {
        if (session == null)
            return null;
        else
            return (UserInfo)session.getAttribute(_SESSION__USER_INFO_);
    }
    public static String getSessionUserID(HttpSession session) {
        if (session == null)
            return null;
        else
            return (String)session.getAttribute(__ATTR__USER_ID);
    }

    /**
     * (로그인)사용자정보 SESSION 에 담기
     * @param request
     * @param userInfo
     */
    private static void setUserInfo(HttpServletRequest request, UserInfo userInfo) {
        if (request != null)
            setUserInfo(request.getSession(false), userInfo);
    }
    private static void setUserInfo(HttpSession session, UserInfo userInfo) {
        logger.debug("\n==> session : "+ session +"\n==> userInfo: "+ userInfo.toString() );
        if (session != null) {
            session.setAttribute(__ATTR__SYS_MGT_NO, userInfo.getSysMgtNo() );
            session.setAttribute(__ATTR__USER_ID, userInfo.getUserId() );
            session.setAttribute(__CUST_STATE, userInfo.getCustState());
            session.setAttribute(_SESSION__USER_INFO_, userInfo );
            
            session.setAttribute(__ATTR_Date_Separator, userInfo.getValue(__ATTR_Date_Separator) );
            session.setAttribute(__ATTR_Time_Separator, userInfo.getValue(__ATTR_Time_Separator) );
            session.setAttribute(__ATTR_Amnt_Separator, userInfo.getValue(__ATTR_Amnt_Separator) );
            session.setAttribute(__ATTR_Pont_Separator, userInfo.getValue(__ATTR_Pont_Separator) );
            session.setAttribute(__ATTR_Date_YMD_Order, userInfo.getValue(__ATTR_Date_YMD_Order) );
            session.setAttribute(__ATTR_Date_YMx_Order, userInfo.getValue(__ATTR_Date_YMx_Order) );
        }
    }

    /**
     * 세션정보에서 페이지경로를 가져옵니다.
     * @param request
     * @return
     */
    public static String getPagePath(HttpServletRequest request) {
        if (request != null) {
            HttpSession session = request.getSession(false);
            if (session != null)
                return (String)session.getAttribute(__PAGE_PATH_);
        }
        return null;
    }
    
    /**
     * 페이지경로를 세션정보에 저장합니다.
     * @param request
     * @return
     */
    public static void setPagePath(HttpServletRequest request, String pagePath) {
        if (request != null) {
            HttpSession session = request.getSession(false);
            if (session != null)
            	session.setAttribute(__PAGE_PATH_, pagePath );
        }
    }
    
    /**
     * 세션정보에서 코인 심볼명을 가져옵니다.
     * @param request
     * @return
     */
    public static String getCoinSymbol(HttpServletRequest request) {
        if (request != null) {
            HttpSession session = request.getSession(false);
            if (session != null)
                return (String)session.getAttribute(__COIN_SYMBOL_);
        }
        return null;
    }
    
    /**
     * 페이지경로를 코인 심볼명을 저장합니다.
     * @param request
     * @return
     */
    public static void setCoinSymbol(HttpServletRequest request, String coinSymbol) {
        if (request != null) {
            HttpSession session = request.getSession(false);
            if (session != null)
            	session.setAttribute(__COIN_SYMBOL_, coinSymbol );
        }
    }
    
    /**
     * 세션정보에서 DATE Separator 구하기
     * @param request
     * @return
     */
    public static String getDateSeparator(HttpServletRequest request) {
        if (request != null) {
            HttpSession session = request.getSession(false);
            if (session != null)
                return (String)session.getAttribute(__ATTR_Date_Separator);
        }
        return null;
    }
    /**
     * 세션정보에서 TIME Separator 구하기
     * @param request
     * @return
     */
    public static String getTimeSeparator(HttpServletRequest request) {
        if (request != null) {
            HttpSession session = request.getSession(false);
            if (session != null)
                return (String)session.getAttribute(__ATTR_Time_Separator);
        }
        return null;
    }
    /**
     * 세션정보에서 AMOUNT(,) Separator 구하기
     * @param request
     * @return
     */
    public static String getAmountSeparator(HttpServletRequest request) {
        if (request != null) {
            HttpSession session = request.getSession(false);
            if (session != null)
                return (String)session.getAttribute(__ATTR_Amnt_Separator);
        }
        return null;
    }
    /**
     * 세션정보에서 Decimal POINT(.) Separator 구하기
     * @param request
     * @return
     */
    public static String getPointSeparator(HttpServletRequest request) {
        if (request != null) {
            HttpSession session = request.getSession(false);
            if (session != null)
                return (String)session.getAttribute(__ATTR_Pont_Separator);
        }
        return null;
    }
    /**
     * 세션정보에서 Date YMD(년월일) 순서 구하기
     * @param request
     * @return
     */
    public static String getDateYMDOrder(HttpServletRequest request) {
        if (request != null) {
            HttpSession session = request.getSession(false);
            if (session != null)
                return (String)session.getAttribute(__ATTR_Date_YMD_Order);
        }
        return null;
    }
    /**
     * 세션정보에서 Date YM(년월) 순서 구하기
     * @param request
     * @return
     */
    public static String getDateYMxOrder(HttpServletRequest request) {
        if (request != null) {
            HttpSession session = request.getSession(false);
            if (session != null)
                return (String)session.getAttribute(__ATTR_Date_YMx_Order);
        }
        return null;
    }
    
    

    /* ************************************************************************* *
    /* *******  사용자 로그인 후 관련 정보 세션에 담는 작업을 진행한다.  ******* *
     * ************************************************************************* */

  

    /**
     * 코인마켓정보 SESSION 에 담기
     * @param request
     * @param arrList
     */
    public static void setMarketPolicyInfo(HttpServletRequest request, FwtpMessage message) {
        if (request != null) {
            HttpSession session = request.getSession(false);
            if (session != null) {
            	
            	LinkedHashMap<String, String> hmap = message.getResponseResultList().get(0);
                logger.debug("===<< getResponseResultList.("+ 0 +") >>===\n"+ hmap );

                MarketPolicyInfo marketPolicyInfo = new MarketPolicyInfo(hmap);

                session.setAttribute(_MARKET_POLICAY_INFO_, marketPolicyInfo );
            }
        }
    }
    @SuppressWarnings("unchecked")
    public static ArrayList<LinkedHashMap<String, String>> getMarketPolicyInfo(HttpServletRequest request) {
        if (request != null) {
            HttpSession session = request.getSession(false);
            if (session != null)
                return (ArrayList<LinkedHashMap<String, String>>) session.getAttribute(_MARKET_POLICAY_INFO_);
        }
        return null;
    }

    
    /**
     * (로그인)메뉴정보 SESSION 에 담기
     * @param request
     * @param arrList
     */
    public static void setUserMenuInfo(HttpServletRequest request, ArrayList<LinkedHashMap<String, String>> arrList) {
        if (request != null) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.setAttribute(__ATTR_User_Menu_Info, arrList );

                if (arrList != null && !arrList.isEmpty()) {
                    LinkedHashMap<String, String> menuNmKey = new LinkedHashMap<String, String>();
                    LinkedHashMap<String, String> menuPaths = new LinkedHashMap<String, String>();
                    LinkedHashMap<String, String> menuHelpU = new LinkedHashMap<String, String>();
                    for (int ii = 0; ii < arrList.size(); ii++) {
                        LinkedHashMap<String, String> hmap = arrList.get(ii);
                        menuHelpU.put( hmap.get("menu_id"), hmap.get("help_uri") );
                        menuPaths.put( hmap.get("menu_id"), hmap.get("menu_path") );
                        menuNmKey.put( hmap.get("menu_id"), hmap.get("menu_nm_key") );
                    }
                    session.setAttribute(__ATTR_User_Menu_HelpU, menuHelpU );	/* 메뉴코드의 HELP-URI 보관 */
                    session.setAttribute(__ATTR_User_Menu_Paths, menuPaths );	/* 메뉴코드의 PATH(Nav~) 보관 */
                    session.setAttribute(__ATTR_User_MenuNm_Key, menuNmKey );	/* 메뉴코드의 메뉴명-KEY 보관 */
                }
            }
        }
    }
    @SuppressWarnings("unchecked")
    public static ArrayList<LinkedHashMap<String, String>> getUserMenuInfo(HttpServletRequest request) {
        if (request != null) {
            HttpSession session = request.getSession(false);
            if (session != null)
                return (ArrayList<LinkedHashMap<String, String>>) session.getAttribute(__ATTR_User_Menu_Info);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static String getUserMenuNmKey(HttpServletRequest request, String menuId) {
        if (request == null || UXUtil.isNull(menuId)) { }
        else {
            HttpSession session = request.getSession(false);
            if (session != null) {
                LinkedHashMap<String, String> menuNames = (LinkedHashMap<String, String>) session.getAttribute(__ATTR_User_MenuNm_Key);
                if (menuNames != null)
                    return menuNames.get(menuId);
            }
        }
        return null;
    }
    @SuppressWarnings("unchecked")
    public static String getUserMenuPaths(HttpServletRequest request, String menuId) {
        if (request == null || UXUtil.isNull(menuId)) { }
        else {
            HttpSession session = request.getSession(false);
            if (session != null) {
                LinkedHashMap<String, String> menuPaths = (LinkedHashMap<String, String>) session.getAttribute(__ATTR_User_Menu_Paths);
                if (menuPaths != null)
                    return menuPaths.get(menuId);
            }
        }
        return null;
    }
    @SuppressWarnings("unchecked")
	public static String getUserHelpUri(HttpServletRequest request, String menuId) {
        if (!UXUtil.isNull(menuId))
            if (request == null || UXUtil.isNull(menuId)) { }
            else {
                HttpSession session = request.getSession(false);
                if (session != null) {
                    LinkedHashMap<String, String> menuHelpU = (LinkedHashMap<String, String>) session.getAttribute(__ATTR_User_Menu_HelpU);
                    if (menuHelpU != null)
                        return menuHelpU.get(menuId);
                }
            }
            return null;
    }
    
    /**
     * (로그인)법인정보 SESSION 에 담기
     * @param request
     * @param arrList
     */
    public static void setUserCrpnInfo(HttpServletRequest request, ArrayList<LinkedHashMap<String, String>> arrList) {
        if (request != null) {
            HttpSession session = request.getSession(false);
            if (session != null)
                session.setAttribute(__ATTR_User_Crpn_Info, arrList );
        }
    }
    @SuppressWarnings("unchecked")
    public static ArrayList<LinkedHashMap<String, String>> getUserCrpnInfo(HttpServletRequest request) {
        if (request != null) {
            HttpSession session = request.getSession(false);
            if (session != null)
                return (ArrayList<LinkedHashMap<String, String>>) session.getAttribute(__ATTR_User_Crpn_Info);
        }
        return null;
    }

    /**
     * (로그인)사업장정보 SESSION 에 담기
     * @param request
     * @param arrList
     */
    public static void setUserBizrInfo(HttpServletRequest request, ArrayList<LinkedHashMap<String, String>> arrList) {
        if (request != null) {
            HttpSession session = request.getSession(false);
            if (session != null)
                session.setAttribute(__ATTR_User_Bizr_Info, arrList );
        }
    }
    @SuppressWarnings("unchecked")
    public static ArrayList<LinkedHashMap<String, String>> getUserBizrInfo(HttpServletRequest request) {
        if (request != null) {
            HttpSession session = request.getSession(false);
            if (session != null)
                return (ArrayList<LinkedHashMap<String, String>>) session.getAttribute(__ATTR_User_Bizr_Info);
        }
        return null;
    }

    /**
     * (로그인)마이메뉴 SESSION 에 담기
     * @param request
     * @param arrList
     */
    public static void setUserMyMenu(HttpServletRequest request, ArrayList<LinkedHashMap<String, String>> arrList) {
        if (request != null) {
            HttpSession session = request.getSession(false);
            if (session != null)
                session.setAttribute(__ATTR_User_My_Menu, arrList );
        }
    }
    @SuppressWarnings("unchecked")
    public static ArrayList<LinkedHashMap<String, String>> getUserMyMenu(HttpServletRequest request) {
        if (request != null) {
            HttpSession session = request.getSession(false);
            if (session != null)
                return (ArrayList<LinkedHashMap<String, String>>) session.getAttribute(__ATTR_User_My_Menu);
        }
        return null;
    }

    /**
     * (로그인)메뉴<보고서> SESSION 에 담기
     * @param request
     * @param arrList
     */
    public static void setUserBriefMenu(HttpServletRequest request, ArrayList<LinkedHashMap<String, String>> arrList) {
        if (request != null) {
            HttpSession session = request.getSession(false);
            if (session != null)
                session.setAttribute(__ATTR_User_Brief_Menu, arrList );
        }
    }
    @SuppressWarnings("unchecked")
    public static ArrayList<LinkedHashMap<String, String>> getUserBriefMenu(HttpServletRequest request) {
        if (request != null) {
            HttpSession session = request.getSession(false);
            if (session != null)
                return (ArrayList<LinkedHashMap<String, String>>) session.getAttribute(__ATTR_User_Brief_Menu);
        }
        return null;
    }

    /**
     * (로그인)SelectBox 개발자저의(모음) SESSION 에 담기
     * @param request
     * @param arrList
     */
    public static void setUserSelectbox5050(HttpServletRequest request, ArrayList<LinkedHashMap<String, String>> arrList) {
        if (request != null) {
            HttpSession session = request.getSession(false);
            if (session != null)
                session.setAttribute(__ATTR_User_Select5050, arrList );
        }
    }
    @SuppressWarnings("unchecked")
    public static ArrayList<LinkedHashMap<String, String>> getUserSelectbox5050(HttpServletRequest request) {
        if (request != null) {
            HttpSession session = request.getSession(false);
            if (session != null)
                return (ArrayList<LinkedHashMap<String, String>>) session.getAttribute(__ATTR_User_Select5050);
        }
        return null;
    }
    
    

}