package com.cnsunrun.jiajiagou.forum.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseFragment;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.ConstantUtils;
import com.cnsunrun.jiajiagou.forum.PostsDetailActicity;
import com.cnsunrun.jiajiagou.forum.adapter.TabPostsContentAdapter;
import com.cnsunrun.jiajiagou.forum.bean.TabPostsBean;
import com.cnsunrun.jiajiagou.forum.entity.RecommendPostsItem;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-08-30 13:05
 */
public class TabPostsFragment extends BaseFragment {
    @BindView(R.id.recycler_tab_homepage)
    RecyclerView mRecyclerView;
    private String url = "https://i.imgur.com/YxjfGotb.jpg ";
    private List<RecommendPostsItem> posttsDatas;
    private TabPostsContentAdapter mTabPostsContentAdapter;


    public static TabPostsFragment newInstance(String content){
        Bundle arguments = new Bundle();
        arguments.putString("content", content);
        TabPostsFragment tabContentFragment = new TabPostsFragment();
        tabContentFragment.setArguments(arguments);
        return tabContentFragment;
    }
    @Override
    protected void init() {
        initData();
        initView();
        initAdapter();
    }

    private void initData() {

        String userId = getArguments().getString(ConstantUtils.POSTS_USER_ID);
        HashMap<String,String> map=new HashMap<>();
        map.put("id",userId);
        HttpUtils.get(NetConstant.HOMEPAGE_POSTS, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                TabPostsBean tabPostsBean = new Gson().fromJson(response, TabPostsBean.class);
                if (tabPostsBean.getStatus()==1) {
                    List<TabPostsBean.InfoBean> info = tabPostsBean.getInfo();
                    mTabPostsContentAdapter.setNewData(info);
                    if (info.size()==0) {
//                        mTabPostsContentAdapter.setEmptyView(View.inflate(mContext,R.layout.layout_empty,null));
                        mTabPostsContentAdapter.setEmptyView(getActivity().getLayoutInflater().inflate( R
                                .layout.layout_empty_forum, (ViewGroup) mRecyclerView.getParent(),false));
                    }
                }
            }
        });
    }

    private void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    private void initAdapter() {
        mRecyclerView.setNestedScrollingEnabled(false);
        mTabPostsContentAdapter = new TabPostsContentAdapter(R.layout.item_tab_content_homepage);
        mTabPostsContentAdapter.setImageLoader(getImageLoader());
        mRecyclerView.setAdapter(mTabPostsContentAdapter);
        mTabPostsContentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<TabPostsBean.InfoBean> data = adapter.getData();
                String id = data.get(position).getId();
                Bundle bundle = new Bundle();
                bundle.putString(ConstantUtils.POSTS_ITEM_ID, id);
                startActivity(PostsDetailActicity.class, false, bundle);
            }
        });
    }

    @Override
    protected int getChildLayoutRes() {
        return R.layout.fragment_tab_homepage;
    }
}
