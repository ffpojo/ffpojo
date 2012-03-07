package com.github.ffpojo.exception;

public class MetadataNotFoundException extends MetadataReaderException {
	private static final long serialVersionUID = 1L;

	public MetadataNotFoundException(String message) {
		super(message);
	}
	
	public MetadataNotFoundException(Throwable cause) {
		super(cause);
	}
	
	public MetadataNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
