package com.finger.protocol.relay;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class RelayMessage implements Serializable {
    private static final long serialVersionUID = -6615265521006158162L;

    private static final String  _SUCCESS_  =   "0000";
    private static final String  _FAILURE_  =   "9999";

    private String  _dataLength     =   "0";
    private String  _relayString    =   "";
    private String  _responseCode   =   _SUCCESS_;
    private String  _responseString =   "";
    private String  _fileName       =   "";
    

    public String getOriginString() {
        return (_dataLength + _relayString);
    }

    public String getDataLength() {
        return _dataLength;
    }
    public void setDataLength(String value) {
        this._dataLength = value;
    }

    public String getRelayString() {
        return _relayString;
    }
    public void setRelayString(String value) {
        this._relayString = value;
    }

    public void setSuccess() {
        this._responseCode = _SUCCESS_;
    }
    public void setFailure() {
        this._responseCode = _FAILURE_;
    }
    public String getResponseCode() {
        return _responseCode;
    }
    /*public void setResponseCode(String value) {
        this._responseCode = value;
    }*/

    public String getResponseString() {
        return _responseString;
    }
    public void setResponseString(String value) {
        this._responseString = value;
    }

    public String getFileName() {
        return _fileName;
    }
    public void setFileName(String value) {
        this._fileName = value;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public static final int _Size_FileName_ =   30;
}