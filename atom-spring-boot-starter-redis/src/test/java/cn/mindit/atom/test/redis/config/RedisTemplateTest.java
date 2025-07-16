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

package cn.mindit.atom.test.redis.config;

import jakarta.annotation.Resource;
import cn.mindit.atom.redis.service.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Catch
 * @since 2023-07-21
 */
@SpringBootTest
class RedisTemplateTest {

    @Resource
    private RedisService redisService;

    @Test
    void test() {
        redisService.set("int", 1);
        redisService.set("int2", 1000000);
        redisService.set("long", 1L);
        redisService.set("long2", 1000000000000000L);
        redisService.set("string", "test");

        Integer i = redisService.get("int", Integer.class);
        System.out.println(i);

        Integer i2 = redisService.get("int2", Integer.class);
        System.out.println(i2);

        Long l = redisService.get("long", Long.class);
        System.out.println(l);

        String s = redisService.get("string", String.class);
        System.out.println(s);

    }

}
