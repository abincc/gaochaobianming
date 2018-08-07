package com.cnsunrun.jiajiagou.product;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseActivity;
import com.cnsunrun.jiajiagou.common.base.BaseEmptyView;
import com.cnsunrun.jiajiagou.common.base.Refreshable;
import com.cnsunrun.jiajiagou.common.constant.ArgConstants;
import com.cnsunrun.jiajiagou.common.network.DialogCallBack;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.RecyclerLineDecoration;
import com.cnsunrun.jiajiagou.home.homepage.HomeProductAdapter;
import com.cnsunrun.jiajiagou.personal.information.InformationActivity;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:
 * Data：2017/8/25 0025-上午 11:04
 * Blog：http://blog.csdn.net/u013983934
 * Author: CZ
 */
public class ProductListActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener
{
    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.iv_sales)
    ImageView mIvSales;
    @BindView(R.id.iv_price)
    ImageView mIvPrice;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    int pageNumber = 1;
    int saleType = 0;
    int priceType = 0;
    private HomeProductAdapter mAdapter;
    private String categroy_id;

    @Override
    protected void init()
    {
        String keyword = getIntent().getStringExtra(ArgConstants.KEYWORD);
        categroy_id = getIntent().getStringExtra(ArgConstants.CATEGROY_ID);
        mEtSearch.setText(keyword);
            getData(pageNumber);
        mEtSearch.setHint("商城搜索");
        mRecycler.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecycler.setBackgroundResource(R.color.white);
        mRecycler.addItemDecoration(new RecyclerLineDecoration(mContext));
        mAdapter = new HomeProductAdapter(R.layout.item_home_product, null);
        mAdapter.setImageLoader(getImageLoader());
        mAdapter.setOnLoadMoreListener(this);
        mAdapter.setEmptyView(new BaseEmptyView(mContext, R.drawable.product_empty, "抱歉没有找到您要找的商品", null));
        mRecycler.addOnItemTouchListener(new OnItemClickListener()
        {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position)
            {
                List<ProductBean> data = adapter.getData();
                Intent intent = new Intent(mContext, ProductDetailActivity.class);
                intent.putExtra(ArgConstants.PRODUCTID, data.get(position).getProduct_id());
                startActivity(intent);
            }
        });

        mRecycler.setAdapter(mAdapter);
        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {

                getData(pageNumber);
                return true;
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        if (ev.getAction() == MotionEvent.ACTION_DOWN)
        {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev))
            {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    // 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
    private boolean isShouldHideKeyboard(View v, MotionEvent event)
    {
        if (v != null && (v instanceof EditText))
        {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(
                    event.getX() > left && event.getX() < right
                            && event.getY() > top && event.getY() < bottom
            );
        }
        return false;
    }

    @Override
    public void onRefresh()
    {
        super.onRefresh();
        pageNumber = 1;
        getData(pageNumber);


    }

    public void getData(final int page)
    {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("keyword", mEtSearch.getText().toString().trim());
        hashMap.put("order_sales", String.valueOf(saleType));
        hashMap.put("order_price", String.valueOf(priceType));
        hashMap.put("categroy_id", categroy_id);
        hashMap.put("p", String.valueOf(page));
        HttpUtils.get(NetConstant.PRODUCT, hashMap, new DialogCallBack((Refreshable) ProductListActivity.this)
        {
            @Override
            public void onResponse(String response, int id)
            {
                LogUtils.d(response);
                ProductResp resp = new Gson().fromJson(response, ProductResp.class);
                if (handleResponseCode(resp))
                {
                    List<ProductBean> info = resp.getInfo();
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
                    mAdapter.checkFullPage(mRecycler);

                }


            }
        });


    }

    /**
     * 更新saletype
     */
    public void updateSaleType()
    {
        switch (saleType)
        {
            case 0:
                saleType = 1;
                mIvSales.setImageResource(R.drawable.sales_btn_rise_sel);
                break;
            case 1:
                saleType = 2;
                mIvSales.setImageResource(R.drawable.sales_btn_drop_sel);
                break;
            case 2:
                saleType = 0;
                mIvSales.setImageResource(R.drawable.sales_btn_sorting_nor);
                break;
        }
        pageNumber = 1;
        getData(pageNumber);

    }

    /**
     * 更新pricetype
     */
    public void updatePriceType()
    {
        switch (priceType)
        {
            case 0:
                priceType = 1;
                mIvPrice.setImageResource(R.drawable.sales_btn_rise_sel);
                break;
            case 1:
                priceType = 2;
                mIvPrice.setImageResource(R.drawable.sales_btn_drop_sel);
                break;
            case 2:
                priceType = 0;
                mIvPrice.setImageResource(R.drawable.sales_btn_sorting_nor);
                break;
        }

        pageNumber = 1;
        getData(pageNumber);

    }

    @Override
    protected int getLayoutRes()
    {
        return R.layout.activity_product_list;
    }

    @OnClick({R.id.iv_back, R.id.iv_news, R.id.tv_sort_overall, R.id.ll_sort_sales, R.id.ll_sort_price})
    public void onViewClicked(View view)
    {
        switch (view.getId())
        {
            case R.id.iv_back:
                onBackPressedSupport();
                break;
            case R.id.iv_news:
                startActivity(new Intent(mContext, InformationActivity.class));
                break;
            case R.id.tv_sort_overall:
                priceType = 0;
                mIvPrice.setImageResource(R.drawable.sales_btn_sorting_nor);
                saleType = 0;
                mIvSales.setImageResource(R.drawable.sales_btn_sorting_nor);
                pageNumber = 1;
                getData(pageNumber);

                break;
            case R.id.ll_sort_sales:
                updateSaleType();
                break;
            case R.id.ll_sort_price:
                updatePriceType();
                break;
        }
    }

    @Override
    public void onLoadMoreRequested()
    {
        mRecycler.post(new Runnable()
        {
            @Override
            public void run()
            {
                getData(pageNumber + 1);
            }
        });
    }
}
