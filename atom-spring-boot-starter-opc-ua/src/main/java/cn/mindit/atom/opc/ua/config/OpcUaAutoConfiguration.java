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

package cn.mindit.atom.opc.ua.config;

import cn.mindit.atom.opc.ua.OpcUaListenerProcessor;
import cn.mindit.atom.opc.ua.OpcUaService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * @author Catch
 * @since 2024-06-23
 */
@Import({OpcUaConfiguration.class, OpcUaService.class, OpcUaListenerProcessor.class})
@EnableConfigurationProperties(OpcUaProperties.class)
@ConditionalOnProperty(value = "atom.opc.ua.enabled", matchIfMissing = true)
public class OpcUaAutoConfiguration {

}
