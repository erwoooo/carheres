package com.example.maptest.mycartest.Utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ${Author} on 2017/3/24.
 * Use to  时间格式操作类
 */

public class DateChangeUtil {

    /*字符串转时间*/
    public static Date StrToDate(String str) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /*字符串转时间*/
    public static Date StrToDateHi(String str) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    /*字符串转时间*/
    public static Date StrToDateS(String str) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /*字符串转时间*/
    public static Date StrToDateYear(String str) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    public static String getdata(long date) {
        String dates = null;
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long ns = 1000;
        long day = date / nd;
        // 计算差多少小时
        long hour = date % nd / nh;
        // 计算差多少分钟
        long min = date % nd % nh / nm;
        // 计算差多少秒//输出结果
        long sec = date % nd % nh % nm / ns;
//        return day + "天" + hour + "小时" + min + "分钟";
        if (day != 0) {
            dates = day + "天" + hour + "小时" + min + "分钟" + sec + "秒";
        } else if (hour != 0) {
            dates = hour + "小时" + min + "分钟" + sec + "秒";
        } else if (min != 0) {
            dates = min + "分钟" + sec + "秒";
        } else {
            dates = sec + "秒";
        }
        return dates;
    }

    /*计算差多少秒*/
    public static long getSec(long date) {
        long nh = 1000;

        return date / nh;
    }

    /*计算差多少小时*/
    public static long getHour(long date) {
        long nh = 1000 * 60 * 60;
        return date / nh;
    }

    /*计算差多少天*/
    public static long getDay(long date) {
        long nd = 1000 * 24 * 60 * 60;
        return date / nd;
    }

    /**
     * 第一种时间转字符串
     *
     * @param date
     * @return
     */
    public static String DateToStr(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = null;
        try {
            str = format.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }


    /**
     * 第二种时间转字符串
     *
     * @param date
     * @return
     */
    public static String DateToStrs(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = null;
        try {
            str = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * @param time 时间
     * @return 时间戳
     */
    public static long timedate(Date time) {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date = simpleDateFormat.parse(time);
        long ts = time.getTime();
        return ts;
    }
    //时间戳毫秒转时间
    public static String timedateTotim(long timedate){
        Date date = new Date(timedate);

        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);

//        System.out.println(format);
        return  format;
    }


    public static String timedateTotimNoS(long timedate){
        Date date = new Date(timedate);

        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);

        System.out.println(format);
        return  format;
    }
}