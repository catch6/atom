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

package cn.mindit.atom.api.param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PageVOTest {

    @Test
    void ofNoArgsReturnsDefaults() {
        PageVO<String> vo = PageVO.of();
        assertThat(vo.getPageNo()).isEqualTo(1);
        assertThat(vo.getPageSize()).isEqualTo(15);
        assertThat(vo.getTotalPage()).isZero();
        assertThat(vo.getTotalRow()).isZero();
        assertThat(vo.getItems()).isEmpty();
    }

    @Test
    void ofPageCopiesPaginationFields() {
        Page<String> page = new Page<>(2, 10);
        page.setTotal(25);
        page.setRecords(List.of("a", "b", "c"));

        PageVO<String> vo = PageVO.of(page);

        assertThat(vo.getPageNo()).isEqualTo(2);
        assertThat(vo.getPageSize()).isEqualTo(10);
        assertThat(vo.getTotalRow()).isEqualTo(25);
        assertThat(vo.getTotalPage()).isEqualTo(page.getPages());
        assertThat(vo.getItems()).containsExactly("a", "b", "c");
    }

    @Test
    void ofPageWithPageSizeMinusOneUsesRecordsSizeAsTotalRow() {
        Page<String> page = new Page<>(1, -1);
        // 业务约定 pageSize=-1 表示查询全部,此时以实际 records 数量为 totalRow
        page.setRecords(List.of("a", "b", "c", "d"));
        page.setTotal(99); // 即便 total 被外部设置,pageSize<0 时也应忽略

        PageVO<String> vo = PageVO.of(page);

        assertThat(vo.getPageSize()).isEqualTo(-1);
        assertThat(vo.getTotalRow()).isEqualTo(4);
        assertThat(vo.getItems()).containsExactly("a", "b", "c", "d");
    }

    @Test
    void ofPageWithMapperTransformsItems() {
        Page<Integer> page = new Page<>(1, 5);
        page.setTotal(3);
        page.setRecords(List.of(1, 2, 3));

        PageVO<String> vo = PageVO.of(page, i -> "#" + i);

        assertThat(vo.getPageNo()).isEqualTo(1);
        assertThat(vo.getPageSize()).isEqualTo(5);
        assertThat(vo.getTotalRow()).isEqualTo(3);
        assertThat(vo.getItems()).containsExactly("#1", "#2", "#3");
    }

    @Test
    void ofPageWithMapperAndPageSizeMinusOneUsesRecordsSize() {
        Page<Integer> page = new Page<>(1, -1);
        page.setRecords(List.of(10, 20));
        page.setTotal(999);

        PageVO<String> vo = PageVO.of(page, i -> String.valueOf(i * 2));

        assertThat(vo.getTotalRow()).isEqualTo(2);
        assertThat(vo.getItems()).containsExactly("20", "40");
    }

}
