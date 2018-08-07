package com.cnsunrun.jiajiagou.home;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseHeaderActivity;
import com.cnsunrun.jiajiagou.common.base.Refreshable;
import com.cnsunrun.jiajiagou.common.constant.SPConstant;
import com.cnsunrun.jiajiagou.common.network.DialogCallBack;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.util.ConstantUtils;
import com.cnsunrun.jiajiagou.common.util.DisplayUtil;
import com.cnsunrun.jiajiagou.common.util.SPUtils;
import com.cnsunrun.jiajiagou.home.adapter.TypeListAdapter;
import com.cnsunrun.jiajiagou.home.adapter.VoteResultAdapter;
import com.cnsunrun.jiajiagou.home.bean.DiscussDetailBean;
import com.cnsunrun.jiajiagou.home.bean.PostRequestBean;
import com.cnsunrun.jiajiagou.product.MyItemDecoration;
import com.google.gson.Gson;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * Created by j2yyc on 2018/1/23.
 */

public class DiscussDetailActivity extends BaseHeaderActivity {

    @BindView(R.id.iv_photo)
    ImageView photoIv;
    @BindView(R.id.tv_vote)
    TextView voteTv;        //是否投票
    @BindView(R.id.tv_discuss_title)
    TextView titleTv;
    @BindView(R.id.tv_content)
    TextView contentTv;
    @BindView(R.id.ngv_discuss_detail_photo)
    NineGridView photoNgv;
    @BindView(R.id.tv_join_number)
    TextView joinTv;        //参与人数
    @BindView(R.id.tv_date)
    TextView dateTv;        //截止时间
    @BindView(R.id.ll_no_vote)
    LinearLayout noVoteLL;  //未投票view
    @BindView(R.id.tv_voted)
    TextView votedTv;       //已投票view
    @BindView(R.id.ll_vote_result)
    LinearLayout voteResultLL;
    //    @BindView(R.id.recycle_vote_result)
//    RecyclerView voteResultREcycle;
//    @BindView(R.id.iv_agree_result)
//    ImageView agreeIv;
//    @BindView(R.id.iv_disagree_result)
//    ImageView disagreeIv;
//    @BindView(R.id.iv_abandon_result)
//    ImageView abandonIv;
    @BindView(R.id.rv_type_list)
    RecyclerView rvTypeList;
    @BindView(R.id.rv_vote_result)
    RecyclerView rvVoteResult;
//    @BindView(R.id.tv_agree_number)
//    TextView agreeNumTv;
//    @BindView(R.id.tv_disagree_number)
//    TextView disagreeNumTv;
//    @BindView(R.id.tv_abandon_number)
//    TextView abandonNumTv;
    private String id;
    private String token;
    private VoteResultAdapter voteResultAdapter;
    private String procedure_id;
    private TypeListAdapter typeAdaper;
    private int infoNumber;
    //    private Integer  agreeNum;
//    private Integer  disagreeNum;
//    private Integer  abstainNum;
//    private Integer  totalNum;


    @Override
    protected void initHeader(TextView tvTitle, ImageView ivBack, TextView tvRight) {
        String title = getIntent().getStringExtra(ConstantUtils.DISCUSS_TITLE);
        tvTitle.setText(title);
    }

    @Override
    protected void init() {
        token = SPUtils.getString(this, SPConstant.TOKEN);
        id = getIntent().getStringExtra(ConstantUtils.DISCUSS_ID);
        int maxwidth = DisplayUtil.getScreenWidth(mContext);
        requestNet();
        getImageLoader().load("").into(photoIv);
//        voteResultREcycle.setLayoutManager(new LinearLayoutManager(this));
//        voteResultREcycle.addItemDecoration(new MyItemDecoration(this, R.color.grayF4, R.dimen.dp_1));
//        voteResultAdapter = new VoteResultAdapter(getVoteResultViewMaxWidth());
        LogUtils.i("max width", getVoteResultViewMaxWidth());
        titleTv.setText("max width");
//        voteResultREcycle.setAdapter(voteResultAdapter);
        photoNgv.setVisibility(View.GONE);
        photoNgv.setSingleImageSize(DisplayUtil.dip2px(mContext, maxwidth));
        photoNgv.setSingleImageRatio(2.0f);

        rvTypeList.setLayoutManager(new GridLayoutManager(this,3));
        typeAdaper = new TypeListAdapter(null);
        rvTypeList.setAdapter(typeAdaper);

        rvVoteResult.setLayoutManager(new LinearLayoutManager(this));
        rvVoteResult.addItemDecoration(new MyItemDecoration(this, R.color.grayF4,R.dimen.dp_1));
        voteResultAdapter = new VoteResultAdapter(getVoteResultViewMaxWidth());
        rvVoteResult.setAdapter(voteResultAdapter);
        initListener();
    }

    private void initListener() {
        typeAdaper.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                DiscussDetailBean.InfoBean.TypeListBean item = typeAdaper.getItem(i);
                postVoteRequest(item.type);
            }
        });
    }

    private void requestNet() {
        HashMap map = new HashMap();
        map.put("procedure_id", id);
        map.put("token", token);
        HttpUtils.get(NetConstant.PROCEDURE_INFO, map, new DialogCallBack((Refreshable) DiscussDetailActivity.this) {
            @Override
            public void onResponse(String response, int id) {
                DiscussDetailBean detailBean = new Gson().fromJson(response, DiscussDetailBean.class);
                if (handleResponseCode(detailBean)) {
                    DiscussDetailBean.InfoBean info = detailBean.getInfo();
                    List<DiscussDetailBean.InfoBean.ImagesBean> images = info.getImages();
                    procedure_id = info.getProcedure_id();
                    getImageLoader().load(images.get(0).getImage()).into(photoIv);
                    initNineGridView(images);
                    setVotedResult(info);
                    titleTv.setText(info.getTitle());
                    setContent(info.getDescription());
                    joinTv.setText("参与人数 : " + info.getNumber());
                    dateTv.setText("截止时间 : " + info.getEnd_date());

                    List<DiscussDetailBean.InfoBean.TypeListBean> type_list = info.getType_list();
                    typeAdaper.setNewData(type_list);
                    voteResultAdapter.setNewData(type_list);
                    infoNumber = info.getNumber();
                    voteResultAdapter.setTotalNum(infoNumber);
//                    List<String> list = Arrays.asList("同意", "不同意", "放弃");
//                    List<VoteResult> voteResults=new ArrayList();
//                    for (String s : list) {
//                        VoteResult voteResult = new VoteResult(s,Integer.valueOf(info.getAgree()), Integer.valueOf
// (info
//                                .getDisagree()), Integer.valueOf(info.getAbstain()), Integer.valueOf(info.getNumber
// ()));
//                        voteResults.add(voteResult);
//                    }
                }

            }
        });
    }

    private void setContent(String content) {
        contentTv.setText(content);
        ViewTreeObserver observer = contentTv.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            boolean isfirstRunning = true;

            @Override
            public void onGlobalLayout() {
                if (isfirstRunning == false) return;//如果不加这一行的条件，出来之后是完整单词+。。。，如果加上，出来就是截断的单词+。。。
                Layout layout2 = contentTv.getLayout();
//                System.out.println("layout2是"+layout2);
                if (contentTv != null && layout2 != null) {
                    int lines = layout2.getLineCount();
                    int originalLength = contentTv.getText().length();
//                    LogUtils.i("原字符串字符数量是 "+originalLength);
//                    System.out.println("当前行数是"+layout2.getLineCount());
//                    System.out.println("被省略的字符数量是"+layout2.getEllipsisCount(lines-1));//看看最后一行被省略掉了多少
//                    System.out.println("被省略的字符起始位置是"+layout2.getEllipsisStart(lines-1));//看看最后一行被省略的起始位置
//                    System.out.println("最后一个可见字符的偏移是"+layout2.getLineVisibleEnd(lines-1));
                    //开始替换系统省略号
                    if (lines < 2) return;//如果只有一行，就不管了
                    if (layout2.getEllipsisCount(lines - 1) == 0) return;//如果被省略的字符数量为0，就不管了
//                    LogUtils.i("被省略长度 "+layout2.getEllipsisCount(lines-1));
                    String showText = contentTv.getText().toString();
                    int ellipsisStart = layout2.getEllipsisStart(lines - 1);//第二行起始位置
                    int singleLength = ellipsisStart - 1;//单行的长度
                    if (originalLength >= singleLength * 2) {
//                        System.out.println("删减前"+showText);
                        showText = showText.substring(0, layout2.getLineVisibleEnd(lines - 1) - singleLength).concat
                                ("...");//在此处自定义要显示的字符
//                        System.out.println("删减后"+showText);
                    } else {
//                        System.out.println("删减前"+showText);
                        showText = showText.substring(0, layout2.getLineVisibleEnd(lines - 1)).concat("...");
                        //在此处自定义要显示的字符
//                        System.out.println("删减后"+showText);
                    }

                    contentTv.setText(showText);
                    isfirstRunning = false;
                }
            }
        });
    }

    private void setVotedResult(DiscussDetailBean.InfoBean info) {
        //当前用户投票状态  0-未投 1-同意 2-不同意 3-弃权
        int maxWidth = getVoteResultViewMaxWidth();

        Integer agreeNum = Integer.valueOf(info.getAgree());
        Integer disagreeNum = Integer.valueOf(info.getDisagree());
        Integer abstainNum = Integer.valueOf(info.getAbstain());
        Integer totalNum = Integer.valueOf(info.getNumber());
//        agreeNumTv.setText(agreeNum + "");
//        disagreeNumTv.setText(disagreeNum + "");
//        abandonNumTv.setText(abstainNum + "");

//        ViewGroup.LayoutParams agreeIvParams = agreeIv.getLayoutParams();
//        ViewGroup.LayoutParams disagreeIvParams = disagreeIv.getLayoutParams();
//        ViewGroup.LayoutParams abandonIvParams = abandonIv.getLayoutParams();


//        agreeIvParams.width = DisplayUtil.dip2px(mContext, (float) agreeNum / (float) totalNum * (float) maxWidth);
//        disagreeIvParams.width = DisplayUtil.dip2px(mContext, (float) disagreeNum / (float) totalNum * (float)
//                maxWidth);
//        abandonIvParams.width = DisplayUtil.dip2px(mContext, (float) abstainNum / (float) totalNum * (float) maxWidth);
//        agreeIv.setLayoutParams(agreeIvParams);
//        disagreeIv.setLayoutParams(disagreeIvParams);
//        abandonIv.setLayoutParams(abandonIvParams);

        if (info.getType().equals("0")) {
            voteTv.setText("未投");
            voteTv.setBackgroundColor(mContext.getResources().getColor(R.color.red_vote));
            noVoteLL.setVisibility(View.VISIBLE);
            votedTv.setVisibility(View.GONE);
//                        voteResultREcycle.setVisibility(View.GONE);
            voteResultLL.setVisibility(View.GONE);
        } else {
            voteTv.setText("已投");
            voteTv.setBackgroundColor(mContext.getResources().getColor(R.color.green_discuss_vote));
            votedTv.setVisibility(View.VISIBLE);
            votedTv.setText(info.getType_title());
            noVoteLL.setVisibility(View.GONE);
            voteResultLL.setVisibility(View.VISIBLE);
//                        voteResultREcycle.setVisibility(View.VISIBLE);
        }
//                    voteResultAdapter.setNewData(voteResults);
    }


    private void initNineGridView(List<DiscussDetailBean.InfoBean.ImagesBean> images) {
        if (images.size() > 1) {
            photoNgv.setVisibility(View.VISIBLE);
            List<ImageInfo> urlList = new ArrayList();
            for (DiscussDetailBean.InfoBean.ImagesBean image : images) {
                ImageInfo info1 = new ImageInfo();
                info1.setThumbnailUrl(image.getImage());
                info1.setBigImageUrl(image.getImage());
                urlList.add(info1);
            }
            photoNgv.setAdapter(new NineGridViewClickAdapter(mContext, urlList));
        } else {
            photoNgv.setVisibility(View.GONE);
        }
    }


//    @OnClick({R.id.tv_agree, R.id.tv_disagree, R.id.tv_abandon})
//    public void onViewClick(View view) {
//        switch (view.getId()) {
//            case R.id.tv_agree:
//                postVoteRequest("1");
//                break;
//            case R.id.tv_disagree:
//                postVoteRequest("2");
//                break;
//            case R.id.tv_abandon:
//                postVoteRequest("3");
//                break;
//
//        }
//    }

    private int getVoteResultViewMaxWidth() {
        int screenWidth = DisplayUtil.px2dip(mContext, DisplayUtil.getScreenWidth(this));
        int parentLayoutMargin = 30;
        int leftTvWidth = 60;
        int rightTvWidth = 50;
        int margin = 20;
        int maxWidth = screenWidth - parentLayoutMargin - leftTvWidth - rightTvWidth - margin;
        return maxWidth;
    }

    //投票类型 1-同意 2-不同意 3-弃权
    private void postVoteRequest(String type) {

        HashMap map = new HashMap();
        map.put("procedure_id", procedure_id);
        map.put("token", token);
        map.put("type", type);
        HttpUtils.post(NetConstant.PROCEDURE_VOTE, map, new DialogCallBack((Refreshable) DiscussDetailActivity.this) {
            @Override
            public void onResponse(String response, int id) {
                PostRequestBean bean = new Gson().fromJson(response, PostRequestBean.class);
                if (handleResponseCode(bean)) {
                    if (bean.getStatus() == 1) {
//                        voteTv.setText("已投");
//                        noVoteLL.setVisibility(View.GONE);
//                        votedTv.setVisibility(View.VISIBLE);
//                        voteResultLL.setVisibility(View.VISIBLE);
                        requestNet();
//                        totalNum+=1;
//                        if (type.equals("1")) {
//                            agreeNum+=1;
//                        }else if (type.equals("2")){
//                            disagreeNum+=1;
//                        }else {
//                            abstainNum
//                        }

                    }
                }
            }
        });
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_discuss_detail;
    }
}
