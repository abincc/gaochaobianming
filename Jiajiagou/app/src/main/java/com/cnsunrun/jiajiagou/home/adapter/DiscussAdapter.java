package com.cnsunrun.jiajiagou.home.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.CZBaseQucikAdapter;
import com.cnsunrun.jiajiagou.home.bean.ConferenceHallBean;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by j2yyc on 2018/1/23.
 */

public class DiscussAdapter extends CZBaseQucikAdapter<ConferenceHallBean.InfoBean> {
    private int maxWidth;
    public DiscussAdapter(int maxWidth) {
        super(R.layout.item_discuss);
        this.maxWidth=maxWidth;
    }

    @Override
    protected void convert(BaseViewHolder helper, ConferenceHallBean.InfoBean item) {
        helper.setText(R.id.tv_title, item.getTitle())
                .setText(R.id.tv_content,item.getDescription())
                .setText(R.id.tv_join_number,"参与人数 : "+item.getNumber())
                .setText(R.id.tv_date,"截止时间 : "+item.getEnd_date());
        if (item.getType().equals("0")) {
            helper.setText(R.id.tv_to_vote,item.getType_title())
                    .setTextColor(R.id.tv_to_vote,mContext.getResources().getColor(R.color.red_vote));
        }else {
            helper.setText(R.id.tv_to_vote,item.getType_title())
                    .setTextColor(R.id.tv_to_vote,mContext.getResources().getColor(R.color.green_discuss_vote));
        }
        helper.setText(R.id.tv_to_vote,item.getType_title());
        NineGridView photoNgv = helper.getView(R.id.ngv_discuss_photo);
        photoNgv.setSingleImageSize(maxWidth);
        photoNgv.setSingleImageRatio(1.9f);
        List<ConferenceHallBean.InfoBean.ImagesBean> images = item.getImages();
        if (images!=null&&images.size()>0) {
            photoNgv.setVisibility(View.VISIBLE);
            List<ImageInfo> urlList = new ArrayList();
            for (ConferenceHallBean.InfoBean.ImagesBean image : images) {
                ImageInfo info = new ImageInfo();
                info.setThumbnailUrl(image.getImage());
                info.setBigImageUrl(image.getImage());
                urlList.add(info);
            }
            photoNgv.setAdapter(new NineGridViewClickAdapter(mContext, urlList));
        }else {
            photoNgv.setVisibility(View.GONE);
        }


    }

}
