package com.api.encryption;

import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import lombok.extern.java.Log;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Properties;

/**
 * RSA加密工具类
 *
 * @author 李星源
 * @version 1.0
 * @date 2020/03/12
 */
@Log
public class RSAUtil {

    public static String publicKeyStr;
    public static String privateKeyStr;

    /**
     * 如果keyPair为空，则创建一个值
     */
    static {
        try {
            Properties properties=new Properties();
            InputStream in=MD5.class.getClassLoader().getResourceAsStream("config/encryption.properties");
            properties.load(in);
            publicKeyStr=properties.getProperty("RSAPublic");
            privateKeyStr=properties.getProperty("RSAPrivate");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成密钥对
     *
     * @return KeyPair对象
     */
    public static KeyPair getKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator=KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair=keyPairGenerator.generateKeyPair();
            return keyPair;
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取公钥(Base64编码)
     *
     * @param keyPair 密钥对
     * @return 公钥
     */
    public static String getPublicKey(KeyPair keyPair){
        PublicKey publicKey=keyPair.getPublic();
        byte[] bytes=publicKey.getEncoded();
        return byte2Base64(bytes);
    }

    /**
     * 获取私钥(Base64编码)
     *
     * @param keyPair 密钥对
     * @return 私钥
     */
    public static String getPrivateKey(KeyPair keyPair){
        PrivateKey privateKey=keyPair.getPrivate();
        byte[] bytes=privateKey.getEncoded();
        return byte2Base64(bytes);
    }

    /**
     * 将Base64编码后的公钥转换成PublicKey对象
     *
     * @param pubStr 公钥
     * @return PublicKey对象
     */
    public static PublicKey string2PublicKey(String pubStr) {
        try {
            byte[] keyBytes=base642Byte(pubStr);
            X509EncodedKeySpec keySpec=new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory=KeyFactory.getInstance("RSA");
            PublicKey publicKey=keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将Base64编码后的私钥转换成PrivateKey对象
     *
     * @param priStr 私钥
     * @return PrivateKey对象
     */
    public static PrivateKey string2PrivateKey(String priStr) {
        try {
            byte[] keyBytes=base642Byte(priStr);
            PKCS8EncodedKeySpec keySpec=new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory=KeyFactory.getInstance("RSA");
            PrivateKey privateKey=keyFactory.generatePrivate(keySpec);
            return privateKey;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 公钥加密
     *
     * @param content 加密的内容
     * @param publicKey 公钥
     * @return 加密后密文
     */
    public static byte[] publicEncrypt(byte[] content, PublicKey publicKey) {
        try {
            Cipher cipher=Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] bytes=cipher.doFinal(content);
            return bytes;
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
     * 私钥解密
     *
     * @param content 加密密文
     * @param privateKey 私钥
     * @return 解密后内容
     */
    public static byte[] privateDecrypt(byte[] content, PrivateKey privateKey) {
        try {
            Cipher cipher=Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] bytes=cipher.doFinal(content);
            return bytes;
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
     * 字节数组转Base64编码
     *
     * @param bytes 字节数组
     * @return Base64编码
     */
    public static String byte2Base64(byte[] bytes){
        BASE64Encoder encoder=new BASE64Encoder();
        return encoder.encode(bytes);
    }

    /**
     * Base64编码转字节数组
     *
     * @param base64Key Base64编码
     * @return 字节数组
     */
    public static byte[] base642Byte(String base64Key) {
        try {
            BASE64Decoder decoder=new BASE64Decoder();
            return decoder.decodeBuffer(base64Key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 运用私钥解密
     * 适应实际需要
     *
     * @param content 密文
     * @param pri base64编码后的私钥
     * @return 解密后的密文
     */
    public static String privateDecryptString(String content,String pri){
//        PrivateKey privateKey=string2PrivateKey(getPrivateKey(keyPair));//得到私钥
        PrivateKey privateKey=string2PrivateKey(pri);
        byte[] base642Byte=base642Byte(content);//解码密文
        byte[] password=privateDecrypt(base642Byte,privateKey);//解密密文
        return new String(password);
    }

    public static void main(String[] args) {
            try {
                //===============生成公钥和私钥，公钥传给客户端，私钥服务端保留==================
                //生成RSA公钥和私钥，并Base64编码
                System.out.println("RSA公钥Base64编码:" + publicKeyStr);
                System.out.println("RSA私钥Base64编码:" + privateKeyStr);

                //=================客户端=================
                //hello, i am infi, good night!加密
                String message = "123";
                //将Base64编码后的公钥转换成PublicKey对象
                PublicKey publicKey = RSAUtil.string2PublicKey(publicKeyStr);
                //用公钥加密
                byte[] publicEncrypt = RSAUtil.publicEncrypt(message.getBytes(), publicKey);
                //加密后的内容Base64编码
                String byte2Base64 = RSAUtil.byte2Base64(publicEncrypt);
                System.out.println("公钥加密并Base64编码的结果：" + byte2Base64);


                //##############    网络上传输的内容有Base64编码后的公钥 和 Base64编码后的公钥加密的内容     #################



                //===================服务端================
                //将Base64编码后的私钥转换成PrivateKey对象
                PrivateKey privateKey = RSAUtil.string2PrivateKey(privateKeyStr);
                //加密后的内容Base64解码
                byte[] base642Byte = RSAUtil.base642Byte(byte2Base64);
                //用私钥解密
                byte[] privateDecrypt = RSAUtil.privateDecrypt(base642Byte, privateKey);
                //解密后的明文

                new String(RSAUtil.privateDecrypt(RSAUtil.base642Byte(byte2Base64),RSAUtil.string2PrivateKey(RSAUtil.privateKeyStr)));
                System.out.println("解密后的明文: " + new String(privateDecrypt));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


}
