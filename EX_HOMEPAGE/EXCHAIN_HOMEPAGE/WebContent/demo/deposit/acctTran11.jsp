<%--
/* =============================================================================
 * System       : 원화입금 Demo
 * FileName     : acctTran11.jsp
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

<style>
 .wrap{background:url(../../theme/deposit/images/ex_pattern.png) repeat;}
</style>

<script type="text/javascript">
$(function () {

	COMM.progressbar("off");

	var serviceKey = "${param.serviceKey}";	// 서비스키
	var inBankCd  = "${param.inBankCd}";	// 입금계좌은행코드
	var inAcctNo  = "${param.inAcctNo}";	// 입금계좌번호
	var outBankCd = "${param.outBankCd}";	// 출금계좌은행코드
	var outAcctNo = "${param.outAcctNo}";	// 출금계좌번호

	var outTelNo1 = "${param.outTelNo1}";   // 고객전화번호[지역번호]
	var outTelNo2 = "${param.outTelNo2}";   // 고객전화번호[국번]
	var outTelNo3 = "${param.outTelNo3}";   // 고객전화번호[전화번호]
	var outTelNo  = "${param.outTelNo}";    // 고객전화번호 전체
	
	var tranamt   = "${param.tranamt}";		// 이체금액
	var tranSeqNo = "${param.tranSeqNo}";	// 거래일련번호
	var hostIP 	  = "exchain.co.kr";
 	var portNO    = "80";

	// 스크래핑 시작(GET)
	 var url = "https://local.finger.co.kr:52187/ScrapService?service_key=c8cee8ee39754a8aa7107f8b34213276&timeout=600";
	var sid = "s1" + outBankCd.substring(1,3) + "0302"; // 출금은행 은행코드(2자리)
	
	var fid ="&tran_count=1" 		+
	         //"&acct_pwd0="   		+ 
			 "&tran_in_bank_id0=" 	+ inBankCd   + // 입금 은행코드
			 "&tran_in_acct_no0=" 	+ inAcctNo   + // 입금 계좌번호 
			 "&tran_amount0=" 		+ tranamt    + // 이체금액
			 "&tran_out_acct_no0=" 	+ outAcctNo  + // 출금 계좌번호
			 "&tran_receiver0=" 	+ tranSeqNo  + // 입금 통장메모
			 "&tran_sender0=" 		+ tranSeqNo  + // 출금 통장메모
			 "&pay_use=" 			+ "COIN" +
			 "&pay_dlg_type=" 		+ "A" +
			 "&task_id="			+ "" +
			 "&sv_host_ip=" 		+ hostIP +
			 "&sv_port_no=" 		+ portNO +
			 "&sv_page=" 			+ "/demo/deposit/serverCheckPage.jsp" +
			 "&sv_param=" 			+ inBankCd + "/" + inAcctNo + "/" + outBankCd + "/" + outAcctNo + "/" + tranSeqNo +
			 "&tran_tel=" 			+ "01012345678" + 
			 "&tran_tel_num1=" 		+ "010" +
			 "&tran_tel_num2=" 		+ "1234" +
			 "&tran_tel_num3=" 		+ "5678";

	var callURL = url + "&sid=" + sid + fid;
	console.info(">[ye]scrapingURL:::"+callURL);
	
	
	/*************************************************************************/
	/* 1. 스크래핑 입력 데이터를 세팅합니다.                                 */
	/*************************************************************************/
	var scUrl = "https://local.finger.co.kr:52187/ScrapService";
	var sid   = "s1" + outBankCd.substring(1,3) + "0302"; // 출금은행 은행코드(2자리)
	
	$("input[name=service_key]").val(serviceKey); 		
	$("input[name=sid]").val(sid); 		
	
	$("input[name=tran_in_bank_id0]").val(inBankCd); 		// 입금 은행코드
	$("input[name=tran_in_acct_no0]").val(inAcctNo); 		// 입금 계좌번호 
	$("input[name=tran_amount0]").val(tranamt); 		    // 이체금액
	$("input[name=tran_out_acct_no0]").val(outAcctNo); 		// 출금 계좌번호
	$("input[name=tran_receiver0]").val(tranSeqNo); 		// 입금 통장메모
	$("input[name=tran_sender0]").val(tranSeqNo); 		    // 출금 통장메모
	$("input[name=tran_tel]").val(outTelNo);				// 고객전화번호 전체
	$("input[name=tran_tel_num1]").val(outTelNo1);			// 고객전화번호 [지역번호]
	$("input[name=tran_tel_num2]").val(outTelNo2);			// 고객전화번호 [국번]
	$("input[name=tran_tel_num3]").val(outTelNo3);			// 고객전화번호 [전화번호]
	
	// 아래 항목은 스크래핑 커스터마이징 후 셋팅될 예정 입니다.
	$("input[name=pay_use]").val("COIN");									// 스크립트이용사이트구분
	$("input[name=sv_host_ip]").val(hostIP);                                // 거래소 서버 IP
	$("input[name=sv_port_no]").val(portNO);                                // 거래소 서버 포트
	$("input[name=sv_page]").val("/demo/deposit/serverCheckPage.jsp");	    // 거래 유효성 확인 서버 페이지[도메인 이하 경로]
	//$("input[name=sv_param]").val("088/123456/004/21222/tranSeqNo");		// 서버에 전달할 파라미터
	
	// 서버에 전달할 파라미터 : 입금계좌은행코드/입금계좌번호/출금계좌은행코드/출금계좌번호/거래일련번호
	var svParam = inBankCd + "/" + inAcctNo + "/" + outBankCd + "/" + outAcctNo + "/" + tranSeqNo;
	$("input[name=sv_param]").val(svParam); 
	
	var param = $("form[name=scform]").serialize();
	console.info("param >> " + param);
	COMM.progressbar("on");
	
	/*************************************************************************/
	/* 2. 원화(KRW)입금(스크래핑 시작) 처리합니다.                          
	/*************************************************************************/
	$.ajax({
		type: "POST",
		dataType: "jsonp",
		crossDomain: true,
		data: param,
		timeout : 600000 ,  // 타임아웃 설정 (대기시간 및 응답시간 설정) 10분이 지나도 응답이 없으면 error 로직을 탄다.
		url: scUrl,
		success: function(data){
			console.info("data >> " + data);
			result = JSON.stringify(data);
			result = result.replace(/\\/gi, "");
			console.info("result >> " + result);
			
			var rCode = data.errCode; // 스크래핑 결과코드
			var rMsg  = data.errMsg;  // 스크래핑 결과메세지
			
			if(rCode == "000")		   // 정상
			{  	  
				COMM.progressbar("off");

				/*************************************************************************/
		  		/* 3.1. 스크래핑 이체 성공시, 원장 반영 처리 페이지로 이동합니다.   							                 
		  		/*************************************************************************/
				COMM.nextPage({
                    url: "deposit/acctTran30.jsp",
                    param: result
                }); 
			} else if(rCode == "001")  // 취소
			{
				COMM.progressbar("off");
				
				/*************************************************************************/
		  		/* 3.1. 스크래핑 이체 취소시, 원화(KRW)입금 취소 페이지로 이동합니다.   
		  		/*      계좌비밀번호입력취소, 인증서로그인취소, 보안매체비밀번호입력취소, 전자서명취소 등							                
		  		/*************************************************************************/
		  		COMM.nextPage({
		            url: "deposit/acctTran90.jsp",
		        	param: result
		        }); 
			} else {                   // 오류
				COMM.progressbar("off");
				
				/*************************************************************************/
				/* 3.1. 스크래핑 처리중 오류 발생                             
				/*************************************************************************/
				failCallback(data.errCode, data.errMsg);
			}
		},
		error: function(e){
			COMM.progressbar("off");
			failCallback("", "스크래핑 실행 실패");
		}
	});
	
	// 스크래핑 처리중 오류 발생 처리
	function failCallback(resultCd, resultMsg) {

		/*************************************************************************/
		/* 전자서명 직전, 스크래핑에서 보내주는 타임아웃 발생시	                 
		/*  - errCode : "9002", errMsg : <RESULT>N</RESULT>                      	
		/*************************************************************************/
		if(resultCd == "9002" || resultMsg == "<RESULT>N</RESULT>") {
			resultMsg = "이체요청 시간이 초과되었습니다.";
		}
		
		resultMsg = "[" + resultCd + "] " + resultMsg;
		
		var resultObj = new Object();
		resultObj.errMsg = resultMsg.replace(/\r\n/gi," ");
		
		/*************************************************************************/
  		/* 스크래핑 이체 오류시, 원화(KRW)입금 오류 페이지로 이동합니다.         
  		/*  - 파라미터 : 오류메시지								                
  		/*************************************************************************/
        COMM.nextPage({
            url: "deposit/acctTran70.jsp",
        	param: resultObj
        }); 
	}
});
</script>

</head>
<body> 
  
	<div id="exchain">
		<form name="scform" id="form">
			<input type="hidden" name="err_msg" value="">
			<input type="hidden" name="logJson" value="">
			
			<input type="hidden" name="service_key" 		value="">
			<input type="hidden" name="timeout" 			value="600">
			<input type="hidden" name="sid" 				value="">
			<input type="hidden" name="tran_count" 			value="1">
			<input type="hidden" name="acct_pwd0" 			value="">
			<input type="hidden" name="tran_in_bank_id0" 	value="">
			<input type="hidden" name="tran_in_acct_no0" 	value="">
			<input type="hidden" name="tran_amount0" 		value="">
			<input type="hidden" name="tran_out_acct_no0" 	value="">
			<input type="hidden" name="tran_receiver0" 		value="">
			<input type="hidden" name="tran_sender0" 		value="">
			<input type="hidden" name="pay_use" 			value="">
			<input type="hidden" name="pay_dlg_type" 		value="">
			<input type="hidden" name="sv_host_ip" 			value="">
			<input type="hidden" name="sv_port_no" 			value="">
			<input type="hidden" name="sv_page" 			value="">
			<input type="hidden" name="sv_param" 			value="">
			<input type="hidden" name="tran_tel" 			value="">
			<input type="hidden" name="tran_tel_num1" 		value="">
			<input type="hidden" name="tran_tel_num2" 		value="">
			<input type="hidden" name="tran_tel_num3" 		value="">

		<!-- <article class="pro_border"> -->
		<article>
			<section class="con-wrap acctcheck_h">	
				<div class="tit_group" >
					<section class="pay_processwarp mt40" style="padding:15px 70px;">
						<div class="pay_process"><span class="act arrow">입금진행중</span></div>
					</section>
				</div>
			</section>
		</article>
		</form>
	</div>

</body>
</html>
