package com.github.ffpojo.decorator;

import com.github.ffpojo.exception.FieldDecoratorException;
import com.github.ffpojo.metadata.FieldDecorator;
import com.github.ffpojo.metadata.extra.ExtendedFieldDecorator;

public class IntegerDecorator extends ExtendedFieldDecorator<Integer>{

	public String toString(Integer field) throws FieldDecoratorException {
		return field == null ? "" :  field.toString();
	}

	public Integer fromString(String field) throws FieldDecoratorException {
		if (field == null) return null;
		return Integer.valueOf(field);
	}

}
