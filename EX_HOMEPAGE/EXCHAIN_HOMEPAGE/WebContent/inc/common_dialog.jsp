<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.finger.fwf.uxui.util.UCS" %>
<!-- dialog define start -->
<div id="dialog_set">
	<!-- confirmation -->
	<div id="confirm_wrapper" style="display: none;" > 
	    <div class="fwf_dialog_content">
	        <div id="confirm__message" class="fwf_dialog_message fwf_dialog_msg_bg" ></div>
	    </div>
	</div>
	
	<!-- error -->
	<div id="error_wrapper" style="display:none">
	    <div class="fwf_dialog_content">
	        <div id="error_message" class="fwf_dialog_message fwf_dialog_msg_bg" ></div>
	        <div><span id="btn_more" class="float-r ui-icon fwf-icon-down2" ></span></div> 
	        <div id="more_error" class="fwf_dialog_message fwf_dialog_more_err fwf_dialog_msg_bg"  style="display:none;"></div>
	    </div>
	</div>
	
	<!-- message -->
	<div id="message_wrapper" style="display: none;"> 
	    <div class="fwf_dialog_content">
	        <div id="message_message" class="fwf_dialog_message fwf_dialog_msg_bg" ></div>
	    </div>
	</div>
	
	<!-- warning -->
	<div id="warning_wrapper" style="display: none;">
	    <div class="fwf_dialog_content_warning">
	        <ul class="float-r">
	           <li><div id="warning_message" class="fwf_dialog_message fwf_dialog_message_war" ></div></li>
	        </ul>
	    </div>
	</div>
	
	<!--  alarm -->
	<div id="alarm_warpper" style="display: none;">
	    <div>
	        <div class="fwf_alarm_front"></div>
	        <div class="fwf_alarm_content">
	            <div class="fwf_alarm_message">
	            	<p class="gray h18"><%=UCS.Cont("COMMON2002", request)%></p>
	                <%=UCS.Cont("COMMON2003", request)%> <span id="fwf_alarm_count" class="fwf_alarm_color"></span> <%=UCS.Cont("COMMON2004", request)%>
	            </div>
	        </div>
	    </div>
	</div> 
	
	<!--  progressbar -->
	<div id="progressbar_wrapper" style="display: none;"></div>
	
	<!--  popup -->
	<div id="popup_wrapper" style="display: none;">
	    <div>
	        <div class="fwf_popup_front"></div>
	        <div class="fwf_popup_content" id="fwf_popup_body"></div>
	    </div>
	</div>
	
	<!--  sub popup -->
	<div id="sub_popup_wrapper" style="display: none;">
	    <div>
	        <div class="fwf_popup_front"></div>
	        <div class="fwf_popup_content" id="fwf_sub_popup_body"></div>
	    </div>
	</div>
	
    <!-- debug -->
    <div id="debug_wrapper" style="display:none">
        <div class="fwf_dialog_content">
            <div id="debug_message" class="fwf_dialog_message fwf_dialog_msg_bg" ></div>
            <div id="debug_more" class="fwf_dialog_message fwf_dialog_more_err fwf_dialog_msg_bg"  style="display:none;"></div>
        </div>
    </div>
</div>
<!-- dialog define end -->