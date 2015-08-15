package com.github.ffpojo.metadata.positional.annotation.extra;

import com.github.ffpojo.metadata.positional.PaddingAlign;

public @interface BooleanPositionalField {

	int initialPosition();
	int finalPosition();
	PaddingAlign paddingAlign() default PaddingAlign.RIGHT;
	char paddingCharacter() default ' ';
	boolean trimOnRead() default true;
	String trueIdentifier() default "true";
	String falseIdentifier() default "false";
	
}
