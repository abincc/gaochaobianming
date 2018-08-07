package com.cnsunrun.jiajiagou.login;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseHeaderActivity;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.ConstantUtils;
import com.cnsunrun.jiajiagou.common.util.OtherUtils;
import com.cnsunrun.jiajiagou.common.widget.view.HighLightEditText;
import com.cnsunrun.jiajiagou.home.bean.PostRequestBean;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Description:
 * Data：2017/8/19 0019-下午 2:32
 * Blog：http://blog.csdn.net/u013983934
 * Author: CZ
 */
public class ResetPswActivity extends BaseHeaderActivity
{
    @BindView(R.id.et_psw)
    HighLightEditText mEtPsw;
    @BindView(R.id.et_psw_repeat)
    HighLightEditText mEtPswRepeat;
    private String code;

    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight)
    {
        tvTitle.setText("重置密码");
    }

    @Override
    protected void init()
    {
        code = getIntent().getStringExtra(ConstantUtils.RESET_PSD_KEY);
        
        Drawable showPswDrawable = ContextCompat.getDrawable(mContext, R.drawable.login_icon_invisible_visible);
        mEtPsw.setOnRightDrawableClickListener(new HighLightEditText.PswRightClickListener(mEtPsw, showPswDrawable));
        mEtPswRepeat.setOnRightDrawableClickListener(new HighLightEditText.PswRightClickListener(mEtPswRepeat, showPswDrawable));
    }

    @Override
    protected int getLayoutRes()
    {
        return R.layout.activity_reset_psw;
    }

    @OnClick(R.id.btn_confirm)
    public void onViewClicked()
    {
        String password = mEtPsw.getText().toString();
        String repassword = mEtPswRepeat.getText().toString();
        if (!password.equals(repassword)) {
            showToast("输入不一致,请重新输入");
            return;
        }
        HashMap<String,String> map=new HashMap();
        map.put("password",password);
        map.put("repassword",repassword);
        map.put("code_id",code);
        HttpUtils.post(NetConstant.GET_PASSWORD_TWO, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                PostRequestBean postRequestBean = new Gson().fromJson(response, PostRequestBean
                        .class);
                if (postRequestBean.getStatus()==1) {
                    showToast(postRequestBean.getMsg());
                    OtherUtils.delayStartActivity(mContext,LoginActivity.class);
                }
            }
        });
    }
}
