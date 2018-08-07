package com.cnsunrun.jiajiagou.common.wxpay;


import android.content.Context;

import com.cnsunrun.jiajiagou.common.JjgApplication;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WxPayUtils
{
    private WxPayUtils()
    {
    }

    public static WxPayUtils getInstance()
    {
        return WxPayUtilsHolder.sInstance;
    }

    private static class WxPayUtilsHolder
    {
        private static final WxPayUtils sInstance = new WxPayUtils();
    }

    //    public String sn;
    public String order_id;
    public String money;
    private static final String signConstant = "appid=APPID&noncestr=NONCESTR&package=Sign=WXPay&partnerid=PARTNERID" +
            "&prepayid=PREPAYID&timestamp=TIMESTAMP&key=cnsunrun87778785asdfqwertdsadfaf";
//private static final String signConstant = "appid=APPID&noncestr=NONCESTR&package=Sign=WXPay&partnerid=PARTNERID" +
//        "&prepayid=PREPAYID&timestamp=TIMESTAMP&key=SIGNKEY";

    public void pay(Context context, PrePayResp.InfoBean data)
    {
        WxConstant.WX_APPID=data.getAppid();
        IWXAPI wxapi = WXAPIFactory.createWXAPI(context, data.getAppid());
        wxapi.registerApp(data.getAppid());
//        sn = data.sn;
        order_id = data.order_id;
        money = data.money;
        PayReq payReq = new PayReq();
        payReq.appId = data.getAppid();
        payReq.partnerId = data.getPartnerid();
        payReq.prepayId = data.getPrepayid();
        payReq.packageValue = "Sign=WXPay";
        payReq.nonceStr = data.getNoncestr();
        payReq.timeStamp = data.getTimestamp();
        String signStr = signConstant.replace("APPID", data.getAppid()).
                replace("NONCESTR", data.getNoncestr()).
                replace("PARTNERID", data.getPartnerid())
                .replace("TIMESTAMP", data.getTimestamp())
                .replace("PREPAYID", data.getPrepayid());
//                .replace("SIGNKEY", data.getSign());

//        payReq.sign = MD5Weixin.MD5Encode(signStr).toUpperCase();
        payReq.sign = data.getSign();
        JjgApplication.sWxapi.sendReq(payReq);
    }
}
