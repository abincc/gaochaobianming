package com.cnsunrun.jiajiagou.personal;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseHeaderActivity;
import com.cnsunrun.jiajiagou.common.base.Refreshable;
import com.cnsunrun.jiajiagou.common.constant.JjgConstant;
import com.cnsunrun.jiajiagou.common.constant.SPConstant;
import com.cnsunrun.jiajiagou.common.network.DialogCallBack;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.SPUtils;
import com.cnsunrun.jiajiagou.login.LoginActivity;
import com.cnsunrun.jiajiagou.personal.bean.NearByBean;
//import com.cnsunrun.jiajiagou.personal.chat.ChatActivity;
//import com.cnsunrun.jiajiagou.personal.chat.RBean;
//import com.cnsunrun.jiajiagou.personal.chat.list.MyChatListActivity;
import com.cnsunrun.jiajiagou.personal.chat.ChatActivity;
import com.cnsunrun.jiajiagou.personal.chat.RBean;
import com.cnsunrun.jiajiagou.product.MyItemDecoration;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

import static com.cnsunrun.jiajiagou.R.id.tv_right;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-11-23 11:17
 */
public class NearByActivity extends BaseHeaderActivity implements TextWatcher, BaseQuickAdapter
        .RequestLoadMoreListener {

    @BindView(R.id.recycle)
    RecyclerView mRecycle;
    @BindView(R.id.et_search)
    EditText editSearch;
    @BindView(R.id.ll_root_layout)
    LinearLayout rootLayout;
    private NearByAdapter mNearByAdapter;
    private int pageNumber = 1;

    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight) {
        tvTitle.setText("邻聊");
        tvRight.setVisibility(View.GONE);
    }

    @Override
    protected void init() {
        mRecycle.setLayoutManager(new LinearLayoutManager(this));
        mRecycle.addItemDecoration(new MyItemDecoration(this, R.color.grayF4, R.dimen.dp_1));
        mRecycle.setNestedScrollingEnabled(false);
        mNearByAdapter = new NearByAdapter(R.layout.item_near_by);
        mRecycle.setAdapter(mNearByAdapter);
        mNearByAdapter.setOnLoadMoreListener(this);
        mNearByAdapter.setImageLoader(getImageLoader());
        requestNet(pageNumber, "", mNearByAdapter);
        editSearch.addTextChangedListener(this);
        mNearByAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {


            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                List<NearByBean.InfoBean> data = adapter.getData();

                if (view.getId() == R.id.near_by) {
                    getRoom(data.get(position).getMember_id());
                }
            }
        });
    }

    public void getRoom(int oid){
        HashMap<String, String> map = new HashMap();
        map.put("oid", String.valueOf(oid));
        map.put("token", JjgConstant.getToken(mContext));
        HttpUtils.get(NetConstant.CREATE_GET_ROOM, map, new DialogCallBack((Refreshable) NearByActivity.this) {
            @Override
            public void onError(Call call, Exception e, int id) {

            }
            @Override
            public void onResponse(String response, int id) {
                RBean rBean = new Gson().fromJson(response, RBean.class);
                if (handleResponseCode(rBean)) {
                    if (rBean.getStatus() == 1) {
                        //进入聊天界面
                        Intent intent = new Intent(NearByActivity.this,ChatActivity.class);
                        intent.putExtra("rid", rBean.getInfo().getId());
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private void requestNet(final int page, String keywords, final NearByAdapter adapter) {
        HashMap<String, String> map = new HashMap();
//        map.put("keywords", keywords);
        map.put("p", String.valueOf(page));
        map.put("token", JjgConstant.getToken(mContext));
        map.put("district_id", SPUtils.getString(mContext, SPConstant.DISTRICT_ID));
        HttpUtils.get(NetConstant.INDEX_COORDINATE, map, new DialogCallBack((Refreshable) NearByActivity.this) {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                NearByBean nearByBean = new Gson().fromJson(response, NearByBean.class);
                if (handleResponseCode(nearByBean)) {
                    if (nearByBean.getStatus() == 1) {

                        List<NearByBean.InfoBean> info = nearByBean.getInfo();
                        if (info.size() == 0 && page == 1) {
                            adapter.setEmptyView(View.inflate(mContext, R.layout.layout_empty, null));
                            adapter.setEmptyView(getLayoutInflater().inflate( R.layout.layout_empty_forum, (ViewGroup) mRecycle.getParent(),false));
                        }

                        if (page == 1) {
                            adapter.setNewData(info);
                        } else {
                            if (info != null && info.size() > 0) {
                                adapter.addData(info);
                                pageNumber++;
                            } else {
                                adapter.loadMoreEnd();
                            }
                        }
                        adapter.checkFullPage(mRecycle);

//                  /*  if (info.size()==0) {
////                        mTabLikeContentAdapter.setEmptyView(View.inflate(mContext,R.layout.layout_empty,null));
//                        adapter.setEmptyView(getLayoutInflater().inflate( R
//                                .layout.layout_empty_forum, (ViewGroup) mRecycle.getParent(),false));
//                    }*/
                    }
                }
            }
        });
    }


    @Override
    public void onRefresh() {
        super.onRefresh();
        pageNumber = 1;
        requestNet(pageNumber, "", mNearByAdapter);
    }

    @Override
    public void onLoadMoreRequested()
    {
        mRecycle.post(new Runnable()
        {
            @Override
            public void run()
            {
                requestNet(pageNumber + 1,"",mNearByAdapter);
            }
        });
    }

    @OnClick({R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case tv_right:
                if (JjgConstant.isLogin(mContext)) {
//                    startActivity(new Intent(mContext, MyChatListActivity.class));
                } else {
                    startActivity(new Intent(mContext, LoginActivity.class));
                }
                break;
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_near_by;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        return imm.hideSoftInputFromWindow(this.editSearch.getWindowToken(), 0);
    }

    //点击其他地方隐藏输入法
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context
                        .INPUT_METHOD_SERVICE);
                if (imm != null) {
                    rootLayout.requestFocus();
                    imm.hideSoftInputFromWindow(editSearch.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        } // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0,
                    0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY()
                    < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        requestNet(1, s.toString(), mNearByAdapter);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public void call(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }
}
