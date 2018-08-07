package com.cnsunrun.jiajiagou.common;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.constant.SPConstant;
import com.cnsunrun.jiajiagou.common.util.SPUtils;
import com.cnsunrun.jiajiagou.common.wxpay.WxConstant;
import com.cnsunrun.jiajiagou.map.GDLocationUtil;
import com.lzy.ninegrid.NineGridView;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.io.IOException;

import cn.jiguang.share.android.api.JShareInterface;
import cn.jpush.android.api.JPushInterface;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Description:
 * Data：2017/8/18 0018-上午 10:55
 * Blog：http://blog.csdn.net/u013983934
 * Author: CZ
 */
public class JjgApplication extends Application {
    public static JjgApplication app;
    public static IWXAPI sWxapi;
    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoggerInterceptor("cz", true))
                .addNetworkInterceptor(getTokenInterceptor())
                .build();
        OkHttpUtils.initClient(okHttpClient);
        // 初始化NineGridView
        NineGridView.setImageLoader(new GlideImageLoader());
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        JShareInterface.init(this);
        JShareInterface.setDebugMode(true);
        // 定位工具初始化
        GDLocationUtil.init(this);
        sWxapi = WXAPIFactory.createWXAPI(this, WxConstant.WX_APPID, true);
        sWxapi.registerApp(WxConstant.WX_APPID);
    }

    @NonNull
    private Interceptor getTokenInterceptor() {
        return new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request originalRequest = chain.request();
                    Request tokenRequest = null;
                    String token = SPUtils.getString(getApplicationContext(), SPConstant.TOKEN);
                    if (TextUtils.isEmpty(token)) {//对 token 进行判空，如果为空，则不进行修改
                        return chain.proceed(originalRequest);
                    }
                    tokenRequest = originalRequest.newBuilder()//往请求头中添加 token 字段
                            .header("token", token)
                            .build();
                    return chain.proceed(tokenRequest);
                }
            };
    }


    private class GlideImageLoader implements NineGridView.ImageLoader {

        @Override
        public void onDisplayImage(Context context, ImageView imageView, String url) {
            // TODO: 2017/8/26  暂时无默认加载图片
            Glide.with(context).load(url).placeholder(R.drawable.sign_icon_id_nor).error(R
                    .drawable.sign_icon_id_nor).into(imageView);
        }

        @Override
        public Bitmap getCacheImage(String url) {
            return null;
        }
    }

    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    public static Context getContext() {
        return app;
    }


}
