package com.cnsunrun.jiajiagou.forum;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseActivity;
import com.cnsunrun.jiajiagou.common.base.CZSwipeRefreshLayout;
import com.cnsunrun.jiajiagou.common.base.Refreshable;
import com.cnsunrun.jiajiagou.common.network.DialogCallBack;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.ConstantUtils;
import com.cnsunrun.jiajiagou.common.widget.CircleTransform;
import com.cnsunrun.jiajiagou.forum.adapter.HomePageContentPagerAdapter;
import com.cnsunrun.jiajiagou.forum.adapter.MyCommonNavigatorAdapter;
import com.cnsunrun.jiajiagou.forum.bean.HomepageUserInfoBean;
import com.cnsunrun.jiajiagou.forum.fragment.TabLikeFragment;
import com.cnsunrun.jiajiagou.forum.fragment.TabPostsFragment;
import com.google.gson.Gson;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 个人主页-帖子已发帖子选项卡
 * <p>
 * author:yyc
 * date: 2017-08-30 12:22
 */
public class PeopleHomepageActivity extends BaseActivity implements AppBarLayout
        .OnOffsetChangedListener {
    @BindView(R.id.iv_avatar_people)
    ImageView mAvatar;
    @BindView(R.id.tv_name_people)
    TextView mName;
    @BindView(R.id.tv_title_people)
    TextView mTitle;
    @BindView(R.id.tv_postsnum_people)
    TextView mLoginDate;
    @BindView(R.id.tl_indicator)
    MagicIndicator mIndicator;
    @BindView(R.id.vp_content)
    AutofitViewPager mContentVp;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.swipe_refresh_layout)
    CZSwipeRefreshLayout mRefreshLayout;
    private CommonNavigator mCommonNavigator;
    private List<String> tabIndicators;
    private List<Fragment> tabFragments;



    private String url = "https://i.imgur.com/YxjfGotb.jpg ";
//    private String mUserId;
    private HomePageContentPagerAdapter mPagerAdapter;
    private MyCommonNavigatorAdapter mCommonNavigatorAdapter;
    private String mUserId;


    @Override
    protected void init() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        mAppBarLayout.addOnOffsetChangedListener(this);
        initData();
        initView();
        initAdapter();

    }



    private void initData() {
        mUserId = getIntent().getExtras().getString(ConstantUtils.POSTS_USER_ID);
        requestNet();
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        requestNet();
    }

    private void requestNet() {
        HashMap<String,String> map=new HashMap<>();
        map.put("id",mUserId);
        HttpUtils.get(NetConstant.HOMEPAGE_USER_INFO, map, new DialogCallBack((Refreshable)PeopleHomepageActivity.this) {
            @Override
            public void onResponse(String response, int id) {
                HomepageUserInfoBean infoBean = new Gson().fromJson(response,
                        HomepageUserInfoBean.class);
                if (handleResponseCode(infoBean)) {
                    if (infoBean.getStatus()==1) {
                        initUserInfo(infoBean.getInfo());
                    }
                }

            }
        });
    }

    private void initView() {

        mCommonNavigator = new CommonNavigator(this);
        mCommonNavigator.setAdjustMode(true);
    }
    public void initUserInfo(HomepageUserInfoBean.InfoBean info){
        if (info!=null) {
            getImageLoader().load(info.getAvatar()).transform(new CenterCrop(mContext), new
                    CircleTransform(mContext)).into(mAvatar);
            mName.setText(info.getNickname());
            if (TextUtils.isEmpty(info.getSignature())) {
                mTitle.setVisibility(View.GONE);
            }else {
                mTitle.setText(info.getSignature());
            }
            String posts="帖子";
            String like="点赞";
            int threads = Integer.parseInt(info.getThreads());
            int likes = Integer.parseInt(info.getLikes());
            String threadResult = threads == 0 ? posts : posts + threads;
            String likeResult = likes == 0 ? like : like + likes;
            tabIndicators = new ArrayList();
            tabIndicators.add(threadResult);
            tabIndicators.add(likeResult);
            tabFragments = new ArrayList<>();
            TabPostsFragment tabPostsFragment = new TabPostsFragment();
            TabLikeFragment tabLikeFragment = new TabLikeFragment();
            Bundle bundle=new Bundle();
            bundle.putString(ConstantUtils.POSTS_USER_ID,mUserId);
            tabPostsFragment.setArguments(bundle);
            tabLikeFragment.setArguments(bundle);
            tabFragments.add(tabPostsFragment);
            tabFragments.add(tabLikeFragment);
            mCommonNavigatorAdapter.updateTabData(tabIndicators);
            mCommonNavigatorAdapter.notifyDataSetChanged();
            mPagerAdapter.setData(tabFragments,tabIndicators);
            mPagerAdapter.notifyDataSetChanged();
            mLoginDate.setText("注册时间 : "+info.getRegister_time()+"  |  最后访问 : "+info.getLogin_time());
        }
    }

    private void initAdapter() {
        mPagerAdapter = new HomePageContentPagerAdapter(getSupportFragmentManager());
        mContentVp.setAdapter(mPagerAdapter);
        mCommonNavigatorAdapter = new MyCommonNavigatorAdapter(mContentVp, null);
        mCommonNavigator.setAdapter(mCommonNavigatorAdapter);
        mIndicator.setNavigator(mCommonNavigator);
        ViewPagerHelper.bind(mIndicator, mContentVp);

    }
    @OnClick(R.id.iv_back_people)
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.iv_back_people:
                onBackPressedSupport();
                break;
        }
    }
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_people_homepage;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset>=0) {
            mRefreshLayout.setEnabled(true);
        }else {
            mRefreshLayout.setEnabled(false);

        }
    }
}
