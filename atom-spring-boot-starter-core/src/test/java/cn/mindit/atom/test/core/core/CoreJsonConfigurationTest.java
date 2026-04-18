/*
 * Copyright (c) 2022-2026 Catch(catchlife6@163.com).
 * Atom is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.mindit.atom.test.core.core;

import cn.mindit.atom.core.core.CoreJsonConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;

import static org.assertj.core.api.Assertions.assertThat;

class CoreJsonConfigurationTest {

    @Test
    void producesJsonMapperBuilderCustomizerBean() {
        CoreJsonConfiguration config = new CoreJsonConfiguration();
        JsonMapperBuilderCustomizer customizer = config.jsonMapperBuilderCustomizer();
        assertThat(customizer).isNotNull();
    }

}
