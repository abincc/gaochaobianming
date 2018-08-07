package com.cnsunrun.jiajiagou.forum.adapter;

import android.support.annotation.LayoutRes;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseAdapter;
import com.cnsunrun.jiajiagou.forum.bean.ForumSearchBean;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-09-19 20:48
 */
public class SearchPromptAdapter extends BaseAdapter<ForumSearchBean.InfoBean.ThreadListBean> {


    public SearchPromptAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, ForumSearchBean.InfoBean.ThreadListBean item) {
        getImageLoader().load(item.getForum_icon()).error(R.drawable.nav_btn_personal_nor).into((ImageView) helper.getView(R.id.iv_avatar_posts));
        helper.setText(R.id.tv_name_posts,item.getForum_title());
        helper.setText(R.id.tv_title_posts,item.getTitle());
        helper.setText(R.id.tv_content_posts,item.getContent());
        helper.setText(R.id.tv_readnum_posts,"阅读 "+item.getViews());
        helper.setText(R.id.tv_commentnum_posts,"评论 "+item.getReplies());
        helper.setText(R.id.tv_date_posts,item.getAdd_time());
    }
}
