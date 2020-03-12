package com.finger.tools.cipher.sha;

import java.security.NoSuchAlgorithmException;

/**
 * SHA-256 algorithm to encrypt or decrypt the data is the class that provides the ability to.
 * @author devhome.tistory.com
 *
 */
public class Sha256Cipher {

	/**
	 * SHA-256 algorithm to encrypt the data.
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

			byte[] encrypt = MessageDigestEx.encrypt(data.getBytes(), bSalt, "SHA-256");

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
		System.out.println("SHA-256 :: ["+ Sha256Cipher.encrypt("abcdefghi1234567890", null) +"]");
		System.out.println("SHA-256 :: ["+ Sha256Cipher.encrypt("abcdefghi1234567890", "Exchain, inc") +"] salt AAAAA");
	}
}