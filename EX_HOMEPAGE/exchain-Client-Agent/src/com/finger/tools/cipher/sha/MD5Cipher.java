package com.finger.tools.cipher.sha;

import java.security.NoSuchAlgorithmException;

public class MD5Cipher {

    /**
     * MD5 algorithm to encrypt the data.
     * @param data Target Data
     * @param salt Masterkey
     * @return Encrypted data
     */
    public static String encrypt(String data, String salt) {
        try {

            byte[] bSalt = null;
            if( salt != null ) {
                bSalt = salt.getBytes();
            }

            byte[] encrypt = MessageDigestEx.encrypt(data.getBytes(), bSalt, "MD5");

            StringBuffer buff = new StringBuffer();
            for( int i = 0; i < encrypt.length; i++ ) {
                String hex = Integer.toHexString( encrypt[i] & 0xFF ).toUpperCase();
                if( hex.length() == 1 ) {
                    buff.append("0");
                }
                buff.append(hex);
            }
            return buff.toString();

        } catch(NoSuchAlgorithmException e) {
            // Never
            return null;
        }
    }

    public static void main(String args[]) {
        System.out.println(""+ MD5Cipher.encrypt("abcdefghi12345678sdfoisudfosuidflsdjflsdjkfsdofiusdoifusdoflskjdfsdofiusdoifusd", null) );
        System.out.println(""+ MD5Cipher.encrypt("abcdefghi12345678sdfoisudfosuidflsdjflsdjkfsdofiusdoifusdoflskjdfsdofiusdoifusd", "Finger-WebFrame") );
    }
}