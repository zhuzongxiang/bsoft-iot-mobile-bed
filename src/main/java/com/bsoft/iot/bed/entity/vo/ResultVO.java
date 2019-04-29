package com.bsoft.iot.bed.entity.vo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bsoft.iot.bed.constant.ResultConstant;
import lombok.Data;

/**
 * @author zzx
 */
@Data
public class ResultVO {

    public static String result(Integer status, String msg) {
        JSONObject object = new JSONObject();
        object.put(ResultConstant.STATUS, status);
        object.put(ResultConstant.MSG, msg);
        return JSON.toJSONString(object, SerializerFeature.WriteMapNullValue);
    }

    public static <T> String result(Integer status, T t) {
        JSONObject object = new JSONObject();
        object.put(ResultConstant.STATUS, status);
        object.put(ResultConstant.MSG, t);
        return JSON.toJSONString(object, SerializerFeature.WriteMapNullValue);
    }
}
