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

package cn.mindit.atom.api.param;

import cn.mindit.atom.api.BaseTest;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PageDTO 测试类
 *
 * @author Catch
 * @since 2025-01-01
 */
class PageDTOTest extends BaseTest {

    @Test
    void testDefaultConstructor() {
        PageDTO pageDTO = new PageDTO();
        
        assertEquals(1L, pageDTO.getPageNo());
        assertEquals(15L, pageDTO.getPageSize());
    }

    @Test
    void testParameterizedConstructor() {
        PageDTO pageDTO = new PageDTO(2L, 20L);
        
        assertEquals(2L, pageDTO.getPageNo());
        assertEquals(20L, pageDTO.getPageSize());
    }

    @Test
    void testToPage() {
        PageDTO pageDTO = new PageDTO(3L, 10L);
        Page<String> page = pageDTO.toPage();
        
        assertEquals(3L, page.getCurrent());
        assertEquals(10L, page.getSize());
    }

    @Test
    void testStaticOfMethod() {
        PageDTO pageDTO = PageDTO.of();
        
        assertEquals(1L, pageDTO.getPageNo());
        assertEquals(15L, pageDTO.getPageSize());
    }

    @Test
    void testStaticOfMethodWithParameters() {
        PageDTO pageDTO = PageDTO.of(5L, 25L);
        
        assertEquals(5L, pageDTO.getPageNo());
        assertEquals(25L, pageDTO.getPageSize());
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 10L, 100L, 1000L})
    void testValidPageNo(long pageNo) {
        PageDTO pageDTO = new PageDTO(pageNo, 15L);
        assertEquals(pageNo, pageDTO.getPageNo());
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 10L, 50L, 100L, -1L})
    void testValidPageSize(long pageSize) {
        PageDTO pageDTO = new PageDTO(1L, pageSize);
        assertEquals(pageSize, pageDTO.getPageSize());
    }

    @Test
    void testBoundaryValues() {
        // 测试边界值
        PageDTO pageDTO1 = new PageDTO(Long.MAX_VALUE, Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, pageDTO1.getPageNo());
        assertEquals(Long.MAX_VALUE, pageDTO1.getPageSize());
        
        PageDTO pageDTO2 = new PageDTO(Long.MIN_VALUE, Long.MIN_VALUE);
        assertEquals(Long.MIN_VALUE, pageDTO2.getPageNo());
        assertEquals(Long.MIN_VALUE, pageDTO2.getPageSize());
    }

    @Test
    void testSettersAndGetters() {
        PageDTO pageDTO = new PageDTO();
        
        // 测试 pageNo setter
        pageDTO.setPageNo(10L);
        assertEquals(10L, pageDTO.getPageNo());
        
        // 测试 pageSize setter
        pageDTO.setPageSize(30L);
        assertEquals(30L, pageDTO.getPageSize());
    }

    @Test
    void testEqualsAndHashCode() {
        PageDTO pageDTO1 = new PageDTO(1L, 15L);
        PageDTO pageDTO2 = new PageDTO(1L, 15L);
        PageDTO pageDTO3 = new PageDTO(2L, 15L);
        
        assertEquals(pageDTO1, pageDTO2);
        assertNotEquals(pageDTO1, pageDTO3);
        assertEquals(pageDTO1.hashCode(), pageDTO2.hashCode());
        assertNotEquals(pageDTO1.hashCode(), pageDTO3.hashCode());
    }

    @ParameterizedTest
    @CsvSource({
            "1, 10",
            "2, 20",
            "5, 50",
            "10, 100"
    })
    void testToPageWithVariousParameters(long pageNo, long pageSize) {
        PageDTO pageDTO = new PageDTO(pageNo, pageSize);
        Page<String> page = pageDTO.toPage();
        
        assertEquals(pageNo, page.getCurrent());
        assertEquals(pageSize, page.getSize());
    }
}