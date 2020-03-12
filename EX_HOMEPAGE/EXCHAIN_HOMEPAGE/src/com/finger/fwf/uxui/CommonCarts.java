package com.finger.fwf.uxui;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class CommonCarts {

    /** SERVER ENVIRONMENT */
    private static  LinkedHashMap<String, String> _m_server_envi = null;

    /** 사용자정보 */
    private static  ArrayList<LinkedHashMap<String, String>> _m_user_info = null;

    /** 시스템정보 */
    private static  ArrayList<LinkedHashMap<String, String>> _m_system_info = null;

    /** 법인정보 */
    private static  ArrayList<LinkedHashMap<String, String>> _m_crpn_info = null;

    /** 사업장정보 */
    private static  ArrayList<LinkedHashMap<String, String>> _m_bizr_info = null;

    /** 공통코드(전체) */
    private static  ArrayList<LinkedHashMap<String, String>> _m_common_code = null;

    /** SelectBox개발자정의용(모음) */
    private static  ArrayList<LinkedHashMap<String, String>> _m_selectbox_5000 = null;
    
    /** 거래현황 */
    private static  ArrayList<LinkedHashMap<String, String>> _m_tran_list_info = null;
    
    /** 코인별 판매목록조회 */ 
    private static  ArrayList<LinkedHashMap<String, String>> _m_bos_sell_list_info 	= null; // BOS
    private static  ArrayList<LinkedHashMap<String, String>> _m_eth_sell_list_info 	= null; // ETH  
    private static  LinkedHashMap<String, ArrayList<LinkedHashMap<String, String>>> _m_erc20_sell_list_info = new LinkedHashMap<String, ArrayList<LinkedHashMap<String, String>>>(); 
 
    /** 챠트조회 일별 */
    private static  ArrayList<LinkedHashMap<String, String>> _m_bos_chart_list_info = null; // BOS
    private static  ArrayList<LinkedHashMap<String, String>> _m_eth_chart_list_info = null; // ETH
    private static  LinkedHashMap<String, ArrayList<LinkedHashMap<String, String>>> _m_erc20_chart_list_info = new LinkedHashMap<String, ArrayList<LinkedHashMap<String, String>>>(); 
    
    /** 챠트조회 시간별 */
    private static  ArrayList<LinkedHashMap<String, String>> _m_time_chart_list_info = null;

    public CommonCarts() { }

    /* ===  SERVER ENVIRONMENT  === */
    public static LinkedHashMap<String, String> getServerEnvi() {
        return _m_server_envi;
    }
    public static void setServerEnvi(LinkedHashMap<String, String> hashMap) {
        _m_server_envi = hashMap;
    }

    /* ===  사용자정보  === */
    public static ArrayList<LinkedHashMap<String, String>> getUserInfo() {
        return _m_user_info;
    }
    public static void setUserInfo(ArrayList<LinkedHashMap<String, String>> arrayList) {
    	_m_user_info = arrayList;
    }
    
    /* ===  시스템정보  === */
    public static ArrayList<LinkedHashMap<String, String>> getSystemInfo() {
        return _m_system_info;
    }
    public static void setSystemInfo(ArrayList<LinkedHashMap<String, String>> arrayList) {
    	_m_system_info = arrayList;
    }

    /* ===  법인정보  === */
    public static ArrayList<LinkedHashMap<String, String>> getCrpnInfo() {
        return _m_crpn_info;
    }
    public static void setCrpnInfo(ArrayList<LinkedHashMap<String, String>> arrayList) {
    	_m_crpn_info = arrayList;
    }

    /* ===  사업장정보  === */
    public static ArrayList<LinkedHashMap<String, String>> getBizrInfo() {
        return _m_bizr_info;
    }
    public static void setBizrInfo(ArrayList<LinkedHashMap<String, String>> arrayList) {
    	_m_bizr_info = arrayList;
    }

    /* ===  공통코드(전체)  === */
    public static ArrayList<LinkedHashMap<String, String>> getCommonCode() {
        return _m_common_code;
    }
    public static void setCommonCode(ArrayList<LinkedHashMap<String, String>> arrayList) {
        _m_common_code = arrayList;
    }

    /* ===  SelectBox개발자정의용(모음)  === */
    public static ArrayList<LinkedHashMap<String, String>> getSelectbox5000() {
        return _m_selectbox_5000;
    }
    public static void setSelectbox5000(ArrayList<LinkedHashMap<String, String>> arrayList) {
        _m_selectbox_5000 = arrayList;
    }
    
    
        
    /* === 거래현황 === */
    public static ArrayList<LinkedHashMap<String, String>> getTranListInfo() {
        return _m_tran_list_info;
    }
    public static void setTranListInfo(ArrayList<LinkedHashMap<String, String>> arrayList) {
    	_m_tran_list_info = new ArrayList<LinkedHashMap<String, String>>();
    	_m_tran_list_info = arrayList;
    }

    /* === 판매목록조회 === */
    public static ArrayList<LinkedHashMap<String, String>> getBosSellListInfo() {
        return _m_bos_sell_list_info;
    }
    public static void setBosSellListInfo(ArrayList<LinkedHashMap<String, String>> arrayList) {
    	_m_bos_sell_list_info = new ArrayList<LinkedHashMap<String, String>>();
    	_m_bos_sell_list_info = arrayList;
    }
    
    public static ArrayList<LinkedHashMap<String, String>> getEthSellListInfo() {
        return _m_eth_sell_list_info;
    }
    public static void setEthSellListInfo(ArrayList<LinkedHashMap<String, String>> arrayList) {
    	_m_eth_sell_list_info = new ArrayList<LinkedHashMap<String, String>>();
    	_m_eth_sell_list_info = arrayList;
    }
    
    public static ArrayList<LinkedHashMap<String, String>> getERC20SellListInfo(String coinName) {
   		return _m_erc20_sell_list_info.get(coinName);
    }
    
    public static void setERC20SellListInfo(ArrayList<LinkedHashMap<String, String>> arrayList,  String coinName) {
    	_m_erc20_sell_list_info.put(coinName, arrayList);
    }
    
    /* === 판매목록조회 끝=== */
    
    /* ===일별 챠트조회 === */
    public static ArrayList<LinkedHashMap<String, String>> getBosChartListInfo() {
        return _m_bos_chart_list_info;
    }
    public static void setBosChartListInfo(ArrayList<LinkedHashMap<String, String>> arrayList) {
    	_m_bos_chart_list_info = new ArrayList<LinkedHashMap<String, String>>();
    	_m_bos_chart_list_info = arrayList;
    }
    
    public static ArrayList<LinkedHashMap<String, String>> getEthChartListInfo() {
        return _m_eth_chart_list_info;
    }
    public static void setEthChartListInfo(ArrayList<LinkedHashMap<String, String>> arrayList) {
    	_m_eth_chart_list_info = new ArrayList<LinkedHashMap<String, String>>();
    	_m_eth_chart_list_info = arrayList;
    }
    
    public static ArrayList<LinkedHashMap<String, String>> getERC20ChartListInfo(String coinName) {
    	return _m_erc20_chart_list_info.get(coinName);
    }
    public static void setERC20ChartListInfo(ArrayList<LinkedHashMap<String, String>> arrayList , String coinName) {   	
    	_m_erc20_chart_list_info.put(coinName, arrayList);
    }
    
    /* ===일별 챠트조회 끝=== */
    
    /* ===시간별 챠트조회 === */
    public static ArrayList<LinkedHashMap<String, String>> getTimeChartListInfo() {
    	return _m_time_chart_list_info;
    }
    public static void setTimeChartListInfo(ArrayList<LinkedHashMap<String, String>> arrayList) {
    	_m_time_chart_list_info = new ArrayList<LinkedHashMap<String, String>>();
    	_m_time_chart_list_info = arrayList;
    }

    
}