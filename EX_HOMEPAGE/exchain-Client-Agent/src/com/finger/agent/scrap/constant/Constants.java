package com.finger.agent.scrap.constant;

public final class Constants {

	/* 세션스펙별 데이터키 정의 [세션스펙은 ConfigurationInfo 에 정의되어 있음.]*/
	public static final String BANK_SES						= "BANK_SES";   // BANK_SESSION 스펙에 저장될 당행측에서 넘어온 세션용 데이터
	public static final String USER_SES						= "USER_SES";   // USER_SESSION 스펙에 저장될 SPB 시스템에서 담는 세션용 데이터
	public static final String COMP_SES						= "COMP_SES";   // COMP_SESSION 스펙에 저장될 세션용 데이터

	/* 신한,조흥 은행명 구분코드 */
    public static final String  BANK_SHB					= "SHB";    // 신한은행
    public static final String  BANK_CHB					= "CHB";    // 조흥은행

	/* 신한,조흥 자산관리시스템명 구분코드 */
    public static final String  BIZ_BANK					= "BizBank";    // 신한은행
    public static final String  E_FMS						= "e-FMS";    // 조흥은행

	/* 금융권 구분 코드 */
    public static final String  FIN_TP_BK					= "1";    // 은행
    public static final String  FIN_TP_CD					= "2";    // 카드
    public static final String  FIN_TP_ST					= "3";    // 증권
    public static final String  FIN_TP_IN					= "4";    // 보험

    /* 메뉴구분코드 */
    public static final String  MENU_CD							= "00000000";    	// 메뉴구분코드
    public static final String	SHB_MENU_01						= "01000000";		// 통합계좌관리
    public static final String	SHB_MENU_02						= "02000000";		// 자금관리
    public static final String	SHB_MENU_03						= "11000000";		// GCMS
    public static final String	SHB_MENU_04						= "03000000";		// 법인카드
    public static final String	SHB_MENU_05						= "04000000";		// 어음관리
    public static final String	SHB_MENU_06						= "05000000";		// B2B
    public static final String	SHB_MENU_07						= "06000000";		// 외환
    public static final String	SHB_MENU_08						= "07000000";		// 이용환경관리
    public static final String	SHB_MENU_09						= "08000000";		// 맞춤서비스

    public static final String	CHB_MENU_01						= "01000000";		// 통합계좌관리
    public static final String	CHB_MENU_02						= "02000000";		// 자금관리
    public static final String	CHB_MENU_03						= "11000000";		// GCMS
    public static final String	CHB_MENU_04						= "03000000";		// 법인카드
    public static final String	CHB_MENU_05						= "04000000";		// 어음관리
    public static final String	CHB_MENU_06						= "05000000";		// B2B
    public static final String	CHB_MENU_07						= "06000000";		// 외환
    public static final String	CHB_MENU_08						= "07000000";		// 이용환경관리
    public static final String	CHB_MENU_09						= "08000000";		// 맞춤서비스


    /* 계좌타입 코드(==예금.대출여부코드:TB_ACCT) */
    public static final String  ACCT_TYPE_ASSET					= "01";    // 자산(예금)
    public static final String  ACCT_TYPE_DEPT					= "02";    // 부채(대출)

    /* 입출금 구분(TB_ACCT_HIST) */
    public static final String  TRANS_TP_AI						= "AI";    // 입금
    public static final String  TRANS_TP_AO						= "AO";    // 지급

    /* BIZ Partner 계좌타입종류코드*/
    public static final String  ACCT_TP_NOR	= "01";		// 일반예금
    public static final String  ACCT_TP_SAV	= "02";		// 정기예금
    public static final String  ACCT_TP_REG	= "03";		// 정기적금
    public static final String  ACCT_TP_TRU	= "04";		// 신탁
    public static final String  ACCT_TP_FOR = "05";     // 외화예금
    public static final String  ACCT_TP_LON	= "06";		// 원화대출
    public static final String  ACCT_TP_RON = "07";     // 외화대출
    public static final String  ACCT_TP_BON = "08";     // 수익증권
    public static final String  ACCT_TP_BIL = "09";     // 어음
    public static final String  ACCT_TP_BAN = "31";     // 당좌예금

    /* 스크래핑 서비스 종류 코드(은행-법인) */
    public static final String  SS_TP_B21		= "21";		// 잔액조회
    public static final String  SS_TP_B22		= "22";		// 입출금거래조회
    public static final String  SS_TP_B23		= "23";     // 계좌이체
    public static final String  SS_TP_B24		= "24";     // 이체처리결과조회
    public static final String  SS_TP_B25		= "20";     // 전계좌조회
    public static final String  SS_TP_B31		= "31";     // 바로바로잔액조회
    public static final String  SS_TP_B32		= "32";     // 바로바로거래내역조회

    /* 스크래핑 서비스 종류 코드(카드-법인) */
    public static final String  SS_TP_C20		= "20";		// 카드정보조회
    public static final String  SS_TP_C22		= "22";		// 결제금액조회
    public static final String  SS_TP_C23		= "23";     // 사용내역조회
    public static final String  SS_TP_C24		= "24";     // 이용한도조회
    public static final String  SS_TP_C25		= "25";     // 승인내역조회
    public static final String  SS_TP_C35		= "35";     // 청구서조회
    public static final String  SS_TP_C36		= "36";     // 사용내역조회
    //public static final String  SS_TP_C25		= "25";     // 현금서비스이체
    //public static final String  SS_TP_C26		= "26";     // 카드론현황조회
    /* 스크래핑 서비스 종류 코드(은행-개인) */
    public static final String  SS_TP_B01		= "01";		// 잔액조회
    public static final String  SS_TP_B02		= "02";		// 입출금거래조회
    public static final String  SS_TP_B03		= "03";     // 계좌이체
    public static final String  SS_TP_B04		= "04";     // 이체처리결과조회
    public static final String  SS_TP_B05		= "05";     // 전계좌조회
    /* 스크래핑 서비스 종류 코드(카드-개인) */
    public static final String  SS_TP_C01		= "01";		// 카드정보조회
    public static final String  SS_TP_C02		= "02";		// 결제금액조회
    public static final String  SS_TP_C03		= "03";     // 사용내역조회
    public static final String  SS_TP_C04		= "04";     // 승인내역조회
    public static final String  SS_TP_C05		= "05";     // 현금서비스이체
    public static final String  SS_TP_C06		= "06";     // 카드론현황조회

    /* 스크래핑통신 리턴 메세지 */
    /* 000 : return data
	 * 001 : 보통 메세지-거래를 했스는데 거래내역이 없습니다.(정보성)
	 * 002 : 에러메세지 - message에 셋팅
	 * 003 : HTML형식
	 * 004 : 결과무시
	 * 900 : 사용자취소
	 * 901 : 타임아웃
	 * 902 : 스크립트 문법에러
	 * 999 : 기타오류
	 */
    public static final String  RET_OK			= "00000000";
    public static final String  RET_NORM		= "S0000001";
    public static final String  RET_ERROR		= "S0000002";
    public static final String  RET_CANCEL		= "S0000900";

    /* Xcipher 로그인 시 anoymous에서 전환하기 위해 필용한 변수   */
    public static final String XCIPHER_SESSION  = "XCIPHER_SESSION";

    /* 계열사구분코드   */
    public static final String COMPANY_CD  = "120000";
    public static final String MAIN_COMP   = "120001"; // 본사코드

    /* 총괄관리자확인 action */
    public static final String MAIN_ACTION   			= "main.MainApp";
    public static final String ADMIN_CERT_ACTION   		= "mng.user.UserApp";
    public static final String ADMIN_CERT_COMMAND  		= "1021";
    public static final String ADMIN_CERT_PROC_COMMAND  = "1022";

    /* 업무메뉴 이외의 메뉴 정의(결재함)-SHB */
    public static final String SHB09000000 = "09000000:결재함:00000000:/user.pb:sancbox.SancBoxApp:1015";

    public static final String SHB09010000 = "09010000:당행결재함:09000000:/user.pb:sancbox.SancBoxApp:1015";
    public static final String SHB09010100 = "09010100:결재:09010000:/user.pb:sancbox.SancBoxApp:1003";
    public static final String SHB09010200 = "09010200:승인:09010000:/user.pb:sancbox.SancBoxApp:1012";
    public static final String SHB09010300 = "09010300:승인취소:09010000:/user.pb:sancbox.SancBoxApp:1014";
    public static final String SHB09010400 = "09010400:진행현황:09010000:/user.pb:sancbox.SancBoxApp:1015";

    public static final String SHB09020000 = "09000000:타행결재함:00000000:/admin.pb:mng.tadc.TaDcInfoApp:4001";
    public static final String SHB09020100 = "09020100:확인:09020000:/admin.pb:mng.tadc.TaDcInfoApp:1001";
    public static final String SHB09020200 = "09020200:승인:09020000:/admin.pb:mng.tadc.TaDcInfoApp:2001";
    public static final String SHB09020300 = "09020300:실행:09020000:/admin.pb:mng.tadc.TaDcInfoApp:3001";
    public static final String SHB09020400 = "09020400:진행현황:09020000:/admin.pb:mng.tadc.TaDcInfoApp:4001";
    public static final String[] SHB_PAY_BOX = {SHB09000000,
                                                SHB09010000,SHB09010100,SHB09010200,SHB09010300,SHB09010400,
                                                SHB09020000,SHB09020100,SHB09020200,SHB09020300,SHB09020400};

    /* 업무메뉴 이외의 메뉴 정의(결재함)-CHB */
    public static final String CHB09000000 = "09000000:결재함:00000000:/user.pb:sancbox.SancBoxApp:1001";
    public static final String CHB09010000 = "09010000:결재내역 조회/승인:09000000:/user.pb:sancbox.SancBoxApp:1001";
    public static final String CHB09020000 = "09020000:기결재내역 조회:09000000:/user.pb:sancbox.SancBoxApp:1008";
    public static final String[] CHB_PAY_BOX = {CHB09000000,CHB09010000,CHB09020000};

    /* 업무메뉴 이외의 메뉴 정의(MY PAGE)-SHB */
    public static final String SHB10000000 = "10000000:MY_PAGE:00000000:/common.pb:mypage.MypageApp:1007";
    public static final String SHB10010000 = "10010000:개인정보 관리:10000000:/common.pb:mypage.MypageApp:1007";
    public static final String SHB10020000 = "10020000:초화면 설정:10000000:/common.pb:mypage.MypageApp:1008";
    public static final String SHB10030000 = "10030000:자주쓰는 입금계좌:10000000:/common.pb:mypage.MypageApp:1001";
    public static final String SHB10040000 = "10040000:이용계좌그룹 설정:10000000:/common.pb:mypage.MypageApp:1006";
    public static final String[] SHB_MY_PAGE = {SHB10000000,SHB10010000,SHB10020000,SHB10030000,SHB10040000};

    /* 업무메뉴 이외의 메뉴 정의(MY PAGE)-CHB */
    public static final String CHB10000000 = "10000000:MY_PAGE:00000000:/common.pb:mypage.MypageApp:1007";
    public static final String CHB10010000 = "10010000:개인정보 관리:10000000:/common.pb:mypage.MypageApp:1007";
    public static final String CHB10020000 = "10020000:초화면 설정:10000000:/common.pb:mypage.MypageApp:1008";
    public static final String CHB10030000 = "10030000:자주쓰는 입금계좌:10000000:/common.pb:mypage.MypageApp:1001";
    public static final String CHB10040000 = "10040000:이용계좌그룹 설정:10000000:/common.pb:mypage.MypageApp:1006";
    public static final String[] CHB_MY_PAGE = {CHB10000000,CHB10010000,CHB10020000,CHB10030000,CHB10040000};

	/*
	 * 신한은행 New-Bank  은행코드
	 * 구 신한코드와 매핑되고 신 코드에도 존재하는 코드구분
	 */
	public static final String YEYAK_ICHE_TIME = "cib_reserv_time";				// [cib_reserv_time]   예약이체시간
	public static final String AUTO_ICHE_JUGI = "iche_jugi";					// [iche_jugi]   자동이체주기
    public static final String TONGHWA_CODE = "CUR_C";							// [CUR_C]   통화코드
    public static final String KUKGA_CODE = "NAT_C";							// [NAT_C]   국가코드
    public static final String KEB_ICHE_SUCHUI_BANK = "keb_iche_suchui_bank";	// [keb_iche_suchui_bank]	KEB이체수취은행
    public static final String SUIB_SUSURYO_BUDAM = "FEEBD_G";					// [FEEBD_G]	수입-수수료부담
	public static final String UPMU_TYPE = "upmu_type";              			// [upmu_type]   업무타입 코드
	public static final String MOGROG_STATUS = "mokrok_status";	    			// [mokrok_status]   목록상태
 	public static final String BANK_CODE = "bank_code";							// [bank_code]   은행코드
	public static final String JIJEOM_CODE = "jijeom_code";						// [jijeom_code]   지점코드
    public static final String MAEIB_CHUSIM_GUBUN = "BUY_COLT_C";				// [BUY_COLT_C]   매입추심구분
    public static final String GYULJE_CONDITION = "6005_FCUR_KJ_JOGN_C";		// [6005_FCUR_KJ_JOGN_C]   결제조건
    public static final String GYULJE_JAGEUM_GUBUN = "KJ_G";					// [KJ_G]   결제자금구분
    public static final String USANCE_GUBUN= "GIHANB_K";						// [GIHANB_K]   USANCE구분
	public static final String UNSONG_BANGBEOB_GUBUN = "UNSONG_MTH";			// [UNSONG_MTH]   운송방법BL구분
    public static final String BUNHAL_SEONJEOK= "DIV_SHIP_G";	    			// [DIV_SHIP_G]   분할선적
    public static final String PRICE_CONDITION = "PRC_JOGN_C";					// [PRC_JOGN_C]   가격조건
    public static final String SEMOG_CODE = "semok_code";						// [semok_code]   국세세목코드
	public static final String GUGSE_GYULJEONG_CODE = "gyuljeong_gbn";			// [gyuljeong_gbn]   국세결정코드
	public static final String GOJI_GUBUN = "goji_gubun";						// [goji_gubun]   고지구분
	public static final String BOGWAN_TRX_CODE = "bogwan_trx_code";				// [bogwan_trx_code] 보관납품거래종류
	public static final String BOGWAN_KIND_CODE = "bogwan_kind_code";			// [bogwan_kind_code] 보관어음종류
	public static final String NABBU_SYSTEM = "nabbu_system";					// [nabbu_system] 공과금납부시스템
	public static final String TAX_SANGHASU_REGION = "tax_sanghasu_region";		// [tax_sanghasu_region] 상하수이용기관지로번호
	public static final String TAX_JIBANG_REGION = "tax_jibang_region";			// [tax_jibang_region] 지방세이용기관지로번호
	public static final String JIBANG_GIBUN = "jibang_gibun";			        // [jibang_gibun] 지방세 기분
	public static final String JIBANG_GWAMOK = "jibang_gwamok";			        // [jibang_gibun] 지방세 과목과 세목

    /* As-Is (구 조흥)
     * 조흥은행 .CDI문서에 정의된 코드 분류값(2005-10-14 by Dustin Pak)
     * 사용방법 : Constants.변수명
     */
    public static final String CDI_0001 = "CURRENCY.cdi";
    public static final String CDI_0002 = "CURRENCY_3.cdi";
    public static final String CDI_0003 = "C머니이체구분.cdi";
    public static final String CDI_0004 = "가맹점상태.cdi";
    public static final String CDI_0005 = "거래구분.cdi";
    public static final String CDI_0006 = "결재구분.cdi";
    public static final String CDI_0007 = "결재처리상태.cdi";
    public static final String CDI_0008 = "급여코드.cdi";
    public static final String CDI_0009 = "매출구분.cdi";
    public static final String CDI_0010 = "법인개인구분.cdi";
    public static final String CDI_0011 = "보관금종류.cdi";
    public static final String CDI_0012 = "보증여부.cdi";
    public static final String CDI_0013 = "상태.cdi";
    public static final String CDI_0014 = "선입금유무.cdi";
    public static final String CDI_0015 = "선지급유무.cdi";
    public static final String CDI_0016 = "송금구분.cdi";
    public static final String CDI_0017 = "송금사유.cdi";
    public static final String CDI_0018 = "송달료납부종류.cdi";
    public static final String CDI_0019 = "수출거래지점.cdi";
    public static final String CDI_0020 = "어음종류.cdi";
    public static final String CDI_0021 = "예금명.cdi";
    public static final String CDI_0022 = "예금명eng.cdi";
    public static final String CDI_0023 = "예금상태.cdi";
    public static final String CDI_0024 = "예금상태eng.cdi";
    public static final String CDI_0025 = "예약시간.cdi";
    public static final String CDI_0026 = "예약시간eng.cdi";
    public static final String CDI_0027 = "예약이체처리상태.cdi";
    public static final String CDI_0028 = "외상매출상태.cdi";
    public static final String CDI_0029 = "외화이체.cdi";
    public static final String CDI_0030 = "외화이체inverse.cdi";
    public static final String CDI_0031 = "외환.cdi";
    public static final String CDI_0032 = "위탁여부.cdi";
    public static final String CDI_0033 = "일치여부.cdi";
    public static final String CDI_0034 = "입금은행선택.cdi";
    public static final String CDI_0035 = "입금은행이름.cdi";
    public static final String CDI_0036 = "입금은행이름eng.cdi";
    public static final String CDI_0037 = "입금은행코드.cdi";
    public static final String CDI_0038 = "정상불능.cdi";
    public static final String CDI_0039 = "정상불능eng.cdi";
    public static final String CDI_0040 = "정상불능처리.cdi";
    public static final String CDI_0041 = "지급승인여부.cdi";
    public static final String CDI_0042 = "지시금지.cdi";
    public static final String CDI_0043 = "채권종류.cdi";
    public static final String CDI_0044 = "채권종류결과.cdi";
    public static final String CDI_0045 = "처리내용.cdi";
    public static final String CDI_0046 = "처리상태.cdi";
    public static final String CDI_0047 = "취소파일상태.cdi";
    public static final String CDI_0048 = "취소파일상태eng.cdi";
    public static final String CDI_0049 = "통화코드.cdi";
    public static final String CDI_0050 = "파일상태.cdi";
    public static final String CDI_0051 = "파일상태eng.cdi";
    public static final String CDI_0052 = "환어음부호.cdi";

    /* 자금이체쪽에서 당행계좌만 이체되기위한 타행계좌조회 여부 체크 비트값(2005-11-30 by Dustin Pak : 이창덕 과장 요청)
     */
    public static final boolean ICHE_TA_YN = true;
}