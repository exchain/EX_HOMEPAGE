<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.finger.fwf.uxui.util.UCS" %>
<script type="text/javascript">
var _SITE_LINK;
<%-- 시스템관리번호: 기본 --%>
var DEFAULT_LINK =
    {
        "options": 
           [
               {    "value":"link00", "text":"--------- Site Link ---------"}
              //,   {"value":"link01", "text":" NH농협은행 [ NongHyup Bank ] "           ,  "url":"http://www.nonghyup.com"}
              // ,   {"value":"link02", "text":" NH농협정보시스템 [ NongHyup IT] "        ,  "url":"http://www.nonghyupit.com"}
              ,   {"value":"link01", "text":" (주)핑거 [ Finger,Ltd. ] "               ,  "url":"http://www.finger.co.kr" }
              // ,   {"value":"link04", "text":" 금융결제원 [ KFTC ] "                    ,  "url":"http://www.kftc.or.kr"   }
              // ,   {"value":"link05", "text":" KS-NET관리사이트 [ KSNET Admin Site ]"   ,  "url":"http://bankplus.ksnet.co.kr/FIMS/login.jsp"}
              // ,   {"value":"link06", "text":" XChipher 수동설치[ Manual Installation ]",  "url":"http://gcmsdev.nonghyupit.com/download.jsp"}
           ],
        "defaultSelectedValue":"link00"
    };
<%-- 시스템관리번호: CMS0001 --%>
var CMS0001 = 
    {
        "options": 
           [
               {    "value":"link00", "text":"--------- Site Link ---------"}
              // ,   {"value":"link01", "text":" NH농협은행 [ NongHyup Bank ] "           ,  "url":"http://www.nonghyup.com"}
              // ,   {"value":"link02", "text":" NH농협정보시스템 [ NongHyup IT] "        ,  "url":"http://www.nonghyupit.com"}
               ,   {"value":"link01", "text":" (주)핑거 [ Finger,Ltd. ] "               ,  "url":"http://www.finger.co.kr" }
              // ,   {"value":"link04", "text":" 금융결제원 [ KFTC ] "                    ,  "url":"http://www.kftc.or.kr"   }
              // ,   {"value":"link05", "text":" KS-NET관리사이트 [ KSNET Admin Site ]"   ,  "url":"http://bankplus.ksnet.co.kr/FIMS/login.jsp"}
              // ,   {"value":"link06", "text":" XChipher 수동설치[ Manual Installation ]",  "url":"http://gcmsdev.nonghyupit.com/download.jsp"}
           ],
        "defaultSelectedValue":"link00"
    };
<%-- 시스템관리번호: 여기에 추가해 주십시요. --%>


<%-- ==================== --%>
<%-- 사이트 링크 설정     --%>
<%-- ==================== --%>
switch ("${_sys_mgt_no_}"){
case "CMS0001":
    _SITE_LINK = CMS0001;
    break;
    
default:
	_SITE_LINK = DEFAULT_LINK;
	break;
}
</script>