package com.cnsunrun.jiajiagou.common.widget;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.utils.KeyboardUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.jiajiagou.R;
import com.cnsunrun.jiajiagou.common.base.CZBaseQucikAdapter;
import com.cnsunrun.jiajiagou.common.util.ImgPickerUtils;
import com.cnsunrun.jiajiagou.common.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

import static com.cnsunrun.jiajiagou.common.widget.AddPicBean.TYPE_ADD;
import static com.cnsunrun.jiajiagou.common.widget.AddPicBean.TYPE_PIC;


/**
 * Description:
 * Data：2017/4/14 0014-下午 2:04
 * Blog：http://blog.csdn.net/u013983934
 * Author: CZ
 */
public class AddPicGridAdapter extends CZBaseQucikAdapter<AddPicBean>
{
    public final int MAX_PIC_COUNT = 9;
    public final int PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    //private final int mWdith;
    private final Activity mActivity;

    public AddPicGridAdapter(int layoutRes, List<AddPicBean> data, RecyclerView recyclerView, Activity activity)
    {
        super(layoutRes, data);
        mActivity = activity;
        //mWdith = (ScreenUtils.getScreenWidth(activity) + ConvertUtils.dp2px(activity,0)) / 4;
        if (recyclerView != null)
        {
            recyclerView.addOnItemTouchListener(new com.chad.library.adapter.base.listener.OnItemChildClickListener()
            {
                @Override
                public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position)
                {
                    List<AddPicBean> data = AddPicGridAdapter.this.getData();
                    if (!AddPicGridAdapter.this.containsAdd())
                    {
                        data.add(new AddPicBean(null, TYPE_ADD));
                        AddPicGridAdapter.this.notifyItemInserted(data.size() - 1);
                    }
                    data.remove(position);
                    AddPicGridAdapter.this.notifyItemRemoved(position);
                }
            });

            recyclerView.addOnItemTouchListener(new com.chad.library.adapter.base.listener.OnItemClickListener()
            {
                @Override
                public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position)
                {
                    KeyboardUtils.hideSoftInput(mActivity);
                    if (AddPicGridAdapter.this.getData().get(position).type == TYPE_ADD)
                    {
                        selectPic();
                    }
                }
            });
        }
    }

    private void selectPic()
    {
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            LogUtils.i( "需要授权 ");
//            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                LogUtils.i(  "进行授权");
                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_CONTACTS);
//            }
        } else {
            LogUtils.i(  "不需要授权 ");
            // 进行正常操作
            ImgPickerUtils.getInstance().multiSelect(mActivity, 9 - this.getData().size() +
                    1, new ImgPickerUtils.ImagePickerCallbackImpl()
            {
                @Override
                public void onSuccess(List<String> photoList)
                {
                    ArrayList<AddPicBean> beanArrayList = new ArrayList<>();
                    for (String s : photoList)
                    {
                        beanArrayList.add(new AddPicBean(s, TYPE_PIC));
                    }
                    AddPicGridAdapter.this.addData(beanArrayList);
                }
            });
        }

    }

    public boolean contains(int type)
    {
        boolean contains = false;
        for (AddPicBean bean : mData)
        {
            if (bean.type == type)
            {
                contains = true;
                break;
            }
        }
        return contains;
    }

    public boolean containsPic()
    {
        return contains(TYPE_PIC);
    }

    public boolean containsAdd()
    {
        return contains(TYPE_ADD);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, AddPicBean bean)
    {
//        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) baseViewHolder.itemView
//                .getLayoutParams();
//        layoutParams.width = mWdith;
//        layoutParams.height = mWdith;
//        baseViewHolder.itemView.setLayoutParams(layoutParams);

        final ImageView ivPic = baseViewHolder.getView(R.id.iv_pic);

        ivPic.post(new Runnable()
        {
            @Override
            public void run()
            {
                LogUtils.printD(ivPic.getHeight() + "..." + ivPic.getWidth());
            }
        });


        if (bean.type != TYPE_ADD)
        {
            //是已选择的图片
            Glide.with(mContext).load(bean.picPath).centerCrop().into(ivPic);
            baseViewHolder.setVisible(R.id.iv_del, true);
            baseViewHolder.addOnClickListener(R.id.iv_del);
        } else
        {
            baseViewHolder.setVisible(R.id.iv_del, false);
            //Glide.with(mContext).load(R.drawable.publish_add).into(ivPic);
            ivPic.setImageDrawable(null);
        }

    }


    @Override
    public void setNewData(List<AddPicBean> data)
    {
        data.add(new AddPicBean(null, TYPE_ADD));
        super.setNewData(data);
    }


    public void addData(List<AddPicBean> newData)
    {
        if (mData.get(mData.size() - 1).type == TYPE_ADD)
            mData.remove(mData.size() - 1);
        if (mData.size() + newData.size() < MAX_PIC_COUNT)
            newData.add(new AddPicBean(null, TYPE_ADD));

        mData.addAll(newData);
        notifyDataSetChanged();
    }
}
