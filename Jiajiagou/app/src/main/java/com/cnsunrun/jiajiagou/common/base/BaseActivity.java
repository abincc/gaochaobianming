package com.cnsunrun.jiajiagou.common.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.CallSuper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.NetworkUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.util.ToastTask;
import com.cnsunrun.jiajiagou.common.util.ToastUtils;
import com.cnsunrun.jiajiagou.login.LoginSuccessEvent;

import org.androidannotations.api.sharedpreferences.BooleanPrefEditorField;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportActivity;

public abstract class BaseActivity extends SupportActivity implements Refreshable, SwipeRefreshLayout.OnRefreshListener
{

    protected Context mContext;
    protected boolean hasInit;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected ProgressDialog mProgressDialog;
    protected boolean mInterceptLoading = false;
    protected RequestManager mImageLoader;
    boolean isActive;

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        Window window = getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(getLayoutRes());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        mContext = this;
        ButterKnife.bind(this);
        initWithNetwork();
        EventBus.getDefault().register(this);
    }

    private void initWithNetwork()
    {
        if (!NetworkUtils.isConnected(mContext) && isNeedNetWork())
        {
            startActivityForResult(new Intent(mContext, NetWorkErrorActivity.class), 404);
        } else
        {
            init();
            getSwipeRefreshLayout();
            hasInit = true;
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        isActive = true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginSuccess(LoginSuccessEvent event)
    {
        //    if (rgMain.getCheckedRadioButtonId() == R.id.rb_learn)
        if (hasInit)
        {
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    onRefresh();
                }
            }, 500);
        }
    }

    public boolean isInterceptLoading()
    {
        return mInterceptLoading;
    }

    public void setInterceptLoading(boolean interceptLoading)
    {
        mInterceptLoading = interceptLoading;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        if (ev.getAction() == MotionEvent.ACTION_DOWN && mInterceptLoading)
        {
            showToast("请等待页面加载");
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public Context getContext()
    {
        return mContext;
    }

    @Override
    public SwipeRefreshLayout getSwipeRefreshLayout()
    {
        if (swipeRefreshLayout == null)
        {
            swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
            if (swipeRefreshLayout != null)
            {
                swipeRefreshLayout.setOnRefreshListener(this);
            }
        }
        return swipeRefreshLayout;
    }

    protected abstract void init();

    public void showToast(String content)
    {
//        ToastUtils.show(mContext, content);

        ToastTask.getInstance(mContext).setMessage(content).error();
    }

    public void showToast(String content, int success)
    {
//        ToastUtils.show(mContext, content);

        ToastTask.getInstance(mContext).setMessage(content).success();
    }
    protected boolean isNeedNetWork()
    {
        return true;
    }

    protected abstract int getLayoutRes();

    @Override
    @CallSuper
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 404 && resultCode == 404 && !NetworkUtils.isConnected(mContext))
        {
            this.finish();
        } else if (requestCode == 404 && resultCode == 200)
        {
            init();
        }
    }


    @Override
    public void onRefresh()
    {
        LogUtils.d("activity");
    }

    protected void setWindowBackground(int res)
    {
        Window window = getWindow();
        if (window != null)
        {
            window.setBackgroundDrawableResource(res);
        }
    }

    protected void showProgreesDialog(String msg, boolean cancelable)
    {
        if (mProgressDialog == null)
        {
            mProgressDialog = new ProgressDialog(mContext);
        }
        mProgressDialog.setMessage(msg);
        mProgressDialog.setCancelable(cancelable);
        mProgressDialog.show();
    }

    protected void dismissProgressDialog()
    {
        if (mProgressDialog != null && !isDestroyed())
        {
            mProgressDialog.dismiss();
        }
    }

    public RequestManager getImageLoader()
    {
        if (mImageLoader == null) mImageLoader = Glide.with(this);
        return mImageLoader;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (mImageLoader != null) mImageLoader.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 打开一个Activity 默认 不关闭当前activity
     *
     * @param clz
     */
    public void startActivity(Class<?> clz)
    {
        startActivity(clz, false, null);
    }

    public void startActivity(Class<?> clz, boolean isCloseCurrentActivity)
    {
        startActivity(clz, isCloseCurrentActivity, null);
    }

    public void startActivity(Class<?> clz, boolean isCloseCurrentActivity, Bundle ex)
    {
        Intent intent = new Intent(mContext, clz);
        if (ex != null)
            intent.putExtras(ex);
        startActivity(intent);
        if (isCloseCurrentActivity)
        {
            finish();
        }
    }

}
