package com.github.ffpojo.reader;

import com.github.ffpojo.exception.MetadataReaderException;
import com.github.ffpojo.metadata.RecordDescriptor;

public abstract class AnnotationMetadataReader {
	
	protected Class<?> recordClazz;

	protected AnnotationMetadataReader(Class<?> recordClazz) {
		this.recordClazz = recordClazz;
	}
	
	public abstract RecordDescriptor readMetadata() throws MetadataReaderException;
	
}
