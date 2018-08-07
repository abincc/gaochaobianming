package com.cnsunrun.jiajiagou.home.homepage;

import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.CZBaseQucikAdapter;
import com.cnsunrun.jiajiagou.home.bean.RecommentThreadBean;

/**
 * Description:
 * Data：2017/8/22 0022-下午 4:41
 * Blog：http://blog.csdn.net/u013983934
 * Author: CZ
 */
public class HomeForumAdapter extends CZBaseQucikAdapter<RecommentThreadBean.InfoBean>
{
    private int height;
    public HomeForumAdapter(int layoutResId)
    {
        super(layoutResId);
    }


    public void setImageHeight(int height){
        this.height=height;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, RecommentThreadBean.InfoBean dataBean)
    {

        ImageView imageView = baseViewHolder.getView(R.id.iv_img);
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        params.height=height;
        imageView.setLayoutParams(params);
        getImageLoader().load(dataBean.getImage()).into(imageView);
        baseViewHolder.setText(R.id.tv_title, dataBean.getTitle());
        String statistice="阅读  "+dataBean.getViews()+"  |  "+"评论  "+dataBean.getReplies()+"  |  "+"点赞  "+dataBean.getLikes();
        baseViewHolder.setText(R.id.tv_statistics, statistice);
        baseViewHolder.setText(R.id.tv_date, dataBean.getAdd_time());

    }

}
