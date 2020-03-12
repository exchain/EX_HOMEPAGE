package com.finger.tools.cipher.seed;

import java.io.UnsupportedEncodingException;

import com.finger.tools.cipher.base64.Base64;
import com.finger.tools.cipher.padding.BlockPadding;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
/**
 * SEED algorithm to encrypt or decrypt the data is the class that provides the ability to.
 * @author devhome.tistory.com
 *
 */
public class Seed128Cipher {

	/**
	 * SEED encryption algorithm block size
	 */
	private static final int SEED_BLOCK_SIZE = 16;

	/**
	 * SEED algorithm to encrypt the data.
	 * @param data Target Data
	 * @param key Masterkey
	 * @param charset Data character set
	 * @return Encrypted data
	 * @throws UnsupportedEncodingException If character is not supported
	 */
	public static String encrypt(String data, byte[] key, String charset) throws UnsupportedEncodingException {

		byte[] encrypt = null;
		if( charset == null ) {
			encrypt = BlockPadding.getInstance().addPadding(data.getBytes(), SEED_BLOCK_SIZE);
		} else {
			encrypt = BlockPadding.getInstance().addPadding(data.getBytes(charset), SEED_BLOCK_SIZE);
		}

		int pdwRoundKey[] = new int[32];
		SEED128.SeedRoundKey(pdwRoundKey, key);

		int blockCount = encrypt.length / SEED_BLOCK_SIZE;
		for( int i = 0; i < blockCount; i++ ) {

			byte sBuffer[] = new byte[SEED_BLOCK_SIZE];
			byte tBuffer[] = new byte[SEED_BLOCK_SIZE];

			System.arraycopy(encrypt, (i * SEED_BLOCK_SIZE), sBuffer, 0, SEED_BLOCK_SIZE);

			SEED128.SeedEncrypt(sBuffer, pdwRoundKey, tBuffer);

			System.arraycopy(tBuffer, 0, encrypt, (i * SEED_BLOCK_SIZE), tBuffer.length);
		}

		return Base64.toString(encrypt);
	}

	/**
	 * ARIA algorithm to decrypt the data.
	 * @param data Target Data
	 * @param key Masterkey
	 * @param charset Data character set
	 * @return Decrypted data
	 * @throws UnsupportedEncodingException If character is not supported
	 */
	public static String decrypt(String data, byte[] key, String charset) throws UnsupportedEncodingException {

		int pdwRoundKey[] = new int[32];
		SEED128.SeedRoundKey(pdwRoundKey, key);

		byte[] decrypt = Base64.toByte(data);
		int blockCount = decrypt.length / SEED_BLOCK_SIZE;
		for( int i = 0; i < blockCount; i++ ) {

			byte sBuffer[] = new byte[SEED_BLOCK_SIZE];
			byte tBuffer[] = new byte[SEED_BLOCK_SIZE];

			System.arraycopy(decrypt, (i * SEED_BLOCK_SIZE), sBuffer, 0, SEED_BLOCK_SIZE);

			SEED128.SeedDecrypt(sBuffer, pdwRoundKey, tBuffer);

			System.arraycopy(tBuffer, 0, decrypt, (i * SEED_BLOCK_SIZE), tBuffer.length);
		}

		if( charset == null ) {
			return new String(BlockPadding.getInstance().removePadding(decrypt, SEED_BLOCK_SIZE));
		} else {
			return new String(BlockPadding.getInstance().removePadding(decrypt, SEED_BLOCK_SIZE), charset);
		}
	}

	/**
	 * SEED algorithm to encrypt the data.
	 * @param data Target Data
	 * @param key Masterkey
	 * @param charset Data character set
	 * @return Encrypted data
	 * @throws UnsupportedEncodingException If character is not supported
	 */
	public static String encryptCBC(String data, byte[] key, String charset) throws UnsupportedEncodingException {

		byte[] enc = null;
		byte bszIV[] = new byte[SEED_BLOCK_SIZE];
		
		try {
			//암호화 함수 호출
			enc = KISA_SEED_CBC.SEED_CBC_Encrypt(key, bszIV, data.getBytes(charset), 0,
					data.getBytes(charset).length);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return Base64.toString(enc);
	}

	/**
	 * ARIA algorithm to decrypt the data.
	 * @param data Target Data
	 * @param key Masterkey
	 * @param charset Data character set
	 * @return Decrypted data
	 * @throws UnsupportedEncodingException If character is not supported
	 */
	public static String decryptCBC(String data, byte[] key, String charset) throws UnsupportedEncodingException {

		byte bszIV[] = new byte[SEED_BLOCK_SIZE];
		byte[] enc = Base64.toByte(data); 
		String result = "";
		byte[] dec = null;

		try {
			//복호화 함수 호출
			dec = KISA_SEED_CBC.SEED_CBC_Decrypt(key, bszIV, enc, 0, enc.length);
			result = new String(dec, charset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * The sample code in the Cipher class
	 * @param args none
	 */
	public static void main(String args[]) {
		try {
			byte[] key = new byte[32];
			for( int i = 0; i < key.length; i++ ) {
				key[i] = (byte)i;
			}
			String data = "한글AASDFASG_1234567890가나12345";

			data = Seed128Cipher.encrypt(data, key, null);
			System.out.println(data);
			data = Seed128Cipher.decrypt(data, key, null);
			System.out.println(data);
		} catch(Exception e) {
			System.out.println("E:" + e.getMessage());
		}
	}
}