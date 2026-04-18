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

import cn.mindit.atom.core.core.CoreConstants;
import cn.mindit.atom.core.core.TraceIdTaskDecorator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

class TraceIdTaskDecoratorTest {

    private final TraceIdTaskDecorator decorator = new TraceIdTaskDecorator();

    @AfterEach
    void clearMdc() {
        MDC.clear();
    }

    @Test
    void generatesTraceIdWhenParentMdcIsNull() {
        MDC.clear();
        AtomicReference<String> captured = new AtomicReference<>();
        Runnable decorated = decorator.decorate(() -> captured.set(MDC.get(CoreConstants.TRACE_ID)));
        decorated.run();
        assertThat(captured.get()).isNotBlank();
    }

    @Test
    void preservesExistingTraceIdFromParent() {
        MDC.put(CoreConstants.TRACE_ID, "existing-id");
        AtomicReference<String> captured = new AtomicReference<>();
        Runnable decorated = decorator.decorate(() -> captured.set(MDC.get(CoreConstants.TRACE_ID)));
        decorated.run();
        assertThat(captured.get()).isEqualTo("existing-id");
    }

    @Test
    void generatesTraceIdWhenParentMdcHasOtherKeysButNoTraceId() {
        MDC.put("other-key", "value");
        AtomicReference<String> traceId = new AtomicReference<>();
        AtomicReference<String> other = new AtomicReference<>();
        Runnable decorated = decorator.decorate(() -> {
            traceId.set(MDC.get(CoreConstants.TRACE_ID));
            other.set(MDC.get("other-key"));
        });
        decorated.run();
        assertThat(traceId.get()).isNotBlank();
        assertThat(other.get()).isEqualTo("value");
    }

    @Test
    void clearsMdcAfterRunnableCompletes() {
        MDC.put(CoreConstants.TRACE_ID, "existing-id");
        Runnable decorated = decorator.decorate(() -> {});
        decorated.run();
        assertThat(MDC.get(CoreConstants.TRACE_ID)).isNull();
    }

    @Test
    void clearsMdcEvenWhenRunnableThrows() {
        Runnable decorated = decorator.decorate(() -> {
            throw new RuntimeException("boom");
        });
        try {
            decorated.run();
        } catch (RuntimeException ignored) {
        }
        assertThat(MDC.get(CoreConstants.TRACE_ID)).isNull();
    }

}
