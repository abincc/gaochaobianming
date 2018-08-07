package com.cnsunrun.jiajiagou.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期相关工具类
 * author:yyc
 * date: 2017-08-23 14:47
 */
public class DateUtils {



        /**
         * 简单日期格式
         */
        private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        /**
         * 比较两个日期之间的大小
         *
         * @param date_input
         * @param date_current
         * @return 前者大于后者返回true 反之false
         */
        public static boolean compareDate(Date date_input, Date date_current) {
            Calendar calendar_input = Calendar.getInstance();
            Calendar calendar_current = Calendar.getInstance();
            calendar_input.setTime(date_input);
            calendar_current.setTime(date_current);

            int result = calendar_input.compareTo(calendar_current);
            return result > 0;
        }


        /**
         * 判断两个字符串日期的大小
         */
        public static boolean compareDate(String sDate, String eDate) {
            try {
                long sTime = sdf.parse(sDate).getTime();
                long eTime = sdf.parse(eDate).getTime();
                return sTime > eTime;
            } catch (Exception e) {
                return false;
            }
        }

        /**
         * 获取两个日期之间的间隔天数
         *
         * @return
         */
        public static int getGapCount(Date startDate, Date endDate) {
            Calendar fromCalendar = Calendar.getInstance();
            fromCalendar.setTime(startDate);
            fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
            fromCalendar.set(Calendar.MINUTE, 0);
            fromCalendar.set(Calendar.SECOND, 0);
            fromCalendar.set(Calendar.MILLISECOND, 0);
            Calendar toCalendar = Calendar.getInstance();
            toCalendar.setTime(endDate);
            toCalendar.set(Calendar.HOUR_OF_DAY, 0);
            toCalendar.set(Calendar.MINUTE, 0);
            toCalendar.set(Calendar.SECOND, 0);
            toCalendar.set(Calendar.MILLISECOND, 0);

            return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000
                    * 60 * 60 * 24));
        }

        /**
         * 获取两个字符串日期之间的间隔天数
         *
         * @return
         */
        public static int getGapCount(String start, String end) {

            Date startDate = null;
            Date endDate = null;
            try {
                startDate = sdf.parse(start);
                endDate = sdf.parse(end);
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.i("获取时间间隔时，日期转换异常");
            }

            Calendar fromCalendar = Calendar.getInstance();
            if (startDate != null) {
                fromCalendar.setTime(startDate);
            }
            fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
            fromCalendar.set(Calendar.MINUTE, 0);
            fromCalendar.set(Calendar.SECOND, 0);
            fromCalendar.set(Calendar.MILLISECOND, 0);

            Calendar toCalendar = Calendar.getInstance();
            if (endDate != null) {
                toCalendar.setTime(endDate);
            }
            toCalendar.set(Calendar.HOUR_OF_DAY, 0);
            toCalendar.set(Calendar.MINUTE, 0);
            toCalendar.set(Calendar.SECOND, 0);
            toCalendar.set(Calendar.MILLISECOND, 0);

            return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000
                    * 60 * 60 * 24));
        }

        /**
         * 转换一个date成为yyyy-MM-dd格式字符串（小于10带0）
         */
        public static String paseDateToString_withZero(Date date) {

            return sdf.format(date);
        }


        /**
         * 获取当前时间（用户指定格式）
         */
        public static String getCurrentTime(String format) {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
            String currentTime = sdf.format(date);
            return currentTime;
        }

        /**
         * 当前时间："yyyy-MM-dd  HH:mm:ss"
         */
        public static String getCurrentTime() {
            return getCurrentTime("yyyy-MM-dd  HH:mm:ss");
        }

        /**
         * 获取10位数字的时间戳：1320917972
         */
        public static String getCurrentTimeFor10() {
            // 获取到的是long（整形）13位数字毫秒数，除以1000再取整，就是10位了
            Long tsLong = System.currentTimeMillis() / 1000;
            String ts = tsLong.toString();
            return ts;
        }

        /**
         * 获取指定日期和今日的间隔天数
         */
        public static String getDateSpace(String date) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date parse = simpleDateFormat.parse(date);
                long l = System.currentTimeMillis() - parse.getTime();
                long day = l / (24 * 60 * 60 * 1000);
                return ((int) day) + "";
            } catch (ParseException e) {
                LogUtils.printE(e.getMessage());
            }
            return "";
        }

        /**
         * 获取当前时间的毫秒数
         */
        public static long getcurrentTimeMillis() {
            return System.currentTimeMillis();
        }


        /**
         * 将字符串转为时间戳
         * 2016-08-05T18:08:09+08:00
         *
         * @param time
         * @return
         */
//        public static long getStringToDate(String time) {
//
//            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Date date = new Date();
//            try {
//                date = sdf.parse(time.replace("T", " ").replace("+08:00", "").replace("Z", ""));
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            return date.getTime() / 1000L;
//        }

        /**
         * 将字符串转为时间戳
         *
         * @param time 秒
         * @param format 格式
         * @return
         */
        public static long getStringToDate(String time, String format) {

            sdf = new SimpleDateFormat(format);
            Date date = new Date();
            try {
                date = sdf.parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return date.getTime() / 1000L;
        }


        /**
         * 将时间戳转为String
         *
         * @param time
         * @return 1997-01-01 00:00:01
         */
        public static String getTimeStampToDate(Long time) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = format.format(time * 1000L);
            return date;
        }

        /**
         * 将时间戳转为String
         * @param time
         * @return 1997-01-01
         */
        public static String getDataToDay(Long time){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String date = format.format(time * 1000L);
            return date;
        }

        /**
         * 将时间戳转为String
         *
         * @param time
         * @return
         */
        public static String getDateToString(Long time) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy年M月d日 HH:mm:ss");
            String date = format.format(time * 1000L);
            return date;
        }

        /**
         * 将时间戳转为String
         *
         * @param time      时间戳
         * @param formatStr "yyyy年MM月dd日  HH:mm:ss"
         * @return
         */
        public static String getDateToString(Long time, String formatStr) {
            SimpleDateFormat format = new SimpleDateFormat(formatStr);
            String date = format.format(time * 1000L);
            return date;
        }


        /**
         * 通过时间戳的差值显示时间
         *
         * @param time1
         * @param time2
         * @return
         */
        public static String getDistanceTime(long time1, long time2) {
            long day = 0;
            long hour = 0;
            String datime = null;
            long min = 0;
            long sec = 0;
            long diff;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String flag;
            if (time1 < time2) {
                diff = time2 - time1;
                flag = "前";
            } else {
                diff = time1 - time2;
                flag = "后";
            }
            day = diff / (24 * 60 * 60);
            datime = sdf.format(time1 * 1000);
            hour = (diff / (60 * 60) - day * 24);
            min = ((diff / (60)) - day * 24 * 60 - hour * 60);
            sec = (diff - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
            if (day > 0 && day <= 7) return day + "天" + flag;
            if (day > 7) return datime;
            if (hour != 0) return hour + "小时" + flag;
            if (min != 0) return min + "分钟" + flag;
            return "刚刚";
        }
    }
