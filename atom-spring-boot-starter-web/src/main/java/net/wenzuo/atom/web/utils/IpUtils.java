/*
 * Copyright (c) 2022-2023 Catch
 * [Atom] is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package net.wenzuo.atom.web.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 获取 IP 工具类
 *
 * @author Catch
 * @since 2021-06-29
 */
public class IpUtils {

	private static final String COMMA = ",";

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
		return ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip);

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
		String[] ips = originIp.split(COMMA);
		return Arrays.stream(ips).map(String::trim).collect(Collectors.toList());
	}

}
