<%--
/* =============================================================================
 * System       : EXCHAIN 솔루션 Demo
 * FileName     : acctDemo.jsp
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
<!DOCTYPE html>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<link rel="StyleSheet" type="text/css"  href="../../theme/deposit/css/default_import.css" />
<link rel="StyleSheet" type="text/css"  href="../../theme/deposit/css/exchain.css"/>
<script type="text/javascript" src="common/exchain_demo.js"></script>

<title>주식회사 익스체인</title>
<%@ include file="../../../inc/globalVar.jsp" %>
<%@ include file="../../../inc/plugin_main-src.jsp" %>
<%@ include file="../../../inc/common_dialog.jsp" %>

<script type="text/javascript">

$(function () {
	
	/*************************************************************************/
	/* 입출금 계좌등록, 원화(KRW)입금 버튼 클릭 이벤트입니다.                */
	/*************************************************************************/
	$("a.doMovePage").on("click", function(){
		var path = $(this).data("path");
		
		/*************************************************************************/
  		/* path에 설정된 페이지로 이동합니다.                                    */
  		/*************************************************************************/
		COMM.nextPage({
			url:    path
		,	param:  null
		});
		
		return false;
	});
	
});
</script>
</head>

<body>
	<header>
		<div class="top" >
			<a href="acctDemo.jsp" class="logo"><img src="../../theme/deposit/images/exchain_logo.png" alt="2XCHANGE LOGO"></a>
			<!--  <span class="header_txt">익스체인 입금솔루션 Demo</span> -->
		</div>         
	</header>

<!-- CONTANTS START -->
	<div class="contants">
		<div id="exchain">
 			<form id="demoMainform">
 				<article>
 					<section class="con-wrap">
	 					<div class="col-center">
	 						<div class="tit_group">
		  						<p class="s-logo"><img src="../../theme/deposit/images/exchain_logo.png" alt="2XCHANGE LOGO"></p>
								<h2>입금솔루션 DEMO 입니다.</h2>
							</div>
	         				<div class="detail img-h">
								<ul>
									<li>
			    						<div class="btns col2">
					  						<div><a href="#" class="btn_type2 doMovePage" style="cursor:pointer" data-path="acctcheck/acctCheck00.jsp">입출금 계좌등록</a></div>	<!-- 입출금 계좌등록 버튼  -->
					  						<div><a href="#" class="btn_type2 doMovePage" style="cursor:pointer" data-path="deposit/acctTran00.jsp">원화(KRW)입금</a></div>	<!-- 원화입금 버튼  -->
			    						</div>
									</li>
								</ul>
	         				</div>
	 					</div><!-- col-center -->
 					</section><!-- con-wrap -->
 				</article>
 			</form>
		</div><!-- exchain -->
	</div>
   <!-- CONTANTS END -->
   <!--    
   <footer>
   		<strong class="question">◈ 제휴문의 : sjlee@exchain.co.kr</strong>
   </footer> 
   -->
</body>
</html>