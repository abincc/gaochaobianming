package com.cnsunrun.jiajiagou.forum.adapter;

import android.support.annotation.LayoutRes;
import android.widget.ImageView;

import com.bumptech.glide.RequestManager;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.forum.bean.ForumHomepageBean;

/**
 * 模块类型和名称,数据由用户上传
 *
 * author: yyc
 * date: 2017-08-22 20:19
 */
public class PlateTypeAdapter extends BaseQuickAdapter<ForumHomepageBean.InfoBean.ForumBean,BaseViewHolder>{
    public PlateTypeAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    private RequestManager imageLoader;
    public void setImageLoader(RequestManager imageLoader){
        this.imageLoader=imageLoader;
    }
    @Override
    protected void convert(BaseViewHolder helper, ForumHomepageBean.InfoBean.ForumBean item) {
        helper.setText(R.id.tv_plate,item.getTitle());
        imageLoader.load(item.getIcon()).into((ImageView) helper.getView(R.id.iv_plate));

    }
}
