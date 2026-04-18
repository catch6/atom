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

package cn.mindit.atom.test.core.util;

import cn.mindit.atom.core.util.BusinessException;
import cn.mindit.atom.core.util.Result;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BusinessExceptionTest {

    @Test
    void defaultConstructorUsesDefaults() {
        BusinessException ex = new BusinessException();
        assertThat(ex.getCode()).isEqualTo(BusinessException.DEFAULT_CODE);
        assertThat(ex.getMessage()).isEqualTo(BusinessException.DEFAULT_MESSAGE);
    }

    @Test
    void messageConstructorKeepsDefaultCode() {
        BusinessException ex = new BusinessException("custom");
        assertThat(ex.getCode()).isEqualTo(BusinessException.DEFAULT_CODE);
        assertThat(ex.getMessage()).isEqualTo("custom");
    }

    @Test
    void codeMessageConstructorSetsBoth() {
        BusinessException ex = new BusinessException(1001, "业务错误");
        assertThat(ex.getCode()).isEqualTo(1001);
        assertThat(ex.getMessage()).isEqualTo("业务错误");
    }

    @Test
    void providerConstructorReadsFromProvider() {
        Result<Void> result = Result.fail(4004, "找不到");
        BusinessException ex = new BusinessException(result);
        assertThat(ex.getCode()).isEqualTo(4004);
        assertThat(ex.getMessage()).isEqualTo("找不到");
    }

    @Test
    void causeConstructorsWrapCause() {
        Throwable cause = new RuntimeException("root");
        BusinessException ex1 = new BusinessException(cause);
        BusinessException ex2 = new BusinessException("msg", cause);
        BusinessException ex3 = new BusinessException(1002, "msg", cause);
        BusinessException ex4 = new BusinessException(Result.fail(1003, "provider"), cause);

        assertThat(ex1.getCause()).isSameAs(cause);
        assertThat(ex1.getCode()).isEqualTo(BusinessException.DEFAULT_CODE);
        assertThat(ex2.getCause()).isSameAs(cause);
        assertThat(ex2.getMessage()).isEqualTo("msg");
        assertThat(ex3.getCode()).isEqualTo(1002);
        assertThat(ex4.getCode()).isEqualTo(1003);
        assertThat(ex4.getMessage()).isEqualTo("provider");
    }

    @Test
    void isRuntimeException() {
        assertThat(new BusinessException()).isInstanceOf(RuntimeException.class);
    }

}
