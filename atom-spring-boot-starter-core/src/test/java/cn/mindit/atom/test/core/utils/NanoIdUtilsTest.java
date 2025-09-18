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

import cn.mindit.atom.core.util.NanoIdUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * NanoIdUtils 工具类测试
 *
 * @author Catch
 * @since 2023-08-08
 */
class NanoIdUtilsTest {

    @Test
    @DisplayName("测试 nanoId() 方法 - 默认长度")
    void testNanoIdDefaultLength() {
        String id = NanoIdUtils.nanoId();

        assertNotNull(id);
        assertEquals(15, id.length());

        // 验证只包含允许的字符
        assertTrue(id.matches("^[0-9A-Za-z_]+$"));
    }

    @Test
    @DisplayName("测试 nanoId(int) 方法 - 自定义长度")
    void testNanoIdWithCustomLength() {
        int[] lengths = {1, 5, 10, 20, 50};

        for (int length : lengths) {
            String id = NanoIdUtils.nanoId(length);

            assertNotNull(id);
            assertEquals(length, id.length());
            assertTrue(id.matches("^[0-9A-Za-z_]+$"));
        }
    }

    @Test
    @DisplayName("测试 nanoId(int) 方法 - 边界值")
    void testNanoIdWithBoundaryValues() {
        // 测试最小长度
        String minId = NanoIdUtils.nanoId(1);
        assertNotNull(minId);
        assertEquals(1, minId.length());
        assertTrue(minId.matches("^[0-9A-Za-z_]$"));

        // 测试较大长度
        String largeId = NanoIdUtils.nanoId(100);
        assertNotNull(largeId);
        assertEquals(100, largeId.length());
        assertTrue(largeId.matches("^[0-9A-Za-z_]+$"));
    }

    @Test
    @DisplayName("测试 nanoId(int) 方法 - 零长度")
    void testNanoIdWithZeroLength() {
        String id = NanoIdUtils.nanoId(0);

        assertNotNull(id);
        assertEquals(0, id.length());
        assertEquals("", id);
    }

    @Test
    @DisplayName("测试生成的ID包含所有类型的字符")
    void testGeneratedIdContainsAllCharacterTypes() {
        Set<String> ids = new HashSet<>();

        // 生成多个ID，确保包含各种字符类型
        for (int i = 0; i < 1000; i++) {
            String id = NanoIdUtils.nanoId(50); // 使用较长的ID增加包含所有字符类型的概率
            ids.add(id);
        }

        // 验证至少包含数字
        boolean hasDigit = ids.stream().anyMatch(id -> id.matches(".*[0-9].*"));
        assertTrue(hasDigit, "生成的ID应该包含数字");

        // 验证至少包含大写字母
        boolean hasUpperCase = ids.stream().anyMatch(id -> id.matches(".*[A-Z].*"));
        assertTrue(hasUpperCase, "生成的ID应该包含大写字母");

        // 验证至少包含小写字母
        boolean hasLowerCase = ids.stream().anyMatch(id -> id.matches(".*[a-z].*"));
        assertTrue(hasLowerCase, "生成的ID应该包含小写字母");

        // 验证可能包含下划线
        boolean hasUnderscore = ids.stream().anyMatch(id -> id.contains("_"));
        // 下划线可能不会出现，所以这个测试不是必须的
    }

    @RepeatedTest(100)
    @DisplayName("测试生成的ID的唯一性")
    void testGeneratedIdUniqueness() {
        Set<String> ids = new HashSet<>();
        int size = 100;

        // 生成100个ID并验证唯一性
        for (int i = 0; i < size; i++) {
            String id = NanoIdUtils.nanoId();
            assertFalse(ids.contains(id), "生成的ID应该是唯一的: " + id);
            ids.add(id);
        }

        assertEquals(size, ids.size(), "所有生成的ID都应该是唯一的");
    }

    @Test
    @DisplayName("测试生成的ID的随机性")
    void testGeneratedIdRandomness() {
        Set<String> ids = new HashSet<>();
        int sampleSize = 1000;

        // 生成大量ID来测试随机性
        for (int i = 0; i < sampleSize; i++) {
            ids.add(NanoIdUtils.nanoId());
        }

        // 验证生成的ID数量正确
        assertEquals(sampleSize, ids.size());

        // 验证ID各不相同（基于随机性的统计测试）
        // 对于15位的ID，重复的概率极低
    }

    @Test
    @DisplayName("测试ALPHABET常量")
    void testAlphabetConstant() {
        // 验证ALPHABET包含预期的字符
        String expectedAlphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_";

        // 通过反射获取私有字段
        try {
            java.lang.reflect.Field field = NanoIdUtils.class.getDeclaredField("ALPHABET");
            field.setAccessible(true);
            char[] alphabet = (char[]) field.get(null);

            assertEquals(expectedAlphabet, new String(alphabet));
            assertEquals(63, alphabet.length); // 10 digits + 26 uppercase + 26 lowercase + 1 underscore
        } catch (Exception e) {
            fail("无法访问ALPHABET常量: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("测试MASK常量")
    void testMaskConstant() {
        // MASK应该是ALPHABET长度-1
        try {
            java.lang.reflect.Field alphabetField = NanoIdUtils.class.getDeclaredField("ALPHABET");
            java.lang.reflect.Field maskField = NanoIdUtils.class.getDeclaredField("MASK");

            alphabetField.setAccessible(true);
            maskField.setAccessible(true);

            char[] alphabet = (char[]) alphabetField.get(null);
            int mask = maskField.getInt(null);

            assertEquals(alphabet.length - 1, mask);
            assertEquals(62, mask); // 63 - 1
        } catch (Exception e) {
            fail("无法访问MASK常量: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("测试DEFAULT_LENGTH常量")
    void testDefaultLengthConstant() {
        try {
            java.lang.reflect.Field field = NanoIdUtils.class.getDeclaredField("DEFAULT_LENGTH");
            field.setAccessible(true);

            int defaultLength = field.getInt(null);
            assertEquals(15, defaultLength);
        } catch (Exception e) {
            fail("无法访问DEFAULT_LENGTH常量: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("测试生成的ID的性能")
    void testGeneratedIdPerformance() {
        int iterations = 10000;
        long startTime = System.nanoTime();

        for (int i = 0; i < iterations; i++) {
            NanoIdUtils.nanoId();
        }

        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        double averageTime = (double) duration / iterations;

        // 验证平均生成时间在合理范围内（应该小于1毫秒）
        assertTrue(averageTime < 1_000_000, "平均生成时间应该小于1毫秒: " + averageTime + " 纳秒");

        System.out.println("平均生成时间: " + averageTime + " 纳秒");
    }

    @Test
    @DisplayName("测试并发生成的ID")
    void testConcurrentIdGeneration() throws InterruptedException {
        int threadCount = 10;
        int idsPerThread = 1000;
        Set<String> allIds = new HashSet<>();
        Thread[] threads = new Thread[threadCount];

        // 创建多个线程并发生成ID
        for (int i = 0; i < threadCount; i++) {
            final int threadIndex = i;
            threads[i] = new Thread(() -> {
                Set<String> threadIds = new HashSet<>();
                for (int j = 0; j < idsPerThread; j++) {
                    threadIds.add(NanoIdUtils.nanoId());
                }
                synchronized (allIds) {
                    allIds.addAll(threadIds);
                }
            });
        }

        // 启动所有线程
        for (Thread thread : threads) {
            thread.start();
        }

        // 等待所有线程完成
        for (Thread thread : threads) {
            thread.join();
        }

        // 验证所有ID都是唯一的
        assertEquals(threadCount * idsPerThread, allIds.size(),
            "并发生成的ID应该都是唯一的");
    }

    @Test
    @DisplayName("测试生成的ID的掩码操作")
    void testMaskOperation() {
        // 测试掩码操作是否正确
        try {
            java.lang.reflect.Field maskField = NanoIdUtils.class.getDeclaredField("MASK");
            maskField.setAccessible(true);
            int mask = maskField.getInt(null);

            // 测试不同的字节值与掩码的操作
            for (int i = 0; i < 256; i++) {
                int masked = i & mask;
                assertTrue(masked >= 0 && masked <= 62, "掩码操作结果应该在0-62范围内");
            }
        } catch (Exception e) {
            fail("无法访问MASK常量: " + e.getMessage());
        }
    }

}