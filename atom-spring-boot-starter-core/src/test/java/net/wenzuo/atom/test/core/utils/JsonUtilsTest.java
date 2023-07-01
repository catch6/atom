/*
 * Copyright (c) 2022-2023 Catch
 * [Atom] is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package net.wenzuo.atom.test.core.utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.wenzuo.atom.core.utils.JsonUtils;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Catch
 * @since 2023-06-06
 */
@Slf4j
class JsonUtilsTest {

    @Test
    void toJson() {
        VO vo = new VO();
        vo.setDateTime(LocalDateTime.now());
        vo.setDate(LocalDate.now());
        vo.setTime(LocalTime.now());
        String result = JsonUtils.toJson(vo);
        log.info("result: {}", result);
    }

    @Test
    void toObject() {
        List<VO> list = new ArrayList<>();
        VO vo1 = new VO();
        vo1.setDateTime(LocalDateTime.now());
        vo1.setDate(LocalDate.now());
        vo1.setTime(LocalTime.now());
        list.add(vo1);
        VO vo2 = new VO();
        vo2.setDateTime(LocalDateTime.now());
        vo2.setDate(LocalDate.now());
        vo2.setTime(LocalTime.now());
        list.add(vo2);
        String jsonString = JsonUtils.toJson(list);
        List<VO> vos = JsonUtils.toObject(jsonString, List.class, VO.class);
        Set<VO> voSet = JsonUtils.toObject(jsonString, Set.class, VO.class);
        log.info("vos: {}", vos);
        log.info("voSet: {}", voSet);
    }

    @Test
    void toPrettyJson() {
        VO vo = new VO();
        vo.setDateTime(LocalDateTime.now());
        vo.setDate(LocalDate.now());
        vo.setTime(LocalTime.now());
        String result = JsonUtils.toPrettyJson(vo);
        log.info("result: {}", result);
    }

    @Data
    static class VO {

        private LocalDateTime dateTime;
        private LocalDate date;
        private LocalTime time;

    }

}