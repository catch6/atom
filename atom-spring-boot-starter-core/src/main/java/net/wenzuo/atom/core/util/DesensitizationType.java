/*
 * Copyright (c) 2022-2023 Catch(catchlife6@163.com).
 * Atom is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package net.wenzuo.atom.core.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * @author Catch
 * @since 2023-08-25
 */
@Getter
@ToString
@RequiredArgsConstructor
public enum DesensitizationType {

	/**
	 * 自定义
	 */
	CUSTOM,
	/**
	 * 用户 ID
	 */
	USER_ID,
	/**
	 * 中文名
	 */
	CHINESE_NAME,
	/**
	 * 身份证号
	 */
	ID_CARD,
	/**
	 * 手机号
	 */
	MOBILE_PHONE,
	/**
	 * 座机号
	 */
	FIXED_PHONE,
	/**
	 * 地址
	 */
	ADDRESS,
	/**
	 * 电子邮件
	 */
	EMAIL,
	/**
	 * 密码
	 */
	PASSWORD,
	/**
	 * 银行卡号
	 */
	BANK_CARD,
	/**
	 * 中国大陆车牌，包含普通车辆、新能源车辆
	 */
	CAR_LICENSE

}
