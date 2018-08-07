package com.cnsunrun.jiajiagou.common.base;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.cnsunrun.jiajiagou.common.util.LogUtils;
import com.cnsunrun.jiajiagou.common.util.ToastTask;
import com.cnsunrun.jiajiagou.login.LoginActivity;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/11/9 0009.
 */

public abstract class BaseCallBack extends StringCallback
{
    private final Context mContext;

    public BaseCallBack(Context context)
    {
        mContext = context;
    }

    @Override
    public void onError(Call call, Exception e, int id)
    {
        LogUtils.printD(e.getLocalizedMessage());
        Toast.makeText(mContext, "网络异常，请稍后再试", Toast.LENGTH_SHORT).show();
    }

    /**
     * 处理服务器相应码
     *
     * @param baseResp 返回的基类
     * @return
     */
    protected boolean handleResponseCode(BaseResp baseResp)
    {
        boolean isCodeOk = false;
        if (baseResp.getStatus() == 1)
        {
            isCodeOk = true;
        } else
        {
            if (baseResp.getStatus() == -1 || baseResp.getStatus() == -2)
            {
                //重新登录
                ToastTask.getInstance(mContext).setMessage("请重新登录").error();
                mContext.startActivity(new Intent(mContext, LoginActivity.class));
            } else
            {
                ToastTask.getInstance(mContext).setMessage(baseResp.getMsg()).error();
//                ToastUtils.show(JjgApplication.getContext(), baseResp.getMsg());
            }
        }
        return isCodeOk;
    }

}

