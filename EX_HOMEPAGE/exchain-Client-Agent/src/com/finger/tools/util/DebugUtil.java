package com.finger.tools.util;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;

public class DebugUtil {
    private static Logger logger = Logger.getLogger(DebugUtil.class);

    @SuppressWarnings("unused")
    public static void printSqlList(List<LinkedHashMap <String, Object>> list) {

        int nListSize = list.size();
        logger.debug("-------------------------");
        logger.debug("List Data Length=" + (nListSize-3) );
        for(int i=3; i<nListSize; i++) {
            LinkedHashMap <String, Object> map = (LinkedHashMap <String, Object>) list.get(i);

            Iterator <String> iter = map.keySet().iterator();
            Object value = null;

            String key = "";

            StringBuffer sb = new StringBuffer();
            sb.append("i=");
            sb.append(i-2);

            int nPos = 0;
            while(iter.hasNext()) {
                nPos++;
                key =iter.next();
                value = map.get(key);

                if (value == null) {
                    value = "NULL";
                }

                sb.append(", ");
                sb.append(key);
                sb.append("=");
                sb.append(value.toString());

            }   // end while
            logger.debug(sb.toString());
        }
    }
}