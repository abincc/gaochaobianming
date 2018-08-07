package com.cnsunrun.jiajiagou.shopclassify;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.CZBaseQucikAdapter;

import java.util.List;

/**
 * Created by ${LiuDi}
 * on 2017/8/18on 17:44.
 */

public class ShopClassiFyAdapter extends CZBaseQucikAdapter<ClassifyResp.InfoBean>
{
    int pos=0;

    public ShopClassiFyAdapter(int layoutResId, List<ClassifyResp.InfoBean> data)
    {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, ClassifyResp.InfoBean dataBean)
    {
        baseViewHolder.setText(R.id.tv_name, dataBean.getTitle());
        getImageLoader().load(dataBean.getIcon()).into((ImageView) baseViewHolder.getView(R.id.iv_imv));
        View convertView = baseViewHolder.getConvertView();
        final ImageView iv_title = baseViewHolder.getView(R.id.iv_title);

        if (pos==baseViewHolder.getPosition()){
            iv_title.setVisibility(View.VISIBLE);
            convertView.setBackgroundColor(0xffffffff);
        }else {
            iv_title.setVisibility(View.INVISIBLE);
            convertView.setBackgroundColor(0xfff4f4f4);
        }

            convertView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (pos!=baseViewHolder.getPosition()){
                        pos=baseViewHolder.getPosition();
                        notifyDataSetChanged();
                    }
                }
            });
    }

}
