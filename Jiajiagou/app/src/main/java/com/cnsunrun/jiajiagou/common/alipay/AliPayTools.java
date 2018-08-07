package com.cnsunrun.jiajiagou.common.alipay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;

import java.util.Map;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-09-27 10:48
 */
public class AliPayTools {
    private static final int SDK_PAY_FLAG = 1;
    private static OnRequestListener sOnRequestListener;
    @SuppressLint({"HandlerLeak"})
    private static Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case 1:
                    PayResult payResult = new PayResult((Map)msg.obj);
                    String resultInfo = payResult.getResult();
                    String resultStatus = payResult.getResultStatus();
                    if(TextUtils.equals(resultStatus, "9000")) {
                        AliPayTools.sOnRequestListener.onSuccess(resultStatus);
                    } else {
                        AliPayTools.sOnRequestListener.onError(resultStatus);
                    }
                default:
            }
        }
    };

    public AliPayTools() {
    }

    public static void aliPay(final Activity activity, final String orderInfo,OnRequestListener onRxHttp1){
        sOnRequestListener = onRxHttp1;
        Runnable payRunnable=new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
                Map<String, String> result = alipay.payV2(orderInfo,true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                AliPayTools.mHandler.sendMessage(msg);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public static void aliPay(final Activity activity, String appid, boolean isRsa2, String alipay_rsa_private, AliPayModel aliPayModel,String notify_url ,OnRequestListener onRxHttp1) {
        sOnRequestListener = onRxHttp1;
        Map params = AliPayOrderInfoUtil.buildOrderParamMap(appid, isRsa2, aliPayModel.getOut_trade_no(), aliPayModel.getName(), aliPayModel.getMoney(), aliPayModel.getDetail(),notify_url);
        String orderParam = AliPayOrderInfoUtil.buildOrderParam(params);
        String sign = AliPayOrderInfoUtil.getSign(params, alipay_rsa_private, isRsa2);
        final String orderInfo = orderParam + "&" + sign;
        Runnable payRunnable = new Runnable() {
            public void run() {
                PayTask alipay = new PayTask(activity);
                Map result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());
                Message msg = new Message();
                msg.what = 1;
                msg.obj = result;
                AliPayTools.mHandler.sendMessage(msg);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public interface OnRequestListener {
        void onSuccess(String msg);

        void onError(String msg);
    }

}
