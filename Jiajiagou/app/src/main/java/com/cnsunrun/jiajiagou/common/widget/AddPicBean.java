package com.cnsunrun.jiajiagou.common.widget;

/**
 * Description:
 * Data：2017/4/20 0020-下午 12:08
 * Blog：http://blog.csdn.net/u013983934
 * Author: CZ
 */
public class AddPicBean
{

    public static final int TYPE_ADD = 110;
    public static final int TYPE_PIC = 120;

    public String picPath;
    public int type;

    public AddPicBean(String picPath, int type)
    {
        this.picPath = picPath;
        this.type = type;
    }

}
