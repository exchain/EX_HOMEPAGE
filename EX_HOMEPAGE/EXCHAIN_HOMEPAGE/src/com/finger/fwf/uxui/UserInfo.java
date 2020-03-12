package com.finger.fwf.uxui;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class UserInfo implements Serializable {
    private static final long serialVersionUID = 3561410721260101450L;

    private static final String _SYS_MGT_NO             =   "sys_mgt_no";
    private static final String _USER_ID                =   "user_id";
    private static final String _USER_NM                =   "user_nm";
    private static final String _USER_PWD               =   "user_pwd";
    private static final String _APPR_PWD               =   "appr_pwd";            //승인비밀번호추가 20160909
    private static final String _LOGIN_ID               =   "login_id";

    public  static final String _IP_ADDRES              =   "ip_addres";
    public  static final String _SESSION_ID             =   "session_id";
    public  static final String _LOGIN_DT               =   "login_dt";

    private static final String _User_Type_Cd           =   "user_type_cd";
    private static final String _Crpn_Seq_No            =   "crpn_seq_no";
    private static final String _Smark_Crpn_Nm          =   "smark_crpn_nm";
    private static final String _Biz_Seq_No             =   "biz_seq_no";
    private static final String  _Pop_Alm_Perd          =   "pop_alm_perd";			//팝업알림주기
    private static final String  _Alm_Popup_Yn          =   "alm_popup_yn";			//알림팝업여부
    private static final String  _Chk_Pwd_Upd_Perd      =   "chk_pwd_upd_perd";		//비밀번호변경주기초과 = Y
    private static final String  _Rptsite_Use_Yn        =   "rptsite_use_yn";		//보고서사이트사용여부

    private static final String  _Recvr_Nm_Cash_Day     =   "recvr_nm_cash_day";
    private static final String  _Cdtm_Bal_Pass_Dtm     =   "cdtm_bal_pass_dtm";
    private static final String  _Re_Trns_Chk_Yn        =   "re_trns_chk_yn";
    private static final String  _Erp_Lnk_Yn            =   "erp_lnk_yn";           //ERP연계여부
    private static final String  _Erp_Lnk_Div           =   "erp_lnk_div";          //ERP연계구분           

    private static final String  _Regr_Auth_Yn          =   "regr_auth_yn";			//등록자권한유무
    private static final String  _Apvr_Auth_Yn          =   "apvr_auth_yn";			//승인자권한유무
    private static final String  _Exer_Auth_Yn          =   "exer_auth_yn";			//실행자권한유무
    private static final String  _Sal_Ofcr_Yn           =   "sal_ofcr_yn";			//급여담당자여부

    private static final String  _Login_Acs_Dt          =   "login_acs_dt";			//로그인접속일자(오늘날짜용)
    private static final String  _Recent_Login_Date     =   "recent_login_date";	//최근접속일자

    private static final String  _Grid_Page_Row_Cnt     =   "grid_page_row_cnt";	//GRID PAGE ROW 건수

    private static final String  _Fmt_Date_Separator    =   "format_date_separator";
    private static final String  _Fmt_Time_Separator    =   "format_time_separator";
    private static final String  _Fmt_Amnt_Separator    =   "format_amount_separator";
    private static final String  _Fmt_Pont_Separator    =   "format_point_separator";
    private static final String  _Fmt_Date_YMD_Order    =   "format_date_ymd_order";
    private static final String  _Fmt_Date_YMx_Order    =   "format_date_ymx_order";
    
    
    private static final String  _Mlang_Use_Yn          =   "mlang_use_yn";			//다국어 사용 여부
    private static final String  _Main_Bank_Cd          =   "main_bank_cd";			//주거래 은행 코드
    private static final String  _Rep_Dmst_Crpn_Seq_No  =   "rep_dmst_crpn_seq_no";	//대표국내법인일련번호
    private static final String  _Rep_Dmst_Biz_Seq_No   =   "rep_dmst_biz_seq_no";	//대표국내사업장일련번호
    
    private static final String _SYS_ID                 = "sys_id";                  //시스템ID
    private static final String _CUST_SEQNO             = "cust_seqno";              //고객일련번호
    private static final String _CUST_ID                = "cust_id";                 //고객ID
    private static final String _HASH_CUST_PW           = "hash_cust_pw";            //고객비밀번호
    private static final String _CUST_STATE             = "cust_state";              //고객상태
    private static final String _MARKET_COIN_ACCT_SEQNO = "market_coin_acct_seqno";  //마켓코인계좌일련번호
    private static final String _TERMS_AGREE_YN         = "terms_agree_yn";          //약관동의여부
    private static final String _TERMS_AGREE_DTM        = "terms_agree_dtm";         //약관동의일시
    private static final String _EMAIL_AUTH_YN          = "email_auth_yn";           //이메일인증여부
    private static final String _EMAIL_AUTH_DTM         = "email_auth_dtm";          //이메일인증일시
    private static final String _PHONE_AUTH_YN          = "phone_auth_yn";           //휴대폰인증여부
    private static final String _PHONE_AUTH_DTM         = "phone_auth_dtm";          //휴대폰인증일시
    private static final String _BANK_ACCT_AUTH_YN      = "bank_acct_auth_yn";       //은행계좌인증여부
    private static final String _BANK_ACCT_AUTH_DTM     = "bank_acct_auth_dtm";      //
    private static final String _COIN_ACCT_AUTH_YN      = "coin_acct_auth_yn";       //코인계좌인증여부
    private static final String _COIN_ACCT_AUTH_DTM     = "coin_acct_auth_dtm";      //코인계좌인증일시
    private static final String _CUST_NM                = "cust_nm";                 //고객성명
    private static final String _BIRTHDAY               = "birthday";                //생년월일
    private static final String _SEX_GB                 = "sex_gb";                  //성별구분
    private static final String _FOREIGN_YN             = "foreign_yn";              //외국인여부
    private static final String _MINOR_YN               = "minor_yn";                //미성년자여부
    private static final String _PHONE_TELCO            = "phone_telco";             //휴대폰통신사
    private static final String _PHONE_NO               = "phone_no";                //휴대폰번호
    private static final String _BANK_CD                = "bank_cd";                 //은행코드
    private static final String _BANK_ACCT_NO           = "bank_acct_no";            //은행계좌번호
    private static final String _WALLET_PUB_ADDR        = "wallet_pub_addr";         //월렛공개주소
    private static final String _REG_DTM                = "reg_dtm";                 //가입일시
    private static final String _WITHDRAW_DTM           = "withdraw_dtm";            //탈퇴일시
    
    

    private LinkedHashMap<String, String> _user_map;

    public UserInfo(String s_no, String u_id, String u_nm) {
        _user_map = null;
        _user_map = new LinkedHashMap<String, String>();
        _user_map.put(_SYS_MGT_NO, s_no );
        _user_map.put(_USER_ID   , u_id );
        _user_map.put(_USER_NM   , u_nm );
    }

    public UserInfo(String s_no, String u_id, String u_nm, String u_ip) {
        _user_map = null;
        _user_map = new LinkedHashMap<String, String>();
        _user_map.put(_SYS_MGT_NO, s_no );
        _user_map.put(_USER_ID   , u_id );
        _user_map.put(_USER_NM   , u_nm );
        _user_map.put(_IP_ADDRES , u_ip );
    }

    public UserInfo(String s_no, String u_id, String u_nm, String u_ip, String s_id) {
        _user_map = null;
        _user_map = new LinkedHashMap<String, String>();
        _user_map.put(_SYS_MGT_NO, s_no );
        _user_map.put(_USER_ID   , u_id );
        _user_map.put(_USER_NM   , u_nm );
        _user_map.put(_IP_ADDRES , u_ip );
        _user_map.put(_SESSION_ID, s_id );
    }

    public UserInfo(LinkedHashMap<String, String> map) {
        _user_map = null;
        _user_map = map;
    }

    public String getValue(String key) {
        String retStr = (String)_user_map.get(key);
        if(retStr == null)
            retStr = "";
        return retStr;
    }
    public void setValue(String key, String value) {
        _user_map.put(key, value);
    }

    /*  getter START */
    public String getSysMgtNo() {
        return getValue(_SYS_MGT_NO);
    }
    public String getUserId() {
        return getValue(_USER_ID);
    }
    public String getUserName() {
        return getValue(_USER_NM);
    }
    public String getUserPwd() {
        return getValue(_USER_PWD);
    }
    public String getApprPwd() {
        return getValue(_APPR_PWD);
    }    
    public String getLoginId() {
        return getValue(_LOGIN_ID);
    }
    public String getIpAddress() {
        return getValue(_IP_ADDRES);
    }
    public String getSessionId() {
        return getValue(_SESSION_ID);
    }
    public String getLoginDatetime() {
        return getValue(_LOGIN_DT);
    }
    public String getUserTypeCd() {
        return getValue(_User_Type_Cd);
    }
    public String getCrpnSeqNo() {
        return getValue(_Crpn_Seq_No);
    }
    public String getSmarkCrpnNm() {
        return getValue(_Smark_Crpn_Nm);
    }
    public String getBizSeqNo() {
        return getValue(_Biz_Seq_No);
    }
    public String getPopAlmPerd() {
        return getValue(_Pop_Alm_Perd);
    }
    public String getAlmPopupYn() {
        return getValue(_Alm_Popup_Yn);
    }
    public String getChkPwdUpdPerd() {
        return getValue(_Chk_Pwd_Upd_Perd);
    }
    public String getRptsiteUseYn() {
        return getValue(_Rptsite_Use_Yn);
    }

    public String getRecvrNmCashDay() {
        return getValue(_Recvr_Nm_Cash_Day);
    }
    public String getCdtmBalPassDtm() {
        return getValue(_Cdtm_Bal_Pass_Dtm);
    }
    public String getReTrnsChkYn() {
        return getValue(_Re_Trns_Chk_Yn);
    }
    public String getErpLnkYn() {
        return getValue(_Erp_Lnk_Yn);
    }
    public String getErpLnkDiv() {
        return getValue(_Erp_Lnk_Div);
    }
    public String getRegrAuthYn() {
        return getValue(_Regr_Auth_Yn);
    }
    public String getApvrAuthYn() {
        return getValue(_Apvr_Auth_Yn);
    }
    public String getExerAuthYn() {
        return getValue(_Exer_Auth_Yn);
    }
    public String getSalOfcrYn() {
        return getValue(_Sal_Ofcr_Yn);
    }

    public String getLoginAcsDt() {
        return getValue(_Login_Acs_Dt);
    }
    public String getRecentLoginDate() {
        return getValue(_Recent_Login_Date);
    }
    
    public String getGridPageRowCnt() {
        return getValue(_Grid_Page_Row_Cnt);
    }

    public String getFmtDateSeparator() {
        return getValue(_Fmt_Date_Separator);
    }
    public String getFmtTimeSeparator() {
        return getValue(_Fmt_Time_Separator);
    }
    public String getFmtAmountSeparator() {
        return getValue(_Fmt_Amnt_Separator);
    }
    public String getFmtPointSeparator() {
        return getValue(_Fmt_Pont_Separator);
    }
    public String getFmtDateYMDOrder() {
        return getValue(_Fmt_Date_YMD_Order);
    }
    public String getFmtDateYMxOrder() {
        return getValue(_Fmt_Date_YMx_Order);
    }
    
    public String getMlangUseYn() {
        return getValue(_Mlang_Use_Yn);
    }
    public String getMainBankCd() {
        return getValue(_Main_Bank_Cd);
    }
    public String getRepDmstCrpnSeqNo() {
        return getValue(_Rep_Dmst_Crpn_Seq_No);
    }
    public String getRepDmstBizSeqNo() {
        return getValue(_Rep_Dmst_Biz_Seq_No);
    }
    
    /* 2018.02.10 추가 */
    public String getSysId() {
        return getValue(_SYS_ID);
    }
    public String getCustSeqno() {
        return getValue(_CUST_SEQNO);
    }
    public String getCustId() {
        return getValue(_CUST_ID);
    }
    public String getHashCustPw() {
        return getValue(_HASH_CUST_PW);
    }
    public String getCustState() {
        return getValue(_CUST_STATE);
    }
    public String getMarketCoinAcctSeqno() {
        return getValue(_MARKET_COIN_ACCT_SEQNO);
    }
    public String getTermsAgreeYn() {
        return getValue(_TERMS_AGREE_YN);
    }
    public String getTermsAgreeDtm() {
        return getValue(_TERMS_AGREE_DTM);
    }
    public String getEmailAuthYn() {
        return getValue(_EMAIL_AUTH_YN);
    }
    public String getEmailAuthDtm() {
        return getValue(_EMAIL_AUTH_DTM);
    }
    public String getPhoneAuthYn() {
        return getValue(_PHONE_AUTH_YN);
    }
    public String getPhoneAuthDtm() {
        return getValue(_PHONE_AUTH_DTM);
    }
    public String getBankAcctAuthYn() {
        return getValue(_BANK_ACCT_AUTH_YN);
    }
    public String getBankAcctAuthDtm() {
        return getValue(_BANK_ACCT_AUTH_DTM);
    }
    public String getCoinAcctAuthYn() {
        return getValue(_COIN_ACCT_AUTH_YN);
    }
    public String getCoinAcctAuthDtm() {
        return getValue(_COIN_ACCT_AUTH_DTM);
    }
    public String getCustNm() {
        return getValue(_CUST_NM);
    }
    public String getBirthday() {
        return getValue(_BIRTHDAY);
    }
    public String getSexGb() {
        return getValue(_SEX_GB);
    }
    public String getForeignYn() {
        return getValue(_FOREIGN_YN);
    }
    public String getMinorYn() {
        return getValue(_MINOR_YN);
    }
    public String getPhoneTelco() {
        return getValue(_PHONE_TELCO);
    }
    public String getPhoneNo() {
        return getValue(_PHONE_NO);
    }
    public String getBankCd() {
        return getValue(_BANK_CD);
    }
    public String getBankAcctNo() {
        return getValue(_BANK_ACCT_NO);
    }
    public String getWalletPubAddr() {
        return getValue(_WALLET_PUB_ADDR);
    }
    public String getRegDtm() {
        return getValue(_REG_DTM);
    }
    public String getWithdrawDtm() {
        return getValue(_WITHDRAW_DTM);
    }
    

    
    
    /*  getter !END!  */

    public String toString() {
        return (new StringBuilder("["+UserInfo.class+"] = ")).append(_user_map.toString()).toString();
    }
}