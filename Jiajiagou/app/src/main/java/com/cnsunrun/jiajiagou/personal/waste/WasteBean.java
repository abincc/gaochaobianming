package com.cnsunrun.jiajiagou.personal.waste;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ${LiuDi}
 * on 2017/9/4on 16:46.
 */

public class WasteBean implements Parcelable
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

    private int id;
    private String image;
    private int pid;

   public void setId(int id){ this.id = id;}

   public int getId(){return id;}

   public void setPid(int pid){ this.pid = pid;}

   public int getPid(){return pid;}

   public void setImage(String image){ this.image = image;}

   public String getImage(){ return image;}

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(this.id);
        dest.writeString(this.image);
        dest.writeInt(this.pid);
    }

    public WasteBean()
    {
    }

    protected WasteBean(Parcel in)
    {
        this.id = in.readInt();
        this.image = in.readString();
        this.pid = in.readInt();
    }

    public static final Creator<WasteBean> CREATOR = new Creator<WasteBean>()
    {
        @Override
        public WasteBean createFromParcel(Parcel source)
        {
            return new WasteBean(source);
        }

        @Override
        public WasteBean[] newArray(int size)
        {
            return new WasteBean[size];
        }
    };
}

