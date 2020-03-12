package com.finger.agent.scrap.entity;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * <p>DTO entity 객체.</p>
 *
 * @author   Kang Dong Youn (kkjava@korea.com)
 * @author   <a href='mailto:kkjava@korea.com'>Finkle</a>
 * @version  1.0
 */
public class Entity extends LinkedHashMap<String, Object> implements Serializable {
    private static final long serialVersionUID = 775526020590928559L;

    /** ResultSet 에서 컬럼 이름을 key로 해서 그 값을 Entity 에 저장 */
    public void parseResultSet(ResultSet rs) throws SQLException {

        ResultSetMetaData md = rs.getMetaData();
        int size = md.getColumnCount();
        String columnName = null;
        for (int i = 1; i <= size; i++) {
            columnName = (md.getColumnName(i)).toLowerCase();
            if (columnName != null) {
                setValue(columnName, rs.getString(i));
            }
        }
    }

    /** ResultSet 에서 컬럼 이름과 인덱스를 key로 해서 그 값을 Entity 에 저장 */
    public void parseResultSet(int iIndex, ResultSet rs) throws SQLException {

        ResultSetMetaData md = rs.getMetaData();
        int size = md.getColumnCount();

        for (int i = 1; i <= size; i++) {
            setValue(
                new StringBuffer((md.getColumnName(i)).toLowerCase()).append(iIndex).toString(), rs.getString(i)
            );
        }
    }


    public void setValue(String sKey, String sValue) {
        if (sValue != null)
            put(sKey, sValue);
    }

    public void setValue(String sKey, String sValues[]) {
        put(sKey, sValues);
    }

    public void setValue(String sKey, byte[] yValues) {
        String sValue = null;

        if (yValues != null)
            sValue = new String(yValues);

        put(sKey, sValue);
    }

    public void setValue(String sKey, byte yValue) {
        put(sKey, Byte.toString(yValue));
    }

    public void setValue(String sKey, char[] cValues) {
        String sValue = null;

        if (cValues != null)
            sValue = new String(cValues);

        put(sKey, sValue);
    }

    public void setValue(String sKey, char cValue) {
        put(sKey, String.valueOf(cValue));
    }

    public void setValue(String sKey, float fValue) {
        put(sKey, String.valueOf(fValue));
    }

    public void setValue(String sKey, boolean bValue) {
        put(sKey, String.valueOf(bValue));
    }

    public void setValue(String sKey, short tValue) {
        put(sKey, String.valueOf(tValue));
    }

    public void setValue(String sKey, int iValue) {
        put(sKey, String.valueOf(iValue));
    }

    public void setValue(String sKey, long lValue) {
        put(sKey, String.valueOf(lValue));
    }

    public void setValue(String sKey, double dValue) {
        put(sKey, String.valueOf(dValue));
    }

    public void setValue(String sKey, java.util.Date value) {

        String sValue = null;

        if (value != null);
            sValue = value.toString();

        put(sKey, sValue);
    }

    public void setValue(String sKey, ArrayList<String> value) {
        put(sKey, value);
    }

    public void setValue(String sKey, Entity value) {
        put(sKey, value);
    }

    public void setValue(String sKey, HashMap<String, Object> value) {
        put(sKey, value);
    }

    /* 2003-07-16 */
    public void setValue(String sKey, Object value) {
        put(sKey, value);
    }

    public String getTrimString(String sKey) {

        String sValue = "";
        Object obj = null;

        try {
            obj = get(sKey);
            if (obj instanceof String) {
                sValue = ((String) obj).trim();
            }
            else if (obj instanceof String[]) {
                sValue = ((String[]) obj)[0].trim();
            }
            else {
                sValue = "";
            }
        }
        catch (Exception e) {
            sValue = "";
        }

        return sValue;
    }

    public String getString(String sKey) {

        String sValue = "";
        Object obj = null;

        try {
            obj = get(sKey);
            if (obj instanceof String) {
                sValue = ((String) obj);
            }
            else if (obj instanceof String[]) {
                sValue = ((String[]) obj)[0];
            }
            else {
                sValue = "";
            }
        }
        catch (Exception e) {
            sValue = "";
        }

        return sValue;
    }


    /**
     *  Entity 에 저장된 String[] 를 return 하는 method.
     *  String 이 저장되어 있을 경우에는 length 가 1인 String[] 를 return.
     */
    public String[] getStrings(String sKey) {

        String sValues[] = null;
        Object obj = null;

        try {
            obj = get(sKey);
            if (obj instanceof String) {
                sValues = new String[1];
                sValues[0] = (String) obj;
            }
            else {
                sValues = (String[])obj;
            }
        }
        catch (Exception e) {
        }

        return sValues;
    }

    public byte getByte(String sKey) {

        byte yResult = (byte)0;

        try {
            yResult = Byte.parseByte((String)get(sKey));
        }
        catch (Exception e) {
        }

        return yResult;
    }

    public byte[] getBytes(String sKey) {

        byte[] yResults = null;

        try {
            yResults = ((String)get(sKey)).getBytes();
        }
        catch (Exception e) {
        }

        return yResults;
    }

    public char getChar(String sKey) {

        char cResult = (char)0;

        try {
            cResult = ((String)get(sKey)).charAt(0);
        }
        catch (Exception e) {
        }

        return cResult;
    }

    public char[] getChars(String sKey) {

        char[] cResults = null;

        try {
            cResults = ((String)get(sKey)).toCharArray();
        }
        catch (Exception e) {
        }

        return cResults;
    }

    public float getFloat(String sKey) {

        float fResult = 0;

        try {
            fResult = Float.parseFloat((String)get(sKey));
        }
        catch (Exception e) {
        }

        return fResult;
    }

    public boolean getBoolean(String sKey) {

        boolean bResult = false;

        try {
            String sVal = (String)get(sKey);

            if (sVal.equals("true")) {
                bResult = true;
            }
            else if (sVal.equals("false")) {
                bResult = false;
            }
            else {
                bResult = Boolean.getBoolean(sVal);
            }
        }
        catch (Exception e) {
        }

        return bResult;
    }

    public short getShort(String sKey) {

        short tResult = 0;

        try {
            tResult = Short.parseShort((String)get(sKey));
        }
        catch (Exception e) {
        }

        return tResult;
    }

    public int getInt(String sKey) {

        int iResult = 0;

        try {
            iResult = Integer.parseInt((String)get(sKey));
        }
        catch (Exception e) {
        }

        return iResult;
    }

    public long getLong(String sKey) {

        long lResult = 0;

        try {
            lResult = Long.parseLong((String)get(sKey));
        }
        catch (Exception e) {
        }

        return lResult;
    }

    public double getDouble(String sKey) {

        double dResult = 0;

        try {
            dResult = Double.parseDouble((String)get(sKey));
        }
        catch (Exception e) {
        }

        return dResult;
    }

    public java.util.Date getDate(String sKey) {

        java.util.Date result = null;

        try {
            String sDate = (String)get(sKey);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            ParsePosition pos = new ParsePosition(0);
            result = formatter.parse(sDate, pos);
        }
        catch (Exception e) {
        }

        return result;
    }

    public Vector<?> getVector(String sKey) {

        Vector<?> vResult = null;

        try {
            vResult = (Vector<?>)get(sKey);
            if (vResult == null)
                vResult = new Vector<Object>();
        }
        catch (Exception e) {
            vResult = new Vector<Object>();
        }

        return vResult;
    }

    public ArrayList<?> getArrayList(String sKey) {

        ArrayList<?> alResult = null;

        try {
            alResult = (ArrayList<?>)get(sKey);
            if (alResult == null)
                alResult = new ArrayList<Object>();
        }
        catch (Exception e) {
            alResult = new ArrayList<Object>();
        }

        return alResult;
    }


    public Hashtable<?, ?> getHashtable(String sKey) {

        Hashtable<?, ?> value = null;

        try {
            value = (Hashtable<?, ?>)get(sKey);
            if (value == null)
                value = new Hashtable<Object, Object>();
        }
        catch (Exception e) {
            value = new Hashtable<Object, Object>();
        }

        return value;
    }

    public Entity getEntity(String sKey) {

        Entity value = null;

        try {
            value = (Entity)get(sKey);
            if (value == null)
                value = new Entity();
        }
        catch (Exception e) {
            value = new Entity();
        }

        return value;
    }

    public HashMap<?, ?> getHashMap(String sKey) {

        HashMap<?, ?> value = null;

        try {
            value = (HashMap<?, ?>)get(sKey);
            if (value == null)
                value = new HashMap<Object, Object>();
        }
        catch (Exception e) {
            value = new HashMap<Object, Object>();
        }

        return value;
    }


    public Object getObject(String sKey) {

        Object value = null;

        try {
            value = get(sKey);
            if (value == null)
                value = new Object();
        }
        catch (Exception e) {
            value = new Object();
        }

        return value;
    }

    public Object remove(Object sKey) {
        HashMap<String, Object> hm = this;
        Entity rtn = (Entity)hm.remove(sKey);
        return rtn;
    }


    @SuppressWarnings("rawtypes")
	public String getKey(String sValue) {

        String sResult = null;

        Set<?> keySet = entrySet();
        Object lists[] = keySet.toArray();

        String sKey = null;
        Object value = null;
        for (int i = 0; i < lists.length; i++) {
            sKey = (String)(((Map.Entry)lists[i]).getKey());
            value = get(sKey);
            if (value instanceof String && ((String)value).trim().equals(sValue)) {
                sResult = (String)sKey;
                break;
            }
        }

        return sResult;
    }

    @SuppressWarnings("rawtypes")
	public String getKey(String sValue, String sKeyPrefix) {

        String sResult = null;

        Set<?> keySet = entrySet();
        Object lists[] = keySet.toArray();

        String sKey = null;
        Object value = null;

        for (int i = 0; i < lists.length; i++) {
            sKey = (String)(((Map.Entry)lists[i]).getKey());
            value = get(sKey);
            if (sKey.startsWith(sKeyPrefix) &&
                value instanceof String &&
                ((String)value).trim().equals(sValue)
            ) {
                sResult = (String)sKey;
                break;
            }
        }

        return sResult;
    }

    public ArrayList<Object> getStringKeys() {
        /*int count = 0;*/
        HashMap<String, Object> hm = this;
        ArrayList<Object> keys = new ArrayList<Object>();
        for (Iterator<String> i = hm.keySet().iterator();i.hasNext();) {
            Object key = i.next();
            Object value = (Object)hm.get(key);
            if(value instanceof String) {
                keys.add(key);
                /*count++;*/
            }
        }

        return keys;
    }

    public ArrayList<Object> getKeys() {
        /*int count = 0;*/
        HashMap<String, Object> hm = this;
        ArrayList<Object> keys = new ArrayList<Object>();
        for (Iterator<String> i = hm.keySet().iterator();i.hasNext();) {
            Object key = i.next();
            keys.add(key);
            /*count++;*/
        }

        return keys;
    }

    public ArrayList<Object> getValues() {
        /*int count = 0;*/
        HashMap<String, Object> hm = this;
        ArrayList<Object> values = new ArrayList<Object>();
        ArrayList<Object> keys = getKeys();
        for (int i=0; i < keys.size(); i++) {
            Object value = (Object)hm.get(keys.get(i));
            values.add(value);
            /*count++;*/
        }

        return values;
    }

    public ArrayList<Object> getStringKeys(HashMap<String, Object> hm) {
        /*int count = 0;*/
        ArrayList<Object> keys = new ArrayList<Object>();
        for (Iterator<String> i = hm.keySet().iterator();i.hasNext();) {
            Object key = i.next();
            if(key instanceof String) {
                keys.add(key);
                /*count++;*/
            }
        }

        return keys;
    }

    public ArrayList<Object> getStringValues() {
        /*int count = 0;*/
        HashMap<String, Object> hm = this;
        ArrayList<Object> values = new ArrayList<Object>();
        ArrayList<Object> keys = getStringKeys(hm);
        for (int i=0; i < keys.size(); i++) {
            Object value = (Object)hm.get(keys.get(i));
            if(value instanceof String) {
                values.add(value);
                /*count++;*/
            }
        }

        return values;
    }

}
