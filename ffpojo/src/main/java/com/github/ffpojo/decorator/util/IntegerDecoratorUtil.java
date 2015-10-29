package com.github.ffpojo.decorator.util;

import java.math.BigInteger;

import com.github.ffpojo.util.StringUtil;

public class IntegerDecoratorUtil {
	
	private static final String EMPTY_STR = "";
	
	public BigInteger fromStringToBigInteger( String text){
		if (StringUtil.isNullOrEmpty(text)) return null;
		return new BigInteger(text);
	}
	
	public String toStringFromBigInteger(BigInteger number) {
		if (number == null) {
			return EMPTY_STR;
		}
		return number.toString();
	}
	
	public String toStringFromLong(Long value){
		if (value == null) return EMPTY_STR;
		return value.toString();
	}
	
	public Long fromStringToLong(String text){
		if (StringUtil.isNullOrEmpty(text)) return null;
		return Long.valueOf(text);
	}
	
	public String toStringFromInteger(Integer value){
		if (value == null) return EMPTY_STR;
		return value.toString();
	}
	
	public Integer fromStringToInteger(String text){
		if (StringUtil.isNullOrEmpty(text)) return null;
		return Integer.valueOf(text); 
	}
	

}
