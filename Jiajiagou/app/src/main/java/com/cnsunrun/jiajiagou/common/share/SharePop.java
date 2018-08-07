package com.cnsunrun.jiajiagou.common.share;

import android.content.Context;
import android.view.View;

import com.blankj.utilcode.utils.LogUtils;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BasePopupWindow;

import java.util.HashMap;

import butterknife.OnClick;
import cn.jiguang.share.android.api.JShareInterface;
import cn.jiguang.share.android.api.PlatActionListener;
import cn.jiguang.share.android.api.Platform;
import cn.jiguang.share.android.api.ShareParams;
import cn.jiguang.share.qqmodel.QQ;
import cn.jiguang.share.qqmodel.QZone;
import cn.jiguang.share.wechat.Wechat;
import cn.jiguang.share.wechat.WechatFavorite;
import cn.jiguang.share.weibo.SinaWeibo;
import cn.jiguang.share.weibo.SinaWeiboMessage;

/**
 * Created by ${LiuDi}
 * on 2017/9/13on 11:36.
 * 分享
 */

public class SharePop extends BasePopupWindow
{
    String mTitle;
    String mContent;
    String mImageUrl;
    String mUrl;

    public SharePop(Context context)
    {
        super(context, -1, -1);
        this.setAnimationStyle(R.style.umeng_socialize_shareboard_animation);

    }

    public void setShareData(String title, String content, String imageUrl, String url)
    {
        this.mTitle = title;
        this.mContent = content;
        this.mImageUrl = imageUrl;
        this.mUrl = url;
    }

    @Override
    protected int getLayoutRes()
    {
        return R.layout.layout_share_pop;
    }

    @OnClick({R.id.fl_empty, R.id.qq, R.id.qzone, R.id.weixin, R.id.weixin_friend, R.id.weixin_collect, R.id.weibo, R
            .id.weibo_sixin, R.id.tv_cancel})
    public void onViewClicked(View view)
    {
        ShareParams shareParams = new ShareParams();
        shareParams.setShareType(Platform.SHARE_WEBPAGE);
        shareParams.setTitle(mTitle);
        shareParams.setText(mContent);
        shareParams.setImageUrl(mImageUrl);
        shareParams.setUrl(mUrl);
        switch (view.getId())
        {
            case R.id.fl_empty:
                this.dismiss();
                break;
            case R.id.qq:
                LogUtils.d("qq");
                JShareInterface.share(QQ.Name, shareParams, mPlatActionListener);
                break;
            case R.id.qzone:
                JShareInterface.share(QZone.Name, shareParams, mPlatActionListener);
                break;
            case R.id.weixin:
                JShareInterface.share(Wechat.Name, shareParams, mPlatActionListener);
                break;
            case R.id.weixin_friend:
                JShareInterface.share(WechatFavorite.Name, shareParams, mPlatActionListener);
                break;
            case R.id.weixin_collect:
                JShareInterface.share(WechatFavorite.Name, shareParams, mPlatActionListener);
                break;
            case R.id.weibo:

                JShareInterface.share(SinaWeibo.Name, shareParams, mPlatActionListener);

                break;
            case R.id.weibo_sixin:

                JShareInterface.share(SinaWeiboMessage.Name, shareParams, mPlatActionListener);

                break;
            case R.id.tv_cancel:
                this.dismiss();
                break;
        }
    }

    private PlatActionListener mPlatActionListener = new PlatActionListener()
    {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap)
        {
            LogUtils.d("comple");
        }

        @Override
        public void onError(Platform platform, int i, int i1, Throwable throwable)
        {
            LogUtils.d("onerror "+platform.getName()+" ,i "+i+" ,i1+"+i1);
        }

        @Override
        public void onCancel(Platform platform, int i)
        {
            LogUtils.d("onCancel");
        }
    };
}
