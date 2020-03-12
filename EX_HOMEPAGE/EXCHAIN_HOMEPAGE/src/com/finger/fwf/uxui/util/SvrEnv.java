package com.finger.fwf.uxui.util;

import javax.servlet.http.HttpServletRequest;

import com.finger.fwf.uxui.CommonCarts;

public class SvrEnv {

    private static final String     _is_real_server     =   "is_real_server";

    public static boolean isRealServer(HttpServletRequest request) {
        return Boolean.parseBoolean( CommonCarts.getServerEnvi().get(_is_real_server) );
    }
    
    public static String getDB_Driver() {
        return String.valueOf(CommonCarts.getServerEnvi().get("db_driver"));
    }

    public static String getDB_Url() {
        return String.valueOf(CommonCarts.getServerEnvi().get("db_url"));
    }

    public static String getDB_Username() {
        return String.valueOf(CommonCarts.getServerEnvi().get("db_username"));
    }

    public static String getDB_Password() {
        return String.valueOf(CommonCarts.getServerEnvi().get("db_password"));
    }
    
}