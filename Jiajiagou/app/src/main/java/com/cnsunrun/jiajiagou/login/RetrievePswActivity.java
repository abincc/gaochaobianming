package com.cnsunrun.jiajiagou.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseHeaderActivity;
import com.cnsunrun.jiajiagou.common.network.DialogCallBack;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.ConstantUtils;
import com.cnsunrun.jiajiagou.common.widget.BtnTimerCount;
import com.cnsunrun.jiajiagou.common.widget.view.HighLightEditText;
import com.cnsunrun.jiajiagou.forum.bean.SendCodeBean;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Description:
 * Data：2017/8/19 0019-下午 12:24
 * Blog：http://blog.csdn.net/u013983934
 * Author: CZ
 */
public class RetrievePswActivity extends BaseHeaderActivity {
    @BindView(R.id.et_phone)
    HighLightEditText mEtPhone;
    @BindView(R.id.btn_getcode)
    Button mBtnGetcode;
    @BindView(R.id.et_code)
    HighLightEditText mEtCode;
    private String mPhone;
    private String mCode;

    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight) {
        tvTitle.setText("找回密码");
    }

    @Override
    protected void init() {
    }

    private void requestCode(String phone) {
        HashMap<String, String> map = new HashMap();
        map.put("mobile", phone);
        map.put("type", 2 + "");//业务类型  1-注册业务  2-找回密码业务 3-修改原始手机 4-修改新手机
        HttpUtils.get(NetConstant.SEND_MOBILE_CODE, map, new DialogCallBack(mContext)
        {
            @Override
            public void onResponse(String response, int id) {
                SendCodeBean codeBean = new Gson().fromJson(response, SendCodeBean.class);
                if (handleResponseCode(codeBean))
                {
                    showToast(codeBean.getMsg());
                    new BtnTimerCount(60 * 1000, 1000, mBtnGetcode).start();
                }
            }
        });

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_retrieve_psw;
    }


    @OnClick({R.id.btn_getcode, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_getcode:
                mPhone = mEtPhone.getText().toString();
                if (TextUtils.isEmpty(mPhone)) {
                    showToast("请填写手机号");
                    return;
                }
                requestCode(mPhone);

                break;
            case R.id.btn_next:
                String phone = mEtPhone.getText().toString();
                if (!phone.equals(mPhone)) {
                    showToast("请重新发送验证码");
                    return;
                }
                mCode = mEtCode.getText().toString();
                if (TextUtils.isEmpty(mPhone)) {
                    showToast("请填写手机号");
                    return;
                }
                if (TextUtils.isEmpty(mCode)) {
                    showToast("请输入验证码");
                    return;
                }
                HashMap<String, String> map = new HashMap<>();
                map.put("mobile", mPhone);
                map.put("code", mCode);

                HttpUtils.post(NetConstant.GET_PASSWORD_ONE, map, new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        GetRequestBean requestBean = new Gson().fromJson(response,
                                GetRequestBean.class);
                        if (requestBean.getStatus() == 1) {
                            String code = requestBean.getInfo();
                            Bundle bundle=new Bundle();
                            bundle.putString(ConstantUtils.RESET_PSD_KEY,code);
                            startActivity(ResetPswActivity.class,false,bundle);

                        }
                    }
                });
                break;
        }
    }
}
