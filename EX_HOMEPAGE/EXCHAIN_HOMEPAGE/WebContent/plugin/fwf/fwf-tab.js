/* =============================================================================
 * System       : F-CMS
 * FileName     : fwf-tab.js
 * Version      : 1.1
 * Description  : UI 1.9.1 tabs 확장"
 * Author       : 이 병 직
 * Date         : 2013.03.29
 * -----------------------------------------------------------------------------
 * Modify Date  :  
 * -----------------------------------------------------------------------------
 * Etc          : widget factory (jquery.ui.widget.js)를 사용.
 * -----------------------------------------------------------------------------
 * version 1.0 	: 2012.11.23
 * version 1.1  : 2013.03.29 
 * -----------------------------------------------------------------------------
 * Copyrights 2013 by Finger. All rights reserved. ~ by Finger.
 * =============================================================================
 */

// 바인딩 문제로 인해. 11.25
// 2013.04.09 버튼자체를 이용해서 바인딩하지 않고, class name으로 바인딩 했음. 불필요.
//var FWF_TAB_FULL_SCREEN = "";


/*
 * 주의!! jQuery Ui 1.9.1 tabs 을 기반으로 동작하는 tab
 */

(function ($, undefined) {
// console 테스트 (11.15)
// IE9는 개발자모드일때만 console 지원 (11/18)
var console = window.console || {
        log: function () {} 
    ,   info: function () {}
    ,   warn: function () {}
    ,   error: function () {}
    ,   assert: function () {}
}; 

var fwfTabVersion = 1.1
,   tabIndex = 0
,   minNaviWidth = 60
,   tabOffset = 0       // 가려질 위치. 가려진 것이 아님! (결국 앞에서 가려진 개수)
,   tabOffsetEnd = 0    // 뒤에서 가려진 개수
,   tabsWidth = []
,   recall = []         // 이미 열린 탭을 다시 부르는 경우
,   thatGlobal = null   // window.resize의 경우, 콜백함수 파라미터를 넘겨 줄수가 없기 때문에 사용 (11.11)
,   btnPreGlobal = null
,   btnNextGlobal = null
,   fingerTabIdGlobal = ""
,   tabsLimit = 15;     // 탭 개수 제한

if(DEBUG_MODE) {
	console.info("======== tab console 테스트 시작 ========");
	console.info("설정된 minNaviWidth: ", minNaviWidth);
	console.info("======== tab console 테스트 끝 ========");
} 

if (window.console) {
	console.info("fwf-tab.js Version: ", fwfTabVersion);
}

$.widget("ui.tabs", $.ui.tabs, {
        version: "UI 1.9.1 tabs 확장 fwf-tab" + fwfTabVersion
    ,   base: "#center_content_bak"
    ,   fwfTab: '<div id="center_content_sub" style="height:100%;"><ul></ul></div>'
    ,   options: {
                icons:{close:"fwf-icon-close", pre:"fwf-icon-prev", next:"fwf-icon-next", fullscreen:"fwf-icon-fullscreen", allclose:"fwf-icon-allclose", oneclose:"fwf-icon-oneclose"}
            ,   disableIcons: {pre:"fwf-icon-prev-dis", next:"fwf-icon-next-dis", normalscreen:"fwf-icon-normalscreen" } 
            ,   tabTemplate: "<li><a href='#{href}'>#{label}</a><span class='ui-icon fwf-icon-close '>Remove Tab</span></li>" // (04.30 - 기본 템플릿. 아래 코드에서 2줄 지원으로 변경됨)
            ,   tabMargin: 0            // 각각의 탭 간격 (2013.05.23 탭 간격은 없다.)
            ,   naviWidth: minNaviWidth // 혹시 강제로 탭 navi너비를 지정해야 된다면 설정 가능. (60보다 크면 설정됨. 60이 최소크기)
            ,   naviHide: false        // 네비를 필요시에만 보여줄 것인지 여부.
            ,   add: function( event, ui ) {
                    //console.log("add콜백 ui.index: ", ui.index);
                    //console.log("add콜백 ui.tab: ", ui.tab);
                    
                    // ui에는 tab, panel, index 속성이 있음.
            	
                    // tab은 현재문서 위치 + tab 의 url임.
                    // 예) file://블라블라/zuzu.html --- ajax로 이용할 때
                    // 예) file://블라블라/tabWidget.html#zuzu --- 문서안에서 ID로 이동할 때
            	
            		// tab은  				HTMLAnchorElement (2013.04.29)
                    // panel은 내용 <div> 	HTMLDivElement 
                    // index는 tab index임
                    
                    // 2012.11.11 크롬과 파폭등은 보안정책으로 ajax가 로컬에서 동작하지 않는다. 
                    // ( 무조건 네트워크 상에서만 동작하도록 바뀌었다. )
                    // 다음과 같이 같은 도메인에서만 동작하며, http://localhost:8080/weekend/zuzu.html
                    // C:\Users\aloepig\workspace\home\weekend\WebContent\zuzu.html 같이 직접접근은 허용하지 않는다.
                    // IE에서는 로컬에서 동작하는 것을 허용한다. (IE 7,8,9)
                }
            ,   fullscreen: function (){
                    alert("탭 생성시, fullscreen 옵션을 정의 하세요. (Please define the [fullscreen] option tab when creating.)");
                }
            ,   normalscreen: function (){
                    alert("탭 생성시, normalscreen 옵션을 정의 하세요. (Please define the [normalscreen] option tab when creating.)");
                }
            ,   btnMeassge: function (){
                    alert("마지막 탭 입니다. (Last tab)");
                }
            ,   remove: function () {
                    alert("탭 삭제시, remove 옵션을 정의 하세오. (Please define the [remove] option tab when creating.)");
                }
        }

    ,   _create:function(){
        
    		if(DEBUG_MODE){
    			//console info//
    			console.info(">>> fwf_tabs: _create 함수 시작 <<<");
    		}
            
            this._super();

            var that = this
            ,   options = this.options
            ,   iconClose = options.icons.close
            ,   fingerTab =  this.element               // 대상 엘리먼트. 플러그인 fn에서 $(this)와 같음.
            ,   tabSpan = "span." + iconClose           // tab생성시 사용자가 class를 지정할 경우, 변경된 class명을 사용.
            ,   fingerTabId = "#"+fingerTab.prop("id")
            ,   btnPreName = fingerTabId + " .btn-pre"  // (11.16) 네비 탭 이동버튼이름. 왜인지 $()를 변수로 받으면 사용이 안됨. 
            ,   btnNextName = fingerTabId + " .btn-next"
            ,   btnFullscreenName = fingerTabId + " .btn-fullscreen"
            ,   btnAllcloseName = fingerTabId + " .btn-allclose"
            ,   btnOnecloseName = fingerTabId + " .btn-oneclose"
            ,   naviWidth = options.naviWidth;
            
            // 전역에 저장해 주고(11.11)
            thatGlobal = that; 
            fingerTabIdGlobal = fingerTabId;
            btnPreGlobal = btnPreName;
            btnNextGlobal = btnNextName;
            
            // 네비게이터 
            this.setNavigator(options.icons);           //네비 아이콘 설정

            // 클릭 이벤트 바인드
            $(btnPreName).click(function () {
                if ( $(this).hasClass( options.icons.pre ) ) {
                    that._widthCalculator("pre");
                }
            });
            $(btnNextName).click(function(){
                if ( $(this).hasClass( options.icons.next ) ) {
                    that._widthCalculator("next");
                }
            });  
            $(btnFullscreenName).click(function(){
                if ( $(this).hasClass( options.icons.fullscreen ) ){
                    options.fullscreen();
                    $(this).removeClass( options.icons.fullscreen ).addClass(options.disableIcons.normalscreen);
                } else if ( $(this).hasClass(options.disableIcons.normalscreen) ) {
                    options.normalscreen();
                    $(this).removeClass(options.disableIcons.normalscreen).addClass(options.icons.fullscreen);
                }
                setTimeout( that._widthCalculator, 100 );
            });
            $(btnAllcloseName).click(function(){
                if ( $(this).hasClass( options.icons.allclose ) ){
	    			COMM.dialog({
	    				type: "confirm",
	    				id: "Q0002",
	    				message: "모든 탭을 닫으시겠습니까?",
	    	           	callback: function() {
	    	           		$(tabSpan).click();        		
	    	           	},
	    	           	data: null
	    			});
                }
            });
            $(btnOnecloseName).click(function(){
                if ( $(this).hasClass( options.icons.oneclose ) ){
	    			COMM.dialog({
	    				type: "confirm",
	    				id: "Q0002",
	    				message: "현재 탭을 닫으시겠습니까?",
	    	           	callback: function() {
	    	           		$(".ui-tabs-nav li[aria-selected=true] span").click();  
	    	           	},
	    	           	data: null
	    			});
                }
            });

            // 탭 닫기 아이콘은 생성시에만 설정하면 되므로, 함수로 만들지 않고 여기에 작성.
            // (2013.04.30 label 2줄 표시를 위한 스타일 추가
            options.tabTemplate = "<li><a href='#{href}'><div class=\"ui-tabs-style\">#{label}</div></a><span class='ui-icon "+iconClose+" '>Remove Tab</span></li>";
            
            if (DEBUG_MODE) {
            	//console info//
            	console.info("tabs >> navi width 판단 시작");
            }
            
            if ( naviWidth <= minNaviWidth ) { // 같거나(default), 설정보다 작으면 측정시작 (11.16)
                naviWidth = $( fingerTabId + " .fwf-tabs-navi").width(); 
                
                if (naviWidth < minNaviWidth) { // 측정해도 최소값보다 작으면 최소값으로 복구.
                    naviWidth = minNaviWidth;
                    if (DEBUG_MODE) {
                    	console.log("minNaviWidth 로 복구: ", naviWidth);
                    }
                };
            } //최소값보다 크면 유저 설정을 따름.
            options.naviWidth= naviWidth;
            if (DEBUG_MODE) {
            	console.log("naviWidth 저장: ", naviWidth);
            	//console info//
            	console.info("tabs >> navi width 판단 종료");
            }
            
            // 탭 제거 정의
            $(document).on( "click", tabSpan, function() { //탭 제거 함수
                
                if (DEBUG_MODE) {
                	//console info//
                	console.info("tabs >> 탭 제거 시작");
                }
                
                var index = $(".ui-tabs-nav li[aria-controls]").index( $(this).parent() )
                // index = $(this).parent().attr("tabIndex") // 인덱스는 arrayList처럼 동작. 제거되면 전체 순서가 당겨짐
                // 탭이 0으로 돌아간 뒤엔 tabIndex가 -1로 고정(11.18).
                ,   temp1 = tabsWidth.slice(0,index)
                ,   temp2 = tabsWidth.slice(index+1)
                ,   temp3 = recall.slice(0,index)
                ,   temp4 = recall.slice(index+1);
                
                fingerTab.tabs("remove",index);
                
                tabIndex--;
                if (DEBUG_MODE){
                	console.log("제거된index: ", index);
                	console.log("tabIndex 감소후: ", tabIndex);
                }
            
                // widthCal()에서  길이배열의 뒷부분만 조작하는데 뒷부분 일부가 남아있으므로 제거해야 함.
                // 처음엔 요소 길이를 새로계산해서, 남은 배열까지 사용하지 않으므로 정상처럼 보이지만 문제 발생함.
                // 따라서 여기세 제거되는 부분을 빼 내야 한다.
                // 아니면 widthCal에서 offset 이 아니라 배열초기화후 전체를 계산하도록 변경할 것.
                
                // url이 저장되어 있는 배열을 제거 (11.11)
                tabsWidth = temp1.concat(temp2);
                recall = temp3.concat(temp4);
                
                if (DEBUG_MODE){
                	console.log("tabsWidth[]: ", tabsWidth);
                	console.log("recall[]: ", recall);
                }
                console.assert( (tabsWidth.length === recall.length), "배열 연산의 치명적인 문제");
            
                if (tabOffsetEnd > 0 && tabOffset === 0){  //만약 탭이 제거되었는데 뒤에만 숨겨진 개수가 있다면
                    that._widthCalculator("delete");
                } else {
                    that._widthCalculator();
                }
                
                //탭이 전부 사라졌다면, 자신을 파기 (11.19)
                if ( tabsWidth.length === 0 && recall.length === 0 ) {
                    // 모두 지우고 다시 생성하면 문제가 발생. UI생성시 이벤트가 중복되는 현상발생(11.25)
                	location.href=$("#footer_fwf_page_id").val();
                }
                
                if (DEBUG_MODE) {
                	//console info//
                	console.info("tabs >> 탭 제거 끝");
                }
                
            });

            if (DEBUG_MODE) {
            	// 설정에 따라 항상 보이던가 아니던가.
            	console.info("(히든 여부) options.naviHide: ", options.naviHide);
            }
            
            if (options.naviHide) {
                $(btnPreName).hide();
                $(btnNextName).hide();
            } else {
                if (DEBUG_MODE) {
                	console.log("btnPre: ", $(btnPreName).attr("class"));
                }
                $(btnPreName).removeClass(options.icons.pre).addClass(options.disableIcons.pre);
                $(btnNextName).removeClass(options.icons.next).addClass(options.disableIcons.next);
                //console.log("btnPre: ", $(btnPreName).attr("class"));
                //console.log("btnNext: ", $(btnNextName).attr("class"));
            }

            //윈도우 창이 변할 때 마다 작동 (11.25)
            var wiatResize = function() {
                setTimeout (that._widthCalculator, 200 );
            };
            $.fwf.resizeStop( wiatResize );
            //$(window).resize( wiatResize ); // 이경우, resize가 호출하는 콜백함수는 무엇이든지간, 즉 widthCalculator에서 this는 object가 아닌 window임. (11.11) 그래서 동작하지 않았다.

            if (DEBUG_MODE) {
            	//console info//
            	console.info(">>> fwf_tabs: _create 함수 종료 <<<");
            }
        }
    
        //_init:function(){ }, // create다음에 실행. 다른 함수보다 빠름. 매번 실행
    
        // 탭 헤더에 네비게이터 달기.  - 혹시 탭생성 후 $navi를 바꿔야 할 일이 있다면 정의해서 사용할 수 있다.
    ,   setNavigator: function(icons){
            var icons = this.options.icons;
            $navi = $("<p class='fwf-tabs-navi'><span class='btn-pre ui-icon " + 
                    icons.pre + "'></span><span class='btn-next ui-icon " + 
                    icons.next + "'></span><span class='btn-fullscreen ui-icon " + 
                    icons.fullscreen + "'></span><span class='btn-allclose ui-icon " + 
                    icons.allclose + "'></span><span class='btn-oneclose ui-icon " +
                    icons.oneclose + "'></span></p>");

            $(".ui-tabs-nav").append($navi);
            // 2013.04.09 버튼자체를 이용해서 바인딩하지 않고, class name으로 바인딩 했음. 불필요.
            //FWF_TAB_FULL_SCREEN = "<span class='btn-fullscreen ui-icon " + icons.fullscreen + "'></span>";
            
            if (DEBUG_MODE) {
            	//console info//
            	console.info(">>> setNavigator 함수 시작-종료 <<< 네비생성");
            }
        }
    
        // add 함수 확장
    ,   add: function(url, label, index){
        
			if (DEBUG_MODE) {
				//console info//
				console.info(">>> add 함수 시작 <<<");
			}
            
            var recallLength = recall.length
            ,   check = -1;
        
            // 한 개 이상 탭이 있으면 중복 체크
            if (recallLength !== 0) {
                for (var v = 0; v < recallLength; v += 1) {
                    if (recall[v] === url){
                        check = v;
                        
                        //debug//
                        //console.log("중복url 발견: ", url);
                        //console.log("중복url 번호: ", v);
                        
                        break;
                    };
                } //for END
            }
            
            // 중복이 없으면 탭 생성
            if (check === -1) {
                // 탭 개수 제한 삭제
                if (tabIndex >= tabsLimit) {
                    COMM.dialog({
                        id: "session",
                        type:"session",
                        message:"<li>" + _UCS.COMMON2008.replace("%1", tabsLimit)  + "</li>"
                    });
                    setTimeout( function(){ $(COMM._INIT.SESSION_EXPIRATION.base).dialog("close");}, 2000);
                    return;
                } 
                
                recall.push( url );
                
                if (DEBUG_MODE) {
                	//console info//
                	console.info("tabs >> 탭생성 시작");
                	console.log("입력값 >> url:", url, "label:", label, "index:", index); // (11.16) index지정시 수동으로 탭 삽입 위치 결정가능
                }
                this._super(url, label, index); // 탭이 하나 생기고.
                
                if (DEBUG_MODE) {
                	//console info//
                	console.info("tabs >> 탭생성 끝");
                }
                
                this.element.tabs({"active":tabIndex}); //새로 생긴 탭 인덱스
                tabIndex++; //다음에 생길 탭 인덱스
                
                console.assert( (tabIndex > 0), "tabIndex에 심각한 오류");
                
                this._widthCalculator("add"); // 추가로 인한 계산
            } else {
                this.element.tabs({"active":check}); // 기존 탭을 활성화
                
                // 활성화 하면서 visible (2.8) (다음 버전에선 길이정보 배열을 바탕으로 바로 이동하게 할 것!)
                if (  ! $($(fingerTabIdGlobal +" ul:first > li")[check]).is(":visible") ) {
                    if (tabOffset > check) {
                        while (! $($(fingerTabIdGlobal +" ul:first > li")[check]).is(":visible")) {
                            $(btnPreGlobal).click();
                            //console.log("활성화를 위한 pre 클릭 동작");
                        }           
                        
                    } else {
                        while (! $($(fingerTabIdGlobal +" ul:first > li")[check]).is(":visible")) {
                            $(btnNextGlobal).click();
                            //console.log("활성화를 위한 next 클릭 동작");
                        }
                    }
                }
                //debug//
                //console.log("기존탭을 활성: ", check);
            }
            
            if (DEBUG_MODE) {
            	//console info//
            	console.info(">>> add 함수 종료 <<<");
            }
        }

        // tab 길이 계산
    ,   _widthCalculator:function(operation){
    	
	    	if (DEBUG_MODE) {
	    		//console info//
	    		console.info(">>> _widthCalculator 함수 시작 <<<");
	    	}

            // 예외처리 - 탭이 없으면 계산할 필요가 없음
            if (thatGlobal.anchors.length === 0){
            	if (DEBUG_MODE) {
            		//console info//
            		console.info(">>> _widthCalculator 함수 종료 (탭이 없음) <<<");
            	}
                return;
            }
            
            var that = thatGlobal                       // 전역을 지역에 저장하고...(window.때문에 이방법을 사용 11.11)
            ,   options = that.options
            ,   tabMargin = options.tabMargin           // 탭의 마진+패딩 크기
            ,   maxWidth =  that.element.width() - options.naviWidth 
            ,   totalWidth = 0                          //항상 초기화.
            ,   gap = 0               					// maxWidth - totalWidth (12.10)
            ,   fingerTabId = fingerTabIdGlobal
            ,   tabs = $(fingerTabId +" ul:first > li") // 여기가 틀어지면 안된다. 구조를 지켜야 함.
            ,   btnPre = $(btnPreGlobal)
            ,   btnNext = $(btnNextGlobal)
            ,   tabsLength = tabs.length;
            
            var totalWidthCal = function () {
                // 보여지는 부분 width 계산 (12.10 분리)
                //debug//
                //console.info("tabs >> 총 너비 계산");
                totalWidth = 0; // 항상 초기화.
                
                for (var x = tabOffset; x < (tabsLength-tabOffsetEnd); x += 1){  //tabs.length는 index와 같다.
                    tabsWidth[x] = tabsWidth[x] ? tabsWidth[x] : $(tabs[x]).outerWidth() +tabMargin;
                    totalWidth += tabsWidth[x];

                    //debug//
                    if (DEBUG_MODE){
                    	console.log("x === tabOffset ",tabOffset);
                    	console.log("x는", x, "<", (tabsLength - tabOffsetEnd), "tab:", tabsWidth[x],"total:", totalWidth, "max:", maxWidth);
                    }
                }
                
                return maxWidth - totalWidth;
            };

            //debug//
            //console.log("tabsLength: ", tabsLength);
            //console.log("that.anchors.length: ", that.anchors.length); //앵커 선언이 반드시 있어야 정상 동작. ( ui-tab설계가 그러함 ) 11.16
            console.assert( (tabsLength > 0 ), "!!! tabsLength 심각한 오류 !!!");
            //console.log("테스트값: tabs[0]의 자동부여 속성", $(tabs[0]).attr( "aria-controls")); //테스트(11.11), 1부터 무조건 1씩 증가

            switch (operation) {
            case "add":
                //debug//
                //console.info("tabs >> add 일 때 계산");
                
                 // 먼저 가장 뒤에 add되므로, 뒤에 숨겨진 모든 탭을 보여주고, tabOffsetEnd =0 으로 되돌린다.
                //if (tabOffsetEnd > 0) {
                    //debug//
                    //console.info("뒤에 숨은 탭이 있을 때 새로 탭 생성");
                //}
                while (tabOffsetEnd > 0){
                    
                    //debug//
                    //console.log("숨은 탭 수 tabOffsetEnd: ",tabOffsetEnd);
                    
                    $(tabs[tabIndex - tabOffsetEnd]).show(); //뒤에 하나 보여주고
                    tabOffsetEnd--;
                }
                break;
            case "pre":
                
                if (tabOffset > 0) {
                    
                    //debug//
                	if (DEBUG_MODE){
                		console.info("tabs >> pre 일 때 계산: tabOffset > 0 일 때");
                		console.log("실행전 >> 앞:", tabOffset, "뒤:", tabOffsetEnd);
                	}
                    
                    tabOffset--;                                    //앞은 줄고. 숨어있는 셀 개수로 보면 된다.
                    $(tabs[tabOffset]).show();                      //앞에 하나 보여주고
                    $(tabs[tabIndex - tabOffsetEnd - 1]).hide();    //뒤에 하나 가리고, (11.11: 현재 탭인덱스를 구하기 위해 -1)
                    tabOffsetEnd++;                                 //뒤는 가려졌으니 늘고
                    
                    // 만약 앞에 것을 보여주었는데 총 범위를 초과한다면, 뒤를 하나더 숨긴다. (12.10)
                    gap = totalWidthCal();
                    while (gap < 0 ){
                    	 $(tabs[tabIndex - tabOffsetEnd - 1]).hide();
                    	 gap += tabsWidth[tabIndex - tabOffsetEnd -1];
                    	 tabOffsetEnd++;
                    }
                    
                    // 클릭시 나타난 탭을 활성화 (2.8)
                    this.element.tabs({"active":tabOffset});
                    
                    //debug//
                    if (DEBUG_MODE){
                    	console.log("실행후 >> 앞:", tabOffset, "뒤:", tabOffsetEnd);
                    }
                } else {
                    
                	if (DEBUG_MODE) {
                		//console//
                		console.warn("offset 0 입니다");
                	}
                    
                    options.btnMeassge(); // 콜백 호출(11.16)
                }
                break;
            case "next":
                
                if (tabOffsetEnd > 0) {
                    
                    //debug//
                	if (DEBUG_MODE) {
                		console.info("next 일 때 계산: tabOffsetEnd > 0 일 때 ");
                		console.log("실행전 >> 앞:", tabOffset, "뒤:", tabOffsetEnd);
                	}
                    
                    $(tabs[tabOffset]).hide();                  //앞에 하나 가리고
                    tabOffset++;                                //가려졌으니 하나 추가
                    tabOffsetEnd--;                             // 하나 줄이고 보여줄 것이므로 먼저 줄이고,
                    $(tabs[tabIndex - tabOffsetEnd - 1]).show();//뒤에 하나 보여주고, (11.11: 현재 탭인덱스를 구하기 위해 -1함. 옮기면서 로직 변경?)
                    
                    // 뒤에 보여 준 뒤에도 다음 탭이 나올 공간이 남아 있다면 동작 
                    // (2013.05.23 탭 활성화 처리를 한번에 하기 위해 switch 문 안에서 처리 - 기존에는 밖에서 공통 처리. )
                    gap = totalWidthCal();
                    while (tabOffsetEnd > 0 && gap >= tabsWidth[tabIndex - tabOffsetEnd]) {
                        $(tabs[tabIndex - tabOffsetEnd]).show();
                        gap -= tabsWidth[tabIndex - tabOffsetEnd];
                        tabOffsetEnd--;
                    }
                    
                    // 클릭시 나타난 탭을 활성화 (2.8)
                    this.element.tabs({"active":(tabIndex - tabOffsetEnd - 1)});
                    
                    //debug//
                    if (DEBUG_MODE) {
                    	console.log("실행후 >> 앞:", tabOffset, "뒤:", tabOffsetEnd);
                    }
                    
                }else {
                	if (DEBUG_MODE) {
                		console.warn("offsetEND 0 입니다");
                	}
                    options.btnMeassge();    // 콜백 호출(11.16)
                }
                break;
            case "delete":
                
                //debug//
                //console.info("delete 일 때 계산");
                
                tabOffsetEnd--;                             //하나 보여줘야 함
                $(tabs[tabIndex - tabOffsetEnd-1]).show();  //뒤에 하나 보여주고
                break;
            case "fullscreen":
                
                //debug//
                //console.info("fullscreen 일 때 계산");
                
                break;
            default:
                //debug//
                //console.info("default 계산");
                
                break;
            }

            totalWidthCal();
        
            // 허용범위를 초과했을 경우 작동
            // offset을 기준으로 동작하므로, debug시 offset이 정확한지 확인할 것.
            while( totalWidth > maxWidth) {
                
                //debug//
                //console.info("허용범위 초과");
                //console.log("tabOffset", tabOffset);
                
                $(tabs[tabOffset]).hide();
                totalWidth = totalWidth - tabsWidth[tabOffset];
                tabOffset++;

                //debug//
                //console.log("tabOffset", tabOffset);
                
            // 초과하면 앞부턴 사라지고, 그 다음에 공간이 남으면 뒤를 보여주는 방식이므로 tabOffsetEnd는 계산할 필요 없음.
            }

            // 먼저 뒤에 숨겨진 탭을 보여주고 (11.2)
            // 만약 여유공간이 뒤에 숨겨진 탭을 보여줄 수 있다면 작동(12.10)
        	//여기서 tabIndex는 다음에 생길 탭번호이므로 offsetEND만 빼주면 된다. (11.11) 
        	while (tabOffsetEnd > 0 && totalWidth + tabsWidth[tabIndex - tabOffsetEnd] <= maxWidth) { 
        		
        		//debuge//
        		//console.info("tabOffsetEnd > 0 이고, maxWidth 여유가 있을 때");
        		//console.log("tabOffset", tabOffset);
        		
        		$(tabs[tabIndex - tabOffsetEnd]).show();
        		totalWidth = totalWidth + tabsWidth[tabIndex - tabOffsetEnd];
        		tabOffsetEnd--;
        		
        	}
            // 뒤에 숨은탭을 보여줬음에도 허용범위보다 작고, 앞에 숨은 탭이 있다면. (delete시 주로 발생) (12.10)
            // 또는 전체창으로 변할 때 적용된다.
            if ( tabOffsetEnd === 0 ) {
                while (tabOffset > 0 && totalWidth + tabsWidth[tabOffset - 1] <= maxWidth) {
                    $(tabs[tabOffset-1]).show();
                    totalWidth = totalWidth + tabsWidth[tabOffset];
                    tabOffset--;
                }
            }
            
            // 버튼 상태 체크 (while문에서 분리 2013.05.23)
            // offset 정상값 0만이 아니라 오류값인 - 도 처리. console로 오류확인(2013.05.23)
            if (that.options.naviHide) {
                if (tabOffsetEnd > 0) {
                    btnNext.show(); //뒤에 있을 때만 보여줌
                } else {
                    btnNext.hide();	
                }
                if (tabOffset > 0) {
                	btnPre.show();      //앞 화살표만 보여줌
                } else {
                	btnPre.hide(); //화살표를 감추고
                }
            } else {                //add와 같이 tabOffset이 0이 되는 경우를 고려(12.10)
                if (tabOffsetEnd > 0) {
                    btnNext.addClass(options.icons.next).removeClass(options.disableIcons.next);
                } else {
                    btnNext.addClass(options.disableIcons.next).removeClass(options.icons.next);
                }
                if (tabOffset > 0){
                	btnPre.addClass(options.icons.pre).removeClass(options.disableIcons.pre);
                } else {
                	btnPre.removeClass(options.icons.pre).addClass(options.disableIcons.pre);
                }
            }

        	// 비정상 값 생성시 로그
        	console.assert( (tabOffsetEnd >= 0), "tabOffsetEnd 오류. -값이 생성." );
        	console.assert( (tabOffset >= 0), "tabOffset 오류. -값이 생성." );
        	
            if (DEBUG_MODE) {
            	//console info//
            	console.info(">>> _widthCalculator 함수 종료 <<<");
            }
        } // _widthCalculator 끝
});
         
})(jQuery);
 /////////////////// widget 끝 ////////////////////////