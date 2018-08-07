package com.cnsunrun.jiajiagou.personal;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseHeaderActivity;
import com.cnsunrun.jiajiagou.common.base.Refreshable;
import com.cnsunrun.jiajiagou.common.constant.SPConstant;
import com.cnsunrun.jiajiagou.common.network.DialogCallBack;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.ConstantUtils;
import com.cnsunrun.jiajiagou.common.util.SPUtils;
import com.cnsunrun.jiajiagou.personal.bean.ReportListBean;
import com.cnsunrun.jiajiagou.product.MyItemDecoration;
import com.google.gson.Gson;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-11-23 11:17
 */
public class MedicalReportActivity extends BaseHeaderActivity implements BaseQuickAdapter
        .OnItemClickListener {

    @BindView(R.id.recycle)
    RecyclerView mRecycle;
    private ReportListAdapter mReportListAdapter;

    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight) {
        tvTitle.setText("体检报告");
        tvRight.setText("上传体检");
        tvRight.setVisibility(View.VISIBLE);
    }

    @Override
    protected void init() {
        mRecycle.setLayoutManager(new LinearLayoutManager(this));
        mRecycle.addItemDecoration(new MyItemDecoration(this,R.color.grayF4,R.dimen.dp_1));
        mRecycle.setNestedScrollingEnabled(false);
        mReportListAdapter = new ReportListAdapter(R.layout.item_report);
        mRecycle.setAdapter(mReportListAdapter);
        mReportListAdapter.setOnItemClickListener(this);
        requestNet();
    }

    private void requestNet(){
        String token = SPUtils.getString(this, SPConstant.TOKEN);
        HashMap<String, String> map = new HashMap<>();
        map.put("token", token);
        HttpUtils.get(NetConstant.PEPORT_LIST, map, new DialogCallBack((Refreshable) MedicalReportActivity.this) {
            @Override
            public void onResponse(String response, int id) {
                ReportListBean reportListBean=new Gson().fromJson(response, ReportListBean.class);
                if (handleResponseCode(reportListBean)) {
                    if (reportListBean.getStatus()==1) {
                        List<ReportListBean.InfoBean> list = reportListBean.getInfo();
                        mReportListAdapter.setNewData(list);
                        if (list.size()==0) {
//                        mTabLikeContentAdapter.setEmptyView(View.inflate(mContext,R.layout.layout_empty,null));
                            mReportListAdapter.setEmptyView(getLayoutInflater().inflate( R
                                    .layout.layout_empty_forum, (ViewGroup) mRecycle.getParent(),false));
                        }
                    }
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRreshEvent(ReportEvent event)
    {
        onRefresh();
    }

    @OnClick(R.id.tv_right)
    public void onViewClick(View v){
        switch (v.getId()){
            case R.id.tv_right:
                startActivity(UploadReportActivity.class);
                break;
        }
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        requestNet();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_medical_report;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        List<ReportListBean.InfoBean> data = adapter.getData();
        String id = data.get(position).getReport_id();
        Bundle bundle = new Bundle();
        bundle.putString(ConstantUtils.REPORT_ID, id);
        startActivity(ReportDetailActivity.class, false, bundle);

    }
}
