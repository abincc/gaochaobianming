package com.cnsunrun.jiajiagou.common.network;


import android.content.Context;

import com.cnsunrun.jiajiagou.common.base.BaseResp;
import com.cnsunrun.jiajiagou.common.base.Refreshable;

import java.lang.reflect.Type;

/**
 * Description:
 * Data：2017/6/9 0009-下午 3:41
 * Blog：http://blog.csdn.net/u013983934
 * Author: CZ
 */
public interface DataCallback<T>
{
    void onSuccess(T t);

    void onError(BaseResp baseResp);

    void onAfter();

    boolean isShowDialog();

    Type getTypeClazz();

    Refreshable getRefreshable();

    Context getContext();

    boolean showErrToast();

}
