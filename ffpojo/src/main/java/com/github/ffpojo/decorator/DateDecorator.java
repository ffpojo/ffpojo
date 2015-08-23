package com.github.ffpojo.decorator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.github.ffpojo.exception.FieldDecoratorException;
import com.github.ffpojo.metadata.extra.ExtendedFieldDecorator;

public class DateDecorator extends ExtendedFieldDecorator<Date> {

	private static final String DEFAULT_FORMAT = "dd/MM/yyyy";
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
	
	public static Class<?>[] getTypesConstructorExtended() {
		return new Class<?>[]{String.class};
	}
	
	public static String[] getMethodContainsContstructorValues(){
		return new String[]{"dateFormat"};
	}

}
