package com.cnsunrun.jiajiagou.forum.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseFragment;
import com.cnsunrun.jiajiagou.common.base.Refreshable;
import com.cnsunrun.jiajiagou.common.network.DialogCallBack;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.ConstantUtils;
import com.cnsunrun.jiajiagou.common.util.DateUtils;
import com.cnsunrun.jiajiagou.forum.PostsDetailActicity;
import com.cnsunrun.jiajiagou.forum.adapter.TabEssenceContentAdapter;
import com.cnsunrun.jiajiagou.forum.bean.PlateEssenceThemeBean;
import com.cnsunrun.jiajiagou.forum.entity.RecommendPostsItem;
import com.google.gson.Gson;

import java.util.ArrayList;
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
public class TabEssenceFragment extends BaseFragment {

    @BindView(R.id.recycler_tab_content)
    RecyclerView mRecyclerView;
    private List<RecommendPostsItem> posttsDatas;
    private String url = "https://i.imgur.com/YxjfGotb.jpg ";
    private String contentUrl = "https://i.imgur.com/0c1KlhO.jpg ";
    private TabEssenceContentAdapter mTabEssenceContentAdapter;

    public static TabEssenceFragment newInstance(String content) {
        Bundle arguments = new Bundle();
        arguments.putString("content", content);
        TabEssenceFragment tabContentFragment = new TabEssenceFragment();
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
        requestNet();
        posttsDatas = new ArrayList();
        String content = "测试测试测试测试测试测试测试测试测试测试测试测试";
        for (int i = 1; i < 6; i++) {
            RecommendPostsItem item = new RecommendPostsItem();
            item.setAvatarImg(url);
            item.setName("陌上出黛" + i);
            item.setTitle(i + "个小心机,让宝宝爱上安全座椅");
            item.setContent(content + content + content + content);
            item.setContentImg(contentUrl);
            item.setReadNum(100 + i);
            item.setCommentNum(20 + i);
            item.setLikeNum(40 + i);
            item.setDate(DateUtils.getCurrentTime("yyyy-MM-dd"));
            posttsDatas.add(item);
        }
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
        HttpUtils.get(NetConstant.ESSENCE_THEMES, map, new DialogCallBack((Refreshable)
                TabEssenceFragment.this) {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                PlateEssenceThemeBean essenceThemeBean = new Gson().fromJson(response,
                        PlateEssenceThemeBean.class);
                if (handleResponseCode(essenceThemeBean)) {

                    if (essenceThemeBean.getStatus() == 1) {
                        List<PlateEssenceThemeBean.InfoBean> info = essenceThemeBean.getInfo();
                        mTabEssenceContentAdapter.setNewData(info);
                        if (info.size() == 0) {
//                            mTabEssenceContentAdapter.setEmptyView(View.inflate(mContext, R
//                                    .layout.layout_empty, null));
                            mTabEssenceContentAdapter.setEmptyView(getActivity().getLayoutInflater().inflate( R
                                    .layout.layout_empty_forum, (ViewGroup) mRecyclerView.getParent(),false));

                        }
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
        mTabEssenceContentAdapter = new TabEssenceContentAdapter(R.layout
                .item_tab_content_plate);
        mTabEssenceContentAdapter.setImageLoader(getImageLoader());
        mRecyclerView.setAdapter(mTabEssenceContentAdapter);
        mTabEssenceContentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener
                () {

            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<PlateEssenceThemeBean.InfoBean> data = adapter.getData();
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
