/**
 * Title:HashUtils.java
 * Author:czy
 * Datetime:2016年11月18日 下午4:10:04
 */
package com.riozenc.quicktool.common.util.cryption.en;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtils {

	public static byte[] getHash(String algorithmName, byte[] bytes, byte[] salt, int hashIterations) throws Exception {
		MessageDigest digest = getDigest(algorithmName);
		if (salt != null) {
			digest.reset();
			digest.update(salt);
		}
		byte[] hashed = digest.digest(bytes);
		int iterations = hashIterations - 1; // already hashed once above
		// iterate remaining number:
		for (int i = 0; i < iterations; i++) {
			digest.reset();
			hashed = digest.digest(hashed);
		}
		return hashed;
	}

	private static MessageDigest getDigest(String algorithmName) throws Exception {
		try {
			return MessageDigest.getInstance(algorithmName);
		} catch (NoSuchAlgorithmException e) {
			String msg = "No native '" + algorithmName + "' MessageDigest instance available on the current JVM.";
			throw new Exception(msg, e);
		}
	}

}
