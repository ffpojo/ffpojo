package com.github.ffpojo.decorator;

import java.util.List;

public class ListDecorator extends CollectionDecorator {

	public ListDecorator(Class<?> itensCollectionType) {
		super(itensCollectionType, List.class);
	}

}
