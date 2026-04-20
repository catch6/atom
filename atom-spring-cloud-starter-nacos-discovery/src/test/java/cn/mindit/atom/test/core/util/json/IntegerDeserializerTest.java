package cn.mindit.atom.test.core.util.json;

import cn.mindit.atom.core.util.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IntegerDeserializerTest {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Bean {
        private Integer value;
        private int primitive;
    }

    @Test
    void parsesNumericValue() {
        Bean bean = JsonUtils.toObject("{\"value\":42,\"primitive\":1}", Bean.class);
        assertThat(bean.getValue()).isEqualTo(42);
        assertThat(bean.getPrimitive()).isEqualTo(1);
    }

    @Test
    void parsesStringValue() {
        Bean bean = JsonUtils.toObject("{\"value\":\"42\",\"primitive\":\"1\"}", Bean.class);
        assertThat(bean.getValue()).isEqualTo(42);
        assertThat(bean.getPrimitive()).isEqualTo(1);
    }

    @Test
    void trimsWhitespace() {
        Bean bean = JsonUtils.toObject("{\"value\":\"  42  \",\"primitive\":0}", Bean.class);
        assertThat(bean.getValue()).isEqualTo(42);
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
        assertThat(bean1.getValue()).isEqualTo(1);
        assertThat(bean2.getValue()).isZero();
    }

}
