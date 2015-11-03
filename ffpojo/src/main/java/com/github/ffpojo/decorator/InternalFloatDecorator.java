package com.github.ffpojo.decorator;

import com.github.ffpojo.decorator.util.FloatPointDecoratorUtil;
import com.github.ffpojo.metadata.extra.ExtendedFieldDecorator;

public class InternalFloatDecorator extends ExtendedFieldDecorator<Float>{

	private static final int DEFAULT_PRECISION = 2;
	private final FloatPointDecoratorUtil floatPointDecoratorUtil;
	
	public InternalFloatDecorator() {
		this(DEFAULT_PRECISION);
	}
	
	public InternalFloatDecorator(int precision) {
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
