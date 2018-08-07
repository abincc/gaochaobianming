package com.cnsunrun.jiajiagou.forum.adapter;

import android.support.annotation.LayoutRes;
import android.widget.ImageView;

import com.bumptech.glide.RequestManager;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.forum.bean.ForumPlateListBean;

/**
 * 论坛分类主列表Adapter
 * <p>
 * author:yyc
 * date: 2017-08-25 18:35
 */
public class ForumMenuRightAdapter extends BaseQuickAdapter<ForumPlateListBean.InfoBean.ForumListBean, BaseViewHolder> {
    public ForumMenuRightAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }
    private RequestManager imageLoader;
    public void setImageLoader(RequestManager imageLoader){
        this.imageLoader=imageLoader;
    }

    @Override
    protected void convert(BaseViewHolder helper, ForumPlateListBean.InfoBean.ForumListBean item) {
//        Glide.with(mContext).load(item.getIcon()).transform(new CenterCrop(mContext), new
//                CircleTransform(mContext)).into((ImageView) helper.getView(R.id
//                .iv_item_right));
        imageLoader.load(item.getIcon()).into((ImageView) helper.getView(R.id
                .iv_item_right));
        helper.setText(R.id.tv_item_right,item.getTitle());
    }
}
