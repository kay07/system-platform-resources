package com.elco.system.platform.resources.utils;

import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * @author kay
 * @date 2021/9/17
 */
@Component
public class MyAESUtil {
    private static final String KEY="jkAePOIU1234++==";

    public String Encrypt(String sSrc) {
        byte[] bytes1 = null;
        try {
            byte[] bytes = KEY.getBytes("utf-8");
            SecretKeySpec secretKeySpec=new SecretKeySpec(bytes,"AES");
            Cipher instance = Cipher.getInstance("AES/ECB/PKCS5Padding");//算法，模式，补充方式
            instance.init(Cipher.ENCRYPT_MODE,secretKeySpec);
            bytes1 = instance.doFinal(sSrc.getBytes("utf-8"));
        }catch (Exception e){ }
        return new BASE64Encoder().encode(bytes1);
    }
    public String Decrypt(String sSrc){
        String s="";
        try {
            byte[] bytes = KEY.getBytes("utf-8");
            SecretKeySpec secretKeySpec=new SecretKeySpec(bytes,"AES");
            Cipher instance = Cipher.getInstance("AES/ECB/PKCS5Padding");//算法，模式，补充方式
            instance.init(Cipher.DECRYPT_MODE,secretKeySpec);
            byte[] bytes1 = new BASE64Decoder().decodeBuffer(sSrc);
            byte[] bytes2 = instance.doFinal(bytes1);
             s = new String(bytes2, "utf-8");
        }catch (Exception e){}
        return s;
    }

}
