package com.cnsunrun.jiajiagou.personal.chat.list;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.CZBaseQucikAdapter;

import java.util.List;

/**
 * Created by ${LiuDi}
 * on 2017/8/24on 14:31.
 */

public class MyChatListAdapter extends CZBaseQucikAdapter<MyChatListResp.InfoBean>
{
    boolean isEditor = false;

    public MyChatListAdapter(int layoutResId, List<MyChatListResp.InfoBean> data)
    {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final MyChatListResp.InfoBean dataBean)
    {
        baseViewHolder.setText(R.id.tv_room_name, dataBean.getMember().getNickname() != null && dataBean.getMember().getNickname() != "" ? dataBean.getMember().getNickname():getNum(dataBean.getMember().getMobile()))
                .setText(R.id.tv_room_content, dataBean.getMsg().getContent().length() > 20?dataBean.getMsg().getContent().substring(0,20):dataBean.getMsg().getContent());
//                .setText(R.id.tv_collect_price, "¥" + dataBean.price);
        getImageLoader().load(dataBean.getMember().getHeadimg()).into(((ImageView) baseViewHolder.getView(R.id.iv_room_img)));

        baseViewHolder.addOnClickListener(R.id.room);

        /**
         * 添加item 点击事件
         */
//        baseViewHolder.getConvertView().setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
////                Intent intent = new Intent(mContext, ProductDetailActivity.class);
////                intent.putExtra(ArgConstants.PRODUCTID, dataBean.product_id);
////                mContext.startActivity(intent);
//                LogUtils.d("--------222222222-----------");
//            }
//        });
    }

    private String getNum(String mobile) {
        String str = mobile;
        //字符串截取
        String bb =str.substring(3,7);
        //字符串替换
        String cc = str.replace(bb,"****");
        return cc;
    }
}
