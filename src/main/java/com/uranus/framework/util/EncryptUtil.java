package com.uranus.framework.util;

import org.springframework.util.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public final class EncryptUtil {

	private final static char[] hexDigits = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	/**
	 * 通过MD5加密字符串
	 * @param str
	 *            需要被加密的字符串
	 * @return 对字符串str进行MD5加密后，将加密字符串返回
	 * 
	 */
	public static String EncryptByMD5(String str) {
		return EncryptStr(str, "MD5");
	}

	/**
	 * 返回16位MD5串
	 * 
	 * @param str 字符串
	 * @return String 密文
	 */
	public static String EncryptByMD5Bit16(String str) {
		String encrypt = EncryptStr(str, "MD5");
		if (StringUtils.isEmpty(encrypt)) {
		    return null;
        }
		return bytesToHex(encrypt.getBytes()).substring(8, 24);
	}

	/**
	 * 通过SHA1加密字符串
	 * @param str
	 *            需要被加密的字符串
	 * @return 对字符串str进行SHA-1加密后，将加密字符串返回
	 * 
	 */
	public static String EncryptBySHA1(String str) {
		return EncryptStr(str, "SHA-1");
	}

	/**
	 * 通过SHA256加密字符串
	 * @param str
	 *            需要被加密的字符串
	 * @return 对字符串str进行SHA-256加密后，将加密字符串返回
	 * 
	 */
	public static String EncryptBySHA256(String str) {
		return EncryptStr(str, "SHA-256");
	}


    /**
     * 具体算法
     * @param strSrc
     *            需要被加密的字符串
     * @param encName
     *            加密方式，有 MD5、SHA-1和SHA-256 这三种加密方式
     * @return 返回加密后的字符串
     */
    private static String EncryptStr(String strSrc, String encName) {
        MessageDigest md;
        String strDes;
        byte[] bt = strSrc.getBytes();
        try {
            encName = Optional.ofNullable(encName).orElse("MD5");
            md = MessageDigest.getInstance(encName);
            md.update(bt);
            strDes = bytes2Hex(md.digest()); // to HexString
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }

	/**
     *
	 * 返回加密字符串
	 * @param bts md5后的数据
	 * @return String
	 */
	private static String bytes2Hex(byte[] bts) {
		StringBuilder des = new StringBuilder();
		String tmp;
        for (byte bt : bts) {
            tmp = (Integer.toHexString(bt & 0xFF));
            if (tmp.length() == 1) {
                des.append("0");
            }
            des.append(tmp);
        }
		return des.toString();
	}

    /**
     * 返回Hex字符串
     * @param bytes 加密后的MD5串
     * @return String
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        int t;
        for (int i = 0; i < 16; i++) {
            t = bytes[i];
            if (t < 0)
                t += 256;
            sb.append(hexDigits[(t >>> 4)]);
            sb.append(hexDigits[(t % 16)]);
        }
        return sb.toString();
    }

	/**
	 * 通过秘钥和MD5加密str
	 *
	 * @param str
	 *            要加密的字符串
	 * @param key
	 *            加密的key
	 * @return String
	 */
	public static String encrypt(String str, String key) {
	    if (StringUtils.isEmpty(str)) {
	        return null;
        }
		if (StringUtils.isEmpty(StringUtils.trimAllWhitespace(key))) {
			return encrypt(str);
		}
		return encrypt(union(str, key));
	}

    /**
     * 先将str进行一次MD5加密，
     * 加密后再取加密后的字符串的第1、3、5个字符追加到加密串，
     * 再拿这个加密串进行加密
     *
     * @param str 字符串
     * @return string
     */
    private static String encrypt(String str) {
        String encryptStr = EncryptByMD5(str);
        if (encryptStr != null) {
            encryptStr = encryptStr + encryptStr.charAt(0)
                    + encryptStr.charAt(2) + encryptStr.charAt(4);
            encryptStr = EncryptByMD5(encryptStr);
        }
        return encryptStr;
    }

    /**
     * 对字符串进行加密混合
     * @param str 字符串
     * @param key 秘钥
     * @return String
     */
    private static String union(String str, String key) {
        int strLen = str.length();
        int keyLen = key.length();
        Character[] s = new Character[strLen + keyLen];

        boolean flag = true;
        int strIdx = 0;
        int keyIdx = 0;
        for (int i = 0; i < s.length; i++) {
            if (flag) {
                if (strIdx < strLen) {
                    s[i] = str.charAt(strIdx);
                    strIdx++;
                }
                if (keyIdx < keyLen) {
                    flag = false;
                }

            } else {
                if (keyIdx < keyLen) {
                    s[i] = key.charAt(keyIdx);
                    keyIdx++;
                }
                if (strIdx < strLen) {
                    flag = true;
                }

            }
        }
        return StringUtils.arrayToDelimitedString(s,"");
    }

}
