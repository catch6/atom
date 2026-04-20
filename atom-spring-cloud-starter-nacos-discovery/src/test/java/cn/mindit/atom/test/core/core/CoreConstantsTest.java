package cn.mindit.atom.test.core.core;

import cn.mindit.atom.core.core.CoreConstants;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CoreConstantsTest {

    @Test
    void traceIdKeyIsStable() {
        assertThat(CoreConstants.TRACE_ID).isEqualTo("Trace-Id");
    }

}
