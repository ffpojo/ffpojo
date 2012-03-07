package com.github.ffpojo.exception;

public class RecordProcessorException extends FFPojoException {
	private static final long serialVersionUID = 1L;

	public RecordProcessorException(String message) {
		super(message);
	}
	
	public RecordProcessorException(Throwable cause) {
		super(cause);
	}
	
	public RecordProcessorException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
