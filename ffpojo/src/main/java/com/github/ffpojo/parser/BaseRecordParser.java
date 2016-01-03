package com.github.ffpojo.parser;

import com.github.ffpojo.exception.FFPojoException;
import com.github.ffpojo.exception.FieldDecoratorException;
import com.github.ffpojo.exception.RecordParserException;
import com.github.ffpojo.metadata.FieldDecorator;
import com.github.ffpojo.metadata.FieldDescriptor;
import com.github.ffpojo.metadata.RecordDescriptor;
import com.github.ffpojo.metadata.delimited.DelimitedFieldDescriptor;
import com.github.ffpojo.util.ReflectUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

abstract class BaseRecordParser implements RecordParser {

	protected RecordDescriptor recordDescriptor;
	
	protected BaseRecordParser(RecordDescriptor recordDescriptor) {
		this.recordDescriptor = recordDescriptor;
	}

	protected RecordDescriptor getRecordDescriptor() {
		return recordDescriptor;
	}

	protected  <T> void setValueByField(T record, FieldDescriptor actualFieldDescriptor, String fieldValue){
		FieldDecorator<?> decorator = actualFieldDescriptor.getDecorator();
		Object parameter = decorator.fromString(fieldValue);
		final Method getter = actualFieldDescriptor.getGetter();
		final String fieldName =  ReflectUtil.getFieldNameFromGetterOrSetter(getter);
		try{
			final Field field = ReflectUtil.getField(record.getClass(), fieldName);
			field.setAccessible(true);
			field.set(record, parameter);
		}catch (Exception e){
			throw new FFPojoException(e);
		}
	}


	protected  <T> void setValueBySetterMethod(T record, FieldDescriptor actualFieldDescriptor, String fieldValue, Method setter) {
		Object parameter;
		try {
			FieldDecorator<?> decorator = actualFieldDescriptor.getDecorator();
			parameter = decorator.fromString(fieldValue);
			if (setter != null)
				setter.invoke(record, parameter);
		} catch (FieldDecoratorException e) {
			throw new RecordParserException(e);
		} catch (Exception e) {
			throw new RecordParserException("Error while invoking setter method, make sure that is provided a compatible fromString decorator method: " + setter, e);
		}
	}
}
