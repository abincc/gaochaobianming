package com.cnsunrun.jiajiagou.personal;

import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.CZBaseQucikAdapter;
import com.cnsunrun.jiajiagou.home.bean.NoticeBean;

import java.util.List;

/**
 * Created by ${LiuDi}
 * on 2017/9/1on 14:22.
 */

public class NoticeAdapter extends CZBaseQucikAdapter<NoticeBean>
{
    public NoticeAdapter(int layoutResId, List<NoticeBean> data)
    {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NoticeBean item)
    {
        helper.setText(R.id.tv_content, item.title)
                .setText(R.id.tv_time, item.add_time);


    }
}
