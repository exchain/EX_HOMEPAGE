<%--
/* =============================================================================
 * System       : 입출금 계좌등록 Demo
 * FileName     : acctCheck70.jsp
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
<title>Exchain AcctCheck Demo</title>

<script type="text/javascript">
$(function () {
	
	var rmsg  = "${param.rmsg}"			// acctCheck10.jsp 에서 전달된 [오류메세지] 

	$("#rmsg").text(rmsg); 
	
	/*************************************************************************/
	/* 확인 버튼 클릭 이벤트입니다.                                          
	/*************************************************************************/
	$("#btnConfirm").on("click", function(){
		
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
				<section class="max-w" >
					<div class="ment">
 						<div id="ment_wrap">
 							<p class="checked"><img src="../../theme/deposit/images/warning.png" style="widht:60px; height:60px;"></p>
 							<h4 class="ment_tit">본인계좌가 아닙니다.</h4>
 							<div class="link_txt" id="rmsg"></div>
 							<div class="btns col2 cancel_btns">
 								<div class="btn_center holder"><a id="btnConfirm" class="btn_type2">확인</a></div>
 							</div>
 						</div>
					</div>
				</section>
			</article>
	
		</form>
	</div>
</body>
</html>
