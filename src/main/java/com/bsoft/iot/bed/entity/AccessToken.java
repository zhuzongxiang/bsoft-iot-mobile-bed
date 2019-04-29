package com.bsoft.iot.bed.entity;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;

/**
 * @author zzx
 */
public class AccessToken implements Serializable {

    private static final long serialVersionUID = 1L;

    private String accessToken;
    private long expiresIn;
    private long createTime;
    private String jsApiTicket;


    public String getJsApiTicket() {
        return jsApiTicket;
    }

    public void setJsApiTicket(String jsApiTicket) {
        this.jsApiTicket = jsApiTicket;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public static AccessToken fromJson(String json) {
        AccessToken token = JSONObject.parseObject(json, AccessToken.class);
        token.setCreateTime(System.currentTimeMillis());
        return token;
    }

}
