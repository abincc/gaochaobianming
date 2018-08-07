package com.cnsunrun.jiajiagou.personal.logistics;

import com.cnsunrun.jiajiagou.common.base.BaseResp;

import java.util.List;

/**
 * Created by ${LiuDi}
 * on 2017/9/1on 16:26.
 */

public class LittleHelpResp extends BaseResp
{

    /**
     * status : 1
     * info : [{"trifle_id":"19",
     * "content":"啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊阿啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊阿啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊阿啊啊啊啊啊啊啊啊阿啊啊",
     * "status":"2","add_time":"2017-08-30 15:36:05","trifle_service_title":"水管维修","status_title":"已处理",
     * "image":"http://test.cnsunrun.com/wuye/Uploads/Trifle/20170830/59a66ae533ad4.png"},{"trifle_id":"18",
     * "content":"啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊阿啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊阿啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊阿啊啊啊啊啊啊啊啊阿啊啊",
     * "status":"2","add_time":"2017-08-30 15:35:41","trifle_service_title":"水管维修","status_title":"已处理",
     * "image":"http://test.cnsunrun.com/wuye/Uploads/Trifle/20170830/59a66acdb0a29.png"}]
     */

    private List<HelpBean> info;

    public List<HelpBean> getInfo()
    {
        return info;
    }

    public void setInfo(List<HelpBean> info)
    {
        this.info = info;
    }

}
