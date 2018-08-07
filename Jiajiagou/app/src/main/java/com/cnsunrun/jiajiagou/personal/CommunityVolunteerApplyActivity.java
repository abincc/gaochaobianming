package com.cnsunrun.jiajiagou.personal;

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
import com.cnsunrun.jiajiagou.common.util.OtherUtils;
import com.cnsunrun.jiajiagou.common.util.PickerViewUtils;
import com.cnsunrun.jiajiagou.common.util.SPUtils;
import com.cnsunrun.jiajiagou.common.widget.view.HighLightEditText;
import com.google.gson.Gson;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 社区志愿者
 * <p>
 * author:yyc
 * date: 2017-11-23 10:18
 */
public class CommunityVolunteerApplyActivity extends BaseHeaderActivity {

    //专长
    @BindView(R.id.et_expertise)
    EditText mExpertise;
    //工作意向
    @BindView(R.id.et_work_intention)
    EditText mWorkIntention;
    //工作开始时间
    @BindView(R.id.et_start_time)
    HighLightEditText mStartTime;
    //工作结束时间
    @BindView(R.id.et_end_time)
    HighLightEditText mEndTime;

    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight) {
        tvTitle.setText("社区志愿者申请");
        tvRight.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void init() {
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN|WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mStartTime.setOnRightDrawableClickListener(new HighLightEditText.OnRightDrawableClickListener() {


            @Override
            public void onRightDrawableClick(HighLightEditText highLightEditText) {
                PickerViewUtils.show(mContext,highLightEditText);
            }
        });
        mEndTime.setOnRightDrawableClickListener(new HighLightEditText.OnRightDrawableClickListener() {
            @Override
            public void onRightDrawableClick(HighLightEditText highLightEditText) {
                PickerViewUtils.show(mContext,highLightEditText);
            }
        });
    }

    @OnClick(R.id.btn_commit_apply)
    public void onViewClick(View v){
        switch (v.getId()){
            case R.id.btn_commit_apply:
                if (OtherUtils.isFastClick()) {
                    commitApply();
                }
                break;
        }
    }

    private void commitApply(){
        String token = SPUtils.getString(this, SPConstant.TOKEN);
        String expertise = mExpertise.getText().toString();
        String workIntention = mWorkIntention.getText().toString();
        String startDate = mStartTime.getText().toString();
        String endDate = mEndTime.getText().toString();
        if (TextUtils.isEmpty(expertise)) {
            showToast("请输入专长");
            return;
        }
        if (TextUtils.isEmpty(workIntention)) {
            showToast("请输入工作意向");
            return;
        }
        if (TextUtils.isEmpty(startDate)) {
            showToast("请选择开始时间");
            return;
        }
        if (TextUtils.isEmpty(endDate)) {
            showToast("请选择结束时间");
            return;
        }
        HashMap<String,String> map=new HashMap();
        map.put("token",token);
        map.put("skill",expertise);
        map.put("intention",workIntention);
        map.put("start_date",startDate);
        map.put("end_date",endDate);
        HttpUtils.post(NetConstant.APPLY_VOLUNTEER, map, new DialogCallBack(mContext) {
            @Override
            public void onResponse(String response, int id) {
                BaseResp postRequestBean = new Gson().fromJson(response, BaseResp
                        .class);
                if (handleResponseCode(postRequestBean)) {
                    if (postRequestBean.getStatus() == 1) {
                        showToast(postRequestBean.getMsg(), 1);
                        OtherUtils.delayFinishActivity(CommunityVolunteerApplyActivity.this, 1000);
                    }
                }
            }
        });
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_volunteer_apply;
    }
}
