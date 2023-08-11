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

package net.wenzuo.atom.test.mybatisplus;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.wenzuo.atom.mybatisplus.config.MybatisPlusAutoConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Catch
 * @since 2023-06-06
 */
@Slf4j
@SpringBootTest
class ApplicationTests {

	@Resource
	private AnnotationConfigApplicationContext applicationContext;

	@Test
	void contextLoads() {
		Assertions.assertNotNull(applicationContext.getBean(MybatisPlusAutoConfiguration.class));
		Assertions.assertNotNull(applicationContext.getBean(MybatisPlusInterceptor.class));
		Assertions.assertFalse(applicationContext.containsBean("fillMetaObjectHandler"));
	}

}
