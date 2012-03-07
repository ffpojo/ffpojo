package com.github.ffpojo.exception;

public class FieldDecoratorException extends FFPojoException {
	private static final long serialVersionUID = 1L;

	public FieldDecoratorException(String message) {
		super(message);
	}
	
	public FieldDecoratorException(Throwable cause) {
		super(cause);
	}
	
	public FieldDecoratorException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
