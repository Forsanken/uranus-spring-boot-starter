package com.uranus.framework.util;

import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * 日期工具
 */
public class DateTimeUtil {

    public final static String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public final static String DATETIME_FORMAT_NO_SECOND = "yyyy-MM-dd HH:mm";
    public final static String DATETIME_FORMAT_NUMBER_NO_SECOND = "yyyyMMddHHmm";
    public final static String DATETIME_FORMAT_NUMBER = "yyyyMMddHHmmss";


    public final static String DATE_FORMAT = "yyyy-MM-dd";
    public final static String DATE_FORMAT_NUMBER = "yyyyMMdd";
    public final static String DATE_FORMAT_CN = "yyyy年MM月dd日";

    public final static String TIME_FORMAT = "HH:mm:ss";
    public final static String TIME_FORMAT_NUMBER = "HHmmss";
    public final static String TIME_FORMAT_CN = "HH时mm分ss秒";


    /**
     * 日期时间格式化为字符串
     * @param date 时间
     * @param pattern 格式化模板
     * @return String
     */
    public static String formatDateTime(Date date, String pattern) {
        if (StringUtils.isEmpty(pattern)) {
            pattern = DATETIME_FORMAT;
        }
        return new SimpleDateFormat(pattern).format(date);
    }
    /**
     * 字符串格式化为日期时间
     * @param str 字符串
     * @param pattern 格式化模板
     * @return Date
     */
    public static Date parseDate(String str, String pattern) {
        if (StringUtils.isEmpty(pattern)) {
            pattern = DATETIME_FORMAT;
        }
        try {
            return new SimpleDateFormat(pattern).parse(str);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Date转换为LocalDateTime
     * @param date Date
     * @return LocalDateTime
     */
    public static LocalDateTime convertDateToLDT(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * LocalDateTime转换为Date
     * @param time LocalDateTime
     * @return Date
     */
    public static Date convertLDTToDate(LocalDateTime time) {
        return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 日期时间格式化为字符串
     * @param time 时间
     * @param pattern 格式化模板
     * @return String
     */
    public static String formatDateTime(LocalDateTime time,String pattern) {
        if (StringUtils.isEmpty(pattern)) {
            pattern = DATETIME_FORMAT;
        }
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 日期格式化为字符串
     * @param time 时间
     * @param pattern 格式化模板
     * @return String
     */
    public static String formatTime(LocalDate time,String pattern) {
        if (StringUtils.isEmpty(pattern)) {
            pattern = DATETIME_FORMAT;
        }
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 时间格式化为字符串
     * @param time 时间
     * @param pattern 格式化模板
     * @return String
     */
    public static String formatDate(LocalTime time, String pattern) {
        if (StringUtils.isEmpty(pattern)) {
            pattern = DATETIME_FORMAT;
        }
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }


    /**
     * 字符串格式化为日期时间
     * @param str 字符串
     * @param pattern 格式化模板
     * @return LocalDateTime
     */
    public static LocalDateTime parseLocalDateTime(String str, String pattern) {
        if (StringUtils.isEmpty(pattern)) {
            pattern = DATETIME_FORMAT;
        }
        return  LocalDateTime.parse(str, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 字符串格式化为日期
     * @param str 字符串
     * @param pattern 格式化模板
     * @return LocalDateTime
     */
    public static LocalDate parseLocalDate(String str, String pattern) {
        if (StringUtils.isEmpty(pattern)) {
            pattern = DATETIME_FORMAT;
        }
        return  LocalDate.parse(str, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 字符串格式化为时间
     * @param str 字符串
     * @param pattern 格式化模板
     * @return LocalDateTime
     */
    public static LocalTime parseLocalTime(String str, String pattern) {
        if (StringUtils.isEmpty(pattern)) {
            pattern = DATETIME_FORMAT;
        }
        return  LocalTime.parse(str, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 获取指定日期的毫秒
     * @param time 日期时间
     * @return long
     */
    public static Long getMilliByTime(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 获取指定日期的秒
     * @param time 日期时间
     * @return long
     */
    public static Long getSecondsByTime(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
    }

    /**
     * 获取当前时间的指定格式
     * @param pattern 格式模板
     * @return String
     */
    public static String formatNow(String pattern) {
        return formatDateTime(LocalDateTime.now(), pattern);
    }

    /**
     * 获取两个日期的差  field参数为ChronoUnit.*
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param field  单位(年月日时分秒)
     * @return long
     */
    public static long betweenTwoTime(LocalDateTime startTime, LocalDateTime endTime, ChronoUnit field) {
        Period period = Period.between(LocalDate.from(startTime), LocalDate.from(endTime));
        if (field == ChronoUnit.YEARS) return period.getYears();
        if (field == ChronoUnit.MONTHS) return period.getYears() * 12 + period.getMonths();
        return field.between(startTime, endTime);
    }

    /**
     * 获取一天的开始时间，2017,7,22 00:00
     * @param time 时间
     * @return LocalDateTime
     */
    public static LocalDateTime getDayStart(LocalDateTime time) {
        return time.withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }

    /**
     * 获取一天的开始时间，2017,7,22 00:00
     * @param time 时间
     * @return LocalDateTime
     */
    public static LocalDateTime getDayStart(LocalDate time) {
        return LocalDateTime.of(time,LocalTime.of(0,0,0));
    }

    /**
     * 获取一天的结束时间，2017,7,22 23:59:59.999999999
     * @param time 时间
     * @return LocalDateTime
     */
    public static LocalDateTime getDayEnd(LocalDateTime time) {
        return time.withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withNano(999999999);
    }

    /**
     * 获取一天的结束时间，2017,7,22 23:59:59.999999999
     * @param time 时间
     * @return LocalDateTime
     */
    public static LocalDateTime getDayEnd(LocalDate time) {
        return LocalDateTime.of(time,LocalTime.of(23,59,59,999999999));
    }
}