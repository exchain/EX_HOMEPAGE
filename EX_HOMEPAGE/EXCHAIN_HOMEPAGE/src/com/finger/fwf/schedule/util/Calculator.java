package com.finger.fwf.schedule.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.finger.tools.util.lang.StrUtil;

public class Calculator {
    private static Logger logger = Logger.getLogger(Calculator.class);

    /**
     * 실행시작시간 구하기 - 반복실행 시
     * @param minute
     * @return
     */
    public static Date executeDate(String minute) {
        return executeDate( Integer.parseInt(StrUtil.nvl(minute, "0")), 0 );
    }
    public static Date executeDate(String minute, int delay) {
        return executeDate( Integer.parseInt(StrUtil.nvl(minute, "0")), delay );
    }
    public static Date executeDate(int minute, int delay) {
        Calendar calExecute =  new GregorianCalendar();
        calExecute.setTime( new Date() );
        calExecute.add( Calendar.MINUTE, minute );
        calExecute.add( Calendar.MINUTE, delay );

        return calExecute.getTime();
    }

    /**
     * 실행시작시간 구하기 - 일정시간실행 시
     * @param value
     * @return
     */
    public static Date nextRunTimeByDay(String value) {
        Date dateNow = new Date();
        Date runningDate = new Date();

        String hour = value.substring(2,4);
        String minute = value.substring(4,6);
        //String sRunDate = (new SimpleDateFormat("yyyy-MM-dd HH:mm:00")).format(new Date());
        String sRunDate = (new SimpleDateFormat("yyyy-MM-dd "+hour+":"+minute+":00")).format(new Date());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        logger.debug("### sRunDate ==>["+ sRunDate +"]");
        try {
            runningDate = dateFormat.parse( sRunDate );
        } catch (ParseException pex) { pex.printStackTrace(); }

        Date nextRunTime = null;
        if (dateNow.before(runningDate)) {
            nextRunTime = runningDate;
        }
        else {
            Calendar calTomorrow =  new GregorianCalendar();
            calTomorrow.setTime(runningDate);
            calTomorrow.add(Calendar.DATE, 1 );  // 이전시간이면 1일 더하기
            nextRunTime = calTomorrow.getTime();
        }

        return nextRunTime;
    }

    public static void main(String args[]) {
        System.out.println("==========================================");

        String sRunDate = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA);
        System.out.println("### sRunDate ==>["+ sRunDate +"]");
        sRunDate = "20130207160341406334".substring(0, 14);
        try {
            Date runningDate = dateFormat.parse( sRunDate );
            System.out.println("    runningDate ==> "+ runningDate );
        } catch (ParseException pex) { pex.printStackTrace(); }

    }
}