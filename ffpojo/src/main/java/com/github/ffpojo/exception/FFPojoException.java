package com.github.ffpojo.exception;

public class FFPojoException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public FFPojoException(String message) {
		super(message);
	}
	
	public FFPojoException(Throwable cause) {
		super(cause);
	}
	
	public FFPojoException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
