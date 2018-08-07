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

public class LittleHelpAdapter extends CZBaseQucikAdapter<HelpBean>
{

    public LittleHelpAdapter(int layoutResId, List<HelpBean> data)
    {
        super(layoutResId, data);

    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, HelpBean dataBean)
    {
        baseViewHolder.setText(R.id.tv_content, dataBean.content)
                .setText(R.id.tv_name, dataBean.trifle_service_title + " ·")
                .setText(R.id.tv_dispose, dataBean.status_title)
                .setText(R.id.tv_time, dataBean.add_time)
                .setVisible(R.id.btn_cancel, dataBean.status == 1)
                .addOnClickListener(R.id.btn_cancel);
        getImageLoader().load(dataBean.image).into(((ImageView) baseViewHolder.getView(R.id.iv_imv)));

        if(!JjgConstant.isProperty(mContext)){
            baseViewHolder.setVisible(R.id.btn_cancel, false);
        }
    }
}
