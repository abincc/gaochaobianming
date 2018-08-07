package com.cnsunrun.jiajiagou.common.util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by ${LiuDi}
 * on 2017/4/23on 16:32.
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration
{

    private int space;

    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        if(parent.getChildPosition(view) != 0)
            outRect.top = space;
    }
}
