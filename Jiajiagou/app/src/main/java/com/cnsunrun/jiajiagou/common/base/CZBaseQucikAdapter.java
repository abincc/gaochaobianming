package com.cnsunrun.jiajiagou.common.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.blankj.utilcode.utils.LogUtils;
import com.bumptech.glide.RequestManager;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.Collection;
import java.util.List;

/**
 * Description:
 * Data：2016/11/28 0028-上午 11:45
 * Blog：http://blog.csdn.net/u013983934
 * Author: CZ
 */

public abstract class CZBaseQucikAdapter<T  > extends BaseQuickAdapter<T, BaseViewHolder>
{

    protected int maxItemCount = -1;
    private View mEmptyView;
    private RequestManager imageLoader;

    public CZBaseQucikAdapter(int layoutResId, List<T> data)
    {
        super(layoutResId, data);
    }

    public CZBaseQucikAdapter(List<T> data)
    {
        super(data);
    }

    public CZBaseQucikAdapter(int layoutResId)
    {
        super(layoutResId);
    }


    public void setImageLoader(RequestManager imageLoader){
        this.imageLoader=imageLoader;
    }

    public RequestManager getImageLoader(){
        return imageLoader;
    }
    @Override
    public void setNewData(List<T> data)
    {

        super.setNewData(data);
        if (mEmptyView!=null)
        super.setEmptyView(mEmptyView);
        setEnableLoadMore(false);
    }

    public  void setEmptyViewNow(View emptyView){
        super.setEmptyView(emptyView);
    }

    @Override
    public void setEmptyView(View emptyView)
    {
        mEmptyView = emptyView;
    }



    @Override
    public void addData(@NonNull Collection<? extends T> newData)
    {
        super.addData(newData);
        loadMoreComplete();
    }

    public Context getContext()
    {
        return mContext;
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
                        LogUtils.i("setEnableLoadMore");
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
                    final int[] positions = new int[staggeredGridLayoutManager.getSpanCount()];
                    staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(positions);
                    if ((positions[1] + 1) != getItemCount())
                    {
                        setEnableLoadMore(true);
                    }
                }
            }, 50);
        }
    }


    @Override
    public int getItemCount()
    {
        return super.getItemCount();
    }

    public void setMaxItremCount(int maxCount)
    {
        this.maxItemCount = maxCount;
    }
}
