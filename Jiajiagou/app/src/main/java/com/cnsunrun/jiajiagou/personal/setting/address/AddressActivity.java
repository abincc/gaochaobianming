package com.cnsunrun.jiajiagou.personal.setting.address;

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
import com.cnsunrun.jiajiagou.common.constant.ArgConstants;
import com.cnsunrun.jiajiagou.common.constant.JjgConstant;
import com.cnsunrun.jiajiagou.common.network.DialogCallBack;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.SpaceItemDecoration;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ${LiuDi}
 * on 2017/8/19on 16:00.
 */

public class AddressActivity extends BaseHeaderActivity
{
    @BindView(R.id.recycle)
    RecyclerView mRecycle;
    private AddressAdapter mAdapter;
    private boolean mIs_return;

    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight)
    {
        tvTitle.setText("地址管理");
    }

    @Override
    protected void init()
    {
        Intent intent = getIntent();
        if (intent != null)
            mIs_return = intent.getBooleanExtra(ArgConstants.IS_FROM_ORDER, false);
        initRecycles();
        getData();

    }

    @Override
    public void onRefresh()
    {
        super.onRefresh();
        getData();
    }

    private void getData()
    {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", JjgConstant.getToken(mContext));
        HttpUtils.get(NetConstant.ADDRESS, hashMap, new DialogCallBack((Refreshable) AddressActivity.this)
        {
            @Override
            public void onResponse(String response, int id)
            {
                LogUtils.d(response);
                AddressResp resp = new Gson().fromJson(response, AddressResp.class);
                if (handleResponseCode(resp))
                {
                    mAdapter.setPreChecked(-1);
                    mAdapter.setNewData(resp.getInfo());
                }
            }
        });


    }

    private void initRecycles()
    {
        mRecycle.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mAdapter = new AddressAdapter(R.layout.item_addresss, null);
        mRecycle.setAdapter(mAdapter);
        View inflate = View.inflate(mContext, R.layout.layout_empty, null);
        TextView tvEmpty = (TextView) inflate.findViewById(R.id.tv_empty);
        tvEmpty.setText("暂无收货地址");
        mAdapter.setEmptyView(inflate);
        mRecycle.addItemDecoration(new SpaceItemDecoration(10));
        mRecycle.addOnItemTouchListener(new OnItemChildClickListener()
        {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, final int position)
            {
                ArrayList<AddressBean> list = (ArrayList<AddressBean>) adapter.getData();
                final AddressBean infoBean = list.get(position);
                switch (view.getId())
                {
                    case R.id.tv_delete:
                        final TwoButtonDialog dialog = new TwoButtonDialog();
                        dialog.setContent("确定执行该操作吗?");
                        dialog.setOnBtnConfirmClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("token", JjgConstant.getToken(mContext));
                                hashMap.put("address_id", infoBean.address_id);
                                HttpUtils.post(NetConstant.ADDRESS_DEL, hashMap, new BaseCallBack(mContext)
                                {
                                    @Override
                                    public void onResponse(String response, int id)
                                    {
                                        LogUtils.d(response);
                                        BaseResp baseResp = new Gson().fromJson(response, BaseResp.class);
                                        if (handleResponseCode(baseResp))
                                        {
                                            mAdapter.remove(position);
                                            dialog.dismiss();
                                        }
                                    }
                                });
                            }
                        });
                        dialog.show(getSupportFragmentManager(), null);

                        break;

                    case R.id.tv_editor:
                        Intent intent = new Intent(mContext, AddAddressActivity.class);
                        intent.putExtra(ArgConstants.AREABEAN, infoBean);
                        startActivityForResult(intent, 300);

                        break;
                }
            }
        });

        mRecycle.addOnItemTouchListener(new OnItemClickListener()
        {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position)
            {
                if (mIs_return)
                {

                    List<AddressBean> data = adapter.getData();
                    Intent intent = new Intent();
                    intent.putExtra(ArgConstants.ADDRESSBEAN, data.get(position));
                    setResult(200, intent);
                    finish();
                }
            }
        });

    }

    @Override
    protected int getLayoutRes()
    {
        return R.layout.activity_address;
    }

    @OnClick(R.id.btn_confirm)
    public void onViewClicked()
    {

        startActivityForResult(new Intent(mContext, AddAddressActivity.class), 200);

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
}
