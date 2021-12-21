package de.symeda.sormas.api.utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.validation.ValidationException;

public final class PasswordHelper {

	private PasswordHelper() {
		// Hide Utility Class Constructor
	}

	private static final char[] PASSWORD_CHARS = new char[26 - 2 + 26 - 3 + 8];
	static {
		int i = 0;
		for (char ch = 'a'; ch <= 'z'; ch++) {
			switch (ch) {
			case 'l':
				continue;
			case 'v':
				continue;
			default:
				PASSWORD_CHARS[i++] = ch;
			}
		}
		for (char ch = 'A'; ch <= 'Z'; ch++) {
			switch (ch) {
			case 'I':
				continue;
			case 'O':
				continue;
			case 'V':
				continue;
			default:
				PASSWORD_CHARS[i++] = ch;
			}
		}
		for (char ch = '2'; ch <= '9'; ch++) {
			PASSWORD_CHARS[i++] = ch;
		}

		if (i != PASSWORD_CHARS.length) {
			throw new ValidationException("Size of password char array does not match defined values.");
		}
	}

	public static String createPass(final int length) {

		SecureRandom rnd = new SecureRandom();

		char[] chs = new char[length];
		for (int i = 0; i < length; i++)
			chs[i] = PASSWORD_CHARS[rnd.nextInt(PASSWORD_CHARS.length)];
		final String val = new String(chs);

		return val;
	}

	public static String encodePassword(String password, String seed) {

		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
			digest.reset();
			byte[] digested = digest.digest((password + seed).getBytes(StandardCharsets.UTF_8));
			String encoded = hexEncode(digested);

			return encoded;

		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public static String hexEncode(byte[] aData) {
		return new BigInteger(1, aData).toString(16);
	}
}
