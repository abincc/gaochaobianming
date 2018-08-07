package com.cnsunrun.jiajiagou.personal.order;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ${LiuDi}
 * on 2017/9/4on 16:46.
 */

public class OrderBean implements Parcelable
{
    /**
     * cart_id : 55
     * sku_id : 11
     * product_id : 7
     * product_title : 鼠粮
     * product_price : 200.00
     * product_spec_value : 白色
     * product_num : 2
     * product_image : http://test.cnsunrun.com/wuye/Uploads/Product/Product/Cover/2017-08-22/599beea3049fb.jpg
     */

    private String cart_id;
    private String sku_id;
    private String product_id;
    private String product_title;
    private String product_price;
    private String product_spec_value;
    private String product_num;
    private String product_image;
    private int comment_status;//评价状态
    private String order_detail_id;
    private String order_id;

    public int getComment_status()
    {
        return comment_status;
    }

    public void setComment_status(int comment_status)
    {
        this.comment_status = comment_status;
    }

    public String getOrder_detail_id()
    {
        return order_detail_id;
    }

    public void setOrder_detail_id(String order_detail_id)
    {
        this.order_detail_id = order_detail_id;
    }

    public String getOrder_id()
    {
        return order_id;
    }

    public void setOrder_id(String order_id)
    {
        this.order_id = order_id;
    }

    public String getCart_id()
    {
        return cart_id;
    }

    public void setCart_id(String cart_id)
    {
        this.cart_id = cart_id;
    }

    public String getSku_id()
    {
        return sku_id;
    }

    public void setSku_id(String sku_id)
    {
        this.sku_id = sku_id;
    }

    public String getProduct_id()
    {
        return product_id;
    }

    public void setProduct_id(String product_id)
    {
        this.product_id = product_id;
    }

    public String getProduct_title()
    {
        return product_title;
    }

    public void setProduct_title(String product_title)
    {
        this.product_title = product_title;
    }

    public String getProduct_price()
    {
        return product_price;
    }

    public void setProduct_price(String product_price)
    {
        this.product_price = product_price;
    }

    public String getProduct_spec_value()
    {
        return product_spec_value;
    }

    public void setProduct_spec_value(String product_spec_value)
    {
        this.product_spec_value = product_spec_value;
    }

    public String getProduct_num()
    {
        return product_num;
    }

    public void setProduct_num(String product_num)
    {
        this.product_num = product_num;
    }

    public String getProduct_image()
    {
        return product_image;
    }

    public void setProduct_image(String product_image)
    {
        this.product_image = product_image;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(this.cart_id);
        dest.writeString(this.sku_id);
        dest.writeString(this.product_id);
        dest.writeString(this.product_title);
        dest.writeString(this.product_price);
        dest.writeString(this.product_spec_value);
        dest.writeString(this.product_num);
        dest.writeString(this.product_image);
        dest.writeInt(this.comment_status);
        dest.writeString(this.order_detail_id);
        dest.writeString(this.order_id);
    }

    public OrderBean()
    {
    }

    protected OrderBean(Parcel in)
    {
        this.cart_id = in.readString();
        this.sku_id = in.readString();
        this.product_id = in.readString();
        this.product_title = in.readString();
        this.product_price = in.readString();
        this.product_spec_value = in.readString();
        this.product_num = in.readString();
        this.product_image = in.readString();
        this.comment_status = in.readInt();
        this.order_detail_id = in.readString();
        this.order_id = in.readString();
    }

    public static final Creator<OrderBean> CREATOR = new Creator<OrderBean>()
    {
        @Override
        public OrderBean createFromParcel(Parcel source)
        {
            return new OrderBean(source);
        }

        @Override
        public OrderBean[] newArray(int size)
        {
            return new OrderBean[size];
        }
    };
}

