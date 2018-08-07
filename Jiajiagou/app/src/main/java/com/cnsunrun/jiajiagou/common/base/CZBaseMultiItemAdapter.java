package com.cnsunrun.jiajiagou.common.base;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.Collection;
import java.util.List;

/**
 * Description:
 * Data：2016/11/28 0028-上午 11:45
 * Blog：http://blog.csdn.net/u013983934
 * Author: CZ
 */

public abstract class CZBaseMultiItemAdapter<T extends MultiItemEntity> extends BaseMultiItemQuickAdapter<T,
        BaseViewHolder>
{
    private View mEmptyView;

    public CZBaseMultiItemAdapter(List<T> data)
    {
        super(data);
    }

    @Override
    public void setNewData(List<T> data)
    {
        super.setNewData(data);
        setEnableLoadMore(false);
        if (mEmptyView != null)
            super.setEmptyView(mEmptyView);
    }

    @Override
    public void setEmptyView(View emptyView)
    {
        mEmptyView = emptyView;
    }

    public  void setEmptyViewNow(View emptyView){
        super.setEmptyView(emptyView);
    }

    @Override
    public void addData(@NonNull Collection<? extends T> newData)
    {
        super.addData(newData);
        loadMoreComplete();
    }

    /**
     * check if full page after setNewData, if full, it will open load more again.
     *
     * @param recyclerView your recyclerView
     * @see #setNewData(List)
     */
    public void checkFullPage(RecyclerView recyclerView)
    {
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof LinearLayoutManager)
        {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) manager;
            recyclerView.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    if ((linearLayoutManager.findLastCompletelyVisibleItemPosition() + 1) != getItemCount())
                    {
                        setEnableLoadMore(true);
                    }
                }
            }, 50);
        } else if (manager instanceof StaggeredGridLayoutManager)
        {
            final StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) manager;
            recyclerView.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    final int[] positions = new int[2];
                    staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(positions);
                    if ((positions[1] + 1) != getItemCount())
                    {
                        setEnableLoadMore(true);
                    }
                }
            }, 50);
        }
    }
}
