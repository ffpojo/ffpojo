package com.github.ffpojo.file.reader;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.ffpojo.FFPojoHelper;
import com.github.ffpojo.exception.FFPojoException;
import com.github.ffpojo.file.reader.extra.IdentifierLine;
import com.github.ffpojo.metadata.positional.annotation.PositionalRecord;
import com.github.ffpojo.metadata.positional.annotation.PositionalRecordLineIdentifier;
import com.github.ffpojo.parser.RecordParser;


public class FlatFileReaderDefinition {
	
	private final FFPojoHelper ffpojoHelper = FFPojoHelper.getInstance();
	
	private Class<?> body;
	private Class<?> header;
	private Class<?> trailer;
	
	@Deprecated
	private RecordParser bodyParser;
	@Deprecated
	private RecordParser headerParser;
	@Deprecated
	private RecordParser trailerParser;
	
	private Map<String, Class<?>> definitions =  new HashMap<String, Class<?>>();;
	private IdentifierLine idLine;
	
	@SuppressWarnings("rawtypes")
	public FlatFileReaderDefinition(Class bodyClass) throws FFPojoException {
		this(Arrays.asList(bodyClass));
	}
	
	@SuppressWarnings({ "rawtypes"})
	public FlatFileReaderDefinition(List<Class> bodyClasses) throws FFPojoException {
		if (bodyClasses == null || bodyClasses.isEmpty()) {
			throw new IllegalArgumentException("Class<?> object is null");
		}
		createMapDefinitions(bodyClasses);
	}
	
	@SuppressWarnings("rawtypes")
	private void createMapDefinitions(List<Class> bodyClasses){
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
	
	public void setHeader(Class<?> header) throws FFPojoException {
		this.header = header;
		if (header == null) {
			this.headerParser = null;
		} else {
			this.headerParser = ffpojoHelper.getRecordParser(header);
		}
	}
	
	public void setTrailer(Class<?> trailer) throws FFPojoException {
		this.trailer = trailer;
		if (trailer == null) {
			this.trailerParser = null;
		} else {
			this.trailerParser = ffpojoHelper.getRecordParser(trailer);
		}
	}
	
	// GETTERS AND SETTERS
	
	public Class<?> getBody(String message) {
		return body = this.definitions.get(message.substring(idLine.getStartPosition(), idLine.getSize()));
	}
	
	public Class<?> getBody() {
		return body;
	}
	public Class<?> getHeader() {
		return header;
	}
	public Class<?> getTrailer() {
		return trailer;
	}
	
	@Deprecated
	public RecordParser getBodyParser() {
		return bodyParser;
	}
	@Deprecated
	public RecordParser getHeaderParser() {
		return headerParser;
	}
	@Deprecated
	public RecordParser getTrailerParser() {
		return trailerParser;
	}
}
