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

package net.wenzuo.atom.test.redis.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.wenzuo.atom.redis.service.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Catch
 * @since 2023-06-15
 */
@Slf4j
@SpringBootTest
class RedisServiceTest {

	@Resource
	private RedisService redisService;

	@Test
	void set() {
		List<Item> items = new ArrayList<>();
		items.add(new Item("test", 1));
		items.add(new Item("test", 2));
		redisService.set("items", items);
		List<Item> list = redisService.get("items", List.class, Item.class);
		list.forEach(item -> log.info("item:{}", item));

		Map<String, Item> itemMap = new HashMap<>();
		itemMap.put("item1", new Item("test", 1));
		itemMap.put("item2", new Item("test", 2));
		redisService.set("itemMap", itemMap);
		Map<String, Item> map = redisService.get("itemMap", Map.class, String.class, Item.class);
		map.forEach((key, value) -> log.info("key:{},value:{}", key, value));
	}

	@Test
	void testSet() {
	}

	@Test
	void get() {
	}

	@Test
	void testGet() {
	}

	@Test
	void del() {
	}

	@Test
	void testDel() {
	}

	@Test
	void expire() {
	}

	@Test
	void getExpire() {
	}

	@Test
	void hasKey() {
	}

	@Test
	void incr() {
	}

	@Test
	void decr() {
	}

	@AllArgsConstructor
	@Data
	private static class Item {

		private String name;
		private Integer age;

	}

}