package com.github.ffpojo;

import com.github.ffpojo.container.HybridMetadataContainer;
import com.github.ffpojo.container.MetadataContainer;
import com.github.ffpojo.exception.FFPojoException;
import com.github.ffpojo.exception.MetadataContainerException;
import com.github.ffpojo.metadata.RecordDescriptor;
import com.github.ffpojo.parser.RecordParser;
import com.github.ffpojo.parser.RecordParserFactory;


public class FFPojoHelper {

	private static FFPojoHelper singletonInstance;
	
	private MetadataContainer metadataContainer;
	
	private FFPojoHelper() throws FFPojoException {
		try {
			this.metadataContainer = new HybridMetadataContainer();
		} catch (MetadataContainerException e) {
			throw new FFPojoException(e);
		}
	}
	
	public static FFPojoHelper getInstance() throws FFPojoException {
		if (singletonInstance == null) {
			singletonInstance = new FFPojoHelper();
		}
		return singletonInstance;
	}
	
	public <T> T createFromText(Class<T> recordClazz, String text) throws FFPojoException {
		try{
			RecordDescriptor recordDescriptor = metadataContainer.getRecordDescriptor(recordClazz);
			RecordParser recordParser = RecordParserFactory.createRecordParser(recordDescriptor);
			return recordParser.parseFromText(recordClazz, text);
		}catch(Exception e){
			throw new FFPojoException(e);
		}
		
	}
	
	public <T> String parseToText(T record) throws FFPojoException {
		try{			
			RecordDescriptor recordDescriptor = metadataContainer.getRecordDescriptor(record.getClass());
			RecordParser recordParser = RecordParserFactory.createRecordParser(recordDescriptor);
			return recordParser.parseToText(record);
		}catch(Exception e){
			throw new FFPojoException(e);
		}
	}
	
	public RecordParser getRecordParser(Class<?> recordClazz) throws FFPojoException {
		try{
			RecordDescriptor recordDescriptor = metadataContainer.getRecordDescriptor(recordClazz);
			return RecordParserFactory.createRecordParser(recordDescriptor);			
		}catch(Exception e){
			throw new FFPojoException(e);
		}
	}
	
}
