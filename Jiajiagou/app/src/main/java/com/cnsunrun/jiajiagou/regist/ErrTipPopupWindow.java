package com.cnsunrun.jiajiagou.regist;

import android.content.Context;
import android.widget.TextView;

import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BasePopupWindow;

import butterknife.BindView;

/**
 * Description:
 * Data：2017/8/21 0021-下午 3:10
 * Blog：http://blog.csdn.net/u013983934
 * Author: CZ
 */
public class ErrTipPopupWindow extends BasePopupWindow
{
    @BindView(R.id.tv_content)
    TextView mTvContent;

    public ErrTipPopupWindow(Context context, int width, int height)
    {
        super(context, width, height);
    }

    public void setText(CharSequence content)
    {
        mTvContent.setText(content);
    }

    @Override
    protected int getLayoutRes()
    {
        return R.layout.pop_err_tip;
    }
}
