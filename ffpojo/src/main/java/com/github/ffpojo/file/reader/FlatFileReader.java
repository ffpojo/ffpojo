package com.github.ffpojo.file.reader;

import java.io.IOException;
import java.util.Iterator;



public interface FlatFileReader extends Iterator<Object>, Iterable<Object> {

	public void reset() throws IOException;
	public void close() throws IOException;
	public boolean isResetSupported();
	public boolean isClosed();

	public RecordType getRecordType();
	public String getRecordText();
	public long getRecordIndex();
	
	// Iterator
	public boolean hasNext();	
	public Object next();
	@Deprecated
	public void remove();
	
	// Iterable
	public Iterator<Object> iterator();
	
}
