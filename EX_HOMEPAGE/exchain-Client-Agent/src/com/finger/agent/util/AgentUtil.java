package com.finger.agent.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AgentUtil {
    
	private static final String PHONE_NUM_PATTERN = "(01[016789])(\\d{3,4})(\\d{4})";
	private static final String EMAIL_PATTERN     = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
	                                               + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
    /** 입력 받은 String에 원하는 수의 공백(" ")을 추가한다
     * @param value 문자열
     * @param len   입력문자열을 포함한 전체 길이
     * @return 공백이 추가된 문자열
     */
    public static String fillSpaceString(String value, int len) {
        if (value == null) value = "";

        StringBuffer buffer = new StringBuffer(len);
        int loop = len - value.getBytes().length;

        buffer.append(value);
        for (int i = 0; i < loop; i++)
            buffer.append(" ");

        byte[] stringTobyteValue = buffer.toString().getBytes();
        byte[] byteTostringValue = new byte[len];

        System.arraycopy(stringTobyteValue, 0, byteTostringValue, 0, len);
        String cString = new String(byteTostringValue);

        return cString;
    }

    /** 입력받은 문자열 앞에 숫자 0을 붙인다
     * @param value 문자열
     * @param len   문자열을 포함한 전체 길이
     * @return 0을 채운 문자열
     */
    public static String fillNumericString(String value, int len) {
        return fillNumericString(value, len, 0);
    }
    /** 입력받은 문자열 앞에 숫자 0을 붙인다
     * @param value  문자열
     * @param length 문자열을 포함한 전체 길이
     * @param scale  문자열 중 소숫점 자리수
     * @return 0을 채운 문자열
     */
    public static String fillNumericString(String value, int length, int scale) {
        value = nvl(value, "0");

        BigDecimal bigDecimalValue = new BigDecimal(value);
        String sForeValue = bigDecimalValue.toString();
        String sRearValue = "";
        if (sForeValue.indexOf(".") > -1) {
            sForeValue = bigDecimalValue.toString().substring(0,bigDecimalValue.toString().indexOf("."));
            sRearValue = bigDecimalValue.toString().substring(bigDecimalValue.toString().indexOf(".")+1);
        }
        
        StringBuffer buffer = new StringBuffer(length);
        int loopF = length - scale - sForeValue.getBytes().length;
        int loopR = scale - sRearValue.getBytes().length;

        for (int i = 0; i < loopF; i++) buffer.append("0");
        buffer.append(sForeValue);
        buffer.append(sRearValue);
        for (int i = 0; i < loopR; i++) buffer.append("0");

        byte[] stringTobyteValue = buffer.toString().getBytes();
        byte[] byteTostringValue = new byte[length];

        System.arraycopy(stringTobyteValue, 0, byteTostringValue, 0, length);
        String cString = new String(byteTostringValue);

        return cString;
    }

    public static String nvl(String str) {
        return nvl(str, "");
    }
    public static String nvl(String str, String chg) {
        return isNull(str) ? chg : str ;
    }
    public static boolean isNull(String str) {
        return (str == null || "".equals(str)) ? true : false ;
    }

    public static String getByteString(byte srcBytes[], int offset, int size) throws ArrayIndexOutOfBoundsException {
        byte fieldData[] = new byte[size];
        System.arraycopy(srcBytes, offset, fieldData, 0, size);
        return new String(fieldData);
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // JSON 데이터 처리를 위한 XSS (Cross-Site Scripting) 해제
    public static String revokeXSSFilter (String sInvalid) {
        if (sInvalid == null || sInvalid.equals(""))
            return "";

        String sValid = sInvalid.trim();

        sValid = sValid.replaceAll("¶", "\\n");
        sValid = sValid.replaceAll("§", "\\r");
        sValid = sValid.replaceAll("&lt;", "<");
        sValid = sValid.replaceAll("&gt;", ">");
        sValid = sValid.replaceAll("&quot;", "\"");
        sValid = sValid.replaceAll("&#039;", "\'");
        sValid = sValid.replaceAll("&amp;", "&");

        return sValid;
    }
    
    // 스크래핑 수신 데이터 화면으로 넘기기 위한 필더링
    public static String scrapRecvDataFilter (String sInvalid) {
        if (sInvalid == null || sInvalid.equals(""))
            return "";

        String sValid = sInvalid.trim();

        sValid = sValid.replaceAll("\\n"   , "¶");
        sValid = sValid.replaceAll("&#x0A;", "¶");
        sValid = sValid.replaceAll("\\r"   , "§");

        return sValid;
    }
    
    /**
     * 이메일이든, 휴대폰번호든 각 포맷에 맞게 마스킹된 결과값 리턴해주는 함수
     * 포맷이 맞지 않을 경우 인풋으로 들어온 값 그대로 리턴
     *
     * public은 이거 하나! valid check류의 메서드들도 추후 필요하면 public으로 바꿀 예정
     *
     * @param id
     * @return maskedId
     */
    public static String getMasked(String str) 
    {
    
    	if (isEmail(str)) 
    	{
    		return getMaskedEmail(str);
    	} else if (isPhoneNum(str)) 
    	{
    		return getMaskedPhoneNum(str);
    	} else
    	{
    		return getMaskedEct(str);
    	}
    }

    /**
     * 이메일 포맷 Validator
     * @param str
     * @return isValidEmailFormat
     */
    private static boolean isEmail(final String str) {
       return isValid(EMAIL_PATTERN, str);
    }

    /**
     * 휴대폰 번호 포맷 Validator
     * @param str
     * @return isValidCellPhoneNumFormat
     */
    private static boolean isPhoneNum(final String str) {
       return isValid(PHONE_NUM_PATTERN, str);
    }

    /**
     * 문자열이 정규식에 맞는 포맷인지 체크
     * @param regex
     * @param target
     * @return isValid
     */
    private static boolean isValid(final String regex, final String target) {
       Matcher matcher = Pattern.compile(regex).matcher(target);
       return matcher.matches();
    }

    /**
     * 이메일 주소 마스킹 처리
     * @param email
     * @return maskedEmailAddress
     */
    private static String getMaskedEmail(String email) {
       /*
       * 요구되는 메일 포맷
       * {userId}@domain.com
       * */
       String regex = "\\b(\\S+)+@(\\S+.\\S+)";
       Matcher matcher = Pattern.compile(regex).matcher(email);
       if (matcher.find()) {
          String id = matcher.group(1); // 마스킹 처리할 부분인 userId
          /*
          * userId의 길이를 기준으로 세글자 초과인 경우 뒤 세자리를 마스킹 처리하고,
          * 세글자인 경우 뒤 두글자만 마스킹,
          * 세글자 미만인 경우 모두 마스킹 처리
          */
          int length = id.length();
          if (length < 3) {
             char[] c = new char[length];
             Arrays.fill(c, '*');
             return email.replace(id, String.valueOf(c));
          } else if (length == 3) {
             return email.replaceAll("\\b(\\S+)[^@][^@]+@(\\S+)", "$1**@$2");
          } else {
             return email.replaceAll("\\b(\\S+)[^@][^@][^@]+@(\\S+)", "$1***@$2");
          }
       }
       return email;
    }
    
    /**
     * 휴대폰 번호 마스킹 처리
     * @param phoneNum
     * @return maskedCellPhoneNumber
     */
    private static String getMaskedPhoneNum(String phoneNum) {
       /*
       * 요구되는 휴대폰 번호 포맷
       * 01055557777 또는 0113339999 로 010+네자리+네자리 또는 011~019+세자리+네자리 이!지!만!
       * 사실 0107770000 과 01188884444 같이 가운데 번호는 3자리 또는 4자리면 돈케어
       * */
       String regex = "(01[016789])(\\d{3,4})\\d{4}$";
       Matcher matcher = Pattern.compile(regex).matcher(phoneNum);
       if (matcher.find()) {
          String replaceTarget = matcher.group(2);
          char[] c = new char[replaceTarget.length()];
          Arrays.fill(c, '*');
          return phoneNum.replace(replaceTarget, String.valueOf(c));
       }
       return phoneNum;
    }
    
    /**
     * 문자열 뒷자리 4자리 마스킹 처리
     * @param str
     * @return str
     */
    private static String getMaskedEct(String str) {

		int maxMsk = 4;
    	if(str.length() > 0)
    	{
			int iMsk = Math.round(str.length() / 2);
			if(iMsk > maxMsk)
				iMsk = str.length() - maxMsk;
	
			String regex = "(?<=.{"+iMsk+"}).";
			str = str.replaceAll(regex , "*");
    	}
       return str;
    }
     
    /**
     * 숫자에 천단위마다 콤마 넣기
     * @param int
     * @return String
     */
	public static String toNumFormat(double num) { 
		DecimalFormat df = new DecimalFormat("#,###");
    	return df.format(num);
    }
}