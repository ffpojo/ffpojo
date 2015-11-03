package com.github.ffpojo.metadata.extra;


import com.github.ffpojo.decorator.*;
import com.github.ffpojo.exception.FFPojoException;
import com.github.ffpojo.exception.MetadataReaderException;
import com.github.ffpojo.metadata.FieldDecorator;
import com.github.ffpojo.metadata.positional.annotation.extra.RemainPositionalField;
import com.github.ffpojo.util.ReflectUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class FFPojoAnnotationFieldManager {

    final private static Map<String, Class<? extends FieldDecorator<?>>> mapAnnotationDecoratorClass = new HashMap<String,  Class<? extends FieldDecorator<?>>>();

    static{
        mapAnnotationDecoratorClass.put("date", InternalDateDecorator.class);
        mapAnnotationDecoratorClass.put("long", InternalLongDecorator.class);
        mapAnnotationDecoratorClass.put("integer",InternalIntegerDecorator.class);
        mapAnnotationDecoratorClass.put("boolean", InternalBooleanDecorator.class);
        mapAnnotationDecoratorClass.put("list", InternalListDecorator.class);
        mapAnnotationDecoratorClass.put("set", InternalSetDecorator.class);
        mapAnnotationDecoratorClass.put("double", InternalDoubleDecorator.class);
        mapAnnotationDecoratorClass.put("float", InternalFloatDecorator.class);
        mapAnnotationDecoratorClass.put("bigdecimal", InternalBigDecimalDecorator.class);
        mapAnnotationDecoratorClass.put("biginteger", InternalBigIntegerDecorator.class);
        mapAnnotationDecoratorClass.put("enum", InternalEnumDecorator.class);
    }

    public Class<?> getClassDecorator(Class<? extends Annotation> clazzPositionalFieldAnnotation){
        if (!isFFPojoAnnotationField(clazzPositionalFieldAnnotation)){
            throw new FFPojoException(String.format("The class %s not seem a DelimitedField or PositionalField annotation.", clazzPositionalFieldAnnotation));
        }
        final String dataType =  clazzPositionalFieldAnnotation.getSimpleName()
                .replaceAll("DelimitedField", "")
                .replaceAll("PositionalField", "")
                .toLowerCase();
        return mapAnnotationDecoratorClass.get(dataType);
    }

    public boolean isFFPojoAnnotationField(Class<? extends Annotation> annotation){
        return isDelimitedField(annotation) || isPositionalField(annotation);
    }

    public boolean isRemainPositionalField(Class<? extends Annotation> annotation){
        return annotation.isAssignableFrom(RemainPositionalField.class);
    }

    public boolean isPositionalField(Class<? extends Annotation> annotation){
        try{
            annotation.getMethod("initialPosition");
            annotation.getMethod("finalPosition");
        }catch (Exception e){
            return isRemainPositionalField(annotation);
        }
        return true;
    }

    public boolean isDelimitedField(Class<? extends Annotation> annotation){
        try{
            annotation.getMethod("positionIndex");
        }catch (Exception e){
            return false;
        }
        return true;
    }


    public boolean isFieldAlreadyFFPojoAnnotation( Field field) {
        Annotation[] annotationsField =  field.getAnnotations();
        boolean hasFieldAnnotaded =  false;
        for (int i = 0; i < annotationsField.length; i++) {
            hasFieldAnnotaded = this.isFFPojoAnnotationField(annotationsField[i].annotationType());
            if (hasFieldAnnotaded){
                return hasFieldAnnotaded;
            }
        }
        return hasFieldAnnotaded;
    }


    public FieldDecorator<?> createNewInstanceDecorator(Annotation fieldAnnotation) throws MetadataReaderException {
        Class<? extends Annotation> clazzPositionalFieldAnnotation =  fieldAnnotation.annotationType();
        Class<?> clazzFieldDecorator = this.getClassDecorator(clazzPositionalFieldAnnotation);
        if (clazzFieldDecorator!= null && ReflectUtil.getSuperClassesOf(clazzFieldDecorator).contains(ExtendedFieldDecorator.class)){
            try {
                Method getTypesConstructorExtended =  clazzFieldDecorator.getMethod("getTypesConstructorExtended");
                Method getMethodContainsContstructorValues =  clazzFieldDecorator.getMethod("getMethodContainsContstructorValues");

                Class<?>[] typesToConstructor = (Class[]) getTypesConstructorExtended.invoke(null);
                String[] methodsName = (String[]) getMethodContainsContstructorValues.invoke(null);
                Object[] parameters =  new Object[methodsName.length];
                for (int i = 0; i < methodsName.length; i++) {
                    Method method = clazzPositionalFieldAnnotation.getMethod(methodsName[i]);
                    parameters[i] = method.invoke(fieldAnnotation);
                }
                clazzFieldDecorator.getConstructors();
                FieldDecorator<?> filedDecorator = (FieldDecorator<?>) clazzFieldDecorator.getConstructor(typesToConstructor).newInstance(parameters);
                return filedDecorator;
            } catch (Exception e) {
                throw new FFPojoException(e);
            }
        }
        return getDecoratorInstance(fieldAnnotation);
    }

    @SuppressWarnings("all")
    private FieldDecorator<?> getDecoratorInstance(Annotation annotation){
		Class<? extends FieldDecorator> decorator = null;
        try {
            decorator = (Class<? extends FieldDecorator>) annotation.getClass().getMethod("decorator").invoke(annotation);
            return decorator.newInstance();
        } catch (Exception e) {
            throw new MetadataReaderException("Error while instantiating decorator class, make sure that is provided a default constructor for class " + decorator, e);
        }

    }

}
