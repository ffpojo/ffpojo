package com.github.ffpojo.metadata;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.github.ffpojo.metadata.positional.annotation.AccessorType;
import com.github.ffpojo.util.ReflectUtil;
import com.github.ffpojo.util.StringUtil;


public abstract class FieldDescriptor {

	private Method getter;
	private AccessorType accessorType =  AccessorType.PROPERTY;
	private boolean isFullLineField;
	private Field field;
		
	// GETTERS AND SETTERS
	
	public Method getGetter() {
		return getter;
	}
	public void setGetter(Method getter) {
		this.getter = getter;
	}
	public AccessorType getAccessorType() {
		return accessorType;
	}
	public void setAccessorType(AccessorType accessorType) {
		this.accessorType = accessorType;
	}
	public boolean isFullLineField() {
		return isFullLineField;
	}
	public void setIsFullLineField(boolean isFullLineField) {
		this.isFullLineField = isFullLineField;
	}

	public String getFieldDescriptorName(){
		String fieldName = "";
		if (getter!= null){
			fieldName =  getter.getName();
		}else if (field != null){
			fieldName =  field.getName();
		}
		return StringUtil.pastelCaseToCamelCase(fieldName);
	}

	private String getterToFieldName(Method getter){
		if (getter == null) return null;
		String getName=  getter.getName();
		if (getName.startsWith("get")){
			return getName.substring(3);
		}
		return getName.substring(2);

	}

}
