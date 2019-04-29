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

    @PostMapping("/deviceChange")
    public String callback(@RequestBody JSONObject modifyDeviceInfo_NotifyMessage) {
        LOGGER.info("modifyDeviceInfo_NotifyMessage is {}", modifyDeviceInfo_NotifyMessage);
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(modifyDeviceInfo_NotifyMessage));
        return modifyDeviceInfo_NotifyMessage.toString();
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
