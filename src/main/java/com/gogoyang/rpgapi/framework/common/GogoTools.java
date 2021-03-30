package com.gogoyang.rpgapi.framework.common;

import org.apache.tomcat.util.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.KeyFactory;
import java.security.Security;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

public class GogoTools {
    public static String UUID() throws Exception {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    /**
     * 生成一个AES秘钥，256位
     *
     * @return
     * @throws Exception
     */
    public static String generateAESKey256() throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(256);
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");

        return Base64.encodeBase64String(key.getEncoded());
    }

    /**
     * 生成一个AES秘钥，128位
     *
     * @return
     * @throws Exception
     */
    public static String generateAESKey128() throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");

        return Base64.encodeBase64String(key.getEncoded());
    }

    /**
     * AES加密
     *
     * @param codec
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptAESKey(String codec, String key) throws Exception {
//        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
//        Cipher cipher = Cipher.getInstance("AES");//"算法/模式/补码方式"
//        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");//"算法/模式/补码方式"
//        byte[] dataBytes = codec.getBytes("UTF-8");//如果有中文，记得加密前的字符集
//        SecretKey keyspec = new SecretKeySpec(key.getBytes(), "AES");
//        cipher.init(Cipher.ENCRYPT_MODE, keyspec);
//        byte[] encrypted = cipher.doFinal(dataBytes);
//        String outCode = Base64.encodeBase64String(encrypted);
//        return outCode;

        //////////////////
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] plaintTextByteArray = codec.getBytes("UTF8");

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] cipherText = cipher.doFinal(plaintTextByteArray);

        String outCode = Base64.encodeBase64String(cipherText);
        return outCode;
    }

    /**
     * 用AES解密
     *
     * @param codec
     * @param key
     * @return
     * @throws Exception
     */
    public static String decryptAESKey(String codec, String key) throws Exception {

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        byte[] keyByte = Base64.decodeBase64(key);
//        Key AESKEY = new SecretKeySpec(Base64.decodeBase64(key), "AES");
        SecretKey keyspace=new SecretKeySpec(key.getBytes(),"AES");
        cipher.init(Cipher.DECRYPT_MODE, keyspace);
//        byte[] result = cipher.doFinal(Base64.decodeBase64(codec));

        String src= new String(cipher.doFinal(Base64.decodeBase64(codec)));

//        String src = new String(result);
        return src;
    }

    public static String decryptAESKey256(String code, String AESKey) throws Exception{
        String algorithmStr = "AES/CBC/PKCS7Padding";
        byte[] encryptedText = null;
        // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
        int base = 16;
        byte[] keyBytes=AESKey.getBytes();
        if (keyBytes.length % base != 0) {
            int groups = keyBytes.length / base + (keyBytes.length % base != 0 ? 1 : 0);
            byte[] temp = new byte[groups * base];
            Arrays.fill(temp, (byte) 0);
            System.arraycopy(keyBytes, 0, temp, 0, keyBytes.length);
            keyBytes = temp;
        }
        // 初始化
        Security.addProvider(new BouncyCastleProvider());
        // 转化成JAVA的密钥格式
        Key key= new SecretKeySpec(keyBytes, "AES");
        Cipher cipher;

            // 初始化cipher
            cipher = Cipher.getInstance(algorithmStr, "BC");

        byte[] iv = AESKey.getBytes();

        System.out.println("IV：" + new String(iv));

//            cipher.init(Cipher.DECRYPT_MODE, key);
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            encryptedText = cipher.doFinal(code.getBytes());
        String src= Base64.encodeBase64String(encryptedText);
        return src;
    }

    /**
     * RSA公钥加密
     *
     * @param str
     *            加密字符串
     * @param publicKey
     *            公钥
     * @return 密文
     * @throws Exception
     *             加密过程中的异常信息
     */
    public static String RSAPublicKeyEncrypt( String str, String publicKey ) throws Exception{
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
        return outStr;
    }

    // 两个日期相减得到的毫秒数
    public static long dateDiff(Date beginDate, Date endDate) {
        long date1ms = beginDate.getTime();
        long date2ms = endDate.getTime();
        return date2ms - date1ms;
    }
}
