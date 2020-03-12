package com.finger.fwf.uxui.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import com.finger.agent.exception.AgentException;
import com.finger.fwf.uxui.CommonCarts;

public class MarketLoad {

    /**
     * 시스템정보
     */
    public static LinkedHashMap<String, Object> getSystemInfo(HttpServletRequest request) {
        ArrayList<LinkedHashMap<String, String>> _m_system_info = CommonCarts.getSystemInfo();

        String _default = "";
        ArrayList<LinkedHashMap<String, String>> _arrList = new ArrayList<LinkedHashMap<String, String>>();
        for (int ii = 0; ii < _m_system_info.size(); ii++) {
            LinkedHashMap<String, String> preMap = _m_system_info.get(ii);

            LinkedHashMap<String, String> _hmap = new LinkedHashMap<String, String>();
            _hmap.put("value", preMap.get("sys_mgt_no") );
            _hmap.put("text" , UCS.Cont(UXUtil.nvl(preMap.get("sys_mgt_nm_key")), UCS.getULang(request)) );
            _arrList.add( _hmap );
        }
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
        resultMap.put("options"             , _arrList );
        resultMap.put("defaultSelectedValue", _default );

        return resultMap;
    }
    /**
     * 법인정보
     * @throws AgentException 
     */
    public static LinkedHashMap<String, Object> getCrpnInfo(HttpServletRequest request) throws AgentException {
        ArrayList<LinkedHashMap<String, String>> _m_crpn_info = CommonCarts.getCrpnInfo();
        String sys_mgt_no = UXUtil.nvl(request.getParameter("sys_mgt_no"));

        if (UXUtil.isNull(sys_mgt_no))
            throw new AgentException("시스템관리번호 입력이 필요합니다.<br/>시스템담장자에게 문의 바랍니다.");

        String _default = "";
        ArrayList<LinkedHashMap<String, String>> _arrList = new ArrayList<LinkedHashMap<String, String>>();
        for (int ii = 0; ii < _m_crpn_info.size(); ii++) {
            LinkedHashMap<String, String> preMap = _m_crpn_info.get(ii);
            LinkedHashMap<String, String> _hmap = new LinkedHashMap<String, String>();
            if (sys_mgt_no.equals(preMap.get("sys_mgt_no"))) {
                _hmap.put("value", preMap.get("crpn_seq_no") );
                _hmap.put("text" , UCS.Cont(UXUtil.nvl(preMap.get("smark_crpn_nm_key")), UCS.getULang(request)) );
                _arrList.add( _hmap );
            }
        }
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
        resultMap.put("options"             , _arrList );
        resultMap.put("defaultSelectedValue", _default );

        return resultMap;
    }
    /**
     * 사업장정보
     * @throws AgentException 
     */
    public static LinkedHashMap<String, Object> getBizrInfo(HttpServletRequest request) throws AgentException {
        ArrayList<LinkedHashMap<String, String>> _m_bizr_info = CommonCarts.getBizrInfo();
        String sys_mgt_no  = UXUtil.nvl(request.getParameter("sys_mgt_no"));
        String crpn_seq_no = UXUtil.nvl(request.getParameter("crpn_seq_no"));
        String bizr_div    = UXUtil.nvl(request.getParameter("bizr_div"));

        if (UXUtil.isNull(sys_mgt_no))
            throw new AgentException("시스템관리번호 입력이 필요합니다.<br/>시스템담장자에게 문의 바랍니다.");

        String _default = "";
        ArrayList<LinkedHashMap<String, String>> _arrList = new ArrayList<LinkedHashMap<String, String>>();
        for (int ii = 0; ii < _m_bizr_info.size(); ii++) {
            LinkedHashMap<String, String> preMap = _m_bizr_info.get(ii);
            LinkedHashMap<String, String> _hmap = new LinkedHashMap<String, String>();
            if (UXUtil.isNull(crpn_seq_no)) {
                /*_hmap.put("value", preMap.get("biz_seq_no") );
                _hmap.put("text" , preMap.get("smark_biz_nm") );
                _arrList.add( _hmap );*/
                throw new AgentException("법인일련번호/ALL 입력이 필요합니다.");
            } else
            if (sys_mgt_no.equals(preMap.get("sys_mgt_no"))) {
                if (crpn_seq_no.equals(preMap.get("crpn_seq_no")) || "ALL".equals(crpn_seq_no)) {
                    if (UXUtil.isNull(bizr_div) || (!UXUtil.isNull(bizr_div) && bizr_div.equals(preMap.get("bizr_div")))) {
                        _hmap.put("value", preMap.get("biz_seq_no") );
                        _hmap.put("text" , UCS.Cont(UXUtil.nvl(preMap.get("smark_biz_nm_key")), UCS.getULang(request)) );
                        _arrList.add( _hmap );
                    }
                }
            }
        }
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
        resultMap.put("options"             , _arrList );
        resultMap.put("defaultSelectedValue", _default );

        return resultMap;
    }
    /**
     * 공통코드
     */
    public static LinkedHashMap<String, Object> getCommonCode(HttpServletRequest request) {
        ArrayList<LinkedHashMap<String, String>> _m_common_code = CommonCarts.getCommonCode();
        String _grp_cd = UXUtil.nvl(request.getParameter("grp_cd"));
        String _attr_1 = UXUtil.nvl(request.getParameter("attr_1_cd"));
        String _attr_2 = UXUtil.nvl(request.getParameter("attr_2_cd"));
        String _attr_3 = UXUtil.nvl(request.getParameter("attr_3_cd"));

        ArrayList<LinkedHashMap<String, String>> _arrList = new ArrayList<LinkedHashMap<String, String>>();
        String _default = "";
        for (int ii = 0; ii < _m_common_code.size(); ii++) {
            LinkedHashMap<String, String> preMap = _m_common_code.get(ii);
            if (_grp_cd.equals(preMap.get("grp_cd"))) {
                boolean isSetting = true;
                if ("Y".equals(preMap.get("use_yn"))) {
                    if (!UXUtil.isNull(_attr_1) && !_attr_1.equals(preMap.get("attr_1_cd"))) isSetting = false;
                    if (!UXUtil.isNull(_attr_2) && !_attr_2.equals(preMap.get("attr_2_cd"))) isSetting = false;
                    if (!UXUtil.isNull(_attr_3) && !_attr_3.equals(preMap.get("attr_3_cd"))) isSetting = false;

                    if ( isSetting ) {
                        LinkedHashMap<String, String> _hmap = new LinkedHashMap<String, String>();
                        _hmap.put("value", preMap.get("com_cd") );
                        _hmap.put("text" , UCS.Cont(UXUtil.nvl(preMap.get("com_cd_nm_key")), UCS.getULang(request)) );
                        _arrList.add( _hmap );

                        if ("Y".equals(preMap.get("default_yn"))) _default = preMap.get("com_cd");
                    }
                }
            }
        }
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
        resultMap.put("options"             , _arrList );
        resultMap.put("defaultSelectedValue", _default );

        return resultMap;
    }

    /**
     * SelectBox 개발자정의용 - 로그인무관
     */
    public static LinkedHashMap<String, Object> getSelectbox5000(HttpServletRequest request) {
        ArrayList<LinkedHashMap<String, String>> _m_common_code = CommonCarts.getSelectbox5000();
        String _sboxGroup = UXUtil.nvl(request.getParameter("sboxGroup"));
        String sys_mgt_no = UXUtil.nvl(request.getParameter("sys_mgt_no"));

        ArrayList<LinkedHashMap<String, String>> _arrList = new ArrayList<LinkedHashMap<String, String>>();
        String _default = "";
        for (int ii = 0; ii < _m_common_code.size(); ii++) {
            LinkedHashMap<String, String> preMap = _m_common_code.get(ii);
            if (_sboxGroup.equals(preMap.get("group_name"))) {
                if (sys_mgt_no.equals(preMap.get("sys_mgt_no"))) {
                    String value = UXUtil.nvl(preMap.get("sbox_code"));
                    String tKeys = UXUtil.nvl(preMap.get("sbox_nm_keys"));
                    String texts = UXUtil.nvl(preMap.get("sbox_nm_expr"));
                    String[] arrText = tKeys.split("[|]");
                    for (int kk = 0; kk < arrText.length; kk++) {
                        String text = UCS.Cont(arrText[kk], UCS.getULang(request));
                        texts = texts.replaceAll( "@"+(kk+1), text );
                    }
                    LinkedHashMap<String, String> _hmap = new LinkedHashMap<String, String>();
                    _hmap.put("value", value );
                    _hmap.put("text" , texts );

                    _arrList.add( _hmap );
                }
            }
        }
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
        resultMap.put("options"             , _arrList );
        resultMap.put("defaultSelectedValue", _default );

        return resultMap;
    }
}