package com.cnsunrun.jiajiagou.personal.posts;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.CZBaseQucikAdapter;
import com.cnsunrun.jiajiagou.forum.bean.TabPostsBean;

import java.util.List;

/**
 * Description:
 * Data：2017/8/22 0022-下午 3:17
 * Blog：http://blog.csdn.net/u013983934
 * Author: CZ
 */
public class MyPostsAdapter extends CZBaseQucikAdapter<TabPostsBean.InfoBean>
{
    public MyPostsAdapter(int layoutResId, List<TabPostsBean.InfoBean> data)
    {
        super(layoutResId, data);
    }



    @Override
    protected void convert(BaseViewHolder helper, TabPostsBean.InfoBean item)
    {
        getImageLoader().load(item.getForum_icon()).into((ImageView) helper.getView(R.id.iv_icon));
        helper.setText(R.id.tv_post_title, item.getTitle());
        helper.setText(R.id.tv_plate_name, item.getForum_title());
        helper.setText(R.id.tv_time, item.getAdd_time());
        helper.setText(R.id.tv_read_comment_thumbs, "阅读 "+item.getViews()+"  |  评论 "+item.getReplies()+"  |  点赞 "+item.getLikes());
    }
}
