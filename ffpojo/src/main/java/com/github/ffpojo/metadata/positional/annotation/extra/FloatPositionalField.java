package com.github.ffpojo.metadata.positional.annotation.extra;

import com.github.ffpojo.metadata.positional.PaddingAlign;

public @interface FloatPositionalField {
	
	int initialPosition();
	int finalPosition();
	PaddingAlign paddingAlign() default PaddingAlign.RIGHT;
	char paddingCharacter() default ' ';
	boolean trimOnRead() default true;
	int precision() default 2;
	
}
