package com.github.ffpojo.decorator;

import java.lang.annotation.Annotation;

import com.github.ffpojo.exception.FieldDecoratorException;
import com.github.ffpojo.metadata.extra.ExtendedFieldDecorator;

public class BooleanDecorator  extends ExtendedFieldDecorator<Boolean>{

	private String trueIdentifier;
	private String falseIdentifier;
	
	public BooleanDecorator(String trueItendifier, String falseIdentifier) {
		this.trueIdentifier =  trueItendifier;
		this.falseIdentifier =  falseIdentifier;
	}
	
	public String toString(Boolean field) throws FieldDecoratorException {
		if (field==null) return null;
		if (field.booleanValue()){
			return trueIdentifier;
		}
		return falseIdentifier;
	}

	public Boolean fromString(String field) throws FieldDecoratorException {
		if (field== null) return null;
		return (field.equalsIgnoreCase(trueIdentifier));
	}
	
	public static Class<?>[] getTypesConstructorExtended(){
		return new Class[]{String.class, String.class};
	}
	
	/**
	 * Return the methods names in annotation that contains the values to call the constructor
	 * @return
	 */
	public static String[] getMethodContainsContstructorValues(){
		return new String[]{"trueIdentifier", "falseIdentifier"};
	}
	
	/**
	 * Return the Annotation linked with the decoration
	 * @return
	 */
	public static Class<? extends Annotation> annotationLinked(){
		return Annotation.class;
	}

}
