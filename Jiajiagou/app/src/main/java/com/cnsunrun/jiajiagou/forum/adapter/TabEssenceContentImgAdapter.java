package com.cnsunrun.jiajiagou.forum.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.common.base.BaseAdapter;
import com.cnsunrun.jiajiagou.forum.bean.PlateEssenceThemeBean;

import java.util.List;

import static com.cnsunrun.jiajiagou.R.id.iv_content_posts;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-09-06 12:25
 */
public class TabEssenceContentImgAdapter extends BaseAdapter<PlateEssenceThemeBean.InfoBean.ImageListBean> {

    public TabEssenceContentImgAdapter(@LayoutRes int layoutResId, @Nullable List<PlateEssenceThemeBean.InfoBean.ImageListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PlateEssenceThemeBean.InfoBean.ImageListBean item) {
        getImageLoader().load(item.getImage()).into((ImageView) helper.getView(iv_content_posts));

    }
}
