package com.cnsunrun.jiajiagou.home;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseCallBack;
import com.cnsunrun.jiajiagou.common.base.BaseHeaderActivity;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.SpaceItemDecoration;
import com.google.gson.Gson;

import butterknife.BindView;

/**
 * Created by chengzhiyong on 17/8/20.
 */

public class ConvenienceActivity extends BaseHeaderActivity
{
    @BindView(R.id.recycle)
    RecyclerView mRecycle;
    private ConvenienceAdapter mAdapter;

    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight)
    {
        tvTitle.setText(R.string.convenience_title);
    }

    @Override
    protected void init()
    {
        initRecycles();
        getData();
    }

    private void getData()
    {

        HttpUtils.get(NetConstant.CONVENIENCE, null, new BaseCallBack(mContext)
        {
            @Override
            public void onResponse(String response, int id)
            {
                LogUtils.d(response);
                ConvenienceResp resp = new Gson().fromJson(response, ConvenienceResp.class);
                if (handleResponseCode(resp))
                {
                    mAdapter.setNewData(resp.getInfo());
                }
            }
        });

    }

    private void initRecycles()
    {
        mRecycle.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mAdapter = new ConvenienceAdapter(R.layout.item_convenience, null);
        mAdapter.setImageLoader(getImageLoader());
        mRecycle.setAdapter(mAdapter);
        mRecycle.addItemDecoration(new SpaceItemDecoration(15));
    }

    @Override
    protected int getLayoutRes()
    {
        return R.layout.activity_convenience_service;
    }

}
