package com.github.ffpojo.exception;

public class MetadataReaderException extends FFPojoException {
	private static final long serialVersionUID = 1L;

	public MetadataReaderException(String message) {
		super(message);
	}
	
	public MetadataReaderException(Throwable cause) {
		super(cause);
	}
	
	public MetadataReaderException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
