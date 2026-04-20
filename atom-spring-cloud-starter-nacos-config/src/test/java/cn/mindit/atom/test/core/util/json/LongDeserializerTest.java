package cn.mindit.atom.test.core.util.json;

import cn.mindit.atom.core.util.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LongDeserializerTest {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Bean {
        private Long value;
        private long primitive;
    }

    @Test
    void parsesNumericValue() {
        Bean bean = JsonUtils.toObject("{\"value\":9223372036854775807,\"primitive\":1}", Bean.class);
        assertThat(bean.getValue()).isEqualTo(Long.MAX_VALUE);
        assertThat(bean.getPrimitive()).isEqualTo(1L);
    }

    @Test
    void parsesStringValue() {
        Bean bean = JsonUtils.toObject("{\"value\":\"12345\",\"primitive\":\"67890\"}", Bean.class);
        assertThat(bean.getValue()).isEqualTo(12345L);
        assertThat(bean.getPrimitive()).isEqualTo(67890L);
    }

    @Test
    void trimsWhitespace() {
        Bean bean = JsonUtils.toObject("{\"value\":\"  1234567  \",\"primitive\":0}", Bean.class);
        assertThat(bean.getValue()).isEqualTo(1234567L);
    }

    @Test
    void nullAndEmptyReturnNullForBoxed() {
        Bean bean1 = JsonUtils.toObject("{\"value\":null,\"primitive\":0}", Bean.class);
        Bean bean2 = JsonUtils.toObject("{\"value\":\"\",\"primitive\":0}", Bean.class);
        assertThat(bean1.getValue()).isNull();
        assertThat(bean2.getValue()).isNull();
    }

    @Test
    void booleanMapsToZeroOrOne() {
        Bean bean1 = JsonUtils.toObject("{\"value\":true,\"primitive\":0}", Bean.class);
        Bean bean2 = JsonUtils.toObject("{\"value\":false,\"primitive\":0}", Bean.class);
        assertThat(bean1.getValue()).isEqualTo(1L);
        assertThat(bean2.getValue()).isZero();
    }

}
