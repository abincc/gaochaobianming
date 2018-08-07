package com.cnsunrun.jiajiagou.personal;

import android.support.annotation.LayoutRes;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.personal.bean.MessageBean;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-09-16 17:33
 */
public class MessageAdapter extends BaseQuickAdapter<MessageBean.InfoBean,BaseViewHolder> {
    public MessageAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageBean.InfoBean item) {
        helper.setText(R.id.tv_message_title,item.getTitle())
                .setText(R.id.tv_message_date,item.getAdd_time());
    }
}