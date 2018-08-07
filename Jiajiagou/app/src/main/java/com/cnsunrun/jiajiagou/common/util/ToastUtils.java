package com.cnsunrun.jiajiagou.common.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Description:
 * Data：2016/11/7 0007-上午 11:08
 * Blog：http://blog.csdn.net/u013983934
 * Author: CZ
 */

public class ToastUtils
{
    public static void show(Context context, String content)
    {
        show(context, content, Toast.LENGTH_SHORT);
    }

    public static void show(Context context, int stringRes)
    {
        if (context != null)
            show(context, context.getString(stringRes));
    }

    public static void show(Context context, String content, int dur)
    {
        if (context != null)
        {
            Toast.makeText(context, content, dur).show();
//            TextView tv = new TextView(context);
//            tv.setText(content);
//            tv.setTextSize(14);
//            tv.setBackgroundResource(R.drawable.shape_white_corner_blue_solid);
//            tv.setTextColor(0xffffffff);
//            int leftRightPx = UIUtils.dip2px(context, 12);
//            int topBottomPx = UIUtils.dip2px(context, 8);
//            tv.setPadding(leftRightPx, topBottomPx, leftRightPx, topBottomPx);
//            Toast toast = new Toast(context);
//            toast.setDuration(dur);
//            toast.setGravity(Gravity.BOTTOM, 0, 180);
//            toast.setView(tv);
//            toast.show();
        }
    }
}
