package com.cnsunrun.jiajiagou.common.constant;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.cnsunrun.jiajiagou.common.util.SPUtils;
import com.cnsunrun.jiajiagou.common.util.ToastUtils;
import com.cnsunrun.jiajiagou.login.LoginActivity;


/**
 * Created by ${LiuDi}
 * on 2017/8/30on 10:01.
 */

public class JjgConstant
{

    public static String getToken(Context context)
    {
        String token = SPUtils.getString(context, SPConstant.TOKEN);
        if (TextUtils.isEmpty(token))
        {
            ToastUtils.show(context, "请重新登录~");
            context.startActivity(new Intent(context, LoginActivity.class));
        }
        return token;
    }

    public static boolean isLogin(Context context)
    {
        String token = SPUtils.getString(context, SPConstant.TOKEN);
        return !TextUtils.isEmpty(token);
    }

    /**
     * 判断是否是物业
     */
    public static boolean isProperty(Context context)
    {
        return SPUtils.getInt(context, SPConstant.TYPE, 2) == 3;
    }

    public static String getWxCallBackUrl(Context context)
    {
        String wxUrl = SPUtils.getString(context, "wxUrl");
        return TextUtils.isEmpty(wxUrl) ? "http://test.cnsunrun.com/wuye/Api/Common/Callback/wx_notify" : wxUrl;
    }

    public static String getAliCallBackUrl(Context context)
    {
        String aliUrl = SPUtils.getString(context, "aliUrl");
        return TextUtils.isEmpty(aliUrl) ? "http://test.cnsunrun.com/wuye/Api/Common/Callback/ali_notify" : aliUrl;
    }
}


