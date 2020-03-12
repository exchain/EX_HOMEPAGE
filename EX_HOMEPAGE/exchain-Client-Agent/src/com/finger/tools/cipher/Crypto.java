package com.finger.tools.cipher;

import java.io.UnsupportedEncodingException;

import com.finger.tools.cipher.seed.Seed128Cipher;
import com.finger.tools.cipher.sha.MD5Cipher;
import com.finger.tools.util.lang.StrUtil;
import com.finger.tools.cipher.sha.Sha256Cipher;
import com.finger.agent.config.*;

public class Crypto {
    private static String	_Master_Key	=	"Finger-WebFrame.Exchain,Inc.";

    public static String encryptSD(String data) throws UnsupportedEncodingException {
        return encryptSD(data, null);
    }
    public static String encryptSD(String data, String charset) throws UnsupportedEncodingException {
        if (StrUtil.isNull(data)) return data;
        return Seed128Cipher.encrypt(data, _Master_Key.getBytes(), charset);
    }

    public static String decryptSD(String data) throws UnsupportedEncodingException {
        return decryptSD(data, null);
    }
    public static String decryptSD(String data, String charset) throws UnsupportedEncodingException {
        if (StrUtil.isNull(data)) return data;
        return Seed128Cipher.decrypt(data, _Master_Key.getBytes(), charset);
    }

    /*KISA_SEED_CBC*/
    public static String encryptSDCBC(String data) throws UnsupportedEncodingException {
        return encryptSDCBC(data, FcAgentConfig._CharSet_);
    }
    public static String encryptSDCBC(String data, String charset) throws UnsupportedEncodingException {
        if (StrUtil.isNull(data)) return data;
        return Seed128Cipher.encryptCBC(data, _Master_Key.getBytes(), charset);
    }

    public static String decryptSDCBC(String data) throws UnsupportedEncodingException {
        return decryptSDCBC(data, FcAgentConfig._CharSet_);
    }
    public static String decryptSDCBC(String data, String charset) throws UnsupportedEncodingException {
        if (StrUtil.isNull(data)) return data;
        return Seed128Cipher.decryptCBC(data, _Master_Key.getBytes(), charset);
    }
    
    public static String encryptMD5(String data) throws UnsupportedEncodingException {
        if (StrUtil.isNull(data)) return data;
        return MD5Cipher.encrypt(data, _Master_Key);
    }

    public static String encryptSHA256(String data) throws UnsupportedEncodingException {
        if (StrUtil.isNull(data)) return data;
        return Sha256Cipher.encrypt(data, _Master_Key);
    }
    
    public static void main(String args[]) {
        try {
            if (args == null || args.length < 1) {
            	 
                String orgData = "86fab89e329a3658a5e50d9244fa5eee113059154791b824452ce1d0e34272a1";
                
                System.out.println("["+ Crypto.encryptSDCBC(orgData)  +"]");
                
                String cipData = Crypto.encryptSDCBC(orgData);

                System.out.println("["+ Crypto.decryptSDCBC("IzlBwX46hCzBwQWUWCgiIg==")  +"]");
                
                
                System.out.println("Original Data ["+ orgData +"] => ENC ["+ Crypto.encryptSDCBC("SBDAPPSCO4R5UTUPMIPFYKDJG4MUAYQAXGGDW5JC5AEYM2UFEBOWFNQA")  +"]");
                System.out.println("Original Data ["+ orgData +"] => ENC ["+ Crypto.encryptSDCBC("SBUIN4X5JEIKT4E6O4E4WNBKG5SHHMO654OVINFT7RQE3Z7ZIIQ43CYS")  +"]");
                System.out.println("Original Data ["+ orgData +"] => ENC ["+ Crypto.encryptSDCBC("SCC5CN5U7XLXLBPKDL26SH4Z3SQOS6LC24SOA2EMNPMKM33NVSOSJMIZ")  +"]");
                System.out.println("Original Data ["+ orgData +"] => ENC ["+ Crypto.encryptSDCBC("SCNUGH4TCNQT5URHU5I2BPAWBWHC6VRWT3MXUH5PFYZ3Y3JPCIX56RY7")  +"]");
                System.out.println("Original Data ["+ orgData +"] => ENC ["+ Crypto.encryptSDCBC("SDN3SQV4C4IDBAVEYNV4AHX3QSOPMUFOZSA5D2UQKOVOA6FJTFSMNQYS")  +"]");

                /*
                 * 신한','4GtFy67QilDvj588DEy9zA==
					국민','fpnQD56ju/ZlwKscfGKV7g==
					하나','IrFTDNRs9IkGIVd1rM/yqA==
					기업','YFLcf6MlzAnWlE2UGnPCtQ==
					우리','0qI4eBug2vKKFbhUzB9YJQ==
					농협','AZnJhrgrklXGMoQNMHqE7g==

                 */
            }
            else {
                System.out.println(">> Encrypt ===> input ["+ args[0] +"] : encrypt ["+ Crypto.encryptSD(args[0]) +"]");
            }
        } catch (UnsupportedEncodingException e) {
        	e.printStackTrace();
        }
    }
}