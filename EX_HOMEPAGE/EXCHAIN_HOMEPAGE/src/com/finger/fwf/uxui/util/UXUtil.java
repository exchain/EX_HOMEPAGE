package com.finger.fwf.uxui.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;
import java.lang.StringBuffer;

public class UXUtil {

    public static String nvl(String str) {
        return nvl(str, "");
    }
    public static String nvl(String str, String chg) {
        return isNull(str) ? chg : str;
    }

    public static boolean isNull(String str) {
        return (str == null || "".equals(str)) ? true : false ;
    }

    public static String replaceAll(String str, String chg) {
        return replaceAll(str, chg, "");
    }
    public static String replaceAll(String str, String org, String chg) {
        return nvl(str, "").replaceAll(org, chg);
    }

    public static String shortenString(String str, int len) {
        return shortenString(str, len, "...");
    }
    public static String shortenString(String str, int len, String tail) {
        if (str.length() <= len)
            return str;
        return str.substring(0, len) + (tail==null?"":tail);
    }

    public static LinkedHashMap<String, String[]> makeChartData(ArrayList<LinkedHashMap<String, String>> arrList) {
        LinkedHashMap<String, String[]> _result = new LinkedHashMap<String, String[]>();

        if (arrList != null && !arrList.isEmpty()) {
            LinkedHashMap<String, String> _hmap = arrList.get(0);
            int      nKeyCnt  = _hmap.keySet().size();
            String[] keysName = (String[]) _hmap.keySet().toArray(new String[nKeyCnt]);
            String[] _charts  = null;
            int      nRowCnt  = arrList.size();
            for (int kk=0; kk<nKeyCnt; kk++) {
                _charts = new String[nRowCnt];
                for (int ii=0; ii<nRowCnt; ii++) {
                    _charts[ii] = arrList.get(ii).get( keysName[kk] );
                }
                _result.put( keysName[kk], _charts );
            }
        }

        return _result;
    }
    
    public static String makeSessionkey() {
       StringBuffer sb = new StringBuffer();
       Random rnd = new Random();      
       
       for (int i = 0; i < 10; i++) {
    	   sb.append((char) ((int) (rnd.nextInt(26)) + 65));
       }
       return sb.toString();
    }
}