package com.cnsunrun.jiajiagou.personal.setting.address;

import com.cnsunrun.jiajiagou.common.base.BaseResp;

import java.util.List;

/**
 * Created by ${LiuDi}
 * on 2017/8/31on 10:23.
 */

public class AddAddressResp extends BaseResp
{

    /**
     * status : 1
     * info : [{"id":"506","pid":"505","title":"新抚区","level":"2","path":"-0-471-505-","first_char":"X",
     * "pinyin":"xinfuqu"},{"id":"507","pid":"505","title":"东洲区","level":"2","path":"-0-471-505-","first_char":"D",
     * "pinyin":"dongzhouqu"}]
     */

    private List<AreaBean> info;

    public List<AreaBean> getInfo()
    {
        return info;
    }

    public void setInfo(List<AreaBean> info)
    {
        this.info = info;
    }


}
