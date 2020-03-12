package com.finger.fwf.server.batch;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.finger.fwf.core.task.FwfTasksLoader;
import com.finger.protocol.fwtp.FwtpMessage;

public class BatchExecuter extends TimerTask {
    private static Logger logger = null;

    private static boolean m_bRun = false;

    public BatchExecuter() {
        logger = Logger.getLogger(this.getClass());
    }

    public void run() {

        if (m_bRun) {
            logger.info("배치작업이 실행 중입니다. 완료 후 다시 실행시켜 주시기 바랍니다");
            return;
        }

        try {
            m_bRun = true;

            // 배치실행
            executeBatch();

            m_bRun = false;
        }
        catch (Exception ex) {
            m_bRun = false;
            logger.error(ex.getMessage(), ex);
        }
    }

    public void executeBatch() throws Exception {
        logger.info("##### 배치실행 - 시작 ::: " + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()) );

        FwtpMessage message = new FwtpMessage();
        message.setSysMgtNo("EXCHAIN");
        message.setTaskId("AL1010010");
        message.setUserId("SYS_BATCH");
        FwtpMessage response = FwfTasksLoader.loadTransTask(message);
        logger.debug(response);

        logger.info("##### 배치실행 - 종료 ::: " + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()) );
    }

}