package com.github.ffpojo.reader;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.github.ffpojo.exception.MetadataReaderException;
import com.github.ffpojo.metadata.FieldDecorator;
import com.github.ffpojo.metadata.delimited.DelimitedFieldDescriptor;
import com.github.ffpojo.metadata.delimited.DelimitedRecordDescriptor;
import com.github.ffpojo.metadata.delimited.annotation.DelimitedField;
import com.github.ffpojo.metadata.delimited.annotation.DelimitedRecord;
import com.github.ffpojo.util.ReflectUtil;

class DelimitedRecordAnnotationMetadataReader extends AnnotationMetadataReader {

	public DelimitedRecordAnnotationMetadataReader(Class<?> clazz) {
		super(clazz);
	}

	@Override
	public DelimitedRecordDescriptor readMetadata() throws MetadataReaderException {
		DelimitedRecord delimitedRecordAnnotation = recordClazz.getAnnotation(DelimitedRecord.class);
		List<DelimitedFieldDescriptor> fieldDescriptors = new ArrayList<DelimitedFieldDescriptor>();
		Method[] methods = recordClazz.getMethods();
		for (Method method : methods) {
			if(ReflectUtil.isGetter(method)) {
				DelimitedField delimitedFieldAnnotation = method.getAnnotation(DelimitedField.class);								
				if (delimitedFieldAnnotation != null) {
					DelimitedFieldDescriptor fieldDescriptor = new DelimitedFieldDescriptor();
					fieldDescriptor.setPositionIndex(delimitedFieldAnnotation.positionIndex());
					fieldDescriptor.setGetter(method);
					FieldDecorator<?> decorator;
					try {
						decorator = delimitedFieldAnnotation.decorator().newInstance();
					} catch (Exception e) {
						throw new MetadataReaderException("Error while instantiating decorator class, make sure that is provided a default constructor for class " + delimitedFieldAnnotation.decorator(), e);
					}
					fieldDescriptor.setDecorator(decorator);
					fieldDescriptors.add(fieldDescriptor);
				}
			}
		}
		
		DelimitedRecordDescriptor recordDescriptor = new DelimitedRecordDescriptor(recordClazz, fieldDescriptors, delimitedRecordAnnotation.delimiter());
		recordDescriptor.sortFieldDescriptors();
		recordDescriptor.assertValid();
		
		return recordDescriptor;
	}

}
