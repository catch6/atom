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

package net.wenzuo.atom.test.web;

import net.wenzuo.atom.core.utils.JsonUtils;
import net.wenzuo.atom.test.web.params.TestReq;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.ResourceUtils;

import javax.annotation.Resource;
import java.io.FileInputStream;

/**
 * @author Catch
 * @since 2023-06-06
 */
@SpringBootTest
@AutoConfigureMockMvc
class ApplicationTests {

	@Resource
	private MockMvc mockMvc;

	@Test
	void get() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/test/get"))
			   .andExpect(MockMvcResultMatchers.status().isOk())
			   .andDo(MockMvcResultHandlers.print());
	}

	@Test
	void getQuery() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/test/get/query")
											  .queryParam("id", "1")
											  .queryParam("name", "你好test"))
			   .andExpect(MockMvcResultMatchers.status().isOk())
			   .andDo(MockMvcResultHandlers.print());
	}

	@Test
	void getFile() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/test/get/file"))
			   .andExpect(MockMvcResultMatchers.status().isOk())
			   .andDo(MockMvcResultHandlers.print());
	}

	@Test
	void postFormUrlencoded() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/test/post/x-www-form-urlencoded")
											  .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
											  .param("id", "1")
											  .param("name", "你好test"))
			   .andExpect(MockMvcResultMatchers.status().isOk())
			   .andDo(MockMvcResultHandlers.print());
	}

	@Test
	void postJson() throws Exception {
		TestReq req = new TestReq();
		req.setId(1L);
		req.setName("你好test");
		mockMvc.perform(MockMvcRequestBuilders.post("/test/post/json")
											  .contentType(MediaType.APPLICATION_JSON)
											  .content(JsonUtils.toJson(req)))
			   .andExpect(MockMvcResultMatchers.status().isOk())
			   .andDo(MockMvcResultHandlers.print());
	}

	@Test
	void postFormData() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "测试图片.png", MediaType.MULTIPART_FORM_DATA_VALUE, new FileInputStream(ResourceUtils.getFile("classpath:测试图片.png")));
		mockMvc.perform(MockMvcRequestBuilders.multipart("/test/post/form-data")
											  .file(file)
											  .param("name", "你好test"))
			   .andExpect(MockMvcResultMatchers.status().isOk())
			   .andDo(MockMvcResultHandlers.print());
	}

}
