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
import com.cnsunrun.jiajiagou.personal.LittleEvent;
import com.google.gson.Gson;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * Created by ${LiuDi}
 * on 2017/8/22on 11:03.
 * 小事帮忙
 */

public class LittleHelpFragment extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener
{
    @BindView(R.id.recycle)
    RecyclerView mRecycle;
    private LittleHelpAdapter mAdapter;
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
        HttpUtils.get(NetConstant.LITTLEHELP, hashMap, new DialogCallBack((Refreshable) LittleHelpFragment.this)
        {
            @Override
            public void onResponse(String response, int id)
            {
                LogUtils.d(response);
                LittleHelpResp littleHelpResp = new Gson().fromJson(response, LittleHelpResp.class);
                if (handleResponseCode(littleHelpResp))
                {
                    List<HelpBean> info = littleHelpResp.getInfo();
                    if (page == 1)
                    {
                        mAdapter.setNewData(info);
                    } else
                    {
                        if (info != null && info.size() > 0)
                        {
                            mAdapter.addData(info);
                            pageNumber++;
                        } else
                        {
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

        mAdapter = new LittleHelpAdapter(R.layout.item_little_help, null);
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
                List<HelpBean> data = adapter.getData();
                Intent intent = new Intent(mContext, PropertyDetailActivity.class);
                intent.putExtra("trifle_id", data.get(position).trifle_id);
                startActivityForResult(intent, 200);
            }
        });
        mRecycle.addOnItemTouchListener(new OnItemChildClickListener()
        {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, final int position)
            {
                List<HelpBean> data = adapter.getData();
                String trifle_id = data.get(position).trifle_id;
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("token", JjgConstant.getToken(mContext));
                hashMap.put("trifle_id", trifle_id);
                HttpUtils.post(NetConstant.HELP_CANCEL, hashMap, new BaseCallBack(mContext)
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
            onRefresh();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRreshEvent(LittleEvent event)
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
