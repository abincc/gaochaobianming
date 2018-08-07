package com.cnsunrun.jiajiagou.personal.information;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseFragment;
import com.cnsunrun.jiajiagou.common.constant.SPConstant;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.ConstantUtils;
import com.cnsunrun.jiajiagou.common.util.SPUtils;
import com.cnsunrun.jiajiagou.forum.PostsDetailActicity;
import com.cnsunrun.jiajiagou.login.LoginActivity;
import com.cnsunrun.jiajiagou.personal.ForumMessageAdapter;
import com.cnsunrun.jiajiagou.personal.bean.ForumMessageBean;
import com.cnsunrun.jiajiagou.product.MyItemDecoration;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by ${LiuDi}
 * on 2017/8/19on 10:36.
 * 论坛消息
 */

public class ForumInfoFragment extends BaseFragment
{
    @BindView(R.id.recycle_message)
    RecyclerView mMessageRecycle;
    @Override
    protected void init()
    {
        mMessageRecycle.setLayoutManager(new LinearLayoutManager(mContext));
        mMessageRecycle.setNestedScrollingEnabled(false);
        mMessageRecycle.addItemDecoration(new MyItemDecoration(mContext,R.color.grayF4,R.dimen.dp_1));
        final ForumMessageAdapter messageAdapter = new ForumMessageAdapter(R.layout.item_personal_message);
        mMessageRecycle.setAdapter(messageAdapter);
        messageAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<ForumMessageBean.InfoBean> data = adapter.getData();
                String id = data.get(position).getThread_id();
                Bundle bundle=new Bundle();
                bundle.putString(ConstantUtils.POSTS_ITEM_ID,id);
//                bundle.putString(ConstantUtils.MESSAGE_TYPE,"论坛");
                startActivity(PostsDetailActicity.class,false,bundle);
            }
        });

        HashMap<String,String> map=new HashMap<>();
        String token = SPUtils.getString(mContext, SPConstant.TOKEN);
        if (TextUtils.isEmpty(token)) {
            startActivity(LoginActivity.class);
        }
        map.put("token", token);
        HttpUtils.get(NetConstant.FORUM_MESSAGE, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                ForumMessageBean messageBean = new Gson().fromJson(response, ForumMessageBean.class);
                if (messageBean.getStatus()==1) {
                    List<ForumMessageBean.InfoBean> info = messageBean.getInfo();
                    messageAdapter.setNewData(info);
                    if (info.size()==0) {
                        messageAdapter.setEmptyView(View.inflate(mContext,R.layout.layout_empty,null));
                    }
                }
                if (messageBean.getStatus()==-2) {
                    startActivity(LoginActivity.class);
                }
            }
        });
    }

    @Override
    protected int getChildLayoutRes()
    {
        return R.layout.fragment_forum_info;
    }
}
