<?xml version="1.0" encoding="EUC-KR"?>
<%--
/* =============================================================================
 * System       : ��ȭ�Ա� Demo
 * FileName     : serverCheckPage.jsp
 * Version      : 1.0
 * Description  : ��ȭ�Աݰŷ� ��ȿ�� üũ 
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
	String szParam = request.getParameter("sv_param");	// ��ũ���ο��� ������ ���۵� �Ķ����

	String[] paramArray = szParam.split("/");
	
	String inBankCd   = paramArray[0].toString();	// �Աݰ��������ڵ�
	String inAcctNo   = paramArray[1].toString();	// �Աݰ��¹�ȣ
	String outBankCd  = paramArray[2].toString();	// ��ݰ��������ڵ�
	String outAcctNo  = paramArray[3].toString();	// ��ݰ��¹�ȣ
	String tranSeqNo  = paramArray[4].toString();  	// �ŷ��Ϸù�ȣ

	boolean tranCheck = true;
	String chkval = "N";
	
	/*  
	 * [�ŷ��Һ� ���� �ŷ���ȿ�� üũ ���� �߰�]
	 *	�Ա�����, �������, �ŷ��Ϸù�ȣ ������ �Ա��ص� ���� ������ �������� ���������� üũ �մϴ�.
	 */
	
	// ��ȿ�� ���� ��� �����ΰ�� "Y"
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
