package com.github.ffpojo.decorator;

import java.util.Collection;

import com.github.ffpojo.exception.FieldDecoratorException;
import com.github.ffpojo.metadata.FieldDecorator;

public class CollectionDecorator<T extends Collection<?>> implements FieldDecorator<T>{

	private Class<?> collectionType;
	private Class<?> itensCollectionType;
	
	public CollectionDecorator(Class<?> itensCollectionType, Class<? extends Collection<?>> collectionType){
		this.collectionType = collectionType;
		this.itensCollectionType =  itensCollectionType;
	}
	
	public String toString(T field) throws FieldDecoratorException {
		// TODO Auto-generated method stub
		return null;
	}

	public T fromString(String field) throws FieldDecoratorException {
		return null;
	}

}
