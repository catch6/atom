package cn.mindit.atom.test.mybatisplus.config;

import cn.mindit.atom.mybatisplus.config.FillMetaObjectHandler;
import cn.mindit.atom.mybatisplus.config.MybatisPlusAutoConfiguration;
import cn.mindit.atom.mybatisplus.config.MybatisPlusProperties;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class MybatisPlusAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(MybatisPlusAutoConfiguration.class));

    @Test
    void autoConfigurationLoads_byDefault() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(MybatisPlusAutoConfiguration.class);
            assertThat(context).hasSingleBean(MybatisPlusProperties.class);
        });
    }

    @Test
    void autoConfigurationDisabled_whenPropertyFalse() {
        contextRunner
                .withPropertyValues("atom.mybatis-plus.enabled=false")
                .run(context -> {
                    assertThat(context).doesNotHaveBean(MybatisPlusAutoConfiguration.class);
                    assertThat(context).doesNotHaveBean(MybatisPlusProperties.class);
                });
    }

    @Test
    void paginationInterceptor_enabledByDefault() {
        contextRunner.run(context ->
                assertThat(context).hasSingleBean(MybatisPlusInterceptor.class));
    }

    @Test
    void paginationInterceptor_disabled() {
        contextRunner
                .withPropertyValues("atom.mybatis-plus.pagination=false")
                .run(context ->
                        assertThat(context).doesNotHaveBean(MybatisPlusInterceptor.class));
    }

    @Test
    void autoFill_disabledByDefault() {
        contextRunner.run(context ->
                assertThat(context).doesNotHaveBean(FillMetaObjectHandler.class));
    }

    @Test
    void autoFill_enabledWhenPropertyTrue() {
        contextRunner
                .withPropertyValues("atom.mybatis-plus.auto-fill=true")
                .run(context ->
                        assertThat(context).hasSingleBean(FillMetaObjectHandler.class));
    }

    @Test
    void defaultPropertyValues() {
        contextRunner.run(context -> {
            MybatisPlusProperties properties = context.getBean(MybatisPlusProperties.class);
            assertThat(properties.isEnabled()).isTrue();
            assertThat(properties.isPagination()).isTrue();
            assertThat(properties.getAutoFill()).isFalse();
            assertThat(properties.getCreateTimeField()).isEqualTo("createTime");
            assertThat(properties.getUpdateTimeField()).isEqualTo("updateTime");
        });
    }

    @Test
    void customPropertyValues() {
        contextRunner
                .withPropertyValues(
                        "atom.mybatis-plus.auto-fill=true",
                        "atom.mybatis-plus.create-time-field=createdAt",
                        "atom.mybatis-plus.update-time-field=updatedAt"
                )
                .run(context -> {
                    MybatisPlusProperties properties = context.getBean(MybatisPlusProperties.class);
                    assertThat(properties.getAutoFill()).isTrue();
                    assertThat(properties.getCreateTimeField()).isEqualTo("createdAt");
                    assertThat(properties.getUpdateTimeField()).isEqualTo("updatedAt");
                });
    }

}
