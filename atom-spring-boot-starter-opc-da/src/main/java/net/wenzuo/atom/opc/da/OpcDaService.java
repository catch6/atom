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

package net.wenzuo.atom.opc.da;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.wenzuo.atom.opc.da.config.OpcDaProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author Catch
 * @since 2024-06-21
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class OpcDaService {

	private final OpcDaProperties opcDaProperties;
	private final ApplicationContext applicationContext;

	public void updateItem(String id, String item, Object value) {
		WriteableAccessBase access = applicationContext.getBean(opcDaProperties.getBeanPrefix() + id, WriteableSyncAccess.class);
		access.updateItem(item, value);
	}

	public void updateItem(String id, String item, Object value, boolean isByRef) {
		WriteableAccessBase access = applicationContext.getBean(opcDaProperties.getBeanPrefix() + id, WriteableSyncAccess.class);
		access.updateItem(item, value, isByRef);
	}

}
