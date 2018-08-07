package com.cnsunrun.jiajiagou.personal.setting.address;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.blankj.utilcode.utils.LogUtils;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseCallBack;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.ToastUtils;
import com.google.gson.Gson;
import com.yiguo.adressselectorlib.AddressSelector;
import com.yiguo.adressselectorlib.CityInterface;
import com.yiguo.adressselectorlib.OnItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ${LiuDi}
 * on 2017/9/2on 12:07.
 */

public class AddressPop extends PopupWindow implements AddressSelector.OnTabSelectedListener
{
    Context mContext;
    @BindView(R.id.address)
    AddressSelector mAddress;
    private ArrayList<AreaBean> mInfo;
    private String mId = "0";
    private String princeId;
    private String cityId;
    private String areaId;
    private String princeName;
    private String cityName;
    private String areaName;

    public AddressPop(Context context)
    {
        mContext = context;

        this.setWindowLayoutMode(-1, -1);
        this.setAnimationStyle(R.style.umeng_socialize_shareboard_animation);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setOutsideTouchable(true);
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.layout_address_pop, null);
        setContentView(contentView);
        ButterKnife.bind(this, contentView);

        init();


    }

    public void setOnAddressSelectListener(OnAddressSelectedListener listener)
    {
        this.mListener = listener;
    }

    private void getData(String id)
    {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("pid", id);
        HttpUtils.get(NetConstant.AREA, hashMap, new BaseCallBack(mContext)
        {
            @Override
            public void onResponse(String response, int id)
            {
                LogUtils.d(response);
                AddAddressResp resp = new Gson().fromJson(response, AddAddressResp.class);
                if (handleResponseCode(resp))
                {
                    mInfo = (ArrayList<AreaBean>) resp.getInfo();
                    mAddress.setCities(mInfo);
                }
            }
        });
    }

    private void init()
    {
        getData(mId);

        mAddress.setOnTabSelectedListener(this);
        mAddress.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void itemClick(AddressSelector addressSelector, CityInterface cityInterface, int i)
            {
                AreaBean areaBean = (AreaBean) cityInterface;
                mId = areaBean.getId();
                String name = areaBean.getTitle();
                LogUtils.d(mId);
                switch (i)
                {
                    case 0:
                        princeId = mId;
                        princeName = name;

                        getData(mId);
                        break;
                    case 1:
                        cityId = mId;

                        cityName = name;
                        getData(mId);
//                        mAddress.setCities(mInfo);
//                        addressSelector.setCities(cities3);
                        break;
                    case 2:
                        areaName = name;
                        areaId = mId;
//                        Toast.makeText(MainActivity.this,"tabPosition ："+tabPosition+" "+city.getCityName(),Toast
// .LENGTH_SHORT).show();
                        break;
                }

            }
        });
    }

    private OnAddressSelectedListener mListener;

    @Override
    public void onTabSelected(AddressSelector addressSelector, AddressSelector.Tab tab)
    {
        int index = tab.getIndex();
        switch (index)
        {
            case 0:
                mId = "0";
                cityId = null;
                areaId = null;
                LogUtils.d("00000");
                getData(mId);
                break;
            case 1:
                areaId = null;
                getData(princeId);
                LogUtils.d("111");
                break;

            case 2:
//                getData(mId);
                LogUtils.d("222");
                break;

        }
    }

    @Override
    public void onTabReselected(AddressSelector addressSelector, AddressSelector.Tab tab)
    {
//        int index = tab.getIndex();
//        switch (index)
//        {
//            case 0:
//                cityId=null;
//                areaId=null;
//                LogUtils.d("0000");
//                break;
//            case 1:
//                areaId=null;
//                LogUtils.d("111");
//                break;
//            case 2:
//                LogUtils.d("2222");
//                break;


    }

    @OnClick({R.id.gray_zone, R.id.btn_confirm})
    public void onViewClicked(View view)
    {
        switch (view.getId())
        {
            case R.id.gray_zone:
                this.dismiss();
                break;
            case R.id.btn_confirm:
                LogUtils.d("princeId" + princeId);
                LogUtils.d("cityID" + cityId);
                LogUtils.d("areaid" + areaId);

                if (TextUtils.isEmpty(princeId))
                {
                    ToastUtils.show(mContext, "请选择省份");
                    return;
                }
                if (TextUtils.isEmpty(cityId))
                {
                    ToastUtils.show(mContext, "请选择城市");
                    return;
                }
                if (TextUtils.isEmpty(areaId))
                {
                    ToastUtils.show(mContext, "请选择区县");
                    return;
                }

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(princeName).append(cityName).append(areaName);
                if (mListener != null)
                {
                    mListener.addressSelect(princeId, cityId, areaId, stringBuilder.toString());
                    dismiss();
                }
                break;
        }
    }
}
