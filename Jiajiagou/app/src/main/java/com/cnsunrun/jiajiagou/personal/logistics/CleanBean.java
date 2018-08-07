package com.cnsunrun.jiajiagou.personal.logistics;

/**
 * Created by ${LiuDi}
 * on 2017/9/9on 14:54.
 */

public class CleanBean
{
    private String clean_id;
    private String content;
    private String add_time;
    private String clean_service_title;
    private String status_title;
    private String image;

    public String getClean_id()
    {
        return clean_id;
    }

    public void setClean_id(String clean_id)
    {
        this.clean_id = clean_id;
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

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    private int status;

}
