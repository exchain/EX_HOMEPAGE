package com.finger.fwf.uxui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.finger.fwf.uxui.util.UXUtil;

public class CommonContents {

    /** CONTENTS(다국어화면)정보 */
    private static  ArrayList<LinkedHashMap<String, String>> _arr_common_contents = null;

    public static  LinkedHashMap<String, HashMap<String, String>> _m_common_contents = null;

    public CommonContents() { }

    /* ===  CONTENTS(다국어화면)정보  === */
    public static ArrayList<LinkedHashMap<String, String>> getCommonContentsArr() {
        return _arr_common_contents;
    }
    public static void setCommonContentsArr(ArrayList<LinkedHashMap<String, String>> arrayList) {
        _arr_common_contents = arrayList;

        makeCommonContents();
    }

    /**
     * CONTENTS(다국어화면) 화면용 생성
     * @param lang
     */
    private static void makeCommonContents() {
        _m_common_contents = new LinkedHashMap<String, HashMap<String, String>>();

        if (_arr_common_contents != null) {
            for (int ii = 0; ii < _arr_common_contents.size(); ii++) {
                LinkedHashMap<String, String> contentMap = _arr_common_contents.get(ii);
                if (!(contentMap == null || contentMap.isEmpty())) {
                    String ctt_key = contentMap.get("ctt_key");
                    String ctt_lang = contentMap.get("ctt_lang");
                    String ctt_text = contentMap.get("ctt_text");
                    String ctt_kind = UXUtil.nvl(contentMap.get("ctt_kind"));

                    HashMap<String, String> hmap = _m_common_contents.get( ctt_key );
                    if (hmap == null || hmap.isEmpty()) {
                        hmap = new HashMap<String, String>();
                    }
                    //hmap.put( ctt_lang  , UXXss.XSSFilter( ctt_text ) );
                    hmap.put( ctt_lang  , ctt_text ); // 메세지창에서 <br/>태그를 사용하므로 필터 제외(07.31)
                    hmap.put( _ctt_kind_, ctt_kind );

                    _m_common_contents.put( ctt_key, hmap );
                }
            }
        }
    }

    /**
     * CONTENTS(다국어화면) 키값 및 언어에 해당하는 TEXT 를 구한다.
     * @param cttKey
     * @param cttLang
     * @return
     */
    public static String getCommonContent(String cttKey, String cttLang) {
        String content = "";
        if (!(_m_common_contents == null || _m_common_contents.isEmpty())) {
            try {
                content = _m_common_contents.get(cttKey).get(cttLang);
            } catch(Exception ign) { }
        }
        return UXUtil.nvl( content, cttKey );
    }
    public static String getCommonContentKind(String cttKey) {
        String content = "";
        if (!(_m_common_contents == null || _m_common_contents.isEmpty())) {
            try {
                content = _m_common_contents.get(cttKey).get(_ctt_kind_);
            } catch(Exception ign) { }
        }
        return UXUtil.nvl( content, "~KIND~" );
    }
    private static final String _ctt_kind_ = "cttKind";
}