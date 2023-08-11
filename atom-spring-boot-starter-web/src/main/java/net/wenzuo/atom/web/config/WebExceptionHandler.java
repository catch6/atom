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

package net.wenzuo.atom.web.config;

import lombok.extern.slf4j.Slf4j;
import net.wenzuo.atom.core.exception.HttpException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Catch
 * @since 2023-06-06
 */
@Slf4j
@ConditionalOnProperty(value = "atom.web.exception-handler", matchIfMissing = true)
@RestControllerAdvice
public class WebExceptionHandler {

	/**
	 * Http异常错误处理
	 *
	 * @param e 异常对象
	 * @return Result
	 */
	@ExceptionHandler(HttpException.class)
	public ResponseEntity<String> handler(HttpException e) {
		if (e.getStatus() < 500) {
			log.warn(e.getMessage(), e);
		} else {
			log.error(e.getMessage(), e);
		}
		return ResponseEntity.status(e.getStatus()).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
	}

	/**
	 * 请求参数类型不匹配异常处理
	 * 如要 Integer 参数，却传了字符串，且无法转换为 Integer
	 * eg: Failed to convert value of type 'java.lang.String' to required type 'java.lang.Integer';
	 * nested exception is java.lang.NumberFormatException: For input string: "hello"
	 *
	 * @param e 异常对象
	 * @return Result
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<String> handler(MethodArgumentTypeMismatchException e) {
		log.warn(e.getMessage(), e);
		Class<?> type = e.getRequiredType();
		if (type == null) {
			return ResponseEntity.badRequest().contentType(MediaType.TEXT_PLAIN).body("请求参数类型不匹配");
		}
		return ResponseEntity.badRequest().contentType(MediaType.TEXT_PLAIN).body("请求参数类型不匹配");
	}

	/**
	 * 请求参数校验失败异常处理
	 * 当参数有 {@link org.springframework.validation.annotation.Validated} 注解并校验失败时触发
	 *
	 * @param e 异常对象
	 * @return Result
	 */
	@ExceptionHandler(BindException.class)
	public ResponseEntity<String> handler(BindException e) {
		FieldError fieldError = e.getFieldError();
		if (fieldError == null) {
			log.warn(e.getMessage(), e);
			return ResponseEntity.badRequest().contentType(MediaType.TEXT_PLAIN).body("请求参数错误");
		}
		log.warn("请求参数错误: [" + fieldError.getField() + "] " + fieldError.getDefaultMessage(), e);
		return ResponseEntity.badRequest().contentType(MediaType.TEXT_PLAIN).body(fieldError.getDefaultMessage());
	}

	/**
	 * 请求参数校验失败异常处理
	 * 当参数有 {@link javax.validation.Valid} 注解并校验失败时触发
	 *
	 * @param e 异常对象
	 * @return Result
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<String> handler(ConstraintViolationException e) {
		Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
		if (violations == null) {
			log.warn(e.getMessage(), e);
			return ResponseEntity.badRequest().contentType(MediaType.TEXT_PLAIN).body("请求参数错误");
		}
		Iterator<ConstraintViolation<?>> iterator = violations.iterator();
		if (iterator.hasNext()) {
			ConstraintViolation<?> violation = iterator.next();
			log.warn("请求参数错误: [" + violation.getPropertyPath() + "] " + violation.getMessage(), e);
			return ResponseEntity.badRequest().contentType(MediaType.TEXT_PLAIN).body(violation.getMessage());
		}
		log.warn(e.getMessage(), e);
		return ResponseEntity.badRequest().contentType(MediaType.TEXT_PLAIN).body("请求参数错误");
	}

	/**
	 * 请求体不可读错误异常处理
	 * 如 要求接收一个 json 请求体，但是未读取到 json 格式
	 *
	 * @param e 异常对象
	 * @return Result
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<String> handler(HttpMessageNotReadableException e) {
		Throwable t = e.getRootCause();
		if (t != null) {
			log.warn(t.getMessage(), e);
			return ResponseEntity.badRequest().contentType(MediaType.TEXT_PLAIN).body("请求参数错误");
		}
		log.warn(e.getMessage(), e);
		return ResponseEntity.badRequest().contentType(MediaType.TEXT_PLAIN).body("请求参数错误");
	}

	/**
	 * 请求参数缺失异常处理
	 * 如要 name 字段，却传递 eg: Required String parameter 'name' is not present
	 *
	 * @param e 异常对象
	 * @return Result
	 */
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<String> handler(MissingServletRequestParameterException e) {
		log.warn("请求参数缺失：" + e.getParameterName(), e);
		return ResponseEntity.badRequest().contentType(MediaType.TEXT_PLAIN)
							 .body("请求参数缺失：" + e.getParameterName());
	}

	/**
	 * 请求方法错误异常处理
	 * 即 POST 接口，请求时用了 GET 方法
	 *
	 * @param e 异常对象
	 * @return Result
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<String> handler(HttpRequestMethodNotSupportedException e) {
		String method = e.getMethod();
		String[] supportedMethods = e.getSupportedMethods();
		if (supportedMethods == null) {
			log.warn("请求方法错误: 不支持" + method, e);
			return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).contentType(MediaType.TEXT_PLAIN)
								 .body("请求方法错误: 不支持" + method);
		}
		String methods = String.join(", ", supportedMethods);
		log.warn("请求方法错误: 不支持" + method + ", 支持" + methods, e);
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).contentType(MediaType.TEXT_PLAIN)
							 .body("请求方法错误: 不支持" + method + ", 支持" + methods);
	}

	/**
	 * 请求的 ContentType 不支持
	 * 如 要求接收一个 json 请求体，但传了 form-data
	 *
	 * @param e 异常对象
	 * @return Result
	 */
	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity<String> handler(HttpMediaTypeNotSupportedException e) {
		MediaType contentType = e.getContentType();
		if (contentType == null) {
			log.warn(e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).contentType(MediaType.TEXT_PLAIN)
								 .body("请求内容类型错误");
		}
		String message = "请求内容类型错误: 不支持" + contentType;
		log.warn(message, e);
		return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).contentType(MediaType.TEXT_PLAIN).body(message);
	}

	/**
	 * 默认异常处理
	 *
	 * @param e 异常对象
	 * @return Result
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handler(Exception e) {
		log.error(e.getMessage(), e);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.TEXT_PLAIN)
							 .body("服务繁忙, 请稍后再试");
	}

}
