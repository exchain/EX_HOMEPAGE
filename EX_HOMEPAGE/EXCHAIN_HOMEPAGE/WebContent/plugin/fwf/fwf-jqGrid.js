/* =============================================================================
 * System       : F-CMS
 * FileName     : fwf-jqGrid.js
 * Version      : 1.1
 * Description  : 개발자 공통사용을 위한 jqGrid 모듈화
 * Author       : 이 병 직
 * Date         : 2013.03.29
 * -----------------------------------------------------------------------------
 * Modify Date  : 
 * -----------------------------------------------------------------------------
 * Etc          : 외부에서 접근 가능 script
 * -----------------------------------------------------------------------------
 * version 1.0 	: 2012.11.24
 * version 1.1  : 2013.03.29 
 * 				: JQGRID.updateThroughAjax 명칭 변경 -> JQGRID.blindByTasking
 * 				: JQGRID.selectThroughAjax 명칭 변경 -> JQGRID.showByTasking
 * 				: JQGRID._throughtAjax 명칭 변경 -> JQGRID._byTasking
 * 
 * 				: JQGRID.getRowStatus 추가
 * 				: JQGRID.setRowStatus 추가
 * 				: JQGRID.changeEditable 추가
 * 
 * 				: 2013.04.25
 * 				: JQGRID.getRowData 추가 (기존 html 이 아닌 text 기준으로 값을 가져옴.)
 * 				: 또한 커스텀 currency 를 unformat 하는 기능이 있기에, 기존 grid 제공 포멧은 사용하면 안된다.
 * 
 * 				: JQGRID.dateFormatter 폐기 -> JQGRUD.formatter로 통합
 * -----------------------------------------------------------------------------
 * 참고사항
 * 그리드 특정컬럼 스타일을 조정하고 싶을 때 (두 줄 표시하고 싶을 때) 2013.05.15 
 * $("#jqgh_US1020010grid_bok_fst_noti_exch_rt").css("top","2px").css("height", $("#jqgh_US1020010grid_bok_fst_noti_exch_rt").height() *2);
 * -----------------------------------------------------------------------------
 * Copyrights 2013 by Finger. All rights reserved. ~ by Finger.
 * =============================================================================
 */

//// FWF 추가 옵션 목록 ////
/* ---- 크기 조절 ----
 * autoResizeHeight     boolean
 * autoResizeWidth      boolean
 * fullscreenBotton     boolean
 * popupMode            boolean
 * autoheightParent     string(DOM)
 * autowidthParent      string(DOM)
 * ---- 화면 표시 ----
 * isPager              boolean
 * colSum               object
 * lastrowToFooter      boolean
 * setFrozenColumns     boolean
 * crudHidden			boolean
 * ---- 이벤트----
 * onSelectAllCallback      function
 * loadCompleteCallback     function
 * afterEditCellCallback    function
 * afterRestoreCellCallback function
 * beforeSaveCellCallback   function
 * afterSaveCellCallback    function
 * ---- 셀 편집 ----
 * editCelliRow         number
 * editCelliCol         number
 * ---- 포메터 설정 ----
 * formatterCurrency---- object
 * ---- 페이징처리 ----
 * isPaging				boolean
 * ---- 수신한 rowNum ----
 * recieveRowNum		Number
 * ---- jqGrid.min Fix ---
 * ajaxGridOptions.beforeSuccess	- 그리드 내부 ajax Success 판정시 가장먼저 동작
 * afterAddJSONDataLocal			- 그리드 내부 populate 에서 local/clientside addJSONData 동작 직후 동작
*/

/**
 * @description <h3>공통 javaScript API (문서화)</h3>
 * <br/>
 * COMM 객체는 finger-wf의 공통함수(메소드)를 속성으로 가지고 있다.
 * <br/>--------<br/>
 * 자바스크립트는 자바와 언어구조가 다르므로, 편의상 다음과 같이 분류한다.
 * <dl>
 * <dt><strong>Classes</strong>:</dt><dd>자바스크립트 함수(메소드) - JQGRID 속성에 선언된 함수.</dd>
 * <dt><strong>Event</strong>:</dt><dd>자바스크립트 함수(메소드) - JQGRID 속성에 선언된 함수 중 내부사용 함수.</dd>
 * <dd>(Classes 와 달리 개발자에게 기능을 보장하지 않는다.)</dd>
 * <dt><strong>Namespace</strong>:</dt><dd>자바스크립트 객체 - JQGRID 속성에 선언된 객체(속성 집합)</dd>
 * <dt><strong>Members</strong>:</dt><dd>해당 객체가 가지고 있는 속성 중 함수 이외의 것.</dd>
 * <dt><strong>Method</strong>:</dt><dd>해당 객체가 가지고 있는 속성 중 함수.</dd>
 * </dl>
 * @version 1.1
 * @since 2013.05.27
 * @author 이병직
 * @namespace JQGRID
 */
var JQGRID = JQGRID || {};

/**
 * @description fwf-jqGrid 버전
 * @version 1.1
 * @since 2013.03
 * @author 이병직
 * @memberOf JQGRID
 * @type {String}
 */
JQGRID.version = "1.1";

if (window.console) {
	console.info("fwf-jqGrid.js Version: ", JQGRID.version);
}

/**
 * @description 사용할 상수값을 선언.
 * @version 1.1
 * @since 2013.05
 * @author 이병직
 * @constant
 * @memberOf JQGRID
 * @type {Object}
 */
JQGRID.CONSTANT = {
        DIALOG:"#error_wrapper"         // fwf-common.js 필요
    ,   GRID_PARENT:"#center_content_sub"
        
        ///////////////////////////////////////////////////////////////////////////////////////
        //// style sheet에서 navi 및 footer 의 height가 바뀌면, 다음 상수도 변경해야 한다. ////
    ,   HEIGHT_ADDITION_FOOTER: 48      // 높이 보정값 ( navi(24) + footer(22) + 여유3px)
    ,   HEIGHT_ADDITION: 27             // 푸터시 보정값 (푸터는 22)
    //,   HEIGHT_ADDITION_GROUP: 15     // 그룹시 보정값 -> id로 측정하면 필요가 없다. 02.26
        ///////////////////////////////////////////////////////////////////////////////////////
    
    ,   GRID_HEIGHT_MIN: 60
    ,   GRID_WIDTH_MIN: 700             // jsp의 상단 최소값과 일치해야 함
    ,   FULLSCREEN: ".btn-fullscreen"
    ,   FULLCONTENT: "#center_content_sub"
    ,   LAYOUT_TOGGLER: ".ui-layout-toggler"
    ,   SLOW: 200
    ,   FAST: 50
    ,   FORM_NAME: ".search_in"
    ,   AJAX_URL: INITIALIZE_BASE + "/uxAction"
//    ,   GRID_TITLE_HTML: '<img src="'+ INITIALIZE_BASE_IMG +'/search_time.jpg" alt="조회일시"/><span class="bold">'
    ,   GRID_TITLE_HTML: _UCS.COMMON3001 +' <span class="">'
    ,   GRID_TITLE_HTML_END: '</span>'  // 그리드 조회시간
    ,   GRID_TITLE: ".grid_title"
    ,   EMPTY_ROWS: "<div class='emptyrows ui-default-state  ui-state-active' styl='display: block'>"+ _UCS.COMMON3002 +"</div>" // jquery 객체에서 DOM으로 수정(2.4)
    // 확인, 미확인 class
    ,   CONFIRMED: '<span class="ui-icon fwf-icon-fileopen"></span>'
    ,   UNCONFIRMED: '<span class="ui-icon fwf-icon-fileclose"></span>'
    // 상태 클래스
    ,   STATE_PROG: '<span class="float-l ui-icon fwf-icon-pay02"></span>'
    ,   STATE_WAIT: '<span class="float-l ui-icon fwf-icon-pay03"></span>'
    ,   STATE_BACK: '<span class="float-l ui-icon fwf-icon-pay04"></span>'
    ,   STATE_COMP: '<span class="float-l ui-icon fwf-icon-pay05"></span>'
    // 1.1 추가 CRUD
    ,   ROW_STAT_COL_NAME: "---"
    ,   ROW_STAT_COL_MODEL: "_gridStatus"
    ,   ROW_STAT_COL_REGX:/[IRUD]/
    ,   ROW_STAT_COL_TRANS_REGX:/[IUD]/
    ,   ROW_STAT_COL_WIDTH: 30
    // formatter 정의(globalVar.jsp 에서 선언)
	,   FRACTION_DIGITS: _INIT_SESSION._Fraction_Digits

    // 증감액 class
    ,   INCREASE: '<span class="float-l ui-icon fwf-icon-up3"></span>'
    ,   DECREASE: '<span class="float-l ui-icon fwf-icon-down3"></span>'
    	
    // 표준 구분자. (우편번호, 사업자 번호 등)
    ,   STANDARD_SEPARATOR: _INIT_SESSION._STANDARD_SEPARATOR
    // 다량의 record 경고
    ,   RECORD_WARNING: 1000

};

if (DEBUG_MODE) {
	/**
	 * @description JQGRID 표준 함수 선언
	 * @version 1.1
	 * @since 2013.05
	 * @author 이병직
	 * @namespace JQGRID._STANDARD
	 * @memberOf JQGRID
	 */
  JQGRID._STANDARD = {
	/**
	 * @constant showByTasking
	 * @memberOf JQGRID._STANDARD
	 */  
        showByTasking: {
            "_function_name_":"JQGRID.showByTasking"
        ,   taskID: COMM._INIT.DECLARE_CHECK.require + "타스크 ID "
        ,   pageID: COMM._INIT.DECLARE_CHECK.require + "페이지ID //서버측 메세지 처리에 사용"
        ,   gridID: COMM._INIT.DECLARE_CHECK.require + "그리드 ID"
        ,   formID: "폼 ID"
        ,   transForm: "폼 전송여부. 기본값은 true" 
        ,   transGrid: "그리드 전송여부. 기본값은 false" 
        ,   transGridStatus: "CUD 상태 그리드만 전송. transGrid와 같이 사용 못함. return 값이 false이면 변경된 값이 없다는 의미."
        ,   transMultiselect: "그리드 멀리셀렉트 전송여부. 기본값은 false. transGrid와 같이 사용 못함"
        ,   callback: "콜백함수"
        ,   data: "콜백에 사용할 데이타"
        ,   colSum: "colSumModel"
        }

	/**
	 * @constant blindByTasking
	 * @memberOf JQGRID._STANDARD
	 */  
    ,   blindByTasking: {
            "_function_name_": "JQGRID.blindByTasking"
        ,   taskID: COMM._INIT.DECLARE_CHECK.require + "타스크 ID "
        ,   pageID: COMM._INIT.DECLARE_CHECK.require + "페이지ID //서버측 메세지 처리에 사용"
        ,   formID: "폼 아이디."
        ,   gridID: "그리드ID" 
        ,   transForm: "폼 전송여부. 기본값은 true" 
        ,   transGrid: "그리드 전송여부. 기본값은 false" 
        ,   transGridStatus: "CUD 상태 그리드만 전송. transGrid와 같이 사용 못함. return 값이 false이면 변경된 값이 없다는 의미."
        ,   transMultiselect: "그리드 멀티셀렉트 전송여부. 기본값은 false. transGrid와 같이 사용 못함"
        ,   callback: "콜백함수"
        ,   data: "콜백에 사용할 데이타"
        ,   async: "동기화 여부"
        }

	/**
	 * @constant briefByTasking
	 * @memberOf JQGRID._STANDARD
	 */
    ,   briefByTasking: {
  	  		"_function_name_":"JQGRID.briefByTasking"
  		,   taskID: COMM._INIT.DECLARE_CHECK.require + "타스크 ID "
  		,   pageID: COMM._INIT.DECLARE_CHECK.require + "페이지ID //서버측 메세지 처리에 사용"
  		,   gridID: COMM._INIT.DECLARE_CHECK.require + "그리드 ID"
  		,   isBrief: COMM._INIT.DECLARE_CHECK.require + "항상 true 판정"
  		,   formID: "폼 ID"
  		,   transForm: "폼 전송여부. 기본값은 true" 
  		,   transGrid: "그리드 전송여부. 기본값은 false" 
  		,   transGridStatus: "CUD 상태 그리드만 전송. transGrid와 같이 사용 못함. return 값이 false이면 변경된 값이 없다는 의미."
  		,   transMultiselect: "그리드 멀티셀렉트 전송여부. 기본값은 false. transGrid와 같이 사용 못함"
  		,   callback: "콜백함수"
  		,   data: "콜백에 사용할 데이타"
  		,   colSum: "colSumModel"
    }

	/**
	 * @constant addRow
	 * @memberOf JQGRID._STANDARD
	 */
    ,   addRow: {
            "_function_name_":"JQGRID.addRow"
        ,   gridID: COMM._INIT.DECLARE_CHECK.require + "그리드 ID"
        ,   rowData: COMM._INIT.DECLARE_CHECK.require + "추가할 row data"
        }

	/**
	 * @constant editRow
	 * @memberOf JQGRID._STANDARD
	 */
    ,   editRow: {
    	"_function_name_":"JQGRID.editRow"
    		,   gridID: COMM._INIT.DECLARE_CHECK.require + "그리드 ID"
    		,   rowID: COMM._INIT.DECLARE_CHECK.require + "추가할 rowID"
    		,   changeEditableCRUD: "CRUD(row status)를 이용한 JQGRID.changEditable"
    		,   changeEditable: "JQGRID.changEditable + editrow"
    }
    
	/**
	 * @constant deleteRow
	 * @memberOf JQGRID._STANDARD
	 */
    ,   deleteRow: {
            "_function_name_":"JQGRID.deleteRow"
        ,   gridID: COMM._INIT.DECLARE_CHECK.require + "그리드 ID"
        ,   rowID: COMM._INIT.DECLARE_CHECK.require + "삭제할 rowID"
        }
    
	/**
	 * @constant addJSONData
	 * @memberOf JQGRID._STANDARD
	 */
    ,   addJSONData: {
            "_function_name_":"JQGRID.addJSONData"
            ,   gridID: COMM._INIT.DECLARE_CHECK.require + "그리드 ID"
            ,   JSONData: COMM._INIT.DECLARE_CHECK.require + "그리드에 사용할 JSON Data" 
            ,   pageURL: COMM._INIT.DECLARE_CHECK.require + "호출한 페이지 URL" 
        }
    
	/**
	 * @constant getRowBtn
	 * @memberOf JQGRID._STANDARD
	 */
    ,   getRowBtn: {
            "_function_name_":"JQGRID.multishowByTasking"
        ,   gridID:  COMM._INIT.DECLARE_CHECK.require + "그리드 ID"
        ,   btnAction:  COMM._INIT.DECLARE_CHECK.require + "버튼 action 함수"
        ,   btnClass:  COMM._INIT.DECLARE_CHECK.require + "버튼 Class Name"
        }
    
	/**
	 * @constant insideSelect
	 * @memberOf JQGRID._STANDARD
	 */
    ,   insideSelect: {
            "_function_name_": "JQGRID.insideSelect"
        ,   selectValueMapping: COMM._INIT.DECLARE_CHECK.require + "셀렉트를 그릴 column 이름:[수신 data 중 들어갈 값], ...반복 "
        ,   selectTextMapping: COMM._INIT.DECLARE_CHECK.require + "셀렉트를 그릴 column 이름:[수신 data 중 들어갈 값], ...반복"
        ,   gridID: COMM._INIT.DECLARE_CHECK.require + "그리드 ID"
        ,   onChangeSetValueMapping: "셀렉트 선택시 grid cell value mapping --- 셀렉트를 그린 column 이름: 바꿀 대상: [바꿀 값]" 
        ,   onChangeDelValueMapping: "셀렉트 선택시 타겟 셀렉트를 변경(option삭제)"
        ,   onChangeSameTextCheck: "셀렉트 선택시 중복 text 체크" 
        ,   onChangeSameTextMessage: "셀렉트 선택시 중복 text 발견시 메세지" 
        ,   onChangeSameTextID: "셀렉트 선택시 중복 text 발견시 메세지 ID" 
        ,   choiceText: "선택 option 텍스트"
        ,   choiceValue: "선택 option 벨류"
        ,   onChangeEditable: "선택시 에디트 가능"
        ,   onChangeEvent: "선택시 개발자 이벤트 정의"
        }
    
	/**
	 * @constant changeEditable
	 * @memberOf JQGRID._STANDARD
	 */
    // 기준 colname의 값에 따라 타겟 colname의 editable 여부 변경 
    // 열 전체(COL 전체)가 바뀌기 때문에 그때 그때 체크해야 한다. v1.1 추가
    ,   changeEditable: {
        	"_function_name_": "JQGRID.changeEditable"
        ,   gridID: COMM._INIT.DECLARE_CHECK.require + "그리드 ID"
        ,   rowID: COMM._INIT.DECLARE_CHECK.require + "변경할 row의 ID"
        ,   baseColname: COMM._INIT.DECLARE_CHECK.require + "기준이 되는 colname"
        ,   baseValue: COMM._INIT.DECLARE_CHECK.require + "변경기준이 되는 값 (string 또는 정규표현식 가능)" 
        ,   target: "변경할 컬럼명과 변경할 값. {컬럼명:false, 컬럼명:true, ... } 형식" 
    	}
    
	/**
	 * @constant checkedToColumn
	 * @memberOf JQGRID._STANDARD
	 */
    //  multiselect checked 를 column 에 Y/N 으로 세팅
    ,   checkedToColumn: {
            "_function_name_": "JQGRID.checkedToColumn"
        ,   gridID: COMM._INIT.DECLARE_CHECK.require + "그리드 ID"
        ,   colName: COMM._INIT.DECLARE_CHECK.require + "변환할 colName"
    }
    
	/**
	 * @constant editCellClosed
	 * @memberOf JQGRID._STANDARD
	 */
    // 마지막 편집 창 닫기
    ,   editCellClosed: {
        "_function_name_": "JQGRID.editCellClosed"
            ,   gridID: COMM._INIT.DECLARE_CHECK.require + "그리드 ID"
            ,   closedAction: COMM._INIT.DECLARE_CHECK.require + "닫은 후 실행할 action"
            ,   actionData: "action에 사용할 데이타"
    }
  };
//Debug Mode 가 아니면
} else {
	JQGRID._STANDARD = {};
}
// 초기 설정값
// 주석 부분을 해제할 시, userContents.jsp 를 참고할 것.
$.jgrid = $.jgrid || {};
$.extend($.jgrid,{
	defaults : {
		recordtext: _UCS.COMMON3004,
		emptyrecords: _UCS.COMMON3005,
		loadtext: _UCS.COMMON3006,
		pgtext : _UCS.COMMON3007
	},

/*	search : {
		caption: _UCS.COMMON3008,
		Find: _UCS.COMMON3009,
		Reset: _UCS.COMMON3010,
		odata : [_UCS.COMMON3011, _UCS.COMMON3012, _UCS.COMMON3013, _UCS.COMMON3014, _UCS.COMMON3015, _UCS.COMMON3016, _UCS.COMMON3017, _UCS.COMMON3018, _UCS.COMMON3019, _UCS.COMMON3020, _UCS.COMMON3021, _UCS.COMMON3022, _UCS.COMMON3023, _UCS.COMMON3024],
		groupOps: [	{ op: _UCS.COMMON3025, text: _UCS.COMMON3026 },	{ op: _UCS.COMMON3027,  text: _UCS.COMMON3028 }	],
		matchText: _UCS.COMMON3029,
		rulesText: _UCS.COMMON3030
	},
	edit : {
		addCaption: _UCS.COMMON3031,
		editCaption: _UCS.COMMON3032,
		bSubmit: _UCS.COMMON3033,
		bCancel: _UCS.COMMON3034,
		bClose: _UCS.COMMON3035,
		saveData: _UCS.COMMON3036,
		bYes : _UCS.COMMON3037,
		bNo : _UCS.COMMON3038,
		bExit : _UCS.COMMON3039,
		msg: {
			required:_UCS.COMMON3040,
			number:_UCS.COMMON3041,
			minValue:_UCS.COMMON3042,
			maxValue:_UCS.COMMON3043,
			email: _UCS.COMMON3044,
			integer: _UCS.COMMON3045,
			date: _UCS.COMMON3046,
			url: _UCS.COMMON30407,
			nodefined : _UCS.COMMON3048,
			novalue : _UCS.COMMON3049,
			customarray : _UCS.COMMON3050,
			customfcheck : _UCS.COMMON3051
			
		}
	},*/

	view : {
		caption: _UCS.COMMON3052,
		bClose: _UCS.COMMON3053
	},

/*	del : {
		caption: _UCS.COMMON3054,
		msg: _UCS.COMMON3055,
		bSubmit: _UCS.COMMON3056,
		bCancel: _UCS.COMMON3057
	},
	
	nav : {
		edittext: "",
		edittitle: _UCS.COMMON3059,
		addtext: "",
		addtitle: _UCS.COMMON3061,
		deltext: "",
		deltitle: _UCS.COMMON3063,
		searchtext: "",
		searchtitle: _UCS.COMMON3065,
		refreshtext: "",
		refreshtitle: _UCS.COMMON3067,
		alertcap: _UCS.COMMON3068,
		alerttext: _UCS.COMMON3069,
		viewtext: "",
		viewtitle: _UCS.COMMON3071
	},
	col : {
		caption: _UCS.COMMON3072,
		bSubmit: _UCS.COMMON3073,
		bCancel: _UCS.COMMON3074
	},*/

	errors : {
		errcap : _UCS.COMMON3075,
		nourl : _UCS.COMMON3076,
		norecords: _UCS.COMMON3077,
		model : _UCS.COMMON3078
	},
	formatter : {
		integer : {thousandsSeparator:",", defaultValue: "0"},
		number : {decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2, defaultValue: "0.00"},
		currency : {decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 0, prefix:"", suffix:"", defaultValue: "0"},
		date : {
			dayNames:   [
				_UCS.COMMON3091, _UCS.COMMON3092, _UCS.COMMON3093, _UCS.COMMON3094, _UCS.COMMON3095, _UCS.COMMON3096, _UCS.COMMON3097,
				_UCS.COMMON3098, _UCS.COMMON3099, _UCS.COMMON3100, _UCS.COMMON3101, _UCS.COMMON3102, _UCS.COMMON3103, _UCS.COMMON3104
			],
			monthNames: [
				_UCS.COMMON3105, _UCS.COMMON3106, _UCS.COMMON3107, _UCS.COMMON3108, _UCS.COMMON3109, _UCS.COMMON3110, _UCS.COMMON3111, _UCS.COMMON3112, _UCS.COMMON3113, _UCS.COMMON3114, _UCS.COMMON3115, _UCS.COMMON3116,
				_UCS.COMMON3117, _UCS.COMMON3118, _UCS.COMMON3119, _UCS.COMMON3120, _UCS.COMMON3121, _UCS.COMMON3122, _UCS.COMMON3123, _UCS.COMMON3124, _UCS.COMMON3125, _UCS.COMMON3126, _UCS.COMMON3127, _UCS.COMMON3128
			],
			AmPm : [ _UCS.COMMON3129, _UCS.COMMON3130,_UCS.COMMON3131,_UCS.COMMON3132],
			S: function (j) {return j < 11 || j > 13 ? ['st', 'nd', 'rd', 'th'][Math.min((j - 1) % 10, 3)] : 'th';},
			srcformat: 'Y-m-d',
			newformat: 'Y-m-d',
			masks : {
				ISO8601Long:"Y-m-d H:i:s",
				ISO8601Short:"Y-m-d",
				ShortDate: "Y/j/n",
				LongDate: "l, F d, Y",
				FullDateTime: "l, F d, Y g:i:s A",
				MonthDay: "F d",
				ShortTime: "g:i A",
				LongTime: "g:i:s A",
				SortableDateTime: "Y-m-d\\TH:i:s",
				UniversalSortableDateTime: "Y-m-d H:i:sO",
				YearMonth: "F, Y"
			},
			reformatAfterEdit : false
		},
		baseLinkUrl: "",
		showAction: "",
		target: "",
		checkbox : {disabled:true},
		idName : 'id'
	}

});

/**
 * @description options 모델
 * @version 1.1
 * @since 2013.05
 * @author 이병직
 * @constant
 * @memberOf JQGRID
 * @type {Object}
 */
JQGRID._options = {
        datatype: "json"
    ,   url: "plugin/fwf/data-none.jsp"
    ,   editurl: "plugin/fwf/data-none.jsp"
    ,   cellurl: "plugin/fwf/data-none.jsp"
    ,   editCelliRow: -1
    ,   editCelliCol: -1
    ,   autowidth: true
    ,   autoResizeHeight: true  // 설정
    ,   autoResizeWidth: true   // 설정
    ,   fullscreenBotton: true  // 설정
    ,   popupMode: false
    ,   jsonReader: {
                total: "total"
            ,   page: "page"
            ,   records: "numRow"
            ,   root: "rows"
            ,   repeatitems: false
            ,   id: "id" 
        }
    ,   rowNum: 60000            // rowNum을 정확하게 반영하면, 두번 그려야한다. (다량일땐 시간 2배)
    ,   viewrecords: true
    ,   rownumbers: true
    ,   rownumWidth: 48
    ,   loadonce: true
    //,   recordtext: "총 {2} 건"
    //,   loadtext: "데이터 조회중..."
    ,   pgbuttons: false
    ,   pginput: false
    ,   mtype: "POST"
    ,   postData: {"_DATATYPE_":"GRD"}
    // load 는 정렬만 해도 동작
    ,   loadError: function (data) {
        }
    ,   onSortCol: function (index, iCol, sortorder) {
            // progessbar 가 필요할 정도의 정렬은 지양합시다. (01.21)
        }
    // 셀 resize 이벤트 종료 후, 헤더와 바디를 조정 (2013.05.14)
    // 그리드 resize 방식을 headerGroup과 일반 모두 동일하게 적용 (shrink false)했기에 다음 함수도 공통적용. (2013.05.16)
    ,   resizeStop: function() {
	    	var gboxID = "#gbox_" + $(this).attr("id"),
	    		tableWidth = this.p.width - this.p.scrollOffset;
	    	$(".ui-jqgrid-htable", $(gboxID)).width(tableWidth);
			$(".ui-jqgrid-btable", $(gboxID)).width(tableWidth);
			$(".ui-jqgrid-ftable", $(gboxID)).width(tableWidth);
			// 2013.06.07 htable 높이가 변경될 경우
			JQGRID._htableHeight.isChange( this.p.HHChangeAction );
    	}
}; 

if ( typeof Object.create !== "function" ){
    Object.create= function(o){
        var F = function(){};
        F.prototype = o;
        return new F();
    };
}

/**
 * @description 전역변수 공간
 * @version 1.0
 * @since 2013.01
 * @author 이병직
 * @namespace global
 * @memberOf JQGRID
 */
JQGRID.global = {
	/**
	 * @public
	 * @description cbox 배열
	 * @type {String[]}
	 */
    cboxY: [] // cbox 배열 (multiselect: true 일 경우)
	/**
	 * @public 
	 * @description cbox 배열
	 * @type {String[]}
	 */
,   cboxN: [] // cbox 배열 (multiselect: false 일 경우)
	/**
	 * @
	 * @description cbox 배열
	 * @type {String[]}
	 */
,   cboxM: [] // cbox 배열 (multiselect: remove 일 경우)
//,   beforeEditCellObj: {} // 선택한 셀 정보
//,   selectState: {} // 서로 다른 셀렉트 모델을 저장 - 다중모델 셀렉트 구현은 다음 버전 적용 검토.

};

/**
 * @description 글로벌 변수 초기화
 * @version 1.0
 * @since 2013.01
 * @author 이병직
 * @class
 * @memberOf JQGRID
 */
JQGRID.globalClear = function(){
    JQGRID.global.cboxY = []; // 초기화
    JQGRID.global.cboxN = []; // 초기화
    JQGRID.global.cboxM = []; // 초기화  
};

/**
 * @description 그리드 생성
 * <br/>
 * addOptions 속성은 jqGrid 문서(see 참조) 및 fwf-jqGrid.js 상단 주석을 참고.
 * @version 1.0
 * @since 2013.01
 * @author 이병직
 * @class
 * @memberOf JQGRID
 * @param {String} gridID 그리드 아이디 (table ID)
 * @param {Object} addOptions 그리드 생성옵션
 * @param {Object} [localData] 로컬 데이터
 * @see http://www.trirand.com
 */
JQGRID.create = function (gridID, addOptions, localData){
    var options = Object.create(JQGRID._options)
    ,   id = "#" + gridID
    ,   pagerId = id + "_pager"
    ,   pagerName = pagerId.replace("#","")
    ,   gboxID = "#gbox_" + gridID
    ,   headerBtn = gboxID + " .HeaderButton"
    ,   loadingObj = null // 첫 로딩시 사용할 객체
    ,   CONS = JQGRID.CONSTANT
    ,   autoheightParent = function(){
	    	if (addOptions.autoheightParent) {
	    		return addOptions.autoheightParent;
	    	} else if ( $(CONS.GRID_PARENT).length > 0 ) {
	    		return JQGRID.CONSTANT.GRID_PARENT;
	    	}
    	}()
    ,   autowidthParent = function(){
    		if (addOptions.autowidthParent) {
    			return addOptions.autowidthParent;
    		} else if ( $(CONS.GRID_PARENT).length > 0 ) {
    			return JQGRID.CONSTANT.GRID_PARENT;
    		}
    	}()
    ,   isHeaderGroup = function(){
    		if(addOptions.headerGroup){
    			return true;
    		} else {
    			return false;
    		}
    	}();

    ///////////////////////////////////////
    // options 설정 ( 그리드 생성전 )
    ///////////////////////////////////////
    if (addOptions.isPager !== false) {
        $(id).before("<div id=" + pagerName + "></div>"); 
        options.pager = pagerId;
    }
    
    $.extend(options,addOptions);

    // 팝업 모드 체크
    if (options.popupMode) {
    	options.autoResizeHeight = false;
    	options.autoResizeWidth = false;
    	options.fullscreenBotton = false;
    }
    
    // 2013.06.07 htable 가변시 그리드 크기조절시작.
    options.resizeStart = function(){
    	JQGRID._htableHeight.save(id);
    };
    if(options.autoResizeHeight) { // 주의! height를 지정하는 경우(typeof options.height === "number")에도 동작한다. 즉, 첫 모양만 height가 적용됨.
    	options.HHChangeAction = function(){
    		JQGRID._autohight(id, autoheightParent);
    	};
    } else {
    	options.HHChangeAction = null;
    }
    
    if (options.isPaging === true) {
    	options.pgbuttons = true;
    	options.pginput = true;
    	options.rowNum = _INIT_SESSION._GRID_PAGE_RECORD;
    	options.recordtext = "{0} ~ {1} / " + _UCS.COMMON3004;
    }
    
    
    if (addOptions.colSum) {
        options.footerrow = true; // 변경할 수 없는 param
        
        // 2013.05.10 추가 - 페이징이면서 colSum 인 경우.
        if(addOptions.isPaging === true) {
    	    options.afterAddJSONDataLocal = function (pageData) {
    		    var rows = pageData.rows,
    		   	    pageRecords = rows.length,
    		   	    colSum = addOptions.colSum,
    		   	    x = "", y = 0,
    		   	    numBox =[],
    		   	    strBox =[];
    		   
    		    // box setting 및 초기화 (객체이고, closure 처럼 동작함을 주의!)
    		    for(x in colSum){
    			    if (typeof colSum[x] === "number"){
    				    colSum[x] = 0;
    				    numBox.push(x);
    			    } else if (typeof colSum[x] === "string") {
    				    strBox.push(x);
    			    }
    		    }
    		   
    		    for( pageRecords -=1 ; pageRecords >= 0; pageRecords -=1){
    				 for (y = 0; y < numBox.length; y += 1){
    					 colSum[numBox[y]] += Number(rows[pageRecords][numBox[y]]);
    				 }
    		    }
    		   
    		   $(id).jqGrid("footerData", "set", colSum);
    		   
    	   };
       } // if
    }
    
    if (addOptions.lastrowToFooter === true){
        options.footerrow = true; // 변경할 수 없는 param
    }
    
    // 1.1 추가 CRUD column
    options.colNames.unshift(JQGRID.CONSTANT.ROW_STAT_COL_NAME);
    if (addOptions.crudHidden === false){
    	options.colModel.unshift({name:JQGRID.CONSTANT.ROW_STAT_COL_MODEL, width:JQGRID.CONSTANT.ROW_STAT_COL_WIDTH, align:"center", sortable: true, sorttype:"text", fixed:true, formatter:JQGRID.formatter.CRUD});
    } else {
    	options.colModel.unshift({name:JQGRID.CONSTANT.ROW_STAT_COL_MODEL, width:JQGRID.CONSTANT.ROW_STAT_COL_WIDTH, align:"center", hidden:true, formatter:JQGRID.formatter.CRUD});
    }
    
    options.onSelectAll = function (aRowids, status) {

    	
        var multiselectColumn = JQGRID._getColumnHasFormatter(id, "multiselect");
        //,   multiselectColumnName = null
        //,   count = aRowids.length;
        
        if (multiselectColumn) {
        	/*
            multiselectColumnName = multiselectColumn.name;

            
            if (status) {
                for (var x = 0; x < count; x += 1) {
                	var sCboxID = "#jqg_" + id.replace("#","") + "_" + aRowids[x];
                    if($(sCboxID).is(":visible")){
                    	$(id).jqGrid("setCell", aRowids[x], multiselectColumnName, "Y");
                    }else
                    {
                    	$(id).jqGrid("setCell", aRowids[x], multiselectColumnName, "M");
                    }
                }
            } else {
                for (var x = 0; x < count; x += 1) {
                	var sCboxID = "#jqg_" + id.replace("#","") + "_" + aRowids[x];
                    if($(sCboxID).is(":visible")){
                    	$(id).jqGrid("setCell", aRowids[x], multiselectColumnName, "N");
                    }else
                    {
                    	$(id).jqGrid("setCell", aRowids[x], multiselectColumnName, "M");
                    }
                }
            }
            */
            
            // selectAll 동작시 formatter 동작. 중복 push (2.24)
            // selectAll -> formatter 순으로 동작
            // formatter는 그리드 데이타를 읽을 때면 항상 동작한다. 그리드 데이타를 읽는 getRowData 등 도 해당 (3.20)
            JQGRID.globalClear();
        }
        
        // onSelectAllCallback
        if (addOptions.onSelectAllCallback){
            addOptions.onSelectAllCallback(aRowids, status);
        }
    };
    
/*    // 1.1 추가 formater 설정 방법 중 하나. 포메터로 작성함.(04.11)
    if (addOptions.formatterCurrency) {
    	// 사용자 설정
    	$.extend($.jgrid.formatter.currency, addOptions.formatterCurrency );
    } else {
    	// 기본으로 복원
    	$.extend($.jgrid.formatter.currency, JQGRID.CONSTANT.FORMATTER_CURRENCY);
    }*/
    
    //{decimalSeparator:_UCS.COMMON3085, thousandsSeparator: _UCS.COMMON3086, decimalPlaces: parseInt(_UCS.COMMON3087, 10), prefix:"", suffix:"", defaultValue: _UCS.COMMON3090},
    
/*    options.beforeEditCell = function(rowid, callname, value, iRow, iCol) {
        JQGRID.global.beforeEditCellObj = {
            text: $(this.rows[iRow].cells[iCol]).text()
        ,   rowid: rowid
        ,   callname: callname
        ,   value: value
        ,   iRow: iRow
        ,   iCol: iCol
        };
    };*/
    
    // tab 포커스아웃이 두번이뤄지기에 에러를 유발. <- focus out 자동 close 삭제(1.30)
    options.afterEditCell = function(rowid, callname, value, iRow, iCol) {
        var cellDOM = this.rows[iRow].cells[iCol]
        ,   $cellInput = $("input", $(cellDOM))
        ,   $cellSelect = $("select", $(cellDOM))
        ,   unformatValue = Number(COMM.unformat.currency(value));
        
        // 2013.04.25 그리드 포멧중 시간, currency 포멧 및 time, date 포멧을 기존 grid와 동일하게 동작하도록 추가
        var colModelFormatterType = typeof this.p.colModel[iCol].formatter;
        $cellInput.val(COMM._formatter_module.unformatOnlySeparator(colModelFormatterType, value));
        
        // colModel: focus 제어 예시  editoptions:{dataInit:function(elem){$(elem).focus();
        // 만약 값0 이라면 값을 지우고 시작.
        
        if (Number(value) === 0 || Number(unformatValue) === 0){	// currency 포멧이 0.12 가 아니라 0x12 가 될 수 있기 때문에 NaN은 제외.
            $cellInput.val("");
        // 1.00 과 같은 경우 -소수최대2자 입력 input typeGridNumber2 대응 (2013.05.13)
        } else if (unformatValue) { 	// 0과 NaN이 아니면
        	 $cellInput.val(unformatValue);
        }

        // 마지막 편집 cell 위치 저장
        $(id).jqGrid("setGridParam", {editCelliRow:iRow, editCelliCol:iCol} );
        
        // select box 인가? IE 더블클릭 버그 처리.
        if( $cellSelect.length > 0) {
            $cellSelect.off("dragstart").off("selectstart");
            $cellSelect.one("dragstart", function(event){event.preventDefault();}).one("selectstart", function(event){event.preventDefault();});
        }
        
        if(typeof options.afterEditCellCallback === "function"){
            options.afterEditCellCallback(rowid, callname, value, iRow, iCol);
        }
    };
    
    // 마지막 편집 cell 위치 초기화
    options.afterRestoreCell = function(rowid, callname, value, iRow, iCol) {
        $(id).jqGrid("setGridParam", {editCelliRow:-1, editCelliCol:-1, editCellMode:"restore"});
        if (typeof options.afterRestoreCellCallback === "function") {
            options.afterRestoreCellCallback(rowid, callname, value, iRow, iCol);
        }
    }; // 복원시
    options.beforeSaveCell = function(rowid, callname, value, iRow, iCol) {
        $(id).jqGrid("setGridParam", {editCelliRow:-1, editCelliCol:-1, editCellMode:"save"});
        if (typeof options.beforeSaveCellCallback === "function") {
            options.beforeSaveCellCallback(rowid, callname, value, iRow, iCol);
        }
    }; // 입력시
    
    // 사용자 함수용
    options.afterSaveCell =  function(rowid, callname, value, iRow, iCol) {
        if (typeof options.afterSaveCellCallback === "function"){
            options.afterSaveCellCallback(rowid, callname, value, iRow, iCol);
        }
    };
    
    //정렬만 해도 동작
    options.loadComplete = function (data) {
        var cboxYLength = JQGRID.global.cboxY.length
        ,   cboxNLength = JQGRID.global.cboxN.length
        ,   cboxMLength = JQGRID.global.cboxM.length;

        if (cboxYLength > 0) {
            for (var x = 0; x < cboxYLength; x += 1 ) {
                $(JQGRID.global.cboxY[x]).attr("checked","checked");
                //$(JQGRID.global.cboxY[x]).prop("checked",true); 1.9.1 방식
                $(JQGRID.global.cboxY[x]).removeClass("cbox");  //cobx에 걸린 이벤트 제거를 위해(01.31) - 첫 선택시 row선택 이벤트가 발동함
            }
            JQGRID.global.cboxY = []; // 초기화
        }
        if (cboxNLength > 0) {
           for (var x = 0; x < cboxNLength; x += 1 ) {
                $(JQGRID.global.cboxN[x]).removeAttr("checked");
            }
            JQGRID.global.cboxN = []; // 초기화
        }
        if (cboxMLength > 0) {
            for (var x = 0; x < cboxMLength; x += 1 ) {
                 $(JQGRID.global.cboxM[x]).remove();
            }
            JQGRID.global.cboxM = []; // 초기화
        }
        
        var multiselectColumn = JQGRID._getColumnHasFormatter(id, "multiselect")
        ,   multiselectColumnName = null;

        if (multiselectColumn) {

            multiselectColumnName = multiselectColumn.name;
            
            // 그리드 체크박스 클릭
    		$(id +" input:checkbox").click(function(){
    		    // 정렬시 체크 표시를 남기기 위해. 
    		    // 체크박스 자체가 event에 불안정(IE)하므로, 이 기능만으로 action처리를 하면 안된다. (action시 별도의 checkedToColumn 사용을 할 것)
    		 	//JQGRID.checkedToColumn({gridID:gridID, colName:multiselectColumnName});
    			
    			// 체크박스 클릭 시 checkedToColumn을 사용하면 for문을 돌면서 체크하기때문에 동작시간이 지연되어 아래로직으로 변경함. 2015.08.12
    			var selectRow  = JQGRID.editoptionsDataEvent.getRowID(this);
    			var selectId   = "#jqg_" + gridID + "_" + selectRow;
    			var selectGrid = "#" + gridID;

                if ($(selectId).is(":checked")){;
                	$(selectGrid).jqGrid("setCell", selectRow, multiselectColumnName, "Y");
                } else {
                	$(selectGrid).jqGrid("setCell", selectRow, multiselectColumnName, "N");
                }
    	 	});
    		
    	    // 그리드 헤더 클릭 시 체크박스 변수 초기화
    	    $(".ui-jqgrid-sortable").click(function(){
    	        JQGRID.globalClear();
    	    });
        }

        // callback
        if (typeof addOptions.loadCompleteCallback === "function") {
            addOptions.loadCompleteCallback(data);
        }
        // progressbar가 없을때 "off" 호출하면 error. (예: localdata를 통한 grid)
    };
    
    options.onSortCol = function (index, iCol, sortorder)  {
        // 정렬시 progressbar 필요하지 않을 data를 유지할 것. (_option 참고)
        // callbak 필요시 정의가능
    };
    
    // 2013.06.25 멀티셀렉트 사용시, row, cell 선택하면 selrow 되는 것을 방지 (모든 grid 선택이벤트 정지)
/*    if(options.multiselectNotSelrow){
    	options.beforeSelectRow = function(rowid, e){
		    var $myGrid = $(this),
		    i = $.jgrid.getCellIndex($(e.target).closest('td')[0] ),
		    cm = $myGrid.jqGrid('getGridParam','colModel');
		    return (cm[i].name === 'cb');
	    };
    }*/

    // prototype 에서 지역속성으로 (2013.05.16 - Object.create 보다, this접근과 compete변환이 더 간편함.)
    options.ajaxGridOptions = {
        timeStamp: 0,
        // 가장 나중에 동작. 여러 속성중 data는 responseTest로 넘어온다.
        beforeSend: function() {
            JQGRID.globalClear(); // getDataRow와 같은 동작시에도 포매터가 작동하기 때문. 3.20
            this.timeStamp = $.now();
            // console.log("그리드 로딩(1) 시작(progress ON)" + this.timeStamp);
            COMM.progressbar( this.timeStamp);
        },
        
        error: function(xhr,status,error){
            COMM.progressbar( "off", this.timeStamp ); // 첫 로딩시 ajax not found 에러가 발생한 경우.
        	xhr.devMethod = "JQGRID._options - first loding -";
        	COMM._ajaxError(xhr,status,error); 
        },
        
        complete: function(data){
           // loding 사용시엔 동작하지 않음 _ajaxComplete 가 동작. 
           // console.log("그리드 로딩(1) 종료(progress OFF)" + this.timeStamp); 
           COMM.progressbar("off", this.timeStamp);
        },
        
        beforeSuccess: function(data,st, xhr) {
        	var rowNum = data.rowNum;
        	// 2013.05.31 그리드 rowNum을 내부에 저장 (페이지별 row수가 아닌 총건수)
        	$(id).jqGrid("setGridParam",{"recieveRowNum": rowNum});
        	if (rowNum > JQGRID.CONSTANT.RECORD_WARNING) {
        		alert(rowNum +  " " + _UCS.COMMON2020);
        	}
        }
    };
    
    // first loading
    if(typeof addOptions.loading === "object") {
    	if (addOptions.loading.hasOwnProperty("isBrief")){
    		loadingObj = JQGRID._byTasking("briefLoading", addOptions.loading, JQGRID._STANDARD.briefByTasking);
    	} else {
    		loadingObj = JQGRID._byTasking("loading", addOptions.loading, JQGRID._STANDARD.showByTasking);
    	}
        options.url =  INITIALIZE_BASE + "/uxAction";
        options.postData = loadingObj.postData;
        options.ajaxGridOptions.complete = JQGRID._ajaxComplete({
    		gridID:id.replace("#",""),
    		sGridID:id,
    		colSum: addOptions.colSum,
    		sPageID:"#" + addOptions.loading.pageID,
    		callbackData: addOptions.loading.data,
    		callback: addOptions.loading.callback
    		// 	v1.1 brief
    		//	,   isBrief:loadingObj.isBrief 
        });
    }
    
    ///////////////////////////////////////
    // 생성, 버튼숨김, 검색창, URL
    ///////////////////////////////////////
    $(id).jqGrid(options);
    $(headerBtn).hide();
    $(id).jqGrid("navGrid", pagerId, { add:false, edit:false, del:false, refresh:false, search:false});
    $(id).jqGrid("setGridParam", {url: INITIALIZE_BASE + "/uxAction"});
    
    if (addOptions.setFrozenColumns === true) {
        $(id).jqGrid("setFrozenColumns");
    }
    
    if(isHeaderGroup) {
        $(id).jqGrid("setGroupHeaders", {
            useColSpanStyle: true,
            groupHeaders: addOptions.headerGroup
        });
    }
    
    if (localData && options.datatype === "local") {
        for ( var i=0; i<localData.length; i+=1 ) {
            $(id).jqGrid("addRowData",i+1,localData[i]); 
        }
        
        if (addOptions.colSum) {
            options.footerrow = true;
            JQGRID._colSum(id, addOptions.colSum);
        }
  
        $(id).jqGrid("setGridParam",{"rowNum":localData.length});
        $(id).trigger("reloadGrid");
    } else {     // 데이터없음 메세지
        // 전역 객체를 넣으면, java처럼 마지막 선언한 곳으로 참조가 되어 앞선 객체엔 after 되지 않는 듯 싶다. (2.4)
        $("#load_"+ id.replace("#","")).after($(JQGRID.CONSTANT.EMPTY_ROWS).attr("id", "emptyrows_" + id.replace("#","")));
        $("#emptyrows_" + id.replace("#","")).hide();  //처음엔 안뜸 (12.27)
    }
    
    if (addOptions.lastrowToFooter === true){
        JQGRID.lastrowToFooter(id);
    }
    
    // select box change 시 닫기 (insertSelect를 사용하지 않은 페이지 고려)
/*    $(document).on("change", id, function(){
        JQGRID.editCellClosed( {gridID:gridID, closedAction:function(){}} );
    });*/
    
    // height 설정하지 않았을 때에만 동작 (설정해도 현재는 첫 화면에서만 높이 지정. autoResizeHeight=false 가 아니면 의미가 없다)
    if (typeof addOptions.height !== "number") {
    	JQGRID._autohight (id, autoheightParent);
    }
    
    // 리사이즈
    if (options.autoResizeHeight) {
        $.fwf.resizeStop( {"id": id, "parent":autoheightParent, "resizeType":"resize_height"}, JQGRID._resize);
    }
    if (options.autoResizeWidth) {
        $.fwf.resizeStop( {"id": id, "parent":autowidthParent, "resizeType":"resize_width"}, JQGRID._resize);
    }
    
    // 풀버튼 효과 (unbind 가 관건...원하는 방식은 못찾음. 11.25)
    if ( options.fullscreenBotton ) {
        $(CONS.FULLSCREEN , $(CONS.FULLCONTENT)).click( {"id": id, "parent":autoheightParent, "resizeType":"resize_height_fast" }, JQGRID._resize);
        $(CONS.FULLSCREEN , $(CONS.FULLCONTENT)).click( {"id": id, "parent":autowidthParent, "resizeType":"resize_width_fast" }, JQGRID._resize);
        $(CONS.LAYOUT_TOGGLER).click( {"id": id, "parent":autoheightParent, "resizeType":"resize_height_fast" }, JQGRID._resize);
        $(CONS.LAYOUT_TOGGLER).click( {"id": id, "parent":autowidthParent, "resizeType":"resize_width_fast" }, JQGRID._resize);
    }
};

/**
 * @description 커스텀 포맷 currency를 위한 getCol - getCol 기능중 계산부분만 작성
 * @version 1.1
 * @since 2013.04.23
 * @author 이병직
 * @event
 * @memberOf JQGRID
 * @param {String} id 그리드 sID
 * @param {String|Number} col 컬럼이름 또는 위치
 * @param {String} mathopr 정의된 계산
 */
JQGRID._getColWithCurrency = function (id, col, mathopr) {
	var ret = 0, val=0, sum=0, min=0, max=0, v, pos=-1, $grid = $(id);

	if(mathopr === undefined) { mathopr = "sum"; } // 없으면 sum
	
	//col 이 이름이거나 col의 위치이거나를 판단해서 위치 pos 를 구함
	if(isNaN(col)) {
		$($grid.jqGrid("getGridParam","colModel")).each(function(i){
			if (this.name === col) {
				pos = i;
				return false;
			}
		});
	} else {pos = parseInt(col,10);}
	
	if(pos>=0) {
		$(id + " tr").each( function(index){
			if($(this).hasClass("jqgrow")){
				try {
					val = COMM.unformat.currency($(this).children("td").eq(pos).text());
					// 2013.08.22 currencyB를 이용하면서 합계를 사용하는 경우
					val = val === "" ? 0 : val;
				} catch(e) {
					val = 0;
				}
				v = parseFloat(val);
				sum += v;
				if (max === undefined) {max = min = v;}
				min = Math.min(min, v);
				max = Math.max(max, v);
			}
		});
		
		// 연산 기본값은 sum
		switch(mathopr.toLowerCase()){
		case 'sum': ret =sum; break;
		case 'avg': ret = sum/ln; break;
		case 'count': ret = ln; break;
		case 'min': ret = min; break;
		case 'max': ret = max; break;
		}
	}
	return ret;
};

/**
 * @description colSum = {"계좌번호":"계", "잔액":0, "입금액":0, "출금액":0 };
 * @version 1.0
 * @since 2013.01.21
 * @author 이병직
 * @event
 * @memberOf JQGRID
 * @param {String} id
 * @param {Object} colSum
 */
JQGRID._colSum = function (id, colSum) {
    // 13.01.21 (참조이므로 원본을 유지하려면 다른 객체를 사용해야 한다.)
    var colSumData = {};
    for (var x in colSum) {
        if (!isNaN(colSum[x]) && !colSum[x] ) {
        	colSumData[x] = JQGRID._getColWithCurrency (id, x, "sum");
            // colSumData[x] = $(id).jqGrid("getCol", x, false, "sum"); // jqGrid 제공 currency 사용시에 적용
        } else if (typeof colSum[x] === "string") {
            colSumData[x] = colSum[x];
        }
    }
    $(id).jqGrid("footerData","set", colSumData);
};

/**
 * @description 커스텀 포맷을 위한 getRowData - getRowData 기능중 treeGrid 사용 연산은 제거
 * <br/>
 * html을 보내는 것이 아니라 해당 text를 전송한다. (getRowData는 html 기준)
 * <br/>
 * currency, date, time 을 지원
 * @version 1.1
 * @since 2013.04.25
 * @author 이병직
 * @event
 * @memberOf JQGRID
 * @param {String} gridID
 * @param {String} rowid
 * @param {String} transGridStatus
 */
JQGRID._getRowDataWithCustom = function (gridID, rowid, transGridStatus) {
	var res = {}, 
		resall, 
		colModelName=[], 
		colModelFormatterType=[], 
		sGridID = "#"+gridID,
		nm,ind = null,text;
	
	var getRowDataFunc = function(){
		if( $(ind).hasClass('jqgrow') ) {
			// tr 안의 td 각각
			$('td[role="gridcell"]',ind).each( function(i) {
				nm = colModelName[i]; // 콜 모델에서 콜 이름
				text = $.trim($(this).text());
				
				// transGridStatus 옵션을 통해서만 CRUD 전송여부를 결정함.
				// 따라서 JQGRID.CONSTANT.ROW_STAT_COL_MODEL 도 전송되지 않도록 하는 것이 일괄성이 있다. 
				// transGridStatus === true? nm !== JQGRID.CONSTANT.ROW_STAT_COL_MODEL: true
				if ( nm !== 'cb' && nm !== 'subgrid' && nm !== 'rn' &&( transGridStatus === true ? true: nm !== JQGRID.CONSTANT.ROW_STAT_COL_MODEL)) {
					/*						if($t.p.treeGrid===true && nm == $t.p.ExpandColumn) {  // tree 그리드를 사용하는 경우는 제외.
					res[nm] = $.jgrid.htmlDecode($("span:first",this).html());
				} else {*/
					res[nm] = COMM._formatter_module.unformatOnlySeparator(colModelFormatterType[i], text);
				}
			});
			return true;
		}
		return false;
	};
	
	$( $(sGridID).jqGrid("getGridParam","colModel") ).each(function(){
		colModelName.push(this.name);
		colModelFormatterType.push(typeof this.formatter);
	});

	if(rowid === undefined) { 	// 로우 아이디가 없으면 전체를
		resall = [];
		$(sGridID + " tr").each( function(index){
			ind = $(this);
			if(getRowDataFunc()){
				resall.push(res);
				res={};
			}
		});
		return resall;
	} else { 					// 로우 아이디가 있으면 해당 로우만
		ind = $(sGridID+" tr#"+rowid);
		if(ind.length < 1) { return res; } // 없으면 빈 값.
		getRowDataFunc();
		return res;
	}

};

/**
 * @description 마지막 행을 footer 로.
 * @version 1.0
 * @since 2013.01
 * @author 이병직
 * @class
 * @memberOf JQGRID
 * @param {String} sGridID 그리드 sID
 */
JQGRID.lastrowToFooter = function (sGridID) {
    var rowID = $(sGridID).jqGrid("getDataIDs").pop(); // id가 항상 행 순서일때 정상.
    //rocords 사용시 rowNumber를 id를 사용할때만 정상동작 
    //var rowID = $(sGridID).jqGrid("getGridParam", "records");
    //$(sGridID).jqGrid("footerData","set",  $(sGridID).jqGrid("getRowData", rowID)); // // 04.25 커스텀 currency 타입 지원을 위해 변경.
    $(sGridID).jqGrid("footerData","set",  JQGRID._getRowDataWithCustom(sGridID.replace("#",""), rowID));
    $(sGridID).jqGrid("delRowData", rowID);
};

/**
 * @description 그리드 동적 테이블 크기 조절
 * @version 1.0
 * @since 2013.05.15
 * @author 이병직
 * @class
 * @memberOf JQGRID
 * @param {String} gridID 그리드 ID
 * @param {String} colState showCol, hideCol
 * @param {String[]} colnames 적용될 이름배열 
 */
JQGRID.dynamicCol = function (gridID, colState, colnames ){
	JQGRID._setGridWidthFalse("#" + gridID, false, function(){$("#" + gridID).jqGrid(colState, colnames);});
};

/**
 * @description 그리드 shrink false 옵션 적용 setGridWidth 함수
 * @version 1.1
 * @since 2013.05.24
 * @author 이병직
 * @event
 * @memberOf JQGRID
 * @param {String} sGridID
 * @param {Number} setWidth
 * @param {Function} action
 */
JQGRID._setGridWidthFalse = function (sGridID, setWidth, action){
	var sGboxID = "#gbox_" + sGridID.replace("#",""),
	    scrollOffset = $(sGridID).jqGrid("getGridParam", "scrollOffset"),
	    tableWidth = setWidth ?  setWidth - scrollOffset : $(sGridID).jqGrid("getGridParam", "width") - scrollOffset,
		gridWidth = setWidth || $(sGridID).jqGrid("getGridParam", "width");
	
	if (typeof action === "function") {
		action();
	}
	$(sGridID).jqGrid("setGridWidth", gridWidth, false);	//shrink false
	//그리드 테이블 내용 크기조절 - 그리드 크기가 아닌 내용테이블 크기(2013.05.14)
	$(".ui-jqgrid-htable", $(sGboxID)).width(tableWidth);
	$(".ui-jqgrid-btable", $(sGboxID)).width(tableWidth);
	$(".ui-jqgrid-ftable", $(sGboxID)).width(tableWidth);
};

/**
 * @description 그리드 formatter 를 가진 column 찾기
 * @version 1.0
 * @since 2013.01
 * @author 이병직
 * @event
 * @memberOf JQGRID
 * @param {String} sGridID
 * @param {String} formatterName
 */
JQGRID._getColumnHasFormatter = function (sGridID, formatterName) {
    var colModel = $(sGridID).jqGrid("getGridParam", "colModel")
    ,   colModelLength = colModel.length;
    
    for (var x = 0; x < colModelLength; x += 1 ) {
        if ( colModel[x].formatterName === formatterName ) {
            return colModel[x];
        } 
    };
    return null;
    
};

/**
 * @description 그리드 htable 높이를 구하고 비교하는 메소드를 가진 내부객체
 * <br/> 주의! save 와 isChange가 한 set으로 묶여 동작하므로, 각ID를 저장하지 않음.
 * <br/> 따라서, 사용자가 사용할 수 있게 분리하려면, ID를 저장하고 있어야 함. (jqGrid 객체에 저장하는 것을 추천)
 * @version 1.1
 * @since 2013.06.05
 * @author 이병직
 * @memberOf JQGRID
 */
JQGRID._htableHeight = {
	height: 0,
	gboxID: "",
	_init: function(){
		JQGRID._htableHeight.height = 0;
		JQGRID._htableHeight.gboxID = "";
	},
	/**
	 * @function
	 * @param sGridID {String}
	 */
	save: function(sGridID){
		//JQGRID._htableHeight._init();
		JQGRID._htableHeight.gboxID = sGridID.replace("#","#gbox_");
		JQGRID._htableHeight.height = $(".ui-jqgrid-htable" , $(JQGRID._htableHeight.gboxID)).height();
	},
	/**
	 * @function
	 * @returns {Boolean}
	 */
	isChange:function(changeAction){
		if(JQGRID._htableHeight.height === $(".ui-jqgrid-htable" , $(JQGRID._htableHeight.gboxID)).height()){
			return false;
		} else {
			if($.isFunction(changeAction)){
				changeAction();
			}
			return true;
		}
	}
};

/**
 * @description 자동높이 조절기
 * @version 1.0
 * @since 2013.01
 * @author 이병직
 * @event
 * @memberOf JQGRID
 * @param {String} id
 * @param {String} parent
 */
JQGRID._autohight = function (id, parent ){
    // id로 하면 header 제외한 grid body 부분부터
    // #gbox_ + gridId 로 하면 헤더부터 측정함
    var childHeight = $(parent).height() - $(id).offset().top + $(parent).offset().top;
    
    /*console.error("parent height(1) ", $(parent).height());
    console.error("그리드 offset(2) [헤더제외]", $(id).offset().top);
    console.error("그리드 box offset(2) [헤더포함]", $("#gbox_" + id.replace("#","")).offset().top);
    console.error("parent offset(3) ", $(parent).offset().top);*/
    
    if ( $(id).jqGrid("getGridParam", "footerrow") ){
        childHeight -= JQGRID.CONSTANT.HEIGHT_ADDITION_FOOTER; 
    } else {
        childHeight -= JQGRID.CONSTANT.HEIGHT_ADDITION; 
    }
/*    id로 offset을 구하면 headr 부분 다음부터 측정하므로 계산할 필요가 없다.
 * if ( $(id).jqGrid("getGridParam", "headerGroup") ){
        childHeight -= JQGRID.CONSTANT.HEIGHT_ADDITION_GROUP; 
    }*/
    if (childHeight > JQGRID.CONSTANT.GRID_HEIGHT_MIN ) {
        $(id).jqGrid("setGridHeight", childHeight );
    } else {
        $(id).jqGrid("setGridHeight", JQGRID.CONSTANT.GRID_HEIGHT_MIN);
    }
};

/**
 * @description 자동너비 조절기
 * @version 1.1
 * @since 2013.05.14
 * @author 이병직
 * @event
 * @memberOf JQGRID
 * @param {String} id
 * @param {String} parent
 */
JQGRID._autowidth = function (id, parent){
    var chlidWidth = $(parent).width() -3,	// 멀티브라우저 위한 여유크기(스크롤 고려)
    	// groupHeader 사용시 shrinkToFit true 오류현상 회피코드 (직접 크기 지정 2013.05.14)
	    headerResize = function (setWidth){
	    	JQGRID._setGridWidthFalse(id, setWidth);
		};
	// 헤더 높이가 가변으로 변경시 (white-space:pre-warp 적용) 높이 보정을 위한 함수
	JQGRID._htableHeight.save(id);
    // 리사이즈시 좌측 스크롤영역 계산을위한 부분 (그룹, 비그룹 공통) - resizeStop 함수 연계수정 필요
    if (chlidWidth > JQGRID.CONSTANT.GRID_WIDTH_MIN ) {
    	headerResize(chlidWidth);
    } else {
    	headerResize(JQGRID.CONSTANT.GRID_WIDTH_MIN);
    }
    
    // 헤더가 가변으로 변경시 높이 보정 2013.06.04
    JQGRID._htableHeight.isChange( function(){
    	JQGRID._autohight(id, parent);
    });
};


/*
// 자동너비 조절기
// 테스트 기록용 주석 - 개발가이드
JQGRID._autowidth = function (id, parent, resizeGH){
    var chlidWidth = $(parent).width() -3,	// 멀티브라우저 위한 여유크기(스크롤 고려)
    // groupHeader 사용시 shrinkToFit true 오류현상 회피코드 (직접 크기 지정 2013.05.14)
        headerGroupResize = function (setWidth){
    		$(id).jqGrid("setGridWidth", setWidth, false);	//shrink false
    		JQGRID.setGridTableWidth(id.replace("#",""));
    	};
    	
    // shrink true로 설정하면 테이블 크기 직접 설정이 소용 없다.
    // groupHeader 문제와 상관없이 스크롤 영역을 제외한 크기를 반영하기 위해선 동일하게 사용해야 한다 
    // 따라서 그룹헤더 여부를 알 필요가 없다.(2013.05.14).
    
    // 또한 동일하게 사용하지 않을 시, 
    // 탭 전환시 리사이즈를 하면서 헤더와 바디의 차이가 발생하여 깨지듯이 보인다.
    	if (chlidWidth > JQGRID.CONSTANT.GRID_WIDTH_MIN ) {
        	headerResize(chlidWidth);
        	if(resizeGH && resizeGH.isHeaderGroup){
        		headerResize(chlidWidth, false);
        	} else {
        		headerResize(chlidWidth, true);
        	}
        } else {
        	headerResize(JQGRID.CONSTANT.GRID_WIDTH_MIN);
        	if(resizeGH && resizeGH.isHeaderGroup){
        		headerResize(JQGRID.CONSTANT.GRID_WIDTH_MIN, false);
        	} else {
        		headerResize(JQGRID.CONSTANT.GRID_WIDTH_MIN, true);
        	}
    }
};
*/

/**
 * @description 그리드 리사이즈
 * @version 1.0
 * @since 2013.01
 * @author 이병직
 * @event
 * @memberOf JQGRID
 * @param {Object} event
 */
JQGRID._resize = function ( event ) {
    var id = event.data.id
    ,   parent = event.data.parent
    ,   resizeType = event.data.resizeType
    ,   $scrollElement =  $(".ui-jqgrid-bdiv",$("#gview_" + id.replace("#","")))
    ,   scrollOffset = { left: $scrollElement.scrollLeft(), top: $scrollElement.scrollTop() }
    ,   constant = {
            width:"w", heigth:"h"
        }
    ,   offsetRestore = function(type) {
            switch(type){
            case constant.width:
                $scrollElement.scrollLeft(scrollOffset.left);
                break;
            case constant.height:
                $scrollElement.scrollTop(scrollOffset.top);
                break;
            default:
                $scrollElement.scrollLeft(scrollOffset.left);
                $scrollElement.scrollTop(scrollOffset.top);
                break;
            }
        }
    ,   offsetClear = function(type) {
            switch(type){
            case constant.width:
                $scrollElement.scrollLeft(0);
                break;
            case constant.height:
                $scrollElement.scrollTop(0);
                break;
            default:
                $scrollElement.scrollLeft(0);
                $scrollElement.scrollTop(0);
                break;
            }
        };
    
    if ($(id).length === 0){
        $(id).remove();
        return;
    }
    
    // 현재 화면만 작동
    if(!$(id).is(":visible")){
        return;
    }
    
    // 스크롤 리셋 (화면자체 스크롤 생성 방지. 그리드 길이가 화면 초과 방지)
    // 화면이 커지는 경우, 스크롤이동버튼 크기는 늘어나므로 실 offset은 줄어들고, (조금 아래로 내려감)
    // 화면이 작아지는 경우, 스크롤이동버튼 크기는 줄어들므로 실 offset은 늘어난다. (조금 위로 올라감)
    // 스크롤바 전체 길이(감쳐진 길이 포함)는 일정하다.
    
    switch (resizeType) {
    case "resize_height":
        setTimeout( function(){
            offsetClear(constant.height);
            JQGRID._autohight(id, parent);
            offsetRestore(constant.height);
            }, JQGRID.CONSTANT.SLOW);
        break;
    case "resize_width":
        setTimeout( function(){
            offsetClear(constant.width);
            JQGRID._autowidth(id, parent);
            offsetRestore(constant.width);
        }, JQGRID.CONSTANT.SLOW);
        break;
    case "resize_height_fast":
        setTimeout( function(){
            offsetClear(constant.height);
            JQGRID._autohight(id, parent);
            offsetRestore(constant.height);
        }, JQGRID.CONSTANT.FAST);
        break;
    case "resize_width_fast":
        setTimeout( function(){
            offsetClear(constant.width);
            JQGRID._autowidth(id, parent);
            offsetRestore(constant.width);
        }, JQGRID.CONSTANT.FAST);
        break;
    case "resize_all_fast":
        setTimeout( function(){
            offsetClear(constant.height);
            JQGRID._autohight(id, parent);
            offsetRestore(constant.height);
        }, JQGRID.CONSTANT.FAST);
        setTimeout( function(){
            offsetClear(constant.width);
            JQGRID._autowidth(id, parent);
            offsetRestore(constant.width);
        }, JQGRID.CONSTANT.FAST);
        break;
    }
    
};

/**
 * @description 그리드 ajax가 완료한 후에 동작 - complete을 대체
 * @version 1.1
 * @since 2013.05.09
 * @author 이병직
 * @event
 * @memberOf JQGRID
 * @param {Object} constructor
 */
JQGRID._ajaxComplete = function( constructor ) {
    return function(data){
	    var emptyrowsID = "#emptyrows_" + constructor.gridID
	    ,   sGridID = constructor.sGridID
	    ,   colSum = constructor.colSum
	    ,   sPageID = constructor.sPageID
	    ,   callbackData = constructor.callbackData
	    ,   timeStamp = this.timeStamp
	    ,   callback = constructor.callback
	    ,   CONST = JQGRID.CONSTANT
	    ,   response = $.parseJSON(data.responseText);
	    
	    if (COMM._rCodeCheck(response,"JQGRID._ajaxComplete")){
	        COMM.progressbar("off", timeStamp); // rCodeCheck 이상시 정지.
	        return;
	    };
	    
	    if(response.answerTime){
	        $(CONST.GRID_TITLE,  $(sPageID)).html(CONST.GRID_TITLE_HTML + COMM.formatter("answerTime", response.answerTime) + CONST.GRID_TITLE_HTML_END);
	    }
	    
	    if ($.isEmptyObject(response.rows)) {
	        $(emptyrowsID).show();
	    } else if (response.rows.length === 0 ) {
	        $(emptyrowsID).show();
	    } else {
	        $(emptyrowsID).hide();
	        if (colSum) {
	            JQGRID._colSum(sGridID, colSum );
	        }
	        if ($(sGridID).jqGrid("getGridParam", "lastrowToFooter") === true){
	            JQGRID.lastrowToFooter(sGridID);
	        }
	        
	        // v1.0 brief (구버전)
	        /* if(typeof BRIEF === "object") {
	            if (typeof BRIEF.answerTime === "function") {
	                BRIEF.answerTime(response.answerTime);
	            }
	            if (typeof BRIEF.drawPrepare === "function") {
	                BRIEF.drawPrepare(response.chart);
	            }
	        }*/
	        
	        // v1.1 brief (2013.04.17)
	        /*if (constructor.isBrief){
	        	COMM._drawPrepare(response.chart, sPageID);
	        }*/
	        
	    }
	    
	    if ($.isFunction(callback) ) {
	        callback(response, callbackData);
	    }
	    // 성능문제로 봉인 -다량일 경우 두 번 그리는 시간 무시 못함(12.26)
	    // rowNum만 갱신하면, 조회건수가 늘어날 경우, 데이터가 잘려보일 수 있음.
	    //$(gridID).jqGrid("setGridParam", {rowNum:response.rowNum}); 
	    //$(gridID).trigger("reloadGrid");
	
	    //console.log("그리드 로딩 종료(progress OFF)" +  this.timeStamp);
	    COMM.progressbar("off", timeStamp);
    };
};

/**
 * @description 그리드 footerrow 클리어
 * @version 1.1
 * @since 2013.05.08
 * @author 이병직
 * @event
 * @memberOf JQGRID
 * @param {String} sPageID
 * @param {String} sGridID
 */
JQGRID._clearFooterrow = function (sPageID, sGridID) {
	if( $(sGridID).jqGrid("getGridParam","footerrow") ) {
		$(".ui-jqgrid-ftable td",$(sPageID)).html("&#160;");
	}
};

/**
 * @description 그리드 rows 수신 (내부에서 재 조회)
 * @version 1.1
 * @since 2013.05.14
 * @author 이병직
 * @event
 * @memberOf JQGRID
 * @param {Object} reloadGridObj
 */
JQGRID._reloadGrid = function (reloadGridObj) {
    var gridID = reloadGridObj.gridID
    ,   sPageID = "#" + reloadGridObj.pageID
    ,   sGridID = "#" + gridID;
    
    var ajaxGridOptions = {  
        beforeSend: function() {
            JQGRID.globalClear(); // getDataRow와 같은 동작시에도 포매터가 작동하기 때문. 3.20
            this.timeStamp = $.now();
            //console.log("그리드 로딩(2) 시작(progress ON)" + this.timeStamp);
            COMM.progressbar( this.timeStamp);
            // 이전 코드
            // footer 포함 전부 지우고 시작. (data수신이 없으면 complete를 실행하지 않기 때문이기도 하다) 
            // $(sGridID).jqGrid("clearGridData", true); //-- 2013.04.29 해제 (주석처리), loading 도입으로 조회data가 없을 때에도 동작하는 것으로 보임. 문제시 수정.
            // 2013.05.07 코드
            JQGRID._clearFooterrow(sPageID, sGridID);
        },
            
        complete: JQGRID._ajaxComplete({
        	gridID: gridID,
        	sGridID:sGridID,
        	colSum: reloadGridObj.colSum,
        	sPageID: sPageID,
        	callbackData: reloadGridObj.data,
        	callback: reloadGridObj.callback
        })
        
    };
    COMM._sessionExpirationCheck(); //세션 체크 (url 은 페이지에 맞춰서)
    
    // 그리드 4.4.4 이상시, 다음 현상이 해결. (2013.05.14)
    // 스크롤 리셋 (0 위치에서 그리면 이상이 없다. 혹은 complete에서 다시 조정하면 복원.)
    // 스크롤을 옮기고 그리드 재조회시 해더와 바디가 틀어지는 현상 조정. (그룹해더일 때)
    //$(".ui-jqgrid-bdiv",$("#gview_" + gridID)).scrollLeft(0);
    
    $(sGridID).jqGrid("setGridParam",{postData: null});
    $(sPageID + " .s-ico").css("display", "none");     	// 그리드 asc, desc 선택 표시를 숨김
    $(sGridID).jqGrid("setGridParam",{postData:reloadGridObj.postData , datatype:"json", page:1, ajaxGridOptions: ajaxGridOptions, sortname:"", lastsort:0} ).trigger("reloadGrid");

};

/**
 * @description task 요청
 * @version 1.1
 * @since 2013.03
 * @author 이병직
 * @event
 * @memberOf JQGRID
 * @param {String} type
 * @param {Object} constructor
 * @param {Object} standardObj
 */
JQGRID._byTasking = function (type, constructor, standardObj) {
    var formID = constructor.formID
    ,   gridID = constructor.gridID
    ,   transForm = constructor.transForm
    ,   transGrid = constructor.transGrid
    ,   transGridStatus = constructor.transGridStatus
    ,   transMultiselect = constructor.transMultiselect
    ,   postData = {
            "_TASK_ID_": constructor.taskID
        ,   "_DATATYPE_": "GRD"
        ,   "_PGM_ID_" : constructor.pageID
        }
    ,   sFormID = null
    ,   sGridID = null
    ,   gridData = {}
    ,   reloadGridObj = null
    ,   rows = []
    ,   selarrow = null	
    ,   selarrowLength = null
    ,	CRUDcolumn = null;
    
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
    	CRUDcolumn =  $(sGridID).jqGrid("getCol", JQGRID.CONSTANT.ROW_STAT_COL_MODEL, true);
    	
    	for(var x = 0, lan = CRUDcolumn.length; x < lan; x += 1) {
    		if (JQGRID.CONSTANT.ROW_STAT_COL_TRANS_REGX.test(CRUDcolumn[x]["value"])) {
    			$(sGridID).jqGrid("saveRow", CRUDcolumn[x]["id"]); // CUD이면 저장후 닫음.
    			// rows.push( $(sGridID).jqGrid("getRowData", CRUDcolumn[x]["id"]) ); // 04.25 커스텀 currency 타입 지원을 위해 변경.
    			rows.push( JQGRID._getRowDataWithCustom(gridID, CRUDcolumn[x]["id"], transGridStatus) );
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
    case "blind": // update 는 비동기(기본). 동기는(옵션)
        COMM._AJAX({"param":postData, "callback":constructor.callback, "data":constructor.data, "async":constructor.async});
        break;
    case "show": // select 는 비동기
        reloadGridObj = {
            gridID: gridID
        ,   pageID: constructor.pageID
        ,   callback: constructor.callback
        ,   data: constructor.data
        ,   postData: postData
        ,   colSum: constructor.colSum
        };
        JQGRID._reloadGrid(reloadGridObj);
        break;
    case "brief": // select 는 비동기
    	postData["_DATATYPE_"] = "CHT";
    	reloadGridObj = {
    		gridID: gridID
    	,   pageID: constructor.pageID
    	,   callback: constructor.callback
    	,   data: constructor.data
    	,   postData: postData
    	,   colSum: constructor.colSum
    	,   isBrief: true
    	};
    	JQGRID._reloadGrid(reloadGridObj);
    	break;
    case "loading": // gird 첫 로딩
        return reloadGridObj = {
            gridID: gridID
        ,   pageID: constructor.pageID
        ,   callback: constructor.callback
        ,   data: constructor.data
        ,   postData: postData
        ,   colSum: constructor.colSum
        };
        break;
    case "briefLoading": // gird 첫 로딩
    	postData["_DATATYPE_"] = "CHT";
        return reloadGridObj = {
            gridID: gridID
        ,   pageID: constructor.pageID
        ,   callback: constructor.callback
        ,   data: constructor.data
        ,   postData: postData
        ,   colSum: constructor.colSum
        ,   isBrief: true
        };
        break;
    };
    
};

/**
 * @description Grid 조회: GRD 타입 수신 data 를 Grid 에 그림
 * @version 1.1
 * @since 2013.03
 * @author 이병직
 * @class
 * @memberOf JQGRID
 * @param {Object} constructor
 * @param {String} constructor.taskID 타스크ID
 * @param {String} constructor.pageID 페이지ID
 * @param {String} constructor.gridID 그리드ID
 * @param {String} [constructor.formID] 폼ID
 * @param {Boolean} [constructor.transForm=true] 폼전송
 * @param {Boolean} [constructor.transGrid=false] 그리드전송
 * @param {Boolean} [constructor.transGridStatus] CUD상태 그리드만 전송
 * @param {Boolean} [constructor.transMultiselect=false] 그리드 멀티셀렉트 전송
 * @param {Function} [constructor.callback] 콜백
 * @param {Object} [constructor.data] 콜백 인자
 * @param {Object} [constructor.colSum] colSumModel
 * @returns {Boolean}
 */
JQGRID.showByTasking = function (constructor) {
    return JQGRID._byTasking ("show", constructor, JQGRID._STANDARD.showByTasking);   
};

/**
 * @description Grid 조회: GRD 타입 수신 data 를 Grid 에 그리지 않음
 * @version 1.1
 * @since 2013.03
 * @author 이병직
 * @class
 * @memberOf JQGRID
 * @param {Object} constructor
 * @param {String} constructor.taskID 타스크ID
 * @param {String} constructor.pageID 페이지ID
 * @param {String} [constructor.gridID] 그리드ID
 * @param {String} [constructor.formID] 폼ID
 * @param {Boolean} [constructor.transForm=true] 폼전송
 * @param {Boolean} [constructor.transGrid=false] 그리드전송
 * @param {Boolean} [constructor.transGridStatus] CUD상태 그리드만 전송
 * @param {Boolean} [constructor.transMultiselect=false] 그리드 멀티셀렉트 전송
 * @param {Function} [constructor.callback] 콜백
 * @param {Object} [constructor.data] 콜백 인자
 * @param {Boolean} [constructor.async] 동기화 여부
 * @returns {Boolean}
 */
JQGRID.blindByTasking = function (constructor) {
    return JQGRID._byTasking ("blind", constructor, JQGRID._STANDARD.blindByTasking);    
};

/**
 * @description 보고서용 tasking - 보고서 사이트를 위한 함수
 * @version 1.1
 * @since 2013.03
 * @author 이병직
 * @class
 * @memberOf JQGRID
 * @param {Object} constructor
 * @param {String} constructor.taskID 타스크ID
 * @param {String} constructor.pageID 페이지ID
 * @param {String} constructor.gridID 그리드ID
 * @param {String} [constructor.formID] 폼ID
 * @param {Boolean} constructor.isBrief 항상 true 판정
 * @param {Boolean} [constructor.transForm=true] 폼전송
 * @param {Boolean} [constructor.transGrid=false] 그리드전송
 * @param {Boolean} [constructor.transGridStatus] CUD상태 그리드만 전송
 * @param {Boolean} [constructor.transMultiselect=false] 그리드 멀티셀렉트 전송
 * @param {Function} [constructor.callback] 콜백
 * @param {Object} [constructor.data] 콜백 인자
 * @param {Object} [constructor.colSum] colSumModel
 * @returns {Boolean}
 */
JQGRID.briefByTasking = function(constructor){
	return JQGRID._byTasking ("brief", constructor, JQGRID._STANDARD.briefByTasking); 
};

/**
 * @description Grid 조회: Brief 전용
 * @version 1.0
 * @since 2013.01
 * @author 이병직
 * @class
 * @memberOf JQGRID
 * @param {Object} constructor _reloadGrid 인자 객체
 * @deprecated 현재 GCMS 에서는 사용하지 않는다. (차트없음)
 */
JQGRID.briefThroughAjax = function (constructor) {
    var jsonParam = $("#displayHandler").serializeJSON();
    
    if(!constructor.transType){
        jsonParam["_DATATYPE_"] = "GRD";
    } else {
        switch(constructor.transType) {
        case "GRD": // 그리드만
            jsonParam["_DATATYPE_"] = "GRD";
            break;
        case "CHT":  // 그리드 + 차트
            jsonParam["_DATATYPE_"] = "CHT";
            break;
        default:
            jsonParam["_DATATYPE_"] = "GRD";
            break;
        }
    }
    constructor.postData = jsonParam;
    COMM._sessionExpirationCheck(); // 세션체크
    JQGRID._reloadGrid(constructor);
};

/**
 * @description 컬럼명을 iCOl로 
 * @version 1.1
 * @since 2013.03
 * @author 이병직
 * @class
 * @memberOf JQGRID
 * @param  gridID 그리드 ID {String}
 * @returns colName {String|Number} 
 */
JQGRID.colnameToiCol = function (gridID, colName){
	var sGridID = "#" + gridID
	,   colModel =	$(sGridID).jqGrid("getGridParam", "colModel")
	,   lan = colModel.length
	,   iCol = 0;

	for (var x=0; x < lan; x += 1){
		for (var y in colModel[x]) {
			if (y.toLowerCase() === "name" && colModel[x][y] === colName){
				iCol = x;
				return iCol;
			}
		}
	}
};

/**
 * @description rowID 의 CRUD 상태 확인 v1.1 추가
 * @version 1.1
 * @since 2013.03
 * @author 이병직
 * @class
 * @memberOf JQGRID
 * @param {String} constructor
 * @returns {String} I,R,U,D (CRUD)
 */
JQGRID.getRowStatus = function(constructor) {
	var sGridID =  "#" + constructor.gridID;
	return $(sGridID).jqGrid("getCell", constructor.rowID, JQGRID.CONSTANT.ROW_STAT_COL_MODEL);
};

/**
 * @description rowID 에 CRUD 상태 세팅 v1.1 추가
 * @version 1.1
 * @since 2013.03
 * @author 이병직
 * @class
 * @memberOf JQGRID
 * @param {String} constructor
 */
JQGRID.setRowStatus = function(constructor) {
	var sGridID =  "#" + constructor.gridID;
	$(sGridID).jqGrid("setCell", constructor.rowID, JQGRID.CONSTANT.ROW_STAT_COL_MODEL, constructor.CRUD);
};

/**
 * @description 기준 colname의 값에 따라 타겟 colname의 editable 여부 변경. v1.1 추가
 * <br/>
 * 행 하나만 변경
 * <br/>
 * (전체를 체크해도 의미 없음. COL 전체가 바뀌기 때문. 그때 그때 체크해야 한다.)
 * @version 1.1
 * @since 2013.03
 * @author 이병직
 * @class
 * @memberOf JQGRID
 * @param {Object} constructor
 * @param {String} constructor.gridID 그리드 ID
 * @param {String} constructor.rowID 변경할 row의 ID
 * @param {String} constructor.baseColname 기준이 되는 colname
 * @param {String|Regex} constructor.baseValue 변경기준이 되는 값 (string 또는 정규표현식 가능)
 * @param {Object} constructor.target 변경할 컬럼명과 변경할 값. {컬럼명:false, 컬럼명:true, ... } 형식
 */
JQGRID.changeEditable = function(constructor){
	var sGridID =  "#" + constructor.gridID
	,   rowID = constructor.rowID
	,   baseColname = constructor.baseColname
	,   baseValue = constructor.baseValue // string 혹은 regx(object)
	,   target = constructor.target; // {컬럼명:false, 컬럼명:true}
	
    if(!COMM._declareCheck(constructor, JQGRID._STANDARD.changeEditable)){
        return false;
    };
    
    // 변경하기 전에 편집창을 닫는다. (input 창일 때 false로 변경되면, 닫히지 않는다.)
    // editRow인 경우
    var perRowid = $(sGridID).jqGrid("getGridParam", "editRowRowid");
	if ( typeof  perRowid === "string" ) {
		$(sGridID).jqGrid("saveRow", perRowid);
	}
	// editCell인 경우
	JQGRID.editCellClosed({
		gridID:constructor.gridID,
		closedAction: function(){}
	});
    
	if (typeof baseValue === "string") { 
		if ( $(sGridID).jqGrid("getCell", rowID, baseColname) === baseValue) {
			for (var x in target){
				$(sGridID).jqGrid("setColProp", x, {editable:target[x]});
			}
		} else {
			for (var x in target){
				$(sGridID).jqGrid("setColProp", x, {editable:!target[x]});
			}
		};		
	} else if (typeof baseValue === "object") { //regx 인 경우
		if ( baseValue.test($(sGridID).jqGrid("getCell", rowID, baseColname)) ) {
			for (var x in target){
				$(sGridID).jqGrid("setColProp", x, {editable:target[x]});
			}
		} else {
			for (var x in target){
				$(sGridID).jqGrid("setColProp", x, {editable:!target[x]});
			}
		};
	}
	
};


//// Inline Editing v1.1 정의 시작 - CRUD 개념 포함 ////

/**
 * @description 그리드 동적 add  - Inline Editing v1.1
 * @version 1.1
 * @since 2013.01
 * @author 이병직
 * @class
 * @memberOf JQGRID
 * @param {Object} constructor
 * @param {String} constructor.gridID 그리드 ID
 * @param {Object} constructor.rowData 추가할 row data
 */
JQGRID.addRow = function (constructor) {
    var sGridID = "#" + constructor.gridID
    ,   rowData = constructor.rowData
    ,   rowID = $.now();
    
    if(!COMM._declareCheck(constructor, JQGRID._STANDARD.addRow)){
        return false;
    };
    
    rowData[JQGRID.CONSTANT.ROW_STAT_COL_MODEL] = "I";
    
    // 만약 조회데이터 없음 표시가 있으면 숨기고 (04.30 - if는 생략)
    $("#emptyrows_" + constructor.gridID).hide();
    $(sGridID).jqGrid("addRowData", rowID, rowData);
};

/**
 * @description 그리드 동적 edit  - Inline Editing v1.1
 * @version 1.1
 * @since 2013.03
 * @author 이병직
 * @class
 * @memberOf JQGRID
 * @param {Object} constructor
 * @param {String} constructor.gridID 그리드 ID
 * @param {String} constructor.rowID 추가할 rowID
 * @param {Object} [constructor.changeEditableCRUD] CRUD(row status)를 이용한 JQGRID.changEditable
 * @param {String} constructor.changeEditableCRUD.baseValue I,R,U.D
 * @param {Object} constructor.changeEditableCRUD.target 컬럼명과 변경값 {컬럼명:false, 컬럼명:true, ... } 
 * @param {Object} [constructor.changeEditable] JQGRID.changEditable + editrow
 * @param {String} constructor.changeEditable.baseColname 기준 컬럼명
 * @param {String|Regex} constructor.changeEditable.baseValue 기준값
 * @param {String} constructor.changeEditable.target 컬럼명과 변경값 {컬럼명:false, 컬럼명:true, ... }
 */
JQGRID.editRow = function (constructor) {
    var sGridID = "#" + constructor.gridID
    ,   rowID = constructor.rowID
    ,   editParameter = {
    		keys: true
    	,	oneditfunc: function(){
    			// 현재 id를 저장.
    	    	$(sGridID).jqGrid("setGridParam", {editRowRowid:rowID});
    			
    			// 상태를 판단함.
    			if( JQGRID.getRowStatus({gridID:constructor.gridID, rowID:rowID }) !== "I"){
    				JQGRID.setRowStatus({gridID:constructor.gridID, rowID:rowID, CRUD:"U" });
    			}
    		}
    	,   aftersavefunc: function() {
    		}
    	,   afterrestorefunc: function() {
    		}
    	};
    
    if(!COMM._declareCheck(constructor, JQGRID._STANDARD.editRow)){
        return false;
    };
    
    if (constructor.changeEditableCRUD) {
    	JQGRID.changeEditable({
	        gridID:constructor.gridID
	    ,   rowID:rowID
	    ,   baseColname:JQGRID.CONSTANT.ROW_STAT_COL_MODEL
	    ,   baseValue: constructor.changeEditableCRUD.baseValue
	    ,   target: constructor.changeEditableCRUD.target
	    });
    } else if (constructor.changeEditable) {
    	JQGRID.changeEditable({
	        gridID:constructor.gridID
	    ,   rowID:rowID
	    ,   baseColname: constructor.changeEditable.baseColname
	    ,   baseValue: constructor.changeEditable.baseValue
	    ,   target: constructor.changeEditable.target
	    });	
    }
    
    $(sGridID).jqGrid("editRow", rowID, editParameter );
    
};

/**
 * @description 그리드 동적 delete  - Inline Editing v1.1
 * @version 1.1
 * @since 2013.03
 * @author 이병직
 * @class
 * @memberOf JQGRID
 * @param {String} constructor
 * @param {String} constructor.gridID 그리드 ID
 * @param {String} constructor.rowID 삭제할 rowID
 */
JQGRID.deleteRow = function (constructor) {
    var sGridID =  "#" + constructor.gridID
    ,   rowID = constructor.rowID;
    
    if(!COMM._declareCheck(constructor, JQGRID._STANDARD.deleteRow)){
        return false;
    };
    
    $(sGridID).jqGrid("delRowData", rowID);
};


//// Inline Editing v1.1 정의 종료 ////
/**
 * @description 그리드에 직접 data 세팅
 * @version 1.0
 * @since 2013.01
 * @author 이병직
 * @class
 * @memberOf JQGRID
 * @param {Object} constructor
 * @param {String} constructor.gridID 그리드 ID
 * @param {Object|String} constructor.JSONData 그리드에 사용할 JSON Data
 * @param {String} constructor.pageURL 호출한 페이지 URL
 */
JQGRID.addJSONData = function (constructor){
    var JSONData
    ,   sGridID = "#" + constructor.gridID;
    
    if(!COMM._declareCheck(constructor, JQGRID._STANDARD.addJSONData)){
        return false;
    };
    
/*    if (typeof constructor.JSONData === "string") {
        JSONData = $.parseJSON(constructor.JSONData);
    } else if (typeof constructor.JSONData === "object") {
        JSONData = constructor.JSONData;
    } else {
        JSONData = {};
    }
    */
    //$("#" + constructor.gridID)[0].addJSONData(JSONData);
    
    if (typeof constructor.JSONData === "string") {
        JSONData = constructor.JSONData;
    } else if (typeof constructor.JSONData === "object") {
        JSONData = JSON.sstringify(constructor.JSONData);
    } else {
        JSONData = "";
    }
    
    // 수신 데이터 재송신 후 수신 (정렬 문제 회피)
    $(".ui-jqgrid-bdiv",$("#gview_" + constructor.gridID)).scrollLeft(0);
    $(sGridID).jqGrid("setGridParam",{postData: null}); 
    $(sGridID).jqGrid("setGridParam",{url: constructor.pageURL, postData:{jsondata:JSONData} , datatype:"json", page:1} ).trigger("reloadGrid");
    $(sGridID).jqGrid("setGridParam",{url: INITIALIZE_BASE + "/uxAction"} );
  
};

/**
 * @description 그리드 내부 버튼클릭시 getRow
 * @version 1.1
 * @since 2013.03
 * @author 이병직
 * @class
 * @memberOf JQGRID
 * @param {String} constructor
 * @param {String} constructor.gridID 그리드 ID
 * @param {Function} constructor.btnAction 버튼 action 함수
 * @param {String} constructor.btnClass 버튼 Class Name
 */
JQGRID.getRowBtn = function (constructor) {
    var sGridID =  "#" + constructor.gridID
    ,   btnAction = constructor.btnAction
    ,   dBtnClass =  "." + constructor.btnClass;
    
    if(!COMM._declareCheck(constructor, JQGRID._STANDARD.getRowBtn)){
        return false;
    };
    
    $(sGridID).on("click", dBtnClass, function(){
        var rowID = $(this).parent().parent().attr("id")
        //,   rowData = $(sGridID).jqGrid("getRowData", rowID); // // 04.25 커스텀 currency 타입 지원을 위해 변경.
        ,   rowData = JQGRID._getRowDataWithCustom(constructor.gridID, rowID); 
        
        if( $(this).hasClass("grid_btn_disable") ) {
            return false;
        } else {
            btnAction(rowData, rowID);
        }
    });
    
};

/**
 * @description 그리드 전체 및 부분 추출 (text를 추출)
 * @version 1.1
 * @since 2013.04.25
 * @author 이병직
 * @class
 * @memberOf JQGRID
 * @param {String} gridID
 * @param {String} rowid
 * @returns {Object|Object[]} rowData
 */
JQGRID.getRowData = function (gridID, rowid) {
    // return $("#" + gridID).jqGrid("getRowData"); // 04.25 커스텀 currency 타입 지원을 위해 변경.
	return JQGRID._getRowDataWithCustom(gridID, rowid);
};

/**
 * @description 그리드 record 반환
 * @version 1.0
 * @since 2013.01
 * @author 이병직
 * @class
 * @memberOf JQGRID
 * @param {String} gridID
 * @returns {Number} records
 */
JQGRID.getRecord = function (gridID) {
    return  $("#" + gridID).jqGrid("getGridParam", "records");
};

/**
 * @description colModel 속성 editoptions의 속성 dataEvents 내부에서 사용 
 * @version 1.0
 * @since 2013.01
 * @author 이병직
 * @class
 * @memberOf JQGRID
 */
JQGRID.editoptionsDataEvent = {
	/**
	 * @description rowID 접근을 위해 사용
	 * @version 1.0
	 * @since 2013.01
	 * @author 이병직
	 * @function
	 * @param {Object} that this 객체
	 * @returns {String} row ID
	 */
    getRowID: function (that){
        return $(that).parent().parent().attr("id");
    }
};

/**
 * @description GRD type 을 통한 그리드 내부 select
 * @version 1.0
 * @since 2013.01
 * @author 이병직
 * @class
 * @memberOf JQGRID
 * @param {Object[]} GRDdata GRD 타입 수신데이터
 * @param {Object} constructor.selectValueMapping {셀렉트를 그릴 column 이름:[수신 data 중 들어갈 값], ...반복}
 * @param {Object} constructor.selectTextMapping {셀렉트를 그릴 column 이름:[수신 data 중 들어갈 값], ...반복}
 * @param {String} constructor.gridID 그리드 ID
 * @param {Object} constructor.onChangeSetValueMapping 셀렉트 선택시 grid cell value mapping --- {셀렉트를 그린 column 이름: 바꿀 대상: [바꿀 값]}
 * @param {Object} constructor.onChangeDelValueMapping 셀렉트 선택시 타겟 셀렉트를 변경(option삭제)
 * @param {Object} constructor.onChangeSameTextCheck 셀렉트 선택시 중복 text 체크
 * @param {String} constructor.onChangeSameTextID 셀렉트 선택시 중복 text 발견시 메세지 ID
 * @param {Function} constructor.onChangeSameTextMessage 셀렉트 선택시 중복 text 발견시 메세지
 * @param {String} constructor.choiceText 선택 option 텍스트
 * @param {String} constructor.choiceValue 선택 option 벨류
 * @param {Object} constructor.onChangeEditable 선택시 에디트 가능
 * @param {Function} constructor.onChangeEvent 선택시 개발자 이벤트 정의
 * @example
 * JQGRID.insideSelect(data, {
 * // 대상: value - text. 둘의 크기와 속성 이름이 같아야 한다.
 * // value 값은 항상 달라야 한다. (json을 썼으므로 hashMap 와 같다)
 * // 셀렉트를 그릴 column 이름: [수신 data 중에서 들어갈 값]
 *   selectTextMapping: {"ja_acnt_seq_no":["[","com_cd_nm","]"," ","smark_acnt_no"]} 
 * , selectValueMapping:{"ja_acnt_seq_no":["acnt_seq_no"]}
 * // 셀렉트를 그린 column 이름: 바꿀 대상: [바꿀 값]
 * , onChangeSetValueMapping:onChangeSetValue
 * , onChangeDelValueMapping:{}
 * // 셀렉트를 그린 column 이름: 비교할 column 이름들
 * , onChangeSameTextCheck:{"ja_acnt_seq_no":["mo_acnt_seq_no"]}
 * // 셀렉트를 그린 column 이름: 이벤트 handler
 * //,onChangeEvent:{"":""}
 * , gridID: "${_PGM_ID_}grid"
 * , onChangeSameTextID:"W9990"
 * , onChangeSameTextMessage: function (msg) { 
 * 		msg = '<%=UCS.Cont("MSGCDW0064", request)%>';
 *   	return msg; 
 * }
 * , onChangeEditable: {"ja_acnt_seq_no":["ja_cmon_type"]}
 * , onChangeEvent: function(rowID, e){  
 * 	   //중복리스트 체크
 * 	   if(!sameListConfirm(rowID, e)){
 *         for (var csvName in onChangeSetValue[e.currentTarget.name]) {
 *             $("#${_PGM_ID_}grid").jqGrid("setCell", rowID, csvName, null);
 *         }
 *     }
 * }
 */
JQGRID.insideSelect = function ( GRDdata, constructor ) {
    var choiceText = constructor.choiceText || _UCS.COMMON3150
    ,   defaultChoiceValue = constructor.choiceValue || "default_choice"
    ,   rowNum = GRDdata.rowNum
    ,   rows = GRDdata.rows
    ,   selectValue = constructor.selectValueMapping
    ,   selectText = constructor.selectTextMapping
    ,   onChangeSetValue = constructor.onChangeSetValueMapping
    ,   onChangeSameTextCheck = constructor.onChangeSameTextCheck
    ,   onChangeSameTextMessage = constructor.onChangeSameTextMessage // return message 
    ,   onChangeSameTextID = constructor.onChangeSameTextID // return message ID 
    ,   onChangeEditable = constructor.onChangeEditable 
    ,   onChangeEvent = constructor.onChangeEvent 
    ,   gridID = constructor.gridID
    //,   prevText = null
    ,   selectState = {} // ajax 를 통합하지 않는 한, global 이어야 다른 개체 접근가능.
    // 예시
    /* 전역을 사용할 시*/
    /*     selectState = {
     *       그리드ID 
                ja_crp_biz_name:{  // select box
                    "value": "on",
                    "value": "on"
                }
                ... // select box 계속
             그리드 ID
                ...
        } */
    /* 지역을 사용할 시*/  // 
    /*     selectState = {
                ja_crp_biz_name:{  // select box
                    "value": "on",
                    "value": "on"
                }
                ... // select box 계속
             그리드 ID
                ...
        } */
    ,   editOpValueModel = {}
    ,   editOpValue = {}
    ,   value = null
    ,   text = null
    ,   sGridID = "#" + constructor.gridID
    ,   constructorCheck = false
    // text 조합 (객체에서 배열이름이 있으면) 
    ,   combinationText = function (obj, array) {
            var combiText = "";
            for (var x = 0; x < array.length; x += 1) {
                if ( obj.hasOwnProperty(array[x])) {
                    combiText += obj[array[x]];
                } else {
                    combiText += array[x];  
                }
            }
            return combiText;
        }
    ,   editOpValueModelCopy = function (stName) {
            editOpValue = {};
            for (var x in editOpValueModel[stName]) {
                if (selectState[stName][x] === "off") {
                    editOpValue[x] =  editOpValueModel[stName][x];
                }
            }
            return editOpValue;
        }
    ,   customSetColProp = function (sGridID, stName) {
            var editOptionValue = editOpValueModelCopy(stName);
            $(sGridID).setColProp( stName, {
                editoptions: {
                    value: editOptionValue
                ,   dataEvents: [
                        { type:"change", fn:function(e) {
                            var that = e.currentTarget //또는 this
                            ,   name = that.name // 또는 this.name
                            ,   thisRow = $(this).parent().parent()
                            ,   rowID = thisRow.attr("id")
                            ,   selected = that.selectedIndex
                            ,   rowIndex = selected -1 // 선택을 넣었기 때문
                            ,   svcArray = onChangeSameTextCheck[name]
                            ,   thisText = that[selected].text
                            ,   thisValue = that[selected].value;
                            //,   beforeEditCell = JQGRID.global.beforeEditCellObj;
                            //,   rowNum = $("tbody tr", $(sGridID)).index(thisRow); // 선택된 tr의 순번
                            
                            
                            for (var csvName in onChangeSetValue[name]) {
                                if (thisValue !== defaultChoiceValue) {
                                   	if("" == combinationText(rows[rowIndex], onChangeSetValue[name][csvName])){
                                		$(sGridID).jqGrid("setCell", rowID, csvName, null);
                                	}else{
                                		$(sGridID).jqGrid("setCell", rowID, csvName, ( combinationText(rows[rowIndex], onChangeSetValue[name][csvName]) ));
                                	}
                                } else {
                                    $(sGridID).jqGrid("setCell", rowID, csvName, null);
                                }
                            }
                            
                            for ( var x = 0; x < onChangeEditable[name].length; x += 1) {
                                if (thisValue !== defaultChoiceValue) {
                                    // 세로 열 전체가 적용. 가로열 적용은 셀단위로 (options Event onSelectCell 등을 쓸 것)
                                    //$(sGridID).jqGrid("setColProp", onChangeEditable[name][x], {editable:true});
                                    $(sGridID).jqGrid("setCell", rowID, onChangeEditable[name][x], "0");
                                } else {
                                    //$(sGridID).jqGrid("setColProp", onChangeEditable[name][x], {editable:false});
                                    $(sGridID).jqGrid("setCell", rowID, onChangeEditable[name][x], null);
                                }
                            }
                            
                            for (var x = 0; x < svcArray.length; x += 1) {
                                // 중복 제거
                                //selectState[svcArray[x]][thisValue] = "on"; // editOpValueModel도 전역이어야 가능(1/4)
/*                                for (var z in editOpValueModel[stName]) {
                                    if ( editOpValueModel[stName][z] === prevText){
                                        selectState[stName][z] = "off";
                                        break;
                                    }
                                selectState[stName][thisValue] = "on";
                                prevText = editOpValueModel[stName][thisValue];*/
                                
                                if ( thisText === $(sGridID).jqGrid("getCell", rowID, svcArray[x]) && thisText !== choiceText){
                                    if (!onChangeSameTextID) {
                                        onChangeSameTextID = "W9990";
                                    }
                                    if (!onChangeSameTextMessage) {
                                        onChangeSameTextMessage = function(text) { return text + "가 중복됬습니다"; };
                                    }
                                    //$(sGridID).jqGrid("setCell", rowID, stName, defaultChoiceValue);
                                    that.selectedIndex = 0;
                                    for (var csvName in onChangeSetValue[name]) {
                                        $(sGridID).jqGrid("setCell", rowID, csvName, null);
                                    }
                                    COMM.dialog({ 
                                           type:"warning"
                                       ,   id: onChangeSameTextID
                                       ,   message: onChangeSameTextMessage(thisText)
                                    });
                                }
                                //customSetColProp(sGridID, stName);
                            } 
                            
                            // 개발자 정의 이벤트
                            if(onChangeEvent) { onChangeEvent(rowID, e); }
                            
                            // 선택된 값을 반영 (2.19)
                            JQGRID.editCellClosed( {gridID:gridID, closedAction:function(){
                                // 또는 cloasedAction 함수 부분만 제외 할 것. (03.18)
                                $(sGridID).jqGrid("setCell", rowID, name, that[that.selectedIndex].value);
                            }} );
                            
                        }} // 이벤트 발생
                    ]
                }, // editoptions 
                formatter: function (cellvalue, options, rowObject) {
                    if (cellvalue === choiceText) {
                        return cellvalue;
                    }
                    for (var x in editOpValueModel[stName]) {
                        if (x === cellvalue) {
                            return editOpValueModel[stName][x];
                        }
                    }
                    return null;
                }
            }); // setColProp
        }; // customSetColProp
    
    //  constructor 속성 검사
    COMM._declareCheck(constructor, JQGRID._STANDARD.insideSelect);   
    
    // 전역 사용시
    //JQGRID.global.selectState[gridID] = JQGRID.global.selectState[gridID] || {};
    //selectState = JQGRID.global.selectState[gridID]; // 복사아닌 참조
    
    // 선언 체크
    for (var x in selectValue) {
       constructorCheck = false;
       for (var y in selectText) {
         if ( x === y) {
            constructorCheck = true;
            break; 
         }  
       } 
       if (!constructorCheck) {
           COMM.dialog({
               type: COMM._INIT.DIALOG.typeDebug
           ,   id: "EU014"
           });
           return false;
       }
    }

    // 생성
    for (var stName in selectText) {
        editOpValueModel[stName] = {};
        editOpValueModel[stName][defaultChoiceValue] = choiceText;
        selectState[stName] = {};
        selectState[stName][defaultChoiceValue] = "off";
        
        for ( var x = 0; x < rowNum; x += 1 ) {
            value = combinationText( rows[x], selectValue[stName]);
            text = combinationText( rows[x], selectText[stName]);
            if (selectState[stName][value]) {
                COMM.dialog({
                       type:COMM._INIT.DIALOG.typeDebug
                   ,   id: "DEU015"
                });
            }
            selectState[stName][value] = "off";
            editOpValueModel[stName][value] = text;
        }
        //propView(editOpValueModel);
        //propView(selectState[stName]);
        customSetColProp(sGridID, stName);
    } // for
    
};

/**
 * @description sub tabs 스크립트
 * <br/>
 * 주의! 적용 페이지의 구조가 subTabs 구조를 따라야 한다. - UE1010020.jsp 참고
 * <br/>
 * 탭 구역이 모두 같을 경우에 정상동작 하도록 수정- 선택된 그리드만 재설정. (마지막탭 크기-> 새탭크기)(02.04)
 * @version 1.0
 * @since 2013.01.07
 * @author 이병직
 * @class
 * @memberOf JQGRID
 * @param {String} subtabsID
 */
JQGRID.subtabs = function (subtabsID) {
    var sSubtabsId = "#" + subtabsID
    ,   $subContents = $(sSubtabsId + " > div")
    ,   $subList = $(sSubtabsId + " > ul > li > h2 > a")
    ,   listHref = [];
    
    $subList.each(function(clickIndex){
        var target = $(this).attr("href") 	// 1.9.1 버전에서 attr(값 그대로) 과 prop(절대) 의 차이가 있음
        ,   sGridID = "#" + $(target + " > div > div > div").next().children("table:first").attr("id") // 그리드 생성전에 id를 구함. (JQGRID.subtab은 grid생성전에 호출해야 한다.)
        ,   gridWidth = 0
        ,   gridHeigth = 0;
        
        listHref[clickIndex] = sGridID;
        
        $(this).click(function(event){
            event.preventDefault();
            $(".sub_tab_focus", $(sSubtabsId)).removeClass("sub_tab_focus");
            $(this).parent().addClass("sub_tab_focus");
            $subContents.each(function(index){
                if ( $(this).is(":visible") ){
                    $(this).hide();
                    gridWidth = $(listHref[index]).jqGrid("getGridParam", "width"); // oldTab 값 (열려있던 탭)
                    gridHeigth = $(listHref[index]).jqGrid("getGridParam", "height");
                }
            });

            JQGRID._setGridWidthFalse(sGridID, gridWidth);	// newTab 설정 (새로 열린 탭)
            $(sGridID).jqGrid("setGridHeight", gridHeigth);
            $(target).show();
        });
        
        
    });
};

/**
 * @description  multiselect checkedToColumn
 * <br/>
 * multiselect checked 를 column 에 Y/N 으로 세팅
 * @version 1.0
 * @since 2013.01
 * @author 이병직
 * @class
 * @memberOf JQGRID
 * @param {Object} constructor
 * @param {String} constructor.gridID 그리드 ID
 * @param {String} constructor.colName 변환할 colName
 */
JQGRID.checkedToColumn = function (constructor) {
    var sGridID = "#" + constructor.gridID
    ,   gridID = constructor.gridID
    ,   dataIDs = $(sGridID).jqGrid("getDataIDs")
    ,   colName = constructor.colName
    ,   sCboxID = "";
 
    COMM._declareCheck(constructor, JQGRID._STANDARD.checkedToColumn);  

    JQGRID.globalClear();
    
    for (var x = 0, l = dataIDs.length; x < l; x += 1) {
        sCboxID = "#jqg_" + gridID + "_" + dataIDs[x];

        if($(sCboxID).is(":visible")){
            if ($(sCboxID).is(":checked")){
                $(sGridID).jqGrid("setCell", dataIDs[x], colName, "Y");
            } else {
                $(sGridID).jqGrid("setCell", dataIDs[x], colName, "N");
            }
        }else{
        	$(sGridID).jqGrid("setCell", dataIDs[x], colName, "M");
        }

    }
};

/**
 * @description  마지막 편집 창 닫기
 * <br/>
 * 개발시 참고 $("tbody > tr > td.jqgrid-rownum:first", $(id)).trigger("click"); (multiselect:true 일 때만 동작)
 * @version 1.1
 * @since 2013.05.06
 * @author 이병직
 * @class
 * @memberOf JQGRID
 * @param {Object} constructor
 * @param {String} constructor.gridID 그리드 ID
 * @param {Function} constructor.closedAction 닫은 후 실행할 action
 * @param {Object} constructor.actionData action에 사용할 데이타
 */
JQGRID.editCellClosed = function( constructor ) {
    var sGridID = "#" + constructor.gridID
    ,   iRow = $(sGridID).jqGrid("getGridParam", "editCelliRow")
    ,   iCol = $(sGridID).jqGrid("getGridParam", "editCelliCol");
    
    if(!COMM._declareCheck(constructor, JQGRID._STANDARD.editCellClosed)){
        return false;
    };
    if (iRow === -1 || iCol === -1) { // 정상종료
        constructor.closedAction(constructor.actionData);
    } else {
        $(sGridID).jqGrid("editCell", iRow, iCol, false ); // 닫고
        
        //2013.05.06 1.9.1 및 4.4.5 버전업 후 다음 부분을 동일하게 처리.
        //또한 ajax 이벤트는 document에서 사용하도록 변경되었다. 
        
        // 이전 작성 코드
/*        if ($(sGridID).jqGrid("getGridParam", "editCellMode") === "restore") {
            constructor.closedAction(constructor.actionData);
        } else {
        	// data-none.grid 로 ajax 통신 후 edit 종료
            COMM._ajaxStopOnce(sGridID, constructor.actionData, constructor.closedAction); <-- 1.8.2 방식 (1.9.1 에선 sGridID가 삭제)
        }*/
        
        // 2013.05.06 코드
        constructor.closedAction(constructor.actionData);
        
    }
    
};

//////// colModel custom function 부분 - 각 row 마다 실행된다는 점을 주의! ////////
/**
 * @description ColModel 속성 editrules 안에서 사용
 * @version 1.0
 * @since 2013.01
 * @author 이병직
 * @class
 * @memberOf JQGRID
 */
JQGRID.editrules = {
	/**
	 * @description 3자리 체크
	 * @version 1.0
	 * @since 2013.01
	 * @author 이병직
	 * @function
	 */
    number3: {
        custom:true
    ,   custom_func: function(data, colname){
            if(COMM.isNumber(data)){
                if (data.length > 3) {
                    return [false, "3자리를 초과 했습니다."];
                }
            } else {
                return [false, "숫자가 아닙니다"];
            }
            return [true];
        }
    }
};

/**
 * @description 보고서 부분- 현황에 사용
 * <br/>
 * ColModel 속성 cellattr 에서 사용
 * @version 1.1
 * @since 2013.04.17
 * @author 이병직
 * @class
 * @memberOf JQGRID
 */
JQGRID.cellattr = {
	/**
	 * @description value 저장소 - 그리드row별로 반복되므로 그리드 별로 다른 값을 가진다.
	 * @version 1.1
	 * @since 2013.04.17
	 * @author 이병직
	 * @private
	 */	
	    value: [],
	/**
	 * @description sCellID 임시저장
	 * @version 1.1
	 * @since 2013.04.17
	 * @author 이병직
	 * @private
	 */	
	    sCellID: "",
	    
    /**
     * @description 동작 카운터
     * @version 1.1
     * @since 2013.05.31
     * @author 이병직
     * @private
     */	
	    counter: 0,
	    
	    // rowObj {}- 행 한 줄 
	    // cm {} 	- 적용한 colmodel
	/**
	 * @description 반드시 마지막 행은 다른 val여야 한다. (예. 소계, 합계)
	 * @version 1.1
	 * @since 2013.04.17
	 * @author 이병직
	 * @function
	 */	
	    cellMerge:  function (rowID, val, rawObj, cm, rdata) { 
	    	
	    	var d = new Date();
	    	
	        var value = JQGRID.cellattr.value
	        ,   vl = 0
        	// 페이지별 고유한 id를 생성하기 위해 gridId를 이용함. (또는 시간을 이용할 것 04.29)
	        // ,   gridId = cm.gridID;
	        // colModel에는 그리드ID가 존재하지 않아 undefined가 들어가므로 페이지별로 고유한 id가 생성되지 않음
	        // 시간을 사용하도록 수정 (2015.09.24)
	        ,   gridId = "" + d.getFullYear() + (d.getMonth() + 1) + d.getDate() + d.getHours() + d.getMinutes() + d.getSeconds() + d.getMilliseconds();
	        
	        value.push(val);
	        vl = value.length;
	        
	        if (vl === 1 && val !== "&#160;") { // 빈 cell(&#160;)
	        	JQGRID.cellattr.sCellID = "#start_acnt_type_" + rowID + gridId;
	            return 'id="start_acnt_type_' + rowID + gridId + '"';
	        } else if (vl >= 2 && value[vl-2] !== value[vl-1]) {
	            $(JQGRID.cellattr.sCellID).attr("rowspan", vl - 1 );
	            if (val !== "&#160;") {
	            	JQGRID.cellattr.value =[val]; //변경된 값
	            } else {
	            	JQGRID.cellattr.value =[];
	            }
	            JQGRID.cellattr.sCellID = "#start_acnt_type_" + rowID + gridId;
	            return 'id="start_acnt_type_' + rowID + gridId + '"';
	        } else if (val !== "&#160;"){	// 빈 값이면 else 문으로 넘어가서 그냥 보여준다.  (2013.04.29)
	            return "class=\"display_none\"";
	        } else {
	        	return "";
	        }
	    },
	    
	/**
	 * @description 보고서 acntType 와 달리 마지막 행값이 동일한 경우에도 동작하는 함수
	 * <br/>
	 * 주의!마지막 행을 구하기 위해 총 건수를 사용하므로, paging을 사용하면 비정상 동작할 수 있다.(record는 그려진 후 셋되기 때문에 rowNum 사용) 
	 * <br/>
	 *       또한 recieveRowNum 은 beforeSuccess 함수를 사용하므로 min 파일 수정여부 확인이 필요하다.(업그레이드시) 
	 * @version 1.1
	 * @since 2013.05.31
	 * @author 이병직
	 * @function
	 */	
	    mergeRow:  function (rowID, val, rawObj, cm, rdata) { 
	        var value = JQGRID.cellattr.value
	        ,   vl = 0
	        ,   gridId = cm.gridID	// 페이지별 고유한 id를 생성하기 위해 gridId를 이용함. (또는 시간을 이용할 것 04.29)
	        ,   recieveRowNum = $("#"+gridId).jqGrid("getGridParam","recieveRowNum");
	        
	        value.push(val);
	        vl = value.length;
	        JQGRID.cellattr.counter += 1;
	        
	        // recieveRowNum 을 가져올 수 없는 경우 (2013.06.03)
	        // 화면이 깨지지 않도록 colSpan 동작하지 않도록 처리함.
	        if(!recieveRowNum){
	        	if(DEBUG_MODE){
	        		console.assert(recieveRowNum, "recieveRowNum 이 정의되지 않음.");
	        	}
	        	return "";
	        }
	        
	        if (vl === 1 && val !== "&#160;") { // 빈 cell(&#160;)
	        	JQGRID.cellattr.sCellID = "#start_acnt_type_" + rowID + gridId;
	            return 'id="start_acnt_type_' + rowID + gridId + '"';
	        } else if (vl >= 2 && value[vl-2] !== value[vl-1]) {
	            $(JQGRID.cellattr.sCellID).attr("rowspan", vl - 1 );
	            if (val !== "&#160;") {
	            	JQGRID.cellattr.value =[val]; //변경된 값
	            } else {
	            	JQGRID.cellattr.value =[];
	            }
	            JQGRID.cellattr.sCellID = "#start_acnt_type_" + rowID + gridId;
	            return 'id="start_acnt_type_' + rowID + gridId + '"';
	        } else if (JQGRID.cellattr.counter === recieveRowNum){ // recieveRowNum 값에 의존하므로 beforeSuccess 가 필요!
	        	$(JQGRID.cellattr.sCellID).attr("rowspan", vl );
	        	JQGRID.cellattr.value =[];
	        	JQGRID.cellattr.counter=0;
	        	return "class=\"display_none\"";
	        } else if (val !== "&#160;"){	// 빈 값이면 else 문으로 넘어가서 그냥 보여준다.  (2013.04.29)
	        	return "class=\"display_none\"";
	        } else {
	        	
	        	return "";
	        }
	    }
};

/**
 * @description ColModel 속성 formatter 에서 사용
 * @version 1.0
 * @since 2013.01
 * @author 이병직
 * @class
 * @memberOf JQGRID
 */
JQGRID.formatter = {
	/**
	 * @description CRUD 상태 
	 * @version 1.1
	 * @since 2013.03
	 * @author 이병직
	 * @function
	 */
	CRUD: function (cellvalue, options, rowObject){
		if (JQGRID.CONSTANT.ROW_STAT_COL_REGX.test(cellvalue)) {
        	return cellvalue;
        } else {
        	return "R";
        }
	},
		
	/**
	 * @description 컬럼값을 grid_btn01.png 버튼으로 변경
	 * @version 1.0
	 * @since 2013.01
	 * @author 이병직
	 * @function
	 */
    grid_btn01: function (cellvalue, options, rowObject){
        var btn = '<img src ="'+ INITIALIZE_BASE_IMG + '/grid_btn01.png" class="grid_btn01 btn_cursor"/>';
        return btn;
    },
    
	/**
	 * @description 컬럼값을 grid_btn02.png 버튼으로 변경
	 * <br/>
	 * cell 값이 "N" 이면 grid_btn02_off.png 버튼으로 변경
	 * @version 1.0
	 * @since 2013.01
	 * @author 이병직
	 * @function
	 */
    grid_btn02: function (cellvalue, options, rowObject){
        var btn ="";
        if (cellvalue === "N") {
            btn = '<img src ="'+ INITIALIZE_BASE_IMG + '/grid_btn02_off.png" class="grid_btn02 grid_btn_disable"/>';
        } else {
            btn = '<img src ="'+ INITIALIZE_BASE_IMG + '/grid_btn02.png" class="grid_btn02 btn_cursor"/>';
        }
        
        return btn;
    },
    
	/**
	 * @description 컬럼값을 grid_btn03.png 버튼으로 변경
	 * @version 1.0
	 * @since 2013.01
	 * @author 이병직
	 * @function
	 */
    grid_btn03: function (cellvalue, options, rowObject){
        var btn = '<img src ="'+ INITIALIZE_BASE_IMG + '/grid_btn03.png" class="grid_btn03 btn_cursor"/>';
        return btn;
    },
    
	/**
	 * @description 컬럼값을 grid_btn04.png 버튼으로 변경
	 * <br/>
	 * cell 값이 "B" 이면 grid_btn04_off.png 버튼으로 변경
	 * @version 1.0
	 * @since 2013.01
	 * @author 이병직
	 * @function
	 */
    grid_btn04: function (cellvalue, options, rowObject){
        var btn ="";
        if (cellvalue === "B") {
            btn = '<img src ="'+ INITIALIZE_BASE_IMG + '/grid_btn04_off.png" class="grid_btn04 grid_btn_disable"/>';
        } else {
            btn = '<img src ="'+ INITIALIZE_BASE_IMG + '/grid_btn04.png" class="grid_btn04 btn_cursor"/>';
        }
        return btn;
    },
    
	/**
	 * @description 컬럼값을 grid_btn05.png 버튼으로 변경
	 * @version 1.0
	 * @since 2013.01
	 * @author 이병직
	 * @function
	 */
    grid_btn05: function (cellvalue, options, rowObject){
        var btn = '<img src ="'+ INITIALIZE_BASE_IMG + '/grid_btn05.png" class="grid_btn05 btn_cursor"/>';
        return btn;
    },
    
	/**
	 * @description 컬럼값을 grid_btn06.png 버튼으로 변경
	 * @version 1.0
	 * @since 2013.01
	 * @author 이병직
	 * @function
	 */
    grid_btn06: function (cellvalue, options, rowObject){
        var btn = '<img src ="'+ INITIALIZE_BASE_IMG + '/grid_btn06.png" class="grid_btn06 btn_cursor"/>';
        return btn;
    },
    
	/**
	 * @description 컬럼값을 grid_btn07.png 버튼으로 변경
	 * @version 1.0
	 * @since 2013.01
	 * @author 이병직
	 * @function
	 */
    grid_btn07: function (cellvalue, options, rowObject){
        var btn = '<img src ="'+ INITIALIZE_BASE_IMG + '/grid_btn07.png" class="grid_btn07 btn_cursor"/>';
        return btn;
    },
    
	/**
	 * @description 컬럼값을 grid_btn08.png 버튼으로 변경
	 * @version 1.0
	 * @since 2013.01
	 * @author 이병직
	 * @function
	 */
    grid_btn08: function (cellvalue, options, rowObject){
        var btn = '<img src ="'+ INITIALIZE_BASE_IMG + '/grid_btn08.png" class="grid_btn08 btn_cursor"/>';
        return btn;
    },
    
	/**
	 * @description 컬럼값을 grid_btn09.png 버튼으로 변경
	 * @version 1.0
	 * @since 2013.01
	 * @author 이병직
	 * @function
	 */
    grid_btn09: function (cellvalue, options, rowObject){
        var btn = '<img src ="'+ INITIALIZE_BASE_IMG + '/grid_btn09.png" class="grid_btn09 btn_cursor"/>';
        return btn;
    },
    
	/**
	 * @description 컬럼값을 grid_btn10.png 버튼으로 변경
	 * @version 1.0
	 * @since 2013.01
	 * @author 이병직
	 * @function
	 */
    grid_btn10: function (cellvalue, options, rowObject){
        var btn = '<img src ="'+ INITIALIZE_BASE_IMG + '/grid_btn10.png" class="grid_btn10 btn_cursor"/>';
        return btn;
    },
    
	/**
	 * @description 컬럼값을 grid_btn10_off.png 버튼으로 변경
	 * @version 1.0
	 * @since 2013.01
	 * @author 이병직
	 * @function
	 */
    grid_btn10_off: function (cellvalue, options, rowObject){
        var btn = '<img src ="'+ INITIALIZE_BASE_IMG + '/grid_btn10_off.png" class="grid_btn10_off btn_cursor"/>';
        return btn;
    },
    
	/**
	 * @description 컬럼값을 grid_btn11.png 버튼으로 변경
	 * @version 1.0
	 * @since 2013.01
	 * @author 이병직
	 * @function
	 */
    grid_btn11: function (cellvalue, options, rowObject){
        var btn = '<img src ="'+ INITIALIZE_BASE_IMG + '/grid_btn11.png" class="grid_btn11 btn_cursor"/>';
        return btn;
    },
    
	/**
	 * @description 컬럼값을 grid_btn12.png 버튼으로 변경
	 * @version 1.0
	 * @since 2013.01
	 * @author 이병직
	 * @function
	 */
    grid_btn12: function (cellvalue, options, rowObject){
        var btn = '<img src ="'+ INITIALIZE_BASE_IMG + '/grid_btn12.png" class="grid_btn12 btn_cursor"/>';
        return btn;
    },
    
	/**
	 * @description 컬럼값을 grid_btn13.png 버튼으로 변경
	 * @version 1.0
	 * @since 2013.01
	 * @author 이병직
	 * @function
	 */
    grid_btn13: function (cellvalue, options, rowObject){
        var btn = '<img src ="'+ INITIALIZE_BASE_IMG + '/grid_btn13.png" class="grid_btn13 btn_cursor"/>';
        return btn;
    },
    
	/**
	 * @description 컬럼값을 grid_btn14.png 버튼으로 변경
	 * @version 1.0
	 * @since 2013.01
	 * @author 이병직
	 * @function
	 */
    grid_btn14: function (cellvalue, options, rowObject){
        var btn = '<img src ="'+ INITIALIZE_BASE_IMG + '/grid_btn14.png" class="grid_btn14 btn_cursor"/>';
        return btn;
    },
    
	/**
	 * @description 컬럼값을 grid_btn15.png 버튼으로 변경
	 * @version 1.0
	 * @since 2013.01
	 * @author 이병직
	 * @function
	 */
    grid_btn15: function (cellvalue, options, rowObject){
        var btn = '<img src ="'+ INITIALIZE_BASE_IMG + '/grid_btn15.png" class="grid_btn15 btn_cursor"/>';
        return btn;
    },
    
	/**
	 * @description 컬럼값을 grid_btn16.png 버튼으로 변경
	 * @version 1.0
	 * @since 2013.01
	 * @author 이병직
	 * @function
	 */
	grid_btn16: function (cellvalue, options, rowObject){
        var btn = '<img src ="'+ INITIALIZE_BASE_IMG + '/grid_btn16.png" class="grid_btn16 btn_cursor"/>';
        return btn;
    },
    
	/**
	 * @description 컬럼값을 grid_btn17.png 버튼으로 변경
	 * @version 1.0
	 * @since 2013.01
	 * @author 이병직
	 * @function
	 */
	grid_btn17: function (cellvalue, options, rowObject){
        var btn = '<img src ="'+ INITIALIZE_BASE_IMG + '/grid_btn17.png" class="grid_btn17 btn_cursor"/>';
        return btn;
    },
    
	/**
	 * @description 컬럼값을 grid_btn20.png 버튼으로 변경
	 * @version 1.0
	 * @since 2013.01
	 * @author 이병직
	 * @function
	 */
    grid_btn20: function (cellvalue, options, rowObject){
        var btn = '<img src ="'+ INITIALIZE_BASE_IMG + '/grid_btn20.png" class="grid_btn20 btn_cursor"/>';
        return btn;
    },
    
	/**
	 * @description 컬럼값을 grid_btn21.png 버튼으로 변경
	 * @version 1.0
	 * @since 2013.01
	 * @author 이병직
	 * @function
	 */
    grid_btn21: function (cellvalue, options, rowObject){
        var btn = '<img src ="'+ INITIALIZE_BASE_IMG + '/grid_btn21.png" class="grid_btn21 btn_cursor"/>';
        return btn;
    },
    
	/**
	 * @description 컬럼값을 grid_btn21_off.png 버튼으로 변경
	 * @version 1.0
	 * @since 2013.01
	 * @author 이병직
	 * @function
	 */
    grid_btn21_off: function (cellvalue, options, rowObject){
        var btn = '<img src ="'+ INITIALIZE_BASE_IMG + '/grid_btn21_off.png" class="grid_btn21_off btn_cursor"/>';
        return btn;
    },
    
	/**
	 * @description 컬럼값을 grid_btn22.png 버튼으로 변경
	 * @version 1.0
	 * @since 2013.01
	 * @author 이병직
	 * @function
	 */
    grid_btn22: function (cellvalue, options, rowObject){
        var btn = '<img src ="'+ INITIALIZE_BASE_IMG + '/grid_btn21.png" class="grid_btn22 btn_cursor"/>';
        return btn;
    },
    
	/**
	 * @description 컬럼값을 grid_btn22_off.png 버튼으로 변경
	 * @version 1.0
	 * @since 2013.01
	 * @author 이병직
	 * @function
	 */
    grid_btn22_off: function (cellvalue, options, rowObject){
        var btn = '<img src ="'+ INITIALIZE_BASE_IMG + '/grid_btn21_off.png" class="grid_btn22_off btn_cursor"/>';
        return btn;
    },
    
	/**
	 * @description 컬럼값을 grid_btn23.png 버튼으로 변경
	 * @version 1.0
	 * @since 2013.01
	 * @author 이병직
	 * @function
	 */
    grid_btn23: function (cellvalue, options, rowObject){
        var btn = '<img src ="'+ INITIALIZE_BASE_IMG + '/grid_btn23.png" class="grid_btn23 btn_cursor"/>';
        return btn;
    },
    
	/**
	 * @description 컬럼값을 grid_btn23_off.png 버튼으로 변경
	 * @version 1.0
	 * @since 2013.01
	 * @author 이병직
	 * @function
	 */
    grid_btn23_off: function (cellvalue, options, rowObject){
        var btn = '<img src ="'+ INITIALIZE_BASE_IMG + '/grid_btn21_off.png" class="grid_btn23_off btn_cursor"/>';
        return btn;
    },
    
	/**
	 * @description 컬럼값을 grid_btn24.png 버튼으로 변경
	 * @version 1.0
	 * @since 2013.01
	 * @author 이병직
	 * @function
	 */
    grid_btn24: function (cellvalue, options, rowObject){
        var btn = '<img src ="'+ INITIALIZE_BASE_IMG + '/grid_btn21.png" class="grid_btn24 btn_cursor"/>';
        return btn;
    },
    
	/**
	 * @description 컬럼값을 grid_btn24_off.png 버튼으로 변경
	 * @version 1.0
	 * @since 2013.01
	 * @author 이병직
	 * @function
	 */
    grid_btn24_off: function (cellvalue, options, rowObject){
        var btn = '<img src ="'+ INITIALIZE_BASE_IMG + '/grid_btn21_off.png" class="grid_btn24_off btn_cursor"/>';
        return btn;
    },
    
    /**
	 * @description 아이콘으로 변경
	 * <br/>
	 * cell 값이 "Y" 면 fwf-icon-ok 아이콘
	 * <br/>
	 * cell 값이 "N" 면 fwf-icon-error 아이콘
	 * <br/>
	 * 기타값은 ""
	 * @version 1.0
	 * @since 2013.01
	 * @author 이병직
	 * @function
	 */
    iconState: function (cellvalue, options, rowObject){
        var btn = "";
        if (cellvalue === "N") {
            btn = '<span class="ui-icon fwf-icon-ok"></span>';
        } else if (cellvalue === "Y") {
            btn = '<span class="ui-icon fwf-icon-error"></span>';
        }
        return btn;
    },
     
    /**
 	 * @description 결재선상태아이콘으로 변경
 	 * <br/>
 	 * cell 값이 "Y" 면 fwf-icon-stamp 아이콘
 	 * <br/>
 	 * cell 값이 "N" 면 fwf-icon-stamp-ok 아이콘
	 * <br/>
	 * 기타값은 ""
 	 * @version 1.0
 	 * @since 2013.01
 	 * @author 이병직
 	 * @function
 	 */
    iconStateApLine: function (cellvalue, options, rowObject){
        var btn = "";
        if (cellvalue === "N") {
            btn = '<span class="ui-icon fwf-icon-stamp-ok"></span>';
        } else if (cellvalue === "Y") {
            btn = '<span class="ui-icon fwf-icon-stamp"></span>';
        }
        return btn;
    },
    
    /**
   	 * @description 사용여부아이콘으로 변경
   	 * <br/>
   	 * cell 값이 "Y" 면 ●
   	 * <br/>
   	 * cell 값이 "N" 면 ""
	 * <br/>
	 * 기타값은 ""
   	 * @version 1.0
   	 * @since 2013.01
   	 * @author 이병직
   	 * @function
   	 */
    iconStateUseYn: function (cellvalue, options, rowObject){
        var btn = "";
        if (cellvalue === "N") {
            btn = '<span class=""></span>';
        } else if (cellvalue === "Y") {
            btn = '<span class="green bold font24">●</span>';
        }
        return btn;
    },

    /**
  	 * @description 멀티체크박스 사용시 값이 저장되는 cell에 사용
  	 * <br/>
  	 * cell 값이 "Y"이면 체크, "N"이면 체크해제, "M"이면 체크박스삭제
  	 * <br/>--------<br/>
  	 * onSelectAll 시에도 동작 (2.24)
  	 * <br/>
  	 * undefine과 같은 값은 모두 N으로 처리(3.20)
  	 * @version 1.0
  	 * @since 2013.03.20
  	 * @author 이병직
  	 * @function
  	 */
    multiselect: function (cellvalue, options, rowObject) {
        var sCboxID = "#jqg_" + options.gid + "_" + options.rowId;
        
        if (cellvalue === "Y") {
            JQGRID.global.cboxY.push(sCboxID);
        } else if (cellvalue === "N") {
            JQGRID.global.cboxN.push(sCboxID);
        } else if (cellvalue === "M") {
            JQGRID.global.cboxM.push(sCboxID);
        } else {
            cellvalue = "N";
            JQGRID.global.cboxN.push(sCboxID);     // undefine과 같은 값은 모두 N으로 처리한다. (3/20)
        }
        
        return cellvalue;
    },

    /**
   	 * @description 결제 확인 여부
   	 * <br/>
   	 * cell 값이 "Y"이면 미확인, "N"이면 확인,
   	 * <br/>
   	 * 기타값은 값 그대로.
   	 * @version 1.0
   	 * @since 2013.01
   	 * @author 이병직
   	 * @function
   	 */
    confirm: function (cellvalue, options, rowObject) {
        if (cellvalue === "Y") {
            return JQGRID.CONSTANT.UNCONFIRMED;
        } else if (cellvalue === "N") {
            return JQGRID.CONSTANT.CONFIRMED;
        } else {
            return cellvalue;
        }
    
    },
       
    /**
	 * @description 결제 상태
	 * <br/>
	 * cell 값이 승인중, 승인대기, 반려,승인완료(다국어코드에 따름) 에 따라 아이콘 표시
	 * <br/>
	 * 기타값은 값 그대로.
	 * <br/>
	 * 참고: 다국어코드 COM2000001 ~ COM2000004
	 * @version 1.1
	 * @since 2013.03
	 * @author 이병직
	 * @function
	 */
    state: function (cellvalue, options, rowObject) {
        switch (cellvalue) {
        case _UCS.COM2000001:
        	return JQGRID.CONSTANT.STATE_WAIT + "<span>" + cellvalue + "</span>";
        	break;
        case _UCS.COM2000002:
            return JQGRID.CONSTANT.STATE_PROG + "<span>" + cellvalue + "</span>";
            break;
        case _UCS.COM2000004:
            return JQGRID.CONSTANT.STATE_BACK + "<span>" + cellvalue + "</span>";
            break;
        case _UCS.COM2000003:
            return  JQGRID.CONSTANT.STATE_COMP + "<span>" + cellvalue + "</span>";
            break;
        default:
            return cellvalue;
            break;
        }
    },
     
    /**
 	 * @description 법인 번호 6-7 (앞 13자리가 숫자)
 	 * <br/>
 	 * STANDARD_SEPARATOR 적용
 	 * @version 1.1
 	 * @since 2013.03
 	 * @author 이병직
 	 * @function
 	 */
    crpnInfo: function (cellvalue, options, rowObject) {
        var number = cellvalue
        ,   suffix = "";
        if (cellvalue){
            number = Number(cellvalue.slice(0,13),10);
            if (number && number > 999999999999) {
                suffix = cellvalue.slice(13);
                number = String.format( "{0:######"+JQGRID.CONSTANT.STANDARD_SEPARATOR+"#######}", number ) + suffix;
            } else {
                number = "";
            }
        }
        return number;
    },

    /**
 	 * @description 사업자 번호 3-2-5 (앞 10자리가 숫자)
 	 * <br/>
 	 * STANDARD_SEPARATOR 적용
 	 * @version 1.1
 	 * @since 2013.03
 	 * @author 이병직
 	 * @function
 	 */
    bizrInfo: function (cellvalue, options, rowObject) {
        var number = cellvalue
        ,   suffix = "";
        if (cellvalue){
            number = Number(cellvalue.slice(0,10),10);
            if (number && number > 999999999) {
                suffix = cellvalue.slice(10);
                number = String.format( "{0:###"+JQGRID.CONSTANT.STANDARD_SEPARATOR+"##"+JQGRID.CONSTANT.STANDARD_SEPARATOR+"#####}", number ) + suffix;
            } else {
                number = "";
            }
        }
        return number;
    },
     
    /**
 	 * @description 우편 번호 3-3 (앞 6자리가 숫자)
 	 * <br/>
 	 * STANDARD_SEPARATOR 적용
 	 * @version 1.1
 	 * @since 2013.03
 	 * @author 이병직
 	 * @function
 	 */
    zipCode: function (cellvalue, options, rowObject) {
        var number = cellvalue
        ,   suffix = "";
        if (cellvalue){
            number = Number(cellvalue.slice(0,6),10);
            if (number && number > 99999) {
                suffix = cellvalue.slice(6);
                number = String.format( "{0:###"+JQGRID.CONSTANT.STANDARD_SEPARATOR+"###}", number ) + suffix;
            } else {
                number = "";
            }
        }
        return number;
    },
     
    /**
 	 * @description 반올림- 소수점 반올림(currency 형식)
 	 * @version 1.0
 	 * @since 2013.01
 	 * @author 이병직
 	 * @function
 	 */
    round: function (cellvalue, options, rowObject) {
         var number = cellvalue;
         if (cellvalue){
             number = Math.round(Number(cellvalue, 10));
             if (number || number === 0) {
                 number = String.format( "{0:#,##0}", number );
             } else {
                 number = "";
             }
         }
         return number;
     },
     
     /**
  	 * @description 정수화(버림) - currency
  	 * @version 1.0
  	 * @since 2013.01
  	 * @author 이병직
  	 * @function
  	 */
     integer: function (cellvalue, options, rowObject) {
         var number = cellvalue;
         if (cellvalue){
             number = parseInt(cellvalue, 10);
             if (number || number === 0) {
                 number = String.format( "{0:#,##0}", number );
             } else {
                 number = "";
             }
         }
         return number;
    },
    
    /**
  	 * @description Breif 포메터. 증감액(+/-)에 따른 아이콘 표시
  	 * <br/>
  	 * currencyB와 같이 값이 없으면 표시되지 않는다. (default 는 "")
  	 * @version 1.1
  	 * @since 2013.05.29
  	 * @author 이병직
  	 * @function
  	 */
    balance: function (cellvalue, options, rowObject) {
        var check = cellvalue.slice(0,1);
        if (check === "<") { // footer
            return cellvalue;
        } else if (check === "-") {
            return JQGRID.CONSTANT.DECREASE + "<span>" + COMM._formatter_module.currency(cellvalue, false, false, options) + "</span>"; 
        } else if (/[1-9]/.test(check)) {
            return JQGRID.CONSTANT.INCREASE + "<span>" + COMM._formatter_module.currency(cellvalue, false, false, options) + "</span>"; 
        } else { // 값이 없거나 기타일 경우
            return COMM._formatter_module.currency(cellvalue, "", false, options);
        }
    },
    
    /**
  	 * @description 통화 - defult 는 "0"
  	 * @version 1.1
  	 * @since 2013.04.11
  	 * @author 이병직
  	 * @function
  	 */
    currency: function(cellvalue, options, rowObject) {
        return COMM._formatter_module.currency(cellvalue, false, false, options);
    },
    
    /**
  	 * @description 통화 - defult 는 ""
  	 * @version 1.1
  	 * @since 2013.05.07
  	 * @author 이병직
  	 * @function
  	 */
    currencyB: function(cellvalue, options, rowObject) {
    	return COMM._formatter_module.currency(cellvalue, "", false, options);
    },
    
    /**
  	 * @description 통화 - defult 는 "0.00"
  	 * <br/>
  	 * 소수점은 2자리까지만
  	 * @version 1.1
  	 * @since 2013.05.09
  	 * @author 이병직
  	 * @function
  	 */
    currency2: function(cellvalue, options, rowObject) {
    	return COMM._formatter_module.currency(cellvalue, "0.00", 2, options);
    },
    
    /**
  	 * @description 통화 - defult 는 ""
  	 * <br/>
  	 * 소수점은 2자리까지만
  	 * @version 1.1
  	 * @since 2013.05.15
  	 * @author 이병직
  	 * @function
  	 */
    currency2B: function(cellvalue, options, rowObject) {
    	return COMM._formatter_module.currency(cellvalue, "", 2, options);
    },
     
    /**
  	 * @description 날짜 포메터 (yyyy-mm-dd hh:mm:ss 형식)
  	 * <br/>
  	 * 개발가이드 - 편집시에도 포멧이 작동하므로 두가지 경우로 처리한다.
  	 * @version 1.1
  	 * @since 2013.03
  	 * @author 이병직
  	 * @function
  	 */
    answerTime: function(cellvalue, options, rowObject) {
    	if (cellvalue.length >= 14 && /\d{14}/.test(cellvalue)) {
    		return  COMM.formatter(COMM._INIT.FORMATTER.answerTime, cellvalue);
    	} else if (cellvalue.length === 12 && /\d{12}/.test(cellvalue)){
    		return  COMM.formatter(COMM._INIT.FORMATTER.answerTime, cellvalue);
    	} else if (cellvalue.length > 14){
    		return cellvalue.slice(0,19); // 표준 구분자 포함 최대 +5
    	} else {
    		return "";
    	}
    },
    
    /**
  	 * @description 날짜 포메터 (yyyy-mm-dd 형식)
  	 * <br/>
  	 * 개발가이드 - 편집시에도 포멧이 작동하므로 두가지 경우로 처리한다.
  	 * @version 1.1
  	 * @since 2013.03
  	 * @author 이병직
  	 * @function
  	 */
    ymd: function(cellvalue, options, rowObject) {
    	switch(cellvalue.length) {
    	case 8:
    		return  COMM.formatter(COMM._INIT.FORMATTER.ymd, cellvalue);
    		break;
    	case 10:
    		return cellvalue;
    		break;
    	default:
    		return "";
    		break;
    	}
     },
     
     /**
   	 * @description 날짜 포메터 (yyyy-mm 형식)
   	 * <br/>
   	 * 개발가이드 - 편집시에도 포멧이 작동하므로 두가지 경우로 처리한다.
   	 * @version 1.1
   	 * @since 2013.03
   	 * @author 이병직
   	 * @function
   	 */
    ym: function(cellvalue, options, rowObject) {
    	switch(cellvalue.length) {
    	case 6:
    		return  COMM.formatter(COMM._INIT.FORMATTER.ym, cellvalue);
    		break;
    	case 7:
    		return cellvalue;
    		break;
    	default:
    		return "";
    		break;
    	}
    },
    
    /**
   	 * @description 날짜 포메터 (mm-dd 형식)
   	 * <br/>
   	 * 개발가이드 - 편집시에도 포멧이 작동하므로 두가지 경우로 처리한다.
   	 * @version 1.1
   	 * @since 2013.03
   	 * @author 이병직
   	 * @function
   	 */
    md: function(cellvalue, options, rowObject) {
    	switch(cellvalue.length) {
    	case 4:
    		return  COMM.formatter(COMM._INIT.FORMATTER.md, cellvalue);
    		break;
    	case 5:
    		return cellvalue;
    		break;
    	default:
    		return "";
    		break;
    	}
    },
    
    /**
   	 * @description 날짜 포메터 (hh:mm:ss 형식)
   	 * <br/>
   	 * 개발가이드 - 편집시에도 포멧이 작동하므로 두가지 경우로 처리한다.
   	 * @version 1.1
   	 * @since 2013.03
   	 * @author 이병직
   	 * @function
   	 */
    hms: function(cellvalue, options, rowObject) {
		switch(cellvalue.length) {
		case 6:
			return  COMM.formatter(COMM._INIT.FORMATTER.hms, cellvalue);
			break;
		case 8:
			return cellvalue;
			break;
		default:
			return "";
			break;
    	}
    },
    
    /**
   	 * @description 날짜 포메터 (hh:mm 형식)
   	 * <br/>
   	 * 개발가이드 - 편집시에도 포멧이 작동하므로 두가지 경우로 처리한다.
   	 * @version 1.1
   	 * @since 2013.03
   	 * @author 이병직
   	 * @function
   	 */
    hm: function(cellvalue, options, rowObject) {
    	switch(cellvalue.length) {
    	case 4:
    		return  COMM.formatter(COMM._INIT.FORMATTER.hm, cellvalue);
    		break;
    	case 5:
    		return cellvalue;
    		break;
    	default:
    		return "";
    		break;
    	}
    }

};


/////////////////////////////// 폐기 /////////////////////////////////////////////////
/*// 날짜 포메터 v1.0
JQGRID.dateFormatter = function (cellvalue, options, rowObject) {
	return "JQGRID.formatter 를 사용하십시요. 통합되었음";
};*/
