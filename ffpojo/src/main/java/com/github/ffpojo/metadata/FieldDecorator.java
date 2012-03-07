package com.github.ffpojo.metadata;

import com.github.ffpojo.exception.FieldDecoratorException;



public interface FieldDecorator<T> {

	public String toString(T field) throws FieldDecoratorException;
	
	public T fromString(String field) throws FieldDecoratorException;
	
}
