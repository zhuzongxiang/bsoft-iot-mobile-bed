package com.bsoft.iot.bed.util;

import com.alibaba.fastjson.JSONObject;
import com.bsoft.iot.bed.constant.WxConfig;
import com.bsoft.iot.bed.entity.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zzx
 */
public class MpApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(MpApi.class);

    private static final String GET_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
            + WxConfig.APP_ID + "&secret=" + WxConfig.APP_SECRET;
    private static final String GET_JS_TICKET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";

    private static final String QR_CODE = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=ACCESS_TOKEN";

    private static final String GET_QR_CODE = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET";

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

    public static String createQrCode(String accessToken, String deviceId) {
        Map<String, Object> postParameters = new HashMap<>();
        postParameters.put("action_name", "QR_LIMIT_SCENE");
        JSONObject scene = new JSONObject();
        JSONObject sceneId = new JSONObject();
        sceneId.put("scene_id", deviceId);
        scene.put("scene", sceneId);
        postParameters.put("action_info", scene);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> r = new HttpEntity<>(postParameters, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(QR_CODE.replace("ACCESS_TOKEN", accessToken), r, String.class);
        String result = responseEntity.getBody();
        LOGGER.info("qrcode result is {}", result);
        return result;
    }

    public static String getQrCode(String ticket){
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(QR_CODE.replace("TICKET", ticket), null, String.class);
        String result = responseEntity.getBody();
        LOGGER.info("qrcode result is {}", result);
        return result;
    }

    public static Boolean downloadFile(String urlString, String filePath){
        // 构造URL
        URL url;
        try {
            url = new URL(urlString);
            // 打开连接
            URLConnection con;
            try {
                con = url.openConnection();
                // 输入流
                InputStream is = con.getInputStream();
                // 1K的数据缓冲
                byte[] bs = new byte[1024];
                // 读取到的数据长度
                int len;
                // 输出的文件流
                OutputStream os = new FileOutputStream(filePath);
                // 开始读取
                while ((len = is.read(bs)) != -1) {
                    os.write(bs, 0, len);
                }
                // 完毕，关闭所有链接
                os.close();
                is.close();
                return true;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            }

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

    }
}
