package br.com.browseframework.util.hash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Generates a hash using the informed algorithm 
 * @author Eduardo
 *
 */
public class HashGenerator {

	/**
	 * Generates the second hash algorithm.
	 * @param value
	 * @param algorithm
	 * @return
	 */
	public static byte[] hashByte(String value, String algorithm) {
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			md.update(value.getBytes());
			return md.digest();
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
	
	/**
	 * Generates the hash algorithm and the second returns a String
	 * @param value
	 * @param algorithm
	 * @return
	 */
	public static String hashString(String value, String algorithm) {
		return convertHashToString(hashByte(value, algorithm));
	}

	/**
	 * Converts a byte array to text 
	 * @param bytes
	 * @return
	 */
	private static String convertHashToString(byte[] bytes) {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			int parteAlta = ((bytes[i] >> 4) & 0xf) << 4;
			int parteBaixa = bytes[i] & 0xf;
			if (parteAlta == 0)
				s.append('0');
			s.append(Integer.toHexString(parteAlta | parteBaixa));
		}
		return s.toString();
	} 
	 
}
