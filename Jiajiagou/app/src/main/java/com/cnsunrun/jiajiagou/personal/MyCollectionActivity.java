package com.cnsunrun.jiajiagou.personal;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseCallBack;
import com.cnsunrun.jiajiagou.common.base.BaseHeaderActivity;
import com.cnsunrun.jiajiagou.common.base.BaseResp;
import com.cnsunrun.jiajiagou.common.base.Refreshable;
import com.cnsunrun.jiajiagou.common.constant.JjgConstant;
import com.cnsunrun.jiajiagou.common.network.DialogCallBack;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.RecyclerLineDecoration;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ${LiuDi}
 * on 2017/8/24on 14:27.
 */

public class MyCollectionActivity extends BaseHeaderActivity implements BaseQuickAdapter.RequestLoadMoreListener
{
    @BindView(R.id.recycle)
    RecyclerView mRecycle;
    @BindView(R.id.tv_right)
    TextView mTvRight;
    @BindView(R.id.btn_delete)
    Button mBtnDelete;
    private MyCollectionAdapter mAdapter;
    boolean isEditor = false;
    private int pageNumber = 1;

    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight)
    {
        tvTitle.setText("我的收藏");
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("编辑");
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
        HttpUtils.get(NetConstant.PRODUCTCOLLECTION, hashMap, new DialogCallBack((Refreshable) MyCollectionActivity
                .this)
        {
            @Override
            public void onResponse(String response, int id)
            {
                LogUtils.d(response);
                MyCollectionResp resp = new Gson().fromJson(response, MyCollectionResp.class);
                if (handleResponseCode(resp))
                {
                    List<MyCollectionResp.InfoBean> data = resp.getInfo();
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
        mAdapter = new MyCollectionAdapter(R.layout.item_my_collect, null);
        mAdapter.setImageLoader(getImageLoader());
        View inflate = View.inflate(mContext, R.layout.layout_empty, null);
        TextView tvEmpty = (TextView) inflate.findViewById(R.id.tv_empty);
        tvEmpty.setText("暂无收藏");
        mAdapter.setEmptyView(inflate);
        mRecycle.addItemDecoration(new RecyclerLineDecoration(mContext));
        mAdapter.setOnLoadMoreListener(this, mRecycle);
        mRecycle.setAdapter(mAdapter);

    }

    @Override
    protected int getLayoutRes()
    {
        return R.layout.activity_my_collection;
    }

    @OnClick({R.id.tv_right, R.id.btn_delete})
    public void onViewClicked(View view)
    {
        switch (view.getId())
        {
            case R.id.tv_right:
                isEditor = !isEditor;
                mBtnDelete.setVisibility(isEditor ? View.VISIBLE : View.GONE);
                mTvRight.setText(isEditor ? "完成" : "编辑");
                mAdapter.editor(isEditor);
                break;
            case R.id.btn_delete:

                List<MyCollectionResp.InfoBean> data = mAdapter.getData();
                if (data == null)
                {
                    showToast("您还没有添加收藏的商品");
                    return;
                }
                ArrayList<String> list = new ArrayList<>();
                for (int i = 0; i < data.size(); i++)
                {
                    boolean isChecked = data.get(i).isChecked;
                    if (isChecked)
                    {
                        list.add(data.get(i).collect_id);
                    }
                }

                if (list.size() == 0)
                {
                    showToast("请选择需要移除的收藏商品");
                    return;
                }
                String[] collect_id = list.toArray(new String[list.size()]);

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("token", JjgConstant.getToken(mContext));
                for (int i = 0; i < collect_id.length; i++)
                {
                    hashMap.put("collect_id" + "[" + i + "]", collect_id[i]);
                }
                HttpUtils.post(NetConstant.COLLECTIONCANCEL, hashMap, new BaseCallBack(mContext)
                {
                    @Override
                    public void onResponse(String response, int id)
                    {
                        LogUtils.d(response);
                        BaseResp baseResp = new Gson().fromJson(response, BaseResp.class);
                        if (handleResponseCode(baseResp))
                        {
                            showToast(baseResp.getMsg(), 1);
                            onRefresh();
                        }

                    }
                });


                break;
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
