package com.security.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityUtil {

	private static Logger logger = LoggerFactory.getLogger(SecurityUtil.class);
	
	public static String hashToString(byte[] hash) {
		return Base64.encode(hash);
	}

	public static byte[] stringToHash(String string) {
		try {
			return Base64.decode(string);
		} catch (WSSecurityException e) {
			logger.error("Error decodeing base64: " + e.getMessage());
			return null;
		}
	}

	public static byte[] getHash(String password, byte[] salt)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest digest = MessageDigest.getInstance("SHA-1");
		digest.reset();
		digest.update(salt);
		return digest.digest(password.getBytes("UTF-8"));
	}

	public static byte[] createSalt() throws NoSuchAlgorithmException {
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
		byte[] salt = new byte[8];
		secureRandom.nextBytes(salt);
		return salt;
	}

	public static boolean authenticate(String digest, String enteredPassword,
			String salt) throws NoSuchAlgorithmException,
			UnsupportedEncodingException {
		byte[] bDigest = stringToHash(digest);
		byte[] bSalt = stringToHash(salt);
		return Arrays.equals(getHash(enteredPassword, bSalt), bDigest);
	}

}
