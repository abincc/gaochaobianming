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

public class LittleHelpDetailActivity extends BaseHeaderActivity {
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
    private String mTrifle_id;

    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight) {

        tvTitle.setText("小事帮忙");
    }

    @Override
    protected void init() {
        mTrifle_id = getIntent().getStringExtra("trifle_id");
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
        hashMap.put("trifle_id", mTrifle_id);
        HttpUtils.get(NetConstant.TRIFLE_DETAIL, hashMap, new BaseCallBack(mContext) {
            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(response);
                HelpDetaiResp resp = new Gson().fromJson(response, HelpDetaiResp.class);
                if (handleResponseCode(resp)) {
                    HelpDetaiResp.InfoBean info = resp.getInfo();
                    mTvName.setText(info.getTrifle_service_title());
                    mTvStatus.setText(info.getStatus_title());
                    mTvContent.setText(info.getContent());
                    mTvCommitTime.setText("提交时间：" + info.getAdd_time());
                    mTvDisposeTime.setText("处理时间：" + info.getDeal_time());

                    List<HelpDetaiResp.InfoBean.ImagesBean> images = info.getImages();
                    setBanner(images);

                    if (resp.getInfo().getIs_reply().equals( "1")) {//已回复
                        tvReplyContent.setVisibility(View.VISIBLE);
                        tvReplyContent.setText("回复内容："+resp.getInfo().getReply());
                    } else {
                        tvReplyContent.setVisibility(View.GONE);
                    }
                    List<HelpDetaiResp.InfoBean.ReplyImagesBean> replyImages = resp.getInfo().getReply_images();
                    if (replyImages != null&&replyImages.size()>0){
                        LogUtils.i("not null ");

                        ngvReply.setVisibility(View.VISIBLE);
                        List<ImageInfo> urlList = new ArrayList();
                        for (HelpDetaiResp.InfoBean.ReplyImagesBean replyImage : replyImages) {
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

    private void setBanner(List<HelpDetaiResp.InfoBean.ImagesBean> images) {
        ArrayList<String> pics = new ArrayList<>();

        for (HelpDetaiResp.InfoBean.ImagesBean imageBean : images) {
            pics.add(imageBean.getImage());

        }
        mBanner.update(pics);
    }


    @OnClick(R.id.btn_confirm)
    public void onViewClicked() {
        final TwoButtonDialog dialog = new TwoButtonDialog();
        dialog.setContent("确定执行该操作吗？");
        dialog.setOnBtnConfirmClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("token", JjgConstant.getToken(mContext));
                hashMap.put("trifle_id", mTrifle_id);
                HttpUtils.post(NetConstant.TRIFLE_CANCEL, hashMap, new BaseCallBack(mContext) {
                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.d(response);
                        BaseResp baseResp = new Gson().fromJson(response, BaseResp.class);
                        if (handleResponseCode(baseResp)) {
                            showToast(baseResp.getMsg(), 1);
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

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_little_help_detail;
    }

}
