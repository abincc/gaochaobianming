package com.cnsunrun.jiajiagou.common.network;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseResp;
import com.cnsunrun.jiajiagou.common.base.Refreshable;
import com.cnsunrun.jiajiagou.common.util.ToastTask;
import com.cnsunrun.jiajiagou.common.widget.CustomDialog;
import com.cnsunrun.jiajiagou.login.LoginActivity;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;
import okhttp3.Request;


public abstract class DialogCallBack extends StringCallback
{

    protected Context mContext;
    protected Refreshable mRefreshable;
    //    protected ProgressDialog dialog;
    protected CustomDialog dialog;
    protected boolean showProgressDialog = true;
    protected boolean isRefresh = false;
    protected DataCallback mDataCallback;
    protected CharSequence mDialogMsg = " 加载中...";


    public DialogCallBack(Context context)
    {
        mContext = context;
    }

    public DialogCallBack(Context context, DataCallback callback)
    {
        mContext = context;
        mDataCallback = callback;
        showProgressDialog(callback.isShowDialog());
    }


    public DialogCallBack(Context context, CharSequence dialogMsg)
    {
        mContext = context;
        mDialogMsg = dialogMsg;
    }

    public DialogCallBack(Refreshable refreshable)
    {
        mRefreshable = refreshable;
        mContext = mRefreshable.getContext();
    }

    public DialogCallBack(Refreshable refreshable, DataCallback callback)
    {
        mRefreshable = refreshable;
        mContext = mRefreshable.getContext();
        mDataCallback = callback;
    }

    public DialogCallBack(DataCallback callback)
    {
        mDataCallback = callback;
    }


    @Override
    public void onError(Call call, Exception e, int id)
    {
        Context context = mContext != null ? mContext : mRefreshable == null ? null : mRefreshable.getContext();
        if (context != null)
        {
            Toast.makeText(context, "网络异常，请稍后再试", Toast.LENGTH_SHORT).show();

        }

        if (mDataCallback != null) mDataCallback.onError(null);

    }

    @Override
    public void onBefore(Request request, int id)
    {
        if (mRefreshable != null && mRefreshable.getSwipeRefreshLayout() != null)
        {
            mRefreshable.getSwipeRefreshLayout().setRefreshing(true);
        }

        if (!showProgressDialog)
        {
            return;
        }
        if (isRefresh)
        {
            return;
        }
        if (mContext != null && mRefreshable == null)
        {
            dialog = new CustomDialog(mContext, R.style.CustomDialog);
//            dialog.setMessage(mDialogMsg);
            dialog.setCancelable(false);
            dialog.show();
        }
    }

    @Override
    public void onAfter(int id)
    {
        if (mRefreshable != null)
        {

            if (mRefreshable.isInterceptLoading()) mRefreshable.setInterceptLoading(false);

            if (mRefreshable.getSwipeRefreshLayout() != null)
            {
                mRefreshable.getSwipeRefreshLayout().setRefreshing(false);
            }

        }
        if (!showProgressDialog)
        {
            return;
        }

        if (mContext != null && dialog != null && dialog.isShowing())
        {
            if (mContext instanceof Activity)
            {
                Activity activity = (Activity) this.mContext;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                {
                    if (!activity.isDestroyed() || !activity.isFinishing())
                    {
                        try
                        {
                            dialog.dismiss();
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                } else
                {
                    try
                    {
                        if (!activity.isFinishing())
                            dialog.dismiss();
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            dialog = null;
        }
        isRefresh = true;

        if (mDataCallback != null) mDataCallback.onAfter();
    }

    /**
     * 处理服务器相应码
     *
     * @param code 相应代码
     * @param msg
     * @return
     */
    protected boolean handleResponseCode(int code, String msg)
    {
        boolean isCodeOk = false;
        if (code == 1)
        {
            isCodeOk = true;
        } else
        {
            if (code == -1 || code == -2)
            {
//                ToastUtils.show(mContext, "请重新登录~");
                ToastTask.getInstance(mContext).setMessage("请重新登录").error();
                mContext.startActivity(new Intent(mContext, LoginActivity.class));
            } else
            {
                    ToastTask.getInstance(mContext).setMessage(msg).error();
//                if (mDataCallback != null && mDataCallback.showErrToast())
//                    ToastUtils.show(mContext, msg);
            }
        }
        return isCodeOk;
    }

    protected boolean handleResponseCode(BaseResp baseResp)
    {
        return baseResp != null && handleResponseCode(baseResp.getStatus(), baseResp.getMsg());
    }

    public void showProgressDialog(boolean b)
    {
        showProgressDialog = b;
    }
}
