package com.github.ffpojo.decorator;

import com.github.ffpojo.decorator.util.FloatPointDecoratorUtil;

public class DoubleDecorator {

	private static final int DEFAULT_PRECISION = 2;
	private final FloatPointDecoratorUtil floatPointDecoratorUtil;
	
	public DoubleDecorator() {
		this(DEFAULT_PRECISION);
	}
	
	public DoubleDecorator(int precision) {		
		floatPointDecoratorUtil = new FloatPointDecoratorUtil(precision);
	}

	public int getPrecision() {
		return floatPointDecoratorUtil.getPrecision();
	}

	public String toString(Double value){
		return floatPointDecoratorUtil.toString(value);
	}

	public Double fromString(String value){
		return floatPointDecoratorUtil.fromString(value).doubleValue();
	}	
	
}
