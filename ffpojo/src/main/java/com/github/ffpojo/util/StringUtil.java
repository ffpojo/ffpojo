package com.github.ffpojo.util;

public class StringUtil {
	
	public static final String EMPTY = "";

	public static enum Direction {
		LEFT,
		RIGHT
	}
	
	public static String fillToLength(String s, int length, char fillWith, Direction inDirection) {
		if (length <= 0) {
			return s;
		} else {
			int actualLength = s.length();
			StringBuffer sbuf = new StringBuffer(s);
			if (actualLength < length) {
				StringBuffer sbufDifference = new StringBuffer();
				int difference = length - actualLength;
				for (int i = 0; i < difference; i++) {
					sbufDifference.append(String.valueOf(fillWith));
				}
				if (inDirection == Direction.LEFT) {
					sbuf = new StringBuffer(sbufDifference);
					sbuf.append(s);
				} else if (inDirection == Direction.RIGHT) {
					sbuf.append(sbufDifference);
				}
			} else {
				sbuf.setLength(length);
			}
			return sbuf.toString();
		}
	}
	
	public static boolean isNullOrEmpty(String s) {
		return s == null || s.trim().equals("");
	}

	public static boolean isNotEmpty(String s){
		return not(isNullOrEmpty(s));
	}

	public static String pastelCaseToCamelCase(String sPastel) {
		if (isNullOrEmpty(sPastel)) return sPastel;
		char firstChar = sPastel.charAt(0);
		return String.valueOf(Character.toLowerCase(firstChar)) + sPastel.substring(1);
	}

	public static String camelCaseToPastelCase(String sCamel) {
		if (isNullOrEmpty(sCamel)) return sCamel;
		char firstChar = sCamel.charAt(0);
		return String.valueOf(Character.toUpperCase(firstChar)) + sCamel.substring(1);
	}

	private static boolean not(boolean value){
		return !value;
	}

	
}
