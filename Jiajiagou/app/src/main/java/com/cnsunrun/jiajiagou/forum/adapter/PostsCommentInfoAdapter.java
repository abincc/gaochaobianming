package com.cnsunrun.jiajiagou.forum.adapter;

import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.widget.CircleTransform;
import com.cnsunrun.jiajiagou.forum.bean.PostsCommentBean;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 帖子评论Adapter
 * <p>
 * author:yyc
 * date: 2017-08-27 13:03
 */
public class PostsCommentInfoAdapter extends BaseQuickAdapter<PostsCommentBean.InfoBean
        .PostListBean, BaseViewHolder> {

    public PostsCommentInfoAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }
    private RequestManager imageLoader;
    public void setImageLoader(RequestManager imageLoader){
        this.imageLoader=imageLoader;
    }
    @Override
    protected void convert(BaseViewHolder helper, final PostsCommentBean.InfoBean.PostListBean
            item) {
        imageLoader.load(item.getAvatar()).error(R.drawable.nav_btn_personal_nor)
                .transform(new CenterCrop(mContext), new
                CircleTransform(mContext)).into((ImageView) helper.getView(R.id
                .iv_avatar_comment));
        helper.setText(R.id.tv_name_comment, item.getMember_nickname());
        helper.setText(R.id.tv_date_comment, item.getAdd_time());
        helper.setText(R.id.tv_content_comment, item.getContent());
        helper.setText(R.id.tv_floor_comment, item.getPosition());
        helper.setText(R.id.tv_likenum_comment, item.getLikes());
        final NineGridView mNiceGv = helper.getView(R.id.ngv_comment);
        final LinearLayout linearLayout = helper.getView(R.id.ll_getwidth);
        linearLayout.post(new Runnable() {
            @Override
            public void run() {
                int width = linearLayout.getWidth();
                mNiceGv.setSingleImageSize(width - 30);
            }
        });
        List<PostsCommentBean.InfoBean.PostListBean.ImageListBean> imageList = item
                .getImage_list();
        if (imageList.size() > 0) {
            List<ImageInfo> urlList = new ArrayList();
            for (PostsCommentBean.InfoBean.PostListBean.ImageListBean bean : imageList) {
                ImageInfo info = new ImageInfo();
                info.setThumbnailUrl(bean.getImage());
                info.setBigImageUrl(bean.getImage());
                urlList.add(info);
            }

            mNiceGv.setAdapter(new NineGridViewClickAdapter(mContext, urlList));
        } else {
            mNiceGv.setVisibility(NineGridView.GONE);
        }
        LinearLayout replyView = helper.getView(R.id.ll_reply_comment);//回复布局
        PostsCommentBean.InfoBean.PostListBean.PostInfoBean postInfo = item.getPost_info();
        if (TextUtils.isEmpty(postInfo.getMember_nickname())) {
//            helper.getView(R.id.tv_reply_name_comment)
            replyView.setVisibility(View.GONE);
        } else {
            replyView.setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_reply_name_comment, "回复" + postInfo.getMember_nickname());
            helper.setText(R.id.tv_reply_date_comment, postInfo.getAdd_time());
        }
//        helper.setOnClickListener(R.id.iv_btn_reply_comment, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                LogUtils.d("id"+item.getId());
//            }
//        });
        //#FF5230
        if (item.getIs_like().equals("1")) {//已经点赞,icon和数字为红色
            helper.setImageResource(R.id.iv_btn_like, R.drawable.details_btn_like_sel);
            helper.setTextColor(R.id.tv_likenum_comment, mContext.getResources().getColor(R.color
                    .like_red));
        } else {
            //未点赞
            helper.setImageResource(R.id.iv_btn_like, R.drawable.details_btn_like_nor);
            helper.setTextColor(R.id.tv_likenum_comment, mContext.getResources().getColor(R.color
                    .gray666));

        }
        helper.addOnClickListener(R.id.iv_btn_reply_comment);
        helper.addOnClickListener(R.id.iv_btn_like);
        helper.addOnClickListener(R.id.iv_avatar_comment);
        helper.addOnClickListener(R.id.tv_name_comment);
//        helper.addOnClickListener(R.id.iv_back_plate_detail);

    }
}
