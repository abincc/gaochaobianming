package com.cnsunrun.jiajiagou.personal.posts;

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
import com.cnsunrun.jiajiagou.common.constant.SPConstant;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.ConstantUtils;
import com.cnsunrun.jiajiagou.common.util.SPUtils;
import com.cnsunrun.jiajiagou.forum.bean.HomepageUserInfoBean;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Description:
 * Data：2017/8/22 0022-上午 11:18
 * Blog：http://blog.csdn.net/u013983934
 * Author: CZ
 */
public class PersonalPostsActivity extends BaseHeaderActivity
{
    @BindView(R.id.magic_indicator)
    MagicIndicator mMagicIndicator;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    private ArrayList<Fragment> mFragments;
    private BaseTitleIndicatorAdapter mIndicatorAdapter;
    private String mUserId;

    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight)
    {
        tvTitle.setText("帖子");
    }

    @Override
    protected void init()
    {
        requestNet();
        initFragments();
        initIndicator();
    }

    private void requestNet() {
        mUserId = SPUtils.getString(this, SPConstant.USER_ID);
        HashMap<String,String> map=new HashMap<>();
        map.put("id",mUserId);
        HttpUtils.get(NetConstant.HOMEPAGE_USER_INFO, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                HomepageUserInfoBean infoBean = new Gson().fromJson(response,
                        HomepageUserInfoBean.class);
                    if (infoBean.getStatus()==1) {
                        HomepageUserInfoBean.InfoBean info = infoBean.getInfo();
                        String threads = info.getThreads();
                        String posts = info.getPosts();
                        String likes = info.getLikes();
                        String thread = Integer.parseInt(threads) == 0 ? "帖子" : "帖子" + threads;
                        String post = Integer.parseInt(posts) == 0 ? "回复" : "回复" + posts;
                        String like = Integer.parseInt(likes) == 0 ? "点赞" : "点赞" + likes;
                        List<String> titleList=new ArrayList<>();
                        titleList.add(thread);
                        titleList.add(post);
                        titleList.add(like);
                        mIndicatorAdapter.setTitles(titleList);
                    }
            }
        });
    }

    private void initIndicator()
    {
        mViewPager.setAdapter(new SimpleFragmentStatePagerAdapter(getSupportFragmentManager(), mFragments));
        final CommonNavigator commonNavigator = new CommonNavigator(mContext);

        List<String> titles = Arrays.asList("帖子", "回复", "点赞");

        mIndicatorAdapter = new BaseTitleIndicatorAdapter(mViewPager, titles, 0x99ffffff, 0xffffffff, 14)
        {
            @Override
            public IPagerIndicator getIndicator(Context context)
            {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setColors(0xffffffff);
                indicator.setLineWidth(ConvertUtils.dp2px(context, 15));
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

    private void initFragments()
    {
        mFragments = new ArrayList<>();
        MyPostsFragment postsFragment = new MyPostsFragment();//帖子tab
        Bundle bundle=new Bundle();
        bundle.putString(ConstantUtils.POSTS_USER_ID,mUserId);
        postsFragment.setArguments(bundle);
        MyReplyFragment replyFragment = new MyReplyFragment();//回复tab
        replyFragment.setArguments(bundle);
        MyThumbsUpFragment thumbsUpFragment = new MyThumbsUpFragment();//点赞tab
        thumbsUpFragment.setArguments(bundle);
        mFragments.add(postsFragment);
        mFragments.add(replyFragment);
        mFragments.add(thumbsUpFragment);
    }

    @Override
    protected int getLayoutRes()
    {
        return R.layout.activity_personal_posts;
    }
}
