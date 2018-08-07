package com.cnsunrun.jiajiagou.home.bean;

import com.cnsunrun.jiajiagou.common.base.BaseResp;

import java.util.List;

/**
 * Created by j2yyc on 2018/1/27.
 */

public class ConferenceHallBean extends BaseResp {

    /**
     * status : 1
     * msg : ok
     * info : [{"procedure_id":"10","title":"大大滴滴答答","description":"大大滴滴答答大大滴滴答答大大滴滴答答大大滴滴答答","number":"1",
     * "end_date":"2018-01-31","type":"0","images":[{"image_id":"139","pid":"10","image":"http://test.cnsunrun
     * .com/wuye/Uploads/Procedure/5a6aefe7963b6.png","width":"231","height":"160"},{"image_id":"140","pid":"10",
     * "image":"http://test.cnsunrun.com/wuye/Uploads/Procedure/5a6aefe88ce02.png","width":"231","height":"160"},
     * {"image_id":"141","pid":"10","image":"http://test.cnsunrun.com/wuye/Uploads/Procedure/5a6aefe8cfdd0.png",
     * "width":"230","height":"160"}],"type_title":"未投"},{"procedure_id":"9","title":"小区拆迁意见公投",
     * "description":"小区拆迁意见公投","number":"1","end_date":"2018-02-07","type":"0","images":[{"image_id":"142",
     * "pid":"9","image":"http://test.cnsunrun.com/wuye/Uploads/Procedure/5a6af0dc35306.jpeg","width":"600",
     * "height":"722"}],"type_title":"未投"},{"procedure_id":"8","title":"赶走小区汪星人",
     * "description":"由于最近小区频发恶狗伤人事件！针对小区是否允许养狗进行意见调研！","number":"0","end_date":"2018-03-08","type":"0",
     * "images":[{"image_id":"135","pid":"8","image":"http://test.cnsunrun.com/wuye/Uploads/Procedure/5a6ac46c67f9f
     * .png","width":"300","height":"300"},{"image_id":"136","pid":"8","image":"http://test.cnsunrun
     * .com/wuye/Uploads/Procedure/5a6ac46c9f664.png","width":"300","height":"300"}],"type_title":"未投"}]
     */

    private List<InfoBean> info;

    public List<InfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<InfoBean> info) {
        this.info = info;
    }

    public static class InfoBean {
        /**
         * procedure_id : 10
         * title : 大大滴滴答答
         * description : 大大滴滴答答大大滴滴答答大大滴滴答答大大滴滴答答
         * number : 1
         * end_date : 2018-01-31
         * type : 0
         * images : [{"image_id":"139","pid":"10","image":"http://test.cnsunrun
         * .com/wuye/Uploads/Procedure/5a6aefe7963b6.png","width":"231","height":"160"},{"image_id":"140","pid":"10",
         * "image":"http://test.cnsunrun.com/wuye/Uploads/Procedure/5a6aefe88ce02.png","width":"231","height":"160"},
         * {"image_id":"141","pid":"10","image":"http://test.cnsunrun.com/wuye/Uploads/Procedure/5a6aefe8cfdd0.png",
         * "width":"230","height":"160"}]
         * type_title : 未投
         */

        private String procedure_id;
        private String title;
        private String description;
        private String number;
        private String end_date;
        private String type;
        private String type_title;
        private List<ImagesBean> images;

        public String getProcedure_id() {
            return procedure_id;
        }

        public void setProcedure_id(String procedure_id) {
            this.procedure_id = procedure_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getEnd_date() {
            return end_date;
        }

        public void setEnd_date(String end_date) {
            this.end_date = end_date;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getType_title() {
            return type_title;
        }

        public void setType_title(String type_title) {
            this.type_title = type_title;
        }

        public List<ImagesBean> getImages() {
            return images;
        }

        public void setImages(List<ImagesBean> images) {
            this.images = images;
        }

        public static class ImagesBean {
            /**
             * image_id : 139
             * pid : 10
             * image : http://test.cnsunrun.com/wuye/Uploads/Procedure/5a6aefe7963b6.png
             * width : 231
             * height : 160
             */

            private String image_id;
            private String pid;
            private String image;
            private String width;
            private String height;

            public String getImage_id() {
                return image_id;
            }

            public void setImage_id(String image_id) {
                this.image_id = image_id;
            }

            public String getPid() {
                return pid;
            }

            public void setPid(String pid) {
                this.pid = pid;
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
