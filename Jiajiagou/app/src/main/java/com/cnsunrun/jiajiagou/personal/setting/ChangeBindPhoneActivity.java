package com.cnsunrun.jiajiagou.personal.setting;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.RegexUtils;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseHeaderActivity;
import com.cnsunrun.jiajiagou.common.base.BaseResp;
import com.cnsunrun.jiajiagou.common.constant.JjgConstant;
import com.cnsunrun.jiajiagou.common.network.DialogCallBack;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.widget.BtnTimerCount;
import com.cnsunrun.jiajiagou.forum.bean.SendCodeBean;
import com.google.gson.Gson;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ${LiuDi}
 * on 2017/8/21on 16:26.
 * 修改绑定手机号
 */

public class ChangeBindPhoneActivity extends BaseHeaderActivity
{
    @BindView(R.id.btn_getcode)
    Button mBtnGetcode;
    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.et_code)
    EditText mEtCode;
    private boolean is_fist_step;

    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight)
    {
        tvTitle.setText("修改绑定手机号");
        tvRight.setVisibility(View.VISIBLE);
        if (is_fist_step)
        {
            tvRight.setText("下一步");
        } else
        {
            tvRight.setText("保存");
            mEtPhone.setHint("请输入新手机号");
        }

        tvRight.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String phone = mEtPhone.getText().toString().trim();
                String code = mEtCode.getText().toString().trim();
                if (!RegexUtils.isMobileSimple(phone))
                {
                    showToast("请输入合理的手机号");
                    return;
                }
                if (TextUtils.isEmpty(code))
                {
                    showToast("验证码不能为空");
                    return;
                }
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("token", JjgConstant.getToken(mContext));
                if (is_fist_step)
                {
                    hashMap.put("oldmobile", phone);
                    hashMap.put("code", code);
                    HttpUtils.post(NetConstant.CHANGE_MOBILE_STEP1, hashMap, new DialogCallBack(mContext)
                    {
                        @Override
                        public void onResponse(String response, int id)
                        {
                            LogUtils.d(response);
                            BaseResp baseResp = new Gson().fromJson(response, BaseResp.class);
                            if (handleResponseCode(baseResp))
                            {

                                //下一步
                                Intent intent = new Intent(mContext, ChangeBindPhoneActivity.class);
                                intent.putExtra("is_fist_step", false);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });

                } else
                {
                    hashMap.put("mobile", phone);
                    hashMap.put("code", code);
                    HttpUtils.post(NetConstant.CHANGE_MOBILE_STEP2, hashMap, new DialogCallBack(mContext)
                    {
                        @Override
                        public void onResponse(String response, int id)
                        {
                            LogUtils.d(response);
                            BaseResp baseResp = new Gson().fromJson(response, BaseResp.class);
                            if (handleResponseCode(baseResp))
                            {
                                showToast(baseResp.getMsg(), 1);
                                finish();
                            }
                        }
                    });
                }
            }
        });

    }

    @Override
    protected void init()
    {
        is_fist_step = getIntent().getBooleanExtra("is_fist_step", true);
    }

    @Override
    protected int getLayoutRes()
    {
        return R.layout.activity_bind_phone;
    }


    @OnClick(R.id.btn_getcode)
    public void onViewClicked()
    {
        String mobile = mEtPhone.getText().toString().toString();
        if (mobile.isEmpty())
        {
            showToast("手机号不能为空");
            return;
        }
        if (!RegexUtils.isMobileSimple(mobile))
        {
            showToast("手机号不合法");
            return;
        }
        int type = is_fist_step ? 3 : 4;
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("mobile", mobile);
        hashMap.put("type", String.valueOf(type));

        HttpUtils.post(NetConstant.SEND_MOBILE_CODE, hashMap, new DialogCallBack(mContext)
        {
            @Override
            public void onResponse(String response, int id)
            {
                LogUtils.d(response);
                SendCodeBean codeBean = new Gson().fromJson(response, SendCodeBean.class);
                if (handleResponseCode(codeBean))
                {
                    if (!TextUtils.isEmpty(codeBean.getInfo()))
                    {
                        showToast(codeBean.getMsg(), 1);
                        new BtnTimerCount(60 * 1000, 1000, mBtnGetcode).start();
//                        mEtCode.setText(codeBean.getInfo());
                    }

                }
            }
        });


    }

}
