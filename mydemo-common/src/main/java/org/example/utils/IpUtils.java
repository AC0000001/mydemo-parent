package org.example.utils;

/**
 * @Author: Shengke
 * @Date: 2023-06-07-14:20
 * @Description:
 */


import eu.bitwalker.useragentutils.UserAgent;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class IpUtils {

    public static String convertIPv6ToIPv4(String ipv6) {
        try {
            InetAddress inetAddress = InetAddress.getByName(ipv6);
            byte[] bytes = inetAddress.getAddress();
            if (bytes.length == 16) {
                // IPv6转IPv4的规则：前12个字节为0，第13个字节为255，第14、15、16个字节为IPv4地址的4个字节
                if (bytes[0] == 0 && bytes[1] == 0 && bytes[2] == 0 && bytes[3] == 0
                        && bytes[4] == 0 && bytes[5] == 0 && bytes[6] == 0 && bytes[7] == 0
                        && bytes[8] == 0 && bytes[9] == 0 && bytes[10] == -1 && bytes[11] == -1) {
                    int a = bytes[12] & 0xff;
                    int b = bytes[13] & 0xff;
                    int c = bytes[14] & 0xff;
                    int d = bytes[15] & 0xff;
                    //以:相隔
                    return a + ":" + b + ":" + c + ":" + d;
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    //客户端类型  手机、电脑、平板
    //UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("user-agent"));
    //String clientType = userAgent.getOperatingSystem().getDeviceType().toString();
    //操作系统类型
    //String os = userAgent.getOperatingSystem().getName();
    //请求ip
    //String ip = IpUtils.getIpAddr(request);
    //浏览器类型
    //String browser = userAgent.getBrowser().toString();
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
            //LOGGER.error("X-Real-IP:"+ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("http_client_ip");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        // 如果是多级代理，那么取第一个ip为客户ip
        if (ip != null && ip.indexOf(",") != -1) {
            ip = ip.substring(ip.lastIndexOf(",") + 1, ip.length()).trim();
        }
        return ip;
    }
}
