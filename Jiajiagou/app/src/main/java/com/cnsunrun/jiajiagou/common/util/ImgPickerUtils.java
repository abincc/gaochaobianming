package com.cnsunrun.jiajiagou.common.util;

import android.app.Activity;
import android.content.Context;

import com.bumptech.glide.Glide;
import com.cnsunrun.jiajiagou.R;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;
import com.yancy.gallerypick.inter.IHandlerCallBack;
import com.yancy.gallerypick.inter.ImageLoader;
import com.yancy.gallerypick.widget.GalleryImageView;

/**
 * Description:
 * Data：2016/11/2 0002-上午 10:48
 * Blog：http://blog.csdn.net/u013983934
 * Author: CZ
 */

public class ImgPickerUtils
{

    private static ImgPickerUtils mImgPickerUtils;
    private GalleryConfig.Builder mBuilder;

    private ImgPickerUtils()
    {
        initPickerConfig();
    }

    public static ImgPickerUtils getInstance()
    {
        if (mImgPickerUtils == null)
        {
            mImgPickerUtils = new ImgPickerUtils();
        }
        return mImgPickerUtils;
    }

    private void initPickerConfig()
    {
        mBuilder = new GalleryConfig.Builder()
                .imageLoader(new GlideImageLoader())    // ImageLoader 加载框架（必填）
//                .iHandlerCallBack(iHandlerCallBack)     // 监听接口（必填）
//                .pathList(path)                         // 记录已选的图片
//                .multiSelect(false)                      // 是否多选   默认：false
//                .multiSelect(false, 9)                   // 配置是否多选的同时 配置多选数量   默认：false ， 9
//                .maxSize(9)                             // 配置多选时 的多选数量。    默认：9
//                .crop(true)                             // 快捷开启裁剪功能，仅当单选 或直接开启相机时有效
                .crop(true, 1, 1, 500, 500)             // 配置裁剪功能的参数，   默认裁剪比例 1:1
                .isShowCamera(true).provider("com.cnsunrun.jiajiagou.fileprovider")                     //
                // 是否现实相机按钮  默认：false
                .filePath("/Gallery/Pictures");
    }

    public void singleSelect(Activity activity, IHandlerCallBack callBack)
    {
        GalleryPick.getInstance().setGalleryConfig(mBuilder.multiSelect(false).iHandlerCallBack(callBack).build())
                .open(activity);
    }

    public void singleSelect(Activity activity, IHandlerCallBack callBack, int x, int y, int xPx, int yPx)
    {
        GalleryPick.getInstance().setGalleryConfig(mBuilder.multiSelect(false).crop(true, x, y, xPx, yPx)
                .iHandlerCallBack(callBack).build())
                .open(activity);
        initPickerConfig();
    }

    public void multiSelect(Activity activity, int selectNum, IHandlerCallBack callBack)
    {
        initPickerConfig();
        GalleryPick.getInstance().setGalleryConfig(mBuilder.multiSelect(true, selectNum).iHandlerCallBack(callBack)
                .build()).open(activity);
    }

    public class GlideImageLoader implements ImageLoader
    {
        @Override
        public void displayImage(Activity activity, Context context, String path, GalleryImageView galleryImageView,
                                 int width, int height)
        {
            Glide.with(context)
                    .load(path)
                    .placeholder(R.mipmap.gallery_pick_photo)
                    .centerCrop()
                    .into(galleryImageView);
        }

        @Override
        public void clearMemoryCache()
        {

        }
    }

    public abstract static class ImagePickerCallbackImpl implements IHandlerCallBack
    {

        @Override
        public void onStart()
        {

        }

        @Override
        public void onCancel()
        {

        }

        @Override
        public void onFinish()
        {

        }

        @Override
        public void onError()
        {

        }
    }
}
