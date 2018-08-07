package com.cnsunrun.jiajiagou.common.base;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;

import net.lucode.hackware.magicindicator.FragmentContainerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.List;

/**
 * Description:
 * Data：2017/8/22 0022-下午 12:02
 * Blog：http://blog.csdn.net/u013983934
 * Author: CZ
 */
public abstract class BaseTitleIndicatorAdapter extends CommonNavigatorAdapter
{

    private ViewPager mViewPager;
    private List<String> mTitles;
    private final int mNormalTextColor;
    private final int mSelectedTextColor;
    private final int mTextSize;
    private FragmentContainerHelper mFragmentContainerHelper;

    public BaseTitleIndicatorAdapter(ViewPager viewPager, List<String> titles, int normalTextColor, int
            selectedTextColor, int textSize)
    {
        mViewPager = viewPager;
        mTitles = titles;
        mNormalTextColor = normalTextColor;
        mSelectedTextColor = selectedTextColor;
        mTextSize = textSize;
    }

//    public void updateTitles(List<String> titles){
//        mTitles = titles;
//    }

    public BaseTitleIndicatorAdapter(FragmentContainerHelper fragmentContainerHelper, List<String> titles, int
            normalTextColor, int
                                             selectedTextColor, int textSize)
    {
        mTitles = titles;
        mNormalTextColor = normalTextColor;
        mSelectedTextColor = selectedTextColor;
        mTextSize = textSize;
        mFragmentContainerHelper = fragmentContainerHelper;
    }



    public void setTitles(List<String> titles)
    {
        mTitles = titles;
        notifyDataSetChanged();
    }

    @Override
    public int getCount()
    {
        return mTitles == null ? 0 : mTitles.size();
    }


    @Override
    public IPagerTitleView getTitleView(Context context, final int i)
    {
        ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
        colorTransitionPagerTitleView.setNormalColor(mNormalTextColor);
        colorTransitionPagerTitleView.setTextSize(mTextSize);
        colorTransitionPagerTitleView.setSelectedColor(mSelectedTextColor);
        colorTransitionPagerTitleView.setText(mTitles.get(i));

        colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (mViewPager != null)
                    mViewPager.setCurrentItem(i);
                if (mFragmentContainerHelper != null)
                    mFragmentContainerHelper.handlePageSelected(i);
            }
        });

        return colorTransitionPagerTitleView;
    }
}
