package com.cnsunrun.jiajiagou.shopclassify;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.utils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseCallBack;
import com.cnsunrun.jiajiagou.common.base.BaseFragment;
import com.cnsunrun.jiajiagou.common.constant.ArgConstants;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.product.ProductListActivity;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;

/**
 * Description:
 * Data：2017/8/18 0018-上午 11:56
 * Blog：http://blog.csdn.net/u013983934
 * Author: CZ
 */
public class ShopClassifyFragment extends BaseFragment
{
    @BindView(R.id.recycle)
    RecyclerView mRecycle;
    @BindView(R.id.recycle_goods)
    RecyclerView mRecycleGoods;
    private ShopClassiFyAdapter mAdapter;
    private ShopListAdapter mListAdapter;
    private int pos = 0;

    @Override
    protected void init()
    {
        initRecycles();
        getData();
    }

    private void getData()
    {
        HttpUtils.post(NetConstant.CATEGROY, null, new BaseCallBack(mContext)
        {

            @Override
            public void onResponse(String response, int id)
            {
                LogUtils.d(response);
                ClassifyResp classifyResp = new Gson().fromJson(response, ClassifyResp.class);
                if (handleResponseCode(classifyResp))
                {
                    List<ClassifyResp.InfoBean> info = classifyResp.getInfo();
                    if (info != null && info.size() > 0)
                    {
                        mAdapter.setNewData(info);
                        mListAdapter.setNewData(info.get(0).get_child());
                    }
                }

            }
        });

    }

    private void initRecycles()
    {
        mRecycle.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        mAdapter = new ShopClassiFyAdapter(R.layout.item_shop_classify, null);
        mAdapter.setImageLoader(getImageLoader());
        mRecycle.setAdapter(mAdapter);

        mRecycleGoods.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        mListAdapter = new ShopListAdapter(R.layout.item_shop_list, null);
        mListAdapter.setImageLoader(getImageLoader());
        mRecycleGoods.setAdapter(mListAdapter);
        mRecycle.addOnItemTouchListener(new OnItemClickListener()
        {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position)
            {
                List<ClassifyResp.InfoBean> data = (List<ClassifyResp.InfoBean>) adapter.getData();
                if (pos != position)
                {
                    mListAdapter.setNewData(data.get(position).get_child());
                }
                pos = position;

            }
        });

        mRecycleGoods.addOnItemTouchListener(new OnItemClickListener()
        {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position)
            {
                List<ClassifyResp.InfoBean.ChildBean> data = adapter.getData();
                Intent intent = new Intent(mContext, ProductListActivity.class);
                intent.putExtra(ArgConstants.CATEGROY_ID, data.get(position).getCategroy_id());
                startActivity(intent);
            }
        });

    }

    @Override
    protected int getChildLayoutRes()
    {
        return R.layout.fragment_shop_classify;
    }

}
