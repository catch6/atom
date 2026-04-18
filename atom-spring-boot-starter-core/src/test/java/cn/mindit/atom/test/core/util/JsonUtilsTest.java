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

import cn.mindit.atom.core.util.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import tools.jackson.core.type.TypeReference;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JsonUtilsTest {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Bean {
        private String name;
        private Integer age;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class DateBean {
        private LocalDate date;
        private LocalTime time;
        private LocalDateTime datetime;
    }

    @Test
    void toJsonReturnsNullForNull() {
        assertThat(JsonUtils.toJson(null)).isNull();
    }

    @Test
    void toJsonReturnsStringForCharSequence() {
        assertThat(JsonUtils.toJson("hello")).isEqualTo("hello");
    }

    @Test
    void toJsonReturnsStringForNumber() {
        assertThat(JsonUtils.toJson(123)).isEqualTo("123");
        assertThat(JsonUtils.toJson(3.14)).isEqualTo("3.14");
    }

    @Test
    void toJsonSerializesObject() {
        Bean bean = new Bean("Catch", 30);
        String json = JsonUtils.toJson(bean);
        assertThat(json).contains("\"name\":\"Catch\"").contains("\"age\":30");
    }

    @Test
    void toPrettyJsonContainsIndentation() {
        Bean bean = new Bean("Catch", 30);
        String json = JsonUtils.toPrettyJson(bean);
        assertThat(json).contains("\n");
    }

    @Test
    void toPrettyJsonReturnsNullForNull() {
        assertThat(JsonUtils.toPrettyJson(null)).isNull();
    }

    @Test
    void toPrettyJsonReturnsRawForCharSequenceAndNumber() {
        assertThat(JsonUtils.toPrettyJson("x")).isEqualTo("x");
        assertThat(JsonUtils.toPrettyJson(42)).isEqualTo("42");
    }

    @Test
    void toObjectDeserializesClass() {
        Bean bean = JsonUtils.toObject("{\"name\":\"A\",\"age\":10}", Bean.class);
        assertThat(bean).isNotNull();
        assertThat(bean.getName()).isEqualTo("A");
        assertThat(bean.getAge()).isEqualTo(10);
    }

    @Test
    void toObjectReturnsNullForNullOrEmpty() {
        assertThat(JsonUtils.toObject((String) null, Bean.class)).isNull();
        assertThat(JsonUtils.toObject("", Bean.class)).isNull();
    }

    @Test
    void toObjectReturnsRawStringForCharSequence() {
        assertThat(JsonUtils.toObject("raw-string", String.class)).isEqualTo("raw-string");
    }

    @Test
    void toObjectWithInputStream() {
        InputStream in = new ByteArrayInputStream("{\"name\":\"B\",\"age\":20}".getBytes(StandardCharsets.UTF_8));
        Bean bean = JsonUtils.toObject(in, Bean.class);
        assertThat(bean.getName()).isEqualTo("B");
        assertThat(bean.getAge()).isEqualTo(20);
    }

    @Test
    void toObjectInputStreamReturnsNullForNull() {
        assertThat(JsonUtils.toObject((InputStream) null, Bean.class)).isNull();
    }

    @Test
    void toObjectInputStreamReturnsStringForCharSequence() {
        InputStream in = new ByteArrayInputStream("hello".getBytes(StandardCharsets.UTF_8));
        assertThat(JsonUtils.toObject(in, String.class)).isEqualTo("hello");
    }

    @Test
    void toObjectWithParametricTypes() {
        String json = "[{\"name\":\"A\",\"age\":1},{\"name\":\"B\",\"age\":2}]";
        List<Bean> list = JsonUtils.toObject(json, List.class, Bean.class);
        assertThat(list).hasSize(2);
        assertThat(list.get(0).getName()).isEqualTo("A");
    }

    @Test
    void toObjectWithParametricTypesReturnsNullForEmpty() {
        assertThat((List<Bean>) JsonUtils.toObject("", List.class, Bean.class)).isNull();
        assertThat((List<Bean>) JsonUtils.toObject((String) null, List.class, Bean.class)).isNull();
    }

    @Test
    void toObjectWithTypeReference() {
        String json = "{\"a\":1,\"b\":2}";
        Map<String, Integer> map = JsonUtils.toObject(json, new TypeReference<Map<String, Integer>>() {});
        assertThat(map).containsEntry("a", 1).containsEntry("b", 2);
    }

    @Test
    void toObjectWithTypeReferenceReturnsNullForEmpty() {
        TypeReference<Map<String, Integer>> typeRef = new TypeReference<>() {};
        assertThat(JsonUtils.toObject("", typeRef)).isNull();
        assertThat(JsonUtils.toObject((String) null, typeRef)).isNull();
    }

    @Test
    void toObjectWithTypeUsesGenericType() {
        String json = "{\"a\":1}";
        Map<String, Integer> map = JsonUtils.toObject(json, new TypeReference<Map<String, Integer>>() {}.getType());
        assertThat(map).containsEntry("a", 1);
    }

    @Test
    void longsAreSerializedAsStringsToAvoidPrecisionLoss() {
        // BigDecimal serialized as string by default
        BigDecimal bd = new BigDecimal("3.14159");
        String json = JsonUtils.toJson(Map.of("value", bd));
        assertThat(json).contains("\"3.14159\"");
    }

    @Test
    void longDeserializationFromString() {
        String json = "{\"name\":\"X\",\"age\":\"42\"}";
        Bean bean = JsonUtils.toObject(json, Bean.class);
        assertThat(bean.getAge()).isEqualTo(42);
    }

    @Test
    void dateTimeSerializationFormatsMatch() {
        DateBean bean = new DateBean(
            LocalDate.of(2024, 1, 2),
            LocalTime.of(10, 20, 30),
            LocalDateTime.of(2024, 1, 2, 10, 20, 30)
        );
        String json = JsonUtils.toJson(bean);
        assertThat(json).contains("2024-01-02")
                        .contains("10:20:30")
                        .contains("2024-01-02 10:20:30");
    }

    @Test
    void dateTimeDeserializationParsesFormattedString() {
        String json = "{\"date\":\"2024-01-02\",\"time\":\"10:20:30\",\"datetime\":\"2024-01-02 10:20:30\"}";
        DateBean bean = JsonUtils.toObject(json, DateBean.class);
        assertThat(bean.getDate()).isEqualTo(LocalDate.of(2024, 1, 2));
        assertThat(bean.getTime()).isEqualTo(LocalTime.of(10, 20, 30));
        assertThat(bean.getDatetime()).isEqualTo(LocalDateTime.of(2024, 1, 2, 10, 20, 30));
    }

    @Test
    void jsonMapperIsAccessible() {
        assertThat(JsonUtils.jsonMapper).isNotNull();
        assertThat(JsonUtils.jsonMapper()).isNotNull();
    }

    @Test
    void customizeProvidesBuilderCustomizer() {
        assertThat(JsonUtils.customize()).isNotNull();
    }

}
