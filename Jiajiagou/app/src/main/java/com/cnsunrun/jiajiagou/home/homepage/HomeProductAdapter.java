package com.cnsunrun.jiajiagou.home.homepage;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.CZBaseQucikAdapter;
import com.cnsunrun.jiajiagou.product.ProductBean;

import java.util.List;

/**
 * Description:
 * Data：2017/8/22 0022-下午 4:02
 * Blog：http://blog.csdn.net/u013983934
 * Author: CZ
 */
public class HomeProductAdapter extends CZBaseQucikAdapter<ProductBean>
{
    public HomeProductAdapter(int layoutResId, List<ProductBean> data)
    {
        //super(layoutResId, Arrays.asList(1, 1, 1, 1, 1, 1));
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ProductBean dataBean)
    {
        baseViewHolder.setText(R.id.tv_name, dataBean.getTitle())
                .setText(R.id.tv_desc, dataBean.getDescription())
                .setText(R.id.tv_price, "¥" + dataBean.getPrice())
        ;
        getImageLoader().load(dataBean.getImage())
                .into((ImageView) baseViewHolder.getView(R.id.iv_img));
    }
}
