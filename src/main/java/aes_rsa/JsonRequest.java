package aes_rsa;

/**
 * @author lizhangyu
 * @date 2021/5/4 14:48
 */
public class JsonRequest {

    /**
     * 接口id (可空)
     */
    private String serviceId;

    /**
     * 请求唯一id (非空)
     */
    private String requestId;

    /**
     * 商户id (非空)
     */
    private String appId;

    /**
     * 参数签名 (非空)
     */
    private String sign;

    /**
     * 对称加密key (非空)
     */
    private String aseKey;

    /**
     * 时间戳，精确到毫秒 (非空)
     */
    private long timestamp;

    /**
     * 请求的业务参数(AES加密后传入，可空)
     */
    private String body;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getAseKey() {
        return aseKey;
    }

    public void setAseKey(String aseKey) {
        this.aseKey = aseKey;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
