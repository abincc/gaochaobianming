package com.cnsunrun.jiajiagou.personal.posts;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.CZBaseQucikAdapter;
import com.cnsunrun.jiajiagou.forum.bean.TabLikesBean;

import java.util.List;

/**
 * Description:
 * Data：2017/8/22 0022-下午 3:29
 * Blog：http://blog.csdn.net/u013983934
 * Author: CZ
 */
public class MyThumbsUpAdapter extends CZBaseQucikAdapter<TabLikesBean.InfoBean>
{
    public MyThumbsUpAdapter(int layoutResId, List<TabLikesBean.InfoBean> data)
    {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TabLikesBean.InfoBean item)
    {
        getImageLoader().load(item.getForum_icon()).into((ImageView) helper.getView(R.id.iv_icon));
        helper.setText(R.id.tv_time,item.getAdd_time());
        helper.setText(R.id.tv_plate_name,item.getForum_title());
        helper.setText(R.id.tv_post_title,item.getTitle());
    }
}
