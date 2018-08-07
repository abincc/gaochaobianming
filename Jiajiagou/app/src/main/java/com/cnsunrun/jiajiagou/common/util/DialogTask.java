package com.cnsunrun.jiajiagou.common.util;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnsunrun.jiajiagou.R;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 消息弹窗
 * <p>
 * author:yyc
 * date: 2017-09-21 10:37
 */
public class DialogTask extends DialogFragment {
    @BindView(R.id.iv_status_dialog)
    ImageView mStatusIv;
    @BindView(R.id.tv_message_dialog)
    TextView mMessageTv;
    private static DialogTask mInstance;
    private String mMessage;
    private int mResId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getDialog().setCanceledOnTouchOutside(false);//点击dialog外部不消失
        //点击返回键，不会消失
//        setCancelable(false);
        View view = inflater.inflate(R.layout.dialog_message, container);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        mMessageTv.setText(mMessage);
        mStatusIv.setImageResource(mResId);
    }


    public static DialogTask getInstance() {
        if (mInstance == null) {
            synchronized (DialogTask.class) {
                if (mInstance == null) {
                    mInstance = new DialogTask();
                }
            }
        }
        return mInstance;
    }

    public DialogTask setMessage(String message) {
        this.mMessage = message;
        return this;
    }

    public DialogTask setImageResource(int resId) {
        this.mResId = resId;
        return this;
    }

    public void start(FragmentManager manager,String tag) {
        if (mInstance != null) {
            manager.beginTransaction().remove(this).commit();
            super.show(manager,tag);
//            mInstance.show(manager, tag);
        }
    }

    public void showDialog(FragmentManager manager,String tag) {
        start(manager,tag);
        stop();
    }

    public void stop() {
        stop(1500);
    }

    public void stop(int delay) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (mInstance != null) {
                    mInstance.dismiss();
                    mInstance = null;
                }
            }
        }, delay);
    }


}
