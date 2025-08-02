/*
 * Copyright (c) 2022-2025 Catch(catchlife6@163.com).
 * Atom is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.mindit.atom.api.param;

import cn.mindit.atom.api.BaseTest;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * IdDTO 测试类
 *
 * @author Catch
 * @since 2025-01-01
 */
class IdDTOTest extends BaseTest {

    @Test
    void testDefaultConstructor() {
        IdDTO idDTO = new IdDTO();
        assertNull(idDTO.getId());
    }

    @Test
    void testParameterizedConstructor() {
        IdDTO idDTO = new IdDTO(123L);
        assertEquals(123L, idDTO.getId());
    }

    @Test
    void testStaticOfMethod() {
        IdDTO idDTO = IdDTO.of(456L);
        assertEquals(456L, idDTO.getId());
    }

    @Test
    void testValidId() {
        IdDTO idDTO = new IdDTO(123L);
        
        Set<ConstraintViolation<IdDTO>> violations = validator.validate(idDTO);
        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @NullSource
    void testNullIdValidation(Long id) {
        IdDTO idDTO = new IdDTO(id);
        
        Set<ConstraintViolation<IdDTO>> violations = validator.validate(idDTO);
        assertFalse(violations.isEmpty());
        
        ConstraintViolation<IdDTO> violation = violations.iterator().next();
        assertEquals("ID不能为空", violation.getMessage());
        assertEquals("id", violation.getPropertyPath().toString());
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, 1L, 100L, 1000L, Long.MAX_VALUE, Long.MIN_VALUE})
    void testVariousValidIds(Long id) {
        IdDTO idDTO = new IdDTO(id);
        
        Set<ConstraintViolation<IdDTO>> violations = validator.validate(idDTO);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testSetterGetter() {
        IdDTO idDTO = new IdDTO();
        
        idDTO.setId(789L);
        assertEquals(789L, idDTO.getId());
    }

    @Test
    void testEqualsAndHashCode() {
        IdDTO idDTO1 = new IdDTO(123L);
        IdDTO idDTO2 = new IdDTO(123L);
        IdDTO idDTO3 = new IdDTO(456L);
        
        assertEquals(idDTO1, idDTO2);
        assertNotEquals(idDTO1, idDTO3);
        assertEquals(idDTO1.hashCode(), idDTO2.hashCode());
        assertNotEquals(idDTO1.hashCode(), idDTO3.hashCode());
    }

    @ParameterizedTest
    @CsvSource({
            "1, 1",
            "100, 100",
            "999999999, 999999999",
            "-1, -1"
    })
    void testStaticOfMethodWithVariousIds(long inputId, long expectedId) {
        IdDTO idDTO = IdDTO.of(inputId);
        assertEquals(expectedId, idDTO.getId());
    }

    @Test
    void testToString() {
        IdDTO idDTO = new IdDTO(123L);
        String toString = idDTO.toString();
        
        assertTrue(toString.contains("123"));
        assertTrue(toString.contains("IdDTO"));
    }

    @Test
    void testBoundaryValues() {
        IdDTO idDTO1 = new IdDTO(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, idDTO1.getId());
        
        IdDTO idDTO2 = new IdDTO(Long.MIN_VALUE);
        assertEquals(Long.MIN_VALUE, idDTO2.getId());
        
        // 验证边界值的验证
        Set<ConstraintViolation<IdDTO>> violations1 = validator.validate(idDTO1);
        assertTrue(violations1.isEmpty());
        
        Set<ConstraintViolation<IdDTO>> violations2 = validator.validate(idDTO2);
        assertTrue(violations2.isEmpty());
    }

    @Test
    void testMultipleValidationFailures() {
        IdDTO idDTO = new IdDTO(null);
        
        Set<ConstraintViolation<IdDTO>> violations = validator.validate(idDTO);
        assertEquals(1, violations.size());
        
        ConstraintViolation<IdDTO> violation = violations.iterator().next();
        assertEquals("id", violation.getPropertyPath().toString());
        assertEquals("ID不能为空", violation.getMessage());
    }

    @Test
    void testValidationGroups() {
        IdDTO idDTO = new IdDTO(null);
        
        // 测试默认验证组
        Set<ConstraintViolation<IdDTO>> violations = validator.validate(idDTO);
        assertFalse(violations.isEmpty());
    }
}