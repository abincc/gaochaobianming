package com.cnsunrun.jiajiagou.personal.information;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseFragment;
import com.cnsunrun.jiajiagou.common.constant.SPConstant;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.ConstantUtils;
import com.cnsunrun.jiajiagou.common.util.SPUtils;
import com.cnsunrun.jiajiagou.personal.MessageAdapter;
import com.cnsunrun.jiajiagou.personal.MessageDetailActivity;
import com.cnsunrun.jiajiagou.personal.bean.MessageBean;
import com.cnsunrun.jiajiagou.product.MyItemDecoration;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * 欠费消息
 * Created by ${LiuDi}
 * on 2017/8/19on 10:36.
 */

public class ArrangeInfoFragment extends BaseFragment implements BaseQuickAdapter
        .OnItemClickListener {
    @BindView(R.id.recycle_message)
    RecyclerView mMessageRecycle;

    @Override
    protected void init() {
        mMessageRecycle.setLayoutManager(new LinearLayoutManager(mContext));
        mMessageRecycle.setNestedScrollingEnabled(false);
        mMessageRecycle.addItemDecoration(new MyItemDecoration(mContext, R.color.grayF4, R.dimen
                .dp_1));
        final MessageAdapter messageAdapter = new MessageAdapter(R.layout.item_personal_message);
        mMessageRecycle.setAdapter(messageAdapter);
        HashMap<String, String> map = new HashMap<>();
        map.put("token", SPUtils.getString(mContext, SPConstant.TOKEN));
        HttpUtils.get(NetConstant.ARREARS_MESSAGE, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                MessageBean messageBean = new Gson().fromJson(response, MessageBean.class);
                if (messageBean.getStatus() == 1) {
                    List<MessageBean.InfoBean> info = messageBean.getInfo();
                    messageAdapter.setNewData(info);
                    if (info.size()==0) {
                        messageAdapter.setEmptyView(View.inflate(mContext,R.layout.layout_empty,null));
                    }
                }
            }
        });
        messageAdapter.setOnItemClickListener(this);
    }

    @Override
    protected int getChildLayoutRes() {
        return R.layout.fragment_arrange_info;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        List<MessageBean.InfoBean> data = adapter.getData();
        String message_id = data.get(position).getMessage_id();
        Bundle bundle=new Bundle();
        bundle.putString(ConstantUtils.MESSAGE_ID,message_id);
        bundle.putString(ConstantUtils.MESSAGE_TYPE,"欠费");
        startActivity(MessageDetailActivity.class,false,bundle);
    }
}
