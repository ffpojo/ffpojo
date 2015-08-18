package com.github.ffpojo.metadata.positional.annotation.extra;

import java.util.Collection;
import java.util.List;

import com.github.ffpojo.metadata.positional.PaddingAlign;

public @interface CollectionPositionalField {

	int initialPosition();
	int finalPosition();
	PaddingAlign paddingAlign() default PaddingAlign.RIGHT;
	char paddingCharacter() default ' ';
	boolean trimOnRead() default true;
	@SuppressWarnings("rawtypes")
	Class<? extends Collection> collectionType() default List.class;
	Class<?> collectionItemType() default Object.class;
	
}
