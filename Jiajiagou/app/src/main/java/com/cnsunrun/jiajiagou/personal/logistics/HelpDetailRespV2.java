package com.cnsunrun.jiajiagou.personal.logistics;

import com.cnsunrun.jiajiagou.common.base.BaseResp;

import java.util.List;

/**
 * Created by ${LiuDi}
 * on 2017/9/9on 14:45.
 */

public class HelpDetailRespV2 extends BaseResp
{

    /**
     * status : 1
     * info : {"trifle_id":"44","trifle_service_id":"1","content":"敏敏破哦我破 Mr 你哦","status":"1","add_time":"2017-09-09
     * 14:34:46","building_no":"25","room_no":"555","deal_time":"","username":"lol","mobile":"13212781961",
     * "trifle_service_title":"代收快递","status_title":"待处理","images":[{"image_id":"54","image":"http://test.cnsunrun
     * .com/wuye/Uploads/Trifle/20170909/59b38b8688ae9.jpg"},{"image_id":"55","image":"http://test.cnsunrun
     * .com/wuye/Uploads/Trifle/20170909/59b38b86898f4.jpg"},{"image_id":"56","image":"http://test.cnsunrun
     * .com/wuye/Uploads/Trifle/20170909/59b38b868abcd.jpg"},{"image_id":"57","image":"http://test.cnsunrun
     * .com/wuye/Uploads/Trifle/20170909/59b38b868be34.jpg"},{"image_id":"58","image":"http://test.cnsunrun
     * .com/wuye/Uploads/Trifle/20170909/59b38b868d19c.jpg"}]}
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
         * trifle_id : 44
         * trifle_service_id : 1
         * content : 敏敏破哦我破 Mr 你哦
         * status : 1
         * add_time : 2017-09-09 14:34:46
         * building_no : 25
         * room_no : 555
         * deal_time :
         * username : lol
         * mobile : 13212781961
         * trifle_service_title : 代收快递
         * status_title : 待处理
         * images : [{"image_id":"54","image":"http://test.cnsunrun
         * .com/wuye/Uploads/Trifle/20170909/59b38b8688ae9.jpg"},{"image_id":"55","image":"http://test.cnsunrun
         * .com/wuye/Uploads/Trifle/20170909/59b38b86898f4.jpg"},{"image_id":"56","image":"http://test.cnsunrun
         * .com/wuye/Uploads/Trifle/20170909/59b38b868abcd.jpg"},{"image_id":"57","image":"http://test.cnsunrun
         * .com/wuye/Uploads/Trifle/20170909/59b38b868be34.jpg"},{"image_id":"58","image":"http://test.cnsunrun
         * .com/wuye/Uploads/Trifle/20170909/59b38b868d19c.jpg"}]
         */

        private String trifle_id;
        private String trifle_service_id;
        private String content;
        public int status;
        private String add_time;
        private String building_no;
        private String room_no;
        private String reply;
        private String is_reply;
        private String deal_time;
        private String username;
        private String mobile;
        private String trifle_service_title;
        private String status_title;
        private List<ImagesBean> images;
        private List<ReplyImagesBean> reply_images;

        public List<ReplyImagesBean> getReply_images() {
            return reply_images;
        }

        public String getTrifle_id()
        {
            return trifle_id;
        }

        public void setTrifle_id(String trifle_id)
        {
            this.trifle_id = trifle_id;
        }

        public String getTrifle_service_id()
        {
            return trifle_service_id;
        }

        public void setTrifle_service_id(String trifle_service_id)
        {
            this.trifle_service_id = trifle_service_id;
        }

        public String getContent()
        {
            return content;
        }

        public void setContent(String content)
        {
            this.content = content;
        }

        public String getAdd_time()
        {
            return add_time;
        }

        public void setAdd_time(String add_time)
        {
            this.add_time = add_time;
        }

        public String getBuilding_no()
        {
            return building_no;
        }
        public String getReply() {
            return reply;
        }

        public void setReply(String reply) {
            this.reply = reply;
        }

        public String getIs_reply() {
            return is_reply;
        }

        public void setIs_reply(String is_reply) {
            this.is_reply = is_reply;
        }

        public void setBuilding_no(String building_no)
        {
            this.building_no = building_no;
        }

        public String getRoom_no()
        {
            return room_no;
        }

        public void setRoom_no(String room_no)
        {
            this.room_no = room_no;
        }

        public String getDeal_time()
        {
            return deal_time;
        }

        public void setDeal_time(String deal_time)
        {
            this.deal_time = deal_time;
        }

        public String getUsername()
        {
            return username;
        }

        public void setUsername(String username)
        {
            this.username = username;
        }

        public String getMobile()
        {
            return mobile;
        }

        public void setMobile(String mobile)
        {
            this.mobile = mobile;
        }

        public String getTrifle_service_title()
        {
            return trifle_service_title;
        }

        public void setTrifle_service_title(String trifle_service_title)
        {
            this.trifle_service_title = trifle_service_title;
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
             * image_id : 54
             * image : http://test.cnsunrun.com/wuye/Uploads/Trifle/20170909/59b38b8688ae9.jpg
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
