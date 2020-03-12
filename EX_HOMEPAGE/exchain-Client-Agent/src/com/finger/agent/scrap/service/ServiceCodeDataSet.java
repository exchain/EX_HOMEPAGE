package com.finger.agent.scrap.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ServiceCodeDataSet {

    private String workCode;
    private HashMap<?, ?> inFields;
    private HashMap<?, ?> outFields;
    private HashMap<?, ?> bizServices;
    
    public ServiceCodeDataSet(String strCode) {
        this.workCode = strCode;
    }
    
    public ServiceCodeDataSet(String strCode, HashMap<?, ?> inFields, HashMap<?, ?> outFields, HashMap<?, ?> bizServices) {
        this(strCode);
        this.inFields = inFields;
        this.outFields = outFields;
        this.bizServices = bizServices;
    }
    
    public String getWorkCode() {
        return workCode;
    }

    /*
     * input field 정의
     */
    public HashMap<?, ?> getInFields() {
        return outFields;
    }
    
    public void setInFields(HashMap<?, ?> inFields) {
        this.inFields = inFields;
    }
   
    public int getInFieldsLength() {
        return inFields.size();
    }

    /*
     * output field 정의
     */
    public HashMap<?, ?> getOutFields() {
        return outFields;
    }

    public void setOutFields(HashMap<?, ?> outFields) {
        this.outFields = outFields;
    }
   
    public int getOutFieldsLength() {
        return outFields.size();
    }

    /* biz service */
    public HashMap<?, ?> getBizServices() {
        return bizServices;
    }
    
    public void setBizServices(HashMap<?, ?> bizServices) {
        this.bizServices = bizServices;
    }
    
    public int getBizServicesLength() {
        return bizServices.size();
    }
    
    /**
     * 해당 필드 이름에 필드 길이를 구한다.
     * @param fieldname
     * @return
     */
    public int getInFieldLength(String fieldname) {
        return ((ServiceField)inFields.get(fieldname)).getLength();
    }

    /**
     * 출력 필드 이름에 필드 길이를 구한다.
     * @param fieldname
     * @return
     */
    public int getOutFieldLength(String fieldname) {
        return ((ServiceField)outFields.get(fieldname)).getLength();
    }
    
    
    /**
     * BizService bizService = (BizService)bizServices.get("04");
     */
    public BizService getBizService(String bizCode) {
        BizService bizService = (BizService)bizServices.get(bizCode);
        return bizService;
    }
    
    
    //필드 코드로  해당 되는 필드명을 찾는다.
    public String getFindFieldNameFormFieldCode(String fieldcode) {
        String fieldname = null;
        for (Iterator<?> i = outFields.keySet().iterator(); i.hasNext();) {
            String keyName = (String)i.next();
            ServiceField serviceField = (ServiceField)outFields.get(keyName);
            if ( serviceField.getCode().equals(fieldcode) ) {
                fieldname = serviceField.getEnName();
                break;
            }    
        }
        return fieldname;
    }
    
    //valify 
    public ArrayList<String> getNOTNullFields(String bizCode) {
        ArrayList<String> notnullFields = new ArrayList<String>();
        //필수 항목 필드를 찾는다. isNotNull=true
        for (Iterator<?> i = outFields.keySet().iterator(); i.hasNext();) {
            String keyName = (String)i.next();
            ServiceField serviceField = (ServiceField)outFields.get(keyName);
            if ( serviceField.isNotNull() )
                notnullFields.add( serviceField.getEnName() );
        }
        //bizCode은행별 옵션 항목을 찾는다.
        BizService bizService = (BizService)bizServices.get(bizCode);
        ArrayList<?> fieldCodes = bizService.getFieldCode();
        for (int i = 0; i < fieldCodes.size(); i++) {
            String fieldCode = (String)fieldCodes.get(i);
            String fieldname = getFindFieldNameFormFieldCode(fieldCode);
            if ( fieldname != null ) {
                if( !notnullFields.contains(fieldname) )
                    notnullFields.add(fieldname);
            }
        }
        return notnullFields;
    }
    
}
