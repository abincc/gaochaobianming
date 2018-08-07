package com.cnsunrun.jiajiagou;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.cnsunrun.jiajiagou.common.util.LogUtils;

/**
 * Description:
 * Data：2017/8/19 0019-下午 3:16
 * Blog：http://blog.csdn.net/u013983934
 * Author: CZ
 */
public class LocService extends Service
{

    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private LocBinder mLocBinder = new LocBinder();


    @Override
    public void onCreate()
    {
        super.onCreate();
        mlocationClient = new AMapLocationClient(this);
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mlocationClient.setLocationOption(mLocationOption);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mlocationClient.stopLocation();
        mlocationClient.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return mLocBinder;
    }

    public class LocBinder extends Binder
    {
        /**
         * @param interval 单次定位传-1
         * @param listener 定位回调
         */
        public void startLocation(long interval, AMapLocationListener listener)
        {
            if (interval == -1)
            {
                LogUtils.i("单次定位传-1");
                mLocationOption.setOnceLocation(true);
            } else
            {
                mLocationOption.setOnceLocation(false);
                mLocationOption.setInterval(interval);
            }
            if (listener != null) mlocationClient.setLocationListener(listener);
            mlocationClient.setLocationOption(mLocationOption);
            mlocationClient.startLocation();
        }

        public void stopLocation()
        {
            mlocationClient.stopLocation();
        }
    }

}
