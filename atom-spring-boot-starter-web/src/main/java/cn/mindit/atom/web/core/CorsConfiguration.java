/*
 * Copyright (c) 2022-2024 Catch(catchlife6@163.com).
 * Atom is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.mindit.atom.web.core;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author Catch
 * @since 2023-06-06
 */
@RequiredArgsConstructor
@ConditionalOnProperty(value = "atom.web.cors.enabled", matchIfMissing = true)
@Configuration
public class CorsConfiguration {

    private final CorsProperties corsProperties;

    @Bean
    @ConditionalOnMissingBean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        for (CorsProperties.Config config : corsProperties.getConfigs()) {
            org.springframework.web.cors.CorsConfiguration corsConfig = new org.springframework.web.cors.CorsConfiguration();
            corsConfig.setAllowCredentials(config.getAllowCredentials());
            corsConfig.setAllowedOrigins(config.getAllowedOrigins());
            corsConfig.setAllowedOriginPatterns(config.getAllowedOriginPatterns());
            corsConfig.setAllowedHeaders(config.getAllowedHeaders());
            corsConfig.setAllowedMethods(config.getAllowedMethods());
            corsConfig.setExposedHeaders(config.getExposedHeaders());
            source.registerCorsConfiguration(config.getPattern(), corsConfig);
        }
        return new CorsFilter(source);
    }

}
