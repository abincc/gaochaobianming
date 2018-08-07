package com.cnsunrun.jiajiagou.personal.waste;

import com.cnsunrun.jiajiagou.common.base.BaseResp;
import com.cnsunrun.jiajiagou.personal.order.OrderBean;
import com.cnsunrun.jiajiagou.personal.setting.address.AddressBean;

import java.util.List;

/**
 * Created by ${LiuDi}
 * on 2017/9/7on 10:26.
 */

public class WasteOrderDetailResp extends BaseResp
{

    private InfoBean info;

    public InfoBean getInfo()
    {
        return info;
    }

    public void setInfo(InfoBean info)
    {
        this.info = info;
    }

    public static class InfoBean
    {

        private String id;
        private String order_no;
        private String add_time;
        private String status_title;
        private int status;
        private String date;
        private AddressBean address;

        public int getStatus()
        {
            return status;
        }

        public void setStatus(int status)
        {
            this.status = status;
        }

        private List<OrderBean> product_info;

        public String getId()
        {
            return id;
        }

        public void setId(String id)
        {
            this.id = id;
        }

        public String getOrder_no()
        {
            return order_no;
        }

        public void setOrder_no(String order_no)
        {
            this.order_no = order_no;
        }

        public String getAdd_time()
        {
            return add_time;
        }

        public void setAdd_time(String add_time)
        {
            this.add_time = add_time;
        }

        public String getStatus_title()
        {
            return status_title;
        }

        public void setStatus_title(String status_title)
        {
            this.status_title = status_title;
        }

        public String getDate(){return date;}

        public void setDate(){this.date = date;}

        public AddressBean getAddress()
        {
            return address;
        }

        public void setAddress(AddressBean address)
        {
            this.address = address;
        }

    }
}
