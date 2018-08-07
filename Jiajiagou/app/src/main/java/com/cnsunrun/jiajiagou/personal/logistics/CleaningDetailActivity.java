package com.cnsunrun.jiajiagou.personal.logistics;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseCallBack;
import com.cnsunrun.jiajiagou.common.base.BaseHeaderActivity;
import com.cnsunrun.jiajiagou.common.base.BaseResp;
import com.cnsunrun.jiajiagou.common.base.TwoButtonDialog;
import com.cnsunrun.jiajiagou.common.constant.JjgConstant;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.google.gson.Gson;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ${LiuDi}
 * on 2017/8/24on 10:25.
 */

public class CleaningDetailActivity extends BaseHeaderActivity {


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
    @BindView(R.id.tv_reply_content)
    TextView tvReplyContent;
    @BindView(R.id.ngv_reply)
    NineGridView ngvReply;
    private String mClean_id;

    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight) {

        tvTitle.setText("保洁维修");

    }

    @Override
    protected void init() {
        mClean_id = getIntent().getStringExtra("clean_id");
        mBanner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                getImageLoader().load(path).into(imageView);
            }
        });

        getData();

    }

    private void getData() {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", JjgConstant.getToken(mContext));
        hashMap.put("clean_id", mClean_id);
        HttpUtils.get(NetConstant.CLEAN_INFO, hashMap, new BaseCallBack(mContext) {
            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(response);
                CleanDetaiResp resp = new Gson().fromJson(response, CleanDetaiResp.class);
                if (handleResponseCode(resp)) {
                    CleanDetaiResp.InfoBean info = resp.getInfo();
                    mTvName.setText(info.getClean_service_title());
                    mTvStatus.setText(info.getStatus_title());
                    mTvContent.setText(info.getContent());
                    mTvCommitTime.setText("提交时间：" + info.getAdd_time());
                    mTvDisposeTime.setText("处理时间：" + info.getDeal_time());

                    List<CleanDetaiResp.InfoBean.ImagesBean> images = info.getImages();
                    setBanner(images);
                    if (resp.getInfo().getIs_reply().equals( "1")) {//已回复
                        tvReplyContent.setVisibility(View.VISIBLE);
                        tvReplyContent.setText("回复内容："+resp.getInfo().getReply());
                    } else {
                        tvReplyContent.setVisibility(View.GONE);
                    }
                    List<CleanDetaiResp.InfoBean.ReplyImagesBean> replyImages = resp.getInfo().getReply_images();
                    if (replyImages != null&&replyImages.size()>0){
                        LogUtils.i("not null ");

                        ngvReply.setVisibility(View.VISIBLE);
                        List<ImageInfo> urlList = new ArrayList();
                        for (CleanDetaiResp.InfoBean.ReplyImagesBean replyImage : replyImages) {
                            ImageInfo info1 = new ImageInfo();
                            info1.setThumbnailUrl(replyImage.getImage());
                            info1.setBigImageUrl(replyImage.getImage());
                            urlList.add(info1);
                        }
                        ngvReply.setAdapter(new NineGridViewClickAdapter(mContext, urlList));

                    }

                }

            }
        });

    }

    private void setBanner(List<CleanDetaiResp.InfoBean.ImagesBean> images) {
        ArrayList<String> pics = new ArrayList<>();

        for (CleanDetaiResp.InfoBean.ImagesBean imageBean : images) {
            pics.add(imageBean.getImage());

        }
        mBanner.update(pics);

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_little_help_detail;
    }


    @OnClick(R.id.btn_confirm)
    public void onViewClicked() {
        final TwoButtonDialog dialog = new TwoButtonDialog();
        dialog.setContent("确定执行该操作吗?");
        dialog.setOnBtnConfirmClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("token", JjgConstant.getToken(mContext));
                hashMap.put("clean_id", mClean_id);
                HttpUtils.post(NetConstant.CLEAN_CANCLE, hashMap, new BaseCallBack(mContext) {
                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.d(response);
                        BaseResp baseResp = new Gson().fromJson(response, BaseResp.class);
                        if (handleResponseCode(baseResp)) {
                            //toast消息为详情信息不存在
//                    showToast(baseResp.getMsg());
                            setResult(200);
                            dialog.dismiss();
                            finish();
                        }
                    }
                });
            }
        });
        dialog.show(getSupportFragmentManager(), null);


    }

}
