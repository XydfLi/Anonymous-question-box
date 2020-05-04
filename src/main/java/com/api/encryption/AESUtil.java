package com.api.encryption;

import com.alibaba.fastjson.JSON;
import com.api.signUri.SignUser;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Properties;

/**
 * AES工具类
 *
 * @author 李星源
 * @version 1.0
 * @date 2020/03/12
 */
public class AESUtil {
    private static String KEY=null;//AES密钥
    private static String ALGORITHMSTR=null;//算法

    /**
     * 加载encryption.properties
     */
    static {
        try {
            Properties properties=new Properties();
            InputStream in=MD5.class.getClassLoader().getResourceAsStream("config/encryption.properties");
            properties.load(in);
            KEY=properties.getProperty("AESKEY");
            ALGORITHMSTR=properties.getProperty("ALGORITHMSTR");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 进行base64编码
     *
     * @param bytes 待编码的byte[]
     * @return 编码后的base64 code
     */
    private static String base64Encode(byte[] bytes){
        return Base64.encodeBase64String(bytes);
    }

    /**
     * 进行base 64解码
     *
     * @param base64Code 待解码的base 64 code
     * @return 解码后的byte[]
     */
    private static byte[] base64Decode(String base64Code) {
        try {
            return StringUtils.isEmpty(base64Code)?null:new BASE64Decoder().decodeBuffer(base64Code);
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * AES加密
     *
     * @param content 待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的byte[]
     */
    private static byte[] aesEncryptToBytes(String content,String encryptKey) {
        try {
            KeyGenerator kgen=KeyGenerator.getInstance("AES");
            kgen.init(128);
            Cipher cipher=Cipher.getInstance(ALGORITHMSTR);
            cipher.init(Cipher.ENCRYPT_MODE,new SecretKeySpec(encryptKey.getBytes(),"AES"));

            return cipher.doFinal(content.getBytes("utf-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * AES加密为base64 code
     *
     * @param content 待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的base64 code
     */
    public static String aesEncrypt(String content, String encryptKey) {
        return base64Encode(aesEncryptToBytes(content,encryptKey));
    }

    /**
     * AES解密
     *
     * @param encryptBytes 待解密的byte[]
     * @param decryptKey 解密密钥
     * @return 解密后的String
     */
    private static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) {
        try {
            KeyGenerator kgen=KeyGenerator.getInstance("AES");
            kgen.init(128);

            Cipher cipher=Cipher.getInstance(ALGORITHMSTR);
            cipher.init(Cipher.DECRYPT_MODE,new SecretKeySpec(decryptKey.getBytes(),"AES"));
            byte[] decryptBytes=cipher.doFinal(encryptBytes);

            return new String(decryptBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将base64 code AES解密
     *
     * @param encryptStr 待解密的base 64 code
     * @param decryptKey 解密密钥
     * @return 解密后的string
     */
    public static String aesDecrypt(String encryptStr, String decryptKey) {
        return StringUtils.isEmpty(encryptStr)?null:aesDecryptByBytes(base64Decode(encryptStr),decryptKey);
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
//        System.out.println("秘钥为"+KEY);
//        String s=aesEncrypt("123abc",KEY);
//        System.out.println(s);
        String s="Fl1J2mMyxuBM/m3sYQvU7w==";
        System.out.println("解密后"+aesDecrypt(s,KEY));

//        String content= JSON.toJSONString(new SignUser("cl",new Date()));
//        content=AESUtil.aesEncrypt(content,KEY);
//        System.out.println("加密后："+content);
//        String res= URLEncoder.encode(content,"UTF-8");
//        System.out.println("转化后："+res);
//        res= URLDecoder.decode(res,"UTF-8");
//        content=AESUtil.aesDecrypt(res,KEY);
//        System.out.println("解密后："+content);
    }
}
