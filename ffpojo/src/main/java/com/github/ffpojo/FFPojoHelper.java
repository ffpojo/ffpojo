package com.github.ffpojo;

import com.github.ffpojo.container.HybridMetadataContainer;
import com.github.ffpojo.container.MetadataContainer;
import com.github.ffpojo.exception.FFPojoException;
import com.github.ffpojo.metadata.RecordDescriptor;
import com.github.ffpojo.parser.RecordParser;
import com.github.ffpojo.parser.RecordParserFactory;


public class FFPojoHelper {

	private static FFPojoHelper singletonInstance;
	
	private MetadataContainer metadataContainer;
	
	private FFPojoHelper() throws FFPojoException {
		this.metadataContainer = new HybridMetadataContainer();
	}
	
	public static FFPojoHelper getInstance() throws FFPojoException {
		if (singletonInstance == null) {
			singletonInstance = new FFPojoHelper();
		}
		return singletonInstance;
	}
	
	public <T> T createFromText(Class<T> recordClazz, String text) throws FFPojoException {
		RecordDescriptor recordDescriptor = metadataContainer.getRecordDescriptor(recordClazz);
		RecordParser recordParser = RecordParserFactory.createRecordParser(recordDescriptor);
		return recordParser.parseFromText(recordClazz, text);
	}
	
	public <T> String parseToText(T record) throws FFPojoException {
		RecordDescriptor recordDescriptor = metadataContainer.getRecordDescriptor(record.getClass());
		RecordParser recordParser = RecordParserFactory.createRecordParser(recordDescriptor);
		return recordParser.parseToText(record);
	}
	
	public RecordParser getRecordParser(Class<?> recordClazz) throws FFPojoException {
		RecordDescriptor recordDescriptor = metadataContainer.getRecordDescriptor(recordClazz);
		return RecordParserFactory.createRecordParser(recordDescriptor);
	}
	
}
