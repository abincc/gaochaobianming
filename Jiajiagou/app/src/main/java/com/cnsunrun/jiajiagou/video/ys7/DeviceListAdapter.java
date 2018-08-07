package com.cnsunrun.jiajiagou.video.ys7;

import android.net.Network;
import android.widget.ImageView;

import com.blankj.utilcode.utils.LogUtils;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.CZBaseQucikAdapter;
import com.cnsunrun.jiajiagou.common.network.NetConstant;

import java.util.List;

/**
 * Created by ${LiuDi}
 * on 2017/8/24on 14:31.
 */

public class DeviceListAdapter extends CZBaseQucikAdapter<DeviceListResp.InfoBean>
{
    boolean isEditor = false;

    public DeviceListAdapter(int layoutResId, List<DeviceListResp.InfoBean> data)
    {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final DeviceListResp.InfoBean dataBean)
    {
        baseViewHolder.setText(R.id.tv_video_name, dataBean.getVideo().getTitle())
                .setText(R.id.tv_device_name, dataBean.getTitle())
                .setText(R.id.tv_device_remark, dataBean.getRemark());
        getImageLoader().load(NetConstant.BASE_URL + dataBean.getImage()).into(((ImageView) baseViewHolder.getView(R.id.iv_device_img)));

        baseViewHolder.addOnClickListener(R.id.device);
    }
}
