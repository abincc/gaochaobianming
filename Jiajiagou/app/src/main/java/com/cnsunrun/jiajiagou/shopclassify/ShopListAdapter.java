package com.cnsunrun.jiajiagou.shopclassify;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.CZBaseQucikAdapter;

import java.util.List;

/**
 * Created by ${LiuDi}
 * on 2017/8/21on 15:55.
 */

public class ShopListAdapter extends CZBaseQucikAdapter<ClassifyResp.InfoBean.ChildBean>
{
    public ShopListAdapter(int layoutResId, List<ClassifyResp.InfoBean.ChildBean> data)
    {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ClassifyResp.InfoBean.ChildBean dataBean)
    {
        baseViewHolder.setText(R.id.tv_name, dataBean.getTitle());

        getImageLoader().load(dataBean.getIcon()).into((ImageView) baseViewHolder.getView(R.id.iv_imv));

    }
}
