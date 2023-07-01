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

package net.wenzuo.atom.test.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.wenzuo.atom.test.web.params.TestReq;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author Catch
 * @since 2023-06-06
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/test")
public class TestController {

	@GetMapping("/get")
	public String get() {
		return "你好test";
	}

	@GetMapping("/get/query")
	public TestReq getQuery(TestReq req) {
		return req;
	}

	@GetMapping(value = "/get/file", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<Resource> getFile() throws FileNotFoundException {
		File file = ResourceUtils.getFile("classpath:测试图片.png");
		String contentDisposition = ContentDisposition.builder("attachment")
													  .filename(file.getName())
													  .build()
													  .toString();
		return ResponseEntity.ok()
							 .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
							 .contentType(MediaType.IMAGE_PNG)
							 .body(new FileSystemResource(file));
	}

	@PostMapping("/post/x-www-form-urlencoded")
	public TestReq postFormUrlencoded(TestReq req) {
		return req;
	}

	@PostMapping("/post/json")
	public TestReq postJson(@RequestBody TestReq req) {
		return req;
	}

	@PostMapping("/post/form-data")
	public TestReq postFormData(@RequestParam("file") MultipartFile file, @RequestParam("name") String name) {
		log.info("file: {}", file.getOriginalFilename());
		log.info("file-size: {}", file.getSize());
		TestReq req = new TestReq();
		req.setName(name);
		return req;
	}

}
