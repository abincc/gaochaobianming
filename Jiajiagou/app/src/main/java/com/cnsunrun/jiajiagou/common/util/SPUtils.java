package com.cnsunrun.jiajiagou.common.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.cnsunrun.jiajiagou.common.JjgApplication;
import com.cnsunrun.jiajiagou.common.constant.SPConstant;

public class SPUtils
{

    public static SharedPreferences getSP(Context context)
    {
        return context.getSharedPreferences(SPConstant.SP_NAME, Context.MODE_PRIVATE);
    }

    public static void put(Context context, String key, Object value)
    {
        if (context == null) context = JjgApplication.getContext();

        SharedPreferences sp = getSP(context);
        Editor editor = sp.edit();
        if (value instanceof String)
            editor.putString(key, (String) value);
        else if (value instanceof Integer)
            editor.putInt(key, (Integer) value);
        else if (value instanceof Boolean)
            editor.putBoolean(key, (Boolean) value);
        editor.apply();
    }

    public static boolean getBoolean(Context context, String key)
    {
        return getSP(context).getBoolean(key, false);
    }

    public static boolean getBoolean(Context context, String key, boolean defalutValue)
    {
        return getSP(context).getBoolean(key, defalutValue);
    }

    public static String getString(Context context, String key)
    {
        return getSP(context).getString(key, null);
    }

    public static int getInt(Context context, String key)
    {
        return getSP(context).getInt(key, SPConstant.INT_VALUE_INVAILD);
    }


    public static int getInt(Context context, String key, int defaultValue)
    {
        return getSP(context).getInt(key, defaultValue);
    }

    public static void clearSP(Context context)
    {
        getSP(context).edit().clear().commit();
    }

    public static void remove(Context context, String key)
    {
        getSP(context).edit().remove(key).commit();
    }
}
