package com.cnsunrun.jiajiagou.home.location;

import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.CZBaseQucikAdapter;
import com.cnsunrun.jiajiagou.map.bean.CloudResultBean;

import java.util.List;

/**
 * Description:
 * Data：2017/8/23 0023-下午 6:12
 * Blog：http://blog.csdn.net/u013983934
 * Author: CZ
 */
public class LocationAreaAdapter extends CZBaseQucikAdapter<CloudResultBean>
{
    public LocationAreaAdapter(int layoutResId,List<CloudResultBean> data)
    {
        super(layoutResId,data);

    }

    @Override
    protected void convert(BaseViewHolder helper, CloudResultBean item)
    {
        helper.setText(R.id.tv_area_name,item.getTitle());
        if (item.isSelect()) {
            helper.setVisible(R.id.iv_item_select,true);
        }else {
            helper.setVisible(R.id.iv_item_select,false);
        }
    }
}
