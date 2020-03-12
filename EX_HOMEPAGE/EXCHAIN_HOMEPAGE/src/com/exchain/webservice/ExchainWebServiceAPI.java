package com.exchain.webservice;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import com.finger.agent.util.AgentUtil;


@Path("/api")
public class ExchainWebServiceAPI {

    private static Logger logger = null;

    private static String _SERVER1_IP_ = "http://13.125.188.188:9000/";
    private static String _SERVER2_IP_ = "http://211.232.21.107:9000/";
    
    public ExchainWebServiceAPI() {
        logger = Logger.getLogger(this.getClass());
    }
    
	/*
	 * 계좌 성명조회를 합니다.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/EXW0001")
	public String acctNameCheck(
			@QueryParam("cid")    String cid,
			@QueryParam("bankcd") String bankcd,
			@QueryParam("acctno") String acctno,
			@QueryParam("acctnm") String acctnm
			)
	{
		
		HashMap<String, Object> resultMap = null;

		logger.debug("=================================================================");
		logger.debug("본인계좌 검증 요청 데이터");
		logger.debug("cid    : " + cid);
		logger.debug("bankcd : " + bankcd);
		logger.debug("acctno : " + acctno);
		logger.debug("acctnm : " + acctnm);
		logger.debug("=================================================================");

		String ERROR_MSG = null;
		if(AgentUtil.isNull(cid))
			ERROR_MSG = "필수입력 데이터 검증실패[고객일련번호]";
		if(AgentUtil.isNull(bankcd) || bankcd.length() < 3)
			ERROR_MSG = "필수입력 데이터 검증실패[은행코드]";
		if(AgentUtil.isNull(acctno))
			ERROR_MSG = "필수입력 데이터 검증실패[계좌번호]";
		if(AgentUtil.isNull(acctnm))
			ERROR_MSG = "필수입력 데이터 검증실패[예금주명]";
		
		if(!AgentUtil.isNull(ERROR_MSG))
		{
			resultMap = new HashMap<String, Object>();
			resultMap.put("rcode", "99999999");
			resultMap.put("rname", "");
			resultMap.put("rmsg" , ERROR_MSG);
			
			return JSONUtil.getJsonStringFromMap(resultMap).toString();
		}
	
		return JSONUtil.getJsonStringFromMap(EXW0001(bankcd, acctno, acctnm)).toString();		
	}
	
    /**
     * 계좌인증
     * @param in
     * @return
     */
	private HashMap<String, Object> EXW0001(String bankcd, String acctno, String acctnm ) {

		HashMap<String, Object> resultMap = null;
		
		try {
			
			String insURL = _SERVER1_IP_ + "IFX1001" + "?bankcd=" + bankcd + "&acctnb=" + acctno +"&tranamt=&cmscd=&jiacctnb=";

			URL url = new URL(insURL);
			URLConnection connection = url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestProperty("CONTENT-TYPE","text/xml"); 
			
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(),"euc-kr"));

			String inputLine;
			String buffer = "";
			 
			while ((inputLine = br.readLine()) != null){
				buffer += inputLine.trim();
			}
			
			br.close();
			
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(buffer.toString())));
			NodeList rcodeNodes = doc.getElementsByTagName("RCODE");
			NodeList rnameNodes = doc.getElementsByTagName("RNAME");
			NodeList rmsgNodes 	= doc.getElementsByTagName("RMSG");
			
			String rcode = rcodeNodes.getLength() > 0? ((Element)rcodeNodes.item(0)).getAttribute("value") 	: "";  // 응답코드 (00000000:성공)
			String rname = rnameNodes.getLength() > 0? ((Element)rnameNodes.item(0)).getAttribute("value") 	: "";  // 계좌주
			String rmsg  = rmsgNodes.getLength()  > 0? ((Element)rmsgNodes.item(0)).getAttribute("value") 	: "";  // 오류메시지

			//- RCODE : 결과코드(XXXXXXXX:미조회, 00000000:성공, 그외 은행코드)
			//- RNAME : 수취인성명조회결과
			//- RMSG : 결과(오류)메시지(정상인 경우 빈값)
			
			resultMap = new HashMap<String, Object>();
			resultMap.put("rname", rname);
			resultMap.put("rmsg" , rmsg);
			resultMap.put("rcode", rcode);
			
			logger.debug("=================================================================");
			
			if ("00000000".equals(rcode)) { 
				if (rname.equals(acctnm)) 
					logger.debug("본인계좌 확인 조회결과 : 일치");
				else
					logger.debug("본인계좌 확인 조회결과 : 불일치");
			} else
				logger.debug("본인계좌 확인 조회결과 : 실패");

			logger.debug("=================================================================");
			
		} catch(Exception e)
		{
			logger.debug("ERROR - 본인계좌 확인중 오류가 발생하였습니다.");
    		e.printStackTrace();
    	}
		finally {
			if(resultMap == null)
			{
				resultMap = new HashMap<String, Object>(); 
				resultMap.put("rname", "");
				resultMap.put("rmsg" , "본인계좌 검증 오류");
				resultMap.put("rcode", "X0000002");
			}
		}	
		return resultMap;
	}   
}
