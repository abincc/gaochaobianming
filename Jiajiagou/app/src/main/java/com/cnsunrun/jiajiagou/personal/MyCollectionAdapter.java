package com.cnsunrun.jiajiagou.personal;

import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.CZBaseQucikAdapter;
import com.cnsunrun.jiajiagou.common.constant.ArgConstants;
import com.cnsunrun.jiajiagou.product.ProductDetailActivity;

import java.util.List;

/**
 * Created by ${LiuDi}
 * on 2017/8/24on 14:31.
 */

public class MyCollectionAdapter extends CZBaseQucikAdapter<MyCollectionResp.InfoBean>
{
    boolean isEditor = false;

    public MyCollectionAdapter(int layoutResId, List<MyCollectionResp.InfoBean> data)
    {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final MyCollectionResp.InfoBean dataBean)
    {
        baseViewHolder.setText(R.id.tv_collect_title, dataBean.title)
                .setText(R.id.tv_collect_subTitle, dataBean.description)
                .setText(R.id.tv_collect_price, "¥" + dataBean.price);
        getImageLoader().load(dataBean.image).into(((ImageView) baseViewHolder.getView(R.id.iv_collect)));

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



        final CheckBox checkBox = baseViewHolder.getView(R.id.checkbox);
        final int position = baseViewHolder.getAdapterPosition();
        checkBox.setVisibility(isEditor ? View.VISIBLE : View.GONE);

        checkBox.setChecked(dataBean.isChecked);
        checkBox.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dataBean.isChecked = checkBox.isChecked();
            }
        });


    }

    public void editor(boolean isEditor)
    {
        this.isEditor = isEditor;
        notifyDataSetChanged();
    }

}
