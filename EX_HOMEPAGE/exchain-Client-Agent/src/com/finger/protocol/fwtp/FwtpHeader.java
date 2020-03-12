package com.finger.protocol.fwtp;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * FWTP(Finger-WebFrame Transfer Protocol) Header.
 * 
 * @author Finger21
 */
public class FwtpHeader {
    private static Logger logger = Logger.getLogger(FwtpHeader.class);

    // 성공처리결과/메시지코드
    public static final String	_RESPONSE_OKAY_			=	"OK";
    public static final String	_RESPONSE_WARNING_		=	"WN";
    public static final String	_RESPONSE_ERROR_		=	"XX";
    public static final String	_SUCCESS_MESSAGE_		=	"00000";

    // DataType 정의
    public static final String	_DATATYPE_COMMON		=	"COM";
    public static final String	_DATATYPE_GRID			=	"GRD";
    public static final String	_DATATYPE_CHART			=	"CHT";
    public static final String	_DATATYPE_MEMORY		=	"MEM";

    // Request By (요청/호출 프로세스)
    public static final String	_REQUEST_BY_CORE		=	"CR";
    public static final String	_REQUEST_BY_HERMES		=	"HM";
    public static final String	_REQUEST_BY_UX			=	"UX";
    public static final String	_REQUEST_BY_EMULATOR	=	"ZZ";

    // HEADER SIZE
    public static final int		_HEADER_SIZE	=	100;	//Bytes

    // Deliminator 정의
    public static final String	_COL_DELIM		=	"\t";
    public static final String	_ROW_DELIM		=	"\n";

    // HEADER - FWTP Sign
    public static final int		__INITIAL		=	0;	//  4	  4
    public static final int		__DATA_LEN		=	1;	//  8	 12
    public static final int		__ROW_COUNT		=	2;	//  5	 17
    public static final int		__REQUEST_BY	=	3;	//  2	 19
    public static final int		__CLIENT_IP		=	4;	// 15	 34
    public static final int		__SYS_MGT_NO	=	5;	//  7	 41
    public static final int		__USER_ID		=	6;	// 10	 51
    public static final int		__TIME_STAMP	=	7;	// 20	 71
    public static final int		__TASK_ID		=	8;	//  9	 80
    public static final int		__DATA_TYPE		=	9;	//  3	 83
    public static final int		__RESPONSE_CODE	=	10;	//  2	 85
    public static final int		__MESSAGE_CODE	=	11;	//  5	 90
    public static final int		__PAGE_ID		=	12;	//  9	 99
    public static final int		__FILLER		=	13;	//  1	100

    // String 배열 형태의 HEADER 항목 정보
    public static final String[]	FWTP_HEADER_INFO	=
        {	"INITIAL"			// 0
        ,	"DATA_LEN"			// 1
        ,	"ROW_COUNT"			// 2
        ,	"REQUEST_BY"		// 3
        ,	"CLIENT_IP"			// 4
        ,	"SYS_MGT_NO"		// 5
        ,	"USER_ID"			// 6
        ,	"TIME_STAMP"		// 7
        ,	"TASK_ID"			// 8
        ,	"DATA_TYPE"			// 9
        ,	"RESPONSE_CODE"		//10
        ,	"MESSAGE_CODE"		//11
        ,	"PAGE_ID"			//12
        ,	"FILLER"			//13
        };
    public static final String[]	FWTP_HEADER_TYPE	=
        {	"X"					//INITIAL		// 0
        ,	"9"					//DATA_LEN		// 1
        ,	"9"					//ROW_COUNT		// 2
        ,	"X"					//REQUEST_BY	// 3
        ,	"X"					//CLIENT_IP		// 4
        ,	"X"					//SYS_MGT_NO	// 5
        ,	"X"					//USER_ID		// 6
        ,	"X"					//TIME_STAMP	// 7
        ,	"X"					//TASK_ID		// 8
        ,	"X"					//DATA_TYPE		// 9
        ,	"X"					//RESPONSE_CODE	//10
        ,	"X"					//MESSAGE_CODE	//11
        ,	"X"					//PAGE_ID		//12
        ,	"X"					//FILLER		//13
        };

    public static final String		_C_INITIAL	=	"FWTP";

    public static final String		_C__DATA_	=	"_DATA_";

    public static final String		_GridRows_	=	"_GridRows_";

    // HEADER의 각 항목의 크기
    public static final int []		FWTP_HEADER_SIZE	=
        {  4,  8,  5,  2,  15,  7,  10,  20,  9,  3,  2,  5,  9, 1 };

    public static int getOffset(String itemName) {
        int nOffset = 0;
        int nLen = FWTP_HEADER_INFO.length;

        for (int i=0; i<nLen; i++) {
        	if (itemName.equals(FWTP_HEADER_INFO[i])) {
        		break;
        	}
        	nOffset += FWTP_HEADER_SIZE[i];
        }
        return nOffset;
    }


    /**
     * 헤더문자열 정보를 분석하여 각 필드정보를 Map에 담는다
     * @param argData
     * @return 필정정보Map
     */
    public static LinkedHashMap<String, String> makeHeaderMap(String argData) {
        logger.debug(" ==> argData ["+ argData +"]");
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

        if (FwtpHeader._HEADER_SIZE != argData.getBytes().length)
            return map;

        int length = FwtpHeader.FWTP_HEADER_INFO.length;
        int offset = 0;
        for (int ii=0; ii<length; ii++) {
            String s_item = new String( argData.getBytes(), offset, FwtpHeader.FWTP_HEADER_SIZE[ii] );

            map.put( FwtpHeader.FWTP_HEADER_INFO[ii], s_item.trim() );

            offset += FwtpHeader.FWTP_HEADER_SIZE[ii];
        }

        return map;
    }

    public boolean validateHeaderMap(Map <String, String>map) {
        logger.info("validateHeaderMap()...");

        boolean nRet = true;

        String strKey = "";
        String strValue = "";

        Set<Map.Entry<String, String>> set = map.entrySet();
        for (Map.Entry<String, String> me : set) {
            strKey = me.getKey();
            strValue = me.getValue();
            //--logger.debug("  ==> [ "+ strKey +" | "+ strValue +" ]");
            //key: 0_INITIAL <-> FWTP
            if (FWTP_HEADER_INFO[__INITIAL].equals(strKey)) {
            	if ( !_C_INITIAL.equals(strValue) ) {
            		logger.info( strKey +" on FWTP Message HeaderMap Failed Verification. ["+ strValue +"]");
            		return false;
            	}
            }
            //key: 3_REQUEST_BY <-> CR, HM, UX, XX
            if (FWTP_HEADER_INFO[__REQUEST_BY].equals(strKey)) {
            	if ( !(_REQUEST_BY_CORE.equals(strValue) || _REQUEST_BY_HERMES.equals(strValue) || _REQUEST_BY_UX.equals(strValue)
            			|| _REQUEST_BY_EMULATOR.equals(strValue)) ) {
            		logger.info( strKey +" on FWTP Message HeaderMap Failed Verification. ["+ strValue +"]");
            		return false;
            	}
            }
            //key: 5_SYS_MGT_NO -> SIZE 검사
            /*if (FWTP_HEADER_INFO[__SYS_MGT_NO].equals(strKey)) {
            	if (FWTP_HEADER_SIZE[__SYS_MGT_NO] != strValue.length()) {
            		logger.info( strKey +" on FWTP Message HeaderMap Failed Verification. ["+ strValue +"] != size("+ FWTP_HEADER_SIZE[__SYS_MGT_NO] +")");
            		return false;
            	}
            }*/
            //key: 8_TASK_ID -> SIZE 검사
            /*
            if (FWTP_HEADER_INFO[__TASK_ID].equals(strKey)) {
            	if (FWTP_HEADER_SIZE[__TASK_ID] != strValue.length()) {
            		logger.info( strKey +" on FWTP Message HeaderMap Failed Verification. ["+ strValue +"] != size("+ FWTP_HEADER_SIZE[__TASK_ID] +")");
            		return false;
            	}
            }
            */
            //key: 9_DATA_TYPE <-> COM, GRD, CHT
            if (FWTP_HEADER_INFO[__DATA_TYPE].equals(strKey)) {
            	if (!(_DATATYPE_COMMON.equals(strValue) || _DATATYPE_GRID.equals(strValue) || _DATATYPE_CHART.equals(strValue) || _DATATYPE_MEMORY.equals(strValue)) ) {
            		logger.info( strKey +" on FWTP Message HeaderMap Failed Verification. ["+ strValue +"]");
            		return false;
            	}
            }
        }

        return nRet;
    }
}