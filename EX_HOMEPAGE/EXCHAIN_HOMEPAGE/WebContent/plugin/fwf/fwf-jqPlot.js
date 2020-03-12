/* =============================================================================
 * System       : F-CMS
 * FileName     : fwf-jqPlot.js
 * Version      : 1.1
 * Description  : 개발자 공통사용을 위한 jqPlot 모듈화
 * Author       : 이 병 직
 * Date         : 2013.04.17
 * -----------------------------------------------------------------------------
 * Modify Date  : 
 * -----------------------------------------------------------------------------
 * Etc          : 
 * -----------------------------------------------------------------------------
 * Copyrights 2012 by Finger. All rights reserved. ~ by Finger.
 * =============================================================================
 */

var JQPLOT = JQPLOT || {};

JQPLOT.setter = {}; // 실제 setter 객체 (2013.04.17 고유번호별로 선언이 담겨있다.)

JQPLOT.version = "1.1";
if (window.console) {
	console.info("fwf-jqPlot.js Version: ", JQPLOT.version);
}

JQPLOT.create = function(plotId, constructor) { // 구조화
    var CONS = {
            line: "line"
        ,   bar: "bar"
        ,   curve: "curve"
        },
    
        settings = $.extend({
            type: CONS.line                     // plot 형태
        ,   data: [ [100,200,200] ]             
        ,   xaxisTicks: [ "과거", "현재", "미래"]    // ticks 명칭
        ,   series: [ {label:"샘플1"} ]
        ,   title: null
        ,   titlefontSize: 24
        ,   legend: {
                show: true,
                location: 'ne',     // compass direction, nw, n, ne, e, se, s, sw, w.
                xoffset: 12,        // pixel offset of the legend box from the x (or x2) axis.
                yoffset: 12,        // pixel offset of the legend box from the y (or y2) axis.
                placement: 'inside'
            }
        ,   highlighter: {
                show: true,
                sizeAdjust: 8,
                tooltipAxes: 'y'
            }
        ,   xaxis: {
                renderer:$.jqplot.CategoryAxisRenderer
            }
        ,   yaxis: {
                //renderer:$.jqplot.LineAxisRenderer,
                min: 0,
                //max: 10000000,
                //tickInterval:1000000,
                tickOptions:{
                    formatString: "%'d"
                }
            }
        }, constructor);
    
    //$("#"+plotId).empty();
    settings.plotId = plotId;
    // 항상 ${_PGM_ID_}plot 으로 선언한다고 가정. (2013.04.17)
    // 그렇지 않으면 pageID를 추가로 받으면 된다. (자동화를 위해 생략)
    JQPLOT.setter[plotId.replace("plot","")] = settings; 
    
    switch(settings.type) {
    case CONS.line:
        settings.seriesDefaults = {
            renderer:$.jqplot.LineRenderer,
            pointLabels: {show:false}
            
        };
        settings.series = [];
        break;
    case CONS.bar:
        settings.seriesDefaults = {
            renderer:$.jqplot.BarRenderer,
            rendererOptions: {fillToZero: true},
            pointLabels: {show:true}
        };
        settings.highlighter = { show: false };
        break;
    case CONS.curve:
        // 다음 버전에 지원 예정.
        break;
    }
    
};


// create 설정된 다음에 호출
JQPLOT.draw = function (chartData, pageID) {
    var setter = JQPLOT.setter[pageID]
    ,   dataArray = []
    ,   newLabel = {}
    ,   distinctTicks = {}
    ,   targetData = null
    ,   temp = true
    ,   margin = [""]
    ,   x = 0, y = 0, z = 0, l = 0, len = 0, max = 0;
    
    var lineMarkerStyleFactory = function (count) {
        switch (count%9) {
        case 0:
            return {style:"filledSquare"};
            break;
        case 1:
            return {style:"filledCircle"};
            break;
        case 2:
            return {style:" filledDiamond"};
            break;
        case 3:
            return {style:"x"};
            break;
        case 4:
            return {style:"plus"};
            break;
        case 5:
            return {style:"dash"};
            break;
        case 6:
            return {style:"square"};
            break;
        case 7:
            return {style:"circle"};
            break;
        case 8:
            return {style:"diamond"};
            break;
        }
        return;
    };
    
    if (DEBUG_MODE) {
    	propView(chartData, "받은 값");
    }
    
    if(chartData){
        if (setter.lastdataRemove) {
            for (x in chartData ) {
                chartData[x].pop();
            }
        }
        
        if (setter.xaxisTicksLastName) {
            chartData[setter.xaxisTicksGroup].pop();
            chartData[setter.xaxisTicksGroup].push(setter.xaxisTicksLastName);
        }
        
        setter.xaxis.ticks = chartData[setter.xaxisTicksGroup];
        
        if (setter.xaxisTicksDistinct) {
            // 중복 제거 로직 부분
            for(x = 0, l = setter.xaxis.ticks.length; x < l; x += 1) {
                distinctTicks[setter.xaxis.ticks[x]] = setter.xaxis.ticks[x];
            }
            setter.xaxis.ticks= [];
            for (x in distinctTicks) {
                setter.xaxis.ticks.push(x);
            }
        }
        
        // tick 최소 개수
        for (l = 4 - setter.xaxis.ticks.length; l > 0; l -= 1 ) {
            margin.push("");
        }
        
        setter.xaxis.ticks = setter.xaxis.ticks.concat(margin);
        
        setter.data = [];
        if (typeof setter.dataChange === "object") {
            targetData = chartData[setter.dataChange.valueToLabel];
            // valueToLabel 별로 분류
            for( x = 0, l = targetData.length; x < l; x += 1) { 
                for (var y = 0; y < setter.dataChange.deleteData.length; y += 1) {
                    // deleteData 부분을 여기서 제거한다. (반복횟수를 줄이기 위해)
                    if (setter.dataChange.deleteData[y] === targetData[x]) {
                        targetData[x] = null;
                        break;
                    }
                }
                if (targetData[x] !== null) {
                    newLabel[targetData[x]] = [targetData[x]];
                }
            }
            // 속하는 위치를 구함
            for( x = 0, l = targetData.length; x < l; x += 1) {
                if (targetData[x] !== null && newLabel[targetData[x]][0] ===  targetData[x]) {

                    // 위치에 따른 분류
                    for (y = 0, len = setter.dataGroup.length; y < len; y += 1) {
                        newLabel[targetData[x]].push(parseInt(chartData[setter.dataGroup[y]][x])); // radix 붙이면 이상동작.
                    }
                }
            }
            
            // plot 데이터로 전환
            y = 0;
            for( x in newLabel) {
                setter.series[y] = {}; // 초기화
                if(typeof setter.labelChange === "function") {
                    setter.series[y].label = setter.labelChange(x); // 변환함수
                } else {
                    setter.series[y].label = x; // 구한 name을 그대로 사용
                }
                newLabel[x].shift();
                // sum 추가
                if (setter.dataChange.lastValue === "SUM") {
                    for(z = 0, max = 0, l = newLabel[x].length; z < l; z += 1) {
                        max += newLabel[x][z];
                    }
                    newLabel[x].push(max);
                }
                setter.data.push(newLabel[x]);
                y += 1;
            };
        } else {
            for ( x = 0, l = setter.dataGroup.length; x < l; x += 1 ) {
                while(true){
                    temp = chartData[setter.dataGroup[x]].pop();
                    if(typeof temp !== "undefined") {
                        dataArray.push (parseInt(temp,10));
                    } else {
                        temp = true;
                        setter.data.push(dataArray.reverse()); // 숫자로 변환.
                        dataArray = [];
                        break;
                    }
                } // while
                
                for ( y in chartData) {
                    setter.series[x] = {markerOptions:lineMarkerStyleFactory(x)}; // 초기화
                    if(setter.dataGroup[x] === y) {
                        if(typeof setter.labelChange === "function") {
                            setter.series[x].label = setter.labelChange(y); // 변환함수
                        } else {
                            setter.series[x].label = y; // name을 그대로 사용
                        }
                        break;
                    }
                } // for
                
            } // for
        } // if dataChange
        
        
    } else {
        // local data 이용.....다음 버전에 지원 예정.
        
    }
    var plot = $.jqplot(setter.plotId, setter.data, {
        seriesDefaults: setter.seriesDefaults,
        series: setter.series,
        axes: {
            xaxis: setter.xaxis,
            yaxis: setter.yaxis
        },
        legend: setter.legend,
        
        highlighter: setter.highlighter
        
/*        cursor: {
          //  show: true,
          //  tooltipLocation:'se'
        }*/
    }).redraw();
    
};