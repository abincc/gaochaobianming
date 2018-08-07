package com.cnsunrun.jiajiagou.common.widget;

import net.lucode.hackware.magicindicator.FragmentContainerHelper;
import net.lucode.hackware.magicindicator.MagicIndicator;

/**
 * Description:
 * Data：2017/8/25 0025-上午 9:48
 * Blog：http://blog.csdn.net/u013983934
 * Author: CZ
 */
public class IndicatorSwitchHelper extends FragmentContainerHelper
{
    private OnPageChangedListener mOnPageChangedListener;

    public IndicatorSwitchHelper(MagicIndicator magicIndicator, OnPageChangedListener onPageChangedListener)
    {
        super(magicIndicator);
        mOnPageChangedListener = onPageChangedListener;
    }

    @Override
    public void handlePageSelected(int selectedIndex, boolean smooth)
    {
        super.handlePageSelected(selectedIndex, smooth);
        if (mOnPageChangedListener != null)
        {
            mOnPageChangedListener.onPageChanged(selectedIndex);
        }
    }

    public void setOnPageChangedListener(OnPageChangedListener onPageChangedListener)
    {
        mOnPageChangedListener = onPageChangedListener;
    }

    public interface OnPageChangedListener
    {
        void onPageChanged(int pos);
    }
}
