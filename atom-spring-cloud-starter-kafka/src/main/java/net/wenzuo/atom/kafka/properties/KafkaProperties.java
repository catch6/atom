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

package net.wenzuo.atom.kafka.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Catch
 * @since 2023-08-13
 */
@Data
@ConfigurationProperties(prefix = "atom.kafka")
public class KafkaProperties {

	/**
	 * 是否启用 kafka 模块
	 */
	private Boolean enabled = true;
	/**
	 * 自动创建的 topic
	 */
	private Topic[] topics;

	@Data
	public static class Topic {

		/**
		 * topic 名称
		 */
		private String name;
		/**
		 * 分区数
		 */
		private Integer numPartitions = -1;
		/**
		 * 副本数
		 */
		private Short replicationFactor = -1;

	}

}
