package com.cnsunrun.jiajiagou.forum.bean;

import com.cnsunrun.jiajiagou.common.base.BaseResp;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-08-29 12:51
 */
public class SendCodeBean extends BaseResp
{


    /**
     * status : 1
     * msg : 发送成功
     * info : 2221
     */
    private String info;


    public String getInfo()
    {
        return info;
    }

    public void setInfo(String info)
    {
        this.info = info;
    }
}
