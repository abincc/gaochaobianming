package com.cnsunrun.jiajiagou.shopcart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseCallBack;
import com.cnsunrun.jiajiagou.common.base.BaseEmptyView;
import com.cnsunrun.jiajiagou.common.base.BaseFragment;
import com.cnsunrun.jiajiagou.common.base.BaseResp;
import com.cnsunrun.jiajiagou.common.base.Refreshable;
import com.cnsunrun.jiajiagou.common.constant.ArgConstants;
import com.cnsunrun.jiajiagou.common.constant.JjgConstant;
import com.cnsunrun.jiajiagou.common.network.DialogCallBack;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.RecyclerLineDecoration;
import com.cnsunrun.jiajiagou.product.FreshEvent;
import com.cnsunrun.jiajiagou.product.ProductListActivity;
import com.google.gson.Gson;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:
 * Data：2017/8/18 0018-上午 11:51
 * Blog：http://blog.csdn.net/u013983934
 * Author: ddd
 */
public class ShopCartFragment extends BaseFragment implements ShopCartAdapter.OnItemCheckedListener
{
    @BindView(R.id.tv_editor)
    TextView mTvEditor;
    @BindView(R.id.recycle)
    RecyclerView mRecycle;
    @BindView(R.id.checkbox)
    CheckBox mCheckbox;
    @BindView(R.id.tv_manage)
    TextView mTvManage;
    @BindView(R.id.tv_price)
    TextView mTvPrice;
    @BindView(R.id.ll_price)
    LinearLayout mLlPrice;
    private ShopCartAdapter mAdapter;
    //编辑或完成状态
    boolean isEditor = false;

    @Override
    protected void init()
    {
        initRecycles();
//        addProduct();

        getData();
    }

    @Override
    public void onRefresh()
    {
        super.onRefresh();
        mCheckbox.setChecked(false);
        getData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRreshEvent(FreshEvent event)
    {
        onRefresh();
    }

    private void getData()
    {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", JjgConstant.getToken(mContext));
        HttpUtils.get(NetConstant.SHOPCART, hashMap, new DialogCallBack((Refreshable) ShopCartFragment.this)
        {
            @Override
            public void onResponse(String response, int id)
            {
                LogUtils.d(response);
                ShopCartResp resp = new Gson().fromJson(response, ShopCartResp.class);
                if (handleResponseCode(resp))
                {
                    mAdapter.setNewData(resp.getInfo());
                    setManageText();
                }

            }
        });

    }

    private void initRecycles()
    {
        mRecycle.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mAdapter = new ShopCartAdapter(R.layout.item_shop_cart, null);
        mAdapter.setImageLoader(getImageLoader());
        BaseEmptyView emptyView = new BaseEmptyView(mContext, R.drawable.cart_no_data, "您还没有加入商品到购物车", null);
        emptyView.setBtnText("去选购");
        emptyView.setBtnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(mContext, ProductListActivity.class));
            }
        });
        mAdapter.setEmptyView(emptyView);
        mRecycle.addItemDecoration(new RecyclerLineDecoration(mContext, 0xfff4f4f4));
        mAdapter.setOnItemCheckedListener(this);
        mRecycle.setAdapter(mAdapter);

    }

    @Override
    protected int getChildLayoutRes()
    {
        return R.layout.fragment_shop_cart;
    }

    @OnClick({R.id.tv_editor, R.id.tv_manage, R.id.checkbox})
    public void onViewClicked(View view)
    {
        switch (view.getId())
        {
            case R.id.tv_editor:
                isEditor = !isEditor;
                if (isEditor)
                {
                    mTvEditor.setText("完成");
                } else
                {
                    mTvEditor.setText("编辑");
                }
                mAdapter.editor(isEditor);
                setManageText();


                break;
            case R.id.tv_manage:
                if (isEditor)
                {
                    //删除
                    removeProduct();

                } else
                {
                    //完成 去支付
                    ArrayList<String> list = new ArrayList<>();
                    List<ShopCartResp.InfoBean> data = mAdapter.getData();
                    if (data != null)
                    {
                        for (int i = 0; i < data.size(); i++)
                        {
                            boolean isChecked = data.get(i).isChecked;
                            if (isChecked)
                            {
                                list.add(data.get(i).cart_id);
                            }
                        }
                    }
                    String[] cart_ids = list.toArray(new String[list.size()]);

                    if (cart_ids.length == 0)
                    {
                        showToast("请选择需要结算的订单");
                    } else
                    {
                        Intent intent = new Intent(mContext, ConfirmOrderActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putStringArray(ArgConstants.CART_ID, cart_ids);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                }

                break;

            case R.id.checkbox:
                if (mAdapter != null)
                {
                    List<ShopCartResp.InfoBean> data = mAdapter.getData();
                    if (data != null && data.size() > 0)
                    {

                        for (int i = 0; i < data.size(); i++)
                        {
                            data.get(i).isChecked = mCheckbox.isChecked();
                        }
                    }
                    //更新状态
                    mAdapter.notifyDataSetChanged();
                    setManageText();
                }

                break;
        }
    }

    /**
     * 删除商品
     */
    private void removeProduct()
    {
        ArrayList<String> list = new ArrayList<>();
        List<ShopCartResp.InfoBean> data = mAdapter.getData();
        if (data != null)
        {
            for (int i = 0; i < data.size(); i++)
            {
                boolean isChecked = data.get(i).isChecked;
                if (isChecked)
                {
                    list.add(data.get(i).cart_id);
                }
            }
            String[] cart_ids = list.toArray(new String[list.size()]);
            if (cart_ids.length > 0)
            {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("token", JjgConstant.getToken(mContext));
                for (int i = 0; i < cart_ids.length; i++)
                {
                    hashMap.put("cart_ids" + "[" + i + "]", cart_ids[i]);
                }
                HttpUtils.post(NetConstant.CART_REMOVE, hashMap, new BaseCallBack(mContext)
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
            } else
            {
                showToast("没有可删除的商品");
            }


        }


    }

    /**
     * 更新删除或支付文字状态
     */
    private void setManageText()
    {
        int count = 0;
        BigDecimal price = new BigDecimal(0);

        List<ShopCartResp.InfoBean> data = mAdapter.getData();
        if (data != null)
        {
            for (int i = 0; i < data.size(); i++)
            {
                if (data.get(i).isChecked)
                {
                    count++;
                    price = price.add(new BigDecimal(data.get(i).product_num).multiply(new BigDecimal(data.get(i)
                            .product_price)));
                }
            }
        }

        mLlPrice.setVisibility(isEditor ? View.GONE : View.VISIBLE);
        if (count == 0)
        {
            mTvManage.setText(isEditor ? "删除" : "去结算");
        } else
        {
            mTvManage.setText(isEditor ? "删除(" + count + ")" : "去结算(" + count + ")");
        }

        mTvPrice.setText("¥ " + price);

    }

    @Override
    public void OnItemChecked(int pos)
    {
        setManageText();
    }
}
