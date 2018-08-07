package com.cnsunrun.jiajiagou.video.ys7;

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

public class DeviceListActivity extends BaseHeaderActivity implements BaseQuickAdapter.RequestLoadMoreListener
{
    @BindView(R.id.recycle)
    RecyclerView mRecycle;
    @BindView(R.id.tv_right)
    TextView mTvRight;
    private DeviceListAdapter mAdapter;
    private int pageNumber = 1;

    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight)
    {
        tvTitle.setText("监控列表");
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
        HttpUtils.get(NetConstant.VIDEO_DEVICE_LIST, hashMap, new DialogCallBack((Refreshable) DeviceListActivity.this) {
            @Override
            public void onError(Call call, Exception e, int id) {

            }
            @Override
            public void onResponse(String response, int id) {
                DeviceListResp deviceListResp = new Gson().fromJson(response, DeviceListResp.class);
                if (handleResponseCode(deviceListResp))
                {
                    List<DeviceListResp.InfoBean> data = deviceListResp.getInfo();

                    LogUtils.d("-------hhhhhh---------" + data);

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
        mAdapter = new DeviceListAdapter(R.layout.item_device, null);
        mAdapter.setImageLoader(getImageLoader());
        View inflate = View.inflate(mContext, R.layout.layout_empty, null);
        TextView tvEmpty = (TextView) inflate.findViewById(R.id.tv_empty);
        tvEmpty.setText("暂无设备");
        mAdapter.setEmptyView(inflate);
        mRecycle.addItemDecoration(new RecyclerLineDecoration(mContext));
//        mAdapter.setOnLoadMoreListener(this, mRecycle);
        mRecycle.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                List<DeviceListResp.InfoBean> data = adapter.getData();
                if (view.getId() == R.id.device) {
//                    getRoom(String.valueOf(data.get(position).getMember().getId()));
//                    PlayBackActivity.startPlayActivityGlobal(DeviceListActivity.this, data.get(position).getVideo().getAppkey(), data.get(position).getVideo().getAccess_token(), data.get(position).getEzopen(),"");
                }
            }
        });
    }

    @Override
    protected int getLayoutRes()
    {
        return R.layout.activity_device;
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

}
