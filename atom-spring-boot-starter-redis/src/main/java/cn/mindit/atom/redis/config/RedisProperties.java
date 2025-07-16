/*
 * Copyright (c) 2022-2025 Catch(catchlife6@163.com).
 * Atom is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.mindit.atom.redis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * @author Catch
 * @since 2023-06-06
 */
@Data
@ConfigurationProperties(prefix = "atom.redis")
public class RedisProperties {

    /**
     * 是否启用 redis 模块
     */
    private Boolean enabled = true;

    /**
     * 是否启用 redisTemplate
     */
    private Boolean redisTemplate = true;

    /**
     * 是否启用 RedisService
     */
    private Boolean redisService = true;

    /**
     * 是否启用 CacheService
     */
    private Boolean cacheService = true;
    /**
     * Cache超时时间，默认 30 分钟
     */
    private Duration cacheServiceTimeout = Duration.ofMinutes(30);

    /**
     * key 前缀
     */
    private String prefix;
    /**
     * key 分隔符
     */
    private String delimiter = ":";

    public String getPrefix() {
        if (prefix == null) {
            return "";
        }
        if (prefix.endsWith(delimiter)) {
            return prefix;
        }
        return prefix + delimiter;
    }

}
