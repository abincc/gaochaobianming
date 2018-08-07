package com.cnsunrun.jiajiagou.personal.order;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
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
import com.cnsunrun.jiajiagou.common.util.RecyclerLineDecoration;
import com.cnsunrun.jiajiagou.common.widget.popup.PrePayPoupWindow;
import com.cnsunrun.jiajiagou.personal.setting.address.AddressBean;
import com.cnsunrun.jiajiagou.product.ProductDetailActivity;
import com.cnsunrun.jiajiagou.shopcart.PrePayBean;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ${LiuDi}
 * on 2017/8/25on 14:36.
 */

public class OrderDetailActivity extends BaseHeaderActivity
{
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_loc)
    TextView mTvLoc;
    @BindView(R.id.recycle)
    RecyclerView mRecycle;
    @BindView(R.id.tv_mobile)
    TextView mTvMobile;
    @BindView(R.id.tv_total_price)
    TextView mTvTotalPrice;
    @BindView(R.id.tv_order_no)
    TextView mTvOrderNo;
    @BindView(R.id.tv_state)
    TextView mTvState;
    @BindView(R.id.tv_ship_time)
    TextView mTvShipTime;
    @BindView(R.id.tv_confirm_time)
    TextView mTvConfirmTime;
    @BindView(R.id.tv_confirm_receipt)
    TextView mTvRight;
    @BindView(R.id.tv_reimburse)
    TextView mTvLeft;
    @BindView(R.id.tv_create_time)
    TextView mTvCreateTime;
    @BindView(R.id.tv_pay_time)
    TextView mTvPayTime;
    @BindView(R.id.rl_1)
    RelativeLayout mRl1;
    @BindView(R.id.ll_container)
    LinearLayout mLlContainer;
    private OrderAdapter mAdapter;
    private String order_id;
    private int mStatus;
    private boolean isEvaluate;
    private OrderDetailResp.InfoBean mInfo;
    private PrePayPoupWindow mPayPoupWindow;
    private boolean mIsDelivery;

    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight)
    {
        tvTitle.setText("订单详情");
//        ivBack.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                onBackPressed();
//                EventBus.getDefault().post(new FreshEvent());
//
//            }
//        });
    }

    @Override
    protected void init()
    {
        order_id = getIntent().getStringExtra(ArgConstants.ORDER_ID);
        isEvaluate = getIntent().getBooleanExtra(ArgConstants.IS_EVALUATE, false);
        mIsDelivery = getIntent().getBooleanExtra("isDelivery", false);
        initRecycles();
        getData();

    }

    private void getData()
    {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", JjgConstant.getToken(mContext));
        hashMap.put("order_id", order_id);
        HttpUtils.get(NetConstant.ORDER_INFO, hashMap, new DialogCallBack(mContext)
        {
            @Override
            public void onResponse(String response, int id)
            {
                LogUtils.d(response);
                OrderDetailResp resp = new Gson().fromJson(response, OrderDetailResp.class);
                if (handleResponseCode(resp))
                {
                    mInfo = resp.getInfo();
                    if (mInfo != null)
                    {
                        mAdapter.setNewData(mInfo.getProduct_info());
                        AddressBean address = mInfo.getAddress();
                        mTvOrderNo.setText("订单编号: " + mInfo.getOrder_no());
                        mTvState.setText(mInfo.getStatus_title());
                        mTvTotalPrice.setText("总金额：¥ " + mInfo.getMoney_total() + "元");

                        mTvCreateTime.setText("下单时间：" + mInfo.getAdd_time());

                        if (TextUtils.isEmpty(mInfo.getPay_time()))
                        {
                            mTvPayTime.setVisibility(View.GONE);
                        } else
                        {
                            mTvPayTime.setText("支付时间：" + mInfo.getPay_time());
                        }
                        if (TextUtils.isEmpty(mInfo.getShip_time()))
                        {
                            mTvShipTime.setVisibility(View.GONE);
                        } else
                        {
                            mTvShipTime.setText("发货时间：" + mInfo.getShip_time());
                        }
                        if (TextUtils.isEmpty(mInfo.getConfirm_time()))
                        {
                            mTvConfirmTime.setVisibility(View.GONE);
                        } else
                        {
                            mTvConfirmTime.setText(mInfo.getConfirm_time());
                        }

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
        mTvLeft.setVisibility(status == 10 ? View.VISIBLE : View.GONE);
        mTvRight.setVisibility((status == 10 || status == 20 || status == 30 || status == 40) ? View.VISIBLE : View
                .GONE);
        mRl1.setVisibility((status != 5 && status != 50 && status != 60 && status != 70) ? View.VISIBLE : View.GONE);

//        5-已取消 10-待支付  20-已付款（待系统确认、待收货）30-待收货（系统已确认）
// 40-（用户确认收货）待评价 50-已完成  60-申请退款中 70-已退款
        switch (status)
        {

            case 10:
                mTvLeft.setText("取消订单");
                mTvRight.setText("去支付");
                break;
            case 20:
                mTvRight.setText("申请退款");
                break;
            case 30:
                mTvRight.setText("确认收货");
                break;
            case 40:
                mTvRight.setText("去评价");
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
        mRecycle.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mAdapter = new OrderAdapter(R.layout.layout_product_item, null);
        mAdapter.setImageLoader(getImageLoader());
        mAdapter.setIsEvaluate(isEvaluate);
        mRecycle.setAdapter(mAdapter);
        mRecycle.setNestedScrollingEnabled(false);
        mRecycle.addItemDecoration(new RecyclerLineDecoration(mContext, 0xffffffff));
        mRecycle.addOnItemTouchListener(new OnItemClickListener()
        {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position)
            {
                List<OrderBean> data = adapter.getData();
                Intent intent = new Intent(mContext, ProductDetailActivity.class);
                intent.putExtra(ArgConstants.PRODUCTID, data.get(position).getProduct_id());
                startActivity(intent);

            }
        });
        mRecycle.addOnItemTouchListener(new OnItemChildClickListener()
        {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position)
            {
                List<OrderBean> data = adapter.getData();
                String order_detail_id = data.get(position).getOrder_detail_id();
                int comment_status = data.get(position).getComment_status();
                if (comment_status == 0)
                {
                    Intent intent = new Intent(mContext, ProductReviewsActivity.class);
                    intent.putExtra(ArgConstants.ORDER_ID, order_detail_id);
                    intent.putExtra("bean", data.get(position));
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    protected int getLayoutRes()
    {
        return R.layout.activity_order_detail;
    }


    @OnClick({R.id.tv_confirm_receipt, R.id.tv_reimburse})
    public void onViewClicked(View view)
    {
        final HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", JjgConstant.getToken(mContext));
        hashMap.put("order_id", order_id);

        final TwoButtonDialog dialog = new TwoButtonDialog();
        switch (view.getId())
        {
            case R.id.tv_confirm_receipt:
                //right
                switch (mStatus)
                {
                    case 10:
                        //去支付
                        PrePayBean payBean = new PrePayBean(mInfo.getMoney_total(), mInfo
                                .getOrder_id(), mInfo
                                .getOrder_no(), null);
                        if (mPayPoupWindow == null)
                            mPayPoupWindow = new PrePayPoupWindow(mContext, null);

                        mPayPoupWindow.setInfo(payBean);
                        mPayPoupWindow.showAtLocation(mLlContainer, Gravity.BOTTOM, 0, 0);
                        break;
                    case 20:
                        //申请退款
                        dialog.setContent("确定要申请退款吗？");
                        dialog.setOnBtnConfirmClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                dialog.dismiss();
                                HttpUtils.post(NetConstant.APPLY_RETURN, hashMap, new BaseCallBack(mContext)
                                {
                                    @Override
                                    public void onResponse(String response, int id)
                                    {
                                        LogUtils.d(response);
                                        BaseResp baseResp = new Gson().fromJson(response, BaseResp.class);
                                        if (handleResponseCode(baseResp))
                                        {
                                            showToast(baseResp.getMsg(), 1);
                                            if (mIsDelivery)
                                            {
                                                getData();
                                            } else
                                            {
                                                setResult(200);
                                                finish();
                                            }


                                        }
                                    }
                                });
                            }
                        });
                        dialog.show(getSupportFragmentManager(), null);
                        break;
                    case 30:
                        //确认收货
                        dialog.setContent("确定要收货吗？");
                        dialog.setOnBtnConfirmClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                dialog.dismiss();
                                HttpUtils.post(NetConstant.CONFIRM_RECEIPT, hashMap, new BaseCallBack(mContext)
                                {
                                    @Override
                                    public void onResponse(String response, int id)
                                    {
                                        LogUtils.d(response);
                                        BaseResp baseResp = new Gson().fromJson(response, BaseResp.class);
                                        if (handleResponseCode(baseResp))
                                        {
                                            showToast(baseResp.getMsg(), 1);
                                            setResult(200);
                                            finish();
                                        }
                                    }
                                });
                            }
                        });
                        dialog.show(getSupportFragmentManager(), null);
                        break;
                    case 40:
                        //评价
                        Intent intent = new Intent(mContext, ProductReviewsActivity.class);
                        intent.putExtra(ArgConstants.ORDER_ID, order_id);
                        startActivity(intent);
                        break;
                }
                break;
            case R.id.tv_reimburse:
                //取消订单
                dialog.setContent("确定要取消订单吗？");
                dialog.setOnBtnConfirmClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        dialog.dismiss();
                        HttpUtils.post(NetConstant.ORDER_CANCEL, hashMap, new BaseCallBack(mContext)
                        {
                            @Override
                            public void onResponse(String response, int id)
                            {
                                LogUtils.d(response);
                                BaseResp baseResp = new Gson().fromJson(response, BaseResp.class);
                                if (handleResponseCode(baseResp))
                                {
                                    showToast(baseResp.getMsg(), 1);
                                    setResult(200);
                                    finish();
                                }
                            }
                        });
                    }
                });
                dialog.show(getSupportFragmentManager(), null);
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
