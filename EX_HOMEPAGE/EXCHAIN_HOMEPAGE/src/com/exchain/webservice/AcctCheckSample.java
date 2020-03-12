package com.exchain.webservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class AcctCheckSample {
	
	public static void main(String[] args) {


		openApiGetBank();
			//acctNameBirth();
			//acctName();

	}
	
	public static void openApiGetBank() {

		try {
			URL url = new URL("https://testapi.open-platform.or.kr/bank/status");		// 로컬
			//URL url = new URL("https://dev.2xchange.co.kr:9443/ASP/api/bank/EX0120");		// 개발
			//URL url = new URL("https://www.2xchange.co.kr/ASP/api/bank/EX000100");		// 운영
			
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			byte[] postDataBytes   = null;
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Authorization", "Bearer 08e28d74-664a-4cd8-91da-4a1b38a9aaea");
				conn.setDoOutput(true);
	

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
			//System.out.println("rst_name : " + rst_name);
			System.out.println("조회결과 : " + jsonObject);

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void acctNameBirth() {

		try {
			// 본인계좌 확인할 데이터 셋팅
			Map<String,Object> params = new LinkedHashMap<>(); 
			
			params.put("ex_cust_id" , "EXCHAIN");		// 고객아이디
			params.put("ex_svc_cd" 	, "0120");			// 서비스코드
			params.put("bank_cd" 	, "011");			// 은행코드
			params.put("acct_no"	, "36702017327");	// 계좌번호
			params.put("acct_nm"	, "달나라가자");		// 예금주명
			params.put("birthday"	, "661107");		// 생년월일

			StringBuilder paramData = new StringBuilder();
			for(Map.Entry<String,Object> param : params.entrySet()) 
			{
				if(paramData.length() != 0) 
			    	paramData.append('&');

			    paramData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
			    paramData.append('=');
			    paramData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
			}

			URL url = new URL("http://127.0.0.1:8080/ASP/api/bank/EX0120");		// 로컬
			//URL url = new URL("https://dev.2xchange.co.kr:9443/ASP/api/bank/EX0120");		// 개발
			//URL url = new URL("https://www.2xchange.co.kr/ASP/api/bank/EX000100");		// 운영
			
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			byte[] postDataBytes   = paramData.toString().getBytes("UTF-8");
			   
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

			//String rst_name = jsonObject.get("rst_name").getAsString();	// 조회된예금주명
			String rst_code = jsonObject.get("rst_code").getAsString();	// 결과코드
			String rst_msg  = jsonObject.get("rst_msg").getAsString(); 	// 결과메세지
			
			// 조회 결과 출력
			//System.out.println("rst_name : " + rst_name);
			System.out.println("rst_code : " + rst_code);
			System.out.println("rst_msg  : " + rst_msg);


			if (rst_code == "0000")	// 예금주명 일치  	
			{
				// 거래소 원장 반영처리
				
			}else						// 예금주명 불일치 
			{
				;
			}
			
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

				
	public static void acctName() {

		try {
			// 본인계좌 확인할 데이터 셋팅
			Map<String,Object> params = new LinkedHashMap<>(); 
			
			params.put("ex_cust_id" , "EXCHAIN");		// 고객아이디
			params.put("ex_svc_cd" 	, "0110");			// 서비스코드
			params.put("bank_cd", "088");			// 은행코드
			params.put("acct_no", "110211691190");	// 계좌번호
			params.put("acct_nm", "이성주 ");			// 고객명
			
			StringBuilder paramData = new StringBuilder();
			for(Map.Entry<String,Object> param : params.entrySet()) 
			{
				if(paramData.length() != 0) 
			    	paramData.append('&');

			    paramData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
			    paramData.append('=');
			    paramData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
			}

			URL url = new URL("http://127.0.0.1:8080/ASP/api/bank/EX0110");		// 로컬
			//URL url = new URL("https://dev.2xchange.co.kr:9443/ASP/api/bank/EX0110");		// 개발
			//URL url = new URL("https://www.2xchange.co.kr/ASP/api/bank/EX0110");		// 운영
			
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			byte[] postDataBytes   = paramData.toString().getBytes("UTF-8");
			   
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

			//String rst_name = jsonObject.get("rst_name").getAsString();	// 조회된예금주명
			String rst_code = jsonObject.get("rst_code").getAsString();	// 결과코드
			String rst_msg  = jsonObject.get("rst_msg").getAsString(); 	// 결과메세지
			
			// 조회 결과 출력
			//System.out.println("rst_name : " + rst_name);
			System.out.println("rst_code : " + rst_code);
			System.out.println("rst_msg  : " + rst_msg);
			
			if (rst_code == "0000")		// 예금주명 일치  	
			{
				// 거래소 원장 반영처리ㄴ
				
			}else						// 예금주명 불일치 
			{
				;
			}
			
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
