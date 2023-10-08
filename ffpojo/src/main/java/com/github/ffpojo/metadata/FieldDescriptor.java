package com.github.ffpojo.metadata;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.github.ffpojo.metadata.positional.PositionalFieldDescriptor;
import com.github.ffpojo.metadata.positional.annotation.AccessorType;
import com.github.ffpojo.util.ReflectUtil;
import com.github.ffpojo.util.StringUtil;


public abstract class FieldDescriptor {

	private Method getter;
	private boolean isFullLineField;
	private FieldDecorator<?> decorator;

	// GETTERS AND SETTERS
	
	public Method getGetter() {
		return getter;
	}
	public void setGetter(Method getter) {
		this.getter = getter;
	}
	public boolean isFullLineField() {
		return isFullLineField;
	}
	public void setIsFullLineField(boolean isFullLineField) {
		this.isFullLineField = isFullLineField;
	}

	protected String getFieldDescriptorName(){
		final String fieldName = ReflectUtil.getFieldNameFromGetterOrSetter(getter);
		return StringUtil.pastelCaseToCamelCase(fieldName);
	}

	protected int compareFieldDescriptorFullLine(FieldDescriptor other){
		if (this.isFullLineField()){
			return 1;
		}
		if(other.isFullLineField()){
			return -1;
		}
		return 0;
	}

	protected int compareGetterName(PositionalFieldDescriptor other){
		return  this.getFieldDescriptorName().compareTo(other.getFieldDescriptorName());
	}

	public FieldDecorator<?> getDecorator() {
		return decorator;
	}
	public void setDecorator(FieldDecorator<?> decorator) {
		this.decorator = decorator;
	}

}
