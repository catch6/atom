package cn.mindit.atom.test.core.util.json;

import cn.mindit.atom.core.util.MaskType;
import cn.mindit.atom.core.util.json.MaskSerializer;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.BeanProperty;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;

import static org.assertj.core.api.Assertions.assertThat;

class MaskSerializerTest {

    /**
     * Jackson 注册默认序列化器时会调用 createContextual(property=null)，会返回
     * MaskSerializer.instance（type 为 null）。此子类绕过该分支，便于直接测试 serialize 行为。
     */
    static class FixedMaskSerializer extends MaskSerializer {
        FixedMaskSerializer(MaskType type, int start, int end) {
            super(type, start, end);
        }

        @Override
        public ValueSerializer<?> createContextual(SerializationContext context, BeanProperty property) {
            return this;
        }
    }

    private static <T> String write(Class<T> type, MaskType maskType, int start, int end, T value) {
        SimpleModule module = new SimpleModule();
        module.addSerializer(type, new FixedMaskSerializer(maskType, start, end));
        JsonMapper mapper = JsonMapper.builder().addModule(module).build();
        return mapper.writeValueAsString(value);
    }

    private static String writeString(MaskType maskType, String value) {
        return write(String.class, maskType, 0, 0, value);
    }

    @Test
    void customMaskHidesGivenRange() {
        assertThat(write(String.class, MaskType.CUSTOM, 1, 3, "ABCDE")).isEqualTo("\"A**DE\"");
    }

    @Test
    void nameMaskHidesAllButFirstChar() {
        assertThat(writeString(MaskType.NAME, "李明")).isEqualTo("\"李*\"");
        assertThat(writeString(MaskType.NAME, "王小明")).isEqualTo("\"王**\"");
    }

    @Test
    void phoneMaskShowsFirstThreeAndLastFour() {
        assertThat(writeString(MaskType.PHONE, "13812345678")).isEqualTo("\"138****5678\"");
    }

    @Test
    void idCardMaskShowsFirstThreeAndLastFour() {
        assertThat(writeString(MaskType.ID_CARD, "410123199001011234"))
            .isEqualTo("\"410***********1234\"");
    }

    @Test
    void bankCardMaskShowsOnlyLastFour() {
        assertThat(writeString(MaskType.BANK_CARD, "6222021234567890123"))
            .isEqualTo("\"**** **** **** 0123\"");
    }

    @Test
    void emailMaskShowsFirstCharAndDomain() {
        assertThat(writeString(MaskType.EMAIL, "duoduo@126.com"))
            .isEqualTo("\"d*****@126.com\"");
    }

    @Test
    void passwordMaskReplacesAllChars() {
        assertThat(writeString(MaskType.PASSWORD, "secret")).isEqualTo("\"******\"");
    }

    @Test
    void ipv4IsMasked() {
        assertThat(writeString(MaskType.IPV4, "192.168.1.1")).isEqualTo("\"192.*.*.*\"");
    }

    @Test
    void ipv6IsMasked() {
        assertThat(writeString(MaskType.IPV6, "2001:0db8:86a3:08d3:1319:8a2e:0370:7344"))
            .isEqualTo("\"2001:*:*:*:*:*:*:*\"");
    }

    @Test
    void idMaskZeroesLong() {
        assertThat(write(Long.class, MaskType.ID, 0, 0, 12345L)).isEqualTo("0");
    }

    @Test
    void idMaskZeroesString() {
        assertThat(writeString(MaskType.ID, "abc")).isEqualTo("\"0\"");
    }

    @Test
    void emptyStringStaysEmpty() {
        assertThat(writeString(MaskType.NAME, "")).isEqualTo("\"\"");
    }

    @Test
    void fixedPhoneIsMaskedContainsStars() {
        String result = writeString(MaskType.FIXED_PHONE, "0379-12345678");
        assertThat(result).startsWith("\"").endsWith("\"").contains("*");
    }

    @Test
    void addressIsMaskedContainsStars() {
        String result = writeString(MaskType.ADDRESS, "北京市海淀区中关村大街1号");
        assertThat(result).contains("*");
    }

    @Test
    void carLicenseIsMaskedContainsStars() {
        String result = writeString(MaskType.CAR_LICENSE, "苏D40000");
        assertThat(result).contains("*");
    }

    @Test
    void staticInstanceIsAccessible() {
        assertThat(MaskSerializer.instance).isNotNull();
    }

}
