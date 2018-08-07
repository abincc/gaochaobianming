package com.cnsunrun.jiajiagou.personal.logistics;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseCallBack;
import com.cnsunrun.jiajiagou.common.base.BaseHeaderActivity;
import com.cnsunrun.jiajiagou.common.base.BaseResp;
import com.cnsunrun.jiajiagou.common.constant.JjgConstant;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.ConstantUtils;
import com.cnsunrun.jiajiagou.common.util.DisplayUtil;
import com.cnsunrun.jiajiagou.personal.CleaningEvent;
import com.google.gson.Gson;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ${LiuDi}
 * on 2017/8/24on 10:25.
 * 物业管理 保洁
 */

public class PropertyDetailActivityV2 extends BaseHeaderActivity {


    @BindView(R.id.banner)
    Banner mBanner;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_status)
    TextView mTvStatus;
    @BindView(R.id.tv_content)
    TextView mTvContent;
    @BindView(R.id.tv_commit_time)
    TextView mTvCommitTime;
    @BindView(R.id.tv_dispose_time)
    TextView mTvDisposeTime;
    @BindView(R.id.tv_user)
    TextView mTvUser;
    @BindView(R.id.tv_building_no)
    TextView mTvBuildingNo;
    @BindView(R.id.tv_room_no)
    TextView mTvRoomNo;
    @BindView(R.id.btn_confirm)
    Button mBtnConfirm;
    @BindView(R.id.tv_reply_content)
    TextView mTvReplyContent;
    @BindView(R.id.ngv_reply)
    NineGridView mNgvReply;
//    private String mTrifle_id;
    private String mClean_id;

    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight) {

        tvTitle.setText("物业管理");

    }

    @Override
    protected void init() {
        mBtnConfirm.setVisibility(View.GONE);
        mClean_id  = getIntent().getStringExtra("clean_id");
        mBanner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                getImageLoader().load(path).into(imageView);
            }
        });

        mNgvReply.setSingleImageSize(DisplayUtil.getScreenWidth(mContext) - 30);
        mNgvReply.setSingleImageRatio(1.4f);
        getData();

    }

    private void getData() {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", JjgConstant.getToken(mContext));
        hashMap.put("clean_id", mClean_id);
        HttpUtils.get(NetConstant.CLEAN_DETAil, hashMap, new BaseCallBack(mContext) {
            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(response);
                CleanDetaiRespV2 resp = new Gson().fromJson(response, CleanDetaiRespV2.class);
                if (handleResponseCode(resp)) {
                    CleanDetaiRespV2.InfoBean info = resp.getInfo();
                    mTvName.setText(info.getClean_service_title());
                    mTvStatus.setText(info.getStatus_title());
                    mTvContent.setText(info.getContent());
                    mBtnConfirm.setVisibility(info.status == 1 ? (JjgConstant.isProperty(mContext) ? View.VISIBLE : View.GONE) : View.GONE);
                    mTvCommitTime.setText("提交时间：" + info.getAdd_time());
                    mTvDisposeTime.setVisibility(info.status==2?View.VISIBLE:View.GONE);
                    mTvDisposeTime.setText("处理时间：" + info.getDeal_time());
                    mTvUser.setText(info.getUsername());
                    mTvBuildingNo.setText(info.getBuilding_no());
                    mTvRoomNo.setText(info.getRoom_no());
                    List<CleanDetaiRespV2.InfoBean.ImagesBean> images = info.getImages();
                    setBanner(images);
                    if (resp.getInfo().getIs_reply().equals( "1")) {//已回复
                        mTvReplyContent.setVisibility(View.VISIBLE);
                        mTvReplyContent.setText("回复内容："+resp.getInfo().getReply());
                    } else {
                        mTvReplyContent.setVisibility(View.GONE);
                    }
                    List<CleanDetaiRespV2.InfoBean.ReplyImagesBean> replyImages = resp.getInfo().getReply_images();
                    if (replyImages != null&&replyImages.size()>0){
                        LogUtils.i("not null ");

                        mNgvReply.setVisibility(View.VISIBLE);
                        List<ImageInfo> urlList = new ArrayList();
                        for (CleanDetaiRespV2.InfoBean.ReplyImagesBean replyImage : replyImages) {
                            ImageInfo info1 = new ImageInfo();
                            info1.setThumbnailUrl(replyImage.getImage());
                            info1.setBigImageUrl(replyImage.getImage());
                            urlList.add(info1);
                        }
                        mNgvReply.setAdapter(new NineGridViewClickAdapter(mContext, urlList));

                    }
                    else {
                        LogUtils.i(" null ");

                        mNgvReply.setVisibility(View.GONE);
                    }
                }

            }
        });

    }

    private void setBanner(List<CleanDetaiRespV2.InfoBean.ImagesBean> images) {
        ArrayList<String> pics = new ArrayList<>();

        for (CleanDetaiRespV2.InfoBean.ImagesBean imageBean : images) {
            pics.add(imageBean.getImage());

        }
        mBanner.update(pics);

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRreshEvent(CleaningEvent event)
    {
        getData();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_property_detail;
    }


    @OnClick(R.id.btn_confirm)
    public void onViewClicked() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", JjgConstant.getToken(mContext));
        hashMap.put("clean_id", mClean_id);
        HttpUtils.post(NetConstant.Clean_DETAIL_PROPERTY, hashMap, new BaseCallBack(mContext) {
            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(response);
                BaseResp baseResp = new Gson().fromJson(response, BaseResp.class);
                if (handleResponseCode(baseResp)) {
                    Bundle bundle = new Bundle();
                    bundle.putString(ConstantUtils.FROM_CLEAN_OR_LITTLE, ConstantUtils.FROM_CLEAN);
                    bundle.putString("id", mClean_id);
                    startActivity(PropertyReplyActivity.class, false, bundle);
//                    showToast(baseResp.getMsg(), 1);
//                    setResult(200);
//                    finish();
                }
            }
        });

    }

}
