package com.finger.fwf.uxui;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class MarketPolicyInfo implements Serializable {

	private static final long serialVersionUID = -8346157091419374599L;
	
	private static final String _SYS_MGT_NO             =   "sys_mgt_no";
	private static final String _SYS_MGT_NM             =   "sys_mgt_nm";

    private static final String  _SYS_ID                       = "sys_id";                        //시스템ID
    private static final String  _COIN_NM_KOR                  = "coin_nm_kor";                   //코인명_한글
    private static final String  _COIN_NM_ENG                  = "coin_nm_eng";                   //코인명_영문
    private static final String  _COIN_UNIT_NM                 = "coin_unit_nm";                  //코인단위명
    private static final String  _MRKT_OPEN_HOUR               = "mrkt_open_hour";                //마켓개장시간_HH
    private static final String  _MRKT_CLOSE_HOUR              = "mrkt_close_hour";               //마켓폐장시간_HH
    private static final String  _COIN_SELL_UNIT_QTY           = "coin_sell_unit_qty";            //코인판매단위수량
    private static final String  _COIN_SELL_QTY_MIN            = "coin_sell_qty_min";             //코인판매수량_최소
    private static final String  _COIN_SELL_QTY_MAX            = "coin_sell_qty_max";             //코인판매수량_최대
    private static final String  _COIN_SELL_UNIT_AMT           = "coin_sell_unit_amt";            //코인판매단위금액
    private static final String  _COIN_SELL_AMT_MIN            = "coin_sell_amt_min";             //코인판매금액_최소
    private static final String  _COIN_SELL_AMT_MAX            = "coin_sell_amt_max";             //코인판매금액_최대
    private static final String  _BUY_ALLOW_MINUTE             = "buy_allow_minute";              //구매허용시간_MM
    private static final String  _PNY_TEST_ALLOW_MINUTE        = "pny_test_allow_minute";         //페니테스트허용시간_MM
    private static final String  _WALLET_MIN_BALANCE           = "wallet_min_balance";            //월렛최소잔액
    private static final String  _COIN_SEND_FEE                = "coin_send_fee";                 //코인이체수수료
    private static final String  _SELL_FEE_RATE                = "sell_fee_rate";                 //판매수수료율
    private static final String  _SELL_FEE_MIN                 = "sell_fee_min";                  //판매수수료_최소
    private static final String  _SELL_FEE_MAX                 = "sell_fee_max";                  //판매수수료_최대
    private static final String  _SELL_FEE_DC_RATE             = "sell_fee_dc_rate";              //판매수수료할인율
    private static final String  _SELL_CANCEL_FEE_COIN         = "sell_cancel_fee_coin";          //판매취소수수료
    private static final String  _SELL_CANCEL_FEE_DC_RATE      = "sell_cancel_fee_dc_rate";       //판매취소수수료할인율
    private static final String  _BUY_FEE_RATE                 = "buy_fee_rate";                  //구매수수료율
    private static final String  _BUY_FEE_MIN                  = "buy_fee_min";                   //구매수수료_최소
    private static final String  _BUY_FEE_MAX                  = "buy_fee_max";                   //구매수수료_최대
    private static final String  _BUY_FEE_DC_RATE              = "buy_fee_dc_rate";               //구매수수료할인율
    private static final String  _USE_YN                       = "use_yn";                        //사용여부
    private static final String  _OFFDAY_YN                    = "offday_yn";                     //휴일거래여부
    
	private static final String	 _PV_COIN_SELL_UNIT_QTY		   = "pv_coin_sell_unit_qty";	 	  // 프라이빗코인판매단위수량		
	private static final String	 _PV_COIN_SELL_QTY_MIN		   = "pv_coin_sell_qty_min";	   	  // 프라이빗코인판매수량_최소		
	private static final String	 _PV_COIN_SELL_QTY_MAX		   = "pv_coin_sell_qty_max";          // 프라이빗코인판매수량_최대		
	private static final String	 _PV_COIN_SELL_UNIT_AMT		   = "pv_coin_sell_unit_amt";         // 프라이빗코인판매단위금액		
	private static final String	 _PV_COIN_SELL_AMT_MIN		   = "pv_coin_sell_amt_min";          // 프라이빗코인판매금액_최소		
	private static final String	 _PV_COIN_SELL_AMT_MAX		   = "pv_coin_sell_amt_max";          // 프라이빗코인판매금액_최대		
	private static final String	 _PV_SELL_FEE_RATE		       = "pv_sell_fee_rate";              // 프라이빗판매수수료율		
	private static final String	 _PV_SELL_FEE_MIN		       = "pv_sell_fee_min";               // 프라이빗판매수수료_최소		
	private static final String	 _PV_SELL_FEE_MAX		       = "pv_sell_fee_max";               // 프라이빗판매수수료_최대		
	private static final String	 _PV_SELL_FEE_DC_RATE		   = "pv_sell_fee_dc_rate";           // 프라이빗판매수수료할인율		
	private static final String	 _PV_SELL_CANCEL_FEE_COIN	   = "pv_sell_cancel_fee_coin";       // 프라이빗판매취소수수료		
	private static final String	 _PV_SELL_CANCEL_FEE_DC_RATE   = "pv_sell_cancel_fee_dc_rate";    // 프라이빗판매취소수수료할인율	
	private static final String	 _PV_BUY_FEE_RATE		       = "pv_buy_fee_rate";               // 프라이빗구매수수료율		
	private static final String	 _PV_BUY_FEE_MIN		       = "pv_buy_fee_min";                // 프라이빗구매수수료_최소		
	private static final String	 _PV_BUY_FEE_MAX		       = "pv_buy_fee_max";                // 프라이빗구매수수료_최대		
	private static final String	 _PV_BUY_FEE_DC_RATE		   = "pv_buy_fee_dc_rate";            // 프라이빗구매수수료할인율	
    

    private LinkedHashMap<String, String> _marketinfo_map;


    public MarketPolicyInfo(LinkedHashMap<String, String> map) {
    	_marketinfo_map = null;
    	_marketinfo_map = map;
    }

    public String getValue(String key) {
        String retStr = (String)_marketinfo_map.get(key);
        if(retStr == null)
            retStr = "";
        return retStr;
    }
    public void setValue(String key, String value) {
    	_marketinfo_map.put(key, value);
    }


    public String getSysMgtNo() {
        return getValue(_SYS_MGT_NO);
    }
    public String getSysMgtNm() {
        return getValue(_SYS_MGT_NM);
    }
    public String getSysId() {
        return getValue(_SYS_ID);
    }
    public String getCoinNmKor() {
        return getValue(_COIN_NM_KOR);
    }
    public String getCoinNmEng() {
        return getValue(_COIN_NM_ENG);
    }
    public String getCoinUnitNm() {
        return getValue(_COIN_UNIT_NM);
    }
    public String getMrktOpenHour() {
        return getValue(_MRKT_OPEN_HOUR);
    }
    public String getMrktCloseHour() {
        return getValue(_MRKT_CLOSE_HOUR);
    }
    public String getCoinSellUnitQty() {
        return getValue(_COIN_SELL_UNIT_QTY);
    }
    public String getCoinSellQtyMin() {
        return getValue(_COIN_SELL_QTY_MIN);
    }
    public String getCoinSellQtyMax() {
        return getValue(_COIN_SELL_QTY_MAX);
    }
    public String getCoinSellUnitAmt() {
        return getValue(_COIN_SELL_UNIT_AMT);
    }
    public String getCoinSellAmtMin() {
        return getValue(_COIN_SELL_AMT_MIN);
    }
    public String getCoinSellAmtMax() {
        return getValue(_COIN_SELL_AMT_MAX);
    }
    public String getBuyAllowMinute() {
        return getValue(_BUY_ALLOW_MINUTE);
    }
    public String getPnyTestAllowMinute() {
        return getValue(_PNY_TEST_ALLOW_MINUTE);
    }
    public String getWalletMinBalance() {
        return getValue(_WALLET_MIN_BALANCE);
    }
    public String getCoinSendFee() {
        return getValue(_COIN_SEND_FEE);
    }
    public String getSellFeeRate() {
        return getValue(_SELL_FEE_RATE);
    }
    public String getSellFeeMin() {
        return getValue(_SELL_FEE_MIN);
    }
    public String getSellFeeMax() {
        return getValue(_SELL_FEE_MAX);
    }
    public String getSellFeeDcRate() {
        return getValue(_SELL_FEE_DC_RATE);
    }
    public String getSellCancelFee() {
        return getValue(_SELL_CANCEL_FEE_COIN);
    }
    public String getSellCancelFeeDcRate() {
        return getValue(_SELL_CANCEL_FEE_DC_RATE);
    }
    public String getBuyFeeRate() {
        return getValue(_BUY_FEE_RATE);
    }
    public String getBuyFeeMin() {
        return getValue(_BUY_FEE_MIN);
    }
    public String getBuyFeeMax() {
        return getValue(_BUY_FEE_MAX);
    }
    public String getBuyFeeDcRate() {
        return getValue(_BUY_FEE_DC_RATE);
    }
    public String getUseYn() {
        return getValue(_USE_YN);
    }
    public String getOffDayYn() {
        return getValue(_OFFDAY_YN);
    }
    // 프라이빗거래 정책 추가
    public String getPvCoinSellUnitQty() {								
        return getValue(_PV_COIN_SELL_UNIT_QTY);                       
    }                                                                 
    public String getPvCoinSellQtyMin() {                            
        return getValue(_PV_COIN_SELL_QTY_MIN);                        
    }                                                                 
    public String getPvCoinSellQtyMax() {                            
        return getValue(_PV_COIN_SELL_QTY_MAX);                        
    }                                                                 
    public String getPvCoinSellUnitAmt() {                            
        return getValue(_PV_COIN_SELL_UNIT_AMT);                       
    }                                                                 
	public String getPvCoinSellAmtMin() {                             
        return getValue(_PV_COIN_SELL_AMT_MIN);                        
    }                                                                 
    public String getPvCoinSellAmtMax() {
        return getValue(_PV_COIN_SELL_AMT_MAX);
    }
	public String getPvSellFeeRate() {
        return getValue(_PV_SELL_FEE_RATE);
    }
    public String getPvSellFeeMin() {
        return getValue(_PV_SELL_FEE_MIN);
    }
	public String getPvSellFeeMax() {
        return getValue(_PV_SELL_FEE_MAX);
    }
    public String getPvSellFeeDcRate() {
        return getValue(_PV_SELL_FEE_DC_RATE);
    }
	public String getPvSellCancelFee() {
        return getValue(_PV_SELL_CANCEL_FEE_COIN);
    }
    public String getPvSellCancelFeeDcRate() {
        return getValue(_PV_SELL_CANCEL_FEE_DC_RATE);
    }
	public String getPvBuyFeeRate() {
        return getValue(_PV_BUY_FEE_RATE);
    }
    public String getPvBuyFeeMin() {
        return getValue(_PV_BUY_FEE_MIN);
    }
	public String getPvBuyFeeMax() {
        return getValue(_PV_BUY_FEE_MAX);
    }
    public String getPvBuyFeeDcRate() {
        return getValue(_PV_BUY_FEE_DC_RATE);
    }
    
    /*  getter !END!  */

    public String toString() {
        return (new StringBuilder("["+MarketPolicyInfo.class+"] = ")).append(_marketinfo_map.toString()).toString();
    }
}