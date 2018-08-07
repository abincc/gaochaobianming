package com.cnsunrun.jiajiagou.common.widget;

import android.os.CountDownTimer;
import android.widget.Button;

public class BtnTimerCount extends CountDownTimer {
    private Button bnt;

    public BtnTimerCount(long millisInFuture, long countDownInterval, Button bnt) {
        super(millisInFuture, countDownInterval);
        this.bnt = bnt;
        bnt.setClickable(false);
        bnt.setTextColor(0xff999999);
    }

    public BtnTimerCount(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onFinish() {
        bnt.setClickable(true);
        bnt.setText("获取验证码");
        bnt.setTextColor(0xff0e8def);
    }

    @Override
    public void onTick(long arg0) {
        bnt.setText(arg0 / 1000 + "秒");
    }
}