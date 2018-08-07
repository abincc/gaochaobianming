package com.cnsunrun.jiajiagou.personal;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.ConvertUtils;
import com.blankj.utilcode.utils.LogUtils;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseHeaderActivity;
import com.cnsunrun.jiajiagou.common.constant.JjgConstant;
import com.cnsunrun.jiajiagou.common.network.DialogCallBack;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.google.gson.Gson;

import java.util.HashMap;

import butterknife.BindView;

/**
 * Created by ${LiuDi}
 * on 2017/8/25on 10:11.
 */

public class ParkingManagerActivity extends BaseHeaderActivity
{
    @BindView(R.id.recycle)
    RecyclerView mRecycle;
    private ParkingManagerAdapter mAdapter;
    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight)
    {
        tvTitle.setText("车位管理");
    }

    @Override
    protected void init()
    {

        GridLayoutManager manager = new GridLayoutManager(mContext, 2);
        manager.offsetChildrenVertical(ConvertUtils.dp2px(mContext,30));
        mRecycle.setLayoutManager(manager);
        mAdapter = new ParkingManagerAdapter(R.layout.item_parking_manager, null);
        mAdapter.setImageLoader(getImageLoader());
        mAdapter.setEmptyView(View.inflate(mContext, R.layout.layout_empty, null));
        mRecycle.setAdapter(mAdapter);
        getData();

    }

    private void getData()
    {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", JjgConstant.getToken(mContext));
        HttpUtils.get(NetConstant.PARKING, hashMap, new DialogCallBack(mContext)
        {
            @Override
            public void onResponse(String response, int id)
            {
                LogUtils.d(response);
                ParkingResp resp = new Gson().fromJson(response, ParkingResp.class);
                if (handleResponseCode(resp))
                {
                    mAdapter.setNewData(resp.getInfo());
                }
            }
        });

    }

    @Override
    protected int getLayoutRes()
    {
        return R.layout.parking_manager;
    }

}
