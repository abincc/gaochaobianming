package com.cnsunrun.jiajiagou.forum.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.View;

import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles
        .ColorTransitionPagerTitleView;

import java.util.List;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-08-25 14:49
 */
public class MyCommonNavigatorAdapter extends CommonNavigatorAdapter {
    private ViewPager mContentVp;
    private List<String> tabIndicators;
    public MyCommonNavigatorAdapter(ViewPager viewPager,List<String> tabIndicators){
        this.mContentVp=viewPager;
        this.tabIndicators=tabIndicators;
    }
    public void updateTabData(List<String> tabIndicators){
        this.tabIndicators=tabIndicators;
    }
    @Override
    public int getCount() {
        return tabIndicators==null?0:tabIndicators.size();
    }

    @Override
    public IPagerTitleView getTitleView(Context context, final int index) {
        ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
        colorTransitionPagerTitleView.setNormalColor(Color.parseColor("#99ffffff"));
        colorTransitionPagerTitleView.setSelectedColor(Color.WHITE);
        colorTransitionPagerTitleView.setText(tabIndicators.get(index));
        colorTransitionPagerTitleView.setTextSize(16);
        colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContentVp.setCurrentItem(index);
            }
        });
        return colorTransitionPagerTitleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        LinePagerIndicator indicator = new LinePagerIndicator(context);
        indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
        indicator.setYOffset(UIUtil.dip2px(context,3));
        indicator.setRoundRadius(UIUtil.dip2px(context,3));
        indicator.setColors(Color.parseColor("#ffffff"));
        return indicator;
    }
}
