package com.cnsunrun.jiajiagou.home.location;

import android.support.annotation.LayoutRes;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.map.bean.CloudResultBean;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-09-15 19:14
 */
public class SearchCommunityAdapter extends BaseQuickAdapter<CloudResultBean,BaseViewHolder>{
    public SearchCommunityAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, CloudResultBean item) {
        helper.setText(R.id.tv_area_name,item.getTitle());
        if (item.isSelect()) {
            helper.setVisible(R.id.iv_item_select,true);
        }else {
            helper.setVisible(R.id.iv_item_select,false);
        }
    }
}
