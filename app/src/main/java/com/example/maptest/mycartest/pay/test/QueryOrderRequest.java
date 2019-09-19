package com.example.maptest.mycartest.pay.test;

/**
 * Created by ${Author} on 2019/4/17.
 * Use to
 */

public class QueryOrderRequest {
    // 公众账号id
    private String appid;

    // 商户号
    private String mch_id;


    // 随机字符串，32位以内
    private String nonce_str;

    // 商户订单号，32位以内，不重复
    private String out_trade_no;


    // 签名，遵循签名算法
    private String sign;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
