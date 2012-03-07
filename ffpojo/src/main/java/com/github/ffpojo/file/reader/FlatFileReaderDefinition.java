package com.github.ffpojo.file.reader;

import com.github.ffpojo.FFPojoHelper;
import com.github.ffpojo.exception.FFPojoException;
import com.github.ffpojo.parser.RecordParser;

public class FlatFileReaderDefinition {
	
	private final FFPojoHelper ffpojoHelper = FFPojoHelper.getInstance();
	
	private Class<?> body;
	private Class<?> header;
	private Class<?> trailer;
	
	private RecordParser bodyParser;
	private RecordParser headerParser;
	private RecordParser trailerParser;
	
	public FlatFileReaderDefinition(Class<?> body) throws FFPojoException {
		if (body == null) {
			throw new IllegalArgumentException("Class<?> object is null");
		}
		this.body = body;
		this.bodyParser = ffpojoHelper.getRecordParser(body);
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
	
	public Class<?> getBody() {
		return body;
	}
	public Class<?> getHeader() {
		return header;
	}
	public Class<?> getTrailer() {
		return trailer;
	}
	public RecordParser getBodyParser() {
		return bodyParser;
	}
	public RecordParser getHeaderParser() {
		return headerParser;
	}
	public RecordParser getTrailerParser() {
		return trailerParser;
	}
}
