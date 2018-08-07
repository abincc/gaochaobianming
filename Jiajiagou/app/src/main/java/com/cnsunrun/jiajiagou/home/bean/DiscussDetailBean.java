package com.cnsunrun.jiajiagou.home.bean;

import com.cnsunrun.jiajiagou.common.base.BaseResp;

import java.util.List;

/**
 * Created by j2yyc on 2018/1/27.
 */

public class DiscussDetailBean extends BaseResp {

    /**
     * status : 1
     * info : {"procedure_id":"9","number":"1","type":"0","title":"小区拆迁意见公投","description":"小区拆迁意见公投","agree":"1",
     * "disagree":"0","abstain":"0","end_date":"2018-02-07","district_title":"光谷中心花园","type_title":"未投",
     * "images":[{"image_id":"142","image":"http://test.cnsunrun.com/wuye/Uploads/Procedure/5a6af0dc35306.jpeg",
     * "width":"600","height":"722"}]}
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
         * procedure_id : 9
         * number : 1
         * type : 0
         * title : 小区拆迁意见公投
         * description : 小区拆迁意见公投
         * agree : 1
         * disagree : 0
         * abstain : 0
         * end_date : 2018-02-07
         * district_title : 光谷中心花园
         * type_title : 未投
         * images : [{"image_id":"142","image":"http://test.cnsunrun.com/wuye/Uploads/Procedure/5a6af0dc35306.jpeg",
         * "width":"600","height":"722"}]
         */

        private String procedure_id;
        private int number;
        private String type;
        private String title;
        private String description;
        private String agree;
        private String disagree;
        private String abstain;
        private String end_date;
        private String district_title;
        private String type_title;
        private List<ImagesBean> images;
        private List<TypeListBean> type_list;

        public List<TypeListBean> getType_list() {
            return type_list;
        }

        public void setType_list(List<TypeListBean> type_list) {
            this.type_list = type_list;
        }

        public String getProcedure_id() {
            return procedure_id;
        }

        public void setProcedure_id(String procedure_id) {
            this.procedure_id = procedure_id;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
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

        public String getAgree() {
            return agree;
        }

        public void setAgree(String agree) {
            this.agree = agree;
        }

        public String getDisagree() {
            return disagree;
        }

        public void setDisagree(String disagree) {
            this.disagree = disagree;
        }

        public String getAbstain() {
            return abstain;
        }

        public void setAbstain(String abstain) {
            this.abstain = abstain;
        }

        public String getEnd_date() {
            return end_date;
        }

        public void setEnd_date(String end_date) {
            this.end_date = end_date;
        }

        public String getDistrict_title() {
            return district_title;
        }

        public void setDistrict_title(String district_title) {
            this.district_title = district_title;
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
             * image_id : 142
             * image : http://test.cnsunrun.com/wuye/Uploads/Procedure/5a6af0dc35306.jpeg
             * width : 600
             * height : 722
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

        public static class TypeListBean {
            public String name;
            public String type;
            public int num;
        }
    }
}
