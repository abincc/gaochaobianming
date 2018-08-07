package com.cnsunrun.jiajiagou.shopcart;

import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseCallBack;
import com.cnsunrun.jiajiagou.common.base.BaseResp;
import com.cnsunrun.jiajiagou.common.base.CZBaseQucikAdapter;
import com.cnsunrun.jiajiagou.common.constant.ArgConstants;
import com.cnsunrun.jiajiagou.common.constant.JjgConstant;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.widget.view.AddSubView;
import com.cnsunrun.jiajiagou.product.ProductDetailActivity;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ${LiuDi}
 * on 2017/8/21on 11:12.
 * 购物车adapter
 */

public class ShopCartAdapter extends CZBaseQucikAdapter<ShopCartResp.InfoBean>
{
    boolean isEditor;
    public ShopCartAdapter(int layoutResId, List<ShopCartResp.InfoBean> data)
    {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final ShopCartResp.InfoBean dataBean)
    {
        baseViewHolder.setText(R.id.tv_describe, dataBean.product_spec_value)
                .setText(R.id.tv_price, "¥ " + dataBean.product_price)
                .setText(R.id.tv_name, dataBean.product_title)
                .setVisible(R.id.tv_describe, !isEditor)
                .setVisible(R.id.tv_price, !isEditor)
                .setVisible(R.id.tv_count, !isEditor)
                .setVisible(R.id.subview, isEditor);
        getImageLoader().load(dataBean.product_image).into((ImageView) baseViewHolder.getView(R.id.iv_imv));

        baseViewHolder.getConvertView().setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(mContext, ProductDetailActivity.class);
                intent.putExtra(ArgConstants.PRODUCTID, dataBean.product_id);
                mContext.startActivity(intent);
            }
        });
        final AddSubView subView = baseViewHolder.getView(R.id.subview);
        final TextView tvCount = baseViewHolder.getView(R.id.tv_count);
        subView.setNum(dataBean.product_num);
        tvCount.setText("x" + dataBean.product_num);
        final CheckBox checkBox = baseViewHolder.getView(R.id.checkbox);


        checkBox.setChecked(dataBean.isChecked);

        checkBox.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dataBean.isChecked = checkBox.isChecked();
                if (mListener != null)
                    mListener.OnItemChecked(baseViewHolder.getAdapterPosition());
            }
        });

        subView.setOnTextChangeListener(new AddSubView.OnTextChangeListener()
        {
            @Override
            public void afterTextChanged(EditText s, final int num)
            {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("token", JjgConstant.getToken(mContext));
                hashMap.put("cart_id", dataBean.cart_id);
                hashMap.put("product_num", String.valueOf(num));
                HttpUtils.post(NetConstant.CART_SET, hashMap, new BaseCallBack(mContext)
                {
                    @Override
                    public void onResponse(String response, int id)
                    {
                        LogUtils.d(response);
                        BaseResp baseResp = new Gson().fromJson(response, BaseResp.class);
                        if (handleResponseCode(baseResp))
                        {
                            subView.setNum(String.valueOf(num));
                            dataBean.product_num = String.valueOf(num);
                            tvCount.setText("x" + dataBean.product_num);
                            mListener.OnItemChecked(baseViewHolder.getAdapterPosition());

                        } else
                        {
                            subView.setNum(String.valueOf(subView.getPre_num()));
                        }
                    }
                });
            }

            @Override
            public void beforeTextChanged(CharSequence s, AddSubView v)
            {

            }

            @Override
            public void onReachMaxNum()
            {

            }

            @Override
            public void onReachMinNum()
            {

            }

            @Override
            public void add(final int num, final AddSubView subView)
            {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("token", JjgConstant.getToken(mContext));
                hashMap.put("cart_id", dataBean.cart_id);
                hashMap.put("product_num", String.valueOf(num + 1));
                HttpUtils.post(NetConstant.CART_SET, hashMap, new BaseCallBack(mContext)
                {
                    @Override
                    public void onResponse(String response, int id)
                    {
                        LogUtils.d(response);
                        BaseResp baseResp = new Gson().fromJson(response, BaseResp.class);
                        if (handleResponseCode(baseResp))
                        {
                            subView.setNum(String.valueOf(num + 1));
                            dataBean.product_num = String.valueOf(num + 1);
                            tvCount.setText("x" + dataBean.product_num);
                            mListener.OnItemChecked(baseViewHolder.getAdapterPosition());
                        }
                    }
                });

            }

            @Override
            public void reduce(final int num, final AddSubView subView)
            {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("token", JjgConstant.getToken(mContext));
                hashMap.put("cart_id", dataBean.cart_id);
                hashMap.put("product_num", String.valueOf(num - 1));
                HttpUtils.post(NetConstant.CART_SET, hashMap, new BaseCallBack(mContext)
                {
                    @Override
                    public void onResponse(String response, int id)
                    {
                        LogUtils.d(response);
                        BaseResp baseResp = new Gson().fromJson(response, BaseResp.class);
                        if (handleResponseCode(baseResp))
                        {
                            subView.setNum(String.valueOf(num - 1));
                            dataBean.product_num = String.valueOf(num - 1);
                            tvCount.setText("x" + dataBean.product_num);
                            mListener.OnItemChecked(baseViewHolder.getAdapterPosition());
                        }
                    }
                });
            }
        });

    }

    private OnItemCheckedListener mListener;

    public void setOnItemCheckedListener(OnItemCheckedListener listener)
    {
        mListener = listener;
    }

    public void editor(boolean isEditor)
    {
        this.isEditor = isEditor;
        notifyDataSetChanged();
    }

    public interface OnItemCheckedListener
    {
        void OnItemChecked(int pos);
    }


}
