package com.cnsunrun.jiajiagou.personal;

import com.cnsunrun.jiajiagou.common.base.BaseResp;

/**
 * Created by ${LiuDi}
 * on 2017/9/1on 14:45.
 */

public class NoticeDetailResp extends BaseResp
{
    /**
     * status : 1
     * info : {"notice_id":"2","title":"aaaaaaaaaaaaaa","content":"<p>cccccccccccccccccccccccccc<\/p>",
     * "add_time":"2017-08-30 17:34:04"}
     */

    private InfoBean info;

    public InfoBean getInfo()
    {
        return info;
    }

    public void setInfo(InfoBean info)
    {
        this.info = info;
    }

    public static class InfoBean
    {
        /**
         * notice_id : 2
         * title : aaaaaaaaaaaaaa
         * content : <p>cccccccccccccccccccccccccc</p>
         * add_time : 2017-08-30 17:34:04
         */

        private String notice_id;
        private String title;
        private String content;
        private String add_time;

        public String getNotice_id()
        {
            return notice_id;
        }

        public void setNotice_id(String notice_id)
        {
            this.notice_id = notice_id;
        }

        public String getTitle()
        {
            return title;
        }

        public void setTitle(String title)
        {
            this.title = title;
        }

        public String getContent()
        {
            return content;
        }

        public void setContent(String content)
        {
            this.content = content;
        }

        public String getAdd_time()
        {
            return add_time;
        }

        public void setAdd_time(String add_time)
        {
            this.add_time = add_time;
        }
    }
}
