package com.cnsunrun.jiajiagou.personal;

import com.cnsunrun.jiajiagou.common.base.BaseResp;

import java.util.List;

/**
 * Created by ${LiuDi}
 * on 2017/11/3on 16:36.
 */

public class CouponResp extends BaseResp
{

    /**
     * status : 1
     * info : [{"card_id":"8","status":"1","title":"10元代金劵(废品回收赠送)","description":"废品回收赠送废品回收","amount":"10.00",
     * "begin_date":"2017-11-01","end_date":"2017-11-29","add_time":"2017-11-03 16:33:06","status_title":"未使用"}]
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
         * card_id : 8
         * status : 1
         * title : 10元代金劵(废品回收赠送)
         * description : 废品回收赠送废品回收
         * amount : 10.00
         * begin_date : 2017-11-01
         * end_date : 2017-11-29
         * add_time : 2017-11-03 16:33:06
         * status_title : 未使用
         */

        public String card_id;
        public int status;
        public String title;
        public String description;
        public String amount;
        public String begin_date;
        public String end_date;
        public String add_time;
        public String status_title;
        public String minimum_title;
        public String minimum;
    }
}
