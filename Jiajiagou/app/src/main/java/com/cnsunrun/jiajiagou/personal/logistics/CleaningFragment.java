package com.cnsunrun.jiajiagou.personal.logistics;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.utils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseCallBack;
import com.cnsunrun.jiajiagou.common.base.BaseFragment;
import com.cnsunrun.jiajiagou.common.base.BaseResp;
import com.cnsunrun.jiajiagou.common.base.Refreshable;
import com.cnsunrun.jiajiagou.common.constant.JjgConstant;
import com.cnsunrun.jiajiagou.common.network.DialogCallBack;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.RecyclerLineDecoration;
import com.cnsunrun.jiajiagou.personal.CleaningEvent;
import com.google.gson.Gson;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * Created by ${LiuDi}
 * on 2017/8/22on 11:01.
 * 保洁维修
 */

public class CleaningFragment extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener
{
    @BindView(R.id.recycle)
    RecyclerView mRecycle;
    private CleaningAdapter mAdapter;
    private int pageNumber = 1;

    @Override
    protected void init()
    {
        initRecycles();
        getData(pageNumber);
    }

    @Override
    public void onRefresh()
    {
        super.onRefresh();
        pageNumber = 1;
        getData(pageNumber);
    }

    private void getData(final int page)
    {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", JjgConstant.getToken(mContext));
        hashMap.put("p", String.valueOf(page));
        HttpUtils.get(NetConstant.CLEAN_LIST, hashMap, new DialogCallBack((Refreshable) CleaningFragment.this)
        {
            @Override
            public void onResponse(String response, int id)
            {
                LogUtils.d(response);
                CleaningResp resp = new Gson().fromJson(response, CleaningResp.class);
                if (handleResponseCode(resp))
                {
                    List<CleanBean> info = resp.getInfo();
                    if (page == 1)
                    {
                        mAdapter.setNewData(info);
                    } else
                        {
                            if (info != null && info.size() > 0) {
                                mAdapter.addData(info);
                                pageNumber++;
                        }
                        else
                         {
//                            mAdapter.loadMoreComplete();
                             mAdapter.loadMoreEnd();
                         }
                    }
                    mAdapter.checkFullPage(mRecycle);
                }
            }
        });


    }

    private void initRecycles()
    {
        mRecycle.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        mAdapter = new CleaningAdapter(R.layout.item_little_help, null);
        mAdapter.setImageLoader(getImageLoader());
        mAdapter.setEmptyView(View.inflate(mContext, R.layout.layout_empty, null));
        mAdapter.setOnLoadMoreListener(this);
        mRecycle.setAdapter(mAdapter);
        mRecycle.addItemDecoration(new RecyclerLineDecoration(mContext));
        mRecycle.addOnItemTouchListener(new OnItemClickListener()
        {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position)
            {
                List<CleanBean> data = adapter.getData();
                Intent intent = new Intent(mContext, PropertyDetailActivityV2.class);
                intent.putExtra("clean_id", data.get(position).getClean_id());
                startActivity(intent);
//                startActivityForResult(intent, 200);
            }
        });
        mRecycle.addOnItemTouchListener(new OnItemChildClickListener()
        {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, final int position)
            {
                List<CleanBean> data = adapter.getData();
                String clean_id = data.get(position).getClean_id();
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("token", JjgConstant.getToken(mContext));
                hashMap.put("clean_id", clean_id);
                HttpUtils.post(NetConstant.CLEAN_CANCLEV2, hashMap, new BaseCallBack(mContext)
                {
                    @Override
                    public void onResponse(String response, int id)
                    {
                        LogUtils.d(response);
                        BaseResp baseResp = new Gson().fromJson(response, BaseResp.class);
                        if (handleResponseCode(baseResp))
                        {
                            showToast(baseResp.getMsg(), 1);
                            mAdapter.remove(position);
                        }

                    }
                });


            }
        });

    }
    @Override
    protected int getChildLayoutRes()
    {
        return R.layout.frgament_little_help;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200)
        {
//            onRefresh();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRreshEvent(CleaningEvent event)
    {
        onRefresh();
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
}
