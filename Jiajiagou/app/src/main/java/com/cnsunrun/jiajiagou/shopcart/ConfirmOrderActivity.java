package com.cnsunrun.jiajiagou.shopcart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseCallBack;
import com.cnsunrun.jiajiagou.common.base.BaseHeaderActivity;
import com.cnsunrun.jiajiagou.common.base.TwoButtonDialog;
import com.cnsunrun.jiajiagou.common.constant.ArgConstants;
import com.cnsunrun.jiajiagou.common.constant.JjgConstant;
import com.cnsunrun.jiajiagou.common.network.DialogCallBack;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.OtherUtils;
import com.cnsunrun.jiajiagou.common.util.RecyclerLineDecoration;
import com.cnsunrun.jiajiagou.common.util.SPUtils;
import com.cnsunrun.jiajiagou.common.widget.popup.PrePayPoupWindow;
import com.cnsunrun.jiajiagou.personal.ChooseCouponActivity;
import com.cnsunrun.jiajiagou.personal.order.OrderAdapter;
import com.cnsunrun.jiajiagou.personal.order.OrderBean;
import com.cnsunrun.jiajiagou.personal.setting.address.AddressActivity;
import com.cnsunrun.jiajiagou.personal.setting.address.AddressBean;
import com.cnsunrun.jiajiagou.product.FreshEvent;
import com.cnsunrun.jiajiagou.product.PayEvent;
import com.cnsunrun.jiajiagou.product.ProductDetailActivity;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ${LiuDi}
 * on 2017/8/23on 11:45.
 */

public class ConfirmOrderActivity extends BaseHeaderActivity
{

    @BindView(R.id.rl_1)
    RelativeLayout mRl1;
    @BindView(R.id.recycle)
    RecyclerView mRecycle;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_loc)
    TextView mTvLoc;
    @BindView(R.id.tv_mobile)
    TextView mTvMobile;
    @BindView(R.id.tv_freight)
    TextView mTvFreight;
    @BindView(R.id.tv_total_count)
    TextView mTvTotalCount;
    @BindView(R.id.tv_total_price)
    TextView mTvTotalPrice;
    @BindView(R.id.tv_price)
    TextView mTvPrice;
    @BindView(R.id.rl_address)
    RelativeLayout mRlAddress;
    @BindView(R.id.tv_loc_empty)
    TextView mTvLocEmpty;
    @BindView(R.id.tv_coupon)
    TextView mTvCoupon;
    private PrePayPoupWindow mPayPoupWindow;
    private OrderAdapter mAdapter;
    private String[] mCart_ids;
    private String mAddress_id;
    private TwoButtonDialog mDialog;
    private CreateOrderResp.InfoBean mInfo;
    private PrePayBean mBean;
    private String mCard_id;
    private int mCard_num;
    private String mMoney_total;

    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight)
    {
        tvTitle.setText("确认订单");
    }

    @Override
    protected void init()
    {

        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            mCart_ids = extras.getStringArray(ArgConstants.CART_ID);
            getData();
        }
        initRecycles();
    }

    private void getData()
    {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", JjgConstant.getToken(mContext));
        for (int i = 0; i < mCart_ids.length; i++)
        {
            hashMap.put("cart_ids" + "[" + i + "]", mCart_ids[i]);
        }
        HttpUtils.get(NetConstant.ORDER_CONFIRM, hashMap, new DialogCallBack(mContext)
        {
            @Override
            public void onResponse(String response, int id)
            {
                LogUtils.d(response);
                OrderResp orderResp = new Gson().fromJson(response, OrderResp.class);
                if (handleResponseCode(orderResp))
                {
                    OrderResp.InfoBean info = orderResp.getInfo();
                    if (info != null)
                    {
                        mAdapter.setNewData(info.getCart_list());
                        mTvFreight.setText("¥ " + info.getFreight() + ".00元");
                        mTvTotalCount.setText("共计" + info.getNum_total() + "件商品 小计: ");
                        mTvTotalPrice.setText(" ¥ " + OtherUtils.FormatPrice(Double.parseDouble(info.getSubtotal()))
                                + "元");
                        mMoney_total = info.getMoney_total();
                        mTvPrice.setText("¥ " + OtherUtils.FormatPrice(Double.parseDouble(mMoney_total)) + "元");

                        mCard_num = info.card_num;
                        if (mCard_num > 0)
                        {
                            mTvCoupon.setText("选择代金券");
                            mTvCoupon.setTextColor(0xff007ee5);
                        }

                        AddressBean address = info.getAddress();
                        if (address == null)
                        {
                            mTvLocEmpty.setVisibility(View.VISIBLE);
                            mRlAddress.setVisibility(View.INVISIBLE);
                        } else
                        {
                            mAddress_id = address.address_id;
                            setAddress(address);
                        }
                    }
                }
            }
        });
    }

    /**
     * 设置地址
     *
     * @param address
     */
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
        mRecycle.setAdapter(mAdapter);
        mRecycle.setNestedScrollingEnabled(false);
        mRecycle.addItemDecoration(new RecyclerLineDecoration(mContext));
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
        mAdapter.setWhiteBackGround();
    }

    @Override
    protected int getLayoutRes()
    {
        return R.layout.activity_order_confirm;
    }

    @OnClick({R.id.btn_confirm, R.id.fl_address, R.id.rl_coupon})
    public void onViewClicked(View view)
    {
        switch (view.getId())
        {
            case R.id.rl_coupon:
                if (mCard_num == 0)
                {
                    showToast("沒有可使用的代金券！");
                    return;
                }

                startActivityForResult(new Intent(mContext, ChooseCouponActivity.class).putExtra("money",
                        mMoney_total), 300);
                break;

            case R.id.btn_confirm:
                if (mAddress_id == null)
                {
                    showToast("请选择收货地址");
                    return;
                }
                if (mInfo == null)
                {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("token", JjgConstant.getToken(mContext));
                    hashMap.put("address_id", mAddress_id);
                    if (!TextUtils.isEmpty(mCard_id))
                        hashMap.put("card_id", mCard_id);
                    for (int i = 0; i < mCart_ids.length; i++)
                    {
                        hashMap.put("cart_ids" + "[" + i + "]", mCart_ids[i]);
                    }


                    HttpUtils.post(NetConstant.CREATE_ORDER, hashMap, new BaseCallBack(mContext)
                    {
                        @Override
                        public void onResponse(String response, int id)
                        {
                            LogUtils.d(response);
                            CreateOrderResp resp = new Gson().fromJson(response, CreateOrderResp.class);
                            if (handleResponseCode(resp))
                            {
                                showToast(resp.getMsg(), 1);
                                mInfo = resp.getInfo();
                                SPUtils.put(mContext, "aliUrl", mInfo.getCallback_url_ali());
                                SPUtils.put(mContext, "wxUrl", mInfo.getCallback_url_wx());
                                mBean = new PrePayBean(mInfo.getMoney(), mInfo.getOrder_id(), mInfo
                                        .getOrder_no(), mInfo.getDesc());
                                if (mPayPoupWindow == null)
                                {
                                    mPayPoupWindow = new PrePayPoupWindow(mContext, mBean);
                                }
                                mPayPoupWindow.showAtLocation(mRl1, Gravity.BOTTOM, 0, 0);
                            }

                        }
                    });
                } else
                {
                    if (mPayPoupWindow == null)
                    {
                        mPayPoupWindow = new PrePayPoupWindow(mContext, mBean);
                    }
                    mPayPoupWindow.showAtLocation(mRl1, Gravity.BOTTOM, 0, 0);
                }
                break;
            case R.id.fl_address:
                Intent intent = new Intent(mContext, AddressActivity.class);
                intent.putExtra(ArgConstants.IS_FROM_ORDER, true);
                startActivityForResult(intent, 200);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200)
        {
            if (data != null)
            {
                AddressBean addressBean = data.getParcelableExtra(ArgConstants.ADDRESSBEAN);
                mTvLocEmpty.setVisibility(View.GONE);
                mRlAddress.setVisibility(View.VISIBLE);
                mAddress_id = addressBean.address_id;
                setAddress(addressBean);
            }
        }
        if (resultCode == 300)
        {
            if (data != null)
            {
                mCard_id = data.getStringExtra("card_id");
                String amount = data.getStringExtra("amount");
                if (!TextUtils.isEmpty(mCard_id))
                {
                    mTvCoupon.setText("￥" + amount + "元");
                    mTvCoupon.setTextColor(0xffff4621);
//                mTvPrice.setText("¥ " + OtherUtils.FormatPrice(Double.parseDouble(info.getMoney_total())) + "元");
                    BigDecimal bigDecimal = new BigDecimal(mMoney_total);
                    BigDecimal bigDecimal1 = bigDecimal.subtract(new BigDecimal(amount));
                    mTvPrice.setText("¥ " + OtherUtils.FormatPrice(bigDecimal1.doubleValue()) + "元");
                }
            }
        }
        if (resultCode == 400)
        {
            mCard_id = null;
            mTvCoupon.setText("选择代金券");
            mTvCoupon.setTextColor(0xff007ee5);
            mTvPrice.setText("¥ " + OtherUtils.FormatPrice(Double.parseDouble(mMoney_total)) + "元");
        }
    }

    @Override
    public void onBackPressedSupport()
    {
        if (mDialog == null)
            mDialog = new TwoButtonDialog();
        mDialog.setContent("您确定要退出支付吗？");
        mDialog.setOnBtnConfirmClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mDialog.dismiss();
                EventBus.getDefault().post(new FreshEvent());
                ConfirmOrderActivity.super.onBackPressedSupport();
            }
        });
        mDialog.show(getSupportFragmentManager(), null);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPayResultEvent(PayEvent event)
    {
        finish();
    }
}
