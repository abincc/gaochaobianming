package com.cnsunrun.jiajiagou.product;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.bumptech.glide.Glide;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseCallBack;
import com.cnsunrun.jiajiagou.common.base.BasePopupWindow;
import com.cnsunrun.jiajiagou.common.base.BaseResp;
import com.cnsunrun.jiajiagou.common.constant.ArgConstants;
import com.cnsunrun.jiajiagou.common.constant.JjgConstant;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.ToastTask;
import com.cnsunrun.jiajiagou.common.util.ToastUtils;
import com.cnsunrun.jiajiagou.common.widget.view.AddSubView;
import com.cnsunrun.jiajiagou.shopcart.ConfirmOrderV2Activity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:
 * Data：2017/8/25 0025-下午 2:30
 * Blog：http://blog.csdn.net/u013983934
 * Author:
 */
public class BuyProductPop extends BasePopupWindow implements AddSubView.OnTextChangeListener
        , SpecAdapter.SpecListener
{
    @BindView(R.id.tv_price)
    TextView mTvPrice;
    @BindView(R.id.tv_count)
    TextView mTvCount;
    @BindView(R.id.subview)
    AddSubView mSubview;
    @BindView(R.id.iv_imv)
    ImageView mIvImv;
    @BindView(R.id.btn_confirm)
    Button mBtnConfirm;
    @BindView(R.id.ll_container)
    LinearLayout mLlContainer;
    @BindView(R.id.ll_parent_view)
    LinearLayout mLlParentView;
    private ProductDetailResp.InfoBean infoBean;
    private ArrayList<SpecAdapter> mSpecAdapters = new ArrayList<>();
    private int mInventory;

    boolean isBuy = false;
    private String mFirst;
    private String mSecond;
    private String mThird;
    private String mFour;

    public BuyProductPop(Context context, ProductDetailResp.InfoBean infoBean)
    {

        super(context, -1, -1);
        this.infoBean = infoBean;
        this.setAnimationStyle(R.style.umeng_socialize_shareboard_animation);
        init();

    }

    private void init()
    {
        mSubview.setNum("1");
        mSubview.setOnTextChangeListener(this);
        if (infoBean != null)
        {
            Glide.with(mContext).load(infoBean.getCover()).into(mIvImv);
            mTvPrice.setText("¥" + infoBean.getPrice());
            mTvCount.setText("库存：" + infoBean.getInventory());
            mInventory = Integer.valueOf(infoBean.getInventory());
            List<ProductDetailResp.InfoBean.SpecBean> spec = infoBean.getSpec();

            if (spec != null && spec.size() > 0)
            {
                for (int i = 0; i < spec.size(); i++)
                {

                    ProductDetailResp.InfoBean.SpecBean specBean = spec.get(i);
                    RecyclerView recyclerView = new RecyclerView(mContext);
                    recyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
                    SpecAdapter adapter = new SpecAdapter(R.layout.item_spec, null);
                    recyclerView.setAdapter(adapter);
                    adapter.setSpecListener(this, i);
                    mSpecAdapters.add(adapter);

                    TextView textView = new TextView(mContext);
                    textView.setText(specBean.getSpec_title());
                    textView.setTextColor(0xff333333);
                    textView.setTextSize(14);
                    textView.setPadding(0, 18, 0, 18);
                    adapter.addHeaderView(textView);

                    adapter.setNewData(specBean.getSpec_value_list());
                    mLlContainer.addView(recyclerView);
                }
            }
        }
    }

    @Override
    protected int getLayoutRes()
    {
        return R.layout.pop_buy_product;
    }

    @OnClick({R.id.empty_view, R.id.btn_confirm})
    public void onViewClicked(View view)
    {
        switch (view.getId())
        {
            case R.id.empty_view:
                this.dismiss();
                break;
            case R.id.btn_confirm:
                String skuId = getSkuId();
                int num = mSubview.getNum();
                LogUtils.d("skuId" + skuId);
                if (TextUtils.isEmpty(skuId))
                {
                    ToastTask.getInstance(mContext).setMessage("请选择商品规格").error();
                    return;
                }


                if (isBuy)
                {
                    Intent intent = new Intent(mContext, ConfirmOrderV2Activity.class);
                    intent.putExtra("sku_id", skuId);
                    intent.putExtra("num", String.valueOf(num));
                    intent.putExtra(ArgConstants.PRODUCTID, infoBean.getProduct_id());
                    mContext.startActivity(intent);
                } else
                {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("token", JjgConstant.getToken(mContext));
                    hashMap.put("sku_id", skuId);
                    hashMap.put("num", String.valueOf(num));
                    HttpUtils.post(NetConstant.CART_ADD, hashMap, new BaseCallBack(mContext)
                    {
                        @Override
                        public void onResponse(String response, int id)
                        {
                            LogUtils.d(response);
                            BaseResp baseResp = new Gson().fromJson(response, BaseResp.class);
                            if (handleResponseCode(baseResp))
                            {
                                ToastTask.getInstance(mContext).setMessage(baseResp.getMsg()).success();
                            }
                        }
                    });
                }
                this.dismiss();
                break;
        }
    }

    private String getSkuId()
    {
        String sku_id = null;
        StringBuilder sb = new StringBuilder();
        List<ProductDetailResp.InfoBean.SkuBean> sku = infoBean.getSku();
        if (mSpecAdapters.size() > 0)
        {
            if (mSpecAdapters.size() == 1)
            {
                sb.append(mSpecAdapters.get(0).getSpec_value_id());

            } else
            {
                for (int i = 0; i < mSpecAdapters.size(); i++)
                {
                    sb.append(mSpecAdapters.get(i).getSpec_value_id());
                    if (i != mSpecAdapters.size() - 1)
                    {
                        sb.append("-");
                    }
                }
            }
            if (sku != null && sku.size() > 0)
            {
                for (int i = 0; i < sku.size(); i++)
                {
                    if (sku.get(i).getSpec_value_ids().equals(sb.toString()))
                    {
                        sku_id = sku.get(i).getSku_id();
                        break;
                    } else
                    {
                        sku_id = null;
                    }
                }
            }
        } else
        {
            if (sku != null && sku.size() != 0)
                sku_id = sku.get(0).getSku_id();
        }
        return sku_id;
    }

    @Override
    public void afterTextChanged(EditText s, int num)
    {
        String skuId = getSkuId();
        if (TextUtils.isEmpty(skuId))
        {
            ToastTask.getInstance(mContext).setMessage("请选择商品规格").error();
            mSubview.setNum("1");
            return;
        }
        if (num > mInventory)
        {
            mSubview.setNum(String.valueOf(mInventory));
        } else
        {
            mSubview.setNum(String.valueOf(num));
        }
    }

    public void setIsBuy(boolean isBuy)
    {
        this.isBuy = isBuy;
        mBtnConfirm.setText(isBuy ? "立刻购买" : "加入购物车");
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
    public void showAsDropDown(View anchor)
    {
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        super.showAsDropDown(anchor);
    }

    @Override
    public void onReachMinNum()
    {

    }

    @Override
    public void add(final int num, final AddSubView subView)
    {
        String skuId = getSkuId();
        if (TextUtils.isEmpty(skuId))
        {
            ToastUtils.show(mContext, "请选择商品规格");
            return;
        }
        if (num >= mInventory)
        {
            subView.setNum(String.valueOf(mInventory));
        } else
        {
            subView.setNum(String.valueOf(num + 1));
        }
    }

    @Override
    public void reduce(int num, AddSubView subView)
    {
        String skuId = getSkuId();
        if (TextUtils.isEmpty(skuId))
        {
            ToastUtils.show(mContext, "请选择商品规格");
            return;
        }
        subView.setNum(String.valueOf(num - 1));
    }

    @Override
    public void getSpecValue(String spec_value_id, int index)
    {
        LogUtils.d("spec_value_id" + spec_value_id);
//        mFirst = null;
//        mSecond = null;
//        mThird = null;
//        mFour = null;
        if (index == 0)
        {
            mFirst = spec_value_id;
        } else if (index == 1)
        {
            mSecond = spec_value_id;
        } else if (index == 2)
        {
            mThird = spec_value_id;
        } else if (index == 3)
        {
            mFour = spec_value_id;
        }

        if (mSpecAdapters.size() > 0)
        {
            StringBuilder stringBuilder = new StringBuilder();
            List<ProductDetailResp.InfoBean.SkuBean> sku = infoBean.getSku();
            switch (mSpecAdapters.size())
            {
                case 1:
                    stringBuilder.append(mFirst);
                    break;
                case 2:
                    stringBuilder.append(mFirst)
                            .append("-")
                            .append(mSecond);
                    break;
                case 3:
                    stringBuilder.append(mFirst)
                            .append(mSecond).append("-")
                            .append(mThird);
                    break;
                case 4:
                    stringBuilder.append(mFirst)
                            .append("-")
                            .append(mSecond).append("-")
                            .append(mThird).append("-")
                            .append(mFour);
                    break;
            }
            LogUtils.d(stringBuilder.toString() + "sb");
            if (sku.size() > 0)
            {
                for (int i = 0; i < sku.size(); i++)
                {
                    if (stringBuilder.toString().equals(sku.get(i).getSpec_value_ids()))
                    {
                        mTvPrice.setText("¥" + sku.get(i).getPrice());
                    }
                }
            }

        }

    }


}
