package com.cnsunrun.jiajiagou.personal.logistics;

import com.cnsunrun.jiajiagou.common.base.BaseResp;

import java.util.List;

/**
 * Created by ${LiuDi}
 * on 2017/9/9on 10:03.
 */

public class CleaningResp extends BaseResp
{
    /**
     * status : 1
     * info : [{"clean_id":"14","content":"大圣，你脸肿了1111","add_time":"2017-08-29 18:30:24",
     * "clean_service_title":"水管维修","status_title":"待处理","image":"http://test.cnsunrun
     * .com/wuye/Uploads/Clean/20170829/59a542400fb63.png"},{"clean_id":"13","content":"大圣，你脸肿了1111",
     * "add_time":"2017-08-29 18:30:22","clean_service_title":"水管维修","status_title":"待处理","image":"http://test
     * .cnsunrun.com/wuye/Uploads/Clean/20170829/59a5423e76ddd.png"}]
     */

    private List<CleanBean> info;

    public List<CleanBean> getInfo()
    {
        return info;
    }

    public void setInfo(List<CleanBean> info)
    {
        this.info = info;
    }


}
