package com.cnsunrun.jiajiagou.forum.adapter;

import android.support.annotation.LayoutRes;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseAdapter;
import com.cnsunrun.jiajiagou.common.widget.CircleTransform;
import com.cnsunrun.jiajiagou.forum.bean.TabPostsBean;

import static com.cnsunrun.jiajiagou.R.id.iv_avatar_item;

/**
 * 个人主页-已发帖内容适配器
 * <p>
 * author:yyc
 * date: 2017-08-30 13:12
 */
public class TabPostsContentAdapter extends BaseAdapter<TabPostsBean.InfoBean> {

    public TabPostsContentAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, TabPostsBean.InfoBean item) {
        getImageLoader().load(item.getForum_icon()).transform(new CenterCrop(mContext), new
                CircleTransform(mContext)).into((ImageView) helper.getView(iv_avatar_item));
        helper.setText(R.id.tv_name_item, item.getForum_title());
        helper.setText(R.id.tv_title_item, item.getTitle());
        helper.setText(R.id.tv_readnum_item, "阅读 "+item.getViews());
        helper.setText(R.id.tv_commentnum_item, "评论 "+item.getReplies());
        helper.setText(R.id.tv_likenum_item, "点赞 "+item.getLikes());
        helper.setText(R.id.tv_date_item, item.getAdd_time());

    }
}
