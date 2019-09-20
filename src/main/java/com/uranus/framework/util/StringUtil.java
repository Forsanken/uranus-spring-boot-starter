/**
 * FileName: StringUtil
 * Author:   chy
 * Date:     2019/9/2 15:27
 * Description: String 工具类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.uranus.framework.util;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 〈一句话功能简述〉<br> 
 * 〈String 工具类〉
 *
 * @author chy 2019/9/2
 * @since 1.0.0
 */
public class StringUtil {

    /**
     * 替换掉HTML标签方法
     * @param html html
     * @return String
     */
    public static String stripHtml(String html) {
        if (StringUtils.isBlank(html)){
            return "";
        }
        String regEx = "<.+?>";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(html);
        String s = m.replaceAll("");
        return s;
    }

    /**
     * 缩略字符串（不区分中英文字符）
     * @param str 目标字符串
     * @param length 截取长度
     * @return String
     */
    public static String abbr(String str, int length) {
        if (str == null) {
            return "";
        }
        try {
            StringBuilder sb = new StringBuilder();
            int currentLength = 0;
            for (char c : stripHtml(StringEscapeUtils.unescapeHtml4(str)).toCharArray()) {
                currentLength += String.valueOf(c).getBytes("GBK").length;
                if (currentLength <= length - 3) {
                    sb.append(c);
                } else {
                    sb.append("...");
                    break;
                }
            }
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * String 转Map
     * @param mapString 待转的String
     * @param separator 分割符
     * @param pairSeparator 分离器
     * @return Map<String, Object>
     */
    public static Map<String, Object> convertStringToMap(String mapString, String separator, String pairSeparator) {
        Map<String, Object> map = new HashMap<>();
        String[] fSplit = mapString.split(separator);
        for (String aFSplit : fSplit) {
            if (aFSplit == null || aFSplit.length() == 0) {
                continue;
            }
            String[] sSplit = aFSplit.split(pairSeparator);
            String value = aFSplit.substring(aFSplit.indexOf('=') + 1, aFSplit.length());
            map.put(sSplit[0], value);
        }
        return map;
    }
}