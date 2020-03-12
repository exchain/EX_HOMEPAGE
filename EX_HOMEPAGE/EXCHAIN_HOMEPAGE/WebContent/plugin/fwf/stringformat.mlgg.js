// This culture information has been generated using the Mono class library
// licensed under the terms of the MIT X11 license.
// See: http://www.mono-project.com/FAQ:_Licensing
if(DEBUG_MODE && window.console){
	console.info("_INIT_SESSION._Date_Format - stringformat setting (debug mode)");
	console.log("YMD: ",_INIT_SESSION._Date_Format.YMD);
	console.log(" MD: ",_INIT_SESSION._Date_Format.MD);
	console.log(" YM: ",_INIT_SESSION._Date_Format.YM);
}

msf.registerCulture({
    name: "ko",
    d: _INIT_SESSION._Date_Format.YMD, 	// 년월일
    D: _INIT_SESSION._Date_Format.YMD + " " +_INIT_SESSION._Date_Format.HMS, // 년월일 시분초
    k: _INIT_SESSION._Date_Format.YMD + " " +_INIT_SESSION._Date_Format.HM,	 // 년월일 시분
    t: _INIT_SESSION._Date_Format.HM, 	// 시분
    T: _INIT_SESSION._Date_Format.HMS, 	// 시분초
    M: _INIT_SESSION._Date_Format.MD, 	// 월일
    Y: _INIT_SESSION._Date_Format.YM, 	// 년월
    _am: _UCS.COMMON3131,
    _pm: _UCS.COMMON3132,
    _r: _INIT_SESSION._Point, // Radix point
    _cr: _INIT_SESSION._Point, // Currency radix point
    _t: _INIT_SESSION._Thousands, // Thousands separator
    _ct: _INIT_SESSION._Thousands, // Currency thousands separator
    _c: "\u0027₩\u0027#,0.00", // Currency format string
    _d: [_UCS.COMMON2056,_UCS.COMMON2057,_UCS.COMMON2058,_UCS.COMMON2059,_UCS.COMMON2060,_UCS.COMMON2061,_UCS.COMMON2062],
    _D: [_UCS.COMMON2049,_UCS.COMMON2050,_UCS.COMMON2051,_UCS.COMMON2052,_UCS.COMMON2053,_UCS.COMMON2054,_UCS.COMMON2055],
    _m: [_UCS.COMMON2021,_UCS.COMMON2022,_UCS.COMMON2023,_UCS.COMMON2024,_UCS.COMMON2025,_UCS.COMMON2026,_UCS.COMMON2027,_UCS.COMMON2028,_UCS.COMMON2029,_UCS.COMMON2030,_UCS.COMMON2031,_UCS.COMMON2032,""],
    _M: [_UCS.COMMON2033,_UCS.COMMON2034,_UCS.COMMON2035,_UCS.COMMON2036,_UCS.COMMON2037,_UCS.COMMON2038,_UCS.COMMON2039,_UCS.COMMON2040,_UCS.COMMON2041,_UCS.COMMON2042,_UCS.COMMON2043,_UCS.COMMON2044,""]
});

// \u0027 는 '
/*msf.registerCulture({
    name: "en",
    d: "M/d/yyyy",
    D: "dddd, MMMM dd, yyyy",
    t: "h:mm tt",
    T: "h:mm:ss tt",
    M: "MMMM dd",
    Y: "MMMM, yyyy",
    _am: "AM",
    _pm: "PM",
    _r: ".",
    _cr: ".",
    _t: ",",
    _ct: ",",
    _c: "\u0027$\u0027#,0.00",
    _d: ["Sun","Mon","Tue","Wed","Thu","Fri","Sat"],
    _D: ["Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"],
    _m: ["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec",""],
    _M: ["January","February","March","April","May","June","July","August","September","October","November","December",""]
});*/
