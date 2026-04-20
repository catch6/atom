package cn.mindit.atom.test.redis;

import lombok.extern.slf4j.Slf4j;
import cn.mindit.atom.core.util.JsonUtils;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

/**
 * @author Catch
 * @since 2024-11-21
 */
@Slf4j
public class CaseTest {

    @Test
    public void test() {
        String obj1 = "abc";
        Integer obj2 = 11;
        Long obj3 = 111L;
        Double obj4 = 111.11;
        BigDecimal obj5 = new BigDecimal("111.11");
        log.info("obj1 = {}", JsonUtils.toObject("abc", String.class));
        log.info("obj2 = {}", JsonUtils.toObject(JsonUtils.toJson(obj2), Object.class).getClass());
        log.info("obj3 = {}", JsonUtils.toObject(JsonUtils.toJson(obj3), Object.class).getClass());
        log.info("obj4 = {}", JsonUtils.toObject(JsonUtils.toJson(obj4), Object.class).getClass());
        log.info("obj5 = {}", JsonUtils.toObject(JsonUtils.toJson(obj5), Object.class).getClass());
    }

}
