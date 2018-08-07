package com.cnsunrun.jiajiagou.home;

import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseHeaderActivity;
import com.cnsunrun.jiajiagou.common.base.Refreshable;
import com.cnsunrun.jiajiagou.common.network.DialogCallBack;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.ConstantUtils;
import com.cnsunrun.jiajiagou.home.bean.NewsDetailBean;
import com.google.gson.Gson;

import java.util.HashMap;

import butterknife.BindView;

/**
 * Created by j2yyc on 2018/1/23.
 */

public class NewsDetailActivity extends BaseHeaderActivity {
    @BindView(R.id.tv_Title)
    TextView mTvTitle;
    @BindView(R.id.tv_content)
    TextView mTvContent;
    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.iv_image)
    ImageView mIvImage;
    private String id;
    @BindView(R.id.web_news)
    WebView mWebNews;

    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight) {
        tvTitle.setText("政务内容");
    }

    @Override
    protected void init() {
//        int screenWidth = DisplayUtil.px2dip(this, DisplayUtil.getScreenWidth(this) - 30);
//        ViewGroup.LayoutParams params = mIvImage.getLayoutParams();
//        params.height= (int) (screenWidth*0.64);
//        params.height = DisplayUtil.dip2px(this, (float) (screenWidth * 0.6));
//        LogUtils.i("width " + screenWidth);
//        LogUtils.i("height " + DisplayUtil.dip2px(this, (float) (screenWidth * 0.6)));
//        mIvImage.setLayoutParams(params);
        id = getIntent().getStringExtra(ConstantUtils.GOVERNMENT_ID);
//        String title = getIntent().getStringExtra(ConstantUtils.GOVERNMENT_TITLE);
        String url = getIntent().getStringExtra(ConstantUtils.GOVERNMENT_URL);
//        String content = getIntent().getStringExtra(ConstantUtils.GOVERNMENT_CONTENT);
//        String image = getIntent().getStringExtra(ConstantUtils.GOVERNMENT_IMAGE);
//        String date = getIntent().getStringExtra(ConstantUtils.GOVERNMENT_DATE);
//        mTvTitle.setText(title);
//        mTvContent.setText(Html.fromHtml(content));
//        mTvDate.setText(date);
        mWebNews.loadUrl(url);
//        getImageLoader().load(image).into(mIvImage);
//        requestNet();
    }

    private void requestNet() {
        HashMap map = new HashMap();
        map.put("government_id", id);

        HttpUtils.get(NetConstant.GOVERNMENT_INFO, map, new DialogCallBack((Refreshable) NewsDetailActivity.this) {
            @Override
            public void onResponse(String response, int id) {
                NewsDetailBean bean = new Gson().fromJson(response, NewsDetailBean.class);
                if (bean.getStatus() == 1) {
                    NewsDetailBean.InfoBean info = bean.getInfo();
//                    mTvTitle.setText(info.getTitle());
//                    mTvDate.setText(info.getAdd_time());
//                    mWebNews.loadData(info.getContent(), "text/html", "utf-8");
                }
            }
        });
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_news_detail;
    }

}
