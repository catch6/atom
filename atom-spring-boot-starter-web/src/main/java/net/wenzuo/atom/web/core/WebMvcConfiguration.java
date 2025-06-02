package net.wenzuo.atom.web.core;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Catch
 * @since 2024-01-11
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final TraceIdInterceptor traceIdInterceptor;
    private final LoggingInterceptor loggingInterceptor;
    private final LoggingProperties loggingProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(traceIdInterceptor)
                .addPathPatterns("/**")
                .order(Ordered.HIGHEST_PRECEDENCE);

        if (loggingProperties.getEnabled()) {
            registry.addInterceptor(loggingInterceptor)
                    .addPathPatterns(loggingProperties.getIncludePath())
                    .excludePathPatterns(loggingProperties.getInternalExcludePath())
                    .excludePathPatterns(loggingProperties.getExcludePath());
        }
    }

}
