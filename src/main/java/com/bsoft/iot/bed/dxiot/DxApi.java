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
        object.put("notifyType", "deviceAdded");
        object.put("callbackurl", "https://lab.bsoft.com.cn:443/iot-bed/bsiot-moblie-bed-1.0/dx/deviceAdded");
        HttpResponse deviceAddedHttpResponse = HttpsUtil.doPostJson(DxApiConstant.SUBSCRIBE, headerMap, object.toJSONString());
        String deviceAddedBodySubscribe = HttpsUtil.getHttpResponseBody(deviceAddedHttpResponse);
        System.out.print("res:" + deviceAddedHttpResponse.getStatusLine());
        LOGGER.info("res is {}", deviceAddedBodySubscribe);
        object.put("notifyType", "deviceDeleted");
        object.put("callbackurl", "https://lab.bsoft.com.cn:443/iot-bed/bsiot-moblie-bed-1.0/dx/deviceDeleted");
        HttpResponse deviceDeletedHttpResponse = HttpsUtil.doPostJson(DxApiConstant.SUBSCRIBE, headerMap, object.toJSONString());
        String deviceDeletedBodySubscribe = HttpsUtil.getHttpResponseBody(deviceDeletedHttpResponse);
        System.out.print("res:" + deviceDeletedHttpResponse.getStatusLine());
        LOGGER.info("res is {}", deviceDeletedBodySubscribe);
        object.put("notifyType", "deviceEvent");
        object.put("callbackurl", "https://lab.bsoft.com.cn:443/iot-bed/bsiot-moblie-bed-1.0/dx/deviceEvent");
        HttpResponse deviceEventHttpResponse = HttpsUtil.doPostJson(DxApiConstant.SUBSCRIBE, headerMap, object.toJSONString());
        String deviceEventBodySubscribe = HttpsUtil.getHttpResponseBody(deviceEventHttpResponse);
        System.out.print("res:" + deviceEventHttpResponse.getStatusLine());
        LOGGER.info("res is {}", deviceEventBodySubscribe);
        object.put("notifyType", "messageConfirm");
        object.put("callbackurl", "https://lab.bsoft.com.cn:443/iot-bed/bsiot-moblie-bed-1.0/dx/messageConfirm");
        HttpResponse messageConfirmHttpResponse = HttpsUtil.doPostJson(DxApiConstant.SUBSCRIBE, headerMap, object.toJSONString());
        String messageConfirmBodySubscribe = HttpsUtil.getHttpResponseBody(messageConfirmHttpResponse);
        System.out.print("res:" + messageConfirmHttpResponse.getStatusLine());
        LOGGER.info("res is {}", messageConfirmBodySubscribe);
        object.put("notifyType", "commandRsp");
        object.put("callbackurl", "https://lab.bsoft.com.cn:443/iot-bed/bsiot-moblie-bed-1.0/dx/commandRsp");
        HttpResponse commandRspHttpResponse = HttpsUtil.doPostJson(DxApiConstant.SUBSCRIBE, headerMap, object.toJSONString());
        String commandRspBodySubscribe = HttpsUtil.getHttpResponseBody(commandRspHttpResponse);
        System.out.print("res:" + commandRspHttpResponse.getStatusLine());
        LOGGER.info("res is {}", commandRspBodySubscribe);
        object.put("notifyType", "serviceInfoChanged");
        object.put("callbackurl", "https://lab.bsoft.com.cn:443/iot-bed/bsiot-moblie-bed-1.0/dx/serviceInfoChanged");
        HttpResponse serviceInfoChangedHttpResponse = HttpsUtil.doPostJson(DxApiConstant.SUBSCRIBE, headerMap, object.toJSONString());
        String serviceInfoChangedBodySubscribe = HttpsUtil.getHttpResponseBody(serviceInfoChangedHttpResponse);
        System.out.print("res:" + serviceInfoChangedHttpResponse.getStatusLine());
        LOGGER.info("res is {}", serviceInfoChangedBodySubscribe);
        object.put("notifyType", "ruleEvent");
        object.put("callbackurl", "https://lab.bsoft.com.cn:443/iot-bed/bsiot-moblie-bed-1.0/dx/ruleEvent");
        HttpResponse ruleEventHttpResponse = HttpsUtil.doPostJson(DxApiConstant.SUBSCRIBE, headerMap, object.toJSONString());
        String ruleEventBodySubscribe = HttpsUtil.getHttpResponseBody(ruleEventHttpResponse);
        System.out.print("res:" + ruleEventHttpResponse.getStatusLine());
        LOGGER.info("res is {}", ruleEventBodySubscribe);
        object.put("notifyType", "bindDevice");
        object.put("callbackurl", "https://lab.bsoft.com.cn:443/iot-bed/bsiot-moblie-bed-1.0/dx/bindDevice");
        HttpResponse bindDeviceHttpResponse = HttpsUtil.doPostJson(DxApiConstant.SUBSCRIBE, headerMap, object.toJSONString());
        String bindDeviceBodySubscribe = HttpsUtil.getHttpResponseBody(bindDeviceHttpResponse);
        System.out.print("res:" + bindDeviceHttpResponse.getStatusLine());
        LOGGER.info("res is {}", bindDeviceBodySubscribe);
        object.put("notifyType", "deviceDatasChanged");
        object.put("callbackurl", "https://lab.bsoft.com.cn:443/iot-bed/bsiot-moblie-bed-1.0/dx/deviceDatasChanged");
        HttpResponse datasChangedHttpResponse = HttpsUtil.doPostJson(DxApiConstant.SUBSCRIBE, headerMap, object.toJSONString());
        String datasChangedBodySubscribe = HttpsUtil.getHttpResponseBody(datasChangedHttpResponse);
        System.out.print("res:" + datasChangedHttpResponse.getStatusLine());
        LOGGER.info("res is {}", datasChangedBodySubscribe);
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
