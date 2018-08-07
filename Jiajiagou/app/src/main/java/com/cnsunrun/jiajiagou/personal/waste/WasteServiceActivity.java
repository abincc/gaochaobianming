package com.cnsunrun.jiajiagou.personal.waste;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseCallBack;
import com.cnsunrun.jiajiagou.common.base.BaseHeaderActivity;
import com.cnsunrun.jiajiagou.common.base.BaseResp;
import com.cnsunrun.jiajiagou.common.constant.ArgConstants;
import com.cnsunrun.jiajiagou.common.constant.SPConstant;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.FileUtils;
import com.cnsunrun.jiajiagou.common.util.ImageUtils;
import com.cnsunrun.jiajiagou.common.util.OtherUtils;
import com.cnsunrun.jiajiagou.common.util.PickerViewOtherUtils;
import com.cnsunrun.jiajiagou.common.util.SPUtils;
import com.cnsunrun.jiajiagou.common.util.ToastTask;
import com.cnsunrun.jiajiagou.common.widget.AddPicBean;
import com.cnsunrun.jiajiagou.common.widget.AddPicGridAdapter;
import com.cnsunrun.jiajiagou.common.widget.CustomDialog;
import com.cnsunrun.jiajiagou.common.widget.view.HighLightEditText;
import com.cnsunrun.jiajiagou.home.SelectServiceTypePopup;
import com.cnsunrun.jiajiagou.home.adapter.ServiceCleanAdapter;
import com.cnsunrun.jiajiagou.home.adapter.ServiceHelpAdapter;
import com.cnsunrun.jiajiagou.home.bean.ServiceCleanBean;
import com.cnsunrun.jiajiagou.login.LoginActivity;
import com.cnsunrun.jiajiagou.personal.setting.address.AddressActivity;
import com.cnsunrun.jiajiagou.personal.setting.address.AddressBean;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.cnsunrun.jiajiagou.R.id.et_desc;
import static com.cnsunrun.jiajiagou.R.id.et_report_date;
import static com.cnsunrun.jiajiagou.R.id.tv_right;

/**
 * Description:
 * Data：2017/8/24 0024-上午 9:56
 * Blog：http://blog.csdn.net/u013983934
 * Author: CZ
 */
public class WasteServiceActivity extends BaseHeaderActivity implements HighLightEditText.OnRightDrawableClickListener  {
    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    @BindView(R.id.rl_address)
    RelativeLayout mRlAddress;
    @BindView(R.id.tv_loc_empty)
    TextView mTvLocEmpty;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_loc)
    TextView mTvLoc;
    @BindView(R.id.tv_mobile)
    TextView mTvMobile;
    @BindView(et_desc)
    EditText mEtDesc;

    @BindView(et_report_date)
    HighLightEditText mReportDate;

    public static final int TYPE_CLEAN = 0x11;
    private AddPicGridAdapter mAdapter;
    private SelectServiceTypePopup mSelectServiceTypePopup;
    private int mType;
    private String SERVICE_TYPE_ID;//服务类型id

    private ServiceHelpAdapter mServiceHelpAdapter;
    private ServiceCleanAdapter mServiceCleanAdapter;
    private CustomDialog mDialog;
    private String mAddress_id;


    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight) {
        tvTitle.setText("废品订单");
        tvRight.setText("提交");
        tvRight.setVisibility(View.VISIBLE);
    }

    @Override
    protected void init() {

        mRecycler.setLayoutManager(new GridLayoutManager(mContext, 4));
        mAdapter = new AddPicGridAdapter(R.layout.item_add_pic_cell, null, mRecycler,
                WasteServiceActivity.this);
        mRecycler.setAdapter(mAdapter);
        mAdapter.setNewData(new ArrayList<AddPicBean>());

        mServiceCleanAdapter = new ServiceCleanAdapter(R.layout.item_service_type);
        mServiceCleanAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<ServiceCleanBean.InfoBean> data = adapter.getData();
                SERVICE_TYPE_ID = data.get(position).getClean_service_id();
                String title = data.get(position).getTitle();
                showToast(title + ":" + SERVICE_TYPE_ID);
                mSelectServiceTypePopup.dismiss();
            }
        });

        mTvLocEmpty.setVisibility(View.VISIBLE);
        mRlAddress.setVisibility(View.INVISIBLE);

        mReportDate.setOnRightDrawableClickListener(this);
        }



    @Override
    protected int getLayoutRes() {
        return R.layout.activity_waste_order;
    }

    @OnClick({R.id.tv_right, R.id.fl_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fl_address:
                Intent intent = new Intent(mContext, AddressActivity.class);
                intent.putExtra(ArgConstants.IS_FROM_ORDER, true);
                startActivityForResult(intent, 200);
                break;
            case tv_right:
//                showToast(mAddress_id);
                String content = mEtDesc.getText().toString();//服务内容
                String  token = SPUtils.getString(mContext, SPConstant.TOKEN);
                if (TextUtils.isEmpty(token)) {
                    startActivity(LoginActivity.class, true);
                }

                if (mAddress_id == null)
                {
                    showToast("请选择收货地址");
                    return;
                }

                String date = mReportDate.getText().toString();
                if (TextUtils.isEmpty(date)) {
                    showToast("请选择废品预约上门收货时间");
                    return;
                }

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
                    return;
                }

                HashMap<String, String> map = new HashMap<>();
                map.put("token", token);
                map.put("address_id", mAddress_id);
                map.put("details", content);
                map.put("date", date);
                String url = NetConstant.WASTE_ORDER;

                HttpUtils.postForm(url, map, "img[]", fileMap, new BaseCallBack(mContext) {

                    @Override
                    public void onResponse(String response, int id) {
                        BaseResp bean = new Gson().fromJson(response,
                                BaseResp.class);
                        if (handleResponseCode(bean)) {
                            if (bean.getStatus() == 1) {
                                ToastTask.getInstance(mContext).setMessage(bean.getMsg()).success();
                                OtherUtils.delayFinishActivity(WasteServiceActivity.this);
                            }
                        }
                    }

                });
                break;
        }
    }

    private void setAddress(AddressBean address)
    {
        mTvName.setText(address.name);
        mTvLoc.setText(address.address_detail);
        mTvMobile.setText(address.mobile);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200)
        {
            if (data != null)
            {
                AddressBean addressBean = data.getParcelableExtra(ArgConstants.ADDRESSBEAN);
                mTvLocEmpty.setVisibility(View.GONE);
                mRlAddress.setVisibility(View.VISIBLE);
                mAddress_id = addressBean.address_id;
                setAddress(addressBean);
            }
        }

    }

    @Override
    public void onRightDrawableClick(HighLightEditText view) {
        PickerViewOtherUtils.show(this,view);
    }
}
