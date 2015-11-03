package com.github.ffpojo.decorator;

import java.util.List;

public class InternalListDecorator extends CollectionDecorator {

	public InternalListDecorator(Class<?> itensCollectionType) {
		super(itensCollectionType, List.class);
	}

}
