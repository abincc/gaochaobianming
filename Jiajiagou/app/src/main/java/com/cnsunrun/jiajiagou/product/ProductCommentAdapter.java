package com.cnsunrun.jiajiagou.product;

import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseAdapter;
import com.cnsunrun.jiajiagou.common.widget.CircleTransform;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.width;

/**
 * 商品详情评论列表
 * <p>
 * author:yyc
 * date: 2017-09-13 12:33
 */
public class ProductCommentAdapter extends BaseAdapter<ProductCommentBean.InfoBean> {

    public ProductCommentAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProductCommentBean.InfoBean item) {
        getImageLoader().load(item.getHeadimg()).error(R.drawable.nav_btn_personal_nor)
                .transform(new CenterCrop(mContext), new
                        CircleTransform(mContext)).into((ImageView) helper.getView(R.id.iv_avatar));
        helper.setText(R.id.tv_name, item.getNickname())
                .setText(R.id.tv_content, item.getContent());
        String reply = item.getReply();
        LinearLayout replyView = helper.getView(R.id.ll_reply);

        if (TextUtils.isEmpty(reply)) {
            replyView.setVisibility(View.GONE);
        } else {
            replyView.setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_reply, reply);
        }
        List<String> images = item.getImages();
        final NineGridView mNiceGv = helper.getView(R.id.ngv_comment);
        if (images.size() > 0) {
            final LinearLayout linearLayout = helper.getView(R.id.ll_root_product);
            linearLayout.post(new Runnable() {
                @Override
                public void run() {
                    linearLayout.getWidth();
                    mNiceGv.setSingleImageSize(width - 30);
                }
            });
            mNiceGv.setVisibility(View.VISIBLE);
            List<ImageInfo> urlList = new ArrayList();
            for (String image : images) {
                ImageInfo info = new ImageInfo();
                info.setThumbnailUrl(image);
                info.setBigImageUrl(image);
                urlList.add(info);
            }
            mNiceGv.setAdapter(new NineGridViewClickAdapter(mContext, urlList));
        } else {
            mNiceGv.setVisibility(NineGridView.GONE);
        }
    }
}
