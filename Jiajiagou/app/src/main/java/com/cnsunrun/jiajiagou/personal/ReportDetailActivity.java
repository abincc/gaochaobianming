package com.cnsunrun.jiajiagou.personal;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseCallBack;
import com.cnsunrun.jiajiagou.common.base.BaseHeaderActivity;
import com.cnsunrun.jiajiagou.common.constant.SPConstant;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.ConstantUtils;
import com.cnsunrun.jiajiagou.common.util.SPUtils;
import com.cnsunrun.jiajiagou.personal.bean.ReportDetailBean;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-11-24 16:37
 */
public class ReportDetailActivity extends BaseHeaderActivity {
    @BindView(R.id.recycle)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_report_title)
    TextView mTitle;
    private ReportDetailAdapter mReportDetailAdapter;

    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight) {
        tvTitle.setText("体检报告");
        tvRight.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void init() {
        String token = SPUtils.getString(this, SPConstant.TOKEN);
       String id = getIntent().getExtras().getString(ConstantUtils.REPORT_ID);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setNestedScrollingEnabled(false);
        mReportDetailAdapter = new ReportDetailAdapter(R.layout
                .item_report_detail);
        mReportDetailAdapter.setImageLoader(getImageLoader());
        mRecyclerView.setAdapter(mReportDetailAdapter);
        HashMap<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("report_id", id);
        HttpUtils.get(NetConstant.PEPORT_DETAIL, map, new BaseCallBack(mContext) {
            @Override
            public void onResponse(String response, int id) {
                ReportDetailBean bean= new Gson().fromJson(response, ReportDetailBean.class);
                if (handleResponseCode(bean)) {
                    if (bean.getStatus()==1) {
                        mTitle.setText( bean.getInfo().getTitle());
                        List<ReportDetailBean.InfoBean.ImagesBean> images = bean.getInfo()
                                .getImages();
                        mReportDetailAdapter.setNewData(images);
                    }
                }
            }
        });
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_report_detail;
    }
}
