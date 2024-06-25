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

package net.wenzuo.atom.opc.da.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.wenzuo.atom.opc.da.AccessBase;
import net.wenzuo.atom.opc.da.service.OpcDaService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * @author Catch
 * @since 2024-06-21
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class OpcDaServiceImpl implements OpcDaService {

	private final ApplicationContext applicationContext;

	@Override
	public void write(String id, String tag, Object value) {
		AccessBase accessBase = applicationContext.getBean("opcDaAccessBase-" + id, AccessBase.class);
		accessBase.writeItem(tag, value);
	}

}
