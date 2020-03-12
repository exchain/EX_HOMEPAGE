package com.exchain.webservice;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class RestApiTest {
	
	public static void main(String[] args) {

		try {
			
			// 조회요청할 데이터 셋팅
	        Map<String,Object> params = new LinkedHashMap<>(); 
	        
	        params.put("cid"   , "CID0001");		// 고객번호
	        params.put("bankcd", "088");			// 은행코드
	        params.put("acctno", "110211691190");	// 계좌번호
	        params.put("acctnm", "이성주");			// 예금주명
	 
	        StringBuilder paramData = new StringBuilder();
	        
	        for(Map.Entry<String,Object> param : params.entrySet()) 
	        {
	            if(paramData.length() != 0) 
	            	paramData.append('&');

	            paramData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
	            paramData.append('=');
	            paramData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
	        }
	        
	        /* GET 방식
	        URL url = new URL("http://112.216.70.218:8080/ASP/api/bank/EX000100?"  +paramData.toString());
	        
	        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	        conn.setRequestMethod("GET");
	        conn.setRequestProperty("Accept", "application/json");
	        */
	        
	        // POST 방식
	        URL url = new URL("https://dev.2xchange.co.kr:9443/ASP/api/bank/EX000100");		// 개발
	        byte[] postDataBytes = paramData.toString().getBytes("UTF-8");
	        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	        conn.setRequestMethod("POST");
	        conn.setRequestProperty("Accept", "application/json");
	        conn.setDoOutput(true);
	        conn.getOutputStream().write(postDataBytes);
	        
	        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
	 
	        StringBuffer sb = new StringBuffer();
	        String data;
	        
	        while((data = br.readLine()) != null) { 
	        	 sb.append(data);
	        }
	        
	        br.close();

	        // 조회 결과 파싱
			JsonParser jsonParser = new JsonParser();
			JsonObject jsonObject = (JsonObject) jsonParser.parse(sb.toString()).getAsJsonObject();

	        // 조회 결과 출력
	        System.out.println("rname : " + jsonObject.get("rname").getAsString());
	        System.out.println("rcode : " + jsonObject.get("rcode").getAsString());
	        System.out.println("rmsg  : " + jsonObject.get("rmsg").getAsString());
	        
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
