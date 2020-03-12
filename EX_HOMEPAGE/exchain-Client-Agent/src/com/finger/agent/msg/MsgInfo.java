package com.finger.agent.msg;

import java.util.HashMap;

import com.finger.agent.util.AgentUtil;

public class MsgInfo {

    private static HashMap<String, String>	_MessageMap	=	new HashMap<String, String>();

    /** 정상 (정상처리되었습니다.) */
    public static final String		_MSG_SUCCESS		=	"00000";

    /**
     * 메시지코드에 대한 설명정보를 테이블에 등록한다.
     */
    public static void setMessageInfo(HashMap<String, String> msgMap) {
    	_MessageMap	=	msgMap;
    }

    /**
     * 메시지코드에 해당하는 설명을 얻는다
     * @param code 오류코드
     * @return 오류코드 설명값
     * @throws Exception 
     */
    public static String getMessage(String code) {
        String returnMsg = null;

        if (!isValidateCode(code)) {
            returnMsg = "메시지 코드는 반드시 `"+ FixedMsg._MSG_CODE_SIZE_ +"`자리로 입력되어야 합니다. - ["+ code +"]";
        }
        else {
            if (_MessageMap == null || _MessageMap.isEmpty()) {
                if (FixedMsg.isValidCode(code))
                    returnMsg = FixedMsg.getMessage(code);
                else
                    returnMsg = "메시지정보 (DB)데이터가 로딩되지 않았습니다.";
            }
            returnMsg = _MessageMap.get(code);

            if (AgentUtil.isNull(returnMsg)) {
                if (FixedMsg.isValidCode(code))
                    returnMsg = FixedMsg.getMessage(code);
                else
                    returnMsg = "### 정의되지 않은 메세지 코드를 사용하였습니다.&lt;br/&gt;"
                    		  + "    관리테이블에 코드["+ code +"] 정보를 등록하시기 바랍니다!";
            }
        }

        return returnMsg;
    }

    public static boolean isValidateCode(String code) {
        if (code == null || code.trim().length() != FixedMsg._MSG_CODE_SIZE_)
            return false;
        else
            return true;
    }
}