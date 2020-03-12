/* =============================================================================
 * System       : EXCHAIN DEMO
 * FileName     : exchain_demo.js
 * Version      : 1.0
 * Description  : exchain demo 공통 script
 * Author       : 
 * Date         : 2019.01.10
 * -----------------------------------------------------------------------------
 * Modify Date  : 
 * -----------------------------------------------------------------------------
 * version 1.0 	: 
 * -----------------------------------------------------------------------------
 * Etc          : 
 * -----------------------------------------------------------------------------
 * Copyrights 2019 by Exchain. All rights reserved. ~ by Exchain.
 * =============================================================================
 */

var securityModule = function (ajaxObj , callback) {
    COMM.progressbar("on");
    
    // FSWSS모듈체크
	ajaxObj.fCallback = FSWSSCheckCheckCallback;
	ajaxObj.pCallback = callback;
	
    FSWSSCheck(ajaxObj); 
    
};

var FSWSSCheckCheckCallback = function (ajaxObj, resultObj) {
	
	if(typeof ajaxObj == "undefined" || typeof resultObj == "undefined")
		return;
	
	var fwscheck = resultObj.fwsCheck;
	
	if(fwscheck)
	{
		// 은행보안모듈 체크
		ajaxObj.bCallback = BANKSecurityCheckCallback;
		ajaxObj.fwscheck  = fwscheck;
		ajaxObj.pCallback = ajaxObj.pCallback;
		
		BANKSecurityCheck(ajaxObj);
	}else
	{
	    ajaxObj.fwscheck  = fwscheck;
	    ajaxObj.bankcheck = false;
	    
		var paramObj   = new Object();
    	paramObj.url   = ajaxObj.instPage;
	    paramObj.param = ajaxObj;
	    COMM.progressbar("off");
	    
	    if(ajaxObj.pCallback){
	    	ajaxObj.pCallback(false);
	    }else{
	    	COMM.nextPage(paramObj);
	    }
	}
}

var FSWSSCheck = function (ajaxObj) {
	
	var returnObj 	    = new Object();
	returnObj.fwsCheck 	= false;
	
    $.ajax({
    	type: "GET",
    	timeout: 4500,
    	url: "https://local.finger.co.kr:52187/UpdateEngine?service_key=" + ajaxObj.serviceKey,
    	success: function(data) {	//설치되어있는 경우
    		console.log(">>> FSWSS설치  " );
    		returnObj.fwsCheck = true;    		
    	},
		error:function(xhr, status, error) {
			
			COMM.progressbar("off");
			returnObj.fwsCheck = false;    
		},
		complete:function(data) {
			
            if ( $.isFunction(ajaxObj.fCallback)) {
            	ajaxObj.fCallback(ajaxObj, returnObj);
            }
		}
    });
};

var BANKSecurityCheck = function (ajaxObj) {
	var returnObj 			= new Object();
	returnObj.bankCheck 	= false;
	returnObj.scrappingData = "";	//스크래핑 데이터

    $.ajax({
		type: "GET",
    	dataType : "jsonp",
		url: "https://local.finger.co.kr:52187/ScrapService?service_key=" + ajaxObj.serviceKey + "&timeout=120" + "&sid=s9040603&bank_id=" + "01" + ajaxObj.bankId, //최종적으론 01004 형태로 넘어감
		success: function(data) {			
			console.log(">>> 은행보안모듈 설치" );			
			returnObj = data;
			
		},
		error:function(xhr, status, error) {
			COMM.progressbar("off");
			COMM._ajaxError(xhr, status, error);
		},  
		complete:function(data) {
			COMM.progressbar("off");
			
			if(data.responseJSON.errCode == '104')
			{
				COMM.dialog({
	       			type: "message",
	                message: "사용가능한 서비스키가 아닙니다.",
	                callback: function()
	                {
	                    COMM.nextPage({
	                        url: "deposit/acctTran00.jsp",
	                    	param: null
	                    }); 
	                }
	            });
				
				return false;
			}
			
            if ( $.isFunction(ajaxObj.bCallback)) {
            	ajaxObj.bCallback(ajaxObj, returnObj);
            }
		}
	});    
};

var BANKSecurityCheckCallback = function (ajaxObj, resultObj) {
	
	if(typeof ajaxObj == "undefined" || typeof resultObj == "undefined")
		return;

    var instPage  = ajaxObj.instPage
    ,   nextPage  = ajaxObj.nextPage
    ,   bankId    = ajaxObj.bankId
    ,   fwscheck  = ajaxObj.fwscheck
    ,   bankcheck = false
    ,   count     = 0;
    
    var resultData = resultObj.list;
    
	$.each(resultData, function(index, obj) {
		if(obj.install != "Y")
			count = count +1 ;
	});

    ajaxObj.scrappingData = JSON.stringify(resultObj.list);
    
    var paramObj = new Object();
    
    if(count == 0)
    {
    	bankcheck = true;
    	paramObj.url = nextPage;
    }else
    {
    	bankcheck = false;
    	paramObj.url = instPage;
    }
    
    ajaxObj.fwscheck      = fwscheck;
    ajaxObj.bankcheck     = bankcheck;
    paramObj.param        = ajaxObj;

    if(ajaxObj.pCallback){
		ajaxObj.pCallback(true , bankcheck , ajaxObj.scrappingData);
    }else{
    	COMM.nextPage(paramObj);
    }	
};


var isNumber = function (number) {
    var regExp = new RegExp(/^[0-9]*$/);
    
    if (!regExp.test(number))
        return false;
    
    return true;
};
