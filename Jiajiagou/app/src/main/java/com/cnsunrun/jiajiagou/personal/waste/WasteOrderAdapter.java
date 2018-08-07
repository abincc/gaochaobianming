package com.cnsunrun.jiajiagou.personal.waste;

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

public class WasteOrderAdapter extends CZBaseQucikAdapter<WasteBean>
{
    boolean isWhite;
    boolean isEvaluate;
    List<WasteBean> data;

    public WasteOrderAdapter(int layoutResId, List<WasteBean> data)
    {
        super(layoutResId, data);
    }

    @Override
    public void setNewData(List<WasteBean> data)
    {
        this.data = data;
        super.setNewData(data);
    }

    @Override
    public void addData(@NonNull Collection<? extends WasteBean> newData)
    {
        if (data != null)
            data.addAll(newData);
        super.addData(newData);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, WasteBean dataBean)
    {
        if (isWhite)
        {
            baseViewHolder.getConvertView().setBackgroundColor(0xffffffff);
        }
        if (isEvaluate)
        {
            TextView tv_comment = (TextView) baseViewHolder.getView(R.id.tv_evaluate);
//            baseViewHolder.setVisible(R.id.tv_evaluate, true)
//                    .setVisible(R.id.tv_new_count, true)
//                    .setVisible(R.id.tv_count, false)
//                    .setText(R.id.tv_new_count, "x" + dataBean.getProduct_num());

        }
//        baseViewHolder.setText(R.id.tv_name, dataBean.getProduct_title())
//                .setText(R.id.tv_describe, dataBean.getProduct_spec_value())
//                .setText(R.id.tv_price, "¥ " + dataBean.getProduct_price())
//                .setText(R.id.tv_count, "x" + dataBean.getProduct_num())
//                .addOnClickListener(R.id.tv_evaluate);
        if (!TextUtils.isEmpty(dataBean.getImage())) {
            if (getImageLoader()==null) {
                LogUtils.i("getImageLoader  ","null");
            }
            getImageLoader().load(dataBean.getImage()).into(((ImageView) baseViewHolder.getView(R.id.iv_imv)));
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
