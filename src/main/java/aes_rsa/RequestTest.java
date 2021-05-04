package aes_rsa;

import aes.AESUtils;
import org.apache.commons.lang.StringUtils;
import rsa.RSAUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author lizhangyu
 * @date 2021/5/4 15:17
 * @description: 测试
 */
public class RequestTest {

    /**
     * 公钥key
     */
    private static final String PUBLIC_KEY = "publicKey";

    /**
     * 私钥KEY
     */
    private static final String PRIVATE_KEY = "privateKey";

    public static void main(String[] args) throws Exception{
        /****先给调用方分配一组RSA密钥和一个appId****/
        //初始化RSA密钥
        Map<String, String> keyMap = RSAUtils.createKeys(1024);
        //公钥
        String publicKey = keyMap.get(PUBLIC_KEY);
        //私钥
        String privateKey = keyMap.get(PRIVATE_KEY);
        //appId，32位的uuid
        String appId = getUUID();
        /****先给调用方分配一组RSA密钥和一个appId****/

        /*****调用方（请求方）*****/
        //业务参数
        Map<String,Object>  businessParams = new HashMap<>();
        businessParams.put("name","Longer");
        businessParams.put("job","程序猿");
        businessParams.put("hobby","打篮球");

        JsonRequest jsonRequest = new JsonRequest();
        jsonRequest.setRequestId(getUUID());
        jsonRequest.setAppId(appId);
        jsonRequest.setTimestamp(System.currentTimeMillis());
        //使用AES密钥，并对密钥进行rsa公钥加密
        String aseKey = AESUtils.generateKey();
        String aseKeyStr = RSAUtils.publicEncrypt(aseKey, RSAUtils.getPublicKey(publicKey));
        jsonRequest.setAseKey(aseKeyStr);
        //请求的业务参数进行加密
        String body = "";
        try {
            body = AESUtils.encode(aseKey, JacksonUtil.beanToJson(businessParams));
        } catch (Exception e) {
            throw new RuntimeException("报文加密异常", e);
        }
        jsonRequest.setBody(body);
        //json转map
        Map<String, Object> paramMap = RSAUtils.beanToMap(jsonRequest);
        paramMap.remove("sign");
        // 参数排序
        Map<String, Object> sortedMap = RSAUtils.sort(paramMap);
        //拼接参数：key1Value1key2Value2
        String urlParams = RSAUtils.groupStringParam(sortedMap);
        //私钥签名
        String sign = RSAUtils.sign(HexUtils.hexStringToBytes(urlParams), privateKey);
        jsonRequest.setSign(sign);

        /*****调用方（请求方）*****/

        /*****接收方（自己的系统）*****/
        //参数判空（略）
        //appId校验（略）
        //本条请求的合法性校验《唯一不重复请求；时间合理》（略）
        //验签
        Map<String, Object> paramMap2 = RSAUtils.beanToMap(jsonRequest);
        paramMap2.remove("sign");
        //参数排序
        Map<String, Object> sortedMap2 = RSAUtils.sort(paramMap2);
        //拼接参数：key1Value1key2Value2
        String urlParams2 = RSAUtils.groupStringParam(sortedMap2);
        //签名验证
        boolean verify = RSAUtils.verify(HexUtils.hexStringToBytes(urlParams2), publicKey, jsonRequest.getSign());
        if (!verify) {
            throw new RuntimeException("签名验证失败");
        }
        //私钥解密，获取aseKey
        String aseKey2 = RSAUtils.privateDecrypt(jsonRequest.getAseKey(), RSAUtils.getPrivateKey(privateKey));
        if (!StringUtils.isEmpty(jsonRequest.getBody())) {
            // 解密请求报文
            String requestBody = "";
            try {
                requestBody = AESUtils.decode(aseKey2, jsonRequest.getBody());
            } catch (Exception e) {
                throw new RuntimeException("请求参数解密异常");
            }
            System.out.println("业务参数解密结果："+requestBody);
        }
        /*****接收方（自己的系统）*****/
    }

    /**
     * 生成UUID
     * @return 32位UUID
     */
    public static String getUUID() {
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replace("-", "");
        return uuid;
    }
}
