package com.cnsunrun.jiajiagou.launch;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.cnsunrun.jiajiagou.MainActivity;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseActivity;

import butterknife.BindView;

/**
 * Created by ${LiuDi}
 * on 2017/8/19on 9:49.
 */

public class LaunchActivity extends BaseActivity
{
    @BindView(R.id.iv_imv)
    ImageView mIvImv;

    @Override
    protected void init()
    {
        mIvImv.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                startActivity(new Intent(mContext, MainActivity.class));
                finish();
            }
        }, 1000);
    }

    @Override
    public void onCreate(Bundle bundle)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(bundle);
    }

    @Override
    protected int getLayoutRes()
    {
        return R.layout.activity_launch;
    }
}
