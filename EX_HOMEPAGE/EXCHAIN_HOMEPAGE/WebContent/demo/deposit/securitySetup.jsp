<%--
/* =============================================================================
 * System       : 원화입금 Demo
 * FileName     : securitySetup.jsp
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
	var serviceKey = "${param.serviceKey}"; // 서비스키
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
	
	var fwscheck   = "${param.fwscheck}";
	var bankcheck  = "${param.bankcheck}";

	/*************************************************************************/
	/* FWS 모듈 설치 여부를 확인합니다.                                      
	/*************************************************************************/
	if(fwscheck == "true"){
		$("#FSWSS_download").hide();
		$("#FSWSS_ready").show();
	
		/*************************************************************************/
		/* 은행보안 프로그램 설치 여부를 확인합니다.                             
		/*************************************************************************/
		if(bankcheck == "true"){
			;
		}else{
			var scrappingData = new Array();
			scrappingData = JSON.parse('${param.scrappingData}');
			
			$.each( scrappingData , function(index, obj) {
				var object  = obj.object;
				var install = obj.install;
				
				if(install == "Y"){
					$("#" + object ).show();
					$("#" + object + "_download").hide();
					$("#" + object + "_ready").show();
				}
				else
				{
					$("#" + object ).show();
					$("#" + object + "_download").show();
				}
			});
		}
		
	}else
	{
		$("#FSWSS_download").show();
		$("#FSWSS_ready").hide();
	}

	/*************************************************************************/
	/* 새로고침 버튼 클릭 이벤트입니다.                                      
	/*************************************************************************/
	$("#btnRefresh").on("click", function(){

		securityModule({bankId : outBankCd, serviceKey : serviceKey}, function(fwscheck, bankcheck, scrappingData) {
		
    		var paramObj = new Object();
    		paramObj.serviceKey = serviceKey;	  // 서비스키
           	paramObj.inBankCd  = inBankCd;        // 입금계좌은행코드
           	paramObj.inAcctNo  = inAcctNo;        // 입금계좌번호
    		paramObj.outBankCd = outBankCd;       // 출금계좌은행코드
    		paramObj.outAcctNo = outAcctNo;       // 출금계좌번호
    		paramObj.outTelNo  = outTelNo;        // 고객전화번호
    		paramObj.inBankNm  = inBankNm;        // 입금은행한글명
    		paramObj.outBankNm = outBankNm;       // 출금은행한글명
    		paramObj.tranSeqNo = tranSeqNo;       // 거래일련번호
    		
	        if (!fwscheck || !bankcheck) 
	        {
	    		paramObj.fwscheck      = fwscheck;        // FWS설치여부
	    		paramObj.bankcheck     = bankcheck;       // 은행보안프로그램설치여부
	    		paramObj.scrappingData = scrappingData;   // 스크래핑데이터

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
	
	/*************************************************************************/
	/* 취소 버튼 클릭 이벤트입니다.                                          
	/*************************************************************************/
	$("#btnCancel").on("click", function(){

		/*************************************************************************/
		/* 입금 솔루션 DEMO 메인 페이지로 이동합니다.                            
		/*************************************************************************/
		location.href = "acctDemo.jsp";
		return false;
	});
});
</script>

</head>
<body>
  
<div id="exchain">
	<form id="form">	
	<article>
	    <section class="w1000">
	        <h3 class="tit_h3 bline">은행과 동일하게 안전한 이체거래를 진행합니다. <br>
	        사용중인 은행의 <span class="color_b">보안 프로그램</span>을 설치하세요.</h3>
	        	        
	        <table class="tbtype2">
	        	<colgroup>
	        		<col class="per25">
	        		<col class="per60">
	        		<col class="per15">
	        	</colgroup>
	        	<thead>
	        		<tr>
	        			<th>프로그램명</th>
	        			<th>기능</th>
	        			<th>설치</th>
	        		</tr>
	        	</thead>
	        	<tbody>
	        		<tr id="FSWSS">
	        			<td>
	        				<p>은행보안프로그램 설치확인</p>
	        				<p class="sub_stc">FSWSS(스크래핑모듈) Finger Scraping Web Server SSL</p>
	        			</td>
	        			<td><p class="color_g">거래할 은행의 보안 프로그램 설치 확인을 위한 스크래핑 모듈입니다.</p></td>
	        			<td class="tcenter">
	        				<div id= "FSWSS_download" class="installbox" style="display:none;"> <!-- 미설치시 레이아웃 -->
	        					<p class="color_b">미설치</p>
	        					<a href="https://down.finger.co.kr/down/2xchange/FSWSSSetup.exe" class="btn_type4">다운로드</a>
        					</div>
        					<div id= "FSWSS_ready" class="installbox" style="display:none;"> <!-- 설치시 레이아웃-->
	        					<p class="color_b">설치됨</p>
	        				</div>
	        				<div id= "FSWSS_status" class="installbox" style="display:none;"> <!-- 확인전-->
	        					<p class="color_b">-</p>
	        				</div>
	        				
       					</td>
	        		</tr>
	        		<tr id="I3GM" style="display:none;"> 
	        			<td>
	        				<p>PC지정보안</p>
	        				<p class="sub_stc">I3GManager</p>
	        			</td>
	        			<td><p class="color_g">이체시 PC지정 방식서비스 사용여부 확인을 위한 프로그램 입니다.</p></td>
	        			<td class="tcenter">
	        				<div id= "I3GM_download" class="installbox" style="display:none;"> <!-- 미설치시 레이아웃 -->
	        					<p class="color_b">미설치</p>
	        					<a href="https://down.finger.co.kr/down/2xchange/I3GManager.exe" class="btn_type4">다운로드</a>
        					</div>
        					<div id= "I3GM_ready" class="installbox" style="display:none;"> <!-- 설치시 레이아웃-->
	        					<p class="color_b">설치됨</p>
	        				</div>
	        				<div id= "I3GM_status" class="installbox" style="display:none;"> <!-- 확인전-->
	        					<p class="color_b">-</p>
	        				</div>
	        				
       					</td>
	        		</tr>
	        		<tr id="XWCDataPlugin" style="display:none;"> 
	        			<td>
	        				<p>공인인증서보안</p>
	        				<p class="sub_stc">XWCDataPlugin</p>
	        			</td>
	        			<td><p class="color_g">공인인증서를 통한 로그인과 거래에 대한 전자서명을 위한프로그램입니다.</p></td>
	        			<td class="tcenter">
	        				<div id= "XWCDataPlugin_download" class="installbox" style="display:none;"> <!-- 미설치시 레이아웃 -->
	        					<p class="color_b">미설치</p>
	        					<a href="https://down.finger.co.kr/down/2xchange/xwcup_install_windows_x86.exe" class="btn_type4">다운로드</a>
        					</div>
        					<div id= "XWCDataPlugin_ready" class="installbox" style="display:none;"> <!-- 설치시 레이아웃-->
	        					<p class="color_b">설치됨</p>
	        				</div>
	        				<div id= "XWCDataPlugin_status" class="installbox" style="display:none;"> <!-- 확인전-->
	        					<p class="color_b">-</p>
	        				</div>
	        				
       					</td>
	        		</tr>
	        		<tr id="XW40" style="display:none;"> 
	        			<td>
	        				<p>공인인증서보안</p>
	        				<p class="sub_stc">xecureweb</p>
	        			</td>
	        			<td><p class="color_g">공인인증서를 통한 로그인과 거래에 대한 전자서명을 위한프로그램입니다.</p></td>
	        			<td class="tcenter">
	        				<div id= "XW40_download" class="installbox" style="display:none;"> <!-- 미설치시 레이아웃 -->
	        					<p class="color_b">미설치</p>
	        					<a href="https://down.finger.co.kr/down/2xchange/xw1.exe" class="btn_type4">다운로드</a>
        					</div>
        					<div id= "XW40_ready" class="installbox" style="display:none;"> <!-- 설치시 레이아웃-->
	        					<p class="color_b">설치됨</p>
	        				</div>
	        				<div id= "XW40_status" class="installbox" style="display:none;"> <!-- 확인전-->
	        					<p class="color_b">-</p>
	        				</div>
	        				
       					</td>
	        		</tr>
	        		<tr id="DOL" style="display:none;"> 
	        			<td>
	        				<p>공인인증서보안</p>
	        				<p class="sub_stc">Delfino</p>
	        			</td>
	        			<td><p class="color_g">공인인증서를 통한 로그인과 거래에 대한 전자서명을 위한프로그램입니다.</p></td>
	        			<td class="tcenter">
	        				<div id= "DOL_download" class="installbox" style="display:none;"> <!-- 미설치시 레이아웃 -->
	        					<p class="color_b">미설치</p>
	        					<a href="https://down.finger.co.kr/down/2xchange/delfino.exe" class="btn_type4">다운로드</a>
        					</div>
        					<div id= "DOL_ready" class="installbox" style="display:none;"> <!-- 설치시 레이아웃-->
	        					<p class="color_b">설치됨</p>
	        				</div>
	        				<div id= "DOL_status" class="installbox" style="display:none;"> <!-- 확인전-->
	        					<p class="color_b">-</p>
	        				</div>
	        				
       					</td>
	        		</tr>
	        		<tr id="DOLG3" style="display:none;"> 
	        			<td>
	        				<p>공인인증서보안</p>
	        				<p class="sub_stc">Delfino G3</p>
	        			</td>
	        			<td><p class="color_g">공인인증서를 통한 로그인과 거래에 대한 전자서명을 위한프로그램입니다.</p></td>
	        			<td class="tcenter">
	        				<div id= "DOLG3_download" class="installbox" style="display:none;"> <!-- 미설치시 레이아웃 -->
	        					<p class="color_b">미설치</p>
	        					<a href="https://down.finger.co.kr/down/2xchange/delfino_g3_sha2.exe" class="btn_type4">다운로드</a>
        					</div>
        					<div id= "DOLG3_ready" class="installbox" style="display:none;"> <!-- 설치시 레이아웃-->
	        					<p class="color_b">설치됨</p>
	        				</div>
	        				<div id= "DOLG3_status" class="installbox" style="display:none;"> <!-- 확인전-->
	        					<p class="color_b">-</p>
	        				</div>
	        				
       					</td>
	        		</tr>	 	 
	        		<tr id="ISW64" style="display:none;"> 
	        			<td>
	        				<p>공인인증서보안</p>
	        				<p class="sub_stc">INISAFE6</p>
	        			</td>
	        			<td><p class="color_g">공인인증서를 통한 로그인과 거래에 대한 전자서명을 위한프로그램입니다.</p></td>
	        			<td class="tcenter">
	        				<div id= "ISW64_download" class="installbox" style="display:none;"> <!-- 미설치시 레이아웃 -->
	        					<p class="color_b">미설치</p>
	        					<a href="https://down.finger.co.kr/down/2xchange/INIS64.exe" class="btn_type4">다운로드</a>
        					</div>
        					<div id= "ISW64_ready" class="installbox" style="display:none;"> <!-- 설치시 레이아웃-->
	        					<p class="color_b">설치됨</p>
	        				</div>
	        				<div id= "ISW64_status" class="installbox" style="display:none;"> <!-- 확인전-->
	        					<p class="color_b">-</p>
	        				</div>
	        				
       					</td>
	        		</tr>	        		
	        		<tr id="ASTX" style="display:none;"> 
	        			<td>
	        				<p>해킹방지솔루션</p>
	        				<p class="sub_stc">AhnLab Safe Transaction</p>
	        			</td>
	        			<td><p class="color_g">악성 프로그램에의해 웹페이지가 변조되는것을 차단해주며, 금융거래시 불법 접속 및 불법거래등 이상거래를 탐지하기 위한 기기정보를 수집하는 역할을 하는 프로그램입니다.</p></td>
	        			<td class="tcenter">
	        				<div id= "ASTX_download" class="installbox" style="display:none;"> <!-- 미설치시 레이아웃 -->
	        					<p class="color_b">미설치</p>
	        					<a href="https://down.finger.co.kr/down/2xchange/astxdn.exe" class="btn_type4">다운로드</a>
        					</div>
        					<div id= "ASTX_ready" class="installbox" style="display:none;"> <!-- 설치시 레이아웃-->
	        					<p class="color_b">설치됨</p>
	        				</div>
	        				<div id= "ASTX_status" class="installbox" style="display:none;"> <!-- 확인전-->
	        					<p class="color_b">-</p>
	        				</div>
	        				
       					</td>
	        		</tr>
	        		<tr id="TouchEnKey" style="display:none;"> 
	        			<td>
	        				<p>키보드보안</p>
	        				<p class="sub_stc">TouchEn Key</p>
	        			</td>
	        			<td><p class="color_g">비밀번호와 같이 데이터 보호가 필요한 정보에 대해 키보드로 입력시 입력정보 유출을 차단하는 프로그램입니다.</p></td>
	        			<td class="tcenter">
	        				<div id= "TouchEnKey_download" class="installbox" style="display:none;"> <!-- 미설치시 레이아웃 -->
	        					<p class="color_b">미설치</p>
	        					<a href="https://down.finger.co.kr/down/2xchange/TouchEnKey_Installer_x86.exe" class="btn_type4">다운로드</a>
        					</div>
        					<div id= "TouchEnKey_ready" class="installbox" style="display:none;"> <!-- 설치시 레이아웃-->
	        					<p class="color_b">설치됨</p>
	        				</div>
	        				<div id= "TouchEnKey_status" class="installbox" style="display:none;"> <!-- 확인전-->
	        					<p class="color_b">-</p>
	        				</div>
	        				
       					</td>
	        		</tr>
	        		<tr id="TouchEnNxKey" style="display:none;"> 
	        			<td>
	        				<p>키보드보안</p>
	        				<p class="sub_stc">TouchEnNxKey</p>
	        			</td>
	        			<td><p class="color_g">비밀번호와 같이 데이터 보호가 필요한 정보에 대해 키보드로 입력시 입력정보 유출을 차단하는 프로그램입니다.</p></td>
	        			<td class="tcenter">
	        				<div id= "TouchEnNxKey_download" class="installbox" style="display:none;"> <!-- 미설치시 레이아웃 -->
	        					<p class="color_b">미설치</p>
	        					<a href="https://down.finger.co.kr/down/2xchange/TouchEn_nxKey_32bit.exe" class="btn_type4">다운로드</a>
        					</div>
        					<div id= "TouchEnNxKey_ready" class="installbox" style="display:none;"> <!-- 설치시 레이아웃-->
	        					<p class="color_b">설치됨</p>
	        				</div>
	        				<div id= "TouchEnNxKey_status" class="installbox" style="display:none;"> <!-- 확인전-->
	        					<p class="color_b">-</p>
	        				</div>
	        				
       					</td>
	        		</tr>
	        		<tr id="SCSK" style="display:none;"> 
	        			<td>
	        				<p>키보드보안</p>
	        				<p class="sub_stc">SCSK</p>
	        			</td>
	        			<td><p class="color_g">비밀번호와 같이 데이터 보호가 필요한 정보에 대해 키보드로 입력시 입력정보 유출을 차단하는 프로그램입니다.</p></td>
	        			<td class="tcenter">
	        				<div id= "SCSK_download" class="installbox" style="display:none;"> <!-- 미설치시 레이아웃 -->
	        					<p class="color_b">미설치</p>
	        					<a href="https://down.finger.co.kr/down/2xchange/SCSKInstUserMode.exe" class="btn_type4">다운로드</a>
        					</div>
        					<div id= "SCSK_ready" class="installbox" style="display:none;"> <!-- 설치시 레이아웃-->
	        					<p class="color_b">설치됨</p>
	        				</div>
	        				<div id= "SCSK_status" class="installbox" style="display:none;"> <!-- 확인전-->
	        					<p class="color_b">-</p>
	        				</div>
	        				
       					</td>
	        		</tr>
	        	</tbody>
	        </table>
	        <div class="btns col2">
			    <div><a href="#" id="btnRefresh" class="btn_type2">새로고침</a></div>
			    <div><a href="#" id="btnCancel" class="btn_type1">취소</a></div>
			</div>	
	    </section>	   
	</article>
	</form>	
</div>
</body>
</html>
