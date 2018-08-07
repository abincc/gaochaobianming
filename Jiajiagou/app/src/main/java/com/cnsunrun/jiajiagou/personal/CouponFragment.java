package com.cnsunrun.jiajiagou.personal;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.utils.LogUtils;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseFragment;
import com.cnsunrun.jiajiagou.common.base.BaseResp;
import com.cnsunrun.jiajiagou.common.constant.JjgConstant;
import com.cnsunrun.jiajiagou.common.network.DialogCallBack;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.google.gson.Gson;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ${LiuDi}
 * on 2017/11/3on 15:39.
 */

public class CouponFragment extends BaseFragment
{
    @BindView(R.id.recycle)
    RecyclerView mRecycle;
    @BindView(R.id.et_code)
    EditText mEtCode;
    private int status;
    private CouponAdapter mAdapter;

    @Override
    protected void init()
    {
        status = getArguments().getInt("status");

        mRecycle.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mAdapter = new CouponAdapter(R.layout.item_coupon, null);
        mAdapter.setEmptyView(View.inflate(mContext, R.layout.layout_empty, null));
        mRecycle.setAdapter(mAdapter);
        getData();
    }

    private void getData()
    {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", JjgConstant.getToken(mContext));
        hashMap.put("status", String.valueOf(status));
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

    @Override
    protected int getChildLayoutRes()
    {
        return R.layout.fragment_coupon;
    }


    @OnClick(R.id.btn_confirm)
    public void onViewClicked()
    {
        String code = mEtCode.getText().toString().trim();
        if (TextUtils.isEmpty(code))
        {
            showToast("请输入兑换码");
            return;
        }
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", JjgConstant.getToken(mContext));
        hashMap.put("code", code);
        HttpUtils.post(NetConstant.EXCHANGE, hashMap, new DialogCallBack(mContext)
        {
            @Override
            public void onResponse(String response, int id)
            {
                LogUtils.d(response);
                BaseResp baseResp = new Gson().fromJson(response, BaseResp.class);
                if (handleResponseCode(baseResp))
                {
                    mEtCode.setText("");
                    getData();
                }
            }
        });

    }
}
