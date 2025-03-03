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

package net.wenzuo.atom.scheduling.config;

import lombok.RequiredArgsConstructor;
import net.wenzuo.atom.scheduling.service.impl.TaskServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Catch
 * @since 2024-07-17
 */
@Import(TaskServiceImpl.class)
@EnableScheduling
@RequiredArgsConstructor
@EnableConfigurationProperties(SchedulingProperties.class)
@ConditionalOnProperty(value = "atom.scheduling.enabled", matchIfMissing = true)
public class SchedulingAutoConfiguration {

}
