package com.cnsunrun.jiajiagou.common.util;

import android.util.Log;

import com.cnsunrun.jiajiagou.BuildConfig;

/**
 * Description:
 * Data：2016/11/8 0008-下午 5:30
 * Blog：http://blog.csdn.net/u013983934
 * Author: CZ
 */

public class LogUtils
{
    private static final String TAG = "cz";
    private static final String SEPARATOR = "\n";

    public static boolean allow = BuildConfig.IS_TEST;
    /*******
     * 是否打印super log
     *********/
    public static boolean isSuperLog = true;

    private static String getPrefix()
    {
//        String prefix = "%s.%s(L:%d)";
//
//        StackTraceElement caller = Thread.currentThread().getStackTrace()[4];
//        String callerClazzName = caller.getClassName();
//        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
//        prefix = String.format(prefix, callerClazzName, caller.getMethodName(), caller
//                .getLineNumber());
        return TAG;
    }


    /**
     * Send a DEBUG log message,蓝色
     */
    public static void d(String content)
    {
        if (allow)
        {
            Log.d(getPrefix(), content);
        }
    }

    /**
     * Send an ERROR log message,红色
     */
    public static void e(String content)
    {
        if (allow)
        {
            Log.e(getPrefix(), content + "");
        }
    }

    /**
     * Send an INFO log message,绿色
     */
    public static void i(String content)
    {
        if (allow)
        {
            Log.i(getPrefix(), content);
        }

    }

    /**
     * Send a VERBOSE log message,黑色
     */
    public static void v(String content)
    {
        if (allow)
        {
            Log.v(getPrefix(), content);
        }
    }

    /**
     * Send a WARN log message,黄色
     */
    public static void w(String content)
    {
        if (allow)
        {
            Log.w(getPrefix(), content);
        }
    }

    public static void e(String content, Throwable tr)
    {
        if (allow)
        {
            Log.e(getPrefix(), content, tr);
        }
    }

    public static void e(Throwable tr)
    {
        if (allow)
        {
            Log.e(getPrefix(), tr.getMessage());
        }
    }


    /*******************super log 带行号并且点击可跳转的log**************************/
    /**
     * Send an INFO log message,绿色
     */
    public static void printI(String log)
    {
        if (allow && isSuperLog)
        {
            Log.i(TAG, log + SEPARATOR + callMethodAndLine());
        }
    }

    /**
     * Send a DEBUG log message,蓝色
     */
    public static void printD(String log)
    {
        if (allow && isSuperLog)
        {
            Log.d(TAG, log + SEPARATOR + callMethodAndLine());
        }
    }

    /**
     * Send an ERROR log message,红色
     */
    public static void printE(String log)
    {
        if (allow && isSuperLog)
        {
            Log.e(TAG, log + SEPARATOR + callMethodAndLine());
        }
    }

    /**
     * Send a VERBOSE log message,黑色/白色
     */
    public static void printV(String log)
    {
        if (allow && isSuperLog)
        {
            Log.v("TAG", log + SEPARATOR + callMethodAndLine());
        }
    }

    /**
     * Send a WARN log message,黄色
     */
    public static void printW(String log)
    {
        if (allow && isSuperLog)
        {
            Log.w(TAG, log + SEPARATOR + callMethodAndLine());
        }
    }


    private static String callMethodAndLine()
    {
        StringBuilder result = new StringBuilder("at ");
        StackTraceElement thisMethodStack = (new Exception()).getStackTrace()[2];
        result.append(thisMethodStack.getClassName() + ".").append(thisMethodStack.getMethodName
                ()).append("(" + thisMethodStack.getFileName()).append(":" + thisMethodStack
                .getLineNumber() + ")  ");
        return result.toString();
    }

    public static void printStackTrace()
    {
        if (allow){
            StringBuilder sb = new StringBuilder();
            Exception e = new Exception("trace");
            StackTraceElement[] trace = e.getStackTrace();
            for (StackTraceElement aTrace : trace) sb.append("\nat ").append(aTrace);
            LogUtils.printD("who invoke me" + sb.toString());
        }
    }

    public static void showLongLog(String log)
    {
        int showCount = 3000;
        if (allow && isSuperLog)
        {
            if (log.length() > showCount)
            {
                String show = log.substring(0, showCount);
                v(show + "");
                if ((log.length() - showCount) > showCount)
                {//剩下的文本还是大于规定长度
                    String partLog = log.substring(showCount, log.length());
                    showLongLog(partLog);
                } else
                {
                    String surplusLog = log.substring(showCount, log.length());
                    v(surplusLog + "");
                }
            } else
            {
                v(log + "");
            }
        }
    }
}
