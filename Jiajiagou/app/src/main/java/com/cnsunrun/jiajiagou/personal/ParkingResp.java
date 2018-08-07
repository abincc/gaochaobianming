package com.cnsunrun.jiajiagou.personal;

import com.cnsunrun.jiajiagou.common.base.BaseResp;

import java.util.List;

/**
 * Created by ${LiuDi}
 * on 2017/9/1on 15:27.
 */

public class ParkingResp extends BaseResp
{

    /**
     * status : 1
     * info : [{"parking_id":"7","title":"D-999999",
     * "image":"http://127.0.0.1/wuye/code/Uploads/Parking/59a64d6ad7150.jpg"},{"parking_id":"1","title":"A区88881",
     * "image":"http://127.0.0.1/wuye/code/Uploads/Parking/59a631821ac77.png"}]
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
         * parking_id : 7
         * title : D-999999
         * image : http://127.0.0.1/wuye/code/Uploads/Parking/59a64d6ad7150.jpg
         */

        private String parking_id;
        private String title;
        private String image;

        public String getParking_id()
        {
            return parking_id;
        }

        public void setParking_id(String parking_id)
        {
            this.parking_id = parking_id;
        }

        public String getTitle()
        {
            return title;
        }

        public void setTitle(String title)
        {
            this.title = title;
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