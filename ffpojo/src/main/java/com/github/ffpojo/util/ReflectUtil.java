package com.github.ffpojo.util;

import com.github.ffpojo.metadata.delimited.annotation.DelimitedRecord;
import com.github.ffpojo.metadata.positional.annotation.PositionalRecord;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import static com.github.ffpojo.util.StringUtil.camelCaseToPastelCase;
import static com.github.ffpojo.util.StringUtil.pastelCaseToCamelCase;

public class ReflectUtil {

	public static boolean isGetter(Method method) {
		if (method == null) return false;
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

	public static String getFieldNameFromGetterOrSetter(Method method) {
		String fieldNamePastelCase;
		if (isGetter(method) && method.getName().startsWith("is")) {
			fieldNamePastelCase = method.getName().substring(2);
		} else {
			fieldNamePastelCase = method.getName().substring(3);
		}
		return pastelCaseToCamelCase(fieldNamePastelCase);
	}

	public static Method getSetterFromGetter(Method method, Class<?>[] parameterTypes, Class<?> clazz)
			throws SecurityException, NoSuchMethodException {
		String fieldNamePastelCase;
		if (method.getName().startsWith("is")) {
			fieldNamePastelCase = method.getName().substring(2);
		} else {
			fieldNamePastelCase = method.getName().substring(3);
		}
		return clazz.getMethod("set" + fieldNamePastelCase, parameterTypes);
	}

	public static Method getGetterFromFieldName(String fieldName, Class<?> clazz) throws SecurityException, NoSuchMethodException {
		String getterNameAsDefault = "get" + camelCaseToPastelCase(fieldName);
		String getterNameAsBoolean = "is" + camelCaseToPastelCase(fieldName);
		Method getter = null;
		try {
			getter = clazz.getMethod(getterNameAsDefault, (Class[]) null);
		} catch (NoSuchMethodException e) {
			getter = clazz.getMethod(getterNameAsBoolean, (Class[]) null);
		}
		return getter;
	}

	public static List<Field> getRecursiveFields(Class<?> recordClazz) {
		final Set<Field> listaFields = new TreeSet<Field>(new Comparator<Field>() {
			public int compare(Field o1, Field o2) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});
		Class<?> clazz = recordClazz;
		while (clazz.isAnnotationPresent(PositionalRecord.class) || clazz.isAnnotationPresent(DelimitedRecord.class)) {
			listaFields.addAll(Arrays.asList(clazz.getDeclaredFields()));
			clazz = clazz.getSuperclass();
		}
		return new ArrayList<Field>(listaFields);
	}

	public static Field getField(Class<?> recordClazz, String fieldName){
		List<Field> fields =  getRecursiveFields(recordClazz);
		Class<?> clazz = recordClazz;
		Field field = null;
		while (clazz.isAnnotationPresent(PositionalRecord.class) || clazz.isAnnotationPresent(DelimitedRecord.class)) {
			try {
				field =  clazz.getDeclaredField(fieldName);
				return field;
			} catch (NoSuchFieldException e) {}
			clazz = clazz.getSuperclass();
		}
		return field;
	}

	public static List<Field> getAnnotadedFields(Class<?> recordClazz) {
		final List<Field> listaFields = new ArrayList<Field>();
		if (recordClazz.isAnnotationPresent(PositionalRecord.class) || recordClazz.isAnnotationPresent(DelimitedRecord.class)) {
			listaFields.addAll(Arrays.asList(recordClazz.getDeclaredFields()));
		}
		return listaFields;
	}

	public static List<Class<?>> getSuperClassesOf(Class<?> clazz) {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		Class<?> currentClazz = clazz;
		while (currentClazz.getSuperclass() != null) {
			currentClazz = currentClazz.getSuperclass();
			classes.add(currentClazz);
		}
		return classes;
	}

	public static boolean existMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
		try {
			clazz.getDeclaredMethod(methodName);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
