package com.bsoft.iot.bed.controller;

import com.alibaba.fastjson.JSONObject;
import com.bsoft.iot.bed.dxiot.DxApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * @author zzx
 */
@RestController
@RequestMapping(value = "/dx", produces = {"application/json;charset=utf-8"})
public class DxCallbackController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DxCallbackController.class);

    /**
     * 设备信息变化回调
     *
     * @param json
     * @return
     */
    @PostMapping("/deviceInfoChanged")
    public String deviceInfoChanged(@RequestBody JSONObject json) {
        LOGGER.info("modifyDeviceInfo_NotifyMessage is {}", json);
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(json));
        return json.toString();
    }

    /**
     * 设备数据变化
     *
     * @param json
     * @return
     */
    @PostMapping("/deviceDataChanged")
    public String deviceDataChanged(@RequestBody JSONObject json) {
        LOGGER.info("modifyDeviceInfo_NotifyMessage is {}", json);
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(json));
        return json.toString();
    }

    @GetMapping("/open")
    public String open() {
        try {
            DxApi.openLock();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }
}
