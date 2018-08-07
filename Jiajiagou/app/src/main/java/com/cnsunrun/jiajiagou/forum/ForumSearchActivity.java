package com.cnsunrun.jiajiagou.forum;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseActivity;
import com.cnsunrun.jiajiagou.common.base.Refreshable;
import com.cnsunrun.jiajiagou.common.network.DialogCallBack;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.ConstantUtils;
import com.cnsunrun.jiajiagou.forum.adapter.ForumSearchAdapter;
import com.cnsunrun.jiajiagou.forum.adapter.PlateSearchAdapter;
import com.cnsunrun.jiajiagou.forum.adapter.SearchPromptAdapter;
import com.cnsunrun.jiajiagou.forum.bean.ForumSearchBean;
import com.cnsunrun.jiajiagou.forum.entity.PlateItem;
import com.cnsunrun.jiajiagou.forum.entity.RecommendPostsItem;
import com.cnsunrun.jiajiagou.personal.information.InformationActivity;
import com.cnsunrun.jiajiagou.product.MyItemDecoration;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 论坛搜索
 * <p>
 * author:yyc
 * date: 2017-08-24 14:30
 */
public class ForumSearchActivity extends BaseActivity implements TextWatcher {
    @BindView(R.id.recycler_forum_plate)
    RecyclerView recyclerPlate;
    @BindView(R.id.recycler_forum_search_result)
    RecyclerView recyclerSearchResult;
    @BindView(R.id.et_search)
    EditText editSearch;
    @BindView(R.id.ll_root_layout)
    LinearLayout rootLayout;
//    @BindView(R.id.recycle_input_prompt)
//    RecyclerView inputPromptRecycle;//搜索输入提示框
//    @BindView(R.id.ll_input_layout)
//    LinearLayout inputLayout;
    private List<PlateItem> plateDatas;
    private List<RecommendPostsItem> resultDatas;
    private String url = "https://i.imgur.com/YxjfGotb.jpg ";
    private String contentUrl = "https://i.imgur.com/0c1KlhO.jpg ";
    private PlateSearchAdapter mPlateSearchAdapter;
    private ForumSearchAdapter mForumSearchAdapter;
    private SearchPromptAdapter mSearchPromptAdapter;

    @Override
    protected void init() {
//        setWindowBackground(R.color.white);
//        test();
        initData();
        initView();
        initAdapter();
    }

    private void initData() {
        requestNet();
//        testData();
    }


    private void initView() {
        recyclerPlate.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerSearchResult.setNestedScrollingEnabled(false);
        recyclerSearchResult.setLayoutManager(new LinearLayoutManager(this));
        editSearch.addTextChangedListener(this);
//        inputPromptRecycle.setLayoutManager(new LinearLayoutManager(this));
//        inputPromptRecycle.addItemDecoration(new RecyclerLineDecoration(this));
        mSearchPromptAdapter = new SearchPromptAdapter(R.layout.item_forum_search);
        mSearchPromptAdapter.setImageLoader(getImageLoader());
//        inputPromptRecycle.setAdapter(mSearchPromptAdapter);
        mSearchPromptAdapter.setEmptyView(View.inflate(mContext,R.layout.layout_empty,null));

    }

    private void initAdapter() {
        mPlateSearchAdapter = new PlateSearchAdapter(R.layout.item_plate);
//        mPlateSearchAdapter.setEmptyView(View.inflate(mContext,R.layout.layout_empty,null));
        mPlateSearchAdapter.setImageLoader(getImageLoader());
        recyclerPlate.setAdapter(mPlateSearchAdapter);
//        recyclerPlate.addItemDecoration(new GridSpacingItemDecoration(4,50,false));
//        recyclerPlate.addItemDecoration(new RecyclerViewGridDivider(4,50,20));
        mForumSearchAdapter = new ForumSearchAdapter(R.layout.item_forum_search);
        mForumSearchAdapter.setImageLoader(getImageLoader());
        recyclerSearchResult.setAdapter(mForumSearchAdapter);
        recyclerSearchResult.addItemDecoration(new MyItemDecoration(mContext,R.color.grayF4,R.dimen.dp_10));
        mPlateSearchAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<ForumSearchBean.InfoBean.ForumListBean> data = adapter.getData();
                String id = data.get(position).getId();
                Bundle bundle = new Bundle();
                bundle.putString(ConstantUtils.FORUM_PLATE_ID, id);
                startActivity(PlateHomepageActivity.class, false, bundle);
            }
        });

        mForumSearchAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<ForumSearchBean.InfoBean.ThreadListBean> data = adapter.getData();
                String id = data.get(position).getId();
                Bundle bundle = new Bundle();
                bundle.putString(ConstantUtils.POSTS_ITEM_ID, id);
                startActivity(PostsDetailActicity.class, false, bundle);
            }
        });
        mSearchPromptAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<ForumSearchBean.InfoBean.ThreadListBean> data = adapter.getData();
                String id = data.get(position).getId();
                Bundle bundle = new Bundle();
                bundle.putString(ConstantUtils.POSTS_ITEM_ID, id);
                startActivity(PostsDetailActicity.class, false, bundle);
            }
        });
    }



    private void requestNet() {
        HashMap<String, String> map = new HashMap();
        map.put("keywords", "");
        HttpUtils.get(NetConstant.FORUM_SEARCH, map, new DialogCallBack((Refreshable)
                ForumSearchActivity.this) {

            @Override
            public void onResponse(String response, int id) {
                ForumSearchBean searchBean = new Gson().fromJson(response, ForumSearchBean
                        .class);
                if (handleResponseCode(searchBean)) {
                    if (searchBean.getStatus() == 1) {
                        List<ForumSearchBean.InfoBean.ForumListBean> forumListBean = searchBean
                                .getInfo
                                ().getForum_list();
                        List<ForumSearchBean.InfoBean.ThreadListBean> threadList = searchBean
                                .getInfo().getThread_list();
                            mForumSearchAdapter.setNewData(threadList);
                        if (threadList.size()==0)
                            mForumSearchAdapter.setEmptyView(View.inflate(mContext,R.layout.layout_empty,null));
                        if (forumListBean.size() > 0) {
                            List<ForumSearchBean.InfoBean.ForumListBean> list = new ArrayList();
                            for (int i = 0; i < forumListBean.size(); i++) {
                                if (i < 4) {
                                    list.add(forumListBean.get(i));
                                }
                            }
                            mPlateSearchAdapter.setNewData(list);
                        }
                    }
                }
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.iv_news})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressedSupport();
                break;
            case R.id.iv_news:
                startActivity(InformationActivity.class);
                break;
        }
    }


    @Override
    public void onRefresh() {
        super.onRefresh();
        requestNet();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        return imm.hideSoftInputFromWindow(this.editSearch.getWindowToken(), 0);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_forum_search;
    }

    //点击其他地方隐藏输入法
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context
                        .INPUT_METHOD_SERVICE);
                if (imm != null) {
                    rootLayout.requestFocus();
                    imm.hideSoftInputFromWindow(editSearch.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        } // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0,
                    0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY()
                    < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, final int before, int count) {
        if (s.length() > 0) {
            recyclerPlate.setVisibility(View.GONE);
            recyclerSearchResult.setAdapter(mSearchPromptAdapter);
            HashMap<String, String> map = new HashMap();
            map.put("keywords", s.toString());
            HttpUtils.get(NetConstant.FORUM_SEARCH, map, new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {

                }

                @Override
                public void onResponse(String response, int id) {
                    ForumSearchBean searchBean = new Gson().fromJson(response, ForumSearchBean
                            .class);
                    if (searchBean.getStatus()==1) {
                        List<ForumSearchBean.InfoBean.ThreadListBean> list = searchBean
                                .getInfo().getThread_list();
                            mSearchPromptAdapter.setNewData(list);
                        if (list.size()==0){
                            mSearchPromptAdapter.setEmptyView(View.inflate(mContext,R.layout.layout_empty,null));
                        }
                    }
                }
            });
        }else {
            recyclerPlate.setVisibility(View.VISIBLE);
            recyclerSearchResult.setAdapter(mForumSearchAdapter);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}

