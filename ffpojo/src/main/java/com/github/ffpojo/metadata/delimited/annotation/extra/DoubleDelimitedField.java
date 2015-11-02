package com.github.ffpojo.metadata.delimited.annotation.extra;

import com.github.ffpojo.metadata.positional.PaddingAlign;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface DoubleDelimitedField {
	int positionIndex();
	int precision() default 2;
	
}