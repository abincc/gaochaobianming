package com.cnsunrun.jiajiagou.personal.waste;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseHeaderActivity;
import com.cnsunrun.jiajiagou.login.LoginCancelEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:
 * Data：2017/8/18 0018-上午 11:45
 * Blog：http://blog.csdn.net/u013983934
 * Author: CZ
 */
public class WastePriceActivity extends BaseHeaderActivity {
      @BindView(R.id.waste_order)
      ImageView mIvImv;


    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight) {
        tvTitle.setText("变废为宝");
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
        mIvImv.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                startActivity(new Intent(mContext, WasteMenuActivity.class));
                finish();
            }
        }, 1000);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_waste_price;
    }

    @OnClick({R.id.waste_order})
    public void onViewClicked(View view) {

    }
}
