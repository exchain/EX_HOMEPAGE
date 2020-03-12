package com.finger.agent.scrap;

import java.util.HashMap;

import com.finger.agent.scrap.entity.Entity;

public class InputData {
    private HashMap<String, Object> fields;

    public InputData() {
         fields = new HashMap<String, Object>();
    }

    public String getInputField(String fieldname) {
        String value = (String)fields.get(fieldname);
        if (value == null)
            return "";
        else
            return value;
    }

    public Object getInputFields(String fieldname) {
        return fields.get(fieldname);
    }

    public void setInputField(String fieldname, String value) {
        fields.put(fieldname, value);
    }

    public void setInputField(String fieldname, Object value) {
        fields.put(fieldname, value);
    }

    public void setInputField(Entity entity) {
        fields = entity;
    }

    public int getSize() {
        return fields.size();
    }

    public HashMap<String, Object> getFields() {
        return fields;
    }

    public Object remove(String name) {
        return fields.remove(name);
    }
}