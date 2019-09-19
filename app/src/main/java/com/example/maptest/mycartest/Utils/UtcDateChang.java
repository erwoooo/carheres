package com.example.maptest.mycartest.Utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by ${Author} on 2018/3/14.
 * Use to
 */

public class UtcDateChang {
    /* 将Server传送的UTC时间转换为指定时区的时间 */
    public static String converTime(String srcTime){
        TimeZone timezone = TimeZone.getDefault();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dspFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String convertTime;

        Date result_date;
        long result_time = 0;

        // 如果传入参数异常，使用本地时间
        if (null == srcTime)
        {
            result_time = System.currentTimeMillis();
        }
        else
        {
            // 将输入时间字串转换为UTC时间
            try
            {
                sdf.setTimeZone(TimeZone.getTimeZone("GMT00:00"));
                result_date = sdf.parse(srcTime);
                result_time = result_date.getTime();
            }
            catch (Exception e)
            {
                Log.e("出现异常","yes");
                // 出现异常时，使用本地时间
                result_time = System.currentTimeMillis();
                dspFmt.setTimeZone(TimeZone.getDefault());
                convertTime = dspFmt.format(result_time);
                return convertTime;
            }
        }
        // 设定时区
        dspFmt.setTimeZone(timezone);
        convertTime = dspFmt.format(result_time);
        Log.e("current zone:", "id=" + sdf.getTimeZone().getID()
                + "  name=" + sdf.getTimeZone().getDisplayName());

        return convertTime;
    }




    /* 将Server传送的UTC时间转换为指定时区的时间 */
    public static String converSortTime(String srcTime){
        TimeZone timezone = TimeZone.getDefault();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dspFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String convertTime;

        Date result_date;
        long result_time = 0;

        // 如果传入参数异常，使用本地时间
        if (null == srcTime)
        {
            result_time = System.currentTimeMillis();
        }
        else
        {
            // 将输入时间字串转换为UTC时间
            try
            {
                sdf.setTimeZone(TimeZone.getTimeZone("GMT00:00"));
                result_date = sdf.parse(srcTime);
                result_time = result_date.getTime();
            }
            catch (Exception e)
            {

                // 出现异常时，使用本地时间
                result_time = System.currentTimeMillis();
                dspFmt.setTimeZone(TimeZone.getDefault());
                convertTime = dspFmt.format(result_time);
                return convertTime;
            }
        }

        // 设定时区
        dspFmt.setTimeZone(timezone);
        convertTime = dspFmt.format(result_time);
//        Log.e("current zone:", "id=" + sdf.getTimeZone().getID()
//                + "  name=" + sdf.getTimeZone().getDisplayName());
        return convertTime;
    }


    public static long Local2UTC(String time){       //当地时间转utc时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("gmt"));
        String gmtTime = sdf.format(DateChangeUtil.StrToDateHi(time));
        Log.e("Local2UTC","提交： " +time + "原始 ：" + gmtTime);
        long posttime = DateChangeUtil.StrToDateHi(gmtTime).getTime();
        return posttime;
    }
    public static long Local2UTC(Date time){       //当地时间转utc时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time1 = sdf.format(time);
        sdf.setTimeZone(TimeZone.getTimeZone("gmt"));
        String gmtTime = sdf.format(time);
        long posttime = DateChangeUtil.StrToDateHi(gmtTime).getTime();
        Log.e("转换成的UTC时间",posttime + "");
        return posttime;
    }
    public static String UtcDatetoLocaTime(long utcdate){
        Date date = new Date(utcdate);
        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);  //UTC字符串
        return converSortTime(format);
    }


}
