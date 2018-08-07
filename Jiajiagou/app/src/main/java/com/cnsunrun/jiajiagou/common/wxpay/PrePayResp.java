package com.cnsunrun.jiajiagou.common.wxpay;

import com.cnsunrun.jiajiagou.common.base.BaseResp;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ${LiuDi}
 * on 2017/9/27on 16:53.
 */

public class PrePayResp extends BaseResp
{

    /**
     * status : 1
     * info : {"appId":"wx5b6158570eb80fe3","nonceStr":"fud9m64g588soxzo2bu9fgprwhkktg3f","package":"Sign=WXPay",
     * "prepayid":"wx2017092809363145e0fb5ea00494053051","signType":"MD5",
     * "paySign":"0AF28621EDCA54EE05A3B5CD27A8C208","timestamp":"1506562585"}
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
         * appId : wx5b6158570eb80fe3
         * nonceStr : fud9m64g588soxzo2bu9fgprwhkktg3f
         * package : Sign=WXPay
         * prepayid : wx2017092809363145e0fb5ea00494053051
         * signType : MD5
         * paySign : 0AF28621EDCA54EE05A3B5CD27A8C208
         * timestamp : 1506562585
         */

        private String appid;
        private String noncestr;

        public String getNoncestr()
        {
            return noncestr;
        }

        public void setNoncestr(String noncestr)
        {
            this.noncestr = noncestr;
        }

        @SerializedName("package")
        private String packageX;
        private String prepayid;
        private String signType;
        private String sign;
        private String partnerid;

        public String getAppid()
        {
            return appid;
        }

        public void setAppid(String appid)
        {
            this.appid = appid;
        }

        public String getPartnerid()
        {
            return partnerid;
        }

        public void setPartnerid(String partnerid)
        {
            this.partnerid = partnerid;
        }

        public String getSign()
        {
            return sign;
        }

        public void setSign(String sign)
        {
            this.sign = sign;
        }

        private String timestamp;
        public String order_id;
        public String money;

        public String getPackageX()
        {
            return packageX;
        }

        public void setPackageX(String packageX)
        {
            this.packageX = packageX;
        }

        public String getPrepayid()
        {
            return prepayid;
        }

        public void setPrepayid(String prepayid)
        {
            this.prepayid = prepayid;
        }

        public String getSignType()
        {
            return signType;
        }

        public void setSignType(String signType)
        {
            this.signType = signType;
        }


        public String getTimestamp()
        {
            return timestamp;
        }

        public void setTimestamp(String timestamp)
        {
            this.timestamp = timestamp;
        }
    }
}
