package com.cnsunrun.jiajiagou.personal.setting.address;

import android.view.View;
import android.widget.CheckBox;

import com.blankj.utilcode.utils.LogUtils;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseCallBack;
import com.cnsunrun.jiajiagou.common.base.BaseResp;
import com.cnsunrun.jiajiagou.common.base.CZBaseQucikAdapter;
import com.cnsunrun.jiajiagou.common.constant.JjgConstant;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ${LiuDi}
 * on 2017/8/22on 15:02.
 */

public class AddressAdapter extends CZBaseQucikAdapter<AddressBean>
{
    private int preChecked = -1;
    public void setPreChecked(int preChecked)
    {
        this.preChecked = preChecked;
    }


    public AddressAdapter(int layoutResId, List<AddressBean> data)
    {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final AddressBean dataBean)
    {
        baseViewHolder.setText(R.id.tv_name, dataBean.name)
                .setText(R.id.tv_phone, dataBean.mobile)
                .setText(R.id.tv_loc, dataBean.address_detail)
                .addOnClickListener(R.id.tv_delete)
                .addOnClickListener(R.id.tv_editor);

        final CheckBox checkBox = baseViewHolder.getView(R.id.checkbox);
        final int position = baseViewHolder.getAdapterPosition();

        if (dataBean.is_default == 1 && preChecked == -1)
        {
            preChecked = position;
        }
        if (preChecked != position)
        {
            dataBean.is_default = 0;
        }
        checkBox.setChecked(dataBean.is_default == 1);

        final boolean isChecked = checkBox.isChecked();
        checkBox.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (isChecked)
                {
                    dataBean.is_default = 0;
                } else
                {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("token", JjgConstant.getToken(mContext));
                    hashMap.put("address_id", dataBean.address_id);
                    HttpUtils.post(NetConstant.SET_DEFAULT, hashMap, new BaseCallBack(mContext)
                    {
                        @Override
                        public void onResponse(String response, int id)
                        {
                            LogUtils.d(response);
                            BaseResp baseResp = new Gson().fromJson(response, BaseResp.class);
                            if (handleResponseCode(baseResp))
                            {
                                dataBean.is_default = 1;
                                preChecked = position;
                                LogUtils.d(preChecked);
                                notifyDataSetChanged();
                            }

                        }
                    });
                }

            }
        });
    }
}
