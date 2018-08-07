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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseHeaderActivity;
import com.cnsunrun.jiajiagou.common.base.Refreshable;
import com.cnsunrun.jiajiagou.common.network.DialogCallBack;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.personal.bean.AddressBookBean;
import com.cnsunrun.jiajiagou.product.MyItemDecoration;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-11-23 11:17
 */
public class AddressBookActivity extends BaseHeaderActivity implements TextWatcher, BaseQuickAdapter
        .RequestLoadMoreListener {

    @BindView(R.id.recycle)
    RecyclerView mRecycle;
    @BindView(R.id.et_search)
    EditText editSearch;
    @BindView(R.id.ll_root_layout)
    LinearLayout rootLayout;
    private AddressBookAdapter mAddressBookAdapter;
    private int pageNumber = 1;

    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight) {
        tvTitle.setText("万能通讯录");
        tvRight.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void init() {
        mRecycle.setLayoutManager(new LinearLayoutManager(this));
        mRecycle.addItemDecoration(new MyItemDecoration(this, R.color.grayF4, R.dimen.dp_1));
        mRecycle.setNestedScrollingEnabled(false);
        mAddressBookAdapter = new AddressBookAdapter(R.layout.item_address_book);
        mRecycle.setAdapter(mAddressBookAdapter);
        mAddressBookAdapter.setOnLoadMoreListener(this);
        requestNet(pageNumber, "", mAddressBookAdapter);
        editSearch.addTextChangedListener(this);
        mAddressBookAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {


            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                List<AddressBookBean.InfoBean> data = adapter.getData();
                if (view.getId() == R.id.iv_dial) {
                    call(data.get(position).getNumber());
                }
            }
        });


    }

    private void requestNet(final int page, String keywords, final AddressBookAdapter adapter) {
        HashMap<String, String> map = new HashMap();
        map.put("keywords", keywords);
        map.put("p", String.valueOf(page));
        HttpUtils.get(NetConstant.ADDRESS_BOOK, map, new DialogCallBack((Refreshable) AddressBookActivity.this) {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                AddressBookBean addressBookBean = new Gson().fromJson(response, AddressBookBean
                        .class);
                if (handleResponseCode(addressBookBean)) {
                    if (addressBookBean.getStatus() == 1) {

                        List<AddressBookBean.InfoBean> info = addressBookBean.getInfo();
                        if (info.size() == 0 && page == 1) {
                            adapter.setEmptyView(View.inflate(mContext, R.layout.layout_empty, null));
//                        adapter.setEmptyView(getLayoutInflater().inflate( R
//                                .layout.layout_empty_forum, (ViewGroup) mRecycle.getParent(),false));
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

                  /*  if (info.size()==0) {
//                        mTabLikeContentAdapter.setEmptyView(View.inflate(mContext,R.layout.layout_empty,null));
                        adapter.setEmptyView(getLayoutInflater().inflate( R
                                .layout.layout_empty_forum, (ViewGroup) mRecycle.getParent(),false));
                    }*/
                    }
                }
            }
        });
    }


    @Override
    public void onRefresh() {
        super.onRefresh();
        pageNumber = 1;
        requestNet(pageNumber, "", mAddressBookAdapter);
    }

    @Override
    public void onLoadMoreRequested()
    {
        mRecycle.post(new Runnable()
        {
            @Override
            public void run()
            {
                requestNet(pageNumber + 1,"",mAddressBookAdapter);
            }
        });
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_address_book;
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
        requestNet(1, s.toString(), mAddressBookAdapter);
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
