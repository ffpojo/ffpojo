package com.github.ffpojo.reader;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.ffpojo.exception.FFPojoException;
import com.github.ffpojo.exception.MetadataReaderException;
import com.github.ffpojo.metadata.DefaultFieldDecorator;
import com.github.ffpojo.metadata.extra.FFPojoAnnotationFieldManager;
import com.github.ffpojo.metadata.positional.PaddingAlign;
import com.github.ffpojo.metadata.positional.PositionalFieldDescriptor;
import com.github.ffpojo.metadata.positional.PositionalRecordDescriptor;
import com.github.ffpojo.metadata.positional.annotation.AccessorType;
import com.github.ffpojo.metadata.positional.annotation.PositionalRecord;
import com.github.ffpojo.util.ReflectUtil;

/**
 * @author  Gilberto Holms -  gibaholms@hotmail.com
 * @author  William Miranda  -  blackstile@hotmail.com
 */
class PositionalRecordAnnotationMetadataReader extends AnnotationMetadataReader {

	private FFPojoAnnotationFieldManager annotationFieldManager =  new FFPojoAnnotationFieldManager();
	public PositionalRecordAnnotationMetadataReader(Class<?> clazz) {
		super(clazz);
	}

	@Override
	public PositionalRecordDescriptor readMetadata() throws MetadataReaderException {

		final PositionalRecordDescriptor recordDescriptor = getRecordDescriptor();
		recordDescriptor.assertValid();
		recordDescriptor.setIgnoreMissingFieldsInTheEnd(recordClazz.getAnnotation(PositionalRecord.class).ignoreMissingFieldsInTheEnd());
		recordDescriptor.sortFieldDescriptors();
		return recordDescriptor;
	}

	private PositionalRecordDescriptor getRecordDescriptor() throws MetadataReaderException {
		final Set<PositionalFieldDescriptor> fieldDescriptors = readPositionalFieldDescriptor();
		fieldDescriptors.addAll(readPositionalFieldDescriptorOnProperty());
		return new PositionalRecordDescriptor(recordClazz, new ArrayList<PositionalFieldDescriptor>(fieldDescriptors));
	}

	private Set<PositionalFieldDescriptor> readPositionalFieldDescriptor() throws MetadataReaderException {
		final Set<PositionalFieldDescriptor> fieldDescriptors = new HashSet<PositionalFieldDescriptor>();
		final List<Field> fields = ReflectUtil.getRecursiveFields(recordClazz);
		for (Field field : fields) {
			readFieldDescriptor(fieldDescriptors, field);
		}
		return fieldDescriptors;
	}

	private void readFieldDescriptor(final Set<PositionalFieldDescriptor> fieldDescriptors, Field field) {
		Annotation[] annotations = field.getAnnotations();
		for (Annotation annotation : annotations) {
			if (annotationFieldManager.isPositionalField(annotation.annotationType())) {
				PositionalFieldDescriptor fieldDescriptor = createPositionalDescriptor(annotation);
				try {
					fieldDescriptor.setGetter(ReflectUtil.getGetterFromFieldName(field.getName(), recordClazz));
				} catch (NoSuchMethodException e) {
					throw new FFPojoException(String.format("Not found getter method to field %s ", field.getName()), e);
				}
				fieldDescriptors.add(fieldDescriptor);
			}
		}
	}

	private List<PositionalFieldDescriptor> readPositionalFieldDescriptorOnProperty() throws MetadataReaderException {
		final List<PositionalFieldDescriptor> fieldDescriptors = new ArrayList<PositionalFieldDescriptor>();
		Method[] methods = recordClazz.getMethods();
		for (Method method : methods) {
			if(ReflectUtil.isGetter(method)) {
				Annotation[] annotations =  method.getAnnotations();
				for (Annotation annotation : annotations) {
					if (annotationFieldManager.isPositionalField(annotation.annotationType())){
						try {
							final String fieldName = ReflectUtil.getFieldNameFromGetterOrSetter(method);
							Field field  =  recordClazz.getDeclaredField(fieldName);
							if (!annotationFieldManager.isFieldAnnotedWithFFPojoAnnotation(field)){
								PositionalFieldDescriptor fieldDescriptor = createPositionalDescriptor(annotation);
								fieldDescriptor.setGetter(method);
								fieldDescriptors.add(fieldDescriptor);
							}
						} catch (NoSuchFieldException e) {
							throw new MetadataReaderException(e);
						}
					}
				}
			}
		}
		
		return fieldDescriptors;
	}

	private PositionalFieldDescriptor createPositionalDescriptor(Annotation positionalFieldAnnotation) {
		PositionalFieldDescriptor fieldDescriptor = new PositionalFieldDescriptor();
		Class<? extends Annotation> clazz =  positionalFieldAnnotation.annotationType();
		try{
			fieldDescriptor.setPaddingAlign(((PaddingAlign) clazz.getMethod("paddingAlign").invoke(positionalFieldAnnotation)));
			fieldDescriptor.setPaddingCharacter((((Character) clazz.getMethod("paddingCharacter").invoke(positionalFieldAnnotation))));
			fieldDescriptor.setTrimOnRead(((Boolean) clazz.getMethod("trimOnRead").invoke(positionalFieldAnnotation)));
			fieldDescriptor.setDecorator(new DefaultFieldDecorator());
			final boolean isFullLineField = annotationFieldManager.isFullLineField(clazz);
			final boolean isPositionalFieldRemainder = annotationFieldManager.isPositionalFieldRemainder(clazz);
			fieldDescriptor.setIsFullLineField(isFullLineField);
			fieldDescriptor.setRemainPosition(isPositionalFieldRemainder);
			if (!(isFullLineField || isPositionalFieldRemainder)){
				fieldDescriptor.setDecorator(annotationFieldManager.createNewInstanceDecorator(positionalFieldAnnotation));
				fieldDescriptor.setFinalPosition(((Integer) clazz.getMethod("finalPosition").invoke(positionalFieldAnnotation)));
				fieldDescriptor.setInitialPosition(((Integer) clazz.getMethod("initialPosition").invoke(positionalFieldAnnotation)));
			}
			return fieldDescriptor;
		}catch(Exception e){
			throw new FFPojoException(e);
		}
	}

}
