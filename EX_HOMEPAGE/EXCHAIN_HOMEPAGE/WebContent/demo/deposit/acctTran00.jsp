<%--
/* =============================================================================
 * System       : 원화입금 Demo
 * FileName     : acctTran00.jsp
 * Version      : 1.0
 * Description  : 
 * Author       : 
 * Date         : 2019.01.11
 * -----------------------------------------------------------------------------
 * Modify His   : 
 * -----------------------------------------------------------------------------
 * Etc          :
 * -----------------------------------------------------------------------------
 * Copyrights 2019 by Exchain. All rights reserved. ~ by Exchain.
 * =============================================================================
 */
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.finger.fwf.uxui.util.JSPUtil, com.finger.fwf.uxui.util.UCS" %>

<!DOCTYPE html>
<html>
<head>
<title>Exchain Deposit Demo</title>

<script type="text/javascript">
$(function () {

	$("#serviceKey").val("017266fae2ba426dab6e32eb7abfe78e");
	
	/*************************************************************************/
	/* DEMO용 거래일련번호 생성                                              
	/*************************************************************************/
	var tranSeqno = (Date.now() + Math.floor(Math.random() * 100) + 1).toString();  
	$("#tranSeqNo").val("EX"+tranSeqno.substring(tranSeqno.length - 10, 13));
	
	/*************************************************************************/
	/* 원장정보설정 버튼 클릭 이벤트입니다.                                  
	/*************************************************************************/
	$("#btnDemoSet").on("click", function(){

		/*************************************************************************/
		/* 1. 입력받은 데이터를 세팅합니다.                                      
		/*************************************************************************/
		var serviceKey = $.trim($("#serviceKey").val());			// 서비스키
		var inBankCd  = $("#inBankCd option:selected").val(); 		// 입금계좌은행코드
		var inAcctNo  = $.trim($("#inAcctNo").val());				// 입금계좌번호
		var outBankCd = $("#outBankCd option:selected").val();		// 출금계좌은행코드
		var outAcctNo = $.trim($("#outAcctNo").val());				// 출금계좌번호
		var outTelNo1 = $.trim($("#outTelNum1").val());             // 고객전화번호[지역번호]
		var outTelNo2 = $.trim($("#outTelNum2").val());             // 고객전화번호[국번]
		var outTelNo3 = $.trim($("#outTelNum3").val());             // 고객전화번호[전화번호]
		var outTelNo  = outTelNo1 + outTelNo2 + outTelNo3;          // 고객전화번호전체
		
		/*************************************************************************/
		/* 2. 입력받은 데이터 유효성을 체크합니다.                               
		/*  - 입력 데이터 입력 여부 확인 : 서비스키, 입금계좌은행코드, 입금계좌번호, 출금계좌은행코드, 출금계좌번호, 고객전화번호                   
		/*************************************************************************/
		if(serviceKey == '')
		{
			COMM.dialog({
       			type: "message",
                message: "서비스키를 입력해 주세요."
            });

			return false;
		}
		
		if(inBankCd == '')
		{
			COMM.dialog({
       			type: "message",
                message: "거래소(입금)은행을 선택해 주세요."
            });

			return false;
		}
		
		if(inAcctNo == '')
		{
			COMM.dialog({
       			type: "message",
                message: "거래소(입금)계좌번호를 입력해 주세요."
            });

			return false;
		}
		
		if(outBankCd == '')
		{
			COMM.dialog({
       			type: "message",
                message: "고객(출금)은행을 선택해 주세요."
            });

			return false;
		}
		
		if(outAcctNo == '')
		{
			COMM.dialog({
       			type: "message",
                message: "고객(출금)계좌번호를 입력해 주세요."
            });

			return false;
		}
		
		if(outTelNo1 == '')
        {
            COMM.dialog({
                type: "message",
                message: "고객전화번호[지역번호]를 입력해 주세요."
            });

            return false;
        }
		
		if(outTelNo2 == '')
        {
            COMM.dialog({
                type: "message",
                message: "고객전화번호[국번]를 입력해 주세요."
            });

            return false;
        }
		
		if(outTelNo3 == '')
        {
            COMM.dialog({
                type: "message",
                message: "고객전화번호를 입력해 주세요."
            });

            return false;
        }

		var paramObj = new Object();
		paramObj.serviceKey		= serviceKey;	// 서비스키
       	paramObj.inBankCd       = inBankCd; 	// 입금계좌은행코드
       	paramObj.inAcctNo       = inAcctNo;		// 입금계좌번호
		paramObj.outBankCd 		= outBankCd;	// 출금계좌은행코드
		paramObj.outAcctNo 		= outAcctNo;	// 출금계좌번호
		
		paramObj.outTelNo1 		= outTelNo1;  	// 고객전화번호[지역번호]
		paramObj.outTelNo2 		= outTelNo2;    // 고객전화번호[국번]
		paramObj.outTelNo3 		= outTelNo3;    // 고객전화번호[전화번호]
		paramObj.outTelNo       = outTelNo;     // 고객전화번호 전체
		
		paramObj.inBankNm       = $("#inBankCd option:selected").text();  // 입금은행한글명
		paramObj.outBankNm      = $("#outBankCd option:selected").text(); // 출금은행한글명
		paramObj.tranSeqNo		= $("#tranSeqNo").val() // 거래일련번호
		
		/*************************************************************************/
  		/* 3. 보안모듈 설치 확인 페이지로 이동합니다.                            
		/*  - 파라미터 : 입/출금 데이터                                          
  		/*************************************************************************/
        COMM.nextPage({
            url: "deposit/securityCheck.jsp",
        	param: paramObj
        }); 
		
      	return false;   
	});

	/*************************************************************************/
    /* DEMO용 버튼 이벤트
    /*************************************************************************/
	$(".demo_btn_r").click(function() {
        $("#inBankCd").val("004");
        $("#inAcctNo").val("60860201422146");
        
        $("#outBankCd").val("088");
        $("#outAcctNo").val("110465187992");
        
        $("#outTelNum1").val("010");
        $("#outTelNum2").val("3124");
        $("#outTelNum3").val("5081");
    });
});
</script>

</head>
<body> 

	<div id="exchain">

		<form id="${_PGM_ID_}form">
	
		<div class="tit_wrap">
			<h2 class="tit_00">거래소 원장정보 설정화면 <small>(DEMO전용)</small></h2>
		</div>
	
			<article>
				<section class="con-wrap">
					<div class="tit_group deposit_h">
						<h2 id="pageTitle"></h2>
					</div>

					<div class="detail">
						<ul>
							<li>
								<div class="flexwrap">
									<div class="column col_1">
										<div class="sv_key sv_key_2">
											<p class="key_txt" style="padding-top:10px; color:#254C73;">서비스키</p>
											<input id="serviceKey" type="text" class="choose" style="width:226px; height:35px; font-size:12px">
										</div>
										<p class="deposit_tit">[거래소(입금) 계좌 정보]</p>
										<p class="tit_float">은행</p>
										<select title="은행선택" id="inBankCd" class="bank">
											<option value="">선택</option>
											<option value="004">국민은행</option>
											<option value="088">신한은행</option>
											<option value="020">우리은행</option>
											<option value="081">하나은행</option>
											<option value="003">기업은행</option>
											<option value="011">농협은행</option>
											<option value="005">외환은행</option>
											<option value="002">산업은행</option>
											<option value="032">부산은행</option>
											<option value="031">대구은행</option>
											<option value="035">제주은행</option>
											<option value="037">전북은행</option>
										</select>
									</div>
								</div>
							</li>
							<li>
								<div class="flexwrap">
									<div class="column col_1">
										<p class="tit_float">계좌번호</p>
										<input id="inAcctNo" type="text" class="choose" maxlength="20">
									</div>
								</div>
							</li>
							<div class="tit_group deposit_h">
								<h2 id="pageTitle"></h2>
							</div>
							<li>
								<div class="flexwrap">
									<div class="column">
										<p class="deposit_tit">[고객(출금) 계좌 정보]</p>
										<p class="tit_float">은행</p>
										<select title="은행선택" id="outBankCd" class="bank">
											<option value="">선택</option>
											<option value="004">국민은행</option>
											<option value="088">신한은행</option>
											<option value="020">우리은행</option>
											<option value="081">하나은행</option>
											<option value="003">기업은행</option>
										</select>
									</div>
								</div>
							</li>
							<li>
								<div class="flexwrap">
									<div class="column col_1">
										<p class="tit_float">계좌번호</p>
										<input id="outAcctNo" type="text" class="choose" maxlength="20">
									</div>
								</div>
							</li>
							<li>
								<div class="flexwrap">
									<div class="column col_1" style="margin-top:10px;">
										<p class="tit_float" style="margin-top:6px;">고객연락처</p>
										<input id="outTelNum1" type="text" class="choose phon_N" maxlength="3">-
										<input id="outTelNum2" type="text" class="choose phon_N" maxlength="4">-
										<input id="outTelNum3" type="text" class="choose phon_N" maxlength="4">
									</div>
								</div>
							</li>
							<div class="tit_group deposit_h">
								<h2 id="pageTitle"></h2>
							</div>
							<li>
								<div class="flexwrap">
									<div class="column col_1">
										<p class="deposit_tit">[거래일련번호]</p>
										<input id="tranSeqNo" type="text" class="input-txt" maxlength="20" readonly>
									</div>
								</div>
							</li>
						</ul>
						<div class="btns col2">
							<div class="btn_center holder"><a id="btnDemoSet" class="btn_type2">원장정보설정</a></div>
						</div>
					</div>
					<div class="demo_wrap">
						<div class="demo_btn demo_1"><a class="demo_btn_r"><img src="../../theme/deposit/images/btn_star.png"></a></div>
					</div>
				</section>
			</article>
		</form>
	</div>
</body>
</html>
