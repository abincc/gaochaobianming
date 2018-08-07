package com.cnsunrun.jiajiagou.forum;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseActivity;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.ConstantUtils;
import com.cnsunrun.jiajiagou.forum.adapter.ForumMenuLeftAdapter;
import com.cnsunrun.jiajiagou.forum.adapter.ForumMenuRightAdapter;
import com.cnsunrun.jiajiagou.forum.bean.ForumPlateListBean;
import com.cnsunrun.jiajiagou.forum.entity.ForumMenuLeftItem;
import com.cnsunrun.jiajiagou.forum.entity.ForumMenuRightItem;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 论坛分类菜单
 * <p>
 * author:yyc
 * date: 2017-08-25 17:21
 */
public class ForumMenuActivity extends BaseActivity {
    @BindView(R.id.recycler_left)
    RecyclerView mLeftRecycle;
    @BindView(R.id.recycler_right)
    RecyclerView mRightRecycle;
    private List<ForumMenuLeftItem> parentList;
    private List<ForumMenuRightItem> childList;
    private ForumMenuLeftAdapter mLeftAdapter;
    private ForumMenuRightAdapter mForumMenuRightAdapter;
    private String mFromClazz;

    private static final String SEND_POSTST_ACTIVITY="SendPostsActivity";

    @Override
    protected void init() {
        initData();
        initView();
        initAdapter();
    }


    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null) {
            mFromClazz = bundle.getString(ConstantUtils.FROM_CLAZZ);
        }

        requestNet();
//        parentList = new ArrayList();
//        for (int i = 0; i < 20; i++) {
//            ForumMenuLeftItem item = new ForumMenuLeftItem();
//            item.setId(i + 1);
//            item.setTitle(i + "");
//            parentList.add(item);
//        }
//        //----------子列表数据-----------
//        childList = new ArrayList();
//        for (ForumMenuLeftItem parentItem : parentList) {
//            ForumMenuRightItem childItem = new ForumMenuRightItem();
//            List<ForumMenuRightItem.RightItem> rightItemList = new ArrayList();
//            childItem.setParentId(parentItem.getId());
//            for (int j = 0; j < 5; j++) {
//                ForumMenuRightItem.RightItem item = new ForumMenuRightItem.RightItem();
//                item.setId(j + 1);
//                item.setTitle(parentItem.getTitle() + j);
//                rightItemList.add(item);
//                childItem.setRightItems(rightItemList);
//            }
//            childList.add(childItem);
//        }
    }

    private void requestNet() {
        HttpUtils.get(NetConstant.ALL_PLATE, null, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                ForumPlateListBean bean = new Gson().fromJson(response,
                        ForumPlateListBean.class);
                if (bean.getStatus() == 1) {
                    List<ForumPlateListBean.InfoBean> info = bean.getInfo();
                    mLeftAdapter.setNewData(info);
                    mForumMenuRightAdapter.setNewData(info.get(0).getForum_list());
                }
            }
        });
    }

    private void initView() {
        mLeftRecycle.setNestedScrollingEnabled(false);
        mRightRecycle.setNestedScrollingEnabled(false);
        mLeftRecycle.setLayoutManager(new LinearLayoutManager(this));
        mRightRecycle.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initAdapter() {
        mLeftAdapter = new ForumMenuLeftAdapter(R.layout
                .item_left_forum_menu);
        mLeftRecycle.setAdapter(mLeftAdapter);
        mForumMenuRightAdapter = new ForumMenuRightAdapter(R.layout
                .item_right_forum_menu);
        mForumMenuRightAdapter.setImageLoader(getImageLoader());
        mRightRecycle.setAdapter(mForumMenuRightAdapter);
        //默认显示子列表第一个item
//        requstRightData(1, info);
        mLeftAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<ForumPlateListBean.InfoBean> data = adapter.getData();
                for (ForumPlateListBean.InfoBean item : data) {
                    item.setSelect(false);
                }
                data.get(position).setSelect(true);
                mForumMenuRightAdapter.setNewData(data.get(position).getForum_list());
                mLeftAdapter.notifyDataSetChanged();

            }
        });
        //------------------------------------------------------------------
        mForumMenuRightAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<ForumPlateListBean.InfoBean.ForumListBean> data = adapter.getData();
                if (SEND_POSTST_ACTIVITY.equals(mFromClazz)) {
                    Intent intent=new Intent();
                    intent.putExtra(ConstantUtils.FORUM_PLATE_ID,data.get(position).getId());
                    intent.putExtra(ConstantUtils.FORUM_PLATE_TITLE,data.get(position).getTitle());
                    ForumMenuActivity.this.setResult(RESULT_OK,intent);
                    ForumMenuActivity.this.finish();
                    return;
                }
                Bundle bundle=new Bundle();
                bundle.putString(ConstantUtils.FORUM_PLATE_ID,data.get(position).getId());
                startActivity(PlateHomepageActivity.class,false,bundle);

            }
        });

    }
    @OnClick(R.id.iv_back_plate)
    public void onClick(View v){
        switch (v.getId()){
            case R.id.iv_back_plate:
                onBackPressedSupport();
                break;
        }
    }

    private void requstRightData(int position, List<ForumPlateListBean.InfoBean> info) {
        for (ForumMenuRightItem item : childList) {
            if (item.getParentId() == position) {
//                ForumMenuRightAdapter rightAdapter = new ForumMenuRightAdapter(R.layout
//                        .item_right_forum_menu, item.getRightItems());
//                mRightRecycle.setAdapter(rightAdapter);
//            }
            }
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_forum_menu;
    }

}
