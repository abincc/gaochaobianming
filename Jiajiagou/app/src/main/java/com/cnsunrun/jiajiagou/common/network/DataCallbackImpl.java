package com.cnsunrun.jiajiagou.common.network;


import android.content.Context;

import com.cnsunrun.jiajiagou.common.base.BaseResp;
import com.cnsunrun.jiajiagou.common.base.Refreshable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Description:
 * Data：2017/6/10 0010-上午 10:21
 * Blog：http://blog.csdn.net/u013983934
 * Author: CZ
 */
public abstract class DataCallbackImpl<T> implements DataCallback<T>
{

    @Override
    public Context getContext()
    {
        return null;
    }

    @Override
    public Refreshable getRefreshable()
    {
        return null;
    }

    @Override
    public Type getTypeClazz()
    {
        return ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public void onError(BaseResp baseResp)
    {

    }

    @Override
    public void onAfter()
    {
    }

    @Override
    public boolean showErrToast()
    {
        return true;
    }

    @Override
    public boolean isShowDialog()
    {
        return true;
    }
}
