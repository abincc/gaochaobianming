package com.cnsunrun.jiajiagou.personal;

import android.support.annotation.LayoutRes;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.personal.bean.ReportListBean;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-09-14 11:03
 */
public class ReportListAdapter extends BaseQuickAdapter<ReportListBean.InfoBean,BaseViewHolder> {
    public ReportListAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, ReportListBean.InfoBean item) {
        helper.setText(R.id.tv_date,item.getTitle());
    }
}
