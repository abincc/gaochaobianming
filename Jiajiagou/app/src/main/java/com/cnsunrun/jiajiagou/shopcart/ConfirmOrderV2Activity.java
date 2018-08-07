package com.cnsunrun.jiajiagou.shopcart;

import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
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
import com.cnsunrun.jiajiagou.common.util.SPUtils;
import com.cnsunrun.jiajiagou.common.widget.popup.PrePayPoupWindow;
import com.cnsunrun.jiajiagou.login.LoginCancelEvent;
import com.cnsunrun.jiajiagou.personal.ChooseCouponActivity;
import com.cnsunrun.jiajiagou.personal.order.OrderAdapter;
import com.cnsunrun.jiajiagou.personal.setting.address.AddressActivity;
import com.cnsunrun.jiajiagou.personal.setting.address.AddressBean;
import com.cnsunrun.jiajiagou.product.PayEvent;
import com.google.gson.Gson;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ${LiuDi}
 * on 2017/8/23on 11:45.
 */

public class ConfirmOrderV2Activity extends BaseHeaderActivity
{

    @BindView(R.id.rl_1)
    RelativeLayout mRl1;
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
    @BindView(R.id.iv_imv)
    ImageView mIvImv;
    @BindView(R.id.tv_product_name)
    TextView mTvProductName;
    @BindView(R.id.tv_product_price)
    TextView mProductPrice;
    @BindView(R.id.tv_describe)
    TextView mTvDescribe;
    @BindView(R.id.tv_count)
    TextView mTvCount;
    @BindView(R.id.rl_address)
    RelativeLayout mRlAddress;
    @BindView(R.id.tv_loc_empty)
    TextView mTvLocEmpty;
    @BindView(R.id.tv_coupon)
    TextView mTvCoupon;
    private PrePayPoupWindow mPayPoupWindow;
    private OrderAdapter mAdapter;
    private String mSku_id;
    private String mNum;
    private String mAddress_id;
    private TwoButtonDialog mDialog;
    private String mProductId;
    private CreateOrderResp.InfoBean mInfo;
    private PrePayBean mBean;
    private int mCard_num;
    private String mCard_id;
    private String mMoney_total;
    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight)
    {
        tvTitle.setText("确认订单");
    }

    @Override
    protected void init()
    {

        mSku_id = getIntent().getStringExtra("sku_id");
        mNum = getIntent().getStringExtra("num");
        mProductId = getIntent().getStringExtra(ArgConstants.PRODUCTID);
        getData();

    }

    private void getData()
    {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", JjgConstant.getToken(mContext));
        hashMap.put("sku_id", mSku_id);
        hashMap.put("num", mNum);
        HttpUtils.get(NetConstant.BUY_NOW, hashMap, new DialogCallBack(mContext)
        {
            @Override
            public void onResponse(String response, int id)
            {
                LogUtils.d(response);

                OrderV2Resp orderResp = new Gson().fromJson(response, OrderV2Resp.class);
                if (handleResponseCode(orderResp))
                {
                    OrderV2Resp.InfoBean info = orderResp.getInfo();
                    if (info != null)
                    {
//                        mAdapter.setNewData(info.getCart_list());
                        mTvFreight.setText("¥ " + info.getFreight() + "元");
                        mTvTotalCount.setText("共计" + info.getNum_total() + "件商品 小计 :");
                        mTvTotalPrice.setText("¥ " + info.getSubtotal() + "元");
                        mMoney_total = info.getMoney_total();
                        mTvPrice.setText("¥ " + OtherUtils.FormatPrice(Double.parseDouble(mMoney_total)) + "元");


                        mCard_num = info.card_num;
                        if (mCard_num > 0)
                        {
                            mTvCoupon.setText("选择代金券");
                            mTvCoupon.setTextColor(0xff007ee5);
                        }

                        OrderV2Resp.InfoBean.ProductInfoBean product_info = info.getProduct_info();
                        setProductInfo(product_info);

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
     * @param
     */

    private void setProductInfo(OrderV2Resp.InfoBean.ProductInfoBean product_info)
    {
        mTvProductName.setText(product_info.getProduct_title());
        mTvDescribe.setText(product_info.getSpec_value());
        mProductPrice.setText("¥" + product_info.getPrice());
        mTvCount.setText("x" + product_info.getProduct_num());
        getImageLoader().load(product_info.getImage()).into(mIvImv);
    }


    private void setAddress(AddressBean address)
    {
        mTvName.setText(address.name);
        mTvLoc.setText(address.address_detail);
        mTvMobile.setText(address.mobile);
    }

    @Override
    protected int getLayoutRes()
    {
        return R.layout.activity_order_confirm2;
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

                startActivityForResult(new Intent(mContext, ChooseCouponActivity.class).putExtra("money", mMoney_total), 300);
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
                    hashMap.put("sku_id", mSku_id);
                    hashMap.put("num", mNum);
                    if (!TextUtils.isEmpty(mCard_id))
                    hashMap.put("card_id", mCard_id);

                    HttpUtils.post(NetConstant.BUY_NOW, hashMap, new BaseCallBack(mContext)
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
    public void onRefresh()
    {
        super.onRefresh();
        finish();
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
                mAddress_id = addressBean.address_id;
                mTvLocEmpty.setVisibility(View.GONE);
                mRlAddress.setVisibility(View.VISIBLE);
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
            mCard_id = "";
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
                ConfirmOrderV2Activity.super.onBackPressedSupport();
            }
        });
        mDialog.show(getSupportFragmentManager(), null);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginCancel(LoginCancelEvent event)
    {
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPayResultEvent(PayEvent event)
    {
        finish();
    }
}
