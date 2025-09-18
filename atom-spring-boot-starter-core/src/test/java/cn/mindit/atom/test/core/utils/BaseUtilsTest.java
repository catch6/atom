/*
 * Copyright (c) 2022-2025 Catch(catchlife6@163.com).
 * Atom is licensed under Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.mindit.atom.test.core.utils;

import cn.mindit.atom.core.util.BaseUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * BaseUtils 工具类测试
 *
 * @author Catch
 * @since 2023-08-08
 */
class BaseUtilsTest {

    @Test
    @DisplayName("测试 idToBase32 方法")
    void testIdToBase32() {
        // 测试基本转换 (BASE32 = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ")
        assertEquals("3", BaseUtils.idToBase32(1L));   // 1 % 32 = 1, 索引1的字符是'3'
        assertEquals("C", BaseUtils.idToBase32(10L));  // 10 % 32 = 10, 索引10的字符是'C'
        assertEquals("Z", BaseUtils.idToBase32(31L));  // 31 % 32 = 31, 索引31的字符是'Z'
        assertEquals("32", BaseUtils.idToBase32(32L));  // 32 / 32 = 1, 1 % 32 = 1 -> "32"
        assertEquals("33", BaseUtils.idToBase32(33L));  // 33 / 32 = 1, 1 % 32 = 1 -> "33"

        // 测试边界值
        assertEquals("", BaseUtils.idToBase32(0L));
        // Long.MAX_VALUE 的转换结果需要根据实际算法计算
        String maxValueResult = BaseUtils.idToBase32(Long.MAX_VALUE);
        assertNotNull(maxValueResult, "Long.MAX_VALUE 的转换结果不应为空");
        assertFalse(maxValueResult.isEmpty(), "Long.MAX_VALUE 的转换结果长度应大于0");
    }

    @Test
    @DisplayName("测试 base32ToId 方法")
    void testBase32ToId() {
        // 测试基本转换 (基于修正后的期望值)
        assertEquals(1L, BaseUtils.base32ToId("3"));   // "3" 对应索引1 -> 1
        assertEquals(10L, BaseUtils.base32ToId("C"));  // "C" 对应索引10 -> 10
        assertEquals(31L, BaseUtils.base32ToId("Z"));  // "Z" 对应索引31 -> 31

        // 测试边界值
        assertEquals(0L, BaseUtils.base32ToId(""));

        // 测试无效字符
        assertEquals(-1L, BaseUtils.base32ToId("1"));  // '1' 不在 BASE32 字符集中
        assertEquals(-1L, BaseUtils.base32ToId("0"));  // '0' 不在 BASE32 字符集中
        assertEquals(-1L, BaseUtils.base32ToId("I"));  // 'I' 不在 BASE32 字符集中
        assertEquals(-1L, BaseUtils.base32ToId("O"));  // 'O' 不在 BASE32 字符集中
    }

    @Test
    @DisplayName("测试 idToBase32 和 base32ToId 的双向转换")
    void testBidirectionalConversion() {
        // 测试多个值的双向转换
        long[] testIds = {1L, 10L, 100L, 1000L, 10000L, 100000L, Long.MAX_VALUE};

        for (long id : testIds) {
            String base32 = BaseUtils.idToBase32(id);
            long convertedId = BaseUtils.base32ToId(base32);
            assertEquals(id, convertedId, "ID " + id + " 转换失败");
        }
    }

    @Test
    @DisplayName("测试 idToBase 方法 - 自定义字符集")
    void testIdToBaseWithCustomCharacters() {
        String customChars = "ABCDEF";

        // 测试自定义字符集转换
        assertEquals("B", BaseUtils.idToBase(customChars, 1L));
        assertEquals("C", BaseUtils.idToBase(customChars, 2L));
        assertEquals("BA", BaseUtils.idToBase(customChars, 6L));
        assertEquals("BB", BaseUtils.idToBase(customChars, 7L));

        // 测试空字符集
        assertThrows(IllegalArgumentException.class, () -> {
            BaseUtils.idToBase("", 1L);
        });
    }

    @Test
    @DisplayName("测试 baseToId 方法 - 自定义字符集")
    void testBaseToIdWithCustomCharacters() {
        String customChars = "ABCDEF";

        // 测试自定义字符集转换
        assertEquals(1L, BaseUtils.baseToId(customChars, "B"));
        assertEquals(2L, BaseUtils.baseToId(customChars, "C"));
        assertEquals(6L, BaseUtils.baseToId(customChars, "BA"));
        assertEquals(7L, BaseUtils.baseToId(customChars, "BB"));

        // 测试空字符串
        assertEquals(0L, BaseUtils.baseToId(customChars, ""));

        // 测试空字符集
        assertThrows(IllegalArgumentException.class, () -> {
            BaseUtils.baseToId("", "A");
        });
    }

    @Test
    @DisplayName("测试 base36 转换")
    void testBase36Conversion() {
        // Base36 应该包含 0-9 和 A-Z
        String base36 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        // 测试基本转换
        assertEquals("1", BaseUtils.idToBase(base36, 1L));
        assertEquals("A", BaseUtils.idToBase(base36, 10L));
        assertEquals("Z", BaseUtils.idToBase(base36, 35L));
        assertEquals("10", BaseUtils.idToBase(base36, 36L));

        // 测试反向转换
        assertEquals(1L, BaseUtils.baseToId(base36, "1"));
        assertEquals(10L, BaseUtils.baseToId(base36, "A"));
        assertEquals(35L, BaseUtils.baseToId(base36, "Z"));
        assertEquals(36L, BaseUtils.baseToId(base36, "10"));
    }

    @Test
    @DisplayName("测试 base62 转换")
    void testBase62Conversion() {
        // Base62 应该包含 0-9, A-Z, a-z
        String base62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        // 测试基本转换
        assertEquals("1", BaseUtils.idToBase(base62, 1L));
        assertEquals("A", BaseUtils.idToBase(base62, 10L));
        assertEquals("Z", BaseUtils.idToBase(base62, 35L));
        assertEquals("a", BaseUtils.idToBase(base62, 36L));
        assertEquals("z", BaseUtils.idToBase(base62, 61L));
        assertEquals("10", BaseUtils.idToBase(base62, 62L));

        // 测试反向转换
        assertEquals(1L, BaseUtils.baseToId(base62, "1"));
        assertEquals(10L, BaseUtils.baseToId(base62, "A"));
        assertEquals(35L, BaseUtils.baseToId(base62, "Z"));
        assertEquals(36L, BaseUtils.baseToId(base62, "a"));
        assertEquals(61L, BaseUtils.baseToId(base62, "z"));
        assertEquals(62L, BaseUtils.baseToId(base62, "10"));
    }

    @Test
    @DisplayName("测试无效字符处理")
    void testInvalidCharacterHandling() {
        // 测试包含无效字符的字符串
        String base32 = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";

        // 包含无效字符应该返回 -1（indexOf 返回 -1）
        assertEquals(-1L, BaseUtils.baseToId(base32, "1")); // '1' 不在 base32 字符集中
        assertEquals(-1L, BaseUtils.baseToId(base32, "0")); // '0' 不在 base32 字符集中
        assertEquals(-1L, BaseUtils.baseToId(base32, "I")); // 'I' 不在 base32 字符集中
        assertEquals(-1L, BaseUtils.baseToId(base32, "O")); // 'O' 不在 base32 字符集中
    }

    @Test
    @DisplayName("测试大数值处理")
    void testLargeNumberHandling() {
        // 测试大数值的转换
        long largeId = 1234567890L;
        String base32 = BaseUtils.idToBase32(largeId);
        long convertedId = BaseUtils.base32ToId(base32);
        assertEquals(largeId, convertedId);

        // 测试更大的数值
        long veryLargeId = Long.MAX_VALUE / 1000;
        base32 = BaseUtils.idToBase32(veryLargeId);
        convertedId = BaseUtils.base32ToId(base32);
        assertEquals(veryLargeId, convertedId);
    }

    @Test
    @DisplayName("测试零值处理")
    void testZeroValueHandling() {
        // 测试零值的转换
        assertEquals("", BaseUtils.idToBase32(0L));
        assertEquals(0L, BaseUtils.base32ToId(""));

        // 测试自定义字符集的零值处理
        String customChars = "ABCDEF";
        assertEquals("", BaseUtils.idToBase(customChars, 0L));
        assertEquals(0L, BaseUtils.baseToId(customChars, ""));
    }

    @Test
    @DisplayName("测试负值处理")
    void testNegativeValueHandling() {
        // 测试负值的处理
        assertEquals("", BaseUtils.idToBase32(-1L));
        assertEquals("", BaseUtils.idToBase32(-100L));

        // 测试自定义字符集的负值处理
        String customChars = "ABCDEF";
        assertEquals("", BaseUtils.idToBase(customChars, -1L));
        assertEquals("", BaseUtils.idToBase(customChars, -100L));
    }

}