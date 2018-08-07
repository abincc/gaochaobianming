package com.cnsunrun.jiajiagou.forum.bean;

import java.util.List;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-09-01 14:10
 */
public class ForumPlateListBean {

    /**
     * status : 1
     * msg : ok
     * info : [{"id":"4","title":"谈天说地","forum_list":[{"id":"7","title":"灌者为王",
     * "icon":"http://test.cnsunrun.com/wuye/Uploads/Forum/Forum/599ab56116b19.png"},{"id":"5",
     * "title":"贴图自拍","icon":"http://test.cnsunrun
     * .com/wuye/Uploads/Forum/Forum/5995736f83bf9.png"}]},{"id":"1","title":"默认分区",
     * "forum_list":[{"id":"2","title":"默认版块","icon":""}]}]
     */

    private int status;
    private String msg;
    private List<InfoBean> info;

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

    public List<InfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<InfoBean> info) {
        this.info = info;
    }

    public static class InfoBean {
        /**
         * id : 4
         * title : 谈天说地
         * forum_list : [{"id":"7","title":"灌者为王","icon":"http://test.cnsunrun
         * .com/wuye/Uploads/Forum/Forum/599ab56116b19.png"},{"id":"5","title":"贴图自拍",
         * "icon":"http://test.cnsunrun.com/wuye/Uploads/Forum/Forum/5995736f83bf9.png"}]
         */

        private String id;
        private String title;
        private List<ForumListBean> forum_list;
        private boolean isSelect=false;//记录点击的item

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

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

        public List<ForumListBean> getForum_list() {
            return forum_list;
        }

        public void setForum_list(List<ForumListBean> forum_list) {
            this.forum_list = forum_list;
        }

        public static class ForumListBean {
            /**
             * id : 7
             * title : 灌者为王
             * icon : http://test.cnsunrun.com/wuye/Uploads/Forum/Forum/599ab56116b19.png
             */

            private String id;
            private String title;
            private String icon;

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
        }
    }
}
