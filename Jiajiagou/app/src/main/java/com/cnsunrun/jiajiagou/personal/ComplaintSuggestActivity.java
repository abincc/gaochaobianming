package com.cnsunrun.jiajiagou.personal;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseHeaderActivity;
import com.cnsunrun.jiajiagou.common.constant.SPConstant;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.FileUtils;
import com.cnsunrun.jiajiagou.common.util.ImageUtils;
import com.cnsunrun.jiajiagou.common.util.SPUtils;
import com.cnsunrun.jiajiagou.common.widget.AddPicBean;
import com.cnsunrun.jiajiagou.common.widget.AddPicGridAdapter;
import com.cnsunrun.jiajiagou.home.bean.PostRequestBean;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 个人中心-投诉建议
 * <p>
 * author:yyc
 * date: 2017-09-13 18:47
 */
public class ComplaintSuggestActivity extends BaseHeaderActivity {
    @BindView(R.id.tv_select_district)
    TextView mDistrict;//小区
    @BindView(R.id.et_content)
    EditText mEdContent;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    private AddPicGridAdapter mAdapter;

    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight) {
        tvTitle.setText("投诉建议");
        tvRight.setText("提交");
        tvRight.setVisibility(View.VISIBLE);
    }

    @Override
    protected void init() {
        mRecycler.setLayoutManager(new GridLayoutManager(mContext, 4));
        mDistrict.setText(SPUtils.getString(mContext,SPConstant.DISTRICT_TITLE));
        mAdapter = new AddPicGridAdapter(R.layout.item_add_pic_cell, null, mRecycler, this);
        mRecycler.setAdapter(mAdapter);
        mAdapter.setNewData(new ArrayList<AddPicBean>());

    }

    @OnClick({R.id.tv_right, R.id.tv_select_district})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right:
                requestNet();
                break;
            case R.id.tv_select_district:

                break;
        }
    }

    private void requestNet() {
        String content = mEdContent.getText().toString();
        if (TextUtils.isEmpty(content)) {
            showToast("请填写内容");
            return;
        }
        Map<String,String> map=new HashMap<>();
        map.put("token", SPUtils.getString(this, SPConstant.TOKEN));
        map.put("content",content);
        List<AddPicBean> imgData = mAdapter.getData();
        Map<String,File> fileMap=new HashMap<>();
        for (AddPicBean bean : imgData) {
            if (bean.type!=bean.TYPE_ADD) {
                File file;
                final double size = FileUtils.getFileOrFilesSize(bean.picPath, FileUtils
                        .SIZETYPE_KB);
                if (size>500) {
                    //如果图片大小大于500K,就处理图片大小(系统相机拍的大图)
                    file = new File(ImageUtils.zoomBitmap2File(bean.picPath));
                }else {
                    file = new File(bean.picPath);
                }
                fileMap.put(file.getName(),file);
            }
        }

        HttpUtils.postForm(NetConstant.SUGGEST_SUBMIT, map, "images[]", fileMap, new StringCallback() {

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                PostRequestBean requestBean = new Gson().fromJson(response, PostRequestBean
                        .class);
                if (requestBean.getStatus()==1) {
                    showToast(requestBean.getMsg(), 1);
                    ComplaintSuggestActivity.this.finish();
                }
            }
        });
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_complaint_suggest;
    }
}
