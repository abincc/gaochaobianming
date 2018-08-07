package com.cnsunrun.jiajiagou.forum.adapter;

import android.widget.ImageView;

import com.bumptech.glide.RequestManager;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.forum.bean.ForumSearchBean;

/**
 * 论坛搜索Adapter
 *
 * author: yyc
 * date: 2017-08-24 15:25
 */
public class ForumSearchAdapter extends BaseQuickAdapter<ForumSearchBean.InfoBean.ThreadListBean,BaseViewHolder>{

    public ForumSearchAdapter(int layoutResId){
        super(layoutResId);
    }

    private RequestManager imageLoader;
    public void setImageLoader(RequestManager imageLoader){
        this.imageLoader=imageLoader;
    }

    @Override
    protected void convert(BaseViewHolder helper, ForumSearchBean.InfoBean.ThreadListBean item) {
        imageLoader.load(item.getForum_icon()).error(R.drawable.nav_btn_personal_nor).into((ImageView) helper.getView(R.id.iv_avatar_posts));
        helper.setText(R.id.tv_name_posts,item.getForum_title());
        helper.setText(R.id.tv_title_posts,item.getTitle());
        helper.setText(R.id.tv_content_posts,item.getContent());
        helper.setText(R.id.tv_readnum_posts,"阅读 "+item.getViews());
        helper.setText(R.id.tv_commentnum_posts,"评论 "+item.getReplies());
        helper.setText(R.id.tv_date_posts,item.getAdd_time());
    }
}
