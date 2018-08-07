package com.cnsunrun.jiajiagou.common.util;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.bigkoo.pickerview.TimePickerView;
import com.cnsunrun.jiajiagou.common.widget.view.HighLightEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-11-24 15:36
 */
public class PickerViewOtherUtils {

    private static TimePickerView pvTime;

    private static void initTimePicker(Context context) {
        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(1990, 0, 23,00,00,00);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2500, 11, 28,00,00,00);
        //时间选择器
        pvTime = new TimePickerView.Builder(context, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                /*btn_Time.setText(getTime(date));*/
                HighLightEditText view = (HighLightEditText) v;
                view.setText(getTime(date));
            }
        })
                //年月日时分秒 的显示与否，不设置则默认全部显示
                .setType(new boolean[]{true, true, true, true, true, true})
                .setLabel("", "", "", "", "", "")
                .isCenterLabel(false)
                .setDividerColor(Color.DKGRAY)
                .setContentSize(21)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
//                .setBackgroundId(0x00FFFFFF) //设置外部遮罩颜色
                .setDecorView(null)
                .isCyclic(false)
                .build();
    }

    public static void show(Context context, HighLightEditText view){
        initTimePicker(context);
        pvTime.show(view);
    }

    private static String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }
}
