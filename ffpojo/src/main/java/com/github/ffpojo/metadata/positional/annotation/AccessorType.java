package com.github.ffpojo.metadata.positional.annotation;

/**
 * Define the annotation place, if is on field or property (getter method).
 * @author William Miranda
 */
public enum AccessorType {

	PROPERTY, FIELD;

	public boolean isByField(){
		return FIELD.equals(this);
	}

	public boolean isByProperty(){
		return PROPERTY.equals(this);
	}


}
