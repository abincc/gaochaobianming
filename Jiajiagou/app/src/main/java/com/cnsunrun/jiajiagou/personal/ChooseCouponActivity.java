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
import com.cnsunrun.jiajiagou.common.constant.JjgConstant;
import com.cnsunrun.jiajiagou.common.network.DialogCallBack;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * Created by ${LiuDi}
 * on 2017/11/3on 17:23.
 */

public class ChooseCouponActivity extends BaseHeaderActivity
{
    @BindView(R.id.recycle)
    RecyclerView mRecycle;
    private CouponAdapter mAdapter;
    private String mMoney;

    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight)
    {
        tvTitle.setText("选择优惠券");
        tvRight.setText("不使用");
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setResult(400);
                finish();
            }
        });
    }

    @Override
    protected void init()
    {
        mMoney = getIntent().getStringExtra("money");
        mRecycle.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mAdapter = new CouponAdapter(R.layout.item_coupon, null);
        mRecycle.setAdapter(mAdapter);
        mRecycle.addOnItemTouchListener(new OnItemClickListener()
        {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position)
            {
                List<CouponResp.InfoBean> data = adapter.getData();
                if (Float.valueOf(mMoney) < Float.valueOf(data.get(position).minimum))
                {
                    showToast(data.get(position).minimum_title);
                    return;
                }

                String card_id = data.get(position).card_id;
                Intent intent = new Intent();
                intent.putExtra("card_id", card_id);
                intent.putExtra("amount", data.get(position).amount);
                setResult(300, intent);
                finish();
            }
        });

        getData();
    }

    @Override
    protected int getLayoutRes()
    {
        return R.layout.activity_choose_coupon;
    }

    private void getData()
    {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", JjgConstant.getToken(mContext));
        hashMap.put("status", "1");
        HttpUtils.get(NetConstant.COUPON, hashMap, new DialogCallBack(mContext)
        {
            @Override
            public void onResponse(String response, int id)
            {
                LogUtils.d(response);
                CouponResp resp = new Gson().fromJson(response, CouponResp.class);
                if (handleResponseCode(resp))
                {
                    mAdapter.setNewData(resp.getInfo());
                }
            }
        });
    }

}
