package com.github.ffpojo.metadata;



public class DefaultFieldDecorator implements FieldDecorator<Object> {

	public String toString(Object field) {
		return field.toString();
	}
	
	public Object fromString(String field) {
		return field;
	}
	
}
