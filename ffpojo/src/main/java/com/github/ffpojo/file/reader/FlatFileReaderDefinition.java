package com.github.ffpojo.file.reader;

import java.util.*;

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
	
	private Map<IdentifierLine, Class<?>> definitions =  new HashMap<IdentifierLine, Class<?>>();;
	private IdentifierLine idLine;
	
	@SuppressWarnings("rawtypes")
	public FlatFileReaderDefinition(Class bodyClass){
		this(Arrays.asList(bodyClass));
	}
	
	@SuppressWarnings({ "rawtypes"})
	public FlatFileReaderDefinition(Collection<Class> bodyClasses) {
		if (bodyClasses == null || bodyClasses.isEmpty()) {
			throw new IllegalArgumentException("Class<?> object is null");
		}
		createMapDefinitions(bodyClasses);
	}
	
	@SuppressWarnings("rawtypes")
	private void createMapDefinitions(Collection<Class> bodyClasses){
		for (Class<?> bodyClass : bodyClasses) {
			if (bodyClass.isAnnotationPresent(PositionalRecord.class)){
				final PositionalRecord pr =  bodyClass.getAnnotation(PositionalRecord.class);
				final PositionalRecordLineIdentifier[] identifiers = pr.lineIdentifiers();
				final IdentifierLine identifierLine = new IdentifierLine();
				for (int i = 0; i < identifiers.length; i++) {
					identifierLine.putId(identifiers[i].startPosition(), identifiers[i].textIdentifier());
				}
				definitions.put(identifierLine, bodyClass);
			}
		}
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
		final Set<IdentifierLine> identifierLines = this.definitions.keySet();
		final IdentifierLine messageId =  new IdentifierLine();
		for(IdentifierLine id : identifierLines){
			final Map<Integer, String> mapIds = id.getMapIds();
			final Set<Integer> keys = mapIds.keySet();
			for (Integer startPosition :  keys){
				final String textId = mapIds.get(startPosition);
				int sizeText =  textId.length();
				int finalPosition =  startPosition + sizeText;
				if (finalPosition > message.length()){
					break;
				}
				messageId.putId(startPosition, message.substring(startPosition, finalPosition));
			}
			if (id.equals(messageId)){
				this.body = this.definitions.get(messageId);
				break;
			}else{
				messageId.getMapIds().clear();
			}
		}
		if (this.body == null){
			throw new FFPojoException(String.format("No class matches with the line starting with:  %s ", getStartWithText(message)));
		}

		return this.body;
	}

	private String getStartWithText(String message) {
		String startWith = "";
		if (message.length() > 20) {
            startWith = message.substring(0, 20);
        }else{
            startWith = message;
        }
		return startWith;
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
