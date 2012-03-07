package com.github.ffpojo.exception;

public class InvalidMetadataException extends MetadataReaderException {
	private static final long serialVersionUID = 1L;

	public InvalidMetadataException(String message) {
		super(message);
	}
	
	public InvalidMetadataException(Throwable cause) {
		super(cause);
	}
	
	public InvalidMetadataException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
