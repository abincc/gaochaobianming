package com.cnsunrun.jiajiagou.personal;

import android.text.Html;

import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.CZBaseQucikAdapter;

import java.util.List;

/**
 * Created by ${LiuDi}
 * on 2017/11/3on 16:38.
 */

public class CouponAdapter extends CZBaseQucikAdapter<CouponResp.InfoBean>
{
    public CouponAdapter(int layoutResId, List<CouponResp.InfoBean> data)
    {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CouponResp.InfoBean dataBean)
    {
        helper.setText(R.id.tv_name, dataBean.title)
                .setText(R.id.tv_time, dataBean.end_date + "前使用")
                .setText(R.id.tv_desc, dataBean.description)
                .setText(R.id.tv_miniTitle, dataBean.minimum_title)
                .setTextColor(R.id.tv_money, dataBean.status != 1 ? 0xff333333 : 0xfffc4f3f)
                .setText(R.id.tv_money, Html.fromHtml("<small><small> <small>¥ </small></small></small>" + dataBean.amount));
    }
}
