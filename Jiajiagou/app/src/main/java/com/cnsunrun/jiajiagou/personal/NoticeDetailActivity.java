package com.cnsunrun.jiajiagou.personal;

import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseHeaderActivity;
import com.cnsunrun.jiajiagou.common.constant.ArgConstants;
import com.cnsunrun.jiajiagou.common.network.DialogCallBack;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.google.gson.Gson;

import java.util.HashMap;

import butterknife.BindView;

/**
 * Created by ${LiuDi}
 * on 2017/9/1on 14:29.
 */

public class NoticeDetailActivity extends BaseHeaderActivity
{
    @BindView(R.id.tv_Title)
    TextView mTvTitle;
    @BindView(R.id.tv_content)
    TextView mTvContent;
    private String mNoticeId;

    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight)
    {
        tvTitle.setText("公告内容");
    }

    @Override
    protected void init()
    {
        mNoticeId = getIntent().getStringExtra(ArgConstants.NOTICEID);
        getData();

    }

    private void getData()
    {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("notice_id", mNoticeId);
        HttpUtils.get(NetConstant.NOTICE_INFO, hashMap, new DialogCallBack(mContext)
        {
            @Override
            public void onResponse(String response, int id)
            {
                LogUtils.d(response);
                NoticeDetailResp resp = new Gson().fromJson(response, NoticeDetailResp.class);
                if (handleResponseCode(resp))
                {
                    NoticeDetailResp.InfoBean info = resp.getInfo();
                    if (info != null)
                    {
                        mTvTitle.setText(Html.fromHtml(info.getTitle()));
                        mTvContent.setText(Html.fromHtml(info.getContent()));

                    }
                }

            }
        });

    }

    @Override
    protected int getLayoutRes()
    {
        return R.layout.activity_notice_detail;
    }

}
