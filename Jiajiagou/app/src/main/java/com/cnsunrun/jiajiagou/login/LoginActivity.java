package com.cnsunrun.jiajiagou.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseHeaderActivity;
import com.cnsunrun.jiajiagou.common.constant.SPConstant;
import com.cnsunrun.jiajiagou.common.network.DialogCallBack;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.ConstantUtils;
import com.cnsunrun.jiajiagou.common.util.SPUtils;
import com.cnsunrun.jiajiagou.common.widget.view.HighLightEditText;
import com.cnsunrun.jiajiagou.forum.bean.LoginBean;
import com.cnsunrun.jiajiagou.forum.bean.TokenBean;
import com.cnsunrun.jiajiagou.personal.UserTypeEvent;
import com.cnsunrun.jiajiagou.regist.RegistActivity;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;

import static com.cnsunrun.jiajiagou.R.id.et_phone;

/**
 * Description:
 * Data：2017/8/18 0018-上午 11:45
 * Blog：http://blog.csdn.net/u013983934
 * Author: CZ
 */
public class LoginActivity extends BaseHeaderActivity {
    @BindView(et_phone)
    HighLightEditText mEtPhone;
    @BindView(R.id.et_psw)
    HighLightEditText mEtPsw;
    private int mReLoginCode;

    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight) {
        tvTitle.setText("登录");
        ivBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
                EventBus.getDefault().post(new LoginCancelEvent());
            }
        });
    }

    @Override
    protected void init() {
//        mEtPhone.setText("15888888887");
//        mEtPsw.setText("jj123");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mReLoginCode = bundle.getInt(ConstantUtils.RELOGIN, 0);
        }
        mEtPsw.setOnRightDrawableClickListener(new HighLightEditText.PswRightClickListener(mEtPsw, ContextCompat.getDrawable(mContext, R.drawable.login_icon_invisible_visible)));
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_login;
    }

    @OnClick({R.id.btn_login, R.id.tv_forget_psw, R.id.tv_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                requestNet();
                break;
            case R.id.tv_forget_psw:
                startActivity(new Intent(mContext, RetrievePswActivity.class));
                break;
            case R.id.tv_register:
                startActivity(new Intent(mContext, RegistActivity.class));
                break;
        }
    }

    private void requestNet() {
        if(TextUtils.isEmpty(mEtPhone.getText())||TextUtils.isEmpty(mEtPsw.getText())){
            showToast("手机号或密码不能为空");
            return;
        }
        String phoneNumber = mEtPhone.getText().toString();
        String password = mEtPsw.getText().toString();

        HashMap<String,String> map=new HashMap<>();
        map.put("mobile",phoneNumber);
        map.put("password",password);
        HttpUtils.post(NetConstant.LOGIN, map, new DialogCallBack(mContext)
        {
            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(response);
                LoginBean loginBean = new Gson().fromJson(response, LoginBean.class);
                if (loginBean.getStatus()==1) {
                    String ticket = loginBean.getInfo().getTicket();
                    SPUtils.put(mContext,SPConstant.TICKET,ticket);
                    SPUtils.put(mContext, SPConstant.TYPE, Integer.parseInt(loginBean.getInfo().getType()));
                    SPUtils.put(mContext,SPConstant.DISTRICT_ID,loginBean.getInfo().getDistrict_id());
                    SPUtils.put(mContext,SPConstant.DISTRICT_TITLE,loginBean.getInfo().getDistrict_title());
                    requestToken(ticket);
                    EventBus.getDefault().post(new LoginSuccessEvent());
                    String userId = loginBean.getInfo().getMember_id();
                    SPUtils.put(mContext, SPConstant.USER_ID, userId);
                    JPushInterface.setAlias(mContext, 0, userId);
                    LoginActivity.this.setResult(RESULT_OK);
                    LoginActivity.this.finish();
                }else{
                    showToast(loginBean.getMsg());
                }
            }
        });
    }

    private void requestToken(String ticket) {
        HashMap<String,String> map=new HashMap();
        map.put("ticket",ticket);
        HttpUtils.post(NetConstant.GET_TOKEN, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                TokenBean tokenBean = new Gson().fromJson(response, TokenBean.class);
                if (tokenBean.getStatus()==1) {
                    String token = tokenBean.getInfo().getToken();
                    SPUtils.put(mContext, SPConstant.TOKEN, token);
                    EventBus.getDefault().post(new UserTypeEvent());
                }
            }
        });
    }

    //    private String token;
    private void setToken(String token){
//        this.token=token;
    }
}
