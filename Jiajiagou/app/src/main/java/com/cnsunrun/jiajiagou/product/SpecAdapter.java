package com.cnsunrun.jiajiagou.product;

import android.view.View;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.CZBaseQucikAdapter;

import java.util.List;

/**
 * Created by ${LiuDi}
 * on 2017/9/7on 17:47.
 */

public class SpecAdapter extends CZBaseQucikAdapter<ProductDetailResp.InfoBean.SpecBean.SpecValueListBean>
{
    private int preChecked = -1;
    private String spec_value_id;
    int index = -1;
    public SpecAdapter(int layoutResId, List<ProductDetailResp.InfoBean.SpecBean.SpecValueListBean> data)
    {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final ProductDetailResp.InfoBean.SpecBean.SpecValueListBean
            dataBean)
    {
        final CheckBox checkBox = (CheckBox) helper.getView(R.id.checkbox);
        checkBox.setText(dataBean.getSpec_value_title());

        if (preChecked != -1 && preChecked != helper.getAdapterPosition())
        {
            dataBean.isChecked = false;
        }

        checkBox.setChecked(dataBean.isChecked);
        checkBox.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                dataBean.isChecked = checkBox.isChecked();
                if (checkBox.isChecked())
                {
                    preChecked = helper.getAdapterPosition();
                    spec_value_id = dataBean.getSpec_value_id();
                    if (mListener != null)
                        mListener.getSpecValue(spec_value_id, index);
                    notifyDataSetChanged();
                } else
                {
                    spec_value_id = null;
                }

            }
        });
    }

    private SpecListener mListener;

    public void setSpecListener(SpecListener listener, int index)
    {
        mListener = listener;
        this.index = index;
    }

    public interface SpecListener
    {
        void getSpecValue(String spec_value_id, int index);
    }
    public String getSpec_value_id()
    {
        return spec_value_id;
    }
}
