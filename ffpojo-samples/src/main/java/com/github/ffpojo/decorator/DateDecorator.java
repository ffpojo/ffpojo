package com.github.ffpojo.decorator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.github.ffpojo.exception.FieldDecoratorException;
import com.github.ffpojo.metadata.FieldDecorator;

public class DateDecorator implements FieldDecorator<Date> {
	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	public Date fromString(String str) throws FieldDecoratorException {
		if (str ==null || str.trim().length() ==0){
			return null;
		}
		try {
			return sdf.parse(str);
		} catch (ParseException e) {
			throw new FieldDecoratorException("Error while parsing date field from string: " + str, e);
		}
	}

	public String toString(Date obj) {
		Date date = (Date)obj;
		return sdf.format(date);
	}
}
