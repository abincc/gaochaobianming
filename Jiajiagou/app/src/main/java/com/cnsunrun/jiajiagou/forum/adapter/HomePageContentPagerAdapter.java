package com.cnsunrun.jiajiagou.forum.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 个人主页
 * <p>
 * author:yyc
 * date: 2017-08-30 13:01
 */
public class HomePageContentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> tabFragments;
    private List<String> tabIndicators;

    public HomePageContentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setData(List<Fragment> tabFragments, List<String> tabIndicators) {
        this.tabFragments = tabFragments;
        this.tabIndicators = tabIndicators;
    }


    @Override
    public Fragment getItem(int position) {
        return tabFragments.get(position);
    }

    @Override
    public int getCount() {
        return tabIndicators == null ? 0 : tabIndicators.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabIndicators.get(position);
    }
}
