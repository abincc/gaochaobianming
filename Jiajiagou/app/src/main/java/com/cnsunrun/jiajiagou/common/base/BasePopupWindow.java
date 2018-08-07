package com.cnsunrun.jiajiagou.common.base;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.cnsunrun.jiajiagou.common.widget.popup.RelativePopupWindow;

import butterknife.ButterKnife;

/**
 * Description:
 * Data：2017/8/21 0021-下午 3:03
 * Blog：http://blog.csdn.net/u013983934
 * Author: CZ
 */
public abstract class BasePopupWindow extends RelativePopupWindow
{

    protected Context mContext;

    public BasePopupWindow(Context context, int width, int height)
    {
        mContext = context;
        setWidth(width);
        setHeight(height);
        View view = LayoutInflater.from(context).inflate(getLayoutRes(), null);
        setContentView(view);

        this.setFocusable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setOutsideTouchable(true);
        this.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        ButterKnife.bind(this,view);

    }


    abstract protected int getLayoutRes();
}
