package com.cnsunrun.jiajiagou.forum.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseAdapter;
import com.cnsunrun.jiajiagou.common.widget.CircleTransform;
import com.cnsunrun.jiajiagou.forum.bean.ForumHomepageBean;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 论坛主页推荐帖子Adapter
 * <p>
 * author: yyc
 * date: 2017-08-22 20:30
 */

public class RecommendPostsAdapter extends BaseAdapter<ForumHomepageBean.InfoBean.ListBean> {

    public RecommendPostsAdapter(int layoutResId) {
        super(layoutResId);
    }


    @Override
    protected void convert(BaseViewHolder helper, ForumHomepageBean.InfoBean.ListBean item) {
//        int position = helper.getLayoutPosition();
//        LogUtils.i("pos: " + position);
//        if (position == 0) {
//            helper.setVisible(R.id.iv_line, false);
//        } else {
//            helper.setVisible(R.id.iv_line, true);
//
//        }
        getImageLoader().load(item.getAvatar()).error(R.drawable.nav_btn_personal_nor)
                .transform(new CenterCrop(mContext), new
                        CircleTransform(mContext)).into((ImageView) helper.getView(R.id
                .iv_avatar_posts));
//        Glide.with(mContext).load(item.getAvatarImg()).into((ImageView) helper.getView(R.id
// .iv_avatar_posts));
        final NineGridView mNiceGv = helper.getView(R.id.ngv_comment);
        final LinearLayout linearLayout = helper.getView(R.id.ll_root_layout);
        List<ForumHomepageBean.InfoBean.ListBean.ImageListBean> image_list = item.getImage_list();
        if (image_list.size() > 0) {
            linearLayout.post(new Runnable() {
                @Override
                public void run() {
                    int width = linearLayout.getWidth();
                    mNiceGv.setSingleImageSize(width - 30);
                }
            });
            mNiceGv.setVisibility(View.VISIBLE);
            List<ImageInfo> urlList = new ArrayList();
            for (ForumHomepageBean.InfoBean.ListBean.ImageListBean bean : image_list) {
                ImageInfo info = new ImageInfo();
                info.setThumbnailUrl(bean.getImage());
                info.setBigImageUrl(bean.getImage());
                urlList.add(info);
            }
            mNiceGv.setAdapter(new NineGridViewClickAdapter(mContext, urlList));
//            Glide.with(mContext).load(image_list.get(0).getImage()).into((ImageView) helper
//                    .getView(R.id
//                            .iv_content_posts));
//            helper.setVisible(R.id.iv_content_posts,true);
        } else {
            mNiceGv.setVisibility(View.GONE);
//            helper.setVisible(R.id.iv_content_posts,false);
        }

        helper.setText(R.id.tv_name_plate, item.getForum_title());
        helper.setText(R.id.tv_name_user, item.getMember_nickname());
        helper.setText(R.id.tv_title_posts, item.getTitle());
        helper.setText(R.id.tv_content_posts, item.getContent());
        helper.setText(R.id.tv_readnum_posts, "阅读 " + item.getViews());
        helper.setText(R.id.tv_commentnum_posts, "评论 " + item.getReplies());
        helper.setText(R.id.tv_likenum_posts, "点赞 " + item.getLikes());
        helper.setText(R.id.tv_date_posts, item.getLastpost_time());
        helper.addOnClickListener(R.id.iv_avatar_posts).addOnClickListener(R.id.tv_name_user);

    }
}
