package com.uranus.framework.util;

import javax.servlet.http.HttpServletRequest;

/**
 * @author michael <br>
 *         blog: http://sjsky.iteye.com <br>
 *         mail: sjsky007@gmail.com
 */
public class IPv4Util {

	/**
	 * 获取ip
	 * 
	 * @param request 请求对象
	 * @return long
	 */
	public static long getIpAddr(HttpServletRequest request) {
		return ipAddrToLong(getIpAddrStr(request));
	}

    /**
     * 获取ip
     *
     * @param request 请求对象
     * @return string
     */
    public static String getIpAddrStr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Cdn-Src-Ip");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        if (ip != null) {
            int firstDogTagIndex = ip.indexOf(",");
            if (firstDogTagIndex > -1) {
                ip = ip.substring(0, firstDogTagIndex);
            }
        }

        return ip;
    }

    /**
     * IP地址转换为long
     * @param ipaddress ip地址
     * @return long
     */
	public static long ipAddrToLong(String ipaddress) {

		long[] ip = new long[4];
		int position1 = ipaddress.indexOf(".");

		int position2 = ipaddress.indexOf(".", position1 + 1);

		int position3 = ipaddress.indexOf(".", position2 + 1);

		ip[0] = Long.parseLong(ipaddress.substring(0, position1));
		ip[1] = Long.parseLong(ipaddress.substring(position1 + 1, position2));
		ip[2] = Long.parseLong(ipaddress.substring(position2 + 1, position3));
		ip[3] = Long.parseLong(ipaddress.substring(position3 + 1));

		return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];

	}

	/**
	 * 将十进制整数形式转换成127.0.0.1形式的ip地址
	 * 
	 * @param ipaddress long型IP地址
	 * @return string
	 */
	public static String LongToIpAddr(long ipaddress) {

		StringBuffer sb = new StringBuffer("");

		sb.append(String.valueOf((ipaddress >>> 24)));
		sb.append(".");
		sb.append(String.valueOf((ipaddress & 0x00FFFFFF) >>> 16));
		sb.append(".");
		sb.append(String.valueOf((ipaddress & 0x0000FFFF) >>> 8));
		sb.append(".");
		sb.append(String.valueOf((ipaddress & 0x000000FF)));

		return sb.toString();
	}
}
