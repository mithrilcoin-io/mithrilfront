package io.mithrilcoin.front.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Component;

@Component
public class HashingUtil {

	private static final String INIT_SALT = "playmoreplayaltmflf12)(";
	private static final String HASH_ALGORITHM = "SHA-256";

	private MessageDigest messageDigest;

	/**
	 * SHA 256 해시 처리 및 기본 솔트 값으로 스트링 해시된 값 반환 .
	 * 
	 * @param sourceString
	 * @return
	 */
	public String getHashedString(String sourceString) {
		String mixedString = INIT_SALT + sourceString;
		StringBuilder sb = new StringBuilder();
		try {
			messageDigest = MessageDigest.getInstance(HashingUtil.HASH_ALGORITHM);
			messageDigest.update(mixedString.getBytes("UTF-8"));
			byte[] bytes = messageDigest.digest();

			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}

		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e1) {
			
			e1.printStackTrace();
		}
		return sb.toString();
	}
	
	/**
	 * 
	 * @param sourceString 해시하고 싶은 문자열
	 * @param addSolt 추가 넣고 싶은 문자열
	 * @return
	 */
	public String getHashedString(String sourceString, String addSalt)
	{
		String mixedString = sourceString + addSalt;
		return getHashedString(mixedString);
	}

	public boolean compareRawStringWithHashString(String sourceString, String hashedString) {
		String hashedSource = getHashedString(sourceString);
		if (hashedSource.equals(hashedString)) {
			return true;
		}

		return false;
	}

}
