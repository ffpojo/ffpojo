package com.github.ffpojo.reader;

import com.github.ffpojo.exception.MetadataNotFoundException;
import com.github.ffpojo.metadata.delimited.annotation.DelimitedRecord;
import com.github.ffpojo.metadata.positional.annotation.PositionalRecord;

public class AnnotationMetadataReaderFactory {

	public static AnnotationMetadataReader createAnnotationMetadataReader(Class<?> recordClazz) throws MetadataNotFoundException {
		PositionalRecord positionalRecordAnnotation = recordClazz.getAnnotation(PositionalRecord.class);
		if (positionalRecordAnnotation != null) {
			return new PositionalRecordAnnotationMetadataReader(recordClazz);
		} else {
			DelimitedRecord delimitedRecordAnnotation = recordClazz.getAnnotation(DelimitedRecord.class);
			if (delimitedRecordAnnotation != null) {
				return new DelimitedRecordAnnotationMetadataReader(recordClazz);
			} else {
				throw new MetadataNotFoundException("Annotation metadata not found for class " + recordClazz);
			}
		}
	}
	
}
