package com.github.ffpojo.decorator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.github.ffpojo.FFPojoHelper;
import com.github.ffpojo.decorator.util.CollectionDecoratorUtil;
import com.github.ffpojo.exception.FieldDecoratorException;
import com.github.ffpojo.metadata.FieldDecorator;
import com.github.ffpojo.metadata.extra.ExtendedFieldDecorator;

@SuppressWarnings("all")
public class CollectionDecorator/*<T extends Collection<?>>*/ extends ExtendedFieldDecorator<Collection> {

	private Class<?> itensCollectionType;
	
	public CollectionDecorator(Class<?> itensCollectionType){
		this.itensCollectionType =  itensCollectionType;
	}
	
	public String toString(Collection collection) throws FieldDecoratorException {
		StringBuilder sb =  new StringBuilder();
		for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			String s = FFPojoHelper.getInstance().parseToText(object);
			sb.append(s);
		}
		return sb.toString();
	}

	public Collection fromString(String field) throws FieldDecoratorException {
		CollectionDecoratorUtil collectionDecoratorUtil = new CollectionDecoratorUtil(itensCollectionType);
		int objectLineSize =  collectionDecoratorUtil.objectLineSize();
		int index = 0;
		List listObjects =  new ArrayList();
		while(index < field.length()){
			int finalPosition = index + objectLineSize;
			String item =  field.substring(index, (finalPosition));
			Object o = FFPojoHelper.getInstance().createFromText(itensCollectionType, item);
			listObjects.add(o); 
			index = finalPosition + 1 ; 
		}
		return listObjects;
	}
	
	/**
	 * Should to return a array of Class.
	 * The array returned represent the types paramters of constructor
	 * @return
	 */
	public static Class<?>[] getTypesConstructorExtended(){
		return new Class[]{Collection.class, Object.class};
	}
	
	/**
	 * Return the methods names in annotation that contains the values to call the constructor
	 * @return
	 */
	public static String[] getMethodContainsContstructorValues(){
		return new String[]{"itemType", "collectionType"};
	}

}
