/* =============================================================================
 * System       : BOScoin
 * FileName     : fwf-bos-common.js
 * Version      : 1.0
 * Description  : boscoin 공통 script
 * Author       : 
 * Date         : 2018.02.13
 * -----------------------------------------------------------------------------
 * Modify Date  : 
 * -----------------------------------------------------------------------------
 * version 1.0 	: 
 * -----------------------------------------------------------------------------
 * Etc          : 
 * -----------------------------------------------------------------------------
 * Copyrights 2013 by Finger. All rights reserved. ~ by Finger.
 * =============================================================================
 */

Number.prototype.comma = function () {
	var str = this.toString().replace("NaN" , "");
	var arrStr = str.split(".");
	arrStr[0] = arrStr[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",")
	return arrStr.join(".");
};

String.prototype.comma = function () {
	return Number(this.unComma()).comma();
};

Number.prototype.unComma = function () {
	return this.toString().unComma();
};

String.prototype.unComma = function () {
	var str = this;
	return parseInt((str || "0" ).replace(/[^\d]+/g, ''), 10);
};

/* 
 * console 숨김
 * */
window.console.log = function(msg){
}
window.console.error = function(msg){
}
 



String.prototype.string = function(len){var s = '', i = 0; while (i++ < len) { s += this; } return s;};
String.prototype.zf = function(len){return "0".string(len - this.length) + this;};
Number.prototype.zf = function(len){return this.toString().zf(len);};

Number.prototype.toSec = function(){
	return(this.zf(2) + "초");
};

Number.prototype.toMinSec = function(){
	var min;
	var sec;
	// 정수로부터 남은 분, 초 단위 계산
	min = Math.floor(this/60);
	sec = this - (min*60);
	return(min.zf(2) + "분 " + sec.zf(2) + "초");
};


Date.prototype.format = function(f) {
	if (!this.valueOf()) return " ";

	var weekName = ["일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"];
	var weekNameS = ["일", "월", "화", "수", "목", "금", "토"];
	var d = this;

	return f.replace(/(yyyy|yy|MM|dd|E|hh|mm|ss|a\/p)/gi, function($1) {
		switch ($1) {
			case "yyyy": return d.getFullYear();
			case "yy": return (d.getFullYear() % 1000).zf(2);
			case "MM": return (d.getMonth() + 1).zf(2);
			case "dd": return d.getDate().zf(2);
			case "E": return weekName[d.getDay()];
			case "e": return weekNameS[d.getDay()];
			case "HH": return d.getHours().zf(2);
			case "hh": return ((h = d.getHours() % 12) ? h : 12).zf(2);
			case "mm": return d.getMinutes().zf(2);
			case "ss": return d.getSeconds().zf(2);
			case "a/p": return d.getHours() < 12 ? "오전" : "오후";
			default: return $1;
		}
	});
};

String.prototype.formatBosGMT9 = function(f) {
	var $c = new Date(this); 
	return $c.format(f);
};

String.prototype.masking = function(len) {
	var str = this;
	var arr = [];
	arr.push(str);
	if(str.indexOf("@") > -1){
		arr = [];
		if(str.indexOf(".") > -1){
			var temp = str.split("@");
			var temp2 = temp[1].split(".");
			arr.push(temp[0]);
			arr.push("@"+temp2.splice(0 , 1));
			arr.push("."+temp2.join("."));
		}else{
			arr = str.split("@");
			arr[1] = "@" + arr[1]; 
		}
	}
	
	for(var i = 0 ; i < 2 ; i++){
		var t1 = arr[i];
		if(i > 0){
			len = 2;
		}
		var t2 = t1.substring(0 , len);
		var t3 = t1.substring(len);
		t3 = "*".string(t3.length);
		arr[i] = t2 + t3;
	}
	return arr.join("");
};

var _UTIL = {
		datePickerOption : function(){
			return {
				showOn: "focus"
				,buttonImage:"/resource/images/btn_icon_calendar.gif"
				,buttonImageOnly: true
				,changeMonth: true
				,changeYear: true
				,showButtonPanel: true
				,currentText: '오늘'
				,closeText: '닫기'
				,dateFormat: "yy-mm-dd"
				,dayNames: ['일요일' , '월요일', '화요일', '수요일', '목요일', '금요일', '토요일']
				,dayNamesMin: ['일' , '월', '화', '수', '목', '금', '토']
				,monthNamesShort: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월']
				,monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월']
				,showMonthAfterYear: true
				,maxDate: "D"
			};
		},
		setDatepicker : function(element){
			var $element = element || $(".datepicker");
			$element.attr("readonly", "readonly");
			$element.datepicker(_UTIL.datePickerOption());
			$.each($element , function(index , value){
				var $this = $(value);
				$this.siblings("[data-type]").off("click").on("click" , function(){
					var $button = $(this);
					var buttonType = $button.data("type");
					if(buttonType.indexOf("-") > -1){
						$button.siblings(".startDate").datepicker("setDate" , buttonType);
						$button.siblings(".endDate").datepicker("setDate" , "D");
					}else{
						$button.siblings(".startDate").datepicker("setDate" , "D");
						$button.siblings(".endDate").datepicker("setDate" , buttonType);
					}
					return false;
				});
			});
		},
		openLayer : function($popup , $dim){
			var $body = $("body");
			_UTIL.setCenter($popup);
			
			if($dim){
				$dim.fadeIn("fast");
			}
			$popup.fadeIn("fast");
			$popup.trigger("open").off("open");
			
			$popup.off("click" , ".pop_close").on("click" , ".pop_close" , function(){
				_UTIL.closeLayer($popup , $dim);
				return false;
			})
			$dim.off("click").on("click" , function(){
				_UTIL.closeLayer($popup , $dim);
				return false;
			});
			
			$(window).on("resize" , function(){
				if($popup.is(":visible")){
					_UTIL.setCenter($popup);
				}
			})
			
			return false;
		},
		closeLayer : function($popup , $dim){
			var $body = $("body");
			$dim.fadeOut("fast");
			$popup.fadeOut("fast");
			$body.css("overflow" , "auto");
			
			$popup.trigger("close").off("close");
			
			return false;
		},
		setCenter : function($popup){
			var $body = $("body");
			var scrollHeight = $(window).scrollTop();
			var wWidth = $(window).width();
			var wHeight = $(window).height();
			
			var pWidth = $popup.width();
			var pHeight = $popup.height();
			
			var activeWidth = ((wWidth - pWidth) /2) < 0 ? 0 : (wWidth - pWidth) /2; 
			var activeHeight = ((wHeight - pHeight) /2) < 0 ? 0 : (wHeight - pHeight) /2;
			activeHeight += scrollHeight;
			
			$popup.css({
				"left" : activeWidth
				,"top" : activeHeight
			});

			$body.css("overflow" , "hidden");
		},
		tooltipTimer : null,
		openTooltip : function($target , e){
			var $tooltip = $(".tooltip");
			if($tooltip.is(":visible")){
				$tooltip.hide();
				if(_UTIL.tooltipTimer){
					clearTimeout(_UTIL.tooltipTimer);
				}
			}
			var contents = $target.attr("title") || "";
			$tooltip.find("span").empty().append(contents);
			
			_UTIL.setTooltipPosition($tooltip , $target , e);
			
			$tooltip.fadeIn();
			
			_UTIL.tooltipTimer = setTimeout(function() {
				$tooltip.fadeOut();
			}, 4000);
			
			$tooltip.on("click" , function(){
				if($tooltip.is(":visible")){
					$tooltip.fadeOut();
				}
			})
			$(window).on("resize" , function(){
				if($tooltip.is(":visible")){
					$tooltip.fadeOut();
				}
			})
		},
		setTooltipPosition : function($tooltip , $target , e){
			var eTop = e.clientY;
			var eLeft = e.clientX;
			var scrollTop = $(window).scrollTop();
			var targetHeight = $target.height();
			var wWidth = $(window).width();
			
			$tooltip.css({
				top : eTop + scrollTop+ targetHeight,
				right : (wWidth - eLeft) - 10
			})
		},
		/**
		 * 	[타이머]
		 * _UTIL.timer.start(function(){
				func();
			} , 30 , true , $(element) , "sec");
			
		 */
		timer : {
			intervalFunc : null, //타이머
			timeSec : 60, //타이머 시간
			decTime : 0,
			zeroCallback : null, //0초일때 호출할 함수
			isLoop : true, // 반복여부
			$displayElement : null, // $(element)
			displayType : "minSec", // min , minSec
			start : function(zeroCallback , timeSec , isLoop , $displayElement , displayType){
				_UTIL.timer.pause();
				_UTIL.timer.timeSec = timeSec || _UTIL.timer.timeSec;
				_UTIL.timer.decTime = _UTIL.timer.decTime || _UTIL.timer.timeSec;
				_UTIL.timer.zeroCallback = zeroCallback || _UTIL.timer.zeroCallback;
				_UTIL.timer.isLoop = (typeof(isLoop) != "undefined") ? isLoop : _UTIL.timer.isLoop;
				_UTIL.timer.$displayElement = $displayElement || _UTIL.timer.$displayElement;
				_UTIL.timer.displayType = displayType || _UTIL.timer.displayType;
				
				//최초 1회 실행
				if(!_UTIL.timer.zeroCallback){
					_UTIL.timer.zeroCallback = function(){};
				}
				_UTIL.timer.zeroCallback(true);
				
				_UTIL.timer.intervalFunc = setInterval(function(){
					_UTIL.timer.decrement();
				} , 1000);
			},
			decrement : function(){
				_UTIL.timer.decTime--;
				
				if(_UTIL.timer.$displayElement){
					if(_UTIL.timer.displayType == "minSec"){
						_UTIL.timer.$displayElement.html(_UTIL.timer.decTime.toMinSec());
					}else{
						_UTIL.timer.$displayElement.html(_UTIL.timer.decTime.toSec());	
					}
				}
				
				if(_UTIL.timer.decTime <= 0){
					_UTIL.timer.pause();
					if(_UTIL.timer.isLoop){
						_UTIL.timer.decTime = _UTIL.timer.timeSec;
						_UTIL.timer.start();
					}else{
						_UTIL.timer.zeroCallback();
					}
				}
			},
			stop : function(){ //타이머 제거
				clearInterval(_UTIL.timer.intervalFunc);
				//타이머 초기화
				_UTIL.timer.intervalFunc = null;
				_UTIL.timer.timeSec = 60;
				_UTIL.timer.decTime = 0;
				_UTIL.timer.zeroCallback = null;
				_UTIL.timer.isLoop = true;
				_UTIL.timer.$displayElement = null;
				_UTIL.timer.displayType = "minSec";
			},
			pause : function(){//타이머 일시정지
				clearInterval(_UTIL.timer.intervalFunc);
			},
			resume : function(){//타이머 일시정지 해제
				_UTIL.timer.start();
			}
		}
}

$(function () {
    // 메뉴 event
    $("#openMenu").on("click", function() {
    	var $aside = $("aside");
    	var $dimmw = $(".dimmw");
    	$aside.toggleClass("on");
    	if($aside.hasClass("on")) {
    		$dimmw.show();
    	} else {
    		$dimmw.hide();
    	}
    	return false;
    });
    // 메뉴 닫기
    $("#closeMenu, .dimmw").on("click", function() {
    	$("aside").removeClass("on");
    	$(".dimmw").hide();
    	return false;
    });
    
    $("body").on("click" , ".layer-tooltip" , function(e){
		var $this = $(this);
    	_UTIL.openTooltip($this , e);
    	return false;
    })
    
    $(".openBuisnessPop").on("click" , function(){
    	//609-88-00479
    	var url = "http://www.ftc.go.kr/bizCommPop.do?wrkr_no=6098800479&apv_perm_no=";
    	var features = "menubar=no,toolbar=no,location=no,directories=no,status=no,scrollbars=yes,resizable=yes,width=680,height=750";
    	
    	window.open(url , "buisnessPop" , features);
    	
    	return false;
    });
    
});

var confirmPrivacy = function(callback){
	//FIXME
//	callback(true);
//	return false;
	
	var custState = $("[name=ssCustState]").val();
	var certNumber = "";
	var isTimeOver = false;
	//준회원은 스킵
	if(custState == "010"){
		callback(true);
		return;
	}
	var $popup = $("#fswssPop");
	var $dim = $(".smsDimmed");
	_UTIL.openLayer($popup , $dim);
	
	$popup.on("open" , function(){
	}).on("close" , function(){
		_UTIL.timer.stop();
		$(".pop-view-cert").hide();
		$(".pop-msg-timeover").hide();
		$("[name=pop_user_cert_number]").val("");
		$(".doSendCertSms").removeClass("disable");
	}).off("click" , ".doSendCertSms").on("click" , ".doSendCertSms" , function(){
		
		// 제한시간내 메세지 전송 불가능하게 처리 -- 2018.06.06 이성주
		if($(".doSendCertSms").hasClass("disable") == true)
			return;
		
		COMM.showByTasking({
			  taskID:     "MB2000010"
			, formID:     "noform"
			, pageID:     "no"
			, callback: function(data) {
				if (data.resultData != null) {
					if (data.resultData.result_cd == "OK") {
						certNumber = data.resultData.cert_number;
						$(".pop-view-cert").slideDown();

						// 인증번호버튼 비활성화
						$(".pop-msg-timeover").hide();
						$(".doSendCertSms").addClass("disable");
						
						_UTIL.timer.start(function(isFirst){
							if(!isFirst){
								isTimeOver = true;
								
								// 인증번호버튼 활성화
								$(".doSendCertSms").removeClass("disable");
								$(".pop-msg-timeover").slideDown();
							}
						} ,  10*60 , false , $(".pop-view-timeCount"))
						
						COMM.dialog({
							type: "message"
							, message: "휴대폰으로 발송된 인증번호를 확인하세요."
						});
						
					} else {
						COMM.dialog({
							type: "error"
							, message: "인증문자 발송이 실패했습니다." 
						});
					}
				}
			} 
		});
	}).off("click" , ".doCertSmsConfirm").on("click" , ".doCertSmsConfirm" , function(){
		if(isTimeOver){
			callback(false);
			return false;
		}
		var uCertNumber = $("[name=pop_user_cert_number]").val();
		
		if(!uCertNumber){
			COMM.dialog({
				type: "message"
				, message: "인증번호를 입력해주세요."
				, callback : function(){
					return false;
				}
			});
			return false;
		}
		
		if(uCertNumber != certNumber){
			COMM.dialog({
				type: "message"
				, message: "인증번호를 확인해주세요."
				, callback : function(){
					return false;
				}
			});
			return false;
		}
		
		_UTIL.closeLayer($popup , $dim);
		
		callback(true);
		
	})
	
};

/**
 * @description 보안모듈 설치여부 확인 
 * @version 1.0
 * @since 2018.03
 * @author 이성주
 * @event
 * @memberOf COMM
 * @param {Object} ajaxObj
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
    	url: "https://local.flyhigh-x.com:52177/UpdateEngine?service_key=c8cee8ee39754a8aa7107f8b34213276",
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
		url: "https://local.flyhigh-x.com:52177/ScrapService?service_key=c8cee8ee39754a8aa7107f8b34213276&timeout=120" + "&sid=s9040603&bank_id=" + "01" + ajaxObj.bankId, //최종적으론 01004 형태로 넘어감
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


/** 
 * 이메일주소 마스킹처리(abc***@gmail.com)
 */
var emailMasking = function (email) {
	
 	var len = email.split('@')[0].length;
   	var cnt = 0;
   
   	if(len < 3) {
		cnt = 1;
   	}else if (len == 3) {
	   	cnt = 2 
   	}else {
		cnt = 3
   	}
   
   	return email.replace(new RegExp('.(?=.{0,' + cnt + '}@)', 'g'), '*');
};

/** 
 * 핸드폰번호 마스킹처리(010-****-1111)
 */
var hpMasking = function (hp) {
	
	var pattern = /^(\d{3})-?(\d{2,3})-?\d(\d{4})$/;
    var result  = "";

    var match = pattern.exec(hp);

    if(match) {
        result = match[1]+"-****-"+match[3];
    } else {
        result = "****";
    }
    return result;
};

/** 
 * 문자열 끝자리 마스킹처리(홍길*)
 */
var strMasking = function (str) {
	
	if(str == 'undefined' || str == '') {
	        return "";
    }
	
	if(str.length < 4)
	{   
	    var pattern = /.$/; 
	    return str.replace(pattern, "*");	// 뒤에 1자리 마스킹처리
	    
	}else
	{   
	    var pattern = /.{3}$/;
	    return str.replace(pattern, "***");	// 뒤에 3자리 마스킹처리
	}        
};

// web3js 에서 IE브라우저 지원하지 않는 Object.assign 대체
if (!Object.assign) {
	Object.defineProperty(Object, 'assign', {
		enumerable: false,
		configurable: true,
		writable: true,
		value: function(target) {
			'use strict';
			if (target === undefined || target === null) {
				throw new TypeError('Cannot convert first argument to object');
			}

			var to = Object(target);
			for (var i = 1; i < arguments.length; i++) {
				var nextSource = arguments[i];
				if (nextSource === undefined || nextSource === null) {
					continue;
				}
				nextSource = Object(nextSource);

				var keysArray = Object.keys(Object(nextSource));
				for (var nextIndex = 0, len = keysArray.length; nextIndex < len; nextIndex++) {
					var nextKey = keysArray[nextIndex];
					var desc = Object.getOwnPropertyDescriptor(nextSource, nextKey);
					if (desc !== undefined && desc.enumerable) {
						to[nextKey] = nextSource[nextKey];
					}
				}
			}
			
			return to;
    	}
	});
}

/**
 *	소수점 자리수 포매터
 *  - number    : 포매팅 대상 숫자
 *  - precision : 소수점 자리수
 *  eg) formatDecimal(1.23456, 4) : 1.2345
 */
var formatDecimal = function (number, precision) {
	var unit = Math.pow(10, precision);
	return (Math.floor(number * unit) /unit).toFixed(precision);
};

/**
 *	정수 여부 확인
 *  - number : 확인 대상
 *  eg) isNumber(1.2) : false
 */
var isNumber = function (number) {
    var regExp = new RegExp(/^[0-9]*$/);
    
    if (!regExp.test(number))
        return false;
    
    return true;
};

/**
 *	나머지 연산
 *  - leftNum  : 좌항
 *  - RightNum : 우항
 *  eg) modOperation(10, 2) : true
 */
var modOperation = function (leftNum, RightNum) {
    if (leftNum % RightNum != 0)
        return false;
    
    return true;
};