/* =============================================================================
 * System       : F-CMS
 * FileName     : fwf-extend.js
 * Version      : 1.1
 * Description  : java script 확장 script
 * Author       : 이 병 직
 * Date         : 2013.03.29
 * -----------------------------------------------------------------------------
 * Modify Date  : 
 * -----------------------------------------------------------------------------
 * Etc          :  Java script 및 jQuery 확장코드 
 * -----------------------------------------------------------------------------
 * version 1.0 	: 2012.11.25
 * version 1.1  : 2013.03.29
 * 				: propView 변경
 * -----------------------------------------------------------------------------
 * Copyrights 2013 by Finger. All rights reserved. ~ by Finger.
 * =============================================================================
 */

//////////////////////////////////////////////
// 테스트 도구 (ie9는 console 모드에서만 작동)
//////////////////////////////////////////////
var console = window.console || {
    log: function () {} 
,   info: function () {}
,   warn: function () {}
,   error: function () {}
,   assert: function () {}
}; 
var propView = propView || function ( object, desc ) {
	if ( typeof desc === "undefined") {
		desc = "None";
	}
    console.info("=== Properties List Start === Description: ", desc);
    for (var x in object) {
        console.warn(x);
        console.log(object[x]);
    }
    console.info("=== Properties List End === Description: ", desc);
};

// 스크린상태 확인: 브라우별로 듀얼일때 값읽는 방식이 다르다.
if (DEBUG_MODE) {
	propView( screen, "screen 속성 테스트: Extend.js " );
}

if (window.console) {
	console.info("-------------------------");
	console.info("fwf-plugin loding......");
	console.info("-------------------------");
	console.info("fwf-extend.js Version: 1.1");
}

///////////////////////////
// jQuery 확장 fwf 메소드
///////////////////////////
(function ($){
    $.fwf = $.fwf || {
        // resizeStop - 과도한 동작을 방지. (02.05) - threshold 기준( 마치 stop 시 동작하는 것처럼 ).
        resizeStop: function(eventData, handler, setThreshold){
            var threshold = setThreshold || 600
            ,   event = {
                    data: eventData //resize 형식을 따름.
                }
            ,   stopAction = function(){
                    handler(event);
                }
            ,   timer = null;
            
            if (typeof eventData === "function" && typeof handler === "undefined"){
                handler = eventData;
                
            }
            
            $(window).resize( function(){
                if(timer){
                    clearTimeout(timer);
                }
                timer = setTimeout(stopAction, threshold);
            });
            
        }, // resizeStop
    
    	// getCookie - jquery 플러그인도 있지만, 간단한 뒤로가기, f5 구분에 사용 (04.26)
        getCookie: function ( cookieName ) {
        	var search = cookieName+"="
        	,   start, end, result ={}
        	,   cookie = document.cookie;
        	
        	if (!cookieName && window.console) {
        		console.error("plsase enter cookieName");
        	}
    	
        	if (cookie && (start = cookie.indexOf(search)) != -1){
        		end = cookie.indexOf(";", start + search.length);
        		if (end === -1) {
        			end = cookie.length;
        		}
    		
        		result.cookie =  cookie.slice(start, end);
        		result.name = result.cookie.slice(0, result.cookie.indexOf("="));
        		result.value = result.cookie.slice(result.cookie.indexOf("=")+1);
        	}
    	return result;
        }, // getCookie
        
        // 쿠키 삭제- login.jsp 에서 처리하면 의미가 없기 때문에 작성
        deleteCookie: function (cookieName) {
        	var expire = new Date();
        	document.cookie = cookieName + "=;expires=" + expire.setDate((expire.getDate()-1)).toString() +";path=/";
        },
        
        // jQuery 1.8.x 까지 사용된 browser 판별법
        browser: function() {
        	var a = navigator.userAgent.toLowerCase()
        	,   match = /(chrome)[ \/]([\w.]+)/.exec(a)||/(webkit)[ \/]([\w.]+)/.exec(a)||/(opera)(?:.*version|)[ \/]([\w.]+)/.exec(a)||/(msie) ([\w.]+)/.exec(a)||a.indexOf("compatible")<0&&/(mozilla)(?:.*? rv:([\w.]+)|)/.exec(a)||[];
        	return{browser:match[1]||"",version:match[2]||"0"}; 
        }
        
    };
})(jQuery);


//////////////////////////////////////////
//// 구형 javaScript 엔진용 확장 코드 ////
//////////////////////////////////////////

// trim (IE7,8용으로 추가) - $.trim 사용을 권장.
if ( !String.prototype.hasOwnProperty("trim") ) {
    String.prototype.trim = function() {
        return this.replace(/^\s+|\s+$/g, '');
    };
}

if ( typeof Object.create !== "function" ){
    Object.create= function(o){
        var F = function(){};
        F.prototype = o;
        return new F();
    };
}

//implement JSON.stringify serialization (IE 7 용) - parse 는 $.parseJSON 사용.
//Code from: http://www.sitepoint.com/blogs/2009/08/19/javascript-json-serialization/
var JSON = JSON || {};

JSON.stringify = JSON.stringify || function (obj) {
    var t = typeof (obj);
    if (t != "object" || obj === null) {
        // simple data type
        if (t == "string") obj = '"'+obj+'"';
        return String(obj);
    }
    else {
        // recurse array or object
        var n = null, v, json = [], arr = (obj && obj.constructor == Array);
        for (n in obj) {
            v = obj[n]; t = typeof(v);
            if (t == "string") v = '"'+v+'"';
            else if (t == "object" && v !== null) v = JSON.stringify(v);
            json.push((arr ? "" : '"' + n + '":') + String(v));
        }
        return (arr ? "[" : "{") + String(json) + (arr ? "]" : "}");
    }
};
