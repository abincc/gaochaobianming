package com.cnsunrun.jiajiagou.personal.setting.address;

import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.KeyboardUtils;
import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.RegexUtils;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseHeaderActivity;
import com.cnsunrun.jiajiagou.common.base.BaseResp;
import com.cnsunrun.jiajiagou.common.constant.ArgConstants;
import com.cnsunrun.jiajiagou.common.constant.JjgConstant;
import com.cnsunrun.jiajiagou.common.network.DialogCallBack;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.widget.view.LeftEdittext;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ${LiuDi}
 * on 2017/8/19on 16:05.
 */

public class AddAddressActivity extends BaseHeaderActivity implements OnAddressSelectedListener
{
    @BindView(R.id.ll_container)
    LinearLayout mLlContainer;
    @BindView(R.id.tv_address)
    TextView mTvAddress;
    @BindView(R.id.et_name)
    LeftEdittext mEtName;
    @BindView(R.id.et_number)
    LeftEdittext mEtNumber;
    @BindView(R.id.et_address)
    EditText mEtAddress;

    @BindView(R.id.checkbox)
    CheckBox mCheckbox;
    private List<AreaBean> mList;
    private AddressPop mAddressPop;

    private String province;
    private String city;
    private String area;
    private AddressBean mInfoBean;

    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight)
    {
        tvTitle.setText(mInfoBean == null ? "新增地址" : "编辑地址");
        tvRight.setText("保存");
        tvRight.setVisibility(View.VISIBLE);

        tvRight.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                addAddress();
            }
        });
    }

    private void addAddress()
    {
        String name = mEtName.getText().toString().trim();
        String number = mEtNumber.getText().toString().trim();
        String address = mEtAddress.getText().toString().trim();
        if (TextUtils.isEmpty(name))
        {
            showToast("请输入姓名");
            return;
        }
        if (TextUtils.isEmpty(number) || !RegexUtils.isMobileSimple(number))
        {
            showToast("请输入手机号");
            return;
        }
        if (TextUtils.isEmpty(address))
        {
            showToast("请输入详细地址");
            return;
        }
        if (TextUtils.isEmpty(province))
        {
            showToast("请选择您所在的地区");
            return;
        }
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", JjgConstant.getToken(mContext));
        hashMap.put("name", name);
        hashMap.put("mobile", number);
        hashMap.put("province", province);
        hashMap.put("city", city);
        hashMap.put("area", area);
        hashMap.put("address_detail", address);
        hashMap.put("is_default", mCheckbox.isChecked() ? "1" : "0");

        if (mInfoBean == null)
        {
            HttpUtils.post(NetConstant.ADDRESS_ADD, hashMap, new DialogCallBack(mContext)
            {
                @Override
                public void onResponse(String response, int id)
                {
                    LogUtils.d(response);
                    BaseResp baseResp = new Gson().fromJson(response, BaseResp.class);
                    if (handleResponseCode(baseResp))
                    {
                        showToast(baseResp.getMsg(), 1);
                        setResult(200);
                        finish();
                    }
                }
            });
        } else
        {
            hashMap.put("address_id", mInfoBean.address_id);
            HttpUtils.post(NetConstant.ADDRESS_EDITOR, hashMap, new DialogCallBack(mContext)
            {
                @Override
                public void onResponse(String response, int id)
                {
                    LogUtils.d(response);
                    BaseResp baseResp = new Gson().fromJson(response, BaseResp.class);
                    if (handleResponseCode(baseResp))
                    {
                        showToast(baseResp.getMsg(), 1);
                        setResult(200);
                        finish();
                    }
                }
            });

        }


    }

    @Override
    protected void init()
    {
        Intent intent = getIntent();
        mInfoBean = intent.getParcelableExtra(ArgConstants.AREABEAN);


        if (mInfoBean != null)
        {
            mEtName.setText(mInfoBean.name);
            mEtNumber.setText(mInfoBean.mobile);
            mTvAddress.setText(mInfoBean.province_title + mInfoBean.city_title + mInfoBean.area_title);
            mEtAddress.setText(mInfoBean.address_detail);

            province = mInfoBean.province;
            city = mInfoBean.city;
            area = mInfoBean.area;
            mCheckbox.setChecked(mInfoBean.is_default == 1);
        }
    }

    @Override
    protected int getLayoutRes()
    {
        return R.layout.activity_add_address;
    }


    @OnClick(R.id.rl_area)
    public void onViewClicked()
    {
        KeyboardUtils.hideSoftInput(AddAddressActivity.this);
        if (mAddressPop == null)
        {
            mAddressPop = new AddressPop(mContext);
            mAddressPop.setOnAddressSelectListener(this);
        }
        mAddressPop.showAtLocation(mLlContainer, Gravity.BOTTOM, 0, 0);

    }

    @Override
    public void addressSelect(String princeId, String cityId, String areaId, String name)
    {

        mTvAddress.setText(name);
        province = princeId;
        city = cityId;
        area = areaId;
    }

}
