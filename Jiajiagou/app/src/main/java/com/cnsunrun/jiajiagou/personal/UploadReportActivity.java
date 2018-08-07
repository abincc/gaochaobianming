package com.cnsunrun.jiajiagou.personal;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseHeaderActivity;
import com.cnsunrun.jiajiagou.common.base.BaseResp;
import com.cnsunrun.jiajiagou.common.constant.SPConstant;
import com.cnsunrun.jiajiagou.common.network.DialogCallBack;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.FileUtils;
import com.cnsunrun.jiajiagou.common.util.ImageUtils;
import com.cnsunrun.jiajiagou.common.util.OtherUtils;
import com.cnsunrun.jiajiagou.common.util.PickerViewUtils;
import com.cnsunrun.jiajiagou.common.util.SPUtils;
import com.cnsunrun.jiajiagou.common.widget.AddPicBean;
import com.cnsunrun.jiajiagou.common.widget.AddPicGridAdapter;
import com.cnsunrun.jiajiagou.common.widget.view.HighLightEditText;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.cnsunrun.jiajiagou.R.id.et_report_date;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-11-24 13:52
 */
public class UploadReportActivity extends BaseHeaderActivity implements HighLightEditText.OnRightDrawableClickListener {
    @BindView(et_report_date)
    HighLightEditText mReportDate;
    @BindView(R.id.recycler_upload)
    RecyclerView mUploadRecycler;
    private AddPicGridAdapter mAddPicGridAdapter;
    private String mToken;

    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight) {
        tvTitle.setText("体检报告");
        tvRight.setText("上传");
        tvRight.setVisibility(View.VISIBLE);
    }

    @Override
    protected void init() {
        mToken = SPUtils.getString(this, SPConstant.TOKEN);
        mUploadRecycler.setLayoutManager(new LinearLayoutManager(this));
        mUploadRecycler.setNestedScrollingEnabled(false);
        mAddPicGridAdapter = new AddPicGridAdapter(R.layout.item_add_pic_peport,
                null, mUploadRecycler, this);
        mUploadRecycler.setAdapter(mAddPicGridAdapter);
        mAddPicGridAdapter.setNewData(new ArrayList<AddPicBean>());
        mReportDate.setOnRightDrawableClickListener(this);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_report_upload;
    }

    @OnClick( R.id.tv_right)
    public void onClick(View v){
        switch (v.getId()){
            case R.id.tv_right:
                if (OtherUtils.isFastClick()) {
                    commitReport();
                }
                break;
        }
    }

    private void commitReport(){
        String date = mReportDate.getText().toString();
        if (TextUtils.isEmpty(date)) {
            showToast("请选择日期");
            return;
        }
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("ope_date", date);
        paramsMap.put("token", mToken);
        Map<String, File> fileMap = new HashMap<>();
        List<AddPicBean> data = mAddPicGridAdapter.getData();
        if (data.size()==1) {
            showToast("请上传图片报告");
            return;
        }
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

        HttpUtils.postForm(NetConstant.PEPORT_COMMIT, paramsMap, "img[]", fileMap, new DialogCallBack(mContext) {


            @Override
            public void onResponse(String response, int id) {
                BaseResp postRequestBean = new Gson().fromJson(response, BaseResp
                        .class);
                if (handleResponseCode(postRequestBean)) {
                    if (postRequestBean.getStatus() == 1) {
                        showToast(postRequestBean.getMsg(), 1);
                        EventBus.getDefault().post(new ReportEvent());
                        OtherUtils.delayFinishActivity(UploadReportActivity.this, 1000);
                    }
                }
            }
        });

    }

    @Override
    public void onRightDrawableClick(HighLightEditText view) {
        PickerViewUtils.show(this,view);
    }
}
