package com.github.ffpojo.parser;

import java.lang.reflect.Method;
import java.util.List;

import com.github.ffpojo.exception.FieldDecoratorException;
import com.github.ffpojo.exception.RecordParserException;
import com.github.ffpojo.metadata.FieldDecorator;
import com.github.ffpojo.metadata.delimited.DelimitedFieldDescriptor;
import com.github.ffpojo.metadata.delimited.DelimitedRecordDescriptor;
import com.github.ffpojo.util.ReflectUtil;
import com.github.ffpojo.util.RegexUtil;
import com.github.ffpojo.util.StringUtil;



class DelimitedRecordParser extends BaseRecordParser implements RecordParser {

	public DelimitedRecordParser(DelimitedRecordDescriptor recordDescriptor) {
		super(recordDescriptor);
	}
	
	public <T> T parseFromText(Class<T> recordClazz, String text) throws RecordParserException {
		T record;
		try {
			record = recordClazz.newInstance();
		} catch (Exception e) {
			throw new RecordParserException("Error while instantiating record class, make sure that is provided a default constructor for class " + recordClazz, e);
		}

		List<DelimitedFieldDescriptor> delimitedFieldDescriptors = getRecordDescriptor().getFieldDescriptors();
		String[] textTokens = text.split(RegexUtil.escapeRegexMetacharacters(getRecordDescriptor().getDelimiter()));
		int tokensQtt = textTokens.length;
		for(int i = 0; i < delimitedFieldDescriptors.size(); i++) {
			DelimitedFieldDescriptor actualFieldDescriptor = delimitedFieldDescriptors.get(i);
			
			String fieldValue;
			if (actualFieldDescriptor.getPositionIndex() <= tokensQtt) {
				fieldValue = textTokens[actualFieldDescriptor.getPositionIndex() - 1];
			} else {
				throw new RecordParserException("The position declared in field-mapping is greater than the text tokens amount: " + actualFieldDescriptor.getGetter());
			}
			
			Method setter;
			Class<?> getterReturnType = actualFieldDescriptor.getGetter().getReturnType();
			try {
				setter = ReflectUtil.getSetterFromGetter(actualFieldDescriptor.getGetter(), new Class<?>[] {String.class}, recordClazz);
			} catch (NoSuchMethodException e1) {
				try {
					setter = ReflectUtil.getSetterFromGetter(actualFieldDescriptor.getGetter(), new Class<?>[] {getterReturnType}, recordClazz);
				} catch (NoSuchMethodException e2) {
					throw new RecordParserException("Compatible setter not found for getter " + actualFieldDescriptor.getGetter(), e2);
				}
			}
			
			Object parameter;
			try {
				FieldDecorator<?> decorator = actualFieldDescriptor.getDecorator();
				parameter = decorator.fromString(fieldValue);
				setter.invoke(record, parameter);
			} catch (FieldDecoratorException e) {
				throw new RecordParserException(e);
			} catch (Exception e) {
				throw new RecordParserException("Error while invoking setter method, make sure that is provided a compatible fromString decorator method: " + setter, e);
			} 

		}
		
		return record;
	}
	
	@SuppressWarnings("unchecked")
	public <T> String parseToText(T record) throws RecordParserException {
		StringBuffer sbufRecordLine = new StringBuffer();
		
		List<DelimitedFieldDescriptor> delimitedFieldDescriptors = getRecordDescriptor().getFieldDescriptors();
		for(int i = 0; i < delimitedFieldDescriptors.size(); i++) {
			DelimitedFieldDescriptor actualFieldDescriptor = delimitedFieldDescriptors.get(i);
			
			boolean isFirstFieldDescriptor = i==0;
			DelimitedFieldDescriptor previousFieldDescriptor = null;
			if (!isFirstFieldDescriptor) {
				previousFieldDescriptor = delimitedFieldDescriptors.get(i-1);
			}
			
			Method getter = actualFieldDescriptor.getGetter();
			Object fieldValueObj;
			try {
				fieldValueObj = getter.invoke(record, new Object[]{});
			} catch (Exception e) {
				throw new RecordParserException("Error while invoking getter method: " + getter, e);
			} 
			
			String fieldValue;
			if (fieldValueObj == null) {
				fieldValue = "";
			} else {
				try {
					FieldDecorator<Object> decorator = (FieldDecorator)actualFieldDescriptor.getDecorator();
					fieldValue = decorator.toString(fieldValueObj);
				} catch (FieldDecoratorException e) {
					throw new RecordParserException(e);
				}
			}
			
			// Check for missing fields and fill
			if (isFirstFieldDescriptor && actualFieldDescriptor.getPositionIndex() > 1) {
				int missingFields = actualFieldDescriptor.getPositionIndex() - 1;
				sbufRecordLine.append(StringUtil.fillToLength("", missingFields, ',', StringUtil.Direction.RIGHT));
			} else if (!isFirstFieldDescriptor && previousFieldDescriptor.getPositionIndex() < actualFieldDescriptor.getPositionIndex() - 1) {
				int missingFields = actualFieldDescriptor.getPositionIndex() - previousFieldDescriptor.getPositionIndex() - 1;
				sbufRecordLine.append(StringUtil.fillToLength("", missingFields, ',', StringUtil.Direction.RIGHT));
			}
			
			sbufRecordLine.append(fieldValue);
			if (i < delimitedFieldDescriptors.size() - 1) {
				sbufRecordLine.append(getRecordDescriptor().getDelimiter());
			}

		}

		return sbufRecordLine.toString();
	}
	
	@Override
	protected DelimitedRecordDescriptor getRecordDescriptor() {
		return (DelimitedRecordDescriptor)recordDescriptor;
	}
	
}
