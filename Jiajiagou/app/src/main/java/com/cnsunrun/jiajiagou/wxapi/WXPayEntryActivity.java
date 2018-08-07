package com.cnsunrun.jiajiagou.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.cnsunrun.jiajiagou.common.constant.ArgConstants;
import com.cnsunrun.jiajiagou.common.util.LogUtils;
import com.cnsunrun.jiajiagou.common.util.ToastTask;
import com.cnsunrun.jiajiagou.common.wxpay.WxConstant;
import com.cnsunrun.jiajiagou.common.wxpay.WxPayUtils;
import com.cnsunrun.jiajiagou.product.PayEvent;
import com.cnsunrun.jiajiagou.shopcart.PayResultActivity;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler
{
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, WxConstant.WX_APPID);
        LogUtils.printI("wx appid "+WxConstant.WX_APPID);
        api.handleIntent(getIntent(), this);
    }


    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req)
    {
    }

    @Override
    public void onResp(BaseResp resp)
    {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX)
        {
            Intent intent = new Intent(WXPayEntryActivity.this, PayResultActivity.class);
            switch (resp.errCode)
            {
                case 0:
//                    EventBus.getDefault().post(new WxPaySuccessEvent(WxPayUtils.getInstance().sn));
//                    notifyServer();
                    ToastTask.getInstance(WXPayEntryActivity.this).setMessage("支付成功").success();

                    intent.putExtra(ArgConstants.ORDER_ID, WxPayUtils.getInstance().order_id);
                    intent.putExtra(ArgConstants.MONEY, WxPayUtils.getInstance().money);
                    intent.putExtra(ArgConstants.PAYRESULT, true);
                    startActivity(intent);
                    finish();

                    EventBus.getDefault().post(new PayEvent());
                    break;
                case -1:
                    ToastTask.getInstance(WXPayEntryActivity.this).setMessage("支付失败").error();
                    intent.putExtra(ArgConstants.ORDER_ID, WxPayUtils.getInstance().order_id);
                    intent.putExtra(ArgConstants.MONEY, WxPayUtils.getInstance().money);
                    intent.putExtra(ArgConstants.PAYRESULT, false);
                    startActivity(intent);
                    finish();
                    EventBus.getDefault().post(new PayEvent());
                    break;
                case -2:
                    ToastTask.getInstance(WXPayEntryActivity.this).setMessage("支付取消").error();
                    finish();
                    break;
            }

        }
    }
//
//    private void notifyServer()
//    {
//        //通知后台
//        String sn = JsonUtils.getInstance().put("sn", WxPayUtils.getInstance().sn).getEncodeString();
//
//        HttpUtils.postBiz(NetConstant.PAY_NOTIFY_SERVER, sn, new DialogCallBack(WXPayEntryActivity.this)
//        {
//            @Override
//            public void onResponse(String response, int id)
//            {
//                LogUtils.printD(response);
//                com.jbyxy.app.student.common.base.BaseResp baseResp = new Gson().fromJson(response, com.jbyxy.app
//                        .student.common.base.BaseResp.class);
//                if (handleResponseCode(baseResp))
//                {
//                    finish();
//                }
//            }
//        });
//    }
}
