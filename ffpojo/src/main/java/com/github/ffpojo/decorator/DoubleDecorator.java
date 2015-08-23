package com.github.ffpojo.decorator;

import com.github.ffpojo.decorator.util.FloatPointDecoratorUtil;
import com.github.ffpojo.metadata.extra.ExtendedFieldDecorator;

public class DoubleDecorator extends ExtendedFieldDecorator<Double> {

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

	/**
	 * Return the constructor parameters type
	 * @return
	 */
	public static Class<?>[] getTypesConstructorExtended(){
		return new Class[]{int.class};
	}
	
	/**
	 * Return the methods names in annotation that contains the values to call the constructor
	 * @return
	 */
	public static String[] getMethodContainsContstructorValues(){
		return new String[]{"precision"};
	}
	
}
