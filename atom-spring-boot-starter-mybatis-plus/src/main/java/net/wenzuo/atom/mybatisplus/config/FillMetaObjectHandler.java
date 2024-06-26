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

package net.wenzuo.atom.mybatisplus.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author Catch
 * @since 2021-07-11
 */
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(value = "atom.mybatis-plus.auto-fill", matchIfMissing = false)
@Component
public class FillMetaObjectHandler implements MetaObjectHandler {

	private final MybatisPlusProperties mybatisPlusProperties;

	@Override
	public void insertFill(MetaObject metaObject) {
		this.strictInsertFill(metaObject, mybatisPlusProperties.getCreateTimeField(), LocalDateTime::now, LocalDateTime.class);
		this.strictInsertFill(metaObject, mybatisPlusProperties.getUpdateTimeField(), LocalDateTime::now, LocalDateTime.class);
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		this.strictUpdateFill(metaObject, mybatisPlusProperties.getUpdateTimeField(), LocalDateTime::now, LocalDateTime.class);
	}

}
