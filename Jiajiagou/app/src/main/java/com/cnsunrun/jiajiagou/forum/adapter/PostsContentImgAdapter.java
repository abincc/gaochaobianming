package com.cnsunrun.jiajiagou.forum.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.RequestManager;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.forum.bean.PostsDetailBean;

import java.util.List;

import static com.cnsunrun.jiajiagou.R.id.iv_content_posts;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-09-06 12:11
 */
public class PostsContentImgAdapter extends BaseQuickAdapter<PostsDetailBean.InfoBean.ImageListBean,BaseViewHolder> {

    public PostsContentImgAdapter(@LayoutRes int layoutResId, @Nullable List<PostsDetailBean.InfoBean.ImageListBean> data) {
        super(layoutResId, data);
    }
    private RequestManager imageLoader;
    public void setImageLoader(RequestManager imageLoader){
        this.imageLoader=imageLoader;
    }
    @Override
    protected void convert(BaseViewHolder helper, PostsDetailBean.InfoBean.ImageListBean item) {
                imageLoader.load(item.getImage()).into((ImageView) helper.getView(iv_content_posts));
    }
}
