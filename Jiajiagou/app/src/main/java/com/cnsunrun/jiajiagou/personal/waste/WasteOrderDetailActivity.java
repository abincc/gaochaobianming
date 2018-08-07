package com.cnsunrun.jiajiagou.personal.waste;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.cnsunrun.jiajiagou.MainActivity;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseCallBack;
import com.cnsunrun.jiajiagou.common.base.BaseHeaderActivity;
import com.cnsunrun.jiajiagou.common.base.BaseResp;
import com.cnsunrun.jiajiagou.common.base.TwoButtonDialog;
import com.cnsunrun.jiajiagou.common.constant.ArgConstants;
import com.cnsunrun.jiajiagou.common.constant.JjgConstant;
import com.cnsunrun.jiajiagou.common.network.DialogCallBack;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.personal.setting.address.AddressBean;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ${LiuDi}
 * on 2017/8/25on 14:36.
 */

public class WasteOrderDetailActivity extends BaseHeaderActivity
{

    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_loc)
    TextView mTvLoc;
    @BindView(R.id.recycle)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_mobile)
    TextView mTvMobile;
    @BindView(R.id.tv_status)
    TextView mTvStatus;
    @BindView(R.id.tv_order_no)
    TextView mTvOrderNo;
    @BindView(R.id.tv_confirm_receipt)
    TextView mTvRight;
    @BindView(R.id.tv_create_time)
    TextView mTvCreateTime;
    @BindView(R.id.tv_wait_time)
    TextView mTvWaitTime;

    @BindView(R.id.rl_1)
    RelativeLayout mRl1;
    @BindView(R.id.ll_container)
    LinearLayout mLlContainer;

    private String order_id;
    private int mStatus;
    private boolean isEvaluate;
    private WasteOrderDetailResp.InfoBean mInfo;
    private boolean mIsDelivery;
    private WasteDetailAdapter mWasteDetailAdapter;
    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight)
    {
        tvTitle.setText("订单详情");
    }

    @Override
    protected int getLayoutRes()
    {
        return R.layout.activity_waste_order_detail;
    }

    @Override
    protected void init()
    {
        order_id = getIntent().getStringExtra(ArgConstants.ORDER_ID);
        isEvaluate = getIntent().getBooleanExtra(ArgConstants.IS_EVALUATE, false);
        mIsDelivery = getIntent().getBooleanExtra("isDelivery", false);
        initRecycles();
        getData();

        /**
         * 读取图片信息
         */
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setNestedScrollingEnabled(false);
        mWasteDetailAdapter = new WasteDetailAdapter(R.layout
                .item_waste_detail);
        mWasteDetailAdapter.setImageLoader(getImageLoader());
        mRecyclerView.setAdapter(mWasteDetailAdapter);

        HashMap<String, String> map = new HashMap<>();
        map.put("token", JjgConstant.getToken(mContext));
        map.put("id", order_id);
        HttpUtils.get(NetConstant.WASTE_IMAGE_INFO, map, new BaseCallBack(mContext) {
            @Override
            public void onResponse(String response, int id) {
                WasteDetailBean bean= new Gson().fromJson(response, WasteDetailBean.class);
                if (handleResponseCode(bean)) {
                    List<WasteDetailBean.InfoBean> a = bean.getInfo();
                    mWasteDetailAdapter.setNewData(a);
                }
            }
        });

    }

    /**
     * 读取订单信息
     */
    private void getData()
    {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", JjgConstant.getToken(mContext));
        hashMap.put("id", order_id);
        HttpUtils.get(NetConstant.WASTE_ORDER_INFO, hashMap, new DialogCallBack(mContext)
        {
            @Override
            public void onResponse(String response, int id)
            {
                LogUtils.d(response);
                WasteOrderDetailResp resp = new Gson().fromJson(response, WasteOrderDetailResp.class);
                if (handleResponseCode(resp))
                {
                    mInfo = resp.getInfo();
                    if (mInfo != null)
                    {
                        mTvOrderNo.setText("订单编号: " + mInfo.getOrder_no());
                        mTvStatus.setText(mInfo.getStatus_title());

                        mTvCreateTime.setText("下单时间：" + mInfo.getAdd_time());

                        if (TextUtils.isEmpty(mInfo.getDate()))
                        {
                            mTvWaitTime.setVisibility(View.GONE);
                        } else
                        {
                            mTvWaitTime.setText("预约时间：" + mInfo.getDate());
                        }

                        AddressBean address = mInfo.getAddress();

                        setAddress(address);
                        mStatus = mInfo.getStatus();
                        setStatus(mStatus);

                    }
                }
            }
        });


    }

    private void setStatus(int status)
    {
        mTvRight.setVisibility((status == 10) ? View.VISIBLE : View
                .GONE);
        mRl1.setVisibility((status == 10) ? View.VISIBLE : View.GONE);

        switch (status)
        {
            case 10:
                mTvRight.setText("取消订单");
                break;
        }

    }

    private void setAddress(AddressBean address)
    {
        mTvName.setText(address.name);
        mTvLoc.setText(address.address_detail);
        mTvMobile.setText(address.mobile);

    }

    private void initRecycles()
    {

    }


    @OnClick({R.id.tv_confirm_receipt})
    public void onViewClicked(View view)
    {
        final HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", JjgConstant.getToken(mContext));
        hashMap.put("id", order_id);

        final TwoButtonDialog dialog = new TwoButtonDialog();
        switch (view.getId())
        {
            case R.id.tv_confirm_receipt:
                switch (mStatus)
                {
                    case 10:
                        dialog.setContent("确定取消订单吗？");
                        dialog.setOnBtnConfirmClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                dialog.dismiss();
                                HttpUtils.post(NetConstant.WASTE_CANCAL_ORDER, hashMap, new BaseCallBack(mContext)
                                {
                                    @Override
                                    public void onResponse(String response, int id)
                                    {
                                        LogUtils.d(response);
                                        BaseResp baseResp = new Gson().fromJson(response, BaseResp.class);
                                        if (handleResponseCode(baseResp))
                                        {
                                            showToast(baseResp.getMsg(), 1);
                                            onRefresh();
                                            mTvRight.setVisibility( View.GONE);
                                        }
                                    }
                                });
                            }
                        });
                        dialog.show(getSupportFragmentManager(), null);
                        break;
                }
                break;
        }
    }

    @Override
    public void onBackPressedSupport()
    {
        if (mIsDelivery)
        {
            startActivity(new Intent(mContext, MainActivity.class));
        } else
        {
            super.onBackPressedSupport();
        }
    }
}
