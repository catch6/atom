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

package net.wenzuo.atom.test.core;

import jakarta.annotation.Resource;
import net.wenzuo.atom.core.core.CoreAutoConfiguration;
import net.wenzuo.atom.core.core.CoreJsonConfiguration;
import net.wenzuo.atom.core.core.CoreTraceConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

/**
 * @author Catch
 * @since 2023-06-06
 */
@SpringBootTest
class ApplicationTests {

    @Resource
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(applicationContext.getBean(CoreAutoConfiguration.class));
        Assertions.assertNotNull(applicationContext.getBean(CoreTraceConfiguration.class));
        Assertions.assertNotNull(applicationContext.getBean(CoreJsonConfiguration.class));
    }

}
