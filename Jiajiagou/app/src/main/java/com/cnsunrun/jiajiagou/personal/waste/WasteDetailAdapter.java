package com.cnsunrun.jiajiagou.personal.waste;

import android.support.annotation.LayoutRes;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseAdapter;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-11-24 17:06
 */
public class WasteDetailAdapter extends BaseAdapter<WasteDetailBean.InfoBean> {

    public WasteDetailAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, WasteDetailBean.InfoBean item) {
        getImageLoader().load(item.getImage()).into((ImageView) helper.getView(R.id.iv_waste));
    }
}
