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
import net.wenzuo.atom.core.util.BusinessException;
import net.wenzuo.atom.core.util.Result;
import net.wenzuo.atom.core.util.ServiceException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Catch
 * @since 2023-06-06
 */
@Slf4j
@RestControllerAdvice
@ConditionalOnProperty(value = "atom.web.exception-handler", matchIfMissing = true)
public class WebExceptionHandler {

	/**
	 * 正常业务异常, 输出 warn 日志
	 *
	 * @param e 异常对象
	 * @return Result
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BusinessException.class)
	public Result<?> handler(BusinessException e) {
		log.warn(e.getMessage(), e);
		return Result.fail(e);
	}

	/**
	 * 服务异常, 输出 error 日志
	 *
	 * @param e 异常对象
	 * @return Result
	 */
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(ServiceException.class)
	public Result<?> handler(ServiceException e) {
		log.error(e.getMessage(), e);
		return Result.fail(e);
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
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public Result<?> handler(MethodArgumentTypeMismatchException e) {
		log.warn("请求参数类型不匹配" + e.getMessage(), e);
		return Result.fail(BusinessException.DEFAULT_CODE, BusinessException.DEFAULT_MESSAGE);
	}

	/**
	 * 请求参数校验失败异常处理
	 * 当参数有 {@link org.springframework.validation.annotation.Validated} 注解并校验失败时触发
	 *
	 * @param e 异常对象
	 * @return Result
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BindException.class)
	public Result<?> handler(BindException e) {
		FieldError fieldError = e.getFieldError();
		if (fieldError == null) {
			log.warn(e.getMessage(), e);
			return Result.fail(BusinessException.DEFAULT_CODE, BusinessException.DEFAULT_MESSAGE);
		}
		String field = fieldError.getField();
		String message = fieldError.getDefaultMessage();
		log.warn("请求参数错误: [" + field + "] " + message, e);
		return Result.fail(BusinessException.DEFAULT_CODE, message);
	}

	/**
	 * 请求参数校验失败异常处理
	 * 当参数有 {@link javax.validation.Valid} 注解并校验失败时触发
	 *
	 * @param e 异常对象
	 * @return Result
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ConstraintViolationException.class)
	public Result<?> handler(ConstraintViolationException e) {
		Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
		if (violations == null) {
			log.warn(e.getMessage(), e);
			return Result.fail(BusinessException.DEFAULT_CODE, BusinessException.DEFAULT_MESSAGE);
		}
		Iterator<ConstraintViolation<?>> iterator = violations.iterator();
		if (iterator.hasNext()) {
			ConstraintViolation<?> violation = iterator.next();
			Path propertyPath = violation.getPropertyPath();
			String message = violation.getMessage();
			log.warn("请求参数错误: [" + propertyPath + "] " + message, e);
			return Result.fail(BusinessException.DEFAULT_CODE, message);
		}
		log.warn(e.getMessage(), e);
		return Result.fail(BusinessException.DEFAULT_CODE, BusinessException.DEFAULT_MESSAGE);
	}

	/**
	 * 请求体不可读错误异常处理
	 * 如 要求接收一个 json 请求体，但是未读取到 json 格式
	 *
	 * @param e 异常对象
	 * @return Result
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public Result<?> handler(HttpMessageNotReadableException e) {
		Throwable t = e.getRootCause();
		if (t != null) {
			log.warn(t.getMessage(), e);
			return Result.fail(BusinessException.DEFAULT_CODE, BusinessException.DEFAULT_MESSAGE);
		}
		log.warn(e.getMessage(), e);
		return Result.fail(BusinessException.DEFAULT_CODE, BusinessException.DEFAULT_MESSAGE);
	}

	/**
	 * 请求参数缺失异常处理
	 * 如要 name 字段，却传递 eg: Required String parameter 'name' is not present
	 *
	 * @param e 异常对象
	 * @return Result
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public Result<?> handler(MissingServletRequestParameterException e) {
		log.warn("请求参数缺失：" + e.getParameterName(), e);
		return Result.fail(BusinessException.DEFAULT_CODE, "请求参数缺失");
	}

	/**
	 * 请求方法错误异常处理
	 * 即 POST 接口，请求时用了 GET 方法
	 *
	 * @param e 异常对象
	 * @return Result
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public Result<?> handler(HttpRequestMethodNotSupportedException e) {
		String method = e.getMethod();
		String[] supportedMethods = e.getSupportedMethods();
		if (supportedMethods == null) {
			log.warn("请求方法错误: 不支持" + method, e);
			return Result.fail(BusinessException.DEFAULT_CODE, "请求方法错误");
		}
		String methods = String.join(", ", supportedMethods);
		log.warn("请求方法错误: 不支持" + method + ", 支持" + methods, e);
		return Result.fail(BusinessException.DEFAULT_CODE, "请求方法错误");
	}

	/**
	 * 请求的 ContentType 不支持
	 * 如 要求接收一个 json 请求体，但传了 form-data
	 *
	 * @param e 异常对象
	 * @return Result
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public Result<?> handler(HttpMediaTypeNotSupportedException e) {
		MediaType contentType = e.getContentType();
		if (contentType == null) {
			log.warn(e.getMessage(), e);
			return Result.fail(BusinessException.DEFAULT_CODE, "请求内容类型错误");
		}
		String message = "请求内容类型错误: 不支持" + contentType;
		log.warn(message, e);
		return Result.fail(BusinessException.DEFAULT_CODE, "请求内容类型错误");
	}

	/**
	 * 默认异常处理
	 *
	 * @param e 异常对象
	 * @return Result
	 */
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public Result<?> handler(Exception e) {
		log.error(e.getMessage(), e);
		return Result.fail(ServiceException.DEFAULT_CODE, ServiceException.DEFAULT_MESSAGE);
	}

}
