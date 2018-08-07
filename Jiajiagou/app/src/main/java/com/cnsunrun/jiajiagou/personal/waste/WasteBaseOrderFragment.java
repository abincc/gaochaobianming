package com.cnsunrun.jiajiagou.personal.waste;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseCallBack;
import com.cnsunrun.jiajiagou.common.base.BaseFragment;
import com.cnsunrun.jiajiagou.common.base.BaseResp;
import com.cnsunrun.jiajiagou.common.base.Refreshable;
import com.cnsunrun.jiajiagou.common.base.TwoButtonDialog;
import com.cnsunrun.jiajiagou.common.constant.ArgConstants;
import com.cnsunrun.jiajiagou.common.constant.JjgConstant;
import com.cnsunrun.jiajiagou.common.network.DialogCallBack;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.widget.popup.PrePayPoupWindow;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * Created by ${LiuDi}
 * on 2017/9/28on 16:01.
 */

public class WasteBaseOrderFragment extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener
{
    @BindView(R.id.recycle)
    RecyclerView mRecycle;
    private AllWasteOrderAdapter mAdapter;
    private int pageNumber = 1;
    private PrePayPoupWindow mPayPoupWindow;
    private int type;


    @Override
    protected void init()
    {
        type = getArguments().getInt("type");
        mRecycle.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mAdapter = new AllWasteOrderAdapter(R.layout.item_all_waste_order, null);
        mAdapter.setImageLoader(getImageLoader());
        mAdapter.setOnLoadMoreListener(this);
        View inflate = View.inflate(mContext, R.layout.layout_empty, null);
        TextView tvEmpty = (TextView) inflate.findViewById(R.id.tv_empty);
        tvEmpty.setText("暂无订单");
        mAdapter.setEmptyView(inflate);
        mRecycle.setAdapter(mAdapter);
//        mAdapter.setNewData(mList);
        mRecycle.addOnItemTouchListener(new OnItemClickListener()
        {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position)
            {
                List<AllWasteOrderResp.InfoBean> data = adapter.getData();

                Intent intent = new Intent(mContext, WasteOrderDetailActivity.class);
                intent.putExtra(ArgConstants.ORDER_ID, data.get(position).getId());
                startActivityForResult(intent, 200);
            }
        });

        mRecycle.addOnItemTouchListener(new OnItemChildClickListener()
        {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position)
            {
                List<AllWasteOrderResp.InfoBean> data = adapter.getData();
                AllWasteOrderResp.InfoBean infoBean = data.get(position);
                String id = infoBean.getId();
                int status = infoBean.getStatus();
                final HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("token", JjgConstant.getToken(mContext));
                hashMap.put("id", id);
                final TwoButtonDialog dialog = new TwoButtonDialog();
                switch (view.getId())
                {
                    case R.id.tv_confirm_receipt:

                        switch (status)
                        {
                            case 10:
                                dialog.setContent("确定取消订单吗？");
                                dialog.setOnBtnConfirmClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        dialog.dismiss();
                                        HttpUtils.post(NetConstant.WASTE_CANCAL_ORDER, hashMap, new BaseCallBack(mContext)
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
                                    }
                                });
                                dialog.show(getFragmentManager(), null);
                                break;
                        }
                }
            }
        });

        getData(pageNumber);
    }

    @Override
    public void onRefresh()
    {
        super.onRefresh();
        pageNumber = 1;
        getData(pageNumber);
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

    private void getData(final int page)
    {
        String Url = null;
        if (type == 1)
        {
            Url = NetConstant.WASTE_ORDER_ALL;
        } else if (type == 2)
        {
            Url = NetConstant.WASTE_ORDER_UNDEAL;
        } else if (type == 3)
        {
            Url = NetConstant.WASTE_ORDER_DEAL;
        } else if (type == 4)
        {
            Url = NetConstant.WASTE_ORDER_CANCEL;
        }
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", JjgConstant.getToken(mContext));
        hashMap.put("p", String.valueOf(page));
        HttpUtils.get(Url, hashMap, new DialogCallBack((Refreshable) WasteBaseOrderFragment.this)
        {
            @Override
            public void onResponse(String response, int id)
            {
                LogUtils.d(response);

                AllWasteOrderResp resp = new Gson().fromJson(response, AllWasteOrderResp.class);
                if (handleResponseCode(resp))
                {
                    List<AllWasteOrderResp.InfoBean> info = resp.getInfo();
                    if (page == 1)
                    {
                        mAdapter.setNewData(info);
                    } else
                    {
                        if (info != null && info.size() > 0)
                        {
                            mAdapter.addData(info);
                            mAdapter.loadMoreComplete();
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

    @Override
    protected int getChildLayoutRes()
    {
        return R.layout.fragment_all_order;
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
