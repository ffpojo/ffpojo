package com.github.ffpojo.exception;

public class RecordParserException extends FFPojoException {
	private static final long serialVersionUID = 1L;

	public RecordParserException(String message) {
		super(message);
	}
	
	public RecordParserException(Throwable cause) {
		super(cause);
	}
	
	public RecordParserException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
