package com.github.ffpojo.metadata;

import java.lang.reflect.Method;


public abstract class FieldDescriptor {

	private Method getter;
		
	// GETTERS AND SETTERS
	
	public Method getGetter() {
		return getter;
	}
	public void setGetter(Method getter) {
		this.getter = getter;
	}
}
