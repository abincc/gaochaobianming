package com.cnsunrun.jiajiagou.personal.bean;

import com.cnsunrun.jiajiagou.common.base.BaseResp;

import java.util.List;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-11-24 16:23
 */
public class ReportDetailBean extends BaseResp {


    /**
     * status : 1
     * info : {"report_id":"18","ope_date":"2017-11-24","add_time":"2017-11-24 16:21:21",
     * "title":"2017-11-24 (第四季度)","images":[{"image_id":"27","image":"http://test.cnsunrun
     * .com/wuye/Uploads/Report/20171124/5a17d681b0dc0.png","width":"1080","height":"2040"}]}
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
         * report_id : 18
         * ope_date : 2017-11-24
         * add_time : 2017-11-24 16:21:21
         * title : 2017-11-24 (第四季度)
         * images : [{"image_id":"27","image":"http://test.cnsunrun
         * .com/wuye/Uploads/Report/20171124/5a17d681b0dc0.png","width":"1080","height":"2040"}]
         */

        private String report_id;
        private String ope_date;
        private String add_time;
        private String title;
        private List<ImagesBean> images;

        public String getReport_id() {
            return report_id;
        }

        public void setReport_id(String report_id) {
            this.report_id = report_id;
        }

        public String getOpe_date() {
            return ope_date;
        }

        public void setOpe_date(String ope_date) {
            this.ope_date = ope_date;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<ImagesBean> getImages() {
            return images;
        }

        public void setImages(List<ImagesBean> images) {
            this.images = images;
        }

        public static class ImagesBean {
            /**
             * image_id : 27
             * image : http://test.cnsunrun.com/wuye/Uploads/Report/20171124/5a17d681b0dc0.png
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
