package cn.mindit.atom.test.mybatisplus.util;

import cn.mindit.atom.api.param.PageDTO;
import cn.mindit.atom.api.param.PageVO;
import cn.mindit.atom.mybatisplus.util.PageUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PageUtilsTest {

    @Test
    void toPage_convertsPageDTOToMybatisPlusPage() {
        PageDTO dto = new PageDTO();
        dto.setPageNo(2);
        dto.setPageSize(20);

        Page<Object> page = PageUtils.toPage(dto);

        assertThat(page.getCurrent()).isEqualTo(2);
        assertThat(page.getSize()).isEqualTo(20);
    }

    @Test
    void toPage_usesDefaultValues() {
        PageDTO dto = PageDTO.of();

        Page<Object> page = PageUtils.toPage(dto);

        assertThat(page.getCurrent()).isEqualTo(1);
        assertThat(page.getSize()).isEqualTo(15);
    }

    @Test
    void toPageVO_convertsPageToPageVO() {
        Page<String> page = new Page<>(1, 10);
        page.setTotal(25);
        page.setRecords(List.of("a", "b", "c"));

        PageVO<String> vo = PageUtils.toPageVO(page);

        assertThat(vo.getPageNo()).isEqualTo(1);
        assertThat(vo.getPageSize()).isEqualTo(10);
        assertThat(vo.getTotalRow()).isEqualTo(25);
        assertThat(vo.getTotalPage()).isEqualTo(3);
        assertThat(vo.getItems()).containsExactly("a", "b", "c");
    }

    @Test
    void toPageVO_withNegativePageSize_usesRecordCount() {
        Page<String> page = new Page<>(1, -1);
        page.setTotal(0);
        page.setRecords(List.of("a", "b"));

        PageVO<String> vo = PageUtils.toPageVO(page);

        assertThat(vo.getTotalRow()).isEqualTo(2);
    }

    @Test
    void toPageVO_withMapper_transformsRecords() {
        Page<Integer> page = new Page<>(1, 10);
        page.setTotal(3);
        page.setRecords(List.of(1, 2, 3));

        PageVO<String> vo = PageUtils.toPageVO(page, String::valueOf);

        assertThat(vo.getItems()).containsExactly("1", "2", "3");
        assertThat(vo.getTotalRow()).isEqualTo(3);
    }

    @Test
    void toPageVO_emptyPage() {
        Page<String> page = new Page<>(1, 10);
        page.setTotal(0);
        page.setRecords(List.of());

        PageVO<String> vo = PageUtils.toPageVO(page);

        assertThat(vo.getTotalRow()).isZero();
        assertThat(vo.getTotalPage()).isZero();
        assertThat(vo.getItems()).isEmpty();
    }

}
