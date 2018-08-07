package com.cnsunrun.jiajiagou.home.homepage;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.KeyboardUtils;
import com.blankj.utilcode.utils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseCallBack;
import com.cnsunrun.jiajiagou.common.base.BaseFragment;
import com.cnsunrun.jiajiagou.common.base.Refreshable;
import com.cnsunrun.jiajiagou.common.constant.ArgConstants;
import com.cnsunrun.jiajiagou.common.constant.JjgConstant;
import com.cnsunrun.jiajiagou.common.constant.SPConstant;
import com.cnsunrun.jiajiagou.common.network.DialogCallBack;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.ConstantUtils;
import com.cnsunrun.jiajiagou.common.util.DisplayUtil;
import com.cnsunrun.jiajiagou.common.util.SPUtils;
import com.cnsunrun.jiajiagou.common.widget.GridSpacingItemDecoration;
import com.cnsunrun.jiajiagou.common.widget.view.NoticeView;
import com.cnsunrun.jiajiagou.forum.ForumHomepageActivity;
import com.cnsunrun.jiajiagou.forum.PostsDetailActicity;
import com.cnsunrun.jiajiagou.home.ConferenceHallActivity;
import com.cnsunrun.jiajiagou.home.ConvenienceActivity;
import com.cnsunrun.jiajiagou.home.NewsActivity;
import com.cnsunrun.jiajiagou.home.PostPropertyServiceActivity;
import com.cnsunrun.jiajiagou.home.adapter.AnnouncementAdapter;
import com.cnsunrun.jiajiagou.home.adapter.HomeEnterAdapter;
import com.cnsunrun.jiajiagou.home.bean.NoticeBean;
import com.cnsunrun.jiajiagou.home.bean.RecommentThreadBean;
import com.cnsunrun.jiajiagou.home.location.LocationAreaActivity;
import com.cnsunrun.jiajiagou.login.LoginActivity;
import com.cnsunrun.jiajiagou.personal.AddressBookActivity;
import com.cnsunrun.jiajiagou.personal.CommunityVolunteerApplyActivity;
import com.cnsunrun.jiajiagou.personal.MedicalReportActivity;
import com.cnsunrun.jiajiagou.personal.NoticeActivity;
import com.cnsunrun.jiajiagou.personal.NoticeDetailActivity;
import com.cnsunrun.jiajiagou.personal.NoticeResp;
import com.cnsunrun.jiajiagou.personal.information.InformationActivity;
import com.cnsunrun.jiajiagou.personal.waste.WastePriceActivity;
import com.cnsunrun.jiajiagou.product.MyItemDecoration;
import com.cnsunrun.jiajiagou.product.ProductListActivity;
import com.cnsunrun.jiajiagou.video.ys7.DeviceListActivity;
import com.google.gson.Gson;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description:
 * Data：2017/8/18 0018-上午 11:48
 * Blog：http://blog.csdn.net/u013983934
 * Author: CZ
 */
public class HomeFragment extends BaseFragment implements NoticeView.OnNoticeClickListener,
        TextView.OnEditorActionListener, BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.RequestLoadMoreListener, View.OnClickListener {
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 999;
    @BindView(R.id.iv_news)
    ImageView mIvNews;
//    @BindView(R.id.banner)
   private Banner mBanner;
//
//    @BindView(R.id.recycle_enter)
   private  RecyclerView mEnterRecycler;
//    @BindView(R.id.recyler_announcement)

   private RecyclerView mAnnouncementRecycler;     //公告
    private View headerView;

    @BindView(R.id.home_forum)
    RecyclerView mForumRecycler;


//    @BindView(R.id.iv_help)
//    ImageView mIvHelp;
//    @BindView(R.id.iv_clean)
//    ImageView mIvClean;
//    @BindView(R.id.iv_service)
//    ImageView mIvService;
//    @BindView(R.id.tv_help)
//    TextView mTvHelp;
//    @BindView(R.id.tv_clean)
//    TextView mTvClean;
//    @BindView(R.id.tv_service)
//    TextView mTvService;

//    @BindView(R.id.iv_doctors)
//    ImageView mIvDoctors;
//    @BindView(R.id.iv_volunteer)
//    ImageView mIvVolunteer;
//    @BindView(R.id.iv_address_book)
//    ImageView mIvAddressBook;
//    @BindView(R.id.tv_doctors)
//    TextView mTvDoctors;
//    @BindView(R.id.tv_volunteer)
//    TextView mTvVolunteer;
//    @BindView(R.id.tv_address_book)
//    TextView mTvAddressBook;
    //    @BindView(R.id.notice_view)
//    NoticeView mNoticeView;
//    @BindView(R.id.ll_quick_entrance)
//    LinearLayout mLlQuickEntrance;
//    @BindView(R.id.ll_bianmin)
//    LinearLayout mBianminLayout;

    List<HomeResp.InfoBean.BannerBean> mBannerBeanList;

    @BindView(R.id.et_search)
    EditText mEtSearch;
    //    private HomeProductAdapter mProductAdapter;
    //    private HomeForumAdapter mForumAdapter;
    private String mDistrict_id;
    //    private HomeForumBannerAdapter mForumBannerAdapter;
    private HomeForumAdapter mForumAdapter;
    private List<HomeForumBean> mForum;
    //    private ForumPlateAdapter mForumPlateAdapter;

    private int lastPosition = 0;//记录上一次position,默认为0,第一页
    private AnnouncementAdapter announcementAdapter;
    private HomeEnterAdapter homeEnterAdapter;
    private int pageNumber = 1;

    private void initHeaderView(int screenWidth){
        headerView = View.inflate(mContext, R.layout.header_home_fragment, null);
        mBanner = ButterKnife.findById(headerView, R.id.banner);
        RecyclerView mEnterRecycler= ButterKnife.findById(headerView, R.id.recycle_enter);
        TextView mEnterForumTv= ButterKnife.findById(headerView, R.id.tv_enter_forum);
        TextView showAllTv= ButterKnife.findById(headerView, R.id.tv_show_all);
        mEnterForumTv.setOnClickListener(this);
        showAllTv.setOnClickListener(this);

        RecyclerView mAnnouncementRecycler= ButterKnife.findById(headerView, R.id.recyler_announcement);
        mEnterRecycler.setLayoutManager(new GridLayoutManager(mContext, 5));
        mEnterRecycler.addItemDecoration(new GridSpacingItemDecoration(5, DisplayUtil.dip2px
                (mContext, 15), true));
        mEnterRecycler.setNestedScrollingEnabled(false);
        int imageWidth = DisplayUtil.dip2px(mContext, (screenWidth - 96) / 5);
        homeEnterAdapter = new HomeEnterAdapter(getImageLoader(), imageWidth);

        mEnterRecycler.setAdapter(homeEnterAdapter);
        homeEnterAdapter.setOnItemClickListener(this);
        mAnnouncementRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        mAnnouncementRecycler.setNestedScrollingEnabled(false);
        mAnnouncementRecycler.addItemDecoration(new MyItemDecoration(mContext, R.color.transparent, R.dimen.dp_8));
        announcementAdapter = new AnnouncementAdapter();
//        announcementAdapter.setFooterView(View.inflate(mContext,R.layout.footer_announcement,null));
        mAnnouncementRecycler.setAdapter(announcementAdapter);
        announcementAdapter.setOnItemClickListener(this);
    }
    @Override
    protected void init() {
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        int screenWidth = DisplayUtil.px2dip(mContext, metric.widthPixels);
//        //7.5是view的外边距
//        double spacing = 7.5 * 4;
//        int recyclerViewWidth = (int) (screenWidth - spacing);
//        double height = recyclerViewWidth / 3 * 0.7;
//        ViewGroup.LayoutParams layoutParams = mLlQuickEntrance.getLayoutParams();
//        layoutParams.height = DisplayUtil.dip2px(mContext, (float) height);
//        ViewGroup.LayoutParams params = mBianminLayout.getLayoutParams();
//        params.height = DisplayUtil.dip2px(mContext, (float) height);

        /**  UI调整*/


        /** end*/

        initHeaderView(screenWidth);
        mForumRecycler.setNestedScrollingEnabled(false);
        mForumRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        mForumRecycler.addItemDecoration(new MyItemDecoration(mContext, R.color.grayF4, R.dimen
                .dp_10));
        mForumAdapter = new HomeForumAdapter(R.layout.item_home_forum);
        mForumAdapter.addHeaderView(headerView);
        mForumAdapter.setImageLoader(getImageLoader());
        int imageHeight = DisplayUtil.dip2px(mContext, (float) (screenWidth * 0.43));
        mForumAdapter.setImageHeight(imageHeight);
        mForumRecycler.setAdapter(mForumAdapter);
        mForumAdapter.setOnLoadMoreListener(this);
        getData();
        requestPostsData(pageNumber);
//        mNoticeView.setOnNoticeClickListener(this);

//        List<HomeResp.InfoBean.LuntanListBean> list=new ArrayList<>();
//        for (int i=0;i<5;i++){
//            HomeResp.InfoBean.LuntanListBean bean = new HomeResp.InfoBean.LuntanListBean();
//            bean.setLikes(i+"");
//            bean.setReplies(i+"");
//            bean.setViews(i+"");
//            bean.setTitle("test"+i);
//            list.add(bean);
//        }
//        int width = getResources().getDisplayMetrics().widthPixels / 10;
//        mForumAdapter.setWidth(width);
//        postsAdapter.setNewData(list);
        mForumAdapter.setOnItemClickListener(this);


        mBanner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {

                getImageLoader().load(path).into(imageView);
            }
        });
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if (mBannerBeanList != null) {
                    HomeResp.InfoBean.BannerBean bannerBean = mBannerBeanList.get(position);
                }
            }
        });

        mEtSearch.setOnEditorActionListener(this);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (adapter instanceof AnnouncementAdapter) {
            List<NoticeBean> data = adapter.getData();
            LogUtils.i("点击了 " + view.getId());
            Intent intent = new Intent(mContext, NoticeDetailActivity.class);
            intent.putExtra(ArgConstants.NOTICEID, data.get(position).notice_id);
            startActivity(intent);
        } else if (adapter instanceof HomeEnterAdapter) {
            List<HomeResp.InfoBean.Entry> data = adapter.getData();
            Intent intent;
            switch (data.get(position).getEntry_id()) {
                case "1":   /**变废为宝*/
                    startActivity(WastePriceActivity.class);
                    break;
                case "2":   /**政务公开*/
                    startActivity(NewsActivity.class);
                    break;
                case "3":   /**议事堂*/
                    if (JjgConstant.isLogin(mContext)) {
                        startActivity(ConferenceHallActivity.class);
                    } else {
                        startActivity(new Intent(mContext, LoginActivity.class));
                    }
                    break;
                case "4":   /**智慧社区*/
//                    showToast("功能暂未开放, 敬请期待!");
                    if (JjgConstant.isLogin(mContext)) {
                        startActivity(DeviceListActivity.class);
                    } else {
                        startActivity(new Intent(mContext, LoginActivity.class));
                    }
                    break;
                case "5":   /**健康档案*/
                    if (JjgConstant.isLogin(mContext)) {
                        startActivity(new Intent(mContext, MedicalReportActivity.class));
                    } else {
                        startActivity(new Intent(mContext, LoginActivity.class));
                    }
                    break;
                case "6":   /**小事帮忙*/
                    if (JjgConstant.isLogin(mContext)) {
                        intent = new Intent(mContext, PostPropertyServiceActivity.class);
                        intent.putExtra(ArgConstants.SERVICE_TYPE, PostPropertyServiceActivity
                                .TYPE_PACKAGE);
                        startActivity(intent);
                    } else {
                        startActivity(new Intent(mContext, LoginActivity.class));
                    }
                    break;
                case "7":   /**保洁维修*/
                    if (JjgConstant.isLogin(mContext)) {
                        intent = new Intent(mContext, PostPropertyServiceActivity.class);
                        intent.putExtra(ArgConstants.SERVICE_TYPE, PostPropertyServiceActivity
                                .TYPE_CLEAN);
                        startActivity(intent);
                    } else {
                        startActivity(new Intent(mContext, LoginActivity.class));
                    }
                    break;
                case "8":   /**家政服务*/
                    startActivity(new Intent(mContext, ConvenienceActivity.class));
                    break;
                case "9":   /**社区志愿者*/
                    if (JjgConstant.isLogin(mContext)) {
                        startActivity(new Intent(mContext, CommunityVolunteerApplyActivity.class));
                    } else {
                        startActivity(new Intent(mContext, LoginActivity.class));
                    }
                    break;
                case "10":   /**万能通讯录*/
                    startActivity(new Intent(mContext, AddressBookActivity.class));
                    break;
            }
        } else if (adapter instanceof HomeForumAdapter) {
            List<RecommentThreadBean.InfoBean> data = adapter.getData();
            String id = data.get(position).getId();
            Bundle bundle = new Bundle();
            bundle.putString(ConstantUtils.POSTS_ITEM_ID, id);
            startActivity(PostsDetailActicity.class, false, bundle);
        }
    }

    @Override
    public void onLoadMoreRequested() {
        LogUtils.i("onLoadMoreRequested");
        mForumRecycler.post(new Runnable()
        {
            @Override
            public void run()
            {
                requestPostsData(pageNumber + 1);
            }
        });
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getData();
        pageNumber=1;
        requestPostsData(pageNumber);
    }

    private void getData() {
        HashMap params=new HashMap();
        params.put("district_id",SPUtils.getString(mContext,SPConstant.DISTRICT_ID));
        HttpUtils.get(NetConstant.HOME, params, new DialogCallBack((Refreshable) HomeFragment.this) {
            @Override
            public void onResponse(String response, int id) {

                HomeResp homeResp = new Gson().fromJson(response, HomeResp.class);
                if (handleResponseCode(homeResp)) {

                    HomeResp.InfoBean info = homeResp.getInfo();
                    if (info != null) {
                        List<HomeResp.InfoBean.BannerBean> banner = info.getBanner();
//                        List<HomeResp.InfoBean.LuntanListBean> luntanList = info.getLuntan_list();
                        List<HomeResp.InfoBean.Entry> entry = info.getEntry();
//                        mForum = info.getForum();
                        List<NoticeBean> notic = info.getNotic();
                        List<HomeResp.InfoBean.PropertyBean> property = info.getProperty();
                        List<HomeResp.InfoBean.BianminBean> bianmin = info.getBianmin();
//                        mForumAdapter.setNewData(luntanList);
                        homeEnterAdapter.setNewData(entry);
                        announcementAdapter.setNewData(notic);
//                        mForumAdapter.notifyDataSetChanged();
//                        mNoticeView.addNotice(notic);
//                        mNoticeView.startFlipping();
                        fillbanner(banner);
                    }
                }
            }
        });

    }

    public void requestPostsData(final int page){
        HashMap map=new HashMap();
        map.put("p",String.valueOf(page));
        HttpUtils.get(NetConstant.HOME_RECOMMEND_THREAD, map, new DialogCallBack((Refreshable) HomeFragment.this) {
            @Override
            public void onResponse(String response, int id) {
                RecommentThreadBean threadBean = new Gson().fromJson(response, RecommentThreadBean.class);
                if (handleResponseCode(threadBean)) {
                    List<RecommentThreadBean.InfoBean> info = threadBean.getInfo();
                    if (page == 1)
                    {
                        LogUtils.i("1");
                        mForumAdapter.setNewData(info);
                    } else
                    {
                        LogUtils.i("2");
                        if (info != null && info.size() > 0)
                        {
                            LogUtils.i("3");
                            mForumAdapter.addData(info);
                            mForumAdapter.loadMoreComplete();
                            pageNumber++;
                        } else
                        {
                            LogUtils.i("4");
                            mForumAdapter.loadMoreEnd();
                        }
                    }
                    mForumAdapter.checkFullPage(mForumRecycler);
                    LogUtils.i("page "+page);
                }
            }
        });

    }

    
//   tv_enter_forum

    private void fillbanner(List<HomeResp.InfoBean.BannerBean> banner) {
        mBannerBeanList = banner;
        ArrayList<String> bannerImv = new ArrayList<>();
        for (HomeResp.InfoBean.BannerBean bannerbean : banner) {
            bannerImv.add(bannerbean.getImage());
        }
        mBanner.update(bannerImv);
    }

    @Override
    protected int getChildLayoutRes() {
        return R.layout.fragment_home;
    }


    @OnClick({R.id.iv_loc, R.id.iv_news})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.iv_loc:
                checkSelfPermission();
                break;
            case R.id.iv_news:
                if (JjgConstant.isLogin(mContext)) {
                    startActivity(new Intent(mContext, InformationActivity.class));
                } else {
                    startActivity(new Intent(mContext, LoginActivity.class));
                }

                break;
//            case R.id.tv_enter_forum:
//                break;

        }
    }

    private void checkSelfPermission() {

        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            Bundle bundle = new Bundle();
            bundle.putInt(ConstantUtils.TO_COMMUNITY_SELECT, ConstantUtils.HOMEPA_FRAGMENT);
            Intent intent = new Intent(mContext, LocationAreaActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, ConstantUtils.HOMEPA_FRAGMENT);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == ConstantUtils.HOMEPA_FRAGMENT) {
                mDistrict_id = data.getStringExtra(ConstantUtils.COMMUNITY_ID);
                if (!TextUtils.isEmpty(mDistrict_id)) {
                    SPUtils.put(mContext, SPConstant.DISTRICT_ID, mDistrict_id);
                    getNoticeData();
                }
                return;
            }
            mDistrict_id = data.getStringExtra(ConstantUtils.COMMUNITY_ID);
            if (!TextUtils.isEmpty(mDistrict_id)) {
                SPUtils.put(mContext, SPConstant.DISTRICT_ID, mDistrict_id);
                getNoticeData();
            }
        }
    }

    private void getNoticeData() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("district_id", mDistrict_id);

        HttpUtils.get(NetConstant.NOTICE, hashMap, new BaseCallBack(mContext) {
            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(response);
                NoticeResp resp = new Gson().fromJson(response, NoticeResp.class);
                if (handleResponseCode(resp)) {
                    List<NoticeBean> info = resp.getInfo();
                    if (info != null && info.size() > 0) {
                        announcementAdapter.setNewData(info);
//                        mNoticeView.addNotice(info);
//                        mNoticeView.startFlipping();
                    }
                }
            }
        });
    }

    @Override
    public void onNoticeClick(int position, String notice_id) {
        Intent intent = new Intent(mContext, NoticeDetailActivity.class);
        intent.putExtra(ArgConstants.NOTICEID, notice_id);
        startActivity(intent);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            KeyboardUtils.hideSoftInput(getActivity());
            Intent intent = new Intent(mContext, ProductListActivity.class);
            intent.putExtra(ArgConstants.KEYWORD, mEtSearch.getText().toString().trim());
            startActivity(intent);
        }


        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_enter_forum:
                startActivity(new Intent(mContext, ForumHomepageActivity.class));
                break;
            case R.id.tv_show_all:
                startActivity(new Intent(mContext, NoticeActivity.class));
                break;
        }
    }
}
