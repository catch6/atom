package cn.mindit.atom.test.core.util;

import cn.mindit.atom.core.util.Code;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CodeTest {

    enum Status implements Code {
        OK(0),
        WARN(1),
        ERROR(2);

        private final Integer code;

        Status(Integer code) {
            this.code = code;
        }

        @Override
        public Integer getCode() {
            return code;
        }
    }

    @Test
    void getInstanceReturnsMatchingEnumConstant() {
        assertThat(Code.getInstance(Status.class, 0)).isEqualTo(Status.OK);
        assertThat(Code.getInstance(Status.class, 1)).isEqualTo(Status.WARN);
        assertThat(Code.getInstance(Status.class, 2)).isEqualTo(Status.ERROR);
    }

    @Test
    void getInstanceReturnsNullWhenCodeUnknown() {
        assertThat(Code.getInstance(Status.class, 999)).isNull();
    }

    @Test
    void getInstanceCachesTheLookupMap() {
        Status first = Code.getInstance(Status.class, 1);
        Status second = Code.getInstance(Status.class, 1);
        assertThat(first).isSameAs(second);
        assertThat(Code.CLASS_CODE_MAP).containsKey(Status.class);
    }

}
