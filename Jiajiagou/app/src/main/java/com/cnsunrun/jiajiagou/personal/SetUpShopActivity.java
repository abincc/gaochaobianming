package com.cnsunrun.jiajiagou.personal;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.RegexUtils;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseHeaderActivity;
import com.cnsunrun.jiajiagou.common.base.BaseResp;
import com.cnsunrun.jiajiagou.common.constant.SPConstant;
import com.cnsunrun.jiajiagou.common.network.DialogCallBack;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.ConstantUtils;
import com.cnsunrun.jiajiagou.common.util.ImgPickerUtils;
import com.cnsunrun.jiajiagou.common.util.LogUtils;
import com.cnsunrun.jiajiagou.common.util.SPUtils;
import com.cnsunrun.jiajiagou.home.location.LocationAreaActivity;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ${LiuDi}
 * on 2017/8/22on 10:17.
 */

public class SetUpShopActivity extends BaseHeaderActivity {
    @BindView(R.id.et_shop_name)
    EditText mShopName;//店铺名称
    @BindView(R.id.district_name)
    TextView mDistrictName;//小区名称
    @BindView(R.id.iv_arrow)
    ImageView mArrowSelect;//小区选择
    @BindView(R.id.et_floor_number)
    EditText mFloorNumber;//楼层号
    @BindView(R.id.et_user_name)
    EditText mUserName;//姓名
    @BindView(R.id.ed_phone_number)
    EditText mPhoneNumber;//手机号
    @BindView(R.id.et_email)
    EditText mEmail;//邮箱
    @BindView(R.id.et_id_card)
    EditText mIdCard;//身份证号码
    @BindView(R.id.tv_id_card_btn_positive)
    TextView mIdCardBtnPositive;
    @BindView(R.id.tv_id_card_btn_back)
    TextView mIdCardBtnBack;
    @BindView(R.id.iv_id_card_positive)
    ImageView mIdCardImgPositive;//身份证正面
    @BindView(R.id.iv_id_card_back)
    ImageView mIdCardImgBack;//身份证背面
    private File mFrontCardFile;
    private File mNegativeCardFile;
    private String mCommunityId;
    private static int PERMISSIONS_REQUEST_READ_CONTACTS_1=1;
    private static int PERMISSIONS_REQUEST_READ_CONTACTS_2=2;


    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight) {
        tvTitle.setText("我要开店");
        tvRight.setText("提交");
        tvRight.setVisibility(View.VISIBLE);
    }

    @Override
    protected void init() {
        String id = SPUtils.getString(mContext, SPConstant.DISTRICT_ID);
        if (!TextUtils.isEmpty(id)) {
            mCommunityId = id;
        }
        String title = SPUtils.getString(mContext, SPConstant.DISTRICT_TITLE);
        if (!TextUtils.isEmpty(title)) {
            mDistrictName.setText(title);
        }
    }

    @OnClick({R.id.tv_id_card_btn_positive, R.id.tv_id_card_btn_back, R.id.tv_right, R.id.iv_arrow})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_arrow:
                Bundle bundle = new Bundle();
                bundle.putInt(ConstantUtils.TO_COMMUNITY_SELECT, ConstantUtils.SHOP_ACT);
                Intent intent = new Intent(mContext, LocationAreaActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, ConstantUtils.SHOP_ACT);
                break;
            case R.id.tv_id_card_btn_positive://正面按钮

                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    LogUtils.i( "需要授权 ");
//                    if (ActivityCompat.shouldShowRequestPermissionRationale(SetUpShopActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                        LogUtils.i( "拒绝过了");
                        // 提示用户如果想要正常使用，要手动去设置中授权。
//                        Toast.makeText(mContext, "请在 设置-应用管理 中开启此应用的储存授权。", Toast.LENGTH_SHORT).show();
//                        showToast("请在 设置-应用管理 中开启此应用的储存授权");
//                    } else {
                        LogUtils.i(  "进行授权");
                        ActivityCompat.requestPermissions(SetUpShopActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_CONTACTS_1);
//                    }
                } else {
                    LogUtils.i(  "不需要授权 ");
                    // 进行正常操作
                    ImgPickerUtils.getInstance().singleSelect(this, new ImgPickerUtils
                            .ImagePickerCallbackImpl() {

                        @Override
                        public void onSuccess(List<String> photoList) {
                            if (photoList.size() > 0) {
                                String path = photoList.get(0);
                                mFrontCardFile = new File(path);
                                getImageLoader().load(path).into(mIdCardImgPositive);
//                            Glide.with(mContext).load(path).into(mIdCardImgPositive);
                            }
                        }
                    });
                }


                break;
            case R.id.tv_id_card_btn_back://背面按钮

                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(SetUpShopActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_CONTACTS_2);
                } else {
                    LogUtils.i(  "不需要授权 ");
                    // 进行正常操作
                    ImgPickerUtils.getInstance().singleSelect(this, new ImgPickerUtils
                            .ImagePickerCallbackImpl() {

                        @Override
                        public void onSuccess(List<String> photoList) {
                            if (photoList.size() > 0) {
                                String path = photoList.get(0);
                                mNegativeCardFile = new File(path);
                                getImageLoader().load(path).into(mIdCardImgBack);
//                            Glide.with(mContext).load(path).into(mIdCardImgPositive);
                            }
                        }
                    });
                }

//                ImgPickerUtils.getInstance().singleSelect(this, new ImgPickerUtils
//                        .ImagePickerCallbackImpl() {
//
//                    @Override
//                    public void onSuccess(List<String> photoList) {
//                        if (photoList.size() > 0) {
//                            String path = photoList.get(0);
//                            mNegativeCardFile = new File(path);
//                            getImageLoader().load(path).into(mIdCardImgBack);
////                            Glide.with(mContext).load(path).into(mIdCardImgBack);
//                        }
//                    }
//                });
                LogUtils.printD("gone");
                break;
            case R.id.tv_right://提交
                String shopName = mShopName.getText().toString();
                String floorNumber = mFloorNumber.getText().toString();
                String name = mUserName.getText().toString();
                String phoneNumber = mPhoneNumber.getText().toString();
                String email = mEmail.getText().toString();
                String idCardNumber = mIdCard.getText().toString();
                Map<String, String> map = new HashMap<>();
                map.put("token", SPUtils.getString(this, SPConstant.TOKEN));
                map.put("shop_name", shopName);
                map.put("district_id", mCommunityId);
                map.put("building_no", floorNumber);
                map.put("username", name);
                map.put("mobile", phoneNumber);
                map.put("email", email);
                map.put("idcard", idCardNumber);
                if (TextUtils.isEmpty(shopName)) {
                    showToast("请填写店铺名称");
                    return;
                }
                if (TextUtils.isEmpty(mDistrictName.getText().toString())) {
                    showToast("请选择小区");
                    return;
                }

                if (TextUtils.isEmpty(floorNumber)) {
                    showToast("请填写楼号");
                    return;
                }
                if (TextUtils.isEmpty(name)) {
                    showToast("请填写姓名");
                    return;
                }
                if (TextUtils.isEmpty(phoneNumber)) {
                    showToast("请填写手机号");
                    return;
                }
                if (!RegexUtils.isMobileExact(phoneNumber)) {
                    showToast("请输入正确的手机号");
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    showToast("请填写电子邮件");
                    return;
                }
                if (!RegexUtils.isEmail(email)) {
                    showToast("请输入正确的邮箱");
                    return;
                }
                if (TextUtils.isEmpty(idCardNumber)) {
                    showToast("请填写身份证号码");
                    return;
                }
                if (!RegexUtils.isIDCard18(idCardNumber)) {
                    showToast("请输入正确的身份证号码");
                    return;
                }
                if (mFrontCardFile == null) {
                    showToast("请上传身份证");
                    return;
                }
                if (mNegativeCardFile == null) {
                    showToast("请上传身份证");
                    return;
                }
//                Map<String, File> fileMap = new HashMap<>();
//                fileMap.put(mFrontCardFile.getName(),mFrontCardFile);
//                fileMap.put(mNegativeCardFile.getName(),mNegativeCardFile);
//                HttpUtils.postForm(NetConstant.APPLY_SHOP,map,);
                OkHttpUtils.post()
                        .url(NetConstant.BASE_URL + NetConstant.APPLY_SHOP)
                        .params(map)
                        .addFile("front_card", mFrontCardFile.getName(), mFrontCardFile)
                        .addFile("negative_card", mNegativeCardFile.getName(), mNegativeCardFile)
                        .build()
                        .execute(new DialogCallBack(mContext) {

                            @Override
                            public void onResponse(String response, int id) {
                                BaseResp requestBean = new Gson().fromJson(response,
                                        BaseResp.class);
                                if (handleResponseCode(requestBean)) {
                                    if (requestBean.getStatus() == 1) {
                                        showToast(requestBean.getMsg(),1);
                                        SetUpShopActivity.this.finish();
                                    }else {
                                        showToast(requestBean.getMsg());
                                    }
                                }
                            }
                        });
                break;
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_set_up_shop;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ConstantUtils.SHOP_ACT) {
                mCommunityId = data.getStringExtra(ConstantUtils.COMMUNITY_ID);
                String title = data.getStringExtra(ConstantUtils.COMMUNITY_TITLE);
                mDistrictName.setText(title);
                return;
            }
            mCommunityId = data.getStringExtra(ConstantUtils.COMMUNITY_ID);
            String title = data.getStringExtra(ConstantUtils.COMMUNITY_TITLE);
            mDistrictName.setText(title);
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context
                        .INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                //使EditText触发一次失去焦点事件
                v.setFocusable(false);
//                v.setFocusable(true); //这里不需要是因为下面一句代码会同时实现这个功能
                v.setFocusableInTouchMode(true);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS_1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               LogUtils.i( "同意授权");
                // 进行正常操作。
                ImgPickerUtils.getInstance().singleSelect(this, new ImgPickerUtils
                        .ImagePickerCallbackImpl() {

                    @Override
                    public void onSuccess(List<String> photoList) {
                        if (photoList.size() > 0) {
                            String path = photoList.get(0);
                            mFrontCardFile = new File(path);
                            getImageLoader().load(path).into(mIdCardImgPositive);
//                            Glide.with(mContext).load(path).into(mIdCardImgPositive);
                        }
                    }
                });
            } else {
                LogUtils.i(  "拒绝授权");
            }
        }else if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS_2){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LogUtils.i( "同意授权");
                // 进行正常操作。
                ImgPickerUtils.getInstance().singleSelect(this, new ImgPickerUtils
                        .ImagePickerCallbackImpl() {

                    @Override
                    public void onSuccess(List<String> photoList) {
                        if (photoList.size() > 0) {
                            String path = photoList.get(0);
                            mNegativeCardFile = new File(path);
                            getImageLoader().load(path).into(mIdCardImgBack);
//                            Glide.with(mContext).load(path).into(mIdCardImgPositive);
                        }
                    }
                });
            } else {
                LogUtils.i(  "拒绝授权");
            }
        }
    }
}
