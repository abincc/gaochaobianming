package com.cnsunrun.jiajiagou.forum;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseActivity;
import com.cnsunrun.jiajiagou.common.base.BaseCallBack;
import com.cnsunrun.jiajiagou.common.base.BaseResp;
import com.cnsunrun.jiajiagou.common.base.Refreshable;
import com.cnsunrun.jiajiagou.common.constant.SPConstant;
import com.cnsunrun.jiajiagou.common.network.DialogCallBack;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.share.SharePop;
import com.cnsunrun.jiajiagou.common.util.ConstantUtils;
import com.cnsunrun.jiajiagou.common.util.FileUtils;
import com.cnsunrun.jiajiagou.common.util.ImageUtils;
import com.cnsunrun.jiajiagou.common.util.LogUtils;
import com.cnsunrun.jiajiagou.common.util.SPUtils;
import com.cnsunrun.jiajiagou.common.widget.AddPicBean;
import com.cnsunrun.jiajiagou.common.widget.AddPicGridAdapter;
import com.cnsunrun.jiajiagou.common.widget.CircleTransform;
import com.cnsunrun.jiajiagou.forum.adapter.PostsCommentInfoAdapter;
import com.cnsunrun.jiajiagou.forum.adapter.PostsContentImgAdapter;
import com.cnsunrun.jiajiagou.forum.bean.PostsCommentBean;
import com.cnsunrun.jiajiagou.forum.bean.PostsDetailBean;
import com.cnsunrun.jiajiagou.forum.bean.SharePostsBean;
import com.cnsunrun.jiajiagou.forum.bean.TokenBean;
import com.cnsunrun.jiajiagou.login.LoginActivity;
import com.google.gson.Gson;
import com.lzy.ninegrid.ImageInfo;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

import static com.cnsunrun.jiajiagou.R.id.iv_avatar_detail;
import static com.cnsunrun.jiajiagou.R.id.iv_btn_like;
import static com.cnsunrun.jiajiagou.R.id.ll_btn_share;

/**
 * 论坛帖子详情
 * <p>
 * author:yyc
 * date: 2017-08-26 11:31
 */
public class PostsDetailActicity extends BaseActivity implements TextWatcher
{
    @BindView(R.id.scroll_photo)
    NestedScrollView mScrollView;
    @BindView(R.id.recycle_comment_detail)
    RecyclerView mCommentRv; //评论信息
    //    @BindView(R.id.mprv_img)
//    MultiPickResultView mPickResultView;  //选择图片
    @BindView(R.id.recycle_photo_result)
    RecyclerView mPhotoResult;
    @BindView(R.id.tv_send)
    TextView mSendTv;//发送
    @BindView(R.id.ll_btn_like)
    LinearLayout mLikeBtn; //点赞
    @BindView(R.id.iv_like)
    ImageView likeIv;
    @BindView(R.id.tv_like_or_cancel)
    TextView mLikeOrCancelTv; //点赞或者取消
    @BindView(ll_btn_share)
    LinearLayout mShareBtn; //分享
    @BindView(R.id.ll_btn_like_reply)
    LinearLayout mReplyLikeBtn; //回复点赞
    @BindView(R.id.tv_like_or_cancel_reply)
    TextView mReplyLikeOrCancelTv; //回复点赞或者取消
    @BindView(R.id.et_comment)
    EditText mEdComment;//输入评论
    @BindView(R.id.ll_parent_view)
    LinearLayout mParentView;
    @BindView(iv_avatar_detail)
    ImageView mAvatarIv;//用户头像
    @BindView(R.id.tv_name_detail)
    TextView mNameTv;//用户昵称
    @BindView(R.id.tv_plate_detail)
    TextView mPlateTv;//板块名称
    @BindView(R.id.tv_date_detail)
    TextView mDateTv;//帖子日期
    @BindView(R.id.tv_title_detail)
    TextView mTitleTv;//帖子标题
    @BindView(R.id.tv_content_detail)
    TextView mContentTv;//帖子内容
    @BindView(R.id.tv_comment_num_detail)
    TextView mCommentNumTv;//帖子评论数
    @BindView(R.id.tv_read_num_detail)
    TextView mReadNumTv;//帖子阅读数
    @BindView(R.id.tv_like_num_detail)
    TextView mMLikeNumTv;//帖子点赞数
    @BindView(R.id.recycle_content_posts)
    RecyclerView recyclerView;//显示图片
    private SharePop mSharePop;


    //保存将要发送的图片路径
    private ArrayList<String> mImagePathList = new ArrayList<>();
    private String token;

    private PostsCommentInfoAdapter mCommentInfoAdapter;
    private String themeId;
    private String mPostsId;
    private boolean isReply = false;//是否是回复,点击回复时为true;
    private String mReplyId;//被回复人id
    private AddPicGridAdapter mAdapter;
    private HashMap<String, String> mMap;
    private PostsDetailBean.InfoBean mInfo;

    @Override
    protected void init()
    {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams
                .SOFT_INPUT_STATE_HIDDEN);
        initData();
        initView();
        initAdapter();
    }


    private void initView()
    {
        mPhotoResult.setLayoutManager(new GridLayoutManager(mContext, 6));
        mAdapter = new AddPicGridAdapter(R.layout.item_add_pic_posts, null, mPhotoResult, this);
        mPhotoResult.setAdapter(mAdapter);
        mAdapter.setNewData(new ArrayList<AddPicBean>());
        mCommentRv.setNestedScrollingEnabled(false);
        mCommentRv.setLayoutManager(new LinearLayoutManager(this));
        //初始化已选择的图片
//        mPickResultView.init(this, MultiPickResultView.ACTION_SELECT, mImagePathList);
        mEdComment.addTextChangedListener(this);
    }

    private void initAdapter()
    {
        mCommentInfoAdapter = new PostsCommentInfoAdapter(R.layout
                .item_posts_comment_info);
        mCommentInfoAdapter.setImageLoader(getImageLoader());
        mCommentRv.setAdapter(mCommentInfoAdapter);
        mCommentRv.addOnItemTouchListener(new OnItemChildClickListener()
        {
            @Override
            public void onSimpleItemChildClick(final BaseQuickAdapter adapter, View view, final
            int position)
            {
                List<PostsCommentBean.InfoBean.PostListBean> data = adapter.getData();
                String id = data.get(position).getId();
                switch (view.getId())
                {
                    case R.id.iv_btn_reply_comment:
                        LogUtils.d("onSimpleItemChildClick :" + data.get(position).getId());
                        isReply = true;
                        mEdComment.requestFocus();
                        InputMethodManager imm = (InputMethodManager) mEdComment.getContext()
                                .getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(mEdComment, 0);
                        mLikeBtn.setVisibility(View.GONE);
                        mShareBtn.setVisibility(View.GONE);
                        mReplyLikeBtn.setVisibility(View.VISIBLE);
                        mReplyId = data.get(position).getId();
                        break;
                    case iv_btn_like:  //评论点赞
                        if (!checkIsLogin())
                        {
                            return;
                        }
                        HashMap<String, String> map = new HashMap();
                        map.put("token", token);
                        map.put("id", id);
                        map.put("thread_id", themeId);
                        //有header所以position+1
                        final TextView likeNumTv = (TextView) adapter.getViewByPosition
//                                (mCommentRv, position + 1, R.id
        (mCommentRv, position, R.id
                .tv_likenum_comment);
                        final ImageView likeBtn = (ImageView) adapter.getViewByPosition
                                (mCommentRv, position,
                                        iv_btn_like);
                        //当前item  点赞的数字是灰色,点击发送点赞请求
                        if (likeNumTv.getCurrentTextColor() == mContext.getResources().getColor(R
                                .color.gray666))
                        {
                            //评论点赞请求
                            requestCommentLick(adapter, position, map, likeNumTv, likeBtn);
                            LogUtils.printD("点赞");
                        }
                        //当前item  点赞的数字是红色,发送取消点赞请求
                        if (likeNumTv.getCurrentTextColor() == mContext.getResources().getColor(R
                                .color.like_red))
                        {
                            //评论取消点赞请求
                            LogUtils.printD("取消");
                            requestCommentLickCancel(adapter, position, map, likeNumTv, likeBtn);

                        }
                        break;
                    case R.id.iv_avatar_comment:
                    case R.id.tv_name_comment:
                        Bundle bundle = new Bundle();
                        bundle.putString(ConstantUtils.POSTS_USER_ID, data.get(position)
                                .getMember_id());
                        startActivity(PeopleHomepageActivity.class, false, bundle);
                        break;
                }
            }
        });

    }

    //评论取消点赞请求
    private void requestCommentLickCancel(final BaseQuickAdapter adapter, final int position,
                                          HashMap<String, String> map, final TextView likeNumTv,
                                          final ImageView likeBtn)
    {
        HttpUtils.post(NetConstant.COMMENT_LIKE_CANCEL, map, new BaseCallBack
                (mContext)
        {
            @Override
            public void onResponse(String response, int id)
            {
                BaseResp likeBean = new Gson().fromJson(response,
                        BaseResp.class);
                if (handleResponseCode(likeBean))
                {
                    if (likeBean.getStatus() == 1)
                    {
                        likeNumTv.setTextColor(mContext.getResources().getColor(R
                                .color.gray666));
                        likeNumTv.setText(Integer.parseInt(likeNumTv.getText()
                                .toString()) - 1 + "");
                        likeBtn.setImageResource(R.drawable.details_btn_like_nor);
//                        adapter.setda
//                        adapter.notifyItemChanged(position);
                    }
                }
            }
        });
    }

    //评论点赞请求
    private void requestCommentLick(final BaseQuickAdapter adapter, final int position,
                                    HashMap<String, String> map, final TextView likeNumTv, final
                                    ImageView likeBtn)
    {
        HttpUtils.post(NetConstant.COMMENT_LIKE, map, new BaseCallBack
                (mContext)
        {
            @Override
            public void onResponse(String response, int id)
            {
                BaseResp likeBean = new Gson().fromJson(response,
                        BaseResp.class);
                if (handleResponseCode(likeBean))
                {
                    if (likeBean.getStatus() == 1)
                    {
                        likeNumTv.setTextColor(mContext.getResources().getColor(R
                                .color.like_red));
                        String s = likeNumTv.getText().toString();
                        LogUtils.printD("num " + s);
                        likeNumTv.setText(Integer.parseInt(likeNumTv.getText()
                                .toString()) + 1 + "");
                        String s1 = likeNumTv.getText().toString();
                        LogUtils.printD("num1 " + s1);
                        likeBtn.setImageResource(R.drawable.details_btn_like_sel);
//                        adapter.notifyItemChanged(position);
                    }
                }
            }
        });
    }


    private void initData()
    {
        requestNet();
        String url1 = "http://img.ivsky.com/img/tupian/pre/201407/28/chaka_yanhu.jpg";
        List<String> topList = new ArrayList<>();
        topList.add(url1);
//        topList.add(url2);
//        topList.add(url3);
    }

    //回复他人
    private void replyToOthers()
    {
        String content = mEdComment.getText().toString();
        mImagePathList.clear();
//        ArrayList<String> photos = mPickResultView.getPhotos();
//        mImagePathList.addAll(photos);
        List<AddPicBean> data = mAdapter.getData();
        for (AddPicBean bean : data)
        {
            if (bean.type != bean.TYPE_ADD)
            {
                mImagePathList.add(bean.picPath);
            }
        }
        Map<String, File> fileMap = new HashMap<>();
        if (mImagePathList.size() > 0)
        {
            LogUtils.printD("图片集合: " + mImagePathList.size());
            for (String path : mImagePathList)
            {
                File file;
                final double size = FileUtils.getFileOrFilesSize(path, FileUtils
                        .SIZETYPE_KB);
                LogUtils.printD("压缩前图片大小:" + size);
                if (size > 500)
                {
                    //如果图片大小大于500K,就处理图片大小(系统相机拍的大图)
                    file = new File(ImageUtils.zoomBitmap2File(path));
                } else
                {
                    file = new File(path);
                }
                LogUtils.printD("压缩后图片大小:" + FileUtils.getFileOrFilesSize(file.getAbsolutePath(),
                        FileUtils.SIZETYPE_KB));
                fileMap.put(file.getName(), file);
            }
        }
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("token", token);
        paramsMap.put("thread_id", themeId);
        paramsMap.put("id", mReplyId);
        paramsMap.put("content", content);
        HttpUtils.postForm(NetConstant.REPLY, paramsMap, "file[]", fileMap, new BaseCallBack
                (mContext)
        {
            @Override
            public void onResponse(String response, int id)
            {
                BaseResp requestBean = new Gson().fromJson(response, BaseResp
                        .class);
                if (handleResponseCode(requestBean))
                {

                    if (requestBean.getStatus() == 1)
                    {
                        isReply = false;
                        mEdComment.setText("");
                    }
                }
            }
        });
    }

    //评论主题
    private void commentTheme()
    {
        HashMap<String, String> paramsMap = new HashMap();
        String content = mEdComment.getText().toString();
        mImagePathList.clear();
//        ArrayList<String> photos = mPickResultView.getPhotos();
//        mImagePathList.addAll(photos);
        List<AddPicBean> data = mAdapter.getData();
        for (AddPicBean bean : data)
        {
            if (bean.type != bean.TYPE_ADD)
            {
                mImagePathList.add(bean.picPath);
            }
        }
        paramsMap.put("token", token);
        paramsMap.put("id", themeId);//主题id
        paramsMap.put("content", content);//评论内容
//        map.put("file[]","");//评论内容

        if (TextUtils.isEmpty(content))
        {
            showToast("请输入评论内容");
            return;
        }
//        if (content.length() < 10) {
//            showToast("评论不能少于10字");
//            return;
//        }
        Map<String, File> fileMap = new HashMap<>();
        if (mImagePathList.size() > 0)
        {
            LogUtils.printD("图片集合: " + mImagePathList.size());
            for (String path : mImagePathList)
            {
                File file;
                final double size = FileUtils.getFileOrFilesSize(path, FileUtils
                        .SIZETYPE_KB);
                LogUtils.printD("压缩前图片大小:" + size);
                if (size > 500)
                {
                    //如果图片大小大于500K,就处理图片大小(系统相机拍的大图)
                    file = new File(ImageUtils.zoomBitmap2File(path));
                } else
                {
                    file = new File(path);
                }
                LogUtils.printD("压缩后图片大小:" + FileUtils.getFileOrFilesSize(file.getAbsolutePath(),
                        FileUtils.SIZETYPE_KB));
                fileMap.put(file.getName(), file);
            }
        }
        OkHttpUtils.post()
                .url(NetConstant.BASE_URL + NetConstant.COMMENT_THEME)
                .files("file[]", fileMap)
                .params(paramsMap)
                .build()
                .execute(new DialogCallBack(mContext)
                {
                    @Override
                    public void onResponse(String response, int id)
                    {
                        BaseResp requestBean = new Gson().fromJson(response,
                                BaseResp.class);
                        if (handleResponseCode(requestBean))
                        {
                            if (requestBean.getStatus() == 1)
                            {
                                mEdComment.setText("");
                                mImagePathList.clear();
                                requestCommentInfo(mMap);//评论成功刷新评论数据
                            }
                        }

                    }
                });
    }

    @Override
    public void onRefresh()
    {
        super.onRefresh();
        requestNet();
    }

    private void requestNet()
    {
        token = SPUtils.getString(mContext, SPConstant.TOKEN);
        Bundle bundle = getIntent().getExtras();
        mPostsId = bundle.getString(ConstantUtils.POSTS_ITEM_ID);
        mMap = new HashMap<>();
        mMap.put("id", mPostsId);
        mMap.put("token", token);
        //主题信息
        HttpUtils.get(NetConstant.THEMES_DETAIL, mMap, new DialogCallBack((Refreshable)
                PostsDetailActicity.this)
        {
            @Override
            public void onResponse(String response, int id)
            {
                PostsDetailBean detailBean = new Gson().fromJson(response, PostsDetailBean
                        .class);
                if (handleResponseCode(detailBean))
                {
                    if (detailBean.getStatus() == 1)
                    {
                        mInfo = detailBean.getInfo();
                        getPostsInfo(mInfo);
//                        mCommentInfoAdapter.setHeaderView(getHeaderView(info));
                        mLikeOrCancelTv.setTextColor(mContext.getResources().getColor(R.color
                                .gray666));
                        mLikeOrCancelTv.setTextSize(11);
                        if (mInfo.getIs_like().equals("0"))
                        {//未点赞
                            mLikeOrCancelTv.setText("点赞");
                            mLikeOrCancelTv.setTextColor(mContext.getResources().getColor(R.color.gray666));
                            likeIv.setImageResource(R.drawable.details_btn_good_nor);
                        } else
                        {
                            mLikeOrCancelTv.setText("取消");
                            mLikeOrCancelTv.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                            likeIv.setImageResource(R.drawable.details_btn_good_sel);

                        }
                        themeId = mInfo.getId();
                        requestCommentInfo(mMap);
                    }
                }

            }
        });

    }

    //评论信息接口
    private void requestCommentInfo(HashMap<String, String> map)
    {
        HttpUtils.get(NetConstant.THEMES_COMMENT, map, new BaseCallBack(mContext)
        {

            @Override
            public void onResponse(String response, int id)
            {
                PostsCommentBean postsCommentBean = new Gson().fromJson(response,
                        PostsCommentBean.class);

                if (postsCommentBean.getStatus() == 1)
                {
                    List<PostsCommentBean.InfoBean.PostListBean> postListBeen = postsCommentBean
                            .getInfo().getPost_list();
                    mCommentInfoAdapter.setNewData(postListBeen);
                    if (postListBeen.size() == 0)
                    {
                        mCommentInfoAdapter.setEmptyView(getLayoutInflater().inflate(R
                                .layout.layout_empty_fixed_height, (ViewGroup) mCommentRv.getParent(), false));
                    }
                }
            }
        });
    }

    @OnClick({R.id.ll_photo_detail, R.id.tv_send, R.id.ll_btn_like, R.id.iv_back_plate_detail, R
            .id.ll_btn_share, iv_avatar_detail, R.id.tv_name_detail})
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.iv_back_plate_detail:
                onBackPressedSupport();
                break;
            case R.id.ll_photo_detail:
                if (mScrollView.isShown())
                {
                    mScrollView.setVisibility(View.GONE);
                    return;
                }
                mScrollView.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_send:
                if (!checkIsLogin())
                {
                    return;
                }
                if (isReply)
                {
                    replyToOthers();
                } else
                {
                    commentTheme();
                }
                break;
            case R.id.ll_btn_like://主题点赞
                if (!checkIsLogin())
                {
                    return;
                }

                String text = mLikeOrCancelTv.getText().toString();
                if (text.equals("点赞"))
                {
                    requestThemeLike();

                }
                if (text.equals("取消"))
                {
                    requestThemeLikecancel();
                }
                break;
            case R.id.ll_btn_share:
//                if (true)
//                {
//                    return;
//                }
                sendShare(v);


                break;
            case R.id.iv_avatar_detail:
            case R.id.tv_name_detail:
                Bundle bundle = new Bundle();
                bundle.putString(ConstantUtils.POSTS_USER_ID, mInfo.getMember_id());
                startActivity(PeopleHomepageActivity.class, false, bundle);
                break;
        }
    }

    private void sendShare(final View v) {
        if (mSharePop == null)
            mSharePop = new SharePop(mContext);
        HashMap map=new HashMap();
        map.put("id",mPostsId);
        HttpUtils.get(NetConstant.SHARE_POSTS, map, new DialogCallBack((Refreshable) this) {
            @Override
            public void onResponse(String response, int id) {
                SharePostsBean bean=new Gson().fromJson(response,SharePostsBean.class);
                if (bean.getStatus()==1) {
                    SharePostsBean.InfoBean info = bean.getInfo();
                    mSharePop.setShareData(info.getTitle(),info.getContent(),info.getImage(),info.getUrl());
                    mSharePop.showAtLocation(v, Gravity.BOTTOM, 0, 0);
                }
            }
        });

    }

    private void requestThemeLike()
    {
        token = SPUtils.getString(mContext, SPConstant.TOKEN);
        HashMap<String, String> map = new HashMap();
        map.put("id", themeId);
        map.put("token", token);
        HttpUtils.post(NetConstant.THEME_LIKE, map, new BaseCallBack(mContext)
        {
            @Override
            public void onResponse(String response, int id)
            {
                BaseResp themeLikeBean = new Gson().fromJson(response,
                        BaseResp.class);
                if (handleResponseCode(themeLikeBean))
                {

                    if (themeLikeBean.getStatus() == 1)
                    {
                        LinearLayout headerLayout = mCommentInfoAdapter.getHeaderLayout();
                        mMLikeNumTv.setText(Integer.parseInt(mMLikeNumTv.getText().toString()) + 1 + "");
//                                mCommentInfoAdapter.notifyItemChanged(0);
                        mLikeOrCancelTv.setText("取消");
                        likeIv.setImageResource(R.drawable.details_btn_good_sel);
                        mLikeOrCancelTv.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                        likeIv.setImageResource(R.drawable.details_btn_good_sel);
                    }
                }

            }
        });
    }

    //token失效重新请求
    public void reRequestNet()
    {
        String ticket = SPUtils.getString(mContext, SPConstant.TICKET);
        HttpUtils.getToken(ticket, new StringCallback()
        {
            @Override
            public void onError(Call call, Exception e, int id)
            {

            }

            @Override
            public void onResponse(String response, int id)
            {
                TokenBean tokenBean = new Gson().fromJson(response, TokenBean.class);

                if (tokenBean.getStatus() == 1)
                {
                    SPUtils.put(mContext, SPConstant.TOKEN, tokenBean.getInfo().getToken());
                    requestNet();
                }
            }
        });
    }

    private void requestThemeLikecancel()
    {
        token = SPUtils.getString(mContext, SPConstant.TOKEN);
        HashMap<String, String> map = new HashMap();
        map.put("id", themeId);
        map.put("token", token);
        HttpUtils.post(NetConstant.THEME_LIKE_CANCEL, map, new BaseCallBack(mContext)
        {
            @Override
            public void onResponse(String response, int id)
            {
                BaseResp themeLikeBean = new Gson().fromJson(response,
                        BaseResp
                                .class);
                if (handleResponseCode(themeLikeBean))
                {

                    if (themeLikeBean.getStatus() == 1)
                    {
                        mLikeOrCancelTv.setText("点赞");
                        likeIv.setImageResource(R.drawable.details_btn_good_nor);
                        mMLikeNumTv.setText(Integer.parseInt(mMLikeNumTv.getText().toString()) - 1 + "");
                        mLikeOrCancelTv.setTextColor(mContext.getResources().getColor(R
                                .color.gray666));
                    }
                }

            }
        });
    }


    private boolean checkIsLogin()
    {
        token = SPUtils.getString(mContext, SPConstant.TOKEN);
        if (TextUtils.isEmpty(token))
        {
//            startActivity(LoginActivity.class, true);
            Intent intent = new Intent(this, LoginActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(ConstantUtils.RELOGIN, ConstantUtils.RELOGIN_CODE);
            intent.putExtras(bundle);
            startActivityForResult(intent, ConstantUtils.RELOGIN_CODE);
            return false;
        }
        return true;
    }


    private void getPostsInfo(PostsDetailBean.InfoBean info)
    {

//        View view = View.inflate(this, R.layout.header_posts_detail, null);
//        ImageView mAvatarIv = (ImageView) view.findViewById(R.id.iv_avatar_detail);//用户头像
//        TextView mNameTv = (TextView) view.findViewById(R.id.tv_name_detail);//用户头像
//        TextView mPlateTv = (TextView) view.findViewById(R.id.tv_plate_detail);//板块名称
//        TextView mDateTv = (TextView) view.findViewById(R.id.tv_date_detail);//帖子日期
//        TextView mTitleTv = (TextView) view.findViewById(R.id.tv_title_detail);//帖子标题
//        TextView mContentTv = (TextView) view.findViewById(R.id.tv_content_detail);//帖子内容
//        TextView mCommentNumTv = (TextView) view.findViewById(R.id.tv_comment_num_detail);//帖子评论数
//        TextView mReadNumTv = (TextView) view.findViewById(R.id.tv_read_num_detail);//帖子阅读数
//        //帖子点赞数
//        TextView mMLikeNumTv = (TextView) view.findViewById(R.id.tv_like_num_detail);
//        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycle_content_posts);
        //显示图片
        if (info != null)
        {
            getImageLoader().load(info.getAvatar()).transform(new CenterCrop(mContext),
                    new CircleTransform(mContext))
                    .error(R.drawable.nav_btn_personal_nor)
                    .into(mAvatarIv);
            mNameTv.setText(info.getMember_nickname());
            mPlateTv.setText(info.getForum_title());
            mDateTv.setText(info.getLastpost_time());
            mTitleTv.setText(info.getTitle());
            mContentTv.setText(info.getContent());
            mCommentNumTv.setText(info.getReplies());
            mReadNumTv.setText(info.getViews());
            mMLikeNumTv.setText(info.getLikes());

            List<ImageInfo> mContentImgs = new ArrayList();
            List<PostsDetailBean.InfoBean.ImageListBean> imageList = info.getImage_list();
            if (imageList.size() > 0)
            {
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setLayoutManager(new GridLayoutManager(mContext, 1));
                recyclerView.setNestedScrollingEnabled(false);
                PostsContentImgAdapter postsContentImgAdapter = new PostsContentImgAdapter(R.layout
                        .item_tab_content_plate_img, imageList);
                postsContentImgAdapter.setImageLoader(getImageLoader());
                recyclerView.setAdapter(postsContentImgAdapter);
//            for (PostsDetailBean.InfoBean.ImageListBean bean : imageList) {
//                ImageInfo imageInfo = new ImageInfo();
//                imageInfo.setBigImageUrl(bean.getImage());
//                imageInfo.setThumbnailUrl(bean.getImage());
//                mContentImgs.add(imageInfo);
//            }
//            DisplayMetrics dm = new DisplayMetrics();
//            this.getWindowManager().getDefaultDisplay().getMetrics(dm);
//            int width = dm.widthPixels;
//
//            nineGv.setSingleImageSize(width);
//            nineGv.setAdapter(new NineGridViewClickAdapter(this, mContentImgs));

            } else
            {
                recyclerView.setVisibility(View.GONE);
            }

        }
//        return view;
    }

    @Override
    protected int getLayoutRes()
    {
        return R.layout.activity_posts_detail;
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode==RESULT_OK) {
////            token = data.getStringExtra(ConstantUtils.TOKEN);
//           reRequestNet();
//        }
//    }


    //点击其他地方隐藏输入法
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {

        if (ev.getAction() == MotionEvent.ACTION_DOWN)
        {
            View v = getCurrentFocus();//当前获取焦点的view
            if (isShouldHideInput(v, ev))
            {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context
                        .INPUT_METHOD_SERVICE);
                if (imm != null)
                {
                    String content = mEdComment.getText().toString();
                    if (TextUtils.isEmpty(content))
                    {

                    } else
                    {
                        mShareBtn.setVisibility(View.GONE);
                        mLikeBtn.setVisibility(View.GONE);

                    }
                    if (mSendTv.isShown())
                    {
//                            mSendTv.setVisibility(View.GONE);

                    } else if (isReply)
                    {
                        mShareBtn.setVisibility(View.GONE);
                        mLikeBtn.setVisibility(View.GONE);
                        mReplyLikeBtn.setVisibility(View.VISIBLE);
                    } else
                    {
                        mShareBtn.setVisibility(View.VISIBLE);
                    }
                    imm.hideSoftInputFromWindow(mEdComment.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    mParentView.requestFocus();

                }
            }
            return super.dispatchTouchEvent(ev);
        } // 必不可少，否则所有的组件都不会有TouchEvent了
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
            int[] leftTop = {0,
                    0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY()
                    < bottom)
            {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        if (s.length() == 0 && isReply != true)
        {
            mSendTv.setVisibility(View.GONE);
            mReplyLikeBtn.setVisibility(View.GONE);//不是回复时讲回复按钮隐藏
            mLikeBtn.setVisibility(View.VISIBLE);
            mShareBtn.setVisibility(View.VISIBLE);
        } else if (s.length() == 0 && isReply == true)
        {//点击了回复,回复按钮显示,其他隐藏
            mReplyLikeBtn.setVisibility(View.VISIBLE);
            mSendTv.setVisibility(View.GONE);
            mLikeBtn.setVisibility(View.GONE);
            mShareBtn.setVisibility(View.GONE);
        } else
        {
            mSendTv.setVisibility(View.VISIBLE);
            mLikeBtn.setVisibility(View.GONE);
            mShareBtn.setVisibility(View.GONE);
            mReplyLikeBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void afterTextChanged(Editable s)
    {

    }
}
