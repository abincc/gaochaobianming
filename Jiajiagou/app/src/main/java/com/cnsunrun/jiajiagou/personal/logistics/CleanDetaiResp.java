package com.cnsunrun.jiajiagou.personal.logistics;

import com.cnsunrun.jiajiagou.common.base.BaseResp;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ${LiuDi}
 * on 2017/9/9on 10:23.
 */

public class CleanDetaiResp extends BaseResp
{
    /**
     * status : 1
     * info : {"clean_id":"16","clean_service_id":"2",
     * "content":"啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊阿啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊阿啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊阿啊啊啊啊啊啊啊啊阿啊啊",
     * "status":"1","add_time":"2017-08-30 15:38:13","deal_time":"","clean_service_title":"水管维修",
     * "status_title":"待处理","images":[{"image_id":"16","image":"http://test.cnsunrun
     * .com/wuye/Uploads/Clean/20170830/59a66b655adf5.png"},{"image_id":"17","image":"http://test.cnsunrun
     * .com/wuye/Uploads/Clean/20170830/59a66b655b7c4.jpg"}]}
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
         * clean_id : 16
         * clean_service_id : 2
         * content : 啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊阿啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊阿啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊阿啊啊啊啊啊啊啊啊阿啊啊
         * status : 1
         * add_time : 2017-08-30 15:38:13
         * deal_time :
         * clean_service_title : 水管维修
         * status_title : 待处理
         * images : [{"image_id":"16","image":"http://test.cnsunrun
         * .com/wuye/Uploads/Clean/20170830/59a66b655adf5.png"},{"image_id":"17","image":"http://test.cnsunrun
         * .com/wuye/Uploads/Clean/20170830/59a66b655b7c4.jpg"}]
         */

        private String clean_id;
        private String clean_service_id;
        private String content;
        @SerializedName("status")
        private String statusX;
        private String add_time;
        private String deal_time;
        private String clean_service_title;
        private String status_title;
        private String is_reply;
        private String reply;
        private List<ImagesBean> images;
        private List<ReplyImagesBean> reply_images;

        public String getIs_reply() {
            return is_reply;
        }

        public void setIs_reply(String is_reply) {
            this.is_reply = is_reply;
        }

        public String getReply() {
            return reply;
        }

        public void setReply(String reply) {
            this.reply = reply;
        }

        public List<ReplyImagesBean> getReply_images() {
            return reply_images;
        }

        public void setReply_images(List<ReplyImagesBean> reply_images) {
            this.reply_images = reply_images;
        }

        public String getClean_id()
        {
            return clean_id;
        }

        public void setClean_id(String clean_id)
        {
            this.clean_id = clean_id;
        }

        public String getClean_service_id()
        {
            return clean_service_id;
        }

        public void setClean_service_id(String clean_service_id)
        {
            this.clean_service_id = clean_service_id;
        }

        public String getContent()
        {
            return content;
        }

        public void setContent(String content)
        {
            this.content = content;
        }

        public String getStatusX()
        {
            return statusX;
        }

        public void setStatusX(String statusX)
        {
            this.statusX = statusX;
        }

        public String getAdd_time()
        {
            return add_time;
        }

        public void setAdd_time(String add_time)
        {
            this.add_time = add_time;
        }

        public String getDeal_time()
        {
            return deal_time;
        }

        public void setDeal_time(String deal_time)
        {
            this.deal_time = deal_time;
        }

        public String getClean_service_title()
        {
            return clean_service_title;
        }

        public void setClean_service_title(String clean_service_title)
        {
            this.clean_service_title = clean_service_title;
        }

        public String getStatus_title()
        {
            return status_title;
        }

        public void setStatus_title(String status_title)
        {
            this.status_title = status_title;
        }

        public List<ImagesBean> getImages()
        {
            return images;
        }

        public void setImages(List<ImagesBean> images)
        {
            this.images = images;
        }

        public static class ImagesBean
        {
            /**
             * image_id : 16
             * image : http://test.cnsunrun.com/wuye/Uploads/Clean/20170830/59a66b655adf5.png
             */

            private String image_id;
            private String image;

            public String getImage_id()
            {
                return image_id;
            }

            public void setImage_id(String image_id)
            {
                this.image_id = image_id;
            }

            public String getImage()
            {
                return image;
            }

            public void setImage(String image)
            {
                this.image = image;
            }
        }
        public static class ReplyImagesBean {
            /**
             * image_id : 9
             * image : http://test.cnsunrun.com/wuye/Uploads/Cleanreply/20180126/5a6af99d092ff.jpg
             * width : 1080
             * height : 2040
             */

            private String image_id;
            private String image;
            private String width;
            private String height;

            public String getImage_id() {
                return image_id;
            }

            public void setImage_id(String image_id) {
                this.image_id = image_id;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getWidth() {
                return width;
            }

            public void setWidth(String width) {
                this.width = width;
            }

            public String getHeight() {
                return height;
            }

            public void setHeight(String height) {
                this.height = height;
            }
        }
    }
}
