/*
 * Copyright (c) 2022-2025 Catch(catchlife6@163.com).
 * Atom is licensed under Mulan PSL v2.
 * You may use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.mindit.atom.api.util;

import cn.mindit.atom.api.BaseTest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ValidatorUtils 测试类
 *
 * @author Catch
 * @since 2025-01-01
 */
class ValidatorUtilsTest extends BaseTest {

    static class TestEntity {
        private String name;
        private Integer age;

        public TestEntity(String name, Integer age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }

    @Test
    void testStaticValidatorsAreNotNull() {
        assertNotNull(ValidatorUtils.VALIDATOR_FAST);
        assertNotNull(ValidatorUtils.VALIDATOR_ALL);
    }

    @Test
    void testValidateWithValidObject() {
        TestEntity entity = new TestEntity("John", 25);
        
        // 应该不抛出异常
        assertDoesNotThrow(() -> ValidatorUtils.validate(validator, entity));
    }

    @Test
    void testValidateWithNullObject() {
        // 验证null对象应该不抛出异常（因为没有验证约束）
        assertDoesNotThrow(() -> ValidatorUtils.validate(validator, null));
    }

    @Test
    void testValidatePropertyWithValidValue() {
        TestEntity entity = new TestEntity("John", 25);
        
        // 应该不抛出异常
        assertDoesNotThrow(() -> ValidatorUtils.validateProperty(validator, entity, "name"));
    }

    @Test
    void testValidatePropertyWithInvalidPropertyName() {
        TestEntity entity = new TestEntity("John", 25);
        
        // 验证不存在的属性应该抛出异常
        ConstraintViolationException exception = assertThrows(
            ConstraintViolationException.class,
            () -> ValidatorUtils.validateProperty(validator, entity, "nonexistent")
        );
        
        assertNotNull(exception);
    }

    @Test
    void testValidateValueWithValidValue() {
        // 应该不抛出异常
        assertDoesNotThrow(() -> ValidatorUtils.validateValue(validator, TestEntity.class, "name", "John"));
    }

    @Test
    void testValidateValueWithInvalidValue() {
        // 验证不存在的属性应该抛出异常
        ConstraintViolationException exception = assertThrows(
            ConstraintViolationException.class,
            () -> ValidatorUtils.validateValue(validator, TestEntity.class, "nonexistent", "value")
        );
        
        assertNotNull(exception);
    }

    @Test
    void testValidateWithGroups() {
        TestEntity entity = new TestEntity("John", 25);
        
        // 测试带验证组的验证
        assertDoesNotThrow(() -> ValidatorUtils.validate(validator, entity, Object.class));
    }

    @Test
    void testValidatePropertyWithGroups() {
        TestEntity entity = new TestEntity("John", 25);
        
        // 测试带验证组的属性验证
        assertDoesNotThrow(() -> ValidatorUtils.validateProperty(validator, entity, "name", Object.class));
    }

    @Test
    void testValidateValueWithGroups() {
        // 测试带验证组的值验证
        assertDoesNotThrow(() -> ValidatorUtils.validateValue(validator, TestEntity.class, "name", "John", Object.class));
    }

    @Test
    void testValidateWithNullValidator() {
        TestEntity entity = new TestEntity("John", 25);
        
        // 测试null验证器应该抛出异常
        assertThrows(NullPointerException.class, () -> ValidatorUtils.validate(null, entity));
    }

    @Test
    void testValidatePropertyWithNullValidator() {
        TestEntity entity = new TestEntity("John", 25);
        
        // 测试null验证器应该抛出异常
        assertThrows(NullPointerException.class, () -> ValidatorUtils.validateProperty(null, entity, "name"));
    }

    @Test
    void testValidateValueWithNullValidator() {
        // 测试null验证器应该抛出异常
        assertThrows(NullPointerException.class, () -> ValidatorUtils.validateValue(null, TestEntity.class, "name", "John"));
    }

    @ParameterizedTest
    @NullSource
    void testValidateWithNullObject(TestEntity entity) {
        // 测试null对象的验证
        assertDoesNotThrow(() -> ValidatorUtils.validate(validator, entity));
    }

    @Test
    void testValidatePropertyWithNullObject() {
        // 测试null对象的属性验证
        assertThrows(IllegalArgumentException.class, () -> ValidatorUtils.validateProperty(validator, null, "name"));
    }

    @Test
    void testValidateValueWithNullClass() {
        // 测试null类的值验证
        assertThrows(NullPointerException.class, () -> ValidatorUtils.validateValue(validator, null, "name", "John"));
    }

    @Test
    void testValidatePropertyWithNullPropertyName() {
        TestEntity entity = new TestEntity("John", 25);
        
        // 测试null属性名的验证
        assertThrows(IllegalArgumentException.class, () -> ValidatorUtils.validateProperty(validator, entity, null));
    }

    @Test
    void testValidateValueWithNullPropertyName() {
        // 测试null属性名的值验证
        assertThrows(IllegalArgumentException.class, () -> ValidatorUtils.validateValue(validator, TestEntity.class, null, "John"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void testValidatePropertyWithEmptyPropertyName(String propertyName) {
        TestEntity entity = new TestEntity("John", 25);
        
        // 测试空属性名的验证
        assertThrows(IllegalArgumentException.class, () -> ValidatorUtils.validateProperty(validator, entity, propertyName));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void testValidateValueWithEmptyPropertyName(String propertyName) {
        // 测试空属性名的值验证
        assertThrows(IllegalArgumentException.class, () -> ValidatorUtils.validateValue(validator, TestEntity.class, propertyName, "John"));
    }

    @Test
    void testValidateWithMultipleGroups() {
        TestEntity entity = new TestEntity("John", 25);
        
        // 测试多个验证组
        assertDoesNotThrow(() -> ValidatorUtils.validate(validator, entity, Object.class, String.class, Integer.class));
    }

    @Test
    void testValidatePropertyWithMultipleGroups() {
        TestEntity entity = new TestEntity("John", 25);
        
        // 测试多个验证组的属性验证
        assertDoesNotThrow(() -> ValidatorUtils.validateProperty(validator, entity, "name", Object.class, String.class));
    }

    @Test
    void testValidateValueWithMultipleGroups() {
        // 测试多个验证组的值验证
        assertDoesNotThrow(() -> ValidatorUtils.validateValue(validator, TestEntity.class, "name", "John", Object.class, String.class));
    }

    @Test
    void testStaticValidatorsAreDifferent() {
        // 验证两个静态验证器是不同的实例
        assertNotEquals(ValidatorUtils.VALIDATOR_FAST, ValidatorUtils.VALIDATOR_ALL);
    }

    @Test
    void testValidatorPerformance() {
        TestEntity entity = new TestEntity("John", 25);
        
        // 测试验证性能
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < 1000; i++) {
            ValidatorUtils.validate(validator, entity);
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        assertTrue(duration < 1000, "验证性能测试失败，耗时: " + duration + "ms");
    }

    static Stream<TestEntity> provideTestEntities() {
        return Stream.of(
            new TestEntity("John", 25),
            new TestEntity("Jane", 30),
            new TestEntity("Bob", 35)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestEntities")
    void testValidateWithVariousEntities(TestEntity entity) {
        // 测试各种不同的实体
        assertDoesNotThrow(() -> ValidatorUtils.validate(validator, entity));
    }

    @Test
    void testValidatePropertyWithDifferentProperties() {
        TestEntity entity = new TestEntity("John", 25);
        
        // 测试不同的属性
        assertDoesNotThrow(() -> ValidatorUtils.validateProperty(validator, entity, "name"));
        assertDoesNotThrow(() -> ValidatorUtils.validateProperty(validator, entity, "age"));
    }

    @Test
    void testValidateValueWithDifferentProperties() {
        // 测试不同的属性值
        assertDoesNotThrow(() -> ValidatorUtils.validateValue(validator, TestEntity.class, "name", "John"));
        assertDoesNotThrow(() -> ValidatorUtils.validateValue(validator, TestEntity.class, "age", 25));
    }
}