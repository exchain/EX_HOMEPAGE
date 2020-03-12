/* =============================================================================
 * System       : F-CMS
 * FileName     : fwf-commom.js
 * Version      : 1.1
 * Description  : 개발자 공통 사용 script
 * Author       : 이 병 직
 * Date         : 2013.03.29
 * -----------------------------------------------------------------------------
 * Modify Date  :
 * -----------------------------------------------------------------------------
 * Etc          : 외부에서 접근 가능 script 
 * -----------------------------------------------------------------------------
 * version 1.0 	: 2012.11.23
 * version 1.1  : 2013.03.29
 * 				: //////// 명칭변경 ////////
 * 				: COMM.updateThroughAjax 명칭 변경 -> COMM.blindByTasking
 * 				: COMM.selectThroughAjax 명칭 변경 -> COMM.showByTasking
 * 				: COMM.configuration 명칭 변경 -> COMM.marketReload
 * 
 * 				: /////// 삭제 ////////
 * 				: COMM._CommonMessage  -> _UCS 에 담겨 있음.
 * 				: COMM._setCommonMessage
 * 
 * 				: ////// 신규 //////
 * 				: COMM.unformat.date() 날짜포멧을 제거함
 * 				: COMM.unformat.time() 시간포멧을 제거함
 * 				: COMM.unformat.currency() 화폐포멧을 제거함
 * 				: COMM.getToday() 	  서버 시간을 포멧적용 또는 formless 로 가져옴
 * -----------------------------------------------------------------------------
 * Copyrights 2013 by Finger. All rights reserved. ~ by Finger.
 * =============================================================================
 */

/**
 * @description <h3>공통 javaScript API (문서화)</h3>
 * <br/>
 * COMM 객체는 finger-wf의 공통함수(메소드)를 속성으로 가지고 있다.
 * <br/>--------<br/>
 * 자바스크립트는 자바와 언어구조가 다르므로, 편의상 다음과 같이 분류한다.
 * <dl>
 * <dt><strong>Classes</strong>:</dt><dd>자바스크립트 함수(메소드) - COMM 속성에 선언된 함수.</dd>
 * <dt><strong>Event</strong>:</dt><dd>자바스크립트 함수(메소드) - COMM 속성에 선언된 함수 중 내부사용 함수.</dd>
 * <dd>(Classes 와 달리 개발자에게 기능을 보장하지 않는다.)</dd>
 * <dt><strong>Namespace</strong>:</dt><dd>자바스크립트 객체 - COMM 속성에 선언된 객체(속성 집합)</dd>
 * <dt><strong>Members</strong>:</dt><dd>해당 객체가 가지고 있는 속성 중 함수 이외의 것.</dd>
 * <dt><strong>Method</strong>:</dt><dd>해당 객체가 가지고 있는 속성 중 함수.</dd>
 * </dl>
 * @version 1.1
 * @since 2013.05.27
 * @author 이병직
 * @namespace COMM
 */
var COMM  = COMM || {}
,   DEBUG_MODE = DEBUG_MODE || false;

/**
 * @description fwf-common 버전
 * @version 1.1
 * @since 2013.03
 * @author 이병직
 * @memberOf COMM
 * @type {String}
 */
COMM.version = "1.1";

if (window.console) {
	console.info("fwf-common.js Version: ", COMM.version);
}

/**
 * @description 사용할 DOM요소 및 속성을 정의( 각 멤버는 상수집합이다 ).
 * <br/>--------<br/>
 * @version 1.1
 * @since 2013.03.29
 * @author 이병직
 * @namespace COMM._INIT
 * @memberOf COMM
 */
COMM._INIT = {
	/**
	 * @description common_dialog 중 alarm
	 * @constant ALARM
	 * @memberOf COMM._INIT
	 */
		ALARM: {
            width: 420
        ,   base: "#alarm_warpper" // ui.dialog 로 변할 요소
        ,   count: "#fwf_alarm_count"
        ,   title: '<div class="fwf_alarm_title"></div>'
        ,   dialogClass: "fwf_alarm_wrapper_box"
        ,   confirmFunc: function(){}
        }

	/**
	 * @description common_dialog 중 type error
	 * @constant ERROR
	 * @memberOf COMM._INIT
	 */
    ,   ERROR: {
            width: 455
        ,   base: "#error_wrapper"
        ,   message: "#error_message"
        ,   msgDetail: "#more_error"
        ,   dialogClass:"error-wrapper-box"
        }

	/**
	 * @description common_dialog 중 type message
	 * @constant MESSAGE
	 * @memberOf COMM._INIT
	 */
    ,   MESSAGE: {
            width: 455
        ,   base: "#message_wrapper"
        ,   message: "#message_message"
        ,   dialogClass:"message-wrapper-box" 
        }
    
	/**
	 * @description common_dialog 중 type confirm
	 * @constant CONFIRM
	 * @memberOf COMM._INIT
	 */
    ,   CONFIRM: {
            width: 455
        ,   base: "#confirm_wrapper"
        ,   message: "#confirm__message"
        ,   dialogClass:"confirm-wrapper-box"
        }
    
	/**
	 * @description common_dialog 중 type warning 
	 * @constant WARNING
	 * @memberOf COMM._INIT
	 */
    ,   WARNING: {
            width: 320
        ,   base: "#warning_wrapper"
        ,   message: "#warning_message"
        ,   dialogClass:"warning-wrapper-box"
    }
    
	/**
	 * @description common_dialog 중 type debug 
	 * @constant DEBUG
	 * @memberOf COMM._INIT
	 */
    ,   DEBUG: {
            width: 455
        ,   base: "#debug_wrapper"
        ,   message: "#debug_message"
        ,   msgDetail: "#debug_more"
        ,   dialogClass:"debug-wrapper-box"
        }
    
	/**
	 * @description common_dialog 중 type progressbar 
	 * @constant PROGRESSBAR
	 * @memberOf COMM._INIT
	 */
    ,   PROGRESSBAR: {
            width: 300
        ,   base: "#progressbar_wrapper"
        ,   dialogClass: "progressbar-wrapper-box"
        ,   image: INITIALIZE_BASE_IMG +"/prograss.gif"
        }
    
	/**
	 * @description common_dialog 중 popup
	 * @constant POPUP
	 * @memberOf COMM._INIT
	 */
    ,   POPUP: {
            base: "#popup_wrapper"
        ,   body: "#fwf_popup_body"
        ,   dialogClass:"popup-wrapper-box"
        }
    
	/**
	 * @description common_dialog 중 subPopup
	 * @constant SUBPOPUP
	 * @memberOf COMM._INIT
	 */    
    ,   SUBPOPUP: {
            base: "#sub_popup_wrapper"
        ,   body: "#fwf_sub_popup_body"
        ,   dialogClass:"popup-wrapper-box"
    	}
    
	/**
	 * @description common_dialog 중 type session
	 * @constant SESSION_EXPIRATION
	 * @memberOf COMM._INIT
	 */
    ,   SESSION_EXPIRATION: {
            base: "#session_expiration_wrapper"
        ,   dialogClass:"session_expiration-wrapper-box"
        ,   loginPage: INITIALIZE_BASE + "/uxLogout"
        ,   message: "#logout-message"
        ,   sessionExpiration: "<li>" + _UCS.COMMON2005 + "</li>"
        ,   serverError: "<li>" + _UCS.COMMON2006 + "</li>"
        //,   noAuthority: "<li>" + _UCS.COMMON2007 + "</li>"
        ,   width: 455
        }
    
	/**
	 * @description common_dialog 요소
	 * @constant DIALOG
	 * @memberOf COMM._INIT
	 */
    ,   DIALOG: {
            removeCloseBtn: ".ui-dialog-titlebar-close.ui-corner-all"
        ,   typeError:	"error"
        ,   typeMessage:"message"
        ,   typeConfirm:"confirm"
        ,   typeWarning:"warning"
        ,   typeSession:"session"
        ,   typeDebug:	"debug"
        }
    
	/**
	 * @description Help button
	 * @constant HELP
	 * @memberOf COMM._INIT
	 */
    ,   HELP: {
            btnHelp: ".fwf-icon-help"
        ,   btnHelpDisClass: "fwf-icon-help-dis"
    	}
    
	/**
	 * @description datepicker 및 monthpicker
	 * @constant DATEPICKER
	 * @memberOf COMM._INIT
	 */
    ,   DATEPICKER: {
    	    datepicker: ".datepicker"
    	,   datepicker_reference: ".datepicker_reference"
    	,   datepicker_prev: ".datepicker_prev"
    	,   datepicker_next: ".datepicker_next"
    	,   monthpicker: ".datepicker_month"
    	,   calendarImg: INITIALIZE_BASE_IMG +"/calendar.png"
    	,   dynamicInputFile: "datepicker10.jsp" 	// 파일명은 메세지에 사용
   		,   dynamicInputTerm: "dynamic_input_term_"
		,   dynamicInputState:"dynamic_input_state_"
        }
    
	/**
	 * @constant monthpicker
	 * @memberOf COMM._INIT
	 */
    ,   MONTHPICKER: ".datepicker_month"
    
	/**
	 * @description 알림함
	 * @constant ALARM_BOX
	 * @memberOf COMM._INIT
	 */
    ,   ALARM_BOX: {
    	    alarmCountElement: "#alarmCount"
    	,   alarmNotiIconElement: ".fwf-icon-notice"
    	,   alarmNewImage: INITIALIZE_BASE_IMG +"/ico_notice_active.gif"
    	,   alarmZeroImage: INITIALIZE_BASE_IMG +"/ico_notice.png"
    	}
    
	/**
	 * @description 결재함
	 * @constant APPROVAL_BOX
	 * @memberOf COMM._INIT
	 */
    ,   APPROVAL_BOX: {
    	    approvalCountElement: "#approvalCount"
    	,   approvalNotiIconElement: ".fwf-icon-pay"
    	,   approvalNewImage: INITIALIZE_BASE_IMG +"/ico_document_active.gif"
    	,   approvalZeroImage: INITIALIZE_BASE_IMG +"/ico_document.png"
    	}
    
	/**
	 * @description input 포맷 및 delim
	 * @constant FORMAT
	 * @memberOf COMM._INIT
	 */
    ,   FORMAT:{
            date_delim: _INIT_SESSION._Date_Delim
        ,   time_delim: _INIT_SESSION._Time_Delim
        ,  typeNumber: "number"
        ,  typePhone: "phone"
        ,  typeDate: "date"
        ,  typeCardno: "cardno"
        ,  typeBizno: "bizno"
        ,  typeGridNumber: "grdnum" // 소수점 0
        ,  typeGridNumber2: "grdnum2" // 소수점 2개 허용
        ,  typeAccount: "account"
        ,  typeFloat: "float"
        ,  typeCurrency: "currency" // 소수점 허용
        ,  typeCurrency0: "currency0" // 소수점 불가
        ,  typeKorea: "kor"
        ,  typeEnglish: "eng"
        ,  typeAlphabet: "alphabet"
        }
    
	/**
	 * @description formatter
	 * @constant FORMATTER
	 * @memberOf COMM._INIT
	 */
    ,   FORMATTER:{
    	    answerTime: "answerTime"
    	,   ymd: "ymd"
        ,   hm: "hm"
        ,   ym: "ym"
        ,   md: "md"
        ,   hms: "hms"
        ,   ms: "ms"
        ,   zipCode: "zipCode"
        ,   currency: "currency"
        ,   currency2: "currency2"
        ,   crpnNo: "crpnNo"
        ,   bizrNo: "bizrNo"
        ,   gridColModelClass: "number_st"	//그리드 currency 를 위한 class
        }
    
	/**
	 * @description unformat 등 함수에서 연산을 줄이기 위해 미리 계산
	 * @constant REGEX
	 * @since 2013.04.26
	 * @memberOf COMM._INIT
	 */
    ,   REGEX:{
    		regx:/([\.\*\_\'\(\)\{\}\+\?\\])/g
    	,   regexDate:new RegExp("["+ _INIT_SESSION._Date_Delim.replace(this.regx,"\\$1")+"]", "g")
    	,   regexTime:new RegExp("["+ _INIT_SESSION._Time_Delim.replace(this.regx,"\\$1")+"]", "g")
    	,   regexThousand:new RegExp("["+ _INIT_SESSION._Thousands.replace(this.regx,"\\$1")+"]", "g")
    	,   regexPoint:new RegExp("["+ _INIT_SESSION._Point.replace(this.regx,"\\$1")+"]", "g")
        ,   regexComm:new RegExp("["+ _INIT_SESSION._STANDARD_SEPARATOR.replace(this.regx,"\\$1")+"]", "g")
    
        ,   regexInputPointCheck: new RegExp ( "[^0-9\\" +_INIT_SESSION._Point + "]" )
    
        ,   testDate: new RegExp("[\\d]{2}[\\"+_INIT_SESSION._Date_Delim+"][\\d]{2}")	// ...00[00-00]00...
	    ,   testTime: new RegExp("[\\s]?[\\d]{2}[\\"+_INIT_SESSION._Time_Delim+"][\\d]{2}")	// ...[]?[00-00]000... //일자+공백+시간 고려
        ,   testThousand: new RegExp("[\\d][\\"+_INIT_SESSION._Thousands+"][\\d]{3}")		// ...00[0,000]00...
        ,   testPoint: new RegExp("[\\d][\\"+_INIT_SESSION._Point+"]")					// ...00[0.]00...
	    ,   testComm: new RegExp("[\\d][\\"+_INIT_SESSION._STANDARD_SEPARATOR+"][\\d]")	// ...00[0-0]00...
        }
    
	/**
	 * @description select box 중 전체선택 상수(ALL로 통일)
	 * @constant SELECT_ALL
	 * @memberOf COMM._INIT
	 */
    ,   SELECT_ALL: {
	        text: "--ALL--"// 2013.04.19 ALL로 통일.// _UCS.COMMON2010 전체
	    ,   value:"ALL"
	    }
    
	/**
	 * @description standard object 체크
	 * @constant DECLARE_CHECK
	 * @memberOf COMM._INIT
	 */
	,   DECLARE_CHECK: {
	        require:"(require)"
	    ,   regx: /(require)/
	    ,   requireWS:"(whiteSpace)"
	    ,   regxWS: /(whiteSpace)/
	    // dialog ID를 다르게 갈 경우 다음을 이용한다. fullCode 는 (keyPrefixUCS + prefixMID + 000)
	    ,   keyPrefixUCS:"MSGCD" 	// UCS에 정의된 명명규칙 prefix (총10자리)
	    ,   prefixMID: "EU"			// Message ID 명명규칙 prefix (총5자리)
		}
	
	/**
	 * @description 안내문구(주의사항)
	 * @constant NOTICE
	 * @memberOf COMM._INIT
	 */
    ,   NOTICE:{
    		btnSrc: INITIALIZE_BASE_IMG + "/notice_img.png"
    	,   contentSrc: INITIALIZE_BASE_IMG + "/notice_img_ov.png" // 현재 미사용(13.04.04)
    	}
    
	/**
	 * @descriptionkeyCode Map
	 * @constant KEYCODE
	 * @memberOf COMM._INIT
	 */
    ,   KEYCODE:{
    		enter:"13"
    	}
    
	/**
	 * @description brief 부분 (탭을 통한 그리드 & 차트)
	 * @constant BRIEF
	 * @memberOf COMM._INIT
	 */
    // jsp에서 tab 형식에 맞게 구조가 되어 있어야 동작합니다. (04.29)
    ,   BRIEF:{
    	    gridBtn:"grid_btn"
    	,   plotBtn:"plot_btn"
    	,   gridView:"grid_view"
    	,   plotView:"plot_view"
    	,   selectBtnClass: "sub_tab_focus"
    	,   briefTabID: "brief_tab"
    	}
    
	/**
	 * @description sessionExpirationCheck 경로
	 * @constant sessionExpirationCheck
	 * @memberOf COMM._INIT
	 */
    ,   sessionExpirationCheck: INITIALIZE_BASE + "/page/process/sessionExpirationCheck.jsp"
    
};

if (DEBUG_MODE) {
/**
 * @description COMM 표준함수선언 (개발자 가이드) - 한글 기준, Debug 모드에서 적용.
 * <br/>--------<br/>
 * 함수 파라미터 이름을 체크하고, 오류시 개발자에게 메세지를 제공하기 위한 표준함수선언.
 * <br/>
 * 각 멤버는 해당 함수 사용시 사용할 수 있는 속성목록을 가지고 있다.
 * @version 1.1
 * @since 2013.03.29
 * @author 이병직
 * @namespace COMM._STANDARD
 * @memberOf COMM
 */
  COMM._STANDARD = {
		/**
		 * @constant dialog
		 * @memberOf COMM._STANDARD
		 */  
        dialog: {
                _function_name_: "COMM.dialog"
            ,   type: COMM._INIT.DECLARE_CHECK.require + (DEBUG_MODE?"error, message, confirm, warning, debug, session":"")
            ,   id: COMM._INIT.DECLARE_CHECK.require + (DEBUG_MODE?"메세지코드 (5자리)":"")
            ,   message:"메세지"
            ,   messageConvertParam:"메세지 치환 파라미터 (%를 param으로 치환)"
            ,   detail:"상세메세지 - error 타입에만 적용됨"
            ,   callback:"콜백함수 (Yes 및 확인 버튼)"
            ,   callbackNo:"콜백함수 (No 버튼) - confirm 타입에만 적용됨"
            ,   data:"콜백함수에서 사용할 데이타(인자)"
        } ,
        
		/**
		 * @constant layer
		 * @memberOf COMM._STANDARD
		 */ 
        layer: {
                _function_name_: "COMM.layer"
            ,   w: COMM._INIT.DECLARE_CHECK.require + "가로길이"
            ,   h: COMM._INIT.DECLARE_CHECK.require + "세로길이"
            ,   m:"true, false // modal 여부"
            ,   param:"&param1=1&param2=2 //전송될 파라미터"
            ,   isSub:"서브 팝업 여부"
            ,   callback: "콜백!"
            ,   data: "콜백 사용 데이타"
            ,   closeAction: "닫힐 때 action"
        },
        
		/**
		 * @constant winOpen
		 * @memberOf COMM._STANDARD
		 */ 
        winOpen: {
                _function_name_: "COMM.winOpen"
            ,   w:"가로길이"
            ,   h:"세로길이"
            ,   name:"창이름"
            ,   resizable:"yes //없으면 리사이즈 불가"
        },
        
    	/**
    	 * @description 특정 value 를 가진 options 삭제
    	 * @constant selectOptionRemove
    	 * @memberOf COMM._STANDARD
    	 */
        selectOptionRemove: {
                _function_name_:"COMM.selectOptionRemove"
            ,   formID: COMM._INIT.DECLARE_CHECK.require + "폼ID"
            ,   selectName: COMM._INIT.DECLARE_CHECK.require + "select name"
            ,   removeValue: COMM._INIT.DECLARE_CHECK.requireWS + "삭제할 option이 가진 value"
        },
        
      	/**
    	 * @description //// select box series ////
    	 * @constant select5000ToSelect
    	 * @memberOf COMM._STANDARD
    	 */
        select5000ToSelect: {
                _function_name_:"COMM.select5000ToSelect"
            ,   formID: COMM._INIT.DECLARE_CHECK.require + "폼ID"
            ,   sysMgtNo: COMM._INIT.DECLARE_CHECK.require + "시스템관리번호"
            ,   sboxGroup: COMM._INIT.DECLARE_CHECK.require + "group 이름"
            ,   selectName: COMM._INIT.DECLARE_CHECK.require + "select 이름 입니다."
            ,   selectAll: "true 이면 -전체(ALL)- 를 붙임"
            ,   selectAllValue: "전체일때 사용할 value 값. 기본값은 ALL"
            ,   selectAllText: "전체일때 사용할 Text 값. 기본값은 ALL"
            ,   chageOption: "select 값이 변할때 동작하는 함수 "
            ,   selectedVal: "select값 selectedVal값으로 선택됨 없으면 무시"
            ,   callback: "콜백"
        },
        
      	/**
    	 * @description //// select box series //// 
    	 * @constant userSelect5050ToSelect
    	 * @memberOf COMM._STANDARD
    	 */
        userSelect5050ToSelect: {
                _function_name_:"COMM.userSelect5050ToSelect"
            ,   formID: COMM._INIT.DECLARE_CHECK.require + "폼ID"
            ,   groupData: COMM._INIT.DECLARE_CHECK.require + "group data"
            ,   sboxGroup: COMM._INIT.DECLARE_CHECK.require + "group 이름"
            ,   selectName: COMM._INIT.DECLARE_CHECK.require + "select 이름 입니다."
            ,   selectAll: "true 이면 -전체(ALL)- 를 붙임"
            ,   selectAllValue: "전체일때 사용할 value 값. 기본값은 ALL"
            ,   selectAllText: "전체일때 사용할 Text 값. 기본값은 ALL"
            ,   chageOption: "select 값이 변할때 동작하는 함수 "
            ,   selectedVal: "select값 selectedVal값으로 선택됨 없으면 무시"
            ,   callback: "콜백"
        },
        
      	/**
    	 * @description //// select box series //// 
    	 * @constant comCodeToSelect
    	 * @memberOf COMM._STANDARD
    	 */
        comCodeToSelect: {
                _function_name_:"COMM.comCodeToSelect"
            ,   formID: COMM._INIT.DECLARE_CHECK.require + "폼ID"
            ,   selectName: COMM._INIT.DECLARE_CHECK.require + "select 이름 입니다."
            ,   taskID: "타스크 ID (시스템)SystemInfo (전체)CrpnInfo, BizrInfo,(권한에 따라)UserCrpnInfo, UserBizrInfo 중 하나"
            ,   attr1Cd: "attr코드1 //공통 코드일때만 사용."
            ,   attr2Cd: "attr코드2 //공통 코드일때만 사용."
            ,   attr3Cd: "attr코드3 //공통 코드일때만 사용."
            ,   grpCd: "grp코드 //공통 코드일때만 사용."
            ,   sysMgtNo: "CrpnInfo, BizrInfo 에서 사용"
            ,   crpnSeqNo: "BizrInfo 에서 사용"
            ,   selectAll: "true 이면 -전체(ALL)- 를 붙임"
            ,   selectAllValue: "전체일때 사용할 value 값. 기본값은 ALL"
            ,   selectAllText: "전체일때 사용할 Text 값. 기본값은 ALL"
            ,   chageOption: "select 값이 변할때 동작하는 함수 "
            ,   selectedVal: "select값 selectedVal값으로 선택됨 없으면 무시"
    	    ,   param: "전달할 파라미터"
            ,   callback: "콜백"
        },

      	/**
    	 * @description //// select box series //// (사용자 task 를 이용한 select)
    	 * @constant actionToSelect
    	 * @memberOf COMM._STANDARD
    	 */
        actionToSelect: {
            	_function_name_:"COMM.actionToSelect"
	        ,   formID: COMM._INIT.DECLARE_CHECK.require + "폼ID"
	        ,   selectName: COMM._INIT.DECLARE_CHECK.require + "select 이름 입니다."
	        ,   taskID: COMM._INIT.DECLARE_CHECK.require + "타스크 ID"
	        ,   textCol: COMM._INIT.DECLARE_CHECK.require  + "text로 사용할 컬럼명"
	        ,   valueCol: COMM._INIT.DECLARE_CHECK.require  + "value로 사용할 컬럼명"
	        ,   selectAll: "true 이면 -전체(ALL)- 를 붙임"
	        ,   selectAllValue: "전체일때 사용할 value 값. 기본값은 ALL"
	        ,   selectAllText: "전체일때 사용할 Text 값. 기본값은 ALL"
	        ,   chageOption: "select 값이 변할때 동작하는 함수 "
	        ,   selectedCol: "해당 column 값이 Y인 값을 기본값으로(둘 이상이면 논리오류). 없으면 무시"
	        ,   param: "전달할 파라미터"
	        ,   async: "ajax async 여부"
	        ,   callback: "콜백"
        },
    
      	/**
      	 * @description //// select box series ////
    	 * @constant comCodeToString
    	 * @memberOf COMM._STANDARD
    	 */
        comCodeToString: {
                _function_name_:"COMM.comCodeToString"
            ,   formID: COMM._INIT.DECLARE_CHECK.require + "폼ID"
            ,   async: "ajax async 여부"
            ,   taskID: "타스크 ID (시스템)SystemInfo (전체)CrpnInfo, BizrInfo,(권한에 따라)UserCrpnInfo, UserBizrInfo 중 하나"
            ,   attr1Cd: "attr코드1 //공통 코드일때만 사용."
            ,   attr2Cd: "attr코드2 //공통 코드일때만 사용."
            ,   attr3Cd: "attr코드3 //공통 코드일때만 사용."
            ,   grpCd: "grp코드 //공통 코드일때만 사용."
            ,   sysMgtNo: "CrpnInfo, BizrInfo 에서 사용"
            ,   crpnSeqNo: "BizrInfo 에서 사용"
            ,   selectAll: "true 이면 -전체(ALL)- 를 붙임"
            ,   selectAllValue: "전체일때 사용할 value 값. 기본값은 ALL"
    	    ,   selectAllText: "전체일때 사용할 Text 값. 기본값은 ALL"            	
            ,   callback: "callback 함수"
            ,   selectedVal: "select값 selectedVal값으로 선택됨 없으면 무시"
            ,   onlyText: "text만 필요한 경우"
        },
        
      	/**
      	 * @description //// select box series ////
    	 * @constant comCodeToArray
    	 * @memberOf COMM._STANDARD
    	 */
        comCodeToArray: {
                _function_name_:"COMM.comCodeToArray"
            ,   isArray: COMM._INIT.DECLARE_CHECK.require + "array 모드"
            ,   async: "ajax async 여부"
            ,   taskID: "타스크 ID (시스템)SystemInfo (전체)CrpnInfo, BizrInfo,(권한에 따라)UserCrpnInfo, UserBizrInfo 중 하나"
            ,   grpCd: "grp코드 //공통 코드일때만 사용."
            ,   sysMgtNo: "CrpnInfo, BizrInfo 에서 사용"
            ,   crpnSeqNo: "BizrInfo 에서 사용"
            ,   callback: "callback 함수"
        },
        
      	/**
      	 * @description //// 조회, 갱신 series ////
    	 * @constant showByTasking
    	 * @memberOf COMM._STANDARD
    	 */
        showByTasking: {
                _function_name_: "COMM.showByTasking"
            ,   taskID: COMM._INIT.DECLARE_CHECK.require  + "타스크 ID "
            ,   pageID: COMM._INIT.DECLARE_CHECK.require  + "페이지ID //서버측 메세지 처리에 사용"
            ,   formID: COMM._INIT.DECLARE_CHECK.require  + "폼 아이디."
            ,   gridID: "그리드 ID"
            ,   transForm: "폼 전송여부. 기본값은 true" 
            ,   transGrid: "그리드 전송여부. 기본값은 false" 
            ,   transGridStatus: "CUD 상태 그리드만 전송. transGrid와 같이 사용 못함. return 값이 false이면 변경된 값이 없다는 의미."
            ,   transMultiselect: "그리드 멀리셀렉트 전송여부. 기본값은 false. transGrid와 같이 사용 못함"
            ,   callback: "콜백함수"
            ,   data: "콜백에 사용할 데이타"
            ,   async: "동기화 여부"
            ,   type: "멀티데이터여부 기본값은COM"
        },
        
      	/**
      	 * @description //// 조회, 갱신 series ////
    	 * @constant blindByTasking
    	 * @memberOf COMM._STANDARD
    	 */
        blindByTasking: {
                _function_name_: "COMM.blindByTasking"
            ,   taskID: COMM._INIT.DECLARE_CHECK.require + "타스크 ID "
            ,   pageID: COMM._INIT.DECLARE_CHECK.require + "페이지ID //서버측 메세지 처리에 사용"
            ,   formID: "폼 아이디."
            ,   gridID: "그리드ID" 
            ,   transForm: "폼 전송여부. 기본값은 true" 
            ,   transGrid: "그리드 전송여부. 기본값은 false" 
            ,   transGridStatus: "CUD 상태 그리드만 전송. transGrid와 같이 사용 못함. return 값이 false이면 변경된 값이 없다는 의미."
            ,   transMultiselect: "그리드 멀리셀렉트 전송여부. 기본값은 false. transGrid와 같이 사용 못함"
            ,   callback: "콜백함수"
            ,   data: "콜백에 사용할 데이타"
            ,   async: "동기화 여부"
        },
        
      	/**
      	 * @description 페이지 이동
    	 * @constant nextPage
    	 * @memberOf COMM._STANDARD
    	 */
        nextPage: {
                _function_name_: "COMM.nextPage"
            ,   pageID: COMM._INIT.DECLARE_CHECK.require + "페이지 ID"
            ,   url: COMM._INIT.DECLARE_CHECK.require + "이동할 URL."
            ,   param: "넘길 파라미터"
        },
        
      	/**
      	 * @description monthpicker 한 쌍을 기간으로 사용시, 기간체크
      	 * <br/>
      	 * 내부함수에 사용되므로 private 처리
    	 * @constant pairOfMonthpicker
    	 * @private 
    	 * @memberOf COMM._STANDARD
    	 */
        pairOfMonthpicker: {
               _function_name_: "COMM.pairOfMonthpicker"
           ,   prevID: COMM._INIT.DECLARE_CHECK.require + "monthpicker prev ID"
           ,   nextID: COMM._INIT.DECLARE_CHECK.require + "monthpicker next ID"
           ,   interval: "조회 기간(년)"
        },
        
      	/**
      	 * @description 폼안에 있는 input 전체에 특정 key입력을 특정버튼과 바인드
    	 * @constant inputKeydownBtnclick
    	 * @memberOf COMM._STANDARD
    	 */
        inputKeydownBtnclick: {
                _function_name_: "COMM.inputKeydownBtnclick"
            ,   formID: COMM._INIT.DECLARE_CHECK.require + "input을 감싼 form ID"
            ,   btnID: COMM._INIT.DECLARE_CHECK.require + "동작할 버튼 ID"
            ,   key: COMM._INIT.DECLARE_CHECK.require + "동작시킬 key 명칭"
            ,   beforeAction: "클릭전에 동작 action. return값이 true이면 btn click. false이면 정지"
        },
        
      	/**
      	 * @description 세션(user_info)에 있는 비밀번호 만료가 Y 이면 로그아웃되는 함수
    	 * @constant logoutPwdChange
    	 * @memberOf COMM._STANDARD
    	 */
        logoutChkPwdUpdPerd: {
            	_function_name_: "COMM.logoutChkPwdUpdPerd"
            ,   logout: "로그아웃 될 때 동작할 함수. 인수로 로그아웃 함수를 받는다."
            ,   stay: "로그아웃 되지 않을 때 동작할 함수"
            ,   isPwdChange: COMM._INIT.DECLARE_CHECK.require + "비밀번호 변경여부"
        }
        
  };

// Debug Mode 가 아니면
} else {
	COMM._STANDARD = {};
}

/**
 * @description 전역변수 공간
 * @version 1.1
 * @since 2013.04.01
 * @author 이병직
 * @namespace global
 * @memberOf COMM
 */
COMM.global = {
		/**
		 * @description 프로그레스바 동작시각
		 * @type {Number}
		 */
        progressTimeStamp: 0
};

/**
 * @description 메세지 전용 객체 속성 표시
 * @version 1.0
 * @since 2013.01
 * @author 이병직
 * @memberOf COMM
 * @event
 * @param {Object} obj JSON형식의 리터럴객체
 * @returns {String} 객체이름과 값이 나열된 문자열
 */
COMM._propertiesToString = function (obj) {
    var str = "\{<br/>";
    for (var x in obj) {
        str += x + ":" + obj[x] + "<br/>";
    }
    return str += "\}";
};

/**
 * @description 함수 선언 검사 (없는 속성 체크, 필수 속성 체크) + 속성자체 여부 체크
 * @version 1.0
 * @since 2013.01
 * @author 이병직
 * @memberOf COMM
 * @event
 * @param {Object} declareObj 검사대상Obj (JSON형식의 리터럴객체)
 * @param {Object} standardObj 표준함수Obj (JSON형식의 리터럴객체)
 * @returns {boolean} true 면 정상. false 면 오류.
 */
COMM._declareCheck = function (declareObj, standardObj) {
	/**
	 * COMM._INIT.DIALOG
	 * @memberOf COMM._declareCheck
	 */
	var DIALOG = COMM._INIT.DIALOG;
	
	if(DEBUG_MODE === false){
		return true;	// 검사 통과
	}
    
    if(declareObj) {
        for (var x in declareObj) {
            if (!standardObj.hasOwnProperty(x)) {
                COMM.dialog({
                    type: DIALOG.typeDebug
                ,   id: "EU001"
                ,   messageConvertParam: [standardObj._function_name_, x] 
                ,   detail: COMM._propertiesToString(standardObj)
                });
                return false;
            }
        };
        
        for (var x in standardObj) {
            if(COMM._INIT.DECLARE_CHECK.regx.test(standardObj[x])) {
                if (!declareObj.hasOwnProperty(x)){
                    COMM.dialog({
                        type: DIALOG.typeDebug
                    ,   id: "EU002"
                    ,   messageConvertParam: [standardObj._function_name, x]
                    });
                    return false;
                }
                
                if (declareObj[x]) {
                    // 정상
                } else {
                    COMM.dialog({
                        type: DIALOG.typeDebug
                    ,   id: "EU003"
                    ,   messageConvertParam: [standardObj._function_name, x]
                    });
                    return false;
                }
            } 
        }
        return true;
    } else {
        COMM.dialog ({
            type: DIALOG.typeDebug
        ,   id: "EU004"
        });
        return false;
    }
};

/**
 * @description ajax 에러 표시 함수 - ajax 에러 발생시 dialog로 표시한다.
 * @version 1.1
 * @since 2013.03
 * @author 이병직
 * @event
 * @param {Object} xhr ajax xhr
 * @param {String} xhr.devMethod 호출한 메소드 명칭 (1.1버전부터)
 * @param {String} status ajax status
 * @param {String} error ajax error
 */
COMM._ajaxError = function (xhr,status,error){
    COMM.dialog ({
        type: COMM._INIT.DIALOG.typeError
    ,   message: "ajax error: "+ xhr.status+ " " + xhr.statusText + "<br/><br/>(Message From WebBrowser Engine)"
    ,   id: "Ajax Error"
    ,   detail: "devMethod: "+ xhr.devMethod + "<br/>responseText: " + xhr.responseText
    });
};

/**
 * @description ajax 정지시 함수를 한 번만 동작시키는 함수.
 * <br/>--------<br/>
 * 주의! ajax 호출순서가 명확한 경우에만 사용을 권한다.
 * @version 1.1
 * @since 2013.05.06
 * @author 이병직
 * @event
 * @deprecated 1.1 부터 내부함수에서 사용하지 않는다.
 * @param {Object} dataObj 헨들러에 전달할 객체(인자) 
 * @param {Function} handler 이벤트 발생시 동작할 함수
 */
COMM._ajaxStopOnce = function (dataObj, handler) {
	// 1.9.1 부터, ajax 이벤트는 document 에 첨부해야 한다. // 파라미터 elementSID 삭제
    $(document).ajaxStop(function(){
        $(document).off("ajaxStop");
        handler(dataObj);
    });
};

/**
 * @description 정의되지 않은 id(메세지코드) 메세지 반환
 * @version 1.0
 * @since 2013.01
 * @author 이병직
 * @event
 * @param {String} msg 메세지
 * @returns {String} msg 내용이 없는 경우 정의된 메세지 반환
 */
COMM._undefineMessage = function (msg) {
    if (msg === "" || typeof msg === "undefine" || typeof msg === "null") {
        return _UCS.MSGCDEU022;
    } else {
        return msg;
    }
};


/**
 * @description 파라미터 A,B 타입이 다를 때 (A),(B),(A,B),() 4가지 경우 체크 및 모두 사용가능하게 자동변환해주는 함수
 * @version 1.1
 * @since 2013.04.10
 * @author 이병직
 * @event
 * @param {Object[]} parametersCheck 검사할 파라미터 객체가 담긴 배열
 * @param {Object} parametersCheck.0 검사할 값과 타입0
 * @param {String} parametersCheck.0.value 검사할 값0
 * @param {String} parametersCheck.0.type  함수 인자 타입0
 * @param {Object} parametersCheck.1 검사할 값과 타입1
 * @param {String} parametersCheck.1.value 검사할 값1
 * @param {String} parametersCheck.1.type 함수 인자 타입1
 * @returns {Object|Boolean} 통과여부 혹은 변환된 객체{a:값,b:값}를 반환
 */
COMM._param2Check = function(parametersCheck){
	var debugMode = function(msgID, msg1,msg2){
		if (DEBUG_MODE) {
			COMM.dialog({
				type:COMM._INIT.DIALOG.typeDebug,
				id: msgID,
				messageConvertParam: [msg1,msg2]
			});
		}
		return false;
	};
	
	// A는 A타입 일때
	if (typeof parametersCheck[0].value === parametersCheck[0].type) {
		// A 는 타입일치, B 는 불일치
		if(DEBUG_MODE && typeof parametersCheck[1].value !== "undefined" && typeof parametersCheck[1].value !== parametersCheck[1].type){
			return debugMode("EU016","2", parametersCheck[1].type); // %1번째 매개변수가 %2 타입이 아닙니다.
		}
	// A는 B타입 일때
	} else if (typeof parametersCheck[0].value === parametersCheck[1].type){
		// A 는 B 타입이고, B 가 A 타입 이거나 없을때
		if (!parametersCheck[1].value || typeof parametersCheck[1].value === parametersCheck[0].type ) {
			return {a:parametersCheck[1].value, b:parametersCheck[0].value};
		} else if(DEBUG_MODE) {
		// A 는 B 타입이고, B 가 A 타입이 아닐 때
			return debugMode("EU018", parametersCheck[0].type); // 2번째 매개변수가 %1 타입이 아닙니다. 
		}
	} else if (!parametersCheck[0].value) { // null, "", undefined,false,0,NaN 의 경우
		// A 는 없음, B 는 존재 (param이 1개 이고)
		if(DEBUG_MODE && typeof parametersCheck[1].value !== "undefined"){
			return debugMode("EU017"); // 1번째 매개변수가 존재하지 않습니다.
		} else {
			return {a:parametersCheck[1].value, b:parametersCheck[0].value}; // 여기를 살리면 항상 뒤바뀜. a는 undefined 가 된다. (2013.04.15) 
		}
	} else if(DEBUG_MODE){
		// A 타입이 A도 B도 아닐때
		return debugMode("EU016",'1', parametersCheck[1].type); // %1번째 매개변수가 %2 타입이 아닙니다.
	}
	return true;
	
};

/**
 * @description 웹브라우저 윈도우 새창 열기 함수
 * @version 1.0
 * @since 2013.01
 * @author 이병직
 * @class
 * @memberOf COMM
 * @param {String} url 페이지 url
 * @param {Object} [pop] 새창 설정값
 * @param {Number} pop.w 가로 길이
 * @param {Number} pop.h 세로 길이
 * @param {String} [pop.name="win"|""] 창 이름
 * @param {String} [pop.resizable="no"] 리사이즈 여부
 * @example
 * 스크린 사이즈에 맞게
 * COMM.winOprn( "test.jsp" );
 * 사용자 사이즈 지정
 * COMM.winOprn( "test.jsp", {w:100, h:100, name:"창이름", resizable:"yes"} );
 */
COMM.winOpen = function( url, pop ) {
    var spec = "menubar,location,scrollbars,resizable,status,titlebar,toolbar,left=0,top=0,width="+screen.width+",height="+screen.availHeight;
    
    if(pop){
        var resizable = pop.resizable || "no";
        
        if(!COMM._declareCheck(pop, COMM._STANDARD.winOpen)){
            return false;
        };
        
        spec ="width=" + pop.w + ",height=" + pop.h + ",left=" + (screen.availWidth - pop.w)/2 + ",top=" + (screen.availHeight - pop.h)/2
             + ",resizable=" + resizable
             + ",menubar=no,location=no,scrollbars=no,status=no,titlebar=no,toolbar=no";
        window.open( INITIALIZE_BASE + url, pop.name || "", spec );  
    } else {
        window.open( INITIALIZE_BASE + url, "win", spec );
    }
};

/**
 * @description 보고서
 * @version 1.0
 * @since 2013.01
 * @author 이병직
 * @class
 * @memberOf COMM
 * @param {String} reportId : 레포트ID
 * @param {String} paramKey : parameter map에 저장할 Key(delimiter:|)
 * @param {String} paramVal : parameter map에 저장할 Value(delimiter:|)
 */
COMM.report = function( reportId, paramKey, paramVal ) {
	COMM.progressbar("on");
	var param = new Object();
	
	param.paramKey = paramKey;
	param.paramVal = paramVal;
	param.reportId = reportId;
	param.rootPath = INITIALIZE_BASE;
	
	$.ajax({
		   type: "POST"
		,  url:  INITIALIZE_BASE + "/report/jsp/print_jasper.jsp"
		,  data: param
		,  success:function(data) {
			  if( data.indexOf("ERROR") == -1 ) {
				  COMM.winOpen( "/report/files/" + data, {w:800, h:600, name:"menual", resizable:"yes"});
			  } else {
				  COMM.dialog({
					  type: "error",
			          id: "E0001",			
		           	  message: "보고서 생성 중 오류가 발생하였습니다."
		       	  });
			  }
		   }
	    ,  error:function(xhr, status, error) {
			  COMM._ajaxError(xhr, status, error);
		   }
	    ,  complete:function(data) {
			  COMM.progressbar("off");
		   }
	});
};

/**
 * @description 알람 다이얼로그 - 확인 페이지로 이동 여부를 묻는 layer.
 * @version 1.0
 * @since 2013.01
 * @author 이병직
 * @class
 * @requires inc/common_dialog.jsp
 * @memberOf COMM
 * @param {Number} [alarmCount] 알람 개수
 * @example
 * COMM.alarm( );	 //기존 값 그대로
 * COMM.alarm( 10 ); //새로 값 세팅
 */
COMM.alarm = function ( alarmCount ) {
    var init = COMM._INIT.ALARM
    ,   dialogInit = COMM._INIT.DIALOG
    ,   setButton = {};
    
    // 명칭을 위해 외부에서 선언
    setButton[_UCS.COMMON2011] = init.confirmFunc;
    setButton[_UCS.COMMON2012] = function () {$( this ).dialog( "close" );};
    
    $(init.count).html( alarmCount );
    $(init.base).dialog({
  		    width: init.width
		,   title: init.title
		,   dialogClass: init.dialogClass
		,   resizable: false
		,   modal: true
		,   draggable: false
		,   closeOnEscape: false
		,   buttons: setButton
		,   open: function(){
		      $("." + init.dialogClass).find(dialogInit.removeCloseBtn).remove();
		    }
		,   close: function(){
	        }
	});
    
    //알림메세지 새창으로 가운데 띄우기
    var screenW = screen.availWidth;  //화면 넓이
    var screenH = screen.availHeight; //화면 높이

    var popW = 438;                   //띄울 창의 넓이
    var popH = 208;                   //띄울 창의 높이

    var mLeft = (screenW - popW)/2;  //가운데 띄우기위한 창의 x위치
    var mTop  = (screenH - popH)/2;  //가운데 띄우기위한 창의 y위치

    var url = INITIALIZE_BASE + "/page/process/pageAlram.jsp";    
    window.open(url, 'CMS_ALRAM','width='+popW+',height='+popH+',top='+mTop+',left='+mLeft
    		  +', channelmode=no, location=no, resizable=no, scrollbars=no, toolbar=no, menubar=no, status=no ');
};

/**
 * @description 다이얼로그(dialog) - 메세지 처리를 위한 layer.
 * @version 1.1
 * @since 2013.04
 * @author 이병직
 * @class
 * @memberOf COMM
 * @requires inc/common_dialog.jsp
 * @param {Object} content 설정값
 * @param {String} content.type error, message, confirm, warning, debug, session
 * @param {String} content.id 메세지 코드(5자리 기준)
 * @param {String} [content.message] 메세지
 * @param {String} [content.messageConvertParam] 메세지 치환 파라미터 (%를 param으로 치환)
 * @param {String} [content.detail] 상세메세지-error 타입에만 적용.
 * @param {Function} [content.callback] 콜백함수(Yes 및 확인 버튼)
 * @param {Function} [content.callbackNo] 콜백함수(No 버튼)-confirm 타입에만 적용
 * @param {Object|String|Number} [content.data] 콜백함수에서 사용할 데이타(인자)
 * @example
 * COMM.dialog({
 *     id:"W0001"
 * ,   type:"warning"
 * ,   message:"경고 메세지"
 * });
 */
// 마우스 우클릭, 더블클릭, 드래그 방지
$(document).on("contextmenu dragstart selectstart",function(e){
	// 지갑보안키 텍스트 박스는 허용한다.
	if(e.target.id == 'secret_key')
		return true;
	else
		return false;
});

//엔터키방지
$(document).on("keydown",function(e){
    if (event.keyCode === 13) {
        event.preventDefault();
    }
});

COMM.wScrollBar = function () {
	return $('body').prop("scrollWidth") >= $('body').innerHeight() ? true : false;
};

COMM.hScrollBar = function () {
	return $('body').prop("scrollHeight") >= $('body').innerHeight() ? true : false;
};

COMM.dialog = function ( content ) {
    var msgId = content.id
    ,   dialogInit = COMM._INIT.DIALOG
    ,   init = null
    ,   setButtonConfirm = {}
    /**
     * 메세지 변환 ( message(0), A(1), B(2), ... ) , param 배열 가능.
     * @private
     * @deprecated 내부에서만 사용하도록 변경
     * @version 1.1
     * @since 2013.04.05
     * @author 이병직
     * @param {String} message 원 메세지
     * @param {String|String[]} param 인수로 사용할 메세지
     * @returns {String} 변환된 메세지
     */
    ,   getConvertMsg = function( message, param) {
		     var parameter;
		     
		     if (typeof param === "string") {
		         parameter = arguments;
		     } else {
		         parameter = ["0"].concat(param);
		     }
		     
		     for(var x = 1, l = parameter.length; x < l; x +=1) {
		         try {
		             message = message.replace( new RegExp("%" + x, "g"), parameter[x] );
		         } catch(er) {
		             if (window.console) {
		                 console.error("getConvertMsg: ", er);
		             }
		         }
		     }
		     return message;
    	}
    
    /**
     * ID를 통한 메세지 세팅 (_UCS에 정의된 내부속성만 해당)
     * @private
     * @deprecated DEBUG모드는 항상 DEBUG 창을 이용한다는 가정하에 동작
     * @version 1.1
     * @since 2013.04.05
     * @author 이병직
     * @param {Object} init 해당 dialog DOM 정보가 담긴 객체
     */
    ,   dialogMsgID = function (init) {
    	    var debugID = COMM._INIT.DECLARE_CHECK.keyPrefixUCS + msgId;
            if (msgId && $(_UCS).attr(debugID) ) { // _UCS에 속성이 있으면.
                if (content.messageConvertParam) {
                    $(init.message).html( getConvertMsg(_UCS[debugID], content.messageConvertParam) );
                } else {
                    $(init.message).html( _UCS[debugID] );
                }
                
                // 2013-04-05 편의상 추가: detail을 debugMode에서는 addMSG처럼 동작
                // DEBUG모드는 항상 DEBUG 창을 이용한다는 가정이 필요하다. 또한 _UCS속성으로 debug 메세지를 판단한다는 것을 주의.
                if (DEBUG_MODE && content.detail && (init.message === COMM._INIT.DEBUG.message) ){
                	$(init.message).html($(init.message).html() + content.detail);
                }
            } else {
                $(init.message).html( COMM._undefineMessage(content.message) );
            }
        }
    /**
     * dialog 생성 함수
     * @private
     * @version 1.1
     * @since 2013.04
     * @author 이병직
     * @param {Object} initialize 해당 dialog DOM 정보가 담긴 객체
     */
    ,   dialogInitialize = function (initialize) {
            var init = initialize.init
            ,   setButton = {};
            
            // 명칭을 위해 외부에서 선언
            setButton[_UCS.COMMON2011] = function() {
                if ( typeof content.callback === "function" ) {
                    content.callback( content.data );
                }
                $( this ).dialog( "close" );
            };
            
            $(init.msgDetail).html( content.detail || "");
            
            if(COMM.progressbar("isOpen"))
        		COMM.progressbar('off');	

            if(!COMM.hScrollBar())
            	$('body').css({'overflow': 'hidden', 'height': '100%'}); 
            
            $(init.base).dialog({
                    modal: true 
                ,   width: init.width
                ,   title: initialize.title
                ,   dialogClass: init.dialogClass
                ,   buttons: setButton
                ,   resizable:false
                ,   closeOnEscape: false
                ,   draggable: false
                ,   open: function(){
                        $("." + init.dialogClass).find(dialogInit.removeCloseBtn).remove();
                    }
                ,   close: function(){
                		$('body').css({'overflow': 'auto', 'height': '100%'}); //scroll hidden 해제
                    }
            });

        }
    /**
     * dialog 생성 함수 - 상단 닫기 버튼이 없는 dialog
     * @private
     * @version 1.0
     * @since 2013.01
     * @author 이병직
     * @param {Object} initialize 해당 dialog DOM 정보가 담긴 객체
     */
    ,   dialogInitNoneBtn = function (initialize) {
            var init = initialize.init;
            if(COMM.progressbar("isOpen"))
        		COMM.progressbar('off');	

            if(!COMM.hScrollBar())
            	$('body').css({'overflow': 'hidden', 'height': '100%'}); 
            
            $(init.base).dialog({
                    modal: true
                ,   width: init.width
                ,   height: init.height
                ,   dialogClass: init.dialogClass
                ,   resizable:false
                ,   closeOnEscape: false
                ,   draggable: false
                ,   open: function(){
                        $("." + init.dialogClass).find(dialogInit.removeCloseBtn).remove();
                    }
	            ,   close: function(){
	        		$('body').css({'overflow': 'auto', 'height': '100%'}); //scroll hidden 해제
	            }
            });
        };

    if(!COMM._declareCheck(content, COMM._STANDARD.dialog)){
        return false;
    }; 
    
    switch(content.type) {
    case dialogInit.typeError:
        init = COMM._INIT.ERROR;
        dialogMsgID(init);
        dialogInitialize( {init:init, title:'<div class="fwf_dialog_title"> <div class="fwf_dialog_title_err"></div></div>'} );
        break;
    case dialogInit.typeMessage:
        init = COMM._INIT.MESSAGE;
        dialogMsgID(init);
        dialogInitialize( {init:init, title:'<div class="fwf_dialog_title"> <div class="fwf_dialog_title_msg"></div></div>'} );
        break;
    case dialogInit.typeWarning:
        init = COMM._INIT.WARNING;
        dialogMsgID(init);
        dialogInitialize( {init:init, title:'<div class="fwf_dialog_title"> <div class="fwf_dialog_title_war"></div></div>'} );
        break;
    case dialogInit.typeConfirm:
        init = COMM._INIT.CONFIRM;
        dialogMsgID(init);
        setButtonConfirm[_UCS.COMMON2013] =  function() {
            if ( typeof content.callback === "function" ) {
                content.callback( content.data );
            }
            $( this ).dialog( "close" );
        };
        setButtonConfirm[_UCS.COMMON2014] = function() {
            if ( typeof content.callbackNo === "function" ) {
                content.callbackNo( content.data );
            }
            $( this ).dialog( "close" );
        };
        
        if(COMM.progressbar("isOpen"))
    		COMM.progressbar('off');	

        if(!COMM.hScrollBar())
        	$('body').css({'overflow': 'hidden', 'height': '100%'}); 
        
        $(init.base).dialog({
                modal: true
            ,   width: init.width
            ,   title: '<div class="fwf_dialog_title"> <div class="fwf_dialog_title_con"></div></div>'
            ,   dialogClass: init.dialogClass
            ,   buttons: setButtonConfirm
            ,   resizable:false
            ,   closeOnEscape:false
            ,   draggable: false
            ,   open: function(){
                    $("." + init.dialogClass).find(COMM._INIT.DIALOG.removeCloseBtn).remove();
                }
            ,   close: function(){
            		$('body').css({'overflow': 'auto', 'height': '100%'}); //scroll hidden 해제
                }
        });
        break;
    case dialogInit.typeSession:
        init = COMM._INIT.SESSION_EXPIRATION;
        dialogMsgID(init);
        dialogInitNoneBtn({init:init});
        break;
    case dialogInit.typeDebug:
    	// DEBUG_MODE 에 따른 DIALOG 제어 (2013.04.05)
    	if (DEBUG_MODE) {
    		init = COMM._INIT.DEBUG;
    	} else {
    		return false;
    	}
        dialogMsgID(init);
        dialogInitialize( {init:init, title:'<div class="fwf_dialog_title"> <div class="fwf_dialog_title_err"></div></div>'} );
        break;
    default:
        if (window.console) {
            window.console.error(_UCS.MSGCDEU000);
        } else {
            alert(_UCS.MSGCDEU000);
        }
        break;
    }
};

/**
 * @description 프로그레스바 - AP와 통신하는 동안 컨텐츠 사용을 방지하는 layer. 
 * <br/>--------<br/>
 * 수동모드"on"은 수동모드"off"를 만날때까지 프로그레스바를 동작시킨다.
 * <br/> 
 * 공통함수에서는 항상 timeStamp를 사용한다.
 * <br/> 
 * 주의! isOpen은 프로그레스바가 off인 상태에서 호출하면 에러를 유발한다. 
 * @version 1.0
 * @since 2013.01
 * @author 이병직
 * @class
 * @requires inc/common_dialog.jsp
 * @param {String} [flag] off, on, isOpen
 * @param {Nunmber} [ajaxTimeStamp] 프로그레스바 시작한 값(miliSec)
 * @example
 * 프로그레스바 활성화 여부 (return true | false )
 * COMM.progressbar("isOpen");
 * 공통함수 비표준 사용 (권장하지 않음)
 * COMM.progressbar();
 * 공통함수 표준 사용.
 * COMM.progressbar(123456789);
 * COMM.progressbar("off", 123456789);
 * 수동모드
 * COMM.progressbar("on");
 * COMM.progressbar("off");
 */
COMM.progressbar = function ( flag, ajaxTimeStamp ) {
    var init = COMM._INIT.PROGRESSBAR
    ,   timeStamp = COMM.global.progressTimeStamp; // 값 복사(주의).
    if (COMM.progressbar.manualMode === true) {
        if (flag === "off" && typeof ajaxTimeStamp === "undefined") {
            COMM.progressbar.manualMode = false;
            $(init.base).dialog("close");
        }
        return;
    }
    
    if (flag === "isOpen") {
        return $(init.base).dialog("isOpen");
    } else if (flag !== "off") {

        if(!COMM.hScrollBar())
        	$('body').css({'overflow': 'hidden', 'height': '100%'}); 
        
        $(init.base).dialog({
                width: init.width
            ,   height: init.height
            ,   modal: true
            ,   title: '<img src="' + init.image + '" alt="프로그레스바 이미지" />'
            ,   dialogClass: init.dialogClass
            ,   resizable:false
            ,   draggable: false
            ,   closeOnEscape: false
            ,   open: function(){
                    $(".ui-widget-overlay").css("opacity", 0);
                    $("." + init.dialogClass).find(COMM._INIT.DIALOG.removeCloseBtn).remove();
                }
            ,   beforeClose: function() {
                    $(".ui-widget-overlay").css("opacity", 0.2);
            	}
            ,   zIndex:9999
            ,   close: function(){
        		$('body').css({'overflow': 'auto', 'height': '100%'}); //scroll hidden 해제
            }
        });
        if(flag === "on") { // 수동모드
            COMM.progressbar.manualMode = true;
            return;
        };
        
        // 두 개 이상의 ajax 구동시, 종료시점 문제로 timeStamp를 찍음. (12.24)
        if (typeof flag === "number") {
            COMM.global.progressTimeStamp = flag;
        }
    } else if ( typeof ajaxTimeStamp === "number"){
        if ( flag === "off" && timeStamp === ajaxTimeStamp) {
            $(init.base).dialog("close");
        }
    } else  {
        $(init.base).dialog("close");
    }

};
COMM.progressbar.manualMode = false;

/**
 * @description 레이어 형식 팝업 - 팝업을 띄우는 layer.
 * <br/>--------<br/>
 * isSub 를 사용하면 레이어 위에 레이어를 띄울 수 있다.
 * <br/>
 * 그외의 경우 COMM.layer는 항상 한 개만 띄울 수 있다. 
 * <br/>
 * 참고: fwf 의 팝업은 layer로 되어 있다. winOpen과 유사하게 사용.
 * @version 1.0
 * @since 2013.01
 * @author 이병직
 * @class
 * @requires inc/common_dialog.jsp
 * @memberOf COMM
 * @param {String} url 페이지 url
 * @param {Object} constructor 새창 설정값
 * @param {Number} constructor.w 가로 길이
 * @param {Number} constructor.h 세로 길이
 * @param {Boolean} [constructor.m=true] mordal 여부
 * @param {String} [constructor.param] request로 전달할 param
 * @param {Boolean} [constructor.isSub] 서브레이어 여부
 * @param {String} [constructor.callback] 레이어가 열린 후 동작할 함수 (로딩된 페이지 함수가 우선)
 * @param {Object|String|Number} [constructor.data] 레이어가 열릴 후 동작하는 함수에 전달되는 data 
 * @param {String} [constructor.closeAction] 레이어가 닫힐 때 동작할 함수
 * @example
 * COMM.layer( "test.jsp", {w:100, h:100, m:false, param:"a=1&b=2"} );
 * COMM.layer( "test.jsp", {w:100, h:100, m:false, param:"a=1&b=2", callback:function(){} } );
 */
COMM.layer = function ( url, constructor ) {
    var init = COMM._INIT.POPUP
    ,   titleTemplet =  '<div class="popup_title">';
    
    if(!COMM._declareCheck(constructor, COMM._STANDARD.layer)){
        return false;
    };
    
    if(constructor.m !== false) {
        constructor.m = true;
    }
    
    if(constructor.isSub === true) {
        init = COMM._INIT.SUBPOPUP;
    }
    
    $.post( INITIALIZE_BASE + url, constructor.param, function(data){
    	$(init.body).html("");
        $(init.base).dialog({
                width: constructor.w || "100%"
            ,   height: constructor.h || "100%"
            ,   dialogClass: init.dialogClass
            ,   resizable: false
            ,   modal: constructor.m
            ,   draggable: true
            ,   closeOnEscape: false
            ,   close: constructor.closeAction || null
        });
        $(init.body).html(data); // 스크립트 동작 (특히 progressbar)
        titleTemplet = titleTemplet + $(init.body + " .popup_title").html() + "</div>";
        $(init.base).dialog("option", "position", {my: "center", at: "center", of: window});
        $(init.base).dialog("option", "title", titleTemplet);
    }).complete( function() { 
        $(init.body + " .popup_title").remove();
        
        if(typeof constructor.callback === "function") {
            constructor.callback(constructor.data);
        }
    }).error( function(xhr,status,error){
    	xhr.devMethod = "COMM.layer";
    	COMM._ajaxError(xhr,status,error); 
    });
};

/**
 * @description 레이어 형식 팝업을 외부에서 닫는 함수.
 * <br/>--------<br/>
 * 주의! Layer close Method는 닫기 자체에 걸린 event임을 주의해서 사용할 것.
 * @version 1.0
 * @since 2013.01.17
 * @author 이병직
 * @class
 * @requires inc/common_dialog.jsp
 * @memberOf COMM
 * @example
 * COMM.layerClose( );
 */
COMM.layerClose = function (  ) {
    var $sub = $(COMM._INIT.SUBPOPUP.base)
    ,   $pop =  $(COMM._INIT.POPUP.base);
    
    if($sub.is(":visible")){
        $sub.dialog("close");
    } else {
        $pop.dialog("close");
    }
    
};

/**
 * @description _INIT_SESSION._Chk_Pwd_Upd_Perd === "Y" 인 경우 로그아웃 함수
 * <br/>--------<br/>
 * 현재, 로그인 비밀번호 변경 페이지 전용 함수이다.
 * <br/>
 * 로그인 비밀번호 변경시 체크기간이 만료되면 로그아웃
 * @version 1.1
 * @since 2013.05.22
 * @author 이병직
 * @class
 * @memberOf COMM
 * @param {Object} constructor
 * @param {String} constructor.isPwdChange 비밀번호 변경여부(true,false)
 * @param {Function} [constructor.logout] 로그아웃 될 때 동작할 함수. 인수로 로그아웃 함수를 받는다.
 * @param {Function} [constructor.stay] 로그아웃 되지 않을 때 동작할 함수.
 * @example
 * COMM.logoutChkPwdUpdPerd( );
 * COMM.logoutChkPwdUpdPerd({
 *     logout: function(logout){}
 * ,   stay: function(){}
 * ,   pwdChange: isChange
 * });
 */
COMM.logoutChkPwdUpdPerd = function(constructor){
	/**
	 * @description 로그아웃 함수 - logout 인수로 사용된다.
	 * @inner
	 */
	var logout = function(){
		location.href = INITIALIZE_BASE + "/uxLogout";
	};
	
	if(!constructor){
		constructor = {};
	}
	
	if ( !COMM._declareCheck(constructor, COMM._STANDARD.logoutChkPwdUpdPerd) ){
        return false;
	};
	
	if (_INIT_SESSION._Chk_Pwd_Upd_Perd === "Y" && constructor.isPwdChange === true){
		if( $.isFunction(constructor.logout)) {
			constructor.logout(logout);
		} else {
			logout();
		}
	} else {
		if( $.isFunction(constructor.stay)) {
			constructor.stay();
		}
	}
};

/**
 * @description 세션 불일치시 refresh 
 * @version 1.0
 * @since 2013.01
 * @author 이병직
 * @event
 * @memberOf COMM
 * @param {String} targetUrl
 */
COMM._sessionExpirationCheck = function (targetUrl) {
    var init = COMM._INIT.SESSION_EXPIRATION
    ,   checkURL = COMM._INIT.sessionExpirationCheck
    ,   sessionKey = $("#footer_session_id").val()
    ,   url = targetUrl || $("#footer_fwf_page_id").val()
    ,   checkResult = false;
    $.ajax({
        url: checkURL
    ,   async: false
    ,   dataType: "json"
    ,   data: {session_key:sessionKey}
    ,   beforeSend: function() {
        }
    ,   success: function (data, textStatus, jqXHR) {
            if (data.sessionExpirationCheck === "on") {
            	// 정상
            	checkResult = true;
            } else if (data.sessionExpirationCheck === "off"){
            	
                COMM.dialog({
                    id: "session",
                    type: COMM._INIT.DIALOG.typeSession,
                    message:init.sessionExpiration
                });
                setTimeout( function(){location.href = init.loginPage;}, 7000);
            }
        }
    ,   error: function (e, s, m){
            COMM.dialog({
                id: "session",
                type:COMM._INIT.DIALOG.typeSession,
                message:init.serverError
            });
            setTimeout( function(){location.href = init.loginPage;}, 7000);
        }
    }); // ajax

    return checkResult;
};

/**
 * @description datepicker 처리 함수
 * @version 1.0
 * @since 2013.01
 * @author 이병직
 * @event
 * @memberOf COMM
 * @param {String} sPageID
 */
COMM._setDatepicker = function (sPageID) {
    var INIT = COMM._INIT.DATEPICKER;
    // 달력 (버튼취급 11.28 / backspace 차단 )
    if ( $(INIT.datepicker_reference, $(sPageID)).length > 0) {
        COMM._datepicker_reference( INIT.datepicker_reference, sPageID );
        $(INIT.datepicker_reference, $(sPageID)).keydown(COMM.keyEventPrevent.keydownBackspace);
    }
    if ( $(INIT.datepicker), $(sPageID).length > 0 ) {
        COMM._datepicker(INIT.datepicker, sPageID);
        $(INIT.datepicker, $(sPageID)).keydown(COMM.keyEventPrevent.keydownBackspace);
    }
    
    // 월 달력 (2013.01.17)
    if ( $(INIT.monthpicker, $(sPageID)).length > 0 ) {
        COMM._setMonthpicker(sPageID);
    }
};

/**
 * @description 버튼 생성 함수
 * @version 1.0
 * @since 2013.01
 * @author 이병직
 * @event
 * @memberOf COMM
 * @param {String} sPageID
 * @param {String} pageID
 */
/*
COMM._createButton = function (sPageID ,pageID) {
    // 버튼 클릭시 이벤트는 프레임웍화 하지 않고 개발자가 바인딩. --- 다음 버전 지원 검토 ---
    $(document).ready( function(){
    // 서브 페이지 버튼
    $(".button_title01", $(sPageID)).button({ icons: {primary: "fwf-icon-page-w"} }).children(".ui-button-text").text(_UCS.COMMON2101);
    $(".button_title02", $(sPageID)).button({ icons: {primary: "fwf-icon-search-w"} }).children(".ui-button-text").text(_UCS.COMMON2102);
    $(".button_title03", $(sPageID)).button({ icons: {primary: "fwf-icon-disk-w"} }).children(".ui-button-text").text(_UCS.COMMON2103);
    $(".button_title04", $(sPageID)).button({ icons: {primary: "fwf-icon-insert-w"} }).children(".ui-button-text").text(_UCS.COMMON2104);
    $(".button_title05", $(sPageID)).button({ icons: {primary: "fwf-icon-arrow-w"} }).children(".ui-button-text").text(_UCS.COMMON2105);
    $(".button_title06", $(sPageID)).button({ icons: {primary: "fwf-icon-upload-w"} }).children(".ui-button-text").text(_UCS.COMMON2106);
    $(".button_title07", $(sPageID)).button({ icons: {primary: "fwf-icon-return-w"} }).children(".ui-button-text").text(_UCS.COMMON2107);
    $(".button_title08", $(sPageID)).button({ icons: {primary: "fwf-icon-check-w" } }).children(".ui-button-text").text(_UCS.COMMON2108);
    $(".button_title09", $(sPageID)).button({ icons: {primary: "fwf-icon-return-w"} }).children(".ui-button-text").text(_UCS.COMMON2109);
    $(".button_title10", $(sPageID)).button({ icons: {primary: "fwf-icon-folder-w"} }).children(".ui-button-text").text(_UCS.COMMON2110);
    $(".button_title11", $(sPageID)).button({ icons: {primary: "fwf-icon-pen-w"} }).children(".ui-button-text").text(_UCS.COMMON2111);
    $(".button_title12", $(sPageID)).button({ icons: {primary: "fwf-icon-disk2-w"} }).children(".ui-button-text").text(_UCS.COMMON2112);
    $(".button_title13", $(sPageID)).button({ icons: {primary: "fwf-icon-close-w"} }).children(".ui-button-text").text(_UCS.COMMON2113);
    $(".button_title14", $(sPageID)).button({ icons: {primary: "fwf-icon-page-w"} }).children(".ui-button-text").text(_UCS.COMMON2114);
    $(".button_title15", $(sPageID)).button({ icons: {primary: "fwf-icon-plus-w"} }).children(".ui-button-text").text(_UCS.COMMON2115);
    $(".button_title16", $(sPageID)).button({ icons: {primary: "fwf-icon-return-w"} }).children(".ui-button-text").text(_UCS.COMMON2116);
    $(".button_title17", $(sPageID)).button({ icons: {primary: "fwf-icon-arrow-w"} }).children(".ui-button-text").text(_UCS.COMMON2117);
    $(".button_title18", $(sPageID)).button({ icons: {primary: "fwf-icon-arrow-w"} }).children(".ui-button-text").text(_UCS.COMMON2118); 
    $(".button_title19", $(sPageID)).button({ icons: {primary: "fwf-icon-upload-w"} }).children(".ui-button-text").text(_UCS.COMMON2119);
    $(".button_title20", $(sPageID)).button({ icons: {primary: "fwf-icon-disk-w"} }).children(".ui-button-text").text(_UCS.COMMON2120);
    $(".button_title21", $(sPageID)).button({ icons: {primary: "fwf-icon-disk-w"} }).children(".ui-button-text").text(_UCS.COMMON2121);
    $(".button_title22", $(sPageID)).button({ icons: {primary: "fwf-icon-disk-w"} }).children(".ui-button-text").text(_UCS.COMMON2122);
    $(".button_title23", $(sPageID)).button({ icons: {primary: "fwf-icon-disk-w"} }).children(".ui-button-text").text(_UCS.COMMON2123);
    $(".button_title24", $(sPageID)).button({ icons: {primary: "fwf-icon-disk-w"} }).children(".ui-button-text").text(_UCS.COMMON2124);
    $(".button_title25", $(sPageID)).button({ icons: {primary: "fwf-icon-return-w"} }).children(".ui-button-text").text(_UCS.COMMON2125);
    $(".button_title26", $(sPageID)).button({ icons: {primary: "fwf-icon-search-w"} }).children(".ui-button-text").text(_UCS.COMMON2169);	//	등록대상조회
    $(".button_title27", $(sPageID)).button({ icons: {primary: "fwf-icon-disk-w"} }).children(".ui-button-text").text(_UCS.COMMON2170);
    $(".button_title28", $(sPageID)).button({ icons: {primary: "fwf-icon-return-w"} }).children(".ui-button-text").text(_UCS.COMMON2176);   //승인비밀번호초기화 20160909
    $(".button_title29", $(sPageID)).button({ icons: {primary: "fwf-icon-download-w"} }).children(".ui-button-text").text(_UCS.COMMON2177);   //결재상신취소 20160921
    // 검색 버튼
    $(".button_search01", $(sPageID)).button({ icons: {primary: "fwf-icon-search-b"} }).children(".ui-button-text").text(_UCS.COMMON2126);	//	조회
    $(".button_search02", $(sPageID)).button({ icons: {primary: "fwf-icon-plus-b"} }).children(".ui-button-text").text(_UCS.COMMON2127);
    $(".button_search03", $(sPageID)).button({ icons: {primary: "fwf-icon-search-b"} }).children(".ui-button-text").text(_UCS.COMMON2168);	//	실시간조회
    $(".button_search03", $(sPageID)).button({ icons: {primary: "fwf-icon-search-b"} }).children(".ui-button-text").text(_UCS.COMMON2172);	//	폼뱡킹정보요약
    // 검색창 안쪽 버튼
    $(" .button_search_in01" ,$(sPageID)).button().children(".ui-button-text").text(_UCS.COMMON2128);
    $(" .button_search_in02" ,$(sPageID)).button().children(".ui-button-text").text(_UCS.COMMON2129);
    $(" .button_search_in03" ,$(sPageID)).button().children(".ui-button-text").text(_UCS.COMMON2130);
    $(" .button_search_in04" ,$(sPageID)).button().children(".ui-button-text").text(_UCS.COMMON2131);
    $(" .button_search_in05" ,$(sPageID)).button().children(".ui-button-text").text(_UCS.COMMON2132);
    $(" .button_search_in06" ,$(sPageID)).button().children(".ui-button-text").text(_UCS.COMMON2133);
    $(" .button_search_in07" ,$(sPageID)).button().children(".ui-button-text").text(_UCS.COMMON2134);
    $(" .button_search_in08" ,$(sPageID)).button().children(".ui-button-text").text(_UCS.COMMON2135);
    $(" .button_search_in09" ,$(sPageID)).button().children(".ui-button-text").text(_UCS.COMMON2136);
    $(" .button_search_in10" ,$(sPageID)).button().children(".ui-button-text").text(_UCS.COMMON2137);
    $(" .button_search_in11" ,$(sPageID)).button().children(".ui-button-text").text(_UCS.COMMON2138);
    $(" .button_search_in12" ,$(sPageID)).button().children(".ui-button-text").text(_UCS.COMMON2139);
    // 그리드 바깥 버튼
//  $(".button_grid01", $(sPageID)).addClass("fwf-icon-excel").addClass("ui-icon").click(function(event){
    $(".button_grid01", $(sPageID)).button({ icons: {primary: "fwf-icon-excel"} }).children(".ui-button-text").text(_UCS.COMMON2140).end().click(function(event){
    
        //// 주의사항!! - {pageID} + grid 인 경우만 동작한다. 그렇지 않으면 init에서 id를 받도록 수정해야 한다. //////
        ///           * rownumbers 사용 여부와는 무관하게 동작한다.  있으나 없으나 잘 동작함(03/18) ///// 
        // DOM copy를 이용한 방법 (2.4)
        var excelFormID = pageID + "excelForm"
        ,   sGridID = "#" + pageID + "grid"
        ,   gviewID = "#gview_" + pageID + "grid" //예 gview_UP1020010grid
        ,   gviewData = $(gviewID).html() // excel 로 보낼 data
        ,   colModel = $(sGridID).jqGrid("getGridParam", "colModel")
        ,   colModelLength = colModel.length // rownumbers 있으면 모델이 하나더 추가됨 (따라서 따로 작업이 필요없다.) - check박스도 자동으로 추가됨. 즉, 둘 다 사용시 모델 길이가 2 증가
        ,   isHeaderGroup = false
        ,   colspanHeaderGroup = []
        ,   realHeaderGroup = []
        ,   gviewFilter = []        // 필터
        ,   excelTitle = ""         // 엑셀 타이틀
        ,   excelAnswerTime = ""   	// 엑셀 조회시간
        ,   excelGridHeadNum = ""	// 엑셀 컬럼수\
        ,   that = this 			// excelProcessor 함수안에서 사용하기 위해.
        ,   gridPage = $(sGridID).jqGrid("getGridParam", "page")
        ,   isPaging = $(sGridID).jqGrid("getGridParam", "isPaging");
        
        // progressbar를 띄우기 위한 지연함수화 (04.03) 
        // 참고 - 다른 방안은 excelProcessor를 async로 구현하고 progressbar를 띄우는 것. (다음 버전에서 고려할 것.)
        // 		- 마찬가지로 현재 sync방식으로 tasking을 하면 함수 호출은 되지만 progressbar가 화면에 올라오지 않는다.
        var excelProcessor = function() {
        	// 엑셀 타이틀
            // popupLayer가 떠 있으면, popup 에서만 처리 된다고 가정한 경우에만 올바르다.
            if ($(COMM._INIT.POPUP.base).is(":visible")) {
                excelTitle = $("div." + COMM._INIT.POPUP.dialogClass + " span.title_pad").text();
                excelAnswerTime = $("div." + COMM._INIT.POPUP.dialogClass + " div.grid > .grid_title" ).text();
            } else {
                excelTitle = $(sPageID + " > .sub_title span.title_pad" ).text();
                excelAnswerTime = $(sPageID + " div.sub_grid > .grid_title" ).text();
            }
            document.getElementById('excel_box').innerHTML = gviewData;
            
            // 필터 세팅
            for (var x = 0; x < colModelLength; x += 1 ) {
                if ( colModel[x].hidden === true ) {
                    gviewFilter.push(x);
                } else if ( colModel[x].excel === false ) {
                    gviewFilter.push(x);
                }
            }
            
            // 헤드 제거
            $("div.ui-jqgrid-titlebar", $("#excel_box")).remove();
            
            // table 전 빈div 제거
            if ($(sGridID, $("#excel_box")).prev().text() === "") {
                $(sGridID, $("#excel_box")).prev().remove();
            }
            
            // table 의 "jqgfirstrow" 제거 (col-width 정의된 부분이나, 엑셀에서 한 행을 차지)
            $("tr.jqgfirstrow", $("#excel_box")).remove();
            
            // frozen 제거 (jqGrid는 헤더 부분을 frozen 만큼 한번 더 생성한다.)
            $("div.frozen-div",$("#excel_box")).remove();
            $("div.frozen-bdiv",$("#excel_box")).remove();
            
            // header 그룹 있을 때
            if ($(sGridID).jqGrid("getGridParam", "headerGroup")){
                isHeaderGroup = true;
                
                $("#excel_box").find("thead > tr.ui-jqgrid-labels").each(function(index){
                    var colspanIndex = 0; // colspan이 시작되는 index
                    $(this).children("th").each(function(index2){
                        if(index === 0) { // colSpan이 있는 부분
                            var colspan = $(this).attr("colSpan");
                            if (colspan > 1){ // IE8은 속성이 없어도 1로 찍힘(2.6).
                                colspan = parseInt(colspan, 10);
                                colspanHeaderGroup.push(index2);
                                for(var x = index2; x < index2 + colspan; x += 1){
                                    realHeaderGroup.push("colspan");
                                };
                            } else {
                                realHeaderGroup.push($(this).attr("id"));
                            }
                        } else {
                            colspanIndex = colspanHeaderGroup[index-1];
                            while ( realHeaderGroup[colspanIndex] === "colspan" ){
                                realHeaderGroup[colspanIndex] = $(this).attr("id");
                            }
                            colspanHeaderGroup[index-1] = colspanIndex + 1;
                        }
                        // 헤더그룹이고 display:none 일 때 (dynamicCol 사용시 발생. html 에선 이상 없으나 엑셀변환시 표시됨)
                        // 헤더그룹이 아닐 때에는 이상이 없다.
                        if ($(this).css("display") === "none" ){
                        	$(this).remove();
                        }
                    });
                });
   
            }
            
            // 필터 적용
            for (var x = gviewFilter.length - 1; x >= 0; x -= 1 ) {
                if (isHeaderGroup) {
                    $("thead > tr:first > th", $("#excel_box")).eq(gviewFilter[x]).remove();
                    // 당연하게도 동일 id를 두 번이상 사용하면 안됨! jqGrid 에서 name을 중복 사용하지 말 것.(03.27)
                    $("#" + realHeaderGroup[gviewFilter[x]], $("#excel_box") ).remove();
                } else {
                    $("thead > tr > th" ,$("#excel_box")).eq(gviewFilter[x]).remove();
                }
                $("tbody", $("#excel_box")).each(function(){
                   $(this).children("tr").each(function(){
                     $(this).children("td").eq(gviewFilter[x]).remove();
                   });
                });
            }

            // 필터 적용 후, 만약 multiselect 사용시 check 제거.
            if ( $(sGridID).jqGrid("getGridParam", "multiselect") ){
            	// multiselect 일 때, excelGridHeadNum 설정.
                excelGridHeadNum = $("thead > tr:first > th", $("#excel_box")).length -1;
                
                $("thead > tr.jqg-first-row-header", $("#excel_box")).remove(); // 설정
                $("thead > tr th" + sGridID +"_cb", $("#excel_box")).remove();  // 헤드 체크박스
                if($(sGridID).jqGrid("getGridParam", "footerrow")){
                	$(".ui-jqgrid-ftable > tbody > tr > td", $("#excel_box")).eq(1).remove();  // 푸터
                }

                if ( $(sGridID).jqGrid("getGridParam", "rownumbers") ){
                    $(sGridID + " tbody > tr ", $("#excel_box")).each(function(){
                        $(this).children("td:eq(1)").remove();
                    });
                } else {
                    $(sGridID + " tbody > tr ", $("#excel_box")).each(function(){
                        $(this).children("td:eq(0)").remove();
                    });
                }
                //.attr("aria-describedby", sGridID.replace("#","") UP1020050grid_cb")         // rows 체크박스 직접접근
            } else {
                // multiselect 아닐 때, excelGridHeadNum 설정.
                excelGridHeadNum = $("thead > tr:first > th", $("#excel_box")).length;
            }
            
            // colName 에 존재하는 span 제거
            $("thead > tr" ,$("#excel_box")).each(function(){
               $(this).find("span").remove();
            });
            
            // cellattr.mergeRow, acntType 사용시 2013.05.31
            $(".display_none",$("#excel_box")).remove();
            
            // colData 에 존재하는 img 제거 (혹은 이미지 클래스에 따라 특정 문자로 치환도 가능함.
            $("#excel_box img").each(function(){
                if ($(this).attr("alt")){
                    $(this).after($(this).attr("alt"));
                }
                $(this).remove();
             });
            
            // format을 currency로 준 숫자 항목을 읽어 소수점이 포함되었는지 확인한다
            // 소수점이 포함되면 inc_dot 클래스를 추가하고 없으면 non_dot 클래스를 추가한다
            // dot 클래스는 excel.jsp 파일에서 mso-number-format을 주기위한 구분자로 사용한다
            // (inc_dot이면 소수점 이하 2자리 표시, non_dot이면 소수점이하 표시X)
            $("#excel_box .number_st, #excel_box tr.footrow td").each(function(){
            	var num_org = parseFloat($(this).text().replace(/\,/g, "").replace(/^\s*|\s*$/g, ''));    // ex) 123.45
            	var num_psi = parseInt(num_org); // ex) 123
            	
            	// value가 숫자일 때만...
            	if(!(num_org == '' || isNaN(num_org)) || num_org == '0') {
	            	// 원래 숫자에서 정수화한 숫자를 뺐을 때 0이 되지 않으면
	            	// 해당 숫자는 소수점을 포함한다
	            	if ( (num_org - num_psi) != 0 ) {
	            		$(this).attr("class", "inc_dot");
	            	} else {
	            		$(this).attr("class", "non_dot");
	            	}
	
	            	$(this).removeClass("number_st");
            	}
            });
            
            gviewData = $("#excel_box").html();
            gviewData = encodeURIComponent(gviewData);
            $(that).append('<form id="'+ excelFormID + '" action="page/process/excel.jsp" method="POST">'
                    + '<input name="excel" type="hidden"/><input name="pageid" type="hidden"/>'
                    + '<input name="title" type="hidden"/><input name="date" type="hidden"/>'
                    + '<input name="gridheadnum" type="hidden"/><input name="date" type="hidden"/>'
                    + '</form>');
            
            $("input[name=excel]", $(sPageID)).val(gviewData);
            $("input[name=pageid]", $(sPageID)).val(pageID + (isPaging?"_" + COMM. formatNumber(gridPage,3): ""));
            $("input[name=title]", $(sPageID)).val( encodeURIComponent(excelTitle)); // 엑셀 타이틀
            $("input[name=date]", $(sPageID)).val( encodeURIComponent(excelAnswerTime));
            $("input[name=gridheadnum]", $(sPageID)).val( excelGridHeadNum );
            $("#" + excelFormID).submit();
            
            $(that).children("form:first").remove();
            document.getElementById('excel_box').innerHTML = "";
            // 성공!. 
        };
        
        
    	if($(sGridID).jqGrid("getGridParam", "records") === 0){
    		alert(_UCS.COMMON2009);
    	} else {
    		// excel 작업중 progressbar (04.03)
    		COMM.progressbar("on");
    		setTimeout( function(){excelProcessor(); COMM.progressbar("off");}, 100);
    	}; 

    });
    $(".button_grid02", $(sPageID)).button({ icons: {primary: "fwf-icon-arrow-w"} }).children(".ui-button-text").text(_UCS.COMMON2141);
    $(".button_grid03", $(sPageID)).button({ icons: {primary: "fwf-icon-return-w"} }).children(".ui-button-text").text(_UCS.COMMON2142);
    $(".button_grid04", $(sPageID)).button({ icons: {primary: "fwf-icon-close-w"} }).children(".ui-button-text").text(_UCS.COMMON2143);
    $(".button_grid05", $(sPageID)).button({ icons: {primary: "fwf-icon-check-w"} }).children(".ui-button-text").text(_UCS.COMMON2144);
    $(".button_grid06", $(sPageID)).button({ icons: {primary: "fwf-icon-insert-w"} }).children(".ui-button-text").text(_UCS.COMMON2145);
    $(".button_grid07", $(sPageID)).button({ icons: {primary: "fwf-icon-print-w"} }).children(".ui-button-text").text(_UCS.COMMON2146);
    $(".button_grid08", $(sPageID)).button({ icons: {primary: "fwf-icon-addrow-w"} }).children(".ui-button-text").text(_UCS.COMMON2147);
    $(".button_grid09", $(sPageID)).button({ icons: {primary: "fwf-icon-return-w"} }).children(".ui-button-text").text(_UCS.COMMON2148);
    $(".button_grid10", $(sPageID)).button({ icons: {primary: "fwf-icon-window-w"} }).children(".ui-button-text").text(_UCS.COMMON2149);
    $(".button_grid11", $(sPageID)).button({ icons: {primary: "fwf-icon-check-w"} }).children(".ui-button-text").text(_UCS.COMMON2150);
    $(".button_grid12", $(sPageID)).button({ icons: {primary: "fwf-icon-excel"} }).children(".ui-button-text").text(_UCS.COMMON2151);
    $(".button_grid13", $(sPageID)).button({ icons: {primary: "fwf-icon-excel"} }).children(".ui-button-text").text(_UCS.COMMON2152).click(function(event){
        var excelSampleFormID = pageID + "excelSampleForm";
        $(this).append('<form id="'+ excelSampleFormID + '" action="page/work/sample/'+ pageID + '.xls" method="POST"></form>');
        $("#" + excelSampleFormID).submit();
        $(this).children("form:first").remove();
    });
    $(".button_grid14", $(sPageID)).button({ icons: {primary: "fwf-icon-disk-w"} }).children(".ui-button-text").text(_UCS.COMMON2153);
    $(".button_grid15", $(sPageID)).button({ icons: {primary: "fwf-icon-search-b"} }).children(".ui-button-text").text(_UCS.COMMON2154);
    $(".button_grid16", $(sPageID)).button({ icons: {primary: "fwf-icon-check-w"} }).children(".ui-button-text").text(_UCS.COMMON2174);
    $(".button_grid17", $(sPageID)).button({ icons: {primary: "fwf-icon-page-b"} }).children(".ui-button-text").text(_UCS.COMMON2175);
    $(".button_grid18", $(sPageID)).button({ icons: {primary: "fwf-icon-print-w"} }).children(".ui-button-text").text(_UCS.COMMON3151);
    $(".button_grid19", $(sPageID)).button({ icons: {primary: "fwf-icon-print-w"} }).children(".ui-button-text").text(_UCS.COMMON3152);
    $(".button_grid20", $(sPageID)).button({ icons: {primary: "fwf-icon-check-w"} }).children(".ui-button-text").text("출금계좌잔액확인"); //다국어화필요
    $(".button_grid21", $(sPageID)).button({ icons: {primary: "fwf-icon-check-w"} }).children(".ui-button-text").text("실시간잔액조회"); //다국어화필요
    
    // form 영역 버튼
    $(".button_form01", $(sPageID)).button({ icons: {primary: "fwf-icon-search-b"} }).children(".ui-button-text").text(_UCS.COMMON2154);
    $(".button_form02", $(sPageID)).button({ icons: {primary: "fwf-icon-window-b"} }).children(".ui-button-text").text(_UCS.COMMON2155);
    $(".button_form03", $(sPageID)).button({ icons: {primary: "fwf-icon-account-b"} }).children(".ui-button-text").text(_UCS.COMMON2156);
    $(".button_form04", $(sPageID)).button({ icons: {primary: "fwf-icon-check-b"} }).children(".ui-button-text").text(_UCS.COMMON2157);
    $(".button_form05", $(sPageID)).button({ icons: {primary: "fwf-icon-check-b"} }).children(".ui-button-text").text(_UCS.COMMON2158);
    $(".button_form06", $(sPageID)).button({ icons: {primary: "fwf-icon-search-b"} }).children(".ui-button-text").text(_UCS.COMMON2159);
    $(".button_form07", $(sPageID)).button({ icons: {primary: "fwf-icon-account-b"} }).children(".ui-button-text").text(_UCS.COMMON2171);
    // 팝업 버튼
    $(".button_popup01", $(sPageID)).button({ icons: {primary: "fwf-icon-close-b"} }).children(".ui-button-text").text(_UCS.COMMON2160);
    $(".button_popup02", $(sPageID)).button({ icons: {primary: "fwf-icon-check-b"} }).children(".ui-button-text").text(_UCS.COMMON2161);
    $(".button_popup03", $(sPageID)).button({ icons: {primary: "fwf-icon-page-b"} }).children(".ui-button-text").text(_UCS.COMMON2162);
    $(".button_popup04", $(sPageID)).button({ icons: {primary: "fwf-icon-close-b"} }).children(".ui-button-text").text(_UCS.COMMON2163);
    $(".button_popup05", $(sPageID)).button({ icons: {primary: "fwf-icon-disk-b"} }).children(".ui-button-text").text(_UCS.COMMON2164);
    $(".button_popup06", $(sPageID)).button({ icons: {primary: "fwf-icon-print-b"} }).children(".ui-button-text").text(_UCS.COMMON2165);
    $(".button_popup07", $(sPageID)).button({ icons: {primary: "fwf-icon-check-b"} }).children(".ui-button-text").text(_UCS.COMMON2173);
    // 기타 버튼
    $(".button_etc01", $(sPageID)).button({ icons: {primary: "fwf-icon-arrow-w"} }).children(".ui-button-text").text(_UCS.COMMON2166);    
    $(".button_etc02", $(sPageID)).button({ icons: {primary: "fwf-icon-search-b"} }).children(".ui-button-text").text(_UCS.COMMON2167);
    
    $("input", $(sPageID)).each(function(){
        // document 에 bind 했기 때문에, 하위요소에도 모두 적용되고 있다. 사용시 두 번 중복됨 (2.26)
        //$(this).keydown(COMM.keyEventPrevent.keydownBackspace); // 동적으로 readOnly가 바뀌는 페이지가 있기에 이벤트를 걸어 놓음.
    });
    
    });  
};
*/
/**
 * @description 키 이벤트 바인드 함수모음
 * @version 1.0
 * @since 2013.01
 * @author 이병직
 * @class
 * @memberOf COMM
 */
COMM.keyEventPrevent ={
	/**
	 * @function
	 * @version 1.1
	 * @since 2013.03
	 * @author 이병직
	 * @param {Object} event key이벤트 객체
	 */
   keypress: function (event) {
       var keyCode = event.which // mozilla 는 keypress에서 방향키는 which가 0. 엔터 백스페이스 등은 keycode와 which 둘 다 동일 
       ,   character = String.fromCharCode(keyCode)
       ,   INIT = COMM._INIT.FORMAT
       ,   temp = ""
       ,   regx = "";
       
       // type 보다 regx 우선 - 개발자가 regx를 직접 사용할 수 있음.
       if (typeof event.data.regx === "object") {
           regx = event.data.regx;
       } else if (typeof event.data.type === "string"){
           switch(event.data.type){
           case INIT.typeGridNumber:	// 현재(4.24) number와 동일
           case INIT.typeCurrency0:		// 현재 (5.8) number와 동일
           case INIT.typeNumber:
               regx = /\D/;
               break;
           case INIT.typeAlphabet:
        	   regx = /[^a-zA-Z]/;
        	   break;
           case INIT.typeEnglish: // 영어,숫자,공백, 마침표,연결자, 슬래시 허용
        	   regx = /[^\w\s.,\-_\/]/;
        	   break;
           case INIT.typePhone:
               regx = /[^0-9\-]/;
               break;
           case INIT.typeAccount:
               regx = /[^0-9\-]/;
               break;
           case INIT.typeDate:
               regx =  /\D/;
               break;
           case INIT.typeCardno:
               regx = /[^0-9\-]/;
               break;
           case INIT.typeBizno:
               regx = /[^0-9\-]/;
               break;
           case INIT.typeFloat:
               regx = /[^0-9\.]/;
               if (/\./.test($(this).val()) && character === "."){ // .가 두 번 이상
                   event.preventDefault();
               }
               break;
           case INIT.typeCurrency:
        	   regx = COMM._INIT.REGEX.regexInputPointCheck;
        	   temp = $(event.target).val();
        	   if ( COMM._formatter_module.decimalPointNumber(temp) > 0 && String.fromCharCode(keyCode) === _INIT_SESSION._Point){
        		   event.preventDefault();
        		   return;
        	   }
               break;
           case INIT.typeGridNumber2: // currency + 소수2자리까지
        	   regx = COMM._INIT.REGEX.regexInputPointCheck;
        	   temp = $(event.target).val();
        	   if (temp.indexOf(_INIT_SESSION._Point) !== -1 && character === _INIT_SESSION._Point){
        	           event.preventDefault();
        	           return;
        	   }
        	   // 소수자리 3개면 입력 막기 - keypu과 함께 사용 (3자리 이상시 자름. keyup되기 전 최대3자리만 입력되도록 처리)
        	   if (temp.indexOf(_INIT_SESSION._Point) !== -1 ){
        	       if (temp.length - temp.indexOf(_INIT_SESSION._Point) > 3){
        	    	   //$(event.target).val("");	// 만약 3자리 이상이면 오류이므로 값 전체를 삭제. (keyup을 안쓸때 대안)
        	           event.preventDefault();
        	           return;
        	       }
        	   }
        	   break;
           default:
               regx = /[^w]/;
               break;
           }
       }
       
       // 한글은 style에서 ime-mode=disabled 적용
       // 37,39로 처리하면 안됨. 37은 <- 의 keyCode %의 which
       // 파이어폭스 적용 (key 0, 8)
       if (regx.test(character) && keyCode !== 0 && keyCode !==8){
           event.preventDefault();
       }
   },
   
	/**
	 * input 및 모든 페이지에서 backspace 처리 목적
	 * @function
	 * @version 1.0
	 * @since 2013.01
	 * @author 이병직
	 * @param {Object} event key이벤트 객체
	 */
   keydownBackspace: function (event){
       var tagName = event.srcElement ? event.srcElement.tagName : event.target.tagName // mozilla
       ,   keyCode = event.keyCode ? event.keyCode : event.which;
       if(keyCode === 8 && $(this).prop("disabled") !== true){
           switch(tagName){
           case "INPUT": // readOnly 이면 prevent
               if($(event.target).prop("readOnly") === true) {
                   event.preventDefault();
               }
               break;
           case "TEXTAREA":
               if($(event.target).prop("readOnly") === true) {
                   event.preventDefault();
               }
               break;
           default:
               event.preventDefault();
           break;
           }
       } else {
           event.returnValue = false;
       }
   }
}; 

/**
 * @description 클릭 이벤트 핸들러 예비... (선언만 됨)
 * @version 1.1
 * @since 2013.03
 * @author 이병직
 * @class
 * @memberOf COMM
 */
COMM.click = function(event) {
	
};

/**
 * @description keydown 이벤트 핸들러 함수모음
 * @version 1.1
 * @since 2013.03
 * @author 이병직
 * @class
 * @memberOf COMM
 */
COMM.keydown = {
	/**
	 * 달력 인풋창(date형식) key 이벤트
	 * @function
	 * @version 1.1
	 * @since 2013.03
	 * @author 이병직
	 * @param {Object} event key이벤트 객체
	 */
	dateLengthCheck: function(event) {
        if(!$(this).attr("readonly") && $(event.target).val().length > 8){
     	   $(event.target).val($(event.target).val().replace(COMM._INIT.REGEX.regexDate,""));
        };	
	}
};

/**
 * @description blur 이벤트 핸들러. input val replace
 * @version 1.0
 * @since 2013.01
 * @author 이병직
 * @class
 * @memberOf COMM
 * @param {Object} event blur 이벤트 객체
 * @param {Object} [event.data.regx] 문자열에서 제거할 패턴 
 * @param {String} [event.data.type] "kor", "number", "date", "float" (사전정의된 패턴)
 */
COMM.inputBlurReplace = function(event) {
    var returnValue = ""
    ,   INIT = COMM._INIT.FORMAT;
    
    if($(this).prop("readonly") !== true) { // IE 버그 대응
        
        // type 보다 regx 우선
        if (typeof event.data.regx === "object") {
            returnValue = $(this).val().replace(event.data.regx,"");
            
        } else if (typeof event.data.type === "string"){
            
            switch(event.data.type){
            case INIT.typeKorea:
                returnValue = $(this).val().replace(/[ㄱ-ㅎㅏ-ㅣ가-힝]/g,"");
                break;
            case INIT.typeNumber:
                returnValue = $(this).val().replace(/\D/g,"");
                break;
            case INIT.typeDate:
            	returnValue = $(this).val().replace(/\D/g,"");
            	returnValue =  COMM._formatter_module.dateCheck(returnValue);
            	break;
            case INIT.typeFloat:
                returnValue = $(this).val().replace(/[ㄱ-ㅎㅏ-ㅣ가-힝]/g,"");
                returnValue = returnValue === "" ? 0 : returnValue;
                if ($(this).hasClass("format_float_fixed1")) {
                    returnValue = parseFloat(returnValue).toFixed(1);
                } else if ($(this).hasClass("format_float_fixed2")) {
                    returnValue = parseFloat(returnValue).toFixed(2);
                } else if ($(this).hasClass("format_float_fixed3")) {
                    returnValue = parseFloat(returnValue).toFixed(3);
                } else {
                    returnValue = parseFloat(returnValue).toFixed(0);
                }
                break;
            default:
                returnValue = $(this).val();
                break;
            }
        }

        $(this).val( returnValue );
    } // IE 버그 대응
};

/**
 * @description 버튼 생성- html class만 선언하면 자동 동작.
 * @version 1.0
 * @since 2012.11.25
 * @author 이병직
 * @class
 * @memberOf COMM
 * @param {String} pageID
 * @param {Object} [initialize]
 * @param {String} [initialize.datepickerPervInit] 예시) d01, m01 (오늘날짜기준 -00일, -00월)
 * @param {String} [initialize.helpUri] 도움말 파일(pdf)
 * @param {String} [initialize.notice] 주의사항 내용 (ol 태그 안에 작성)
 * @param {Boolean} [initialize.noneDatepicker] 달력화 비활성
 * @param {Boolean} [initialize.noneButton] 버튼화 비활성
 */

/*
COMM.button = function ( pageID, initialize ) {
    var INIT = COMM._INIT
    //  아래 DOM 재정의에서 사용되는 변수이다.
    //,   gridTitleRadioName = pageID + "_grid_view_style"
    ,   sPageID = "";
    
    if ( typeof pageID !== "string" ) {
        if (window.console) {
            window.console.error(_UCS.MSGCDEU005);
        }
        COMM.dialog({
        	type: COMM._INIT.DIALOG.typeDebug,  // debug mode 는 comm.dialog에서 제어
        	id: "EU005"
        });
    } else {
        sPageID = "#" + pageID;

        // 보고서가 있는가 여부판단 (2013.04.17)
        if( $(sPageID + INIT.BRIEF.briefTabID).is(":visible") ){
        	COMM._briefTab(sPageID);
        };
        
        // DOM 재정의 (2013.05.21)
        // $jqgridTd.css("white-space", "pre-wrap"); 를 기본으로 함 (원본은 pre) 
        
        // 주의! 다음 코드는 문제없는 코드이다. 
        // 		 현재는 비활성으로 처리. 필요시 다시 동작시킬 예정이다. (2013.05.22)
        /*if ( $(".grid_title", $(sPageID)).is(":visible") ){
        	$(".grid_title", $(sPageID)).after(
        			"<div class=\"grid_title_radio\">" + 
        			"<input type=\"radio\" name=\"" + gridTitleRadioName + "\" value=\"1\" />" +
        			_UCS.COMMON2017 + " <input type=\"radio\" name=\"" + gridTitleRadioName + "\" value=\"2\"/ checked=\"checked\">" + 
        			_UCS.COMMON2018 + " </div>"
        		); // .next().hide()로 숨겨 둘 수도 있다.
        	$("input[name=\"" + gridTitleRadioName + "\"]", $(sPageID)).click( function(){
        		var selected = $(this).val(),
        			$jqgridTd = $(".sub_grid .ui-jqgrid tr.jqgrow td", $(sPageID));
        		switch(selected){
        		case "1":
        			$jqgridTd.css("white-space", "pre");
        			break;
        		case "2":
        			$jqgridTd.css("white-space", "pre-wrap");
        			break;
        		default:
        			break;
        		}
        	});
        }*/
        
/*
        // 초기화 객체
        if (initialize) {
            // datepicker_prev 초기값 설정
            if (initialize.datepickerPervInit) {
                $(INIT.DATEPICKER.datepicker_prev, $(sPageID)).attr("alt", initialize.datepickerPervInit);
            }
            // 도움말
            if (initialize.helpUri) {
                COMM._helpUri(initialize.helpUri, sPageID);
            } else {
                $(COMM._INIT.HELP.btnHelp, $(sPageID)).attr("class", "ui-icon float-r " + COMM._INIT.HELP.btnHelpDisClass );
            }
            // 주의사항
            if (initialize.notice) {
                COMM._notice(initialize.notice, sPageID, initialize.noticeAuto);
            }
            // 달력 미사용
            if (!initialize.noneDatepicker) {
                COMM._setDatepicker(sPageID);
            }
            // 버튼 미사용
            if (!initialize.noneButton) {
                COMM._createButton(sPageID, pageID);
            }
            
        } else {
            COMM._setDatepicker(sPageID);
            COMM._createButton(sPageID, pageID);
        }

        // 인풋창 입력 제한 및 포메터(13.1.16 수정: 한글방지)
        // 영어만
        $("input.format_alphabet", $(sPageID)).keypress({type:INIT.FORMAT.typeAlphabet}, COMM.keyEventPrevent.keypress ).blur({type:INIT.FORMAT.typeKorea}, COMM.inputBlurReplace);
        // 영어, 숫자
        $("input.format_english", $(sPageID)).keypress({type:INIT.FORMAT.typeEnglish}, COMM.keyEventPrevent.keypress ).blur({type:INIT.FORMAT.typeKorea}, COMM.inputBlurReplace);
        
        $("input.format_number", $(sPageID)).keypress({type:INIT.FORMAT.typeNumber}, COMM.keyEventPrevent.keypress ).blur({type:INIT.FORMAT.typeNumber}, COMM.inputBlurReplace);

        $("input.format_phone", $(sPageID)).keypress({type:INIT.FORMAT.typePhone}, COMM.keyEventPrevent.keypress ).blur({type:INIT.FORMAT.typeKorea}, COMM.inputBlurReplace);

        $("input.format_account", $(sPageID)).keypress({type:INIT.FORMAT.typeAccount}, COMM.keyEventPrevent.keypress ).blur({type:INIT.FORMAT.typeKorea}, COMM.inputBlurReplace);

        $("input.format_date", $(sPageID)).keypress({type:INIT.FORMAT.typeDate}, COMM.keyEventPrevent.keypress ).blur({type:INIT.FORMAT.typeDate}, COMM.inputBlurReplace).keydown(COMM.keydown.dateLengthCheck);

        $("input.format_cardno", $(sPageID)).keypress({type:INIT.FORMAT.typeCardno}, COMM.keyEventPrevent.keypress ).blur({type:INIT.FORMAT.typeKorea}, COMM.inputBlurReplace);

        $("input.format_bizno", $(sPageID)).keypress({type:INIT.FORMAT.typeBizno}, COMM.keyEventPrevent.keypress ).blur({type:INIT.FORMAT.typeKorea}, COMM.inputBlurReplace);

        $("input.format_float", $(sPageID)).keypress({type:INIT.FORMAT.typeFloat}, COMM.keyEventPrevent.keypress ).blur({type:INIT.FORMAT.typeFloat}, COMM.inputBlurReplace);

        $("input.format_currency", $(sPageID)).keypress({type:INIT.FORMAT.typeCurrency}, COMM.keyEventPrevent.keypress ).blur({type:INIT.FORMAT.typeKorea}, COMM.inputBlurReplace).keyup(function(k){
        	var text = $(this).val()
        	,   pointIndex = text.indexOf(_INIT_SESSION._Point)
        	,   fraction = ""	// 소수부분
        	,   integer = "" 	// 정수부분
        	,   keyCode = k.keyCode ? k.keyCode : k.which;
        	
        	if (pointIndex !== -1) {
        		fraction = text.slice(pointIndex);
        		text = text.slice(0,pointIndex);
        	}
        	
        	integer = $.trim(text.replace(/\D/g,""));
            
        	// keyup은 keydown과 코드가 같다. 따라서 keyEventPrevent 와 같이 하지 않는다.
        	if (keyCode !== 37 && keyCode !== 39) {
        		if (integer.length > 0) {
        			$(this).val(String.format("{0:#,##0}", parseInt(integer, 10)) + fraction); // ,는 문자[,]가 아니라 ,에 해당(thousand)하는 값을 찍음
        		} else {
        		    $(this).val("");
        		}
        	}
        	// chrome 적용
        	// $(this).val( $(this).val().replace(/[ㄱ-ㅎㅏ-ㅣ가-힝]/g,"") ); 포커스 맨뒤로 이동하므로 사용 금지
        });
        
        $("input.format_currency0", $(sPageID)).keypress({type:INIT.FORMAT.typeCurrency0}, COMM.keyEventPrevent.keypress ).blur({type:INIT.FORMAT.typeKorea}, COMM.inputBlurReplace).keyup(function(k){
        	var text = $(this).val()
        	,   integer = $.trim(text.replace(/\D/g,"")) 	// 정수부분
        	,   keyCode = k.keyCode ? k.keyCode : k.which;
        	
        	// keyup은 keydown과 코드가 같다. 따라서 keyEventPrevent 와 같이 하지 않는다.
        	if (keyCode !== 37 && keyCode !== 39) {
        		if (integer.length > 0) {
        			$(this).val(String.format("{0:#,##0}", Number(integer)) ); // ,는 문자[,]가 아니라 ,에 해당(thousand)하는 값을 찍음
        		} else {
        			$(this).val("");
        		}
        	}
        });
        
        // select valuew = "" 이면 제거
        $("select", $(sPageID)).one("change", function() {
            $("option[value='']", $(this)).remove();
        });
    }
};
*/
/**
 * @description 날짜선택 공통 dialog
 * @version 1.0
 * @since 2013.01
 * @author 이병직
 * @event
 * @memberOf COMM
 * @param {String} type
 * @param {String} intervalYear
 */
COMM._datepickerDialog = function(type, intervalYear){
    switch (type) {
    case "interval":
        COMM.dialog({
            type:COMM._INIT.DIALOG.typeWarning,
            id:"EU020",
            messageConvertParam: [intervalYear]
        });
        break;
    case "inverse":
        COMM.dialog({
            type:COMM._INIT.DIALOG.typeWarning,
            id:"EU021"
        });
        break;
    }
};

/**
 * @description dynamicInput 요소 탐색 (class명을 통한 탐색)
 * @version 1.1
 * @since 2013.03
 * @author 이병직
 * @event
 * @memberOf COMM
 * @param {String} classes
 * @returns dynamicInput {Object} 
 */
COMM._detectDynamicInput = function ( classes ){
	var DATEPICKER = COMM._INIT.DATEPICKER
	,   dynamicInput = {}
	,   checked ={ term:false, state:false} //체크 되었나 여부
	,   inputClasses = classes.split(" ")
	,   classesLength = inputClasses.length;
	
	for (var x = 0; x < classesLength; x += 1) {
		if ( !checked.term && inputClasses[x].indexOf(DATEPICKER.dynamicInputTerm) === 0){
			dynamicInput.term = inputClasses[x].slice(DATEPICKER.dynamicInputTerm.length);
			checked.term = true; // 클래스는 하나뿐 이므로
		} else if ( !checked.state && inputClasses[x].indexOf(DATEPICKER.dynamicInputState) === 0){
			dynamicInput.state = inputClasses[x].slice(DATEPICKER.dynamicInputState.length);
			checked.state = true; // 클래스는 하나뿐 이므로
		}
	}
	
	dynamicInput.isDynamic = checked.term || checked.state; 
	return dynamicInput;
};

/**
 * @description 날짜선택 - 단독
 * @version 1.0
 * @since 2013.01
 * @author 이병직
 * @event
 * @memberOf COMM
 * @param {String} className
 * @param {String} sPageID
 */
COMM._datepicker = function ( className, sPageID ) {
	// datepuicker 생성
    $(className, $(sPageID) ).datepicker({
        showOn: "button",
        buttonImage: COMM._INIT.DATEPICKER.calendarImg,
        buttonImageOnly: true,
        buttonText: _UCS.COMMON2015,
        changeMonth: true,
        changeYear: true
        // 제한없음으로 변경 (01.24)
        //maxDate: 365,
        //minDate: -365
    });
    
    // dynamic input 처리
    $(className, $(sPageID) ).each( function (){
    	var dynamic = COMM._detectDynamicInput( $(this).attr("class") );
    	
    	if (dynamic.isDynamic){
    		if (dynamic.term) {
    			$(this).val( COMM._prevDateView(COMM.getToday(false), dynamic.term, [COMM._INIT.DATEPICKER.dynamicInputFile, "term"]));
    		}
    		
    		switch(dynamic.state){
    		case "disable":
    			$(this).datepicker("disable");
    			break;
    		case "readonly":
    			$(this).next().css("opacity","0.5").css("cursor","default").unbind("click");
    			break;
    		case "editable":
    			$(this).prop("readonly", false);
    			$(this).css("background","#FFF");
    			break;
    		default:
    			break;
    		}
    	}
    });
    
};

/**
 * @description 포멧넘버 - 자리수에 맞게 숫자표시
 * @version 1.0
 * @since 2013.01
 * @author 이병직
 * @class
 * @memberOf COMM
 * @param {Number} value 숫자
 * @param {Number} len 숫자자리수
 * @returns {String} 포멧넘버
 * @example
 * COMM.formatNumber(10,6);
 * "000010"
 */
COMM.formatNumber = function( value, len) {
    var num = '' + value;
    while (num.length < len) {
        num = '0' + num;
    }
    return num;
};

/**
 * @description 윤년 계산
 * @version 1.0
 * @since 2013.01
 * @author 이병직
 * @event
 * @memberOf COMM
 * @param {Number} year
 * @returns 개월별일자 {Number[]} 
 */
COMM._leap = function ( year ) {
    if ( year % 4 === 0 && year % 100 !== 0 || year % 400 === 0 ) {
        return [ 31,29,31,30,31,30,31,31,30,31,30,31 ];
    } else {
        return [ 31,28,31,30,31,30,31,31,30,31,30,31 ];
    }
};

/**
 * @description 오늘
 * @version 1.1
 * @since 2013.03
 * @author 이병직
 * @class
 * @memberOf COMM
 * @param {String} [type] ym, md, y, m, d
 * @param {Boolean} [isFormatter] 포멧적용 여부
 * @returns 오늘날짜 또는 false {String|false} 
 * @example
 * getToday();		//포멧적용 YMD
 * getToday(false);	//일반 YMD
 * getToday("ym");	//포멧적용 YM
 */
// 오늘 v1.1 - formatter 적용여부(true/false) 기본값은 true
COMM.getToday = function(type, isFormatter) {
	var TODAY_STRING =_INIT_SESSION._Today
	,   today = COMM._formatter_module.toDate(TODAY_STRING, "ymd")
	,	param = {a:type, b:isFormatter}
	,	result = COMM._param2Check([{"type":"string","value":param.a}, {"type":"boolean","value":param.b}]);
	if (typeof result === "object") {
		param = result;
	} else if (result === false){
		throw alert(TypeError("COMM.getToday 파라미터 타입오류 - 스크립트 구동정지."));
	}

	// isFormatter만 false이거나 모두 없는 경우 - YMD 반환 
	if (!param.a && param.b === false) {
		return TODAY_STRING;
	} else if (!param.a){
		return today.format("d"); 
	}
	
	// type이 존재하는 경우
	switch (param.a){
    case "ym":	// 년월
    	if (param.b === false) {
    		return TODAY_STRING.slice(0,6);
    	} else {
    		return today.format("Y");
    	}
        break;
    case "md":	// 월일
    	if (param.b === false) {
    		return TODAY_STRING.slice(4,8);
    	} else {
    		return today.format("M");
    	}
    	break;
    case "y":	// 년
    	return TODAY_STRING.slice(0,4);
    	break;
    case "m":	// 월
    	return TODAY_STRING.slice(4,6);
    	break;
    case "d":	// 일
    	return TODAY_STRING.slice(6,8);
    	break;
    default:	// 없는 포멧이면 YMD 반환
		if (param.b === false) {
			return TODAY_STRING;
		} else {
			return today.format("d");
		}
		break;
    }
};

/**
 * @description 날짜계산 (초기날자세팅 등) 
 * @version 1.1
 * @since 2013.04.16
 * @author 이병직
 * @event
 * @memberOf COMM
 * @param {String} dateString 20130416
 * @param {String} calulate d7, m3 등
 * @param {Boolean} errorMsg
 * @returns {Obejct} 년월일 string 이 담긴 객체
 */
COMM._prevDateCalculator = function(dateString, calulate, errorMsg) {
	var baseMonth =  parseInt(dateString.slice(4,6),10)
	,   baseDay =  parseInt(dateString.slice(6,8),10)
	,   baseDate = {
			yy: parseInt(dateString.slice(0,4),10),
			mm: baseMonth,
			dd: baseDay
		}
	,   resultDateString = {}
	,   days = COMM._leap(baseDate.yy);
	
    if(/^[m][0-9][0-9]?/.test(calulate)) {  //월 단위
    	calulate = parseInt( calulate.replace("m",""),10);
    	// 0 ~ 12 개월
        if (calulate > 12 || calulate <=0 ) {
            COMM.dialog({
                id: "EU006"
            ,   type: COMM._INIT.DIALOG.typeDebug
            ,	messageConvertParam: errorMsg
            });
            return false;
        } else {
        	baseDate.mm = baseMonth - calulate;
            if (baseDate.mm === 0) {
            	baseDate.yy -= 1; 
            	baseDate.mm = 12;
            	baseDate.dd = 31;
            } else if (baseDate.mm < 0){
            	baseDate.yy -= 1;
            	baseDate.mm = 12;
            	baseDate.dd = days[baseMonth -1];
            } else {
            	baseDate.dd = days[baseMonth - 1];
            }
            if (baseDate.dd > baseDay) {
            	baseDate.dd = baseDay;
            }
        }
        
    } else if (/^[d][0-9][0-9]?/.test(calulate)) { // 일 단위 (0~30)
    	calulate = parseInt( calulate.replace("d",""),10);
        if (calulate > 30 || calulate < 0) { //0 허용
            COMM.dialog({
                id: "EU007"
                ,   type: COMM._INIT.DIALOG.typeDebug
                ,	messageConvertParam: errorMsg
                });
                return false;
        } else {
        	baseDate.dd = baseDay - calulate;
            
            if (baseDate.dd  <= 0) {
            	baseDate.mm -= 1;
                if (baseDate.mm === 0) {
                	baseDate.yy -= 1; 
                	baseDate.mm = 12;
                	baseDate.dd = 31 + baseDate.dd;
                } else {
                	baseDate.dd = days[baseDate.mm - 1] + baseDate.dd;
                }
            }
        }
    } else {
        COMM.dialog({
            id: "EU008"
        ,   type: COMM._INIT.DIALOG.typeDebug
        ,	messageConvertParam: errorMsg
        });
        return false;
    }
    
    resultDateString = {yy:COMM.formatNumber(baseDate.yy, 4), mm:COMM.formatNumber(baseDate.mm, 2), dd:COMM.formatNumber(baseDate.dd, 2)};
    resultDateString.ymd = resultDateString.yy + resultDateString.mm + resultDateString.dd;
    
    return resultDateString; // 결과는 string 이 담긴 객체
};

/**
 * @description 포멧적용된 날짜계산 후 결과 반환 (ymd 형식)
 * @version 1.1
 * @since 2013.04.16
 * @author 이병직
 * @event
 * @memberOf COMM
 * @param {String} dateString 20130416
 * @param {String} calulate d7, m3 등
 * @param {Boolean} errorMsg
 * @returns {String} ymd형식
 */
COMM._prevDateView = function(dateString, calulate, errorMsg){
	return COMM._formatter_module.toDate(COMM._prevDateCalculator(dateString, calulate, errorMsg ).ymd, "ymd" ).format("d");
};

/**
 * @description 숫자 년월일을 입력하면 포멧적용된 YMD 반환
 * @version 1.1
 * @since 2013.04.16
 * @author 이병직
 * @event
 * @memberOf COMM
 * @param {Number} year
 * @param {Number} month 
 * @param {Number} day
 * @returns {String} ymd형식
 */
COMM._dateYMD = function (year, month, day) {
	return new Date(year, month -1, day).format("d");
};

/**
 * @description 날짜선택 - 조회기간
 * @version 1.1
 * @since 2013.03
 * @author 이병직
 * @event
 * @memberOf COMM
 * @param {String} className
 * @param {String} sPageID 
 */
COMM._datepicker_reference = function( className, sPageID ) {
    // var today = new Date()
    //,   todayYMD = { yy: today.getFullYear(), mm: today.getMonth() + 1, dd: today.getDate() }
	var todayYMD = {yy:COMM.getToday("y"), mm:COMM.getToday("m"), dd:COMM.getToday("d")}
    //,   INIT = COMM._INIT // 1기 예정 등 버튼에서 사용
    ,   DATEPICKER = COMM._INIT.DATEPICKER
    ,   todayFormat = ""
    ,   dp = { YY:0, MM:0, DD:0 }
    ,   dn = { YY:0, MM:0, DD:0 }
    ,   days = COMM._leap(todayYMD.yy)
    ,   alt = null;
    
    todayFormat = COMM.getToday();
    
    // 현재 표시된 기간별 날짜값으로 갱신
    var dateCalculator = function() {
        var getDp = COMM._formatter_module.toDateString($(DATEPICKER.datepicker_prev, $(sPageID)).val())
        ,   getDn = COMM._formatter_module.toDateString($(DATEPICKER.datepicker_next, $(sPageID)).val());
                
        dp.YY = parseInt(getDp.year, 10);
        dp.MM = parseInt(getDp.month, 10);
        dp.DD = parseInt(getDp.day, 10);
        
        dn.YY = parseInt(getDn.year, 10);
        dn.MM = parseInt(getDn.month, 10);
        dn.DD = parseInt(getDn.day, 10);
    };
    
    $(function() {
        $(className, $(sPageID) ).datepicker({
            showOn: "button",
            buttonImage: COMM._INIT.DATEPICKER.calendarImg,
            buttonImageOnly: true,
            changeMonth: true,
            changeYear: true,
            buttonText:"기간선택"
            // 제한없음으로 변경 (01.24)
            // maxDate:0 // 제한 없음으로 변경 
        });
        
        $(className, $(sPageID)).val( todayFormat );
        
        // 초기 날짜 세팅
        alt =  $(DATEPICKER.datepicker_prev , $(sPageID)).attr("alt");
        if (alt) {
        	$(DATEPICKER.datepicker_prev, $(sPageID)).val( COMM._prevDateView(COMM.getToday(false), alt, ["datepicker_prev", "datepickerPervInit"]) );
        }

        // change 이벤트
        $(className, $(sPageID)).change( function(){
            // var intervalYear = 1;  //제한은 1년 (03.27 폐기. 다음버전에선 옵션으로 경고만 띄우게 할 것)
            dateCalculator();
            // 정상일 경우가 더 많을 것이므로.
            if (dp.YY < dn.YY) {
            	// 정상
                /*if ( (dn.YY - dp.YY > intervalYear) || (dn.YY - dp.YY === intervalYear && dp.MM < dn.MM) || (dn.YY - dp.YY === intervalYear && dp.MM === dn.MM && dp.DD < dn.DD) ) {
                    COMM._datepickerDialog("interval", intervalYear);
                    $(className, $(sPageID)).val( todayFormat );
                }*/
            } else if (dp.YY === dn.YY && dp.MM < dn.MM) {
                //정상
            } else if (dp.YY === dn.YY && dp.MM === dn.MM && dp.DD <= dn.DD) {
                //정상
            } else {
                //COMM._datepickerDialog("inverse", intervalYear); //사용 중지.(13.2.27)
                //$(className, $(sPageID)).val( todayFormat );
                // 보통 앞부터 세팅하려는 심리가 있으므로, 다음 날짜를 선택한 날짜로 변경함. -> 앞의 날짜는 뒤의 날짜보다 항상 작음으로 변경됨.
                $(className, $(sPageID)).val( COMM._dateYMD(dn.YY, dn.MM, dn.DD) );
            }
        });
        // 바로가기 버튼
        $(".button_search_in01", $(sPageID)).click(function(){
            $(DATEPICKER.datepicker_prev, $(sPageID)).val( $(DATEPICKER.datepicker_next, $(sPageID)).val() );
        });
        $(".button_search_in02", $(sPageID)).click(function(){
            dateCalculator();
            dn.DD -= 1;
            if (dn.DD  === 0) {
                dn.MM -= 1;
                if (dn.MM === 0) {
                    dn.YY -= 1; 
                    dn.MM = 12;
                    dn.DD = 31;
                } else {
                    dn.DD = days[dn.MM - 1];
                }
            } 
            $(DATEPICKER.datepicker_prev, $(sPageID)).val( COMM._dateYMD(dn.YY, dn.MM, dn.DD) );
        });
        $(".button_search_in03", $(sPageID)).click(function(){
            dateCalculator();
            dn.DD -= 7;
            if (dn.DD  <= 0) {
                dn.MM -= 1;
                if (dn.MM === 0) {
                    dn.YY -= 1; 
                    dn.MM = 12;
                    dn.DD = 31 + dn.DD;
                } else {
                    dn.DD = days[dn.MM - 1] + dn.DD;
                }
            }
            $(DATEPICKER.datepicker_prev, $(sPageID)).val( COMM._dateYMD(dn.YY, dn.MM, dn.DD) );
        });
        $(".button_search_in04", $(sPageID)).click(function(){
            dateCalculator();
            dn.MM -= 1;
            if (dn.MM === 0) {
                dn.YY -= 1; 
                dn.MM = 12;
                dp.DD = 31;
            } else {
                dp.DD = days[dn.MM - 1];
            }
            if (dn.DD > dp.DD) {
                dn.DD = dp.DD;
            }
            $(DATEPICKER.datepicker_prev, $(sPageID)).val( COMM._dateYMD(dn.YY, dn.MM, dn.DD) );
        });
        // 3개월 버튼제거 (2013.05.14) - include 폴더 datepicker.jsp 와 짝을 이룸
/*        $(".button_search_in05", $(sPageID)).click(function(){
            dateCalculator();
            dn.MM -= 3;
            if (dn.MM === 0) {
                dn.YY -= 1; 
                dn.MM = 12;
                dp.DD = 31;
            } else if (dn.MM < 0){
                dn.YY -= 1;
                dn.MM += 12;
                dp.DD = days[dn.MM -1];
            } else {
                dp.DD = days[dn.MM - 1];
            }
            if (dn.DD > dp.DD) {
                dn.DD = dp.DD;
            }
            $(DATEPICKER.datepicker_prev, $(sPageID)).val( COMM._dateYMD(dn.YY, dn.MM, dn.DD) );
        });*/
        
        // By 정승환
        // 1기 예정(1월1일 ~ 3월 31일) 
        $(".button_search_in07", $(sPageID)).click(function(){;
            dateCalculator();

            $(DATEPICKER.datepicker_prev, $(sPageID)).val( COMM.formatNumber(dn.YY, 4) + COMM._INIT.FORMAT.date_delim + COMM.formatNumber(1, 2) + COMM._INIT.FORMAT.date_delim + COMM.formatNumber(1, 2) );
            $(DATEPICKER.datepicker_next, $(sPageID)).val( COMM.formatNumber(dn.YY, 4) + COMM._INIT.FORMAT.date_delim + COMM.formatNumber(3, 2) + COMM._INIT.FORMAT.date_delim + COMM.formatNumber(31, 2) );
            
        });
        
        // 1기 확정(4월1일 ~ 6월 30일) 
        $(".button_search_in08", $(sPageID)).click(function(){
            dateCalculator();
            
            $(DATEPICKER.datepicker_prev, $(sPageID)).val( COMM.formatNumber(dn.YY, 4) + COMM._INIT.FORMAT.date_delim + COMM.formatNumber(4, 2) + COMM._INIT.FORMAT.date_delim + COMM.formatNumber(1, 2) );
            $(DATEPICKER.datepicker_next, $(sPageID)).val( COMM.formatNumber(dn.YY, 4) + COMM._INIT.FORMAT.date_delim + COMM.formatNumber(6, 2) + COMM._INIT.FORMAT.date_delim + COMM.formatNumber(30, 2) );
            
        });
        
        // 2기 예정(7월1일 ~ 9월 30일) 
        $(".button_search_in09", $(sPageID)).click(function(){
            dateCalculator();
            
            $(DATEPICKER.datepicker_prev, $(sPageID)).val( COMM.formatNumber(dn.YY, 4) + COMM._INIT.FORMAT.date_delim + COMM.formatNumber(7, 2) + COMM._INIT.FORMAT.date_delim + COMM.formatNumber(1, 2) );
            $(DATEPICKER.datepicker_next, $(sPageID)).val( COMM.formatNumber(dn.YY, 4) + COMM._INIT.FORMAT.date_delim + COMM.formatNumber(9, 2) + COMM._INIT.FORMAT.date_delim + COMM.formatNumber(30, 2) );
            
        });
        
        // 2기 확정(10월1일 ~ 12월 31일) 
        $(".button_search_in10", $(sPageID)).click(function(){
            dateCalculator();
            
            $(DATEPICKER.datepicker_prev, $(sPageID)).val( COMM.formatNumber(dn.YY, 4) + COMM._INIT.FORMAT.date_delim + COMM.formatNumber(10, 2) + COMM._INIT.FORMAT.date_delim + COMM.formatNumber(1, 2) );
            $(DATEPICKER.datepicker_next, $(sPageID)).val( COMM.formatNumber(dn.YY, 4) + COMM._INIT.FORMAT.date_delim + COMM.formatNumber(12, 2) + COMM._INIT.FORMAT.date_delim + COMM.formatNumber(31, 2) );
            
        });
        
        // 1기 (1월1일 ~ 6월 30일 
        $(".button_search_in11", $(sPageID)).click(function(){
            dateCalculator();
            
            $(DATEPICKER.datepicker_prev, $(sPageID)).val( COMM.formatNumber(dn.YY, 4) + COMM._INIT.FORMAT.date_delim + COMM.formatNumber(1, 2) + COMM._INIT.FORMAT.date_delim + COMM.formatNumber(1, 2) );
            $(DATEPICKER.datepicker_next, $(sPageID)).val( COMM.formatNumber(dn.YY, 4) + COMM._INIT.FORMAT.date_delim + COMM.formatNumber(6, 2) + COMM._INIT.FORMAT.date_delim + COMM.formatNumber(30, 2) );
            
        });
        
        // 2기 (7월1일 ~ 12월 31일 
        $(".button_search_in12", $(sPageID)).click(function(){
            dateCalculator();
            
            $(DATEPICKER.datepicker_prev, $(sPageID)).val( COMM.formatNumber(dn.YY, 4) + COMM._INIT.FORMAT.date_delim + COMM.formatNumber(7, 2) + COMM._INIT.FORMAT.date_delim + COMM.formatNumber(1, 2) );
            $(DATEPICKER.datepicker_next, $(sPageID)).val( COMM.formatNumber(dn.YY, 4) + COMM._INIT.FORMAT.date_delim + COMM.formatNumber(12, 2) + COMM._INIT.FORMAT.date_delim + COMM.formatNumber(31, 2) );
            
        });
    });
    
};

/**
 * @description 도움말 창
 * @version 1.0
 * @since 2013.01
 * @author 이병직
 * @event
 * @memberOf COMM
 * @param {String} uri
 * @param {String} sPageID 
 */
COMM._helpUri = function (uri, sPageID) {
    $(COMM._INIT.HELP.btnHelp, $(sPageID)).click(function (){
        COMM.winOpen( uri, {w:800, h:600, name:"menual", resizable:"yes"});
    });
    
};

/**
 * @description 안내문구(지연시간 참조 추가)
 * @version 1.1
 * @since 2013.03
 * @author 이병직
 * @event
 * @memberOf COMM
 * @param {String} notice
 * @param {String} sPageID 
 * @param {Boolean} auto 
 */
COMM._notice = function (notice, sPageID, auto) {
    // IE 8 지원용 (멀티 backgroud-image 사용하지 않음.)
    $(".title_id", $(sPageID)).after('<img class="readme_button" src="' + COMM._INIT.NOTICE.btnSrc +'" />' + 
            '<span class="readme readme_content"><span class="readme readme_header"></span><span class="readme readme_body"><span class="readme_text">' + notice + '</span></span><span class="readme readme_footer"></span></span>');
    
    $(".readme_button", $(sPageID)).hide().click(function(){
        $(this).hide();
        $(".readme_content", $(sPageID)).slideDown();
    });
    
    if (auto === false || !_INIT_SESSION._DELAY_SECOND ) {
        $(".readme_button", $(sPageID)).show();
        $(".readme_content", $(sPageID)).hide().click(function(){
            $(this).slideUp(100, function() {
                $(".readme_button", $(sPageID)).show();
            });
        });
    } else {
        $(".readme_content", $(sPageID)).hide().slideDown().delay(_INIT_SESSION._DELAY_SECOND).slideUp(100, function(){
            $(".readme_button", $(sPageID)).show();  
        }).click(function(){
            $(this).slideUp(100, function() {
                $(".readme_button", $(sPageID)).show();
            });
        });
    }

};

/**
 * @description 알림숫자 제어
 * @version 1.0
 * @since 2013.01
 * @author 이병직
 * @class
 * @memberOf COMM
 * @param {String|Number} count 개수
 * @example
 * COMM.alarmSetter(10);
 * COMM.alarmSetter("10");
 */
COMM.alarmSetter = function (count) {
    var ace = COMM._INIT.ALARM_BOX.alarmCountElement
    ,   alarmNotiIco = COMM._INIT.ALARM_BOX.alarmNotiIconElement
    ,   documentNotiIco = COMM._INIT.APPROVAL_BOX.approvalNotiIconElement
    ,   newImage = COMM._INIT.ALARM_BOX.alarmNewImage
    ,   zeroImage = COMM._INIT.ALARM_BOX.alarmZeroImage;
    
    if (typeof count === "string") {
        count = parseInt(count, 10);
    }

    if (count > 0) {
    	$(alarmNotiIco).css("background", "url(" + newImage + ") no-repeat 0 0");
    } else if (count === 0) {
    	$(alarmNotiIco).css("background", "url(" + zeroImage + ") no-repeat 0 0");
    } else if (count < 0) {
        count = 0;
    }
    
    $(alarmNotiIco + ", " + documentNotiIco).css("visibility", "hidden");
    $(alarmNotiIco + ", " + documentNotiIco).css("visibility", "visible");
    
    $(ace).text(count);
};

/**
 * @description 알림숫자 갱신
 * @version 1.0
 * @since 2013.01
 * @author 이병직
 * @class
 * @memberOf COMM
 * @example
 * COMM.alarmRenovate();
 */
COMM.alarmRenovate = function() {
    var postData = {"_TASK_ID_":"AM2010030","_DATATYPE_":"COM"}
    ,   alarmCount = 0;
    
    $.getJSON( INITIALIZE_BASE + "/uxAction" ,postData, function (data) {
        if(!COMM._rCodeCheck(data, "alarmRenovate")){
            if (data.resultData.noti_ncnt) {
            	alarmCount =  Number(data.resultData.noti_ncnt);
            }
            COMM.alarmSetter( alarmCount );
            
        }// rCode 체크
    });
    
};

/**
 * @description 결재함 숫자 제어
 * @version 1.1
 * @since 2013.06.04
 * @author 이병직
 * @class
 * @memberOf COMM
 * @param {String|Number} count 개수
 * @example
 * COMM.approvalSetter(10);
 * COMM.approvalSetter("10");
 */
COMM.approvalSetter = function (count) {
    var ace = COMM._INIT.APPROVAL_BOX.approvalCountElement
    ,   alarmNotiIco = COMM._INIT.ALARM_BOX.alarmNotiIconElement
    ,   documentNotiIco = COMM._INIT.APPROVAL_BOX.approvalNotiIconElement
    ,   newImage = COMM._INIT.APPROVAL_BOX.approvalNewImage
    ,   zeroImage = COMM._INIT.APPROVAL_BOX.approvalZeroImage;
    
    if (typeof count === "string") {
        count = parseInt(count, 10);
    }

    if (count > 0) {
    	$(documentNotiIco).css("background", "url(" + newImage + ") no-repeat 0 0");
    } else if (count === 0) {
    	$(documentNotiIco).css("background", "url(" + zeroImage + ") no-repeat 0 0");
    } else if (count < 0) {
        count = 0;
    }
    
    $(alarmNotiIco + ", " + documentNotiIco).css("visibility", "hidden");
    $(alarmNotiIco + ", " + documentNotiIco).css("visibility", "visible");
    
    $(ace).text(count);
};

/**
 * @description 결재함 숫자 갱신
 * @version 1.1
 * @since 2013.06.04
 * @author 이병직
 * @class
 * @memberOf COMM
 * @param {function} errorAction ajax 에러시 동작할 함수
 * @example
 * COMM.approvalRenovate();
 * COMM.approvalRenovate( function(){} );
 */
COMM.approvalRenovate = function(errorAction) {
    var postData = {"_TASK_ID_":"AM1010011","_DATATYPE_":"COM"}
    ,   approvalCount = 0;
    
    if(typeof errorAction !== "function"){
    	errorAction = null;
    }
    
    $.getJSON( INITIALIZE_BASE + "/uxAction" ,postData, function (data) {
        if(!COMM._rCodeCheck(data, "alarmRenovate")){
            if (data.resultData.appr_ncnt) {
            	approvalCount =  Number(data.resultData.appr_ncnt);
            }
            COMM.approvalSetter( approvalCount );
        }// rCode 체크
    }).error( errorAction );
    
};

/**
 * @description 서버 수신 rCode 체크
 * @version 1.0
 * @since 2013.01
 * @author 이병직
 * @event
 * @memberOf COMM
 * @param {Object} json
 * @param {String} debugName
 * @returns {Boolean}
 */
COMM._rCodeCheck = function (json, debugName) {
  if (json["_R-CODE"] === "OK") {
      return false;
  } else if (json["_R-CODE"] === "WN"){
      if (window.console){
          console.info("COMM._rCodeCheck: debugName is ", debugName + " (call function name) [WN]");
      }
      COMM.dialog({
              type: COMM._INIT.DIALOG.typeWarning
          ,   message: json["_M-CONT"]
          ,   id: json["_M-CODE"]
          ,   detail: json["_M-CONT"]
      });
      return true;
  } else {
      if (window.console){
          //console.error("COMM._rCodeCheck: debugName is ", debugName + " (call function name) ["+ json["_R-CODE"] +"]");
      }
      COMM.dialog({
              type: COMM._INIT.DIALOG.typeError
          //,   message: json["_M-DESC"]
          ,   message: json["_M-CONT"]
          ,   id: json["_M-CODE"]
          ,   detail: json["_M-CONT"]
      });
      return true;
  }
};

// select 박스 생성 (12.12) 형식은 [ {text,value}, {text,value} ]
/* 데이타 형식
 { 
 "options": [{"text":"농협","value":"1"},{"text":"국민","value":"2"},{"text":"우리","value":"3"},{"text":"신한","value":"11"}],
 "defaultSelectedValue":"11"
 }  */
(function($) {
    $.fn.FWFselectBox = function( constructor ) {
        var arrayData = constructor.arrayData  // 이름 혼동 가능성으로 arrayData로 명명
        ,   defaultSelectedValue = constructor.defaultSelectedValue
        ,   selectAll = constructor.selectAll
        ,   selectAllValue = constructor.selectAllValue
        ,   selectAllText = constructor.selectAllText;
        
        return this.each(function(){
            if (this.tagName === "SELECT") {
                this.options.length = 0;
                if (selectAll === true){
                	// value ,text 없을때 및 각각 없을 때
                	if ($.isEmptyObject(selectAllValue) && $.isEmptyObject(selectAllText)){
                		this.add(new Option(COMM._INIT.SELECT_ALL.text, COMM._INIT.SELECT_ALL.value));
                	} else if ($.isEmptyObject(selectAllText) && typeof selectAllValue === "string") {
                		this.add(new Option(COMM._INIT.SELECT_ALL.text, selectAllValue));
                	} else if ($.isEmptyObject(selectAllValue) && typeof selectAllText === "string") {
                		this.add(new Option(selectAllText, COMM._INIT.SELECT_ALL.value));
                	} else if (typeof selectAllValue === "string" && typeof selectAllText === "string") {
                		this.add(new Option(selectAllText, selectAllValue));
                	}
                }
                
                if (arrayData) {
                    for (var x = 0; x < arrayData.length; x += 1) {
                    	if (navigator.userAgent.match(/msie/i)) {	// jquery 1.9.1 버전
                        //if ($.browser.msie){
                            this.add(new Option(arrayData[x].text, arrayData[x].value));
                        } else {
                            this.add(new Option(arrayData[x].text, arrayData[x].value), null);
                        }
                    } // for

                    for (var x = 0; x < this.options.length; x++) {
                        if (this.options[x].value === defaultSelectedValue) {
                            this.options[x].selected = true;
                            break;
                        }
                    } // for
                } // if
            }
        });
    };
    
    $.fn.serializeJSON = function() {
        var json = {};
        $.map($(this).serializeArray(), function(n, i){
            json[n["name"]] = n["value"];
        });
        return json;
    };
    
    /*
     * jQuery UI Monthpicker
     *
     * @licensed MIT <see below>
     * @licensed GPL <see below>
     *
     * @author Luciano Costa
     * http://lucianocosta.info/jquery.mtz.monthpicker/
     *
     * Depends:
     *  jquery.ui.core.js
     */

    /**
     * MIT License
     * Copyright (c) 2011, Luciano Costa
     * 
     * Permission is hereby granted, free of charge, to any person obtaining a copy 
     * of this software and associated documentation files (the "Software"), to deal 
     * in the Software without restriction, including without limitation the rights 
     * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell 
     * copies of the Software, and to permit persons to whom the Software is 
     * furnished to do so, subject to the following conditions:
     * 
     * The above copyright notice and this permission notice shall be included in
     * all copies or substantial portions of the Software.
     * 
     * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
     * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
     * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
     * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
     * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
     * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
     * THE SOFTWARE.
     */
    /**
     * GPL LIcense
     * Copyright (c) 2011, Luciano Costa
     * 
     * This program is free software: you can redistribute it and/or modify it 
     * under the terms of the GNU General Public License as published by the 
     * Free Software Foundation, either version 3 of the License, or 
     * (at your option) any later version.
     * 
     * This program is distributed in the hope that it will be useful, but 
     * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
     * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
     * for more details.
     * 
     * You should have received a copy of the GNU General Public License along 
     * with this program. If not, see <http://www.gnu.org/licenses/>.
     */
    
    var methods = {
            init : function (options) { 
                return this.each(function () {
                    var 
                        $this = $(this),
                        data = $this.data('monthpicker'),
                        year = (options && options.year) ? options.year : parseInt(COMM.getToday("y"),10), //(new Date()).getFullYear(),
                        settings = $.extend({
                            //pattern: 'mm/yyyy',
                            pattern: _INIT_SESSION._Date_Format_Order_YM[0] < _INIT_SESSION._Date_Format_Order_YM[1] ? 'yyyy'+ COMM._INIT.FORMAT.date_delim +'mm' : 'mm'+ COMM._INIT.FORMAT.date_delim +'yyyy',
                            selectedMonth: null,
                            selectedMonthName: '',
                            selectedYear: year,
                            startYear: year - 10,
                            finalYear: year + 10,
                            //monthNames: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
                            monthNames: [_UCS.COMMON2021, _UCS.COMMON2022, _UCS.COMMON2023, _UCS.COMMON2024, _UCS.COMMON2025, _UCS.COMMON2026, _UCS.COMMON2027, _UCS.COMMON2028, _UCS.COMMON2029, _UCS.COMMON2030, _UCS.COMMON2031, _UCS.COMMON2032],
                            id: "monthpicker_" + (Math.random() * Math.random()).toString().replace('.', ''),
                            openOnFocus: true,
                            disabledMonths: []
                        }, options);

                    settings.dateSeparator = settings.pattern.replace(/(mmm|mm|m|yyyy|yy|y)/ig,'');

                    // If the plugin hasn't been initialized yet for this element
                    if (!data) {
                        $(this).data('monthpicker', {
                            'target': $this,
                            'settings': settings
                        });

                        // 인풋 클릭 막았음 (위에 버튼 이벤트 처리에서) 그리고 else 추가.
                        if (settings.openOnFocus === true) {
                            $this.bind('focus', function () {
                                $this.monthpicker('show');
                            });
                        } else {
                            $this.prop("readOnly", true);
                            $this.keydown(COMM.keyEventPrevent.keydownBackspace);
                        }
                        
                        // 아이콘 클릭 추가
                        $this.next().bind('click', function (e, month, year) {
                            $this.monthpicker('show');
                        });

                        $this.monthpicker('mountWidget', settings);

                        $this.bind('monthpicker-click-month', function (e, month, year) {
                            $this.monthpicker('setValue', settings);
                            $this.monthpicker('hide');
                        });

                        // hide widget when user clicks elsewhere on page
                        $this.addClass("mtz-monthpicker-widgetcontainer");
                        $(document).unbind("mousedown.mtzmonthpicker").bind("mousedown.mtzmonthpicker", function (e) {
                            if (!e.target.className || e.target.className.toString().indexOf('mtz-monthpicker') < 0) {
                                $(".mtz-monthpicker-widgetcontainer").each(function () {
                                    if (typeof($(this).data("monthpicker"))!="undefined") { 
                                        $(this).monthpicker('hide'); 
                                    }
                                });
                            }
                        });
                    }

                });
            },

            show: function (n) {
                var widget = $('#' + this.data('monthpicker').settings.id);
                var monthpicker = $('#' + this.data('monthpicker').target.attr("id") + ':eq(0)');
                
                // 선택 월 표시 추가
                var selectedMonth = parseInt($(this).val().slice(5,7),10) - 1;
                $(".mtz-monthpicker-month", widget).each(function(index){
                    if (selectedMonth === index ) {
                        $(this).css("color", "#ef651c").css("background-color", "#ffffff").css("border-color", "#89a2c0");
                    } else {
                        $(this).css("color", "#0066b5").css("background-color", "#e6fede").css("border-color", "#cbdee2");
                    }
                });
                
                widget.css("top", monthpicker.offset().top  + monthpicker.outerHeight());
                widget.css("left", monthpicker.offset().left);
                widget.show();
                widget.find('select').focus();
                this.trigger('monthpicker-show');
            },

            hide: function () {
                var widget = $('#' + this.data('monthpicker').settings.id);
                if (widget.is(':visible')) {
                    widget.hide();
                    this.trigger('monthpicker-hide');
                }
            },

            setValue: function (settings) {
                var 
                    month = settings.selectedMonth,
                    year = settings.selectedYear;

                if(settings.pattern.indexOf('mmm') >= 0) {
                    month = settings.selectedMonthName;
                } else if(settings.pattern.indexOf('mm') >= 0 && settings.selectedMonth < 10) {
                    month = '0' + settings.selectedMonth;
                }

                if(settings.pattern.indexOf('yyyy') < 0) {
                    year = year.toString().substr(2,2);
                } 

                if (settings.pattern.indexOf('y') > settings.pattern.indexOf(settings.dateSeparator)) {
                    this.val(month + settings.dateSeparator + year);
                } else {
                    this.val(year + settings.dateSeparator + month);
                }
                this.change(); //체인지 이벤트 발생
            },

            disableMonths: function (months) {
                var 
                    settings = this.data('monthpicker').settings,
                    container = $('#' + settings.id);

                settings.disabledMonths = months;

                container.find('.mtz-monthpicker-month').each(function () {
                    var m = parseInt($(this).data('month'));
                    if ($.inArray(m, months) >= 0) {
                        $(this).addClass('ui-state-disabled');
                    } else {
                        $(this).removeClass('ui-state-disabled');
                    }
                });
            },

            mountWidget: function (settings) {
                var
                    monthpicker = this,
                    container = $('<div id="'+ settings.id +'" class="ui-datepicker ui-widget ui-widget-content ui-helper-clearfix ui-corner-all" />'),
                    header = $('<div class="ui-datepicker-header ui-widget-header ui-helper-clearfix ui-corner-all mtz-monthpicker" />'),
                    combo = $('<select class="mtz-monthpicker mtz-monthpicker-year" />'),
                    table = $('<table class="mtz-monthpicker" />'),
                    tbody = $('<tbody class="mtz-monthpicker" />'),
                    tr = $('<tr class="mtz-monthpicker" />'),
                    td = '',
                 // selectedYear = settings.selectedYear,
                    attrSelectedYear = $(this).data('selected-year'),
                    attrStartYear = $(this).data('start-year'),
                    attrFinalYear = $(this).data('final-year');
                 // 04.16 주석처리 (사용하지 않는 변수)
                 // , currentMonth = $(this).data('current-month') || parseInt( COMM.getToday("m"),10)
                 // , currentYear = $(this).data('current-year') || parseInt(COMM.getToday("y"),10);
                    

                if (attrSelectedYear) {
                    settings.selectedYear = attrSelectedYear;
                }

                if (attrStartYear) {
                    settings.startYear = attrStartYear;
                }

                if (attrFinalYear) {
                    settings.finalYear = attrFinalYear;
                }
                

                container.css({
                    position:'absolute',
                    zIndex:500,
                    whiteSpace:'nowrap',
                    overflow:'hidden',
                    textAlign:'center',
                    display:'none',
                    top: monthpicker.offset().top + monthpicker.outerHeight(),
                    left: monthpicker.offset().left
                });

                // mount years combo
                for (var i = settings.startYear; i <= settings.finalYear; i++) {
                    var option = $('<option class="mtz-monthpicker" />').attr('value', i).append(i);
                    if (settings.selectedYear === i) {
                        option.attr('selected', 'selected');
                    }
                    combo.append(option);
                }
                header.append(combo).appendTo(container);

                // mount months table
                for (var i=1; i<=12; i++) {
                    /*td = $('<td class="ui-state-default mtz-monthpicker mtz-monthpicker-month" style="padding:5px;cursor:default;" />').attr('data-month',i);
                    td.append( settings.monthNames[i-1] );*/
                    // wrap 추가 (2.4)
                    td = $('<td class="ui-state-default mtz-monthpicker"/>');
                    td.append( $('<div class="mtz-monthpicker-month">' + settings.monthNames[i-1] + '</div>').attr('data-month',i) );
                    tr.append(td).appendTo(tbody);
                    if (i % 3 === 0) {
                        tr = $('<tr class="mtz-monthpicker" />'); 
                    }
                }

                table.append(tbody).appendTo(container);

                container.find('.mtz-monthpicker-month').bind('click', function () {
                    var m = parseInt($(this).data('month'));
                    if ($.inArray(m, settings.disabledMonths) < 0 ) {
                        settings.selectedMonth = $(this).data('month');
                        settings.selectedMonthName = $(this).text();
                        monthpicker.trigger('monthpicker-click-month', $(this).data('month'));
                    }
                });

                container.find('.mtz-monthpicker-year').bind('change', function () {
                    settings.selectedYear = $(this).val();
                    monthpicker.trigger('monthpicker-change-year', $(this).val());
                });

                container.appendTo('body');
            },

            destroy: function () {
                return this.each(function () {
                    // TODO: look for other things to remove
                    $(this).removeData('monthpicker');
                });
            }

        };

        $.fn.monthpicker = function (method) {
            if (methods[method]) {
                return methods[method].apply(this, Array.prototype.slice.call( arguments, 1 ));
            } else if (typeof method === 'object' || ! method) {
                // 아이콘 추가
            	$(this).after('<span class="search_t"><img src="'+INITIALIZE_BASE_IMG+'/calendar.png" /></span>');
                return methods.init.apply(this, arguments);
            } else {
                $.error('Method ' + method + ' does not exist on jQuery.mtz.monthpicker');
            }    
        };
    
})(jQuery);

/**
 * @description select options 생성용 객체 처리
 * @version 1.0
 * @since 2013.01
 * @author 이병직
 * @event
 * @memberOf COMM
 * @param {Object} selectObj
 */
COMM._selectAjax = function (selectObj) {
    var async = selectObj.async
    ,   url = "uxMarket"
    ,   stringType = ""; // "value:text;" 형식
    
    if ( selectObj.async !== false) {
        async = true;
    }
    
    if(selectObj.param.isAction){
    	url = INITIALIZE_BASE + "/uxAction";
    }
    
    $.ajax({
        type: "POST"
    ,   url: url
    ,   dataType: 'json'
    ,   async: async
    ,   data: selectObj.param
    ,   success: function(data){
            if(COMM._rCodeCheck(data, "COMM._selectAjax")){
                return
            };
            var dataOptions = null
            ,   selectBox ="";

            if(selectObj.param.isAction){
            	data.options = COMM._rowsToSelectoptions(data.rows, selectObj.valueCol, selectObj.textCol , selectObj.selectedCol);
            	if(selectObj.selectedCol && data.options.length > 0){	// 배열 끝이 선택값.
            		data.defaultSelectedValue = data.options.pop();	
            	}
            }
            
            if (selectObj.selectName) {
            	var selectNames = selectObj.selectName.split(",");
            	var sLength = selectNames.length;
            	
            	// 한번에 여러 셀렉트박스를 그리는 경우를 반영 (2013.07.17)
            	for (var x = 0; x < sLength; x += 1) {
            		
            		selectBox = $(selectObj.sFormID + " select[name=" +selectNames[x] + "]");
            		
            		if (selectObj.selectedVal) {
            			data.defaultSelectedValue = selectObj.selectedVal; 
            		}
            		
            		selectBox.FWFselectBox({
            			"arrayData": data.options
            			,   "defaultSelectedValue": data.defaultSelectedValue
            			,   "selectAll": selectObj.selectAll
            			,   "selectAllValue": selectObj.selectAllValue
            			,   "selectAllText": selectObj.selectAllText
            		});
            		selectBox.change( function(){
            			$("option[value='']", selectBox).remove();
            			if ($.isFunction(selectObj.change)) {
            				selectObj.change(selectBox.val());
            			}
            		});
            		if (typeof selectObj.callback === "function") {
            			selectObj.callback(data);
            		};
            	}
            	
            } else if (selectObj.isArray !== true){
                dataOptions = data.options;
                for (var x = 0; x < dataOptions.length; x += 1) {
                    stringType += dataOptions[x].value + ":" + dataOptions[x].text +";";
                }
                stringType = stringType.slice(0,stringType.length - 1);
                selectObj.callback(stringType);
            } else {
                dataOptions = data.options;
                selectObj.callback(dataOptions);
            }
        }
    ,   error: function(xhr,status,error){
	    	xhr.devMethod = "COMM._selectAjax";
	    	COMM._ajaxError(xhr,status,error); 
	    	}
    });
};

/**
 * @description action을 위한 select options 생성용 객체 처리
 * @version 1.1
 * @since 2013.04.18
 * @author 이병직
 * @event
 * @memberOf COMM
 * @param {Object[]} rows
 * @param {String} valueCol
 * @param {String} textCol
 * @param {String} selectedCol
 * @returns data {Object[]}
 */
COMM._rowsToSelectoptions = function(rows, valueCol, textCol, selectedCol ) {
	var data = []
	,   x = 0
	,   y = 0
	,   box = {}
	,   selectedRow = 0
	,   length = rows.length;
	
	// null 처리
	if(!(rows && length > 0)){
		return data;
	}
	
	
	if (selectedCol){
		for (x = 0; x < length; x +=1) {
			box = {}; // 초기화
			for(y in rows[x]){
				if (y === valueCol) {
					box.value = rows[x][y];
				}
				// 숫자와 같이 value와 text가 동일한 경우를 위해
				if (y === textCol) {
					box.text = rows[x][y];
				}
				// selectedCol 이 있는 경우
				if (y === selectedCol && rows[x][y] === "Y") {
					selectedRow = x;
				}
			}
			data.push(box);
		}
		data.push(data[selectedRow].value); // 선택할 값이 있는 row 번호의 value 값
		
	} else {
		for (x = 0; x < length; x +=1) {
			box = {}; // 초기화
			for(y in rows[x]){
				if (y === valueCol) {
					box.value = rows[x][y];
				}
				// 숫자와 같이 value와 text가 동일한 경우를 위해
				if (y === textCol) {
					box.text = rows[x][y];
				}
				
			}
			data.push(box);
		}
	} // selectedCol 이 없는 경우

	return data;
};

/**
 * @description get방식을 post방식으로
 * @version 1.1
 * @since 2013.03
 * @author 이병직
 * @event
 * @memberOf COMM
 * @param {String} get
 * @returns {Object} post
 */
COMM._getToPost = function (get) {
	var post = {}
	,   box = []
	,   getData = get.split("&");
	
	for (var x = 0 ;x < getData.length; x += 1) {
		box = getData[x].split("=",2);
		post[box[0]] = box[1];
	}
	
	return post;
};

/**
 * @description 객체 속성 복사(추가)
 * @version 1.1
 * @since 2013.03
 * @author 이병직
 * @event
 * @memberOf COMM
 * @param {Object} jsonTarget
 * @param {Object} addJsonData
 */
COMM._addPostData = function (jsonTarget, addJsonData) {
    for (var x in addJsonData) {
        jsonTarget[x] = addJsonData[x];
    };
};

/**
 * @description select options 생성용 객체
 * @version 1.0
 * @since 2013.01
 * @author 이병직
 * @event
 * @memberOf COMM
 * @param {String} type
 * @param {Objec} constructor
 * @param {Objec} standardObj
 */
COMM._selectConstructor = function(type, constructor, standardObj) {
    var postData = {};
    
    if(!COMM._declareCheck(constructor, standardObj)){
        return false;
    };
    
    switch (type) {
    case "comCode":
        if (constructor.grpCd) {
            postData["_TASK_ID_"] = "CommonCode";
            postData["grp_cd"] = constructor.grpCd;
            if (constructor.attr1Cd) { postData["attr_1_cd"] = constructor.attr1Cd; } // 예시 attr1Cd: "Y"
            if (constructor.attr2Cd) { postData["attr_2_cd"] = constructor.attr2Cd; } // 예시 attr2Cd: "Y"
            if (constructor.attr3Cd) { postData["attr_3_cd"] = constructor.attr3Cd; } // 예시 attr3Cd: "Y"
        } else {
            postData["_TASK_ID_"] = constructor.taskID;
            if (constructor.sysMgtNo) { postData["sys_mgt_no"] =  constructor.sysMgtNo; }
            if (constructor.crpnSeqNo) { postData["crpn_seq_no"] = constructor.crpnSeqNo; }
        }
    	if (constructor.param) {
    		COMM._addPostData(postData, COMM._getToPost(constructor.param));
    	}
        break;
    case "select5000":
        postData["_TASK_ID_"] = "Select5000";
        postData["sys_mgt_no"] = constructor.sysMgtNo;
        postData["sboxGroup"] = constructor.sboxGroup;
        break;
    case "userSelect5050":
        postData["_TASK_ID_"] = "UserSelect5050";
        postData["sboxGroup"] = constructor.sboxGroup;
        postData["groupData"] = constructor.groupData;
        break;
    case "action": //(04.18)
    	postData["_TASK_ID_"] = constructor.taskID;
    	postData["_DATATYPE_"] = "GRD";
    	if (constructor.param) {
    		COMM._addPostData(postData, COMM._getToPost(constructor.param));
    	}
    	postData["isAction"] = true;
    	break;
/*    case "languageSelect":
        postData["_TASK_ID_"] = "";
        break;*/
    }
    
    COMM._selectAjax({
            param: postData
        ,   sFormID: "#" + constructor.formID
        ,   selectName: constructor.selectName
        ,   change: constructor.chageOption
        ,   callback: constructor.callback
        ,   selectAll: constructor.selectAll
        ,   selectedVal: constructor.selectedVal
        ,   selectAllValue: constructor.selectAllValue
        ,   selectAllText: constructor.selectAllText
        // data 일 경우
        ,   async: constructor.async
        // array 일 경우
        ,   isArray: constructor.isArray
        // action 일 경우
        ,   valueCol:constructor.valueCol
        ,   textCol:constructor.textCol
        ,   selectedCol:constructor.selectedCol
    });
    
};

/**
 * @description password check 동기화
 * @version 1.0
 * @since 2013.01
 * @author 이병직
 * @class
 * @memberOf COMM
 * @param {String} password 검사할 평문 패스워드
 * @returns {Boolean}
 */
COMM.passwordCheck = function (password) {
    var checkResult = false
    ,   dataObj = {
            "_TASK_ID_":"UserPwdCheck"
        ,   "user_pwd":password
        };
    $.ajax({
        type: "POST"
    ,   url: "uxMarket"
    ,   async: false
    ,   data: dataObj
    ,   success: function(data){
            checkResult = data === "true" ? true: false;  // 결과 값 (String: true, false)
        }
    ,   error: function(xhr,status,error){
    		xhr.devMethod = "COMM.passwordCheck";
    		COMM._ajaxError(xhr,status,error); 
    	}
    });
    
    return checkResult;
    
};

/**
 * @description 특정 value 를 가진 options 삭제
 * @version 1.0
 * @since 2013.01
 * @author 이병직
 * @class
 * @memberOf COMM
 * @param {Object} constructor
 * @param {String} constructor.formID 폼 ID
 * @param {String} constructor.selectName 셀렉트 name
 * @param {String} constructor.removeValue 삭제할 option이 가진 value
 */
COMM.selectOptionRemove = function(constructor) {
    var removeValue = constructor.removeValue
    ,   selectName = constructor.selectName
    ,   formID = constructor.formID;
    
    if(!COMM._declareCheck(constructor, COMM._STANDARD.selectOptionRemove)){
        return false;
    };
    $("option[value='" + removeValue + "']", $("#" + formID + " select[name=" + selectName +"]")).remove();  
    
};

/**
 * @description WAS object refresh ( 파라미터 네가지 형식 지원: t,s 또는 t 또는 s 또는 없음)
 * @version 1.1
 * @since 2013.03
 * @author 이병직
 * @class
 * @memberOf COMM
 * @param {String} [specifyTask] 특정요소 리로드 SADM00010(시스템정보), SADM50010(다국어), SADM00110(공통코드) 외.
 * @param {Function} [successFunction] 리로드 성공시 동작할 함수
 * @example
 * COMM.marketReload();				//전체를 리로드
 * COMM.marketReload("SADM50010");  // 다국어만 리로드
 */
COMM.marketReload  = function (specifyTask, successFunction) {
	var reloadData = ""
	,   param = {a:specifyTask, b:successFunction}
	,   result = ""
	,   stampTime = $.now();
	
	result = COMM._param2Check([{"type":"string","value":param.a}, {"type":"function","value":param.b}]);
	if (typeof result === "object") {
		param = result;
	} else if (result === false){
		return false;
	}
	
	// t,s 또는 t 또는 없음
	if (typeof param.a === "string") {
		reloadData = "_TASK_ID_=MarketReload&specifyTask=" + param.a;
	} else {
		reloadData = "_TASK_ID_=MarketReload";
	};
	
/*	// s 인 경우
	if (typeof specifyTask === "function" && typeof successFunction === "undefined"){
		successFunction = specifyTask;
	} 
*/
    $.ajax({
        url:"uxMarket",
        async: true,
        data: reloadData,
        beforeSend: function() { COMM.progressbar(stampTime); } ,
        success: function(){
            //$("#center_content_sub").tabs("refresh"); //항상 tabs 안에서 쓴다는 보장이 없다. (04.10)
            if (typeof param.b === "function") {
            	param.b();
            }
        },
        complete: function(){
            COMM.progressbar("off", stampTime);
        },
        error:  function(xhr,status,error){
        	xhr.devMethod = "COMM.marketReload";
        	COMM._ajaxError(xhr,status,error); 
        	}
    });
};

//// select box series ////
/**
 * @description //// select box series ////
 * @version 1.1
 * @since 2013.01
 * @author 이병직
 * @class
 * @memberOf COMM
 * @param {Object} constructor
 * @param {String} constructor.formID 폼 ID
 * @param {String} constructor.sysMgtNo 시스템관리번호 
 * @param {String} constructor.sboxGroup 그룹 명칭 
 * @param {String} constructor.selectName 셀렉트 name
 * @param {Boolean} [constructor.selectAll] true이면 전체(ALL)를 붙임
 * @param {String} [constructor.selectAllValue=ALL] 전체일때 사용할 value 값
 * @param {String} [constructor.selectAllText=ALL] 전체일때 사용할 text 값
 * @param {Function} [constructor.chageOption] select 값이 변할 때 동작하는 함수
 * @param {String} [constructor.selectedVal] 최초, selectedVal 값으로 선택
 * @param {Function} [constructor.callback] 콜백
 */
COMM.select5000ToSelect = function (constructor) {
    COMM._selectConstructor("select5000", constructor, COMM._STANDARD.select5000ToSelect);
    
};

/**
 * @description //// select box series ////
 * @version 1.1
 * @since 2013.01
 * @author 이병직
 * @class
 * @memberOf COMM
 * @param {Object} constructor
 * @param {String} constructor.formID 폼 ID
 * @param {String} constructor.groupData 그룹 데이타 
 * @param {String} constructor.sboxGroup 그룹 명칭 
 * @param {String} constructor.selectName 셀렉트 name
 * @param {Boolean} [constructor.selectAll] true이면 전체(ALL)를 붙임
 * @param {String} [constructor.selectAllValue=ALL] 전체일때 사용할 value 값
 * @param {String} [constructor.selectAllText=ALL] 전체일때 사용할 text 값
 * @param {Function} [constructor.chageOption] select 값이 변할 때 동작하는 함수
 * @param {String} [constructor.selectedVal] 최초, selectedVal 값으로 선택
 * @param {Function} [constructor.callback] 콜백
 */
COMM.userSelect5050ToSelect = function (constructor) {
    COMM._selectConstructor("userSelect5050", constructor, COMM._STANDARD.userSelect5050ToSelect);
    
};

/**
 * @description //// select box series ////
 * <br/>
 * select box series 는 타스크명에 따른 사용가능한 옵션을 주의해야 한다.
 * @version 1.1
 * @since 2013.01
 * @author 이병직
 * @class
 * @memberOf COMM
 * @param {Object} constructor
 * @param {String} constructor.formID 폼 ID
 * @param {String} constructor.selectName 셀렉트 name
 * @param {String} [constructor.taskID] (시스템)SystemInfo (전체)CrpnInfo, BizrInfo,(권한에 따라)UserCrpnInfo, UserBizrInfo
 * @param {Boolean} [constructor.attr1Cd] 공통코드 속성
 * @param {Boolean} [constructor.attr2Cd] 공통코드 속성
 * @param {Boolean} [constructor.attr3Cd] 공통코드 속성
 * @param {String} [constructor.grpCd] 공통코드 속성
 * @param {String} [constructor.sysMgtNo] CrpnInfo, BizrInfo 속성 
 * @param {String} [constructor.crpnSeqNo] BizrInfo 속성
 * @param {Boolean} [constructor.selectAll] true이면 전체(ALL)를 붙임
 * @param {String} [constructor.selectAllValue=ALL] 전체일때 사용할 value 값
 * @param {String} [constructor.selectAllText=ALL] 전체일때 사용할 text 값
 * @param {Function} [constructor.chageOption] select 값이 변할 때 동작하는 함수
 * @param {String} [constructor.selectedVal] 최초, selectedVal 값으로 선택
 * @param {String} [constructor.param] task로 전달되는 param
 * @param {Function} [constructor.callback] 콜백
 */
COMM.comCodeToSelect = function (constructor) {
    COMM._selectConstructor("comCode", constructor, COMM._STANDARD.comCodeToSelect);
    
};

/**
 * @description //// select box series ////
 * <br/>
 * 사용자정의 타스크를 이용한 select box (GRD 타입을 이용)
 * @version 1.1
 * @since 2013.04.18
 * @author 이병직
 * @class
 * @memberOf COMM
 * @param {Object} constructor
 * @param {String} constructor.formID 폼ID
 * @param {String} constructor.selectName 셀렉트이름
 * @param {String} constructor.taskID 타스크 ID
 * @param {String} constructor.textCol text로 사용할 컬럼명
 * @param {String} constructor.valueCol value로 사용할 컬럼명
 * @param {String} [constructor.selectAll] 전체를 붙임
 * @param {String} [constructor.selectAllValue=ALL] 전체일때 사용할 value값
 * @param {String} [constructor.selectAllText=ALL] 전체일때 사용할 text 값
 * @param {String} [constructor.chageOption] select 값이 변할 때 동작하는 함수
 * @param {String} [constructor.selectedCol] 해당 column 값이 Y인 값을 기본값으로. 없으면 무시
 * @param {String|Object} [constructor.param] 전달할 파라미터
 * @param {String} [constructor.async] ajax async 여부
 * @param {Function} [constructor.callback] 콜백
 * @example
 * COMM.actionToSelect({
 * 		formID:"${_PGM_ID_}form",
 *		selectName: "noti_seq",
 *		taskID: "AS1020011",
 *		textCol:"text",
 *		valueCol:"value",
 *		selectAll: true,
 *		selectedCol:"",
 *		param: "~D~noti_dt=" + changeDt,
 *		callback: function(){}
 * });
 */
COMM.actionToSelect = function (constructor) {
	COMM._selectConstructor("action", constructor, COMM._STANDARD.actionToSelect);
};

/**
 * @description //// select box series ////
 * <br/>
 * data만 이용시
 * <br/>
 * select box series 는 타스크명에 따른 사용가능한 옵션을 주의해야 한다.
 * @version 1.1
 * @since 2013.01
 * @author 이병직
 * @class
 * @memberOf COMM
 * @param {Object} constructor
 * @param {String} constructor.formID 폼 ID
 * @param {String} [constructor.taskID] (시스템)SystemInfo (전체)CrpnInfo, BizrInfo,(권한에 따라)UserCrpnInfo, UserBizrInfo
 * @param {Boolean} [constructor.attr1Cd] 공통코드 속성
 * @param {Boolean} [constructor.attr2Cd] 공통코드 속성
 * @param {Boolean} [constructor.attr3Cd] 공통코드 속성
 * @param {String} [constructor.grpCd] 공통코드 속성
 * @param {String} [constructor.sysMgtNo] CrpnInfo, BizrInfo 속성 
 * @param {String} [constructor.crpnSeqNo] BizrInfo 속성
 * @param {Boolean} [constructor.async] 동기화 여부
 * @param {Boolean} [constructor.selectAll] true이면 전체(ALL)를 붙임
 * @param {String} [constructor.selectAllValue=ALL] 전체일때 사용할 value 값
 * @param {String} [constructor.selectAllText=ALL] 전체일때 사용할 text 값
 * @param {String} [constructor.selectedVal] 최초, selectedVal 값으로 선택
 * @param {Function} [constructor.callback] 콜백
 * @param {Boolean} [constructor.onlyText] text만 필요한 경우
 */
COMM.comCodeToString = function (constructor) {
    COMM._selectConstructor("comCode", constructor, COMM._STANDARD.comCodeToString);
    
};

/**
 * @description //// select box series ////
 * <br/>
 * data 중 array 필요시
 * <br/>
 * select box series 는 타스크명에 따른 사용가능한 옵션을 주의해야 한다.
 * @version 1.0
 * @since 2013.01
 * @author 이병직
 * @class
 * @memberOf COMM
 * @param {Object} constructor
 * @param {Boolean} constructor.isArray
 * @param {Boolean} [constructor.async] 동기화 여부
 * @param {String} [constructor.taskID] (시스템)SystemInfo (전체)CrpnInfo, BizrInfo,(권한에 따라)UserCrpnInfo, UserBizrInfo
 * @param {String} [constructor.grpCd] 공통코드 속성
 * @param {String} [constructor.sysMgtNo] CrpnInfo, BizrInfo 속성 
 * @param {String} [constructor.crpnSeqNo] BizrInfo 속성
 * @param {Function} [constructor.callback] 콜백
 */
COMM.comCodeToArray = function (constructor) {
    COMM._selectConstructor("comCode", constructor, COMM._STANDARD.comCodeToArray);
    
};

/**
 * @description uxAction 공통 ajax 부분
 * @version 1.0
 * @since 2013.01
 * @author 이병직
 * @event
 * @memberOf COMM
 * @param {Object} ajaxObj
 */
COMM._AJAX = function (ajaxObj) {
    var setTime = $.now()
    ,   callback = ajaxObj.callback
    ,   async = ajaxObj.async
    ,   taskId = ajaxObj.param._TASK_ID_;
    
    if ( ajaxObj.async !== false) {
        async = true;
    }
    
    var dataType = ajaxObj.param._DATATYPE_;

    $.ajax({
        type: "POST"
    ,   url:  INITIALIZE_BASE + "/uxAction"
    ,   dataType: "json"
    ,   async: async
    ,   data: ajaxObj.param
    ,   beforeSend: function() {
    		if(dataType != "MEM")
    			COMM.progressbar(setTime);
        }
    ,   success: function (data) {
    	console.log(data);
            if (COMM._rCodeCheck(data, "COMM._AJAX")) {
                return;
            } else {
                if (typeof callback === "function") {
                    if (!COMM.progressbar("isOpen")){
                        COMM.progressbar(setTime);
                    }
                    callback(data, ajaxObj.data);
                }
            }
        }
    ,   error: function(xhr,status,error){
			xhr.devMethod = "COMM._AJAX";
			
			if(dataType != "MEM")
				COMM._ajaxError(xhr,status,error);
    	}
    ,   complete: function() {
    			COMM.progressbar("off", setTime);
        }
    });
};

/**
 * @description task 요청
 * @version 1.1
 * @since 2013.03
 * @author 이병직
 * @event
 * @memberOf COMM
 * @param {String} type
 * @param {Object} constructor
 * @param {Object} standardObj
 */
COMM._byTasking = function (type, constructor, standardObj){
    var formID = constructor.formID
    ,   gridID = constructor.gridID
    ,   transForm = constructor.transForm
    ,   transGrid = constructor.transGrid
    ,   transGridStatus = constructor.transGridStatus
    ,   transMultiselect = constructor.transMultiselect
    ,   postData = {
            "_TASK_ID_": constructor.taskID
        ,   "_DATATYPE_": constructor.type
        ,   "_PGM_ID_" : constructor.pageID
        }
    ,   callback = constructor.callback
    ,   sFormID = null
    ,   sGridID = null
    ,   gridData = {}
    ,   rows = []
    ,   selarrow = null
    ,   selarrowLength = null
    ,	CRUDcolumn = null;
    
    if(postData._DATATYPE_ == "" || postData._DATATYPE_ == undefined){postData._DATATYPE_ = "COM"}
    	
    if(!COMM._declareCheck(constructor, standardObj)){
        return false;
    };
    
    if (!transForm) { transForm = true; }
    if (!transGrid) { transGrid = false; }
    if (!transMultiselect) { transMultiselect = false; }
    
    if (transGrid === true && transMultiselect === true) {
        COMM.dialog({
            type: COMM._INIT.DIALOG.typeDebug
        ,   id: "EU009"
        });
        return;
    }
    
    if (transGrid === true && transGridStatus === true) {
        COMM.dialog({
            type: COMM._INIT.DIALOG.typeDebug
        ,   id: "EU010"
        });
        return;
    }
    
    if (transForm && !$.isEmptyObject(formID)) {
        sFormID = "#" + formID;
        COMM._addPostData( postData, $(sFormID).serializeJSON() );
    }
    
    if (transGrid && !$.isEmptyObject(gridID)) {
        sGridID = "#" + gridID;
        //gridData.rows = $(sGridID).jqGrid("getRowData"); // 04.25 커스텀 currency 타입 지원을 위해 변경.
        gridData.rows  = JQGRID._getRowDataWithCustom(gridID);
        postData["_GridRows_"] = JSON.stringify(gridData);
    }
    
    if (transGridStatus && !$.isEmptyObject(gridID)) {
    	sGridID = "#" + gridID;
    	CRUDcolumn =  $(sGridID).jqGrid("getCol", JQGRID.CONSTANT.CRUD_COLMODEL_NAME, true);
    	
    	for(var x = 0, lan = CRUDcolumn.length; x < lan; x += 1) {
    		if (JQGRID.CONSTANT.CRUD_TRANS_REGX.test(CRUDcolumn[x]["value"])) {
    			$(sGridID).jqGrid("saveRow", CRUDcolumn[x]["id"]); // CUD이면 저장후 닫음.
    			// rows.push( $(sGridID).jqGrid("getRowData", CRUDcolumn[x]["id"]) ); // 04.25 커스텀 currency 타입 지원을 위해 변경.
    			rows.push( JQGRID._getRowDataWithCustom(gridID, CRUDcolumn[x]["id"], transGridStatus) ); // 04.25 커스텀 currency 타입 지원을 위해 변경.
    		}
    	}
    	
    	// 변경이 없으면 false (04.02)
    	if (rows.length === 0) {
    		return false;
    	}
    	
    	gridData.rows = rows;
    	postData["_GridRows_"] = JSON.stringify(gridData);
    }
    
    if (transMultiselect && !$.isEmptyObject(gridID)) {
        sGridID = "#" + gridID;
        selarrow = $(sGridID).jqGrid("getGridParam", "selarrrow");
        
        if (!$.isEmptyObject(selarrow)){
            selarrowLength = selarrow.length;
        
            for (var x = 0; x < selarrowLength; x += 1) {
                // rows[x] = $(sGridID).jqGrid("getRowData", selarrow[x]); // 04.25 커스텀 currency 타입 지원을 위해 변경.
                rows[x] = JQGRID._getRowDataWithCustom(gridID, selarrow[x]);
            }
            
            gridData.rows = rows;
            postData["_GridRows_"] = JSON.stringify(gridData);
        }
    }
    
    switch (type) {
    case "blind": // 비동기(기본). 동기는(옵션)
        COMM._AJAX({"param":postData, "callback":constructor.callback, "data":constructor.data, "async":constructor.async});
        break;
    case "show": // 비동기(기본). 동기는(옵션)
        COMM._AJAX( {"param":postData, 
            "data": constructor.data,
            "callback":function(data){
                //if (data.resultData) {
                //    COMM.setFormElement(formID, data.resultData);
                //}
                if ( $.isFunction(callback)) {
                    callback(data);
                }
            },
            "async":constructor.async
        });
        break;
    };
};

/**
 * @description COM 타입을 수신 후 보여줌 : 수신 data를 form 에 그림
 * @version 1.1
 * @since 2013.03
 * @author 이병직
 * @class
 * @memberOf COMM
 * @param {Object} constructor
 * @param {String} constructor.taskID 타스크ID
 * @param {String} constructor.pageID 페이지ID
 * @param {String} constructor.formID 폼ID
 * @param {String} [constructor.gridID] 그리드ID
 * @param {Boolean} [constructor.transForm=true] 폼전송
 * @param {Boolean} [constructor.transGrid=false] 그리드전송
 * @param {Boolean} [constructor.transGridStatus] CUD상태 그리드만 전송
 * @param {Boolean} [constructor.transMultiselect=false] 그리드 멀티셀렉트 전송
 * @param {Function} [constructor.callback] 콜백
 * @param {Object} [constructor.data] 콜백 인자
 * @param {Boolean} [constructor.async] 동기화 여부
 * @returns {Boolean}
 */
COMM.showByTasking = function (constructor) {
    return COMM._byTasking ("show", constructor, COMM._STANDARD.showByTasking);   
    
};
/**
 * @description COM 타입을 수신: 수신 data 를 그리지 않음
 * @version 1.1
 * @since 2013.03
 * @author 이병직
 * @class
 * @memberOf COMM
 * @param {Object} constructor
 * @param {String} constructor.taskID 타스크ID
 * @param {String} constructor.pageID 페이지ID
 * @param {String} [constructor.formID] 폼ID
 * @param {String} [constructor.gridID] 그리드ID
 * @param {Boolean} [constructor.transForm=true] 폼전송
 * @param {Boolean} [constructor.transGrid=false] 그리드전송
 * @param {Boolean} [constructor.transGridStatus] CUD상태 그리드만 전송
 * @param {Boolean} [constructor.transMultiselect=false] 그리드 멀티셀렉트 전송
 * @param {Function} [constructor.callback] 콜백
 * @param {Object} [constructor.data] 콜백 인자
 * @param {Boolean} [constructor.async] 동기화 여부
 * @returns {Boolean}
 */
COMM.blindByTasking = function (constructor) {
    return COMM._byTasking ("blind", constructor, COMM._STANDARD.blindByTasking);
    
};

/**
 * @description 페이지 이동
 * @version 1.0
 * @since 2013.01
 * @author 이병직
 * @class
 * @memberOf COMM 
 * @param {Object} pageObj
 * @param {String} pageObj.pageID 페이지 ID
 * @param {String} pageObj.url	이동할 URL
 * @param {Object|String} [pageObj.param] 넘길 파라미터
 */
COMM.nextPage = function (pageObj) {
	
    var $parent = $("#exchain").parent()
    ,   time = $.now();
    
    if(!COMM._declareCheck(pageObj, COMM._STANDARD.nextPage)){
        return false;
    };
    
    if ($parent.length === 0) {
        COMM.dialog({
            type: COMM._INIT.DIALOG.typeDebug
        ,   id: "EU011"
        });
    } else {
        COMM.progressbar(time);
        $.ajax({
            type: "POST"
        ,   url:  pageObj.url
        ,   data: pageObj.param
        ,   success: function (data) {
                $parent.html(data);
            }
        ,   error:  function(xhr,status,error){
        		xhr.devMethod = "COMM.nextPage";
        		COMM._ajaxError(xhr,status,error); 
    		}
        ,   complete: function () {
                COMM.progressbar("off", time);
            }
        });
    }
};

/**
 * @description form 요소를 리터럴객체로 반환 (값이 아닌 객체 참조)
 * @version 1.0
 * @since 2013.01
 * @author 이병직
 * @class
 * @memberOf COMM
 * @param {String} targetFormID 폼 ID
 * @returns {Object} formElement
 * @example
 * 폼 안에 input name=a가 있을 때
 * var formEle = COMM.getFormElement("formID");
 * formEle.a.val();
 */
COMM.getFormElement = function (targetFormID) {
    var formID = "#" + targetFormID
    ,   formElement = {};
    
    $(formID + " input").each(function(){
        formElement[$(this).attr("name")] = $(this);
    });
    
    $(formID +" select").each(function(){
        formElement[$(this).attr("name")] = $(this);
    });
    
    $(formID +" textarea").each(function(){
        formElement[$(this).attr("name")] = $(this);
    });
    
    return formElement;
    
};

/**
 * @description form 요소를 세팅
 * <br/>
 * v1.1 기능 추가 - "~X~" 사용시 값을 변환해서 세팅
 * @version 1.1
 * @since 2013.01
 * @author 이병직
 * @class
 * @memberOf COMM
 * @param {String} targetFormID 폼 ID
 * @param {Object} inputObj 세팅 할 객체 {input name: 값, ...} 형식
 */
COMM.setFormElement = function (targetFormID, inputObj) {
    var formID = "#" + targetFormID;

    $(formID +" input").each(function(){
        var elementName = $(this).prop("name")
        // AP에서 구분자를 자동 제외해주는 경우는 대문자
        ,   regxDate = /^(\~D\~)|^(\~d\~)/ 
        ,   regxTime = /^(\~T\~)|^(\~t\~)/ 
        ,   regxNumber= /^(\~N\~)|^(\~n\~)/
        ,   regxDateYM = /^(\~Dm\~)|^(\~dm\~)/ 
        // 브라우저에서 보여줄 때만 포멧이 적용되는 경우는 소문자
        ,   regxDateTime_B = /^\~dt\~/	
        ,   regxDateMD_B   = /^\~dd\~/	
        ,   regxTimeHM_B   = /^\~tm\~/
        ,   regxZipCode_B  = /^\~zc\~/
        ,   regxCrpnNo_B   = /^\~co\~/ 	//corporation
        ,   regxBizrNo_B   = /^\~bz\~/ 	
        // v1.1추가 자동 포맷변환 - text, textarea 적용 (04.16)
        ,   autoFormat = "";
        
        if (regxDate.test(elementName)) {
            elementName = elementName.replace(regxDate,"");
            autoFormat = "date";
        } else if (regxTime.test(elementName)) {
            elementName = elementName.replace(regxTime,"");
            autoFormat = "time";
        } else if (regxNumber.test(elementName)) {
        	elementName = elementName.replace(regxNumber,""); // 천단위 구분자 있는 숫자포맷
        	autoFormat = "number";
		} else if (regxDateYM.test(elementName)) {
			elementName = elementName.replace(regxDateYM,"");
			autoFormat = "dateYM";
	    } else if (regxDateTime_B.test(elementName)) {
	    	elementName = elementName.replace(regxDateTime_B,"");
	    	autoFormat = "dateTime";
	    } else if (regxZipCode_B.test(elementName)) {
	    	elementName = elementName.replace(regxZipCode_B,"");
	    	autoFormat = "zipCode";
		} else if (regxCrpnNo_B.test(elementName)) {
			elementName = elementName.replace(regxCrpnNo_B,"");
			autoFormat = "crpnNo";
		} else if (regxBizrNo_B.test(elementName)) {
			elementName = elementName.replace(regxBizrNo_B,"");
			autoFormat = "bizrNo";
		} else if (regxTimeHM_B.test(elementName)) {
			elementName = elementName.replace(regxTimeHM_B,"");
			autoFormat = "timeHM";
	    } else if (regxDateMD_B.test(elementName)) {
	    	elementName = elementName.replace(regxDateMD_B,"");
	    	autoFormat = "dateMD";
	    }
        
        if ($(this).prop("type") === "radio" || $(this).prop("type") === "checkbox"){
            if (inputObj.hasOwnProperty(elementName)) {
                if ($(this).val() === inputObj[elementName]){
                    $(this).prop("checked", true);  
                };
            }
        } else {	// hidden 포함
            if (inputObj.hasOwnProperty(elementName)) {
            	switch(autoFormat){
            	case "date":
            		// 2013.05.06 자리수가 아닌 포멧으로 변경
            		//$(this).prop("value", COMM._formatter_module.toDateFormat(inputObj[elementName], "date") );
            		$(this).prop("value", COMM.formatter(COMM._INIT.FORMATTER.ymd, inputObj[elementName]) );
            		break;
            	case "time":
            		// 2013.05.06 자리수가 아닌 포멧으로 변경
            		//$(this).prop("value", COMM._formatter_module.toDateFormat(inputObj[elementName], "time") );
            		$(this).prop("value", COMM.formatter(COMM._INIT.FORMATTER.hms, inputObj[elementName]) );
            		break;
            	case "number":
            		if($(this).hasClass("number_fixed2")){
            			$(this).prop("value", COMM.formatter(COMM._INIT.FORMATTER.currency2, inputObj[elementName]) );
            		} else {
            			$(this).prop("value", COMM.formatter(COMM._INIT.FORMATTER.currency, inputObj[elementName]) );
            		}
            		break;
            	case "dateYM":
            		$(this).prop("value", COMM.formatter(COMM._INIT.FORMATTER.ym, inputObj[elementName]) );
            		break;
            	case "dateTime":
            		$(this).prop("value", COMM.formatter(COMM._INIT.FORMATTER.answerTime, inputObj[elementName]) );
            		break;
            	case "timeHM":
            		$(this).prop("value", COMM.formatter(COMM._INIT.FORMATTER.hm, inputObj[elementName]) );
            		break;
            	case "zipCode":
            		$(this).prop("value", COMM.formatter(COMM._INIT.FORMATTER.zipCode, inputObj[elementName]) );
            		break;
            	case "crpnNo":
            		$(this).prop("value", COMM.formatter(COMM._INIT.FORMATTER.crpnNo, inputObj[elementName]) );
            		break;
            	case "bizrNo":
            		$(this).prop("value", COMM.formatter(COMM._INIT.FORMATTER.bizrNo, inputObj[elementName]) );
            		break;
            	case "dateMD":
            		$(this).prop("value", COMM.formatter(COMM._INIT.FORMATTER.md, inputObj[elementName]) );
            		break;
            	default:
            		//$(this).prop("value", inputObj[elementName] ); // 인쇄용으로 속성값 셋 으로 변경(02.18)
            		// HTML Entity 디코딩 추가(2016-09-08)
            		$(this).prop("value",  $("<textarea/>").html(inputObj[elementName]).text());
            		break;
            	}
            }
        }
    });
    $(formID +" select").each(function(){
        var elementName = $(this).prop("name");
        if (inputObj.hasOwnProperty(elementName)) {
            $(this).val(inputObj[elementName]);
        }
    });
    $(formID +" textarea").each(function(){
        var elementName = $(this).prop("name");
        if (inputObj.hasOwnProperty(elementName)) {
            $(this).text( (inputObj[elementName]) );
        }
    });
    
};

/**
 * @description form 요소에서 특정 키를 누르면 특정 버튼동작 함수
 * <br/>
 * (예: 엔터 누르면 특정 버튼동작)
 * @version 1.1
 * @since 2013.03
 * @author 이병직
 * @class
 * @memberOf COMM
 * @param {Object} constructor
 * @param {String} constructor.formID input을 감싼 form ID
 * @param {String} constructor.btnID 동작할 버튼 ID
 * @param {String} constructor.key 동작시킬 key 명칭
 * @param {Funtion} [constructor.beforeAction] 클릭전에 동작 action. eturn값이 true이면 btn click. false이면 정지.
 */
COMM.inputKeydownBtnclick = function (constructor) {
    var formE = COMM.getFormElement(constructor.formID)
	,   submitKey = function (base, key, btnID){
			if (base === key) {
				if (typeof constructor.beforeAction === "undefined" || constructor.beforeAction() === true ) {
					$("#"+ btnID).click();
				}
			}
		};
		
	// 키변환
	constructor.key =  COMM._INIT.KEYCODE[constructor.key];
		
    if(!COMM._declareCheck(constructor, COMM._STANDARD.inputKeydownBtnclick)){
        return false;
    };	

	for (var x in formE) {
		formE[x].keydown(function(ek){
			submitKey(parseInt(constructor.key,10), ek.keyCode, constructor.btnID);
		});
	};
	
};

/**
 * @description String/Number 에서 currency 체크
 * <br/>
 * 그리드 용 currency(스타일 적용)은 체크하지 않는다. 그리드에서 getCell 등을 사용할 땐, COMM.unformat.currency를 사용할 것 (2013.05.20)
 * @version 1.1
 * @since 2013.04.24
 * @author 이병직
 * @class
 * @memberOf COMM
 * @param {String|Number} value 체크할 값
 * @returns {Boolean}
 * @example
 * COMM.isCurrency("123,233.23"); //true
 * COMM.isCurrency("123233.23");  //false
 */
COMM.isCurrency = function (value){
	value = COMM._formatter_module.numberToString(value);

	if(value){
		return value === COMM.formatter(COMM._INIT.FORMATTER.currency, COMM.unformat.currency(value));
	} else {
		return false;
	}
	
};

/**
 * @description String/Number 에서 숫자체크
 * <br/>
 * 진짜 number타입을 구분하는 함수가 아니다. value를 숫자로 바꿀 수 있는가? 여부.
 * @version 1.1
 * @since 2013.05.07
 * @author 이병직
 * @class
 * @memberOf COMM
 * @param {String|Number} value 체크할 값
 * @returns {Boolean}
 */
COMM.isNumber = function (value) {
	if (isFinite(value) && value) { // "" 인경우를 제외
		return true;
	} else{
		return false;	
	}
	// isNaN을 이용하면 다음과 같이 처리하면 된다.
    /*if ( isNaN(value) ){ // true면 숫자가 아님 (실수포함). false면 숫자및 null등 포함. 
        return false;
    } else if (parseFloat(value,10) || parseFloat(value,10) === 0){	// 숫자일 수 있고, 값이 있을 때 (공백문자 불가)
        return true;
    } else {
    	return false;
    }*/
	
};

/**
 * @description String 에서 영문(영어,숫자,공백, 마침표, 연결자)체크 - 값이 없으면 false
 * @version 1.1
 * @since 2013.03
 * @author 이병직
 * @class
 * @memberOf COMM
 * @param {String} value 체크할 값
 * @returns {Boolean}
 */
COMM.isEnglish = function (value) {
    if ( (!/[^\w\s.,\-_]+$/.test(value)) ){
        return true;
    } else {
        return false;
    }
};

/**
 * @description String 에서 영자 체크 - 값이 없으면 false
 * @version 1.1
 * @since 2013.03
 * @author 이병직
 * @class
 * @memberOf COMM
 * @param {String} value 체크할 값
 * @returns {Boolean}
 */
COMM.isAlphabet = function (value) {
    if ( (/^[a-zA-Z]+$/.test(value)) ){
        return true;
    } else {
        return false;
    }
};

/**
 * @description monthpicker 초기값(오늘월) 세팅
 * @version 1.1
 * @since 2013.03
 * @author 이병직
 * @event
 * @memberOf COMM
 * @param {String} sPageID
 */
COMM._setMonthpicker = function( sPageID ) {
    var INIT_MONTHPICKER= COMM._INIT.MONTHPICKER
    ,   INIT_FORMAT = COMM._INIT.FORMAT
    ,   pickerID =  function() {
            var pickerID = [];
            $(INIT_MONTHPICKER, $(sPageID)).each(function(){
                pickerID.push( $(this).attr("id"));
            });
            return pickerID;
        }()
    ,   dateCalculator = null
    ,   sPickerID = []
    ,   dn = {YY:0, MM:0, DD:0};

    $(INIT_MONTHPICKER, $(sPageID)).monthpicker({
        openOnFocus: false
    }).val(new Date().getFullYear() + INIT_FORMAT.date_delim + COMM.formatNumber(new Date().getMonth() + 1, 2)); // 인풋값 오늘자로 추가
    
    if (pickerID.length === 2) {
        COMM._pairOfMonthpicker({
            prevID:pickerID[0],
            nextID:pickerID[1]
        });
        
        sPickerID[0] = "#" + pickerID[0];
        sPickerID[1] = "#" + pickerID[1];
        
        dateCalculator = function() {
            var getDn = $(sPickerID[1]).val();
            dn.YY = parseInt(getDn.slice(0,4), 10);
            dn.MM = parseInt(getDn.slice(5,7), 10);
            dn.DD = parseInt(getDn.slice(8,10), 10);
        };
        
        // 1기 예정(1월 ~ 3월) 
        $(".button_search_in07", $(sPageID)).click(function(){
            dateCalculator();
            $(sPickerID[0]).val( COMM.formatNumber(dn.YY, 4) + INIT_FORMAT.date_delim + COMM.formatNumber(1, 2) );
            $(sPickerID[1]).val( COMM.formatNumber(dn.YY, 4) + INIT_FORMAT.date_delim + COMM.formatNumber(3, 2) );
        });
        // 1기 확정(4월 ~ 6월) 
        $(".button_search_in08", $(sPageID)).click(function(){
            dateCalculator();
            $(sPickerID[0]).val( COMM.formatNumber(dn.YY, 4) + INIT_FORMAT.date_delim + COMM.formatNumber(4, 2) );
            $(sPickerID[1]).val( COMM.formatNumber(dn.YY, 4) + INIT_FORMAT.date_delim + COMM.formatNumber(6, 2) );
        });
        // 2기 예정(7월~ 9월) 
        $(".button_search_in09", $(sPageID)).click(function(){
            dateCalculator();
            $(sPickerID[0]).val( COMM.formatNumber(dn.YY, 4) + INIT_FORMAT.date_delim + COMM.formatNumber(7, 2) );
            $(sPickerID[1]).val( COMM.formatNumber(dn.YY, 4) + INIT_FORMAT.date_delim + COMM.formatNumber(9, 2) );
        });
        // 2기 확정(10월~ 12월) 
        $(".button_search_in10", $(sPageID)).click(function(){
            dateCalculator();
            $(sPickerID[0]).val( COMM.formatNumber(dn.YY, 4) + INIT_FORMAT.date_delim + COMM.formatNumber(10, 2) );
            $(sPickerID[1]).val( COMM.formatNumber(dn.YY, 4) + INIT_FORMAT.date_delim + COMM.formatNumber(12, 2) );
        });
        // 1기 (1월~ 6월)
        $(".button_search_in11", $(sPageID)).click(function(){
            dateCalculator();
            $(sPickerID[0]).val( COMM.formatNumber(dn.YY, 4) + INIT_FORMAT.date_delim + COMM.formatNumber(1, 2) );
            $(sPickerID[1]).val( COMM.formatNumber(dn.YY, 4) + INIT_FORMAT.date_delim + COMM.formatNumber(6, 2) );
        });
        // 2기 (7월~ 12월)
        $(".button_search_in12", $(sPageID)).click(function(){
            dateCalculator();
            $(sPickerID[0]).val( COMM.formatNumber(dn.YY, 4) + INIT_FORMAT.date_delim + COMM.formatNumber(7, 2) );
            $(sPickerID[1]).val( COMM.formatNumber(dn.YY, 4) + INIT_FORMAT.date_delim + COMM.formatNumber(12, 2) );
        });
    }
    
};

/**
 * @description monthpicker를 기간으로 사용시, 기간체크
 * @version 1.1
 * @since 2013.03
 * @author 이병직
 * @event
 * @memberOf COMM
 * @param {Object} constructor
 * @deprecated 현재 사용하지 않는 함수 (setMonthpicker 에서 삭제)
 */
COMM._pairOfMonthpicker = function( constructor ) {
    var prev = "#" + constructor.prevID
    ,   next = "#" + constructor.nextID
    //,   interval = constructor.interval || 1	// 기간제한 해제(03.27)
    ,   setValue = function() {
            $(prev).val(COMM.getToday("ym"));
            $(next).val(COMM.getToday("ym"));
        }
    ,   intervalCheck = function(){
            var prevValue = $(prev).val()
            ,   nextValue =  $(next).val();
            //,   prevYYYY =  prevValue.slice(0,4)
            //,   nextYYYY =  nextValue.slice(0,4);
            
            if ( prevValue > nextValue ) {
                COMM._datepickerDialog("inverse");
                setValue();
            } /*else if (nextYYYY - prevYYYY > interval || ( nextYYYY - prevYYYY === interval && prevValue.slice(5,7) <= nextValue.slice(5,7)) ){
                COMM._datepickerDialog("interval", interval);
                setValue();
            }*/
        };
  
    // 2013.05.29 개발자에게 - 내부함수이지만 객체 속성 확인을 위해 체크하고 있다.
    if(!COMM._declareCheck(constructor, COMM._STANDARD.pairOfMonthpicker)){
        return false;
    };
    
    $(prev).change(intervalCheck);
    $(next).change(intervalCheck);
  
};

/**
 * @description 포메터 내부 공통 함수
 * @version 1.1
 * @since 2013.04.11
 * @author 이병직
 * @namespace COMM._formatter_module
 * @memberOf COMM
 */
COMM._formatter_module = {

	/**
	 * @description 포매터 예약어로 처리하는 방법
	 * <br/>
	 * 통화 - currency 
	 * 		- defult 는 0
	 *  	- value 는 String
	 * @version 1.1
	 * @since 2013.04.19
	 * @author 이병직
	 * @event
	 * @memberOf COMM._formatter_module
	 * @param {String|Number} value
	 * @param {String} defaultValue
	 * @param {String} fraction_digits
	 * @param {Object} gridOptions
	 * @returns {String} 포멧적용 숫자
	 */
	currency: function(value, defaultValue, fraction_digits, gridOptions) {		//기본 값을 변경할 수 있는 변수 defaultValue를 추가(2013.05.07) - 그리드 currencyB에 적용
		var defaultNumber = "0"
		,   pointIndex		// 소수점 위치
		,	number
	    ,   fractionDigits = 2 // 소수 개수 (string)
		,   gridColModelClass = COMM._INIT.FORMATTER.gridColModelClass;
		
		value = this.numberToString(value);
		
		if(typeof defaultValue === "string") {
			number = defaultValue;
		} else {
			number = defaultNumber;
		}
		
		// 폰트 적용을 위한 클래스 추가 (2013.05.21)
		if (gridOptions) {
			// 기존 classes가 있는 경우. 
			if(gridOptions.colModel.classes && !new RegExp(gridColModelClass).test(gridOptions.colModel.classes)){
	    		gridOptions.colModel.classes = gridColModelClass + " " + gridOptions.colModel.classes;
	    	// 기존 classes가 없는 경우
	    	} else if (!new RegExp(gridColModelClass).test(gridOptions.colModel.classes)){	// classes가 있거나(현 코드 실행시 classes가 생성됨),없고 number_st가 없는 경우.
	    		gridOptions.colModel.classes = gridColModelClass;
	    	}
		}
    		
		//value가 숫자로 변환 가능하고
	    if (COMM.isNumber(value)){
	    	// 2013.05.13 소수점 버림을 위한 사전 연산
	    	pointIndex = value.indexOf(".");
	    	if(pointIndex !== -1) {
	    		if (JQGRID.CONSTANT.FRACTION_DIGITS){
	    			fractionDigits = JQGRID.CONSTANT.FRACTION_DIGITS;
	    		} else {
	    			fractionDigits = value.slice( pointIndex + 1 ).length;
	    		}
	    	} else {
	    		pointIndex = value.length; // 정수일 때 정해진 소수자리 연산을 위해서. (123 -> 123. 으로 처리)
	    	}
	    	// 2013.05.09 정해진 소수자리수가 있으면 항상 고정
	    	if(fraction_digits || fraction_digits === 0){
	    		value =  value.slice(0, pointIndex + fraction_digits + 1);
	    		number = String.format("{0:N" + fraction_digits + "}", Number(value)); // 소수점 버림.
	   	    // 실수면
	    	} else if (/[.]/.test(value)){
	    		value =  value.slice(0, pointIndex + fractionDigits + 1);
		   		number = String.format("{0:N" + fractionDigits + "}",  Number(value)); // 소수점 버림.
		   	// 정수면
		   	} else {
		   		number = String.format("{0:N0}", parseInt(value, 10));
		   	}

	    }
	    return number;
	},
	
	/**
	 * @description 날짜스트링(년월일시분초 형식)을 date 형식으로.
	 * <br/>
	 * 14자리 기준. 그 이하 자리수도 지원.
	 * @version 1.1
	 * @since 2013.04.15
	 * @author 이병직
	 * @event
	 * @memberOf COMM._formatter_module
	 * @param {String|Number} value
	 * @param {String} set
	 * @returns {Date}
	 */
	toDate: function(value, set) {
		var valueLength = 0
		,   FORMATTER = COMM._INIT.FORMATTER
		,   newDate = new Date()	// 반환하기 위한 날짜객체 선언 (오늘을 구하기 위해 사용하는 것이 아님)
		,   debugMode = function(length){
				if(DEBUG_MODE && valueLength < length) {
					COMM.dialog({
						type:COMM._INIT.DIALOG.typeDebug,
						id:"EU019",
						messageConvertParam: length
					});
					return false;
				} else {
					return true;
				}
			};
			
		value = this.numberToString(value);
		
		valueLength = value.length;
		
		// 축약된 날짜스트링인 경우
		switch (set) {
		case FORMATTER.answerTime:	// 년월일+시분초
			if ( debugMode(12) ){
				// 기준 날짜스트링인 경우 (2013.04.30 - 9자리 이상인 경우 동작하도록 변경함.)
				// 시, 분, 초 각 2자리 고정이기에 가능.
				if (valueLength >= 14) {		// ymd_hms 112001 및 11201도 -> 11시 20분 1초로 인식하는 경우. 비정상 데이터를 인지할 수 없으므로, 규약대로 2자리를 지키도록 변경. (2013.05.10)
					return new Date(parseInt(value.slice(0,4),10), parseInt(value.slice(4,6),10) -1,parseInt(value.slice(6,8),10),parseInt(value.slice(8,10),10),parseInt(value.slice(10,12),10),parseInt(value.slice(12,14),10));
				} else if (valueLength === 12){ // ymd_hm
					return new Date(parseInt(value.slice(0,4),10), parseInt(value.slice(4,6),10) -1,parseInt(value.slice(6,8),10),parseInt(value.slice(8,10),10),parseInt(value.slice(10,12),10));
				}
				// [년월일 시] 는 무의미 하므로 제외 (2013.05.10)
				/*} else if (valueLength >= 10){ // ymd_h
					return new Date(parseInt(value.slice(0,4),10), parseInt(value.slice(4,6),10) -1,parseInt(value.slice(6,8),10),parseInt(value.slice(8,10),10));
				}*/ 
			}
			break;
		// 현재 1월 1일 처럼 외자인 경우는 허용하지 않는다. (6자리가 된다.) - 아래 toDateFormat 함수 참고.
		// 파라미터 전부 사용(누락시 특정경우 (2013/02 등)시간계산 오류) 및 parseInt 를 Number로 변경 (05.29)
		case FORMATTER.ymd:	// 년월일
			if( debugMode(8) ){
				newDate.setFullYear(Number(value.slice(0,4)), Number(value.slice(4,6)) -1, Number(value.slice(6,8)) );
			}
			break;
		case FORMATTER.ym:	// 년월
			if( debugMode(6) ){
				newDate.setFullYear(Number(value.slice(0,4)), Number(value.slice(4,6)) -1, 1 );
			}
			break;
		case FORMATTER.md:	// 월일
			if( debugMode(4) ){
				newDate.setMonth( Number(value.slice(0,2)) -1, Number(value.slice(2,4)) );
			}
			break;
		case FORMATTER.hms: // 시분초
			if ( debugMode(6) ){
				newDate.setHours(Number(value.slice(0,2)), Number(value.slice(2,4)), Number(value.slice(4,6)), 0) ;
			}
			break;
		case FORMATTER.hm: // 시분
			if ( debugMode(4) ){
				newDate.setHours(Number(value.slice(0,2)), Number(value.slice(2,4)), 0, 0) ;
			}
			break;
		case FORMATTER.ms: // 분초
			if ( debugMode(4) ){
				newDate.setMinutes(Number(value.slice(0,2)), Number(value.slice(2,4)), 0) ;
			}
			break;
		default:
			return debugMode(14);
			break;
		} // switch
		return newDate;
			
	}, // toDate

	/**
	 * @description 포멧적용된 날짜 10자리를 date스트링(yyyymmdd 순서)으로 - 리터럴객체를 반환
	 * <br/>
	 * 포멧이 적용되지 않은 날짜 8자리도 지원 - (즉,2012-11-11 및 20121111 지원)
	 * <br/>
	 * ymd 및 ym, md 지원
	 * @version 1.1
	 * @since 2013.04.18
	 * @author 이병직
	 * @event
	 * @memberOf COMM._formatter_module
	 * @param {String|Number} value
	 * @returns {Object} formlessDate
	 */
	toDateString: function(value) {
		var yearScope =[]
		,   monthScope =[]
		,   dayScope =[]
		,   valueLength
		,   unformat1 = 0
		,   unformat2 = 0
		,   yOrder = _INIT_SESSION._Date_Format_Order[0]
		,   mOrder = _INIT_SESSION._Date_Format_Order[1]
		,   dOrder = _INIT_SESSION._Date_Format_Order[2] 		// md 형식에서 사용 (05.02)
		,   yOrderYM = _INIT_SESSION._Date_Format_Order_YM[0]	// YM 형식 분리 (2013.05.07)
		,   mOrderYM = _INIT_SESSION._Date_Format_Order_YM[1]	// YM 형식 분리 (2013.05.07)
		,   formlessDate = {year: "",month: "",day: "",date: ""	};
		
		value = this.numberToString(value);
		valueLength = value.length;
		
		switch(valueLength){
		// ymd 형식
		case 10:
			unformat1 = 1;
			unformat2 = 2;
		case 8:
			// 포멧이 적용되지 않은 8자리일 경우
			
			// ymd 일 때 연산
			yearScope.push(yOrder * (2 + unformat1) );
			yearScope.push(yOrder * (2 + unformat1) + 4);
			
			switch(yOrder){
			case 0:
				if ( mOrder === 1 ){
					monthScope.push(4 + unformat1);
					monthScope.push(6 + unformat1);
					dayScope.push(6 + unformat2);
					dayScope.push(8 + unformat2);
				} else {
					dayScope.push(4 + unformat1);
					dayScope.push(6 + unformat1);
					monthScope.push(8);
					monthScope.push(10);
				}
				break;
			case 1:
				if ( mOrder === 0 ){
					monthScope.push(0);
					monthScope.push(2);
					dayScope.push(6 + unformat2);
					dayScope.push(8 + unformat2);
				} else {
					dayScope.push(0);
					dayScope.push(2);
					monthScope.push(6 + unformat2);
					monthScope.push(8 + unformat2);
				}
				break;
			case 2:
				if ( mOrder === 0 ){
					monthScope.push(0);
					monthScope.push(2);
					dayScope.push(2 + unformat1);
					dayScope.push(4 + unformat1);
				} else {
					dayScope.push(0);
					dayScope.push(2);
					monthScope.push(2 + unformat1);
					monthScope.push(4 + unformat1);
				}
				break;
			}// ymd 일 때 연산
			formlessDate.year = value.slice(yearScope[0],yearScope[1]);
			formlessDate.month = value.slice(monthScope[0],monthScope[1]);
			formlessDate.day = value.slice(dayScope[0],dayScope[1]);
			formlessDate.date = formlessDate.year + formlessDate.month + formlessDate.day;
			break;	//  8,10
		// ym 형식
		case 7:
			unformat1 = 1;
		case 6:
			if (yOrderYM > mOrderYM) {
				monthScope.push(0);
				monthScope.push(2);
				yearScope.push(2 + unformat1);
				yearScope.push(6 + unformat1);
			} else {
				yearScope.push(0);
				yearScope.push(4);
				monthScope.push(4 + unformat1);
				monthScope.push(6 + unformat1);
			}
			formlessDate.year = value.slice(yearScope[0],yearScope[1]);
			formlessDate.month = value.slice(monthScope[0],monthScope[1]);
			formlessDate.date = formlessDate.year + formlessDate.month;
			break;	// 6,7
		// md 형식
		case 5:
			unformat1 = 1;
		case 4:
			if (dOrder > mOrder) {
				monthScope.push(0);
				monthScope.push(2);
				dayScope.push(2 + unformat1);
				dayScope.push(4 + unformat1);
			} else {
				dayScope.push(0);
				dayScope.push(2);
				monthScope.push(2 + unformat1);
				monthScope.push(4 + unformat1);
			}
			formlessDate.month = value.slice(monthScope[0],monthScope[1]);
			formlessDate.day = value.slice(dayScope[0],dayScope[1]);
			formlessDate.date = formlessDate.month + formlessDate.day;
			break; // 4,5
		}
		
		return formlessDate;
	},
	
	/**
	 * @description 자리수에 따라 반환. COMM.formatter 그대로 이용. input 세팅에 사용. - 현 미사용
	 * @version 1.1
	 * @since 2013.05.06
	 * @author 이병직
	 * @deprecated 삭제 예정(미사용). 
	 * <br/>라인 4000 COMM.setFormElement 에서 자리수가 아닌 포멧에 따른 사용으로 변경으로 인한 미사용.
	 * @event
	 * @memberOf COMM._formatter_module
	 * @param {String|Number} value
	 * @param {String} type
	 * @returns {String}
	 */
	toDateFormat: function(value, type) {
		var length = value.length
	    ,   INIT_FORMATTER = COMM._INIT.FORMATTER;
		
		value = this.numberToString(value);
		
		// 값이 없으면 빈 문자를 반환
		if(!value) {
			return "";
		}
		
		if (type === "date") {
			switch(length){
			case 14: // ymd_hms
			case 13: // ymd_hms
			case 12: // ymd_hm
			case 11: // ymd_hm
			case 10: // ymd_h
			case 9: // ymd_h
				return COMM.formatter(INIT_FORMATTER.answerTime, value);
				break;
			// 현재 1월 1일 처럼 외자인 경우는 허용하지 않는다. (6자리가 된다.)
			case 8: // ymd
				return COMM.formatter(INIT_FORMATTER.ymd, value);
				break;
			case 6: // ym
				return COMM.formatter(INIT_FORMATTER.ym, value);
				break;
			case 4: // md
				return COMM.formatter(INIT_FORMATTER.md, value);
				break;
			default: // 잘못된 값
		        COMM.dialog({
		            type:COMM._INIT.DIALOG.typeDebug
		        ,   id:"EU030"
		        ,   messageConvertParam:"~D~"
		        });
				break;
			}
		} else if (type === "time") {
			switch(length){
			case 6: // hms
				return COMM.formatter(INIT_FORMATTER.hms, value);
				break;
			case 4: // hm
				return COMM.formatter(INIT_FORMATTER.hm, value);
				break;
			default: // 잘못된 값
		        COMM.dialog({
		            type:COMM._INIT.DIALOG.typeDebug
		        ,   id:"EU030"
		        ,   messageConvertParam:"~T~"
		        });				
				break;
			}
		}
	},
	
	/**
	 * @description 8자리 숫자가 년월일 범위에 맞는지 체크 후 값 반환
	 * @version 1.1
	 * @since 2013.03
	 * @author 이병직
	 * @event
	 * @memberOf COMM._formatter_module
	 * @param {String|Number} value
	 * @returns {String}
	 */
	dateCheck: function(value) {
		var dateStrObj = null
		,   date = null;
		
		value = this.numberToString(value);
		
		if (value && value.length === 8) {
			// 년월일을 구분
			dateStrObj = COMM._formatter_module.toDateString(value);
			date = COMM._formatter_module.toDate(dateStrObj.date, "ymd");
			
			// 윤년등 계산이 있기에.
			if ( date.getFullYear() !== parseInt(dateStrObj.year,10) ||
					date.getMonth() + 1 !== parseInt(dateStrObj.month,10) ||
					date.getDate()  !== parseInt(dateStrObj.day,10) ) {
				return "";
			} else {
				return COMM.formatter("ymd", dateStrObj.date);
			}
		} else {
			return "";
		}
		
	},
	
	/**
	 * @description 숫자 point 가 몇개가 있는지 반환
	 * @version 1.1
	 * @since 2013.04.24
	 * @author 이병직
	 * @event
	 * @memberOf COMM._formatter_module
	 * @param {String|Number} value
	 * @returns {Number} length
	 */
	decimalPointNumber: function (value) {
		value = this.numberToString(value);
		
		var pointArray = value.match( COMM._INIT.REGEX.regexPoint );
		if (pointArray){
			return pointArray.length;
		} else {
			return 0;
		}
	},
	
	/**
	 * @description 숫자이면 스트링으로 변환하는 함수 - 없으면 ""값
	 * @version 1.1
	 * @since 2013.04.24
	 * @author 이병직
	 * @event
	 * @memberOf COMM._formatter_module
	 * @param value {String|Number}
	 * @returns value {String}
	 */
	numberToString: function (value){
		switch (typeof value){
		case "string":
			break;
		case "number":
			value = value.toString();
			break;
		default:
			value = "";
			break;
		}
		return value;
	},
	
	/**
	 * @description 타입과 값을 가지고 언포멧. 
	 * <br/>
	 * getRowData와 짝을 이루는 함수
	 * <br/>
	 * 주의!!!! COMM.unformat과는 달리 단순 구분자만 없앤다. !!!! (혼선방지를 위한 명칭변경)
	 * @version 1.1
	 * @since 2013.05.02
	 * @author 이병직
	 * @event
	 * @memberOf COMM._formatter_module
	 * @param {String} objectType
	 * @param {String} value
	 * @returns {String} value
	 */
	unformatOnlySeparator: function( objectType, value){
		var timeCh,dateCh,commCh,result=value;
		
		// 사실 format_name 과 같은 속성을 하나 더 부과하는 것이 성능에는 좋다. (다음 버전에 적용. ${format.currency} 에 전부 담아 둔다던가...)
		if( objectType === "function" && value &&!/img/.test(value) && !/\D{2}/.test(value)){ // 사용자정의 포메터는 크게 img태그, currency, text 이므로 (문자하나는 공백일수 있으므로 연속 2개로 제한)
			// currency 인 경우
			if (COMM._INIT.REGEX.testThousand.test(value) ||COMM._INIT.REGEX.testPoint.test(value)){	// test는 빠른 비교함수 이므로. 한 번더 필터하고.
				if (COMM.isCurrency(value)) {
					result = COMM.unformat.currency(value);
					return result;
				}
			}
			
			timeCh = COMM._INIT.REGEX.testTime.test(value);	// []?[00-00] 인경우
			dateCh = COMM._INIT.REGEX.testDate.test(value);	// [00-00] 인경우
			commCh = COMM._INIT.REGEX.testComm.test(value); // [0-0] 인 경우
			
			// 날짜나 시간인 경우. 날짜나 시간 구분자가 통화와 동일한 경우를 고려함.
			// ( 문자가 연속1개 이하이고, function 타입인 경우 )
			if (dateCh && timeCh){
				// dt 타입은 처리할 필요가 없다. ap로 갈 일이 없기 때문. (2013.05.09)
				result = value;
			} else if (dateCh){
				result = value.replace(COMM._INIT.REGEX.regexDate, "");
			} else if (timeCh){
				result =value.replace(COMM._INIT.REGEX.regexTime, "");
			} else if (commCh){
				result = value.replace(COMM._INIT.REGEX.regexComm, "");
			}
			// 04.26 주의사항!
			// 주의! 기타 포메터와 구분자가 겹치는 경우가 있을 수 있음. (우편번호등의 - )
			// 설정에 따라 겹치던 포메터가 겹치지 않는 경우가 발생한다. (: 와 - 였다가 - 와 - 가 되는 경우) 
			// standard 로 변환은 하고 있으나 특수한 경우 문제소지가 있을 수 있다.
			// 그리드에서 우편번호등을 저장하는 경우는 아직 없으므로 로직에 문제는 없지만, format_name으로 하는 경우가 가장 정확하다.
			// 포매터를 사용하지 않은 경우엔 문제가 없음. (function체크)
		} // 문자가 연속 한 개인 경우
		
		return result;
	}
	
};


/**
 * @description 언포매터 - 포멧적용된 날짜,시간,통화 문자열을 미적용 문자열로
 * <br/>
 *  주의! 원data 값으로 되돌린다. (yyyymmdd 와 같이)
 * @version 1.1
 * @since 2013.04.30
 * @author 이병직
 * @class
 * @memberOf COMM
 */
COMM.unformat = {
	/**
	 * toDateString 참고
	 * @function
	 * @version 1.1
	 * @since 2013.05.02
	 * @author 이병직
	 * @param {String} value date포멧 적용된 값 (ymd, ym, md 형식 지원)
	 * @returns {String} unformatDate
	 */
	date: function(value){ 
		return unformatDate = COMM._formatter_module.toDateString(value).date;  // date는 ymd 자리가 변동되기 때문.(04.30)
		//return value.replace(COMM._INIT.REGEX.regexDate, "");
	},
	
	/**
	 * @function
	 * @version 1.1
	 * @since 2013.05.02
	 * @author 이병직
	 * @param {String} value time포멧 적용된 값
	 * @returns {String} replace
	 */
	time: function(value){
		return value.replace(COMM._INIT.REGEX.regexTime, "");
	},
	
	/**
	 * @function
	 * @version 1.1
	 * @since 2013.05.02
	 * @author 이병직
	 * @param {String} value currency포멧 적용된 값
	 * @returns {String} replace
	 */
	currency: function(value){
		return value.replace(COMM._INIT.REGEX.regexThousand, "").replace(COMM._INIT.REGEX.regexPoint,".");
	},
	
	/**
	 * @function
	 * @version 1.1
	 * @since 2013.05.02
	 * @author 이병직
	 * @param {String} value 일반포멧(-연결자) 적용된 값
	 * @returns {String} replace
	 */
	standard: function(value){
		return value.replace(COMM._INIT.REGEX.regexComm, "");
	}
	
};

/**
 * @description 포매터 - 공백문자나 값이 없을땐 공백문자를 반환
 * @version 1.1
 * @since 2013.04.17
 * @author 이병직
 * @class
 * @memberOf COMM
 * @param {String} formatType answerTime, ymd, ym, md, hms, hm, zipCode, currency, currency2, crpnNo, bizrNo 
 * @param {String} value 변환할 값
 * @returns {String} value
 */
COMM.formatter = function (formatType, value) {
    var result =""
    ,   number =""
    ,   suffix =""
    ,   valueLength
    ,   INIT_FORMATTER = COMM._INIT.FORMATTER;
    
    if(!value){
    // 공백문자나 값이 없을땐 공백문자를 반환하도록 변경 (2013.04.17)
/*        COMM.dialog({
            type:COMM._INIT.DIALOG.typeDebug
        ,   id:"EU012"
        ,   messageConvertParam:formatType
        });
        return false;*/
    	return "";
    } else {
    	valueLength = value.length;
    }
    
    switch (formatType) {
    case INIT_FORMATTER.answerTime:
    	if (valueLength >=13) {
    		result = COMM._formatter_module.toDate(value, INIT_FORMATTER.answerTime).format("D"); 
    	} else if (valueLength >=11) {
    		result = COMM._formatter_module.toDate(value, INIT_FORMATTER.answerTime).format("k"); 
    	} else if (valueLength >= 9) {
    		result = COMM._formatter_module.toDate(value, INIT_FORMATTER.answerTime).format("d") + " " + value.slice(8,10); 
    	}
        break;
    case INIT_FORMATTER.ymd:
        result = COMM._formatter_module.toDate(value, INIT_FORMATTER.ymd).format("d");
        break;
    case INIT_FORMATTER.ym:
    	result = COMM._formatter_module.toDate(value, INIT_FORMATTER.ym).format("Y"); 
    	break;
    case INIT_FORMATTER.md:
    	result = COMM._formatter_module.toDate(value, INIT_FORMATTER.md).format("M"); 
    	break;
    case INIT_FORMATTER.hms:
    	result = COMM._formatter_module.toDate(value, INIT_FORMATTER.hms).format("T"); 
    	break;
    case INIT_FORMATTER.hm:
        result = COMM._formatter_module.toDate(value, INIT_FORMATTER.hm).format("t"); 
        break;
    case INIT_FORMATTER.zipCode:
        result = String.format("{0:###-###}", parseInt(value.slice(0,6), 10) ); 
        break;        
    case INIT_FORMATTER.currency:
        result =  COMM._formatter_module.currency(value);
        break;
    case INIT_FORMATTER.currency2:
    	// 그리드에 사용하지 않는 input 용 curreny2 (스타일 없음 2013.05.20)
    	result =  COMM._formatter_module.currency(value, "0.00", 2); 
    	break;
    case INIT_FORMATTER.crpnNo:
        number = parseInt(value.slice(0,13),10);
        suffix = value.slice(13);
        result = String.format( "{0:######-#######}", number ) + suffix;
        break;
    case INIT_FORMATTER.bizrNo:
    	number = parseInt(value.slice(0,10),10);
    	suffix = value.slice(10);
    	result = String.format( "{0:###-##-#####}", number ) + suffix;
        break;        
    default:
        COMM.dialog({
            type:COMM._INIT.DIALOG.typeDebug
        ,   id:"EU013"
        ,   detail: function(){
        		var str = "type list:<br/>";
        		for(var x in INIT_FORMATTER) {
        			str += x + "<br/>";
        		}
        		return str;
        	}()
        });
        break;
    }
    
    return result;
};

/**
 * @description 바이트 체크
 * @version 1.0
 * @since 2013.01
 * @author 변준호
 * @class
 * @memberOf COMM
 * @param {String} str 체크할 문자열
 * @returns {Number} resultSize
 */
COMM.getByte = function(str) {
	var resultSize = 0;
	
	if (str == null) {
		return 0;
	}
	
	for (var i=0; i<str.length; i++) {
		var c = escape(str.charAt(i));
		if (c.legnth == 1) {
			resultSize++;
		} else if (c.indexOf("%u") != -1) {
			resultSize += 2;
		} else {
			resultSize++;
		}
	}
	
	return resultSize;
};

/**
 * @description 이체그리드용 함수 - 소수점(point) 불가
 * <br/>
 * 그리드 옵션안에서 사용하는 함수 (see 참조)
 * @see http://www.trirand.com
 * @version 1.0
 * @since 2013.01
 * @author 이병직
 * @class
 * @memberOf COMM
 * @param {Object} event keypress 이벤트 
 */
COMM.gridNumberCheck = function(event) {
    event.data = {type:COMM._INIT.FORMAT.typeGridNumber};
    COMM.keyEventPrevent.keypress(event);
};

/**
 * @description 이체그리드용 함수 - 소수점(potin) 가능
 * <br/>
 * 그리드 옵션안에서 사용하는 함수
 * @see jqGrid 문서참조 http://www.trirand.com
 * @version 1.1
 * @since 2013.03
 * @author 이병직
 * @class
 * @memberOf COMM
 * @param {Object} event keypress 이벤트 
 */
COMM.gridNumberCheck2 = function(event) {
    event.data = {type:COMM._INIT.FORMAT.typeGridNumber2};
    COMM.keyEventPrevent.keypress(event);
};

/**
 * @description 이체그리드용 함수 - 소수점(potin) 가능 keyup 부분.
 * <br/>
 * 그리드 옵션안에서 사용하는 함수
 * @see jqGrid 문서참조 http://www.trirand.com
 * @version 1.1
 * @since 2013.05.16
 * @author 이병직
 * @class
 * @memberOf COMM
 * @param {Object} event keyup 이벤트 
 */
COMM.gridNumberCheck2Up = function(event) {
	var text = $(event.target).val()
	,   number = text.split(_INIT_SESSION._Point);
	
	if (number[1] && number[1].length > 2) {
		$(event.target).val(text.slice(0,text.length-1));
	}
	
};

/**
 * @description 보고서 탭 (그리드&차트) 함수
 * @version 1.1
 * @since 2013.03
 * @author 이병직
 * @event
 * @memberOf COMM
 * @deprecated 현재 적용 페이지가 없음. 삭제예정.
 * @param {String} sPageID
 */
COMM._briefTab = function(sPageID) {
	var BRIEF = COMM._INIT.BRIEF
	,   gridBtn = sPageID + BRIEF.gridBtn
	,   plotBtn = sPageID + BRIEF.plotBtn
	,   gridView = sPageID + BRIEF.gridView
	,   plotView = sPageID + BRIEF.plotView;
	
	$(sPageID).on("click", gridBtn, function(event){
		event.preventDefault();
		if(! $(gridBtn + " >h2").hasClass(BRIEF.selectBtnClass) ){
			$(gridBtn + " >h2").attr("class", BRIEF.selectBtnClass);
			$(plotBtn + " >h2").removeClass(BRIEF.selectBtnClass);
			
			$(plotView).hide();
			$(gridView).show();
		}
	});
	
	$(sPageID).on("click", plotBtn, function(event){
		event.preventDefault();
		if(! $(plotBtn + " >h2").hasClass(BRIEF.selectBtnClass) ){
			$(plotBtn + " >h2").attr("class", BRIEF.selectBtnClass);
			$(gridBtn + " >h2").removeClass(BRIEF.selectBtnClass);
			
			$(gridView).hide();
			$(plotView).show();
		}
	});
};

/**
 * @description plot 화면이 열려있을 때 create를 호출
 * @version 1.1
 * @since 2013.03
 * @author 이병직
 * @event
 * @memberOf COMM
 * @deprecated 1.1에서는 차트를 사용하지 않는다.
 * @param {String} sPageID
 * @param {Object[]} chartData
 */
COMM._drawPrepare = function(chartData, sPageID) {
	var BRIEF = COMM._INIT.BRIEF
	,   plotBtn = sPageID + BRIEF.plotBtn
	,   plotView = sPageID + BRIEF.plotView;
    
    if ($(plotView).is(":visible")) {
        JQPLOT.draw(chartData, sPageID);
    } else {
        $(plotBtn).one("click", function(){
            $(plotView).show();
            JQPLOT.draw(chartData, sPageID.replace("#",""));
        });
    }
};

/**
 * @description active 중인 탭을 닫음. (tab 1.1 사용시 동작)
 * @version 1.1
 * @since 2013.09
 * @author 이병직
 */
COMM.activeTabClose = function(){
	$(".ui-tabs-active.ui-state-active span:first").click();
};



/////////////////////////////////////////////////////////////////////
//////// WARNING 사용금지 및 대체된 함수 안내창 (1.0 -> 1.1) ////////
/////////////////////////////////////////////////////////////////////

/*
COMM.updateThroughAjax = function () {
	COMM.dialog({
		type:COMM._INIT.DIALOG.typeDebug
	,   id:"명칭변경"
	,   message: "COMM.updateThroughAjax 명칭 변경<br/>-> COMM.blindByTasking"
	});
};

COMM.selectThroughAjax = function () {
	COMM.dialog({
		type:COMM._INIT.DIALOG.typeDebug
	,   id:"명칭변경"
	,   message: "COMM.selectThroughAjax 명칭 변경<br/>-> COMM.showByTasking"
	});
};
*/

// 팝업 관련. CUST로 변경
/*
COMM.global._openUserPopupCallback = function () {
	COMM.dialog({
		type:COMM._INIT.DIALOG.typeDebug
	,   id:"명칭변경"
	,   message: "COMM.global._openUserPopupCallback 명칭 변경<br/>-> CUST.global.openUserPopupCallback"
	});
};

COMM.openUserPopup = function () {
	COMM.dialog({
		type:COMM._INIT.DIALOG.typeDebug
	,   id:"명칭변경"
	,   message: "COMM.openUserPopup 명칭 변경<br/>-> CUST.openUserPopup"
	});
};

COMM.openUserPwPopup= function () {
	COMM.dialog({
		type:COMM._INIT.DIALOG.typeDebug
	,   id:"명칭변경"
	,   message: "COMM.openUserPwPopup 명칭 변경<br/>-> CUST.openUserPwPopup"
	});
};
COMM.openUserExecPopup= function () {
	COMM.dialog({
		type:COMM._INIT.DIALOG.typeDebug
		,   id:"명칭변경"
			,   message: "COMM.openUserExecPopup 명칭 변경<br/>-> CUST.openUserExecPopup"
	});
};
COMM.openMyAcntPopup= function () {
	COMM.dialog({
		type:COMM._INIT.DIALOG.typeDebug
		,   id:"명칭변경"
			,   message: "COMM.openMyAcntPopup 명칭 변경<br/>-> CUST.openMyAcntPopup"
	});
};
COMM.openAdminAcntPopup= function () {
	COMM.dialog({
		type:COMM._INIT.DIALOG.typeDebug
		,   id:"명칭변경"
			,   message: "COMM.openAdminAcntPopup 명칭 변경<br/>-> CUST.openAdminAcntPopup"
	});
};
COMM.openExcelGridPopup= function () {
	COMM.dialog({
		type:COMM._INIT.DIALOG.typeDebug
		,   id:"명칭변경"
			,   message: "COMM.openExcelGridPopup 명칭 변경<br/>-> CUST.openExcelGridPopup"
	});
};
COMM.byteCheck= function () {
	COMM.dialog({
		type:COMM._INIT.DIALOG.typeDebug
		,   id:"명칭변경"
			,   message: "COMM.byteCheck 명칭 변경<br/>-> CUST.byteCheck"
	});
};
*/
