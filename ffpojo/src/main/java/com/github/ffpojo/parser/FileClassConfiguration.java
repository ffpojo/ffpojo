package com.github.ffpojo.parser;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileClassConfiguration {
	
	private Map<Id, Class<?>> mappedClasses;
	
	public FileClassConfiguration(){
	}
	
	public FileClassConfiguration addClass(Class<?> clazz){
		if (this.mappedClasses == null){
			mappedClasses =  new HashMap<Id, Class<?>>();
		}
		mappedClasses.put(generateId(clazz), clazz);
		return this;
		
	}
	
	private  Id generateId (Class<?> clazz){
		List<Method> methods = Arrays.asList(clazz.getMethods());
		for (Method method : methods) {
			//TODO: fazer logica de pegar os valores da anotacao
		}
		//TODO: to implement
		return new Id();
	}
	
	private Id generateId (String line){
		//TODO: to implement
		return new Id();
	}
	
	Class<?> getClassByIdLine(String line){		
		return mappedClasses.get(generateId(line));
	}
	

}
class Id{
	
}
