package com.cnsunrun.jiajiagou.personal;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseHeaderActivity;
import com.cnsunrun.jiajiagou.common.base.Refreshable;
import com.cnsunrun.jiajiagou.common.constant.ArgConstants;
import com.cnsunrun.jiajiagou.common.constant.SPConstant;
import com.cnsunrun.jiajiagou.common.network.DialogCallBack;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.RecyclerLineDecoration;
import com.cnsunrun.jiajiagou.common.util.SPUtils;
import com.cnsunrun.jiajiagou.home.bean.NoticeBean;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * Created by ${LiuDi}
 * on 2017/8/19on 15:16.
 */

public class NoticeActivity extends BaseHeaderActivity
{
    @BindView(R.id.recycle)
    RecyclerView mRecycle;
    private NoticeAdapter mAdapter;

    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight)
    {
        tvTitle.setText("公告");
    }

    @Override
    public void onRefresh()
    {
        super.onRefresh();
        getData();
    }

    @Override
    protected void init()
    {
        initRecycles();
        getData();
    }

    private void getData()
    {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("district_id", SPUtils.getString(mContext, SPConstant.DISTRICT_ID));

        HttpUtils.get(NetConstant.NOTICE, hashMap, new DialogCallBack((Refreshable) NoticeActivity.this)
        {
            @Override
            public void onResponse(String response, int id)
            {
                LogUtils.d(response);
                NoticeResp resp = new Gson().fromJson(response, NoticeResp.class);
                if (handleResponseCode(resp))
                {
                    mAdapter.setNewData(resp.getInfo());
                }

            }
        });


    }


    protected void initRecycles()
    {
        mRecycle.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mAdapter = new NoticeAdapter(R.layout.item_notice, null);
        mAdapter.setEmptyView(View.inflate(mContext, R.layout.layout_empty, null));
        mRecycle.setAdapter(mAdapter);
        mRecycle.addItemDecoration(new RecyclerLineDecoration(mContext));
        mRecycle.addOnItemTouchListener(new OnItemClickListener()
        {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position)
            {
                List<NoticeBean> list = adapter.getData();

                Intent intent = new Intent(mContext, NoticeDetailActivity.class);
                intent.putExtra(ArgConstants.NOTICEID, list.get(position).notice_id);
                startActivity(intent);


            }
        });
    }

    @Override
    protected int getLayoutRes()
    {
        return R.layout.activity_notice;
    }

}
