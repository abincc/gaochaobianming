package com.cnsunrun.jiajiagou.personal;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseHeaderActivity;
import com.cnsunrun.jiajiagou.common.constant.SPConstant;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.ConstantUtils;
import com.cnsunrun.jiajiagou.common.util.LogUtils;
import com.cnsunrun.jiajiagou.common.util.SPUtils;
import com.cnsunrun.jiajiagou.personal.bean.MessageDetailBean;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-09-14 11:54
 */
public class MessageDetailActivity extends BaseHeaderActivity {
    @BindView(R.id.tv_message_title)
    TextView mTitleTv;
    @BindView(R.id.tv_message_date)
    TextView mDateTv;
    @BindView(R.id.tv_message_content)
    TextView mContentTv;
    private String mTitle;

    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight) {
        tvTitle.setText(mTitle + "消息");
    }

    @Override
    protected void init() {
        String messageId = getIntent().getStringExtra(ConstantUtils.MESSAGE_ID);
        mTitle = getIntent().getStringExtra(ConstantUtils.MESSAGE_TYPE);
        LogUtils.printD("" + mTitle);
        HashMap<String, String> map = new HashMap<>();
        map.put("token", SPUtils.getString(this, SPConstant.TOKEN));
        map.put("message_id", messageId);
        HttpUtils.get(NetConstant.MESSAGE_DETAIL, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                MessageDetailBean messageDetailBean = new Gson().fromJson(response,
                        MessageDetailBean.class);
                if (messageDetailBean.getStatus() == 1) {
                    MessageDetailBean.InfoBean info = messageDetailBean.getInfo();
                    mContentTv.setText(info.getContent());
                    mTitleTv.setText(info.getTitle());
                    mDateTv.setText(info.getAdd_time());
                }
            }
        });
    }

    @OnClick({R.id.iv_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressedSupport();
                break;
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_message_detail;
    }
}
