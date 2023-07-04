package net.wenzuo.atom.core.exception;

import lombok.Getter;

/**
 * @author Catch
 * @since 2023-07-03
 */
@Getter
public class HttpException extends RuntimeException {

	private final int status;
	private final String message;

	public HttpException(int status, String message) {
		super(message);
		this.status = status;
		this.message = message;
	}

	public HttpException(Throwable t, int status, String message) {
		super(t);
		this.status = status;
		this.message = message;
	}

}
