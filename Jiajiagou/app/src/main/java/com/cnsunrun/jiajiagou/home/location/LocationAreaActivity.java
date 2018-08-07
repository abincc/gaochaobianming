package com.cnsunrun.jiajiagou.home.location;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseActivity;
import com.cnsunrun.jiajiagou.common.constant.SPConstant;
import com.cnsunrun.jiajiagou.common.util.ConstantUtils;
import com.cnsunrun.jiajiagou.common.util.RecyclerLineDecoration;
import com.cnsunrun.jiajiagou.common.util.SPUtils;
import com.cnsunrun.jiajiagou.home.adapter.EndlessRecyclerOnScrollListener;
import com.cnsunrun.jiajiagou.map.CloudSearchTask;
import com.cnsunrun.jiajiagou.map.GDLocationUtil;
import com.cnsunrun.jiajiagou.map.bean.CloudResultBean;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH;

/**
 * Description:
 * Data：2017/8/23 0023-下午 5:59
 * Blog：http://blog.csdn.net/u013983934
 * Author: CZ
 */
public class LocationAreaActivity extends BaseActivity implements BaseQuickAdapter
        .OnItemClickListener, RadioGroup.OnCheckedChangeListener, GDLocationUtil
        .MyLocationListener {
    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.rg_area)
    RadioGroup mRgArea;
    @BindView(R.id.tv_area_nearby)
    TextView mTvAreaNearby;
    @BindView(R.id.recycler_all)
    RecyclerView mAllRecycler;
    @BindView(R.id.recycler_search_result)
    RecyclerView mSearchRecycler;
    @BindView(R.id.iv_select)
    ImageView mSelectIv;
    private LocationAreaAdapter mAllCommunityAdapter;
    private static final String mTableID = "5a015576afdf521e86dfba87";
    private double lat = 30.505346;//光谷坐标   测试用
    private double lng = 114.400328;//光谷坐标
    private SearchCommunityAdapter mSearchCommunityAdapter;
    private int mRequestCode;
    private List<Integer> mRequestCodeList;
    private int page=1;

    @Override
    protected void init() {
        mRequestCodeList = Arrays.asList(ConstantUtils.POST_PROPERTY_SERVICE_ACT, ConstantUtils.SHOP_ACT);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mRequestCode = bundle.getInt(ConstantUtils.TO_COMMUNITY_SELECT, 0);
        }
        mAllRecycler.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager
                .VERTICAL, false));
        mAllRecycler.addItemDecoration(new RecyclerLineDecoration(mContext));
        mAllCommunityAdapter = new LocationAreaAdapter(R.layout.item_location_area, null);
        mAllRecycler.setAdapter(mAllCommunityAdapter);

        mSearchRecycler.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager
                .VERTICAL, false));
        mSearchRecycler.addItemDecoration(new RecyclerLineDecoration(mContext));
        mSearchCommunityAdapter = new SearchCommunityAdapter(R.layout.item_location_area);
        mSearchRecycler.setAdapter(mSearchCommunityAdapter);

        ((RadioButton) mRgArea.getChildAt(0)).setChecked(true);
        GDLocationUtil.getLocation(this);
        mRgArea.setOnCheckedChangeListener(this);
        mAllCommunityAdapter.setOnItemClickListener(this);
        mSearchCommunityAdapter.setOnItemClickListener(this);
//        mEtSearch.addTextChangedListener(this);
        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(final TextView v, int actionId, KeyEvent event) {
                if (actionId == IME_ACTION_SEARCH) {
                    ((RadioButton) mRgArea.getChildAt(0)).setChecked(true);
                    mSearchRecycler.setVisibility(View.VISIBLE);
                    mAllRecycler.setVisibility(View.GONE);
                    GDLocationUtil.getLocation(new GDLocationUtil.MyLocationListener() {
                        @Override
                        public void result(AMapLocation location) {
                            CloudSearchTask.getInstance(mContext).setAdapter(mSearchCommunityAdapter)
                                    .onSearch(mTableID, v.getText().toString(), true, location.getLatitude(), location.getLongitude());
                        }
                    });
                }
                return false;
            }
        });
        String title = SPUtils.getString(mContext, SPConstant.DISTRICT_TITLE);
        if (!TextUtils.isEmpty(title)){
            mTvAreaNearby.setText(title);
        }else {
            GDLocationUtil.getLocation(new GDLocationUtil.MyLocationListener() {
                @Override
                public void result(AMapLocation location) {
                    CloudSearchTask.getInstance(mContext).setRecommendCommunity(mTvAreaNearby)
                            .setTextViewFlag(true)
                            .onSearch(mTableID, "", false, location.getLatitude(), location.getLongitude());
                }
            });
        }

        mSearchRecycler.addOnScrollListener(new EndlessRecyclerOnScrollListener((LinearLayoutManager) mSearchRecycler.getLayoutManager()) {
            @Override
            public void onLoadMore(int currentPage) {
                page++;
                loadAllCommunity();
            }
        });
    }


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_location_area;
    }

    @OnClick({R.id.iv_back, R.id.tv_enter, R.id.tv_area_nearby, R.id.rl_recommend})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressedSupport();
                break;
            case R.id.tv_enter:
                if (mSelectIv.isShown() && !TextUtils.isEmpty(mTvAreaNearby.getText())) {
                    fromRecommendGetCommunity();
                } else if (!mSelectIv.isShown()&&fromSearchOrAllGetCommunity()==null){
                    showToast("请勾选小区");
                }else {
                    fromSearchOrAllGetCommunity();
                }
                break;
            case R.id.rl_recommend:
//                if (!TextUtils.isEmpty(mTvAreaNearby.getText().toString())) {
//                    getCommunity();
//                }
                mSelectIv.setVisibility(View.VISIBLE);
                clearAdapterSelect();
                break;
        }
    }

    //从位置推荐获取小区
    private void fromRecommendGetCommunity() {
        List<CloudResultBean> data = mAllCommunityAdapter.getData();
        if (data.size() > 0 && mRequestCode != 0) {
            Intent intent = new Intent();
            String id = data.get(0).getId();
            String title = data.get(0).getTitle();
            intent.putExtra(ConstantUtils.COMMUNITY_ID, id);
            intent.putExtra(ConstantUtils.COMMUNITY_TITLE, title);
            SPUtils.put(mContext, SPConstant.DISTRICT_ID,id);
            SPUtils.put(mContext, SPConstant.DISTRICT_TITLE,title);
            LocationAreaActivity.this.setResult(RESULT_OK, intent);
            LocationAreaActivity.this.finish();
        }
    }

    //从搜索或全部获取小区
    private CloudResultBean fromSearchOrAllGetCommunity() {
        CloudResultBean resultBean = null;
        if (mRequestCode != 0) {
            if (mAllRecycler.isShown()) {
                List<CloudResultBean> allData = mAllCommunityAdapter.getData();
                for (CloudResultBean bean : allData) {
                    if (bean.isSelect()) {
                        resultBean = bean;
                    }
                }
            } else if (mSearchRecycler.isShown()) {
                List<CloudResultBean> allData = mSearchCommunityAdapter.getData();
                for (CloudResultBean bean : allData) {
                    if (bean.isSelect()) {
                        resultBean = bean;
                    }
                }
            }
        }
        if (resultBean!=null) {
            Intent intent = new Intent();
            intent.putExtra(ConstantUtils.COMMUNITY_ID, resultBean.getId());
            intent.putExtra(ConstantUtils.COMMUNITY_TITLE, resultBean.getTitle());
            SPUtils.put(mContext, SPConstant.DISTRICT_ID,resultBean.getId());
            SPUtils.put(mContext, SPConstant.DISTRICT_TITLE,resultBean.getTitle());
            LocationAreaActivity.this.setResult(RESULT_OK, intent);
            LocationAreaActivity.this.finish();
        }
        return resultBean;
    }

    private void clearAdapterSelect() {
        List<CloudResultBean> AllData = mAllCommunityAdapter.getData();
        List<CloudResultBean> searchData = mSearchCommunityAdapter.getData();
        for (CloudResultBean bean : AllData) {
            bean.setSelect(false);
        }
        for (CloudResultBean bean : searchData) {
            bean.setSelect(false);
        }
        mAllCommunityAdapter.setNewData(AllData);
        mSearchCommunityAdapter.setNewData(searchData);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        List<CloudResultBean> data = adapter.getData();
//        mTvAreaNearby.setText(data.get(position).getTitle());
//            if (mRequestCode != 0) {
//                Intent intent = new Intent();
//                intent.putExtra(ConstantUtils.COMMUNITY_ID, data.get(position).getId());
//                intent.putExtra(ConstantUtils.COMMUNITY_TITLE, data.get(position).getTitle());
//                LocationAreaActivity.this.setResult(RESULT_OK, intent);
//                LocationAreaActivity.this.finish();
//            }

        if (mAllRecycler.isShown()) {//全部
            mSelectIv.setVisibility(View.GONE);
            for (CloudResultBean resultBean : data) {
                resultBean.setSelect(false);
            }
            data.get(position).setSelect(true);
            mAllCommunityAdapter.setNewData(data);
        }
        if (mSearchRecycler.isShown()) {//搜索
            mSelectIv.setVisibility(View.GONE);
            for (CloudResultBean resultBean : data) {
                resultBean.setSelect(false);
            }
            data.get(position).setSelect(true);
            mSearchCommunityAdapter.setNewData(data);

        }
    }

//    @Override
//    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//    }
//
//    @Override
//    public void onTextChanged(final CharSequence s, int start, int before, int count) {
//        if (s.length() > 0) {
//            ((RadioButton) mRgArea.getChildAt(0)).setChecked(true);
//            mSearchRecycler.setVisibility(View.VISIBLE);
//            mAllRecycler.setVisibility(View.GONE);
//            GDLocationUtil.getLocation(new GDLocationUtil.MyLocationListener() {
//                @Override
//                public void result(AMapLocation location) {
//                    CloudSearchTask.getInstance(mContext).setAdapter(mSearchCommunityAdapter)
//                            .onSearch(mTableID, s.toString(), true, location.getLatitude(), location.getLongitude());
//                }
//            });
//        } else {
////            mSearchRecycler.setVisibility(View.GONE);
////            mSearchCommunityAdapter.getData().clear();
////            mSearchCommunityAdapter.notifyDataSetChanged();
//        }
//    }
//
//    @Override
//    public void afterTextChanged(Editable s) {
//
//    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.rb_search_area:
                mSearchRecycler.setVisibility(View.VISIBLE);
                mAllRecycler.setVisibility(View.GONE);
//                mAllRecycler.setAdapter(mSearchCommunityAdapter);
                //将全部小区tab勾选状态取消
                List<CloudResultBean> allData = mAllCommunityAdapter.getData();
                for (CloudResultBean bean : allData) {
                    bean.setSelect(false);
                }
                mAllCommunityAdapter.setNewData(allData);
                break;
            case R.id.rb_all_area:
                mSearchRecycler.setVisibility(View.GONE);
                mAllRecycler.setVisibility(View.VISIBLE);
//                mAllRecycler.setAdapter(mAllCommunityAdapter);
                //将搜索小区tab勾选状态取消
                List<CloudResultBean> searchData = mSearchCommunityAdapter.getData();
                for (CloudResultBean bean : searchData) {
                    bean.setSelect(false);
                }
                mSearchCommunityAdapter.setNewData(searchData);
                break;
        }
    }

    // 获取之前定位位置，如果之前未曾定位，则重新定位
    @Override
    public void result(AMapLocation location) {
        loadAllCommunity();
    }

    private void loadAllCommunity() {
        CloudSearchTask.getInstance(mContext)
                .setAdapter(mAllCommunityAdapter)
                .setAdapter(mSearchCommunityAdapter)
                .onSearchCity(mTableID, true,"","全国",page);
    }

}
