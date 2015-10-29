package com.github.ffpojo.file.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;

import com.github.ffpojo.exception.RecordParserException;

public class InputStreamFlatFileReader extends BaseFlatFileReader implements FlatFileReader {
	
	private static final boolean IS_RESET_SUPPORTED = false;
	
	private BufferedReader inputStreamReader;
	private String nextLine;
	
	public InputStreamFlatFileReader(InputStream inputStream, FlatFileReaderDefinition flatFile) throws IOException {
		if (inputStream == null) {
			throw new IllegalArgumentException("InputStream object is null");
		}
		
		this.inputStreamReader = new BufferedReader(new InputStreamReader(inputStream));
		this.flatFileDefinition = flatFile;		
		this.nextLine = inputStreamReader.readLine();
	}
		
	public boolean isResetSupported() {
		return IS_RESET_SUPPORTED;
	}
	
	@Deprecated
	public void reset() throws IOException {
		throw new UnsupportedOperationException("Reset method not supported by " + getClass());
	}
	
	public void close() throws IOException {
		if (inputStreamReader != null) {
			this.inputStreamReader.close();
		}
		this.closed = true;
		System.gc();
	}
	
	public boolean hasNext() {
		if (nextLine == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public Object next() {	
		if (!this.hasNext()) {
			throw new NoSuchElementException("There are no more records to read");
		}
		try {
            String currLine = nextLine;
            nextLine = inputStreamReader.readLine();
            Object record = parseRecordFromText(currLine);
            this.recordText = nextLine;
            recordIndex++;
            return record;
		} catch (IOException e) {
			throw new RuntimeException("Error while decoding the line number " + (recordIndex + 1), e);
		} catch (RecordParserException e) {
			throw new RuntimeException("Error while parsing from text the line number " + (recordIndex + 1), e);
		}
	}

}
