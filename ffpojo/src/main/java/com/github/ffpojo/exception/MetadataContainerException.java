package com.github.ffpojo.exception;

public class MetadataContainerException extends FFPojoException {
	private static final long serialVersionUID = 1L;

	public MetadataContainerException(String message) {
		super(message);
	}
	
	public MetadataContainerException(Throwable cause) {
		super(cause);
	}
	
	public MetadataContainerException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
