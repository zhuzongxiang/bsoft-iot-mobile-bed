package com.bsoft.iot.bed.util;

import ctd.util.context.Context;
import ctd.util.context.ContextUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zzx
 */
@Component
public class IpUtil {

    /**
     * 获取当前请求ip
     *
     * @return ip
     */
    public String getCurrentIp() {
        Map<String, Object> headers = (Map<String, Object>) ContextUtils.get(Context.RPC_INVOKE_HEADERS);
        return (String) headers.get(Context.CLIENT_IP_ADDRESS);
    }

    /**
     * 验证ip有效否
     *
     * @param ip ip
     * @return true or false
     */
    public static Boolean validIp(String ip) {
        String regex = "[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(ip).matches();
    }


    private static final Integer PORT_MAX = 65535;
    private static final Integer PORT_MIN = 1;
    private static final Integer PORT_MAX_LENGTH = 6;

    /**
     * 验证port是否合法
     *
     * @param port port
     * @return true or false
     */
    public static boolean validPort(Integer port) {
        String regex = "[0-9]*";
        Pattern pattern = Pattern.compile(regex);
        Matcher isNum = pattern.matcher(port.toString());
        return isNum.matches() && port.toString().length() < PORT_MAX_LENGTH && port >= PORT_MIN
                && port <= PORT_MAX;
    }

    /**
     * 获取当前网络ip
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress = inet.getHostAddress();
            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) {
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }
}
