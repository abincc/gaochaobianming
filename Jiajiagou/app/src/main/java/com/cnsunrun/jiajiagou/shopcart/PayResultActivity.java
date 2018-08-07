package com.cnsunrun.jiajiagou.shopcart;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseHeaderActivity;
import com.cnsunrun.jiajiagou.common.constant.ArgConstants;
import com.cnsunrun.jiajiagou.personal.order.OrderDetailActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ${LiuDi}
 * on 2017/8/24on 11:07.
 */

public class PayResultActivity extends BaseHeaderActivity
{

    @BindView(R.id.iv_pay_result)
    ImageView mIvPayResult;
    @BindView(R.id.tv_pay_result)
    TextView mTvPayResult;
    @BindView(R.id.tv_pay_money)
    TextView mTvPayMoney;
    private boolean payResult;
    private String order_id;
    private String money;
    private boolean mIsDelivery;

    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight)
    {
        tvTitle.setVisibility(View.GONE);
    }

    @Override
    protected void init()
    {
        payResult = getIntent().getBooleanExtra(ArgConstants.PAYRESULT, false);
        order_id = getIntent().getStringExtra(ArgConstants.ORDER_ID);
        money = getIntent().getStringExtra(ArgConstants.MONEY);
        mIsDelivery = getIntent().getBooleanExtra("isDelivery", false);
        if (payResult)
        {
            mTvPayResult.setText("支付成功");
            if (mIsDelivery)
                mTvPayResult.setText("下单成功");
            mIvPayResult.setImageResource(R.drawable.pay_icon_succeed_nor);
        } else
        {
            mTvPayResult.setText("支付失败");
            if (mIsDelivery)
                mTvPayResult.setText("下单失败");
            mIvPayResult.setImageResource(R.drawable.pay_icon_fail_nor);
        }
        if (!TextUtils.isEmpty(money))
        {
            mTvPayMoney.setText("支付金额：¥" + money);
            if (mIsDelivery)
                mTvPayMoney.setText("到货应付金额：¥" + money);
        }
    }

    @Override
    protected int getLayoutRes()
    {
        return R.layout.activity_pay_result;
    }


    @OnClick(R.id.btn_confirm)
    public void onViewClicked()
    {
        Intent intent = new Intent(mContext, OrderDetailActivity.class);
        intent.putExtra(ArgConstants.ORDER_ID, order_id);
        intent.putExtra("isDelivery", mIsDelivery);
        startActivity(intent);
        finish();
    }
}
