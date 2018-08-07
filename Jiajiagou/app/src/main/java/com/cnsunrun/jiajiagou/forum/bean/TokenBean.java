package com.cnsunrun.jiajiagou.forum.bean;

import com.cnsunrun.jiajiagou.common.base.BaseResp;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-08-29 19:14
 */
public class TokenBean extends BaseResp
{


    /**
     * status : 1
     * msg : OK
     * info : {"token":"fnVyqrJ3dLCudXOuhHp1a4V1eaqDtpingnfJ2bKbzMl
     * -hZxpsoeEb651eHiEoHGugqpgqoWlZayEhqPdtIXQ2n91oKqynnSxsHVzrg","expires_in":"1800"}
     */


    private InfoBean info;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean {
        /**
         * token : fnVyqrJ3dLCudXOuhHp1a4V1eaqDtpingnfJ2bKbzMl
         * -hZxpsoeEb651eHiEoHGugqpgqoWlZayEhqPdtIXQ2n91oKqynnSxsHVzrg
         * expires_in : 1800
         */

        private String token;
        private String expires_in;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(String expires_in) {
            this.expires_in = expires_in;
        }
    }
}
