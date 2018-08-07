package com.cnsunrun.jiajiagou.personal;

import com.cnsunrun.jiajiagou.common.base.BaseResp;

import java.util.List;

/**
 * Created by ${LiuDi}
 * on 2017/8/30on 16:33.
 */

public class MyCollectionResp extends BaseResp

{


    /**
     * status : 1
     * info : [{"collect_id":"4","product_id":"5","add_time":"2017-08-23 09:59:27","title":"麻辣冰淇淋","price":"12.00",
     * "image":"http://test.cnsunrun.com/wuye/Uploads/Product/Product/Cover/2017-08-22/599bec223a2d1.jpg",
     * "description":"麻辣冰淇淋"},{"collect_id":"3","product_id":"2","add_time":"2017-08-23 09:58:52","title":"饼干大将克力架",
     * "price":"1888.00","image":"http://test.cnsunrun
     * .com/wuye/Uploads/Product/Product/Cover/2017-08-22/599be9e95a06f.png","description":"夏洛特.克力架"}]
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
         * collect_id : 4
         * product_id : 5
         * add_time : 2017-08-23 09:59:27
         * title : 麻辣冰淇淋
         * price : 12.00
         * image : http://test.cnsunrun.com/wuye/Uploads/Product/Product/Cover/2017-08-22/599bec223a2d1.jpg
         * description : 麻辣冰淇淋
         */

        public String collect_id;
        public String product_id;
        public String add_time;
        public String title;
        public String price;
        public String image;
        public String description;
        public boolean isChecked;
    }
}
