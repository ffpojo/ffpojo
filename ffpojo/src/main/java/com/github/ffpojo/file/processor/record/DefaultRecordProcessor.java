package com.github.ffpojo.file.processor.record;

import com.github.ffpojo.exception.RecordProcessorException;
import com.github.ffpojo.file.processor.record.event.RecordEvent;


public class DefaultRecordProcessor implements RecordProcessor {

	public void processBody(RecordEvent event) throws RecordProcessorException {
		// blank
	}

	public void processHeader(RecordEvent event) throws RecordProcessorException {
		// blank
	}

	public void processTrailer(RecordEvent event) throws RecordProcessorException {
		// blank
	}
	
}
