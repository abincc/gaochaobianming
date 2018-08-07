package com.cnsunrun.jiajiagou.common.network;

/**
 * Created by ${LiuDi}
 * on 2017/8/29on 9:24.
 */

public interface NetConstant
{

//        String BASE_URL = "http://test.cnsunrun.com/wuye/";
    String BASE_URL = "http://www.jiajgou.com/";

//    String BASE_URL = "http://192.168.0.105/lulin/";

    /**
     * 商品分类
     */
    String CATEGROY = "Api/Common/Product/categroy";
    /**
     * 修改绑定手机号 第一步
     */
    String CHANGE_MOBILE_STEP1 = "Api/User/Account/change_mobile_step1";
    /**
     * 修改绑定手机号 第二步
     */
    String CHANGE_MOBILE_STEP2 = "Api/User/Account/change_mobile_step2";
    /**
     * 头像上传
     */
    String UPLOAD_HEADIMG = BASE_URL + "Api/User/Account/upload_headimg";
    /**
     * 获取用户信息
     */
    String GET_USER_INFO = "Api/User/Account/get_user_info";
    /**
     * 模拟token
     */
    String TOKEN = "fnVyqrJ3dLCudXOuhHp1sIV1eaqDtpingnfJ2bKrosl-dZxpsoeWb691mniEoHGugqpgqoWlZa2CdqPctIXQ2n" +
            "-roGO0eHSxsHVzrg";
    /**
     * 上传用户信息
     */
    String EDIT_USER_INFO = "Api/User/Account/edit_user_info";
    /**
     * 用户注册
     */
    String REGISTER =  "Api/Common/User/register";
    /**
     * 发送验证码
     */
    String SEND_MOBILE_CODE =  "Api/Common/Code/send_mobile_code";
    /**
     * 登录
     */
    String LOGIN =  "Api/Common/User/login";
    /**
     * 获取token
     */
    String GET_TOKEN =  "Api/Common/Public/get_token";
    /**
     * 修改密碼
     */
    String CHANGE_PASSWORD = "Api/User/Account/change_password";
    /**
     * 购物车
     */
    String SHOPCART = "Api/User/Cart/index";
    /**
     * 收藏列表
     */
    String PRODUCTCOLLECTION = "Api/User/ProductCollection/index";
    /**
     * 移除收藏
     */
    String COLLECTIONCANCEL = "Api/User/ProductCollection/cancel";
    /**
     * 商城搜索
     */
    String PRODUCT = "Api/Common/Product/index";
    /**
     * 地区
     */
    String AREA = "Api/Common/Public/area";
    /**
     * 首页
     */
    String HOME = "Api/Common/Index/index";
    /**
     * 首页-推荐帖子
     */
    String HOME_RECOMMEND_THREAD = "Api/Common/Index/recommend_thread";
    /**
     * 便民服务
     */
    String CONVENIENCE = "Api/Common/Bianmin/index";
    /**
     * 地址列表
     */
    String ADDRESS = "Api/User/Address/index";
    /**
     * 设为默认
     */
    String SET_DEFAULT = "Api/User/Address/set_default";
    /**
     * 商品详情
     */
    String PRODUCT_INFO = "Api/Common/Product/info";
    /**
     * 删除地址
     */
    String ADDRESS_DEL = "Api/User/Address/del";
    /**
     * 公告
     */
    String NOTICE = "Api/Common/Notice/index";
    /**
     * 公告详情
     */
    String NOTICE_INFO = "Api/Common/Notice/info";
    /**
     * 车位管理
     */
    String PARKING = "Api/User/Parking/index";
    /**
     * 小事帮忙列表
     */
    String LITTLEHELP = "Api/User/Property/trifle";
    /**
     * 添加地址
     */
    String ADDRESS_ADD = "Api/User/Address/add";
    /**
     * 编辑地址
     */
    String ADDRESS_EDITOR = "Api/User/Address/edit";
    /**
     * 添加购物车
     */
    String ADDPRODUCT = "Api/User/Cart/add";
    /**
     * 删除购物车
     */
    String CART_REMOVE = "Api/User/Cart/del";
    /**
     * 购物车修改数量
     */
    String CART_SET = "Api/User/Cart/set_num";
    /**
     * 确认订单界面
     */
    String ORDER_CONFIRM = "Api/User/Order/create";
    /**
     * 收藏
     */
    String COLLECT = "Api/User/ProductCollection/add";
    /**
     * 立刻购买
     */
    String BUY_NOW = "Api/User/Order/buy_now";
    /**
     * 确认订单
     */
    String CREATE_ORDER = "Api/User/Order/create";
    /**
     * 全部订单
     */
    String ALLORDER = "Api/User/Order/index";
    /**
     * 待收货
     */
    String RECEIVE = "Api/User/Order/received";
    /**
     * 论坛首页
     */
    String FORUM_HOMEPAGE = "Api/Forum/ForumPublic/index";
    /**
     * 板块主页
     */
    String PLATE_HOMEPAGE = "Api/Forum/ForumPublic/forum_index";
    /**
     * 版块主页-全部主题
     */
    String ALL_THEMES = "Api/Forum/ForumPublic/thread_all";
    /**
     * 版块主页-精华主题
     */
    String ESSENCE_THEMES = "Api/Forum/ForumPublic/thread_digest";
    /**
     * 主题详情-主题信息
     */
    String THEMES_DETAIL = "Api/Forum/ForumPublic/thread_detail";
    /**
     * 主题详情-评论列表
     */
    String THEMES_COMMENT = "Api/Forum/ForumPublic/post_list";
    /**
     * 全部版块
     */
    String ALL_PLATE = "Api/Forum/ForumPublic/forum_list";
    /**
     * 发布主题
     */
    String SEND_THEME = "Api/Forum/Forum/thread_add";
    /**
     * 论坛搜索
     */
    String FORUM_SEARCH = "Api/Forum/ForumPublic/search";
    /**
     * 评论主题
     */
    String COMMENT_THEME = "Api/Forum/Forum/post_add";
    /**
     * 主题点赞
     */
    String THEME_LIKE = "Api/Forum/Forum/thread_like";
    /**
     * 主题取消点赞
     */
    String THEME_LIKE_CANCEL = "Api/Forum/Forum/thread_like_cancel";
//    String THEME_LIKE_CANCEL = "Api/Forum/thread_like_cancel";
    /**
     * 评论点赞
     */
    String COMMENT_LIKE = "Api/Forum/Forum/post_like";
    /**
     * 评论取消点赞
     */
    String COMMENT_LIKE_CANCEL = "Api/Forum/Forum/post_like_cancel";
    /**
     * 回复
     */
    String REPLY = "Api/Forum/Forum/reply_add";
    /**
     * 个人主页-用户信息
     */
    String HOMEPAGE_USER_INFO = "Api/Forum/ForumPublic/home";
    /**
     * 个人主页-帖子
     */
    String HOMEPAGE_POSTS = "Api/Forum/ForumPublic/home_thread";
    /**
     * 个人主页-点赞
     */
    String HOMEPAGE_LIKE = "Api/Forum/ForumPublic/home_like";
    /**
     * 个人主页-回复
     */
    String HOMEPAGE_REPLY = "Api/Forum/ForumPublic/home_post";
    /**
     * 个人中心-主页
     */
    String PERSONAL_CENTER = "Api/User/Index/index";

    /**
     *获取小事服务类型
     */
    String LITTLE_HELP_SERVICE= "Api/Common/Public/get_trifle_service";
    /**
     *获取保洁维修服务类型
     */
    String CLEANING_MAINTENANCE_SERVICE= "Api/Common/Public/get_clean_service";
    /**
     * 个人中心-小事帮忙-列表
     */
    String LITTLE_HELP_LIST= "Api/User/Trifle/index";
    /**
     *个人中心-小事帮忙-提交
     */
    String LITTLE_HELP_SUBMIT= "Api/User/Trifle/add";
    /**
     *个人中心-保洁维修-提交
     */
    String CLEANING_MAINTENANCE_SUBMIT= "Api/User/Clean/add";
    /**
     * 个人中心-开店申请
     */
    String APPLY_SHOP = "Api/User/Account/apply_shop";
    /**
     * 个人中心-投诉建议-提交
     */
    String SUGGEST_SUBMIT = "Api/User/Complaint/index";
    /**
     * 个人中心-意见反馈
     */
    String FEEDBACK = "Api/User/Feedback/index";
    /**
     * 我的订单待支付
     */
    String UNPAID = "Api/User/Order/unpaid";
    /**
     * 我的订单待评价
     */
    String EVALUATE = "Api/User/Order/evaluat";
    /**
     * 订单详情
     */
    String ORDER_INFO = "Api/User/Order/info";
    /**
     * 添加购物车
     */
    String CART_ADD = "Api/User/Cart/add";
    /**
     * 申请退款
     */
    String APPLY_RETURN = "Api/User/Order/apply_returns";
    /**
     * 确认收货
     */
    String CONFIRM_RECEIPT = "Api/User/Order/confirm_receipt";
    /**
     * 保洁维修列表
     */
//    String CLEAN = "Api/User/Property/clean";
    String CLEAN = "Api/User/Clean/index";
    /**
     * 保洁维修取消
     */
    String CLEAN_CANCLE = "Api/User/Clean/cancel";
    /**
     * 保洁详情
     */
    String CLEAN_INFO = "Api/User/Clean/info";
    /**
     * 小事帮忙取消
     */
    String TRIFLE_CANCEL = "Api/User/Trifle/cancel";
    /**
     * 小事帮忙详情
     */
    String TRIFLE_DETAIL = "Api/User/Trifle/info";
    /***
     * 物业管理小事帮忙详情
     */
    String CLEAN_PROPERTY = "Api/User/Property/info_trifle";
    /**
     * 物业管理小事帮忙处理
     */
    String TRIFLE_DETAIL_PROPERTY = "Api/User/Property/deal_trifle";
    /**
     * 物业管理小事帮忙取消
     */
    String HELP_CANCEL = "Api/User/Property/cancel_trifle";
    /**
     * 物业管理保洁列表
     */
    String CLEAN_LIST = "Api/User/Property/clean";
//    String CLEAN_LIST = "Api/User/Clean/index";
    /**
     * 物业管理保洁取消
     */
    String CLEAN_CANCLEV2 = "Api/User/Property/cancel_clean";
    /**
     * 物业管理保洁详情
     */
    String CLEAN_DETAil = "Api/User/Property/info_clean";
    /**
     * 物业管理保洁维修处理
     */
    String Clean_DETAIL_PROPERTY = "Api/User/Property/deal_clean";
    /**
     * 取消订单
     */
    String ORDER_CANCEL = "Api/User/Order/cancel";
    /**
     * 商品详情-商品评论
     */
    String PRODUCT_COMMENT="Api/Common/Product/comment";
    /**
     * 找回密码（第一步）
     */
    String GET_PASSWORD_ONE="Api/Common/User/forgot_step_1";
    /**
     * 找回密码（第二步）
     */
    String GET_PASSWORD_TWO="Api/Common/User/forgot_step_2";
    /**
     * 我的消息-论坛消息-列表
     */
    String FORUM_MESSAGE="Api/Forum/Forum/message_list";
    /**
     * 我的消息-欠费消息-列表
     */
    String ARREARS_MESSAGE="Api/User/Message/arrears";
    /**
     * 我的消息-订单消息-列表
     */
    String ORDER_MESSAGE="Api/User/Message/order";
    /**
     * 我的消息-物业消息-列表
     */
    String PROPERTY_MESSAGE="Api/User/Message/property";
    /**
     * 我的消息-消息详情
     */
    String MESSAGE_DETAIL="Api/User/Message/info";

    /**
     * 评价商品
     */
    String COMMENT = "Api/User/Order/comment";
    /**
     * 商品详情-商品分享数据
     */
    String COMMODITY_DETAIL_SHARE= "Api/Common/Product/share";
    /**
     * 关于我们
     */
    String GET_ABOUT_URL = "Api/Common/Public/get_about_url";
    /**
     * prepare id
     */
    String WX_IOS_PAY = "Api/User/Order/wx_ios_pay";
    /**
     * 货到付款
     */
    String DELIVERY_PAY = "Api/User/Order/cod";
    /**
     * 获取客服电话
     */
    String GETPHONE = "Api/Common/Public/get_service_telphone";
    /**
     * 卡券
     */
    String COUPON = "Api/User/Card/index";
    /**
     * 兑换
     */
    String EXCHANGE = "Api/User/Card/exchange";

    /**
     * 万能通讯录
     */
    String ADDRESS_BOOK="Api/Common/Tbook/index";

    /**
     * 体检报告--列表
     */
    String PEPORT_LIST="Api/User/Report/index";
    /**
     * 体检报告--提交
     */
    String PEPORT_COMMIT="Api/User/Report/add";
    /**
     * 体检报告--详情
     */
    String PEPORT_DETAIL="Api/User/Report/detail";
    /**
     * 申请志愿者
     */
    String APPLY_VOLUNTEER="Api/User/Account/apply_volunteer";

    /**
     * 支付宝-生成订单签名
     */
    String ORDER_SIGN="Api/User/Order/ali_pay";
    /**
     * 个人中心-物业管理-保洁保修-处理回复
     */
    String REPLY_CLEAN="Api/User/Property/reply_clean";
    /**
     * 个人中心-物业管理-小事帮忙-处理回复
     */
    String REPLY_TRIFLE="Api/User/Property/reply_trifle";
    /**
     *  议事堂-事件列表
     */
    String PROCEDURE_LIST="Api/User/Procedure/index";
    /**
     *  议事堂-事件详情
     */
    String PROCEDURE_INFO="Api/User/Procedure/info";
    /**
     *  议事堂-事件详情-投票
     */
    String PROCEDURE_VOTE="Api/User/Procedure/vote";
    /**
     *  政务公开-列表
     */
    String GOVERNMENT="Api/Common/Government/index";
    /**
     *  政务公开-列表详情
     */
    String GOVERNMENT_INFO="Api/Common/Government/info";
    /**
     *  主题详情-主题信息-分享接口
     */
    String SHARE_POSTS="Api/Forum/ForumPublic/share_detail";

    /**
     * 废品列表
     */
    String WASTE_LIST= "Api/Common/Waste/index";

    /**
     * 创建废品订单
     */
    String WASTE_ORDER = "Api/Common/Waste/create";

    /**
     * 废品全部订单
     */
    String WASTE_ORDER_ALL =  "Api/Common/Waste/all";

    /**
     * 废品未处理订单
     */
    String WASTE_ORDER_UNDEAL =  "Api/Common/Waste/undeal";

    /**
     * 废品已处理订单
     */
    String WASTE_ORDER_DEAL =  "Api/Common/Waste/deal";

    /**
     * 废品已取消订单
     */
    String WASTE_ORDER_CANCEL =  "Api/Common/Waste/cancel";

    /**
     * 废品信息
     */
    String  WASTE_ORDER_INFO = "Api/Common/Waste/info";

    /**
     * 废品图片信息
     */
    String WASTE_IMAGE_INFO = "Api/Common/Waste/imagesInfo";

    /**
     * 取消订单
     */
    String WASTE_CANCAL_ORDER = "Api/Common/Waste/delOrder";

    /**
     * 获取版本信息
     */
    String APP_VERSION_INFO = "Api/User/Index/get_version_info";

    /**
     *   添加用户坐标
     */
    String ADD_COORDINATE = "Api/User/Coordinate/add";

    /**
     *  获取其他用户的坐标
     */
    String INDEX_COORDINATE = "Api/User/Coordinate/index";

    /**
     *  获取两个聊天用户的信息
     */
    String GET_ROOM_USER_INFO = "Api/User/Chat/get_room_user_info";

    /**
     * 附近的人聊天信息发送
     */
    String CHAT_SEND_CONTENT = "Api/User/Chat/sendMessage";

    /**
     * 创建和获取房间信息房间号
     */
    String CREATE_GET_ROOM = "Api/User/Chat/getRoom";

    /**
     * 根据房号获取对应的信息
     */
    String GET_CHAT_MESSAGE = "Api/User/Chat/getAllChatMessage";

    /**
     *  获取用户对应的所有房间
     */
    String ROOM_LIST = "Api/User/Chat/roomList";

    /**
     *  获取用户对应的能看到的设备监控
     */
    String VIDEO_DEVICE_LIST = "Api/User/Video/deviceList";
}
