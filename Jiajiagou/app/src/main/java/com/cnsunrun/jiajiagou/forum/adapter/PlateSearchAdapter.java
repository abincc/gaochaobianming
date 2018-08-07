package com.cnsunrun.jiajiagou.forum.adapter;

import android.support.annotation.LayoutRes;
import android.widget.ImageView;

import com.bumptech.glide.RequestManager;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.forum.bean.ForumSearchBean;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-09-01 17:45
 */
public class PlateSearchAdapter extends BaseQuickAdapter<ForumSearchBean.InfoBean.ForumListBean,BaseViewHolder> {
    public PlateSearchAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    private RequestManager imageLoader;
    public void setImageLoader(RequestManager imageLoader){
        this.imageLoader=imageLoader;
    }
    @Override
    protected void convert(BaseViewHolder helper, ForumSearchBean.InfoBean.ForumListBean item) {
            helper.setText(R.id.tv_plate,item.getTitle());
        imageLoader.load(item.getIcon()).into((ImageView) helper.getView(R.id.iv_plate));
    }
}
