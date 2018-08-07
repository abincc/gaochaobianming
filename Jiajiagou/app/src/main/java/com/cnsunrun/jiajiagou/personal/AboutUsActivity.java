package com.cnsunrun.jiajiagou.personal;

import android.text.TextUtils;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseCallBack;
import com.cnsunrun.jiajiagou.common.base.BaseHeaderActivity;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.google.gson.Gson;

import butterknife.BindView;

/**
 * Created by ${LiuDi}
 * on 2017/8/18on 17:01.
 * 关于我们
 */

public class AboutUsActivity extends BaseHeaderActivity
{
    @BindView(R.id.web_view)
    WebView mWebView;

    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight)
    {
        tvTitle.setText("关于我们");
    }

    @Override
    protected void init()
    {

        getData();
    }

    private void getData()
    {
        HttpUtils.get(NetConstant.GET_ABOUT_URL, null, new BaseCallBack(mContext)
        {
            @Override
            public void onResponse(String response, int id)
            {
                LogUtils.d(response);
                AboutUsResp resp = new Gson().fromJson(response, AboutUsResp.class);
                if (handleResponseCode(resp))
                {
                    String info = resp.info;
                    if (!TextUtils.isEmpty(info))
                    {
                        mWebView.loadUrl(info);
                    }
                }
            }
        });

    }

    @Override
    protected int getLayoutRes()
    {
        return R.layout.activity_about_us;
    }

}
