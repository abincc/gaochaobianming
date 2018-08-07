package com.cnsunrun.jiajiagou.home.homepage;

import com.cnsunrun.jiajiagou.common.base.BaseResp;
import com.cnsunrun.jiajiagou.home.bean.NoticeBean;

import java.util.List;

/**
 * Created by ${LiuDi}
 * on 2017/8/31on 11:23.
 */

public class HomeResp extends BaseResp
{

    /**
     * status : 1
     * info : {"banner":[{"adspace_id":"1","image":"http://test.cnsunrun
     * .com/wuye/Uploads/Banner/59a6143132dce.png"},{"adspace_id":"1","image":"http://test
     * .cnsunrun.com/wuye/Uploads/Banner/59a6141a0846a.png"},{"adspace_id":"1",
     * "image":"http://test.cnsunrun.com/wuye/Uploads/Banner/59a6144180ee6.png"}],
     * "recommend":[{"product_id":"11","title":"桃子","image":"http://test.cnsunrun
     * .com/wuye/Uploads/Product/Product/Cover/2017-10-17/59e58ec9abf7d.jpg","price":"5.00",
     * "description":""},{"product_id":"13","title":"石榴","image":"http://test.cnsunrun
     * .com/wuye/Uploads/Product/Product/Cover/2017-10-17/59e58f75a4445.jpg","price":"8.00",
     * "description":""},{"product_id":"19","title":"香蕉","image":"http://test.cnsunrun
     * .com/wuye/Uploads/Product/Product/Cover/2017-10-17/59e590e881bab.jpg","price":"3.50",
     * "description":""},{"product_id":"20","title":"提子","image":"http://test.cnsunrun
     * .com/wuye/Uploads/Product/Product/Cover/2017-10-17/59e5911e13f1b.jpg","price":"4.80",
     * "description":""},{"product_id":"22","title":"榴莲","image":"http://test.cnsunrun
     * .com/wuye/Uploads/Product/Product/Cover/2017-10-17/59e5915c4d84e.jpg","price":"8.00",
     * "description":""},{"product_id":"23","title":"猕猴桃","image":"http://test.cnsunrun
     * .com/wuye/Uploads/Product/Product/Cover/2017-10-17/59e5918f08ec3.jpg","price":"5.00",
     * "description":""},{"product_id":"24","title":"芒果","image":"http://test.cnsunrun
     * .com/wuye/Uploads/Product/Product/Cover/2017-10-17/59e591c352e66.jpg","price":"5.50",
     * "description":""},{"product_id":"25","title":"草莓","image":"http://test.cnsunrun
     * .com/wuye/Uploads/Product/Product/Cover/2017-10-17/59e591f1612ea.jpg","price":"8.00",
     * "description":"草莓吃了好，吃了打老虎！！"}],"notic":[{"notice_id":"6","title":"高超网络便民服务平台使用说明"},
     * {"notice_id":"5","title":"高超网络是一家互联网科技公司"}],"forum":[{"forum_title":"好孩纸模块",
     * "image":"http://test.cnsunrun.com/wuye/Uploads/Banner/59a6147dc9665.png","forum_id":"1"},
     * {"forum_title":"社区活动模块","image":"http://test.cnsunrun
     * .com/wuye/Uploads/Banner/59d2f4a1e79be.jpg","forum_id":"2"},{"forum_title":"代收快递",
     * "image":"http://test.cnsunrun.com/wuye/Uploads/Banner/59deda8421cf7.png","forum_id":"5"}],
     * "property":[{"property_title":"小事帮忙","image":"http://test.cnsunrun
     * .com/wuye/Uploads/Banner/59a614a8dfed1.png","property_id":"1"},{"property_title":"保洁维修",
     * "image":"http://test.cnsunrun.com/wuye/Uploads/Banner/59a614b41bd4f.png",
     * "property_id":"2"},{"property_title":"便民服务","image":"http://test.cnsunrun
     * .com/wuye/Uploads/Banner/59a614c1e7446.png","property_id":"3"}],
     * "bianmin":[{"bianmin_title":"健康档案","image":"http://test.cnsunrun
     * .com/wuye/Uploads/Banner/5a16a520bed5a.png","bianmin_id":""},{"bianmin_title":"社区志愿者",
     * "image":"http://test.cnsunrun.com/wuye/Uploads/Banner/5a16a53c3ae6a.png","bianmin_id":""},
     * {"bianmin_title":"万能通讯录","image":"http://test.cnsunrun
     * .com/wuye/Uploads/Banner/5a16a54f7d1ca.png","bianmin_id":""}],"luntan_list":[{"id":"192",
     * "member_id":"64","member_nickname":"论坛管理员","title":"海鲜螃蟹粥","views":"0","replies":"0",
     * "likes":"0","add_time":"2017-11-03 11:35","avatar":"http://test.cnsunrun
     * .com/wuye/Public/Static/img/avatar_M.jpg?time=1511491878","image":"http://test.cnsunrun
     * .com/wuye/Uploads/Forum/ForumThread/2017-11-03/59fbe41017cb7.jpg","width":"500",
     * "height":"367"},{"id":"194","member_id":"64","member_nickname":"论坛管理员",
     * "title":"抗癌佳品，罗汉果雪梨汤。","views":"0","replies":"0","likes":"0","add_time":"2017-11-03
     * 11:55","avatar":"http://test.cnsunrun.com/wuye/Public/Static/img/avatar_M
     * .jpg?time=1511491878","image":"http://test.cnsunrun
     * .com/wuye/Uploads/Forum/ForumThread/2017-11-03/59fbe8b64ebef.jpg","width":"373",
     * "height":"230"},{"id":"185","member_id":"64","member_nickname":"肖琼","title":"丝瓜排骨汤",
     * "views":"7","replies":"0","likes":"0","add_time":"2017-11-02 08:40","avatar":"http://test
     * .cnsunrun.com/wuye/Public/Static/img/avatar_M.jpg?time=1511491878","image":"http://test
     * .cnsunrun.com/wuye/Uploads/Forum/ForumThread/2017-11-02/59fa6994e57fd.jpg","width":"290",
     * "height":"377"},{"id":"184","member_id":"64","member_nickname":"肖琼","title":"雪梨百合山药粥",
     * "views":"3","replies":"0","likes":"0","add_time":"2017-11-02 08:34","avatar":"http://test
     * .cnsunrun.com/wuye/Public/Static/img/avatar_M.jpg?time=1511491878","image":"http://test
     * .cnsunrun.com/wuye/Uploads/Forum/ForumThread/2017-11-02/59fa6823aeae1.jpg","width":"318",
     * "height":"296"},{"id":"181","member_id":"67","member_nickname":"音音","title":"滋补鸽子汤",
     * "views":"18","replies":"0","likes":"1","add_time":"2017-10-31 11:00","avatar":"http://test
     * .cnsunrun.com/wuye/Public/Static/img/avatar_M.jpg?time=1511491878","image":"http://test
     * .cnsunrun.com/wuye/Uploads/Forum/ForumThread/2017-10-31/59f7e7500dac9.jpg","width":"401",
     * "height":"257"}]}
     */

    private InfoBean info;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class     InfoBean {
        private List<BannerBean> banner;
        private Object recommend;
        private List<NoticeBean> notic;
        private List<ForumBean> forum;
        private List<PropertyBean> property;
        private List<BianminBean> bianmin;
        private List<Entry> entry;
        private List<LuntanListBean> luntan_list;

        public List<Entry> getEntry() {
            return entry;
        }

        public void setEntry(List<Entry> entry) {
            this.entry = entry;
        }

        public List<BannerBean> getBanner() {
            return banner;
        }

        public void setBanner(List<BannerBean> banner) {
            this.banner = banner;
        }


        public List<NoticeBean> getNotic() {
            return notic;
        }

        public void setNotic(List<NoticeBean> notic) {
            this.notic = notic;
        }

        public List<ForumBean> getForum() {
            return forum;
        }

        public void setForum(List<ForumBean> forum) {
            this.forum = forum;
        }

        public List<PropertyBean> getProperty() {
            return property;
        }

        public void setProperty(List<PropertyBean> property) {
            this.property = property;
        }

        public List<BianminBean> getBianmin() {
            return bianmin;
        }

        public void setBianmin(List<BianminBean> bianmin) {
            this.bianmin = bianmin;
        }

        public List<LuntanListBean> getLuntan_list() {
            return luntan_list;
        }

        public void setLuntan_list(List<LuntanListBean> luntan_list) {
            this.luntan_list = luntan_list;
        }

        public static class BannerBean {
            /**
             * adspace_id : 1
             * image : http://test.cnsunrun.com/wuye/Uploads/Banner/59a6143132dce.png
             */

            private String adspace_id;
            private String image;

            public String getAdspace_id() {
                return adspace_id;
            }

            public void setAdspace_id(String adspace_id) {
                this.adspace_id = adspace_id;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }
        }



        public static class ForumBean {
            /**
             * forum_title : 好孩纸模块
             * image : http://test.cnsunrun.com/wuye/Uploads/Banner/59a6147dc9665.png
             * forum_id : 1
             */

            private String forum_title;
            private String image;
            private String forum_id;

            public String getForum_title() {
                return forum_title;
            }

            public void setForum_title(String forum_title) {
                this.forum_title = forum_title;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getForum_id() {
                return forum_id;
            }

            public void setForum_id(String forum_id) {
                this.forum_id = forum_id;
            }
        }

        public static class PropertyBean {
            /**
             * property_title : 小事帮忙
             * image : http://test.cnsunrun.com/wuye/Uploads/Banner/59a614a8dfed1.png
             * property_id : 1
             */

            private String property_title;
            private String image;
            private String property_id;

            public String getProperty_title() {
                return property_title;
            }

            public void setProperty_title(String property_title) {
                this.property_title = property_title;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getProperty_id() {
                return property_id;
            }

            public void setProperty_id(String property_id) {
                this.property_id = property_id;
            }
        }

        public static class BianminBean {
            /**
             * bianmin_title : 健康档案
             * image : http://test.cnsunrun.com/wuye/Uploads/Banner/5a16a520bed5a.png
             * bianmin_id :
             */

            private String bianmin_title;
            private String image;
            private String bianmin_id;

            public String getBianmin_title() {
                return bianmin_title;
            }

            public void setBianmin_title(String bianmin_title) {
                this.bianmin_title = bianmin_title;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getBianmin_id() {
                return bianmin_id;
            }

            public void setBianmin_id(String bianmin_id) {
                this.bianmin_id = bianmin_id;
            }
        }

        public static class Entry{
            private String title;
            private String image;
            private String entry_id;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getEntry_id() {
                return entry_id;
            }

            public void setEntry_id(String entry_id) {
                this.entry_id = entry_id;
            }
        }

        public static class LuntanListBean {
            /**
             * id : 192
             * member_id : 64
             * member_nickname : 论坛管理员
             * title : 海鲜螃蟹粥
             * views : 0
             * replies : 0
             * likes : 0
             * add_time : 2017-11-03 11:35
             * avatar : http://test.cnsunrun.com/wuye/Public/Static/img/avatar_M.jpg?time=1511491878
             * image : http://test.cnsunrun.com/wuye/Uploads/Forum/ForumThread/2017-11-03/59fbe41017cb7.jpg
             * width : 500
             * height : 367
             */

            private String id;
            private String member_id;
            private String member_nickname;
            private String title;
            private String views;
            private String replies;
            private String likes;
            private String add_time;
            private String avatar;
            private String image;
            private String width;
            private String height;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getMember_id() {
                return member_id;
            }

            public void setMember_id(String member_id) {
                this.member_id = member_id;
            }

            public String getMember_nickname() {
                return member_nickname;
            }

            public void setMember_nickname(String member_nickname) {
                this.member_nickname = member_nickname;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getViews() {
                return views;
            }

            public void setViews(String views) {
                this.views = views;
            }

            public String getReplies() {
                return replies;
            }

            public void setReplies(String replies) {
                this.replies = replies;
            }

            public String getLikes() {
                return likes;
            }

            public void setLikes(String likes) {
                this.likes = likes;
            }

            public String getAdd_time() {
                return add_time;
            }

            public void setAdd_time(String add_time) {
                this.add_time = add_time;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getWidth() {
                return width;
            }

            public void setWidth(String width) {
                this.width = width;
            }

            public String getHeight() {
                return height;
            }

            public void setHeight(String height) {
                this.height = height;
            }
        }
    }
}
