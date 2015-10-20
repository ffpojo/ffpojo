package com.github.ffpojo.metadata.positional.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface PositionalRecord {
	public static final PositionalRecordLineIdentifier[] defaultIdentifier = new PositionalRecordLineIdentifier[]{};
	boolean ignorePositionNotFound() default false;
	boolean autoFillRemainPosition() default false;
	String textLineIdentifier() default "";
}
