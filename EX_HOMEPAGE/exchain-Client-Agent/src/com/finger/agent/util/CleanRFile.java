package com.finger.agent.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;

public class CleanRFile {
    private static Logger logger = Logger.getLogger(CleanRFile.class);

    public CleanRFile() { }

    public static int cleanRecvFileByKSNET(String cleanPath, String prefix, String todayYMD, int keepDays) {
        logger.debug((new StringBuilder("@@@ cleanPath [")).append(cleanPath).append("], prefix [").append(prefix).append("], todayYMD [").append(todayYMD).append("], keepDays [").append(keepDays).append("]").toString());

        int nDelCount = 0;

        File folder;
        folder = new File(cleanPath);
        if(!folder.exists())
            return nDelCount;

        try {
            Calendar calMargin = new GregorianCalendar();
            calMargin.setTime((new SimpleDateFormat("yyyyMMdd")).parse(todayYMD));
            calMargin.add(Calendar.DATE, -keepDays);
            int nMargin = Integer.parseInt((new SimpleDateFormat("yyyyMMdd")).format(calMargin.getTime()));
logger.debug((new StringBuilder("===>>  nMargin [")).append(nMargin).append("]").toString());
            if(folder.isDirectory())
            {
                File files[] = folder.listFiles();
                for(int ii = 0; ii < files.length; ii++) {
                    if(files[ii].isFile())
                    {
                        String fileNm = files[ii].getName();
                        if(fileNm != null && fileNm.indexOf(prefix) > -1)
                        {
                            int nStart = fileNm.indexOf(prefix) + prefix.length();
                            int nFileYMD = Integer.parseInt(fileNm.substring(nStart, nStart + 8));
                            if(nFileYMD < nMargin)
                            {
                                files[ii].delete();
                                if(++nDelCount == 1)
                                    logger.info((new StringBuilder("* Cleaning Path: ")).append(cleanPath).toString());
                                logger.info((new StringBuilder("* Deleting File: ")).append(fileNm).toString());
                            }
                        }
                        Thread.sleep(200L);
                    }
                }
            }
        } catch(Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        return nDelCount;
    }
}