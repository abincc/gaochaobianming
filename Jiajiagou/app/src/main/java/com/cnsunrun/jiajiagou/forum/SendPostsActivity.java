package com.cnsunrun.jiajiagou.forum;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.cnsunrun.jiajiagou.common.util.LogUtils;
import com.cnsunrun.jiajiagou.common.util.OtherUtils;
import com.cnsunrun.jiajiagou.common.util.SPUtils;
import com.cnsunrun.jiajiagou.common.widget.AddPicBean;
import com.cnsunrun.jiajiagou.common.widget.AddPicGridAdapter;
import com.cnsunrun.jiajiagou.forum.bean.TokenBean;
import com.cnsunrun.jiajiagou.login.LoginActivity;
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
 * 发帖
 * <p>
 * author:yyc
 * date: 2017-08-30 11:18
 */
public class SendPostsActivity extends BaseHeaderActivity {

    @BindView(R.id.et_title)
    EditText mEdTitle;//输入标题
    @BindView(R.id.et_content)
    EditText mEdContent;//输入内容
    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    @BindView(R.id.tv_plate_result)
    TextView mTvResult;
    private AddPicGridAdapter mAdapter;
    private String token;
    private String plateId;

    private boolean reClick = false;

    @Override
    protected void init() {
        checkIsLogin();
        mRecycler.setLayoutManager(new GridLayoutManager(mContext, 4));
        mAdapter = new AddPicGridAdapter(R.layout.item_add_pic_cell, null, mRecycler,
                SendPostsActivity.this);
        mRecycler.setAdapter(mAdapter);
        mAdapter.setNewData(new ArrayList<AddPicBean>());
    }

    private void checkIsLogin() {
        token = SPUtils.getString(mContext, SPConstant.TOKEN);
        if (TextUtils.isEmpty(token)) {
            Intent intent = new Intent(this, LoginActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(ConstantUtils.RELOGIN, ConstantUtils.RELOGIN_CODE);
            intent.putExtras(bundle);
            startActivityForResult(intent, ConstantUtils.RELOGIN_CODE);
        }
    }


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_send_posts;
    }

    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight) {
        tvRight.setVisibility(View.VISIBLE);
        tvTitle.setText("发帖");
        tvRight.setText("发布");
    }

    @OnClick({R.id.tv_select_plate, R.id.tv_right})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_select_plate:
                Bundle bundle = new Bundle();
                bundle.putString(ConstantUtils.FROM_CLAZZ, "SendPostsActivity");
//                startActivity(ForumMenuActivity.class,false,bundle);
                Intent intent = new Intent(mContext, ForumMenuActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, ConstantUtils.PLATE_CODE);
                break;
            case R.id.tv_right:
                if (OtherUtils.isFastClick()) {
                    requestNet();
                }
                break;
        }
    }

    private void requestNet() {
        token = SPUtils.getString(mContext, SPConstant.TOKEN);
        List<AddPicBean> data = mAdapter.getData();
        LogUtils.printD("图片集合大小 : " + data.size());
        String title = mEdTitle.getText().toString();
        String content = mEdContent.getText().toString();
        if (TextUtils.isEmpty(title)) {
            showToast("标题不能为空");
            return;
        }
//        if (content.length()<10) {
//            showToast("描述不能少于10字");
//            return;
//        }
        if (TextUtils.isEmpty(plateId)) {
            showToast("请选择板块");
            return;
        }
        HashMap<String, String> map = new HashMap();
        map.put("token", token);
        map.put("forum_id", plateId);
        map.put("title", title);
        map.put("content", content);
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
            HttpUtils.postForm(NetConstant.SEND_THEME, map, "file[]", fileMap, new DialogCallBack
                    (mContext) {
                @Override
                public void onResponse(String response, int id) {
                    BaseResp postRequestBean = new Gson().fromJson(response, BaseResp
                            .class);
                    if (handleResponseCode(postRequestBean)) {
                        if (postRequestBean.getStatus() == 1) {
                            showToast(postRequestBean.getMsg(), 1);
                            OtherUtils.delayFinishActivity(SendPostsActivity.this, 1000);
                        }
                    }
                }
            });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ConstantUtils.PLATE_CODE) {
                plateId = data.getExtras().getString(ConstantUtils.FORUM_PLATE_ID);
                String plateTitle = data.getExtras().getString(ConstantUtils.FORUM_PLATE_TITLE);
                mTvResult.setText(plateTitle == null ? "" : plateTitle);
                return;
            }
            if (requestCode == ConstantUtils.RELOGIN_CODE) {
                String ticket = SPUtils.getString(mContext, SPConstant.TICKET);
                HttpUtils.getToken(ticket, new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        TokenBean tokenBean = new Gson().fromJson(response, TokenBean.class);
                        if (tokenBean.getStatus() == 1) {
                            String token = tokenBean.getInfo().getToken();
                            SPUtils.put(mContext, SPConstant.TOKEN, token);
                            checkIsLogin();
                        }
                    }
                });
                return;
            }
        }
    }
}
