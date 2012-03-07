package com.github.ffpojo.file.processor.record;

import com.github.ffpojo.exception.RecordProcessorException;
import com.github.ffpojo.file.processor.record.event.RecordEvent;


public interface RecordProcessor {

	public void processHeader(RecordEvent event) throws RecordProcessorException;
	public void processBody(RecordEvent event) throws RecordProcessorException;
	public void processTrailer(RecordEvent event) throws RecordProcessorException;
	
}
