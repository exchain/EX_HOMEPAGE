package com.finger.tools.util.lang;

import static com.finger.tools.util.lang.StrUtil.nvl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {

    /**
     * 입력일자(yyyyMMdd)에 대하여 가(감)산일수로 계산한 일자를 구한다.
     * @param inputDate 입력일자(yyyyMMdd)
     * @param addDays 가(감)일수
     * @return 일자(yyyyMMdd)
     */
    public static String dateAddDays(String inputDate, int addDays) {
        String rtnDate = "";
        try {
            Calendar calBase = new GregorianCalendar();
            calBase.setTime( (new SimpleDateFormat("yyyyMMdd")).parse( inputDate ) );
            calBase.add(Calendar.DATE, addDays );
            rtnDate = (new SimpleDateFormat("yyyyMMdd")).format( calBase.getTime() );
        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        return rtnDate;
    }

    /**
     * 두 날짜 사이의 일자를 배열로 반환한다.
     * 마지막 일자를 배열에 넣는 것을 기본으로 한다.
     * @param startDate 시작일자
     * @param endDate 종료일자
     * @return
     */
    public static String[] getBetweenDays(String startDate, String endDate) {
        return getBetweenDays(startDate, endDate, true);
    }
    public static String[] getBetweenDays(String startDate, String endDate, boolean isLastAdd) {
        ArrayList<String> arrList = new ArrayList<String>();

        /*String sRunDate = (new SimpleDateFormat("yyyy-MM-dd HH:mm:00")).format(new Date());*/
        /*SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);*/
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

        Date dateStart;
        Date dateEnd;
        try {
            dateStart = dateFormat.parse( startDate );
            dateEnd = dateFormat.parse( endDate );

            Calendar calBase = new GregorianCalendar();
            calBase.setTime( dateStart );
            while (dateEnd.after(calBase.getTime())) {
                arrList.add( (new SimpleDateFormat("yyyyMMdd")).format( calBase.getTime() ) );
                calBase.add(Calendar.DATE, 1 );
            }
            if (isLastAdd)
                arrList.add( (new SimpleDateFormat("yyyyMMdd")).format( calBase.getTime() ) );
        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        return arrList.toArray(new String[arrList.size()]);
    }

    /**
     * YMD Order 형식의 표기로된 일자를 정규표현식으로 변경한다.
     * @param inputDate 화면표기 형식으로 변경된 문자(8자)
     * @param ymdOrder 화면표기용 표현식(YMD,MDY,DMY)
     * @return 정규표기일자(YMD형식)
     */
    public static String dateOrderToRegular(String inputDate, String ymdOrder) {
        String rtnDate = inputDate;
        //--if (!StrUtil.isNull(inputDate) && !StrUtil.isNull(ymdOrder) && ymdOrder.length() == 3) {
        if (!StrUtil.isNull(inputDate) && !StrUtil.isNull(ymdOrder)) {
            //if (ymdOrder.indexOf("Y") > -1 && ymdOrder.indexOf("M") > -1 && ymdOrder.indexOf("D") > -1) {
                String[] regular = {"yyyy","MM","dd"};

                String[] dateFmtIn = new String[3];
                String[] dateFmtRe = new String[3];
                if (ymdOrder.indexOf(_Y_) > -1) { dateFmtIn[ymdOrder.indexOf(_Y_)] = regular[0]; dateFmtRe[0] = regular[0]; }
                if (ymdOrder.indexOf(_M_) > -1) { dateFmtIn[ymdOrder.indexOf(_M_)] = regular[1]; dateFmtRe[1] = regular[1]; }
                if (ymdOrder.indexOf(_D_) > -1) { dateFmtIn[ymdOrder.indexOf(_D_)] = regular[2]; dateFmtRe[2] = regular[2]; }
                Calendar calBase = new GregorianCalendar();
                try {
                    calBase.setTime( (new SimpleDateFormat(nvl(dateFmtIn[0])+nvl(dateFmtIn[1])+nvl(dateFmtIn[2]))).parse( inputDate ) );
                } catch (ParseException e) { e.printStackTrace(); }

                rtnDate = (new SimpleDateFormat(nvl(dateFmtRe[0])+nvl(dateFmtRe[1])+nvl(dateFmtRe[2]))).format( calBase.getTime() );
            //}
        }

        return rtnDate;
    }
    private static final String     _Y_     =   "Y";
    private static final String     _M_     =   "M";
    private static final String     _D_     =   "D";

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void main(String args[]) {
        System.out.println(">>>> dateAddDays(`20130325`, 7) = "+ dateAddDays("20130325", 7) );
        System.out.println(">>>> dateAddDays(`20130401`,-7) = "+ dateAddDays("20130401",-7) );
        System.out.println("");
/*        String[] days = getBetweenDays("20130325", "20130401");
        if (days != null)
            for (int ii = 0; ii < days.length; ii++) System.out.println("---> days["+ ii +"] = "+ days[ii] );
        System.out.println("");
        days = getBetweenDays("20130325", "20130401", false);
        if (days != null)
            for (int ii = 0; ii < days.length; ii++) System.out.println("===> days["+ ii +"] = "+ days[ii] );*/
        System.out.println(">>>> dateOrderToRegular(`20130401`,`YMD`) = "+ dateOrderToRegular("20130401","YMD") +"\n");
        System.out.println(">>>> dateOrderToRegular(`04012013`,`MDY`) = "+ dateOrderToRegular("04012013","MDY") +"\n");
        System.out.println(">>>> dateOrderToRegular(`06052013`,`DMY`) = "+ dateOrderToRegular("06052013","DMY") +"\n");
        System.out.println(">>>> dateOrderToRegular(`052013`,`MY`) = "+ dateOrderToRegular("052013","MY") +"\n");
    }
}