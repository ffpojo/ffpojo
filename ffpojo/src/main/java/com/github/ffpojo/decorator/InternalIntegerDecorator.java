package com.github.ffpojo.decorator;

import com.github.ffpojo.decorator.util.IntegerDecoratorUtil;
import com.github.ffpojo.exception.FieldDecoratorException;
import com.github.ffpojo.metadata.extra.ExtendedFieldDecorator;

public class InternalIntegerDecorator extends ExtendedFieldDecorator<Integer>{

	private IntegerDecoratorUtil util =  new IntegerDecoratorUtil();
	
	public String toString(Integer field) throws FieldDecoratorException {
		return util.toStringFromInteger(field);
	}

	public Integer fromString(String field) throws FieldDecoratorException {
		return util.fromStringToInteger(field);
	}

}
