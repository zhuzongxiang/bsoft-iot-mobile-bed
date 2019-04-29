package com.bsoft.iot.bed.dxiot;

import com.alibaba.fastjson.JSONObject;
import com.bsoft.iot.bed.constant.DxApiConstant;
import com.bsoft.iot.bed.constant.DxConstant;
import com.bsoft.iot.bed.entity.DxAccessToken;
import com.bsoft.iot.bed.util.SpringBeanUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zzx
 */
public class DxApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(DxApi.class);

    private static DxAccessToken dxAccessToken;

    private static RestTemplate restTemplate = SpringBeanUtil.getBean(RestTemplate.class);

    public static void init() {
        try {
            auth();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void auth() throws Exception {
        new HttpsUtil().initSSLConfigForTwoWay();
        Map<String, String> mParam = new HashMap<String, String>(2);
        mParam.put("appId", DxConstant.APP_ID);
        mParam.put("secret", DxConstant.SECRET);
        String res = HttpsUtil.doPostFormUrlEncodedForString(DxApiConstant.AUTH_API, mParam);
        LOGGER.info("res is {}", res);
        if (StringUtils.isBlank(res)) {
            return;
        }
        dxAccessToken = JSONObject.parseObject(res, DxAccessToken.class);
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");
        headerMap.put("app_key", DxConstant.APP_ID);
        headerMap.put("Authorization", "Bearer " + getAccessToken());
        JSONObject object = new JSONObject();
        object.put("notifyType", "deviceInfoChanged");
        object.put("callbackurl", "https://lab.bsoft.com.cn:443/iot-bed/bsiot-moblie-bed-1.0/dx/deviceInfoChanged");
        HttpResponse infoChangeHttpResponse = HttpsUtil.doPostJson(DxApiConstant.SUBSCRIBE, headerMap, object.toJSONString());
        String infoChangeBodySubscribe = HttpsUtil.getHttpResponseBody(infoChangeHttpResponse);
        System.out.print("res:" + infoChangeHttpResponse.getStatusLine());
        LOGGER.info("res is {}", infoChangeBodySubscribe);
        object.put("notifyType", "deviceDataChanged");
        object.put("callbackurl", "https://lab.bsoft.com.cn:443/iot-bed/bsiot-moblie-bed-1.0/dx/deviceDataChanged");
        HttpResponse dataChangeHttpResponse = HttpsUtil.doPostJson(DxApiConstant.SUBSCRIBE, headerMap, object.toJSONString());
        String dataChangeBodySubscribe = HttpsUtil.getHttpResponseBody(dataChangeHttpResponse);
        System.out.print("res:" + dataChangeHttpResponse.getStatusLine());
        LOGGER.info("res is {}", dataChangeBodySubscribe);
    }

    public static String getAccessToken() {
        return dxAccessToken.getAccessToken();
    }

    public static String openLock() throws Exception {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");
        headerMap.put("app_key", DxConstant.APP_ID);
        headerMap.put("Authorization", "Bearer " + getAccessToken());
        JSONObject object = new JSONObject();
        object.put("deviceId", "e0b2873c-bd63-405b-9d45-c070cbf6b32a");
        Map<String, Object> command = new HashMap<>(3);
        object.put("command", command);
        command.put("serviceId", "VehicleDetectorBasic");
        command.put("method", "SET_DEVICE_LEVEL");
        Map<String, String> paras = new HashMap<>(3);
        paras.put("value", "20");
        command.put("paras", paras);
        String res = HttpsUtil.doPostJsonForString(String.format(DxApiConstant.COMMAND, DxConstant.APP_ID), headerMap, object.toJSONString());
        LOGGER.info("res is {}", res);
        return res;
    }
}
