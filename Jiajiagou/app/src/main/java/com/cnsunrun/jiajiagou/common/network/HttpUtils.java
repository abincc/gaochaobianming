package com.cnsunrun.jiajiagou.common.network;

import android.content.Context;

import com.cnsunrun.jiajiagou.common.constant.JjgConstant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * Data：2016/10/30 0030-下午 6:43
 * Blog：http://blog.csdn.net/u013983934
 * Author: CZ
 */

public class HttpUtils
{
//    private static final String PARAM_KEY = "biz";

    public static void post(String url, HashMap<String, String> map, Callback callback)
    {


        OkHttpUtils.post().url(NetConstant.BASE_URL + url).params(map).tag(url).build().execute(callback);
    }

    public static void postForm(String url,Map<String,String> map,String key,Map<String, File> files,Callback callback){
        OkHttpUtils.post().url(NetConstant.BASE_URL+url).params(map).files(key,files).build().execute(callback);
    }

    public static void downloadFile(String url, FileCallBack fileCallBack)
    {
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(fileCallBack);
    }

    public static void get(String url, HashMap<String, String> params, Callback callback)
    {
        OkHttpUtils
                .get()
                .url(NetConstant.BASE_URL + url)
                .params(params)
                .build()
                .execute(callback);

    }

    public static void getToken(String ticket,Callback callback){
        HashMap<String,String> map=new HashMap();
        map.put("ticket",ticket);
        OkHttpUtils
                .get()
                .url(NetConstant.BASE_URL + NetConstant.GET_TOKEN)
                .params(map)
                .build()
                .execute(callback);
    }

    /**
     * 头像上传
     *
     * @param file
     * @param callback
     */
    public static void postImv(File file, Context context, Callback callback)
    {
        HashMap<String, String> params = new HashMap<>();
        params.put("headimg", file.getPath());
        params.put("token", JjgConstant.getToken(context));
        OkHttpUtils.post()//
                .addFile("headimg", "jjg.png", file)//
                .url(NetConstant.UPLOAD_HEADIMG)
                .params(params)
                .build()//
                .execute(callback);

    }
    public static void cancelByTag(Object tag)
    {
        OkHttpUtils.getInstance().cancelTag(tag);
    }
}
