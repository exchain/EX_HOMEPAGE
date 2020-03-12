package com.finger.tools.dbm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.finger.agent.msg.FixedMsg;
import com.finger.protocol.fwtp.FwtpMessage;
import com.finger.tools.util.ThrowTraceInfo;

/**
 * 요청에 필요한 Meta 정보 및 Data 를 FwtpMessage에 쉽게 설정하도록 돕는 Class
 */

public class QueryExecutor {
    private static Logger logger = Logger.getLogger(QueryExecutor.class);

    public QueryExecutor() {}

    /**
     * FwtpMessage에 결과를 셋팅한다
     * @param fpmsg FwtpMessage
     * @param list  iBatis Result List
     * @return 성공여부
     */
    public boolean setResult (FwtpMessage fpmsg, List <LinkedHashMap<String,Object>> list) {
        return setResult(fpmsg, list, false);
    }

    public boolean setResult2 (FwtpMessage fpmsg, List <LinkedHashMap<String,String>> list) {
        return setResult2(fpmsg, list, false);
    }
    
    public boolean setResult (FwtpMessage fpmsg, List<LinkedHashMap<String,Object>> list, boolean isAutoColName) {
        boolean bRet = true;

        try {
            logger.debug("~~~~> list.size() = "+ list.size() );

            Iterator<LinkedHashMap<String,Object>> iterMap = list.iterator();

            String [] arOrgColName = null;      // 원래의 컬럼이름

            String [] arColName = null;         // 컬럼 이름
            String [] arColType = null;         // 컬럼 타입
            String [] arColSize = null;         // 컬럼 사이즈

            int nFieldCount = 0;
            if (iterMap.hasNext()) {
                LinkedHashMap<String, Object> mapFieldName = iterMap.next();
                nFieldCount = mapFieldName.keySet().size();
                arOrgColName = (String[]) mapFieldName.keySet().toArray(new String[nFieldCount]);
                if (isAutoColName) {
                    String strColName = "";
                    arColName = new String[nFieldCount];
                    for (int i=0; i<nFieldCount; i++) {
                        strColName = "COL_"+ (i+1);
                        arColName[i] = strColName;
                    }
                }
                else {
                    arColName = (String[]) mapFieldName.keySet().toArray(new String[nFieldCount]);
                }
            }
            if (iterMap.hasNext()) {
                LinkedHashMap<String, Object> mapFieldType = iterMap.next();
                arColType = new String [nFieldCount];
                for(int i=0; i<nFieldCount; i++) {
                    arColType[i] = (String) mapFieldType.get(arOrgColName[i]);
                }
            }
            if (iterMap.hasNext()) {
                LinkedHashMap<String, Object> mapFieldSize = iterMap.next();
                arColSize = new String [nFieldCount];
                for(int i=0; i<nFieldCount; i++) {
                    arColSize[i] = (String) mapFieldSize.get(arOrgColName[i]);
                }
            }

            // "COM" 영역 생성하기
            fpmsg.setResponseTableInfo(arColName, arColType, arColSize);

            // Data 영역 생성하기
            int nRecordCnt = 0;
            Object objValue = null;
            while (iterMap.hasNext()) {
                int i=0;
                try {
                    LinkedHashMap<String, Object> mapRow = iterMap.next();
                    for( ; i<nFieldCount; i++) {
                        objValue = mapRow.get(arOrgColName[i]);
                        if (objValue==null) objValue = "";
                        fpmsg.setResponseField(nRecordCnt, arColName[i], objValue.toString());
                    }
                    nRecordCnt++;
                }
                catch(Exception e){
                    logger.error("error:"+ nRecordCnt + " i="+ i + " "+ e.toString());
                    bRet = false;
                }
            }

            // Class, LineNumber 설정
            ThrowTraceInfo li = new ThrowTraceInfo(new Throwable(), 2);
            String className = li.getClassName();
            String lineNumber  = Integer.toString(li.getLineNumber());

            fpmsg.setThrowClassName( className );
            fpmsg.setThrowLineNumber( lineNumber );

            // 사용자에게 전달되는 메세지코드 셋팅
            if (fpmsg.getResponseCode().equals(FwtpMessage._RESPONSE_OKAY_)) {
                fpmsg.setMessageCode(FixedMsg._MSG_CODE_00001);  //정상적으로 조회가 완료되었습니다
            }
            else {
                fpmsg.setMessageCode(FixedMsg._MSG_CODE_90001);  //조회 시 오류가 발생하였습니다
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
            logger.error(ex.toString(), ex);
            bRet = false;
        }

        fpmsg.setTimeStamp();   // 서버타임으로  셋팅한다

        return bRet;
    }
    
    public boolean setResult2 (FwtpMessage fpmsg, List<LinkedHashMap<String,String>> list, boolean isAutoColName) {
        boolean bRet = true;

        try {
            logger.debug("~~~~> list.size() = "+ list.size() );

            Iterator<LinkedHashMap<String,String>> iterMap = list.iterator();

            String [] arOrgColName = null;      // 원래의 컬럼이름

            String [] arColName = null;         // 컬럼 이름
            String [] arColType = null;         // 컬럼 타입
            String [] arColSize = null;         // 컬럼 사이즈

            int nFieldCount = 0;
            if (iterMap.hasNext()) {
                LinkedHashMap<String, String> mapFieldName = iterMap.next();
                nFieldCount = mapFieldName.keySet().size();
                arOrgColName = (String[]) mapFieldName.keySet().toArray(new String[nFieldCount]);
                if (isAutoColName) {
                    String strColName = "";
                    arColName = new String[nFieldCount];
                    for (int i=0; i<nFieldCount; i++) {
                        strColName = "COL_"+ (i+1);
                        arColName[i] = strColName;
                    }
                }
                else {
                    arColName = (String[]) mapFieldName.keySet().toArray(new String[nFieldCount]);
                }
            }
            if (iterMap.hasNext()) {
                LinkedHashMap<String, String> mapFieldType = iterMap.next();
                arColType = new String [nFieldCount];
                for(int i=0; i<nFieldCount; i++) {
                    arColType[i] = (String) mapFieldType.get(arOrgColName[i]);
                }
            }
            if (iterMap.hasNext()) {
                LinkedHashMap<String, String> mapFieldSize = iterMap.next();
                arColSize = new String [nFieldCount];
                for(int i=0; i<nFieldCount; i++) {
                    arColSize[i] = (String) mapFieldSize.get(arOrgColName[i]);
                }
            }

            // "COM" 영역 생성하기
            fpmsg.setResponseTableInfo(arColName, arColType, arColSize);

            // Data 영역 생성하기
            int nRecordCnt = 0;
            Object objValue = null;
            while (iterMap.hasNext()) {
                int i=0;
                try {
                    LinkedHashMap<String, String> mapRow = iterMap.next();
                    for( ; i<nFieldCount; i++) {
                        objValue = mapRow.get(arOrgColName[i]);
                        if (objValue==null) objValue = "";
                        fpmsg.setResponseField(nRecordCnt, arColName[i], objValue.toString());
                    }
                    nRecordCnt++;
                }
                catch(Exception e){
                    logger.error("error:"+ nRecordCnt + " i="+ i + " "+ e.toString());
                    bRet = false;
                }
            }

            // Class, LineNumber 설정
            ThrowTraceInfo li = new ThrowTraceInfo(new Throwable(), 2);
            String className = li.getClassName();
            String lineNumber  = Integer.toString(li.getLineNumber());

            fpmsg.setThrowClassName( className );
            fpmsg.setThrowLineNumber( lineNumber );

            // 사용자에게 전달되는 메세지코드 셋팅
            if (fpmsg.getResponseCode().equals(FwtpMessage._RESPONSE_OKAY_)) {
                fpmsg.setMessageCode(FixedMsg._MSG_CODE_00001);  //정상적으로 조회가 완료되었습니다
            }
            else {
                fpmsg.setMessageCode(FixedMsg._MSG_CODE_90001);  //조회 시 오류가 발생하였습니다
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
            logger.error(ex.toString(), ex);
            bRet = false;
        }

        fpmsg.setTimeStamp();   // 서버타임으로  셋팅한다

        return bRet;
    }    

    public List<LinkedHashMap<String,Object>> makeList (String fieldName, Object objValue) {
         List<LinkedHashMap<String,Object>> list = new ArrayList<LinkedHashMap<String,Object>>();
         LinkedHashMap <String, Object> mapName = new LinkedHashMap<String,Object>();
         LinkedHashMap <String, Object> mapType = new LinkedHashMap<String,Object>();
         LinkedHashMap <String, Object> mapSize = new LinkedHashMap<String,Object>();
         LinkedHashMap <String, Object> mapData = new LinkedHashMap<String,Object>();
         mapName.put(fieldName, fieldName.toUpperCase());
         mapType.put(fieldName, "VARCHAR");
         if (objValue == null) {
             mapSize.put(fieldName, "1");
             mapData.put(fieldName, "");
         }
         else {
             mapSize.put(fieldName, Integer.toString((objValue.toString()).length()));
             mapData.put(fieldName, objValue);
         }

         list.add(mapName);
         list.add(mapType);
         list.add(mapSize);
         list.add(mapData);

         return list;
    }
}