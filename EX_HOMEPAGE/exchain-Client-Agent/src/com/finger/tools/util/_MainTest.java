package com.finger.tools.util;

import java.math.BigDecimal;

import com.finger.agent.util.AgentUtil;


public class _MainTest {

    public static void main(String args[]) {
        try {
            if (args == null || args.length < 1) {
                System.out.println("--------------------------------------------------------------");
                /*String[] aaaaa = {"smark_crpn_nm", "smark_crpn_xx", "smark_crpn_", "smark_crpn_nm_en", "smark_crpn_nm_ko"};
                for (int ii=0; ii<aaaaa.length; ii++) {
                    System.out.println(ii+"=> smark_crpn_nm :: "+ aaaaa[ii] +" | "+ aaaaa[ii].startsWith("smark_crpn_nm") );
                    System.out.println(ii+"=> smarkcrpn :: "+ aaaaa[ii] +" | "+ aaaaa[ii].startsWith("smarkcrpn") );
                }*/
/*                String param = "123,4:5-6,789.1234";
                System.out.println("[ 1 ]==> ["+ param +"]");
                System.out.println("[ 2 ]==> ["+ param.replaceAll("[-]", "" ) +"]");
                System.out.println("[ 3 ]==> ["+ param.replaceAll("[:]", ",") +"]");
                System.out.println("[ 4 ]==> ["+ param.indexOf(".") +"]");
                System.out.println("[ 5 ]==> ["+ param.replaceAll("[.]", ".") +"]");*/
/*                System.out.println("===>> InetAddress.getLocalHost().getHostAddress() :: "+ InetAddress.getLocalHost().getHostAddress() );
                System.out.println("===>> InetAddress.getLocalHost().getHostAddress() :: "+ InetAddress.getLocalHost().getHostAddress().indexOf("10.220.2") );
                System.out.println("===>> InetAddress.getLocalHost().getCanonicalHostName() :: "+ InetAddress.getLocalHost().getCanonicalHostName() );*/
/*                String textKeys = "DB00001_SMARK_BIZR_NM_KEY";
                String texts = "[＆1/＆2]＆3:123234-234234-234234";
                String[] arrText = textKeys.split("[|]");
                for (int ii = 0; ii < arrText.length; ii++) {
                    String text = arrText[ii];
                    texts = texts.replaceAll( "＆"+(ii+1), text );
                    System.out.println("---> ["+ ii +"] "+ texts );
                }*/

                String sDoubleValue = "00012345678901230";
                int length = Integer.parseInt("17");
                int pscale = Integer.parseInt("4");
                System.out.println("==>  sDoubleValue ["+ sDoubleValue +"]");
                System.out.println("==>  new BigDecimal() ["+ new BigDecimal(sDoubleValue) +"]");
                BigDecimal bigDecimalValue = new BigDecimal(sDoubleValue.substring(0, sDoubleValue.length()-pscale) +"."+ sDoubleValue.substring(sDoubleValue.length()-pscale));
                System.out.println("     -: pscale = "+ pscale +" :: ["+ bigDecimalValue.toString() +"]");
                System.out.println("");
                System.out.println("     -: length = "+ length +" :: ["+ bigDecimalValue.toString().substring(0,bigDecimalValue.toString().indexOf(".")) +"]");
                System.out.println("     -: pscale = "+ pscale +" :: ["+ bigDecimalValue.toString().substring(bigDecimalValue.toString().indexOf(".")+1) +"]");
                System.out.println("     -: fillNumericString :: ["+ AgentUtil.fillNumericString(bigDecimalValue.toString().substring(0,bigDecimalValue.toString().indexOf(".")), length-pscale) +"]");
                System.out.println("     -: fillNumericString :: ["+ AgentUtil.fillNumericString(bigDecimalValue.toString(), length, 0) +"]");
            }
            else {
                System.out.println("==============================================================");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}