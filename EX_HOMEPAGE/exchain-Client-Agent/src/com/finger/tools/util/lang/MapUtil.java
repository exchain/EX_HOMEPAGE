package com.finger.tools.util.lang;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class MapUtil {

    public static String getMapAsString(Map<String, Object> map, String key) {
        return StrUtil.nvl((String)map.get(key));
    }

    public static String getMapAsString(Map<String, Object> map, String key, String defaultStr) {
        return StrUtil.nvl((String)map.get(key), defaultStr);
    }

    public static int getMapAsInt(Map<String, Object> map, String key) {
        return getMapAsInt(map, key, 0);
    }

    public static int getMapAsInt(Map<String, Object> map, String key, int defaultInt) {

        Object valueObj = map.get(key);

        if (valueObj == null || valueObj.toString().equals("")) {
            return defaultInt;
        } else {
            return Integer.parseInt(valueObj.toString());
        }
    }

    public static double getMapAsDouble(Map<String, Object> map, String key) {
        return getMapAsDouble(map, key, 0.0);
    }

    public static double getMapAsDouble(Map<String, Object> map, String key, double defaultDouble) {

        Object valueObj = map.get(key);

        if (valueObj == null || valueObj.toString().equals("")) {
            return defaultDouble;
        } else {
            return Double.parseDouble(valueObj.toString());
        }
    }

	public static LinkedHashMap<String, String> getMapAsLowerCaseKey(LinkedHashMap<String, String> map) {
    	Set<String> keySet = map.keySet();
    	
    	String key = "";
    	String value = null;
    	
    	Iterator<String> i= keySet.iterator();
    	
    	LinkedHashMap<String, String> rstMap = new LinkedHashMap<String, String>();
    	
    	while(i.hasNext()) {
    		key = i.next();
    		value = map.get(key);
    		
    		rstMap.put(key.toLowerCase(), value);
    	}
    	
    	return rstMap;
    }
}