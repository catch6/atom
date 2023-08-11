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

package net.wenzuo.atom.feign.config;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import net.wenzuo.atom.core.util.Result;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
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
	 * 第三方服务调用异常处理
	 *
	 * @param e 异常对象
	 * @return Result
	 */
	@ResponseStatus(HttpStatus.BAD_GATEWAY)
	@ExceptionHandler(ThirdException.class)
	public Result<?> handler(ThirdException e) {
		log.error(e.getMessage(), e);
		return Result.fail(e);
	}

	/**
	 * 第三方服务调用异常处理
	 *
	 * @param e 异常对象
	 * @return Result
	 */
	@ResponseStatus(HttpStatus.BAD_GATEWAY)
	@ExceptionHandler(FeignException.class)
	public Result<?> handler(FeignException e) {
		log.error(e.getMessage(), e);
		return Result.fail(ThirdException.DEFAULT_CODE, ThirdException.DEFAULT_MESSAGE);
	}

}
