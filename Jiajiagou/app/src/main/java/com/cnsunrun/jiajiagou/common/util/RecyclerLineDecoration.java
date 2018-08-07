package com.cnsunrun.jiajiagou.common.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cnsunrun.jiajiagou.R;


/**
 * Description:
 * Data：2017/3/10 0010-下午 5:02
 * Blog：http://blog.csdn.net/u013983934
 * Author: CZ
 */
public class RecyclerLineDecoration extends RecyclerView.ItemDecoration
{
    private int dividerHeight;
    private Paint dividerPaint;

    public RecyclerLineDecoration(Context context, int color) {
        dividerPaint = new Paint();
        dividerPaint.setColor(color);
        dividerHeight = context.getResources().getDimensionPixelSize(R.dimen.dp_1);
    }


    public RecyclerLineDecoration(Context context) {
        dividerPaint = new Paint();
        dividerPaint.setColor(0xfff4f4f4);
        dividerHeight = context.getResources().getDimensionPixelSize(R.dimen.dp_1);
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = dividerHeight;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        for (int i = 0; i < childCount - 1; i++) {
            View view = parent.getChildAt(i);
            float top = view.getBottom();
            float bottom = view.getBottom() + dividerHeight;
            c.drawRect(left, top, right, bottom, dividerPaint);
        }
    }
}
