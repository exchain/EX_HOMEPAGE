package com.finger.agent.scrap.service;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class BizService {

    private String bizName;
    private String bizCode;
    private String fieldCode;
    private ArrayList<String> fields = new ArrayList<String>();
    
    public BizService(String bzName, String bizCode, String fieldCode) {
        this.bizName = bzName;
        this.bizCode = bizCode;
        this.fieldCode = fieldCode;
    }
    
    public String getBizName() {
        return bizName;
    }
    
    public String getBizCode() {
        return bizCode;
    }
    
    public ArrayList<String> getFieldCode() {
        StringTokenizer token = new StringTokenizer(fieldCode, ",");
        while (token.hasMoreTokens()) 
            fields.add(token.nextToken());

        return fields;  
    }
}