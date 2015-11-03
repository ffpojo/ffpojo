package com.github.ffpojo.decorator;

import com.github.ffpojo.decorator.util.IntegerDecoratorUtil;
import com.github.ffpojo.exception.FieldDecoratorException;
import com.github.ffpojo.metadata.extra.ExtendedFieldDecorator;

public class InternalLongDecorator extends ExtendedFieldDecorator<Long>{

	private final IntegerDecoratorUtil util = new IntegerDecoratorUtil();
	
	public String toString(Long field) throws FieldDecoratorException {
		return util.toStringFromLong(field);
	}

	public Long fromString(String field) throws FieldDecoratorException {
		return util.fromStringToLong(field);
	}

}
