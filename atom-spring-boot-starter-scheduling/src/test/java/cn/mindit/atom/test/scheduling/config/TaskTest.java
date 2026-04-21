package cn.mindit.atom.test.scheduling.config;

import cn.mindit.atom.scheduling.config.Task;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class TaskTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void noArgConstructor_createsEmptyTask() {
        Task task = new Task();
        assertThat(task.getId()).isNull();
        assertThat(task.getCron()).isNull();
        assertThat(task.getClazz()).isNull();
        assertThat(task.getMethod()).isNull();
        assertThat(task.getParam()).isNull();
    }

    @Test
    void fourArgConstructor_setsFieldsWithoutParam() {
        Task task = new Task("t1", "0/5 * * * * ?", "com.example.MyBean", "run");
        assertThat(task.getId()).isEqualTo("t1");
        assertThat(task.getCron()).isEqualTo("0/5 * * * * ?");
        assertThat(task.getClazz()).isEqualTo("com.example.MyBean");
        assertThat(task.getMethod()).isEqualTo("run");
        assertThat(task.getParam()).isNull();
    }

    @Test
    void fiveArgConstructor_setsAllFields() {
        Task task = new Task("t1", "0/5 * * * * ?", "com.example.MyBean", "run", "hello");
        assertThat(task.getParam()).isEqualTo("hello");
    }

    @Test
    void validation_allFieldsPresent_noViolations() {
        Task task = new Task("t1", "0/5 * * * * ?", "com.example.MyBean", "run");
        Set<ConstraintViolation<Task>> violations = validator.validate(task);
        assertThat(violations).isEmpty();
    }

    @Test
    void validation_blankId_hasViolation() {
        Task task = new Task("", "0/5 * * * * ?", "com.example.MyBean", "run");
        Set<ConstraintViolation<Task>> violations = validator.validate(task);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("id");
    }

    @Test
    void validation_blankCron_hasViolation() {
        Task task = new Task("t1", " ", "com.example.MyBean", "run");
        Set<ConstraintViolation<Task>> violations = validator.validate(task);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("cron");
    }

    @Test
    void validation_blankClazz_hasViolation() {
        Task task = new Task("t1", "0/5 * * * * ?", "", "run");
        Set<ConstraintViolation<Task>> violations = validator.validate(task);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("clazz");
    }

    @Test
    void validation_blankMethod_hasViolation() {
        Task task = new Task("t1", "0/5 * * * * ?", "com.example.MyBean", "");
        Set<ConstraintViolation<Task>> violations = validator.validate(task);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("method");
    }

    @Test
    void validation_allFieldsBlank_hasMultipleViolations() {
        Task task = new Task();
        Set<ConstraintViolation<Task>> violations = validator.validate(task);
        assertThat(violations).hasSize(4);
    }

    @Test
    void settersAndGetters_workCorrectly() {
        Task task = new Task();
        task.setId("id1");
        task.setCron("0 0 * * * ?");
        task.setClazz("com.example.Foo");
        task.setMethod("bar");
        task.setParam("baz");

        assertThat(task.getId()).isEqualTo("id1");
        assertThat(task.getCron()).isEqualTo("0 0 * * * ?");
        assertThat(task.getClazz()).isEqualTo("com.example.Foo");
        assertThat(task.getMethod()).isEqualTo("bar");
        assertThat(task.getParam()).isEqualTo("baz");
    }

}
