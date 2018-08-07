package com.cnsunrun.jiajiagou.personal.setting.address;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ${LiuDi}
 * on 2017/9/4on 16:33.
 */

public class AddressBean implements Parcelable
{
    public String address_id;
    public String name;
    public String mobile;
    public String province;
    public String city;
    public String area;
    public String address_detail;
    public int is_default;
    public String province_title;
    public String city_title;
    public String area_title;

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(this.address_id);
        dest.writeString(this.name);
        dest.writeString(this.mobile);
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.area);
        dest.writeString(this.address_detail);
        dest.writeInt(this.is_default);
        dest.writeString(this.province_title);
        dest.writeString(this.city_title);
        dest.writeString(this.area_title);
    }

    public AddressBean()
    {
    }

    protected AddressBean(Parcel in)
    {
        this.address_id = in.readString();
        this.name = in.readString();
        this.mobile = in.readString();
        this.province = in.readString();
        this.city = in.readString();
        this.area = in.readString();
        this.address_detail = in.readString();
        this.is_default = in.readInt();
        this.province_title = in.readString();
        this.city_title = in.readString();
        this.area_title = in.readString();
    }

    public static final Creator<AddressBean> CREATOR = new Creator<AddressBean>()
    {
        @Override
        public AddressBean createFromParcel(Parcel source)
        {
            return new AddressBean(source);
        }

        @Override
        public AddressBean[] newArray(int size)
        {
            return new AddressBean[size];
        }
    };
}
