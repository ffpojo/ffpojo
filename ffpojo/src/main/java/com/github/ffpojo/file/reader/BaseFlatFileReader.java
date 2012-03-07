package com.github.ffpojo.file.reader;

import java.util.Iterator;

import com.github.ffpojo.exception.RecordParserException;
import com.github.ffpojo.parser.RecordParser;

abstract class BaseFlatFileReader implements FlatFileReader {
	
	protected FlatFileReaderDefinition flatFileDefinition;

	protected RecordType recordType;
	protected String recordText;
	protected long recordIndex;
	protected boolean closed;
	
	protected Object parseRecordFromText(String text) throws RecordParserException {
		Class<?> recordClazz;
		RecordParser parser;
		if (recordIndex == 0 && flatFileDefinition.getHeader() != null) {
			this.recordType = RecordType.HEADER;
			recordClazz = flatFileDefinition.getHeader();
			parser = flatFileDefinition.getHeaderParser();
		} else if (!hasNext() && flatFileDefinition.getTrailer() != null) {
			this.recordType = RecordType.TRAILER;
			recordClazz = flatFileDefinition.getTrailer();
			parser = flatFileDefinition.getTrailerParser();
		} else {
			this.recordType = RecordType.BODY;
			recordClazz = flatFileDefinition.getBody();
			parser = flatFileDefinition.getBodyParser();
		}
		return parser.parseFromText(recordClazz, text);
	}
	
	@Deprecated
	public void remove() {
		throw new UnsupportedOperationException("Remove method not supported");
	}
	
	public Iterator<Object> iterator() {
		return this;
	}

	public RecordType getRecordType() {
		return recordType;
	}
	public String getRecordText() {
		return recordText;
	}
	public long getRecordIndex() {
		return recordIndex;
	}
	public boolean isClosed() {
		return closed;
	}
}
