package com.bsoft.iot.bed.controller;

import com.alibaba.fastjson.JSONObject;
import com.bsoft.iot.bed.constant.MsgType;
import com.bsoft.iot.bed.constant.WxConfig;
import com.bsoft.iot.bed.constant.XmlResp;
import com.bsoft.iot.bed.util.AccessTokenUtil;
import com.bsoft.iot.bed.util.MpApi;
import com.bsoft.iot.bed.util.SHA1;
import eu.bitwalker.useragentutils.DeviceType;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.List;

/**
 * @author zzx
 */
@Controller
@RequestMapping(value = "/callback")
public class CallbackController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CallbackController.class);

    @GetMapping
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            // 开发者接入验证
            String timestamp = req.getParameter("timestamp");
            String nonce = req.getParameter("nonce");
            String signature = req.getParameter("signature");
            String echostr = req.getParameter("echostr");

            if (signature.equals(SHA1.gen(WxConfig.TOKEN, timestamp, nonce))) {
                out(echostr, resp);
            } else {
                out("123456", resp);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            out("", resp);
        }
    }

    @PostMapping
    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        LOGGER.info("testtest=========>");
        try {
            // 编码格式
            req.setCharacterEncoding("UTF-8");

            // 将XML转换为JSON
            JSONObject json = parseXml(req.getInputStream());

            String toUser = json.getString("ToUserName");

            // 验证签名
            String timestamp = req.getParameter("timestamp");
            String nonce = req.getParameter("nonce");
            String signature = req.getParameter("signature");
            if (!signature.equals(SHA1.gen(WxConfig.TOKEN, timestamp, nonce))) {
                LOGGER.info("signature is error");
                out("", resp);
                return;
            }

            json.put("timestamp", timestamp);
            json.put("nonce", nonce);
            json.put("signature", signature);

            LOGGER.info("msgJson : ===" + json);
            // 处理请求
            String xmlStr = handle(json);

            LOGGER.info("10xmlStr=" + xmlStr);
            // null 转为空字符串
            xmlStr = xmlStr == null ? "" : xmlStr;

            out(xmlStr, resp);
        } catch (Throwable e) {
            e.printStackTrace();
            // 异常时响应空串
            out("", resp);
        }
    }

    public String handle(JSONObject json) throws Exception {
        String msgType = json.getString("MsgType");
        String fromUser = json.getString("FromUserName");
        String toUser = json.getString("ToUserName");
        // 基础事件推送
        if (MsgType.EVENT.equals(msgType)) {
            String event = json.getString("Event");
            // 关注公众号
            String openId = json.getString("FromUserName");
            if (MsgType.Event.SUBSCRIBE.equals(event)) {
                // 回复欢迎语
                return XmlResp.buildText(fromUser, toUser, String.format("欢迎关注%s"));
            }

            // 取消关注公众号
            else if(MsgType.Event.UNSUBSCRIBE.equals(event)) {
            }
        }

        // 未处理的情况返回空字符串
        return "";
    }

    private void out(String str, HttpServletResponse response) {
        Writer out = null;
        try {
            response.setContentType("text/xml;charset=UTF-8");
            out = response.getWriter();
            out.append(str);
            out.flush();
        } catch (IOException e) {
            // ignore
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }

    /**
     * 解析请求中的xml元素为Map
     */
    @SuppressWarnings("unchecked")
    private static JSONObject parseXml(InputStream in) throws DocumentException, IOException {
        JSONObject json = new JSONObject();
        SAXReader reader = new SAXReader();
        Document document = reader.read(in);
        Element root = document.getRootElement();
        List<Element> elementList = root.elements();
        for (Element e : elementList) {
            json.put(e.getName(), e.getText());
        }
        return json;
    }


    @GetMapping("/createCode")
    public String createCode() {
        String result = MpApi.createQrCode(AccessTokenUtil.queryAccessToken().getAccessToken(), "e0b2873c-bd63-405b-9d45-c070cbf6b32a");
        result = MpApi.getQrCode(JSONObject.parseObject(result).getString("ticket"));
        return result;
    }
}
