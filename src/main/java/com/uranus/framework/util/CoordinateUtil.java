package com.uranus.framework.util;

/**
 * 〈一句话功能简述〉<br>
 * 〈经纬度距离测算类〉
 *
 * @author ChenYongke
 * @create 2018/11/5
 */
public class CoordinateUtil {

    // 单位千米
    private static double EARTH_RADIUS = 6378.137;

    /**
     * 角度弧度计算公式 rad:(). <br/>
     *
     * 360度=2π π=Math.PI
     *
     * x度 = x*π/360 弧度
     *
     * @param degree
     *              角度幅度
     * @return double
     * @since JDK 1.6
     */
    private static double getRadian(double degree) {
        return degree * Math.PI / 180.0;
    }

    /**
     * 依据经纬度计算两点之间的距离 GetDistance:(). <br/>
     *
     *
     * @param lat1
     *            1点的纬度
     * @param lng1
     *            1点的经度
     * @param lat2
     *            2点的纬度
     * @param lng2
     *            2点的经度
     * @return 距离 单位 米
     * @since JDK 1.6
     */
    public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = getRadian(lat1);
        double radLat2 = getRadian(lat2);
        // 两点纬度差
        double a = radLat1 - radLat2;
        // 两点的经度差
        double b = getRadian(lng1) - getRadian(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1)
                * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        return s * 1000;
    }

    /**
     * 经纬度数转换为10进制
     *
     * @param value 度分秒  103°45′55″或者  1034555
     */
    public static double degreeConvert(String value) {
        int secondIndex = value.indexOf("″");
        int minuteIndex = value.indexOf("′");
        int degreeIndex = value.indexOf("°");
        double second;
        int minute;
        int degree;
        if (secondIndex == -1 || minuteIndex == -1 || degreeIndex == -1) {
            int length = value.length();
            second = Double.parseDouble(value.substring(length - 2));
            minute = Integer.parseInt(value.substring(length - 4, length - 2));
            degree = Integer.parseInt(value.substring(0, length - 4));
        } else {
            second = Double.parseDouble(value.substring(minuteIndex + 1, secondIndex));
            minute = Integer.parseInt(value.substring(degreeIndex + 1, minuteIndex));
            degree = Integer.parseInt(value.substring(0, degreeIndex));
        }
        //四舍五入取5位小数
        return (double) Math.round((degree + (double) minute / 60 + second / 3600) * 100000) / 100000;
    }
}
