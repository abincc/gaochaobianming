package com.cnsunrun.jiajiagou.home.adapter;

import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.widget.CircleTransform;
import com.cnsunrun.jiajiagou.home.homepage.HomeResp;

/**
 * Created by j2yyc on 2018/1/24.
 */

public class HomeEnterAdapter extends BaseQuickAdapter<HomeResp.InfoBean.Entry,BaseViewHolder> {
    private RequestManager imageLoader;
    private int imageWidth;

    public HomeEnterAdapter(RequestManager imageLoader,int imageWidth) {
        super(R.layout.item_home_enter);
        this.imageLoader = imageLoader;
        this.imageWidth=imageWidth;
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeResp.InfoBean.Entry item) {
        ImageView iamgeView=helper.getView(R.id.iv_image);
        ViewGroup.LayoutParams params = iamgeView.getLayoutParams();
        params.width=imageWidth;
        params.height=imageWidth;
        imageLoader.load(item.getImage()).transform(new CenterCrop(mContext),new CircleTransform(mContext)).into(iamgeView);
        helper.setText(R.id.tv_title,item.getTitle());
    }
}
