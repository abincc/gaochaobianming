package com.cnsunrun.jiajiagou.personal.setting;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseHeaderActivity;

import butterknife.OnClick;

/**
 * Created by ${LiuDi}
 * on 2017/8/19on 14:33.
 */

public class AccountSafetyActivity extends BaseHeaderActivity
{
    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight)
    {
        tvTitle.setText("账户安全");
    }

    @Override
    protected void init()
    {

    }

    @Override
    protected int getLayoutRes()
    {
        return R.layout.activity_account_safety;
    }

    @OnClick({R.id.rl_change_pwd, R.id.rl_change_phone})
    public void onViewClicked(View view)
    {
        switch (view.getId())
        {
            case R.id.rl_change_pwd:
                startActivity(new Intent(mContext,ChangePwdActivity.class));

                break;
            case R.id.rl_change_phone:
                Intent intent=new Intent(mContext,ChangeBindPhoneActivity.class);
                intent.putExtra("is_fist_step",true);
                startActivity(intent);

                break;
        }
    }
}
