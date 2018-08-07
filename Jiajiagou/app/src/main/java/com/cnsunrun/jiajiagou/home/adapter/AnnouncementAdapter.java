package com.cnsunrun.jiajiagou.home.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.home.bean.NoticeBean;

/**
 * Created by j2yyc on 2018/1/24.
 */

public class AnnouncementAdapter extends BaseQuickAdapter<NoticeBean,BaseViewHolder> {
    public AnnouncementAdapter() {
        super(R.layout.item_announcement);
    }

    @Override
    protected void convert(BaseViewHolder helper, NoticeBean item) {
        helper.setText(R.id.tv_title,item.title);
    }
}
