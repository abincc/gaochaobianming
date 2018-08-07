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
import com.cnsunrun.jiajiagou.common.util.SPUtils;
import com.cnsunrun.jiajiagou.home.adapter.NewsAdapter;
import com.cnsunrun.jiajiagou.home.bean.NewsListBean;
import com.cnsunrun.jiajiagou.product.MyItemDecoration;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * Created by j2yyc on 2018/1/23.
 */

public class NewsActivity extends BaseHeaderActivity implements BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.recycle_news)
    RecyclerView newsRecycler;
    private String district_id;
    private NewsAdapter newsAdapter;
    private int pageNumber = 1;

    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight) {
        tvTitle.setText("政务公开");
    }

    @Override
    protected void init() {
        district_id = SPUtils.getString(mContext, SPConstant.DISTRICT_ID);
        newsRecycler.setLayoutManager(new LinearLayoutManager(this));
        newsRecycler.setNestedScrollingEnabled(false);
        newsRecycler.addItemDecoration(new MyItemDecoration(this,R.color.grayF4,R.dimen.dp_1));
        newsAdapter = new NewsAdapter(getImageLoader());
        newsAdapter.setEmptyView(View.inflate(mContext,R.layout.layout_empty,null));
        newsRecycler.setAdapter(newsAdapter);

        newsAdapter.setOnLoadMoreListener(this);
        newsAdapter.setOnItemClickListener(this);
        requestNet(pageNumber);
    }

    private void requestNet(final int page) {
        HashMap map=new HashMap();

//        map.put("token",token);
        map.put("district_id",district_id);
        map.put("p",String.valueOf(page));
        HttpUtils.get(NetConstant.GOVERNMENT, map, new DialogCallBack((Refreshable) NewsActivity.this) {
            @Override
            public void onResponse(String response, int id) {
                NewsListBean bean=new Gson().fromJson(response,NewsListBean.class);
                if (handleResponseCode(bean)) {
                    List<NewsListBean.InfoBean> info = bean.getInfo();
                    if (page == 1)
                    {
                        newsAdapter.setNewData(info);
                    } else
                    {
                        if (info != null && info.size() > 0)
                        {
                            newsAdapter.addData(info);
                            newsAdapter.loadMoreComplete();
                            pageNumber++;
                        } else
                        {
                            newsAdapter.loadMoreEnd();
                        }
                    }
                    newsAdapter.checkFullPage(newsRecycler);
                }
            }
        });
    }

    @Override
    public void onRefresh()
    {
        super.onRefresh();
        pageNumber=1;
        requestNet(pageNumber);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_news;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        List<NewsListBean.InfoBean> data = adapter.getData();
        NewsListBean.InfoBean infoBean = data.get(position);
        Bundle bundle=new Bundle();
        bundle.putString(ConstantUtils.GOVERNMENT_ID,infoBean.getGovernment_id());
//        bundle.putString(ConstantUtils.GOVERNMENT_TITLE,infoBean.getTitle());
//        bundle.putString(ConstantUtils.GOVERNMENT_CONTENT,infoBean.getContent());
//        bundle.putString(ConstantUtils.GOVERNMENT_DATE,infoBean.getAdd_time());
//        bundle.putString(ConstantUtils.GOVERNMENT_IMAGE,infoBean.getCover());
        bundle.putString(ConstantUtils.GOVERNMENT_URL,infoBean.getUrl());
        startActivity(NewsDetailActivity.class,false,bundle);
    }

    @Override
    public void onLoadMoreRequested() {
        newsRecycler.post(new Runnable()
        {
            @Override
            public void run()
            {
                requestNet(pageNumber + 1);
            }
        });
    }
}
