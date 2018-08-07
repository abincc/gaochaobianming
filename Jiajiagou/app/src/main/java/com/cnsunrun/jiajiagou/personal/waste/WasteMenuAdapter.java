package com.cnsunrun.jiajiagou.personal.waste;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.CZBaseQucikAdapter;

import java.util.List;

/**
 * Created by ${LiuDi}
 * on 2017/8/25on 10:14.
 */

public class WasteMenuAdapter extends CZBaseQucikAdapter<WasteResp.InfoBean>
{
    public WasteMenuAdapter(int layoutResId, List<WasteResp.InfoBean> data)
    {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, WasteResp.InfoBean item)
    {
        baseViewHolder.setText(R.id.tv_name, item.getTitle() + "(" + item.getPrice() + "元/" + item.getUnit() + ")");
        getImageLoader().load(item.getImage()).into(((ImageView) baseViewHolder.getView(R.id.iv_imv)));

    }

}
