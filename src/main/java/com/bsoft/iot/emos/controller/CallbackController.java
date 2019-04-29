package com.bsoft.iot.emos.controller;

import com.bsoft.iot.emos.constant.WxConfig;
import com.bsoft.iot.emos.util.SHA1;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * @author zzx
 */
@Controller
@RequestMapping(value = "/callback")
public class CallbackController {

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
}
