package com.cnsunrun.jiajiagou.personal;

import android.support.annotation.LayoutRes;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.personal.bean.ForumMessageBean;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-09-14 11:03
 */
public class ForumMessageAdapter extends BaseQuickAdapter<ForumMessageBean.InfoBean,BaseViewHolder> {
    public ForumMessageAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, ForumMessageBean.InfoBean item) {
        helper.setText(R.id.tv_message_title,item.getThread_title())
                .setText(R.id.tv_message_date,item.getAdd_time());
    }
}
