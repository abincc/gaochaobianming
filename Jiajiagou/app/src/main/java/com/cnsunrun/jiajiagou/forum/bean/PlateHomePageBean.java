package com.cnsunrun.jiajiagou.forum.bean;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-08-31 13:09
 */
public class PlateHomePageBean {

    /**
     * status : 1
     * msg : ok
     * info : {"id":"7","title":"灌者为王","icon":"http://test.cnsunrun
     * .com/wuye/Uploads/Forum/Forum/599ab56116b19.png","posts":"5"}
     */

    private int status;
    private String msg;
    private InfoBean info;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean {
        /**
         * id : 7
         * title : 灌者为王
         * icon : http://test.cnsunrun.com/wuye/Uploads/Forum/Forum/599ab56116b19.png
         * posts : 5
         */

        private String id;
        private String title;
        private String icon;
        private String posts;

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

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getPosts() {
            return posts;
        }

        public void setPosts(String posts) {
            this.posts = posts;
        }
    }
}
