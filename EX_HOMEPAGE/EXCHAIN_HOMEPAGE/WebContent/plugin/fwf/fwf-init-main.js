/* =============================================================================
 * System       : F-CMS
 * FileName     : fwf-init-w.js
 * Version      : 1.1
 * Description  : work 부분 설정 script
 * Author       : 이 병 직
 * Date         : 2013.03.29
 * -----------------------------------------------------------------------------
 * Modify Date  : 2013.01.21
 * -----------------------------------------------------------------------------
 * version 1.0 	: 2012.11.25
 * version 1.1  : 2013.03.29 
 * -----------------------------------------------------------------------------
 * Etc          : 외부에서 접근 불가 script 
 * -----------------------------------------------------------------------------
 * Copyrights 2013 by Finger. All rights reserved. ~ by Finger.
 * =============================================================================
 */

$(function () {
    
    //////// initialize (11.25) ////////
    var URL_INIT = {
            version: "1.1"
        ,   pageSelelctor: INITIALIZE_BASE + "/page/process/pageSelect.jsp"
        ,   brief: "/brief.jsp"
        ,   logout: INITIALIZE_BASE + "/uxLogout"
        ,   alarm: "/page/work/um20/UM2010010.jsp"
        ,   alarmTitle: _UCS.COMMON2016
        ,   MSG_S2013: _UCS.MSGCDS2013
        ,   page403: "/page/process/page403.jsp"
        ,   mainPage: INITIALIZE_BASE + "/main.jsp"
        ,   btnAuthority: 0
    };
    
    var approvalInterval = null
    ,   alramInterval = null;
    
    if (window.console) {
    	console.info("fwf-init-w.js Version: ", URL_INIT.version);
    }
    // 로그인시 session 및 application 에 생성된 값 읽기
    propView(_INIT_SESSION, "get session attribute");

    // 관련사이트 생성
    (function (){
        $("#sitelink").FWFselectBox({
            arrayData: _SITE_LINK.options
        ,   defaultSelectedValue: _SITE_LINK.defaultSelectedValue
        }).change( function () {
            var linkIndex = parseInt($.trim($(this).val()).slice(4,6),10);
            window.open(_SITE_LINK.options[linkIndex].url);
            this.selectedIndex = 0;
        });
    }());

    // 로그인 시간 표시
    $("#loginDateTime").text( COMM.formatter("answerTime", _INIT_SESSION._LOGIN_TIME) );
    
    // backspace 방지 Mo-Zilla 적용
    $(document).keydown(COMM.keyEventPrevent.keydownBackspace);
    
    // 레이아웃 스크롤바
    // 스크롤바 영역을 고정폭으로 변경으로 인한 삭제 - 그리드영역에 영향을 미치기 때문 (2013.05.15)
/*    var setLayout = function () {
        var west_height =  $(".ui-layout-west").outerHeight()
        ,   west_content_height = $("#left_menu").offset().top - $(".ui-layout-west").offset().top + $("#left_menu_content").outerHeight() + 42 ;
        
        if (west_content_height > west_height) {
            layout.sizePane("west", 240);
            layout.resizeAll();
        } else {
            layout.sizePane("west", 230);
            layout.resizeAll();
        }
    };*/
    
    // 스택 지연
/*    var wiatResizeSlow = function() {
        setTimeout( setLayout, 200 ); 
    };
    var wiatResizeFast = function() {
        setTimeout( setLayout, 50 ); 
    };*/
    
    // 레이아웃 
    var layout = $("body").layout({ defaults: { fxName:"none" }, west__size: "240", north__size:"71", south__spacing_open: 1});
    //$.fwf.resizeStop( wiatResizeSlow );
    
    // 탭으로
    var tabGenerator = function (target, title) {
        $("#center_content_sub").tabs({
            cache: true,
            fullscreen: function () {
                layout.close("north");
                layout.close("west");
                
                $(".ui-tabs-nav").css("left", 17);
            },
            normalscreen: function () {
                layout.open("north");
                layout.open("west");
                
                $(".ui-tabs-nav").css("left", 256);
            },
            remove: function () {
            },
            activate: function(event, ui ) {
                // 활성화된 탭만 동작
                var targetID = $(ui.newPanel).find(".grid_area > div:first").attr("id");
                
                if (typeof targetID !== "undefined") {
                    targetID = "#" + targetID.replace("gbox_","");
                    JQGRID._resize({ data:{"id": targetID, "parent":"#center_content_sub", "resizeType":"resize_width_fast"}});
                    JQGRID._resize({ data:{"id": targetID, "parent":"#center_content_sub", "resizeType":"resize_height_fast"}});
                    //2013.06.03 IE OL 버그 대응. 주의사항이 열렸을 때, 커서가 OL포함된 DOM을 벗어날 때까지 0으로 보이는 버그.
                    $(".readme.readme_body").hide().show();
                };    
            }
        });
        if (DEBUG_MODE && window.console) {
            console.info("=== tab 생성 준비 ===");
            console.log("target: ", target);
            console.log("title: ", title);
        }
        
        COMM._sessionExpirationCheck(URL_INIT.mainPage); // fwf_user_id 중복 체크
        $("#center_content_sub").tabs("add", INITIALIZE_BASE + target, title);
    };
    
    // 이벤트 bind
    $("#center_content_sub").on( "tabsload", function( event, ui ) {
        //COMM._sessionExpirationCheck(URL_INIT.mainPage); // fwf_user_id 중복 체크
        if (DEBUG_MODE && window.console) {
            console.info("=== 로딩 완료 ===");
        }
    });
    
    // a 를 tab 으로
    var contentTotab = function(node){
        node.bind("click", function(ev) {
            var target = $(this).attr("href")
            ,   title = $(this).attr("title") || $(this).text();
            
            if ( target === "#" ) {
                target = URL_INIT.warnPage;
            }
            ev.preventDefault();
            tabGenerator(target, title);
        });
    };
    
    // dialog 정의
    $("#btn_more").click( function (){
        $(this).toggleClass("fwf-icon-down2").toggleClass("fwf-icon-up2");
        $("#more_error").toggle();
        $("#error_wrapper").dialog( "option", "position", { at:"center" });
    });
    $(".fwf_dialog_message").focus( function() {
        $(this).blur();
    });
    $(".fwf_dialog_more_err", $("#error_wrapper")).focus( function() {
        $(this).blur();
    });
    
    // 좌측메뉴 아코디언
    $("#my_menu").accordion({
            collapsible: true
        ,   autoHeight: false
        ,   navigation: true
        ,   icons: { "header": "fwf-icon-up", "headerSelected": "fwf-icon-down" }
    });

    // 2013-05-03 
    // ui-1.10.2 를 적용한다면, 아코디언에서 jquery로 변경해야 한다. 
    // 바로 보여주는 효과이므로 아코디언이 필요 없이 다음 코드처럼 작성하면 된다.
    // 단, 헤더 아이콘 toggle 효과를 추가 작업해야한다. -244 line 부분도 주석처리 해야 함.
    /*$("#left_menu_title").click(function(){
    	 $("#left_menu_content_div").toggle();
    });*/
    var $leftMenu = $("#left_menu").accordion({
            collapsible: true
        ,   autoHeight: false
        ,   navigation: true
        ,   animate: false
        ,   icons: { "header": "fwf-icon-up", "headerSelected": "fwf-icon-down" }
    });
    
    // 초기 아이콘
    $(".button_notice").button({
        icons: { primary: "fwf-icon-notice-dis" }
    }).click(function(ev){ev.preventDefault();});
    $(".button_pay").button({
        icons: { primary: "fwf-icon-pay-dis" }
    }).click(function(ev){ev.preventDefault();});
    $("#alarmCount").hide().next().hide();
    
    
    // 결제함 버튼, 알림함 버튼
    (function() {
        if ( $("#btn_pay").attr("href") !== "" ) {
            contentTotab(
                    $(".button_pay",  $("#left_content")).button({
                        icons: { primary: "fwf-icon-pay" }
                    }).parent()
            );
        }
        
        if ( $("#btn_notice").attr("href") !== "" ) {
            contentTotab(
                    $(".button_notice", $("#left_content")).addClass("button_notice_on").button({
                        icons: { primary: "fwf-icon-notice" }
                    }).parent()
            );
            $("#alarmCount").show().next().show();
        }
        
    }());
    
    
    // my 메뉴 선택
    contentTotab( $("#my_menu ul li a") );
    
    // 1depth선택
    $("#left_menu").hide();
    var topMenu =$("#menu ul").children("li");
    topMenu.click( function(ev){
        ev.preventDefault();
        
        COMM._sessionExpirationCheck(URL_INIT.mainPage); // fwf_user_id 중복 체크
        layout.open("west");
        $(".ui-tabs-nav").css("left", 256);
        
    	if ($(this).hasClass("menu_disabled") ){
    		return;
    	};
    	
        var element = $(this).children("a")
        ,   target = URL_INIT.pageSelelctor
        ,   param = $(this).attr("id");
        if(param === "m9") {
            COMM.winOpen( URL_INIT.brief );
        } else {
            var title = element.text();
            if (DEBUG_MODE && window.console) {
                console.log("1depth title: ", title);
            }
            $("#left_menu").show();
            $("#left_menu_sub_title").text(title);
            $.post( target
                ,   "page="+param
                ,   function(html){ 
                        $("#left_menu_content_div").html(html);
                        contentTotab( $("#left_menu_content ul li a") );
                    }
            ).complete(function() { 
                $leftMenu.accordion("option", "active", 0); //항상 열리게
               // wiatResizeFast();
            });
        }
    });
    
    // 3depth 접힘
    $(document).on( "click", "#left_menu_content h5", function() {
        var h5 = $(this);
        h5.next("ul").toggle();
        h5.children("span:first").toggleClass("fwf-icon-minus").toggleClass("fwf-icon-plus");
    });
     
    // 로그아웃
    $("#logout").click( function () {
        $(location).attr("href", URL_INIT.logout); 
    });
     
    // 개인정보 수정
    if ($("#privateInfo").attr("href") === "") {
        $("#privateInfo").attr("href",URL_INIT.page403);
    } 
    // 주의! jQuery 1.9.1 버전
    //console.error($("#privateInfo").attr("href")) 지정된 경로. /page/process/page403.jsp 
    //console.error($("#privateInfo").prop("href")) 절대경로. http://localhost:8080/page/process/page403.jsp 
    contentTotab( $("#privateInfo") );

    // 비밀번호 변경기간 초과시 개인정보 수정 페이지로
    if ( _INIT_SESSION._Chk_Pwd_Upd_Perd === "Y" ) {
        COMM.dialog({
            type: "message"
        ,   id: "S2013"
        ,   message: URL_INIT.MSG_S2013
        ,   callback: function() {
                var target = $("#privateInfo").attr("href")
                ,   title = $("#privateInfo").attr("title");
                tabGenerator(target, title);
            }
        });
    }
    
    // 보고서 권한체크
    if ( _INIT_SESSION._Rptsite_Use_Yn === "Y" ) {
        
    } else {
        $("#m9").off();
        $("#m9 img").attr("src", INITIALIZE_BASE_IMG + "/btn_go_report_dis.jpg");
        $("#m9 img").attr("title", "보고서 사이트 권한 없음");
    }
    
    
    // 알림 숫자 갱신 (체크시마다 progress bar 생략하기 위해서 별도작성)
    var alramCountThroughAjax = function (isStarted) {
        var postData = {"_TASK_ID_":"AM2010030","_DATATYPE_":"COM"}
        ,   alarmCount = 0;
        
        COMM._sessionExpirationCheck(URL_INIT.mainPage); // fwf_user_id 중복 체크 (세션 일치 체크)
        $.getJSON( "uxAction" ,postData, function (data) {
            if(!COMM._rCodeCheck(data, "alramCountThroughAjax")){
                if (data.resultData.noti_ncnt) {
                    alarmCount =  parseInt(data.resultData.noti_ncnt, 10);
                    COMM.alarmSetter( alarmCount );
                    if (alarmCount > 0 && _INIT_SESSION._Alm_Popup_Yn ==="Y") {
                        if(isStarted){
                            
                        } else {
                            COMM.alarm (alarmCount);
                        }
                    }
                }
            }// rCode 체크
        }).error( function(){
            clearInterval(alramInterval);
        });
    };
    
    // 결재함 숫자 갱신
    var approvalCountThroughAjax = function () {
    	 COMM.approvalRenovate( function(){clearInterval(approvalInterval);} );
    };

    // 결재함 권한이 있으면
    if ( $("#btn_pay").attr("href") !== "") {
    	URL_INIT.btnAuthority += 1;
    }
    // 알림함 권한이 있으면
    if ( $("#btn_notice").attr("href") !== "") {
    	URL_INIT.btnAuthority += 2;
    }
    switch(URL_INIT.btnAuthority){
    // 없음
    case 0:
    	break;
    // 결재
    case 1:
    	COMM._sessionExpirationCheck(URL_INIT.mainPage); // fwf_user_id 중복 체크
    	approvalCountThroughAjax();
    	approvalInterval = setInterval ( approvalCountThroughAjax, _INIT_SESSION._Pop_Alm_Perd * 1000);
    	break;
    // 알림
    case 2:
        COMM._sessionExpirationCheck(URL_INIT.mainPage); // fwf_user_id 중복 체크
        if ($("#footer_fwf_page_state").val() !== "started") {
            alramCountThroughAjax();
        } else if ($("#footer_fwf_page_state").val() === "started") {
            alramCountThroughAjax(true);
        }
        alramInterval = setInterval ( alramCountThroughAjax, _INIT_SESSION._Pop_Alm_Perd * 1000);
    	break;
    // 결재+알림
    case 3:
        COMM._sessionExpirationCheck(URL_INIT.mainPage); // fwf_user_id 중복 체크
        if ($("#footer_fwf_page_state").val() !== "started") {
            alramCountThroughAjax();
        } else if ($("#footer_fwf_page_state").val() === "started") {
            alramCountThroughAjax(true);
        }
        approvalCountThroughAjax();
        alramInterval = setInterval ( alramCountThroughAjax, _INIT_SESSION._Pop_Alm_Perd * 1000);
        approvalInterval = setInterval (approvalCountThroughAjax, _INIT_SESSION._Pop_Alm_Perd * 1000);
    	break;
    }

    
    // session View (m_ex) - sysadmin
    //if ( $("#footer_fwf_user_id").val() === "U000000001") {
    //    $("#top_login").find(".fwf-icon-human:first").click(function(){ $("#m_ex").click(); });
    //}

    // 알림창 승인함수
    COMM._INIT.ALARM.confirmFunc = function() {
        var target = URL_INIT.alarm
        ,   title = URL_INIT.alarmTitle;
        tabGenerator(target, title);
        $( this ).dialog( "close" );
        
    };

	//////////////////////////////////////////////////
	//// 뒤로가기, F5 구분 및 뒤로가기 방지 2013.04.29
	//////////////////////////////////////////////////
    (function () {
    	var cookieValue = $.fwf.getCookie("page_loading_time").value;
    	if ( cookieValue === $.fwf.getCookie("page_loading_time_copy").value){
    		$.fwf.deleteCookie("page_loading_time_copy");
    		location.href = URL_INIT.logout;
    	} else {
    		document.cookie = "page_loading_time_copy=" + cookieValue;
    	}
    })();
});
