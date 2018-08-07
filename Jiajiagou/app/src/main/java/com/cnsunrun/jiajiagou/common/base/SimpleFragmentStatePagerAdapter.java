package com.cnsunrun.jiajiagou.common.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;


public class SimpleFragmentStatePagerAdapter extends FragmentPagerAdapter
{
    private List<Fragment> mFragments;


    public SimpleFragmentStatePagerAdapter(FragmentManager fm, List<Fragment> fragments)
    {
        super(fm);
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int arg0)
    {
        return mFragments.get(arg0);
    }

    @Override
    public int getCount()
    {
        return mFragments.size();
    }

}
