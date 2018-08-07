package com.cnsunrun.jiajiagou.common.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 其他工具类
 * <p>
 * author:yyc
 * date: 2017-09-20 12:57
 */
public class OtherUtils {

    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;

    /**
     * 延迟关闭activity
     * @param activity
     * @param delay
     */
    public static void delayFinishActivity(final Activity activity,int delay){
        Timer timer=new Timer();
        TimerTask task=new TimerTask() {
            @Override
            public void run() {
                activity.finish();
            }
        };
        timer.schedule(task,delay);
    }
    public static void delayFinishActivity(Activity activity){
        delayFinishActivity(activity,2000);
    }
    public static void delayStartActivity(final Context context, final Class<?> cls){
        delayStartActivity(context,cls,1500);
    }

    public static void delayStartActivity(final Context context, final Class<?> cls, int delay){
        Timer timer=new Timer();
        TimerTask task=new TimerTask() {
            @Override
            public void run() {
                context.startActivity(new Intent(context,cls));
            }
        };
        timer.schedule(task,delay);
    }

    /**
     * 格式化价格 支付宝用
     *
     * @return
     */
    public static String FormatPrice(double price) {
        DecimalFormat format = new DecimalFormat("0.00");
        String totalprice = format.format(price);
        return totalprice;
    }


    /**
     * 解决重复点击发送请求
     * @return
     */
    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }
}
