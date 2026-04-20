package cn.mindit.atom.mybatisplus.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.boot.autoconfigure.AutoConfiguration;

/**
 * @author Catch
 * @since 2023-06-06
 */
@Import(FillMetaObjectHandler.class)
@RequiredArgsConstructor
@EnableConfigurationProperties(MybatisPlusProperties.class)
@PropertySource("classpath:application-mybatis-plus.properties")
@ConditionalOnProperty(value = "atom.mybatis-plus.enabled", matchIfMissing = true)
@AutoConfiguration
public class MybatisPlusAutoConfiguration {

    @ConditionalOnProperty(value = "atom.mybatis-plus.pagination", matchIfMissing = true)
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        return interceptor;
    }

}
