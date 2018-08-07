package com.cnsunrun.jiajiagou.personal.posts;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseFragment;
import com.cnsunrun.jiajiagou.common.base.Refreshable;
import com.cnsunrun.jiajiagou.common.network.DialogCallBack;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.ConstantUtils;
import com.cnsunrun.jiajiagou.common.util.RecyclerLineDecoration;
import com.cnsunrun.jiajiagou.forum.PostsDetailActicity;
import com.cnsunrun.jiajiagou.forum.bean.TabReplyBean;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Description:
 * Data：2017/8/22 0022-上午 11:38
 * Blog：http://blog.csdn.net/u013983934
 * Author: CZ
 */
public class MyReplyFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    private MyReplyAdapter mAdapter;

    @Override
    protected void init()
    {
        requestNet();

        mRecycler.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecycler.addItemDecoration(new RecyclerLineDecoration(mContext));
        mRecycler.setBackgroundResource(R.color.white);
        mAdapter = new MyReplyAdapter(R.layout.item_my_reply,null);
        mRecycler.setAdapter(mAdapter);
        mAdapter.setEmptyView(View.inflate(mContext,R.layout.layout_empty,null));
        mAdapter.setOnItemClickListener(this);
    }

    private void requestNet() {
        String userId = getArguments().getString(ConstantUtils.POSTS_USER_ID);
        HashMap<String, String> map = new HashMap<>();
        map.put("id", userId);
        HttpUtils.get(NetConstant.HOMEPAGE_REPLY, map, new DialogCallBack((Refreshable)
                MyReplyFragment.this) {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                TabReplyBean tabReplyBean = new Gson().fromJson(response, TabReplyBean.class);
                if (handleResponseCode(tabReplyBean)) {
                    if (tabReplyBean.getStatus() == 1) {
                        List<TabReplyBean.InfoBean> info = tabReplyBean.getInfo();
                        mAdapter.setNewData(info);
                        if (info.size()==0) {
                        }
                    }
                }


            }
        });
    }

    @Override
    public void onRefresh()
    {
        super.onRefresh();
        requestNet();
    }

    @Override
    protected int getChildLayoutRes()
    {
        return R.layout.layout_swipe_recycler;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        List<TabReplyBean.InfoBean> data = adapter.getData();
        String id = data.get(position).getThread_id();
        Bundle bundle = new Bundle();
        bundle.putString(ConstantUtils.POSTS_ITEM_ID, id);
        startActivity(PostsDetailActicity.class, false, bundle);
    }
}
