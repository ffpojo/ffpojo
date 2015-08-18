package com.github.ffpojo.file.reader;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.ffpojo.exception.FFPojoException;
import com.github.ffpojo.file.reader.extra.IdentifierLine;
import com.github.ffpojo.metadata.positional.annotation.PositionalRecord;
import com.github.ffpojo.metadata.positional.annotation.PositionalRecordLineIdentifier;

public class FlatFileReaderMultiDefinition{
	
	private Class<?> body;
	private Class<?> header;
	private Class<?> trailer;

	
	private Map<String, Class<?>> definitions =  new HashMap<String, Class<?>>();;
	private IdentifierLine idLine;
	
	
	@SuppressWarnings("rawtypes")
	public FlatFileReaderMultiDefinition(Class bodyClass) throws FFPojoException {
		this(Arrays.asList(bodyClass));
	}
	
	@SuppressWarnings("rawtypes")
	public FlatFileReaderMultiDefinition(List<Class> bodyClasses) throws FFPojoException {
		if (bodyClasses == null || bodyClasses.isEmpty()) {
			throw new IllegalArgumentException("Class<?> object is null");
		}
		createMapDefinitions(bodyClasses);
	}
	
	@SuppressWarnings("rawtypes")
	public void createMapDefinitions(List<Class> bodyClasses){
		int size=0;
		int startPosition=0;
		for (Class<?> bodyClass : bodyClasses) {
			if (bodyClass.isAnnotationPresent(PositionalRecord.class)){
				PositionalRecord pr =  bodyClass.getAnnotation(PositionalRecord.class);
				PositionalRecordLineIdentifier lineIdentifier =  pr.recordLineIdentifier();
				size = lineIdentifier.textIdentifier().length();
				startPosition =  lineIdentifier.startPosition();
				definitions.put(lineIdentifier.textIdentifier(), bodyClass);
			}
		}
		idLine =  new IdentifierLine(startPosition, size);
	}
	
	// GETTERS AND SETTERS
	public Class<?> getBody() {
		return body;
	}
	
	public Class<?> getBody(String message) {
		return body = this.definitions.get(message.substring(idLine.getStartPosition(), idLine.getSize()));
	}
	
	public Class<?> getHeader() {
		return header;
	}
	public Class<?> getTrailer() {
		return trailer;
	}

	public void setHeader(Class<?> header) {
		this.header = header;
	}

	public void setTrailer(Class<?> trailer) {
		this.trailer = trailer;
	}
	
	
}
