package com.cnsunrun.jiajiagou.forum;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseActivity;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.ConstantUtils;
import com.cnsunrun.jiajiagou.forum.adapter.ContentPagerAdapter;
import com.cnsunrun.jiajiagou.forum.adapter.MyCommonNavigatorAdapter;
import com.cnsunrun.jiajiagou.forum.bean.PlateHomePageBean;
import com.cnsunrun.jiajiagou.forum.fragment.TabAllFragment;
import com.cnsunrun.jiajiagou.forum.fragment.TabEssenceFragment;
import com.cnsunrun.jiajiagou.personal.information.InformationActivity;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 板块主页
 * <p>
 * author:yyc
 * date: 2017-08-24 11:38
 */
public class PlateHomepageActivity extends BaseActivity {


    @BindView(R.id.iv_back_plate)
    ImageView mBackBtn;
    @BindView(R.id.tl_indicator)
//    TabLayout mIndicator;
    MagicIndicator mIndicator;
    @BindView(R.id.vp_content_plate)
    AutofitViewPager mContentVp;
    @BindView(R.id.iv_avatar_plate)
    ImageView mPlateImg;
    @BindView(R.id.tv_name_plate)
    TextView mPlateName;
    @BindView(R.id.tv_postsnum_plate)
    TextView mPlatePostsNum;
    private List<String> tabIndicators;
    private List<Fragment> tabFragments;
    private CommonNavigator mCommonNavigator;

    private String url = "https://i.imgur.com/YxjfGotb.jpg ";
    private String contentUrl = "https://i.imgur.com/0c1KlhO.jpg ";

    @Override
    protected void init() {
        initData();
        initView();
        initAdapter();
    }


    private void initView() {

        mCommonNavigator = new CommonNavigator(this);
        mCommonNavigator.setAdjustMode(true);
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        String plateId = bundle.getString(ConstantUtils.FORUM_PLATE_ID);
        TabAllFragment tabAllFragment = new TabAllFragment();
        TabEssenceFragment tabEssenceFragment = new TabEssenceFragment();
        tabAllFragment.setArguments(bundle);
        tabEssenceFragment.setArguments(bundle);
        requestNet(plateId);
        tabIndicators = new ArrayList();
        tabIndicators.add("全部");
        tabIndicators.add("精华");
        tabFragments = new ArrayList<>();
        tabFragments.add(tabAllFragment);
        tabFragments.add(tabEssenceFragment);
    }

    private void requestNet(String plateId) {
        HashMap<String, String> map = new HashMap<>();
        map.put("id", plateId);
        HttpUtils.get(NetConstant.PLATE_HOMEPAGE, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                PlateHomePageBean pageBean = new Gson().fromJson(response,
                        PlateHomePageBean.class);
                if (pageBean.getStatus() == 1) {
                    getImageLoader().load(pageBean.getInfo().getIcon()).into(mPlateImg);
                    mPlateName.setText(pageBean.getInfo().getTitle());
                    mPlatePostsNum.setText("帖子"+pageBean.getInfo().getPosts());
                }
            }
        });

    }

    private void initAdapter() {
        ContentPagerAdapter contentAdapter = new ContentPagerAdapter
                (getSupportFragmentManager(), tabFragments, tabIndicators);
        mContentVp.setAdapter(contentAdapter);
        mCommonNavigator.setAdapter(new MyCommonNavigatorAdapter(mContentVp, tabIndicators));
        mIndicator.setNavigator(mCommonNavigator);
        ViewPagerHelper.bind(mIndicator, mContentVp);
    }

    @OnClick({R.id.iv_back_plate,R.id.iv_message_plate})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_plate:
                onBackPressedSupport();
                break;
            case R.id.iv_message_plate:
                startActivity(InformationActivity.class);
                break;
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_plate_homepage;
    }

}
