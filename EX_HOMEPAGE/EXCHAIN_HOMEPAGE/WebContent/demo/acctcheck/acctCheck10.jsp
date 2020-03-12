<%--
/* =============================================================================
 * System       : 입출금 계좌등록 Demo
 * FileName     : acctCheck10.jsp
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

	/*************************************************************************/
	/* 입출금 계좌등록 버튼 클릭 이벤트입니다.
	/*************************************************************************/
	$("#btnAcctCheck").on("click", function(){		
		
		/*************************************************************************/
		/* 1. 입력받은 데이터를 세팅합니다.
		/*************************************************************************/
		var ex_cust_id	= "${param.ex_cust_id}";			// 고객번호(익스체인에서 서비스 이용시 발급됩니다.)
		var bankcd  	= $("#bank option:selected").val();	// 은행코드
	 	var acctno  	= $.trim($("#acct_no").val());		// 계좌번호('-'제거)
		var custnm 		= "${param.custnm}";				// 거래소 원장정보 설정화면에서 전달된 [고객명] 
	
		/*************************************************************************/
		/* 2. 입력받은 데이터 유효성을 체크합니다.
		/*  - 은행코드, 계좌번호 입력 여부 확인
		/*************************************************************************/
		if(bankcd == '')
		{
			COMM.dialog({
       			type: "message",
                message: "은행을 선택해 주세요."
            });

			return false;
		}
		
		if(acctno == '')
		{
			COMM.dialog({
       			type: "message",
                message: "계좌번호를 입력해 주세요."
            });

			return false;
		}
		
		/*************************************************************************/
		/* 홈페이지 반영용 로직 시작
		/*************************************************************************/
		
		/*************************************************************************/
		/* 3. 서버로 입력 데이터를 전달하여 응답결과를 받습니다.                
		/*    - 샘플소스 : AcctCheckSample.java 참조
		/*************************************************************************/
		/*
		$("input[name=ex_cust_id]").val(ex_cust_id); 	// 고객아이디
		$("input[name=ex_svc_cd]").val("0120"); 		// 서비스코드
		$("input[name=bank_cd]").val(bankcd); 	    	// 은행코드
		$("input[name=acct_no]").val(acctno); 			// 계좌번호
		$("input[name=acct_nm]").val(custnm); 			// 고객명
		
		COMM.showByTasking({
	        taskID: "ACCT00010",               
	        formID: "acctCheckForm",
	        callback: function(data) {
	            
	        	var rcode = data.resultData["rst_code"];	// 결과코드
	        	var rmsg  = data.resultData["rst_msg"];		// 결과메세지
	        	//var rname = data.resultData["rst_name"];	// 조회된 예금주명
	        	
        		var paramObj = new Object();
               	paramObj.rcode = rcode; 	
               	paramObj.rmsg  = rmsg; 	
               	//paramObj.rname = rname; 	
               	
	            if (rcode == "0000") 	// 예금주명 일치 (원장 반영처리 완료)
	            {
	            	/*************************************************************************/
			  		/* 3.1. 입출금 계좌등록 완료 페이지로 이동합니다.                        							                 
			  		/*************************************************************************/
	          	/*	COMM.nextPage({
	                    url: "acctcheck/acctCheck80.jsp",
	                	param: paramObj
	                }); 
	            }
	            else 						// 예금주명 불일치
	            {
	            	/*************************************************************************/
			  		/* 3.2. 입출금 계좌등록 불일치(오류) 페이지로 이동합니다.                						   
			  		/*************************************************************************/
	            /*    COMM.nextPage({
	                    url: "acctcheck/acctCheck70.jsp",
	                	param: paramObj
	                }); 
	            }
	        }
	    });
	
		/*************************************************************************/
		/* 홈페이지 반영용 로직 끝.
		/*************************************************************************/
		
		/*************************************************************************/
		// 샘플 소스용 로직 시작
		/*************************************************************************/
		
        var paramObj = new Object();
        paramObj.ex_cust_id    	= ex_cust_id;		// 고객아이디
        paramObj.ex_svc_cd    	= "0110";			// 서비스코드
        paramObj.bank_cd 		= bankcd;			// 은행코드
        paramObj.acct_no 		= acctno;			// 계좌번호
        paramObj.acct_nm 		= custnm;			// 고객명

		/*************************************************************************/
		/* 3. 본인계좌 확인 페이지로 이동합니다.
		/*  - 파라미터 : 고객번호, 은행코드, 계좌번호, 고객명
		/*************************************************************************/
        
		COMM.nextPage({
            url: "acctcheck/acctCheck20.jsp",
            param: paramObj
        });     
        
        
        /*************************************************************************/
     	// 샘플 소스용 로직 끝
		/*************************************************************************/
	}); 
	
	/*************************************************************************/
    /* DEMO용 버튼 이벤트
    /*************************************************************************/
	$(".demo_btn_r").on("click", function() {
		$("#bank").val("088");
        $("#acct_no").val("110465187992");
    });
    
    $(".demo_w_btn").click(function() {
        $("#bank").val("004");
        $("#acct_no").val("123456789000");
    });	
});
</script>

</head>
<body> 
	<div id="exchain">
		<form id="acctCheckForm">	
		
			<input type="hidden" name="ex_cust_id"    value="">
			<input type="hidden" name="ex_svc_cd"    value="">
			<input type="hidden" name="bank_cd" value="">
			<input type="hidden" name="acct_no" value="">
			<input type="hidden" name="acct_nm" value="">
	
		<article>
			<section class="con-wrap">
				<div class="tit_group">
					<h2 id="pageTitle"></h2>
				</div>
	
				<div class="detail">
					<ul>
						<li id="pvCustInfo" >
							<div class="flexwrap check_c">
								<div class="column">
									<p class="notmark">입출금 계좌로 등록할 본인계좌 정보 입력</p>
									<div class="info">
										<p class="tleft">
											<span id="pv_cust_info"></span>
										</p>
									</div>
								</div>								
							</div>
						</li>
	       
						<li>
							<div class="flexwrap">
								<div class="column" style="margin-top:15px;">
									<p class="tit_float">은행</p>
									<select title="은행선택" id="bank" class="bank">
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
								<div class="column">
									<p class="tit_float">계좌번호</p>
									<input id="acct_no" type="text" class="choose" maxlength="20">
								</div>
							</div>
 						</li>
 						
					</ul>
					<div class="btns col2">
						<div class="btn_center holder"><a id="btnAcctCheck" class="btn_type2">입출금 계좌등록</a></div>
					</div>
				</div>
				<div class="demo_wrap">
					<div class="demo_btn demo_1"><a class="demo_btn_r"><img src="../../theme/deposit/images/btn_star.png"></a></div>
					<div class="demo_btn check_1"><a class="demo_w_btn"><img src="../../theme/deposit/images/btn_star_2.png"></a></div>
				</div>
			</section>
		</article>
		</form>
	</div>
</body>
</html>