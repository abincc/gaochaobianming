package com.cnsunrun.jiajiagou.personal.order;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.CZBaseQucikAdapter;

import java.util.List;

/**
 * Created by ${LiuDi}
 * on 2017/8/25on 15:22.
 */

public class AllOrderAdapter extends CZBaseQucikAdapter<AllOrderResp.InfoBean>
{

    public AllOrderAdapter(int layoutResId, List<AllOrderResp.InfoBean> data)
    {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, AllOrderResp.InfoBean dataBean)
    {
        baseViewHolder.setText(R.id.tv_order_id, "订单编号: " + dataBean.getOrder_no())
                .setText(R.id.tv_time, "下单时间：" + dataBean.getAdd_time())
                .setText(R.id.tv_state, dataBean.getStatus_title())
                .setText(R.id.tv_total_price, "总金额：" + dataBean.getMoney_total() + "元")
                .addOnClickListener(R.id.tv_reimburse)
                .addOnClickListener(R.id.tv_confirm_receipt);

        RecyclerView recyclerView = (RecyclerView) baseViewHolder.getView(R.id.recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        OrderAdapter adapter = new OrderAdapter(R.layout.layout_product_item, null);
        adapter.setImageLoader(getImageLoader());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
        adapter.setNewData(dataBean.getProduct_info());


        int status = dataBean.getStatus();

        TextView left = baseViewHolder.getView(R.id.tv_reimburse);
        TextView right = baseViewHolder.getView(R.id.tv_confirm_receipt);
        baseViewHolder.setVisible(R.id.tv_reimburse, status == 10)
                .setVisible(R.id.tv_confirm_receipt, status == 10 || status == 20 || status == 30 || status == 40)
                .setVisible(R.id.rl_1, status != 5 && status != 50 && status != 60 && status != 70);

//        5-已取消 10-待支付  20-已付款（待系统确认、待收货）30-待收货（系统已确认）
// 40-（用户确认收货）待评价 50-已完成  60-申请退款中 70-已退款
        switch (status)
        {

            case 10:
                left.setText("取消订单");
                right.setText("去支付");
                break;
            case 20:
                right.setText("申请退款");
                break;
            case 30:
                right.setText("确认收货");
                break;
            case 40:
                right.setText("去评价");
                break;

        }

    }
}
