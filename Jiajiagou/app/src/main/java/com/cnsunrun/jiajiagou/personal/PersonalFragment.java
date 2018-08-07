package com.cnsunrun.jiajiagou.personal;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BaseFragment;
import com.cnsunrun.jiajiagou.common.base.Refreshable;
import com.cnsunrun.jiajiagou.common.constant.JjgConstant;
import com.cnsunrun.jiajiagou.common.network.DialogCallBack;
import com.cnsunrun.jiajiagou.common.network.HttpUtils;
import com.cnsunrun.jiajiagou.common.network.NetConstant;
import com.cnsunrun.jiajiagou.common.widget.CircleTransform;
import com.cnsunrun.jiajiagou.personal.bean.PersonInfoBean;
import com.cnsunrun.jiajiagou.personal.information.InformationActivity;
import com.cnsunrun.jiajiagou.personal.logistics.CleaningActivity;
import com.cnsunrun.jiajiagou.personal.logistics.LittleHelpActivity;
import com.cnsunrun.jiajiagou.personal.logistics.PropertyActivity;
import com.cnsunrun.jiajiagou.personal.order.MyOrderActivity;
import com.cnsunrun.jiajiagou.personal.posts.PersonalPostsActivity;
import com.cnsunrun.jiajiagou.personal.setting.SettingActivity;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:
 * Data：2017/8/18 0018-上午 11:53
 * Blog：http://blog.csdn.net/u013983934
 * Author: 迪迪
 * 个人中心
 */
public class PersonalFragment extends BaseFragment {
    @BindView(R.id.iv_imv)
    ImageView mAvatar;//头像
    @BindView(R.id.textView)
    TextView mNickname;//昵称
    @BindView(R.id.tv_forum_count)
    TextView mForumCount;//论坛消息数
    @BindView(R.id.tv_tenement_count)
    TextView mTenementCount;//物业消息数
    @BindView(R.id.tv_order_count)
    TextView mOrderCount;//订单消息数
    @BindView(R.id.tv_overdue_count)
    TextView mOverdueCount;//欠费消息数
    @BindView(R.id.rl_logistics_manager)
    RelativeLayout rl_Property;

    @Override
    protected void init() {
        getData();
    }

    private void getData()
    {
//        rl_Property.setVisibility(JjgConstant.isProperty(mContext) ? View.VISIBLE : View.GONE);
        final HashMap<String, String> map = new HashMap();
        map.put("token", JjgConstant.getToken(mContext));
        //个人中心首页用户信息接口
        HttpUtils.get(NetConstant.PERSONAL_CENTER, map, new DialogCallBack((Refreshable) PersonalFragment.this)
        {

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(response);
                PersonInfoBean personInfoBean = new Gson().fromJson(response, PersonInfoBean.class);
                if (handleResponseCode(personInfoBean))
                {
                    PersonInfoBean.InfoBean info = personInfoBean.getInfo();
                    getImageLoader()
                            .load(info.getHeadimg())
                            .error(R.drawable.nav_btn_personal_nor)
                            .transform(new CenterCrop(mContext), new CircleTransform(mContext))
                            .into(mAvatar);
                    mNickname.setText(info.getNickname());
                    mForumCount.setText(info.getMessage_forum());
                    mTenementCount.setText(info.getMessage_property());
                    mOrderCount.setText(info.getMessage_order());
                    mOverdueCount.setText(info.getMessage_arrears());
                }
            }
        });
    }

    @Override
    protected int getChildLayoutRes() {
        return R.layout.fragment_personal;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRreshEvent(UserTypeEvent event) {
        getData();
    }

    @Override
    public void onRefresh()
    {
        super.onRefresh();
        getData();
    }

    @OnClick({R.id.tv_setting, R.id.tv_notice, R.id.ll_forum, R.id.ll_tenement, R.id.ll_order, R
            .id.ll_overdue, R.id
            .tv_pay_for, R.id.tv_await, R.id.tv_evaluate, R.id.tv_all_order, R.id.rl_collect, R
            .id.rl_set_up_shop, R
            .id.rl_invitation, R.id.rl_clean, R.id.rl_help, R.id.rl_parking_manager, R.id
            .rl_advice, R.id
            .rl_logistics_manager, R.id.rl_coupon, R.id.rl_nearby, R.id.rl_wuye_xcx})
    public void onViewClicked(View view) {
        Intent inforIntent = new Intent(mContext, InformationActivity.class);
        Intent orderIntent = new Intent(mContext, MyOrderActivity.class);
        switch (view.getId()) {
            case R.id.tv_setting:
                startActivity(new Intent(mContext, SettingActivity.class));
                break;
            //公告
            case R.id.tv_notice:

                startActivity(new Intent(mContext, NoticeActivity.class));

                break;
            case R.id.ll_forum:
                //论坛消息

                inforIntent.putExtra("pos", 0);
                startActivity(inforIntent);

                break;
            case R.id.ll_tenement:
                //物业消息
                inforIntent.putExtra("pos", 1);
                startActivity(inforIntent);
                break;
            case R.id.ll_order:
                //订单消息
                inforIntent.putExtra("pos", 2);
                startActivity(inforIntent);
                break;
            case R.id.ll_overdue:
                //欠费消息
                inforIntent.putExtra("pos", 3);
                startActivity(inforIntent);
                break;
            //代付款
            case R.id.tv_pay_for:
                orderIntent.putExtra("pos", 1);
                startActivity(orderIntent);

                break;
            //待收货
            case R.id.tv_await:
                orderIntent.putExtra("pos", 2);
                startActivity(orderIntent);

                break;
            //待评价
            case R.id.tv_evaluate:
                orderIntent.putExtra("pos", 3);
                startActivity(orderIntent);
                break;
            //全部订单
            case R.id.tv_all_order:
                orderIntent.putExtra("pos", 0);
                startActivity(orderIntent);
                break;
            case R.id.rl_collect:
                //我的收藏
                startActivity(new Intent(mContext, MyCollectionActivity.class));
                break;
            case R.id.rl_set_up_shop:
                //我要开店

                startActivity(new Intent(mContext, SetUpShopActivity.class));

                break;
            case R.id.rl_invitation:
                //帖子
                startActivity(new Intent(mContext, PersonalPostsActivity.class));
                break;
            case R.id.rl_clean://保洁维修
                Intent intent = new Intent(mContext, CleaningActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_help://小事帮忙
                Intent intentHelp = new Intent(mContext, LittleHelpActivity.class);
                startActivity(intentHelp);

                break;
            case R.id.rl_parking_manager:
                startActivity(new Intent(mContext, ParkingManagerActivity.class));
                break;
            case R.id.rl_advice:
                //投诉建议
                startActivity(ComplaintSuggestActivity.class);

                break;
            case R.id.rl_logistics_manager:
                //物业管理
                startActivity(new Intent(mContext, PropertyActivity.class));
                break;
            case R.id.rl_coupon:
                //卡券
                startActivity(new Intent(mContext, MyCouponActivity.class));
                break;
            case R.id.rl_nearby:
                //附近的人
                startActivity(new Intent(mContext, NearByActivity.class));
                break;
            case  R.id.rl_wuye_xcx:
                String appId = "wx0008eb3738042cc7"; // 填应用AppId
                IWXAPI api = WXAPIFactory.createWXAPI(mContext, appId);

                WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
                req.userName = "gh_250943a17798"; // 填小程序原始id
//                req.path = path;                  //拉起小程序页面的可带参路径，不填默认拉起小程序首页
                req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
                api.sendReq(req);
                break;
        }
    }

}
