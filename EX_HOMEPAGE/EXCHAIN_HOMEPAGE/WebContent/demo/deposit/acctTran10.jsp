<%--
/* =============================================================================
 * System       : 원화입금 Demo
 * FileName     : acctTran10.jsp
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
	var serviceKey = "${param.serviceKey}";	// 서비스키
	var inBankCd   = "${param.inBankCd}";	// 입금계좌은행코드
	var inAcctNo   = "${param.inAcctNo}";	// 입금계좌번호
	var outBankCd  = "${param.outBankCd}";	// 출금계좌은행코드
	var outAcctNo  = "${param.outAcctNo}";	// 출금계좌번호

	var outTelNo1  = "${param.outTelNo1}";  // 고객전화번호[지역번호]
	var outTelNo2  = "${param.outTelNo2}";  // 고객전화번호[국번]
	var outTelNo3  = "${param.outTelNo3}";  // 고객전화번호[전화번호]
	var outTelNo   = "${param.outTelNo}";   // 고객전화번호 전체
	
	var tranamt    = "${param.tranamt}";	// 입금금액
	var inBankNm   = "${param.inBankNm}";	// 입금은행한글명
	var outBankNm  = "${param.outBankNm}";  // 출금은행한글명
	var tranSeqNo  = "${param.tranSeqNo}";  // 거래일련번호
	
	$("#outBankNm").val(outBankNm);
	$("#outAcctNo").val(outAcctNo);
	
	$("#inBankInfo").text(inBankNm + " : " + inAcctNo);

	/*************************************************************************/
	/* 원화(KRW)입금실행 버튼 클릭 이벤트입니다.                             
	/*************************************************************************/
	$("#btnAcctDeposit").on("click", function(){
		
		/*************************************************************************/
		/* 1. 입력받은 데이터를 세팅합니다.                                      
		/*************************************************************************/
		var tranamt  = $.trim($("#tranAmt").val());

		/*************************************************************************/
		/* 2. 입력받은 데이터 유효성을 체크합니다.                               
		/*  - 입금금액 입력 여부, 숫자여부 확인					                 
		/*************************************************************************/
		if(tranamt == '')
		{
			COMM.dialog({
       			type: "message",
                message: "입금금액을 입력해 주세요."
            });

			return false;
		}else
		{
			if(!isNumber(tranamt))
			{
				COMM.dialog({
	       			type: "message",
	                message: "입금금액은 숫자만 입력해 주세요."
	            });

				return false;
			}
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
		paramObj.outTelNo       = outTelNo;     // 고객전화번호
		
		paramObj.tranamt 	    = tranamt;		// 입금금액
		paramObj.tranSeqNo 	    = tranSeqNo;	// 거래일련번호
		
		/*************************************************************************/
  		/* 3. 입금 처리(스크래핑 실행) 페이지로 이동합니다.                      
  		/*  - 파라미터 : 입/출금 데이터                                          
  		/*************************************************************************/
        COMM.nextPage({
            url: "deposit/acctTran11.jsp",
        	param: paramObj
        }); 
		
      	return false;   
		
	});

});
</script>

</head>
<body> 
  
	<div id="exchain">
		<form id="form">
	
			<article>
				<section class="con-wrap">
					<div class="tit_group">
						<h2 id="pageTitle"></h2>
					</div>

					<div class="detail">
						<ul>
							<li class="account_txt" >
								<div class="flexwrap">
									<div id="blah">
										<h4>거래소(입금)계좌 정보</h4>
										<p id="inBankInfo"></p>
									</div>							
								</div>
							</li>
							<li>
								<div class="flexwrap">
									<div class="column tran_h">
		                            	<p>은행</p>
		                            	<input id="outBankNm" type="text" class="choose input-txt" maxlength="20" readonly>
	                        		</div>
								</div>
							</li>
							<li>
								<div class="flexwrap">
									<div class="column tran_h">
										<p>계좌번호</p>
										<input id="outAcctNo" type="text" class="choose input-txt" maxlength="20" readonly>
									</div>
								</div>
							</li>
							<li>
								<div class="flexwrap">
									<div class="column tran_h">
			                            <p>입금금액</p>
							            <input id="tranAmt" type="text" class="tran_w" maxlength="20" placeholder="원화(KRW) 입금금액을 입력해주세요 ">
					         		</div>
				        		</div>
							</li>
						</ul>
						<div class="btns col2">
							<div class="btn_center holder"><a id="btnAcctDeposit" class="btn_type2">원화(KRW)입금실행</a></div>
						</div>
					</div>
				</section>
			</article>
		</form>
	</div>
</body>
</html>
