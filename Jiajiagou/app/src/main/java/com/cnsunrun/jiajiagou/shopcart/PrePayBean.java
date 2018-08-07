package com.cnsunrun.jiajiagou.shopcart;

/**
 * Created by ${LiuDi}
 * on 2017/9/28on 11:33.
 */

public class PrePayBean
{
    public PrePayBean(String money, String order_id, String order_no, String desc)
    {
        this.money = money;
//        this.money = "0.01";
        this.order_id = order_id;
        this.order_no = order_no;
        this.desc = desc;
    }

    public String money;
    public String order_id;
    public String order_no;
    public String desc;
}
