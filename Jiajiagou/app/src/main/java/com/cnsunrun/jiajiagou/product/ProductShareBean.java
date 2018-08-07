package com.cnsunrun.jiajiagou.product;

import com.cnsunrun.jiajiagou.common.base.BaseResp;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-09-18 13:43
 */
public class ProductShareBean extends BaseResp{

    /**
     * status : 1
     * msg : ok
     * info : {"product_id":"2","title":"饼干大将克力架 饼干大将克力架",
     * "content":"夏洛克·福尔摩斯是由19世纪末的英国侦探小说家阿瑟·柯南·道尔所塑造","image":"http://test.cnsunrun
     * .com/wuye/Uploads/Product/Product/Cover/2017-08-22/599be9e95a06f.png","url":"http://test
     * .cnsunrun.com/wuye/Home/Index/Index/product_share_info/product_id/2.html"}
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
         * product_id : 2
         * title : 饼干大将克力架 饼干大将克力架
         * content : 夏洛克·福尔摩斯是由19世纪末的英国侦探小说家阿瑟·柯南·道尔所塑造
         * image : http://test.cnsunrun
         * .com/wuye/Uploads/Product/Product/Cover/2017-08-22/599be9e95a06f.png
         * url : http://test.cnsunrun.com/wuye/Home/Index/Index/product_share_info/product_id/2.html
         */

        private String product_id;
        private String title;
        private String content;
        private String image;
        private String url;

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
