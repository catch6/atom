/*
 * Copyright (c) 2022-2024 Catch(catchlife6@163.com).
 * Atom is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package net.wenzuo.atom.mqtt;

import java.lang.annotation.*;

/**
 * @author Catch
 * @since 2024-06-25
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MqttListener {

	/**
	 * 实例 ID, 为空则使用 MqttProperties.getId()
	 *
	 * @return 实例 ID
	 */
	String id() default "default";

	/**
	 * 订阅主题
	 *
	 * @return 主题
	 */
	String[] topics();

	/**
	 * QoS
	 *
	 * @return QoS, 默认为 0
	 */
	int[] qos() default 0;

}
