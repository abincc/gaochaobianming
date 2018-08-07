package com.cnsunrun.jiajiagou.common.base;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;

/**
 * Description:
 * Data：2016/12/8 0008-下午 12:00
 * Blog：http://blog.csdn.net/u013983934
 * Author: CZ
 */
public interface Refreshable
{
    SwipeRefreshLayout getSwipeRefreshLayout();

    Context getContext();

    boolean isInterceptLoading();

    void setInterceptLoading(boolean interceptLoading);
}
