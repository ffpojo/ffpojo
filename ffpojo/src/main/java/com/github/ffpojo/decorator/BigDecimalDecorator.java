package com.github.ffpojo.decorator;

import java.math.BigDecimal;

import com.github.ffpojo.decorator.util.FloatPointDecoratorUtil;
import com.github.ffpojo.metadata.extra.ExtendedFieldDecorator;

public class BigDecimalDecorator extends ExtendedFieldDecorator<BigDecimal> {
 
	private static final int DEFAULT_PRECISION = 2;
	private final FloatPointDecoratorUtil floatPointDecoratorUtil;
	
	public BigDecimalDecorator() {
		this(DEFAULT_PRECISION);
	}
	
	public BigDecimalDecorator(int precision) {		
		floatPointDecoratorUtil = new FloatPointDecoratorUtil(precision);
	}

	public int getPrecision() {
		return floatPointDecoratorUtil.getPrecision();
	}

	public String toString(BigDecimal value){
		return floatPointDecoratorUtil.toString(value);
	}

	public BigDecimal fromString(String value){
		return floatPointDecoratorUtil.fromString(value);
	}	
	
}
