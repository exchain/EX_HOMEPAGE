package com.finger.protocol.hermes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.finger.agent.msg.FixedMsg;
import com.finger.agent.util.AgentUtil;
import com.finger.protocol.fwtp.FwtpHeader;
import com.finger.protocol.relay.RelayMessage;

/**
 * Hermes-GFBK Message.
 */
public class HermesMessage implements Serializable {
    private static final long serialVersionUID = 1861076732864361024L;

    public static final int         _SizeOfMessage      =    9;

    public static final String      _Ch_NewLine_        =   "¶";	// ROW_DELIM : \n
    public static final String      _Ch_Tabs_           =   "≫";	// COL_DELIM : \t
    public static final String      _SUCCESS_RCODE_     =   "0000";
    public static final String      _FAILURE_RCODE_     =   "9999";
    public static final int         _Len_ResCode_       =    4;
    public static final int         _Len_MsgCode_       =    5;
    public static final int         _Len_Task_ID_       =    15;

    private String      _responseCode   =   _SUCCESS_RCODE_;
    private String      _errorCode      =   "";
    private String      _errorExtra     =   "";

    /** Storage for body of Hermes-GFBK response. */
    private String      _originData     =   "";
    private int         _dataLength     =   0;

    private String      _hmTaskDesc     =   "";
    private String      _hmTaskId       =   "";
    private String      _relayTarget    =   "";
    private String      _relayApTask    =   "";
    private String      _communiType    =   "";	// async, sync

    private ArrayList<LinkedHashMap<String, String>>    _io_Format      =   null;
    private LinkedHashMap<String, String>               _hermesData     =   new LinkedHashMap<String, String>();

    /*  _originData  */
    public void setOriginData(String originData) {
        this._originData = originData;
    }
    public String getOriginData() {
        return this._originData;
    }
    /*  _dataLength  */
    public void setDataLength(int dataLength) {
        this._dataLength = dataLength;
    }
    public int getDataLength() {
        return this._dataLength;
    }
    /*  _hmTaskDesc  */
    public void setHmTaskDesc(String taskDesc) {
        this._hmTaskDesc = taskDesc;
    }
    public String getHmTaskDesc() {
        return this._hmTaskDesc;
    }
    /*  _hmTaskId  */
    public void setHmTaskId(String taskId) {
        this._hmTaskId = taskId;
    }
    public String getHmTaskId() {
        return this._hmTaskId;
    }
    /*  _relayTarget  */
    public void setRelayTarget(String target) {
        this._relayTarget = target;
    }
    public String getRelayTarget() {
        return this._relayTarget;
    }
    /*  _relayApTask  */
    public void setRelayApTask(String aptask) {
        this._relayApTask = aptask;
    }
    public String getRelayApTask() {
        return this._relayApTask;
    }
    /*  _communiType  */
    public void setCommuniType(String type) {
        this._communiType = type;

        if ("sync".equalsIgnoreCase(AgentUtil.nvl(this._communiType)))
            this.isAsync = false;
        else
            this.isAsync = true;
    }
    public String getCommuniType() {
        return this._communiType;
    }
    public boolean isAsync = true;

    /*  _ioFormat  */
    public void setIoFormat(ArrayList<LinkedHashMap<String, String>> ioFormat) {
        this._io_Format = ioFormat;
    }
    public ArrayList<LinkedHashMap<String, String>> getIoFormat() {
        return this._io_Format;
    }
    public boolean isExistIoFormat() {
        if (this._io_Format == null || this._io_Format.isEmpty())
            return false;
        else
            return true;
    }

    /*  _hermesData : by Sending */
    public LinkedHashMap<String, String> getHermesData() {
        return this._hermesData;
    }
    public void setHermesSData(LinkedHashMap<String, String> inputData) {
        this._hermesData = inputData;
        this.settingInitMessage();
    }
    /* 공통부 항목 중 기본값 셋팅 */
    private void settingInitMessage() {
        if (this._hermesData == null || AgentUtil.isNull(this._hmTaskId))
            return;
        if (_VAN_FILESND_.equals(this._hmTaskId) || _VAN_FILERCV_.equals(this._hmTaskId))
            return;

        if (AgentUtil.isNull(this._hermesData.get(__Key_C001_)))
        this._hermesData.put(__Key_C001_, "SRS1" );																//식별코드 <고정값적용>

        this._hermesData.put(__Key_C004_, this._hmTaskId.substring(_len_prefix_, _len_prefix_+_len_MsgDv_) );	//전문구분코드
        this._hermesData.put(__Key_C005_, this._hmTaskId.substring(_len_prefix_+_len_MsgDv_) );					//업무구분코드

        if (AgentUtil.isNull(this._hermesData.get(__Key_C008_)))
        this._hermesData.put(__Key_C008_, (new SimpleDateFormat("yyyyMMdd")).format(new Date()));				//전송일자
        this._hermesData.put(__Key_C009_, (new SimpleDateFormat("HHmmss")).format(new Date()));					//전송시간

        /* 원화일때... */
        if (_prefix_vwn_.equals(this._hmTaskId.substring(0, _len_prefix_)))
        {
            if (AgentUtil.isNull(this._hermesData.get(__Key_C016_)))
                this._hermesData.put(__Key_C016_, new SimpleDateFormat("HHmmssSSS").format(new Date()) );		//예비 (업체영역 sync...)

        } else
        /* 외화일때... */
        if (_prefix_vfc_.equals(this._hmTaskId.substring(0, _len_prefix_)))
        {
            if (AgentUtil.isNull(this._hermesData.get(__Key_C011_)))
                this._hermesData.put(__Key_C011_, "HERMESFBK" );												//식별코드(송신자) <고정값적용>
            if (AgentUtil.isNull(this._hermesData.get(__Key_C013_)))
                this._hermesData.put(__Key_C013_, new SimpleDateFormat("HHmmssSSS").format(new Date()) );		//업체영역 (sync...)
        }
    }
    /* _hermesData : by Receiving */
    public void setHermesRData(LinkedHashMap<String, String> inputData) {
        this._hermesData = inputData;
    }
    public void setHermesRData(String inputData) {
        this._hermesData = parseDelimMessage(inputData);
    }
    public int getHermesDataRows() {
        if (this._hermesData == null || this._hermesData.isEmpty())
            return 0;
        else
            return this._hermesData.size();
    }
    public String getHmParameter(String key) {
        return AgentUtil.nvl(this._hermesData.get(key));
    }

    public LinkedHashMap<String, String> getHermesTaskData() {
        LinkedHashMap<String, String> _hmData = this._hermesData;

        if (!(_hmData == null || _hmData.isEmpty())) {
            _hmData.put(__HmTaskID_, this._hmTaskId );
            _hmData.put(__HmErrCd_ , this._errorCode );
            _hmData.put(__HmErrExt_, this._errorExtra );
        }

        return _hmData;
    }

    /*  _responseCode : 결과코드  */
    public void setResponseCode(String rCode) {
        if (rCode == null || rCode.length() != _Len_ResCode_)
            return;
        this._responseCode = rCode;
    }
    public String getResponseCode() {
        return this._responseCode;
    }
    public boolean isResponseSucces() {
        if (_SUCCESS_RCODE_.equals(this._responseCode))
            return true;
        else
            return false;
    }

    /*  _errorCode : 에러코드  */
    public void setErrorCode(String errCode) {
        if (errCode.length() != _Len_MsgCode_)
            return;
        this._errorCode = errCode;
        this._responseCode = _FAILURE_RCODE_;
    }
    public String getErrorCode() {
        return this._errorCode;
    }
    /*  _errorExtra : 에러메시지(추가내용)  */
    public void setErrorExtra(String errExtra) {
        this._errorExtra = errExtra;
    }
    public String getErrorExtra() {
        return this._errorExtra;
    }

    /**
     * DELIM 형태의 문자열 전문을 읽어 LinkedHashMap 형태 자료구조로 변환
     * @param
     * @return
     */
    private LinkedHashMap<String, String> parseDelimMessage(String inputData) {
        LinkedHashMap<String, String> htbl = new LinkedHashMap<String, String>();

        if (_VAN_FILESND_.equals(this._hmTaskId) || _VAN_FILERCV_.equals(this._hmTaskId)) {
            htbl.put(__Key_RlyFileName_, inputData.substring(0, RelayMessage._Size_FileName_) );
            htbl.put(__Key_RlyFileData_, inputData.substring(RelayMessage._Size_FileName_) );
        }
        else {
            String  _row  = "";
            String[] rows = inputData.split(FwtpHeader._ROW_DELIM);

            if (rows.length < 1)
                return htbl;

            // 첫번째 라인은 FID (필드 리스트)
            _row = rows[0].trim(); // 프로토콜상 레코드 구분자는 '\n'이나 '\r\n'를 보내는 경우를 위해 처리
            String[] arrFields = _row.split(FwtpHeader._COL_DELIM);
            int nFieldCount = arrFields.length;

            for (int i=1; i<rows.length; i++) {
                _row = rows[i];
                String[] arrValues = _row.split(FwtpHeader._COL_DELIM);
                for (int j=0; j<nFieldCount; j++) {
                    try {
                        htbl.put( arrFields[j], arrValues[j] );
                    } catch(Exception e) { 
                        htbl.put( arrFields[j], "" );
                    }
                }
            }
        }

        return htbl;
    }

    /**
     * 응답 메시지(String) 생성. To Hermes...
     * @return
     */
    public String buildResponseMessage() {
        if (_SUCCESS_RCODE_.equals(this._responseCode))
            return this._responseCode;
        else
            return this._responseCode + _errorCode + _errorExtra;
    }

    /**
     * 중계전송 형식의 전문을 검사한다. (To Hermes~~)
     * @return
     */
    public boolean validateMessageByHermes() {
        this.setResponseCode(_SUCCESS_RCODE_);

        if (this.getHermesDataRows() == 0) {
            this.setErrorCode(FixedMsg._MSG_HFBK_H0002);
            return false;
        }

        return true;
    }
    /**
     * 중계전송 형식의 전문 String 문자열을 생성한다. (To Hermes~~)
     * @return
     */
    public String buildMessageByHermes() {
        StringBuilder _sbKey = new StringBuilder();
        StringBuilder _sbVal = new StringBuilder();

        _sbKey.append( AgentUtil.fillSpaceString(this._hmTaskId, _Len_Task_ID_) );

        if (_VAN_FILESND_.equals(this._hmTaskId) || _VAN_FILERCV_.equals(this._hmTaskId)) {
            _sbKey.append( AgentUtil.fillSpaceString(this._responseCode, _Len_ResCode_) );
            _sbKey.append( AgentUtil.fillSpaceString(_hermesData.get(__Key_RlyFileName_), RelayMessage._Size_FileName_) );
            _sbKey.append( _hermesData.get(__Key_RlyFileData_) );
        }
        else {
            int nCnt = 0;
            if (this._hermesData == null || this._hermesData.isEmpty()) {
                _sbKey.append( AgentUtil.fillSpaceString(_FAILURE_RCODE_, _Len_ResCode_) );
            }
            else {
                _sbKey.append( AgentUtil.fillSpaceString(this._responseCode, _Len_ResCode_) );

                Iterator <String> iter = _hermesData.keySet().iterator();

                while (iter.hasNext()) {
                    String key = iter.next();
                    _sbKey.append( nCnt>0?FwtpHeader._COL_DELIM:"" ).append( key );
                    _sbVal.append( nCnt>0?FwtpHeader._COL_DELIM:"" ).append( _hermesData.get(key) );
                    nCnt ++;
                }
            }
            _sbKey.append(nCnt>0?FwtpHeader._COL_DELIM:"").append( __HmErrCd_      ).append(FwtpHeader._COL_DELIM).append( __HmErrExt_      );
            _sbVal.append(nCnt>0?FwtpHeader._COL_DELIM:"").append( this._errorCode ).append(FwtpHeader._COL_DELIM).append( this._errorExtra );

            _sbKey.append( FwtpHeader._ROW_DELIM ).append( _sbVal.toString() ).toString();
        }

        return _sbKey.toString();
    }
    public boolean isTaskForFile() {
        if (_VAN_FILESND_.equals(this._hmTaskId) || _VAN_FILERCV_.equals(this._hmTaskId))
            return true;
        else
            return false;
    }

    private boolean _isSurelyCheck = false;
    public void setIsSurelyCheck(boolean surely) {
        this._isSurelyCheck = surely;
    }
    public boolean getIsSurelyCheck() {
        return this._isSurelyCheck;
    }
    /**
     * 외부전송 형식의 전문을 검사한다. (To Outside<FIRM/VAN>...)
     * @return
     */
    public boolean validateMessageByOutside() {
        this.setResponseCode(_SUCCESS_RCODE_);

        if (_io_Format == null) {
            this.setErrorCode(FixedMsg._MSG_HFBK_H0001);
            return false;
        }
        else if (this.getHermesDataRows() == 0) {
            this.setErrorCode(FixedMsg._MSG_HFBK_H0002);
            return false;
        }
        else {
            if (this._isSurelyCheck) {
                for (int ii=0; ii<_io_Format.size(); ii++) {
                    LinkedHashMap<String, String> htItem = (LinkedHashMap<String, String>)_io_Format.get(ii);
                    String _sno = htItem.get("sno");
                    String desc = htItem.get("desc");
                    String surely = htItem.get("surely");
                    if ("Y".equalsIgnoreCase(surely) && AgentUtil.isNull(_hermesData.get(_sno))) {
                        this.setErrorCode(FixedMsg._MSG_HFBK_H0003);
                        this.setErrorExtra( _sno +" : "+ desc );
                        return false;
                    }
                }
            }
        }
        return true;
    }
    /**
     * 외부전송 형식의 전문 String 문자열을 생성한다. (To Outside<FIRM/VAN>...) <Ex> 300bytes String...
     * @return
     */
    public String buildMessageByOutside() {

        StringBuilder _sbuild = new StringBuilder();

        if (!(_io_Format == null || _hermesData.isEmpty())) {
        	
        	// 2000/505 전문송신 시 필요하여 추가 2015.08.07
        	String bankCd = "";
        	
            for (int ii=0; ii<_io_Format.size(); ii++) {
                LinkedHashMap<String, String> htItem = (LinkedHashMap<String, String>)_io_Format.get(ii);
                try {
                    String _sno = htItem.get("sno");
                    String type = htItem.get("type");
                    String length = AgentUtil.nvl(htItem.get("length"), "0");
                    String pscale = AgentUtil.nvl(htItem.get("pscale"), "0");
                    
                    String data   = "";
                    
                    if(AgentUtil.nvl(htItem.get("bankcdyn"), "N").equals("Y"))
                    {
                    	bankCd = AgentUtil.nvl(_hermesData.get(_sno));
                    }
                    
                    if ("9".equalsIgnoreCase(type))
                    	data = AgentUtil.fillNumericString(AgentUtil.nvl(_hermesData.get(_sno)), Integer.parseInt(length), Integer.parseInt(pscale));
                    else /*if ("X".equalsIgnoreCase(type))*/
                    	data = AgentUtil.fillSpaceString(AgentUtil.nvl(_hermesData.get(_sno)), Integer.parseInt(length));
                    
                    // 농협은행은 소숫점 자리수없이 전문을 생성하지만 제주은행은 소숫점 자리수를 포함하여 전문을 생성한다.
                    // 전문XML에 caleyn을 구분하여 Y이면 소숫점을 생성하고 N이면 소숫점없이 전문을 생성한다.
                    if(bankCd.equals("35"))
                    {
                    	int iPscale = Integer.parseInt(pscale);

                        if (iPscale > 0 && !AgentUtil.isNull(data.trim()))
                        {
                        	data = data.substring(1, data.length()-iPscale) +"."+ data.substring(data.length()-iPscale);
                        }
                    }
                    
                    _sbuild.append(data);
                }
                catch (Exception e) { e.printStackTrace(); }
            }
        }

        return _sbuild.toString();
    }

    /**
     * 외부전송 형식의 전문 String 문자열을 생성한다. (To Outside<VAN#File>)
     * @return
     */
    public String buildMessageByRly2File() {

        StringBuilder _sbuild = new StringBuilder();

        if (!(this._hermesData == null || this._hermesData.isEmpty())) {
            _sbuild.append( AgentUtil.fillSpaceString(_hermesData.get(__Key_RlyFileName_), RelayMessage._Size_FileName_) );
            _sbuild.append( _hermesData.get(__Key_RlyFileData_) );
        }

        return _sbuild.toString();
    }


    /**
     * 문자열을 입력 전문포맷을 읽어 LinkedHashMap 형태의 자료구조로 변환
     * @param inputData 전문(String)
     * @param list 전문포맷(Map)
     * @return
     */
    public static LinkedHashMap<String, String> makeMessage4Hermes(String inputData, ArrayList<LinkedHashMap<String, String>> list) {
        LinkedHashMap<String, String> htbl = new LinkedHashMap<String, String>();

        byte[] byteData = inputData.getBytes();

        int iTotLen = 0;
        for (int i=0; i<list.size(); i++) {
            LinkedHashMap<String, String> finfo = (LinkedHashMap<String, String>)list.get(i);
            try {
                String _sno = finfo.get("sno");
                String type = finfo.get("type");

                int iSize = Integer.parseInt(finfo.get("length"));
                String data = AgentUtil.getByteString(byteData, iTotLen, iSize);
                data = AgentUtil.nvl(data).trim();
                // 숫자타입은 소숫점 삽입 (필요 시 주석 Open하여 사용...)
                if ("9".equalsIgnoreCase(type)) {
                    if (!AgentUtil.isNull(data)) {
                        int pscale = Integer.parseInt(AgentUtil.nvl(finfo.get("pscale"),"0"));
                        
                        // 데이터에 소숫점이 있으면 SKIP 하고 없으면 pscale 만큼 소숫점 자릿수를 생성한다.
                        // 농협은행 : 소숫점 없음, 제주은행 : 소숫점 있음
                        if (pscale > 0 && data.indexOf(".") < 0)
                        {
                        	data = data.substring(0, data.length()-pscale) +"."+ data.substring(data.length()-pscale);
                        }
                        data = new BigDecimal(data).toString();
                    }
                }

                htbl.put(_sno, data);

                iTotLen += iSize;
            }
            catch (Exception e) { e.printStackTrace(); }
        }

        return htbl;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public static final String	__HmTaskID_		=	"HermesTaskID";
    public static final String	__HmRespCd_		=	"HermesRespCD";
    public static final String	__HmErrCd_		=	"HermesErrCD";
    public static final String	__HmErrExt_		=	"HermesErrExt";
    /*  전문 공통부 파라미터 키(Key)  */
    public static final String	__Key_C001_		=	"C001";
    public static final String	__Key_C002_		=	"C002";
    public static final String	__Key_C003_		=	"C003";
    public static final String	__Key_C004_		=	"C004";
    public static final String	__Key_C005_		=	"C005";
    public static final String	__Key_C006_		=	"C006";
    public static final String	__Key_C007_		=	"C007";
    public static final String	__Key_C008_		=	"C008";
    public static final String	__Key_C009_		=	"C009";
    public static final String	__Key_C010_		=	"C010";
    public static final String	__Key_C011_		=	"C011";
    public static final String	__Key_C012_		=	"C012";
    public static final String	__Key_C013_		=	"C013";
    public static final String	__Key_C014_		=	"C014";
    public static final String	__Key_C015_		=	"C015";
    public static final String	__Key_C016_		=	"C016";
    /*  전문 개별부 파라미터 키(Key)  */
    public static final String	__Key_G001_		=	"G001";
    public static final String	__Key_G002_		=	"G002";
    public static final String	__Key_G003_		=	"G003";
    public static final String	__Key_G004_		=	"G004";
    public static final String	__Key_G005_		=	"G005";
    public static final String	__Key_G006_		=	"G006";
    public static final String	__Key_G007_		=	"G007";
    public static final String	__Key_G008_		=	"G008";
    public static final String	__Key_G009_		=	"G009";
    public static final String	__Key_G010_		=	"G010";
    public static final String	__Key_G011_		=	"G011";
    public static final String	__Key_G012_		=	"G012";
    public static final String	__Key_G013_		=	"G013";
    public static final String	__Key_G014_		=	"G014";
    public static final String	__Key_G015_		=	"G015";
    public static final String	__Key_G016_		=	"G016";
    public static final String	__Key_G017_		=	"G017";
    public static final String	__Key_G018_		=	"G018";
    public static final String	__Key_G019_		=	"G019";
    public static final String	__Key_G020_		=	"G020";
    public static final String	__Key_G021_		=	"G021";
    public static final String	__Key_G022_		=	"G022";
    public static final String	__Key_G023_		=	"G023";
    public static final String	__Key_G024_		=	"G024";
    public static final String	__Key_G025_		=	"G025";
    public static final String	__Key_G026_		=	"G026";
    public static final String	__Key_G027_		=	"G027";
    public static final String	__Key_G028_		=	"G028";
    public static final String	__Key_G029_		=	"G029";
    public static final String	__Key_G030_		=	"G030";
    public static final String	__Key_G031_		=	"G031";
    public static final String	__Key_G032_		=	"G032";
    public static final String	__Key_G033_		=	"G033";
    public static final String	__Key_G034_		=	"G034";
    public static final String	__Key_G035_		=	"G035";
    public static final String	__Key_G036_		=	"G036";
    public static final String	__Key_G037_		=	"G037";
    public static final String	__Key_G038_		=	"G038";
    public static final String	__Key_G039_		=	"G039";
    public static final String	__Key_G040_		=	"G040";
    public static final String	__Key_G041_		=	"G041";
    public static final String	__Key_G042_		=	"G042";
    public static final String	__Key_G043_		=	"G043";
    public static final String	__Key_G044_		=	"G044";
    public static final String	__Key_G045_		=	"G045";
    public static final String	__Key_G046_		=	"G046";
    public static final String	__Key_G047_		=	"G047";
    public static final String	__Key_G048_		=	"G048";
    public static final String	__Key_G049_		=	"G049";
    public static final String	__Key_G050_		=	"G050";
    public static final String	__Key_G051_		=	"G051";
    public static final String	__Key_G052_		=	"G052";
    public static final String	__Key_G053_		=	"G053";
    public static final String	__Key_G054_		=	"G054";
    public static final String	__Key_G055_		=	"G055";
    public static final String	__Key_G056_		=	"G056";
    public static final String	__Key_G057_		=	"G057";
    public static final String	__Key_G058_		=	"G058";
    public static final String	__Key_G059_		=	"G059";
    public static final String	__Key_G060_		=	"G060";
    
    /*  전문 개별부 파라미터 키(Key)  */
    public static final String  __Key_RlyFileName_	=	"RlyFileName";
    public static final String  __Key_RlyFileData_	=	"RlyFileData";

    public static final int		_len_MsgDv_		=	4 ;		//전문구분길이
    public static final int		_len_prefix_	=	3 ;		//prefix 길이
    public static final String	_prefix_vwn_	=	"VWN";	//원화 prefix
    public static final String	_prefix_vfc_	=	"VFC";	//외화 prefix

    /*  TASK-ID List - [원화:WON]거래전문  */
    public static final String	_VKN_0800800_	=	"VWN0800800";	//[공통]폴링전문
    public static final String	_VKN_0810800_	=	"VWN0810800";	//{KSNET서비스명}_{은행서비스명}

    public static final String	_VWN_0800100_	=	"VWN0800100";	//[원환]업무개시_이체개시
    public static final String	_VWN_0810100_	=	"VWN0810100";
    public static final String	_VWN_0800300_	=	"VWN0800300";	//[원화]업무종료_이체종료
    public static final String	_VWN_0810300_	=	"VWN0810300";

    public static final String	_VWN_0600300_	=	"VWN0600300";	//[원화]잔액조회_잔액조회
    public static final String	_VWN_0610300_	=	"VWN0610300";
    public static final String	_VWN_0600101_	=	"VWN0600101";	//[원화]처리결과조회_이체결과조회
    public static final String	_VWN_0610101_	=	"VWN0610101";
    public static final String	_VWN_0600400_	=	"VWN0600400";	//[원화]계좌조회_이체예비거래
    public static final String	_VWN_0610400_	=	"VWN0610400";
    public static final String	_VWN_0100100_	=	"VWN0100100";	//[원화]이체요구_자금이체(자행,타행)
    public static final String	_VWN_0110100_	=	"VWN0110100";
    public static final String	_VWN_0400100_	=	"VWN0400100";	//[원화]타행이체불능통지_타행이체불능전송
    public static final String	_VWN_0410100_	=	"VWN0410100";
    public static final String	_VWN_0200300_	=	"VWN0200300";	//[원화]거래내역전송_원장내역전송
    public static final String	_VWN_0210300_	=	"VWN0210300";

    /*  TASK-ID List - [외화:FCC]거래전문  */
    public static final String	_VFC_1000100_	=	"VFC1000100";	//[외화]이체개시
    public static final String	_VFC_1100100_	=	"VFC1100100";
    public static final String	_VFC_1000200_	=	"VFC1000200";	//[외화]이체종료
    public static final String	_VFC_1100200_	=	"VFC1100200";

    public static final String	_VFC_9000601_	=	"VFC9000601";	//[외화]계좌등록/해지/변경통지(신전문)
    public static final String	_VFC_9100601_	=	"VFC9100601";
    public static final String	_VFC_9000701_	=	"VFC9000701";	//[외화]계좌등록/해지/변경통지결번(신전문)
    public static final String	_VFC_9100701_	=	"VFC9100701";
    public static final String	_VFC_7000900_	=	"VFC7000900";	//[외화]예금잔액조회
    public static final String	_VFC_7100900_	=	"VFC7100900";
    public static final String	_VFC_3000700_	=	"VFC3000700";	//[외화]송금결과통지
    public static final String	_VFC_3100700_	=	"VFC3100700";
    public static final String	_VFC_3000800_	=	"VFC3000800";	//[외화]송금결과통지결번
    public static final String	_VFC_3100800_	=	"VFC3100800";
    public static final String	_VFC_8000601_	=	"VFC8000601";	//[외화]입출금거래명세통지(신전문)
    public static final String	_VFC_8100601_	=	"VFC8100601";
    public static final String	_VFC_8000701_	=	"VFC8000701";	//[외화]입출금거래명세통지결번(신전문)
    public static final String	_VFC_8100701_	=	"VFC8100701";
    public static final String	_VFC_6000401_	=	"VFC6000401";	//[외화]외화예금주명조회
    public static final String	_VFC_6100401_	=	"VFC6100401";
    public static final String	_VFC_2000500_	=	"VFC2000500";	//[외화]외화송금의뢰(타행)
    public static final String	_VFC_2100500_	=	"VFC2100500";
    public static final String	_VFC_2000505_	=	"VFC2000505";	//[외화]외화송금의뢰(당행)
    public static final String	_VFC_2100505_	=	"VFC2100505";
    public static final String	_VFC_6000521_	=	"VFC6000521";	//[외화]실시간환율조회
    public static final String	_VFC_6100521_	=	"VFC6100521";
    public static final String	_VFC_6000525_	=	"VFC6000525";	//[외화]예약환율번호조회
    public static final String	_VFC_6100525_	=	"VFC6100525";
    public static final String	_VFC_6000526_	=	"VFC6000526";	//[외화]타발송금내역조회
    public static final String	_VFC_6100526_	=	"VFC6100526";
    public static final String	_VFC_6000527_	=	"VFC6000527";	//[외화]사후관리번호조회
    public static final String	_VFC_6100527_	=	"VFC6100527";
    public static final String	_VFC_9100824_	=	"VFC9100824";	//[외화]해외계좌거래내역통지
    public static final String	_VFC_9101824_	=	"VFC9101824";
    public static final String	_VFC_9001825_	=	"VFC9001825";	//[외화]해외계좌거래내역통지결번
    public static final String	_VFC_9101825_	=	"VFC9101825";
    public static final String	_VFC_8000800_	=	"VFC8000800";	//[외화]외화송금도착내역통지
    public static final String	_VFC_8100800_	=	"VFC8100800";
    public static final String	_VFC_8000900_	=	"VFC8000900";	//[외화]외화송금도착내역통지결번
    public static final String	_VFC_8100900_	=	"VFC8100900";
    
    /*  TASK-ID List - *별도작업용*  */
    public static final String	_VAN_FILESND_	=	"VANFILESND";	//파일저장_APS->중계서버
    public static final String	_VAN_FILERCV_	=	"VANFILERCV";	//파일다운_중계서버->APS
}