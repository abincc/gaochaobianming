package com.cnsunrun.jiajiagou.home;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseHeaderActivity;
import com.cnsunrun.jiajiagou.common.base.Refreshable;
import com.cnsunrun.jiajiagou.common.constant.SPConstant;
import com.cnsunrun.jiajiagou.common.network.DialogCallBack;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.ConstantUtils;
import com.cnsunrun.jiajiagou.common.util.DisplayUtil;
import com.cnsunrun.jiajiagou.common.util.SPUtils;
import com.cnsunrun.jiajiagou.home.adapter.DiscussAdapter;
import com.cnsunrun.jiajiagou.home.bean.ConferenceHallBean;
import com.cnsunrun.jiajiagou.product.MyItemDecoration;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**议事堂
 * Created by j2yyc on 2018/1/23.
 */

public class ConferenceHallActivity extends BaseHeaderActivity implements BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.recycle_discuss)
    RecyclerView discussRecycle;
    private String token;
    private String district_id;
    private DiscussAdapter adapter;
    private int pageNumber = 1;
    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight) {
        tvTitle.setText("议事堂");
    }

    @Override
    protected void init() {
        String url1="https://cdn.pixabay.com/photo/2015/06/24/15/59/train-820321_960_720.jpg";
        for (int i = 0; i < 3; i++) {
//            new TestBean();
        }
        token = SPUtils.getString(this, SPConstant.TOKEN);
        district_id = SPUtils.getString(mContext, SPConstant.DISTRICT_ID);
        discussRecycle.setLayoutManager(new LinearLayoutManager(mContext));
        discussRecycle.setNestedScrollingEnabled(false);
        discussRecycle.addItemDecoration(new MyItemDecoration(mContext,R.color.grayF4,R.dimen.dp_1));
        int maxwidth = DisplayUtil.getScreenWidth(mContext) - 30;
        adapter = new DiscussAdapter(DisplayUtil.dip2px(mContext,maxwidth));
        discussRecycle.setAdapter(adapter);
        adapter.setEmptyView(View.inflate(mContext,R.layout.layout_empty,null));
        adapter.setOnLoadMoreListener(this);
        adapter.setOnItemClickListener(this);
        requestData(pageNumber);
    }

    private void requestData(final int page) {
        HashMap map=new HashMap();
        map.put("token",token);
        map.put("p",String.valueOf(page));
        map.put("district_id",district_id);
        HttpUtils.get(NetConstant.PROCEDURE_LIST, map, new DialogCallBack((Refreshable) ConferenceHallActivity.this) {
            @Override
            public void onResponse(String response, int id) {
                ConferenceHallBean bean = new Gson().fromJson(response, ConferenceHallBean.class);
                if (handleResponseCode(bean)) {
                    List<ConferenceHallBean.InfoBean> info = bean.getInfo();
                    if (page == 1)
                    {
                        adapter.setNewData(info);
                    } else
                    {
                        if (info != null && info.size() > 0)
                        {
                            adapter.addData(info);
                            adapter.loadMoreComplete();
                            pageNumber++;
                        } else
                        {
                            adapter.loadMoreEnd();
                        }
                    }
                    adapter.checkFullPage(discussRecycle);
                }
            }
        });
    }

    @Override
    public void onRefresh()
    {
        super.onRefresh();
        pageNumber=1;
        requestData(pageNumber);
    }


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_conference_hall;
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        List<ConferenceHallBean.InfoBean> data = adapter.getData();
        ConferenceHallBean.InfoBean bean = data.get(position);
        Bundle bundle=new Bundle();
        bundle.putString(ConstantUtils.DISCUSS_ID,bean.getProcedure_id());
        bundle.putString(ConstantUtils.DISCUSS_TITLE,bean.getTitle());
        startActivity(DiscussDetailActivity.class,false,bundle);
    }

    @Override
    public void onLoadMoreRequested() {
        discussRecycle.post(new Runnable()
        {
            @Override
            public void run()
            {
                requestData(pageNumber + 1);
            }
        });
    }
}
