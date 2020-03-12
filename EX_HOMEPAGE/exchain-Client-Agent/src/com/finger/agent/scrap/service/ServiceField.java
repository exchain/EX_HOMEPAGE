package com.finger.agent.scrap.service;

public class ServiceField {

    private String  code; 
    private String  koName;
    private String  enName;
    private int     length;
    private boolean isNotNull;

    public ServiceField(String code, String koName, String enName, int length, boolean isNotNull) {

        this.code = code;
        this.koName = koName;
        this.enName = enName;
        this.length = length;
        this.isNotNull = isNotNull;
    }

    public String getCode() {
        return code;
    }

    public String getKoName() {
        return koName;
    }

    public String getEnName() {
        return enName;
    }

    public int getLength() {
        return length;
    }

    public boolean isNotNull() {
        return isNotNull;
    }
}