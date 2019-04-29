package com.bsoft.iot.emos.entity;

import com.bsoft.iot.emos.constant.WxConfig;
import com.bsoft.iot.emos.util.AccessTokenUtil;
import com.bsoft.iot.emos.util.SHA1;

import java.security.NoSuchAlgorithmException;


/**
 * @author zzx
 */
public class JsConfig {
    private String appId;
    private long timestamp;
    private String nonceStr;
    private String signature;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAppId() {
        return WxConfig.APP_ID;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getSignature() throws NoSuchAlgorithmException {
        return SHA1.gen2("jsapi_ticket=" + AccessTokenUtil.getJsTicket(), "noncestr=" + getNonceStr(), "timestamp=" + getTimestamp(), "url=" + url);
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }


}
