package com.github.ffpojo.decorator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.github.ffpojo.FFPojoHelper;
import com.github.ffpojo.decorator.util.CollectionDecoratorUtil;
import com.github.ffpojo.exception.FieldDecoratorException;
import com.github.ffpojo.metadata.extra.ExtendedFieldDecorator;

@SuppressWarnings("all")
public class CollectionDecorator  extends ExtendedFieldDecorator<Collection> {

	private final Class<?> itensCollectionType;
	private final Class<? extends Collection> collectionToReturn;
	
	public CollectionDecorator(Class itensCollectionType, Class<? extends Collection> clazzCollection){
		this.itensCollectionType =  itensCollectionType;
		this.collectionToReturn =  clazzCollection;
	}
	
	public String toString(Collection collection) throws FieldDecoratorException {
		StringBuilder sb =  new StringBuilder();
		for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
			Object object =  iterator.next();
			String s = FFPojoHelper.getInstance().parseToText(object);
			sb.append(s);
		}
		return sb.toString();
	}

	public Collection fromString(String field) throws FieldDecoratorException {
		if (field == null || field.isEmpty()) return null;
		CollectionDecoratorUtil collectionDecoratorUtil = new CollectionDecoratorUtil(itensCollectionType);
		int objectLineSize =  collectionDecoratorUtil.objectLineSize();
		int index = 0;
		Collection listObjects;
		if (Set.class.isAssignableFrom(collectionToReturn)){
			listObjects = new HashSet();
		}else{
			listObjects = new ArrayList();
		}
		while(index < field.length()-1){
			int finalPosition = index + objectLineSize;
			if (finalPosition > field.length()-1){
				finalPosition =  field.length()-1;
			}
			String item =  field.substring(index, (finalPosition));
			Object o = FFPojoHelper.getInstance().createFromText(itensCollectionType, item);
			listObjects.add(o); 
			index = finalPosition; 
		}
		return listObjects;
	}
	
	/**
	 * Should to return a array of Class.
	 * The array returned represent the types paramters of constructor
	 * @return
	 */
	public static Class<?>[] getTypesConstructorExtended(){
		return new Class[]{Class.class};
	}
	
	/**
	 * Return the methods names in annotation that contains the values to call the constructor
	 * @return
	 */
	public static String[] getMethodContainsContstructorValues(){
		return new String[]{"itemType"};
	}

}
