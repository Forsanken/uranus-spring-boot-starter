/**
 * FileName: TokenUtil
 * Author:   chy
 * Date:     2018/11/28 16:36
 * Description: token生成工具
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.uranus.framework.util;

import org.springframework.util.StringUtils;

/**
 * 〈一句话功能简述〉<br> 
 * 〈token生成工具〉
 *
 * @author chy
 * @create 2018/11/28
 * @since 1.0.0
 */
public class TokenUtil {

    /**
     * 通过MD5加密获得token
     * @param data 加密数据
     * @return String
     */
    public static String getTokenByMD5(String data) {
        if (StringUtils.isEmpty(data)) {
            return null;
        }
        return EncryptUtil.EncryptByMD5(data);
    }
}