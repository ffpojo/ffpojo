package com.github.ffpojo.decorator;

import java.util.Set;

public class SetDecorator extends CollectionDecorator{

	public SetDecorator(Class<?> itensCollectionType) {
		super(itensCollectionType, Set.class);
	}

}
