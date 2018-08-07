package com.cnsunrun.jiajiagou.common.alipay;

import com.cnsunrun.jiajiagou.common.alipay.Base64;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-09-27 10:57
 */
public class SignUtils {
    private static final String ALGORITHM = "RSA";
    private static final String SIGN_ALGORITHMS = "SHA1WithRSA";
    private static final String SIGN_SHA256RSA_ALGORITHMS = "SHA256WithRSA";
    private static final String DEFAULT_CHARSET = "UTF-8";

    public SignUtils() {
    }

    private static String getAlgorithms(boolean rsa2) {
        return rsa2?"SHA256WithRSA":"SHA1WithRSA";
    }

    public static String sign(String content, String privateKey, boolean rsa2) {
        try {
            PKCS8EncodedKeySpec e = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(e);
            Signature signature = Signature.getInstance(getAlgorithms(rsa2));
            signature.initSign(priKey);
            signature.update(content.getBytes("UTF-8"));
            byte[] signed = signature.sign();
            return Base64.encode(signed);
        } catch (Exception var8) {
            var8.printStackTrace();
            return null;
        }
    }
}
