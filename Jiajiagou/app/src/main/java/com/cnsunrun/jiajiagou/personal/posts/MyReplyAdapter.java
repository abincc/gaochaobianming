package com.cnsunrun.jiajiagou.personal.posts;

import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.CZBaseQucikAdapter;
import com.cnsunrun.jiajiagou.forum.bean.TabReplyBean;

import java.util.List;

/**
 * Description:
 * Data：2017/8/22 0022-下午 3:29
 * Blog：http://blog.csdn.net/u013983934
 * Author: CZ
 */
public class MyReplyAdapter extends CZBaseQucikAdapter<TabReplyBean.InfoBean>
{
    public MyReplyAdapter(int layoutResId, List<TabReplyBean.InfoBean> data)
    {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TabReplyBean.InfoBean item)
    {
        helper.setText(R.id.tv_post_title, item.getThread_info().getTitle());
        helper.setText(R.id.tv_reply_content, item.getContent());
        helper.setText(R.id.tv_time, item.getAdd_time());
    }
}
