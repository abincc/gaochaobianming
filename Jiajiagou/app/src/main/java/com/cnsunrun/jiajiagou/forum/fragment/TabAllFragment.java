package com.cnsunrun.jiajiagou.forum.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseFragment;
import com.cnsunrun.jiajiagou.common.base.Refreshable;
import com.cnsunrun.jiajiagou.common.network.DialogCallBack;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.ConstantUtils;
import com.cnsunrun.jiajiagou.forum.PeopleHomepageActivity;
import com.cnsunrun.jiajiagou.forum.PostsDetailActicity;
import com.cnsunrun.jiajiagou.forum.adapter.TabAllContentAdapter;
import com.cnsunrun.jiajiagou.forum.bean.PlateAllThemeBean;
import com.cnsunrun.jiajiagou.forum.entity.RecommendPostsItem;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * 论坛板块主页,精华帖子
 * <p>
 * author:yyc
 * date: 2017-08-25 11:26
 */
public class TabAllFragment extends BaseFragment {

    @BindView(R.id.recycler_tab_content)
    RecyclerView mRecyclerView;
    private List<RecommendPostsItem> posttsDatas;
    private String url = "https://i.imgur.com/YxjfGotb.jpg ";
    private String contentUrl = "https://i.imgur.com/0c1KlhO.jpg ";
    private TabAllContentAdapter mTabAllContentAdapter;

//    public static TabAllFragment newInstance(String content){
//        Bundle arguments = new Bundle();
//        arguments.putString("content", content);
//        TabAllFragment tabContentFragment = new TabAllFragment();
//        tabContentFragment.setArguments(arguments);
//        return tabContentFragment;
//    }

    @Override
    protected void init() {
        initData();
        initView();
        initAdapter();
    }

    private void initData() {
        requestNet();
    }

//    @Override
//    public void onRefresh() {
//        super.onRefresh();
//        requestNet();
//    }

    private void requestNet() {
        String plateId = getArguments().getString(ConstantUtils.FORUM_PLATE_ID);
        HashMap<String, String> map = new HashMap<>();
        map.put("id", plateId);
        //全部主题
        HttpUtils.get(NetConstant.ALL_THEMES, map, new DialogCallBack((Refreshable)TabAllFragment.this) {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                PlateAllThemeBean allThemeBean = new Gson().fromJson(response,
                        PlateAllThemeBean.class);
                if (handleResponseCode(allThemeBean)) {
                    if (allThemeBean.getStatus()==1) {
                        List<PlateAllThemeBean.InfoBean> info = allThemeBean.getInfo();
                        mTabAllContentAdapter.setNewData(info);
                        if (info.size()==0) {
//                            mTabAllContentAdapter.setEmptyView(View.inflate(mContext, R
//                                    .layout.layout_empty, null));
                            mTabAllContentAdapter.setEmptyView(getActivity().getLayoutInflater().inflate( R
                                    .layout.layout_empty_forum, (ViewGroup) mRecyclerView.getParent(),false));
                        }
                    }
                }
            }
        });
    }


    private void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setNestedScrollingEnabled(false);
    }

    private void initAdapter() {
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        mTabAllContentAdapter = new TabAllContentAdapter(R.layout
                .item_tab_content_plate);
        mTabAllContentAdapter.setImageLoader(getImageLoader());
        mTabAllContentAdapter.setWidth(width);
        mRecyclerView.setAdapter(mTabAllContentAdapter);
        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                List<PlateAllThemeBean.InfoBean> data = adapter.getData();
                Bundle bundle = new Bundle();
                bundle.putString(ConstantUtils.POSTS_USER_ID,data.get(position).getMember_id() );
                startActivity(PeopleHomepageActivity.class,false,bundle);
            }
        });
        mTabAllContentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<PlateAllThemeBean.InfoBean> data = adapter.getData();
                String id = data.get(position).getId();
                Bundle bundle = new Bundle();
                bundle.putString(ConstantUtils.POSTS_ITEM_ID, id);
                startActivity(PostsDetailActicity.class, false, bundle);
            }
        });
    }

    @Override
    protected int getChildLayoutRes() {
        return R.layout.fragment_tab_platehome;
    }
}
