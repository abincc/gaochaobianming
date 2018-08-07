package com.cnsunrun.jiajiagou.personal.waste;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
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
import com.cnsunrun.jiajiagou.login.LoginActivity;
import com.google.gson.Gson;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

import static com.cnsunrun.jiajiagou.R.id.tv_right;

/**
 * Created by ${LiuDi}
 * on 2017/8/25on 10:11.
 */

public class WasteMenuActivity extends BaseHeaderActivity
{
    @BindView(R.id.recycle)
    RecyclerView mRecycle;
    @BindView(R.id.ll1)
    View boder;
    private WasteMenuAdapter mAdapter;

    private Button btn;

    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight)
    {
        tvTitle.setText("变废为宝");
        tvRight.setText("我的订单");
        tvRight.setVisibility(View.VISIBLE);
    }

    @Override
    protected void init()
    {

        GridLayoutManager manager = new GridLayoutManager(mContext, 2);
        manager.offsetChildrenVertical(ConvertUtils.dp2px(mContext,30));
        mRecycle.setLayoutManager(manager);
        mAdapter = new WasteMenuAdapter(R.layout.activity_waste_item, null);
        mAdapter.setImageLoader(getImageLoader());
        View inflate = View.inflate(mContext, R.layout.layout_empty, null);
        TextView tvEmpty = (TextView) inflate.findViewById(R.id.tv_empty);
        tvEmpty.setText("暂无类别");
        mAdapter.setEmptyView(inflate);
        mRecycle.setAdapter(mAdapter);
        getData();

        /**
         * 下单按钮
         */
        btn = (Button) findViewById(R.id.tv_waste_order);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (JjgConstant.isLogin(mContext)) {
                    startActivity(new Intent(mContext, WasteServiceActivity.class));
                } else {
                    startActivity(new Intent(mContext, LoginActivity.class));
                }
            }
        });
    }

    @OnClick({R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case tv_right:
                if (JjgConstant.isLogin(mContext)) {
                    startActivity(new Intent(mContext, MyWasteOrderActivity.class));
                } else {
                    startActivity(new Intent(mContext, LoginActivity.class));
                }
                break;
        }
    }


    private void getData()
    {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", JjgConstant.getToken(mContext));
        HttpUtils.get(NetConstant.WASTE_LIST, hashMap, new DialogCallBack(mContext)
        {
            @Override
            public void onResponse(String response, int id)
            {
                LogUtils.d(response);
                WasteResp resp = new Gson().fromJson(response, WasteResp.class);
                if (handleResponseCode(resp))
                {
                    mAdapter.setNewData(resp.getInfo());
                    if(resp.getInfo().size() < 1){
                        btn.setVisibility(View.GONE);
                        boder.setVisibility(View.GONE);
                    }
                }
            }
        });

    }

    @Override
    protected int getLayoutRes()
    {
        return R.layout.activity_waste_menu;
    }

}
