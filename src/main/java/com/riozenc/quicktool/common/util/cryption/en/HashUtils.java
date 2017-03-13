/**
 * Title:HashUtils.java
 * Author:czy
 * Datetime:2016年11月18日 下午4:10:04
 */
package com.riozenc.quicktool.common.util.cryption.en;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

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
	
	public static void main(String[] args) {
		try {
			
			System.out.println(Base64.getEncoder().encodeToString(HashUtils.getHash("SHA-512", "18660509556".getBytes("utf-8"),null,10)));
			System.out.println(Base64.getEncoder().encodeToString(HashUtils.getHash("SHA-512", "18660509556".getBytes("utf-8"),null,5)));
			System.out.println(Base64.getEncoder().encodeToString(HashUtils.getHash("SHA-512", "18660509556".getBytes("utf-8"),null,1)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
