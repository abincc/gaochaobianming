package com.cnsunrun.jiajiagou.personal.order;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
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
import com.cnsunrun.jiajiagou.shopcart.PrePayBean;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * Created by ${LiuDi}
 * on 2017/8/19on 11:11.
 * 待收货
 */

public class WaitForRecFragment extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener
{
    @BindView(R.id.recycle)
    RecyclerView mRecycle;
    private AllOrderAdapter mAdapter;
    private int pageNumber = 1;
    private PrePayPoupWindow mPayPoupWindow;

    @Override
    protected void init()
    {
        mRecycle.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mAdapter = new AllOrderAdapter(R.layout.item_all_order, null);
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
                List<AllOrderResp.InfoBean> data = adapter.getData();

                Intent intent = new Intent(mContext, OrderDetailActivity.class);
                intent.putExtra(ArgConstants.ORDER_ID, data.get(position).getOrder_id());
                startActivityForResult(intent, 200);
            }
        });

        mRecycle.addOnItemTouchListener(new OnItemChildClickListener()
        {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position)
            {
                List<AllOrderResp.InfoBean> data = adapter.getData();
                AllOrderResp.InfoBean infoBean = data.get(position);
                String order_id = infoBean.getOrder_id();
                int status = infoBean.getStatus();
                final HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("token", JjgConstant.getToken(mContext));
                hashMap.put("order_id", order_id);
                final TwoButtonDialog dialog = new TwoButtonDialog();
                switch (view.getId())
                {
                    //left
                    case R.id.tv_reimburse:
                        //取消订单

                        dialog.setContent("确定要取消订单吗?");
                        dialog.setOnBtnConfirmClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                dialog.dismiss();
                                HttpUtils.post(NetConstant.ORDER_CANCEL, hashMap, new BaseCallBack(mContext)
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
                    case R.id.tv_confirm_receipt:

                        switch (status)
                        {

                            case 10:
                                //去支付
                                PrePayBean payBean = new PrePayBean(infoBean.getMoney_total(), infoBean
                                        .getOrder_id(), infoBean
                                        .getOrder_no(), null);
                                if (mPayPoupWindow == null)
                                    mPayPoupWindow = new PrePayPoupWindow(mContext, null);

                                mPayPoupWindow.setInfo(payBean);
                                mPayPoupWindow.showAtLocation(mRecycle, Gravity.BOTTOM, 0, 0);

                                break;
                            case 20:
                                //申请退款
                                dialog.setContent("确定要申请退款吗？");
                                dialog.setOnBtnConfirmClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        dialog.dismiss();
                                        HttpUtils.post(NetConstant.APPLY_RETURN, hashMap, new BaseCallBack(mContext)
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
                            case 30:
                                //确认收货
                                dialog.setContent("确定要收货吗？");
                                dialog.setOnBtnConfirmClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        dialog.dismiss();
                                        HttpUtils.post(NetConstant.CONFIRM_RECEIPT, hashMap, new BaseCallBack(mContext)
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
                            case 40:
                                //评价
                                Intent intent = new Intent(mContext, OrderDetailActivity.class);
                                intent.putExtra(ArgConstants.ORDER_ID, order_id);
                                intent.putExtra(ArgConstants.IS_EVALUATE, true);
                                startActivity(intent);
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

    private void getData(final int page)
    {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", JjgConstant.getToken(mContext));
        hashMap.put("p", String.valueOf(page));
        HttpUtils.get(NetConstant.RECEIVE, hashMap, new DialogCallBack((Refreshable) WaitForRecFragment.this)
        {
            @Override
            public void onResponse(String response, int id)
            {
                LogUtils.d(response);

                AllOrderResp resp = new Gson().fromJson(response, AllOrderResp.class);
                if (handleResponseCode(resp))
                {
                    List<AllOrderResp.InfoBean> info = resp.getInfo();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200)
        {
            onRefresh();
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

    @Override
    protected int getChildLayoutRes()
    {
        return R.layout.fragment_wait_receive;
    }
}
