package com.cnsunrun.jiajiagou.personal.logistics;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.CZBaseQucikAdapter;
import com.cnsunrun.jiajiagou.common.constant.JjgConstant;

import java.util.List;

/**
 * Created by ${LiuDi}
 * on 2017/8/23on 15:10.
 */

public class CleaningAdapter extends CZBaseQucikAdapter<CleanBean>
{

    public CleaningAdapter(int layoutResId, List<CleanBean> data)
    {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, CleanBean dataBean)
    {
        baseViewHolder.setText(R.id.tv_content, dataBean.getContent())
                .setText(R.id.tv_name, dataBean.getClean_service_title() + " ·")
                .setText(R.id.tv_dispose, dataBean.getStatus_title())
                .setText(R.id.tv_time, dataBean.getAdd_time())
                .setVisible(R.id.btn_cancel, dataBean.getStatus() == 1)
                .addOnClickListener(R.id.btn_cancel);
        getImageLoader().load(dataBean.getImage()).into(((ImageView) baseViewHolder.getView(R.id.iv_imv)));

        if(!JjgConstant.isProperty(mContext)){
            baseViewHolder.setVisible(R.id.btn_cancel, false);
        }
    }
}
