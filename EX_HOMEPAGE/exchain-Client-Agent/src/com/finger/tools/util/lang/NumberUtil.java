package com.finger.tools.util.lang;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class NumberUtil {

    /**
     * double의 소수점 조정
     * @param num  입력값
     * @param size 소수점 자리수
     * @return 변환된 double
     */
    public static double doublePoint(double num, int size) {
         return doublePoint(num, size, "H");
    }
    /**
     * double의 소수점 조정
     * @param num  입력값
     * @param size 소수점 자리수
     * @param type 변환 타입. (H-반올림, F-아무것도 안함, C-올림, D-내림)
     * @return 변환된 double
     */
    public static double doublePoint(double num, int size, String type) {
        BigDecimal returnData = null;

        if (type.equalsIgnoreCase("H")) {
            returnData = new BigDecimal(num).setScale(size, BigDecimal.ROUND_HALF_UP);
        }
        else if (type.equalsIgnoreCase("F")) {
            returnData = new BigDecimal(num).setScale(size, BigDecimal.ROUND_FLOOR);
        }
        else if (type.equalsIgnoreCase("C")) {
            returnData = new BigDecimal(num).setScale(size, BigDecimal.ROUND_CEILING);
        }
        else {
            returnData = new BigDecimal(num).setScale(size, BigDecimal.ROUND_DOWN);
        }

        // double형으로 형변환.
        return Double.parseDouble(returnData.toString());
    }

    /**
     * double -> 소수점 조정된 String
     * @param num  입력값
     * @param size 소수점 자리수
     * @return 변환된 String
     */
    public static String doubleTostring(double num, int size) {
         return doubleTostring(num, size, "H");
    }
    /**
     * double -> 소수점 조정된 String
     * @param num  입력값
     * @param size 소수점 자리수
     * @param type 변환 타입. (U-반올림, F-아무것도 안함, C-올림, D-내림)
     * @return 변환된 String
     */
    public static String doubleTostring(double num, int size, String type) {
        BigDecimal returnData = null;

        if (type.equalsIgnoreCase("H")) {
            returnData = new BigDecimal(num).setScale(size, BigDecimal.ROUND_HALF_UP);
        }
        else if (type.equalsIgnoreCase("F")) {
            returnData = new BigDecimal(num).setScale(size, BigDecimal.ROUND_FLOOR);
        }
        else if (type.equalsIgnoreCase("C")) {
            returnData = new BigDecimal(num).setScale(size, BigDecimal.ROUND_CEILING);
        }
        else {
            returnData = new BigDecimal(num).setScale(size, BigDecimal.ROUND_DOWN);
        }

        // String형으로 형변환.
        return returnData.toString();
    }

    public static int changeMonth(String month) {
        int nRet = 0;

        if(month!=null && !month.equals("")) {
            try {
                nRet = Integer.valueOf(   Double.toString((Double.valueOf(month)))  );
            }
            catch(Exception e) {
                String tmpMonth = month.toUpperCase();
                if (tmpMonth.endsWith("Y")) {
                    nRet = Integer.valueOf(   doubleTostring((Double.valueOf(tmpMonth.replace("Y", "")) * 12),0,"C" ));
                }
                else if (tmpMonth.endsWith("M")) {
                    nRet = Integer.valueOf(   doubleTostring((Double.valueOf(tmpMonth.replace("Y", "")) ),0,"C" ));
                }
            }
        }

        return nRet;
    }

    /**
     * 개월 수를 M 혹은 Y로 바꾸기 소주둘째자리까지 표현  (3 -> 3M,  16-> 1.50Y)
     * @param month 개월수
     * @return 연화 Y
     */
    public static String changeYear(int month) {
        return changeYear(month, false);
    }

    /**
     * 개월 수를 M 혹은 Y로 바꾸기  (3 -> 3M,  16-> 1.5Y)
     * @param month 개월수
     * @param isRemoveEndZero 소주점의 0 제거 여부
     * @return 연화 Y
     */
    public static String changeYear(int month, boolean isRemoveEndZero) {
        String sYear = "";
        if (month<12) {
            sYear = month + "M";
        }
        else {
            if (isRemoveEndZero) {
                String tmpYear =  NumberUtil.doubleTostring((month/12.0), 2);
                sYear =tmpYear.replaceAll("[.]?[0]*$", "") + "Y";       // 소수점 아래 0 지우기
            }
            else {
                sYear = NumberUtil.doubleTostring((month/12.0), 2) + "Y";
            }
        }
        return sYear;
     }


     public static String fillZero(String string, int scale) {
         String newVal = string;
         if (string == null) return string;

         int nSize = string.length();

         int nDiff = scale - nSize;
         for (int i=0; i<nDiff; i++) {
             newVal = "0" + newVal;
         }

         return newVal;
     }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////

        public static String toCurrency(double number) {
            DecimalFormat df = new DecimalFormat("#,###.00");
            return df.format(number);
        }

        public static String toCurrency(float number) {
            DecimalFormat df = new DecimalFormat("#,###.00");
            return df.format(number);
        }

        public static String toCurrency(long number) {
            DecimalFormat df = new DecimalFormat("#,###");
            return df.format(number);
        }

        public static String toCurrency(int number) {
            DecimalFormat df = new DecimalFormat("#,###");
            return df.format(number);
        }

        public static String toCurrency(String number) {
            return  number.indexOf('.') < 0 ?
                    toCurrency(Long.parseLong(number)) :
                    toCurrency(Double.parseDouble(number));
        }
}