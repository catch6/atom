package cn.mindit.atom.test.core.util;

import cn.mindit.atom.core.util.BusinessException;
import cn.mindit.atom.core.util.Result;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ResultTest {

    @Test
    void okWithoutDataUsesDefaults() {
        Result<Object> result = Result.ok();
        assertThat(result.getCode()).isEqualTo(200);
        assertThat(result.getMessage()).isEqualTo("成功");
        assertThat(result.getData()).isNull();
    }

    @Test
    void okWithDataPreservesData() {
        Result<String> result = Result.ok("hello");
        assertThat(result.getCode()).isEqualTo(200);
        assertThat(result.getMessage()).isEqualTo("成功");
        assertThat(result.getData()).isEqualTo("hello");
    }

    @Test
    void failWithCodeAndMessage() {
        Result<Object> result = Result.fail(4001, "参数错误");
        assertThat(result.getCode()).isEqualTo(4001);
        assertThat(result.getMessage()).isEqualTo("参数错误");
        assertThat(result.getData()).isNull();
    }

    @Test
    void failWithProviderReadsCodeAndMessage() {
        BusinessException provider = new BusinessException(5000, "下游不可用");
        Result<Object> result = Result.fail(provider);
        assertThat(result.getCode()).isEqualTo(5000);
        assertThat(result.getMessage()).isEqualTo("下游不可用");
        assertThat(result.getData()).isNull();
    }

    @Test
    void allArgsConstructorSetsAllFields() {
        Result<Integer> result = new Result<>(201, "created", 42);
        assertThat(result.getCode()).isEqualTo(201);
        assertThat(result.getMessage()).isEqualTo("created");
        assertThat(result.getData()).isEqualTo(42);
    }

    @Test
    void noArgsConstructorCreatesEmptyResult() {
        Result<Object> result = new Result<>();
        assertThat(result.getCode()).isZero();
        assertThat(result.getMessage()).isNull();
        assertThat(result.getData()).isNull();
    }

    @Test
    void resultImplementsResultProvider() {
        Result<Object> result = Result.ok();
        assertThat(result.getCode()).isEqualTo(200);
        assertThat(result.getMessage()).isEqualTo("成功");
    }

}
