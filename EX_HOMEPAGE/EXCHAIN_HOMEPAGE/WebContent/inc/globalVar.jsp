<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.finger.fwf.uxui.util.UCS, com.finger.fwf.uxui.util.JSPUtil" %>
<%@ page import="com.finger.agent.config.FcAgentConfig" %>
<script type="text/javascript">
var _UCS = {};
_UCS.COMMON2005 = "로그인 사용자 불일치로 페이지를 새로고침 합니다.";
_UCS.COMMON2006 = "서버 이상으로 로그아웃합니다.";
_UCS.COMMON2011 = "확인"; 
_UCS.COMMON2013 = "예";
_UCS.COMMON2014 = "아니오";
_UCS.MSGCDEU000 = "COMM.dialog [ Syntax Error ]: dialog type 이 잘못 되었습니다. type을 확인해 보십시요. (error, message, confirm, warning, debug)";
               
<%-- 
/* _ 표시는 공통 내부에서만 사용을 의미
 * common.jsp 에서 home 선언
 */
--%>
var INITIALIZE_BASE = INITIALIZE_BASE || "${fwf_home}"
,   INITIALIZE_BASE_IMG = "/theme/deposit/images";
<%-- session 값 저장 --%>
var _INIT_SESSION = {
        "_Pop_Alm_Perd": "${sessionScope.FWF_User_Info.popAlmPerd}"
    ,   "_Alm_Popup_Yn": "${sessionScope.FWF_User_Info.almPopupYn}"
    ,   "_Chk_Pwd_Upd_Perd": "${sessionScope.FWF_User_Info.chkPwdUpdPerd}"
    ,   "_Rptsite_Use_Yn": "${sessionScope.FWF_User_Info.rptsiteUseYn}"
    ,   "_Date_Delim": "${sessionScope.FWF_User_Info.fmtDateSeparator}"||"${sessionScope.format_date_separator}"
    ,   "_Time_Delim": "${sessionScope.FWF_User_Info.fmtTimeSeparator}"||"${sessionScope.format_time_separator}"
    ,   "_Date_Format_Order": function(dateformat){
    	   return [dateformat.indexOf("Y"),dateformat.indexOf("M"),dateformat.indexOf("D")];
        }("${sessionScope.format_date_ymd_order}")  // 날짜형식순서 (YMD 세글자를 이용 2013.04.15) > 세션변수추가(20130430)
    ,   "_Date_Format_Order_YM": function(dateformatYM){
    	   return [dateformatYM.indexOf("Y"),dateformatYM.indexOf("M")];
        }("${sessionScope.format_date_ymx_order}") // 날짜형식순서 중 년월표시 - 년월일 과 년월 표시를 분리(2013.05.07)
    ,   "_Fraction_Digits": null    // 화폐소수점단위(number 타입). 없으면 무제한
    ,   "_Today" : "${sessionScope.FWF_User_Info.loginAcsDt}" // 로그인 기준시간
    ,   "_Point": "${sessionScope.format_point_separator}"||"."
    ,   "_Thousands": "${sessionScope.format_amount_separator}"||","
    ,   "_STANDARD_SEPARATOR": "-"
    ,   "_DELAY_SECOND": Number('<%=JSPUtil.getSysInfo("NOTI_MTTR_POP_DTM", request)%>') * 1000 // 단, 값이 없으면 Null 이므로 NaN이 됨. 공통에서 처리
    ,   "_LOGIN_TIME": "${sessionScope.FWF_User_Info.loginDatetime}"
    ,   "_GRID_PAGE_RECORD": Number( "${sessionScope.FWF_User_Info.gridPageRowCnt}" || "<%= com.finger.fwf.uxui.util.JSPUtil.getSysInfo("grid_page_row_cnt", request) %>")
    };

<%-- 전역에서 날짜 포멧제어(stringformat.js 이용) - _Date_Format_Order 값에 따라 결정(2013.04.15 ) --%>
_INIT_SESSION._Date_Format = {
	DATEPICKER_YMD: function(dateformatOrder){
        var dateFormat = [];
        
        dateFormat[dateformatOrder[0]] = "yy";
        dateFormat[dateformatOrder[1]] = "mm";
        dateFormat[dateformatOrder[2]] = "dd";
        
        return dateFormat.join(_INIT_SESSION._Date_Delim);
        
    }(_INIT_SESSION._Date_Format_Order),
		
	YMD: function(dateformatOrder){
		var dateFormat = [];
		
		dateFormat[dateformatOrder[0]] = "yyyy";
		dateFormat[dateformatOrder[1]] = "MM";
		dateFormat[dateformatOrder[2]] = "dd";
		
		return dateFormat.join(_INIT_SESSION._Date_Delim);
		
	}(_INIT_SESSION._Date_Format_Order),

    MD: function(dateformatOrder){
        if (dateformatOrder[1] < dateformatOrder[2]) {
            return "MM" + _INIT_SESSION._Date_Delim + "dd";
        } else {
            return "dd" + _INIT_SESSION._Date_Delim + "MM";
        }
    }(_INIT_SESSION._Date_Format_Order),
    
	YM:  function(dateformatOrderYM){
        if (dateformatOrderYM[0] < dateformatOrderYM[1]) {
            return "yyyy" + _INIT_SESSION._Date_Delim + "MM";
        } else {
            return "MM" + _INIT_SESSION._Date_Delim + "yyyy";
        }
    }(_INIT_SESSION._Date_Format_Order_YM),
    

    HMS: "HH" + _INIT_SESSION._Time_Delim + "mm" + _INIT_SESSION._Time_Delim + "ss",
    HM: "HH" + _INIT_SESSION._Time_Delim + "mm",

};

</script>
