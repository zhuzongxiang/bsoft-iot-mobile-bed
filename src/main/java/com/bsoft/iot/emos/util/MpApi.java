package com.bsoft.iot.emos.util;

import com.alibaba.fastjson.JSONObject;
import com.bsoft.iot.emos.constant.WxConfig;
import com.bsoft.iot.emos.entity.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * @author zzx
 */
public class MpApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(MpApi.class);

    private static final String GET_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
            + WxConfig.APP_ID + "&secret=" + WxConfig.APP_SECRET;
    private static final String GET_JS_TICKET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";

    private static RestTemplate restTemplate = SpringBeanUtil.getBean(RestTemplate.class);

    /**
     * 获取访问凭证
     * <p>
     * 正常情况下access_token有效期为7200秒，重复获取将导致上次获取的access_token失效。
     * 由于获取access_token的api调用次数非常有限，需要全局存储与更新access_token <br/>
     * 文档位置：基础支持->获取access token
     */
    public static AccessToken getAccessToken() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(GET_ACCESS_TOKEN_URL, String.class);
        String result = responseEntity.getBody();
        LOGGER.info("getAccessToken result is {}", result);
        return AccessToken.fromJson(result);
    }

    /**
     * 获取微信JS接口的临时票据jsapi_ticket
     */
    public static String getJsApiTicket(String accessToken) {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(GET_JS_TICKET.replace("ACCESS_TOKEN", accessToken), String.class);
        String result = responseEntity.getBody();
        LOGGER.info("getJsApiTicket result is {}", result);
        return JSONObject.parseObject(result).getString("ticket");
    }

}
