package com.cnsunrun.jiajiagou.product;

import com.cnsunrun.jiajiagou.common.base.BaseResp;

import java.util.List;

/**
 * Created by ${LiuDi}
 * on 2017/8/31on 9:55.
 */

public class ProductResp extends BaseResp
{

    /**
     * status : 1
     * info : [{"product_id":"2","title":"饼干大将克力架","image":"http://test.cnsunrun
     * .com/wuye/Uploads/Product/Product/Cover/2017-08-22/599be9e95a06f.png","price":"1888.00","description":"夏洛特
     * .克力架"},{"product_id":"3","title":"恶魔果实","image":"http://test.cnsunrun
     * .com/wuye/Uploads/Product/Product/Cover/2017-08-22/599beace80a0a.png","price":"6000.00","description":"弹弹果实"},
     * {"product_id":"4","title":"咖啡奶茶","image":"http://test.cnsunrun
     * .com/wuye/Uploads/Product/Product/Cover/2017-08-22/599beb6ef0bb4.jpg","price":"16.00","description":"咖啡奶茶"},
     * {"product_id":"5","title":"麻辣冰淇淋","image":"http://test.cnsunrun
     * .com/wuye/Uploads/Product/Product/Cover/2017-08-22/599bec223a2d1.jpg","price":"12.00","description":"麻辣冰淇淋"},
     * {"product_id":"6","title":"火雨奶茶","image":"http://test.cnsunrun
     * .com/wuye/Uploads/Product/Product/Cover/2017-08-22/599becf0cba39.jpg","price":"18.00","description":"火雨奶茶"},
     * {"product_id":"7","title":"鼠粮","image":"http://test.cnsunrun
     * .com/wuye/Uploads/Product/Product/Cover/2017-08-22/599beea3049fb.jpg","price":"200.00","description":"鼠粮"}]
     */

    private List<ProductBean> info;

    public List<ProductBean> getInfo()
    {
        return info;
    }

    public void setInfo(List<ProductBean> info)
    {
        this.info = info;
    }


}
