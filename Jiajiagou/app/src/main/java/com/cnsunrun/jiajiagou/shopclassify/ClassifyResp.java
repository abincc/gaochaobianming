package com.cnsunrun.jiajiagou.shopclassify;

import com.cnsunrun.jiajiagou.common.base.BaseResp;

import java.util.List;

/**
 * Created by ${LiuDi}
 * on 2017/8/29on 9:34.
 */

public class ClassifyResp extends BaseResp
{
    /**
     * status : 1
     * info : [{"pid":"0","title":"进口食品","icon":"","categroy_id":"1","_child":[{"pid":"1","title":"饼干蛋糕","icon":"",
     * "categroy_id":"2"},{"pid":"1","title":"糖果/巧克力","icon":"","categroy_id":"3"},{"pid":"1","title":"休闲零食",
     * "icon":"","categroy_id":"4"},{"pid":"1","title":"冲调饮料","icon":"","categroy_id":"5"},{"pid":"1","title":"粮油调味",
     * "icon":"","categroy_id":"6"},{"pid":"1","title":"牛奶","icon":"","categroy_id":"7"}]},{"pid":"0","title":"休闲食品",
     * "icon":"","categroy_id":"8","_child":[{"pid":"8","title":"休闲零食","icon":"","categroy_id":"9"},{"pid":"8",
     * "title":"坚果炒货","icon":"","categroy_id":"10"},{"pid":"8","title":"肉干肉脯","icon":"","categroy_id":"11"},
     * {"pid":"8","title":"蜜饯果干","icon":"","categroy_id":"12"},{"pid":"8","title":"无糖食品","icon":"",
     * "categroy_id":"13"}]},{"pid":"0","title":"女装","icon":"","categroy_id":"14","_child":[{"pid":"14","title":"T恤",
     * "icon":"","categroy_id":"15"}]}]
     */

    private List<InfoBean> info;

    public List<InfoBean> getInfo()
    {
        return info;
    }

    public void setInfo(List<InfoBean> info)
    {
        this.info = info;
    }

    public static class InfoBean
    {
        /**
         * pid : 0
         * title : 进口食品
         * icon :
         * categroy_id : 1
         * _child : [{"pid":"1","title":"饼干蛋糕","icon":"","categroy_id":"2"},{"pid":"1","title":"糖果/巧克力","icon":"",
         * "categroy_id":"3"},{"pid":"1","title":"休闲零食","icon":"","categroy_id":"4"},{"pid":"1","title":"冲调饮料",
         * "icon":"","categroy_id":"5"},{"pid":"1","title":"粮油调味","icon":"","categroy_id":"6"},{"pid":"1",
         * "title":"牛奶","icon":"","categroy_id":"7"}]
         */

        private String pid;
        private String title;
        private String icon;
        private String categroy_id;
        private List<ChildBean> child;

        public String getPid()
        {
            return pid;
        }

        public void setPid(String pid)
        {
            this.pid = pid;
        }

        public String getTitle()
        {
            return title;
        }

        public void setTitle(String title)
        {
            this.title = title;
        }

        public String getIcon()
        {
            return icon;
        }

        public void setIcon(String icon)
        {
            this.icon = icon;
        }

        public String getCategroy_id()
        {
            return categroy_id;
        }

        public void setCategroy_id(String categroy_id)
        {
            this.categroy_id = categroy_id;
        }

        public List<ChildBean> get_child()
        {
            return child;
        }

        public void set_child(List<ChildBean> _child)
        {
            this.child = _child;
        }

        public static class ChildBean
        {
            /**
             * pid : 1
             * title : 饼干蛋糕
             * icon :
             * categroy_id : 2
             */

            private String pid;
            private String title;
            private String icon;
            private String categroy_id;

            public String getPid()
            {
                return pid;
            }

            public void setPid(String pid)
            {
                this.pid = pid;
            }

            public String getTitle()
            {
                return title;
            }

            public void setTitle(String title)
            {
                this.title = title;
            }

            public String getIcon()
            {
                return icon;
            }

            public void setIcon(String icon)
            {
                this.icon = icon;
            }

            public String getCategroy_id()
            {
                return categroy_id;
            }

            public void setCategroy_id(String categroy_id)
            {
                this.categroy_id = categroy_id;
            }
        }
    }
}
