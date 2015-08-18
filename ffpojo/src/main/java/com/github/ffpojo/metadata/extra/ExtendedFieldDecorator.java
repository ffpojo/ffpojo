package com.github.ffpojo.metadata.extra;

import java.lang.annotation.Annotation;

import com.github.ffpojo.metadata.FieldDecorator;

public abstract class ExtendedFieldDecorator<T> implements FieldDecorator<T> {

	/**
	 * Should to return a array of Class.
	 * The array returned represent the types paramters of constructor
	 * @return
	 */
	public static Class<?>[] getTypesConstructorExtended(){
		return new Class[]{};
	}
	
	/**
	 * Return the methods names in annotation that contains the values to call the constructor
	 * @return
	 */
	public static String[] getMethodContainsContstructorValues(){
		return new String[]{};
	}
	
	/**
	 * Return the Annotation linked with the decoration
	 * @return
	 */
	public static Class<? extends Annotation> annotationLinked(){
		return Annotation.class;
	}
	
}
