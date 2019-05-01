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
        LOGGER.info("设备信息变化回调 is {}", json);
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
        LOGGER.info("设备数据变化 is {}", json);
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(json));
        return json.toString();
    }

    /**
     * 添加新设备
     *
     * @param json
     * @return
     */
    @PostMapping("/deviceAdded")
    public String deviceAdded(@RequestBody JSONObject json) {
        LOGGER.info("添加新设备 is {}", json);
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(json));
        return json.toString();
    }

    /**
     * 删除设备
     *
     * @param json
     * @return
     */
    @PostMapping("/deviceDeleted")
    public String deviceDeleted(@RequestBody JSONObject json) {
        LOGGER.info("删除设备 is {}", json);
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(json));
        return json.toString();
    }

    /**
     * 设备事件
     *
     * @param json
     * @return
     */
    @PostMapping("/deviceEvent")
    public String deviceEvent(@RequestBody JSONObject json) {
        LOGGER.info("设备事件 is {}", json);
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(json));
        return json.toString();
    }

    /**
     * 消息确认
     *
     * @param json
     * @return
     */
    @PostMapping("/messageConfirm")
    public String messageConfirm(@RequestBody JSONObject json) {
        LOGGER.info("消息确认 is {}", json);
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(json));
        return json.toString();
    }

    /**
     * 响应命令
     *
     * @param json
     * @return
     */
    @PostMapping("/commandRsp")
    public String commandRsp(@RequestBody JSONObject json) {
        LOGGER.info("响应命令 is {}", json);
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(json));
        return json.toString();
    }

    /**
     * 设备信息
     *
     * @param json
     * @return
     */
    @PostMapping("/serviceInfoChanged")
    public String serviceInfoChanged(@RequestBody JSONObject json) {
        LOGGER.info("设备信息 is {}", json);
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(json));
        return json.toString();
    }

    /**
     * 规则事件
     *
     * @param json
     * @return
     */
    @PostMapping("/ruleEvent")
    public String ruleEvent(@RequestBody JSONObject json) {
        LOGGER.info("规则事件 is {}", json);
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(json));
        return json.toString();
    }

    /**
     * 设备绑定激活
     *
     * @param json
     * @return
     */
    @PostMapping("/bindDevice")
    public String bindDevice(@RequestBody JSONObject json) {
        LOGGER.info("设备绑定激活 is {}", json);
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(json));
        return json.toString();
    }

    /**
     * 设备数据批量变化
     *
     * @param json
     * @return
     */
    @PostMapping("/deviceDatasChanged")
    public String deviceDatasChanged(@RequestBody JSONObject json) {
        LOGGER.info("设备数据批量变化 is {}", json);
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
