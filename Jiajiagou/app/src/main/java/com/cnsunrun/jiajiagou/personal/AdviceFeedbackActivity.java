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
import com.cnsunrun.jiajiagou.common.network.DialogCallBack;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.FileUtils;
import com.cnsunrun.jiajiagou.common.util.ImageUtils;
import com.cnsunrun.jiajiagou.common.util.SPUtils;
import com.cnsunrun.jiajiagou.common.widget.AddPicBean;
import com.cnsunrun.jiajiagou.common.widget.AddPicGridAdapter;
import com.cnsunrun.jiajiagou.home.bean.PostRequestBean;
import com.cnsunrun.jiajiagou.login.LoginActivity;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by ${LiuDi}
 * on 2017/8/25on 9:55.
 */

public class AdviceFeedbackActivity extends BaseHeaderActivity
{
    @BindView(R.id.et_desc)
    EditText mEtDesc;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    private AddPicGridAdapter mAdapter;

    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight)
    {
        tvTitle.setText("意见反馈");
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("提交");
    }

    @Override
    protected void init()
    {
        mRecycler.setLayoutManager(new GridLayoutManager(mContext, 4));
        mAdapter = new AddPicGridAdapter(R.layout.item_add_pic_cell, null, mRecycler, AdviceFeedbackActivity.this);
        mRecycler.setAdapter(mAdapter);
        mAdapter.setNewData(new ArrayList<AddPicBean>());

    }
    @OnClick(R.id.tv_right)
    public void onClick(View v){
        switch (v.getId()){
            case R.id.tv_right:
                String content = mEtDesc.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    showToast("请填写内容");
                    return;
                }
                Map<String,String> map=new HashMap<>();
                map.put("token", SPUtils.getString(this,SPConstant.TOKEN));
                map.put("content",content);
                List<AddPicBean> data = mAdapter.getData();
                Map<String,File> fileMap=new HashMap<>();
                for (AddPicBean bean : data) {
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
                HttpUtils.postForm(NetConstant.FEEDBACK, map, "img", fileMap, new DialogCallBack(mContext)
                {

                    @Override
                    public void onResponse(String response, int id) {
                        PostRequestBean postRequestBean = new Gson().fromJson(response,
                                PostRequestBean.class);
                        if (postRequestBean.getStatus()==1) {
                            showToast(postRequestBean.getMsg(), 1);
                            AdviceFeedbackActivity.this.finish();
                        }
                        if (postRequestBean.getStatus()==-2) {
                            startActivity(LoginActivity.class);
                        }
                    }
                });
                break;
        }
    }

    @Override
    protected int getLayoutRes()
    {
        return R.layout.activity_advice_feedback;
    }
}
