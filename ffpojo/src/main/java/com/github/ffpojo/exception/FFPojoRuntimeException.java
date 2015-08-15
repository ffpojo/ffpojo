package com.github.ffpojo.exception;

public class FFPojoRuntimeException extends RuntimeException{
	 
	private static final long serialVersionUID = 1L;
	
	public FFPojoRuntimeException() {
		super();
	}
	
	public FFPojoRuntimeException(String msg){
		super(msg);
	}
	
	public FFPojoRuntimeException(String msg, Throwable e){
		super(msg, e);
	}
	
	public FFPojoRuntimeException(Throwable e){
		super(e);
	}

}
