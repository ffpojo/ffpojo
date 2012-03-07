package com.github.ffpojo.container;

import com.github.ffpojo.exception.MetadataContainerException;
import com.github.ffpojo.exception.MetadataNotFoundException;
import com.github.ffpojo.exception.MetadataReaderException;
import com.github.ffpojo.metadata.RecordDescriptor;
import com.github.ffpojo.reader.AnnotationMetadataReader;
import com.github.ffpojo.reader.AnnotationMetadataReaderFactory;


class AnnotationMetadataContainer extends BaseMetadataContainer implements MetadataContainer {

	public RecordDescriptor getRecordDescriptor(Class<?> recordClazz) throws MetadataContainerException {
		RecordDescriptor recordDescriptor = recordDescriptorByClazzMap.get(recordClazz);
		if (recordDescriptor == null) {
			AnnotationMetadataReader annotationMetadataReader;
			try {
				annotationMetadataReader = AnnotationMetadataReaderFactory.createAnnotationMetadataReader(recordClazz);
				recordDescriptor = annotationMetadataReader.readMetadata();
				recordDescriptorByClazzMap.put(recordClazz, recordDescriptor);
			} catch (MetadataNotFoundException e) {
				recordDescriptor = null;
			} catch (MetadataReaderException e) {
				throw new MetadataContainerException("Error while reading annotation metadata for class " + recordClazz, e);
			}
		}
		return recordDescriptor;
	}

}
