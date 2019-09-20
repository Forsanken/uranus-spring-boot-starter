package com.uranus.framework.util;

import org.springframework.util.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * <pre>
 * <p>Title: 文本标准化转义</p>
 * <p>Description: 基于时间的加密解密工具
 * 支持字母和常用字符和数字
 * 中文大部分支持(不建议)
 * base64加位运算</p>
 * <p>Company: xique.com</p>
 * </pre>
 * 
 */

@SuppressWarnings("restriction")
public final class EncryptBase64Util {
	private static BASE64Encoder base64en = new BASE64Encoder();
	private static BASE64Decoder base64de = new BASE64Decoder();

	/**
	 * 加密
	 * 
	 * @param str 加密字符串
	 * @return String
	 */
	public static String encryStr(String str) {
		if (StringUtils.isEmpty(str)) {
			throw new NullPointerException();
		}
		long t = DateTimeUtil.getMilliByTime(LocalDateTime.now());

		str = str + "|" + t;
		byte[] bytes = str.getBytes();
		byte[] rbytes = new byte[bytes.length];
		for (int i = 0; i < bytes.length; i++) {
			rbytes[i] = (byte) (bytes[i] ^ 0x0f);
		}
        return base64en.encode(rbytes);
	}

	/**
	 * 解密
	 * 
	 * @param str 解密字符串
	 * @return String
	 */
	public static String decryStr(String str) {
		if (StringUtils.isEmpty(str)) {
			throw new NullPointerException();
		}

		String result = baseDecryStr(str);
		if (result.contains("|")) {
			result = result.substring(0, result.lastIndexOf("|"));
		}
		return result;
	}

	private static String baseDecryStr(String str) {
		byte[] rbytes;
		try {
			rbytes = new String(base64de.decodeBuffer(str)).getBytes();
			byte[] rrbytes = new byte[rbytes.length];
			for (int i = 0; i < rbytes.length; i++) {
				rrbytes[i] = (byte) (rbytes[i] ^ 0x0f);
			}
			return new String(rrbytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 检查加密字符串是否正确
	 * 
	 * @param str
	 *            原始字符串
	 * @param encryStr
	 *            加密字符串
	 * @return boolean
	 */
	public static boolean check(String str, String encryStr) {
		if (StringUtils.isEmpty(str) || StringUtils.isEmpty(encryStr)) {
			throw new NullPointerException();
		}
		String de = decryStr(encryStr);
        return de.equals(str);
    }

	/**
	 * 检查加密字符串在有效时间是否正确(建议使用)
	 * 
	 * @param str
	 *            原始字符串
	 * @param encryStr
	 *            加密字符串
	 * @param overtime
	 *            加密码有效时间(单位 ms)
	 * @return boolean
	 */
	public static boolean timeBasedCheck(String str, String encryStr,
			long overtime) {
        if (StringUtils.isEmpty(str) || StringUtils.isEmpty(encryStr)) {
            throw new NullPointerException();
        }

        String result = baseDecryStr(encryStr);
        if (!result.contains("|")) {
            return false;
        }

        String de = result.substring(0, result.lastIndexOf("|"));
        long now = DateTimeUtil.getMilliByTime(LocalDateTime.now());
        long time = Long.parseLong(result.substring(result.lastIndexOf("|") + 1, result.length()));
        return de.equals(str) && (now - time) <= overtime;
    }

}
