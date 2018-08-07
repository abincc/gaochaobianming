package com.cnsunrun.jiajiagou.forum;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseActivity;
import com.cnsunrun.jiajiagou.common.base.Refreshable;
import com.cnsunrun.jiajiagou.common.network.DialogCallBack;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.ConstantUtils;
import com.cnsunrun.jiajiagou.common.util.DateUtils;
import com.cnsunrun.jiajiagou.common.widget.GridSpacingItemDecoration;
import com.cnsunrun.jiajiagou.forum.adapter.PlateTypeAdapter;
import com.cnsunrun.jiajiagou.forum.adapter.RecommendPostsAdapter;
import com.cnsunrun.jiajiagou.forum.bean.ForumHomepageBean;
import com.cnsunrun.jiajiagou.forum.entity.PlateItem;
import com.cnsunrun.jiajiagou.forum.entity.RecommendPostsItem;
import com.cnsunrun.jiajiagou.personal.information.InformationActivity;
import com.cnsunrun.jiajiagou.product.MyItemDecoration;
import com.google.gson.Gson;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description: 论坛主页
 * Data：2017/8/18 0018-上午 11:58
 * Blog：http://blog.csdn.net/u013983934
 * Author: CZ
 */
public class ForumHomepageActivity extends BaseActivity {

    @BindView(R.id.recycler_forum_plate)
    RecyclerView recyclerPlate;  //板块
    @BindView(R.id.recycler_forum_posts)
    RecyclerView recyclerPosts;  //推荐帖子
    @BindView(R.id.banner_forum)
    Banner forumBanner;
    private List<PlateItem> plateDatas;
    List<ForumHomepageBean.InfoBean.AdBean> mBannerBeanList;


    private String url = "https://i.imgur.com/YxjfGotb.jpg ";
    private String contentUrl = "https://i.imgur.com/0c1KlhO.jpg ";
    private List<RecommendPostsItem> posttsDatas;
    private PlateTypeAdapter mPlateTypeAdapter;
    private RecommendPostsAdapter mPostsAdapter;

    @Override
    protected void init() {

        initData();
        initView();
        initAdapter();
//        DialogTask.getInstance().setMessage("测试").setImageResource(R.drawable.message_error).start
//                (getSupportFragmentManager());
//        DialogTask.getInstance().stop();
    }

    @OnClick({R.id.et_search, R.id.iv_back, R.id.tv_plate_menu, R.id.ll_btn_send_posts, R.id
            .iv_news})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_search:
                startActivity(new Intent(mContext, ForumSearchActivity.class));
                break;
            case R.id.tv_plate_menu:
                Bundle bundle = new Bundle();
                bundle.putString(ConstantUtils.FROM_CLAZZ, "ForumHomepageActivity");
                startActivity(ForumMenuActivity.class, false, bundle);
                break;
            case R.id.ll_btn_send_posts:
                startActivity(SendPostsActivity.class);
                break;
            case R.id.iv_back:
                onBackPressedSupport();
                break;
            case R.id.iv_news:
                startActivity(InformationActivity.class);
                break;
        }
    }

    private void initData() {
        requestNet();
//        testData();
    }

    private void requestNet() {
//        HttpUtils.get(NetConstant.FORUM_HOMEPAGE, null, new StringCallback() {
//            @Override
//            public void onError(Call call, Exception e, int id) {
//
//            }
//
//            @Override
//            public void onResponse(String response, int id) {
//                ForumHomepageBean homepageBean = new Gson().fromJson(response,
//                        ForumHomepageBean.class);
//                if (homepageBean.getStatus() == 1) {
//                    List<ForumHomepageBean.InfoBean.ForumBean> mPalteDatas = homepageBean
// .getInfo()
//                            .getForum();//板块信息
//                    List<ForumHomepageBean.InfoBean.AdBean> mBannerDatas = homepageBean.getInfo()
//                            .getAd();//banner
//                    List<ForumHomepageBean.InfoBean.ListBean> mRecommendedPostDatas =
//                            homepageBean.getInfo()
//                                    .getList();//推荐帖子
//                    mPlateTypeAdapter.setNewData(mPalteDatas);
//                    mPostsAdapter.setNewData(mRecommendedPostDatas);
//                    fillbanner(mBannerDatas);
//                }
//            }
//        });
        HttpUtils.get(NetConstant.FORUM_HOMEPAGE, null, new DialogCallBack((Refreshable)
                ForumHomepageActivity.this) {
            @Override
            public void onResponse(String response, int id) {
                ForumHomepageBean homepageBean = new Gson().fromJson(response,
                        ForumHomepageBean.class);
                if (handleResponseCode(homepageBean)) {
                    if (homepageBean.getStatus() == 1) {
                        List<ForumHomepageBean.InfoBean.ForumBean> mPalteDatas = homepageBean
                                .getInfo()
                                .getForum();//板块信息
                        List<ForumHomepageBean.InfoBean.AdBean> mBannerDatas = homepageBean
                                .getInfo()
                                .getAd();//banner
                        List<ForumHomepageBean.InfoBean.ListBean> mRecommendedPostDatas =
                                homepageBean.getInfo()
                                        .getList();//推荐帖子
                        mPlateTypeAdapter.setNewData(mPalteDatas);
                        mPostsAdapter.setNewData(mRecommendedPostDatas);
                        if (mRecommendedPostDatas.size()==0) {
                            mPostsAdapter.setEmptyView(View.inflate(mContext, R.layout.layout_empty,
                                    null));
                        }
                        fillbanner(mBannerDatas);
                    }
                }
            }
        });
    }

    private void testData() {
        plateDatas = new ArrayList();
        for (int i = 0; i < 4; i++) {
            PlateItem item = new PlateItem();
            item.setTitle("模块名称");
            item.setImageUrl(url);
            plateDatas.add(item);
        }
        posttsDatas = new ArrayList();
        String content = "测试测试测试测试测试测试测试测试测试测试测试测试";
        for (int i = 1; i < 6; i++) {
            RecommendPostsItem item = new RecommendPostsItem();
            item.setAvatarImg(url);
            item.setName("陌上出黛" + i);
            item.setTitle(i + "个小心机,让宝宝爱上安全座椅");
            item.setContent(content + content + content + content);
            item.setContentImg(contentUrl);
            item.setReadNum(100 + i);
            item.setCommentNum(20 + i);
            item.setLikeNum(40 + i);
            item.setDate(DateUtils.getCurrentTime("yyyy-MM-dd"));
            posttsDatas.add(item);
        }
    }

    private void initAdapter() {
        mPlateTypeAdapter = new PlateTypeAdapter(R.layout.item_plate);
        mPlateTypeAdapter.setImageLoader(getImageLoader());
        recyclerPlate.setAdapter(mPlateTypeAdapter);
        mPostsAdapter = new RecommendPostsAdapter(R.layout
                .item_recommend_posts);
        mPostsAdapter.setImageLoader(getImageLoader());
        recyclerPosts.setAdapter(mPostsAdapter);
        mPlateTypeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                ForumHomepageBean.InfoBean.ForumBean item = (ForumHomepageBean.InfoBean
                        .ForumBean) adapter.getData().get(position);
                bundle.putString(ConstantUtils.FORUM_PLATE_ID, item.getId());
                startActivity(PlateHomepageActivity.class, false, bundle);
            }
        });
        mPostsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                getChildView(view);

                ForumHomepageBean.InfoBean.ListBean item = (ForumHomepageBean.InfoBean.ListBean)
                        adapter.getData().get(position);
                String id = item.getId();
                Bundle bundle = new Bundle();
                bundle.putString(ConstantUtils.POSTS_ITEM_ID, id);
                startActivity(PostsDetailActicity.class, false, bundle);

            }
        });
        recyclerPosts.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                List<ForumHomepageBean.InfoBean.ListBean> data = adapter.getData();
                Bundle bundle = new Bundle();
                bundle.putString(ConstantUtils.POSTS_USER_ID, data.get(position).getMember_id());
                startActivity(PeopleHomepageActivity.class, false, bundle);

            }
        });

    }

    private void getChildView(View view) {
        TextView text = (TextView) view.findViewById(R.id.tv_name_user);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_avatar_posts);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(PeopleHomepageActivity.class);
            }
        });
    }

    private void initView() {
        recyclerPlate.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerPlate.addItemDecoration(new GridSpacingItemDecoration(4, 50, true));
        recyclerPosts.setLayoutManager(new LinearLayoutManager(this));
        recyclerPlate.setNestedScrollingEnabled(false);
        recyclerPosts.setNestedScrollingEnabled(false);
        recyclerPosts.addItemDecoration(new MyItemDecoration(mContext,R.color.grayF4,R.dimen.dp_8));

        forumBanner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                getImageLoader().load(path).into(imageView);
            }
        });
        forumBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if (forumBanner != null) {
                    mBannerBeanList.get(position);
                }
            }
        });
    }

    private void fillbanner(List<ForumHomepageBean.InfoBean.AdBean> banner) {
        mBannerBeanList = banner;
        ArrayList<String> bannerImv = new ArrayList<>();
        for (ForumHomepageBean.InfoBean.AdBean bannerbean : banner) {
            bannerImv.add(bannerbean.getSave_path());
        }
        forumBanner.update(bannerImv);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        requestNet();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_forum_homepage;
    }

}
