package cn.mindit.atom.test.api.param;

import cn.mindit.atom.api.param.PageVO;
import org.junit.jupiter.api.Test;

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

}
