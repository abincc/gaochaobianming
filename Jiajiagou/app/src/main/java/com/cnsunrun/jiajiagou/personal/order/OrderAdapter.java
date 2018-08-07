package com.cnsunrun.jiajiagou.personal.order;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.CZBaseQucikAdapter;

import java.util.Collection;
import java.util.List;

/**
 * Created by ${LiuDi}
 * on 2017/8/25on 15:07.
 */

public class OrderAdapter extends CZBaseQucikAdapter<OrderBean>
{
    boolean isWhite;
    boolean isEvaluate;
    List<OrderBean> data;

    public OrderAdapter(int layoutResId, List<OrderBean> data)
    {
        super(layoutResId, data);
    }

    @Override
    public void setNewData(List<OrderBean> data)
    {
        this.data = data;
        super.setNewData(data);
    }

    @Override
    public void addData(@NonNull Collection<? extends OrderBean> newData)
    {
        if (data != null)
            data.addAll(newData);
        super.addData(newData);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, OrderBean dataBean)
    {
        if (isWhite)
        {
            baseViewHolder.getConvertView().setBackgroundColor(0xffffffff);
        }
        if (isEvaluate)
        {
            TextView tv_comment = (TextView) baseViewHolder.getView(R.id.tv_evaluate);
            baseViewHolder.setVisible(R.id.tv_evaluate, true)
                    .setVisible(R.id.tv_new_count, true)
                    .setVisible(R.id.tv_count, false)
                    .setText(R.id.tv_new_count, "x" + dataBean.getProduct_num());
            if (dataBean.getComment_status() == 1)
            {
                tv_comment.setText("已评价");
                tv_comment.setClickable(false);
                tv_comment.setBackground(null);
            } else
            {
                tv_comment.setText("评价");
                tv_comment.setClickable(true);
                tv_comment.setBackgroundResource(R.drawable.shape_blue_border);
            }

        }
        baseViewHolder.setText(R.id.tv_name, dataBean.getProduct_title())
                .setText(R.id.tv_describe, dataBean.getProduct_spec_value())
                .setText(R.id.tv_price, "¥ " + dataBean.getProduct_price())
                .setText(R.id.tv_count, "x" + dataBean.getProduct_num())
                .addOnClickListener(R.id.tv_evaluate);
        if (!TextUtils.isEmpty(dataBean.getProduct_image())) {
            if (getImageLoader()==null) {
                LogUtils.i("getImageLoader  ","null");
            }
            getImageLoader().load(dataBean.getProduct_image()).into(((ImageView) baseViewHolder.getView(R.id.iv_imv)));
        }
        if (data != null)
        {
            LogUtils.d(data.size());
            LogUtils.d("position" + baseViewHolder.getPosition());
            LogUtils.d(baseViewHolder.getAdapterPosition() == data.size() - 1);
            View view = baseViewHolder.getView(R.id.line);
            if (baseViewHolder.getAdapterPosition() == (data.size() - 1))
            {
                view.setVisibility(View.GONE);
            } else
            {
                view.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setWhiteBackGround()
    {
        isWhite = true;
    }

    public void setIsEvaluate(boolean isEvaluate)
    {
        this.isEvaluate = isEvaluate;
    }
}
