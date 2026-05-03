package cn.mindit.atom.web.util;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 获取 IP 工具类
 *
 * @author Catch
 * @since 2021-06-29
 */
public abstract class IpUtils {

    /**
     * 获取 ip 地址
     * 格式 xxx.xxx.xxx.xxx
     *
     * @param request HttpServletRequest
     * @return ip 地址
     */
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (isValidIp(ip)) {
            return ip;
        }
        ip = request.getHeader("Proxy-Client-IP");
        if (isValidIp(ip)) {
            return ip;
        }
        ip = request.getHeader("WL-Proxy-Client-IP");
        if (isValidIp(ip)) {
            return ip;
        }
        ip = request.getHeader("HTTP_CLIENT_IP");
        if (isValidIp(ip)) {
            return ip;
        }
        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (isValidIp(ip)) {
            return ip;
        }
        ip = request.getRemoteAddr();
        if (isValidIp(ip)) {
            return ip;
        }
        return null;
    }

    /**
     * 校验是否是有效 ip
     *
     * @param ip ip 地址
     * @return 是否有效, true=有效, false=无效
     */
    private static boolean isValidIp(String ip) {
        return ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip);

    }

    /**
     * 获取 ip 数组
     * 因为一些电脑有多个网卡, 比如在同时使用了有线网卡和无线网卡时会检测出多个 ip
     *
     * @param originIp 原始 ip 地址
     * @return ip集合
     */
    private static List<String> getIps(String originIp) {
        if (originIp == null) {
            return null;
        }
        String[] ips = originIp.split(",");
        return Arrays.stream(ips).map(String::trim).collect(Collectors.toList());
    }

}
