/*
 * Copyright (c) 2022-2023 Catch(catchlife6@163.com).
 * Atom is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package net.wenzuo.atom.test.jwt.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.wenzuo.atom.jwt.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Catch
 * @since 2023-06-06
 */
@Slf4j
@SpringBootTest
class JwtServiceTest {

    @Resource
    private JwtService jwtService;

    @Test
    void sign() {
        String signed = jwtService.sign("test");
        log.info("signed: {}", signed);
    }

    @Test
    void parse() {
        String signed = jwtService.sign("test");
        log.info("signed: {}", signed);
        String parsed = jwtService.parse(signed, String.class);
        log.info("parsed: {}", parsed);
    }

}