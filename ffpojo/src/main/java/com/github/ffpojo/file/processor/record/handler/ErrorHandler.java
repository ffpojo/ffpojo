package com.github.ffpojo.file.processor.record.handler;

import com.github.ffpojo.exception.RecordProcessorException;

public interface ErrorHandler {

	public void error(RecordProcessorException exception) throws RecordProcessorException;
	
}
