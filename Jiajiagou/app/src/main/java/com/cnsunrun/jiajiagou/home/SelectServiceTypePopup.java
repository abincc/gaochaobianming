package com.cnsunrun.jiajiagou.home;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.blankj.utilcode.utils.ConvertUtils;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.BasePopupWindow;

import butterknife.BindView;

/**
 * Description:
 * Data：2017/8/24 0024-下午 2:48
 * Blog：http://blog.csdn.net/u013983934
 * Author: CZ
 */
public class SelectServiceTypePopup extends BasePopupWindow
{


//    private final View.OnClickListener mOnClickListener;
//    @BindView(R.id.tv_package)
//    TextView mTvPackage;
//    @BindView(R.id.tv_clean)
//    TextView mTvClean;
    @BindView(R.id.recycle_service_type)
    RecyclerView mRecyclerServiceType;

//    public SelectServiceTypePopup(Context context, int width, int height, View.OnClickListener onClickListener)
//    {
//        super(context, ConvertUtils.dp2px(context, 145), height);
//        mOnClickListener = onClickListener;
//        mTvPackage.setOnClickListener(mOnClickListener);
//        mTvClean.setOnClickListener(mOnClickListener);
//    }
    public SelectServiceTypePopup(Context context, int width, int height, RecyclerView.Adapter adapter)
    {
        super(context, ConvertUtils.dp2px(context, 145), height);
//        mOnClickListener = onClickListener;
//        mTvPackage.setOnClickListener(mOnClickListener);
//        mTvClean.setOnClickListener(mOnClickListener);
        mRecyclerServiceType.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerServiceType.setAdapter(adapter);
    }
    @Override
    protected int getLayoutRes()
    {
        return R.layout.pop_service_type;
    }


}
