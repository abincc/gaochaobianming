package com.cnsunrun.jiajiagou.common.base;

import com.blankj.utilcode.utils.NetworkUtils;
import com.cnsunrun.jiajiagou.R;

import butterknife.OnClick;

/**
 * Description:
 * Data：2017/8/18 0018-上午 10:27
 * Blog：http://blog.csdn.net/u013983934
 * Author: CZ
 */
public class NetWorkErrorActivity extends BaseActivity
{
    @Override
    protected int getLayoutRes()
    {
        return R.layout.activity_network_error;
    }

    @Override
    protected void init()
    {

    }

    @Override
    protected boolean isNeedNetWork()
    {
        return false;
    }

    @OnClick(R.id.btn_action)
    public void onClick()
    {
        if (NetworkUtils.isConnected(mContext))
        {
            setResult(200);
            finish();
        } else
        {
            showToast("网络不可用");
        }
    }

    @Override
    public void onBackPressedSupport()
    {
        setResult(NetworkUtils.isConnected(mContext) ? 200 : 404);
        finish();
    }
}
