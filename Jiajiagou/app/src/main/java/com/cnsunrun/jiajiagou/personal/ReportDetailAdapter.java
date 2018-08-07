package com.cnsunrun.jiajiagou.personal;

import android.support.annotation.LayoutRes;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseAdapter;
import com.cnsunrun.jiajiagou.personal.bean.ReportDetailBean;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-11-24 17:06
 */
public class ReportDetailAdapter extends BaseAdapter<ReportDetailBean.InfoBean.ImagesBean> {

    public ReportDetailAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, ReportDetailBean.InfoBean.ImagesBean item) {
        getImageLoader().load(item.getImage()).into((ImageView) helper.getView(R.id.iv_report));
    }
}
