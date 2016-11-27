/**
 * Title:WebPasswordUtils.java
 * Author:czy
 * Datetime:2016年11月18日 下午4:21:21
 */
package com.riozenc.quicktool.common.util.cryption.en;

import java.security.SecureRandom;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.Validate;

public class WebPasswordUtils {
	private static SecureRandom random = new SecureRandom();
	private static final int Iterations = 1024;

	// MD2 MD5 SHA-1 SHA-256 SHA-384 SHA-512
	private static final String ALGORITHM_NAME = "SHA-512";

	public static String encodePassword(String password) {
		try {
			byte[] salt = generateSalt(8);
			byte[] hash = HashUtils.getHash(ALGORITHM_NAME, password.getBytes(), salt, Iterations);
			return new String(Hex.encodeHex(salt)) + new String(Hex.encodeHex(hash));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	private static byte[] generateSalt(int numBytes) {
		Validate.isTrue(numBytes > 0, "numBytes argument must be a positive integer (1 or larger)", numBytes);

		byte[] bytes = new byte[numBytes];
		random.nextBytes(bytes);
		return bytes;
	}
}
