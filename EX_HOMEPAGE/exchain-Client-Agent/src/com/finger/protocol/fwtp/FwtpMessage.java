package com.finger.protocol.fwtp;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.log4j.Logger;

import com.finger.agent.util.AgentUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

/**
 * FWTP(Finger-WebFrame Transfer Protocol) Message.
 */
public class FwtpMessage extends FwtpHeader implements Serializable {
	
    private static final long serialVersionUID = 1861076732864361024L;
    private static Logger logger = Logger.getLogger(FwtpMessage.class);

    /** Storage for body of FWTP response. */
    // request & response
    private LinkedHashMap<String, String>				_mapHeader		=	new LinkedHashMap<String, String>();
    /** 입력 Parameter */
    private ArrayList<LinkedHashMap<String, String>>	_requestData	=	new ArrayList<LinkedHashMap<String, String>>();
    private ArrayList<LinkedHashMap<String, String>>	_requestGrid	=	new ArrayList<LinkedHashMap<String, String>>();
    /** 결과응답값(Server) */
    private ArrayList<LinkedHashMap<String, String>>	_responseData	=	new ArrayList<LinkedHashMap<String, String>>();

    /** 결과응답List(Client) */
    private ArrayList<LinkedHashMap<String, String>>	_responseResultList	=	null;

    // Additional Error Message
    private String		_addErrorMessage	=	"";

    private String[]	_arrColumnName		=	null;
    private String[]	_arrColumnType		=	null;
    private String[]	_arrColumnSize		=	null;

    private String		_biz_taskClass		=	"";
    private String		_biz_taskAction		=	"";

    private String		_throw_ClassName	=	"";
    private String		_throw_LineNumber	=	"";

    public FwtpMessage() {
        this.clearErrorMessage();
        this.setTimeStamp();
        this.setDataType(_DATATYPE_COMMON);
        this.setResponseCode(_RESPONSE_OKAY_);
    }

    /*
     * Task Class of BizService
     */
    public void setBizTaskClass(String value) {
        this._biz_taskClass = value;
    }
    public String getBizTaskClass() {
        return this._biz_taskClass;
    }
    /*
     * Task Action of BizService
     */
    public void setBizTaskAction(String value) {
        this._biz_taskAction = value;
    }
    public String getBizTaskAction() {
        return this._biz_taskAction;
    }

    /*
     * set/get Header Map
     */
    public void setHeader(LinkedHashMap<String, String> header) {
        this._mapHeader = header;

        String s_data = header.get(_C__DATA_);
        s_data = (s_data == null) ? "" : s_data;

        parseRequestData(s_data);
    }
    public LinkedHashMap<String, String> getHeader() {
        return _mapHeader;
    }

    public String getItemData(int index) {
        String strRet = "";

        strRet = _mapHeader.get(FWTP_HEADER_INFO[index]);
        strRet = (strRet == null) ? "" : strRet;

        return strRet;
    }

    public String getHeaderValue(String key) {
        return _mapHeader.get(key) == null ? "" : _mapHeader.get(key);
    }

    /**
     * 요청하는 HEADER 및 DATA 부를 전문형식에 맞게 문자열(String)로 생성한다.
     * @return 요청문자열
     */
    public String buildRequestMessage() {

        LinkedHashMap<String, String> mapHeader = this.getHeader();

        if (_mapHeader == null)
            return null;

        String requestData = this.getRequestDataToString();

        StringBuilder strbuild = new StringBuilder();

        String strValue = "";
        int nLength = 0;
        int nHSize = 0;

        // 1. INITIAL = `FWTP`
        strbuild.append( _C_INITIAL );

        // 2. DATA LENGTH
        nLength = requestData.getBytes().length;
        nHSize  = FWTP_HEADER_SIZE[__DATA_LEN];
        strbuild.append( AgentUtil.fillNumericString(Integer.toString(nLength), nHSize) );
        // 3. ROW COUNT
        nLength = this.getRequestDataSize();
        nHSize  = FWTP_HEADER_SIZE[__ROW_COUNT];
        strbuild.append( AgentUtil.fillNumericString(Integer.toString(nLength), nHSize) );

        // 네번째(REQUEST_BY) 항목부터 끝까지 셋팅
        for (int i=3; i<FWTP_HEADER_SIZE.length; i++) {
            strValue = mapHeader.get(FWTP_HEADER_INFO[i]);
            nHSize   = FWTP_HEADER_SIZE[i];

            if (FWTP_HEADER_TYPE[i].toUpperCase().equals("X")) {	// 문자열처리
                strbuild.append(AgentUtil.fillSpaceString(strValue, nHSize));
            }
            else {	// Numeric 처리
                strbuild.append(AgentUtil.fillNumericString(strValue, nHSize));
            }
        }
        strbuild.append( requestData );

        return strbuild.toString();
    }

    /*
     * _REQUEST_BY
     */
    public void setRequestBy(String value) {
        _mapHeader.put(FWTP_HEADER_INFO[__REQUEST_BY], value);
    }
    public String getRequestBy() {
        String value = _mapHeader.get(FWTP_HEADER_INFO[__REQUEST_BY]);
        return value == null ? "" : value;
    }
    /*
     * _CLIENT_IP
     */
    public void setClientIp(String value) {
        _mapHeader.put(FWTP_HEADER_INFO[__CLIENT_IP], value);
    }
    public String getClientIp() {
        String value = _mapHeader.get(FWTP_HEADER_INFO[__CLIENT_IP]);
        return value == null ? "" : value;
    }
    /*
     * _SYS_MGT_NO
     */
    public void setSysMgtNo(String value) {
        _mapHeader.put(FWTP_HEADER_INFO[__SYS_MGT_NO], value);
    }
    public String getSysMgtNo() {
        String value = _mapHeader.get(FWTP_HEADER_INFO[__SYS_MGT_NO]);
        return value == null ? "" : value;
    }
    /*
     * _SYS_ID
     */
    public void setSysId(String value) {
        _mapHeader.put(FWTP_HEADER_INFO[__SYS_MGT_NO], value);
    }
    public String getSysId() {
        String value = _mapHeader.get(FWTP_HEADER_INFO[__SYS_MGT_NO]);
        return value == null ? "" : value;
    }
    /*
     * _CUST_SEQNO
     */
    public void setCustSeqno(String value) {
        _mapHeader.put(FWTP_HEADER_INFO[__USER_ID], value);
    }
    public String getCustSeqno() {
        String value = _mapHeader.get(FWTP_HEADER_INFO[__USER_ID]);
        return value == null ? "" : value;
    }
    
    /*
     * _USER_ID
     */
    public void setUserId(String value) {
        _mapHeader.put(FWTP_HEADER_INFO[__USER_ID], value);
    }
    public String getUserId() {
        String value = _mapHeader.get(FWTP_HEADER_INFO[__USER_ID]);
        return value == null ? "" : value;
    }
    /*
     * _TIME_STAMP
     */
    public void setTimeStamp() {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        _mapHeader.put(FWTP_HEADER_INFO[__TIME_STAMP], timeStamp);
    }
    public void setTimeStamp(String datetime) {
        if (datetime == null || "".equals(datetime)) ;
        else
            _mapHeader.put(FWTP_HEADER_INFO[__TIME_STAMP], datetime);
    }
    public String getTimeStamp() {
        String value = _mapHeader.get(FWTP_HEADER_INFO[__TIME_STAMP]);
        return value == null ? "" : value;
    }
    /*
     * _TASK_ID
     */
    public void setTaskId(String value) {
        if (value.length()>FWTP_HEADER_SIZE[__TASK_ID]) {
            value = value.substring(0, FWTP_HEADER_SIZE[__TASK_ID]);
        }
        _mapHeader.put(FWTP_HEADER_INFO[__TASK_ID], value);
    }
    public String getTaskId() {
        String value = _mapHeader.get(FWTP_HEADER_INFO[__TASK_ID]);
        return value == null ? "" : value;
    }
    /*
     * _PAGE_ID
     */
    public void setPageId(String value) {
        _mapHeader.put(FWTP_HEADER_INFO[__PAGE_ID], value);
    }
    public String getPageId() {
        String value = _mapHeader.get(FWTP_HEADER_INFO[__PAGE_ID]);
        return value == null ? "" : value;
    }
    /*
     * _DATA_TYPE
     */
    public void setDataType(String value) {
        _mapHeader.put(FWTP_HEADER_INFO[__DATA_TYPE], value);
    }
    public String getDataType() {
        String value = _mapHeader.get(FWTP_HEADER_INFO[__DATA_TYPE]);
        if (value == null) {
            setDataType(_DATATYPE_COMMON);
            value = _DATATYPE_COMMON;
        }
        return value;
    }
    /*
     * _RESPONSE_CODE
     */
    public void setResponseCode(String value) {
        _mapHeader.put(FWTP_HEADER_INFO[__RESPONSE_CODE], value);
    }
    public void setResponseWarning() {
        _mapHeader.put(FWTP_HEADER_INFO[__RESPONSE_CODE], _RESPONSE_WARNING_);
    }
    public String getResponseCode() {
        String value = _mapHeader.get(FWTP_HEADER_INFO[__RESPONSE_CODE]);
        return (value == null || value.equals(""))? _RESPONSE_OKAY_ : value;
    }
    /*
     * _MESSAGE_CODE
     */
    public void setMessageCode(String value) {
        _mapHeader.put(FWTP_HEADER_INFO[__MESSAGE_CODE], value);

        /* MESSAGE 코드가 `0`으로 시작하지 않으면, `OK`로 리턴하지 않는다. */
        if ( value != null) {
            if (!_SUCCESS_MESSAGE_.substring(0,1).equals(value.substring(0,1)))
                setResponseCode(_RESPONSE_ERROR_);
        }
    }
    public String getMessageCode() {
        String value = _mapHeader.get(FWTP_HEADER_INFO[__MESSAGE_CODE]);
        return (value == null || value.equals("")) ? "" : value;
    }
    public boolean isSuccessMessage() {
        String msgCd = getMessageCode();
        if (AgentUtil.isNull(msgCd) || _SUCCESS_MESSAGE_.substring(0,1).equals(msgCd.substring(0,1)))
            return true;
        else
            return false;
    }

    /*
     * Additional Error Message
     */
    public void setErrorMessage(String value) {
        this._addErrorMessage = value;
    }
    public String getErrorMessage() {
        return _addErrorMessage;
    }
    public void clearErrorMessage() {
        this._addErrorMessage = "";
    }

    public void addErrorMessage(String value) {
        if (this._addErrorMessage == null || "".equals(this._addErrorMessage))
            this._addErrorMessage = value;
        else
            this._addErrorMessage += ("\n"+ value);
    }

    /* 
     * _DATA_
     */
    public void setRequestData(String data) {
        parseRequestData(data);
    }
    public void setRequestData(LinkedHashMap<String, String> requestData) {
        _requestData = new ArrayList<LinkedHashMap<String, String>>();
        _requestData.add( requestData );
    }
    public void setRequestData(ArrayList<LinkedHashMap<String, String>> arrRequestData) {
        _requestData = arrRequestData;
    }
    public ArrayList<LinkedHashMap<String, String>> getRequestData() {
        return _requestData;
    }
    public String getRequestDataToString() {
        return makeRequestDataString();
    }
    /*
     * _GridRows_
     */
    public void setRequestGrid(ArrayList<LinkedHashMap<String, String>> arrRequestGrid) {
        _requestGrid = arrRequestGrid;
    }
    public ArrayList<LinkedHashMap<String, String>> getRequestGrid() {
        return _requestGrid;
    }

    /**
     * 입력된 문자열을 FWTP _DATA_영역 포맷으로 파싱하여 ArrayList(HashMap)형태로 저장한다.
     * @param  FWTP _DATA_
     * @return None
     */
    private void parseRequestData(String data) {

        String row = "";
        String[] rows = data.split(_ROW_DELIM);

        _requestData = new ArrayList<LinkedHashMap<String, String>>();

        if (rows.length < 1)
            return;

        /* 첫번째 라인은 FID (필드 리스트) */
        row = rows[0].trim(); // 프로토콜상 레코드 구분자는 '\n'이나 '\r\n'를 보내는 경우를 위해 처리
        String[] arrFields = row.split(_COL_DELIM);

        /* 두번째 라인 이후는 데이타, 파싱해서 ArrayList(HashMap) 구조로 변환 */
        int nFieldCount = arrFields.length;

        if (nFieldCount==1 && "query".equals(row)) {
            LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
            map.put("query", data.substring(5));
            _requestData.add(map);
        }
        else {
            for (int i=1; i<rows.length; i++) {
                LinkedHashMap<String, String> hmap = new LinkedHashMap<String, String>();

                // 필드의값 배열 (입력 데이터이므로 대부분 i
                row = rows[i];
                String[] arrValues = row.split(_COL_DELIM);
                
                logger.debug("nFieldCount=["+nFieldCount+"]");

                for (int j=0; j<nFieldCount; j++) {
                    try {
                        //logger.debug("arrFields["+ j +"] ==> ["+ arrFields[j] +"]");
                        if (_GridRows_.equals(arrFields[j])){
                        	
                        	logger.debug("parseRequestData parseAddGridRows start");
                        	
                        	parseAddGridRows( arrValues[j] );
                        	
                        	logger.debug("parseRequestData parseAddGridRows end");
                        }
                        else{
                        	//hmap.put(arrFields[j], AgentUtil.revokeXSSFilter(arrValues[j]));
                        	hmap.put(arrFields[j], arrValues[j]);
                        }

                    }
                    catch(Exception e) { 
                        hmap.put(arrFields[j], "");
                    }
                }
                _requestData.add(hmap);
            }
        }
    }
    /**
     * 요청 문자열의 _DATA_영역 중 _GridRows_데이터를 파싱하여 ArrayList(HashMap)형태로 저장한다.
     * @param data
     */
    private void parseAddGridRows(String data) {
    	
    	try {
    	
	    	logger.debug("parseAddGridRows 데이터 특수문자 변환");
	    	
	        String gridRows = AgentUtil.revokeXSSFilter(data);
	        
	        //logger.debug("parseAddGridRows gridRows=["+gridRows+"]");
	
	        _requestGrid = new ArrayList<LinkedHashMap<String, String>>();
	        Type gridType = new TypeToken<ArrayList<LinkedHashMap<String, String>>>(){}.getType();
	        
	        JsonObject jsonObjA = (JsonObject)new JsonParser().parse( gridRows );
	        JsonArray jsonArray = jsonObjA.get("rows").getAsJsonArray();
	        
	        _requestGrid = new Gson().fromJson(jsonArray, gridType);
	        
	        logger.debug("parseAddGridRows 데이터를 파싱하여 ArrayList(HashMap)형태로 저장");
	
	        //logger.debug("----->> _requestGrid : \n"+ _requestGrid );
        }
        catch(Exception e) { 
	        logger.debug("parseAddGridRows Exception=["+e.toString()+"]");
        }
    }

    private String makeRequestDataString() {
        StringBuilder strbuild = new StringBuilder();

        if (_requestData == null) {
        }
        else {
            // 데이터 출력
            String[] keysName = null;
            for (int ii=0; ii<_requestData.size(); ii++) {
                LinkedHashMap<String, String> hmap = (LinkedHashMap<String, String>)_requestData.get(ii);

                if (hmap != null)
                {
                    int nKeyCnt = hmap.keySet().size();
                    if (keysName == null) {
                        keysName = hmap.keySet().toArray(new String[nKeyCnt]);

                        for (int k1=0; k1<nKeyCnt; k1++) {
                            if (k1 == 0)
                                strbuild.append( keysName[k1] );
                            else
                                strbuild.append( _COL_DELIM + keysName[k1] );
                        }
                        strbuild.append(_ROW_DELIM);
                    }
                    for (int k2=0; k2<nKeyCnt; k2++) {
                        if (k2 == 0)
                            strbuild.append( hmap.get(keysName[k2]) );
                        else
                            strbuild.append( _COL_DELIM + hmap.get(keysName[k2]) );
                    }
                    strbuild.append(_ROW_DELIM);
                }
            }
        }

        return strbuild.toString();
    }

    /**
     * input 데이타 row 수를 구한다.
     * @return rows
     */
    public int getRequestDataSize() {
        return _requestData.size();
    }
    /**
     * 그리드 데이타 row 수를 구한다.
     */
    public int getRequestGridSize() {
        return _requestGrid.size();
    }

    /**
     * input 데이타 row 수를 구한다.
     * @return rows
     */
    public int getResponseDataSize() {
        return _responseData.size();
    }

    /**
     * ArrayList(HashMap)형태로 저장된 input 데이타를 구한다.
     * @param index 행의 index
     * @param key 컬럼명
     * @return parameter 값
     */
    public String getRequestField(int index, String key) {
        LinkedHashMap<String, String> hmap = (LinkedHashMap<String, String>)_requestData.get(index);
        String val = hmap.get(key);
        return val;
    }

    public int getParameterRows() {
        if (_requestData == null)
            return 0;
        else
            return _requestData.size();
    }
    public int getGridParameterRows() {
        if (_requestGrid == null)
            return 0;
        else
            return _requestGrid.size();
    }

    /**
     * 요청전문으로 전달된 Parameter 값을 구한다.
     * @param key 컬럼명
     * @return parameter 값
     */
    public Object getParameterDefault(String key, Object DefaultValue) throws NullPointerException {
        Object objRet = null;
        String sValue = getParameter(key, 0);

        if (sValue == null) {
            objRet = DefaultValue;
        }
        else {
            if (DefaultValue instanceof Integer) {
                objRet = Integer.valueOf(sValue);
            }
            else if (DefaultValue instanceof Double) {
                objRet = Double.valueOf(sValue);
            }
            else {
                objRet = sValue; 
            }
        }
        return objRet;
    }

    public String getParameter(String key) throws NullPointerException {
        return getParameter(key, 0);
    }
    public String getParameter(String key, int index) throws NullPointerException {
        return getParameter(key, index, false);
    }
    public String getParameter(String key, boolean isNullCheck) throws NullPointerException {
        return getParameter(key, 0, isNullCheck);
    }
    public String getParameter(String key,int index, boolean isNullCheck) throws NullPointerException {
        String val = "";
        try {
            if (isNullCheck && _requestData.isEmpty())
                throw new NullPointerException("["+ key +"]에 해당하는 Parameter 정보를 얻을 수 없습니다");

            if (!_requestData.isEmpty()) {
                LinkedHashMap<String, String> map = (LinkedHashMap<String, String>)_requestData.get(index);
                val = map.get(key);
            }
        }
        catch (NullPointerException e) { }

        if (isNullCheck && val == null) {
            throw new NullPointerException("["+ key +"]에 해당하는 Parameter 정보를 얻을 수 없습니다");
        }

        return val;
    }

    public LinkedHashMap<String, String> getParameterMap() {
        return getParameterMap(0);
    }
    public LinkedHashMap<String, String> getParameterMap(int index) {
    	LinkedHashMap <String, String> mapRet = new LinkedHashMap <String, String>();
        String key = "";
        String value = "";

        if (_requestData.isEmpty())
            return mapRet;

        LinkedHashMap<String, String> mapData = (LinkedHashMap<String, String>)_requestData.get(index);
        Iterator <String> iter =  mapData.keySet().iterator();

        while (iter.hasNext()) {
            key = iter.next();
            value = mapData.get(key);
            mapRet.put(key, value);
            //logger.debug("\t"+ (i++) +" ["+ key +"]\t=> ["+ value +"]");
        }

        return mapRet;
    }

    /**
     * 요청전문으로 전단된 Parameter Key 값을 배열로 리턴한다.
     * @return 문자배열
     */
    public String[] getParameterCols() {
        return getParameterCols(null, 0);
    }
    public String[] getParameterCols(String sCol) {
        return getParameterCols(sCol, 0);
    }
    public String[] getParameterCols(String sCol, int index) {
        ArrayList<String> arrList = new ArrayList<String>();

        LinkedHashMap<String, String> mapData = (LinkedHashMap<String, String>)_requestData.get(index);
        String[] arrKeys =  mapData.keySet().toArray(new String[mapData.keySet().size()]);
        for (int ii = 0; ii < arrKeys.length; ii++) {
            if (sCol == null || sCol.length() < 1)
                arrList.add( arrKeys[ii] );
            else if (arrKeys[ii].startsWith(sCol))
                arrList.add( arrKeys[ii] );
        }
        return arrList.toArray(new String[arrList.size()]);
    }

    /* ↓↓↓↓↓  Grid Data 처리 추가 - 2012.12.20  ↓↓↓↓↓ */
    /**
     * 요청전문으로 전달된 Parameter 값 중 그리드를 구한다.
     * @param key 컬럼명
     * @return parameter 값
     */
    public String getGridParameter(String key) throws NullPointerException {
        return getGridParameter(key, 0);
    }
    public String getGridParameter(String key, int index) throws NullPointerException {
        return getGridParameter(key, index, false);
    }
    public String getGridParameter(String key, boolean isNullCheck) throws NullPointerException {
        return getGridParameter(key, 0, isNullCheck);
    }
    public String getGridParameter(String key,int index, boolean isNullCheck) throws NullPointerException {
        String val = "";
        try {
            if (isNullCheck && _requestGrid.isEmpty())
                throw new NullPointerException("["+ key +"]에 해당하는 Grid Parameter 정보를 얻을 수 없습니다");

            if (!_requestGrid.isEmpty()) {
                LinkedHashMap<String, String> map = (LinkedHashMap<String, String>)_requestGrid.get(index);
                val = map.get(key);
            }
        }
        catch (NullPointerException e) { }

        if (isNullCheck && val == null) {
            throw new NullPointerException("["+ key +"]에 해당하는 Grid Parameter 정보를 얻을 수 없습니다");
        }

        return val;
    }

    public LinkedHashMap<String, String> getGridParameterMap() {
        return getGridParameterMap(0);
    }
    public LinkedHashMap<String, String> getGridParameterMap(int index) {
        LinkedHashMap <String, String> mapRet = new LinkedHashMap <String, String>();
        String key = "";
        String value = "";

        if (_requestGrid.isEmpty())
            return mapRet;

        LinkedHashMap<String, String> mapData = (LinkedHashMap<String, String>)_requestGrid.get(index);
        Iterator <String> iter =  mapData.keySet().iterator();

        logger.debug("Map Grid Parameter Infomation (Task ID : "+ this.getTaskId() +" )");
        int i = 1;
        while (iter.hasNext()) {
            key = iter.next();
            value = mapData.get(key);
            mapRet.put(key, value);
            logger.debug("\t"+ (i++) +" ["+ key +"]\t=> ["+ value +"]");
        }

        return mapRet;
    }
    /* ↑↑↑↑↑  GRID Data 처리 추가 - 2012.12.20  ↑↑↑↑↑ */

    /**
     * 실행결과 데이터 ArrayList
     * @return rows
     */
    public ArrayList<LinkedHashMap<String, String>> getResponseData() {
        return _responseData;
    }

    /**
     * 출력된 ArrayList(HashMap)형태의 데이타를 FWTP DATA포맷으로 변환한다.
     * @return String
     */
    public String getResponseDataString() {
        // ResponseCode가 정상인지 체크 => 오류일 경우 오류메세지를 데이터로 반환
        if (!getResponseCode().equals(_RESPONSE_OKAY_)) {
            return getErrorMessage();
        }
        else {
            return makeResponseDataString();
        }
    }

    private String makeResponseDataString() {
        StringBuilder sb = new StringBuilder();

        if (_arrColumnName == null || _arrColumnName.length < 1)
            return sb.toString();

        // 1 LINE : field Name 정보
        //--makeResponseRow(sb, _arrColumnName, _COL_DELIM);
        makeResponseRowLower(sb, _arrColumnName, _COL_DELIM);
        sb.append(_ROW_DELIM);

        // 2 LINE : field Type 정보
        makeResponseRow(sb, _arrColumnType, _COL_DELIM);
        sb.append(_ROW_DELIM);

        // 3 LINE : field Size 정보
        makeResponseRow(sb, _arrColumnSize, _COL_DELIM);
        sb.append(_ROW_DELIM);

        // 데이터 출력
        for (int i=0; i<_responseData.size(); i++) {
            LinkedHashMap<String, String> hmap = (LinkedHashMap<String, String>)_responseData.get(i);

            for (int j=0; j<_arrColumnName.length; j++) {
                try {
                    sb.append(hmap.get(_arrColumnName[j]));
                    if (j < _arrColumnName.length-1) {
                        sb.append(_COL_DELIM);
                    }
                }
                catch(Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
            sb.append(_ROW_DELIM);
        }

        return sb.toString();
    }

    /**
     * 한 row 를 delim 으로 출력한다
     * @param sb StringBuilder
     * @param data 출력할 배열
     * @param delim 구분자
     */
    private void makeResponseRow(StringBuilder sb, String[] data, String delim) {
        if (data==null) return;

        for (int i=0; i<data.length; i++) {
            sb.append(data[i]);
            if (i < data.length-1) {
                sb.append(delim);
            }
        }
    }
    private void makeResponseRowLower(StringBuilder sb, String[] data, String delim) {
        if (data==null) return;

        for (int i=0; i<data.length; i++) {
            sb.append(data[i].toLowerCase());
            if (i < data.length-1) {
                sb.append(delim);
            }
        }
    }

    /**
     * FWTP COM 타입의 헤더를 설정한다.
     * @param arFields
     */
    public void setResponseFields(String[] arFields) {
        _arrColumnName = arFields;
    }

    /**
     * FWTP GRID 타입의 버츄얼테이블 필드정보를  설정한다.
     * @param field
     * @param arType
     * @param arSize
     */
    public void setResponseTableInfo(String[] field, String[] arType, String[] arSize) {
        _arrColumnName = field;
        _arrColumnType = arType;
        _arrColumnSize = arSize;
    }

    /**
     * ArrayList(HashMap)형태로 Response 데이터를 저장한다.
     * @param rowIdx
     * @param key
     * @param value
     * @throws Exception
     */
    public void setResponseField(int rowIdx, String key, String value)throws Exception {
        if (_arrColumnName == null) {
            throw new Exception("출력할 Field가 정의되지 않았습니다.");
        }

        if (rowIdx ==_responseData.size()) {
            _responseData.add(new LinkedHashMap<String, String>());
        }
        else if (rowIdx >_responseData.size()) {
            throw new Exception("잘못된 인덱스 지정입니다.");
        }

        if (value == null) value="";

        LinkedHashMap<String, String> hm = (LinkedHashMap<String, String>)_responseData.get(rowIdx);
        hm.put(key, value);
    }

    /**
     * 전문(Client) 요청에 대한 결과 수신값을 응답결과 리스트로 담는다.
     * @param arrList
     */
    public void setResponseResultList(ArrayList<LinkedHashMap<String, String>> arrList) {
        this._responseResultList = arrList;
    }
    public ArrayList<LinkedHashMap<String, String>> getResponseResultList() {
        return this._responseResultList;
    }
    public LinkedHashMap<String, String> getResponseResultData() {
        if (this._responseResultList == null || this._responseResultList.isEmpty())
            return null;
        else
            return (LinkedHashMap<String, String>)this._responseResultList.get(0);
    }
    /**
     * 응답결과 리스트의 Row(행) 수를 반환한다.
     * @return 응답행수
     */
    public int getResponseResultSize() {
        if (this._responseResultList == null)
            return 0;

        return this._responseResultList.size();
    }

    /*
     * Throw Class Name of BizService
     */
    public void setThrowClassName(String value) {
        this._throw_ClassName = value;
    }
    public String getThrowClassName() {
        return this._throw_ClassName;
    }
    /*
     * Task Class of BizService
     */
    public void setThrowLineNumber(String value) {
        this._throw_LineNumber = value;
    }
    public String getThrowLineNumber() {
        return this._throw_LineNumber;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}