package com.cnsunrun.jiajiagou.home.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.home.bean.DiscussDetailBean;

import java.util.List;

/**
 * Created by wangchao on 2018-06-19.
 */
public class TypeListAdapter extends BaseQuickAdapter<DiscussDetailBean.InfoBean.TypeListBean,BaseViewHolder>{


    public TypeListAdapter(@Nullable List<DiscussDetailBean.InfoBean.TypeListBean> data) {
        super(R.layout.item_type_list,data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, DiscussDetailBean.InfoBean.TypeListBean typeListBean) {
        baseViewHolder.setText(R.id.tv_type_name,typeListBean.name);
        baseViewHolder.addOnClickListener(R.id.tv_type_name);
    }
}
