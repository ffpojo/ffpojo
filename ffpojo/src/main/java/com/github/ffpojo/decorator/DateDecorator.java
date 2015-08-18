package com.github.ffpojo.decorator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.github.ffpojo.exception.FieldDecoratorException;
import com.github.ffpojo.metadata.FieldDecorator;
import com.github.ffpojo.metadata.extra.ExtendedFieldDecorator;
import com.github.ffpojo.metadata.positional.annotation.extra.BigDecimalPositionalField;

public class DateDecorator extends ExtendedFieldDecorator<Date> {

	private static final String DEFAULT_FORMAT = "dd/MM/yyyy";
	private String format = DEFAULT_FORMAT;
	private final SimpleDateFormat sdf;
	
	public DateDecorator(String format) {
		sdf = new SimpleDateFormat(format);
	}
	public DateDecorator(){
		this(DEFAULT_FORMAT);
	}

	public String toString(Date field) throws FieldDecoratorException {		
		return sdf.format(field);
	}

	public Date fromString(String field) throws FieldDecoratorException {
		try {
			return sdf.parse(field);
		} catch (ParseException e) {
			throw new FieldDecoratorException(e);
		}		
	}
	
	public static void main(String[] args) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, FieldDecoratorException {
		Class decoratorClass =  DateDecorator.class;
		Method getTypesConstructorExtended =  decoratorClass.getDeclaredMethod("getTypesConstructorExtended");
		Constructor<?> c =  decoratorClass.getDeclaredConstructor((Class[])getTypesConstructorExtended.invoke(null));
		FieldDecorator<?> f =  (FieldDecorator<?>) c.newInstance("dd-MM-yyyy");
		
		System.out.println(callToString(new Date(), f));
		Constructor<?> c2 = BigDecimalDecorator.class.getDeclaredConstructor(int.class);
		FieldDecorator<?> f2 = (FieldDecorator<?>) c2.newInstance(10);
		System.out.println(callToString(BigDecimal.valueOf(1.234567), f2));
		
		if (Annotation.class.isAssignableFrom(BigDecimalPositionalField.class)){
			Method m =  BigDecimalPositionalField.class.getDeclaredMethod("precision");
//			System.out.println(m.invoke());
			System.out.println("É uma anotacao");
		}
		
	}
	
	public static <T> String callToString(T value, FieldDecorator f) throws FieldDecoratorException{
		return f.toString(value);
	}
	
	public static Class[] getTypesConstructorExtended() {
		return new Class[]{String.class};
	}
	
	public static Object[] getValuesConstructorExtended() {
		return new Object[]{};
	}

}
