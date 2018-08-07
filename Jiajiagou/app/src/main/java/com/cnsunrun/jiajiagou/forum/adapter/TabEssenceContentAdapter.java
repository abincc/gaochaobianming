package com.cnsunrun.jiajiagou.forum.adapter;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseAdapter;
import com.cnsunrun.jiajiagou.common.widget.CircleTransform;
import com.cnsunrun.jiajiagou.forum.bean.PlateEssenceThemeBean;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.cnsunrun.jiajiagou.R.id.iv_avatar_posts;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-08-25 12:15
 */
public class TabEssenceContentAdapter extends BaseAdapter<PlateEssenceThemeBean.InfoBean> {
    private int width;
    public TabEssenceContentAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }
    public void setWidth(int width){
        this.width=width;
    }
    @Override
    protected void convert(BaseViewHolder helper, PlateEssenceThemeBean.InfoBean item) {
//        Glide.with(mContext).load(item.getImage_list().get(0).getImage()).into((ImageView) helper.getView(iv_content_posts));
        getImageLoader().load(item.getAvatar()).transform(new CenterCrop(mContext), new
                CircleTransform(mContext)).into((ImageView) helper.getView(iv_avatar_posts));
        final NineGridView nineGv = helper.getView(R.id.nice_gv);

        List<PlateEssenceThemeBean.InfoBean.ImageListBean> imageList = item.getImage_list();
        if (imageList.size()>0) {
            //        Glide.with(mContext).load(item.getImage_list().get(0).getImage()).into((ImageView) helper.getView(iv_content_posts));
            List<ImageInfo> mContentImgs = new ArrayList();
            for (PlateEssenceThemeBean.InfoBean.ImageListBean bean : imageList) {
                ImageInfo imageInfo = new ImageInfo();
                imageInfo.setBigImageUrl(bean.getImage());
                imageInfo.setThumbnailUrl(bean.getImage());
                mContentImgs.add(imageInfo);
            }
            nineGv.setSingleImageSize(width);
            nineGv.setAdapter(new NineGridViewClickAdapter(mContext, mContentImgs));
        }else {
            nineGv.setVisibility(View.GONE);
        }
        helper.setText(R.id.tv_name_user, item.getMember_nickname());
        helper.setText(R.id.tv_title_posts, item.getTitle());
        helper.setText(R.id.tv_content_posts, item.getContent());
        helper.setText(R.id.tv_readnum_posts, "阅读 "+item.getViews());
        helper.setText(R.id.tv_commentnum_posts, "评论 "+item.getReplies());
        helper.setText(R.id.tv_likenum_posts, "点赞 "+item.getLikes());
        helper.setText(R.id.tv_date_posts, item.getLastpost_time());

    }
}
