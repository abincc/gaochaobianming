package com.cnsunrun.jiajiagou.personal;

import com.cnsunrun.jiajiagou.common.base.BaseResp;
import com.cnsunrun.jiajiagou.home.bean.NoticeBean;

import java.util.List;

/**
 * Created by ${LiuDi}
 * on 2017/9/1on 14:21.
 */

public class NoticeResp extends BaseResp
{
    /**
     * status : 1
     * info : [{"notice_id":"5","title":"尚软科技是一家互联网科技公司","add_time":"2017-08-30 17:44:43"},{"notice_id":"4",
     * "title":"阿萨德行按时窗体","add_time":"2017-08-30 17:34:44"},{"notice_id":"3","title":"asdasd","add_time":"2017-08-30
     * 17:34:13"},{"notice_id":"2","title":"aaaaaaaaaaaaaa","add_time":"2017-08-30 17:34:04"},{"notice_id":"1",
     * "title":"cccccccccccccczzzzzzzzzzz","add_time":"2017-08-30 17:30:31"}]
     */

    private List<NoticeBean> info;

    public List<NoticeBean> getInfo()
    {
        return info;
    }

    public void setInfo(List<NoticeBean> info)
    {
        this.info = info;
    }


}
