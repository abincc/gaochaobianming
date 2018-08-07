package com.cnsunrun.jiajiagou.personal.chat.list;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseHeaderActivity;
import com.cnsunrun.jiajiagou.common.base.Refreshable;
import com.cnsunrun.jiajiagou.common.constant.JjgConstant;
import com.cnsunrun.jiajiagou.common.network.DialogCallBack;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.RecyclerLineDecoration;
import com.cnsunrun.jiajiagou.personal.chat.ChatActivity;
import com.cnsunrun.jiajiagou.personal.chat.RBean;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by ${LiuDi}
 * on 2017/8/24on 14:27.
 */

public class MyChatListActivity extends BaseHeaderActivity implements BaseQuickAdapter.RequestLoadMoreListener
{
    @BindView(R.id.recycle)
    RecyclerView mRecycle;
    @BindView(R.id.tv_right)
    TextView mTvRight;
    private MyChatListAdapter mAdapter;
    private int pageNumber = 1;

    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight)
    {
        tvTitle.setText("聊天记录");
        tvRight.setVisibility(View.GONE);
    }

    @Override
    protected void init()
    {
        initRecycles();


    }

    @Override
    protected void onResume()
    {
        super.onResume();
        getData(pageNumber);
    }

    private void getData(final int page)
    {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", JjgConstant.getToken(mContext));
        hashMap.put("p", String.valueOf(page));
        HttpUtils.get(NetConstant.ROOM_LIST, hashMap, new DialogCallBack((Refreshable) MyChatListActivity
                .this)
        {
            @Override
            public void onResponse(String response, int id)
            {
                LogUtils.d(response);
                MyChatListResp resp = new Gson().fromJson(response, MyChatListResp.class);
                if (handleResponseCode(resp))
                {
                    List<MyChatListResp.InfoBean> data = resp.getInfo();
                    if (page == 1)
                    {
                        mAdapter.setNewData(data);
                    } else
                    {
                        if (data != null && data.size() != 0)
                        {
                            mAdapter.addData(data);
                            mAdapter.loadMoreComplete();
                            pageNumber++;
                        } else
                        {
                            mAdapter.loadMoreEnd();
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
        pageNumber = 1;
        getData(pageNumber);
    }

    private void initRecycles()
    {
        mRecycle.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mAdapter = new MyChatListAdapter(R.layout.item_room, null);
        mAdapter.setImageLoader(getImageLoader());
        View inflate = View.inflate(mContext, R.layout.layout_empty, null);
        TextView tvEmpty = (TextView) inflate.findViewById(R.id.tv_empty);
        tvEmpty.setText("暂无聊天");
        mAdapter.setEmptyView(inflate);
        mRecycle.addItemDecoration(new RecyclerLineDecoration(mContext));
//        mAdapter.setOnLoadMoreListener(this, mRecycle);
        mRecycle.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                List<MyChatListResp.InfoBean> data = adapter.getData();

                if (view.getId() == R.id.room) {
                    getRoom(String.valueOf(data.get(position).getMember().getId()));
                }
            }
        });

    }

    @Override
    protected int getLayoutRes()
    {
        return R.layout.activity_my_room;
    }

    @OnClick({R.id.tv_right})
    public void onViewClicked(View view)
    {
        switch (view.getId())
        {

        }
    }

    @Override
    public void onLoadMoreRequested()
    {

        mRecycle.post(new Runnable()
        {
            @Override
            public void run()
            {
                getData(pageNumber + 1);
            }
        });
    }

    public void getRoom(String oid){
        HashMap<String, String> map = new HashMap();
        map.put("oid", oid);
        map.put("token", JjgConstant.getToken(mContext));
        HttpUtils.get(NetConstant.CREATE_GET_ROOM, map, new DialogCallBack((Refreshable) MyChatListActivity.this) {
            @Override
            public void onError(Call call, Exception e, int id) {

            }
            @Override
            public void onResponse(String response, int id) {
                RBean rBean = new Gson().fromJson(response, RBean.class);
                if (handleResponseCode(rBean)) {
                    if (rBean.getStatus() == 1) {
                        //进入聊天界面
                        Intent intent = new Intent(MyChatListActivity.this,ChatActivity.class);
                        intent.putExtra("rid", rBean.getInfo().getId());
                        startActivity(intent);
                    }
                }
            }
        });
    }
}
