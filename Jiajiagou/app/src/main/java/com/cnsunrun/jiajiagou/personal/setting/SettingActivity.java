package com.cnsunrun.jiajiagou.personal.setting;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnsunrun.jiajiagou.MainActivity;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseHeaderActivity;
import com.cnsunrun.jiajiagou.common.base.TwoButtonDialog;
import com.cnsunrun.jiajiagou.common.util.SPUtils;
import com.cnsunrun.jiajiagou.personal.AboutUsActivity;
import com.cnsunrun.jiajiagou.personal.AdviceFeedbackActivity;
import com.cnsunrun.jiajiagou.personal.setting.address.AddressActivity;

import butterknife.OnClick;

/**
 * Created by ${LiuDi}
 * on 2017/8/18on 16:39.
 * 设置
 */

public class SettingActivity extends BaseHeaderActivity
{
    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight)
    {
        tvTitle.setText("设置");
    }

    @Override
    protected void init()
    {

    }

    @Override
    protected int getLayoutRes()
    {
        return R.layout.activity_setting;
    }


    @OnClick({R.id.rl_info, R.id.rl_receiver_address, R.id.rl_safety, R.id.rl_feedback, R.id.rl_about_us, R.id
            .btn_confirm})
    public void onViewClicked(View view)
    {
        switch (view.getId())
        {
            //个人资料
            case R.id.rl_info:
                startActivity(new Intent(mContext,PersonalInfoActivity.class));
                break;
            //收货地址
            case R.id.rl_receiver_address:

            startActivity(new Intent(mContext,AddressActivity.class));
                break;
            //账户安全
            case R.id.rl_safety:
                startActivity(new Intent(mContext,AccountSafetyActivity.class));
                break;
            //意见反馈
            case R.id.rl_feedback:
                startActivity(new Intent(mContext, AdviceFeedbackActivity.class));
                break;
            //关于我们
            case R.id.rl_about_us:
                startActivity(new Intent(mContext,AboutUsActivity.class));
                break;
            //退出登录
            case R.id.btn_confirm:
                final TwoButtonDialog dialog = new TwoButtonDialog();
                dialog.setContent("是否退出登录？");
                dialog.setOnBtnConfirmClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        dialog.dismiss();
                        SPUtils.clearSP(mContext);
                        Intent intent = new Intent(mContext, MainActivity.class);
                        intent.putExtra("check_index", 0);
                        startActivity(intent);
                    }
                });
                dialog.show(getSupportFragmentManager(), null);


                break;
        }
    }
}
