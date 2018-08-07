package com.cnsunrun.jiajiagou.personal.setting.address;

import com.cnsunrun.jiajiagou.common.base.BaseResp;

import java.util.List;

/**
 * Created by ${LiuDi}
 * on 2017/8/31on 15:55.
 */

public class AddressResp extends BaseResp
{

    /**
     * status : 1
     * info : [{"address_id":"49","name":"陆龙飞aa","mobile":"13377850432","province":"1709","city":"1710",
     * "area":"1711","address_detail":"sdsd","is_default":"1","province_title":"湖北省","city_title":"武汉市",
     * "area_title":"江岸区"}]
     */

    private List<AddressBean> info;

    public List<AddressBean> getInfo()
    {
        return info;
    }

    public void setInfo(List<AddressBean> info)
    {
        this.info = info;
    }


}
