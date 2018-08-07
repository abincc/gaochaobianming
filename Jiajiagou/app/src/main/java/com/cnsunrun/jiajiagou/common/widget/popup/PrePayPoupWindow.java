package com.cnsunrun.jiajiagou.common.widget.popup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.alipay.AliPayTools;
import com.cnsunrun.jiajiagou.common.alipay.OrderSignBean;
import com.cnsunrun.jiajiagou.common.base.BaseCallBack;
import com.cnsunrun.jiajiagou.common.base.BaseResp;
import com.cnsunrun.jiajiagou.common.constant.ArgConstants;
import com.cnsunrun.jiajiagou.common.constant.JjgConstant;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.SPUtils;
import com.cnsunrun.jiajiagou.common.wxpay.PrePayResp;
import com.cnsunrun.jiajiagou.common.wxpay.WxPayUtils;
import com.cnsunrun.jiajiagou.product.PayEvent;
import com.cnsunrun.jiajiagou.shopcart.PayResultActivity;
import com.cnsunrun.jiajiagou.shopcart.PrePayBean;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ${LiuDi}
 * on 2017/8/23on 12:05.
 */

public class PrePayPoupWindow extends PopupWindow implements AliPayTools.OnRequestListener,
        RadioGroup.OnCheckedChangeListener
{
    Context mContext;
    @BindView(R.id.rb_ali_pay)
    RadioButton mRbAliPay;
    @BindView(R.id.rb_wei_pay)
    RadioButton mRbWeiPay;
    @BindView(R.id.rb_delivery_pay)
    RadioButton mRbDeliveryPay;
    @BindView(R.id.rg_pay)
    RadioGroup mRgPay;
    PrePayBean info;
    @BindView(R.id.tv_order_no)
    TextView mTvOrderNo;
    @BindView(R.id.tv_price)
    TextView mTvPrice;
    private static int PAYMENT_METHOD = 0;//支付方式
    private static final int ALI_PAY = 1;//支付宝
    private static final int WECHAT_PAY = 2;//微信
    private static final int DELIVERY_PAY = 3;//货到付款


    /**
     * 支付宝支付业务：入参app_id
     */
    public static final String APPID = "2016042901347956";

    /**
     * 支付宝账户登录授权业务：入参pid值
     */
    public static final String PID = "2088911997056539";
    /**
     * 商户私钥，pkcs8格式
     */
    public static final String RSA2_PRIVATE =
            "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCwgxCojf5EJqICKll8Uq4y2gThVFV7MkVXLylbvNAsbUZdi6X0wNNUUGe5tpTeNzJWCp6JvLpu1rvK4O8gTEDQqIJ7uBvQ1GHmtDSSqxRwFfIAZYypQu/porEyRqVl1IpC3vuKtMVgBUaQ9ZysOhQ6icC2vXEFmy6T436v2jMFug827fWNHYQUHCwt18uscXrRoVXNiWr3UHXg/hLFeZWYThvg57wXJUNEXTDJR6b0610cDI2Pwk959puamPtZx5YR/h2xSUyWEqEsUQnStysh+SS+fH6nsBULpRtWV1kNyL3DIk28qn5WMUY1b2cmmS/IuTSMBdVfI3srma+6SiS7AgMBAAECggEAWoqQc/WKFMlAVx4EEuqSYpFE6ZSJ2Izb1rHhZCZBHljbPEbS1O5IOLqZykBmsmnbzxjZY8vEUfCiGZox7v5OTPnzHKU+12rp4R5UzVsmO6x2G0/7zCMCz3Rdsm1ckBRq64xXuTmq+Osb3GWwfQO5tqFMkjAwlnCYftG1/VxOycNhdhGcwPQ7y6H4+HIuD/StBiARgU/J5UpirbHXCybc/tAxVQ+GzSuU1o24Pf2pt94I6iXA/GKmqYCOeo80u0KUJGAOMUohSS1Te/syUWvShLN83aF6EBrnTZb77340KIqEapzuXl4bI3c0XHpf+LuCYHryx/M8oUVYB7lF6ZOQUQKBgQDg/MzsreHiFef+sXBbvRHtqhwUXaWUF6upbPp69wcWiAICF2Ofnh7RtSsdtj74FA85NFRaqdeo5JZqGoaevojcKxC+raLhlsp6MZAXjzJQHCyJuPraYNCUW3l/ZnossR+gtDBwWKksXJc30+zHu7SG7ijc4fsOFeVv5Q6quolsqQKBgQDI17HJHvMJ/qWzTfkavDdjlaSl9Go9A/8ddofdAXo1nCYdcroM+meHls56GEv3MC6CAyjbZA61U0oYHjIu9Rgm87Yt9F0pu44lZpwxcTtekEQq2z1OJvVBlbxetvwphAyB4e+zxXX3RhA3ALU45qbUu4tvru5Q6WHqvD2+SRVgwwKBgD2AfZg5VCUUbe2unZNGVO9N8A0VKF0aN1/CHZqdin97QGlH8YDBnZUf3CBsIDx6Z0rw5ho+kWhmzENG6wPb+9eLojEtj/fw23qZr9Tw3QUPHpGrUk995aaYdtb/sWgkJwwsFY2wftZUnTWypehXhFraIat3zsQj8isrR14eTdC5AoGBAJKWY2twjGC6HISfH3Z081Razx5VIwk/ln8RmR4kSuc//c22g7afONsMpc0VWLSk1P0Ng0+FWvrbPJQXzfbZ6nPboxctaKXh/2Cq4MtxYqylTSuecsNMyerBeAgFNEy01VAi8IncUDpXR9RtTtel+RQBgkbUfZpOjKoAFGyC+PhzAoGAcMB8fcCNc7lNE1BjJMrNAb5A/JZcyJdpI5v1hvJj1bSn4EP8niPGje0Nz/dKov/F6nSyxFdHykV4Fpc8Hld8dhanJDSvungLyWMkRcir3d3/ecWolAIhjFYAs7st+8bq9G2+8435X30Af6MMQYHWIgu9bVXz2GgcbOWs59vPVWQ=";


    public PrePayPoupWindow(Context context, PrePayBean info)
    {
        this.info = info;
        mContext = context;
        this.setWindowLayoutMode(-1, -1);
        this.setAnimationStyle(R.style.umeng_socialize_shareboard_animation);
        this.setFocusable(true);

        this.setBackgroundDrawable(new BitmapDrawable());
        this.setOutsideTouchable(true);
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.layout_prepay_poup, null);
        setContentView(contentView);
        ButterKnife.bind(this, contentView);
        initView();

    }

    private void initView()
    {
        mRgPay.setOnCheckedChangeListener(this);
        mRbAliPay.setChecked(true);
        PAYMENT_METHOD = ALI_PAY;
        setData();
    }

    @OnClick({R.id.gray_zone, R.id.btn_confirm})
    public void onViewClicked(View view)
    {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", JjgConstant.getToken(mContext));
        hashMap.put("order_id", info.order_id);
        final String aliUrl = SPUtils.getString(mContext,"aliUrl");
        switch (view.getId())
        {
            case R.id.gray_zone:
                dismiss();
                break;
            case R.id.btn_confirm:
                switch (PAYMENT_METHOD)
                {
                    //支付宝
                    case ALI_PAY:

                        HttpUtils.post(NetConstant.ORDER_SIGN, hashMap, new BaseCallBack(mContext) {
                            @Override
                            public void onResponse(String response, int id) {
                                OrderSignBean bean=new Gson().fromJson(response, OrderSignBean.class);
                                final String orderInfo = bean.getInfo();
                                if (handleResponseCode(bean)) {
                                    AliPayTools.aliPay((Activity) mContext,orderInfo,PrePayPoupWindow.this);

//                                    AliPayTools.aliPay((Activity) mContext, APPID, true, bean.getInfo(), new
//                                            AliPayModel(PrePayPoupWindow.this.info.order_no, "0.01", ""
//                                            , PrePayPoupWindow.this.info.desc), aliUrl, PrePayPoupWindow.this);
                                }
                            }
                        });

                        dismiss();
                        break;
                    //微信
                    case WECHAT_PAY:

                        HttpUtils.post(NetConstant.WX_IOS_PAY, hashMap, new BaseCallBack(mContext)
                        {
                            @Override
                            public void onResponse(String response, int id)
                            {
                                LogUtils.d(response);
                                PrePayResp resp = new Gson().fromJson(response, PrePayResp.class);
                                PrePayResp.InfoBean data = resp.getInfo();

                                if (handleResponseCode(resp))
                                {
                                    data.order_id = info.order_id;
                                    data.money = info.money;
                                    WxPayUtils.getInstance().pay(mContext, data);
                                }
                            }
                        });

                        dismiss();
                        break;
                    //货到付款
                    case DELIVERY_PAY:

                        HttpUtils.post(NetConstant.DELIVERY_PAY, hashMap, new BaseCallBack(mContext)
                        {
                            @Override
                            public void onResponse(String response, int id)
                            {
                                BaseResp baseResp = new Gson().fromJson(response, BaseResp.class);
                                if (handleResponseCode(baseResp))
                                {
                                    Intent intent = new Intent(mContext, PayResultActivity.class);
                                    intent.putExtra(ArgConstants.ORDER_ID, info.order_id);
                                    intent.putExtra(ArgConstants.MONEY, info.money);
                                    intent.putExtra(ArgConstants.PAYRESULT, true);
                                    intent.putExtra("isDelivery", true);
                                    mContext.startActivity(intent);
                                }
                            }
                        });
                        dismiss();
                        break;
                }
                break;
        }
    }



    @Override
    public void onSuccess(String var1)
    {
        Intent intent = new Intent(mContext, PayResultActivity.class);
        intent.putExtra(ArgConstants.ORDER_ID, info.order_id);
        intent.putExtra(ArgConstants.MONEY, info.money);
        intent.putExtra(ArgConstants.PAYRESULT, true);
        mContext.startActivity(intent);
        EventBus.getDefault().post(new PayEvent());
    }

    @Override
    public void onError(String var1)
    {
        Intent intent = new Intent(mContext, PayResultActivity.class);
        intent.putExtra(ArgConstants.ORDER_ID, info.order_id);
        intent.putExtra(ArgConstants.MONEY, info.money);
        intent.putExtra(ArgConstants.PAYRESULT, false);
        mContext.startActivity(intent);
        EventBus.getDefault().post(new PayEvent());
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId)
    {
        switch (checkedId)
        {
            case R.id.rb_ali_pay:
                PAYMENT_METHOD = ALI_PAY;
                break;
            case R.id.rb_wei_pay:
                PAYMENT_METHOD = WECHAT_PAY;
                break;
            case R.id.rb_delivery_pay:
                PAYMENT_METHOD = DELIVERY_PAY;
                break;
        }
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y)
    {
        super.showAtLocation(parent, gravity, x, y);
        setData();
    }

    public void setInfo(PrePayBean info)
    {
        this.info = info;
    }

    private void setData()
    {
        if (info != null)
        {
            mTvPrice.setText("¥ " + info.money + "元");
            mTvOrderNo.setText("订单号：" + info.order_no);
        }
    }
}
