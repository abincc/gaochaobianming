package com.cnsunrun.jiajiagou.forum.bean;

import com.cnsunrun.jiajiagou.common.base.BaseResp;

/**
 * Created by j2yyc on 2018/1/29.
 */

public class SharePostsBean extends BaseResp{

    /**
     * status : 1
     * msg : ok
     * info : {"id":"192","title":"海鲜螃蟹粥",
     * "content":"螃蟹含有丰富的蛋白质、微量元素等营养，对身体有很好的滋补作用。近年来研究发现，螃蟹还有抗结核作用，吃蟹对结核病的康复大有补益。一般认为，药用以淡水蟹为好，海水蟹只可供食用。中医认为螃蟹有清热解毒、补骨添髓、养筋活血、通经络、利肢节、续绝伤、滋肝阴、充胃液之功效。对于淤血、损伤、黄疸、腰腿酸痛和风湿性关节炎等疾病有一定的食疗效果,今天小编就给大家推荐一款海鲜螃蟹粥。\n \n食材\n主料：螃蟹1只、大米120克\n辅料：姜6克、盐、小香葱1根、胡椒粉\n步骤：\n1.大米，葱姜，螃蟹备好\n2.先把大米浸泡半个小时，然后淘洗干净，这样可以缩短煮的时间\n3.淘洗好的大米放入锅里，加入适量的水\n4.这个时候准备螃蟹，螃蟹清洗干净，去掉内脏，然后切成块，姜切丝，葱切碎备用\n5.启动煮饭程序\n6.粥煮好了，特别浓稠，已经完全熟的大米粥\n7.放入姜丝和螃蟹再煮5分钟\n8.撒上切好的小香葱，放入少许盐，搅拌一下撒点胡椒粉，盛碗里便可以吃了\n9.螃蟹粥特别香，粥里满满浸出来的蟹黄\n小贴士\n粥要煲的稠一点，比平时熬粥的米稍微多一点点\n放入螃蟹出锅时可以加点胡椒粉，根据个人的口味喜好吧","image":"http://test.cnsunrun.com/wuye/Uploads/Forum/ForumThread/2017-11-03/59fbe41017cb7.jpg","url":"http://test.cnsunrun.com/wuye/Home/Index/Index/thread_share_info/id/192.html"}
     */

    private InfoBean info;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean {
        /**
         * id : 192
         * title : 海鲜螃蟹粥
         * content :
         * 螃蟹含有丰富的蛋白质、微量元素等营养，对身体有很好的滋补作用。近年来研究发现，螃蟹还有抗结核作用，吃蟹对结核病的康复大有补益。一般认为，药用以淡水蟹为好，海水蟹只可供食用。中医认为螃蟹有清热解毒、补骨添髓、养筋活血、通经络、利肢节、续绝伤、滋肝阴、充胃液之功效。对于淤血、损伤、黄疸、腰腿酸痛和风湿性关节炎等疾病有一定的食疗效果,今天小编就给大家推荐一款海鲜螃蟹粥。
          
         食材
         主料：螃蟹1只、大米120克
         辅料：姜6克、盐、小香葱1根、胡椒粉
         步骤：
         1.大米，葱姜，螃蟹备好
         2.先把大米浸泡半个小时，然后淘洗干净，这样可以缩短煮的时间
         3.淘洗好的大米放入锅里，加入适量的水
         4.这个时候准备螃蟹，螃蟹清洗干净，去掉内脏，然后切成块，姜切丝，葱切碎备用
         5.启动煮饭程序
         6.粥煮好了，特别浓稠，已经完全熟的大米粥
         7.放入姜丝和螃蟹再煮5分钟
         8.撒上切好的小香葱，放入少许盐，搅拌一下撒点胡椒粉，盛碗里便可以吃了
         9.螃蟹粥特别香，粥里满满浸出来的蟹黄
         小贴士
         粥要煲的稠一点，比平时熬粥的米稍微多一点点
         放入螃蟹出锅时可以加点胡椒粉，根据个人的口味喜好吧
         * image : http://test.cnsunrun.com/wuye/Uploads/Forum/ForumThread/2017-11-03/59fbe41017cb7.jpg
         * url : http://test.cnsunrun.com/wuye/Home/Index/Index/thread_share_info/id/192.html
         */

        private String id;
        private String title;
        private String content;
        private String image;
        private String url;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
