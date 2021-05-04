package aes;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author lizhangyu
 * @date 2021/5/4 0:10
 */
public class AESUtils {

    /**
     * 编码格式
     */
    public static final String CHARSET = "UTF-8";

    /**
     * 加密算法
     */
    public static final String AES_ALGORITHM = "AES";

    /**
     * 生成密钥
     * @return
     */
    public static String generateKey() {
        try {
            //1.构造密钥生成器，指定为AES算法,不区分大小写
            KeyGenerator keygen = KeyGenerator.getInstance(AES_ALGORITHM);
            //2.生成一个128位的随机源
            keygen.init(128, new SecureRandom());
            //3.产生原始对称密钥
            SecretKey secretKey = keygen.generateKey();
            //4.获得原始对称密钥的字节数组
            byte[] byteKey = secretKey.getEncoded();
            //5.返回密钥
            return Hex.encodeHexString(byteKey);
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        //如果有错就返加nulll
        return null;
    }

    /**
     * 加密
     * @param thisKey 密钥
     * @param data 数据
     * @return 加密后的数据
     */
    public static String encode(String thisKey, String data) {
        try {
            //1.转换KEY
            Key key = new SecretKeySpec(Hex.decodeHex(thisKey), AES_ALGORITHM);
            //2.根据指定算法AES自成密码器
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            //3.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作，第二个参数为使用的KEY
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //4.获取加密内容的字节数组(这里要设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
            byte[] byte_encode = data.getBytes(CHARSET);
            //5.根据密码器的初始化方式--加密：将数据加密
            byte[] result = cipher.doFinal(byte_encode);
            //6.将字符串返回
            return Hex.encodeHexString(result);
        }catch (DecoderException e) {
            e.printStackTrace();
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //如果有错就返加nulll
        return null;
    }

    /**
     * 解密
     * @param thisKey 密钥
     * @param data 加密的数据
     * @return 解密后的数据
     */
    public static String decode(String thisKey, String data) {
        try {
            //1.转换KEY
            Key key = new SecretKeySpec(Hex.decodeHex(thisKey), AES_ALGORITHM);
            //2.根据指定算法AES自成密码器
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            //3.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作，第二个参数为使用的KEY
            cipher.init(Cipher.DECRYPT_MODE, key);
            //4.将加密并编码后的内容解码成字节数组
            byte[] byte_content = Hex.decodeHex(data);
            //5.解密
            byte[] byte_decode = cipher.doFinal(byte_content);
            //6.获取加密内容的字节数组(这里要设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
            String result = new String(byte_decode, CHARSET);
            return result;
        }catch (DecoderException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        //如果有错就返加nulll
        return null;
    }

    public static void main(String[] args) {
        String key = generateKey();
        System.out.println("生成的密钥为：" + key);
//        String pubKey = "6aa7066f738828f333b1bb84b62606c6";
//        String priKey = "6aa7066f738828f333b1bb84b62606c6";
        String data = "使用AES对称加密，请输入加密的规则使用AES对称加密AAAAAABBBBB";
        String encodeData = encode(key, data);
        System.out.println("加密后的数据为：" + encodeData);
        String decodeData = decode(key, encodeData);
        System.out.println("解密后的数据为：" + decodeData);
    }
}
