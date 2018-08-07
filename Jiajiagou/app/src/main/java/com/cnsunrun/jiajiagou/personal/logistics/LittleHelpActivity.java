package com.cnsunrun.jiajiagou.personal.logistics;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseCallBack;
import com.cnsunrun.jiajiagou.common.base.BaseHeaderActivity;
import com.cnsunrun.jiajiagou.common.base.BaseResp;
import com.cnsunrun.jiajiagou.common.base.Refreshable;
import com.cnsunrun.jiajiagou.common.base.TwoButtonDialog;
import com.cnsunrun.jiajiagou.common.constant.JjgConstant;
import com.cnsunrun.jiajiagou.common.network.DialogCallBack;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.RecyclerLineDecoration;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * Created by ${LiuDi}
 * on 2017/8/23on 15:02.
 * 小事帮忙
 */

public class LittleHelpActivity extends BaseHeaderActivity implements BaseQuickAdapter.RequestLoadMoreListener
{
    @BindView(R.id.recycle)
    RecyclerView mRecycle;
    private LittleHelpAdapter mAdapter;
    private int pageNumber = 1;

    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight)
    {
        tvTitle.setText("小事帮忙");

    }

    @Override
    protected void init()
    {
        initRecycles();

        getData(pageNumber);

    }

    @Override
    public void onRefresh()
    {
        super.onRefresh();
        pageNumber = 1;
        getData(pageNumber);
    }

    private void getData(final int page)
    {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", JjgConstant.getToken(mContext));
        HttpUtils.get(NetConstant.LITTLE_HELP_LIST, hashMap, new DialogCallBack((Refreshable) LittleHelpActivity.this)
        {

            @Override
            public void onResponse(String response, int id)
            {
                LogUtils.d(response);
                LittleHelpResp resp = new Gson().fromJson(response, LittleHelpResp.class);
                if (handleResponseCode(resp))
                {
                    List<HelpBean> info = resp.getInfo();
                    if (page == 1)
                    {
                        mAdapter.setNewData(info);
                    } else
                    {
                        if (info != null && info.size() > 0)
                        {
                            mAdapter.addData(info);
                            pageNumber++;
                            mAdapter.loadMoreComplete();
                        } else
                        {
                            mAdapter.loadMoreEnd();
                        }
                    }
                    mAdapter.checkFullPage(mRecycle);
                }


            }
        });

    }

    private void initRecycles()
    {


        mRecycle.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        mAdapter = new LittleHelpAdapter(R.layout.item_little_help, null);
        mAdapter.setImageLoader(getImageLoader());
        mAdapter.setEmptyView(View.inflate(mContext, R.layout.layout_empty, null));
        mAdapter.setOnLoadMoreListener(this);
        mRecycle.setAdapter(mAdapter);
        mRecycle.addItemDecoration(new RecyclerLineDecoration(mContext));
        mRecycle.addOnItemTouchListener(new OnItemClickListener()
        {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position)
            {
                List<HelpBean> data = adapter.getData();
                String trifle_id = data.get(position).trifle_id;
                Intent intent = new Intent(mContext, LittleHelpDetailActivity.class);
                intent.putExtra("trifle_id", trifle_id);

                startActivityForResult(intent, 200);
            }
        });

        mRecycle.addOnItemTouchListener(new OnItemChildClickListener()
        {
            @Override
            public void onSimpleItemChildClick(final BaseQuickAdapter adapter, View view, final int position)
            {

                final TwoButtonDialog dialog = new TwoButtonDialog();
                dialog.setContent("确定执行该操作吗？");
                List<HelpBean> data = adapter.getData();
                final String trifle_id = data.get(position).trifle_id;
                dialog.setOnBtnConfirmClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("token", JjgConstant.getToken(mContext));
                        hashMap.put("trifle_id", trifle_id);
                        HttpUtils.post(NetConstant.TRIFLE_CANCEL, hashMap, new BaseCallBack(mContext)
                        {
                            @Override
                            public void onResponse(String response, int id)
                            {
                                LogUtils.d(response);
                                BaseResp baseResp = new Gson().fromJson(response, BaseResp.class);
                                if (handleResponseCode(baseResp))
                                {
                                    showToast(baseResp.getMsg());
                                    mAdapter.remove(position);
                                    dialog.dismiss();
                                }
                            }
                        });
                    }
                });
                dialog.show(getSupportFragmentManager(), null);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200)
        {

            onRefresh();
        }
    }

    @Override
    protected int getLayoutRes()
    {
        return R.layout.activity_little_help;
    }

    @Override
    public void onLoadMoreRequested()
    {
        mRecycle.post(new Runnable()
        {
            @Override
            public void run()
            {
                getData(pageNumber + 1);
            }
        });
    }
}
