<%--
/* =============================================================================
 * System       : 입출금 계좌등록 Demo
 * FileName     : acctCheck30.jsp
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
    /* DEMO용 버튼 이벤트(원장 반영 정상, 실패 처리)
    /*************************************************************************/
	$("a.doMovePage").on("click", function() {
        var result = $(this).data("result");
        
        if (result == "success") {
        	/*************************************************************************/
            /* 입출금 계좌등록 완료 페이지로 이동합니다.
            /*************************************************************************/
        	COMM.nextPage({
	            url:   "acctcheck/acctCheck80.jsp",
	            param: null
	        });
        } else {
        	/*************************************************************************/
            /* 입출금 계좌등록시 오류 발생에 따른 처리 내용을 작성합니다.
            /*************************************************************************/
            alert("입출금 계좌등록 오류");
        	
            location.href = "acctDemo.jsp";
        	return;
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
	                    <section class="pay_processwarp mt40" style="padding:15px 60px;">
	                        <div class="pay_process"><span class="act arrow">원장 반영 처리</span></div>
	                    </section>
	                </div>
                    <div class="detail img-h">
                        <ul>
                            <li>
                                <div class="btns col2">
                                    <div><a href="#" class="btn_type2 doMovePage" style="cursor:pointer" data-result="success">정상</a></div>
                                    <div><a href="#" class="btn_type2 doMovePage" style="cursor:pointer" data-result="failure">실패</a></div>
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