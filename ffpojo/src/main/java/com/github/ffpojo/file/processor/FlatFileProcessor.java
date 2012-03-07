package com.github.ffpojo.file.processor;

import com.github.ffpojo.file.processor.record.RecordProcessor;
import com.github.ffpojo.file.processor.record.handler.ErrorHandler;


public interface FlatFileProcessor {

	public void processFlatFile(RecordProcessor processor);
	
	public void setErrorHandler(ErrorHandler errorHandler);
	
}
