package com.cnsunrun.jiajiagou.common.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-09-16 20:32
 */
public class RecyclerViewGridDivider extends RecyclerView.ItemDecoration {

    private int colCount;
    private int colSeparateSize;
    private int rowSeparateSize;
    int[] insetBuffers;
    /**
     * 自定义分割线
     * 注意：如果没有设置分割线大小(setDividerSize)，默认不画分割线，只有 item 之间的间距
     *
     * @param colCount 列数量
     * @param colSeparateSize item 水平间距
     * @param rowSeparateSize item 垂直间距
     */
    public RecyclerViewGridDivider(int colCount, int colSeparateSize, int rowSeparateSize) {
        if (colCount <= 1) {
            throw new IllegalArgumentException("wrong args! colCount must larger than 1");
        }
        this.colCount = colCount;
        this.colSeparateSize = colSeparateSize;
        this.rowSeparateSize = rowSeparateSize;

        // 预分配 getItemOffsets 方法内设置的 left, right 间距。如果不这样计算，水平方向上的各个 item 的宽度将不相等
        float inset = colSeparateSize / colCount;
        insetBuffers = new int[colCount * 2];
        for (int i = 0 ; i < colCount ; i++) {
            // 计算规则：每个 item 的 left + right 要相等，前一个 item 的 right 加上后一个 item 的 left 等于 colSeparateSize
            insetBuffers[i * 2 + 0] = (int) (inset * i); // left
            insetBuffers[i * 2 + 1] = (int) (inset * (colCount - 1 - i)); // right
        }
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        final int itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewAdapterPosition();

        if (itemPosition < colCount) {
            outRect.top = 0;
        } else {
            outRect.top = rowSeparateSize;
        }

        outRect.bottom = 0;

        int i = itemPosition % colCount;
        outRect.left = insetBuffers[i * 2 + 0];
        outRect.right = insetBuffers[i * 2 + 1];
    }
}
