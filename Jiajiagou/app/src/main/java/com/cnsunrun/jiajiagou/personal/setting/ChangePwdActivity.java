package com.cnsunrun.jiajiagou.personal.setting;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseHeaderActivity;
import com.cnsunrun.jiajiagou.common.base.BaseResp;
import com.cnsunrun.jiajiagou.common.constant.JjgConstant;
import com.cnsunrun.jiajiagou.common.network.DialogCallBack;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.OtherUtils;
import com.cnsunrun.jiajiagou.common.widget.view.HighLightEditText;
import com.cnsunrun.jiajiagou.login.LoginActivity;
import com.google.gson.Gson;

import java.util.HashMap;

import butterknife.BindView;

/**
 * Created by ${LiuDi}
 * on 2017/8/19on 15:46.
 */

public class ChangePwdActivity extends BaseHeaderActivity
{
    @BindView(R.id.et_old_pwd)
    HighLightEditText mEtOldPwd;
    @BindView(R.id.et_new_pwd)
    HighLightEditText mEtNewPwd;
    @BindView(R.id.et_again_pwd)
    HighLightEditText mEtAgainPwd;

    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight)
    {
        tvTitle.setText("修改密码");
        tvRight.setText("保存");
        tvRight.setVisibility(View.VISIBLE);

        tvRight.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                changePwd();
            }
        });
    }

    private void changePwd()
    {
        String oldpassword = mEtOldPwd.getText().toString().trim();
        String password = mEtAgainPwd.getText().toString().trim();
        final String repassword = mEtNewPwd.getText().toString().trim();


        if (TextUtils.isEmpty(oldpassword) || TextUtils.isEmpty(password) || TextUtils.isEmpty(repassword))
        {
            showToast("输入密码不能为空");
            return;
        }

        if (!repassword.equals(password))
        {
            showToast("两次输入不一致");
            return;
        }
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", JjgConstant.getToken(mContext));
        hashMap.put("oldpassword", oldpassword);
        hashMap.put("password", password);
        hashMap.put("repassword", repassword);

        HttpUtils.post(NetConstant.CHANGE_PASSWORD, hashMap, new DialogCallBack(mContext)
        {
            @Override
            public void onResponse(String response, int id)
            {
                LogUtils.d(response);
                BaseResp baseResp = new Gson().fromJson(response, BaseResp.class);
                if (handleResponseCode(baseResp))
                {
                    showToast(baseResp.getMsg(), 1);
                    OtherUtils.delayStartActivity(mContext,LoginActivity.class);
                }
            }
        });



    }

    @Override
    protected void init()
    {
        mEtOldPwd.setOnRightDrawableClickListener(new HighLightEditText.PswRightClickListener(mEtOldPwd, ContextCompat
                .getDrawable(mContext, R.drawable.login_icon_invisible_visible)));
        mEtNewPwd.setOnRightDrawableClickListener(new HighLightEditText.PswRightClickListener(mEtNewPwd, ContextCompat
                .getDrawable(mContext, R.drawable.login_icon_invisible_visible)));
        mEtAgainPwd.setOnRightDrawableClickListener(new HighLightEditText.PswRightClickListener(mEtAgainPwd, ContextCompat
                .getDrawable(mContext, R.drawable.login_icon_invisible_visible)));


    }

    @Override
    protected int getLayoutRes()
    {
        return R.layout.activity_change_pwd;
    }

}
