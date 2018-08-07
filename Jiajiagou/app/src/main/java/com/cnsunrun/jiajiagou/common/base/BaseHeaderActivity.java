package com.cnsunrun.jiajiagou.common.base;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnsunrun.jiajiagou.R;

public abstract class BaseHeaderActivity extends BaseActivity
{

    private boolean hasInitHeader;

    @Override
    protected void onResume()
    {
        super.onResume();
        if (!hasInitHeader)
        {
            initHeaderView();
            hasInitHeader = true;
        }
    }

    private void initHeaderView()
    {
        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        ImageView ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressedSupport();
            }
        });
        TextView tvRight = (TextView) findViewById(R.id.tv_right);
        initHeader(tvTitle, ivBack, tvRight);
    }


    abstract protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight);
}
