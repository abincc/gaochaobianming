package com.cnsunrun.jiajiagou.personal.logistics;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseHeaderActivity;
import com.cnsunrun.jiajiagou.common.base.BaseResp;
import com.cnsunrun.jiajiagou.common.constant.SPConstant;
import com.cnsunrun.jiajiagou.common.network.DialogCallBack;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.ConstantUtils;
import com.cnsunrun.jiajiagou.common.util.FileUtils;
import com.cnsunrun.jiajiagou.common.util.ImageUtils;
import com.cnsunrun.jiajiagou.common.util.OtherUtils;
import com.cnsunrun.jiajiagou.common.util.SPUtils;
import com.cnsunrun.jiajiagou.common.widget.AddPicBean;
import com.cnsunrun.jiajiagou.common.widget.AddPicGridAdapter;
import com.cnsunrun.jiajiagou.personal.CleaningEvent;
import com.cnsunrun.jiajiagou.personal.LittleEvent;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by j2yyc on 2018/1/26.
 */

public class PropertyReplyActivity extends BaseHeaderActivity {
    @BindView(R.id.et_content)
    EditText mEdContent;//输入内容
    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    private AddPicGridAdapter mAdapter;
    private String key;
    private String id;

    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight) {
        tvTitle.setText("回复");
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("确认");
    }

    @Override
    protected void init() {
        key = getIntent().getExtras().getString(ConstantUtils.FROM_CLEAN_OR_LITTLE);
        id = getIntent().getExtras().getString("id");
        mRecycler.setLayoutManager(new GridLayoutManager(mContext, 4));
        mAdapter = new AddPicGridAdapter(R.layout.item_add_pic_cell, null, mRecycler,this);
        mRecycler.setAdapter(mAdapter);
        mAdapter.setNewData(new ArrayList<AddPicBean>());

    }

    @OnClick({ R.id.tv_right})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right:
                if (OtherUtils.isFastClick()) {
                    requestNet();
                }
                break;
        }
    }

    private void requestNet() {
        String token = SPUtils.getString(mContext, SPConstant.TOKEN);
        List<AddPicBean> data = mAdapter.getData();
        String content = mEdContent.getText().toString();
        HashMap<String, String> map = new HashMap();
        String url="";
        if (key.equals(ConstantUtils.FROM_CLEAN)) {
            map.put("clean_id",id);
            url=NetConstant.REPLY_CLEAN;
        }
        if (key.equals(ConstantUtils.FROM_LITTLE)) {
            map.put("trifle_id",id);
            url=NetConstant.REPLY_TRIFLE;

        }
        map.put("reply",content);
        map.put("token",token);
        Map<String, File> fileMap = new HashMap<>();
        for (AddPicBean bean : data) {
            if (bean.type != bean.TYPE_ADD) {
                File file;
                final double size = FileUtils.getFileOrFilesSize(bean.picPath, FileUtils
                        .SIZETYPE_KB);
                if (size > 500) {
                    //如果图片大小大于500K,就处理图片大小(系统相机拍的大图)
                    file = new File(ImageUtils.zoomBitmap2File(bean.picPath));
                } else {
                    file = new File(bean.picPath);
                }
                fileMap.put(file.getName(), file);
            }
        }

        HttpUtils.postForm(url, map, "img[]", fileMap, new DialogCallBack
                (mContext) {
            @Override
            public void onResponse(String response, int id) {
                BaseResp postRequestBean = new Gson().fromJson(response, BaseResp
                        .class);
                if (handleResponseCode(postRequestBean)) {
                    if (postRequestBean.getStatus() == 1) {
                        showToast(postRequestBean.getMsg(), 1);
                        if (key.equals(ConstantUtils.FROM_CLEAN)) {
                            EventBus.getDefault().post(new CleaningEvent());
                        }
                        if (key.equals(ConstantUtils.FROM_LITTLE)) {
                            EventBus.getDefault().post(new LittleEvent());
                        }
                        OtherUtils.delayFinishActivity(PropertyReplyActivity.this, 1000);
                    }
                }
            }
        });
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_property_reply;
    }
}
