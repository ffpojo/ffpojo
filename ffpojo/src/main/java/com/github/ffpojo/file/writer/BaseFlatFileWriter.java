package com.github.ffpojo.file.writer;


abstract class BaseFlatFileWriter implements FlatFileWriter {

	protected long recordsWritten;
	protected boolean closed;

	public long getRecordsWritten() {
		return recordsWritten;
	}
	
	public boolean isClosed() {
		return closed;
	}
}
