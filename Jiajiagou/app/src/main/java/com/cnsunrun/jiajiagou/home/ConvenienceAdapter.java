package com.cnsunrun.jiajiagou.home;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.CZBaseQucikAdapter;

import java.util.List;

/**
 * Created by ${LiuDi}
 * on 2017/8/24on 16:53.
 */

public class ConvenienceAdapter extends CZBaseQucikAdapter<ConvenienceResp.InfoBean>
{

    public ConvenienceAdapter(int layoutResId, List<ConvenienceResp.InfoBean> data)
    {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final ConvenienceResp.InfoBean dataBean)
    {
        baseViewHolder.setText(R.id.tv_convenience_companyName, dataBean.getTitle())
                .setText(R.id.tv_convenience_companyProfile, dataBean.getService())
                .setText(R.id.tv_convenience_phone, dataBean.getContact_number());

        getImageLoader().load(dataBean.getImage()).into((ImageView) baseViewHolder.getView(R.id.iv_imv));
        baseViewHolder.getView(R.id.iv_call).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + dataBean.getContact_number());
                intent.setData(data);
                mContext.startActivity(intent);

            }
        });

    }
}
