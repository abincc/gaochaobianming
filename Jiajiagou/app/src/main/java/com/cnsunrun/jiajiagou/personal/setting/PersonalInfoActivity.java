package com.cnsunrun.jiajiagou.personal.setting;

import android.Manifest;
import android.app.ProgressDialog;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseCallBack;
import com.cnsunrun.jiajiagou.common.base.BaseHeaderActivity;
import com.cnsunrun.jiajiagou.common.base.BaseResp;
import com.cnsunrun.jiajiagou.common.constant.JjgConstant;
import com.cnsunrun.jiajiagou.common.constant.SPConstant;
import com.cnsunrun.jiajiagou.common.network.DialogCallBack;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.ConstantUtils;
import com.cnsunrun.jiajiagou.common.util.ImgPickerUtils;
import com.cnsunrun.jiajiagou.common.util.SPUtils;
import com.cnsunrun.jiajiagou.common.widget.CircleTransform;
import com.cnsunrun.jiajiagou.common.widget.view.LeftEdittext;
import com.cnsunrun.jiajiagou.home.location.LocationAreaActivity;
import com.google.gson.Gson;
import com.yancy.gallerypick.inter.IHandlerCallBack;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.OptionPicker;

import static com.cnsunrun.jiajiagou.R.id.tv_gender;

/**
 * Created by ${LiuDi}
 * on 2017/8/19on 12:01.
 */

public class PersonalInfoActivity extends BaseHeaderActivity
{
    @BindView(R.id.iv_imv)
    ImageView mIvImv;
    @BindView(R.id.et_nick)
    LeftEdittext mEtNick;
    @BindView(R.id.tv_loc)
    TextView mTvLoc;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_identity_card)
    TextView mTvIdentityCard;
    @BindView(tv_gender)
    TextView mTvGender;
    @BindView(R.id.et_age)
    LeftEdittext mEtAge;
    @BindView(R.id.et_signature)
    LeftEdittext mEtSignature;
    @BindView(R.id.et_room_num)
    TextView mEtRoomNum;
    @BindView(R.id.et_building_num)
    TextView mEtBuildingNum;
    @BindView(R.id.tv_code)
    TextView mTvCode;
    @BindView(R.id.rl_code_view)
    RelativeLayout mCodeView;


    private ProgressDialog progressDialog;
    private String photoPath;
    int sex;
    private String mDistrict_id;
    private String mTitle;
    private static int PERMISSIONS_REQUEST_READ_CONTACTS=0;

    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight)
    {
        tvTitle.setText("个人资料");
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("保存");


        tvRight.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                postInfo();
            }
        });


    }

    /**
     * 完善信息
     */
    private void postInfo()
    {
        //楼号
        String nickname = mEtNick.getText().toString().trim();

        String building_no = mEtBuildingNum.getText().toString().trim();

        String room_no = mEtRoomNum.getText().toString().trim();

        String age = mEtAge.getText().toString().trim();

        String signature = mEtSignature.getText().toString().trim();


        if (TextUtils.isEmpty(nickname))
        {
            showToast("昵称不可为空");
            return;
        }
        if (TextUtils.isEmpty(building_no))
        {
            showToast("楼号不可为空");
            return;
        }
        if (TextUtils.isEmpty(room_no))
        {
            showToast("房号不可为空");
            return;
        }
        if (TextUtils.isEmpty(age))
        {
            showToast("请输入您的年龄");
            return;
        }
        if (TextUtils.isEmpty(signature))
        {
            showToast("请输入个性签名");
            return;
        }
        if (TextUtils.isEmpty(mDistrict_id))
        {
            showToast("请选择所在小区");
            return;
        }
        com.cnsunrun.jiajiagou.common.util.LogUtils.printD("post"+mDistrict_id);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", JjgConstant.getToken(mContext));
        hashMap.put("nickname", nickname);
        hashMap.put("district_id", mDistrict_id);
        hashMap.put("building_no", building_no);
        hashMap.put("room_no", room_no);
        hashMap.put("age", age);
        hashMap.put("sex", String.valueOf(sex));
        hashMap.put("signature", signature);
        HttpUtils.post(NetConstant.EDIT_USER_INFO, hashMap, new DialogCallBack(mContext)
        {
            @Override
            public void onResponse(String response, int id)
            {
                LogUtils.d(response);
                BaseResp baseResp = new Gson().fromJson(response, BaseResp.class);
                if (handleResponseCode(baseResp))
                {
                    showToast(baseResp.getMsg(), 1);
                    SPUtils.put(mContext, SPConstant.DISTRICT_ID, mDistrict_id);
                    SPUtils.put(mContext, SPConstant.DISTRICT_TITLE, mTitle);
                    finish();
                }

            }
        });

    }

    @Override
    protected void init()
    {
//        mRelativeLayout.setFocusable(true);
//        mRelativeLayout.setFocusableInTouchMode(true);
//        mRelativeLayout.requestFocus();

        mCodeView.setVisibility( SPUtils.getInt(this,SPConstant.TYPE)==2 ? View.VISIBLE : View.GONE);
        getUserInfo();
    }

    @Override
    public void onRefresh()
    {
        super.onRefresh();
        getUserInfo();
    }

    private void getUserInfo()
    {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", JjgConstant.getToken(mContext));
        HttpUtils.get(NetConstant.GET_USER_INFO, hashMap, new DialogCallBack(mContext)
        {
            @Override
            public void onResponse(String response, int id)
            {
                LogUtils.d(response);
                PersonalInfoResp resp = new Gson().fromJson(response, PersonalInfoResp.class);

                if (handleResponseCode(resp))
                {

                    PersonalInfoResp.InfoBean info = resp.getInfo();

                    getImageLoader().load(info.getHeadimg()).transform(new CircleTransform(mContext)).into(mIvImv);
                    mEtNick.setText(info.getNickname());
//                        mTvLoc.setText(info.get);
                    //楼号
                    mEtBuildingNum.setText(info.getBuilding_no());

                    mEtRoomNum.setText(info.getRoom_no());
                    //姓名
                    mTvName.setText(info.getUsername());
                    //身份证
                    mTvIdentityCard.setText(info.getIdcard());

                    mTvGender.setText(info.getSex() == 1 ? "男" : "女");
                    sex = info.getSex();
                    mEtAge.setText(info.getAge());
                    mDistrict_id = info.getDistrict_id();
                    mEtSignature.setText(info.getSignature());
                    mTvLoc.setText(TextUtils.isEmpty(info.district_title) ? "请选择" : info.district_title);
                    mTvCode.setText(info.register_code);
                }

            }
        });

    }

    /**
     * 选择并上传头像
     */
    private void upLoadImg()
    {
        ImgPickerUtils.getInstance().singleSelect(this, new IHandlerCallBack()
        {
            @Override
            public void onStart()
            {

            }

            @Override
            public void onSuccess(List<String> photoList)
            {
                photoPath = photoList.get(0);
                progressDialog = ProgressDialog.show(mContext, null, "正在上传头像..请稍候", true, false);


                HttpUtils.postImv(new File(photoPath), mContext, new BaseCallBack(mContext)
                {
                    @Override
                    public void onResponse(String response, int id)
                    {
                        LogUtils.d(response);
                        BaseResp baseResp = new Gson().fromJson(response, BaseResp.class);
                        if (handleResponseCode(baseResp))
                        {
                            showToast(baseResp.getMsg(), 1);
                            progressDialog.dismiss();
                            getImageLoader().load(photoPath).transform(new CircleTransform(mContext)).into(mIvImv);
                        }
                    }
                });


            }

            @Override
            public void onCancel()
            {

            }

            @Override
            public void onFinish()
            {

            }

            @Override
            public void onError()
            {

            }
        });
    }

    @Override
    protected int getLayoutRes()
    {
        return R.layout.activity_personal_info;
    }

    @OnClick({R.id.rl_pic, R.id.rl_gender, R.id.rl_loc})
    public void onViewClicked(View view)
    {
        switch (view.getId())
        {
            case R.id.rl_pic:

                if (ContextCompat.checkSelfPermission(PersonalInfoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    LogUtils.i( "需要授权 ");
//                    if (ActivityCompat.shouldShowRequestPermissionRationale(PersonalInfoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                        LogUtils.i(  "进行授权");
                        ActivityCompat.requestPermissions(PersonalInfoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_CONTACTS);
//                    }
                } else {
                    LogUtils.i(  "不需要授权 ");
                    // 进行正常操作
                    upLoadImg();
                }
                break;

            case R.id.rl_gender:

                OptionPicker genderPicker = new OptionPicker(PersonalInfoActivity.this, new String[]{"男", "女"});
                genderPicker.setCycleDisable(true);
                genderPicker.setOffset(2);
                genderPicker.setSelectedIndex(0);
                genderPicker.setTextSize(16);
                genderPicker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener()
                {
                    @Override
                    public void onOptionPicked(int index, String item)
                    {
                        sex = index == 0 ? 1 : 2;
                        mTvGender.setText(index == 0 ? "男" : "女");

                    }
                });
                genderPicker.show();
                break;
            case R.id.rl_loc:
                Bundle bundle = new Bundle();
                bundle.putInt(ConstantUtils.TO_COMMUNITY_SELECT, ConstantUtils.SHOP_ACT);
                Intent intent = new Intent(mContext, LocationAreaActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, ConstantUtils.SHOP_ACT);

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            if (requestCode == ConstantUtils.SHOP_ACT)
            {
                mDistrict_id = data.getStringExtra(ConstantUtils.COMMUNITY_ID);
                mTitle = data.getStringExtra(ConstantUtils.COMMUNITY_TITLE);
                mTvLoc.setText(mTitle);
                com.cnsunrun.jiajiagou.common.util.LogUtils.printD("小区:"+mTitle+"  id: "+mDistrict_id);
                return;
            }
            mDistrict_id = data.getStringExtra(ConstantUtils.COMMUNITY_ID);
            mTitle = data.getStringExtra(ConstantUtils.COMMUNITY_TITLE);
            mTvLoc.setText(mTitle);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        if (ev.getAction() == MotionEvent.ACTION_DOWN)
        {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev))
            {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null)
                {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev))
        {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event)
    {
        if (v != null && (v instanceof EditText))
        {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom)
            {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else
            {
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
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LogUtils.i( "同意授权");
                // 进行正常操作。
                upLoadImg();
            } else {
                LogUtils.i(  "拒绝授权");
            }
        }
    }
}
