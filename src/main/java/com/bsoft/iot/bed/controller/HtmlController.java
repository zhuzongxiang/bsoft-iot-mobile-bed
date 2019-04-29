package com.bsoft.iot.bed.controller;

import com.alibaba.fastjson.JSONObject;
import com.bsoft.iot.bed.entity.JsConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.UUID;

/**
 * @author zzx
 */
@Controller
public class HtmlController {

    @RequestMapping("/jsConfig")
    public void jsConfig(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String url = req.getParameter("url");
        url = URLDecoder.decode(url, "utf-8");
        JsConfig jsConfig = new JsConfig();
        jsConfig.setUrl(url);
        jsConfig.setNonceStr(UUID.randomUUID().toString().replaceAll("-", ""));
        jsConfig.setTimestamp(System.currentTimeMillis() / 1000);
        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().write(JSONObject.toJSONString(jsConfig));
    }

}
