package com.cnsunrun.jiajiagou.personal;

import android.support.annotation.LayoutRes;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.CZBaseQucikAdapter;
import com.cnsunrun.jiajiagou.personal.bean.NearByBean;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-09-14 11:03
 */
public class NearByAdapter extends CZBaseQucikAdapter<NearByBean.InfoBean> {

    public NearByAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, NearByBean.InfoBean item) {

        if(item.getNickname() != null && !item.getNickname().equals("")){
            helper.setText(R.id.tv_near_name,item.getNickname());
        }else{
            helper.setText(R.id.tv_near_name,getNum(item.getMobile()));
        }

        getImageLoader().load(item.getHeadimg()).into(((ImageView) helper.getView(R.id.iv_near_img)));

        helper.setText(R.id.tv_near_distance,item.getMsg().getContent());

        if(item.getSex() == 2){
            helper.setImageResource(R.id.iv_sex_head, R.drawable.woman);
        }

        helper.addOnClickListener(R.id.near_by);
    }

    private String getNum(String mobile) {
        String str = mobile;
        //字符串截取
        String bb =str.substring(3,7);
        //字符串替换
        String cc = str.replace(bb,"****");
        return cc;
    }
}
