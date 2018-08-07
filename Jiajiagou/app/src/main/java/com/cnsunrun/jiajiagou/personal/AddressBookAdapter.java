package com.cnsunrun.jiajiagou.personal;

import android.support.annotation.LayoutRes;

import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.CZBaseQucikAdapter;
import com.cnsunrun.jiajiagou.personal.bean.AddressBookBean;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-09-14 11:03
 */
public class AddressBookAdapter extends CZBaseQucikAdapter<AddressBookBean.InfoBean> {
    public AddressBookAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, AddressBookBean.InfoBean item) {
        helper.setText(R.id.tv_phone_number,item.getNumber());
        helper.setText(R.id.tv_phone_name,item.getTitle());
        helper.setText(R.id.tv_phone_address,item.getAddress());
        helper.addOnClickListener(R.id.iv_dial);
    }
}
