package com.exchain.webservice;
import java.util.Random;

public class ExCIDNumber {

	public static void main(String[] args) {
		
		// 예금주조회 서비스에서 허용할 고객식별번호를 채번 합니다.
		// EX + 5자리 영숫자
		Random rnd =new Random();

		StringBuffer buf =new StringBuffer();

		for(int i=0;i<5;i++){
		    if(rnd.nextBoolean()){
		        buf.append((char)((int)(rnd.nextInt(26))+97));
		    }else{
		        buf.append((rnd.nextInt(10))); 
		    }
		}
		
		System.out.println("EX"+buf.toString().toUpperCase());
	}

}
