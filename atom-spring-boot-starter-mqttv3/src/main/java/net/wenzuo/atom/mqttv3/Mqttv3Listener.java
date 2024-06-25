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

package net.wenzuo.atom.mqttv3;

import java.lang.annotation.*;

/**
 * @author Catch
 * @since 2024-06-16
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Mqttv3Listener {

	/**
	 * 实例 ID
	 */
	String id();

	/**
	 * 主题
	 */
	String[] topics();

	/**
	 * QoS
	 * 如果所有 topic 都是相同的 QoS, 可以只写一个,否则需要每个 topic 都写一个
	 */
	int[] qos() default {1};

}
