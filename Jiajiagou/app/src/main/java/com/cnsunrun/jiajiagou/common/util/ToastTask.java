package com.cnsunrun.jiajiagou.common.util;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cnsunrun.jiajiagou.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-09-22 18:06
 */
public class ToastTask {
    private static ToastTask mInstance;
    private Context mContext;
    private  TextView mMessageTv;
    private  ImageView mMessageIv;
    private  Toast mToast;
    private String mMessage;
    private int mResId;
    private boolean isFirst =true;//防止多次点击

    private ToastTask(Context context){
        this.mContext=context;
    }

    public static ToastTask getInstance(Context context) {
        if (mInstance == null) {
            synchronized (ToastTask.class) {
                if (mInstance == null) {
                    mInstance = new ToastTask(context);
                }
            }
        }
        return mInstance;
    }

    public ToastTask setMessage(String message) {
        this.mMessage = message;
        return this;
    }

    public ToastTask setImageResource(int resId) {
        this.mResId = resId;
        return this;
    }

    public void error(){
        this.mResId=R.drawable.message_error;
        show();
    }
    public void success(){
        this.mResId=R.drawable.message_success;
        show();
    }

    public void show(){
        show(2000);
    }


    public void show(int duration) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_message, null);
        mMessageTv = (TextView) view.findViewById(R.id.tv_message_dialog);
        mMessageIv = (ImageView) view.findViewById(R.id.iv_status_dialog);
        if (TextUtils.isEmpty(mMessage))
            return;
        mMessageTv.setText(mMessage);
        mMessageIv.setImageResource(mResId);
        if (mToast==null)
            mToast = new Toast(mContext);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.setView(view);
//        LogUtils.printD("  "+isFirst);
        if (isFirst) {
            mToast.show();
            isFirst=false;
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    mToast.cancel();
                    isFirst=true;
                }
            },duration);
        }

    }


}
