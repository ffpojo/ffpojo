package com.github.ffpojo.metadata.delimited.annotation.extra;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface BooleanDelimitedField {
	int positionIndex();
	String trueIdentifier() default "true";
	String falseIdentifier() default "false";
	
}
