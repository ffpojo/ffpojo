package com.github.ffpojo.decorator;

import com.github.ffpojo.decorator.util.FloatPointDecoratorUtil;

public class FloatDecorator {

	private static final int DEFAULT_PRECISION = 2;
	private final FloatPointDecoratorUtil floatPointDecoratorUtil;
	
	public FloatDecorator() {
		this(DEFAULT_PRECISION);
	}
	
	public FloatDecorator(int precision) {		
		floatPointDecoratorUtil = new FloatPointDecoratorUtil(precision);
	}

	public int getPrecision() {
		return floatPointDecoratorUtil.getPrecision();
	}

	public String toString(Float value){
		return floatPointDecoratorUtil.toString(value);
	}

	public Float fromString(String value){
		return floatPointDecoratorUtil.fromString(value).floatValue();
	}	

	
}
