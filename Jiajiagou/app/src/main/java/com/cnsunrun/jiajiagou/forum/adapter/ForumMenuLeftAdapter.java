package com.cnsunrun.jiajiagou.forum.adapter;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.forum.bean.ForumPlateListBean;

/**
 * 论坛分类主列表Adapter
 * <p>
 * author:yyc
 * date: 2017-08-25 17:45
 */
public class ForumMenuLeftAdapter extends BaseQuickAdapter<ForumPlateListBean.InfoBean, BaseViewHolder> {
    private boolean flag = true;

    public ForumMenuLeftAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }


    @Override
    protected void convert(BaseViewHolder helper, ForumPlateListBean.InfoBean item) {

        //默认选中第一个
        if (flag) {
            helper.setBackgroundRes(R.id.ll_item_left, R.color.white);
            helper.getView(R.id.iv_item_left).setVisibility(View.VISIBLE);
            item.setSelect(true);
            flag = false;
        }
        if (item.isSelect()) {
            helper.getView(R.id.iv_item_left).setVisibility(View.VISIBLE);
            helper.setBackgroundRes(R.id.ll_item_left, R.color.white);
        } else {
            helper.setBackgroundRes(R.id.ll_item_left, R.color.grayF4);
            helper.getView(R.id.iv_item_left).setVisibility(View.INVISIBLE);
        }


        helper.setText(R.id.tv_item_left, item.getTitle());

    }
}
