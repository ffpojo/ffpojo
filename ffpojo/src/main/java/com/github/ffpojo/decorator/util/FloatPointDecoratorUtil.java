package com.github.ffpojo.decorator.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.github.ffpojo.util.StringUtil;

public class FloatPointDecoratorUtil {
	
	private static final int DEFAULT_PRECISION = 2;
	private int precision;
	
	public FloatPointDecoratorUtil(int precision){
		this.precision = precision;
	}
	
	public FloatPointDecoratorUtil() {
		this(DEFAULT_PRECISION);
	}

	public String toString(BigDecimal value){
		return toString(value, precision);
	}
	
	public String toString(double value){
		return toString(value, precision);
	}
	
	public String toString(float value){
		return toString(value, precision);
	}
	
	public BigDecimal fromString(String value){
		return fromString(value, this.precision);
	}
	public double doubleFromString(String value){
		return doubleFromString(value, this.precision);
	}
	public float floatFromString(String value){
		return floatFromString(value, this.precision);
	}
	
	public static String toString(BigDecimal value,  int precision){
		if (value == null) return StringUtil.EMPTY;
		return value.multiply(getMultiplierFactor(precision)).toString().split("\\.")[0];
	}
	
	public static String toString(double value,  int precision){
		return toString(BigDecimal.valueOf(value), precision);
	}
	
	public static String toString(float value,  int precision){
		//It is required to convert float to string, because float is 32 bits and occurs a  error precision.
		return toString(new BigDecimal(String.valueOf(value)), precision);
	}

	public static BigDecimal fromString(String value,  int precision){
		return new BigDecimal(value)
				.divide(getMultiplierFactor(precision))
				.setScale(precision, RoundingMode.CEILING);
		
	}
	
	public static double doubleFromString(String value,  int precision){
		return fromString(value, precision).doubleValue();
	}
	
	public static float floatFromString(String value,  int precision){
		return fromString(value, precision).floatValue();
	}

	private static BigDecimal getMultiplierFactor(int precision){
		return BigDecimal.valueOf(Math.round(Math.pow(10, precision)));
	}
	
	public int getPrecision() {
		return precision;
	}
	
	public void setPrecicion(int precision){
		this.precision =  precision;
	}
}
