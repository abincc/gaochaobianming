package com.cnsunrun.jiajiagou.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.ConvertUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseCallBack;
import com.cnsunrun.jiajiagou.common.base.BaseHeaderActivity;
import com.cnsunrun.jiajiagou.common.base.BaseResp;
import com.cnsunrun.jiajiagou.common.constant.ArgConstants;
import com.cnsunrun.jiajiagou.common.constant.SPConstant;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.ConstantUtils;
import com.cnsunrun.jiajiagou.common.util.FileUtils;
import com.cnsunrun.jiajiagou.common.util.ImageUtils;
import com.cnsunrun.jiajiagou.common.util.OtherUtils;
import com.cnsunrun.jiajiagou.common.util.SPUtils;
import com.cnsunrun.jiajiagou.common.util.ToastTask;
import com.cnsunrun.jiajiagou.common.widget.AddPicBean;
import com.cnsunrun.jiajiagou.common.widget.AddPicGridAdapter;
import com.cnsunrun.jiajiagou.common.widget.CustomDialog;
import com.cnsunrun.jiajiagou.common.widget.popup.RelativePopupWindow;
import com.cnsunrun.jiajiagou.home.adapter.ServiceCleanAdapter;
import com.cnsunrun.jiajiagou.home.adapter.ServiceHelpAdapter;
import com.cnsunrun.jiajiagou.home.bean.ServiceCleanBean;
import com.cnsunrun.jiajiagou.home.bean.ServiceHelpBean;
import com.cnsunrun.jiajiagou.home.location.LocationAreaActivity;
import com.cnsunrun.jiajiagou.login.LoginActivity;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.cnsunrun.jiajiagou.R.id.et_desc;
import static com.cnsunrun.jiajiagou.R.id.tv_right;
import static com.cnsunrun.jiajiagou.R.id.tv_select_area;
import static com.cnsunrun.jiajiagou.R.id.tv_select_type;

/**
 * Description:
 * Data：2017/8/24 0024-上午 9:56
 * Blog：http://blog.csdn.net/u013983934
 * Author: CZ
 */
public class PostPropertyServiceActivity extends BaseHeaderActivity implements View
        .OnClickListener {
    @BindView(tv_select_area)
    TextView mTvSelectArea;
    @BindView(tv_select_type)
    TextView mTvSelectType;
    @BindView(et_desc)
    EditText mEtDesc;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    public static final int TYPE_CLEAN = 0x11;
    public static final int TYPE_PACKAGE = 0x22;
    private AddPicGridAdapter mAdapter;
    private SelectServiceTypePopup mSelectServiceTypePopup;
    private int mType;
    private String SERVICE_TYPE_ID;//服务类型id

    private ServiceHelpAdapter mServiceHelpAdapter;
    private ServiceCleanAdapter mServiceCleanAdapter;
    private String mCommunityId;
    private String mTitle;
    private String defaultContent = "下拉选择";
    private CustomDialog mDialog;


    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight) {
        tvTitle.setText(mType == TYPE_CLEAN ? "保洁维修" : mType == TYPE_PACKAGE ? "小事帮忙" : "");
        tvRight.setText("提交");
        tvRight.setVisibility(View.VISIBLE);
    }

    @Override
    protected void init() {
        mTvSelectType.setText(defaultContent);
        mTitle = SPUtils.getString(mContext, SPConstant.DISTRICT_TITLE);
//        mId = SPUtils.getString(mContext, SPConstant.DISTRICT_ID);
        if (!TextUtils.isEmpty(mTitle)) {
            mTvSelectArea.setText(mTitle);
        }
        mType = getIntent().getIntExtra(ArgConstants.SERVICE_TYPE, TYPE_PACKAGE);
        requestNet();
//        mTvSelectType.setText(mType == TYPE_CLEAN ? "保洁" : mType == TYPE_PACKAGE ? "代收快递" : "");
        mRecycler.setLayoutManager(new GridLayoutManager(mContext, 4));
        mAdapter = new AddPicGridAdapter(R.layout.item_add_pic_cell, null, mRecycler,
                PostPropertyServiceActivity.this);
        mRecycler.setAdapter(mAdapter);
        mAdapter.setNewData(new ArrayList<AddPicBean>());
        if (mType == TYPE_CLEAN) {//保洁维修
            mServiceCleanAdapter = new ServiceCleanAdapter(R.layout.item_service_type);
            mServiceCleanAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    List<ServiceCleanBean.InfoBean> data = adapter.getData();
                    SERVICE_TYPE_ID = data.get(position).getClean_service_id();
                    String title = data.get(position).getTitle();
                    mTvSelectType.setText(title);
                    mSelectServiceTypePopup.dismiss();

                }
            });
        }
        if (mType == TYPE_PACKAGE) {//小事帮忙
            mServiceHelpAdapter = new ServiceHelpAdapter(R.layout.item_service_type);
            mServiceHelpAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    List<ServiceHelpBean.InfoBean> data = adapter.getData();
                    SERVICE_TYPE_ID = data.get(position).getTrifle_service_id();
                    String title = data.get(position).getTitle();
                    mTvSelectType.setText(title);
                    mSelectServiceTypePopup.dismiss();

                }
            });
        }

    }

    private void requestNet() {
        String url = mType == TYPE_CLEAN ? NetConstant.CLEANING_MAINTENANCE_SERVICE : mType ==
                TYPE_PACKAGE
                ? NetConstant.LITTLE_HELP_SERVICE : "";
        HttpUtils.get(url, null, new BaseCallBack(mContext)
        {
            @Override
            public void onResponse(String response, int id) {
                if (mType == TYPE_CLEAN) {//保洁维修
                    ServiceCleanBean cleanBean = new Gson().fromJson(response, ServiceCleanBean
                            .class);
                    if (cleanBean.getStatus() == 1) {
                        mServiceCleanAdapter.setNewData(cleanBean.getInfo());
                    }
                } else {
                    ServiceHelpBean helpBean = new Gson().fromJson(response, ServiceHelpBean
                            .class);
                    if (helpBean.getStatus() == 1) {
//                            LogUtils.printD("type_size :"+typeBean.getInfo().size());
                        mServiceHelpAdapter.setNewData(helpBean.getInfo());
//                            mServiceHelpAdapter.notifyDataSetChanged();
                    }
                }


            }
        });
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_post_property_service;
    }

    @OnClick({tv_select_area, tv_select_type, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case tv_select_area:
                Bundle bundle = new Bundle();
                bundle.putInt(ConstantUtils.TO_COMMUNITY_SELECT, ConstantUtils
                        .POST_PROPERTY_SERVICE_ACT);
                Intent intent = new Intent(mContext, LocationAreaActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, ConstantUtils.POST_PROPERTY_SERVICE_ACT);
                break;
            case tv_select_type:
                if (mSelectServiceTypePopup == null) {
                    if (mType == TYPE_CLEAN) {
                        mSelectServiceTypePopup = new SelectServiceTypePopup(mContext, -2, -2,
                                mServiceCleanAdapter);
                    }
                    if (mType == TYPE_PACKAGE) {//小事帮忙
                        mSelectServiceTypePopup = new SelectServiceTypePopup(mContext, -2, -2,
                                mServiceHelpAdapter);
                    }
                }
//                    mSelectServiceTypePopup = new SelectServiceTypePopup(mContext, -2, -2, this);


                mSelectServiceTypePopup.showOnAnchor(view, RelativePopupWindow
                                .VerticalPosition.BELOW, RelativePopupWindow.HorizontalPosition
                                .CENTER,
                        0, ConvertUtils.dp2px
                                (mContext, 0));
                break;
            case tv_right:
                String content = mEtDesc.getText().toString();//服务内容
                String token = SPUtils.getString(mContext, SPConstant.TOKEN);
                if (TextUtils.isEmpty(token)) {
                    startActivity(LoginActivity.class, true);
                }
                if (TextUtils.isEmpty(SERVICE_TYPE_ID)) {
                    ToastTask.getInstance(mContext).setMessage("请选择服务类型").error();
                    return;
                }
                if (TextUtils.isEmpty(content)) {

                    ToastTask.getInstance(mContext).setMessage("请填写内容").error();
                    return;
                }
//                if (TextUtils.isEmpty(mTvSelectArea.getText())) {
//                    return;
//                }
                if (mDialog == null)
                    mDialog = new CustomDialog(mContext, R.style.CustomDialog);
                mDialog.show();
                List<AddPicBean> data = mAdapter.getData();
                Map<String, File> fileMap = new HashMap<>();
                if (data.size() > 0) {
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
                }
                if (fileMap.size()==0) {
                    ToastTask.getInstance(mContext).setMessage("请添加图片").error();
                    if (mDialog != null)
                        mDialog.dismiss();
                    return;
                }

                HashMap<String, String> map = new HashMap<>();
                map.put("token", token);
                map.put("content", content);
                String url = "";
                if (mType == TYPE_CLEAN) {//保洁维修
                    url = NetConstant.CLEANING_MAINTENANCE_SUBMIT;
                    map.put("clean_service_id", SERVICE_TYPE_ID);
                }
                if (mType == TYPE_PACKAGE) {//小事帮忙
                    url = NetConstant.LITTLE_HELP_SUBMIT;
                    map.put("trifle_service_id", SERVICE_TYPE_ID);

                }
                HttpUtils.postForm(url, map, "img[]", fileMap, new BaseCallBack(mContext) {

                    @Override
                    public void onResponse(String response, int id) {
                        BaseResp bean = new Gson().fromJson(response,
                                BaseResp.class);
                        if (handleResponseCode(bean)) {
                            if (bean.getStatus() == 1) {
                                ToastTask.getInstance(mContext).setMessage(bean.getMsg()).success();
                                if (mDialog != null)
                                    mDialog.dismiss();
                                OtherUtils.delayFinishActivity(PostPropertyServiceActivity.this);
                            }
                        }
                    }

                });
                break;
        }
    }


    @Override
    public void onClick(View v) {
//        mTvSelectType.setText(((TextView) v).getText());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ConstantUtils.POST_PROPERTY_SERVICE_ACT) {
                mCommunityId = data.getStringExtra(ConstantUtils.COMMUNITY_ID);
                String title = data.getStringExtra(ConstantUtils.COMMUNITY_TITLE);
                mTvSelectArea.setText(title);
                return;
            }
            mCommunityId = data.getStringExtra(ConstantUtils.COMMUNITY_ID);
            String title = data.getStringExtra(ConstantUtils.COMMUNITY_TITLE);
            mTvSelectArea.setText(title);
        }

    }
}
