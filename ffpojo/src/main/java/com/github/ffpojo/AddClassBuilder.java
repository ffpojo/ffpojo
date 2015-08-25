package com.github.ffpojo;

import java.util.List;

public interface AddClassBuilder {

	public FFReaderBuilder addRecordClass(Class<?> clazz);
	
	public FFReaderBuilder addRecordClasses(List<Class<?>> clazz);
	
}
