package com.cnsunrun.jiajiagou.regist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.blankj.utilcode.utils.ConvertUtils;
import com.blankj.utilcode.utils.RegexUtils;
import com.blankj.utilcode.utils.ScreenUtils;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseHeaderActivity;
import com.cnsunrun.jiajiagou.common.base.BaseResp;
import com.cnsunrun.jiajiagou.common.network.DialogCallBack;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.ConstantUtils;
import com.cnsunrun.jiajiagou.common.util.LogUtils;
import com.cnsunrun.jiajiagou.common.widget.BtnTimerCount;
import com.cnsunrun.jiajiagou.common.widget.view.HighLightEditText;
import com.cnsunrun.jiajiagou.forum.bean.RegistBean;
import com.cnsunrun.jiajiagou.home.location.LocationAreaActivity;
import com.cnsunrun.jiajiagou.login.LoginActivity;
import com.google.gson.Gson;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

import static com.cnsunrun.jiajiagou.R.id.et_area;

/**
 * Description:
 * Data：2017/8/18 0018-上午 11:59
 * Blog：http://blog.csdn.net/u013983934
 * Author: CZ
 */
public class RegistActivity extends BaseHeaderActivity implements RadioGroup
        .OnCheckedChangeListener, HighLightEditText.OnRightDrawableClickListener {
    @BindView(R.id.rg_regist_type)
    RadioGroup mRgRegistType;
    @BindView(et_area)
    HighLightEditText mEtArea;
    @BindView(R.id.et_name)
    HighLightEditText mEtName;
    @BindView(R.id.et_card_num)
    HighLightEditText mEtCardNum;
    @BindView(R.id.et_building_num)
    HighLightEditText mEtBuildingNum;
    @BindView(R.id.et_room_num)
    HighLightEditText mEtRoomNum;
    @BindView(R.id.et_phone)
    HighLightEditText mEtPhone;
    @BindView(R.id.et_psw)
    HighLightEditText mEtPsw;
    @BindView(R.id.btn_getcode)
    Button mBtnGetcode;
    @BindView(R.id.et_code)
    HighLightEditText mEtCode;
    @BindView(R.id.et_invite_code)
    HighLightEditText mEtInviteCode;
    private ErrTipPopupWindow mErrTipPopupWindow;
    private String locationResult;

    private int userType = 2;   //注册账户类  ： 1-租户 ，2-业主， 3-物业, 默认为2
    private String mCommunityid;

    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight) {
        tvTitle.setText("注册");
    }

    @Override
    protected void init() {
        LogUtils.d("userType: " + userType);
        int type = mEtPsw.getInputType();
        LogUtils.d("psd: " + type);

        mEtArea.setOnRightDrawableClickListener(new HighLightEditText
                .OnRightDrawableClickListener() {
            @Override
            public void onRightDrawableClick(HighLightEditText highLightEditText) {
                Bundle bundle = new Bundle();
                bundle.putInt(ConstantUtils.TO_COMMUNITY_SELECT, ConstantUtils.REGISTERED_ACT);
                Intent intent = new Intent(mContext, LocationAreaActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, ConstantUtils.REGISTERED_ACT);
            }
        });
        mRgRegistType.setOnCheckedChangeListener(this);
        ((RadioButton) mRgRegistType.getChildAt(0)).setChecked(true);

        mEtPsw.setOnRightDrawableClickListener(this);
    }

    @OnClick({R.id.btn_regist, R.id.btn_getcode})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_regist:
                sendRequest();
                break;
            case R.id.btn_getcode:
                String phoneNumber = mEtPhone.getText().toString();
                if (!RegexUtils.isMobileExact(phoneNumber)) {
                    showToast("请输入正确的手机号");
                    return;
                }
                HashMap<String, String> map = new HashMap<>();
                map.put("mobile", phoneNumber);
                map.put("type", 1 + "");//业务类型 1-注册业务  2-找回密码业务 3-修改原始手机 4-修改新手机
                HttpUtils.post(NetConstant.SEND_MOBILE_CODE, map, new DialogCallBack(mContext) {

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.d(response);
                        BaseResp resp = new Gson().fromJson(response, BaseResp
                                .class);
                        if (handleResponseCode(resp)) {
                            showToast(resp.getMsg(), 1);
                            new BtnTimerCount(60 * 1000, 1000, mBtnGetcode).start();
                        }
                    }
                });
                break;
        }
    }

    private void sendRequest() {
        HashMap<String, String> requstParamMap = new HashMap();
        if (!RegexUtils.isMobileExact(mEtPhone.getText())) {
            showToast("请输入正确的手机号");
            return;
        }
        if (TextUtils.isEmpty(mEtName.getText()) || TextUtils.isEmpty(mEtCardNum.getText()) || TextUtils.isEmpty
                (mEtPhone.getText()) || TextUtils.isEmpty(mEtCode.getText()) || TextUtils.isEmpty(mEtPsw.getText())) {
            showToast("请填写完所有信息");
            return;
        }
        requstParamMap.put("type", userType + "");
        requstParamMap.put("username", mEtName.getText().toString());
        requstParamMap.put("idcard", mEtCardNum.getText().toString());
        requstParamMap.put("mobile", mEtPhone.getText().toString());
        requstParamMap.put("code", mEtCode.getText().toString());
        requstParamMap.put("password", mEtPsw.getText().toString());
        if (userType == 2) {//业主
            if (TextUtils.isEmpty(mCommunityid)||TextUtils.isEmpty(mEtBuildingNum.getText())||TextUtils.isEmpty(mEtRoomNum.getText())){
                showToast("请填写完所有信息");
                return;
            }
            requstParamMap.put("district_id", mCommunityid);
            requstParamMap.put("building_no", mEtBuildingNum.getText().toString());
            requstParamMap.put("room_no", mEtRoomNum.getText().toString());
        }
        if (userType == 1) {//租客
//            requstParamMap.put("district_id","");
//            requstParamMap.put("room_no", mEtRoomNum.getText().toString());
            if (TextUtils.isEmpty(mEtInviteCode.getText())) {
                showToast("请填写邀请码");
                return;
            }
            requstParamMap.put("register_code", mEtInviteCode.getText().toString());
        }

        HttpUtils.post(NetConstant.REGISTER, requstParamMap, new DialogCallBack(mContext) {
            @Override
            public void onResponse(String response, int id) {
                RegistBean registBean = new Gson().fromJson(response, RegistBean.class);
                if (registBean.getStatus() == 1) {
                    startActivity(LoginActivity.class, true);
                } else {
                    showToast(registBean.getMsg(), 1);
                }
            }
        });
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_regist;
    }

    @OnClick({R.id.btn_getcode})
    public void onViewClicked() {
        if (mErrTipPopupWindow == null) {
            mErrTipPopupWindow = new ErrTipPopupWindow(mContext, ScreenUtils.getScreenWidth
                    (mContext) - ConvertUtils
                    .dp2px(mContext, 15), -2);
        }

//        mErrTipPopupWindow.setText("信息不正确");
//        mErrTipPopupWindow.showOnAnchor(mEtCardNum, RelativePopupWindow
//                        .VerticalPosition.BELOW, RelativePopupWindow.HorizontalPosition.CENTER, 0,
//                ConvertUtils.dp2px
//                        (mContext, -10));
    }


    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        RadioButton button = (RadioButton) findViewById(checkedId);
        if (button.getText().equals( "业主注册")) {
            userType = 2;
        }
        if (button.getText().equals("租客注册")) {
            userType = 1;
        }
        mEtInviteCode.setVisibility(checkedId == R.id.rb_renter ? View.VISIBLE : View.GONE);
        mEtBuildingNum.setVisibility(checkedId == R.id.rb_renter ? View.GONE : View.VISIBLE);
        mEtRoomNum.setVisibility(checkedId == R.id.rb_renter ? View.GONE : View.VISIBLE);
        mEtArea.setVisibility(checkedId == R.id.rb_renter ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ConstantUtils.REGISTERED_ACT) {
                mCommunityid = data.getStringExtra(ConstantUtils.COMMUNITY_ID);
                String title = data.getStringExtra(ConstantUtils.COMMUNITY_TITLE);
                mEtArea.setText(title);
                return;
            }
            mCommunityid = data.getStringExtra(ConstantUtils.COMMUNITY_ID);
            String title = data.getStringExtra(ConstantUtils.COMMUNITY_TITLE);
            mEtArea.setText(title);
        }

    }

    @Override
    public void onRightDrawableClick(HighLightEditText highLightEditText) {
        int type = mEtPsw.getInputType();
        if (type==129) {
            mEtPsw.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            type = mEtPsw.getInputType();
        }else {
            mEtPsw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            type = mEtPsw.getInputType();
        }
        LogUtils.d("click_psd: " + type);
    }
}
