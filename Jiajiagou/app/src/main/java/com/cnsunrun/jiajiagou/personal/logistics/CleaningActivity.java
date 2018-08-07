package com.cnsunrun.jiajiagou.personal.logistics;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseCallBack;
import com.cnsunrun.jiajiagou.common.base.BaseHeaderActivity;
import com.cnsunrun.jiajiagou.common.base.BaseResp;
import com.cnsunrun.jiajiagou.common.base.Refreshable;
import com.cnsunrun.jiajiagou.common.base.TwoButtonDialog;
import com.cnsunrun.jiajiagou.common.constant.JjgConstant;
import com.cnsunrun.jiajiagou.common.network.DialogCallBack;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.RecyclerLineDecoration;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * Created by ${LiuDi}
 * on 2017/8/23on 15:02.
 * 保洁维修
 */

public class CleaningActivity extends BaseHeaderActivity implements BaseQuickAdapter.RequestLoadMoreListener
{
    @BindView(R.id.recycle)
    RecyclerView mRecycle;
    private CleaningAdapter mAdapter;
    private int pageNumber = 1;

    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight)
    {
        tvTitle.setText("保洁维修");
    }

    @Override
    protected void init()
    {
        initRecycles();
        getData(pageNumber);
    }

    private void getData(final int page)
    {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", JjgConstant.getToken(mContext));
        hashMap.put("p", String.valueOf(page));
        HttpUtils.get(NetConstant.CLEAN, hashMap, new DialogCallBack((Refreshable) CleaningActivity.this)
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
                        if (info != null && info.size() > 0)
                        {
                            mAdapter.addData(info);
                            pageNumber++;
                            mAdapter.loadMoreComplete();
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

        mAdapter = new CleaningAdapter(R.layout.item_little_help, null);
        mAdapter.setImageLoader(getImageLoader());
        mRecycle.setAdapter(mAdapter);
        mAdapter.setEmptyView(View.inflate(mContext, R.layout.layout_empty, null));
        mRecycle.addItemDecoration(new RecyclerLineDecoration(mContext));
        mAdapter.setOnLoadMoreListener(this);
        mRecycle.addOnItemTouchListener(new OnItemClickListener()
        {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position)
            {
                List<CleanBean> data = adapter.getData();
                String clean_id = data.get(position).getClean_id();
                Intent intent = new Intent(mContext, CleaningDetailActivity.class);
                intent.putExtra("clean_id", clean_id);

                startActivityForResult(intent, 200);
            }
        });

        mRecycle.addOnItemTouchListener(new OnItemChildClickListener()
        {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, final int position)
            {

                List<CleanBean> data = adapter.getData();
                final String clean_id = data.get(position).getClean_id();

                final TwoButtonDialog dialog = new TwoButtonDialog();
                dialog.setContent("确定执行该操作吗？");
                dialog.setOnBtnConfirmClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("token", JjgConstant.getToken(mContext));
                        hashMap.put("clean_id", clean_id);
                        HttpUtils.post(NetConstant.CLEAN_CANCLE, hashMap, new BaseCallBack(mContext)
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
                                    dialog.dismiss();
                                }
                            }
                        });
                    }
                });
                dialog.show(getSupportFragmentManager(), null);


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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200)
        {
            onRefresh();
        }
    }

    @Override
    protected int getLayoutRes()
    {
        return R.layout.activity_little_help;
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
