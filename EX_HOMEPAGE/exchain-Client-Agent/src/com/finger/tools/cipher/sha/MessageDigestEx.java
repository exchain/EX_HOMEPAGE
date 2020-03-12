package com.finger.tools.cipher.sha;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageDigestEx {

	private static final int    ITERATION_CNT = 5;
	private static final String SALT          = "0215449350";

	public static byte[] getSalt() {
		return SALT.getBytes();
	}

	public static byte[] encrypt(byte[] data, byte[] salt, String algorithm) throws NoSuchAlgorithmException {

		MessageDigest digest = MessageDigest.getInstance(algorithm);
		digest.reset();
		if( salt == null ) {
			digest.update(getSalt());
		} else {
			digest.update(salt);
		}

		for( int i = 0; i < ITERATION_CNT; i++ ) {
			data = digest.digest(data);
		}

		return data;
	}
}