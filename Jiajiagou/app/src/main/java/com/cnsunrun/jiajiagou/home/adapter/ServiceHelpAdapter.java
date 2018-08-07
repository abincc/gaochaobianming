package com.cnsunrun.jiajiagou.home.adapter;

import android.support.annotation.LayoutRes;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.home.bean.ServiceHelpBean;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-09-08 10:27
 */
public class ServiceHelpAdapter extends BaseQuickAdapter<ServiceHelpBean.InfoBean,BaseViewHolder>{
    public ServiceHelpAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, ServiceHelpBean.InfoBean item) {
        helper.setText(R.id.tv_text,item.getTitle());
    }
}
