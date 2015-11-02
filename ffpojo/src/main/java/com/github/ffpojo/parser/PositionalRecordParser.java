package com.github.ffpojo.parser;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import com.github.ffpojo.exception.FFPojoException;
import com.github.ffpojo.exception.FieldDecoratorException;
import com.github.ffpojo.exception.RecordParserException;
import com.github.ffpojo.metadata.FieldDecorator;
import com.github.ffpojo.metadata.positional.PositionalFieldDescriptor;
import com.github.ffpojo.metadata.positional.PositionalRecordDescriptor;
import com.github.ffpojo.util.ReflectUtil;
import com.github.ffpojo.util.StringUtil;


/**
 * @author  Gilberto Holms - gibaholms@hotmail.com
 * @author  William Miranda - blackstile@hotmail.com
 */
class PositionalRecordParser extends BaseRecordParser implements RecordParser {

	public PositionalRecordParser(PositionalRecordDescriptor recordDescriptor) {
		super(recordDescriptor);
	}

	public <T> T parseFromText(Class<T> recordClazz, String text) throws RecordParserException {

		final T record = createRecordInstance(recordClazz);
		final List<PositionalFieldDescriptor> positionalFieldDescriptors = getRecordDescriptor().getFieldDescriptors();

		for (int i = 0; i < positionalFieldDescriptors.size(); i++) {
			final PositionalFieldDescriptor actualFieldDescriptor = positionalFieldDescriptors.get(i);
			final PositionalFieldDescriptor previousFieldDescriptor =  readPreviousFieldDescriptors(positionalFieldDescriptors, i);
			final String fieldValue = readFieldValue(text, actualFieldDescriptor, previousFieldDescriptor);

			if (actualFieldDescriptor.isByField()){
				setValueByFieldFromText(record, actualFieldDescriptor, fieldValue);
			}else{
				setValueByPropertyFromText(recordClazz, record, actualFieldDescriptor, fieldValue);
			}

		}
		
		return record;
	}

	@SuppressWarnings("unchecked")
	public <T> String parseToText(T record) throws RecordParserException {
		final StringBuffer sbufRecordLine = new StringBuffer();
		final List<PositionalFieldDescriptor> positionalFieldDescriptors = getRecordDescriptor().getFieldDescriptors();

		for (int i = 0; i < positionalFieldDescriptors.size(); i++) {
			final PositionalFieldDescriptor actualFieldDescriptor = positionalFieldDescriptors.get(i);

			final PositionalFieldDescriptor previousFieldDescriptor = readPreviousFieldDescriptors(positionalFieldDescriptors, i);
			final boolean isFirstFieldDescriptor =  previousFieldDescriptor == null;

			final String fieldValue = readFieldValueFromRecord(record, actualFieldDescriptor);
			int fieldLength = actualFieldDescriptor.getFinalPosition() - actualFieldDescriptor.getInitialPosition() + 1;
			String sizedFieldValue = StringUtil.fillToLength(fieldValue, fieldLength, actualFieldDescriptor.getPaddingCharacter(), StringUtil.Direction.valueOf(actualFieldDescriptor.getPaddingAlign().toString()));
			if (actualFieldDescriptor.isRemainPosition()){
				sizedFieldValue = fieldValue;
			}

			// Check for blank spaces and fill
			if (isFirstFieldDescriptor && actualFieldDescriptor.getInitialPosition() > 1) {
				int blankSpaces = actualFieldDescriptor.getInitialPosition() - 1;
				sizedFieldValue = StringUtil.fillToLength(sizedFieldValue, blankSpaces + fieldLength, ' ', StringUtil.Direction.LEFT);
			} else if (!isFirstFieldDescriptor && previousFieldDescriptor.getFinalPosition() < actualFieldDescriptor.getInitialPosition() - 1) {
				int blankSpaces = actualFieldDescriptor.getInitialPosition() - previousFieldDescriptor.getFinalPosition() - 1;
				sizedFieldValue = StringUtil.fillToLength(sizedFieldValue, blankSpaces + fieldLength, ' ', StringUtil.Direction.LEFT);
			}
			sbufRecordLine.append(sizedFieldValue);
		}

		return sbufRecordLine.toString();
	}

	private PositionalFieldDescriptor readPreviousFieldDescriptors(List<PositionalFieldDescriptor> positionalFieldDescriptors , int indexActualFieldDescriptor){
		boolean isFirstFieldDescriptor = indexActualFieldDescriptor==0;
		PositionalFieldDescriptor previousFieldDescriptor = null;
		if (!isFirstFieldDescriptor) {
			previousFieldDescriptor = positionalFieldDescriptors.get(indexActualFieldDescriptor-1);
		}
		return previousFieldDescriptor;
	}


	private <T> T createRecordInstance(Class<T> recordClazz) {
		T record;
		try {
			record = recordClazz.newInstance();
		} catch (Exception e) {
			throw new RecordParserException("Error while instantiating record class, make sure that is provided a default constructor for class " + recordClazz, e);
		}
		return record;
	}

	private String readFieldValueFromRecord(Object record, PositionalFieldDescriptor actualFieldDescriptor){
		Object fieldValueObj = readFieldValueObject(record, actualFieldDescriptor);
		return applyDecorator(actualFieldDescriptor, fieldValueObj);
	}

	private String applyDecorator(PositionalFieldDescriptor actualFieldDescriptor, Object fieldValueObj) {
		String fieldValue;
		if (fieldValueObj == null) {
            fieldValue = "";
        } else {
            try {
                FieldDecorator<Object> decorator = (FieldDecorator<Object>)actualFieldDescriptor.getDecorator();
                fieldValue = decorator.toString(fieldValueObj);
            } catch (FieldDecoratorException e) {
                throw new RecordParserException(e);
            }
        }
		return fieldValue;
	}

	private <T> Object readFieldValueObject(T record, PositionalFieldDescriptor actualFieldDescriptor) {
		Object fieldValueObj;

		if (actualFieldDescriptor.isByField()){
            fieldValueObj = readValueObjectByField(record, actualFieldDescriptor);
        }else{
            fieldValueObj = readFieldValueObjectByProperty(record, actualFieldDescriptor);
        }
		return fieldValueObj;
	}

	private <T> Object readFieldValueObjectByProperty(T record, PositionalFieldDescriptor actualFieldDescriptor) {
		Object fieldValueObj;
		Method getter = actualFieldDescriptor.getGetter();
		try {
            fieldValueObj = getter.invoke(record, new Object[]{});
        } catch (Exception e) {
            throw new RecordParserException("Error while invoking getter method: " + getter, e);
        }
		return fieldValueObj;
	}

	private <T> Object readValueObjectByField(T record, PositionalFieldDescriptor actualFieldDescriptor) {
		Object fieldValueObj;
		Field field = actualFieldDescriptor.getField();
		field.setAccessible(true);
		try {
            fieldValueObj = field.get(record);
        } catch (Exception e) {
            throw new RecordParserException("Error while reading value on field: " +  field.getName());
        }
		return fieldValueObj;
	}


	private String applyTrimOnRead(String fieldValue, PositionalFieldDescriptor actualFieldDescriptor) {
		if (actualFieldDescriptor.isTrimOnRead()) {
            fieldValue = fieldValue.trim();
        }
		return fieldValue;
	}

	private <T> void setValueByPropertyFromText(Class<T> recordClazz, T record, PositionalFieldDescriptor actualFieldDescriptor, String fieldValue) {
		Method setter;
		Class<?> getterReturnType = actualFieldDescriptor.getGetter().getReturnType();
		try {
            setter = ReflectUtil.getSetterFromGetter(actualFieldDescriptor.getGetter(), new Class<?>[]{String.class}, recordClazz);
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

	private <T> void setValueByFieldFromText(T record, PositionalFieldDescriptor actualFieldDescriptor, String fieldValue) {
		Field field = actualFieldDescriptor.getField();
		field.setAccessible(true);
		try {
            Object value =  actualFieldDescriptor.getDecorator().fromString(fieldValue);
            field.set(record, value);
        } catch (Exception e) {
            throw new FFPojoException(e);
        }
	}

	private String readFieldValue(String text, PositionalFieldDescriptor actualFieldDescriptor, PositionalFieldDescriptor previousFieldDescriptor) {
		String fieldValue = StringUtil.EMPTY;
		int initialIndex = 0;
		int finalIndex = 0 ;actualFieldDescriptor.getFinalPosition();
		if (actualFieldDescriptor.isRemainPosition()){
			if (previousFieldDescriptor != null){
				if (text.length() > previousFieldDescriptor.getFinalPosition()) {
					initialIndex = previousFieldDescriptor.getFinalPosition();
				}
			}
			finalIndex = text.length();
			fieldValue = text.substring(initialIndex, finalIndex);
        }else {
			initialIndex = actualFieldDescriptor.getInitialPosition() - 1;
			finalIndex = actualFieldDescriptor.getFinalPosition();
			if (text.length() < finalIndex) {
				if (!((PositionalRecordDescriptor) recordDescriptor).isIgnorePositionNotFound()) {
					throw new RecordParserException("The text length is less than the declared length in field mapping: " + actualFieldDescriptor.getGetter());
				}
				if (text.length() > initialIndex && initialIndex >= 0) {
					fieldValue = text.substring(initialIndex);
				}
			} else {
				fieldValue = text.substring(initialIndex, finalIndex);
			}
		}

		return applyTrimOnRead(fieldValue, actualFieldDescriptor);
	}

	@Override
	protected PositionalRecordDescriptor getRecordDescriptor() {
		return (PositionalRecordDescriptor)recordDescriptor;
	}
	
}
