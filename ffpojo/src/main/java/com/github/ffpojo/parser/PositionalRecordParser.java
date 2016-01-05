package com.github.ffpojo.parser;

import com.github.ffpojo.exception.FieldDecoratorException;
import com.github.ffpojo.exception.RecordParserException;
import com.github.ffpojo.metadata.FieldDecorator;
import com.github.ffpojo.metadata.positional.PositionalFieldDescriptor;
import com.github.ffpojo.metadata.positional.PositionalRecordDescriptor;
import com.github.ffpojo.util.ReflectUtil;
import com.github.ffpojo.util.StringUtil;

import java.lang.reflect.Method;
import java.util.List;


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
			setValueFromText(recordClazz, record, actualFieldDescriptor, fieldValue);
		}
		return record;
	}

	@SuppressWarnings("unchecked")
	public <T> String parseToText(T record) throws RecordParserException {
		final StringBuffer sbufRecordLine = new StringBuffer();
		final List<PositionalFieldDescriptor> positionalFieldDescriptors = getRecordDescriptor().getFieldDescriptors();

		for (int i = 0; i < positionalFieldDescriptors.size(); i++) {
			final PositionalFieldDescriptor actualFieldDescriptor = positionalFieldDescriptors.get(i);
			if (actualFieldDescriptor.isFullLineField()){
				continue;
			}
			final PositionalFieldDescriptor previousFieldDescriptor = readPreviousFieldDescriptors(positionalFieldDescriptors, i);

			final boolean isFirstFieldDescriptor =  previousFieldDescriptor == null;

			final String fieldValue = readFieldValueFromRecord(record, actualFieldDescriptor);
			int fieldLength = actualFieldDescriptor.getFinalPosition() - actualFieldDescriptor.getInitialPosition() + 1;
			String sizedFieldValue = StringUtil.fillToLength(fieldValue, fieldLength, actualFieldDescriptor.getPaddingCharacter(), StringUtil.Direction.valueOf(actualFieldDescriptor.getPaddingAlign().toString()));
			if (actualFieldDescriptor.isPositionalFieldRemainder()){
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

	@SuppressWarnings("unchecked")
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
		Method getter = actualFieldDescriptor.getGetter();
		try {
            return getter.invoke(record);
        } catch (Exception e) {
            throw new RecordParserException("Error while invoking getter method: " + getter, e);
        }
	}

	private String applyTrimOnRead(String fieldValue, PositionalFieldDescriptor actualFieldDescriptor) {
		if (actualFieldDescriptor.isTrimOnRead()) {
            fieldValue = fieldValue.trim();
        }
		return fieldValue;
	}

	private <T> void setValueFromText(Class<T> recordClazz, T record, PositionalFieldDescriptor actualFieldDescriptor, String fieldValue) {
		Method setter = null;
		Class<?> getterReturnType = actualFieldDescriptor.getGetter().getReturnType();
		try {
            setter = ReflectUtil.getSetterFromGetter(actualFieldDescriptor.getGetter(), new Class<?>[]{String.class}, recordClazz);
        } catch (NoSuchMethodException e1) {
            try {
                setter = ReflectUtil.getSetterFromGetter(actualFieldDescriptor.getGetter(), new Class<?>[] {getterReturnType}, recordClazz);
            } catch (NoSuchMethodException e2) {
                logger.warning("Compatible setter not found for getter " + actualFieldDescriptor.getGetter());
            }
        }
		setValue(record, actualFieldDescriptor, fieldValue, setter);
	}

	private String readFieldValue(String text, PositionalFieldDescriptor actualFieldDescriptor, PositionalFieldDescriptor previousFieldDescriptor) {
		String fieldValue = StringUtil.EMPTY;
		int initialIndex = 0;
		int finalIndex;
		if (actualFieldDescriptor.isFullLineField()){
			return text;
		}
		if (actualFieldDescriptor.isPositionalFieldRemainder()){
			if (previousFieldDescriptor != null){
				if (text.length() > previousFieldDescriptor.getFinalPosition()) {
					initialIndex = previousFieldDescriptor.getFinalPosition();
				}
			}
			if (previousFieldDescriptor == null || (initialIndex > 0 && initialIndex <  text.length())){
				finalIndex = text.length();
				fieldValue = text.substring(initialIndex, finalIndex);
			}
        }else {
			initialIndex = actualFieldDescriptor.getInitialPosition() - 1;
			finalIndex = actualFieldDescriptor.getFinalPosition();
			if (text.length() < finalIndex) {
				if (!((PositionalRecordDescriptor) recordDescriptor).isIgnoreMissingFieldsInTheEnd()) {
					throw new RecordParserException("The text length is less than the declared length in field mapping: " + actualFieldDescriptor.getGetter().getName());
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
