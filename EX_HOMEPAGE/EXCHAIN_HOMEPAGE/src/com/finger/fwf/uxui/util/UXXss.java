package com.finger.fwf.uxui.util;

public class UXXss {

    // 보안취약점 XSS (Cross-Site Scripting) 방지
    public static String XSSFilter (String sInvalid)
    {
        if (sInvalid == null || sInvalid.equals("")) {
            return "";
        }

        String sValid = sInvalid.trim();

        sValid = sValid.replaceAll("&", "&amp;");
        sValid = sValid.replaceAll("<", "&lt;");
        sValid = sValid.replaceAll(">", "&gt;");
        sValid = sValid.replaceAll("\"", "&quot;");
        sValid = sValid.replaceAll("\'", "&#039;");
        sValid = sValid.replaceAll("\\n", "¶");
        sValid = sValid.replaceAll("\\r", "§");

        return sValid;
    }

    // 보안취약점 Path Manipulation (Cross-Site Scripting) 방지
    public static String HttpResponseFilter (String sInvalid)
    {
        if (sInvalid == null || sInvalid.equals("")) {
            return "";
        }

        String sValid = sInvalid.trim();

        sValid = sValid.replaceAll("\\n", "¶");
        sValid = sValid.replaceAll("\\r", "§");

        return sValid;
    }
}