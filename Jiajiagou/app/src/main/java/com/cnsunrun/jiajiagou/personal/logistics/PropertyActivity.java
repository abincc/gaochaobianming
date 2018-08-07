package com.cnsunrun.jiajiagou.personal.logistics;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.ConvertUtils;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseHeaderActivity;
import com.cnsunrun.jiajiagou.common.base.BaseTitleIndicatorAdapter;
import com.cnsunrun.jiajiagou.common.base.SimpleFragmentStatePagerAdapter;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * Created by ${LiuDi}
 * on 2017/8/22on 10:52.
 */

public class PropertyActivity extends BaseHeaderActivity
{
    @BindView(R.id.magic_indicator)
    MagicIndicator mMagicIndicator;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    private ArrayList<Fragment> mFragments;
    private BaseTitleIndicatorAdapter mIndicatorAdapter;
    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight)
    {
        tvTitle.setText("物业管理");
    }

    @Override
    protected void init()
    {
        mFragments=new ArrayList<>();

        //保洁维修
        mFragments.add(new CleaningFragment());
        //小事帮忙
        mFragments.add(new LittleHelpFragment());


        mViewPager.setAdapter(new SimpleFragmentStatePagerAdapter(getSupportFragmentManager(), mFragments));

        initIndicator();
    }
    private void initIndicator()
    {
        List<String> titles = Arrays.asList("保洁维修","小事帮忙");
        final CommonNavigator commonNavigator = new CommonNavigator(mContext);
        mIndicatorAdapter = new BaseTitleIndicatorAdapter(mViewPager, titles, 0x99ffffff, 0xffffffff, 14)
        {
            @Override
            public IPagerIndicator getIndicator(Context context)
            {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setColors(0xffffffff);
                indicator.setLineWidth(ConvertUtils.dp2px(context, 25));
                indicator.setYOffset(ConvertUtils.dp2px(context, 2));
                indicator.setRoundRadius(ConvertUtils.dp2px(context, 3));
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                return indicator;
            }
        };
        commonNavigator.setAdapter(mIndicatorAdapter);
        commonNavigator.setAdjustMode(true);
        mMagicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);


    }
    @Override
    protected int getLayoutRes()
    {
        return R.layout.activity_property;
    }
}
