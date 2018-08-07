package com.cnsunrun.jiajiagou.common.alipay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-09-27 10:55
 */
public class AliPayOrderInfoUtil {
    public AliPayOrderInfoUtil() {
    }

    public static Map<String, String> buildAuthInfoMap(String pid, String app_id, String target_id, boolean rsa2) {
        HashMap keyValues = new HashMap();
        keyValues.put("app_id", app_id);
        keyValues.put("pid", pid);
        keyValues.put("apiname", "com.alipay.account.auth");
        keyValues.put("app_name", "mc");
        keyValues.put("biz_type", "openservice");
        keyValues.put("product_id", "APP_FAST_LOGIN");
        keyValues.put("scope", "kuaijie");
        keyValues.put("target_id", target_id);
        keyValues.put("auth_type", "AUTHACCOUNT");
        keyValues.put("sign_type", rsa2?"RSA2":"RSA");
        return keyValues;
    }

    public static Map<String, String> buildOrderParamMap(String app_id, boolean rsa2, String out_trade_no, String name, String price, String detail,String notify_url) {
        HashMap keyValues = new HashMap();
        keyValues.put("app_id", app_id);
        keyValues.put("biz_content", "{\"timeout_express\":\"30m\",\"product_code\":\"QUICK_MSECURITY_PAY\",\"total_amount\":\"" + price + "\",\"subject\":\"" + detail + "\",\"body\":\"" + name + "\",\"out_trade_no\":\"" + out_trade_no + "\",\"notify_url\":\"" + notify_url+"\"}");
        keyValues.put("charset", "utf-8");
        keyValues.put("method", "alipay.trade.app.pay");
        keyValues.put("sign_type", rsa2?"RSA2":"RSA");
        keyValues.put("timestamp", "2016-07-29 16:55:53");
        keyValues.put("version", "1.0");
        return keyValues;
    }

    public static String buildOrderParam(Map<String, String> map) {
        ArrayList keys = new ArrayList(map.keySet());
        StringBuilder sb = new StringBuilder();

        String tailValue;
        for(int tailKey = 0; tailKey < keys.size() - 1; ++tailKey) {
            tailValue = (String)keys.get(tailKey);
            String value = (String)map.get(tailValue);
            sb.append(buildKeyValue(tailValue, value, true));
            sb.append("&");
        }

        String var6 = (String)keys.get(keys.size() - 1);
        tailValue = (String)map.get(var6);
        sb.append(buildKeyValue(var6, tailValue, true));
        return sb.toString();
    }

    private static String buildKeyValue(String key, String value, boolean isEncode) {
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append("=");
        if(isEncode) {
            try {
                sb.append(URLEncoder.encode(value, "UTF-8"));
            } catch (UnsupportedEncodingException var5) {
                sb.append(value);
            }
        } else {
            sb.append(value);
        }

        return sb.toString();
    }

    public static String getSign(Map<String, String> map, String rsaKey, boolean rsa2) {
        ArrayList keys = new ArrayList(map.keySet());
        Collections.sort(keys);
        StringBuilder authInfo = new StringBuilder();

        String tailValue;
        String oriSign;
        for(int tailKey = 0; tailKey < keys.size() - 1; ++tailKey) {
            tailValue = (String)keys.get(tailKey);
            oriSign = (String)map.get(tailValue);
            authInfo.append(buildKeyValue(tailValue, oriSign, false));
            authInfo.append("&");
        }

        String var11 = (String)keys.get(keys.size() - 1);
        tailValue = (String)map.get(var11);
        authInfo.append(buildKeyValue(var11, tailValue, false));
        oriSign = SignUtils.sign(authInfo.toString(), rsaKey, rsa2);
        String encodedSign = "";

        try {
            encodedSign = URLEncoder.encode(oriSign, "UTF-8");
        } catch (UnsupportedEncodingException var10) {
            var10.printStackTrace();
        }

        return "sign=" + encodedSign;
    }

    private static String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);
        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }
}
