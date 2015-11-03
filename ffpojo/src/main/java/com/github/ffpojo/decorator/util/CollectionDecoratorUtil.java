package com.github.ffpojo.decorator.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.github.ffpojo.metadata.positional.annotation.AccessorType;
import com.github.ffpojo.metadata.positional.annotation.PositionalField;
import com.github.ffpojo.metadata.positional.annotation.PositionalRecord;
import com.github.ffpojo.metadata.positional.annotation.extra.BigDecimalPositionalField;
import com.github.ffpojo.metadata.positional.annotation.extra.BooleanPositionalField;
import com.github.ffpojo.metadata.positional.annotation.extra.DatePositionalField;
import com.github.ffpojo.metadata.positional.annotation.extra.DoublePositionalField;
import com.github.ffpojo.metadata.positional.annotation.extra.FloatPositionalField;
import com.github.ffpojo.metadata.positional.annotation.extra.IntegerPositionalField;
import com.github.ffpojo.metadata.positional.annotation.extra.LongPositionalField;

public class CollectionDecoratorUtil {
	
	
	private Class<?> clazz;
	private int clazzPositionSize;
	
	public CollectionDecoratorUtil(Class<?> clazzPositionalFieldAnnotaded) {
		this.clazz =  clazzPositionalFieldAnnotaded;
		readAnnotations(clazz);
	}

	public int objectLineSize(){
		return clazzPositionSize;
	}
	
	private void readAnnotations(Class<?> clazz){		
		AccessorType accessorType  =  AccessorType.PROPERTY;
		if (clazz.isAnnotationPresent(PositionalRecord.class)){
			readObjectLineSizeFromField(clazz);
			readObjectLineSizeFromProperties(clazz);

		}
	}

	private void readObjectLineSizeFromField(Class<?> clazz) {
		Field[] fields =  clazz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field =  fields[i];
			field.setAccessible(true);
			Annotation[] annotations = field.getAnnotations();
			for (int j = 0; j < annotations.length; j++) {
				clazzPositionSize =  getMoreThanPosition(getFinalPosition(annotations[j]));
			}
		}
		readSuperClassRecursive(clazz);
	}

	private void readObjectLineSizeFromProperties(Class<?> clazz) {
		Method[] methods = clazz.getMethods();
		for (int i = 0; i < methods.length; i++) {
			if (methods[i].getName().startsWith("get")){
				Method method =  methods[i];
				getClassPositionSize(method);					
			}
		}
		readSuperClassRecursive(clazz);
		
	}

	private void readSuperClassRecursive(Class<?> clazz) {
		Class<?> superClazz = clazz.getSuperclass();
		if (superClazz != null && superClazz.isAnnotationPresent(PositionalRecord.class)){
			readAnnotations(superClazz);
		}
	}

	private void getClassPositionSize(Method method) {
		Annotation[] annotations = method.getAnnotations();
		for (int j = 0; j < annotations.length; j++) {					
			clazzPositionSize =  getMoreThanPosition(getFinalPosition(annotations[j]));					
		}
	}
	
	private int getMoreThanPosition(int actualValueAnnotationPosition){
		return clazzPositionSize < actualValueAnnotationPosition ? actualValueAnnotationPosition : clazzPositionSize;
	}
	private int getFinalPosition(Annotation positionalField){
		IntegerPositionalField.class.isAssignableFrom(positionalField.getClass());
		if (PositionalField.class.isAssignableFrom(positionalField.getClass())){
			return ((PositionalField)positionalField).finalPosition();
		}else if (BigDecimalPositionalField.class.isAssignableFrom(positionalField.getClass())){
			return ((BigDecimalPositionalField)positionalField).finalPosition();
		}else if (BooleanPositionalField.class.isAssignableFrom(positionalField.getClass())){
			return ((BooleanPositionalField)positionalField).finalPosition();
		}else if (DatePositionalField.class.isAssignableFrom(positionalField.getClass())){
			return ((DatePositionalField)positionalField).finalPosition();
		}else if (DoublePositionalField.class.isAssignableFrom(positionalField.getClass())){
			return ((DoublePositionalField)positionalField).finalPosition();
		}else if (FloatPositionalField.class.isAssignableFrom(positionalField.getClass())){
			return ((FloatPositionalField)positionalField).finalPosition();
		}else if (IntegerPositionalField.class.isAssignableFrom(positionalField.getClass())){
			return ((IntegerPositionalField)positionalField).finalPosition();
		}else if (LongPositionalField.class.isAssignableFrom(positionalField.getClass())){
			return ((LongPositionalField)positionalField).finalPosition();
		}
		return 0;
	}

}
