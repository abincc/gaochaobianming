package com.cnsunrun.jiajiagou.personal.waste;

import android.content.Context;
import android.os.Bundle;
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
 * on 2017/8/19on 11:00.
 */

public class MyWasteOrderActivity extends BaseHeaderActivity
{
    @BindView(R.id.magic_indicator)
    MagicIndicator mMagicIndicator;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    private ArrayList<Fragment> mFragments;
    private int pos;
    private BaseTitleIndicatorAdapter mIndicatorAdapter;
    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight)
    {
     tvTitle.setText("我的订单");
    }

    @Override
    protected void init()
    {
        pos=getIntent().getIntExtra("pos",0);
        mFragments=new ArrayList<>();

        //全部订单
//        mFragments.add(new AllOrderFragment());
//
//        //代付款
//        mFragments.add(new ObligationFragment());
//        //待收货
//        mFragments.add(new WaitForRecFragment());
//        //待评价
//        mFragments.add(new RemainEvaluatedFragment());

        for (int i = 1; i < 5; i++)
        {
            WasteBaseOrderFragment fragment = new WasteBaseOrderFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("type", i);
            fragment.setArguments(bundle);
            mFragments.add(fragment);
        }

        mViewPager.setAdapter(new SimpleFragmentStatePagerAdapter(getSupportFragmentManager(), mFragments));

        initIndicator();
    }
    private void initIndicator()
    {
        List<String> titles = Arrays.asList("全部", "未处理", "已处理", "已取消");
        final CommonNavigator commonNavigator = new CommonNavigator(mContext);
        mIndicatorAdapter = new BaseTitleIndicatorAdapter(mViewPager, titles, 0xff666666, 0xff007ee5, 14)
        {
            @Override
            public IPagerIndicator getIndicator(Context context)
            {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setColors(0xff007ee5);
                indicator.setLineWidth(ConvertUtils.dp2px(context, 15));
                indicator.setYOffset(ConvertUtils.dp2px(context, 2.5f));
                indicator.setRoundRadius(ConvertUtils.dp2px(context, 3));
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                return indicator;
            }
        };
        commonNavigator.setAdapter(mIndicatorAdapter);
        commonNavigator.setAdjustMode(true);
        mMagicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);

        mViewPager.setCurrentItem(pos);
    }
    @Override
    protected int getLayoutRes()
    {
        return R.layout.activity_my_waste_order;
    }
}
