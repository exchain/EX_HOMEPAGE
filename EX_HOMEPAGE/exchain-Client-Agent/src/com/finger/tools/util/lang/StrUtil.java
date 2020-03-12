package com.finger.tools.util.lang;

public class StrUtil {

    public static boolean isNull(String str) {
        return (str == null || "".equals(str)) ? true : false ;
    }

    public static String nvl(String str) {
        return nvl(str, "");
    }
    public static String nvl(String str, String chg) {
        return isNull(str) ? chg : str;
    }

    public static String[] nvl(String[] strArray) {
        return nvl(strArray, "");
    }
    public static String[] nvl(String[] strArray, String chg) {
        return strArray == null ? new String[] {chg} : strArray;
    }

    public static String nvlTo(String str, String chg) {
        return nvl(str).equals("") ? chg : str;
    }

    public static String replace(String str, String org, String chg) {
        if (isNull(str))
            return str;

        return str.replaceAll(org, chg);
    }

    public static String delCr(String str) {
        return str.replaceAll("\r", "").replaceAll("\n", "").replaceAll(Character.toString((char)0), "");
    }

    public static String shortenString(String str, int len) {
        if (str.length() <= len)
            return str;

        return str.substring(0, len) +"...";
    }

    public static String subStringByte(String str, int start, int length) {
        byte[] strBytes = str.getBytes();
        return new String(strBytes, start, length);
    }

    /**
     * 입력된 갯수만큼의 스트링 원소를 갖는 배열을 만듬
     * FramePlus SQLManager.setColParam() 사용시 쓸데가 있음.
     * @param str
     * @param size
     * @return
     */
    public static String[] stringToArray(String str, int size) {
        String[] saRet = new String[size];
        for (int i = 0; i < size; i++) {
            saRet[i] = str;
        }

        return saRet;
    }
}