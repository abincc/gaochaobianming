package com.cnsunrun.jiajiagou.common.alipay;

/**
 * Created by ${LiuDi}
 * on 2017/9/13on 10:15.
 */

public class Constants
{
    protected static final int PAYID = 000;
    protected static final int PAYERROR = 111;
    protected static final int PAYBUSY = 222;

    public static final int SDK_PAY_FLAG = 1;

    public static final int SDK_CHECK_FLAG = 2;
    //商户PID
    public static final String PARTNER = "2017090608580337 ";
    //商户收款账号
    public static final String SELLER = "gaochaowangluo@sina.cn@qq.com";
    //商户私钥，pkcs8格式
    public static final String RSA_PRIVATE =
            "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQD4rxwokHNHpY1k5GbV83bbKfv67NR" +
                    "//CbjBNtdD0zcKgotsgbY6qiBHxZy7t6MlhR8rZqGtJozRAQrSgdPzo2Ot3Tvpw88" +
                    "/xtmRLzd6WF6M7p2Zj3Tn3MXBlvXdL8AV3HiyHT8v6wuqTY2Kz2N5q6WxCD1tfiIexF3jW344P1+uvGOx" +
                    "/RV1PeO9lDPTAeGQ1ryFTcmD+TZ2nKyJ/ylTQsxyrEXNNGYyBald2LYxrzcdUrVGIKJmE790/bjvVd0V5" +
                    "+vG4So457C1MLxJON+5c9HSWINRqxZ9r+f0DYNSyU8li/lNZ5EEROYHnDVYIbCi/DLfNVivS" +
                    "+IpJ9WazgDbN0FAgMBAAECggEBANBrgkAFAHjVqTEbs4wwpjZ8c3ETt+0j2Tt+wJK31gTXPvyZLd/ymxLMiTX+OIq" +
                    "+5aCwpseq6Wl54byeKI6cYEtyOhD4hG7sp3wIs/mQNWADpozAl6SbCAwOCj8C22lf4FTfCB0kTxw0OPkhOE+LPFujOLR1tDw" +
                    "+WJ9H6xkhB5ODVyy7ON4OpOz4jpYXdLgxR8" +
                    "+07UsRQzRDJhpUkTNes3ZZISphmSML9bdH87V7tVEVuyEeDL8uXEckupZKS7GEoo" +
                    "/sQ8qB4CKEOGj9iXIfeOGYbzMl7KYXv1sInSXAMus/aIMIV299CI2PRSFsiU8XD9kjC2l/me0rMShwWcCusoECgYEA/O" +
                    "/4UeUxOXYFPnLlBOUPfD5A+5HHrRvinzCAbFOIxlLUw2H4tIWu36rrrv4VSBEeGGfnAKgjIpfKqZ" +
                    "+pglvHyYDklnm52SywaYxXZQVxR0wWIxcsvqU78KC9rUVsbB0bPaxA9hD6aP6qvv8wPwgAuKirU0YOuA3Cnq9qyXNNW9UCgYEA+7H0sv+iv/kOBtrjLhH1A57vkYPwTd1QqN8qgcTEo1TboArZt3c+uLFYL8T1NiFuKhuRUzX7KkQoJfrVUlU8HmIfdbn7g8+sZ0cAb0qEOWBTDpWBWl5ARz3mfR4gSdND20B+L6vw+pXpGPClGtP/3aZl/W9I/BrH6OcNi+l4BHECgYBb8KZkrli8OEgjsQPKSbDdSou4Zf8cz3wKvUiRF7Qp0sX/10bd55HvP2O4EieOMJqt6GaxeGx9EIvcKGsnNVFjz3RHtit146akVx0VXWsVrXSGgNAE5G7rQqJvr0J/8fyK6GgciYNcUuWh04SoBLKubLEJ7fc/s9DAeabpXv4JmQKBgQCH3ZMawPXorReOScASkWzybGwj39XrIkm1LupyLHZ9OIRpyxGlgMAh0NGqz6YSd09ReZokEeKn+Sy2+8UFca9HM26KDUAg8gy9n00zxkTD+CQ9niS6VGBcVKTD62xAjcN2akTzlNw7WIsAhH6Lkd7vljA980GxnP9G0Nq2KOw5cQKBgQCTeyyiwE0ocWiZh2aVdxxB5SRSyjF67LmMUiabr+wQmRAEV0g6toSofIT1DNQ4OgD0/c7Ld2meQXyMOZUN8gNi3rsdeys71ZUqsoLNIxetDRGkMG6Ot3u4hobYvtkTU032aCa/WtW5xikHIM8jWwjNzq3i20pnWFaYWxQXoF+S8w==";
    //支付宝公钥
    public static final String RSA_PUBLIC = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA+K8cKJBzR6WNZORm1fN22yn7" +
            "+uzUf/wm4wTbXQ9M3CoKLbIG2OqogR8Wcu7ejJYUfK2ahrSaM0QEK0oHT86Njrd076cPPP8bZkS83elhejO6dmY9059zFwZb13S" +
            "/AFdx4sh0/L+sLqk2Nis9jeaulsQg9bX4iHsRd41t+OD9frrxjsf0VdT3jvZQz0wHhkNa8hU3Jg" +
            "/k2dpysif8pU0LMcqxFzTRmMgWpXdi2Ma83HVK1RiCiZhO/dP2471XdFefrxuEqOOewtTC8STjfuXPR0liDUasWfa" +
            "/n9A2DUslPJYv5TWeRBETmB5w1WCGwovwy3zVYr0viKSfVms4A2zdBQIDAQAB";


}
