package com.cnsunrun.jiajiagou.common.base;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.bumptech.glide.RequestManager;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-12-30 17:11
 */
public abstract class BaseAdapter<T > extends BaseQuickAdapter<T, BaseViewHolder> {
    private RequestManager imageLoader;
    public BaseAdapter(@LayoutRes int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
    }

    public BaseAdapter(@Nullable List<T> data) {
        super(data);
    }

    public BaseAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    public void setImageLoader(RequestManager imageLoader){
        this.imageLoader=imageLoader;
    }

    public RequestManager getImageLoader(){
       return imageLoader;
    }


}
