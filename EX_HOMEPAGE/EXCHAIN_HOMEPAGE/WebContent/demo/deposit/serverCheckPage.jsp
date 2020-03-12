<?xml version="1.0" encoding="EUC-KR"?>
<%--
/* =============================================================================
 * System       : 원화입금 Demo
 * FileName     : serverCheckPage.jsp
 * Version      : 1.0
 * Description  : 원화입금거래 유효성 체크 
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
<%@ page language="java" contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8"%>
<RESULT>
<%
try{
	String szParam = request.getParameter("sv_param");	// 스크래핑에서 서버로 전송된 파라미터

	String[] paramArray = szParam.split("/");
	
	String inBankCd   = paramArray[0].toString();	// 입금계좌은행코드
	String inAcctNo   = paramArray[1].toString();	// 입금계좌번호
	String outBankCd  = paramArray[2].toString();	// 출금계좌은행코드
	String outAcctNo  = paramArray[3].toString();	// 출금계좌번호
	String tranSeqNo  = paramArray[4].toString();  	// 거래일련번호

	boolean tranCheck = true;
	String chkval = "N";
	
	/*  
	 * [거래소별 서버 거래유효성 체크 로직 추가]
	 *	입금정보, 출금정보, 거래일련번호 등으로 입금해도 문제 없는지 서버에서 최종적으로 체크 합니다.
	 */
	
	// 유효성 검증 결과 정상인경우 "Y"
	if(tranCheck)
	{
		chkval = "Y";
	}

	out.write(chkval);
	
}catch(Exception e)
{
	out.write("");
}
%>
</RESULT>
