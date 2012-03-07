package com.github.ffpojo.container;

import com.github.ffpojo.exception.MetadataContainerException;
import com.github.ffpojo.metadata.RecordDescriptor;

public interface MetadataContainer {

	public RecordDescriptor getRecordDescriptor(Class<?> recordClazz) throws MetadataContainerException;
	
}
