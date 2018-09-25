package com.yugoo.gis.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.security.Key;
import java.security.Security;

/**
 * Created by rocky on 17/3/3.
 */
public class DesUtils {
    private static final Logger logger = LoggerFactory.getLogger(DesUtils.class);

    /** 字符串默认键值     */
    private static String strDefaultKey = "national";
    /** 默认加密工具     */
    private static Cipher encryptDefaultCipher = null;
    /** 默认解密工具     */
    private static Cipher decryptDefaultCipher = null;
    static {
        try{
            Security.addProvider(new com.sun.crypto.provider.SunJCE());
            Key key = getKey(strDefaultKey.getBytes());

            encryptDefaultCipher = Cipher.getInstance("DES");
            encryptDefaultCipher.init(Cipher.ENCRYPT_MODE, key);

            decryptDefaultCipher = Cipher.getInstance("DES");
            decryptDefaultCipher.init(Cipher.DECRYPT_MODE, key);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
    }

    /**
     * 默认加密
     * @param strIn
     * @return
     * @throws Exception
     */
    public static String encrypt(String strIn) {
        try {
            String result = byteArr2HexStr(encrypt(strIn.getBytes(),encryptDefaultCipher));
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static String encrypt(String strIn,String key) throws Exception {
        Security.addProvider(new com.sun.crypto.provider.SunJCE());
        Key keyB = getKey(key.getBytes());
        Cipher encryptCipher=Cipher.getInstance("DES");
        encryptCipher.init(Cipher.ENCRYPT_MODE, keyB);
        return byteArr2HexStr(encrypt(strIn.getBytes(),encryptCipher));
    }

    /**
     * 默认解密
     * @param strIn
     * @return
     * @throws Exception
     */
    public static String decrypt(String strIn) throws Exception {
        return new String(decrypt(hexStr2ByteArr(strIn),decryptDefaultCipher));
    }
    public static String decrypt(String strIn,String key) throws Exception {
        Security.addProvider(new com.sun.crypto.provider.SunJCE());
        Key keyB = getKey(key.getBytes());
        Cipher decryptCipher = Cipher.getInstance("DES");
        decryptCipher.init(Cipher.DECRYPT_MODE, keyB);
        return new String(decrypt(hexStr2ByteArr(strIn),decryptCipher));
    }


    private static String byteArr2HexStr(byte[] arrB) throws Exception {
        int iLen = arrB.length;
        // 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp = arrB[i];
            // 把负数转换为正数
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            // 小于0F的数需要在前面补0
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }
    private static byte[] encrypt(byte[] arrB,Cipher cipher) throws Exception {
        return cipher.doFinal(arrB);
    }
    private static Key getKey(byte[] arrBTmp) throws Exception {
        // 创建一个空的8位字节数组（默认值为0）
        byte[] arrB = new byte[8];

        // 将原始字节数组转换为8位
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }
        // 生成密钥
        Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");
        return key;
    }
    private static byte[] hexStr2ByteArr(String strIn) throws Exception {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;

        // 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }
    private static byte[] decrypt(byte[] arrB,Cipher cipher) throws Exception {
        return cipher.doFinal(arrB);
    }

    public static void main(String[] args) {
        System.out.println(encrypt("123456"));
    }
}
