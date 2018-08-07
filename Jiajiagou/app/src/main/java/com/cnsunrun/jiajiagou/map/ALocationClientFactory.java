package com.cnsunrun.jiajiagou.map;

import android.content.Context;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-09-15 17:03
 */
public class ALocationClientFactory {
    public static AMapLocationClientOption createDefaultOption() {
        AMapLocationClientOption option = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        option.setInterval(2000);
        return option;
    }

    public static AMapLocationClientOption createOnceOption() {
        AMapLocationClientOption option = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
        //该方法默认为false。
//        option.setOnceLocation(true);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        option.setOnceLocationLatest(true);
        return option;
    }
    public static AMapLocationClient createLocationClient(Context context, AMapLocationClientOption option, AMapLocationListener listener) {
        AMapLocationClient client = new AMapLocationClient(context);
        client.setLocationOption(option);
        client.setLocationListener(listener);
        return client;
    }

//    public static AMapLocationClient createDefaultLocationClient(Context context) {
//        return createLocationClient(context, createDefaultOption(), );
//    }

}
