<%--
/* =============================================================================
 * System       : 원화입금 Demo
 * FileName     : securityCheck.jsp
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
	var inBankCd   = "${param.inBankCd}";	// 입금은행코드
	var inAcctNo   = "${param.inAcctNo}";	// 입금계좌번호
	var outBankCd  = "${param.outBankCd}";	// 출금계좌은행코드
	var outAcctNo  = "${param.outAcctNo}";	// 출금계좌번호
	
	var outTelNo1  = "${param.outTelNo1}";  // 고객전화번호[지역번호]
	var outTelNo2  = "${param.outTelNo2}";  // 고객전화번호[국번]
	var outTelNo3  = "${param.outTelNo3}";  // 고객전화번호[전화번호]
	var outTelNo   = "${param.outTelNo}";   // 고객전화번호 전체
	
	var inBankNm   = "${param.inBankNm}";	// 입금은행한글명
	var outBankNm  = "${param.outBankNm}";  // 출금은행한글명
	var tranSeqNo  = "${param.tranSeqNo}";  // 거래일련번호
	
	/*************************************************************************/
	/* 보안모듈 설치여부를 확인합니다.(공통스크립트 파일 exchain_demo.js 함수 호출)
	/* param : bankId = 출금계좌 은행코드
	/* result 
	/*  fwscheck : 핑거 FWS 모듈 설치여부
	/*  bankcheck : 은행보안 모듈 설치여부
	/*  scrappingData : 보안모듈 설치여부 결과 데이터
	/* 
	/*************************************************************************/
	securityModule({bankId : outBankCd, serviceKey : serviceKey}, function(fwscheck, bankcheck, scrappingData) {
		
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
		
		paramObj.inBankNm       = inBankNm; 	// 입금은행한글명
		paramObj.outBankNm      = outBankNm; 	// 출금은행한글명
		paramObj.tranSeqNo		= tranSeqNo 	// 거래일련번호

        if (!fwscheck || !bankcheck) 
        {
    		paramObj.fwscheck 		= fwscheck;
    		paramObj.bankcheck 		= bankcheck;
    		paramObj.scrappingData 	= scrappingData;
    		
    		/*************************************************************************/
    		/* FWS 모듈, 은행보안 프로그램 미설치시                                  
    		/* 보안프로그램 설치 페이지로 이동합니다.                                
    		/*************************************************************************/
            COMM.nextPage({
                url: "deposit/securitySetup.jsp",
                param: paramObj
            }); 
        	return false;   
        } else 
        {
        	/*************************************************************************/
        	/* 원화(KRW)입금실행 페이지로 이동합니다.                                
        	/*************************************************************************/
            COMM.nextPage({            	
                url: "deposit/acctTran10.jsp",
                param: paramObj
            }); 
            return false;  
        }
    }); 
});
</script>

</head>
<body> 
  
	<div id="exchain">
		<form id="form">
	  
			<article>
				<section class="con-wrap">
					<div class="tit_group security">
						<h2 id="pageTitle">보안프로그램 설치 확인중</h2>
					</div>
	
					<div class="detail">
						<ul>
							<li>
								<div id="safesetup">
									<div class="safedown">
										<img src="data:image/png;base64,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">
										<div class="txtbox">
											<p>· 고객님이 사용중인 인터넷뱅킹과 동일한 방식으로 안전하게 거래소 입금계좌로 입금됩니다.</p>
											<br />
											<p>· 해당 은행의 보안 프로그램이 미설치된 경우 설치 완료 후 입금할 수 있습니다.</p>
										</div>
									</div>
								</div>
							</li>
						</ul>
					</div>
				</section>
			</article>	
		</form>
	</div>
</body>
</html>
