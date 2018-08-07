package com.cnsunrun.jiajiagou.common.alipay;

import android.text.TextUtils;

import java.util.Iterator;
import java.util.Map;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-09-27 11:11
 */
public class PayResult {
    private String resultStatus;
    private String result;
    private String memo;

    public PayResult(Map<String, String> rawResult) {
        if(rawResult != null) {
            Iterator var2 = rawResult.keySet().iterator();

            while(var2.hasNext()) {
                String key = (String)var2.next();
                if(TextUtils.equals(key, "resultStatus")) {
                    this.resultStatus = (String)rawResult.get(key);
                } else if(TextUtils.equals(key, "result")) {
                    this.result = (String)rawResult.get(key);
                } else if(TextUtils.equals(key, "memo")) {
                    this.memo = (String)rawResult.get(key);
                }
            }

        }
    }

    public String toString() {
        return "resultStatus={" + this.resultStatus + "};memo={" + this.memo + "};result={" + this.result + "}";
    }

    public String getResultStatus() {
        return this.resultStatus;
    }

    public String getMemo() {
        return this.memo;
    }

    public String getResult() {
        return this.result;
    }
}
