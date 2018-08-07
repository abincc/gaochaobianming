package com.cnsunrun.jiajiagou.product;

import com.cnsunrun.jiajiagou.common.base.BaseResp;

import java.util.List;

/**
 * Created by ${LiuDi}
 * on 2017/9/1on 9:45.
 */

public class ProductDetailResp extends BaseResp
{

    /**
     * status : 1
     * info : {"product_id":"4","title":"咖啡奶茶","price":"16.00","description":"咖啡奶茶","cover":"http://test.cnsunrun
     * .com/wuye/Uploads/Product/Product/Cover/2017-08-22/599beb6ef0bb4.jpg","content":"<p>咖啡奶茶<\/p>",
     * "spec":[{"spec_id":"1","spec_title":"颜色","spec_value_list":[{"spec_value_id":"1","spec_value_title":"白色"},
     * {"spec_value_id":"2","spec_value_title":"红色"}]},{"spec_id":"2","spec_title":"尺寸",
     * "spec_value_list":[{"spec_value_id":"5","spec_value_title":"S"}]}],"inventory":"300","sku":[{"sku_id":"4",
     * "spec_value_ids":"1-5"},{"sku_id":"5","spec_value_ids":"2-5"}],"is_collect":"0","images":[{"image_id":"14",
     * "image":"http://test.cnsunrun.com/wuye/Uploads/Product/Photo/2017-08-22/599beb8e66be2.jpg"},{"image_id":"15",
     * "image":"http://test.cnsunrun.com/wuye/Uploads/Product/Photo/2017-08-22/599beb908c5ed.jpg"}],
     * "comment_number":"0"}
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
         * product_id : 4
         * title : 咖啡奶茶
         * price : 16.00
         * description : 咖啡奶茶
         * cover : http://test.cnsunrun.com/wuye/Uploads/Product/Product/Cover/2017-08-22/599beb6ef0bb4.jpg
         * content : <p>咖啡奶茶</p>
         * spec : [{"spec_id":"1","spec_title":"颜色","spec_value_list":[{"spec_value_id":"1","spec_value_title":"白色"},
         * {"spec_value_id":"2","spec_value_title":"红色"}]},{"spec_id":"2","spec_title":"尺寸",
         * "spec_value_list":[{"spec_value_id":"5","spec_value_title":"S"}]}]
         * inventory : 300
         * sku : [{"sku_id":"4","spec_value_ids":"1-5"},{"sku_id":"5","spec_value_ids":"2-5"}]
         * is_collect : 0
         * images : [{"image_id":"14","image":"http://test.cnsunrun
         * .com/wuye/Uploads/Product/Photo/2017-08-22/599beb8e66be2.jpg"},{"image_id":"15","image":"http://test
         * .cnsunrun.com/wuye/Uploads/Product/Photo/2017-08-22/599beb908c5ed.jpg"}]
         * comment_number : 0
         */

        private String product_id;
        private String title;
        private String price;
        private String description;
        private String cover;
        private String content;
        private String inventory;
        private int is_collect;
        private String comment_number;
        private List<SpecBean> spec;
        private List<SkuBean> sku;
        private List<ImagesBean> images;

        public String getProduct_id()
        {
            return product_id;
        }

        public void setProduct_id(String product_id)
        {
            this.product_id = product_id;
        }

        public String getTitle()
        {
            return title;
        }

        public void setTitle(String title)
        {
            this.title = title;
        }

        public String getPrice()
        {
            return price;
        }

        public void setPrice(String price)
        {
            this.price = price;
        }

        public String getDescription()
        {
            return description;
        }

        public void setDescription(String description)
        {
            this.description = description;
        }

        public String getCover()
        {
            return cover;
        }

        public void setCover(String cover)
        {
            this.cover = cover;
        }

        public String getContent()
        {
            return content;
        }

        public void setContent(String content)
        {
            this.content = content;
        }

        public String getInventory()
        {
            return inventory;
        }

        public void setInventory(String inventory)
        {
            this.inventory = inventory;
        }

        public int getIs_collect()
        {
            return is_collect;
        }

        public void setIs_collect(int is_collect)
        {
            this.is_collect = is_collect;
        }

        public String getComment_number()
        {
            return comment_number;
        }

        public void setComment_number(String comment_number)
        {
            this.comment_number = comment_number;
        }

        public List<SpecBean> getSpec()
        {
            return spec;
        }

        public void setSpec(List<SpecBean> spec)
        {
            this.spec = spec;
        }

        public List<SkuBean> getSku()
        {
            return sku;
        }

        public void setSku(List<SkuBean> sku)
        {
            this.sku = sku;
        }

        public List<ImagesBean> getImages()
        {
            return images;
        }

        public void setImages(List<ImagesBean> images)
        {
            this.images = images;
        }

        public static class SpecBean
        {
            /**
             * spec_id : 1
             * spec_title : 颜色
             * spec_value_list : [{"spec_value_id":"1","spec_value_title":"白色"},{"spec_value_id":"2",
             * "spec_value_title":"红色"}]
             */

            private String spec_id;
            private String spec_title;
            private List<SpecValueListBean> spec_value_list;

            public String getSpec_id()
            {
                return spec_id;
            }

            public void setSpec_id(String spec_id)
            {
                this.spec_id = spec_id;
            }

            public String getSpec_title()
            {
                return spec_title;
            }

            public void setSpec_title(String spec_title)
            {
                this.spec_title = spec_title;
            }

            public List<SpecValueListBean> getSpec_value_list()
            {
                return spec_value_list;
            }

            public void setSpec_value_list(List<SpecValueListBean> spec_value_list)
            {
                this.spec_value_list = spec_value_list;
            }

            public static class SpecValueListBean
            {
                /**
                 * spec_value_id : 1
                 * spec_value_title : 白色
                 */

                private String spec_value_id;
                private String spec_value_title;
                public boolean isChecked;
                public String getSpec_value_id()
                {
                    return spec_value_id;
                }

                public void setSpec_value_id(String spec_value_id)
                {
                    this.spec_value_id = spec_value_id;
                }

                public String getSpec_value_title()
                {
                    return spec_value_title;
                }

                public void setSpec_value_title(String spec_value_title)
                {
                    this.spec_value_title = spec_value_title;
                }
            }
        }

        public static class SkuBean
        {
            /**
             * sku_id : 4
             * spec_value_ids : 1-5
             */

            private String sku_id;
            private String spec_value_ids;
            private String price;

            public String getPrice()
            {
                return price;
            }

            public void setPrice(String price)
            {
                this.price = price;
            }

            public String getSku_id()
            {
                return sku_id;
            }

            public void setSku_id(String sku_id)
            {
                this.sku_id = sku_id;
            }

            public String getSpec_value_ids()
            {
                return spec_value_ids;
            }

            public void setSpec_value_ids(String spec_value_ids)
            {
                this.spec_value_ids = spec_value_ids;
            }
        }

        public static class ImagesBean
        {
            /**
             * image_id : 14
             * image : http://test.cnsunrun.com/wuye/Uploads/Product/Photo/2017-08-22/599beb8e66be2.jpg
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
    }
}
