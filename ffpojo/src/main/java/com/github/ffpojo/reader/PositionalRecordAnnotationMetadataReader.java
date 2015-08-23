package com.github.ffpojo.reader;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.ffpojo.decorator.BooleanDecorator;
import com.github.ffpojo.decorator.CollectionDecorator;
import com.github.ffpojo.decorator.DateDecorator;
import com.github.ffpojo.decorator.DoubleDecorator;
import com.github.ffpojo.decorator.IntegerDecorator;
import com.github.ffpojo.decorator.LongDecorator;
import com.github.ffpojo.exception.FFPojoRuntimeException;
import com.github.ffpojo.exception.MetadataReaderException;
import com.github.ffpojo.metadata.FieldDecorator;
import com.github.ffpojo.metadata.extra.ExtendedFieldDecorator;
import com.github.ffpojo.metadata.positional.PaddingAlign;
import com.github.ffpojo.metadata.positional.PositionalFieldDescriptor;
import com.github.ffpojo.metadata.positional.PositionalRecordDescriptor;
import com.github.ffpojo.metadata.positional.annotation.AccessorType;
import com.github.ffpojo.metadata.positional.annotation.FFPojoAccessorType;
import com.github.ffpojo.metadata.positional.annotation.PositionalField;
import com.github.ffpojo.metadata.positional.annotation.PositionalRecord;
import com.github.ffpojo.metadata.positional.annotation.extra.BooleanPositionalField;
import com.github.ffpojo.metadata.positional.annotation.extra.CollectionPositionalField;
import com.github.ffpojo.metadata.positional.annotation.extra.DatePositionalFiled;
import com.github.ffpojo.metadata.positional.annotation.extra.DoublePositionalField;
import com.github.ffpojo.metadata.positional.annotation.extra.IntegerPositionalField;
import com.github.ffpojo.metadata.positional.annotation.extra.LongPositionalField;
import com.github.ffpojo.util.ReflectUtil;

class PositionalRecordAnnotationMetadataReader extends AnnotationMetadataReader {

	public PositionalRecordAnnotationMetadataReader(Class<?> clazz) {
		super(clazz);
	}

	@Override
	public PositionalRecordDescriptor readMetadata() throws MetadataReaderException {
		
		final AccessorType accessorType = getAccessorType();
		final PositionalRecordDescriptor recordDescriptor = getRecordDescriptor(accessorType);
		recordDescriptor.sortFieldDescriptors();
		recordDescriptor.assertValid();
		recordDescriptor.setIgnorePositionNotFound(recordClazz.getAnnotation(PositionalRecord.class).ignorePositionNotFound());
		return recordDescriptor;
	}

	
	private AccessorType getAccessorType() {
		AccessorType accessorType =  AccessorType.PROPERTY;
		if (super.recordClazz.isAnnotationPresent(FFPojoAccessorType.class)){
			accessorType =  super.recordClazz.getAnnotation(FFPojoAccessorType.class).accessorType();
		}
		return accessorType;
	}

	
	private PositionalRecordDescriptor getRecordDescriptor(AccessorType accessorType) throws MetadataReaderException {
		List<PositionalFieldDescriptor> fieldDescriptors = new ArrayList<PositionalFieldDescriptor>();
		if (AccessorType.FIELD.equals(accessorType)){
			return readPositionalFieldDescriptorByAccessorTypeField(fieldDescriptors);
		}
		return readPositionalFieldDescriptorByAccessorTypeProperty(fieldDescriptors);
	}

	private PositionalRecordDescriptor readPositionalFieldDescriptorByAccessorTypeField(List<PositionalFieldDescriptor> fieldDescriptors) throws MetadataReaderException {
		final List<Field> fields = ReflectUtil.getAnnotadedFields(recordClazz);
		for (Field field : fields) {
			Annotation[] annotations =  field.getAnnotations();
			 for (Annotation annotation : annotations) {
					if (isAnnotationPositionalField(annotation)){
						PositionalFieldDescriptor fieldDescriptor = positionalDescriptorFromPositionalField(annotation);
						fieldDescriptor.setAccessorType(AccessorType.FIELD);
						fieldDescriptor.setField(field);
						fieldDescriptors.add(fieldDescriptor);
					}
				}
		}

		PositionalRecordDescriptor recordDescriptor = new PositionalRecordDescriptor(recordClazz, fieldDescriptors);
		return recordDescriptor;
	}

	private PositionalRecordDescriptor readPositionalFieldDescriptorByAccessorTypeProperty(List<PositionalFieldDescriptor> fieldDescriptors) throws MetadataReaderException {
		Method[] methods = recordClazz.getMethods();
		for (Method method : methods) {
			if(ReflectUtil.isGetter(method)) {
				Annotation[] annotations =  method.getAnnotations();
				for (Annotation annotation : annotations) {
					if (isAnnotationPositionalField(annotation)){
						PositionalFieldDescriptor fieldDescriptor = positionalDescriptorFromPositionalField(annotation);
						fieldDescriptor.setAccessorType(AccessorType.PROPERTY);
						fieldDescriptor.setGetter(method);
						fieldDescriptors.add(fieldDescriptor);
					}
				}
			}
		}
		
		PositionalRecordDescriptor recordDescriptor = new PositionalRecordDescriptor(recordClazz, fieldDescriptors);
		return recordDescriptor;
	}
	
	private PositionalFieldDescriptor positionalDescriptorFromPositionalField(Annotation positionalFieldAnnotation) {
		PositionalFieldDescriptor fieldDescriptor = new PositionalFieldDescriptor();
		Class<?> clazz =  positionalFieldAnnotation.annotationType();
		try{
			fieldDescriptor.setInitialPosition(((Integer) clazz.getMethod("initialPosition").invoke(positionalFieldAnnotation)));
			fieldDescriptor.setFinalPosition(((Integer) clazz.getMethod("finalPosition").invoke(positionalFieldAnnotation)));
			fieldDescriptor.setPaddingAlign(((PaddingAlign) clazz.getMethod("paddingAlign").invoke(positionalFieldAnnotation)));
			fieldDescriptor.setPaddingCharacter((((Character) clazz.getMethod("paddingCharacter").invoke(positionalFieldAnnotation))));
			fieldDescriptor.setTrimOnRead(((Boolean) clazz.getMethod("trimOnRead").invoke(positionalFieldAnnotation)));
			fieldDescriptor.setDecorator(createNewInstanceDecorator(positionalFieldAnnotation));
			return fieldDescriptor;			
		}catch(Exception e){
			throw new FFPojoRuntimeException(e);
		}
	}

	private FieldDecorator<?> createNewInstanceDecorator(Annotation positionalFieldAnnotation) throws MetadataReaderException {
		Class<?> clazzPositionalFieldAnnotation =  positionalFieldAnnotation.annotationType();
		Class<?> clazzFieldDecorator = getClassDecorator(positionalFieldAnnotation, clazzPositionalFieldAnnotation);
		if (clazzFieldDecorator!= null && ReflectUtil.getSuperClassesOf(clazzFieldDecorator).contains(ExtendedFieldDecorator.class)){
			try {
				Method getTypesConstructorExtended =  clazzFieldDecorator.getMethod("getTypesConstructorExtended");
				Method getMethodContainsContstructorValues =  clazzFieldDecorator.getMethod("getMethodContainsContstructorValues");
				
				Class<?>[] typesToConstructor = (Class[]) getTypesConstructorExtended.invoke(null);
				String[] methodsName = (String[]) getMethodContainsContstructorValues.invoke(null);
				Object[] parameters =  new Object[methodsName.length];
				for (int i = 0; i < methodsName.length; i++) {
					Method method = clazzPositionalFieldAnnotation.getMethod(methodsName[i]);
					parameters[i] = method.invoke(positionalFieldAnnotation);
				}
				FieldDecorator<?> filedDecorator = (FieldDecorator<?>) clazzFieldDecorator.getConstructor(typesToConstructor).newInstance(parameters);
				return filedDecorator;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return getDecoratorInstance((PositionalField)positionalFieldAnnotation);
	}

	private Class<?> getClassDecorator(Annotation positionalFieldAnnotation, Class<?> clazzPositionalFieldAnnotation)
			throws FFPojoRuntimeException {
		try{
			Class<?> clazzFieldDecorator = (Class<?>) getClassDecorator(positionalFieldAnnotation);
			return clazzFieldDecorator;
			
		}catch(Exception e){
			throw new FFPojoRuntimeException(e);
		}
	}
	
	private Class<?> getClassDecorator(Annotation positionalField){
		Class<?> type =  positionalField.annotationType();
		Map<Class<? extends Annotation>, Class<? extends FieldDecorator<?>>> mapAnnotationDecoratorClass = new HashMap<Class<? extends Annotation>, Class<? extends FieldDecorator<?>>>();
		mapAnnotationDecoratorClass.put(DatePositionalFiled.class, DateDecorator.class);
		mapAnnotationDecoratorClass.put(LongPositionalField.class, LongDecorator.class);
		mapAnnotationDecoratorClass.put(IntegerPositionalField.class, IntegerDecorator.class);
		mapAnnotationDecoratorClass.put(BooleanPositionalField.class, BooleanDecorator.class);
		mapAnnotationDecoratorClass.put(CollectionPositionalField.class, CollectionDecorator.class);
		mapAnnotationDecoratorClass.put(DoublePositionalField.class, DoubleDecorator.class);
//		mapAnnotationDecoratorClass.put(DatePositionalFiled.class, DateDecorator.class);
//		mapAnnotationDecoratorClass.put(DatePositionalFiled.class, DateDecorator.class);
//		mapAnnotationDecoratorClass.put(DatePositionalFiled.class, DateDecorator.class);
		return mapAnnotationDecoratorClass.get(type);
	}

	private FieldDecorator<?> getDecoratorInstance(PositionalField positionalFieldAnnotation)
			throws MetadataReaderException {
		FieldDecorator<?> decorator;
		try {
			decorator = positionalFieldAnnotation.decorator().newInstance();
		} catch (Exception e) {
			throw new MetadataReaderException("Error while instantiating decorator class, make sure that is provided a default constructor for class " + positionalFieldAnnotation.decorator(), e);
		}
		return decorator;
	}
	
	private static boolean isAnnotationPositionalField(Annotation annotation){
			try{
				annotation.annotationType().getMethod("initialPosition");
				annotation.annotationType().getMethod("finalPosition");
			}catch (Exception e){
				return false;
			}
			return true;
	}
}
