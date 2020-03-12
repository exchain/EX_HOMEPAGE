package com.finger.agent.scrap.parser;

import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.finger.agent.scrap.entity.Entity;
import com.finger.agent.scrap.entity.ReceiveEntity;
import com.finger.agent.scrap.exception.ScrapingByException;
import com.finger.agent.scrap.service.CodeManager;
import com.finger.agent.scrap.service.ServiceCodeDataSet;
import com.finger.agent.scrap.service.ServiceField;

public class FWFDataHandler extends DataHandler {
	
	private Logger LOG = Logger.getLogger(FWFDataHandler.class);

    private ServiceCodeDataSet codeSet;

    public FWFDataHandler (Entity entity) {
        this.entity = entity;
    }

    public ReceiveEntity receiveEntity() throws ScrapingByException {
        ReceiveEntity receiveEntity = null;
        String serviceID = entity.getString("serviceID");
        String retCode = entity.getString("retCode");
        String revData = entity.getString("revData");
        int rowCnt = entity.getInt("rowCnt");
        String workCode = "";

        String headerSID = serviceID.substring(0, 2);

		/* 수정(20061110) : Bobby Shin : 서비스 코드 7자리로 변경
		      앞의 두자리가 TH(타행)일 경우 7자리의 xml을 parsing 한다
		      참고 : SID정의시 7자리의 xml을 사용하려면 PH + SID 로 정의하면 된다
		*/
		if(headerSID.equals("TH")){
			workCode = serviceID.substring(2, 9);
			codeSet = CodeManager.getInstance().getCode(workCode);
		}else{
		// TH가 아닐경우 기존방식대로  3자리의 xml을 parsing 한다
			workCode = serviceID.substring(0, 1)
					+ serviceID.substring(3, 5);
			codeSet = CodeManager.getInstance().getCode(workCode);
		}

        receiveEntity = new ReceiveEntity( serviceID, retCode, revData);
        Entity[] entities = createEntity(revData.getBytes(), rowCnt);
             
        if (rowCnt > 0) {
            receiveEntity.setEntity(entities[0]);
            receiveEntity.setEntityArray(entities);
        }

        return receiveEntity;
    }


    /**
     * 스크립트 서버에서 받은 데이터를 Entity에 레코드화 한다.
     * @param message
     * @param rowCount
     * @return
     * @throws ICMSServiceException
     */
    private Entity[] createEntity(byte[] message, int rowCount)
        throws ScrapingByException {
    	
        Entity[] entityArray = new Entity[rowCount];
        int pointer = 0;
        int totalLen = message.length;

        System.out.println(">>>>>>>>>>>>>>>> message "+ new String(message));

        try {
            for (int j=0; j < rowCount; j++) {
                Entity entity = new Entity();
                //XML에 output으로 정의 된 데이터에서 length만큼 잘른다.
                HashMap<?, ?> outfield = codeSet.getOutFields();

                for (Iterator<?> i = outfield.keySet().iterator(); i.hasNext();) {
                    String param = (String)i.next();
                    ServiceField serviceField = (ServiceField)outfield.get(param);
                    int length = serviceField.getLength();
                    
                    //if ( length > totalLen )
                    //    length = totalLen;

                    byte copy[] = new byte[length];
                    
                    System.arraycopy(message, pointer, copy, 0, length);
                     	
                    if ( totalLen > pointer + length )
                         pointer = pointer + length;
                    else
                         pointer = totalLen - pointer;
                    
                    // 데이터 보정. 2012-04-05 By 유정일 /////
                    String paramVal1 = new String(copy);
                    int cutSize = paramVal1.lastIndexOf("          "); // 뒤부터 10Byte 빈값  인덱스 위치를 찾는다.
                    if (cutSize < 0) cutSize = paramVal1.length();
                    String paramVal2 = paramVal1.substring(0, cutSize); // 10Byte 빈값을 제외한 실제값을 구한다.
                    String paramVal3 = paramVal1.substring(cutSize).replaceAll("          ", ""); // 잘못 짤려 붙은 문자열을 찾는다.
                    int dupSize = paramVal3.getBytes().length; // 잘못 짤려 붙은 문자열의 Byte 길이를 구한다.
                    entity.put(param, paramVal2.trim());
                    
                    pointer = pointer - dupSize; // pointer 위치를 정정해준다.
                }
                entityArray[j] = entity;
            }
        } catch (Exception e) {
        	LOG.debug(e.getStackTrace());
        	
        	StringBuffer buffer = new StringBuffer();
            HashMap<?, ?> outfield = codeSet.getOutFields();
            for (Iterator<?> i = outfield.keySet().iterator(); i.hasNext();) {
                String param = (String)i.next();
                ServiceField serviceField = (ServiceField)outfield.get(param);
                int length = serviceField.getLength();
                buffer.append("[FIELD_NAME="+param+",FIELD_LENGTH="+length+"]\n");
            }
            throw new ScrapingByException("[output data Entity 생성 실패하였습니다].\n " +
                                          " 수신 데이터["+new String(message)+"]\n" +
                                          " output format["+buffer.toString()+"]");
        }
        return entityArray;
    }

}