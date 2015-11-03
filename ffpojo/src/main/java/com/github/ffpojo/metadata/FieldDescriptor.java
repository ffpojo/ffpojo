package com.github.ffpojo.metadata;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.github.ffpojo.metadata.positional.annotation.AccessorType;


public abstract class FieldDescriptor {

	private Method getter;
	private Field field;
	private AccessorType accessorType =  AccessorType.PROPERTY;
		
	// GETTERS AND SETTERS
	
	public Method getGetter() {
		return getter;
	}
	public void setGetter(Method getter) {
		this.getter = getter;
	}
	public Field getField() {
		return field;
	}
	public void setField(Field field) {
		this.field = field;
	}
	public AccessorType getAccessorType() {
		return accessorType;
	}
	public void setAccessorType(AccessorType accessorType) {
		this.accessorType = accessorType;
	}

	public boolean isByField(){
		return this.accessorType.isByField();
	}

	public boolean isByProperty(){
		return this.accessorType.isByProperty();
	}
}
