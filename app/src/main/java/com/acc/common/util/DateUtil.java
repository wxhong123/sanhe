package com.acc.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 注释标记示范类。
 * <p>
 * 关于时间操作的工具类
 * </p>
 *
 * @author 张琦
 * @version v1.0.0
 * <p>
 * 最后更新 by 张琦 @ Dec 7, 2012
 * </p>
 */
public class DateUtil {
    /**
     * 日期的默认格式
     */
    public final static String DATE_FORMAT = "yyyy-MM-dd";
    /**
     * 时间的默认格式
     */
    public final static String TIME_FORMAT = "HH:mm:ss";

    public final static String FULLFORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 把时间转化为"yyyy-MM-dd"格式字符串返回, 转化失败返回""。
     *
     * @param date 待处理时间
     * @return String 转化失败返回""
     */
    public static String getDateToString(Date date) {
        String result = "";
        try {
            SimpleDateFormat formater = new SimpleDateFormat(DATE_FORMAT);
            result = formater.format(date);
            return result;
        } catch (Exception e) {
            return result;
        }
    }

    /**
     * 把时间转化为"yyyy-MM-dd HH:mm:ss"格式字符串返回, 转化失败返回""。
     *
     * @param date 待处理时间
     * @return String 转化失败返回""
     */
    public static String getFullDateToString(Date date) {
        String result = "";
        try {
            SimpleDateFormat formater = new SimpleDateFormat(DATE_FORMAT + " " + TIME_FORMAT);
            result = formater.format(date);
            return result;
        } catch (Exception e) {
            return result;
        }
    }

    /**
     * 把时间转化为指定格式字符串返回, 转化失败返回""。
     * <p>
     * </p>
     *
     * @param date   待处理时间
     * @param format 指定返回的时间格式
     * @return String
     */
    public static String getDateToString(Date date, String format) {
        String result = "";
        try {
            SimpleDateFormat formater = new SimpleDateFormat(format);
            result = formater.format(date);
            return result;
        } catch (Exception e) {
            return result;
        }
    }

    /**
     * 把"yyyy-MM-dd"格式的字符串转化为时间类型返回。
     * <p>
     * </p>
     *
     * @param source 待处理字符串
     * @return Date 转化失败返回null
     */
    public static Date formatDate(String source) {
        SimpleDateFormat formator = new SimpleDateFormat(DATE_FORMAT);
        try {
            Date date = formator.parse(source);
            return date;
        } catch (Exception e) {
            return null;
        }
    }

    public static Date formatDateFromFullDate(String source) {
        SimpleDateFormat formator = new SimpleDateFormat(FULLFORMAT);
        try {
            Date date = formator.parse(source);
            return date;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 把"yyyy-MM-dd HH:mm:ss"格式的字符串转化为时间类型返回。
     * <p>
     * </p>
     *
     * @param source 待处理字符串
     * @return Date 转化失败返回null
     */
    public static Date formatFullDate(String source) {
        SimpleDateFormat formator = new SimpleDateFormat(DATE_FORMAT + " " + TIME_FORMAT);
        try {
            Date date = formator.parse(source);
            return date;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 把字符串转化为指定时间格式的时间类型返回。
     * <p>
     * 方法详述（简单方法可不必详述）。
     * </p>
     *
     * @param source 待处理字符串
     * @param format 时间格式
     */
    public static Date formatDate(String source, String format) {
        SimpleDateFormat formator = new SimpleDateFormat(format);
        try {
            Date date = formator.parse(source);
            return date;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 返回date增加或减少days天的日期。
     * <p>
     * 方法详述（简单方法可不必详述）。
     * </p>
     *
     * @param date 待处理时间
     * @param days
     * @return
     */
    public static Date getDateAddDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }

    /**
     * 返回source对应日期增加或减少days天的日期。
     * <p>
     * 方法详述（简单方法可不必详述）。
     * </p>
     *
     * @param source 待处理字符串
     * @param days
     * @return
     */
    public static Date getDateAddDays(String source, int days) {
        Date date = formatDate(source, DATE_FORMAT + " " + TIME_FORMAT);
        return getDateAddDays(date, days);
    }

    /**
     * 返回date增加或减少hours小时的日期。
     * <p>
     * 方法详述（简单方法可不必详述）。
     * </p>
     *
     * @param date
     * @param hours
     * @return
     */
    public static Date getDateAddHours(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }

    /**
     * 返回source对应时间增加或减少hours小时的日期。
     * <p>
     * 方法详述（简单方法可不必详述）。
     * </p>
     *
     * @param source
     * @param hours
     * @return
     */
    public static Date getDateAddHours(String source, int hours) {
        Date date = formatDate(source, DATE_FORMAT + " " + TIME_FORMAT);
        return getDateAddHours(date, hours);
    }

    /**
     * 返回date增加或减少mins分钟的日期。
     * <p>
     * 方法详述（简单方法可不必详述）。
     * </p>
     *
     * @param date
     * @param mins
     * @return
     */
    public static Date getDateAddMins(Date date, int mins) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, mins);
        return calendar.getTime();
    }

    /**
     * 返回source对应时间增加或减少mins分钟的日期。
     * <p>
     * 方法详述（简单方法可不必详述）。
     * </p>
     *
     * @param source
     * @param mins
     * @return
     */
    public static Date getDateAddMins(String source, int mins) {
        Date date = formatDate(source, DATE_FORMAT + " " + TIME_FORMAT);
        return getDateAddMins(date, mins);
    }

    /**
     * 根据long类型转换成时间类型。
     * <p>
     * 方法详述（简单方法可不必详述）。
     * </p>
     *
     * @param m
     * @return
     */
    public static Date getLongFormatDate(long m) {
        return new Date(m);
    }

    /**
     * 返回date增加或减少seconds秒的日期。
     * <p>
     * 方法详述（简单方法可不必详述）。
     * </p>
     *
     * @param date
     * @param seconds
     * @return
     */
    public static Date getDateAddSeconds(Date date, int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, seconds);
        return calendar.getTime();
    }

    /**
     * 返回source对应日期增加或减少seconds秒的日期。
     * <p>
     * 方法详述（简单方法可不必详述）。
     * </p>
     *
     * @param source
     * @param seconds
     * @return
     */
    public static Date getDateAddSeconds(String source, int seconds) {
        Date date = formatDate(source, DATE_FORMAT + " " + TIME_FORMAT);
        return getDateAddSeconds(date, seconds);
    }

    /**
     * 返回date增加或减少 days天、hours小时、mins分钟、seconds秒的日期。
     * <p>
     * 方法详述（简单方法可不必详述）。
     * </p>
     *
     * @param date
     * @param days
     * @param hours
     * @return
     */
    public static Date getDateAddAll(Date date, int days, int hours, int mins, int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        calendar.add(Calendar.MINUTE, mins);
        calendar.add(Calendar.SECOND, seconds);
        return calendar.getTime();
    }

    /**
     * 返回source对应时间增加或减少 days天、hours小时、mins分钟、seconds秒的日期。
     * <p>
     * 方法详述（简单方法可不必详述）。
     * </p>
     *
     * @param source
     * @param days
     * @param hours
     * @param mins
     * @param seconds
     * @return
     */
    public static Date getDateAddAll(String source, int days, int hours, int mins, int seconds) {
        Date date = formatDate(source, DATE_FORMAT + " " + TIME_FORMAT);
        return getDateAddAll(date, days, hours, mins, seconds);
    }

    /**
     * 返回两个时间相差的毫秒数。
     * <p>
     * 方法详述（简单方法可不必详述）。
     * </p>
     *
     * @param date1
     * @param date2
     * @return
     */
    public static long getCompare(Date date1, Date date2) {
        return date1.getTime() - date2.getTime();
    }

    /**
     * 返回两个时间相差的毫秒数。
     * <p>
     * 方法详述（简单方法可不必详述）。
     * </p>
     *
     * @param source1
     * @param source2
     * @return
     */
    public static long getCompare(String source1, String source2) {
        Date date1 = formatDate(source1, DATE_FORMAT + " " + TIME_FORMAT);
        Date date2 = formatDate(source2, DATE_FORMAT + " " + TIME_FORMAT);
        return getCompare(date1, date2);
    }

    /**
     * 返回某个月份的天数。
     * <p>
     * 方法详述（简单方法可不必详述）。
     * </p>
     *
     * @param source "yyyy-MM"
     * @return
     */
    public static int getDaysByMonth(String source) {
        String[] temp;
        int result = 0;
        if (source.contains("-")) {
            temp = source.split("-");
            result = days(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
        }
        return result;
    }

    /**
     * 返回某天星期几。
     * <p>
     * 方法详述（简单方法可不必详述）。
     * </p>
     *
     * @param source "yyyy-MM-dd"
     * @return 返回0代表星期日, 1代表星期一, 2代表星期二...依次类推到6代表星期六
     */
    public static int getWeekByDay(String source) {
        Date date = formatDate(source, DATE_FORMAT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek;
    }

    /**
     * 返回某月的天数。
     * <p>
     * 方法详述（简单方法可不必详述）。
     * </p>
     *
     * @param year  年份
     * @param month 月份
     * @return
     */
    private static int days(int year, int month) {
        int days = 0;
        if (month != 2) {
            switch (month) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    days = 31;
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    days = 30;
            }
        } else {
            if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0)
                days = 29;
            else
                days = 28;
        }
        return days;
    }
}
