<%--
/* =============================================================================
 * System       : 입출금 계좌등록 Demo
 * FileName     : acctCheck00.jsp
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

	$("#ex_cust_id").val("EXCHAIN");
	
	/*************************************************************************/
	/* 원장정보설정 버튼 클릭 이벤트입니다.                                  		
	/*************************************************************************/
	$("#btnDemoSet").on("click", function(){

		/*************************************************************************/
		/* 1. 입력받은 데이터를 세팅합니다.                                    		
		/*  - 거래소 원장정보에 있는 고객명입니다.                               		
		/*************************************************************************/
		var ex_cust_id 	= $.trim($("#ex_cust_id").val()); 		// 고객식별번호
		var custnm  	= $.trim($("#cust_nm").val());			// 고객명

		/*************************************************************************/
		/* 2. 입력받은 데이터 유효성을 체크합니다.                               		
		/*  - 고객명, 고객식별번호 입력 여부 확인								           		
		/*************************************************************************/
		if(ex_cust_id == '')
		{
			COMM.dialog({
       			type: "message",
                message: "고객식별번호를 입력해 주세요."
            });

			return false;
		}
		
		if(custnm == '')
		{
			COMM.dialog({
       			type: "message",
                message: "고객명을 입력해 주세요."
            });

			return false;
		}

		var paramObj = new Object();
		paramObj.ex_cust_id = ex_cust_id;
       	paramObj.custnm = custnm; 		

       	/*************************************************************************/
  		/* 3. 입출금 계좌등록 페이지로 이동합니다.                               
  		/*  - 파라미터 : 고객명									                 
  		/*************************************************************************/
        COMM.nextPage({
            url: "acctcheck/acctCheck10.jsp",
        	param: paramObj
        }); 
	}); 
	
	/*************************************************************************/
	/* DEMO 전용 원장정보입력 퀵 버튼 이벤트
	/*************************************************************************/
	$(".demo_btn_r").on("click", function() {
		$("#cust_nm").val("이성주");
	});
});

</script>

</head>
<body> 
	<div id="exchain">
		<form id="acctCheck">	
			<div class="tit_wrap">
				<h2 class="tit_00">거래소 원장정보 설정화면 <small>(DEMO전용)</small></h2>
			</div>
			<article>
				<section class="con-wrap acctcheck_h">
					<div class="sv_key">
						<p class="key_txt">고객번호</p>
						<input id="ex_cust_id" type="text" class="choose" malength="7" style="width:110px; height:35px;">
					</div>
					<p class="sub_title_00">[거래소에 가입된 고객정보] </p>
					<div class="detail">
						<ul>   
							<li>
								<div class="flexwrap">
									<div class="column">
										<p class="tit_float">고객명</p>
										<input id="cust_nm" type="text" class="choose" maxlength="20">
									</div>
								</div>
							</li>
						</ul>
		     
						<div class="btns col2">
							<div class="btn_center holder"><a id="btnDemoSet" class="btn_type2">원장정보설정</a></div>
						</div>
					</div>
					<div class="demo_wrap">
						<div class="demo_btn"><a class="demo_btn_r"><img src="../../theme/deposit/images/btn_star.png"></a></div>
					</div>
				</section>
			</article>
		</form>
	</div>
</body>
</html>
