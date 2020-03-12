package com.finger.tools.util;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class CheckXml {
    private static Logger logger = Logger.getLogger(CheckXml.class);

    private static final String _SEPARATOR_ = System.getProperty("file.separator");

    public CheckXml() { }

    public boolean checkQueryXml(String folder) {

        File dirXml = new File( folder );
        if (dirXml.isDirectory()) {

            String[] arrDirJob = dirXml.list();
            int nDirJobSize = arrDirJob.length;

            boolean isSuccess = true;
            for (int i = 0; i < nDirJobSize; i++) {
                File dirJob = new File(folder +_SEPARATOR_+ arrDirJob[i]);
                if (dirJob.isDirectory()) {
                    if (arrDirJob[i].equals(".") || arrDirJob[i].equals("..")) continue;
                    logger.debug("Dir ... [" + arrDirJob[i] +"]");

                    String[] arrTasks = dirJob.list();
                    for (int j = 0; j < arrTasks.length; j++) {
                        if (arrTasks[j].equals(".") || arrTasks[j].equals("..")) continue;
                        logger.debug("........... ["+ arrTasks[j] +"]");

                        String strXmlFile = folder +_SEPARATOR_+ arrDirJob[i] +_SEPARATOR_+ arrTasks[j];

                        isSuccess = openXml(strXmlFile);
                        if (!isSuccess) {
                            return false;
                        }
                    }
                }
            }
            logger.debug("[~/sqlMap] All XML is OK !!!!");
        }

        return true;
    }

    @SuppressWarnings("unused")
    private boolean openXml(String xmlFilePath) {
        boolean bRet = true;
        try {
            SAXBuilder builder = new SAXBuilder();
            Document doc =  builder.build(xmlFilePath);
            logger.debug(xmlFilePath + " ... OK");
        }
        catch (JDOMException e) {
            e.printStackTrace();
            logger.debug(xmlFilePath + " ... Fail !!!");
            bRet = false;
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            logger.debug(xmlFilePath + " ... Fail !!!");
            bRet = false;
        }
        catch (IOException e) {
            e.printStackTrace();
            logger.debug(xmlFilePath + " ... Fail !!!");
            bRet = false;
        }
        finally {
        }

        return bRet;
    }
}