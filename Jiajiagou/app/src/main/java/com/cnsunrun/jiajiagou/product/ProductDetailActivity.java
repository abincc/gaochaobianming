package com.cnsunrun.jiajiagou.product;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.ConvertUtils;
import com.blankj.utilcode.utils.LogUtils;
import com.cnsunrun.jiajiagou.MainActivity;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseCallBack;
import com.cnsunrun.jiajiagou.common.base.BaseHeaderActivity;
import com.cnsunrun.jiajiagou.common.base.BaseResp;
import com.cnsunrun.jiajiagou.common.base.BaseTitleIndicatorAdapter;
import com.cnsunrun.jiajiagou.common.base.Refreshable;
import com.cnsunrun.jiajiagou.common.constant.ArgConstants;
import com.cnsunrun.jiajiagou.common.constant.JjgConstant;
import com.cnsunrun.jiajiagou.common.network.DialogCallBack;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.share.SharePop;
import com.cnsunrun.jiajiagou.common.widget.IndicatorSwitchHelper;
import com.cnsunrun.jiajiagou.login.LoginActivity;
import com.google.gson.Gson;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;
import com.zhy.http.okhttp.callback.StringCallback;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Description:
 * Data：2017/8/24 0024-下午 5:10
 * Blog：http://blog.csdn.net/u013983934
 * Author: CZ
 */
public class ProductDetailActivity extends BaseHeaderActivity implements IndicatorSwitchHelper
        .OnPageChangedListener
{
    @BindView(R.id.magic_indicator)
    MagicIndicator mMagicIndicator;
    @BindView(R.id.banner)
    Banner mBanner;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_desc)
    TextView mTvDesc;
    @BindView(R.id.tv_price)
    TextView mTvPrice;
    @BindView(R.id.web_view)
    WebView mWebView;
    @BindView(R.id.recycle_product_comment)
    RecyclerView mCommentRecycleView;
    @BindView(R.id.cb_collect)
    CheckBox mCbCollect;
    private BaseTitleIndicatorAdapter mIndicatorAdapter;
    private String mProductId;
    private String mComment_number;
    private BuyProductPop mProductPop;
    private SharePop mSharePop;


    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight)
    {
        tvTitle.setText("商品详情");

        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setImageResource(R.drawable.cart_detail);
        imageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (JjgConstant.isLogin(mContext))
                {
                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.putExtra("check_index", 2);
                    startActivity(intent);
                    EventBus.getDefault().post(new FreshEvent());
                } else
                {
                    startActivity(new Intent(mContext, LoginActivity.class));
                }

            }
        });
        ((ViewGroup) findViewById(R.id.rl_header_50)).addView(imageView, tvRight.getLayoutParams());
    }

    @Override
    protected void init()
    {
        setWindowBackground(R.color.white);
        mProductId = getIntent().getStringExtra(ArgConstants.PRODUCTID);
        mBanner.setImageLoader(new ImageLoader()
        {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView)
            {
                getImageLoader().load(path).into(imageView);
            }
        });
        getData();
        initCommentView();
    }

    private void initCommentView()
    {
        HashMap<String, String> map = new HashMap();
        map.put("product_id", mProductId);
        mCommentRecycleView.setNestedScrollingEnabled(false);
        mCommentRecycleView.setLayoutManager(new LinearLayoutManager(mContext));
        mCommentRecycleView.addItemDecoration(new MyItemDecoration(mContext, R.color.grayF4, R
                .dimen.dp_8));
        final ProductCommentAdapter adapter = new ProductCommentAdapter(R.layout
                .item_product_comment_info);
        adapter.setImageLoader(getImageLoader());
        mCommentRecycleView.setAdapter(adapter);
        HttpUtils.get(NetConstant.PRODUCT_COMMENT, map, new StringCallback()
        {
            @Override
            public void onError(Call call, Exception e, int id)
            {

            }

            @Override
            public void onResponse(String response, int id)
            {
                ProductCommentBean commentBean = new Gson().fromJson(response,
                        ProductCommentBean.class);
                if (commentBean.getStatus() == 1)
                {
                    List<ProductCommentBean.InfoBean> info = commentBean.getInfo();
                    adapter.setNewData(info);
                    if (info.size()==0) {
                        adapter.setEmptyView(View.inflate(mContext,R.layout.layout_empty,null));
                    }
                }
            }
        });
    }


    private void initIndicator()
    {

        IndicatorSwitchHelper indicatorSwitchHelper = new IndicatorSwitchHelper(mMagicIndicator,
                this);
        final CommonNavigator commonNavigator = new CommonNavigator(mContext);
        List<String> titles = Arrays.asList("详情", "评论( " + mComment_number + " )");

        mIndicatorAdapter = new BaseTitleIndicatorAdapter(indicatorSwitchHelper, titles,
                0xff666666, 0xff007ee5, 14)
        {
            @Override
            public IPagerIndicator getIndicator(Context context)
            {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setColors(0xff007ee5);
                indicator.setLineWidth(ConvertUtils.dp2px(context, 18));
                indicator.setYOffset(ConvertUtils.dp2px(context, 2));
                indicator.setRoundRadius(5);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                return indicator;
            }
        };
        commonNavigator.setAdapter(mIndicatorAdapter);
        commonNavigator.setAdjustMode(true);
        mMagicIndicator.setNavigator(commonNavigator);
    }

    private void initWebView()
    {
        if (Build.VERSION.SDK_INT >= 21)
            mWebView.setNestedScrollingEnabled(false);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setSupportZoom(false);
        webSettings.setTextSize(WebSettings.TextSize.LARGER);
        webSettings.setDefaultFontSize(18);
//        webSettings.setUseWideViewPort(true);//关键点
        mWebView.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.loadUrl(url);
                return true;
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient()
        {

        });
    }

    private void getData()
    {
        HashMap<String, String> hashMap = new HashMap<>();
//        hashMap.put("token", JjgConstant.getToken(mContext));
        hashMap.put("product_id", mProductId);
        if (JjgConstant.isLogin(mContext))
            hashMap.put("token", JjgConstant.getToken(mContext));
        HttpUtils.get(NetConstant.PRODUCT_INFO, hashMap, new DialogCallBack((Refreshable) ProductDetailActivity.this)
        {
            @Override
            public void onResponse(String response, int id)
            {
                LogUtils.d(response);
                ProductDetailResp resp = new Gson().fromJson(response, ProductDetailResp.class);
                if (handleResponseCode(resp))
                {
                    ProductDetailResp.InfoBean info = resp.getInfo();

                    setInfo(info);
                }

            }
        });


    }

    private void setInfo(ProductDetailResp.InfoBean info)
    {
        mTvName.setText(info.getTitle());
        mTvDesc.setText(info.getDescription());
        mTvPrice.setText("¥" + info.getPrice());
        mComment_number = info.getComment_number();

        fillBanner(info.getImages());
        mCbCollect.setChecked(info.getIs_collect() == 1);

        mWebView.loadUrl(info.getContent());

        initWebView();

        initIndicator();

        if (mProductPop == null)
            mProductPop = new BuyProductPop(mContext, info);


    }

    @Override
    public void onRefresh()
    {
        super.onRefresh();
        getData();
    }

    private void fillBanner(List<ProductDetailResp.InfoBean.ImagesBean> images)
    {
        ArrayList<String> pics = new ArrayList<>();

        for (ProductDetailResp.InfoBean.ImagesBean imageBean : images)
        {
            pics.add(imageBean.getImage());

        }
        mBanner.update(pics);
    }

    @Override
    protected int getLayoutRes()
    {
        return R.layout.activity_product_detail;
    }

    @Override
    public void onPageChanged(int pos)
    {
        switch (pos)
        {
            case 0: //详情
                mWebView.setVisibility(View.VISIBLE);
                mCommentRecycleView.setVisibility(View.GONE);
                break;
            case 1://评论
                mWebView.setVisibility(View.GONE);
                mCommentRecycleView.setVisibility(View.VISIBLE);
                break;
        }
    }


    @OnClick({R.id.cb_collect, R.id.tv_share, R.id.tv_add_cart, R.id.tv_buy})
    public void onViewClicked(final View view)
    {
        switch (view.getId())
        {
            case R.id.cb_collect:
                if (JjgConstant.isLogin(mContext))
                {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("token", JjgConstant.getToken(mContext));
                    hashMap.put("product_id", mProductId);
                    if (mCbCollect.isChecked())
                    {
                        HttpUtils.post(NetConstant.COLLECT, hashMap, new BaseCallBack(mContext)
                        {
                            @Override
                            public void onResponse(String response, int id)
                            {
                                LogUtils.d(response);
                                BaseResp baseResp = new Gson().fromJson(response, BaseResp.class);
                                if (handleResponseCode(baseResp))
                                {
                                    showToast(baseResp.getMsg(), 1);
                                } else
                                {
                                    mCbCollect.setChecked(false);
                                }
                            }
                        });
                    } else
                    {
                        HttpUtils.post(NetConstant.COLLECTIONCANCEL, hashMap, new BaseCallBack
                                (mContext)
                        {
                            @Override
                            public void onResponse(String response, int id)
                            {
                                LogUtils.d(response);
                                BaseResp baseResp = new Gson().fromJson(response, BaseResp.class);
                                if (handleResponseCode(baseResp))
                                {
                                    showToast(baseResp.getMsg(), 1);
                                } else
                                {
                                    mCbCollect.setChecked(true);
                                }
                            }
                        });
                    }
                } else
                {
                    mCbCollect.setChecked(false);
                    startActivity(new Intent(mContext, LoginActivity.class));
                }

                break;
            case R.id.tv_share:
//                if (true)
//                    return;
                HashMap<String, String> map = new HashMap<>();
                map.put("product_id", mProductId);
                HttpUtils.get(NetConstant.COMMODITY_DETAIL_SHARE, map, new StringCallback()
                {
                    @Override
                    public void onError(Call call, Exception e, int id)
                    {

                    }

                    @Override
                    public void onResponse(String response, int id)
                    {
                        LogUtils.d(response);
                        ProductShareBean shareBean = new Gson().fromJson(response,
                                ProductShareBean.class);
                        if (shareBean.getStatus() == 1)
                        {
                            ProductShareBean.InfoBean info = shareBean.getInfo();
                            if (mSharePop == null)
                                mSharePop = new SharePop(mContext);
                            mSharePop.setShareData(info.getTitle(), info.getContent(), info
                                    .getImage(), info.getUrl());
                            mSharePop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                        }
                    }
                });
                break;
            case R.id.tv_add_cart:
                if (JjgConstant.isLogin(mContext))
                {
                    if (mProductPop != null)
                    {
                        mProductPop.setIsBuy(false);
                        mProductPop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                    }
                } else
                {
                    startActivity(new Intent(mContext, LoginActivity.class));
                }

                break;
            case R.id.tv_buy:
                if (JjgConstant.isLogin(mContext))
                {
                    if (mProductPop != null)
                    {
                        mProductPop.setIsBuy(true);
                        mProductPop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                    }
                } else
                {
                    startActivity(new Intent(mContext, LoginActivity.class));
                }

                break;
        }
    }

}
