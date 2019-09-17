package com.fuq.demo.tool;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.shiro.util.ByteSource;

import com.fuq.demo.tool.dto.UserInfo;

/**
 * @author fuqCalendar
 * @version 1.0
 * @ClassName: PasswordUtil
 * @Description: 加解密
 * @date 2018/8/19 0019  19:25
 */
public class PasswordUtil {
    /**
     * JAVA6支持以下任意一种算法 PBEWITHMD5ANDDES PBEWITHMD5ANDTRIPLEDES
     * PBEWITHSHAANDDESEDE PBEWITHSHA1ANDRC2_40 PBKDF2WITHHMACSHA1
     */
    /**
     * 定义使用的算法为:PBEWITHMD5andDES算法
     */
    public static final String ALGORITHM = "PBEWithMD5AndDES";// 加密算法
    public static final String Salt = "63293188";// 密钥
    public static final String SIEKDFDSERER = "2abe0b989e1a4a0d";

    public static final String ACCOUNT = "account";

    public static final String PASSWORD = "password";
    /**
     * 定义迭代次数为1000次
     */
    private static final int ITERATIONCOUNT = 1000;
    /**
     * 获取加密算法中使用的盐值,解密中使用的盐值必须与加密中使用的相同才能完成操作. 盐长度必须为8字节
     *
     * @return byte[] 盐值
     */
    public static byte[] getSalt() throws Exception {
        // 实例化安全随机数
        SecureRandom random = new SecureRandom();

        // 产出盐
        return random.generateSeed(8);
    }
    public static byte[] getStaticSalt() {
        // 产出盐
        return Salt.getBytes();
    }
    /**
     * 根据PBE密码生成一把密钥
     *
     * @param password
     *            生成密钥时所使用的密码
     * @return Key PBE算法密钥
     */
    private static Key getPBEKey(String password) {
        // 实例化使用的算法
        SecretKeyFactory keyFactory;
        SecretKey secretKey = null;
        try {
            keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            // 设置PBE密钥参数
            PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
            // 生成密钥
            secretKey = keyFactory.generateSecret(keySpec);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return secretKey;
    }
    /**
     * 加密明文字符串
     *
     * @param plaintext
     *            待加密的明文字符串
     * @param password
     *            生成密钥时所使用的密码
     * @param salt
     *            盐值
     * @return 加密后的密文字符串
     * @throws Exception
     */
    public static String encrypt(String plaintext, String password, byte[] salt) {
        Key key = getPBEKey(password);
        if (null == salt) {
            salt = getStaticSalt();
        }
        byte[] encipheredData = null;
        PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, ITERATIONCOUNT);
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);
            encipheredData = cipher.doFinal(plaintext.getBytes());
        } catch (Exception e) {
        }
        return bytesToHexString(encipheredData);
    }
    /**
     * 解密密文字符串
     *
     * @param ciphertext
     *            待解密的密文字符串
     * @param password
     *            生成密钥时所使用的密码(如需解密,该参数需要与加密时使用的一致)
     * @param salt
     *            盐值(如需解密,该参数需要与加密时使用的一致)
     * @return 解密后的明文字符串
     * @throws Exception
     */
    public static String decrypt(String ciphertext, String password, byte[] salt) {
        Key key = getPBEKey(password);
        if (null == salt) {
            salt = getStaticSalt();
        }
        byte[] passDec = null;
        PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, ITERATIONCOUNT);
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);
            passDec = cipher.doFinal(hexStringToBytes(ciphertext));
        } catch (Exception e) {
        }
        if (null == passDec) {
            return null;
        }
        return new String(passDec);
    }
    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param src
     * @return
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
    /**
     * 将十六进制字符串转换为字节数组
     * @param hexString
     *            十六进制字符串
     * @return
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
    public static boolean checkSignature(String token, String signature, String timestamp, String nonce) {
        String[] arr = new String[] { token, timestamp, nonce };
        // 排序
        Arrays.sort(arr);
        // 生成字符串
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            content.append(arr[i]);
        }
        // sha1加密 // temp == 1488437519509558447cnbiweixin
        // >>141a7b0eeab1966cd78a2a338d4c117123146cdd
        String temp = PasswordUtil.getSHA1String(content.toString());
        // 与微信传递过来的签名进行比较
        return temp.equals(signature);
    }
    public static String getSHA1String(String data) {
        try {
            MessageDigest cript = MessageDigest.getInstance("SHA-1");
            data = byteArrayToHexString(cript.digest(data.getBytes()));
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
        // return DigestUtils.sha1Hex(data); // 使用commons codec生成sha1字符串
    }
    /**
     * 明文:ceo 密码:cnbisoft 密文:73f56b105d6acaaf 明文:ceo
     *
     * @param args
     */
    public static void main(String[] args) {// licensePath
//        int i = 10;
//        for (int j = 0; j < i; j++) {
//            if ((j) % 3 == 0) {
//                // System.out.print("<br>");
//            } else {
//                // System.out.print(j);
//            }
//        }
        // sha1加密 // temp == 1488437519509558447cnbiweixin
        // >>141a7b0eeab1966cd78a2a338d4c117123146cdd
//        String content = "1488437519509558447cnbiweixin";
        //content = "";
//        String temp = PasswordUtil.getSHA1String(content.toString());
//        System.out.println("我的结果是：" + temp + "<====>威信的结果是：141a7b0eeab1966cd78a2a338d4c117123146cdd");
//        temp = new String(PasswordUtil.hexStringToBytes(content));
//        System.out.println("222我的结果是：" + temp + "<====>威信的结果是：141a7b0eeab1966cd78a2a338d4c117123146cdd");
        // System.out.print(-1%2==0);
//        String user = "cnbigjx";
//        String password = "123456";// licensePath licensevalid
        String userName = "fuq";
        String password = "1";
        String name = "傅强";
        
        UserInfo build = UserInfo.builder().username(userName).password(password)
        		.name(name).salt(Salt).build();
        try {
//            byte[] salt = PasswordUtil.getStaticSalt();
        	byte[] salt = build.getCredentialsSalt().getBytes();
            String ciphertext = PasswordUtil.encrypt(userName, password, salt);
            System.out.println("密文:"+ ciphertext);
            String plaintext = PasswordUtil.decrypt(ciphertext,password, salt);
            System.out.println("明文:" + plaintext);
//        	String authorization   = PasswordUtil.getEncodeAuth(user, user, password);
//        	System.out.println("密文:"+authorization);
//        	String authorizationPlaintext = StringUtils.join(PasswordUtil.getDecoderToken(authorization));
//        	System.out.println("明文:"+authorizationPlaintext);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public static String byteArrayToHexString(byte[] b) {
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

    /**
     * 过期时间30分钟
     */
    private static final long EXPIRE_TIME = 30*60*1000;

}