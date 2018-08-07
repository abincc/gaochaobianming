package com.cnsunrun.jiajiagou.personal.waste;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.CZBaseQucikAdapter;

import java.util.List;

/**
 * Created by ${LiuDi}
 * on 2017/8/25on 15:22.
 */

public class AllWasteOrderAdapter extends CZBaseQucikAdapter<AllWasteOrderResp.InfoBean>
{

    public AllWasteOrderAdapter(int layoutResId, List<AllWasteOrderResp.InfoBean> data)
    {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder baseViewHolder, AllWasteOrderResp.InfoBean dataBean)
    {
        baseViewHolder.setText(R.id.tv_order_id, "订单编号: " + dataBean.getOrder_no().substring(0,4) + dataBean.getOrder_no().substring(8, 16))
                .setText(R.id.tv_time, "下单时间：" + dataBean.getAdd_time())
                .setText(R.id.tv_state, dataBean.getStatus_title())
                .setText(R.id.tv_total_price, "预约时间：" + dataBean.getDate())
                .addOnClickListener(R.id.tv_confirm_receipt);


        int status = dataBean.getStatus();

        TextView right = baseViewHolder.getView(R.id.tv_confirm_receipt);
        baseViewHolder.setVisible(R.id.tv_confirm_receipt, status == 10 || status == 20 || status == 30)
                .setVisible(R.id.rl_1, status == 10);

// 4
//10-（用户确认收货）未处理 20-已处理  30-已取消
       switch (status)
        {

            case 10:
                right.setText("取消订单");
                break;
            case 20:
                right.setText("已处理");
                break;
            case 30:
                right.setText("已取消");
                break;
        }

    }
}
