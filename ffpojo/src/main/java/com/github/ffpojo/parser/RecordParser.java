package com.github.ffpojo.parser;

import com.github.ffpojo.exception.RecordParserException;

public interface RecordParser {

	public <T> T parseFromText(Class<T> recordClazz, String text) throws RecordParserException;
	
	public <T> String parseToText(T record) throws RecordParserException;

}
