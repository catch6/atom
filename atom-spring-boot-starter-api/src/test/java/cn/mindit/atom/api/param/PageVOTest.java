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
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PageVO 测试类
 *
 * @author Catch
 * @since 2025-01-01
 */
class PageVOTest extends BaseTest {

    private Page<String> testPage;
    private List<String> testData;

    @BeforeEach
    void setUp() {
        testData = Arrays.asList("item1", "item2", "item3", "item4", "item5");
        testPage = new Page<>(2L, 10L);
        testPage.setRecords(testData);
        testPage.setTotal(50L);
        testPage.setPages(5L);
    }

    @Test
    void testDefaultConstructor() {
        PageVO<String> pageVO = new PageVO<>();
        
        assertEquals(1L, pageVO.getPageNo());
        assertEquals(15L, pageVO.getPageSize());
        assertEquals(0L, pageVO.getTotalPage());
        assertEquals(0L, pageVO.getTotalRow());
        assertTrue(pageVO.getItems().isEmpty());
    }

    @Test
    void testParameterizedConstructor() {
        List<String> items = Arrays.asList("test1", "test2");
        PageVO<String> pageVO = new PageVO<>(2L, 20L, 3L, 50L, items);
        
        assertEquals(2L, pageVO.getPageNo());
        assertEquals(20L, pageVO.getPageSize());
        assertEquals(3L, pageVO.getTotalPage());
        assertEquals(50L, pageVO.getTotalRow());
        assertEquals(items, pageVO.getItems());
    }

    @Test
    void testStaticOfMethod() {
        PageVO<String> pageVO = PageVO.of();
        
        assertEquals(1L, pageVO.getPageNo());
        assertEquals(15L, pageVO.getPageSize());
        assertEquals(0L, pageVO.getTotalPage());
        assertEquals(0L, pageVO.getTotalRow());
        assertTrue(pageVO.getItems().isEmpty());
    }

    @Test
    void testStaticOfMethodWithPage() {
        PageVO<String> pageVO = PageVO.of(testPage);
        
        assertEquals(2L, pageVO.getPageNo());
        assertEquals(10L, pageVO.getPageSize());
        assertEquals(5L, pageVO.getTotalPage());
        assertEquals(50L, pageVO.getTotalRow());
        assertEquals(testData, pageVO.getItems());
    }

    @Test
    void testStaticOfMethodWithPageAndFunction() {
        PageVO<Integer> pageVO = PageVO.of(testPage, String::length);
        
        assertEquals(2L, pageVO.getPageNo());
        assertEquals(10L, pageVO.getPageSize());
        assertEquals(5L, pageVO.getTotalPage());
        assertEquals(50L, pageVO.getTotalRow());
        
        List<Integer> expectedLengths = Arrays.asList(5, 5, 5, 5, 5);
        assertEquals(expectedLengths, pageVO.getItems());
    }

    @Test
    void testStaticOfMethodWithNegativePageSize() {
        Page<String> negativeSizePage = new Page<>(1L, -1L);
        negativeSizePage.setRecords(testData);
        negativeSizePage.setTotal(-1L);
        negativeSizePage.setPages(1L);
        
        PageVO<String> pageVO = PageVO.of(negativeSizePage);
        
        assertEquals(1L, pageVO.getPageNo());
        assertEquals(-1L, pageVO.getPageSize());
        assertEquals(1L, pageVO.getTotalPage());
        assertEquals(5L, pageVO.getTotalRow()); // 应该使用 records.size()
        assertEquals(testData, pageVO.getItems());
    }

    @Test
    void testSettersAndGetters() {
        PageVO<String> pageVO = new PageVO<>();
        List<String> items = Arrays.asList("test1", "test2");
        
        pageVO.setPageNo(3L);
        pageVO.setPageSize(25L);
        pageVO.setTotalPage(10L);
        pageVO.setTotalRow(250L);
        pageVO.setItems(items);
        
        assertEquals(3L, pageVO.getPageNo());
        assertEquals(25L, pageVO.getPageSize());
        assertEquals(10L, pageVO.getTotalPage());
        assertEquals(250L, pageVO.getTotalRow());
        assertEquals(items, pageVO.getItems());
    }

    @Test
    void testEqualsAndHashCode() {
        List<String> items = Arrays.asList("test1", "test2");
        PageVO<String> pageVO1 = new PageVO<>(1L, 10L, 2L, 20L, items);
        PageVO<String> pageVO2 = new PageVO<>(1L, 10L, 2L, 20L, items);
        PageVO<String> pageVO3 = new PageVO<>(2L, 10L, 2L, 20L, items);
        
        assertEquals(pageVO1, pageVO2);
        assertNotEquals(pageVO1, pageVO3);
        assertEquals(pageVO1.hashCode(), pageVO2.hashCode());
        assertNotEquals(pageVO1.hashCode(), pageVO3.hashCode());
    }

    @Test
    void testFunctionTransformation() {
        PageVO<String> pageVO = PageVO.of(testPage, String::toUpperCase);
        
        List<String> expected = Arrays.asList("ITEM1", "ITEM2", "ITEM3", "ITEM4", "ITEM5");
        assertEquals(expected, pageVO.getItems());
    }

    @Test
    void testEmptyPage() {
        Page<String> emptyPage = new Page<>(1L, 10L);
        emptyPage.setRecords(Arrays.asList());
        emptyPage.setTotal(0L);
        emptyPage.setPages(0L);
        
        PageVO<String> pageVO = PageVO.of(emptyPage);
        
        assertEquals(1L, pageVO.getPageNo());
        assertEquals(10L, pageVO.getPageSize());
        assertEquals(0L, pageVO.getTotalPage());
        assertEquals(0L, pageVO.getTotalRow());
        assertTrue(pageVO.getItems().isEmpty());
    }

    @Test
    void testNullItems() {
        PageVO<String> pageVO = new PageVO<>();
        pageVO.setItems(null);
        
        assertNull(pageVO.getItems());
    }

    @Test
    void testBoundaryValues() {
        List<String> items = Arrays.asList("test");
        PageVO<String> pageVO = new PageVO<>(Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE, items);
        
        assertEquals(Long.MAX_VALUE, pageVO.getPageNo());
        assertEquals(Long.MAX_VALUE, pageVO.getPageSize());
        assertEquals(Long.MAX_VALUE, pageVO.getTotalPage());
        assertEquals(Long.MAX_VALUE, pageVO.getTotalRow());
        assertEquals(items, pageVO.getItems());
    }
}