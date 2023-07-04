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

package net.wenzuo.atom.feign.config;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Catch
 * @since 2021-06-29
 */
@Slf4j
@RestControllerAdvice
@ConditionalOnProperty(value = "atom.feign.exception-handler", matchIfMissing = true)
public class FeignExceptionHandler {

	/**
	 * openfeign 异常处理,
	 *
	 * @param e 异常对象
	 * @return Result
	 */
	@ExceptionHandler(ThirdException.class)
	public ResponseEntity<String> handler(ThirdException e) {
		if (e.status() < 500) {
			log.warn(e.getMessage(), e);
		} else {
			log.error(e.getMessage(), e);
		}
		return ResponseEntity.status(e.status()).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
	}

	/**
	 * openfeign 异常处理,
	 *
	 * @param e 异常对象
	 * @return Result
	 */
	@ExceptionHandler(FeignException.class)
	public ResponseEntity<String> handler(FeignException e) {
		if (e.status() < 500) {
			log.warn(e.getMessage(), e);
		} else {
			log.error(e.getMessage(), e);
		}
		return ResponseEntity.status(e.status()).contentType(MediaType.TEXT_PLAIN).body("第三方服务异常");
	}

}
