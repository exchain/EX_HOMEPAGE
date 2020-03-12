package com.finger.agent.scrap;

import java.util.ArrayList;

public class OutputData {

    private ArrayList<String> fields;

    public OutputData() {
         fields = new ArrayList<String>();
    }

    public void addOutputField(String fieldName) {
        fields.add(fieldName);
    }

    public ArrayList<String> getOutputField() {
        return fields;
    }
}