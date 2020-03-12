/* =============================================================================
 * System       : F-CMS
 * FileName     : fwf-custom.js
 * Version      : 1.1
 * Description  : 사용자 맞춤 script
 * Author       : 이 병 직 (초기작성)
 * Date         : 2013.04.09
 * -----------------------------------------------------------------------------
 * Modify Date  : 
 * -----------------------------------------------------------------------------
 * Etc          : 특정 페이지에서만 재사용하는 코드
 * -----------------------------------------------------------------------------
 * -----------------------------------------------------------------------------
 * Copyrights 2013 by Finger. All rights reserved. ~ by Finger.
 * =============================================================================
 */

var CUST = {};
CUST.version = "1.1";
if (window.console) {
	console.info("fwf-custom.js Version: ", CUST.version);
}

// 전역변수
CUST.global = {
        openUserPopupCallback: ""
      , outAcntBalCheck : ""
      , variable1 : new Object()
	  , variable2 : new Object()
};

///////////////////////////
// 특정 페이지를 popup 으로
///////////////////////////

//사용자검색 팝업창 (by 변준호)
CUST.openUserPopup = function(openerParam, openerCallback) {
 
 CUST.global.openUserPopupCallback = openerCallback;
 
 COMM.layer( 
         "/page/work/uc20/UC2030010.jsp", 
         { w:860, h:600, m:true, param:"openerParam=" + openerParam } 
 );
};

//사용자 패스워드 팝업창 (by 변준호)
CUST.openUserPwPopup = function(openerParam, openerCallback) {
 
 CUST.global.openUserPopupCallback = openerCallback;
 
 COMM.layer( 
         "/page/work/uc20/UC2030020.jsp", 
         { w:320, h:600, m:true, param:openerParam } 
 );
};

//출금계좌입금내역조회팝업
CUST.openOutAcntTrnsPopup = function(openerParam, openerCallback) {
	CUST.global.openUserPopupCallback = openerCallback;
	COMM.layer( 
         "/page/work/up10/UP1020031.jsp", 
         { w:860, h:600, m:true, param:openerParam } 
	);
};

//알림상세팝업
CUST.openAlarmDetailPopup = function(openerParam, openerCallback) {
	CUST.global.openUserPopupCallback = openerCallback;
	COMM.layer( 
         "/page/work/um20/UM2010020.jsp", 
         { w:850, h:1, m:true, param:openerParam } 
	);
};

//실행자조회 팝업창 (by 변준호)
CUST.openUserExecPopup = function(openerParam, openerCallback) {
 
 CUST.global.openUserPopupCallback = openerCallback;
 
 COMM.layer( 
         "/page/work/uc20/UC2030030.jsp", 
         { w:860, h:600, m:true, param:openerParam}  
 );
};

//결재선상세조회팝업창
CUST.openApprLinePopup = function(appr_ln_seq_no) {
	COMM.layer( 
			"/page/work/uc20/UC2030040.jsp", 
			{ w:600, h:400, m:true, param:"appr_ln_seq_no=" + appr_ln_seq_no}  
	);
};

//자주쓰는입금계좌조회 팝업창 (수정 이종태 - 원화, 외화, 해외 분리)
CUST.openMyAcntPopup = function(curDiv, focPayDiv, openerParam, openerCallback) {
	var url = "";
	
	CUST.global.openUserPopupCallback = openerCallback;
	
	if(curDiv == "KRW"){
		url = "/page/work/uc20/UC2030070.jsp";
		
		if (openerParam.bank_cd) {
			COMM.layer(url, { w:860, h:600, m:true, param:openerParam, isSub:true});	
		} else {
			COMM.layer(url, { w:860, h:600, m:true, param:openerParam, isSub:false});
		}
	}
	else if(curDiv == "FOC"){
		// focPayDiv로 당행:1, 타행-실시간:2, 타행-KEB:3 지급유형 구분
		url = "/page/work/uc20/UC2030071.jsp?foc_pay_div="+focPayDiv;
		COMM.layer( url, {w:860, h:484, m:true, param:openerParam, isSub:true} );
	}
	else if(curDiv == "OVRS"){
		url = "/page/work/uc20/UC2030072.jsp";
		COMM.layer( url, {w:1100, h:484, m:true, param:openerParam, isSub:true} );
	}
};

//출금계좌조회 팝업창 - 2016-09-09
CUST.openOutAcntPopup = function(openerParam, openerCallback) {
	var url = "";
	
	CUST.global.openUserPopupCallback = openerCallback;
	
	url = "/page/work/uc20/UC2030073.jsp";
	
	COMM.layer(url, { w:600, h:400, m:true, param:openerParam, isSub:true});
};


//펌뱅킹계좌조회 팝업창 (by 변준호)
CUST.openAdminAcntPopup = function(openerParam, openerCallback) {
 
 CUST.global.openUserPopupCallback = openerCallback;
 
 COMM.layer( 
         "/page/work/uc20/UC2030110.jsp", 
         { w:860, h:600, m:true, param:openerParam}  
 );
};

//엑셀업로드 팝업창 (by 변준호)
CUST.openExcelGridPopup = function(openerParam) {
	
	COMM.winOpen("/page/work/uc20/UC2030080.jsp?gridId=" + openerParam.gridId + "&gridColumn=" + JSON.stringify(openerParam.gridColumn)   
			, {w:600,h:200, name:"UC2030080"});
};

//예약환율 팝업창
CUST.openApmtExchRtPopup = function(openerParam, openerCallback) {
	
	CUST.global.openUserPopupCallback = openerCallback;
	
	var url = "/page/work/uc20/UC2030120.jsp"
			+ "?pageId=" + openerParam.pageId 
			+ "&acnt_seq_no=" + openerParam.acnt_seq_no 
			+ "&bank_cd=" + openerParam.bank_cd 
			+ "&out_cur_cd=" + openerParam.out_cur_cd 
			+ "&remt_cur_cd=" + openerParam.remt_cur_cd; 
	
	COMM.layer( url, {w:1100, h:540, m:true, param:openerParam, isSub:true} );
};

//처리결과조회 팝업창
CUST.openProcRstPopup = function(openerParam) {
	
	//"기관구분:은행코드:에러코드"로 구성된 proc_rst_cd를 파라미터로 넘겨주어야한다.
	COMM.layer( 
			"/page/work/uc20/UC2030060.jsp", 
			{ w:420, h:800, m:true, param:openerParam}  
	);
};

///////////////////////
//이체 페이지 적용 함수
///////////////////////

// 이체용 바이트체크 함수 (by 변준호)
CUST.byteCheck = function(str, length, msg) {
	if (COMM.getByte(str) > length) {
		COMM.dialog({
			type: "warning",
	        id: "W0027",
	        message: msg
       	});
		return false;
	}
	return true;
};

// 이체용 결재선 셀렉트박스 생성
CUST.makeSelectApprLine = function(pageID) {
	COMM.showByTasking({
	    taskID: "AC1010260",
	    pageID: pageID,
	    formID: pageID + "form",
	    async: false,
	    callback: function (data) {
	    	
	    	data.resultData = data.resultData || {};
	    	var appr_line = "";
	    	
	    	if ($.isEmptyObject(data.resultData)) {
	    		appr_line = "<option value='X'>" + _UCS.MSGCDW0059 + "</option>";	// 등록가능한 결재선이 없습니다.
	    	} else {
    			appr_line = "<option value='X'>" + _UCS.MSGCDW0056 + "</option>" + data.resultData["appr_line"];	    			
	    	}
	    	
	    	$("#" + pageID + " select[name=appr_ln_seq_no]").html(appr_line);
	    }
	});
};

// 외화이체용 원화,외화 출금계좌 셀렉트박스 생성
CUST.makeSelectOutAcnt = function(pageID, acnt_krw_foc_div) {
	
    var appendHtml = "<input type='hidden' name='acnt_krw_foc_div' value='" + acnt_krw_foc_div + "' />";
    $("#" + pageID + "form").append(appendHtml);
    
	COMM.showByTasking({
	    taskID: "AC1020220",
	    pageID: pageID,
	    formID: pageID + "form",
	    async: false,
	    callback: function (data) {
	    	
	    	data.resultData = data.resultData || {};
	    	var out_acnt_seq_no_list = "";
	    	
	    	if ($.isEmptyObject(data.resultData)) {
	    		
	    		out_acnt_seq_no_list = "<option value='X'>" + _UCS.MSGCDW0057 + "</option>";	// 등록된 출금계좌번호가 없습니다
	    		
	    	} else {
	    		out_acnt_seq_no_list = data.resultData["out_acnt_seq_no_list"];
	    		out_acnt_seq_no_list = "<option value='X'>" + _UCS.MSGCDW0056 + "</option>" + out_acnt_seq_no_list;		// 선택하세요
	    	}
	    	
	    	if (acnt_krw_foc_div == "K") {
	    		$("#" + pageID + " select[name=krw_out_acnt_seq_no_cur]").html(out_acnt_seq_no_list);
	    	} else if (acnt_krw_foc_div == "F") {
	    		$("#" + pageID + " select[name=foc_out_acnt_seq_no_cur]").html(out_acnt_seq_no_list);	
	    	} else if (acnt_krw_foc_div == "ALL") {
	    		$("#" + pageID + " select[name=all_out_acnt_seq_no_cur]").html(out_acnt_seq_no_list);	
	    	} else if (acnt_krw_foc_div == "FEE") {
	    		$("#" + pageID + " select[name=fee_amt_do_krw_acnt_seq_no]").html(out_acnt_seq_no_list);	
	    	}		
	    }
	});
	
	$("#" + pageID + "form input[name=acnt_krw_foc_div]").remove();
};

//대량이체출금계좌 셀렉트박스 생성
CUST.makeSelectMtrnsOutAcnt = function(pageID) {
	COMM.showByTasking({
	    taskID: "AC1010280",
	    pageID: pageID,
	    formID: pageID + "form",
	    async: false,
	    callback: function (data) {
	    	
	    	data.resultData = data.resultData || {};
	    	var out_acnt_seq_no_list = "";
	    	
	    	if ($.isEmptyObject(data.resultData)) {
	    		out_acnt_seq_no_list = "<option value='X'>" + _UCS.MSGCDW0057 + "</option>";	// 등록된 출금계좌번호가 없습니다
	    	} else {
	    		out_acnt_seq_no_list = data.resultData["out_acnt_seq_no_list"];
	    		out_acnt_seq_no_list = "<option value='X'>" + _UCS.MSGCDW0056 + "</option>" + out_acnt_seq_no_list;		// 선택하세요
	    	}
	    	
	    	$("#" + pageID + " select[name=out_acnt_seq_no]").html(out_acnt_seq_no_list);
	    }
	});
};

//개별지급출금계좌 셀렉트박스 생성
CUST.makeSelectSingleOutAcnt = function(pageID) {
	COMM.showByTasking({
	    taskID: "AC1010290",
	    pageID: pageID,
	    formID: pageID + "form",
	    async: false,
	    callback: function (data) {
	    	
	    	data.resultData = data.resultData || {};
	    	var out_acnt_seq_no_list = "";
	    	
	    	if ($.isEmptyObject(data.resultData)) {
	    		out_acnt_seq_no_list = "<option value='X'>" + _UCS.MSGCDW0057 + "</option>";	// 등록된 출금계좌번호가 없습니다
	    	} else {
	    		out_acnt_seq_no_list = data.resultData["out_acnt_seq_no_list"];
	    		out_acnt_seq_no_list = "<option value='X'>" + _UCS.MSGCDW0056 + "</option>" + out_acnt_seq_no_list;		// 선택하세요
	    	}
	    	
	    	$("#" + pageID + " select[name=out_acnt_seq_no]").html(out_acnt_seq_no_list);
	    }
	});
};

//외화이체용 통화코드 셀렉트박스 생성
CUST.makeSelectCurCd = function(pageID, srch_div, lngg_cd, bank_cd) {
	
    var appendHtml = "<input type='hidden' name='srch_div' value='" + srch_div + "' />";
    appendHtml += "<input type='hidden' name='lngg_cd' value='" + lngg_cd + "' />";
    appendHtml += "<input type='hidden' name='bank_cd' value='" + bank_cd + "' />";
    
    $("#" + pageID + "form").append(appendHtml);
    
	COMM.showByTasking({
	    taskID: "AC1020270",
	    pageID: pageID,
	    formID: pageID + "form",
	    async: false,
	    callback: function (data) {
	    	
	    	data.resultData = data.resultData || {};
	    	var cur_cd_list = "";
	    	
	    	if ($.isEmptyObject(data.resultData)) {
	    		
	    	} else {
	    		cur_cd_list = data.resultData["cur_cd_list"];
	    		cur_cd_list = "<option value='X'>" + _UCS.MSGCDW0056 + "</option>" + cur_cd_list;		// 선택하세요
	    	}
	    	
	    	if (srch_div == "SELF") {
	    		$("#" + pageID + " select[name=in_cur_cd]").html(cur_cd_list);
	    	}
	    	
	    	$("#" + pageID + " select[name=remt_cur_cd]").html(cur_cd_list);
	    }
	});
	
	$("#" + pageID + "form input[name=srch_div]").remove();
	$("#" + pageID + "form input[name=lngg_cd]").remove();
	$("#" + pageID + "form input[name=bank_cd]").remove();
};

//공통코드조회
CUST.getComCdMap = function(pageID, grp_cd, lang_cd) {
	
    var appendHtml = "<input type='hidden' name='srch_grp_cd' value='" + grp_cd + "' />";

    if (lang_cd) {
    	appendHtml += "<input type='hidden' name='srch_lngg_cd' value='" + lang_cd + "' />";    	
    } else {
    	appendHtml += "<input type='hidden' name='srch_lngg_cd' value='en' />";
    }
    
    $("#" + pageID + "form").append(appendHtml);
    
    var returnMap = new Object();
    
	COMM.showByTasking({
	    taskID: "AC1020250",
	    pageID: pageID,
	    formID: pageID + "form",
	    async: false,
	    callback: function (data) {
	    	
	    	data.resultData = data.resultData || {};
	    	var com_code_list = "";
	    	
	    	if ($.isEmptyObject(data.resultData)) {
	    		com_code_list = "";
	    	} else {
	    		com_code_list = data.resultData["com_code_list"];
	    	}
	    	
	    	var arrComCode = com_code_list.split(";");
        	
        	for (var i=0; i<arrComCode.length; i++) {
        		var arrTmp = arrComCode[i].split("^");
        		returnMap[arrTmp[0]] = arrTmp[1];
        	}
	    }
	});

	$("#" + pageID + "form input[name=srch_grp_cd]").remove();
	$("#" + pageID + "form input[name=srch_lngg_cd]").remove();
	
	return returnMap;
};

// 이체용 원화,외화 출금계좌 잔액조회
CUST.setBalanceAmt = function(pageID, acnt_krw_foc_div, selectValue) {

	if (selectValue == "X" || selectValue == "" || selectValue == null) {
		
		// 원화이체용 폼필드
		if ($("#" + pageID + " input[name=out_bank_cd]").length == 1) {
			
			$("#" + pageID + " input[name=sup_posb_bal]").val("");
			$("#" + pageID + " input[name=bal_lst_upd_date]").val("");
			$("#" + pageID + " input[name=out_bank_cd]").val("");
			$("#" + pageID + " input[name=out_acnt_no]").val("");
			
		} else {
		
			if (acnt_krw_foc_div == "K") {
				$("#" + pageID + " input[name=krw_sup_posb_bal]").val("");
				$("#" + pageID + " input[name=krw_bal_lst_upd_date]").val("");
				$("#" + pageID + " input[name=krw_out_acnt_no]").val("");
				$("#" + pageID + " input[name=krw_out_bank_cd]").val("");
			} else if (acnt_krw_foc_div == "F") {
				$("#" + pageID + " input[name=foc_sup_posb_bal]").val("");
				$("#" + pageID + " input[name=foc_bal_lst_upd_date]").val("");
				$("#" + pageID + " input[name=foc_out_acnt_no]").val("");
				$("#" + pageID + " input[name=foc_out_bank_cd]").val("");
			} else if (acnt_krw_foc_div == "ALL") {
				$("#" + pageID + " input[name=all_sup_posb_bal]").val("");
				$("#" + pageID + " input[name=all_bal_lst_upd_date]").val("");
				$("#" + pageID + " input[name=all_out_acnt_no]").val("");
				$("#" + pageID + " input[name=all_out_bank_cd]").val("");
			} else if (acnt_krw_foc_div == "FEE") {
				$("#" + pageID + " input[name=fee_sup_posb_bal]").val("");
				$("#" + pageID + " input[name=fee_bal_lst_upd_date]").val("");
				$("#" + pageID + " input[name=fee_out_acnt_no]").val("");
				$("#" + pageID + " input[name=fee_out_bank_cd]").val("");
			}
		}
		
		return;
	}
	
	var acnt_seq_no = CUST.getAcntSeqNo(selectValue);
	
	var appendHtml = "<input type='hidden' name='bal_acnt_seq_no' value='" + acnt_seq_no + "' />";
    $("#" + pageID + "form").append(appendHtml);
    
	COMM.showByTasking({
	    taskID: "AC1010190",
	    pageID: pageID,
	    formID: pageID + "form",
	    callback: function (data) {
	    	
	    	var bal = $.trim(data.resultData["bal"]);
	    	var sup_posb_bal = $.trim(data.resultData["sup_posb_bal"]);
			var bal_lst_upd_date = $.trim(data.resultData["bal_lst_upd_date"]);
			var bank_cd = $.trim(data.resultData["bank_cd"]);
			var acnt_no = $.trim(data.resultData["acnt_no"]);
			var cur_cd = $.trim(data.resultData["cur_cd"]);
			//var acnt_seq_no = $.trim(data.resultData["acnt_seq_no"]);
			
			if (bal_lst_upd_date != "") {
				bal_lst_upd_date = "(" + COMM.formatter("answerTime", bal_lst_upd_date) + ")";
			}
			
			if (cur_cd == "KRW") {
				bal = COMM.formatter("currency", bal);
				sup_posb_bal = COMM.formatter("currency", sup_posb_bal);
			} else {
				bal = COMM.formatter("currency2", bal);
				sup_posb_bal = COMM.formatter("currency2", sup_posb_bal);
			}
			
	    	// 원화이체용 폼필드
	    	if ($("#" + pageID + " input[name=out_bank_cd]").length == 1) {
	    		
	    		$("#" + pageID + " input[name=sup_posb_bal]").val(sup_posb_bal);
    			$("#" + pageID + " input[name=bal_lst_upd_date]").val(bal_lst_upd_date);
    			$("#" + pageID + " input[name=out_bank_cd]").val(bank_cd);
    			$("#" + pageID + " input[name=out_acnt_no]").val(acnt_no);
    			
    			// 개별지급일 경우...
    			if (pageID == "UP1020030") {
    				outAcntPosbBal = sup_posb_bal;
    				CUST.global.openUserPopupCallback();
    			}
	    		
	    	} else {
	    		if (acnt_krw_foc_div == "K") {
		    		
	    			$("#" + pageID + " input[name=krw_sup_posb_bal]").val(bal);
	    			$("#" + pageID + " input[name=krw_bal_lst_upd_date]").val(bal_lst_upd_date);
	    			$("#" + pageID + " input[name=krw_out_acnt_no]").val(acnt_no);
	    			$("#" + pageID + " input[name=krw_out_bank_cd]").val(bank_cd);
	    			
		    	} else if (acnt_krw_foc_div == "F") {
		    		
		    		$("#" + pageID + " input[name=foc_sup_posb_bal]").val(bal);
	    			$("#" + pageID + " input[name=foc_bal_lst_upd_date]").val(bal_lst_upd_date);
	    			$("#" + pageID + " input[name=foc_out_acnt_no]").val(acnt_no);
	    			$("#" + pageID + " input[name=foc_out_bank_cd]").val(bank_cd);
	    			
		    	} else if (acnt_krw_foc_div == "ALL") {
		    		
		    		$("#" + pageID + " input[name=all_sup_posb_bal]").val(bal);
	    			$("#" + pageID + " input[name=all_bal_lst_upd_date]").val(bal_lst_upd_date);
	    			$("#" + pageID + " input[name=all_out_acnt_no]").val(acnt_no);
	    			$("#" + pageID + " input[name=all_out_bank_cd]").val(bank_cd);
	    			
		    	} else if (acnt_krw_foc_div == "FEE") {
		    		
		    		$("#" + pageID + " input[name=fee_sup_posb_bal]").val(bal);
	    			$("#" + pageID + " input[name=fee_bal_lst_upd_date]").val(bal_lst_upd_date);
	    			$("#" + pageID + " input[name=fee_out_acnt_no]").val(acnt_no);
	    			$("#" + pageID + " input[name=fee_out_bank_cd]").val(bank_cd);
		    	}		
	    	}			
	    }
	});

	$("#" + pageID + "form input[name=bal_acnt_seq_no]").remove();
};

//그리드에 예약환율번호 존재여부체크
CUST.isExistExchGrid = function(pageID) {
	var arrGridRowId = $("#" + pageID + "grid").jqGrid("getDataIDs");
	var isExistExch = false; 
	
	for (var i=0; i<arrGridRowId.length; i++) {
		var apmt_exch_rt_no = $.trim( $("#" + pageID + "grid").jqGrid("getCell", arrGridRowId[i], "apmt_exch_rt_no") );
		if (apmt_exch_rt_no != "") {
			isExistExch = true;
			break;
		}
	}
	
	return isExistExch;
};

//그리드컬럼초기화
CUST.clearGridColumn = function(pageID, gridColumn) {
	
	var arrGridColumn = gridColumn.split("|");
	var arrGridRowId = $("#" + pageID + "grid").jqGrid("getDataIDs");
	
	var returnObj = new Object();
	for (var j=0; j<arrGridColumn.length; j++) {
		returnObj[arrGridColumn[j]] = "";
	}
	
	for (var i=0; i<arrGridRowId.length; i++) {
		$("#" + pageID + "grid").jqGrid("setRowData", arrGridRowId[i], returnObj);
	}
};

//그리드'null'컬럼초기화(콤보박스)
CUST.clearGridNullColumn = function(pageID, gridColumn) {
	
	var arrGridColumn = gridColumn.split("|");
	var arrGridRowId = $("#" + pageID + "grid").jqGrid("getDataIDs");

	for (var i=0; i<arrGridRowId.length; i++) {
		for (var j=0; j<arrGridColumn.length; j++) { 
			var gridColumn = $.trim($("#" + pageID + "grid").jqGrid("getCell", arrGridRowId[i], arrGridColumn[j])); 
			if(gridColumn == "null"){
				$("#" + pageID + "grid").jqGrid("setCell", arrGridRowId[i], arrGridColumn[j], null);
			}
		}
	}
};

CUST.getCurrency = function(selectValue) {
	return CUST.getAcntInfo(selectValue).currency;
};

CUST.getAcntSeqNo = function(selectValue) {
	return CUST.getAcntInfo(selectValue).acntSeqNo;
};

CUST.getAcntInfo = function(selectValue) {
	
	if (selectValue == "X" || selectValue == "" || selectValue == null) {
		var returnAcnt = new Object();
		returnAcnt.currency = "";
		returnAcnt.acntSeqNo = "X";
		return returnAcnt;
	}
	
	var arrSelectValue = selectValue.split(",");
	var returnAcnt = new Object();
	
	if (arrSelectValue.length == 2) {
		
		returnAcnt.currency = arrSelectValue[0];
		returnAcnt.acntSeqNo = arrSelectValue[1];
		
	} else {
		
		returnAcnt.currency = "";
		returnAcnt.acntSeqNo = "";
		
		if (selectValue.length == 3) {
			returnAcnt.currency = selectValue;
		}
		
		if (selectValue.length == 10) {
			returnAcnt.acntSeqNo = selectValue;
		}
	}
	
	return returnAcnt;
};

CUST.openTransRegPopup = function(pageID, paramObject) {
	
	var width = 900;
	var height = 600;
	var url = "/page/work/" + pageID.substr(0,4).toLowerCase() + "/" + pageID.substr(0,6) + "031.jsp";
	paramObject.pageID = pageID;
	
	COMM.progressbar("on");
	COMM.layer(url, {w:width, h:height, m:true, param:paramObject, callback:function(){COMM.progressbar("off");} });
};

// select 필수체크
CUST.validateSelect = function (pageID, selectName, errorMsg) {
	
	var selectValue = $("#" + pageID + " select[name='" + selectName + "']").val();
	if (selectValue == "" || selectValue == "X" || selectValue == null) {
		COMM.dialog({
			type: "warning",
	        id: "W0018",			
           	message: errorMsg		// %1 [을/를] 선택해주세요.
       	});
		
		return false;
	}			
	
	return true;
};

// input 필수체크
CUST.validateEmpty = function (pageID, inputName, errorMsg) {
	
	var inputValue = $.trim($("#" + pageID + " input[name='" + inputName + "']").val());
	$("#" + pageID + " input[name='" + inputName + "']").val(inputValue);
	
	if ( inputValue == "" ) {
		COMM.dialog({
			type: "warning",
	        id: "W0019",			
           	message: errorMsg,		// %1 [을/를] 입력해주세요.
           	callback: function() {
           		$("#" + pageID + " input[name='" + inputName + "']").focus();
           	}
       	});
		
		return false;
	}
	
	return true;
};

//input 숫자체크
CUST.validateNumber = function (pageID, inputName, errorMsg) {
	
	var inputValue = COMM.unformat.currency($("#" + pageID + " input[name='" + inputName + "']").val());
	
	if ( !COMM.isNumber(inputValue) ) {
		COMM.dialog({
			type: "warning",
	        id: "W0042",			
           	message: errorMsg,		// %1 [을/를] 숫자로 입력해주세요.
           	callback: function() {
           		$("#" + pageID + " input[name='" + inputName + "']").focus();
           	}
       	});
		
		return false;
	}
	
	return true;
};

//길이체크
CUST.validateLength = function (pageID, inputName, checkLength, errorMsg) {
	
	var inputValue = $.trim($("#" + pageID + " input[name='" + inputName + "']").val());
	$("#" + pageID + " input[name='" + inputName + "']").val(inputValue);
	
	if ( inputValue.length != checkLength ) {
		COMM.dialog({
			type: "warning",
	        id: "W0061",			
           	message: errorMsg,		// %1 [은/는] %2자리로  입력해주세요.
           	callback: function() {
           		$("#" + pageID + " input[name='" + inputName + "']").focus();
           	}
       	});
		
		return false;
	}
	
	return true;
};

//숫자, 영문자 체크
CUST.validateEng = function (pageID, inputName, errorMsg) {
	
	var regExp = /[a-zA-Z0-9]+$/;
	var inputValue = $.trim($("#" + pageID + " input[name='" + inputName + "']").val());
	$("#" + pageID + " input[name='" + inputName + "']").val(inputValue);
	
	if ( !regExp.test(inputValue) ) {
		COMM.dialog({
			type: "warning",
	        id: "W0060",			
           	message: errorMsg,		// %1 [은/는] 영문,숫자만 입력해주세요.
           	callback: function() {
           		$("#" + pageID + " input[name='" + inputName + "']").focus();
           	}
       	});
		
		return false;
	}
	
	return true;
};

//e메일 체크
CUST.validateEmail = function (pageID, inputName, errorMsg) {
	
	var regExp = /^[_a-zA-Z0-9-]+@[._a-zA-Z0-9-]+\.[a-zA-Z]+$/;
	var inputValue = $.trim($("#" + pageID + " input[name='" + inputName + "']").val());
	$("#" + pageID + " input[name='" + inputName + "']").val(inputValue);
	
	if (inputValue.match(regExp) == null) {
		COMM.dialog({
			type: "warning",
	        id: "W0062",			
           	message: errorMsg,			// %1 [이/가] 유효하지 않습니다.
           	callback: function() {
           		$("#" + pageID + " input[name='" + inputName + "']").focus();
           	}
       	});
		
		return false;
	}
	
	return true;
};

CUST.validateHandPhone = function (pageID, inputName, errorMsg) {
	
	var regExp = /(01)(0|1|6|7|8|9)(\d{7}|\d{8})$/g;
	var inputValue = $.trim($("#" + pageID + " input[name='" + inputName + "']").val());
	$("#" + pageID + " input[name='" + inputName + "']").val(inputValue);
	
	if ( !regExp.test(inputValue) ) {
		COMM.dialog({
			type: "warning",
	        id: "W0062",			
           	message: errorMsg,			// %1 [이/가] 유효하지 않습니다.
           	callback: function() {
           		$("#" + pageID + " input[name='" + inputName + "']").focus();
           	}
       	});
		
		return false;
	}
	
	return true;
};

//송금금액체크
CUST.validateAmt = function (pageID, inputName, emptyErrorMsg, numberErrorMsg, amtErrorMsg) {
	
	var inputValue = $("#" + pageID + " input[name='" + inputName + "']").val();
	
	if ( !CUST.validateEmpty(pageID, inputName, emptyErrorMsg) ) {
		return false;
	}
	
	if ( !CUST.validateNumber(pageID, inputName, numberErrorMsg) ) {
		return false;
	}
	
	if (parseFloat(inputValue) <= 0) {
		COMM.dialog({
	        type: "warning",
	        id: "W0045",			
	        message: amtErrorMsg,		// %1 [을/를] 1이상 입력해주세요.
	        callback: function() {
	        	$("#" + pageID + " input[name='" + inputName + "']").focus();
           	}
	    });
		
	    return false;
	}
	
	return true;
	
};

// 필수, 숫자, 영문자 체크
CUST.validateEmptyNEng = function (pageID, inputName, emptyErrorMsg, engErrorMsg) {
	
	if ( !CUST.validateEmpty(pageID, inputName, emptyErrorMsg) ) {
		return false;
	}
	
	if ( !CUST.validateEng(pageID, inputName, engErrorMsg) ) {
		return false;
	}
	
	return true;
};

//필수, 숫자 체크
CUST.validateEmptyNNumber = function (pageID, inputName, emptyErrorMsg, numberErrorMsg) {
	
	if ( !CUST.validateEmpty(pageID, inputName, emptyErrorMsg) ) {
		return false;
	}
	
	if ( !CUST.validateNumber(pageID, inputName, numberErrorMsg) ) {
		return false;
	}
	
	return true;
};

//실시간국내송금, 국내간외화송금 거래가능시간 체크 (16:30분)
CUST.focPayTimeCheck = function(errorMsg){

	var notTime = String(new Date().getHours())+String(new Date().getMinutes());
	var payTime = "1630";

	if(parseInt(notTime) >= parseInt(payTime)){
         COMM.dialog({
             type: "warning"
             , id: "W0098"            
             , message: errorMsg 
         });
         return false;
	}
    return true;
};
