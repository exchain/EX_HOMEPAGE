<%--
/* =============================================================================
 * System       : 입출금 계좌등록 Demo
 * FileName     : acctCheck20.jsp
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
	COMM.progressbar("on");
	
	/*************************************************************************/
    /* 1. 입력받은 데이터를 세팅합니다.                                          
    /*************************************************************************/
    var ex_cust_id    = "${param.ex_cust_id}";
	var ex_svc_cd     = "${param.ex_svc_cd}";
    var bank_cd 	  = "${param.bank_cd}";
	var acct_no 	  = "${param.acct_no}";
	var acct_nm 	  = "${param.acct_nm}";
	
	var params = 'ex_cust_id='  + ex_cust_id + '&' +
				 'ex_svc_cd='   + ex_svc_cd  + '&' +
                 'bank_cd='     + bank_cd    + '&' +
                 'acct_no='     + acct_no    + '&' +
                 'acct_nm='     + acct_nm;
	
	//var url = 'https://dev.2xchange.co.kr:9443/ASP/api/bank/EX0110';         // 개발
	var url = 'https://www.2xchange.co.kr:9443/ASP/api/bank/EX0110';            // 운영
	
    fetch(url, {
		method: 'POST',
		timeout: 1000,
		headers: {
			Accept: 'application/json',
			'Content-Type': 'application/json'
		},
		body: params
	})
	.then((response) => response.json())
	.then((data) => {
		COMM.progressbar("off");
		
	    var paramObj = new Object();
        paramObj.rcode = data.rst_code;		// 결과코드
        paramObj.rmsg  = data.rst_msg;		// 결과메세지
        
		/*************************************************************************/
	    /* 2. 본인계좌 확인 결과에 따라 분기 처리합니다.
	    /*************************************************************************/
		if (data.rst_code == "0000")  // 예금주명 일치 (원장 반영처리 완료)
        {
			/*************************************************************************/
	        /* 3.1. 원장 반영처리 페이지로 이동합니다.
	        /*************************************************************************/
            COMM.nextPage({
                url: "acctcheck/acctCheck80.jsp",
                param: paramObj
            });
        }
        else                      // 예금주명 불일치
        {
          /*************************************************************************/
          /* 3.2. 입출금 계좌등록 불일치(오류) 페이지로 이동합니다.                                           
          /*************************************************************************/
            COMM.nextPage({
                url: "acctcheck/acctCheck70.jsp",
                param: paramObj
            }); 
        }
	});
});
</script>

</head>
<body> 
	<div id="exchain">
		<form id="acctCheck"> 
            <article>
                <section class="con-wrap acctcheck_h">   
                    <div class="tit_group" >
                        <section class="pay_processwarp mt40" style="padding:15px 50px;">
                            <div class="pay_process"><span class="act arrow">본인 계좌 확인 중</span></div>
                        </section>
                    </div>
                </section>
            </article>
        </form>
	</div>
</body>
</html>