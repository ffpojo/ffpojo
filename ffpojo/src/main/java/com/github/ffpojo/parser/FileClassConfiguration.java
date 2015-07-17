package com.github.ffpojo.parser;

import java.util.ArrayList;
import java.util.List;

public class FileClassConfiguration {
	
	private List<Class<?>> fileClass;
	
	public FileClassConfiguration(){
	}
	
	public FileClassConfiguration addClass(Class<?> clazz){
		if (fileClass == null){
			fileClass =  new ArrayList<Class<?>>();
		}
		fileClass.add(clazz);
		return this;
		
	}
	
	Class<?> getPositionAnnotationClass(){
		return null;
	}
	

}
