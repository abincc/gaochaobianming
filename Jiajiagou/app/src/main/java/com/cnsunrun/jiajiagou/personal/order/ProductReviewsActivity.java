package com.cnsunrun.jiajiagou.personal.order;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseHeaderActivity;
import com.cnsunrun.jiajiagou.common.base.BaseResp;
import com.cnsunrun.jiajiagou.common.constant.ArgConstants;
import com.cnsunrun.jiajiagou.common.constant.JjgConstant;
import com.cnsunrun.jiajiagou.common.network.DialogCallBack;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.FileUtils;
import com.cnsunrun.jiajiagou.common.util.ImageUtils;
import com.cnsunrun.jiajiagou.common.widget.AddPicBean;
import com.cnsunrun.jiajiagou.common.widget.AddPicGridAdapter;
import com.google.gson.Gson;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by ${LiuDi}
 * on 2017/8/25on 11:06.
 */

public class ProductReviewsActivity extends BaseHeaderActivity
{
    @BindView(R.id.et_desc)
    EditText mEtDesc;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    @BindView(R.id.recycle_order)
    RecyclerView mRecycleOrder;
    @BindView(R.id.ratingbar)
    SimpleRatingBar mRatingbar;
    private AddPicGridAdapter mAdapter;
    private String order_detail_id;
    private OrderAdapter mOrderAdapter;
    private OrderBean mOrderBean;

    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight)
    {
        tvTitle.setText("商品评论");
        tvRight.setText("提交");
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                evaluate();
            }
        });
    }

    /**
     * 评价
     */
    private void evaluate()
    {
        String content = mEtDesc.getText().toString().trim();
        int star = (int) mRatingbar.getRating();
        if (star == 0)
        {
            showToast("请选择星级");
            return;
        }
        if (TextUtils.isEmpty(content))
        {
            showToast("请输入评价内容");
            return;
        }

        List<AddPicBean> data = mAdapter.getData();
        HashMap<String, String> map = new HashMap();
        map.put("token", JjgConstant.getToken(mContext));
        map.put("order_detail_id", order_detail_id);
        map.put("content", content);
        map.put("star", String.valueOf(star));
        Map<String, File> fileMap = new HashMap<>();
        for (AddPicBean bean : data)
        {
            if (bean.type != bean.TYPE_ADD)
            {
                File file;
                final double size = FileUtils.getFileOrFilesSize(bean.picPath, FileUtils
                        .SIZETYPE_KB);
                if (size > 500)
                {
                    //如果图片大小大于500K,就处理图片大小(系统相机拍的大图)
                    file = new File(ImageUtils.zoomBitmap2File(bean.picPath));
                } else
                {
                    file = new File(bean.picPath);
                }
                fileMap.put(file.getName(), file);
            }
        }
        HttpUtils.postForm(NetConstant.COMMENT, map, "img[]", fileMap, new DialogCallBack(mContext)
        {

            @Override
            public void onResponse(String response, int id)
            {
                LogUtils.d(response);
                BaseResp baseResp = new Gson().fromJson(response, BaseResp.class);
                if (handleResponseCode(baseResp))
                {
                    showToast(baseResp.getMsg(), 1);
                    finish();
                }
            }
        });

    }

    @Override
    protected void init()
    {
        order_detail_id = getIntent().getStringExtra(ArgConstants.ORDER_ID);

        mOrderBean = getIntent().getParcelableExtra("bean");
        ArrayList<OrderBean> list = new ArrayList<>();
        list.add(mOrderBean);
        mRecycler.setLayoutManager(new GridLayoutManager(mContext, 4));
        mOrderAdapter = new OrderAdapter(R.layout.layout_product_item, list);
        mOrderAdapter.setImageLoader(getImageLoader());
        mAdapter = new AddPicGridAdapter(R.layout.item_add_pic_cell, null, mRecycler, ProductReviewsActivity.this);
        mRecycleOrder.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecycleOrder.setNestedScrollingEnabled(false);
        mRecycleOrder.setAdapter(mOrderAdapter);
        mRecycler.setAdapter(mAdapter);
        mAdapter.setNewData(new ArrayList<AddPicBean>());
    }

    @Override
    protected int getLayoutRes()
    {
        return R.layout.activity_product_reviews;
    }

}
