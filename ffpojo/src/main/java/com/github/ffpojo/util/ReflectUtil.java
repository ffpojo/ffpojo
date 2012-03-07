package com.github.ffpojo.util;

import java.lang.reflect.Method;

public class ReflectUtil {

	public static boolean isGetter(Method method) {
		if (!method.getName().startsWith("get") && !method.getName().startsWith("is")) {
			return false;
		} else if (method.getName().equals("getClass")) {
			return false;
		} else if (method.getParameterTypes().length != 0) {
			return false;
		} else if (void.class.equals(method.getReturnType())) {
			return false;
		} else {
			return true;
		}
	}
	
	public static boolean isSetter(Method method) {
		if (!method.getName().startsWith("set")) {
			return false;
		} else if (method.getParameterTypes().length != 1) {
			return false;
		} else {
			return true;
		}
	}
	
	public static String getFieldNameFromGetterOrSetter(Method method, Class<?> clazz) {
		String fieldNamePastelCase;
		if (isGetter(method) && method.getName().startsWith("is")) {
			fieldNamePastelCase = method.getName().substring(2);
		} else {
			fieldNamePastelCase = method.getName().substring(3);
		}
		return pastelCaseToCamelCase(fieldNamePastelCase);
	}
	
	public static Method getSetterFromGetter(Method method, Class<?>[] parameterTypes, Class<?> clazz) throws SecurityException, NoSuchMethodException {
		String fieldNamePastelCase;
		if (method.getName().startsWith("is")) {
			fieldNamePastelCase = method.getName().substring(2);
		} else {
			fieldNamePastelCase = method.getName().substring(3);
		}
		return clazz.getMethod("set" + fieldNamePastelCase, parameterTypes);
	}
	
	public static Method getGetterFromFieldName(String fieldName, Class<?> clazz) throws SecurityException, NoSuchMethodException {
		String getterNameAsDefault = "get" + ReflectUtil.camelCaseToPastelCase(fieldName);
		String getterNameAsBoolean = "is" + ReflectUtil.camelCaseToPastelCase(fieldName);
		Method getter = null;
		try {
			getter = clazz.getMethod(getterNameAsDefault, (Class[]) null);
		} catch (NoSuchMethodException e) {
			getter = clazz.getMethod(getterNameAsBoolean, (Class[]) null);
		}
		return getter;
	}
	
	public static String pastelCaseToCamelCase(String sPastel) {
		char firstChar = sPastel.charAt(0);
		return String.valueOf(Character.toLowerCase(firstChar)) + sPastel.substring(1);
	}
	
	public static String camelCaseToPastelCase(String sCamel) {
		char firstChar = sCamel.charAt(0);
		return String.valueOf(Character.toUpperCase(firstChar)) + sCamel.substring(1);
	}
	
}
