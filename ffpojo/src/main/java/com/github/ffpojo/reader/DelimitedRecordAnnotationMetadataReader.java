package com.github.ffpojo.reader;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.github.ffpojo.exception.FFPojoException;
import com.github.ffpojo.exception.MetadataReaderException;
import com.github.ffpojo.metadata.FieldDecorator;
import com.github.ffpojo.metadata.delimited.DelimitedFieldDescriptor;
import com.github.ffpojo.metadata.delimited.DelimitedRecordDescriptor;
import com.github.ffpojo.metadata.delimited.annotation.DelimitedField;
import com.github.ffpojo.metadata.delimited.annotation.DelimitedRecord;
import com.github.ffpojo.metadata.extra.FFPojoAnnotationFieldManager;
import com.github.ffpojo.metadata.positional.PositionalFieldDescriptor;
import com.github.ffpojo.metadata.positional.PositionalRecordDescriptor;
import com.github.ffpojo.metadata.positional.annotation.AccessorType;
import com.github.ffpojo.util.ReflectUtil;

class DelimitedRecordAnnotationMetadataReader extends AnnotationMetadataReader {

	final private FFPojoAnnotationFieldManager annotationFieldManager =  new FFPojoAnnotationFieldManager();

	public DelimitedRecordAnnotationMetadataReader(Class<?> clazz) {
		super(clazz);
	}

	@Override
	public DelimitedRecordDescriptor readMetadata() throws MetadataReaderException {
		DelimitedRecordDescriptor recordDescriptor = getRecordDescriptor();
		recordDescriptor.sortFieldDescriptors();
		recordDescriptor.assertValid();
		return recordDescriptor;
	}

	private DelimitedRecordDescriptor getRecordDescriptor() throws MetadataReaderException {
		final DelimitedRecord delimitedRecord = recordClazz.getAnnotation(DelimitedRecord.class);
		final List<DelimitedFieldDescriptor> fieldDescriptors = readDelimitedFieldDescriptor();
		fieldDescriptors.addAll(readPositionalFieldDescriptorOnProperty());
		return new DelimitedRecordDescriptor(recordClazz, fieldDescriptors, delimitedRecord.delimiter());
	}

	private List<DelimitedFieldDescriptor> readDelimitedFieldDescriptor() throws MetadataReaderException {
		final List<DelimitedFieldDescriptor> fieldDescriptors = new ArrayList<DelimitedFieldDescriptor>();
		final List<Field> fields = ReflectUtil.getAnnotadedFields(recordClazz);
		for (Field field : fields) {
			readFieldDescriptor(fieldDescriptors, field);
		}
		return fieldDescriptors;
	}

	private void readFieldDescriptor(final List<DelimitedFieldDescriptor> fieldDescriptors, Field field) {
		Annotation[] annotations = field.getAnnotations();
		for (Annotation annotation : annotations) {
			if (annotationFieldManager.isDelimitedField(annotation.annotationType())) {
				DelimitedFieldDescriptor fieldDescriptor = createDelimitedDescriptor(annotation);
				fieldDescriptor.setAccessorType(AccessorType.FIELD);
				fieldDescriptor.setField(field);
				fieldDescriptors.add(fieldDescriptor);
			}
		}
	}

	private DelimitedFieldDescriptor createDelimitedDescriptor(Annotation annotation) {
		DelimitedFieldDescriptor fieldDescriptor = new DelimitedFieldDescriptor();
		Class<?> clazz =  annotation.annotationType();
		try {
			fieldDescriptor.setPositionIndex(((Integer) clazz.getMethod("positionIndex").invoke(annotation)));
			fieldDescriptor.setDecorator(annotationFieldManager.createNewInstanceDecorator(annotation));
		} catch (Exception e) {
			throw new FFPojoException(e);
		}
		return  fieldDescriptor;
	}

	private List<DelimitedFieldDescriptor> readPositionalFieldDescriptorOnProperty() throws MetadataReaderException {
		final List<DelimitedFieldDescriptor> fieldDescriptors = new ArrayList<DelimitedFieldDescriptor>();
		Method[] methods = recordClazz.getMethods();
		for (Method method : methods) {
			if(ReflectUtil.isGetter(method)) {
				Annotation[] annotations =  method.getAnnotations();
				for (Annotation annotation : annotations) {
					if (annotationFieldManager.isDelimitedField(annotation.annotationType())){
						try {
							final String fieldName = ReflectUtil.getFieldNameFromGetterOrSetter(method);
							Field field  =  recordClazz.getDeclaredField(fieldName);
							if (!annotationFieldManager.isFieldAlreadyFFPojoAnnotation(field)){
								DelimitedFieldDescriptor fieldDescriptor = createDelimitedDescriptor(annotation);
								fieldDescriptor.setAccessorType(AccessorType.PROPERTY);
								fieldDescriptor.setGetter(method);
								fieldDescriptors.add(fieldDescriptor);
							}
						} catch (NoSuchFieldException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

		return fieldDescriptors;
	}


}
