package com.cnsunrun.jiajiagou.common.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.util.ToastTask;
import com.cnsunrun.jiajiagou.login.LoginSuccessEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportFragment;


public abstract class BaseFragment extends SupportFragment implements Refreshable, SwipeRefreshLayout.OnRefreshListener
{

    public boolean hasInit = false;
    protected View contentView;
    protected Context mContext;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected Bundle mBundle;
    public boolean mHasNewAction;
    protected boolean mInterceptLoading;
    protected RequestManager mImageLoader;

    public RequestManager getImageLoader()
    {
        if (mImageLoader == null) mImageLoader = Glide.with(this);
        return mImageLoader;
    }

    @Override
    public SwipeRefreshLayout getSwipeRefreshLayout()
    {
        if (swipeRefreshLayout == null)
        {
            swipeRefreshLayout = (SwipeRefreshLayout) contentView.findViewById(R.id.swipe_refresh_layout);
            if (swipeRefreshLayout != null)
            {
                swipeRefreshLayout.setOnRefreshListener(this);
            }
        }
        return swipeRefreshLayout;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (contentView == null)
        {
            mContext = getContext();
            contentView = inflater.inflate(getChildLayoutRes(), null);
            ((BaseActivity) getActivity()).setInterceptLoading(mInterceptLoading);
        }
        return contentView;
    }

    @Override
    public void onHiddenChanged(boolean hidden)
    {
        super.onHiddenChanged(hidden);
        if (!hidden)
        {
            onHiddenShow();
        }
    }

    protected void onHiddenShow()
    {
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        if (!hasInit)
        {
            hasInit = true;
            ButterKnife.bind(this, view);
            getSwipeRefreshLayout();
            init();
        }
    }

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

    abstract protected void init();

    abstract protected int getChildLayoutRes();

    @Override
    public void onRefresh()
    {

    }

    @Override
    public boolean isInterceptLoading()
    {
        return mInterceptLoading;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginSuccess(LoginSuccessEvent event)
    {
        if (isVisible())
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

    @Override
    public void setInterceptLoading(boolean interceptLoading)
    {
        mInterceptLoading = interceptLoading;
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
            getActivity().finish();
        }
    }
}
