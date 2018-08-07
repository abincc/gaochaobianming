package com.cnsunrun.jiajiagou.common.util;

import com.cnsunrun.jiajiagou.common.JjgApplication;

/**
 * 常量工具类
 * <p>
 * author:yyc
 * date: 2017-08-26 11:34
 */
public class ConstantUtils {
    //论坛帖子id
    public static final String POSTS_ITEM_ID="posts_item_id";
    //论坛板块id
    public static final String FORUM_PLATE_ID="forum_plate_id";
    //用于判断来自来个类
    public static final String FROM_CLAZZ="from_clazz";
    //选择的模块对象
    public static final String PLATE_OBJ="plate_obj";
    //论坛板块title
    public static final String FORUM_PLATE_TITLE="forum_plate_title";
    //发帖用户id
    public static final String POSTS_USER_ID="posts_user_id";
    //商品详情
    public static final String PRODUCT_DETAIL="product_detail";
    //商品id
    public static final String PRODUCT_ID="product_id";
    //忘记密码获取的key
    public static final String RESET_PSD_KEY="reset_psd_key";
    //消息id
    public static final String MESSAGE_ID="message_id";
    //消息title类型
    public static final String MESSAGE_TYPE="MESSAGE_TYPE";
    //跳转到小区选择类
    public static final String TO_COMMUNITY_SELECT="to_community_select";
    //小事帮忙或保洁维修
    public static final int POST_PROPERTY_SERVICE_ACT=102;
    //我要开店
    public static final int SHOP_ACT=103;
    //注册
    public static final int REGISTERED_ACT=104;
    //首页
    public static final int HOMEPA_FRAGMENT=105;
    //再次登录
    public static final String RELOGIN="relogin";
    //再次登录请求码
    public static final int RELOGIN_CODE=201;
    //论坛板块请求码
    public static final int PLATE_CODE=202;
    //小事帮忙
    public static final String HELE_SERVICE="hele_service";
    //小区id和title
    public static final String COMMUNITY_ID="community_id";
    public static final String COMMUNITY_TITLE="community_title";

    public static final String TOKEN="token";
    //体检报告id
    public static final String REPORT_ID="report_id";
    //来自保洁 or 小事帮忙
    public static final String FROM_CLEAN_OR_LITTLE="from_clean_or_little";
    public static final String FROM_CLEAN="from_clean";
    public static final String FROM_LITTLE="from_little";
    //议论堂 事件id和title
    public static final String DISCUSS_ID="discuss_id";
    public static final String DISCUSS_TITLE="discuss_title";
    //政务公开-详情id,title,content
    public static final String GOVERNMENT_ID="government_id";
    public static final String GOVERNMENT_TITLE="government_title";
    public static final String GOVERNMENT_CONTENT="government_content";
    public static final String GOVERNMENT_DATE="government_date";
    public static final String GOVERNMENT_IMAGE="government_image";
    public static final String GOVERNMENT_URL="government_url";







    //缓存存储的目录
    public static String ExternalCacheDir = JjgApplication.getContext().getExternalCacheDir()
            .getAbsolutePath();
}
