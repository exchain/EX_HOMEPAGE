package com.finger.fwf.server.batch;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Timer;

import org.apache.log4j.Logger;

public class BatchManager {
    private static Logger logger = Logger.getLogger(BatchManager.class);;

    public BatchManager() { }

    public void runBatchManager() {

        // 스케쥴러로 실행되는 Daily BATCH
        Date runDate = getBatchNextRunTime();
        logger.info("==>> BATCH실행시작(매일05시) :: [ "+ runDate +" ]");

        runningBatchTask(runDate);
    }

    private Date getBatchNextRunTime() {

        Date dateNow = new Date();
        Date runningDate = new Date();

        //String sRunDate = (new SimpleDateFormat("yyyy-MM-dd HH:mm:00")).format(new Date());
        String sRunDate = (new SimpleDateFormat("yyyy-MM-dd 05:00:00")).format(new Date());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        try {
            runningDate = dateFormat.parse( sRunDate );
        } catch (ParseException e) {
            e.printStackTrace();
        }

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

    private void runningBatchTask(Date runDateTime) {
        BatchExecuter batchExecuter = new BatchExecuter();
        Timer jobScheduler = new Timer();

        jobScheduler.scheduleAtFixedRate(batchExecuter, runDateTime, 24*60*60*1000);  // 1일단위로 실행
        //jobScheduler.scheduleAtFixedRate(batchExecuter, runDateTime, 3*60*1000);  // 3분단위로 실행
    }
}