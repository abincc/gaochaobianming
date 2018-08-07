package com.cnsunrun.jiajiagou.home;

import com.cnsunrun.jiajiagou.common.base.BaseResp;

import java.util.List;

/**
 * Created by ${LiuDi}
 * on 2017/8/31on 15:04.
 */

public class ConvenienceResp extends BaseResp
{

    /**
     * status : 1
     * info : [{"title":"家政服务","service":"保洁，维修","contact_number":"15800000000","image":"http://test.cnsunrun
     * .com/wuye/Uploads/Property/BianMin/599ea4e140a13.png","bianmin_id":"1"}]
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
         * title : 家政服务
         * service : 保洁，维修
         * contact_number : 15800000000
         * image : http://test.cnsunrun.com/wuye/Uploads/Property/BianMin/599ea4e140a13.png
         * bianmin_id : 1
         */

        private String title;
        private String service;
        private String contact_number;
        private String image;
        private String bianmin_id;

        public String getTitle()
        {
            return title;
        }

        public void setTitle(String title)
        {
            this.title = title;
        }

        public String getService()
        {
            return service;
        }

        public void setService(String service)
        {
            this.service = service;
        }

        public String getContact_number()
        {
            return contact_number;
        }

        public void setContact_number(String contact_number)
        {
            this.contact_number = contact_number;
        }

        public String getImage()
        {
            return image;
        }

        public void setImage(String image)
        {
            this.image = image;
        }

        public String getBianmin_id()
        {
            return bianmin_id;
        }

        public void setBianmin_id(String bianmin_id)
        {
            this.bianmin_id = bianmin_id;
        }
    }
}
