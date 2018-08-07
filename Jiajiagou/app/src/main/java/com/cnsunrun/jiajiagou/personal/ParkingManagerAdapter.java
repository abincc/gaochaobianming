package com.cnsunrun.jiajiagou.personal;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.CZBaseQucikAdapter;

import java.util.List;

/**
 * Created by ${LiuDi}
 * on 2017/8/25on 10:14.
 */

public class ParkingManagerAdapter extends CZBaseQucikAdapter<ParkingResp.InfoBean>
{
    public ParkingManagerAdapter(int layoutResId, List<ParkingResp.InfoBean> data)
    {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ParkingResp.InfoBean item)
    {
        baseViewHolder.setText(R.id.tv_name, item.getTitle());
        getImageLoader().load(item.getImage()).into(((ImageView) baseViewHolder.getView(R.id.iv_imv)));

    }
}
